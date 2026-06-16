<template>
    <div class="contents">
        <div v-if="correctiveActionRequestStore && correctiveActionRequestStore.inputForm" id="form" class="box form ui pr">
            <OverlayScrollbarsComponent
                class="h100p pa2-2rem md-pa1-2rem"
                :options="{
                    scrollbars: {
                        autoHide: 'hover',
                        x: 'hidden',
                        y: 'visible'
                    }
                }"
            >
                <form @submit.prevent ref="myForm">
                    <!-- 문서발행번호 -->
                    <div class="row flex gutters1rem">
                        <div class="grid12-2 es-grid12-12">
                            <div class="field">
                                <label for="">작성년도</label>
                                <input :value="correctiveActionRequestStore.searchParam.writeYear" type="text" class="radius datepicker" v-calendar="'yyyy'" readonly />
                            </div>
                        </div>
                        <div class="grid12-3 es-grid12-12">
                            <div class="field">
                                <label>
                                    <span>{{ t('corrective_docNo') }}</span>
                                </label>
                                <input :value="correctiveActionRequestStore.inputForm.cmd == 'U' ? correctiveActionRequestStore.inputForm.writeYear + '-' + correctiveActionRequestStore.inputForm.docType + '-' + correctiveActionRequestStore.inputForm.docNo : ''" readonly v-input type="text" class="w100p radius" id="docNo" />
                            </div>
                        </div>

                        <div class="grid12-4 es-grid12-12">
                            <div class="field">
                                <!-- 제목 -->
                                <div class="field">
                                    <label for="title" required>
                                        <span>{{ t('corrective_title') }}</span>
                                    </label>
                                    <input v-input v-model="correctiveActionRequestStore.inputForm.title" type="text" class="w100p radius" id="title" required placeholder="제목을 입력해주세요." />
                                </div>
                            </div>
                        </div>
                        <!-- 사용여부 -->
                        <div class="grid12-3 es-grid12-12">
                            <div class="field">
                                <label>
                                    <span>{{ t('corrective_useAt') }}</span>
                                </label>
                                <div class="h4-4rem df aic">
                                    <input v-input="'사용'" type="checkbox" class="df switch" :checked="correctiveActionRequestStore.inputForm.useYn == 'Y'" @change="correctiveActionRequestStore.toggleUseYn" />
                                </div>
                            </div>
                        </div>

                        <div class="grid12-3 es-grid12-6">
                            <!-- 주관조직 -->
                            <div class="field">
                                <label required>
                                    <span>{{ t('corrective_hostOrg') }}</span>
                                </label>
                                <i-chips :chips="correctiveActionRequestStore.ctrlOrgnItem" @popup="addCtrlOrgn" required></i-chips>
                            </div>
                        </div>

                        <!-- 조치조직 -->
                        <div class="grid12-3 es-grid12-6">
                            <div class="field">
                                <label>
                                    <span>{{ t('corrective_actionOrg') }}</span>
                                </label>
                                <i-chips :chips="correctiveActionRequestStore.actionOrgnItem" @popup="addActionOrgn"></i-chips>
                            </div>
                        </div>

                        <!-- 작성일 -->
                        <div class="grid12-3 es-grid12-6">
                            <div class="field">
                                <label>
                                    <span>{{ t('corrective_regDt') }}</span>
                                </label>
                                <input v-input v-model="correctiveActionRequestStore.inputForm.writeDt" :min="correctiveActionRequestStore.searchParam.writeYear + '.01.01'" :max="correctiveActionRequestStore.searchParam.writeYear + '.12.31'" type="text" v-calendar="getDateFormat()" class="datepicker w100p radius" />
                            </div>
                        </div>

                        <!-- 회신요구일 -->
                        <div class="grid12-3 es-grid12-6">
                            <div class="field">
                                <label>
                                    <span>{{ t('corrective_returnDt') }}</span>
                                </label>
                                <input v-input v-model="correctiveActionRequestStore.inputForm.returnDt" type="text" v-calendar="getDateFormat()" class="datepicker w100p radius" />
                            </div>
                        </div>
                        <!-- 부적합 사항(시정조치 요구사항) -->
                        <div class="grid12-12 bcF9FAFF br4px mt2-2rem">
                            <div class="pa2-2rem">
                                <div class="row flex gutters1rem">
                                    <div class="grid12-8 sm-grid12-12">
                                        <div class="field">
                                            <label>
                                                <span>{{ t('corrective_check') }}</span>
                                            </label>
                                            <textarea v-model="correctiveActionRequestStore.inputForm.actionRequestContent" ref="contentTextarea" class="minh15-2rem radius" placeholder="내용을 입력해주세요."></textarea>
                                        </div>
                                    </div>
                                    <div class="grid12-4 sm-grid12-12">
                                        <!-- 결재란 -->
                                        <div class="mt4-4rem sm-mt2-2rem sm-shrink0 sm-minw40rem es-fg1 us-minw100p">
                                            <ISignature :cmd="correctiveActionRequestStore.inputForm.cmd" ref="signatureComponent1" :is-writer="false" :types="signatureType1" :writeYear="correctiveActionRequestStore.inputForm.writeYear" :docNo="correctiveActionRequestStore.inputForm.docNo" targetType="CAR" />
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- 원인 분석 및 재발 방지 대책-->
                        <div class="grid12-12 bcF9FAFF br4px mt2-2rem">
                            <div class="pa2-2rem">
                                <div class="row flex gutters1rem">
                                    <div class="grid12-8 sm-grid12-12">
                                        <div class="field">
                                            <label>
                                                <span>{{ t('corrective_analyticAndPrevent') }}</span>
                                            </label>
                                            <textarea v-model="correctiveActionRequestStore.inputForm.analyticPreventContent" ref="contentTextarea" class="minh15-2rem radius" placeholder="내용을 입력해주세요."></textarea>
                                        </div>
                                    </div>
                                    <div class="grid12-4 sm-grid12-12">
                                        <!-- 결재란 -->
                                        <div class="mt4-4rem sm-mt2-2rem sm-shrink0 sm-minw40rem es-fg1 us-minw100p">
                                            <ISignature :cmd="correctiveActionRequestStore.inputForm.cmd" ref="signatureComponent2" :is-writer="false" :types="signatureType2" :writeYear="correctiveActionRequestStore.inputForm.writeYear" :docNo="correctiveActionRequestStore.inputForm.docNo" targetType="CAR" />
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="grid12-12 bcF9FAFF br4px mt2-2rem">
                            <div class="pa2-2rem">
                                <div class="row flex gutters1rem">
                                    <div class="grid12-8 lg-grid12-12">
                                        <div class="df aic jcsb fww">
                                            <div class="field es-w100p">
                                                <label>
                                                    <span>{{ t('corrective_validate_check') }}</span>
                                                </label>
                                                <div class="h4-4rem df aic fww gap1rem">
                                                    <input type="radio" v-input="'미조치'" v-model="correctiveActionRequestStore.inputForm.resultValidationYn" value="N" />
                                                    <input type="radio" v-input="'조치'" v-model="correctiveActionRequestStore.inputForm.resultValidationYn" value="S" />
                                                    <input type="radio" v-input="'부적합'" v-model="correctiveActionRequestStore.inputForm.resultValidationYn" value="F" />
                                                </div>
                                            </div>
                                            <div class="field es-w100p">
                                                <label>
                                                    <span>{{ t('corrective_checkDt') }}</span>
                                                </label>
                                                <input v-input v-model="correctiveActionRequestStore.inputForm.checkDt" type="text" v-calendar="getDateFormat()" class="datepicker w100p radius" />
                                            </div>
                                        </div>
                                    </div>

                                    <div class="grid12-8 sm-grid12-12">
                                        <!-- 시정조치 결과 유효성 확인-->
                                        <textarea v-model="correctiveActionRequestStore.inputForm.resultValidationContent" ref="contentTextarea" class="minh15-2rem radius" placeholder="내용을 입력해주세요."></textarea>
                                    </div>
                                    <div class="grid12-4 sm-grid12-12">
                                        <!-- 결재란 -->
                                        <div class="sm-mt2-2rem sm-shrink0 sm-minw40rem es-fg1 us-minw100p">
                                            <ISignature :cmd="correctiveActionRequestStore.inputForm.cmd" ref="signatureComponent3" :is-writer="false" :types="signatureType3" :writeYear="correctiveActionRequestStore.inputForm.writeYear" :docNo="correctiveActionRequestStore.inputForm.docNo" targetType="CAR" class="w100" />
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </form>
            </OverlayScrollbarsComponent>

            <!-- 조직 모달 팝업 컴포넌트 시작  -->
            <teleport to="body">
                <i-PopupDialog ref="ctrlPopup">
                    <!-- 단일 그리드 -->
                    <div class="contents form ui w70rem md-w100p">
                        <base-select-popup :title="'조직'" :inputForm="correctiveActionRequestStore.inputForm" filterKey="orgnNm" useYnKey="useYn" :excluded-value="'N'" :single="true" :fetch-data="getOrganization" @close="correctiveActionRequestStore.closeOrgnCtrl" />
                    </div>
                </i-PopupDialog>
                <i-PopupDialog ref="actionPopup">
                    <!-- 단일 그리드 -->
                    <div class="contents form ui w70rem md-w100p">
                        <base-select-popup :title="'조직'" :inputForm="correctiveActionRequestStore.inputForm" filterKey="orgnNm" useYnKey="useYn" :excluded-value="'N'" :single="true" :fetch-data="getOrganization" @close="correctiveActionRequestStore.closeOrgnAction" />
                    </div>
                </i-PopupDialog>
            </teleport>
        </div>
    </div>
</template>

<script setup>
import BaseView from '@/components/base/BaseView';
import { ref } from 'vue';
const { validationStore, onMounted, t, btnSearch, btnBack, btnSave, btnDelete, btnDownload, toastPopup, setRouterParam } = BaseView();
import router from '@/router';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import { getOrganization } from '@/stores/system/base/api/organizationApi.js';
import BaseSelectPopup from '@/views/system/base/popup/BaseSelectPopup.vue';
import { useCorrectiveActionRequestStore } from '@/stores/safewiz/improvement/correctiveActionRequest.js';
import { getDateFormat } from '@/utils/dateUtil.js';

//서명 컴포넌트
import ISignature from '@/components/common/iSignature.vue';
import { useRoute } from 'vue-router';

const correctiveActionRequestStore = useCorrectiveActionRequestStore();
const route = useRoute();
const contentTextarea = ref(null);
const signatureComponent1 = ref();
const signatureComponent2 = ref();
const signatureComponent3 = ref();

onMounted(async () => {
    const param = setRouterParam(); // 라우터에 담긴 정보 호출, 있으면 key value 형식, 없으면 null
    if (param) {
        // 상세보기 or 라우터 이동인 경우 state를 전달받음
        await correctiveActionRequestStore.btnDetail(param.writeYear, param.docNo);
        correctiveActionRequestStore.layoutStore.useBtnList = ['btnBack', 'btnSearch', 'btnSave', 'btnDelete', 'btnDownload'];
    } else if (!correctiveActionRequestStore.inputForm || !correctiveActionRequestStore.inputForm.cmd) {
        // 새로고침이거나 데이터가 소실된 경우 카드 뷰로 강제 이동
        router.push('CorrectiveActionRequest');
        return;
    } else {
        // 추가버튼으로 왔을 때
        correctiveActionRequestStore.layoutStore.useBtnList = ['btnBack', 'btnSave'];
    }

    correctiveActionRequestStore.setRefsCtrl(ctrlPopup);
    correctiveActionRequestStore.setRefsAction(actionPopup);
    correctiveActionRequestStore.signature1 = signatureComponent1.value;
    correctiveActionRequestStore.signature2 = signatureComponent2.value;
    correctiveActionRequestStore.signature3 = signatureComponent3.value;
    await signatureComponent1.value.Search();
    await signatureComponent2.value.Search();
    await signatureComponent3.value.Search();
});

//목록 버튼
btnBack(async () => {
    await signatureComponent1.value.Search();
    await signatureComponent2.value.Search();
    await signatureComponent3.value.Search();
    await correctiveActionRequestStore.changedDataCheck('Back', '', '');
    //router.push({ name: 'CorrectiveActionRequest' });
});

// 조회 버튼
btnSearch(async () => {
    correctiveActionRequestStore.changedDataCheck('Search', correctiveActionRequestStore.inputForm.writeYear, correctiveActionRequestStore.inputForm.docNo);
    await signatureComponent1.value.Search();
    await signatureComponent2.value.Search();
    await signatureComponent3.value.Search();
});

//출력 버튼
btnDownload(async () => {
    await correctiveActionRequestStore.changedDataCheck('Down', correctiveActionRequestStore.inputForm.writeYear, correctiveActionRequestStore.inputForm.docNo);
});

const signatureType1 = ref([
    {
        name: '담당', // UI에 표시될 이름은 여거 작성해주세요
        sysCodeOrdSeq: 1 //시스템 코드의 C0045 코드의 ordSeq 번호와 맵핑 됩니다.
    },
    {
        name: '조직장', // UI에 표시될 이름은 여거 작성해주세요
        sysCodeOrdSeq: 2 //시스템 코드의 C0045 코드의 ordSeq 번호와 맵핑 됩니다.
    }
]);
const signatureType2 = ref([
    {
        name: '담당', // UI에 표시될 이름은 여거 작성해주세요
        sysCodeOrdSeq: 3 //시스템 코드의 C0045 코드의 ordSeq 번호와 맵핑 됩니다.
    },
    {
        name: '조직장', // UI에 표시될 이름은 여거 작성해주세요
        sysCodeOrdSeq: 4 //시스템 코드의 C0045 코드의 ordSeq 번호와 맵핑 됩니다.
    }
]);
const signatureType3 = ref([
    {
        name: '담당', // UI에 표시될 이름은 여거 작성해주세요
        sysCodeOrdSeq: 5 //시스템 코드의 C0045 코드의 ordSeq 번호와 맵핑 됩니다.
    },
    {
        name: '조직장', // UI에 표시될 이름은 여거 작성해주세요
        sysCodeOrdSeq: 6 //시스템 코드의 C0045 코드의 ordSeq 번호와 맵핑 됩니다.
    }
]);

//저장 버튼
btnSave(async () => {
    const isValid = await validationStore.validateAllFields('form', true);
    console.log('@@ isValid', isValid);
    if (isValid) {
        //주관 조직의 validation
        if (correctiveActionRequestStore.ctrlOrgnItem.length == 0) {
            toastPopup('주관조직', '주관조직은 필수값입니다.');
        } else {
            //주관 조직
            if (correctiveActionRequestStore.ctrlOrgnItem.length !== 0) {
                correctiveActionRequestStore.inputForm.ctrlOrgnId = correctiveActionRequestStore.ctrlOrgnItem[0].id;
                correctiveActionRequestStore.inputForm.ctrlOrgnNm = correctiveActionRequestStore.ctrlOrgnItem[0].name;
            }

            //조치 조직
            if (correctiveActionRequestStore.actionOrgnItem.length !== 0) {
                correctiveActionRequestStore.inputForm.actionOrgnId = correctiveActionRequestStore.actionOrgnItem[0].id;
                correctiveActionRequestStore.inputForm.actionOrgnNm = correctiveActionRequestStore.actionOrgnItem[0].name;
            } else {
                correctiveActionRequestStore.inputForm.actionOrgnId = '';
                correctiveActionRequestStore.inputForm.actionOrgnNm = '';
            }

            // if(correctiveActionRequestStore.inputForm.writeDt === ""){
            //   correctiveActionRequestStore.inputForm.value.writeDt = getCurrentDate();
            // }

            await correctiveActionRequestStore.btnSave();
        }
    }
});

btnDelete(async () => {
    await correctiveActionRequestStore.changedDataCheck('Delete', correctiveActionRequestStore.inputForm.writeYear, correctiveActionRequestStore.inputForm.docNo);
    //correctiveActionRequestStore.btnDelete('D');
});

//주관조직
const ctrlPopup = ref(null);
const addCtrlOrgn = () => {
    ctrlPopup.value.onOpen();
};

//조치조직
const actionPopup = ref(null);
const addActionOrgn = () => {
    actionPopup.value.onOpen();
};
</script>
