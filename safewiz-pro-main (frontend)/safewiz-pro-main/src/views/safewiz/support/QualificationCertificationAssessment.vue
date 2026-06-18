<template>
    <!-- 콘텐츠 영역 -->
    <div class="contents df fdc">
        <!-- 검색 필드 -->
        <div class="control-field ui form mb2-2rem">
            <div class="row flex gutters1rem">
                <div class="grid12-3 es-grid12-12">
                    <div class="control-field ui form">
                        <div class="field">
                            <input v-input type="text" v-calendar="'yyyy'" year v-model="qualificationManagementStore.writeYear" class="datepicker w20rem radius es-w100p" @input="init" />
                        </div>
                    </div>
                </div>
                <div class="grid12-6 es-grid12-12">
                    <div class="df bcffffff">
                        <input v-input type="text" class="radius w100p search" placeholder="검색어를 입력하세요" v-model="searchTerm" @keydown.enter="applyFilter" />
                        <button type="submit" class="shrink0" @click="applyFilter">
                            <img src="/assets/img/common/icon_search.svg" alt />
                        </button>
                    </div>
                </div>
                <div class="grid12-3 es-grid12-12">
                    <div class="df gap1rem us-fww">
                        <label for=""> </label>
                        <button type="button" class="w100p btn radius active us-w100p" @click="openEditTablePopup">
                            <span>내부심사원 자격인증 평가표 관리</span>
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
                <div class="row flex gutters2rem">
                    <template v-for="(i, idx) in filteredData" :key="idx">
                        <i-card :v-model="i.checked" :data="i" :disabled="i.useYn == 'N'" :type="'profile'" :title="i.hrNm" :log-img="i.logoId ? i.logoId : ''" :useApprovalStatus="true" :approvalStatus="i.approvalStatus" @handle="handleEvent" @editor="btnDetail"></i-card>
                    </template>
                    <div class="grid12-4 ul-grid12-6 lg-grid12-12">
                        <div class="card h100p df aic jcc cp" @click="btnNew()">
                            <div class="tac">
                                <svg width="46" height="46" viewBox="0 0 46 46" fill="none" xmlns="http://www.w3.org/2000/svg">
                                    <path d="M31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417M26.0875 9.11005L18.5802 16.6173C15.3165 19.881 13.0012 23.9703 11.8817 28.4481L11.519 29.8991C11.406 30.3512 11.8155 30.7607 12.2676 30.6477L13.7186 30.2849C18.1963 29.1655 22.2857 26.8502 25.5494 23.5865L33.0566 16.0792C33.9808 15.155 34.5 13.9016 34.5 12.5946C34.5 9.873 32.2937 7.66669 29.5721 7.66669C28.2651 7.66669 27.0116 8.18588 26.0875 9.11005Z" stroke="black" stroke-width="1.5" />
                                    <path d="M26.0874 9.11005C27.0116 8.18588 28.265 7.66669 29.572 7.66669C32.2936 7.66669 34.4999 9.873 34.4999 12.5946C34.4999 13.9016 33.9808 15.155 33.0566 16.0792L31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417L26.0874 9.11005Z" stroke="#3248F6" stroke-width="1.5" />
                                    <path d="M36.4167 38.3333H9.58337" stroke="#3248F6" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
                                </svg>
                                <span class="db fs2rem c999999">{{ t('card_addQualificationCertificationAssessment') }}</span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </OverlayScrollbarsComponent>
        <teleport to="body">
            <i-PopupDialog ref="editTablePopup">
                <!-- 내부심사원 자격인증 평가표 관리 -->
                <div class="contents w1024px md-w100p">
                    <DataSetManageByItemPopup ref="managePopup" title="내부심사원 자격인증 평가표 관리" :masterReadonly="true" :masterApi="getEvaluationList" :masterTitle="'유해위험요인'" masterKey="itemId" masterDisplayKey="itemNm" :detailApi="getEvaluationDetailList" detailKey="itemId" :detailOption="detailOption" :detailType="'point'" @sample="btnSample" @delete="btnFactorDelete" @save="btnEvaluationListSave" @close="closeEditTablePopup" />
                </div>
            </i-PopupDialog>
            <i-PopupDialog ref="samplePopup">
                <!-- 예시 불러오기 팝업 -->
                <div class="contents form ui w70rem md-w100p">
                    <base-select-popup :component="BaseSelectComponet" :title="'평가기준 예시'" filterKey="criteriaNm" useYnKey="useYn" :excluded-value="'N'" :single="false" :fetch-data="getEvaluationDataSetList" :fetch-param="{ searchText: managePopup.selectedCard?.itemId }" @apply="applySampleDataSetMngPopup" @close="closeSampleDataSetMngPopup" />
                </div>
            </i-PopupDialog>
        </teleport>
    </div>
</template>
<script setup>
import { useButtonListStore } from '@/stores/buttonList';

import { getList, deleteList, getEvaluationList, getEvaluationDetailList, getEvaluationDataSetList, saveEvaluationList } from '@/stores/safewiz/support/api/QualificationCertificationAssessmentApi';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
const layoutStore = useButtonListStore();
layoutStore.useBtnList = ['btnSearch', 'btnAdd', 'btnDelete', 'btnBack', 'btnDownload'];

import BaseView from '@/components/base/BaseView';
const { getDuplicatedData, goRouter, btnDownload, computed, getCurrentDate, confirmMsg, alertMsg, ref, onMounted, t, formatDate, watch, btnSearch, btnAdd, btnDelete, btnBack } = BaseView();

import router from '@/router';

import { useQualificationCertStore } from '@/stores/safewiz/support/QualificationCertificationAssessment';
const qualificationCertStore = useQualificationCertStore();
const searchTerm = ref('');
const data = ref();
import { useQualificationManagementStore } from '@/stores/safewiz/support/QualificationManagement';
import _ from 'lodash';
const qualificationManagementStore = useQualificationManagementStore();
import { createSignatureStore } from '@/stores/signature';
const signatureStore = createSignatureStore(); // 컴포넌트마다 고유한 Store 생성
import BaseSelectPopup from '@/views/system/base/popup/BaseSelectPopup.vue';
import DataSetManageByItemPopup from '@/views/system/base/popup/DataSetManageByItemPopup.vue';
import BaseSelectComponet from '@/views/system/base/popup/popupComponent/BaseSelectComponet.vue';
const editTablePopup = ref(null);
const managePopup = ref(null);
const samplePopup = ref(null);
btnDownload(() => {
    let param = data.value.filter(el => {
        return el.checked;
    });
    if (param.length <= 0) {
        alertMsg('warning', '선택한 항목이 없습니다.');
        return;
    }

    confirmMsg('info', t('msgCheckedPrint'), null, { fun: qualificationCertStore.btnDownload, param: param });
});
const detailOption = ref([
    {
        fieldDisplayKey: 'criteriaNm',
        fieldNm: '평가 기준',
        fieldKey: 'criteriaId',
        class: '',
        required: true
    }
]);
const openEditTablePopup = () => {
    editTablePopup.value.onOpen();
};

const btnEvaluationListSave = async saveParam => {
    const saveList = saveParam.flatMap(el =>
        el.detailDataList.map(detail => ({
            ...detail,
            factorId: el.minorCd
        }))
    );
    saveEvaluationList(saveList, true).then(res => {
        managePopup.value.searchMaster();
    });
};

const closeEditTablePopup = () => {
    editTablePopup.value.onClose();
};
const btnSample = () => {
    samplePopup.value.onOpen();
};

const applySampleDataSetMngPopup = data => {
    // 예시 팝업 적용 버튼 클릭. 키가 N개 인 경우 리스트로 전송
    const filteredData = getDuplicatedData(managePopup.value.selectedCard?.detailDataList, data, ['itemId', 'criteriaId'], 'criteriaNm'); // 중복된 항목 추출
    if (filteredData.duplicatedDataList.length === data.length) {
        // 선택한 데이터가 모두 중복된 데이터일 때
        alertMsg('warning', t('msgAlreadyDeduplication'));
        return;
    } else if (filteredData.duplicatedDataList.length === 0) {
        // 중복된 데이터가 없을 때
        confirmMsg('warning', t('msgSelectedApply'), '', { fun: () => applySampleData(filteredData.deDuplicatedDataList) });
    } else {
        confirmMsg('info', t('msgSaveDeduplication'), t('duplicatedData') + '\n' + filteredData.text, { fun: () => applySampleData(filteredData.deDuplicatedDataList) });
    }
};

const applySampleData = async dataList => {
    dataList.forEach(el => {
        el.cmd = 'I';
        el.checked = true;
    });
    // 예시 팝업 적용 버튼 클릭
    managePopup.value.setDetailSampleData(dataList);

    samplePopup.value.onClose();
};

const closeSampleDataSetMngPopup = () => {
    samplePopup.value.onClose();
};
const btnDetail = async value => {
    //디테일 조회
    const param = {
        writeYear: value.writeYear,
        docType: value.docType,
        docNo: value.docNo
    };
    goRouter('QualificationCertificationAssessmentDetail', param);
};
btnBack(() => {
    router.push('/QualificationManagement');
});
btnAdd(() => {
    qualificationCertStore.init();
    qualificationCertStore.cmd = 'I';
    router.push('/QualificationCertificationAssessmentDetail');
});

const btnNew = () => {
    qualificationCertStore.init();
    qualificationCertStore.cmd = 'I';
    router.push('/QualificationCertificationAssessmentDetail');
};

onMounted(() => {
    init();
});

const init = () => {
    qualificationCertStore.init();
    getList({ writeYear: qualificationManagementStore.writeYear }, true).then(res => {
        data.value = res.list.map(el => {
            return {
                ...el,
                detail: {
                    작성일자: formatDate(el.writeDate),
                    작성자: el.writerNm,
                    자격조건: el.hrType ? qualificationCertStore.hrTypeList.find(type => type.value === el.hrType).label : '-',
                    통과여부: el.passYn === 'Y' ? '통과' : '미통과'
                }
            };
        });
        filteredData.value = data.value;
        currentFilteredData.value = _.cloneDeep(filteredData.value);
    });
};
const currentFilteredData = ref('');
const filteredData = ref('');

btnSearch(() => {
    init();
});

btnDelete(() => {
    let param = filteredData.value.filter(el => {
        return el.checked;
    }); // rowKey로 행 데이터를 가져옴

    if (!param.length) {
        alertMsg('error', '선택된 항목이 없습니다.');
        return;
    }
    if (param.some(el => el.useYn === 'N')) {
        alertMsg('error', '이미 삭제 처리된 항목입니다.');
        return;
    }
    confirmMsg('warning', '삭제 하시겠습니까?', '', { fun: deleteAction, param: param });
});
const deleteAction = data => {
    deleteList(data).then(async () => {
        for (let item of data) {
            await signatureStore.forcedUpdateTaskUseYn('N', 'QCA', item.writeYear, item.docNo);
        }
        await init();
    });
};

const applyFilter = () => {
    filteredData.value = _.cloneDeep(currentFilteredData.value);
    if (searchTerm.value != '') {
        filteredData.value = filteredData.value.filter(data => {
            return data.detail?.['작성일자'].includes(searchTerm.value) || data.detail?.['작성자'].toString().toLowerCase().includes(searchTerm.value.toLowerCase()) || data.detail?.['자격조건'].toString().toLowerCase().includes(searchTerm.value.toLowerCase()) || data.detail?.['통과여부'].toString().toLowerCase().includes(searchTerm.value.toLowerCase());
        });
    }
};
</script>
