<template>
    <!-- 콘텐츠 영역 -->
    <!-- id="form" -->
    <div class="contents df fdc form ui" :key="forceRerenderKey" v-if="!loading">
        <OverlayScrollbarsComponent
            ref="scrollbarRef"
            class="h100pscrollbarRef bcFFFFFF br4px bd1pxsolidE1E6ED"
            :options="{
                scrollbars: {
                    autoHide: 'hover',
                    x: 'hidden',
                    y: 'visible'
                }
            }"
        >
            <!-- 결재란 -->
            <div class="px2-2rem pt4rem">
                <ISignature ref="signatureComponent" :cmd="legalManageDetailStore.searchCmd" targetType="LGM" :writeYear="legalManageDetailStore.filteredLegalManageList.writeYear" :docNo="legalManageDetailStore.filteredLegalManageList.docNo" :useYn="legalManageDetailStore.filteredLegalManageList.useYn" />
            </div>
            <hr class="h1px mt2-2rem" />
            <div class="pa2-2rem">
                <div class="control-field mb2-2rem lgm">
                    <div class="row flex gutters1rem">
                        <div class="grid12-3 sm-grid12-6 es-grid12-12">
                            <div class="field">
                                <label for="">
                                    <span>작성년도</span>
                                </label>
                                <input id="writeYear" type="text" v-model="legalManageDetailStore.filteredLegalManageList.writeYear" v-calendar="'yyyy'" year class="datepicker w100p radius" :readonly="legalManageStore.searchDate.cmd === 'U'" />
                            </div>
                        </div>

                        <div class="grid12-3 sm-grid12-6 es-grid12-12">
                            <div class="field">
                                <label for="">
                                    <span>문서번호</span>
                                </label>
                                <input class="br4px" id="docNo" v-model="legalManageDetailStore.filteredLegalManageList.docNo" v-input type="text" readonly />
                            </div>
                        </div>

                        <div class="grid12-3 sm-grid12-6 es-grid12-8">
                            <div class="field">
                                <label for="">
                                    <span>등록일자</span>
                                </label>
                                <input id="createdAt" :value="formatDate(legalManageDetailStore.filteredLegalManageList.createdAt)" v-input type="text" v-calendar="getDateFormat()" class="datepicker w100p radius" readonly />
                            </div>
                        </div>

                        <div class="grid12-2 sm-grid12-6 es-grid12-4">
                            <div class="field">
                                <label for="useYn">사용여부</label>
                                <div class="df aic h4-4rem">
                                    <input v-input="'사용'" type="checkbox" class="df switch" :checked="legalManageDetailStore.filteredLegalManageList.useYn === 'Y'" @change="legalManageDetailStore.toggleUseYn" :disabled="legalManageDetailStore.filteredLegalManageList.confirmedYn === 'Y'" />
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <div id="form" class="control-field ui form mb2-2rem lgm">
                    <div class="row flex gutters1rem">
                        <div class="grid12-6 ul-grid12-6 es-grid12-12">
                            <div class="field">
                                <label for="legalNm" required>
                                    <span>{{ t('legalNm') }}</span>
                                </label>
                                <i-chips :chips="legalManageDetailStore.legalTypeList" @popup="openLegal" @removeChip="removeArticleTitle" :readonly="legalManageStore.searchDate.cmd === 'U'" :required="!isLegalManageTypePopupOpen"></i-chips>
                            </div>
                        </div>

                        <div class="grid12-2 ul-grid12-6 es-grid12-12">
                            <div class="field h100p cp df aife">
                                <button type="button" class="w100p btn radius active us-w100p" @click="legalOpenAPIClick" :disabled="legalManageDetailStore.filteredLegalManageList.legalCd === null || legalManageDetailStore.filteredLegalManageList.legalCd === '' || legalManageDetailStore.filteredLegalManageList.confirmedYn === 'Y'">
                                    <span>Open API 적용</span>
                                </button>
                            </div>
                        </div>

                        <div class="grid12-2 ul-grid12-6">
                            <div class="field">
                                <label for>구분</label>
                                <select name id="divFg" class="br4px" v-model="legalManageDetailStore.filteredLegalManageList.divFg" :disabled="legalManageDetailStore.filteredLegalManageList.confirmedYn === 'Y'">
                                    <option v-for="item in legalManageDetailStore.selectDivList" :key="item.minorCd" :value="item.minorCd">{{ item.minorNm }}</option>
                                </select>
                            </div>
                        </div>

                        <div class="grid12-2 ul-grid12-6">
                            <div class="field">
                                <label for="revisionAt" required>
                                    <span>{{ t('revisionAt') }}</span>
                                </label>
                                <input :required="!isLegalManageTypePopupOpen" id="revisionAt" :value="formatDate(legalManageDetailStore.filteredLegalManageList.revisionAt)" @input="revisionAtChanged" v-input type="text" v-calendar="getDateFormat()" class="datepicker w100p radius" :disabled="legalManageDetailStore.filteredLegalManageList.confirmedYn === 'Y'" />
                            </div>
                        </div>
                    </div>
                </div>

                <div class="control-field mb2-2rem">
                    <div class="row flex gutters1rem">
                        <div class="grid12-6 el-grid12-12 lg-grid12-12 es-grid12-12">
                            <div class="field">
                                <label for="">
                                    <span>통합검색</span>
                                </label>
                                <div class="df aic">
                                    <input id="searchTerm" v-input type="text" class="radius w100p search" placeholder="검색어를 입력하세요" v-model="legalManageDetailStore.searchTerm" @keyup.enter="legalManageDetailStore.applyFilter" />
                                    <button type="submit" class="shrink0" @click.stop="legalManageDetailStore.applyFilter">
                                        <img src="/assets/img/common/icon_search.svg" alt />
                                    </button>
                                </div>
                            </div>
                        </div>

                        <div class="grid12-3 el-grid12-4 lg-grid12-6 es-grid12-12">
                            <div class="field h100p cp df aife">
                                <button class="w100p h4-4rem fs1-6rem pa1-2rem bcF5F6FE br4px c3248F6 bd1pxsolid3248F6" @click="linkPageUp">국가법령정보센터 바로가기</button>
                            </div>
                        </div>
                        <div class="grid12-3 el-grid12-4 lg-grid12-6 es-grid12-12">
                            <div class="field h100p cp df aife">
                                <button type="button" class="w100p btn radius active us-w100p" @click="goComplianceEvaluationTable">법규 준수평가표 화면으로 이동</button>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- 아코디언 영역 (이 화면은 디자인 착수가 필요한 화면입니다. 작업을 위해 우선 디자인없이 마크업만 진행하겠습니다.) -->
                <div class="accordion form ui df fdc gap gap1-2rem">
                    <!-- 1 -->
                    <div v-for="(item, index) in legalManageDetailStore.filteredLegalManageDetailList" :key="index" class="list lgmd" :class="{ disabled: item.useYn === 'N' }">
                        <button type="button" class="df jcsb aic" @click="toggleAccordion(index)" :class="[{ active: legalManageDetailStore.legalManageDetailSegments[index] }]">
                            <!-- 임시 변경 예정 -->
                            <div class="init df aic gap2rem es-w90p">
                                <div @click.stop>
                                    <input type="checkbox" v-input v-model="legalManageDetailStore.filteredLegalManageDetailList[index].checked" />
                                </div>
                                <em class="ellipsis">{{ legalManageDetailStore.filteredLegalManageDetailList[index].articleTitle }}</em>
                            </div>
                            <svg class="shrink0" xmlns="http://www.w3.org/2000/svg" width="10" height="6" viewBox="0 0 10 6" fill="none">
                                <path d="M9 1L5.4714 4.79026C5.21106 5.06991 4.78894 5.06991 4.5286 4.79026L1 1" stroke="black" stroke-linecap="round" />
                            </svg>
                        </button>

                        <div ref="accordionRefs" class="segment oh">
                            <!-- 아코디언 래핑 요소 -->
                            <div class="pa2-2rem bcF8F9FB">
                                <div class="control-field ui mb2-2rem" :id="item.checked ? 'formDetail' + index : null">
                                    <div class="row flex gutters1rem">
                                        <div class="grid12-12">
                                            <div class="df aic gap1rem">
                                                <div class="field fg1">
                                                    <label :for="'articleTitle' + index" required>
                                                        <span>{{ t('articleTitle') }}</span>
                                                    </label>
                                                    <!-- v-model -->
                                                    <input class="br4px" :required="!isLegalManageTypePopupOpen" :id="'articleTitle' + index" :value="legalManageDetailStore.filteredLegalManageDetailList[index].articleTitle" v-input type="text" placeholder="제2조2항해당 조항을 입력하세요." @input="legalManageDetailStore.filteredLegalManageDetailList[index].articleTitle = $event.target.value" :disabled="legalManageDetailStore.filteredLegalManageList.confirmedYn === 'Y'" />
                                                </div>

                                                <div class="field">
                                                    <label for="useYn">사용여부</label>
                                                    <div class="df aic h4-4rem">
                                                        <input v-input="'사용'" type="checkbox" class="df switch" :checked="legalManageDetailStore.filteredLegalManageDetailList[index].useYn === 'Y'" @change="legalManageDetailStore.toggleDetailUseYn($event, index)" :disabled="legalManageDetailStore.filteredLegalManageList.confirmedYn === 'Y'" />
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <div class="field mb2-2rem">
                                    <div class="field">
                                        <label>법규 내용</label>
                                        <textarea class="br4px minh20rem" rows="5" placeholder="법규 내용을 입력하세요." v-model="legalManageDetailStore.filteredLegalManageDetailList[index].articleContent" :disabled="legalManageDetailStore.filteredLegalManageList.confirmedYn === 'Y'"></textarea>
                                    </div>
                                </div>

                                <div class="field">
                                    <div class="control-field ui form mb2-2rem">
                                        <div class="row flex gutters2rem">
                                            <div class="grid12-12 sm-grid12-6 es-grid12-12 df fdc">
                                                <div class="field">
                                                    <label class="fs1-5rem pb2rem">비고</label>
                                                    <textarea class="br4px minh7rem" v-model="legalManageDetailStore.filteredLegalManageDetailList[index].remarkDc" rows="3" :placeholder="legalManageDetailStore.filteredLegalManageList.confirmedYn === 'Y' ? '' : '비고를 입력하세요.'" :disabled="legalManageDetailStore.filteredLegalManageList.confirmedYn === 'Y'"></textarea>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="form ui tac">
                    <button type="button" class="w80px pa2rem" v-button @click="btnAddBottom" :disabled="legalManageDetailStore.filteredLegalManageList.confirmedYn === 'Y'">
                        <span>
                            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none">
                                <rect x="0.5" y="0.5" width="23" height="23" rx="11.5" fill="#EBEDFF" />
                                <rect x="0.5" y="0.5" width="23" height="23" rx="11.5" stroke="#3248F6" />
                                <path d="M17 12L7 12M12 17L12 7" stroke="#3248F6" stroke-linecap="round" />
                            </svg>
                        </span>
                    </button>
                </div>
            </div>

            <teleport to="body">
                <i-PopupDialog ref="peoplePopup">
                    <!-- 단일 그리드 -->
                    <div class="contents w500px md-w100p">
                        <selectUser @close="closePeoplePopup"></selectUser>
                        <div class="form ui tar mt2-5rem">
                            <button type="button" class="btn w80px radius active" v-button @click="selectPeople">
                                <span>저장</span>
                            </button>
                            <button type="button" v-button class="btn w80px radius ml4px bright-grey" @click="closePopup">
                                <span>{{ t('close') }}</span>
                            </button>
                        </div>
                    </div>
                </i-PopupDialog>

                <i-PopupDialog ref="legalPopup">
                    <!-- 단일 그리드 -->
                    <div class="contents w500px md-w100p">
                        <base-select-popup :title="'법규'" filterKey="legalNm" uniqueKey="legalId" useYnKey="useYn" :excluded-value="'N'" :single="true" :fetch-data="getLegalManageTypeList" :fetch-param="param" @close="closeLegal" :selectedIdList="legalManageDetailStore.legalTypeList?.map(el => el.id)" />
                    </div>
                </i-PopupDialog>

                <i-PopupDialog ref="legalOpenAPIPopup">
                    <!-- 단일 그리드 -->
                    <div class="contents form ui w70rem md-w100p">
                        <LegalManageAPIPopup :title="legalManageDetailStore.filteredLegalManageList.legalNm" :datasetYn="'Y'" :popupDataList="popupAPIDataList" :selectedList="legalManageDetailStore.filteredLegalManageDetailList.filter(item => item.refId != null)" @filter="filterLegalAPIPopup" @search="searchLegalAPIPopup" @save="addLegalAPIPopup" @close="closeLegalAPIPopup"> </LegalManageAPIPopup>
                    </div>
                </i-PopupDialog>
            </teleport>
        </OverlayScrollbarsComponent>
    </div>
</template>

<script setup>
import { ref, computed } from 'vue';
import { nextTick } from 'vue';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import selectUser from '@/views/base/member/SelectUser/Index.vue';
import { onBeforeRouteLeave, useRoute } from 'vue-router';
import router from '@/router/index.js';
import _ from 'lodash';
import ISignature from '@/components/common/iSignature.vue';
import { getDateFormat } from '@/utils/dateUtil.js';
import BaseView from '@/components/base/BaseView';
const { t, onMounted, btnBack, btnSave, btnAdd, btnSearch, btnDelete, btnCopy, btnDownload, validationStore, openLoading, getCompId, confirmMsg, endLoading, alertMsg, toastPopup, formatDate, getCurrentDate, setRouterParam, getDuplicatedData, formatDateForDB } = BaseView();

import { useButtonListStore } from '@/stores/buttonList';
const buttonListStore = useButtonListStore();
buttonListStore.useBtnList = ['btnBack', 'btnSearch', 'btnAdd', 'btnSave', 'btnDelete', 'btnCopy', 'btnDownload'];
import { useLegalManageStore } from '@/stores/safewiz/planning/LegalManage';
import { useLegalManageDetailStore } from '@/stores/safewiz/planning/LegalManageDetail';
import { getLegalManageTypeList, getLegalList, getValidLegalDivFg } from '@/stores/safewiz/planning/api/LegalManageApi.js';
import LegalManageAPIPopup from '@/views/safewiz/planning/popup/LegalManageAPIPopup.vue';
import BaseSelectPopup from '@/views/system/base/popup/BaseSelectPopup.vue';
const legalManageStore = useLegalManageStore();
const legalManageDetailStore = useLegalManageDetailStore();

const loading = ref(true);
const beRouter = async () => {
    // router.go(-1);
    router.push('LegalManage');
    return;
};

const accordionRefs = ref([]);
const scrollbarRef = ref();

onBeforeRouteLeave(async () => {
    await legalManageDetailStore.clearData();
    legalManageDetailStore.searchCmd = '';
    await nextTick();
});
const route = useRoute();
onMounted(async () => {
    const param = setRouterParam(); // 라우터에 담긴 정보 호출, 있으면 key value 형식, 없으면 null
    if (param) {
        // 상세보기 or 라우터 이동인 경우 state를 전달받음
        await legalManageStore.btnDetail(param, param.writeYear);
        legalManageStore.searchDate.writeYear = param.writeYear;
        legalManageStore.searchDate.docNo = param.docNo;
        legalManageStore.searchDate.docType = param.docType;
        legalManageStore.searchDate.cmd = 'U';
        legalManageDetailStore.accordionRefs.value = accordionRefs.value;
    } else if (!legalManageStore.cmd && legalManageStore.filteredLegalManageList.length < 1) {
        // 새로고침이거나 데이터가 소실된 경우 카드 뷰로 강제 이동
        router.push('LegalManage');
        return;
    } else {
        // 추가버튼으로 왔을 때
        let writeYear = getCurrentDate().substring(0, 4);
        legalManageStore.btnDetail(null, writeYear);
        legalManageDetailStore.legalTypeList = [];
        legalManageDetailStore.filteredLegalManageList.writeYear = writeYear;
        legalManageStore.searchDate.writeYear = writeYear;
        legalManageDetailStore.filteredLegalManageList.cmd = 'I';
        legalManageStore.searchDate.cmd = 'I';
        buttonListStore.useBtnList = ['btnBack', 'btnAdd', 'btnSave'];
    }

    legalManageDetailStore.userStore.getUserInfo();
    legalManageDetailStore.detailWatchTrigger = true;
    await legalManageDetailStore.clearData();
    await Promise.all([await legalManageDetailStore.selectSearch(), await legalManageDetailStore.searchAction(true)]);

    legalManageDetailStore.detailWatchTrigger = false;
    legalManageDetailStore.originDivFg = legalManageDetailStore.filteredLegalManageList.divFg;
    initLegalTypeList();

    legalManageDetailStore.signature = signatureComponent.value;
    loading.value = false;
});

const toggleAccordion = async index => {
    clearValidationStore();

    legalManageDetailStore.legalManageDetailSegments[index] = !legalManageDetailStore.legalManageDetailSegments[index];

    await nextTick();
    await legalManageDetailStore.accordionSet(index, 0.5);
};

const clearValidationStore = () => {
    validationStore.clearInvalidClasses();
    validationStore.clearAllErrors();
};

// 결재
const signatureComponent = ref();
const legalNm = ref(null);

const linkPageUp = () => {
    window.open('https://www.law.go.kr/');
};

const goComplianceEvaluationTable = () => {
    router.push({
        name: 'ComplianceEvaluationTable'
    });
};
btnAdd(async () => {
    clearValidationStore();
    await legalManageDetailStore.btnAdd(scrollbarRef);
});

const btnAddBottom = async () => {
    legalManageDetailStore.accordionRefs.value = accordionRefs.value;
    clearValidationStore();
    legalManageDetailStore.btnAdd(scrollbarRef);
};

btnBack(() => {
    clearValidationStore();
    legalManageDetailStore.btnBack();
});

btnSearch(async () => {
    clearValidationStore();
    await signatureComponent.value.Search(); // 조회시 서명 항목도 조회 되도록 수정 [JS.SIM 25.03.06]
    legalManageDetailStore.btnSearch();
    initLegalTypeList();
});

btnSave(async () => {
    clearValidationStore();
    legalManageDetailStore.signature = signatureComponent.value;
    await legalManageDetailStore.btnSave();
});

btnDelete(async () => {
    clearValidationStore();
    await legalManageDetailStore.btnDelete();
});

const forceRerenderKey = ref(0);

btnCopy(async () => {
    clearValidationStore();
    await legalManageDetailStore.btnCopy(forceRerenderKey);
});

// 출력
btnDownload(() => {
    clearValidationStore();
    legalManageDetailStore.downloadReport();
});

// 법규명 데이터 적용
const initLegalTypeList = () => {
    legalManageDetailStore.legalTypeList = [
        {
            id: legalManageDetailStore.filteredLegalManageList.legalId,
            name: legalManageDetailStore.filteredLegalManageList.legalNm,
            cd: legalManageDetailStore.filteredLegalManageList.legalCd
        }
    ];
};

// --------------- 법규명 팝업
const legalPopup = ref(null);
const openLegal = () => {
    legalPopup.value.onOpen();
};
const closeLegal = async e => {
    clearValidationStore();
    legalPopup.value.onClose();
    if (e && e.length) {
        const refinedItems = e.map(el => ({
            id: el.legalId,
            name: el.legalNm,
            cd: el.legalCd
        }));

        legalManageDetailStore.legalTypeList = refinedItems;
        legalManageDetailStore.filteredLegalManageList.legalId = refinedItems[0].id;
        legalManageDetailStore.filteredLegalManageList.legalNm = refinedItems[0].name;
        legalManageDetailStore.filteredLegalManageList.legalCd = refinedItems[0].cd;

        // 내규 데이터 유무 확인용 파라미터
        const validLegalDivFgParam = {
            docType: legalManageDetailStore.filteredLegalManageList.docType,
            legalId: legalManageDetailStore.filteredLegalManageList.legalId,
            compId: param.compId
        };

        // api 적용 시 개정으로 설정
        if (legalManageDetailStore.filteredLegalManageList.legalCd !== null && legalManageDetailStore.filteredLegalManageList.legalCd !== '') {
            legalManageDetailStore.filteredLegalManageList.divFg = 'revision';
        } else {
            // 사용자 직접 등록한 내규의 경우 이미 등록한 법규면 개정, 없으면 제정
            const res = await getValidLegalDivFg(validLegalDivFgParam);
            if (res.list.length > 0) {
                legalManageDetailStore.filteredLegalManageList.divFg = 'revision';
            } else {
                legalManageDetailStore.filteredLegalManageList.divFg = 'Initial';
            }
        }
    }
};

// --------------- 법규명 팝업 삭제 시 초기화
const removeArticleTitle = () => {
    legalManageDetailStore.legalTypeList = [];
    legalManageDetailStore.filteredLegalManageList.legalCd = null;
    latestDt.value = getCurrentDate();
};

const param = {
    compId: getCompId()
};

// --------------- open api 적용(안전보건법령) 팝업
const legalOpenAPIPopup = ref(null);
const originPopupAPIData = ref([]);
const popupAPIDataList = ref([]);
const latestDt = ref(null);
const checkedAPIList = computed(() => {
    return popupAPIDataList.value.filter(el => el.checked);
});

// --------------- open api 호출
const legalOpenAPIClick = () => {
    clearValidationStore();
    searchActionLegalAPIPopup(true).then(res => {
        legalOpenAPIPopup.value.onOpen();
    });
};

// --------------- 검색
const filterLegalAPIPopup = text => {
    const filteredData = originPopupAPIData.value.filter(el => el.title.toLowerCase().includes(text.toLowerCase()) || el.content.toLowerCase().includes(text.toLowerCase()));
    popupAPIDataList.value = filteredData;
};

// --------------- 조회
const searchLegalAPIPopup = () => {
    if (checkedAPIList.value.length > 0) {
        confirmMsg('info', t('msgSaveContinue'), '', { fun: searchActionLegalAPIPopup, param: true });
    } else {
        searchActionLegalAPIPopup(true);
    }
};

const searchActionLegalAPIPopup = notify => {
    openLoading();
    return new Promise(resolve => {
        const searchParams = {
            // 검색 키워드
            searchText: ' ',
            // 검색 구분 1:산업안전보건법, 2:산업안전보건법 시행령, 3:산업안전보건법 시행규칙, 4:산업안전보건 기준에 관한 규칙, 8:중대재해처벌법, 9:중대재해처벌법 시행령, 11:유해·위험작업의 취업 제한에 관한 규칙
            searchCd: legalManageDetailStore.legalTypeList[0].cd,
            // RowNum
            searchCd2: 1,
            // PageNum
            searchCd3: 1
        };

        getLegalList(searchParams, notify)
            .then(res => {
                const legalList = res.items.item || [];
                popupAPIDataList.value = legalList.map(item => ({
                    ...item,
                    checked: false
                }));
                latestDt.value = res.latestDt;
                endLoading();
                resolve({ result: popupAPIDataList.value, success: res.success });
            })
            .catch(() => {
                endLoading();
            })
            .finally(() => {
                originPopupAPIData.value = _.cloneDeep(popupAPIDataList.value);
                setTimeout(() => {
                    const element = document.getElementById('list_0');
                    if (element) {
                        element.scrollIntoView({ block: 'center' });
                    }
                }, 100);
            });
    });
};

// --------------- 적용
const addLegalAPIPopup = selectedItems => {
    // 법규 업데이트 여부 체크
    const updateItem = selectedItems.filter(item => item.update);

    if (updateItem.length > 0) {
        let msg = '업데이트된 데이터를 확인하세요.\n';
        updateItem.forEach(item => {
            msg += `- ${item.title}\n`;
        });

        confirmMsg('info', '이미 등록된 데이터 중 업데이트된 조항이 있습니다.\n최신 정보로 반영하시겠습니까?', msg, { fun: addActionLegalAPIPopup, param: selectedItems });
        return;
    }

    confirmMsg('info', 'Open API를 적용하시겠습니까?', '', { fun: addActionLegalAPIPopup, param: selectedItems });
};

const addActionLegalAPIPopup = async selectedItems => {
    legalManageDetailStore.accordionRefs.value = accordionRefs.value;
    legalManageDetailStore.detailWatchTrigger = true;

    const existingItems = legalManageDetailStore.filteredLegalManageDetailList;
    const newItems = [];

    selectedItems.forEach(item => {
        // 기존 데이터에서 동일한 refId를 가진 항목 찾기
        const existingIndex = existingItems.findIndex(existing => existing.refId === item.doc_id);

        if (existingIndex !== -1) {
            // 이미 존재하는 데이터
            const existingItem = existingItems[existingIndex];

            if (item.update) {
                // 업데이트가 필요한 경우는 기존 데이터 갱신
                existingItem.articleTitle = item.title;
                existingItem.articleContent = item.content;
                existingItem.checked = true;
                existingItem.cmd = existingItem.cmd === 'I' ? 'I' : 'U';
            }
        } else {
            // 새로운 데이터 추가
            const addData = {
                articleTitle: item.title,
                articleContent: item.content,
                useYn: 'Y',
                checked: true,
                cmd: 'I',
                docType: 'LGM',
                refId: item.doc_id
            };

            newItems.push(addData);
        }
    });

    // 새로운 데이터를 맨 앞에 추가
    if (newItems.length > 0) {
        legalManageDetailStore.filteredLegalManageDetailList.unshift(...newItems);
        legalManageDetailStore.legalManageDetailSegments.unshift(...new Array(newItems.length).fill(false));
    }

    await nextTick();

    popupAPIDataList.value.forEach(el => (el.checked = false));
    legalManageDetailStore.filteredLegalManageList.revisionAt = latestDt.value || getCurrentDate();

    legalManageDetailStore.detailWatchTrigger = false;

    toastPopup('적용성공', '적용되었습니다.', 'info');
    closeActionLegalAPIPopup();
};

// --------------- 닫기
const closeLegalAPIPopup = () => {
    if (checkedAPIList.value.length > 0) {
        confirmMsg('info', t('msgSaveContinue'), '', { fun: closeActionLegalAPIPopup, param: '' });
    } else {
        closeActionLegalAPIPopup();
    }
};
const closeActionLegalAPIPopup = () => {
    popupAPIDataList.value = [];
    legalOpenAPIPopup.value.onClose();
};

const revisionAtChanged = e => {
    legalManageDetailStore.filteredLegalManageList.revisionAt = e.target.value;
    console.log('revisionAt', legalManageDetailStore.filteredLegalManageList.revisionAt);
};
</script>
<!-- ⬇️ 의미 없는 코드 -->
<!-- 
<style>
.lgmd {
    &.disabled {
        --cardActiveColor: #cbcfd6;

        /*pointer-events: none;*/
        background: #ecf0f4;
        color: rgba(0, 0, 0, 0.4);
        box-shadow: inset 0 0 0 2px var(--cardLineColor);

        input[type='text'] {
            background: none;
            color: inherit;
            pointer-events: none; /* 비활성화 시 입력 불가능 */
        }

        textarea {
            background: none;
            color: inherit;
            opacity: 0.5; /* 텍스트 불투명도를 조정할 수 있습니다. */
            pointer-events: none; /* 비활성화 시 입력 불가능 */
        }

        button {
            background: #ecf0f4; /* 비활성화된 배경색 */
            color: rgba(0, 0, 0, 0.4); /* 비활성화된 텍스트 색상 */
        }

        textarea[type='text'] {
            background: none;
            color: inherit;
        }

        button.btn {
            color: #fff;
            pointer-events: none; /* 비활성화 시 입력 불가능 */
        }

        .control-field .field input {
            background: none;
            color: inherit;
            pointer-events: none; /* 비활성화 시 입력 불가능 */
        }

        .admin {
            i {
                position: relative;
                overflow: hidden;

                img {
                    filter: grayscale(1);
                    opacity: 0.8;
                }

                &::after {
                    display: inline-block;
                    width: 100%;
                    height: 100%;
                    left: 0;
                    top: 0;
                    background: rgba(0, 0, 0, 0.2);
                }

                &.profile::after {
                    display: none;
                }
            }
        }

        .input:not(.switch) {
            input[type='checkbox'] {
                display: none;

                & + label {
                    border-color: #cbcfd6;
                    background-image: url('data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMTQiIGhlaWdodD0iMTQiIHZpZXdCb3g9IjAgMCAxNCAxNCIgZmlsbD0ibm9uZSIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj4KPHBhdGggZD0iTTExLjY2NjYgNC4wODMzN0w1LjY2MjQgMTAuMDg3NkM1LjQzNDU5IDEwLjMxNTQgNS4wNjUyNSAxMC4zMTU0IDQuODM3NDQgMTAuMDg3NkwyLjMzMzI1IDcuNTgzMzciIHN0cm9rZT0iI0NCQ0ZENiIgc3Ryb2tlLXdpZHRoPSIxLjUiIHN0cm9rZS1saW5lY2FwPSJyb3VuZCIvPgo8L3N2Zz4K');
                }
            }
        }

        dl dt::before {
            background: rgba(0, 0, 0, 0.4);
        }
    }
}

.lgm {
    &.disabled {
        --cardLineColor: #e1e6ed;
        --cardActiveColor: #cbcfd6;

        input[type='text'] {
            background: none;
            color: inherit;
            pointer-events: none; /* 비활성화 시 입력 불가능 */
        }

        select {
            background: none;
            color: inherit;
            pointer-events: none; /* 비활성화 시 입력 불가능 */
        }

        textarea {
            background: none;
            color: inherit;
            opacity: 0.5; /* 텍스트 불투명도를 조정할 수 있습니다. */
            pointer-events: none; /* 비활성화 시 입력 불가능 */
        }

        button {
            background: #ecf0f4; /* 비활성화된 배경색 */
            color: rgba(0, 0, 0, 0.4); /* 비활성화된 텍스트 색상 */
        }

        textarea[type='text'] {
            background: none;
            color: inherit;
        }

        button.btn {
            color: #fff;
            pointer-events: none; /* 비활성화 시 입력 불가능 */
        }

        .admin {
            i {
                position: relative;
                overflow: hidden;

                img {
                    filter: grayscale(1);
                    opacity: 0.8;
                }

                &::after {
                    display: inline-block;
                    width: 100%;
                    height: 100%;
                    left: 0;
                    top: 0;
                    background: rgba(0, 0, 0, 0.2);
                }

                &.profile::after {
                    display: none;
                }
            }
        }

        .input:not(.switch) {
            input[type='checkbox'] {
                display: none;

                & + label {
                    border-color: #cbcfd6;
                    background-image: url('data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMTQiIGhlaWdodD0iMTQiIHZpZXdCb3g9IjAgMCAxNCAxNCIgZmlsbD0ibm9uZSIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj4KPHBhdGggZD0iTTExLjY2NjYgNC4wODMzN0w1LjY2MjQgMTAuMDg3NkM1LjQzNDU5IDEwLjMxNTQgNS4wNjUyNSAxMC4zMTU0IDQuODM3NDQgMTAuMDg3NkwyLjMzMzI1IDcuNTgzMzciIHN0cm9rZT0iI0NCQ0ZENiIgc3Ryb2tlLXdpZHRoPSIxLjUiIHN0cm9rZS1saW5lY2FwPSJyb3VuZCIvPgo8L3N2Zz4K');
                }
            }
        }

        dl dt::before {
            background: rgba(0, 0, 0, 0.4);
        }
    }
}
</style> -->
