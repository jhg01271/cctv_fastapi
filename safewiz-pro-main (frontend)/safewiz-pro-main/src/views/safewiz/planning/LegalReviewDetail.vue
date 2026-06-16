<template>
    <!-- 콘텐츠 영역 -->
    <div class="contents" :key="forceRerenderKey" v-if="!loading">
        <div class="box df fdc form ui">
            <OverlayScrollbarsComponent
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
                    <ISignature ref="signatureComponent" :cmd="legalReviewStore.searchDate.cmd" :targetType="legalReviewDetailStore.filteredLegalReviewList.docType" :writeYear="legalReviewDetailStore.filteredLegalReviewList.writeYear" :docNo="legalReviewDetailStore.filteredLegalReviewList.docNo" :useYn="legalReviewDetailStore.filteredLegalReviewList.useYn" />
                </div>
                <div id="form" class="pa2-2rem">
                    <!-- 상단보기 -->
                    <div :class="{ disabled: legalReviewDetailStore.filteredLegalReviewList.useYn === 'N' }">
                        <div class="row flex gutters1rem">
                            <div class="grid12-2 sm-grid12-6 es-grid12-12">
                                <div class="field">
                                    <label for="">
                                        <span>작성년도</span>
                                    </label>
                                    <input id="writeYear" type="text" v-model="legalReviewDetailStore.filteredLegalReviewList.writeYear" v-calendar="'yyyy'" year class="datepicker w100p radius" :readonly="legalReviewStore.searchDate.cmd === 'U'" />
                                </div>
                            </div>

                            <div class="grid12-2 sm-grid12-6 es-grid12-12">
                                <div class="field">
                                    <label for="">
                                        <span>문서번호</span>
                                    </label>
                                    <input id="docNo" class="br4px" v-model="legalReviewDetailStore.filteredLegalReviewList.docNo" v-input type="text" readonly />
                                </div>
                            </div>

                            <div class="grid12-5 sm-grid12-6 es-grid12-12">
                                <div class="field">
                                    <label for="legalReviewNm" required>
                                        <span>제목</span>
                                    </label>
                                    <input id="legalReviewNm" class="br4px" v-model="legalReviewDetailStore.filteredLegalReviewList.legalReviewNm" @change="changeLegalReviewNm" v-input type="text" placeholder="제목을 입력하세요." required />
                                </div>
                            </div>

                            <div class="grid12-2 sm-grid12-6 es-grid12-12">
                                <div class="field">
                                    <label for="">
                                        <span>등록일자</span>
                                    </label>
                                    <input id="createdAt" :value="formatDate(legalReviewDetailStore.filteredLegalReviewList.createdAt)" v-input type="text" v-calendar="getDateFormat()" class="datepicker w100p radius" readonly />
                                </div>
                            </div>

                            <div class="grid12-1 sm-grid12-6 es-grid12-12">
                                <div class="field">
                                    <label for="useYn">사용여부</label>
                                    <div class="df aic h4-4rem">
                                        <input id="useYn" v-input="'사용'" type="checkbox" class="df switch" :checked="legalReviewDetailStore.filteredLegalReviewList.useYn === 'Y'" @change="legalReviewDetailStore.toggleUseYn" />
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="mt2-2rem accordion df fdc rg8px">
                        <div v-for="(segment, index) in legalReviewDetailStore.segments" :key="index" class="list">
                            <button type="button" class="df jcsb aic" @click="toggleAccordion(index)" :class="{ active: legalReviewDetailStore.legalReviewDetailSegments[index] }">
                                <div class="df aic gap8px init">
                                    <input type="checkbox" v-model="segment.dataList[0].checked" v-input @change="checkchange($event, index)" />
                                    <em>{{ segment.legalTitle }}</em>
                                </div>
                                <svg xmlns="http://www.w3.org/2000/svg" width="10" height="6" viewBox="0 0 10 6" fill="none">
                                    <path d="M9 1L5.4714 4.79026C5.21106 5.06991 4.78894 5.06991 4.5286 4.79026L1 1" stroke="black" stroke-linecap="round" />
                                </svg>
                            </button>
                            <div ref="accordionRefs" class="segment oh">
                                <div class="pa2-2rem bcF8F9FB">
                                    <!-- Data Card -->
                                    <!-- class="w100p df mt8px list lgrd" -->
                                    <div v-for="(item, idx) in segment.dataList" :key="idx" :class="{ disabled: item.useYn === 'N' }">
                                        <div class="w100p pa2-2rem bd1pxsolidE1E6ED br5px bcFFFFFF" @click="() => (selectedIndex = index)">
                                            <div class="row flex gutters1rem" :id="item.checked ? 'formDetail' + index : null">
                                                <div class="grid12-6 sm-grid12-6 es-grid12-12">
                                                    <div class="field">
                                                        <label required>
                                                            <span>{{ t('relatedLaws') }}</span>
                                                        </label>
                                                        <!-- v-model -->
                                                        <i-chips :chips="[{ id: item.legalDocNo, name: `${item.legalNm} - ${item.legalArticleNm}` }]" @popup="addLegalReviewPop(index)" @removeChip="removeLegalReview(index)" class="w100p" @change="chkData(item)" required />
                                                    </div>
                                                </div>

                                                <div class="grid12-3 sm-grid12-6 es-grid12-12">
                                                    <div class="field">
                                                        <label :for="'revisionAt' + index" required>
                                                            <span>{{ t('revisionAt') }}</span>
                                                        </label>
                                                        <input :value="formatDate(item.revisionAt)" v-input type="text" v-calendar="getDateFormat()" class="datepicker w100p radius" required :id="'revisionAt' + index" @input="inputdate()" readonly />
                                                    </div>
                                                </div>

                                                <div class="grid12-3 sm-grid12-6 es-grid12-12">
                                                    <div class="field">
                                                        <label for="useYn">사용여부</label>
                                                        <div class="df aic h4-4rem">
                                                            <input v-input="'사용'" type="checkbox" class="df switch" :checked="item.useYn === 'Y'" @change="legalReviewDetailStore.toggleDetailUseYn($event, index)" />
                                                        </div>
                                                    </div>
                                                </div>

                                                <div class="grid12-12 sm-grid12-12 es-grid12-12">
                                                    <div class="field">
                                                        <label class="fs1-5rem pb1rem">법규 내용</label>
                                                        <textarea v-model="item.currentlaws" rows="3" class="br4px minh10rem" readonly></textarea>
                                                    </div>
                                                </div>

                                                <div class="grid12-6 sm-grid12-6 es-grid12-12">
                                                    <div class="field">
                                                        <label class="fs1-5rem pb1rem">해당 시설</label>
                                                        <input class="br4px" v-model="item.facility" v-input type="text" placeholder="해당 시설을 입력하세요." />
                                                    </div>
                                                </div>
                                                <div class="grid12-6 sm-grid12-6 es-grid12-12">
                                                    <div class="field">
                                                        <label class="fs1-5rem pb1rem">조직</label>
                                                        <i-chips :chips="item.legalReviewOrgnList" @popup="addOrgn(index)" @removeChip="removeOrgn(index)" class="w100p" @change="chkData(item)" />
                                                    </div>
                                                </div>

                                                <div class="grid12-12 sm-grid12-6 es-grid12-12">
                                                    <div class="field">
                                                        <label class="fs1-5rem pb1rem">비고</label>
                                                        <textarea v-model="item.remarkDc" rows="3" class="br4px minh10rem" placeholder="비고를 입력하세요."></textarea>
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
                        <button type="button" class="w80px pa2rem" v-button @click="btnAddBottom">
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
            </OverlayScrollbarsComponent>
        </div>
    </div>

    <!-- 조직 지정 팝업 -->
    <teleport to="body">
        <i-PopupDialog ref="orgnPopup">
            <!-- 단일 그리드 -->
            <div class="contents form ui w70rem md-w100p">
                <base-select-popup :title="'조직'" :selectedIdList="legalReviewDetailStore.filteredLegalReviewDetailList[selectIndex].legalReviewOrgnList?.map(el => el.id)" uniqueKey="orgnId" filterKey="orgnNm" useYnKey="useYn" :excluded-value="'N'" :single="false" :fetch-data="getOrganization" @apply="applyOrgn" @close="closeOrgn" />
            </div>
        </i-PopupDialog>
        <i-PopupDialog ref="legalReviewPopup">
            <div class="contents w800px md-w100p">
                <LegalReviewPopup @close="closeLegalReviewPop" @selected="onSelectLawData" />
                <div class="form ui tar mt2-5rem">
                    <button type="button" v-button class="btn w80px radius ml4px bright-grey" @click="closeLegalReviewPop">
                        <span>{{ t('close') }}</span>
                    </button>
                </div>
            </div>
        </i-PopupDialog>
    </teleport>
</template>

<script setup>
import { ref } from 'vue';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import router from '@/router/index.js';
import { getDateFormat } from '@/utils/dateUtil.js';

import BaseView from '@/components/base/BaseView';
const { t, onMounted, confirmMsg, alertMsg, nextTick, btnBack, btnSave, btnAdd, btnSearch, btnDelete, btnCopy, btnDownload, validationStore, onBeforeUnmount, onUnmounted, setRouterParam, formatDate } = BaseView();
import ISignature from '@/components/common/iSignature.vue';
import { useButtonListStore } from '@/stores/buttonList';
const buttonListStore = useButtonListStore();
buttonListStore.useBtnList = ['btnBack', 'btnSearch', 'btnAdd', 'btnSave', 'btnDelete', 'btnCopy', 'btnDownload'];

import { useLegalReviewDetailStore } from '@/stores/safewiz/planning/LegalReviewDetail';
const legalReviewDetailStore = useLegalReviewDetailStore();
import { useLegalReviewStore } from '@/stores/safewiz/planning/LegalReview';
import { onBeforeRouteLeave } from 'vue-router';
import { getOrganization } from '@/stores/system/base/api/organizationApi.js';
import baseSelectPopup from '@/views/system/base/popup/BaseSelectPopup.vue';
import LegalReviewPopup from '@/views/system/base/popup/LegalReviewPopup.vue';
const legalReviewStore = useLegalReviewStore();

import { useLoadingPanelStore } from '@/stores/loadingPanel.js';
const loadingPanelStore = useLoadingPanelStore();

const accordionRefs = ref([]);
const scrollbarRef = ref(null);
const signatureComponent = ref(null);

const loading = ref(true);
const forceRerenderKey = ref(0);
const beRouter = async () => {
    router.push('LegalReview');
    return;
};

onBeforeRouteLeave(async () => {
    await legalReviewDetailStore.clearData();
    legalReviewDetailStore.searchCmd = '';
    await nextTick();
});

onMounted(async () => {
    const param = setRouterParam(); // 라우터에 담긴 정보 호출, 있으면 key value 형식, 없으면 null
    if (param) {
        // 상세보기 or 라우터 이동인 경우 state를 전달받음
        legalReviewStore.btnDetail(param);
        buttonListStore.useBtnList = ['btnBack', 'btnSearch', 'btnSave', 'btnDelete', 'btnCopy', 'btnDownload'];
    } else if (!legalReviewStore.searchDate.cmd) {
        // 새로고침이거나 데이터가 소실된 경우 카드 뷰로 강제 이동
        router.push('LegalReview');
        return;
    } else {
        // 추가버튼으로 왔을 때
        legalReviewDetailStore.selectedSegments = [];
        btnAddBottom();
        buttonListStore.useBtnList = ['btnBack', 'btnAdd', 'btnSave'];
    }

    legalReviewDetailStore.accordionRefs.value = accordionRefs.value;
    legalReviewDetailStore.detailWatchTrigger = true;
    await legalReviewDetailStore.clearData();
    await legalReviewDetailStore.searchAction(true);
    await legalReviewDetailStore.initAccordion();
    legalReviewDetailStore.detailWatchTrigger = false;
    loading.value = false;
});

const btnAddBottom = async () => {
    await legalReviewDetailStore.btnAdd(scrollbarRef);

    // const osInstance = scrollbarRef.value?.osInstance;
    // const element = scrollbarRef.value.$el.querySelectorAll('.list')[0];

    // if (osInstance && element) {
    //     element.scrollIntoView({ behavior: 'auto', block: 'start' });
    // }
};

btnBack(async () => {
    await legalReviewDetailStore.btnBack();
});

btnSearch(async () => {
    await legalReviewDetailStore.btnSearch();
});

btnAdd(async () => {
    await legalReviewDetailStore.btnAdd(scrollbarRef);
});

btnSave(async () => {
    if (await legalReviewDetailStore.btnSave()) {
        await confirmMsg('info', '저장 하시겠습니까?', null, { fun: saveDetail, param: true });
    } else {
        return;
    }
});

btnDelete(() => {
    legalReviewDetailStore.btnDelete();
});

btnCopy(async () => {
    if (await legalReviewDetailStore.btnCopy(forceRerenderKey)) {
        buttonListStore.useBtnList = ['btnBack', 'btnAdd', 'btnSave'];
    }
});

btnDownload(() => {
    legalReviewDetailStore.downloadReport();
});

// 조직 팝업
const orgnPopup = ref(null);
const selectIndex = ref(0);
/* ---------- 조직 팝업 ---------- */
// 조직 팝업 오픈
const addOrgn = index => {
    selectIndex.value = index;
    orgnPopup.value.onOpen();
};

const applyOrgn = e => {
    if (e.length > 0) {
        legalReviewDetailStore.filteredLegalReviewDetailList[selectIndex.value].legalReviewOrgnList = e.map(el => ({ id: el.orgnId, nm: el.orgnNm }));
        legalReviewDetailStore.searchClientGrid(legalReviewDetailStore.filteredLegalReviewDetailList);
    }
    orgnPopup.value.onClose();
};

const closeOrgn = e => {
    orgnPopup.value.onClose();
};

const legalReviewPopup = ref(null);

const addLegalReviewPop = index => {
    selectIndex.value = index;
    validationStore.clearInvalidClasses();
    validationStore.clearAllErrors();
    legalReviewPopup.value.onOpen();
};

const removeLegalReview = async index => {
    await legalReviewDetailStore.resetLegalManageData(index);
};

const removeOrgn = async index => {
    legalReviewDetailStore.filteredLegalReviewDetailList[index].orgnId = '';
    legalReviewDetailStore.filteredLegalReviewDetailList[index].orgnNm = '';
};

const closeLegalReviewPop = () => {
    legalReviewDetailStore.popSearchTerm = '';
    legalReviewPopup.value.onClose();
};

// 데이터 저장
const saveDetail = async () => {
    if (await legalReviewDetailStore.saveAction()) {
        await signatureComponent.value.setApprovalInfo(legalReviewDetailStore.filteredLegalReviewList.docType, legalReviewDetailStore.filteredLegalReviewList.writeYear, legalReviewDetailStore.filteredLegalReviewList.docNo);
        await signatureComponent.value.updateTaskUseYn(legalReviewDetailStore.filteredLegalReviewList.docType, legalReviewDetailStore.filteredLegalReviewList.writeYear, legalReviewDetailStore.filteredLegalReviewList.docNo);
        legalReviewStore.searchDate.cmd = 'U';
        buttonListStore.useBtnList = ['btnBack', 'btnSearch', 'btnAdd', 'btnSave', 'btnDelete', 'btnCopy', 'btnDownload'];
    }
};

const onSelectLawData = async el => {
    // 동일한 법규가 있을 시 return
    const idx = legalReviewDetailStore.checkDupe(el);

    const setLegalData = async () => {
        await legalReviewDetailStore.setLegalManageData(el, selectIndex.value);
        if (idx >= 0) {
            toggleAccordion(idx);
        }
        closeLegalReviewPop();
    };

    if (idx >= 0) {
        selectIndex.value = idx;
        legalReviewDetailStore.legalReviewDetailSegments[idx] = false;

        confirmMsg('warning', '같은 법규가 있습니다. \n 덮어쓰겠습니까?', null, { fun: setLegalData });
    } else {
        await setLegalData(el);
    }
};

const toggleAccordion = async index => {
    legalReviewDetailStore.legalReviewDetailSegments[index] = !legalReviewDetailStore.legalReviewDetailSegments[index];

    await nextTick();
    await legalReviewDetailStore.accordionSet(index, 0.5);
};

// 법규 검토서 메인 제목 변경 확인
const changeLegalReviewNm = () => {
    legalReviewDetailStore.changeLegalMainFlag = true;
};

const checkchange = (event, index) => {
    const isChecked = event.target.checked;

    legalReviewDetailStore.Changecheck(isChecked, index);
    // if (isChecked) {
    //     // 체크된 항목이 없으면 selectedSegments에 추가
    //     if (!legalReviewDetailStore.selectedSegments.includes(index)) {
    //         legalReviewDetailStore.selectedSegments.push(index);
    //     }
    // } else {
    //     // 체크 해제된 항목은 selectedSegments에서 제거
    //     const idx = legalReviewDetailStore.selectedSegments.indexOf(index);
    //     if (idx !== -1) {
    //         legalReviewDetailStore.selectedSegments.splice(idx, 1);
    //     }
    // }

    // // selectedFlag 업데이트
    // legalReviewDetailStore.selectedFlag = legalReviewDetailStore.selectedSegments.length > 0 ? 'Y' : 'N';
};
const inputdate = () => {
    validationStore.clearInvalidClasses();
    validationStore.clearAllErrors();
};
</script>
