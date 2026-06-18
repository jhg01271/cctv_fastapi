<template>
    <div class="control-field ui form mb2-2rem">
        <div class="row flex gutters1rem">
            <div class="grid12-3 es-grid12-6">
                <input type="text" v-calendar="'yyyy'" v-model="emergencyResponseStore.searchParam.searchText" class="datepicker w100p radius" year @input="searchData(true)" />
            </div>
            <div class="grid12-6 es-grid12-6">
                <div class="df bcffffff">
                    <input v-input type="text" class="radius w100p search" placeholder="검색어를 입력하세요" v-model="trainingStore.searchTerm" @keyup.enter="applyFilter" />
                    <button type="button" class="shrink0" @click="applyFilter">
                        <img src="/assets/img/common/icon_search.svg" alt="검색 아이콘" />
                    </button>
                </div>
            </div>
            <div class="grid12-3 es-grid12-12">
                <div class="toggle">
                    <button type="button" class="radius" v-for="div in filterDivList" :key="div.id" :class="{ active: currentFilter === div.id }" @click="filterChanged(div.id)">
                        <span>{{ div.name }}</span>
                    </button>
                </div>
            </div>
        </div>
    </div>
    <div class="df fdc gap1-2rem">
        <template v-for="(item, index) in filteredData" :key="index">
            <div class="title-box">
                <p class="fw500">{{ item.title }}</p>
                <div class="pa1rem">
                    <div class="row flex gutters2rem">
                        <template v-for="(data, dataIndex) in filteredData[index].dataList" :key="dataIndex">
                            <i-card :v-model="data.checked" :data="data" :disabled="data.useYn === 'N'" :type="'basic'" :title="data.trainingNm" @editor="btnDetail(data)" />
                        </template>
                        <div class="grid12-4 ul-grid12-6 lg-grid12-12">
                            <div class="card h100p df aic jcc cp" @click="btnNew(item.dataList[0])">
                                <div class="tac">
                                    <svg width="46" height="46" viewBox="0 0 46 46" fill="none" xmlns="http://www.w3.org/2000/svg">
                                        <path d="M31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417M26.0875 9.11005L18.5802 16.6173C15.3165 19.881 13.0012 23.9703 11.8817 28.4481L11.519 29.8991C11.406 30.3512 11.8155 30.7607 12.2676 30.6477L13.7186 30.2849C18.1963 29.1655 22.2857 26.8502 25.5494 23.5865L33.0566 16.0792C33.9808 15.155 34.5 13.9016 34.5 12.5946C34.5 9.873 32.2937 7.66669 29.5721 7.66669C28.2651 7.66669 27.0116 8.18588 26.0875 9.11005Z" stroke="black" stroke-width="1.5" />
                                        <path d="M26.0874 9.11005C27.0116 8.18588 28.265 7.66669 29.572 7.66669C32.2936 7.66669 34.4999 9.873 34.4999 12.5946C34.4999 13.9016 33.9808 15.155 33.0566 16.0792L31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417L26.0874 9.11005Z" stroke="#3248F6" stroke-width="1.5" />
                                        <path d="M36.4167 38.3333H9.58337" stroke="#3248F6" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
                                    </svg>
                                    <span class="db mt1rem fs2rem c999999">{{ t('card_addEmergencyResponseTrainingResult') }}</span>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </template>
        <div v-if="filteredData.length == 0" class="grid12-4 ul-grid12-6 lg-grid12-12">
            <div class="card h100p df aic jcc cp" @click="btnNew()">
                <div class="tac">
                    <svg width="46" height="46" viewBox="0 0 46 46" fill="none" xmlns="http://www.w3.org/2000/svg">
                        <path d="M31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417M26.0875 9.11005L18.5802 16.6173C15.3165 19.881 13.0012 23.9703 11.8817 28.4481L11.519 29.8991C11.406 30.3512 11.8155 30.7607 12.2676 30.6477L13.7186 30.2849C18.1963 29.1655 22.2857 26.8502 25.5494 23.5865L33.0566 16.0792C33.9808 15.155 34.5 13.9016 34.5 12.5946C34.5 9.873 32.2937 7.66669 29.5721 7.66669C28.2651 7.66669 27.0116 8.18588 26.0875 9.11005Z" stroke="black" stroke-width="1.5" />
                        <path d="M26.0874 9.11005C27.0116 8.18588 28.265 7.66669 29.572 7.66669C32.2936 7.66669 34.4999 9.873 34.4999 12.5946C34.4999 13.9016 33.9808 15.155 33.0566 16.0792L31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417L26.0874 9.11005Z" stroke="#3248F6" stroke-width="1.5" />
                        <path d="M36.4167 38.3333H9.58337" stroke="#3248F6" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
                    </svg>
                    <span class="db mt1rem fs2rem c999999">{{ t('card_addEmergencyResponseTrainingResult') }}</span>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref } from 'vue';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';

import BaseView from '@/components/base/BaseView';
const { goRouter, openLoading, endLoading, t, onMounted, router, confirmMsg, alertMsg, formatDate, btnBack, btnSearch, btnAdd, btnDelete, btnDownload, baseDownload } = BaseView();

import { useButtonListStore } from '@/stores/buttonList';
const layoutStore = useButtonListStore();

import { useEmergencyResponseStore } from '@/stores/safewiz/impl/emergencyResponse.js';
const emergencyResponseStore = useEmergencyResponseStore();

import { useTrainingResultStore } from '@/stores/safewiz/impl/trainingResult.js';
const trainingResultStore = useTrainingResultStore();

import { useTrainingStore } from '@/stores/safewiz/impl/training.js';
const trainingStore = useTrainingStore();

layoutStore.useBtnList = ['btnBack', 'btnSearch', 'btnAdd', 'btnDelete', 'btnDownload'];

import { getResultList, getResultDetail, deleteResult, getResultReport } from '@/stores/safewiz/impl/api/trainingResultApi.js';

const writeYear = ref(emergencyResponseStore.searchParam.searchText);
const filterDivList = ref([
    {
        id: 'orgn',
        name: t('ert_orgnNm')
    },
    {
        id: 'trainingLocation',
        name: t('ert_trainingLocation')
    }
]);
const currentFilter = ref('orgn');

const filterChanged = id => {
    currentFilter.value = id;
    if (id === 'orgn') {
        filteredData.value = orgnRstList.value;
        applyFilter();
    } else if (id === 'trainingLocation') {
        filteredData.value = locRstList.value;
        applyFilter();
    }
};
onMounted(async () => {
    trainingStore.searchTerm = '';
    searchData(true);
});

// 버튼
btnBack(() => {
    router.push('/EmergencyResponse');
});

btnSearch(() => {
    searchData(true);
});

const orgnRstList = ref([]);
const locRstList = ref([]);
const filteredData = ref([]);

const searchData = notify => {
    openLoading();
    getResultList({ writeYear: emergencyResponseStore.searchParam.searchText }, notify)
        .then(res => {
            console.log('@@res,res', res);
            orgnRstList.value = [];
            locRstList.value = [];
            filteredData.value = [];
            if (res.list.length > 0) {
                orgnRstList.value = setData(res.list, 'orgnNm');
                locRstList.value = setData(res.list, 'trainingLocation');
                filteredData.value = orgnRstList.value;
                console.log('@@@ filteredData.value', filteredData.value);
            }
        })
        .catch(() => {
            endLoading();
        })
        .finally(() => {
            endLoading();
        });
};

const filteredRstList = ref([]);
const setData = (dataOrg, div) => {
    filteredRstList.value = [];
    for (var data of dataOrg) {
        const matchings = filteredRstList.value.filter(list => list.title.includes(data[div]));
        data.detail = {
            // [t('orgn')]: data.orgnNm,
            // [t('trainingLocation')]: data.trainingLocation
            [t('ert_orgnNm')]: data.orgnNm,
            [t('ert_trainingLocation')]: data.trainingLocation,
            [t('ert_trainingCnt')]: data.resultList.filter(el => el.useYn === 'Y').length + '회'
        };
        data.actionDt = formatDate(data.actionDt);

        if (matchings.length == 0) {
            filteredRstList.value.push({ title: data[div], dataList: [data] });
        } else {
            matchings[0].dataList.push(data);
        }
    }

    return filteredRstList.value;
};
const btnDetail = data => {
    // trainingResultStore.inputForm = data;
    trainingResultStore.inputForm.writeYear = data.writeYear;
    trainingResultStore.inputForm.docType = data.docType;
    trainingResultStore.inputForm.docNo = data.docNo;
    trainingResultStore.inputForm.cmd = 'U';
    trainingResultStore.actionDtList = [];
    const param = {
        writeYear: data.writeYear,
        docType: data.docType,
        docNo: data.docNo
    };
    goRouter('EmergencyResponseTrainingResultDetail', param);
};
btnAdd(async () => {
    await trainingResultStore.initInputForm();

    router.push('/EmergencyResponseTrainingResultDetail');
});
const btnNew = async item => {
    console.log('@@item', item);
    await trainingResultStore.initInputForm();
    // if (currentFilter.value === 'orgn') {
    //     trainingScenarioStore.inputForm.orgnId = item.orgnId;
    //     trainingScenarioStore.inputForm.orgnNm = item.orgnNm;
    // } else if (currentFilter.value === 'trainingLocation') {
    //     trainingScenarioStore.inputForm.trainingLocation = item.trainingLocation;
    // }
    router.push('/EmergencyResponseTrainingResultDetail');
};
btnDelete(() => {
    let checkedList = [];
    filteredData.value.forEach(el => {
        checkedList = [...checkedList, ...el.dataList.filter(data => data.checked)];
    });
    const param = checkedList;
    if (!param.length) {
        alertMsg('warning', t('msgNoItem'));
        return;
    }
    if (param.some(el => el.useYn === 'N')) {
        alertMsg('warning', t('msgDeletedItem'));
        return;
    }
    confirmMsg('warning', t('msgDelete'), ``, { fun: deleteAction, param: param });
});
const deleteAction = list => {
    let deleteParam = [];
    list.forEach(el => {
        deleteParam = [...deleteParam, ...el.resultList];
    });
    console.log('@@', deleteParam);
    openLoading();
    deleteResult(deleteParam, true)
        .then(res => {
            searchData(false);
        })
        .catch(() => {
            endLoading();
        })
        .finally(() => {
            endLoading();
        });
};
btnDownload(() => {
    let checkedList = [];
    filteredData.value.forEach(el => {
        checkedList = [...checkedList, ...el.dataList.filter(data => data.checked)];
    });
    const param = checkedList;
    if (!param.length) {
        alertMsg('warning', t('msgNoItem'));
        return;
    }
    checkedList = checkedList.map(el => ({
        writeYear: el.writeYear,
        docType: el.docType,
        docNo: el.docNo,
        compId: el.compId
    }));

    confirmMsg('warning', t('msgCheckedPrint'), '', { fun: downloadAction, param: checkedList });
});
const downloadAction = checkedList => {
    let searchVO = { writeYear: emergencyResponseStore.searchParam.searchText, checkedObjList: checkedList };
    baseDownload(getResultReport, searchVO);
    // openLoading();
    // getResultReport({ writeYear: emergencyResponseStore.searchParam.searchText, checkedObjList: checkedList }, true)
    //     .then(res => {
    //         downloadReport(res);
    //     })
    //     .catch(() => {
    //         endLoading();
    //     })
    //     .finally(() => {
    //         endLoading();
    //     });
};

// const searchTerm = ref('');
const applyFilter = () => {
    let filteringData = [];
    if (currentFilter.value === 'orgn') {
        filteringData = trainingStore.applyFilter(orgnRstList.value);
    } else if (currentFilter.value === 'trainingLocation') {
        filteringData = trainingStore.applyFilter(locRstList.value);
    }
    filteredData.value = filteringData;
};
</script>
