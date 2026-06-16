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
            <div class="accordion df fdc rg8px" v-if="segments && segments.length > 0">
                <!-- 1 -->
                <div v-for="(item, index) in segments" :key="index" class="list">
                    <button type="button" class="df jcsb aic tal lh1-5" @click="toggleAccordion(index)" :class="{ active: activeSegments[index] }">
                        <em>{{ item }}</em>
                        <svg xmlns="http://www.w3.org/2000/svg" width="10" height="6" viewBox="0 0 10 6" fill="none">
                            <path d="M9 1L5.4714 4.79026C5.21106 5.06991 4.78894 5.06991 4.5286 4.79026L1 1" stroke="black" stroke-linecap="round" />
                        </svg>
                    </button>
                    <div ref="accordionRefs" class="segment oh">
                        <!-- 아코디언 래핑 요소 -->
                        <div class="pa2-2rem br4px">
                            <div class="row flex gutters2rem">
                                <!--  -->
                                <template v-for="(data, i) in filteredList(item)" :key="i">
                                    <i-card :v-model="data.checked" :data="data" :disabled="data.useYn == 'N'" :title="data.orgnNm" :log-img="data.logoId ? data.logoId : ''" @editor="btnDetail" />
                                </template>
                                <!-- 추가 -->
                                <div class="grid12-4 ul-grid12-6 lg-grid12-12">
                                    <div class="card h100p df aic jcc cp" @click="btnNew()">
                                        <div class="tac">
                                            <svg width="46" height="46" viewBox="0 0 46 46" fill="none" xmlns="http://www.w3.org/2000/svg">
                                                <path d="M31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417M26.0875 9.11005L18.5802 16.6173C15.3165 19.881 13.0012 23.9703 11.8817 28.4481L11.519 29.8991C11.406 30.3512 11.8155 30.7607 12.2676 30.6477L13.7186 30.2849C18.1963 29.1655 22.2857 26.8502 25.5494 23.5865L33.0566 16.0792C33.9808 15.155 34.5 13.9016 34.5 12.5946C34.5 9.873 32.2937 7.66669 29.5721 7.66669C28.2651 7.66669 27.0116 8.18588 26.0875 9.11005Z" stroke="black" stroke-width="1.5" />
                                                <path d="M26.0874 9.11005C27.0116 8.18588 28.265 7.66669 29.572 7.66669C32.2936 7.66669 34.4999 9.873 34.4999 12.5946C34.4999 13.9016 33.9808 15.155 33.0566 16.0792L31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417L26.0874 9.11005Z" stroke="#3248F6" stroke-width="1.5" />
                                                <path d="M36.4167 38.3333H9.58337" stroke="#3248F6" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
                                            </svg>

                                            <span class="db mt1rem fs2rem c999999">{{ t('card_addOhsCorrectiveActionRequest') }}</span>
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
                <div class="card h100p df aic jcc cp" @click="btnNew()">
                    <div class="tac">
                        <svg width="46" height="46" viewBox="0 0 46 46" fill="none" xmlns="http://www.w3.org/2000/svg">
                            <path d="M31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417M26.0875 9.11005L18.5802 16.6173C15.3165 19.881 13.0012 23.9703 11.8817 28.4481L11.519 29.8991C11.406 30.3512 11.8155 30.7607 12.2676 30.6477L13.7186 30.2849C18.1963 29.1655 22.2857 26.8502 25.5494 23.5865L33.0566 16.0792C33.9808 15.155 34.5 13.9016 34.5 12.5946C34.5 9.873 32.2937 7.66669 29.5721 7.66669C28.2651 7.66669 27.0116 8.18588 26.0875 9.11005Z" stroke="black" stroke-width="1.5" />
                            <path d="M26.0874 9.11005C27.0116 8.18588 28.265 7.66669 29.572 7.66669C32.2936 7.66669 34.4999 9.873 34.4999 12.5946C34.4999 13.9016 33.9808 15.155 33.0566 16.0792L31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417L26.0874 9.11005Z" stroke="#3248F6" stroke-width="1.5" />
                            <path d="M36.4167 38.3333H9.58337" stroke="#3248F6" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
                        </svg>

                        <span class="db mt1rem fs2rem c999999">{{ t('card_addOhsCorrectiveActionRequest') }}</span>
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
import { useCorrectiveRequestStore } from '@/stores/safewiz/evaluation/OhsCorrectiveActionRequest';
const correctiveRequestStore = useCorrectiveRequestStore();
import { useAuditStore } from '@/stores/safewiz/evaluation/internalAudit.js';
const auditStore = useAuditStore();
const { openLoading, endLoading, ref, onMounted, btnBack, alertMsg, confirmMsg, btnSearch, btnDownload, btnAdd, btnDelete, t, formatDate, goRouter} = BaseView();

const dataList = ref([]);

btnAdd(() => {
    correctiveRequestStore.addDetail(auditStore.writeYear);
    router.push('/OhsCorrectiveActionRequestDetail');
});

const btnNew = () => {
    correctiveRequestStore.addDetail(auditStore.writeYear);
    router.push('/OhsCorrectiveActionRequestDetail');
};

onMounted(() => {
    init();
});
btnDownload(() => {
    let param = dataList.value.filter(el => {
        return el.checked;
    });
    if (param.length <= 0) {
        alertMsg('warning', '선택한 항목이 없습니다.');
        return;
    }
    correctiveRequestStore.btnDownload(param);
});
const segments = ref([]);
const init = () => {
    openLoading();
    getCorrectiveRequest({ writeYear: auditStore.writeYear }, true)
        .then(res => {
            res.list = res.list.filter(el => {
                return el.auditNm.includes(filterValue.value) || el.orgnNm.includes(filterValue.value);
            });
            dataList.value = res.list.map(el => {
                return {
                    ...el,
                    detail: {
                        심사번호: el.writeYear + el.docNo + el.docSeq,
                        문서발행번호: el.writeYear + el.docNo + el.docSeq + el.docDetailSeq,
                        회신요구일: formatDate(el.reqDt)
                    },
                    detailWithBtn: [
                        {
                            // TODO 2023.03.12 BHJ C로만 표시됨. 로직 확인 후 수정 필요
                            label: '조직장 확인',
                            value: el.approvalStatus,
                            button: [
                                {
                                    value: 'Y', // 조직장 서명이 있는 경우
                                    label: '확인',
                                    class: 'complete'
                                },
                                {
                                    value: 'N', // 조직장 서명이 없는 경우
                                    label: '미확인',
                                    class: 'progress'
                                },
                                {
                                    value: 'C', // 조직장 지정이 없는 경우
                                    label: '대기',
                                    class: 'ready'
                                }
                            ]
                        },
                        {
                            // TODO HBJ 카드 정보 중 결재상태와 같은 형태로 표시하고 싶을 때 아래 소스 참고할 것
                            label: '유효성 확인',
                            value: el.validate ?? 'D',
                            button: [
                                {
                                    value: 'Y', // 유효성 확인 결과에 적합이 선택된 경우
                                    label: '적합',
                                    class: 'complete'
                                },
                                {
                                    value: 'N', // 유효성 확인 결과에 부적합이 선택된 경우
                                    label: '부적합',
                                    class: 'progress'
                                },
                                {
                                    value: 'D', // 유효성 확인  결과에 선택값이 없는 경우
                                    label: '대기',
                                    class: 'ready'
                                }
                            ]
                        }
                    ]
                };
            });
            segments.value = [];
            dataList.value.forEach(item => {
                if (!segments.value.includes(item.auditNm)) {
                    segments.value.push(item.auditNm);
                }
            });
        })
        .finally(() => {
            endLoading();
        });
};
const filteredList = param => {
    let data = dataList.value.filter(el => {
        return el.auditNm == param;
    });
    return data;
};

const filterValue = ref('');
const filterByValue = () => {
    init();
};

import { useButtonListStore } from '@/stores/buttonList';
import { getCorrectiveRequest, deleteCorrectiveRequest } from '@/stores/safewiz/evaluation/api/OhsCorrectiveActionRequestApi';
const layoutStore = useButtonListStore();
layoutStore.useBtnList = ['btnSearch', 'btnDelete', 'btnBack', 'btnDownload', 'btnAdd'];
const btnDetail = async e => {
    const param = {
        writeYear: e.writeYear,
        docType: e.docType,
        docNo: e.docNo,
        docSeq: e.docSeq,
        docDetailSeq: e.docDetailSeq
    }
    goRouter('OhsCorrectiveActionRequestDetail', param);
};

btnBack(() => {
    router.push('/InternalAudit');
});
btnDelete(() => {
    let param = dataList.value.filter(el => {
        return el.checked;
    });
    if (param.length <= 0) {
        alertMsg('warning', '선택한 항목이 없습니다.');
        return;
    }

    confirmMsg('info', '삭제 하시겠습니까?', null, { fun: deleteAction, param: param });
});
const deleteAction = param => {
    deleteCorrectiveRequest(param, true).then(res => {
        if (res.success) init();
    });
};
btnSearch(() => {
    init();
});
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
