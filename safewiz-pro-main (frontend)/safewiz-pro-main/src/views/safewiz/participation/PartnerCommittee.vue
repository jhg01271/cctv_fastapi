<template>
    <!-- 콘텐츠 영역 -->
    <div class="contents df fdc">
        <!-- 검색 필드 -->
        <div class="control-field ui form mb2-2rem">
            <div class="df bcffffff">
                <input v-input type="text" class="radius w100p search" :placeholder="t('placeHolderSearch')" v-model="filterValue" @keydown.enter="filterByValue" />
                <button type="submit" class="shrink0" @click="filterByValue">
                    <img src="/assets/img/common/icon_search.svg" alt />
                </button>
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
            <div class="accordion">
                <!-- 1 -->
                <div v-for="(segment, index) in segments" :key="index" class="list">
                    <button type="button" class="df jcsb aic" @click="toggleAccordion(index)" :class="{ active: activeSegments[index] }">
                        <em>{{ segment }}년도</em>
                        <svg xmlns="http://www.w3.org/2000/svg" width="10" height="6" viewBox="0 0 10 6" fill="none">
                            <path d="M9 1L5.4714 4.79026C5.21106 5.06991 4.78894 5.06991 4.5286 4.79026L1 1" stroke="black" stroke-linecap="round" />
                        </svg>
                    </button>
                    <div ref="accordionRefs" class="segment oh bcFFFFFF">
                        <!-- 아코디언 래핑 요소 -->
                        <div class="pa2-2rem br4px">
                            <div class="row flex gutters2rem">
                                <!--  -->
                                <template v-for="(item, i) in filteredList(segment)" :key="i">
                                    <i-card :v-model="item.checked" :title="formatDate(item.commDt)" :data="item" :type="'basic'" :disabled="item.useYn === 'N'" @editor="btnDetail(item)" useApprovalStatus :approvalStatus="item.approvalStatus" />
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

                                            <span class="db mt1rem fs2rem c999999">신규 위원회 추가</span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div v-if="segments.length === 0" class="grid12-4 ul-grid12-6 lg-grid12-12">
                    <div class="card h100p df aic jcc cp" @click="addAction">
                        <div class="tac">
                            <svg width="46" height="46" viewBox="0 0 46 46" fill="none" xmlns="http://www.w3.org/2000/svg">
                                <path d="M31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417M26.0875 9.11005L18.5802 16.6173C15.3165 19.881 13.0012 23.9703 11.8817 28.4481L11.519 29.8991C11.406 30.3512 11.8155 30.7607 12.2676 30.6477L13.7186 30.2849C18.1963 29.1655 22.2857 26.8502 25.5494 23.5865L33.0566 16.0792C33.9808 15.155 34.5 13.9016 34.5 12.5946C34.5 9.873 32.2937 7.66669 29.5721 7.66669C28.2651 7.66669 27.0116 8.18588 26.0875 9.11005Z" stroke="black" stroke-width="1.5" />
                                <path d="M26.0874 9.11005C27.0116 8.18588 28.265 7.66669 29.572 7.66669C32.2936 7.66669 34.4999 9.873 34.4999 12.5946C34.4999 13.9016 33.9808 15.155 33.0566 16.0792L31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417L26.0874 9.11005Z" stroke="#3248F6" stroke-width="1.5" />
                                <path d="M36.4167 38.3333H9.58337" stroke="#3248F6" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
                            </svg>

                            <span class="db mt1rem fs2rem c999999">신규 위원회 추가</span>
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

const { goRouter, ref, t, onMounted, btnBack, btnSearch, btnAdd, btnDelete, btnDownload, alertMsg, confirmMsg, formatDate, openLoading, endLoading } = BaseView();

const list = ref([]);

import { useCommitteeStore } from '@/stores/safewiz/participation/committee';
const partnerCommitteeStore = useCommitteeStore();

onMounted(() => {
    init();
});
const filterValue = ref('');
const filterByValue = () => {
    const search = filterValue.value.trim().toLowerCase();
    
    list.value = originalList.value.filter(item => {
        return (
            item.commNm?.toLowerCase().includes(search) ||
            formatDate(item.commDt)?.toLowerCase().includes(search) ||
            (item.partnerCount?.toString().includes(search)) ||
            (item.count?.toString().includes(search)) ||
            (item.percent?.toString().includes(search))
        );
    });
    
    // segments 업데이트
    segments.value = [];
    list.value.forEach(item => {
        if (!segments.value.includes(item.writeYear)) {
            segments.value.push(item.writeYear);
        }
    });
};

const btnDetail = item => {
    partnerCommitteeStore.cmd = 'U';
    partnerCommitteeStore.inputForm = item;
    const param = {
        writeYear: item.writeYear,
        docType: item.docType,
        docNo: item.docNo
    };
    goRouter('PartnerCommitteeDetail', param);
};

const originalList = ref([]);
const init = filter => {
    openLoading();
    getParticipation('PAC', true)
        .then(res => {
            segments.value = [partnerCommitteeStore.writeYear];
            if (filter) {
                res.list = res.list.filter(el => {
                    return formatDate(el.commDt).includes(filterValue.value) || el.commNm?.includes(filterValue.value) || `${el.partnerCount}/${el.companyCount} 명`.includes(filterValue.value) || `${el.count}건`.includes(filterValue.value) || `${el.percent}%`.includes(filterValue.value);
                });
            } else {
                list.value = res.list;
            }
            list.value = res.list.map(el => {
                return {
                    ...el,
                    detail: {
                        회의명: el.commNm,
                        '참석(업체/당사)': `${el.partnerCount}/${el.companyCount}`,
                        '안건(협의율)': `${el.count}건 (${el.percent ? el.percent : 0}%)`
                    }
                };
            });

            originalList.value = list.value;
            segments.value = [];
            list.value.forEach(item => {
                if (!segments.value.includes(item.writeYear)) {
                    segments.value.push(item.writeYear);
                }
            });
            segments.value.forEach((item, index) => {
                if (item == partnerCommitteeStore.writeYear) {
                    if (!activeSegments.value[index]) {
                        toggleAccordion(index, true);
                    }
                }
            });
        })
        .finally(() => {
            endLoading();
        });
};

const filteredList = param => {
    let data = list.value.filter(el => {
        return el.writeYear == param;
    });
    return data;
};

// 버튼 세팅
import { useButtonListStore } from '@/stores/buttonList';
import { getParticipation, deleteParticipation } from '@/stores/safewiz/participation/api/participation';
const layoutStore = useButtonListStore();
layoutStore.useBtnList = ['btnSearch', 'btnAdd', 'btnDelete', 'btnBack', 'btnDownload'];

btnSearch(() => {
    init();
});

const addAction = () => {
    partnerCommitteeStore.init('PAC');
    router.push('/PartnerCommitteeDetail');
};

btnAdd(() => {
    addAction();
});
btnDelete(() => {
    let param = list.value.filter(el => {
        return el.checked;
    });
    if (param.length <= 0) {
        alertMsg('warning', t('msgNoItem'));
        return;
    }

    confirmMsg('info', t('msgDelete'), null, { fun: deleteAction });
});

const deleteAction = () => {
    let param = list.value.filter(el => {
        return el.checked;
    });
    deleteParticipation(param, true).then(() => {
        init();
    });
};

btnDownload(() => {
    let param = list.value.filter(el => {
        return el.checked;
    });
    if (param.length <= 0) {
        alertMsg('warning', t('msgNoItem'));
        return;
    }
    confirmMsg('info', t('msgCheckedPrint'), null, { fun: downloadAction, param: param });
});
const downloadAction = list => {
    partnerCommitteeStore.downloadPartReport(list);
};
btnBack(() => {
    router.push('/ConsultationAndParticipation');
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
