<template>
    <!-- 콘텐츠 영역 -->
    <div class="contents df fdc">
        <!-- 검색 필드 -->
        <div class="control-field ui form mb1-6rem es-fww">
            <div class="row flex gutters1rem">
                <div class="grid12-3 us-grid12-12">
                    <input v-input type="text" v-calendar="'yyyy'" v-model="actionPlanStore.searchParam.writeYear" class="datepicker w20rem radius es-w100p" year @input="changeWriteYear" />
                </div>
                <div class="grid12-9 us-grid12-12">
                    <div class="df bcffffff">
                        <input v-input type="text" class="radius w100p search" :placeholder="t('placeHolderSearch')" v-model="actionPlanStore.searchTerm" @keyup.enter="actionPlanStore.initData" />

                        <button type="submit" @click="actionPlanStore.initData">
                            <img src="/assets/img/common/icon_search.svg" alt />
                        </button>
                    </div>
                </div>
            </div>
        </div>
        <OverlayScrollbarsComponent
            class="h100p box"
            :options="{
                scrollbars: {
                    x: 'hidden',
                    y: 'visible'
                }
            }"
        >
            <div class="oh">
                <div :key="actionPlanStore.searchParam.writeYear">
                    <div class="pa2rem" v-if="actionPlanStore.dataFilterList && actionPlanStore.dataFilterList.length > 0">
                        <ISignature ref="signatureComponent" :cmd="actionPlanStore.signCmd" :isWriter="false" :targetType="'OBJ'" type="sign" :writeYear="actionPlanStore.searchParam.writeYear" :docNo="actionPlanStore.searchParam.docNo"></ISignature>
                    </div>

                    <hr />

                    <!-- 아코디언 영역 -->
                    <div class="accordion df fdc rg8px pa2rem" v-if="actionPlanStore.dataFilterList && actionPlanStore.dataFilterList.length > 0">
                        <!-- 1 -->
                        <div v-for="(segment, index) in actionPlanStore.dataFilterList" :key="index" class="list wsn">
                            <button type="button" class="radius df jcsb aic form" :id="`accordion-btn_${index}`" @click="actionPlanStore.toggleAccordion" :class="{ active: activeSegments[index] }">
                                <div class="df aic gap1-2rem ui w80p">
                                    <input v-input type="checkbox" v-model="segment.checked" @change="allChecked(index, $event)" :checked="segment.data?.filter(el => el.checked).length === segment.data?.length && segment.data?.length !== 0" @click="actionPlanStore.toggleAccordion" />
                                    <em class="ellipsis fw600">{{ segment.name }}</em>
                                </div>
                                <div class="df aic">
                                    <i class="fs1-5rem mr1-2rem md-dn">
                                        {{ t('objectives_budget_won') }}
                                        <span class="br4px bcF9FAFF pr1rem pl1rem pt4px pb4px ml1-2rem">{{ formatToAmt(segment.totalAmt) }}</span>
                                    </i>
                                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none">
                                        <path d="M16 11L12.4714 14.7903C12.2111 15.0699 11.7889 15.0699 11.5286 14.7903L8 11" stroke="black" stroke-linecap="round" />
                                    </svg>
                                </div>
                            </button>
                            <div ref="accordionRefs" class="segment oh">
                                <!-- 아코디언 래핑 요소 -->
                                <div class="pa2rem bcF8F9FB">
                                    <div class="row flex gutters2rem" :key="segment.data">
                                        <!-- 카드는 가장 초창기에 작업한 구성과 동일합니다. 해당 카드 사용 부탁드립니다. -->
                                        <template v-for="(item, index) in segment.data" :key="index">
                                            <i-card :v-model="item.checked" :modelValue="item.checked" :data="item" :disabled="item.useYn == 'N'" :title="item.actionObjective" @handle="handleEvent" @editor="btnDetail"></i-card>
                                        </template>
                                        <!-- 추가 -->
                                        <div class="grid12-4 ul-grid12-6 lg-grid12-12">
                                            <div class="card h100p df aic jcc cp" @click="actionPlanStore.btnAdd">
                                                <div class="tac">
                                                    <svg width="46" height="46" viewBox="0 0 46 46" fill="none" xmlns="http://www.w3.org/2000/svg">
                                                        <path d="M31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417M26.0875 9.11005L18.5802 16.6173C15.3165 19.881 13.0012 23.9703 11.8817 28.4481L11.519 29.8991C11.406 30.3512 11.8155 30.7607 12.2676 30.6477L13.7186 30.2849C18.1963 29.1655 22.2857 26.8502 25.5494 23.5865L33.0566 16.0792C33.9808 15.155 34.5 13.9016 34.5 12.5946C34.5 9.873 32.2937 7.66669 29.5721 7.66669C28.2651 7.66669 27.0116 8.18588 26.0875 9.11005Z" stroke="black" stroke-width="1.5" />
                                                        <path d="M26.0874 9.11005C27.0116 8.18588 28.265 7.66669 29.572 7.66669C32.2936 7.66669 34.4999 9.873 34.4999 12.5946C34.4999 13.9016 33.9808 15.155 33.0566 16.0792L31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417L26.0874 9.11005Z" stroke="#3248F6" stroke-width="1.5" />
                                                        <path d="M36.4167 38.3333H9.58337" stroke="#3248F6" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
                                                    </svg>

                                                    <span class="db mt1rem fs2rem c999999">{{ t('card_addHseObjectives') }}</span>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="accordion df fdc rg8px pa2rem" v-else>
                        <!-- 추가 -->
                        <div class="grid12-4 ul-grid12-6 lg-grid12-12">
                            <div class="card h100p df aic jcc cp" @click="actionPlanStore.btnAdd(segment)">
                                <div class="tac">
                                    <svg width="46" height="46" viewBox="0 0 46 46" fill="none" xmlns="http://www.w3.org/2000/svg">
                                        <path d="M31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417M26.0875 9.11005L18.5802 16.6173C15.3165 19.881 13.0012 23.9703 11.8817 28.4481L11.519 29.8991C11.406 30.3512 11.8155 30.7607 12.2676 30.6477L13.7186 30.2849C18.1963 29.1655 22.2857 26.8502 25.5494 23.5865L33.0566 16.0792C33.9808 15.155 34.5 13.9016 34.5 12.5946C34.5 9.873 32.2937 7.66669 29.5721 7.66669C28.2651 7.66669 27.0116 8.18588 26.0875 9.11005Z" stroke="black" stroke-width="1.5" />
                                        <path d="M26.0874 9.11005C27.0116 8.18588 28.265 7.66669 29.572 7.66669C32.2936 7.66669 34.4999 9.873 34.4999 12.5946C34.4999 13.9016 33.9808 15.155 33.0566 16.0792L31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417L26.0874 9.11005Z" stroke="#3248F6" stroke-width="1.5" />
                                        <path d="M36.4167 38.3333H9.58337" stroke="#3248F6" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
                                    </svg>

                                    <span class="db mt1rem fs2rem c999999">{{ t('card_addHseObjectives') }}</span>
                                </div>
                            </div>
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
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import router from '@/router';
import { useActionPlanStore } from '@/stores/safewiz/participation/actionPlan.js';
import { useHseObjectivesStore } from '@/stores/safewiz/participation/hseObjectives.js';
import ISignature from '@/components/common/iSignature.vue';
import { useButtonListStore } from '@/stores/buttonList';
import BaseView from '@/components/base/BaseView.js';

const actionPlanStore = useActionPlanStore();
const hseObjectivesStore = useHseObjectivesStore();
const { setRouterParam, ref, onMounted, formatToAmt, t, btnSearch, btnBack, btnAdd, btnDelete, btnDownload } = BaseView();
const layoutStore = useButtonListStore();
layoutStore.useBtnList = ['btnSearch', 'btnBack', 'btnAdd', 'btnDelete', 'btnDownload'];

// 조회 버튼 이벤트 함수
btnBack(() => {
    actionPlanStore.goBack();
    hseObjectivesStore.searchParam.searchText = actionPlanStore.searchParam.writeYear;
});

btnSearch(async () => {
    await actionPlanStore.getActPlanList(true);
    if (actionPlanStore.dataFilterList.length !== 0) {
        await signatureComponent.value.Search(); // 조회시 서명 항목도 조회 되도록 수정 [JS.SIM 25.03.06]
    }
});
btnAdd(() => {
    actionPlanStore.btnAdd('New');
});

btnDelete(() => {
    actionPlanStore.btnDelete();
});
btnDownload(() => {
    actionPlanStore.btnDownload();
});
//-----------------------------------------------
// [onMounted]
onMounted(async () => {
    const param = setRouterParam(); // 라우터에 담긴 정보 호출, 있으면 key value 형식, 없으면 null
    if (param) {
        actionPlanStore.searchParam.writeYear = param.writeYear;
        actionPlanStore.searchParam.docType = param.docType;
        actionPlanStore.searchParam.docNo = param.docNo;
        actionPlanStore.inputForm.writeYear = param.writeYear;
        actionPlanStore.inputForm.docType = param.docType;
        actionPlanStore.inputForm.docNo = param.docNo;
    } else if (!actionPlanStore.writeYear) {
        //현재날짜 세팅
        actionPlanStore.writeYear = actionPlanStore.currentDate.slice(0, 4);
        actionPlanStore.searchParam.writeYear = actionPlanStore.writeYear;
    }
    const success = await Promise.all([actionPlanStore.getActPlanMasterList(false)]);
    if (success) {
        actionPlanStore.searchParam.writeYear = success.writeYear;
        actionPlanStore.searchParam.docNo = success.docNo;
    }
    actionPlanStore.signature = signatureComponent.value;
    // await signatureComponent.value.Search();
});

const changeWriteYear = async () => {
    const success = await actionPlanStore.getActPlanMasterList(false);
    if (success) {
        actionPlanStore.searchParam.writeYear = success.writeYear;
        actionPlanStore.searchParam.docNo = success.docNo;
    }
    await signatureComponent.value.Search();
};
//-----------------------------------------------
//-----------------------------------------------

const signatureComponent = ref();

//-----------------------------------------------
//-----------------------------------------------
// [아코디언 토글]

gsap.registerPlugin(CustomEase);
CustomEase.create('customEase', 'M0,0 C0.9,0 0.2,1 1,1');

const activeSegments = ref({});
const accordionRefs = ref([]);

//-----------------------------------------------
// [체크박스 데이터]
const handleEvent = e => {
    //체크된 데이터를 checkedList 에 담음
    if (e.checked) {
        actionPlanStore.checkedList.push(e);
    } else {
        actionPlanStore.checkedList = actionPlanStore.checkedList.filter(item => item.docSeq !== e.docSeq);
    }
};

//-----------------------------------------------
// [전체 체크박스 데이터]

const allChecked = (index, event) => {
    const isChecked = event?.target?.checked ?? false; // 안전하게 체크 상태를 가져옵니다.
    const { dataFilterList, checkedList } = actionPlanStore;
    const currentSegment = dataFilterList[index];

    currentSegment.data.forEach(el => (el.checked = isChecked));

    if (isChecked) {
        // 중복 없이 추가
        currentSegment.data.forEach(item => {
            if (!checkedList.some(checkedItem => checkedItem === item)) {
                checkedList.push(item);
            }
        });
    } else {
        // 체크 해제 시 제거
        actionPlanStore.checkedList = checkedList.filter(item => !currentSegment.data.includes(item));
    }
};

//-----------------------------------------------
//-----------------------------------------------
// [상세보기 버튼]
const btnDetail = async e => {
    //디테일 조회
    // actionPlanStore.objectiveId = e.objectiveId;
    actionPlanStore.actionKey = e.writeYear + '_' + e.docType + '_' + e.docNo + '_' + e.compId + '_' + e.docSeq;
    actionPlanStore.param = {
        writeYear: e.writeYear,
        docType: e.docType,
        docNo: e.docNo,
        compId: e.compId,
        docSeq: e.docSeq
    };
    await actionPlanStore.getActPlanDetailList(actionPlanStore.param, false);
    actionPlanStore.isInputActive = false;
    //페이지 이동
    router.push({
        name: 'ObjectivesAndActionPlanDetail'
    });
};
</script>
