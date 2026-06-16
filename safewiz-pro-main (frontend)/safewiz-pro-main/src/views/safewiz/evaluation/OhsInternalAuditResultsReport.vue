<template>
    <!-- 콘텐츠 영역 -->
    <div class="contents df fdc">
        <!-- 검색 필드 -->
        <div class="control-field ui form mb2-2rem">
            <div class="row flex gutters1rem">
                <div class="grid12-3 es-grid12-12">
                    <input type="text" v-calendar="'yyyy'" v-model="auditStore.writeYear" class="datepicker w100p radius" year @input="init" />
                </div>
                <div class="grid12-9 es-grid12-12">
                    <div class="df bcffffff">
                        <input v-input type="text" class="radius w100p search" placeholder="검색어를 입력하세요" v-model="filterValue" @keydown.enter="filterByValue" />
                        <button type="submit" class="shrink0" @click="filterByValue">
                            <img src="/assets/img/common/icon_search.svg" alt />
                        </button>
                    </div>
                </div>
            </div>
        </div>
        <OverlayScrollbarsComponent
            :options="{
                scrollbars: {
                    autoHide: 'hover',
                    x: 'hidden',
                    y: 'visible'
                }
            }"
        >
            <!-- 아코디언 영역 -->
            <div class="accordion df fdc rg8px">
                <!-- 1 -->
                <div v-for="(item, index) in filteredData" :key="index" class="list">
                    <button type="button" class="df jcsb aic" @click="toggleAccordion(index)" :class="{ active: activeSegments[index] }">
                        <em>{{ item.auditNm }}</em>
                        <svg xmlns="http://www.w3.org/2000/svg" width="10" height="6" viewBox="0 0 10 6" fill="none">
                            <path d="M9 1L5.4714 4.79026C5.21106 5.06991 4.78894 5.06991 4.5286 4.79026L1 1" stroke="black" stroke-linecap="round" />
                        </svg>
                    </button>
                    <div ref="accordionRefs" class="segment oh">
                        <!-- 아코디언 래핑 요소 -->
                        <div class="pa2-2rem">
                            <div class="row flex gutters2rem">
                                <!--  -->
                                <template v-for="(data, i) in item.auditOrgnList" :key="i">
                                    <i-card :v-model="data.checked" :data="data" :unregistered="data.submitYn == 'N'" :title="data.orgnNm" :log-img="data.logoId ? data.logoId : ''" :useApprovalStatus="true" :useCheck="data.submitYn == 'Y'" :approvalStatus="data.approvalStatus" @editor="btnDetail" />
                                </template>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </OverlayScrollbarsComponent>
    </div>
</template>

<script setup>
import { nextTick } from 'vue';

import { gsap } from 'gsap';
import CustomEase from 'gsap/CustomEase';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import router from '@/router';
import BaseView from '@/components/base/BaseView';

const { t, openLoading, endLoading, ref, onMounted, btnBack, alertMsg, confirmMsg, btnSearch, btnDownload, formatDate, goRouter } = BaseView();

const dataList = ref([]);
onMounted(() => {
    init();
});
btnDownload(() => {
    let param = [];
    filteredData.value.forEach(item => {
        let tmp = item.auditOrgnList.filter(el => {
            return el.checked;
        });
        param.push(...tmp);
    });
    if (param.length <= 0) {
        alertMsg('warning', '선택한 항목이 없습니다.');
        return;
    }
    confirmMsg('info', t('msgCheckedPrint'), null, { fun: downloadAction, param: param });
});

const downloadAction = param => {
    auditResultReportStore.btnDownload(param);
};

import { useAuditStore } from '@/stores/safewiz/evaluation/internalAudit.js';
const auditStore = useAuditStore();
import _ from 'lodash';

const init = () => {
    openLoading();
    getAuditResult({ writeYear: auditStore.writeYear }, true)
        .then(res => {
            res.list.forEach(orgn => {
                orgn.auditOrgnList.forEach(el => {
                    el.detail = {
                        '심사 번호': el.writeYear + el.docNo,
                        '심사 일시': formatDate(el.auditDt) + ' ' + el.auditTime,
                        '심사 대상 수': el.auditDetailCount
                    };
                    console.log(el);
                });
            });
            dataList.value = res.list;
            filteredData.value = _.cloneDeep(dataList.value);
        })
        .finally(() => {
            endLoading();
        });
};
const filterValue = ref('');
const filteredData = ref([]);
const filterByValue = () => {
    filteredData.value = _.cloneDeep(dataList.value);
    if (filterValue.value == '') {
        return;
    }
    filteredData.value.forEach(item => {
        item.auditOrgnList = item.auditOrgnList.filter(data => {
            console.log(data, filterValue.value);
            return data.orgnNm.includes(filterValue.value) || (data.writeYear + data.docNo).includes(filterValue.value) || (formatDate(data.auditDt) + ' ' + data.auditTime).includes(filterValue.value);
        });
    });
    filteredData.value = filteredData.value.filter(el => {
        return el.auditOrgnList.length;
    });
};
import { useAuditResultReportStore } from '@/stores/safewiz/evaluation/auditResultReport';
const auditResultReportStore = useAuditResultReportStore();
import { useButtonListStore } from '@/stores/buttonList';
import { deleteAuditResult, getAuditResult } from '@/stores/safewiz/evaluation/api/auditResultReportApi';
const layoutStore = useButtonListStore();
layoutStore.useBtnList = ['btnSearch', 'btnBack', 'btnDownload'];
const btnDetail = async e => {
    const param = {
        writeYear: e.writeYear,
        docType: e.docType,
        docNo: e.docNo,
        docSeq: e.docSeq
    }
    goRouter('OhsInternalAuditResultsReportDetail', param);
};

btnBack(() => {
    router.push('/InternalAudit');
});
btnSearch(() => {
    init();
});

// btnDelete(()=>{
//     let param = [];
//     filteredData.value.forEach(item => {
//         let tmp = item.auditOrgnList.filter(el => {
//             return el.checked;
//         });
//         param.push(_.cloneDeep(...tmp.map((el)=>{
//             el.detail=[]
//             return el
//         })));
//     });
//     if (param.length <= 0) {
//         alertMsg('warning', '선택한 항목이 없습니다.');
//         return;
//     }
//     confirmMsg('warning', t('msgDelete'), '', { fun: deleteApi, param: param });
// })

//   const deleteApi = async (param) => {
//     deleteAuditResult(param).then(res => {
//       init()
//     })
//   }
// 아코디언 토글
gsap.registerPlugin(CustomEase);
CustomEase.create('customEase', 'M0,0 C0.9,0 0.2,1 1,1');

const activeSegments = ref({});
const accordionRefs = ref([]);

const toggleAccordion = async index => {
    activeSegments.value[index] = !activeSegments.value[index];

    await nextTick(); // DOM 업데이트 후 실행

    const segment = accordionRefs.value[index];

    if (segment) {
        gsap.to(segment, {
            height: activeSegments.value[index] ? 'auto' : 0,
            duration: 0.5,
            ease: 'customEase'
        });
    } else {
        console.warn(`GSAP target for index ${index} not found`);
    }
};
</script>
