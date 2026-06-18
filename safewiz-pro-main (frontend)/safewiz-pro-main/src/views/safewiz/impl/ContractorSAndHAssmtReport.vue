<template>
    <div class="h100p df fdc">
        <div class="control-field ui form mb2-2rem">
            <div class="row flex gutters1rem">
                <div class="grid12-3 sm-grid12-6 es-grid12-12">
                    <input type="text" v-calendar="'yyyy'" v-model="planningCtrlStore.searchParam.searchText" class="datepicker w100p radius" year @input="searchData" />
                </div>
                <div class="grid12-2 us-grid12-12">
                    <div class="field">
                        <div class="df aic h4-4rem">
                            <input v-input="['사용', '미사용 포함']" type="checkbox" class="df switch" :checked="planningCtrlStore.searchParam.useYn === 'Y'" @change="changedUseYn" />
                        </div>
                    </div>
                </div>
                <div class="grid12-7 sm-grid12-6 es-grid12-12">
                    <div class="df aic w100p bcffffff">
                        <input v-input type="text" class="radius w100p search" placeholder="검색어를 입력하세요" v-model="searchTerm" @keyup.enter="applyFilter" />
                        <button type="button" class="shrink0" @click="applyFilter">
                            <img src="/assets/img/common/icon_search.svg" alt="검색 아이콘" />
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
            <div class="oh">
                <div class="df fdc gap1-2rem">
                    <div class="row flex gutters2rem">
                        <template v-for="(data, dataIndex) in filteredData" :key="dataIndex">
                            <i-card v-if="data.docNo != null" :v-model="data.checked" :data="data" :disabled="data.assmtUseYn === 'N'" :type="'basic'" :title="data.partCompNm" :useApprovalStatus="true" :approvalStatus="data.approvalStatus" @editor="btnDetail(data)" />
                            <i-card v-if="data.docNo == null" :useCheck="false" :unregistered="true" :type="'basic'" :title="data.partCompNm" @editor="btnDetail(data)" />
                        </template>
                        <!-- <div class="grid12-4 ul-grid12-6 lg-grid12-12">
                            <div class="card h100p df aic jcc cp" @click="btnNew()">
                                <div class="tac">
                                    <svg width="46" height="46" viewBox="0 0 46 46" fill="none" xmlns="http://www.w3.org/2000/svg">
                                        <path d="M31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417M26.0875 9.11005L18.5802 16.6173C15.3165 19.881 13.0012 23.9703 11.8817 28.4481L11.519 29.8991C11.406 30.3512 11.8155 30.7607 12.2676 30.6477L13.7186 30.2849C18.1963 29.1655 22.2857 26.8502 25.5494 23.5865L33.0566 16.0792C33.9808 15.155 34.5 13.9016 34.5 12.5946C34.5 9.873 32.2937 7.66669 29.5721 7.66669C28.2651 7.66669 27.0116 8.18588 26.0875 9.11005Z" stroke="black" stroke-width="1.5" />
                                        <path d="M26.0874 9.11005C27.0116 8.18588 28.265 7.66669 29.572 7.66669C32.2936 7.66669 34.4999 9.873 34.4999 12.5946C34.4999 13.9016 33.9808 15.155 33.0566 16.0792L31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417L26.0874 9.11005Z" stroke="#3248F6" stroke-width="1.5" />
                                        <path d="M36.4167 38.3333H9.58337" stroke="#3248F6" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
                                    </svg>
                                    <span class="db mt1rem fs2rem c999999">{{ t('card_addContractorSAndHAssmt') }}</span>
                                </div>
                            </div>
                        </div> -->
                    </div>
                </div>
            </div>
        </OverlayScrollbarsComponent>
    </div>
</template>

<script setup>
import { ref, watch, watchEffect } from 'vue';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';

import BaseView from '@/components/base/BaseView';
const { openLoading, endLoading, getCurrentDate, alertMsg, onMounted, router, confirmMsg, t, btnBack, btnSearch, btnAdd, btnDelete, btnDownload, baseDownload, getFormattedDate } = BaseView();

import { useButtonListStore } from '@/stores/buttonList';
const layoutStore = useButtonListStore();

layoutStore.useBtnList = ['btnBack', 'btnSearch', 'btnDelete', 'btnDownload'];

import { useContractorRegisterStore } from '@/stores/safewiz/impl/contractorRegister.js';
const contractorRegisterStore = useContractorRegisterStore();

onMounted(async () => {
    searchData(true);
});

// 버튼
btnBack(() => {
    router.push('/PlanningAndControl');
});

const btnDetail = async data => {
    //협력사는 존재하지만 안전보건 평가서가 등록되지 않은 데이터를 상세정보 보기 했을 경우
    if (data.docNo === null) {
        btnNew({ writeYear: planningCtrlStore.searchParam.searchText, partCompId: data.partCompId, partCompNm: data.partCompNm });
    } else {
        await contractorAssmtReportStore.btnDetail(data);
    }
    router.push('/ContractorSAndHAssmtReportDetail');
};

btnSearch(() => {
    searchData(true);
});
import { useContractorAssmtReportStore } from '@/stores/safewiz/impl/contractorAssmtReport';
import { deleteAssmtReport, getAssmtReport, getReport } from '@/stores/safewiz/impl/api/contractorAssmtReportApi';
const contractorAssmtReportStore = useContractorAssmtReportStore();

btnDelete(() => {
    const deleteData = dataList.value.filter(item => item.checked);

    if (deleteData.length <= 0) {
        alertMsg('warning', '선택한 항목이 없습니다.');
        return;
    }
    confirmMsg('info', t('msgCheckedDelete'), '', { fun: deleteAction, param: deleteData });
});

import { usePlanningCtrlStore } from '@/stores/safewiz/impl/planningAndControl';
const planningCtrlStore = usePlanningCtrlStore();

btnDownload(() => {
    let param = dataList.value.filter(el => {
        el.printAll = true;
        return el.checked;
    });
    if (param.length <= 0) {
        alertMsg('warning', '선택한 항목이 없습니다.');
        return;
    }
    confirmMsg('info', t('선택한 항목을 출력하시겠습니까?'), null, { fun: btnDownloadConfirm, param: param });
});

const btnDownloadConfirm = param => {
    let searchVO = { checkedObjList: param };
    baseDownload(getReport, searchVO);
};

const deleteAction = data => {
    let param = data.map(el => {
        return {
            writeYear: el.writeYear,
            docNo: el.docNo,
            docType: el.docType
        };
    });

    deleteAssmtReport(param).then(() => {
        searchData();
    });
};

const dataList = ref([]);
const searchData = () => {
    openLoading();
    getAssmtReport({ writeYear: planningCtrlStore.searchParam.searchText, useYn: planningCtrlStore.searchParam.useYn }, true)
        .then(res => {
            dataList.value = res.list.map(el => {
                el.assmtDt = getFormattedDate(el.assmtDt);
                return {
                    ...el,
                    detail: {
                        심사일자: el.assmtDt,
                        심사책임자: el.assmtManagerHrNm,
                        심사점수: el.point
                    }
                };
            });
            dataList.value.forEach(val => {
                //해당 년도에 존재하지 않는 데이터들은 docNo = null 처리
                if (val.assmtDt !== null) {
                    if (val.assmtDt.slice(0, 4) != planningCtrlStore.searchParam.searchText) {
                        val.docNo = null;
                    }
                }
            });
            dataList.value.sort((a, b) => {
                //docNo가 null인 데이터들은 뒤로 위치하도록 정렬
                if (a.docNo === null && b.docNo !== null) return 1;
                if (a.docNo !== null && b.docNo === null) return -1;
                return 0;
            });
            filteredData.value = dataList.value;
            endLoading();
        })
        .catch(() => {
            endLoading();
        });
};

const btnNew = async data => {
    //신규 협력업체 안전보건 평가 추가 버튼이 아닌, 협력사는 존재하지만 안전보건 평가서가 등록되지 않은 데이터를 상세정보 보기 했을 경우
    if (data !== undefined) {
        const today = new Date();
        const mm = String(today.getMonth() + 1).padStart(2, '0');
        const dd = String(today.getDate()).padStart(2, '0');

        await contractorAssmtReportStore.initInputForm();
        contractorAssmtReportStore.writeYear = data.writeYear;
        contractorAssmtReportStore.inputForm[0].assmtDt = data.writeYear + mm + dd;
        contractorAssmtReportStore.inputForm[0].partCompId = data.partCompId;
        contractorAssmtReportStore.inputForm[0].partCompNm = data.partCompNm;
    } else {
        await contractorAssmtReportStore.initInputForm();
    }
    router.push('/ContractorSAndHAssmtReportDetail');
};

const filteredData = ref();
const applyFilter = () => {
    filteredData.value = dataList.value.filter(data => {
        return (data.partCompNm != null && data.partCompNm.toString().toLowerCase().includes(searchTerm.value.toLowerCase())) || (data.assmtDt != null && data.assmtDt.toString().toLowerCase().includes(searchTerm.value.toLowerCase())) || (data.assmtManagerHrNm != null && data.assmtManagerHrNm.toString().toLowerCase().includes(searchTerm.value.toLowerCase()));
    });
};
const searchTerm = ref('');

const changedUseYn = () => {
    if (planningCtrlStore.searchParam.useYn == 'Y') {
        planningCtrlStore.searchParam.useYn = 'N';
    } else {
        planningCtrlStore.searchParam.useYn = 'Y';
    }
    searchData();
};
</script>
