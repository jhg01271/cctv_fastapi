<template>
    <!-- 콘텐츠 영역 -->
    <div class="contents df fdc">
        <!-- 검색 필드 -->
        <div class="control-field ui form mb2-2rem">
            <div class="row flex gutters1rem">
                <div class="grid12-2 es-grid12-12">
                    <input v-model="MgmtOfChangeStore.writeYear" v-input type="text" v-calendar="'yyyy'" year class="datepicker radius" @input="filterByValue" />
                </div>
                <div class="grid12-6 es-grid12-12">
                    <div class="df bcffffff">
                        <input v-input type="text" class="radius w100p search" placeholder="검색어를 입력하세요" v-model="filterValue" @keydown.enter="filterByValue" />
                        <button type="submit" class="shrink0" @click="filterByValue">
                            <img src="/assets/img/common/icon_search.svg" alt />
                        </button>
                    </div>
                </div>
                <div class="grid12-4 es-grid12-12">
                    <div class="toggle df gap1rem">
                        <button type="button" @click="filterBtn('reqOrgnNm')" :class="{ active: filterType === 'reqOrgnNm' }">
                            <span>요청조직</span>
                        </button>
                        <button type="button" @click="filterBtn('changeOrgnNm')" :class="{ active: filterType === 'changeOrgnNm' }">
                            <span>수행조직</span>
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
            <div class="accordion df fdc rg8px" v-if="segments && segments.length > 0">
                <!-- 1 -->
                <div v-for="(segment, index) in segments" :key="index" class="list">
                    <button type="button" class="df jcsb aic" @click="toggleAccordion(index)" :class="{ active: activeSegments[index] }">
                        <em>{{ segment ?? '미등록' }}</em>
                        <svg xmlns="http://www.w3.org/2000/svg" width="10" height="6" viewBox="0 0 10 6" fill="none">
                            <path d="M9 1L5.4714 4.79026C5.21106 5.06991 4.78894 5.06991 4.5286 4.79026L1 1" stroke="black" stroke-linecap="round" />
                        </svg>
                    </button>
                    <div ref="accordionRefs" class="segment oh">
                        <!-- 아코디언 래핑 요소 -->
                        <div class="pa2-2rem">
                            <div class="row flex gutters2rem">
                                <!--  -->
                                <template v-for="(data, i) in filteredList(segment)" :key="i">
                                    <i-card :v-model="data.checked" :data="data" :disabled="data.useYn == 'N'" :title="formatDate(data.reqDt)" :log-img="data.logoId ? data.logoId : ''" @editor="btnDetail" />
                                </template>

                                <!-- 추가 -->
                                <div class="grid12-4 ul-grid12-6 lg-grid12-12">
                                    <div class="card h100p df aic jcc cp" @click="addAction">
                                        <div class="tac">
                                            <svg width="46" height="46" viewBox="0 0 46 46" fill="none" xmlns="http://www.w3.org/2000/svg">
                                                <path d="M31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417M26.0875 9.11005L18.5802 16.6173C15.3165 19.881 13.0012 23.9703 11.8817 28.4481L11.519 29.8991C11.406 30.3512 11.8155 30.7607 12.2676 30.6477L13.7186 30.2849C18.1963 29.1655 22.2857 26.8502 25.5494 23.5865L33.0566 16.0792C33.9808 15.155 34.5 13.9016 34.5 12.5946C34.5 9.873 32.2937 7.66669 29.5721 7.66669C28.2651 7.66669 27.0116 8.18588 26.0875 9.11005Z" stroke="black" stroke-width="1.5" />
                                                <path d="M26.0874 9.11005C27.0116 8.18588 28.265 7.66669 29.572 7.66669C32.2936 7.66669 34.4999 9.873 34.4999 12.5946C34.4999 13.9016 33.9808 15.155 33.0566 16.0792L31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417L26.0874 9.11005Z" stroke="#3248F6" stroke-width="1.5" />
                                                <path d="M36.4167 38.3333H9.58337" stroke="#3248F6" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
                                            </svg>

                                            <span class="db mt1rem fs2rem c999999">{{ t('card_addMgmtOfChange') }}</span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <!-- 추가 -->
            <div class="grid12-4 ul-grid12-6 lg-grid12-12" v-else>
                <div class="card h100p df aic jcc cp" @click="addAction">
                    <div class="tac">
                        <svg width="46" height="46" viewBox="0 0 46 46" fill="none" xmlns="http://www.w3.org/2000/svg">
                            <path d="M31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417M26.0875 9.11005L18.5802 16.6173C15.3165 19.881 13.0012 23.9703 11.8817 28.4481L11.519 29.8991C11.406 30.3512 11.8155 30.7607 12.2676 30.6477L13.7186 30.2849C18.1963 29.1655 22.2857 26.8502 25.5494 23.5865L33.0566 16.0792C33.9808 15.155 34.5 13.9016 34.5 12.5946C34.5 9.873 32.2937 7.66669 29.5721 7.66669C28.2651 7.66669 27.0116 8.18588 26.0875 9.11005Z" stroke="black" stroke-width="1.5" />
                            <path d="M26.0874 9.11005C27.0116 8.18588 28.265 7.66669 29.572 7.66669C32.2936 7.66669 34.4999 9.873 34.4999 12.5946C34.4999 13.9016 33.9808 15.155 33.0566 16.0792L31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417L26.0874 9.11005Z" stroke="#3248F6" stroke-width="1.5" />
                            <path d="M36.4167 38.3333H9.58337" stroke="#3248F6" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
                        </svg>

                        <span class="db mt1rem fs2rem c999999">{{ t('card_addMgmtOfChange') }}</span>
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

const { goRouter, ref, onMounted, btnBack, btnAdd, btnDelete, alertMsg, confirmMsg, btnSearch, btnDownload, t, formatDate } = BaseView();

const list = ref([]);
import { useMgmtOfChangeStore } from '@/stores/safewiz/impl/mgmtOfChange';
const MgmtOfChangeStore = useMgmtOfChangeStore();
import { usePlanningCtrlStore } from '@/stores/safewiz/impl/planningAndControl.js';
const planningCtrlStore = usePlanningCtrlStore();
onMounted(() => {
    MgmtOfChangeStore.writeYear = planningCtrlStore.searchParam.searchText
    init();
});
const filterValue = ref('');
const filterByValue = () => {
    if (filterValue.value != '') {
        init(true);
    } else {
        init();
    }
};
const filterType = ref('reqOrgnNm');
const filterBtn = type => {
    filterType.value = type;
    filterByValue();
    activeSegments.value = {};
};
btnDownload(() => {
    let param = list.value.filter(el => {
        return el.checked;
    });
    if (param.length <= 0) {
        alertMsg('warning', '선택한 항목이 없습니다.');
        return;
    }
    confirmMsg('info', '선택한 항목을 출력하시겠습니까?', null, { fun: btnDownloadAction, param: param });
});

const btnDownloadAction = param => {
    MgmtOfChangeStore.downloadReport(param);
}

const init = filter => {
    segments.value = [];
    getMgmtOfChange().then(res => {
        let data = res.list.map(el => {
            return {
                ...el,
                detail: {
                    요청조직: el.reqOrgnNm,
                    수행조직: el.changeOrgnNm,
                    '변경 검토자': el.changeHrNm,
                    '검토 결과 승인자': el.reviewHrNm
                },
                detailWithBtn: [
                    {
                        label: '검토 결과',
                        value: el.result,
                        button: [
                            {
                                value: '0001',
                                label: '승인',
                                class: 'complete'
                            },
                            {
                                value: '0002',
                                label: '보류',
                                class: 'ready'
                            },
                            {
                                value: '0003',
                                label: '거절',
                                class: 'progress'
                            }
                        ]
                    }
                ]
            };
        });
        if (filter) {
            list.value = data.filter(el => {
                const matchKeyword = el.reqOrgnNm?.includes(filterValue.value) || el.changeOrgnNm?.includes(filterValue.value) || el.reviewHrNm?.includes(filterValue.value) || el.resultNm?.includes(filterValue.value);
                const matchYear = el.writeYear == MgmtOfChangeStore.writeYear;
                return matchKeyword && matchYear;
            });
        } else {
            list.value = data.filter(el => el.writeYear == MgmtOfChangeStore.writeYear);
        }

        list.value.forEach(item => {
            if (!segments.value.includes(item[filterType.value])) {
                segments.value.push(item[filterType.value]);
            }
        });
        const index = segments.value.indexOf(null);
        if (index !== -1) {
            segments.value.splice(index, 1);
            segments.value.push(null);
        }
    });
};

const filteredList = param => {
    return list.value.filter(el => {
        return el[filterType.value] == param && el.writeYear == MgmtOfChangeStore.writeYear;
    });
};

import { useButtonListStore } from '@/stores/buttonList';
import { deleteMgmtOfChange, getMgmtOfChange } from '@/stores/safewiz/impl/api/mgmtOfChangeApi';
const layoutStore = useButtonListStore();
layoutStore.useBtnList = ['btnSearch', 'btnAdd', 'btnDelete', 'btnBack', 'btnDownload'];
const btnDetail = async value => {
    const param = {
        writeYear: value.writeYear,
        docType: value.docType,
        docNo: value.docNo
    };
    goRouter('MgmtOfChangeDetail', param);
};
btnAdd(() => {
    addAction();
});

const addAction = () => {
    MgmtOfChangeStore.init();
    MgmtOfChangeStore.cmd = 'I';
    MgmtOfChangeStore.reqOrgn = [];
    MgmtOfChangeStore.changeOrgn = [];
    router.push('/MgmtOfChangeDetail');
};
btnDelete(() => {
    let param = list.value.filter(el => {
        return el.checked;
    });
    if (param.length <= 0) {
        alertMsg('warning', '선택한 항목이 없습니다.');
        return;
    }

    confirmMsg('info', '삭제 하시겠습니까?', null, { fun: deleteAction, param: param });
});
const deleteAction = param => {
    deleteMgmtOfChange(param, true).then(res => {
        if (res.success) init();
    });
};
btnBack(() => {
    planningCtrlStore.searchParam.searchText = MgmtOfChangeStore.writeYear
    router.push('/PlanningAndControl');
});
btnSearch(() => {
    init();
});
// 아코디언 토글
gsap.registerPlugin(CustomEase);
CustomEase.create('customEase', 'M0,0 C0.9,0 0.2,1 1,1');

const segments = ref([]);

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
