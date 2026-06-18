<template>
    <!-- 콘텐츠 영역 -->
    <div class="contents df fdc">
        <!-- 검색 필드 -->
        <div class="control-field ui form mb2-2rem">
            <div class="df bcffffff">
                <input v-model="searchTerm" v-input type="text" class="radius w100p search" placeholder="검색어를 입력하세요" @keyup.enter="applyFilter" />
                <button type="submit" class="shrink0" @click.stop="applyFilter">
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
            <div class="accordion df fdc rg8px">
                <!-- 1 -->
                <div v-for="(segment, index) in filteredSegments" :key="index" class="list">
                    <button type="button" class="df jcsb aic" @click="toggleAccordion(index)" :class="{ active: taskAsgmtStore.activeSegments[index] }">
                        <em>{{ segment.year }}</em>
                        <svg xmlns="http://www.w3.org/2000/svg" width="10" height="6" viewBox="0 0 10 6" fill="none">
                            <path d="M9 1L5.4714 4.79026C5.21106 5.06991 4.78894 5.06991 4.5286 4.79026L1 1" stroke="black" stroke-linecap="round" />
                        </svg>
                    </button>
                    <div ref="accordionRefs" class="segment oh">
                        <!-- 아코디언 래핑 요소 -->
                        <div class="pa2-2rem">
                            <div class="row flex gutters2rem">
                                <!--  -->
                                <template v-for="(data, idx) in segment.dataList" :key="`card_${idx}`">
                                    <i-card :v-model="data.checked" :data="data" :type="'basic'" :title="data.title" @editor="btnDetail(data)" :disabled="data.useYn == 'N'"></i-card>
                                </template>

                                <!-- 추가 -->
                                <div class="grid12-4 ul-grid12-6 lg-grid12-12">
                                    <div class="card h100p df aic jcc cp" @click="btnNew(segment.year)">
                                        <div class="tac">
                                            <svg width="46" height="46" viewBox="0 0 46 46" fill="none" xmlns="http://www.w3.org/2000/svg">
                                                <path d="M31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417M26.0875 9.11005L18.5802 16.6173C15.3165 19.881 13.0012 23.9703 11.8817 28.4481L11.519 29.8991C11.406 30.3512 11.8155 30.7607 12.2676 30.6477L13.7186 30.2849C18.1963 29.1655 22.2857 26.8502 25.5494 23.5865L33.0566 16.0792C33.9808 15.155 34.5 13.9016 34.5 12.5946C34.5 9.873 32.2937 7.66669 29.5721 7.66669C28.2651 7.66669 27.0116 8.18588 26.0875 9.11005Z" stroke="black" stroke-width="1.5" />
                                                <path d="M26.0874 9.11005C27.0116 8.18588 28.265 7.66669 29.572 7.66669C32.2936 7.66669 34.4999 9.873 34.4999 12.5946C34.4999 13.9016 33.9808 15.155 33.0566 16.0792L31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417L26.0874 9.11005Z" stroke="#3248F6" stroke-width="1.5" />
                                                <path d="M36.4167 38.3333H9.58337" stroke="#3248F6" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
                                            </svg>

                                            <span class="db mt1rem fs2rem c999999">{{ t('card_addEmergencyTask') }}</span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <!-- 추가 -->
                <div v-if="filteredSegments.length == 0" class="grid12-4 ul-grid12-6 lg-grid12-12">
                    <div class="card h100p df aic jcc cp" @click="btnNew()">
                        <div class="tac">
                            <svg width="46" height="46" viewBox="0 0 46 46" fill="none" xmlns="http://www.w3.org/2000/svg">
                                <path d="M31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417M26.0875 9.11005L18.5802 16.6173C15.3165 19.881 13.0012 23.9703 11.8817 28.4481L11.519 29.8991C11.406 30.3512 11.8155 30.7607 12.2676 30.6477L13.7186 30.2849C18.1963 29.1655 22.2857 26.8502 25.5494 23.5865L33.0566 16.0792C33.9808 15.155 34.5 13.9016 34.5 12.5946C34.5 9.873 32.2937 7.66669 29.5721 7.66669C28.2651 7.66669 27.0116 8.18588 26.0875 9.11005Z" stroke="black" stroke-width="1.5" />
                                <path d="M26.0874 9.11005C27.0116 8.18588 28.265 7.66669 29.572 7.66669C32.2936 7.66669 34.4999 9.873 34.4999 12.5946C34.4999 13.9016 33.9808 15.155 33.0566 16.0792L31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417L26.0874 9.11005Z" stroke="#3248F6" stroke-width="1.5" />
                                <path d="M36.4167 38.3333H9.58337" stroke="#3248F6" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
                            </svg>

                            <span class="db mt1rem fs2rem c999999">신규 조직별 업무분장 추가</span>
                        </div>
                    </div>
                </div>
            </div>
        </OverlayScrollbarsComponent>
    </div>
</template>

<script setup>
import { gsap } from 'gsap';
import CustomEase from 'gsap/CustomEase';
import { nextTick } from 'vue';
import BaseView from '@/components/base/BaseView';
const { openLoading, endLoading, ref, t, onMounted, getCompId, confirmMsg, alertMsg, formatDate, btnBack, btnSearch, btnAdd, btnDelete, btnDownload } = BaseView();
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import { useButtonListStore } from '@/stores/buttonList';
const layoutStore = useButtonListStore();
layoutStore.useBtnList = ['btnBack', 'btnSearch', 'btnAdd', 'btnDelete', 'btnDownload'];
import router from '@/router/index.js';

import { useEmerCtrlTaskAsgmt } from '@/stores/safewiz/impl/emerCtrlTaskAsgmt';
const taskAsgmtStore = useEmerCtrlTaskAsgmt();

import { getEmergencyControlTaskAsgmtList, deleteEmergencyControlTaskAsgmt } from '@/stores/safewiz/impl/api/emerCtrlTaskAsgmtApi.js';

import _ from 'lodash';
//-----------------------------------------------
//-----------------------------------------------
// [아코디언]
// 아코디언 토글
gsap.registerPlugin(CustomEase);
CustomEase.create('customEase', 'M0,0 C0.9,0 0.2,1 1,1');

const segments = ref([]);
const filteredSegments = ref([]);

const accordionRefs = ref([]);
const initAccordion = async (initIndex = null) => {
    if (initIndex === -1) {
        filteredSegments.value.push({
            year: emergencyResponseStore.searchParam.searchText + '년도',
            dataList: []
        });
        filteredSegments.value.sort((a, b) => parseInt(b.year.substring(0, 4)) - parseInt(a.year.substring(0, 4)));
        initIndex = await filteredSegments.value.findIndex(el => el.year.substring(0, 4) === emergencyResponseStore.searchParam.searchText);
    }

    filteredSegments.value.forEach((el, index) => {
        taskAsgmtStore.activeSegments[index] = false;
    });
    taskAsgmtStore.activeSegments.forEach((el, index) => {
        accordionSet(index);
    });
    taskAsgmtStore.activeSegments[initIndex] = !taskAsgmtStore.activeSegments[initIndex];
    accordionSet(initIndex);
};
const toggleAccordion = async index => {
    taskAsgmtStore.activeSegments[index] = !taskAsgmtStore.activeSegments[index];

    await nextTick(); // DOM 업데이트 후 실행
    accordionSet(index, 0.5);
};
const accordionSet = (index, duration) => {
    const segment = accordionRefs.value[index];

    if (segment) {
        gsap.to(segment, {
            height: taskAsgmtStore.activeSegments[index] ? 'auto' : 0,
            duration: duration,
            ease: 'customEase'
        });
    } else {
        console.warn(`GSAP target for index ${index} not found`);
    }
};

// 검색 필터
const searchTerm = ref('');
const applyFilter = () => {
    const filteredData = segments.value
        .map(segment => {
            const filteredDataList = segment.dataList.filter(data => data.chartNm.toLowerCase().includes(searchTerm.value.toLowerCase()) || data.roleList.length.toString().includes(searchTerm.value.toLowerCase()) || data.registCnt.toString().includes(searchTerm.value.toLowerCase()) || data.title.toString().includes(searchTerm.value.toLowerCase()));
            return {
                ...segment,
                dataList: filteredDataList
            };
        })
        .filter(segment => segment.dataList.length > 0);

    filteredSegments.value = filteredData;
};
//-----------------------------------------------
// [스토어]
//-----------------------------------------------
onMounted(async () => {
    await searchData(true, true);
});

btnBack(() => {
    router.push('/EmergencyResponse');
});
btnSearch(() => {
    searchData(true);
});
const searchParam = ref({
    compId: getCompId()
});
import { useEmergencyResponseStore } from '@/stores/safewiz/impl/emergencyResponse.js';
const emergencyResponseStore = useEmergencyResponseStore();

const searchData = (notify, init) => {
    openLoading();
    getEmergencyControlTaskAsgmtList(searchParam.value, notify)
        .then(res => {
            setData(res.list);
        })
        .catch(() => {
            endLoading();
        })
        .finally(() => {
            if (init) {
                console.log('@@ filteredSegments.value', filteredSegments.value);
                let initIndex = filteredSegments.value.findIndex(el => el.year.includes(emergencyResponseStore.searchParam.searchText));
                initAccordion(initIndex);
                taskAsgmtStore.init = false;
            }
            filteredSegments.value.forEach((el, index) => {
                accordionSet(index);
            });
            endLoading();
        });
};
const setData = dataOrg => {
    segments.value = [];
    // taskAsgmtStore.activeSegments = {};

    for (var data of dataOrg) {
        const matchingYears = segments.value.filter(segment => segment.year.includes(data.writeYear));
        let chartData = JSON.parse(data.chartData);
        let roleList = [];
        chartData.forEach(el => {
            if (el.id.length === 4) {
                roleList.push({
                    roleId: el.id,
                    roleNm: el.label
                });
            }
        });
        data.roleList = roleList;
        data.detail = {
            [t('비상통제 조직도')]: data.chartNm,
            [t('비상통제 역할 수')]: roleList.length > 0 ? `${roleList.length}개` : '0개',
            [t('담당업무 등록 수')]: `${data.registCnt}건`
        };
        if (matchingYears.length == 0) {
            segments.value.push({ year: data.writeYear + '년도', dataList: [data] });
        } else {
            matchingYears[0].dataList.push(data);
        }
    }
    filteredSegments.value = _.cloneDeep(segments.value);
};

btnAdd(() => {
    taskAsgmtStore.initInputForm();
    router.push('/EmergencyControlTaskAsgmtDetail');
});
const btnNew = year => {
    if (!year) year = emergencyResponseStore.searchParam.searchText;
    taskAsgmtStore.initInputForm(year.substring(0, 4), true);
    router.push('/EmergencyControlTaskAsgmtDetail');
};
btnDelete(() => {
    let checkedList = [];
    filteredSegments.value.forEach(el => {
        checkedList = [...checkedList, ...el.dataList.filter(data => data.checked)];
    });
    if (!checkedList.length) {
        alertMsg('warning', t('msgNoItem'));
        return;
    }
    if (checkedList.some(el => el.useYn === 'N')) {
        alertMsg('warning', t('msgDeletedItem'));
        return;
    }
    confirmMsg('warning', t('msgDelete'), ``, { fun: deleteAction, param: checkedList });
});
const deleteAction = async checkedList => {
    openLoading();
    deleteEmergencyControlTaskAsgmt(checkedList, true)
        .then(res => {})
        .catch(() => {
            endLoading();
        })
        .finally(() => {
            searchData();
            endLoading();
        });
};
btnDownload(() => {
    let checkedList = [];
    filteredSegments.value.forEach(el => {
        checkedList = [...checkedList, ...el.dataList.filter(data => data.checked)];
    });
    if (!checkedList.length) {
        alertMsg('warning', t('msgNoItem'));
        return;
    }
    taskAsgmtStore.btnDownload(checkedList);
});

const btnDetail = data => {
    taskAsgmtStore.btnDetail(data, true);
};
</script>
