<template>
    <!-- 콘텐츠 영역 -->
    <div class="contents df fdc">
        <!-- 검색 필드 -->
        <div class="control-field ui form mb2-2rem">
            <div class="row flex gutters1rem">
                <div class="grid12-8 lg-grid12-6 es-grid12-12">
                    <div class="df aic bcffffff">
                        <input v-input type="text" class="radius w100p search" placeholder="검색어를 입력하세요" v-model="msdsStore.searchTerm" @keyup.enter="msdsStore.applyFilter" />
                        <button type="submit" class="shrink0" @click="msdsStore.applyFilter">
                            <img src="/assets/img/common/icon_search.svg" alt />
                        </button>
                    </div>
                </div>                
                <div class="grid12-4 lg-grid12-6 es-grid12-12">
                    <div class="df gap1rem us-fww">
                        <button type="button" class="w30p btn radius active us-w100p" @click="openUnitPopup">
                            <span>MSDS 단위 관리</span>
                        </button>
                        <div class="toggle w70p us-w100p">
                            <button v-for="div in msdsStore.msdsDivList" :key="div.id" type="button" :class="{ active: msdsStore.currentFilter === div.id }" @click="msdsStore.currentFilter = div.id">
                                <span>{{ div.name }}</span>
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <!-- 작업장 -->
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
                <div class="df fdc gap2-2rem" v-if="msdsStore.currentFilter === 'workplace'">
                    <template v-for="(wp, i) in msdsStore.sortList" :key="i">
                        <div v-if="Object.values(msdsStore.filteredByWpMsdsListBySearch).some(group => group.length > 0)" class="segment box">
                            <div class="pt2-2rem pb1-2rem px2-2rem wbka">
                                <h3>{{ wp }}</h3>
                            </div>
                            <hr />
                            <div class="pa2-2rem">
                                <div class="row flex gutters2rem">
                                    <template v-for="(msds, index) in msdsStore.filteredByWpMsdsListBySearch[wp]" :key="index">
                                        <i-card :v-model="msds.checked" :data="msds" :disabled="msds.useYn === 'N'" :title="msds.msdsNm" :use-thumbnail="true" :thumbnail-img="msds.fileId" @handle="handleEvent" @editor="msdsStore.btnDetail(msds.msdsId)"></i-card>
                                    </template>
                                    <div class="grid12-4 ul-grid12-6 lg-grid12-12">
                                        <div class="card h100p df aic jcc cp" @click="msdsStore.btnAdd('temp', msdsStore.filteredByWpMsdsListBySearch[wp][0].workplaceList)">
                                            <div class="tac">
                                                <svg width="46" height="46" viewBox="0 0 46 46" fill="none" xmlns="http://www.w3.org/2000/svg">
                                                    <path d="M31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417M26.0875 9.11005L18.5802 16.6173C15.3165 19.881 13.0012 23.9703 11.8817 28.4481L11.519 29.8991C11.406 30.3512 11.8155 30.7607 12.2676 30.6477L13.7186 30.2849C18.1963 29.1655 22.2857 26.8502 25.5494 23.5865L33.0566 16.0792C33.9808 15.155 34.5 13.9016 34.5 12.5946C34.5 9.873 32.2937 7.66669 29.5721 7.66669C28.2651 7.66669 27.0116 8.18588 26.0875 9.11005Z" stroke="black" stroke-width="1.5" />
                                                    <path d="M26.0874 9.11005C27.0116 8.18588 28.265 7.66669 29.572 7.66669C32.2936 7.66669 34.4999 9.873 34.4999 12.5946C34.4999 13.9016 33.9808 15.155 33.0566 16.0792L31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417L26.0874 9.11005Z" stroke="#3248F6" stroke-width="1.5" />
                                                    <path d="M36.4167 38.3333H9.58337" stroke="#3248F6" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
                                                </svg>
                                                <span class="db mt1rem fs2rem c999999">{{ t('card_addMSDS') }}</span>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </template>
                    <!-- 데이터가 없을 때 작업장 탭 msds 등록 버튼-->
                    <div v-if="!Object.values(msdsStore.filteredByWpMsdsListBySearch).length > 0">
                        <div class="grid12-4 ul-grid12-6 lg-grid12-12">
                            <div class="card h100p df aic jcc cp" @click="msdsStore.btnAdd('temp')">
                                <div class="tac">
                                    <svg width="46" height="46" viewBox="0 0 46 46" fill="none" xmlns="http://www.w3.org/2000/svg">
                                        <path d="M31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417M26.0875 9.11005L18.5802 16.6173C15.3165 19.881 13.0012 23.9703 11.8817 28.4481L11.519 29.8991C11.406 30.3512 11.8155 30.7607 12.2676 30.6477L13.7186 30.2849C18.1963 29.1655 22.2857 26.8502 25.5494 23.5865L33.0566 16.0792C33.9808 15.155 34.5 13.9016 34.5 12.5946C34.5 9.873 32.2937 7.66669 29.5721 7.66669C28.2651 7.66669 27.0116 8.18588 26.0875 9.11005Z" stroke="black" stroke-width="1.5" />
                                        <path d="M26.0874 9.11005C27.0116 8.18588 28.265 7.66669 29.572 7.66669C32.2936 7.66669 34.4999 9.873 34.4999 12.5946C34.4999 13.9016 33.9808 15.155 33.0566 16.0792L31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417L26.0874 9.11005Z" stroke="#3248F6" stroke-width="1.5" />
                                        <path d="M36.4167 38.3333H9.58337" stroke="#3248F6" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
                                    </svg>
                                    <span class="db mt1rem fs2rem c999999">{{ t('card_addMSDS') }}</span>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <!-- 공정 -->
                <div class="df fdc gap2-2rem" v-else-if="msdsStore.currentFilter === 'process'">
                    <template v-for="(prcs, i) in Object.keys(msdsStore.filteredByPrcsMsdsListBySearch)" :key="i">
                        <div v-if="Object.values(msdsStore.filteredByPrcsMsdsListBySearch).some(group => group.length > 0)" class="segment box">
                            <div class="pt2-2rem pb1-2rem px2-2rem wbka">
                                <h3>{{ prcs }}</h3>
                            </div>
                            <hr />
                            <div class="pa2-2rem">
                                <div class="row flex gutters2rem">
                                    <template v-for="(msds, index) in msdsStore.filteredByPrcsMsdsListBySearch[prcs]" :key="index">
                                        <i-card :v-model="msds.checked" :data="msds" :disabled="msds.useYn === 'N'" :title="msds.msdsNm" :use-thumbnail="true" :thumbnail-img="msds.fileId" @handle="handleEvent" @editor="msdsStore.btnDetail(msds.msdsId)"></i-card>
                                    </template>
                                    <div class="grid12-4 ul-grid12-6 lg-grid12-12">
                                        <div class="card h100p df aic jcc cp" @click="msdsStore.btnAdd('temp', msdsStore.filteredByPrcsMsdsListBySearch[prcs][0].workList)">
                                            <div class="tac">
                                                <svg width="46" height="46" viewBox="0 0 46 46" fill="none" xmlns="http://www.w3.org/2000/svg">
                                                    <path d="M31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417M26.0875 9.11005L18.5802 16.6173C15.3165 19.881 13.0012 23.9703 11.8817 28.4481L11.519 29.8991C11.406 30.3512 11.8155 30.7607 12.2676 30.6477L13.7186 30.2849C18.1963 29.1655 22.2857 26.8502 25.5494 23.5865L33.0566 16.0792C33.9808 15.155 34.5 13.9016 34.5 12.5946C34.5 9.873 32.2937 7.66669 29.5721 7.66669C28.2651 7.66669 27.0116 8.18588 26.0875 9.11005Z" stroke="black" stroke-width="1.5" />
                                                    <path d="M26.0874 9.11005C27.0116 8.18588 28.265 7.66669 29.572 7.66669C32.2936 7.66669 34.4999 9.873 34.4999 12.5946C34.4999 13.9016 33.9808 15.155 33.0566 16.0792L31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417L26.0874 9.11005Z" stroke="#3248F6" stroke-width="1.5" />
                                                    <path d="M36.4167 38.3333H9.58337" stroke="#3248F6" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
                                                </svg>
                                                <span class="db mt1rem fs2rem c999999">{{ t('card_addMSDS') }}</span>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </template>
                    <!-- 데이터가 없을 때 공정 탭 msds 등록 버튼-->
                    <div v-if="!Object.values(msdsStore.filteredByPrcsMsdsListBySearch).length > 0">
                        <div class="grid12-4 ul-grid12-6 lg-grid12-12">
                            <div class="card h100p df aic jcc cp" @click="msdsStore.btnAdd('temp')">
                                <div class="tac">
                                    <svg width="46" height="46" viewBox="0 0 46 46" fill="none" xmlns="http://www.w3.org/2000/svg">
                                        <path d="M31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417M26.0875 9.11005L18.5802 16.6173C15.3165 19.881 13.0012 23.9703 11.8817 28.4481L11.519 29.8991C11.406 30.3512 11.8155 30.7607 12.2676 30.6477L13.7186 30.2849C18.1963 29.1655 22.2857 26.8502 25.5494 23.5865L33.0566 16.0792C33.9808 15.155 34.5 13.9016 34.5 12.5946C34.5 9.873 32.2937 7.66669 29.5721 7.66669C28.2651 7.66669 27.0116 8.18588 26.0875 9.11005Z" stroke="black" stroke-width="1.5" />
                                        <path d="M26.0874 9.11005C27.0116 8.18588 28.265 7.66669 29.572 7.66669C32.2936 7.66669 34.4999 9.873 34.4999 12.5946C34.4999 13.9016 33.9808 15.155 33.0566 16.0792L31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417L26.0874 9.11005Z" stroke="#3248F6" stroke-width="1.5" />
                                        <path d="M36.4167 38.3333H9.58337" stroke="#3248F6" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
                                    </svg>

                                    <span class="db mt1rem fs2rem c999999">{{ t('card_addMSDS') }}</span>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </OverlayScrollbarsComponent>

        <teleport to="body">
            <!-- 팝업-->
            <i-PopupDialog ref="unitManagePopup">
                <!-- 단일 그리드 -->
                <div class="contents form formUnit ui w70rem md-w100p">
                    <!-- <base-select-popup :title="'직위'" uniqueKey="minorCd" filterKey="minorNm" useYnKey="useYn" :excluded-value="'N'" :single="true" :fetchParam="{ majorCd: 'C0005' }" :fetch-data="getSystemCode" @close="closeSystemPopup" /> -->
                    <!-- 디폴트 형태의 DataSetManagePopup인 경우 :cardComponet 를 지정하지 않아도 됨. 다른 형태인 경우 :cardComponet 지정 필수  -->
                    <DataSetManagePopup :title="'MSDS 단위 관리'" :subTitle="'단위명'" :datasetYn="'Y'" id="unitId" nm="unitNm" :popupDataList="popupDataList" :cardComponent="DefaultDataSetManageComponet" @filter="filterUnitManagePopup" @search="searchUnitManagePopup" @sample="sampleUnitManagePopup" @add="addUnitManagePopup" @delete="deleteUnitManagePopup" @save="saveUnitManagePopup" @close="closeUnitManagePopup"> </DataSetManagePopup>
                </div>
            </i-PopupDialog>

            <i-PopupDialog ref="unitSamplePopup">
                <!-- 예시 불러오기 팝업 -->
                <div class="contents form ui w70rem md-w100p">
                    <base-select-popup :component="BaseSelectComponet" :title="'MSDS 단위 예시'" filterKey="unitNm" useYnKey="useYn" :excluded-value="'N'" :single="false" :fetch-data="getMsdsUnitDataset" :fetch-param="{}" @apply="applySampleDataSetMngPopup" @close="closeSampleDataSetMngPopup" />
                </div>
            </i-PopupDialog>
        </teleport>
    </div>
</template>

<script setup>
import BaseView from '@/components/base/BaseView';
const { onMounted, btnSearch, btnAdd, btnDelete, t, btnDownload, ref, onBeforeMount, getCompId, computed, confirmMsg, alertMsg, openLoading, endLoading, validationStore, getDuplicatedData, nextTick } = BaseView();
import { getMsdsUnit, insertMsdsUnit, getMsdsUnitDataset, deleteMsdsUnit } from '@/views/safewiz/planning/api/msdsUnitApi';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import DefaultDataSetManageComponet from '@/views/system/base/popup/popupComponent/DefaultDataSetManageComponet.vue';
import DataSetManagePopup from '@/views/system/base/popup/DataSetManagePopup.vue';
import { useMsdsStore } from '@/stores/safewiz/planning/msds.js';
const msdsStore = useMsdsStore();

import { useButtonListStore } from '@/stores/buttonList';
const layoutStore = useButtonListStore();
layoutStore.useBtnList = ['btnSearch', 'btnAdd', 'btnDelete', 'btnDownload'];

// 조회 버튼 이벤트 함수
btnSearch(() => {
    msdsStore.btnSearch(true);
});
btnAdd(() => {
    msdsStore.btnAdd('temp');
});
btnDelete(() => {
    msdsStore.btnDelete();
});
btnDownload(() => {
    msdsStore.btnDownload(searchparamList.value);
});

const compId = getCompId();

const unitManagePopup = ref(null);

const checkedList = computed(() => {
    return popupDataList.value.filter(el => el.checked);
});

// ------------ 예시 팝업
import BaseSelectPopup from '@/views/system/base/popup/BaseSelectPopup.vue';
import BaseSelectComponet from '@/views/system/base/popup/popupComponent/BaseSelectComponet.vue';
import _ from 'lodash';
const unitSamplePopup = ref(null);
const applySampleDataSetMngPopup = data => {
    // 예시 팝업 적용 버튼 클릭
    const filteredData = getDuplicatedData(popupDataList.value, data, 'id', 'unitNm'); // 중복된 항목 추출
    if (filteredData.duplicatedDataList.length === data.length) {
        // 선택한 데이터가 모두 중복된 데이터일 때
        alertMsg('warning', t('msgAlreadyDeduplication'));
        return;
    } else if (filteredData.duplicatedDataList.length === 0) {
        // 중복된 데이터가 없을 때
        confirmMsg('warning', t('msgSelectedApply'), '', { fun: () => applySampleData(filteredData.deDuplicatedDataList) });
        return;
    }
    confirmMsg('info', t('msgSaveDeduplication'), t('duplicatedData') + '\n' + filteredData.text, { fun: () => applySampleData(filteredData.deDuplicatedDataList) });
};
const applySampleData = async dataList => {
    dataList.forEach(el => {
        el.cmd = 'I';
        el.unitId = el.id;
        el.checked = true;
    });
    // 예시 팝업 적용 버튼 클릭
    popupDataList.value = [...popupDataList.value, ...dataList];
    originPopupData.value = _.cloneDeep(popupDataList.value);
    await nextTick();
    setTimeout(() => {
        const element = document.getElementById(`list_${popupDataList.value.length - 1}`);
        if (element) {
            element.scrollIntoView({ block: 'center' });
        }
    }, 100);

    unitSamplePopup.value.onClose();
};
const closeSampleDataSetMngPopup = () => {
    // 예시 팝업 닫기 버튼 클릭
    unitSamplePopup.value.onClose();
};
// -----------------------

const openUnitPopup = async () => {
    await searchUnitManagePopup();
    unitManagePopup.value.onOpen();
};

const popupDataList = ref([]);
const originPopupData = ref([]);

const filterUnitManagePopup = text => {
    const filteredData = originPopupData.value.filter(el => el.unitNm?.toLowerCase().includes(text.toLowerCase()) || el.ordSeq == text);

    popupDataList.value = filteredData;
};

const searchUnitManagePopup = async () => {
    const searchParam = {
        compId: compId
    };
    const data = await getMsdsUnit(searchParam, true);
    popupDataList.value = data.list;
    originPopupData.value = data.list;
};

const sampleUnitManagePopup = async () => {
    unitSamplePopup.value.onOpen();
    // const searchParam = {
    //     compId: compId
    // };
    // const data = await getMsdsUnitDataset(searchParam, true);
    // console.log(data);
    // popupDataList.value.push(
    //     ...data.list.map(item => {
    //         return {
    //             ...item,
    //             checked: true
    //         };
    //     })
    // );
};

const addUnitManagePopup = () => {
    const newData = {
        cmd: 'I',
        compId: compId,
        unitId: '',
        unitNm: '',
        ordSeq: '',
        useYn: 'Y',
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

const deleteUnitManagePopup = () => {
    if (checkedList.value.length > 0) {
        confirmMsg('info', t('msgDelete'), '', { fun: deleteActionJbrpManagePopup, param: checkedList.value });
    } else {
        alertMsg('warning', t('msgNoItem'));
    }
};

const deleteActionJbrpManagePopup = checkedList => {
    openLoading();
    deleteMsdsUnit(checkedList, true)
        .then(res => {
            endLoading();
            searchUnitManagePopup();
        })
        .catch(() => {
            endLoading();
        });
};

const saveUnitManagePopup = async () => {
    console.log('# checkedList.value', checkedList.value);
    if (checkedList.value.length === 0) {
        alertMsg('warning', t('msgNoItem'));
        return;
    }
    const isValid = await validationStore.validateAllFields('dataSetPopupForm', true);
    if (isValid) {
        confirmMsg('info', t('msgSave'), '', { fun: saveActionUnitManagePopup, param: checkedList.value });
        return;
    }
};

const saveActionUnitManagePopup = checkedList => {
    openLoading();
    insertMsdsUnit(checkedList, true)
        .then(res => {
            endLoading();
            searchUnitManagePopup();
        })
        .catch(() => {
            endLoading();
        });
};

const closeUnitManagePopup = () => {
    unitManagePopup.value.onClose();
};

onBeforeMount(async () => {
    msdsStore.filteredByWpMsdsListBySearch = {};
    await msdsStore.initData();
});

onMounted(async () => {
    // await msdsStore.initData();

    msdsStore.btnSearch(true);
});

const searchparamList = ref([]);

//-----------------------------------------------  //체크박스 데이터
const handleEvent = e => {
    //체크된 데이터를 checkedList 에 담음

    if (e.checked) {
        msdsStore.checkedList.push(e);
        const searchParam = {
            // 검색 키워드
            searchText: e.msdsId,
            // 검색 구분 0: 화학물질명, 1: CAS No.
            searchCd: 1,
            // RowNum
            searchCd2: 10,
            // pageNum
            searchCd3: 1
        };
        searchparamList.value.push(searchParam);
        console.log(searchparamList.value);
    } else {
        msdsStore.checkedList = msdsStore.checkedList.filter(item => item.msdsId !== e.msdsId);
        searchparamList.value = searchparamList.value.filter(item => item.searchText !== e.msdsId);
        console.log(searchparamList.value);
    }
};
</script>
