<template>
    <div class="contents df fdc">
        <div class="control-field ui form mb2-2rem">
            <div class="tar cp">
                <a @click="linkPageUp" style="font-size: 15px; color: blue; text-decoration: underline">국가법령정보센터(www.law.or.kr) 바로가기</a>
            </div>
        </div>
        <div class="control-field ui form mb2-2rem">
            <div class="row flex gutters1rem">
                <div class="grid12-10 es-grid12-12">
                    <div class="df bcffffff">
                        <input v-input type="text" class="radius w100p search" :placeholder="t('placeHolderSearch')" v-model="LegalManageStore.searchTerm" @keyup.enter="LegalManageStore.applyFilter" />
                        <button type="submit" class="shrink0" @click.stop="LegalManageStore.applyFilter">
                            <img src="/assets/img/common/icon_search.svg" alt />
                        </button>
                    </div>
                </div>
                <div class="grid12-2 es-grid12-12">
                    <div class="df us-fww">
                        <label for=""> </label>
                        <button type="button" class="w100p btn radius active us-w100p" @click="legalManageTypeClick">
                            <span>법규 관리</span>
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
            <div class="oh df fdc minh100p ul-ha">
                <div class="df fdc rg2-2rem h100p fg1">
                    <div class="title-box fg1">
                        <p class="c3248F6">확정차수</p>

                        <div class="row flex gutters2rem">
                            <div class="grid12-4 lg-grid12-6 es-grid12-12" v-for="(item, index) in confirmedLegal" :key="index">
                                <i-card-order :useCheck="true" v-model="item.selected" :confirm="true" :title="item.legalNm" :data="item" :disabled="item.useYn === 'N'" @confirm="confirm(item, $event)" @editor="btnDetail(confirmedLegal[index])" :usePreview="false" />
                            </div>
                        </div>
                    </div>
                    <div class="title-box fg1">
                        <p>차수 리스트</p>
                        <div class="row flex gutters2rem">
                            <div class="grid12-4 lg-grid12-6 es-grid12-12" v-for="(item, index) in unConfirmedLegal" :key="index">
                                <i-card-order v-model="item.selected" :confirm="false" :title="item.legalNm" :data="item" :disabled="item.useYn === 'N'" :confirmable="confirmedLegal.filter(i => i.legalCd === item.legalCd).length === 0" @confirm="confirm(item, $event)" @editor="btnDetail(unConfirmedLegal[index])" :usePreview="false" />
                            </div>

                            <!-- 추가 -->
                            <div class="grid12-4 lg-grid12-6 es-grid12-12">
                                <div class="card h100p df aic jcc cp" @click="addAction">
                                    <div class="tac">
                                        <svg width="46" height="46" viewBox="0 0 46 46" fill="none" xmlns="http://www.w3.org/2000/svg">
                                            <path d="M31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417M26.0875 9.11005L18.5802 16.6173C15.3165 19.881 13.0012 23.9703 11.8817 28.4481L11.519 29.8991C11.406 30.3512 11.8155 30.7607 12.2676 30.6477L13.7186 30.2849C18.1963 29.1655 22.2857 26.8502 25.5494 23.5865L33.0566 16.0792C33.9808 15.155 34.5 13.9016 34.5 12.5946C34.5 9.873 32.2937 7.66669 29.5721 7.66669C28.2651 7.66669 27.0116 8.18588 26.0875 9.11005Z" stroke="black" stroke-width="1.5" />
                                            <path d="M26.0874 9.11005C27.0116 8.18588 28.265 7.66669 29.572 7.66669C32.2936 7.66669 34.4999 9.873 34.4999 12.5946C34.4999 13.9016 33.9808 15.155 33.0566 16.0792L31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417L26.0874 9.11005Z" stroke="#3248F6" stroke-width="1.5" />
                                            <path d="M36.4167 38.3333H9.58337" stroke="#3248F6" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
                                        </svg>
                                        <span class="db mt1rem fs2rem c999999">{{ t('card_addRegal') }}</span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <teleport to="body">
                <i-PopupDialog ref="legalManageTypePopup">
                    <!-- 단일 그리드 -->
                    <div class="contents form ui w70rem md-w100p">
                        <DataSetManagePopup :title="'법규 관리'" :datasetYn="'Y'" :popupDataList="popupDataList" :cardComponent="LegalManageTypeComponent" @filter="filterLegalManageTypePopup" @search="searchLegalManageTypePopup" @sample="sampleLegalManageTypePopup" @add="addLegalManageTypePopup" @delete="deleteLegalManageTypePopup" @save="saveLegalManageTypePopup" @close="closeLegalManageTypePopup"> </DataSetManagePopup>
                    </div>
                </i-PopupDialog>

                <i-PopupDialog ref="legalManageSamplePopup">
                    <!-- 단일 그리드 -->
                    <div class="contents form ui w70rem md-w100p">
                        <base-select-popup :title="'법규 관리 예시'" :component="BaseSelectAccordionComponent" uniqueKey="legalId" filterKey="legalNm" useYnKey="useYn" :excluded-value="'N'" :single="false" :fetch-data="getDatasetLegalManageType" @close="closeLegalManageSamplePopup" @apply="applyLegalManageSamplePopup" />
                    </div>
                </i-PopupDialog>
            </teleport>
        </OverlayScrollbarsComponent>
    </div>
</template>

<script setup>
import { nextTick, computed } from 'vue';
import router from '@/router/index.js';
import _ from 'lodash';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import BaseView from '@/components/base/BaseView';
const { btnSearch, btnBack, btnAdd, btnDelete, ref, onMounted, btnDownload, t, confirmMsg, goRouter, validationStore, endLoading, openLoading, alertMsg, getCompId, getDuplicatedData } = BaseView();
import { getLegalManageTypeList, getDatasetLegalManageType, saveLegalManageType, deleteLegalManageType } from '@/stores/safewiz/planning/api/LegalManageApi.js';
import DataSetManagePopup from '@/views/system/base/popup/DataSetManagePopup.vue';
import baseSelectPopup from '@/views/system/base/popup/BaseSelectPopup.vue';
import BaseSelectAccordionComponent from '@/views/system/base/popup/popupComponent/BaseSelectAccordionComponent.vue';
import LegalManageTypeComponent from '@/views/system/base/popup/popupComponent/LegalManageTypeComponent.vue';
import { useButtonListStore } from '@/stores/buttonList';
const layoutStore = useButtonListStore();
layoutStore.useBtnList = ['btnSearch', 'btnBack', 'btnAdd', 'btnDelete', 'btnDownload'];

import { useLegalManageStore } from '@/stores/safewiz/planning/LegalManage.js';
const LegalManageStore = useLegalManageStore();

const linkPageUp = () => {
    window.open('https://www.law.go.kr/');
};

onMounted(async () => {
    await LegalManageStore.initSearchDate();
    await LegalManageStore.btnSearch(true);

    await nextTick();
});

// 조회 버튼 이벤트 함수
btnSearch(() => {
    LegalManageStore.btnSearch(true);
    LegalManageStore.searchTerm = '';
});

//상세보기 버튼
const btnDetail = e => {
    const param = {
        writeYear: e.writeYear,
        docType: e.docType,
        docNo: e.docNo
    };
    goRouter('LegalManageDetail', param);
};

btnBack(() => {
    router.push({
        path: '/LegalMgmtAndComplianceEvaluation'
    });
});

btnDelete(async () => {
    LegalManageStore.btnDelete();
});

btnAdd(() => {
    addAction();
});

const addAction = () => {
    LegalManageStore.cmd = 'I';
    router.push('/LegalManageDetail');
};

// 출력
btnDownload(() => {
    LegalManageStore.downloadReport();
});

// 법규 차수 제어
const confirmedLegal = computed(() => LegalManageStore.confirmedLegal);
const unConfirmedLegal = computed(() => LegalManageStore.unConfirmedLegal);

// 차수 변경
const confirm = (item, param) => {
    const data = _.cloneDeep(item);
    data.confirmedYn = param;
    confirmMsg('info', param == 'Y' ? `${item.legalNm} 을(를) 확정 하시겠습니까?` : `${item.legalNm} 을(를) 확정 취소 하시겠습니까?`, null, { fun: confirmAction, param: data });
};

// 차수 적용
const confirmAction = item => {
    LegalManageStore.updateConfirmLegalManage(item);
};

// --------------- 법규 관리 팝업
const legalManageTypePopup = ref(null);
const originPopupData = ref([]);
const popupDataList = ref([]);
const legalManageTypeCheckedList = computed(() => {
    return popupDataList.value.filter(el => el.checked);
});
const isLegalManageTypePopupOpen = ref(false);

// --------------- 법규 관리 호출
const legalManageTypeClick = () => {
    clearValidationStore();
    searchActionLegalManageTypePopup(true).then(res => {
        legalManageTypePopup.value.onOpen();
    });
    isLegalManageTypePopupOpen.value = true;
};

// --------------- 검색
const filterLegalManageTypePopup = text => {
    const filteredData = originPopupData.value.filter(el => el.legalNm.toLowerCase().includes(text.toLowerCase()) || el.ordSeq == text);
    popupDataList.value = filteredData;
};

// --------------- 조회
const searchLegalManageTypePopup = () => {
    if (legalManageTypeCheckedList.value.length > 0) {
        confirmMsg('info', t('msgSaveContinue'), '', { fun: searchActionLegalManageTypePopup, param: true });
    } else {
        searchActionLegalManageTypePopup(true);
    }
};
const param = {
    compId: getCompId()
};
const searchActionLegalManageTypePopup = notify => {
    openLoading();
    return new Promise(resolve => {
        getLegalManageTypeList(param, notify)
            .then(res => {
                res.list.forEach(item => {
                    item.cmd = 'U';
                });
                popupDataList.value = res.list;
                endLoading();
                resolve({ result: res.list, success: res.success });
            })
            .catch(() => {
                endLoading();
            })
            .finally(() => {
                originPopupData.value = _.cloneDeep(popupDataList.value);
                setTimeout(() => {
                    const element = document.getElementById('list_0');
                    if (element) {
                        element.scrollIntoView({ block: 'center' });
                    }
                }, 100);
            });
    });
};

// --------------- 예시 불러오기
const legalManageSamplePopup = ref('');

// 예시 불러오기 아코디언 팝업 호출
const sampleLegalManageTypePopup = () => {
    legalManageSamplePopup.value.onOpen();
};

const closeLegalManageSamplePopup = () => {
    legalManageSamplePopup.value.onClose();
};

const applyLegalManageSamplePopup = data => {
    // 예시 팝업 적용 버튼 클릭
    const filteredData = getDuplicatedData(popupDataList.value, data, 'legalId', 'legalNm'); // 중복된 항목 추출
    if (filteredData.duplicatedDataList.length === data.length) {
        // 선택한 데이터가 모두 중복된 데이터일 때
        alertMsg('warning', t('msgAlreadyDeduplication'));
        return;
    } else if (filteredData.duplicatedDataList.length === 0) {
        // 중복된 데이터가 없을 때
        confirmMsg('warning', t('msgSelectedApply'), '', { fun: () => applySampleLegalManageType(filteredData.deDuplicatedDataList) });
        return;
    } else if (data.length <= 0) {
        alertMsg('warning', t('msgNoItem'));
        return;
    }
    confirmMsg('info', t('msgSaveDeduplication'), t('duplicatedData') + '\n' + filteredData.text, { fun: () => applySampleLegalManageType(filteredData.deDuplicatedDataList) });
};

const applySampleLegalManageType = dataList => {
    dataList.forEach(item => {
        item.cmd = 'I';
    });
    popupDataList.value = [...popupDataList.value, ...dataList];
    // 새로 추가된 항목으로 포커스 이동
    const index = popupDataList.value.length - 1;
    setTimeout(() => {
        const element = document.getElementById(`list_${index}`);
        if (element) {
            element.scrollIntoView({ block: 'center' });
        }
    }, 100);

    legalManageSamplePopup.value.onClose();
};

// --------------- 추가
const addLegalManageTypePopup = () => {
    const newData = {
        legalId: '',
        legalNm: '',
        ordSeq: '',
        useYn: 'Y',
        cmd: 'I',
        checked: true
    };
    popupDataList.value.push(newData);

    // 새로 추가된 항목으로 포커스 이동
    const index = popupDataList.value.length - 1;
    setTimeout(() => {
        const element = document.getElementById(`list_${index}`);
        if (element) {
            element.scrollIntoView({ block: 'center' });
        }
    }, 100);
};

// --------------- 삭제
const deleteLegalManageTypePopup = () => {
    if (legalManageTypeCheckedList.value.length > 0) {
        confirmMsg('info', t('msgDelete'), '', { fun: deleteActionLegalManageTypePopup, param: legalManageTypeCheckedList.value });
    } else {
        alertMsg('warning', t('msgNoItem'));
    }
};
const deleteActionLegalManageTypePopup = legalManageTypeCheckedList => {
    openLoading();
    deleteLegalManageType(legalManageTypeCheckedList, true)
        .then(res => {
            endLoading();
            searchActionLegalManageTypePopup(false);
        })
        .catch(() => {
            endLoading();
        });
};

// --------------- 저장
const saveLegalManageTypePopup = async () => {
    if (legalManageTypeCheckedList.value.length === 0) {
        alertMsg('warning', t('msgNoItem'));
        return;
    }

    const isValid = await validationStore.validateAllFields('dataSetPopupForm', true);
    if (isValid) {
        confirmMsg('info', t('msgSave'), '', { fun: saveActionLegalManageTypePopup, param: legalManageTypeCheckedList.value });
        return;
    }
};
const saveActionLegalManageTypePopup = async legalManageTypeCheckedList => {
    try {
        openLoading();
        await saveLegalManageType(legalManageTypeCheckedList, true);
        await searchActionLegalManageTypePopup(param, false);
    } catch (e) {
        endLoading();
    } finally {
        endLoading();
    }
};

// --------------- 닫기
const closeLegalManageTypePopup = () => {
    if (legalManageTypeCheckedList.value.length > 0) {
        confirmMsg('info', t('msgSaveContinue'), '', { fun: closeActionLegalManageTypePopup, param: '' });
    } else {
        closeActionLegalManageTypePopup();
    }
};
const closeActionLegalManageTypePopup = () => {
    popupDataList.value = [];
    legalManageTypePopup.value.onClose();
    isLegalManageTypePopupOpen.value = false;
};

const clearValidationStore = () => {
    validationStore.clearInvalidClasses();
    validationStore.clearAllErrors();
};
</script>
