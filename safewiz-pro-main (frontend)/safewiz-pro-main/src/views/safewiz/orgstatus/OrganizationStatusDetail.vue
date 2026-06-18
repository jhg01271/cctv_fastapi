<template>
    <div id="form" class="contents">
        <div class="box form ui" v-if="orgnStatusStore.inputForm && orgnStatusStore.inputForm.cmd">
            <OverlayScrollbarsComponent
                ref="overlayScrollbars"
                class="h100p"
                :options="{
                    scrollbars: {
                        autoHide: 'hover',
                        x: 'hidden',
                        y: 'visible'
                    }
                }"
            >
                <!-- <div @submit.prevent ref="myForm"> -->
                <div class="pa2-2rem">
                    <ISignature ref="signatureComponent" :cmd="orgnStatusStore.inputForm.cmd" :types="signatureType" targetType="ORGST" :writeYear="orgnStatusStore.inputForm.writeYear" :docNo="orgnStatusStore.inputForm.docNo" :useYn="orgnStatusStore.inputForm.useYn"></ISignature>
                </div>

                <hr />

                <div class="pa2-2rem">
                    <!-- 작성일 및 조직 선택 -->
                    <div class="row flex gutters1rem">
                        <div class="grid12-3 es-grid12-6">
                            <div class="field">
                                <label for="writeYear" required>
                                    <span>작성년도</span>
                                </label>
                                <input v-input type="text" v-model="orgnStatusStore.inputForm.writeYear" id="writeYear" v-calendar="'yyyy'" class="datepicker w100p radius" year :readonly="orgnStatusStore.inputForm.cmd !== 'I'" />
                            </div>
                        </div>
                        <div class="grid12-3 es-grid12-6">
                            <div class="field">
                                <label for="writeDt" required>
                                    <span>작성일자</span>
                                </label>
                                <input v-input type="text" v-model="orgnStatusStore.inputForm.writeDt" required id="writeDt" class="datepicker w100p radius" v-calendar="getDateFormat()" />
                            </div>
                        </div>
                        <div class="grid12-3 es-grid12-6">
                            <div class="field">
                                <label for required>
                                    <span>조직</span>
                                </label>

                                <i-chips :chips="orgnStatusStore.orgnItem" @popup="addOrgn" required :readonly="orgnStatusStore.inputForm.cmd !== 'I'"></i-chips>
                            </div>
                        </div>
                        <div class="grid12-3 es-grid12-6">
                            <div class="field">
                                <label>사용여부</label>
                                <div class="h4-4rem df aic">
                                    <input type="checkbox" v-input="'사용'" :true-value="'Y'" :false-value="'N'" class="switch" v-model="saveParam.useYn" :checked="saveParam.useYn == 'Y'" @change="orgnStatusStore.inputForm.useYn = saveParam.useYn" />
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- 선택된 경우 아래 .box 요소에 .selected 클래스를 토글 시켜주시면 됩니다. -->
                    <div v-for="(result, index) in resultList" :key="index" :class="['box', 'df', 'mt2-4rem', 'bcF9FAFF', 'sm-db', { selected: result.checked }, { disabled: result.useYn == 'N' }]">
                        <div class="df aic jcc w5rem shrink0 bdr1pxsolidE1E6ED sm-w100p sm-h7rem sm-bdb1pxsolidE1E6ED sm-bdr0pxsolid000000">
                            <input type="checkbox" v-input v-model="result.checked" />
                        </div>
                        <div class="w100pcalc5rem pa2-2rem sm-pa1-5rem sm-w100p">
                            <div class="h4-4rem df aic jcfe">
                                <input @change="chkData(result)" type="checkbox" v-input="'사용'" :true-value="'Y'" :false-value="'N'" class="switch" v-model="result.useYn" :checked="result.useYn == 'Y'" />
                            </div>
                            <OverlayScrollbarsComponent
                                ref="overlayScrollbars"
                                :options="{
                                    scrollbars: {
                                        autoHide: 'hover',
                                        x: 'visible',
                                        y: 'hidden'
                                    }
                                }"
                            >
                                <table class="responsive-type-one tal us-minw35rem">
                                    <tbody>
                                        <!-- 조직의 상황 상세 -->
                                        <tr>
                                            <th class="w20p h7rem tac">
                                                <div class="field">
                                                    <label :for="'gubun' + index" required>
                                                        <span>구분</span>
                                                    </label>
                                                </div>
                                            </th>
                                            <td>
                                                <div class="field">
                                                    <select @change="chkData(result)" name v-select v-model="result.gubun" :required="result.checked" :id="'gubun' + index">
                                                        <option v-for="item in gubunList" :key="item.minorCd" :value="item.minorCd">{{ item.minorNm }}</option>
                                                    </select>
                                                </div>
                                            </td>
                                        </tr>
                                        <tr>
                                            <th class="w20p h7rem tac">
                                                <div class="field">
                                                    <label :for="'workDesc' + index" required>
                                                        <span>업무</span>
                                                    </label>
                                                </div>
                                            </th>
                                            <td>
                                                <div class="field">
                                                    <textarea @input="chkData(result)" v-model="result.workDesc" name :id="'workDesc' + index" class="w100p radius minh10rem" placeholder="업무를 입력하세요." :required="result.checked"></textarea>
                                                </div>
                                            </td>
                                        </tr>
                                        <tr>
                                            <th class="w20p h7rem tac">
                                                <div class="field">
                                                    <label :for="'issue' + index" :required="result.checked">
                                                        <span>이슈</span>
                                                    </label>
                                                </div>
                                            </th>
                                            <td>
                                                <div class="field">
                                                    <textarea @input="chkData(result)" v-model="result.issue" name :id="'issue' + index" class="w100p radius minh10rem" placeholder="이슈를 입력하세요." :required="result.checked"></textarea>
                                                </div>
                                            </td>
                                        </tr>
                                    </tbody>
                                </table>
                            </OverlayScrollbarsComponent>

                            <OverlayScrollbarsComponent
                                ref="overlayScrollbars"
                                :options="{
                                    scrollbars: {
                                        autoHide: 'hover',
                                        x: 'visible',
                                        y: 'hidden'
                                    }
                                }"
                            >
                                <table class="responsive-type-two tal mt1-2rem es-minw50rem">
                                    <thead>
                                        <tr>
                                            <th colspan="2" class="fs1-7rem tac">조직에 대한 영향</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <tr>
                                            <th class="w20p tac">리스크</th>
                                            <th class="w20p tac">기회</th>
                                        </tr>
                                        <tr>
                                            <td>
                                                <textarea @input="chkData(result)" v-model="result.risk" name id class="w100p radius minh10rem" placeholder="리스크를 입력하세요."></textarea>
                                            </td>
                                            <td>
                                                <textarea @input="chkData(result)" v-model="result.chance" name id class="w100p radius minh10rem" placeholder="기회를 입력하세요."></textarea>
                                            </td>
                                        </tr>
                                    </tbody>
                                </table>
                            </OverlayScrollbarsComponent>
                        </div>
                    </div>

                    <div class="df aic jcfe mt1-2rem">
                        <button type="button" @click="addOrgnStatus()" class="btn active radius w19rem">
                            <span class="mr1rem">조직의 상황 신규 추가</span>
                            <svg xmlns="http://www.w3.org/2000/svg" width="12" height="12" viewBox="0 0 12 12" fill="none">
                                <g clip-path="url(#clip0_378_5168)">
                                    <path d="M6 -2V14M14 6L-2 6" stroke="white" stroke-linecap="round" />
                                </g>
                                <defs>
                                    <clipPath id="clip0_378_5168">
                                        <rect width="12" height="12" fill="white" />
                                    </clipPath>
                                </defs>
                            </svg>
                        </button>
                    </div>
                </div>
            </OverlayScrollbarsComponent>
        </div>

        <!-- 인원 검색 -->
        <i-PopupDialog ref="peoplePopup">
            <!-- 단일 그리드 -->

            <div class="contents w50rem md-w100p">
                <selectUser :single="true" @selected="selectPeople" @close="handlePopupClose"></selectUser>

                <div class="form ui tar mt2-5rem">
                    <button type="button" class="btn w80px radius active" v-button @click="btnSave">
                        <span>저장</span>
                    </button>
                    <button type="button" v-button class="btn w80px radius ml4px bright-grey" @click="closePeoplePopup">
                        <span>{{ t('close') }}</span>
                    </button>
                </div>
            </div>
        </i-PopupDialog>

        <!-- 조직 모달 팝업 컴포넌트 시작  -->
        <teleport to="body">
            <i-PopupDialog ref="orgnPopup">
                <!-- 단일 그리드 -->
                <div class="contents form ui w70rem md-w100p">
                    <base-select-popup :title="'조직'" :selectedIdList="orgnStatusStore.inputForm.orgnIdList" :uniqueKey="[orgnStatusStore.inputForm.orgnId]" filterKey="orgnNm" useYnKey="useYn" :excluded-value="'N'" :single="true" :fetch-data="getOrganization" @close="orgnStatusStore.closeOrgn" />
                    <!-- 버튼 콤포넌트 영역 시작 -->
                </div>
            </i-PopupDialog>
        </teleport>
        <!-- 모달 팝업 콤포넌트 끝  -->

        <!-- 사인 팝업 컴포넌트 시작 -->
        <i-PopupDialog ref="signPopup" class>
            <div class="contents w500px md-w100p">
                <VueSignaturePad id="signature" width="100%" height="156px" ref="signaturePad" />
                <div class="form ui tar mt2-5rem">
                    <button type="button" class="btn w80px radius active" v-button @click="saveSign">
                        <span>저장</span>
                    </button>
                    <button type="button" v-button class="btn w80px radius ml4px bright-grey" @click="closePopup">
                        <span>{{ t('close') }}</span>
                    </button>
                </div>
            </div>
        </i-PopupDialog>
        <!-- 사인 팝업 컴포넌트 끝-->
    </div>
</template>
<script setup>
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import { ref, onMounted } from 'vue';
import BaseView from '@/components/base/BaseView';
import { useOrgnStatusStore } from '@/stores/safewiz/orgstatus/organizationStatus.js';
import { getOrganization } from '@/stores/system/base/api/organizationApi.js';
import { useButtonListStore } from '@/stores/buttonList';
import baseSelectPopup from '@/views/system/base/popup/BaseSelectPopup.vue';
import selectUser from '@/views/base/member/SelectUser/Index.vue';
import { getSystemCode } from '@/stores/system/setting/api/SystemCode.js';
import router from '@/router';
import _ from 'lodash';
import { getDateFormat } from '@/utils/dateUtil.js';
const { openLoading, endLoading, setRouterParam, validationStore, t, confirmMsg, alertMsg, getCurrentDate, btnSearch, btnBack, btnAdd, btnSave, btnDelete, btnDownload, getCompId, formatDate, formatDateForDB } = BaseView();
const orgnPopup = ref(null);
const peoplePopup = ref(null);
const signPopup = ref(null);
const signatureComponent = ref();
const signaturePad = ref();
const overlayScrollbars = ref(null);
const layoutStore = useButtonListStore();
const orgnStatusStore = useOrgnStatusStore();
layoutStore.useBtnList = ['btnSearch', 'btnBack', 'btnAdd', 'btnSave', 'btnDelete', 'btnDownload'];

// 구분 목록
const gubunList = ref([]);
const resultList = ref([]);
const originData = ref([]);

const saveParam = ref({
    writeYear: '',
    orgnId: '',
    gubun: '',
    workDesc: '',
    issue: '',
    risk: '',
    chance: '',
    useYn: 'Y',
    writeDt: getCurrentDate()
});
const signatureType = ref([
    {
        name: '작성',
        // 서명 인원 미리 등록 되어 있어야 할 경우 값을 컴포넌트 밖에서 넣어줘야 합니다.
        sysCodeOrdSeq: 1 //SIGNATURE_ENUM code 정보를 넣어주기
    },
    {
        name: '검토',
        // 서명 인원 미리 등록 되어 있어야 할 경우 값을 컴포넌트 밖에서 넣어줘야 합니다.
        sysCodeOrdSeq: 2 //SIGNATURE_ENUM code 정보를 넣어주기
    },
    {
        name: '승인',
        // 서명 인원 미리 등록 되어 있어야 할 경우 값을 컴포넌트 밖에서 넣어줘야 합니다.
        sysCodeOrdSeq: 3 //SIGNATURE_ENUM code 정보를 넣어주기
    }
]);
// 조회
btnSearch(async () => {
    await signatureComponent.value.Search(); // 조회시 서명 항목도 조회 되도록 수정 [JS.SIM 25.03.06]

    const isModified = originData.value.some(originItem => {
        // 동일한 docSeq를 가진 항목 찾기
        const matchingItem = resultList.value.find(currentItem => currentItem.docSeq === originItem.docSeq);

        // gubun, issue, workDesc 값이 하나라도 다르면 수정된 것으로 간주
        return matchingItem && (originItem.gubun !== matchingItem.gubun || originItem.issue !== matchingItem.issue || originItem.workDesc !== matchingItem.workDesc || originItem.risk !== matchingItem.risk || originItem.chance !== matchingItem.chance);
    });
    // resultList.value에서 cmd 값이 'I'인 항목이 하나라도 있으면 수정된 것으로 간주
    const hasInsertedItems = resultList.value.some(item => item.cmd === 'I');

    if (isModified || hasInsertedItems) {
        confirmMsg('warning', '저장되지 않은 정보가 있습니다. \n 그래도 계속하시겠습니까?', null, { fun: searchDetail, param: orgnStatusStore.inputForm });
    } else {
        searchDetail(orgnStatusStore.inputForm, true);
    }
});

// 목록으로 이동
btnBack(() => {
    // 두 배열 비교: docSeq가 같은 항목에 대해 gubun, issue, workDesc 값 비교
    const isModified = originData.value.some(originItem => {
        // 동일한 docSeq를 가진 항목 찾기
        const matchingItem = resultList.value.find(currentItem => currentItem.docSeq === originItem.docSeq);

        // gubun, issue, workDesc 값이 하나라도 다르면 수정된 것으로 간주
        return matchingItem && (originItem.gubun !== matchingItem.gubun || originItem.issue !== matchingItem.issue || originItem.workDesc !== matchingItem.workDesc || originItem.risk !== matchingItem.risk || originItem.chance !== matchingItem.chance);
    });
    // resultList.value에서 cmd 값이 'I'인 항목이 하나라도 있으면 수정된 것으로 간주
    const hasInsertedItems = resultList.value.some(item => item.cmd === 'I');

    if (isModified || hasInsertedItems) {
        confirmMsg('warning', '저장되지 않은 정보가 있습니다. \n 그래도 계속하시겠습니까?', null, { fun: orgnStatusStore.goBack, param: '' });
    } else {
        orgnStatusStore.goBack();
    }
});

// 추가
btnAdd(() => {
    // 카드 추가
    addOrgnStatus();
});

// 저장
btnSave(async () => {
    // 메인 폼 검증 먼저 수행
    const isValid = await validationStore.validateAllFields('form', true);
    if (isValid) {
        confirmMsg('info', t('msgSave'), '', { fun: updateOrgnStatus, param: orgnStatusStore.inputForm.orgnNm });
    }
});

// 삭제
btnDelete(async () => {
    // 체크된 항목 없을 시 return
    if (resultList.value.filter(item => item.checked).length === 0) {
        alertMsg('warning', t('msgNoItem'));
        return;
    }

    confirmMsg('warning', t('msgDelete'), '', { fun: delOrgnStatus, param: orgnStatusStore.inputForm.orgnNm });
});

// 출력
btnDownload(() => {
    if (resultList.value.some(item => item.gubun === 'I')) {
        alertMsg('warning', '사용중인 항목이 없습니다.');
        return;
    }
    // pdf 출력
    if (resultList.value.filter(item => item.useYn === 'Y').length === 0) {
        alertMsg('warning', '사용중인 항목이 없습니다.');
        return;
    }

    const isModified = originData.value.some(originItem => {
        // 동일한 docSeq를 가진 항목 찾기
        const matchingItem = resultList.value.find(currentItem => currentItem.docSeq === originItem.docSeq);

        // gubun, issue, workDesc 값이 하나라도 다르면 수정된 것으로 간주
        return matchingItem && (originItem.gubun !== matchingItem.gubun || originItem.issue !== matchingItem.issue || originItem.workDesc !== matchingItem.workDesc || originItem.risk !== matchingItem.risk || originItem.chance !== matchingItem.chance);
    });
    // resultList.value에서 cmd 값이 'I'인 항목이 하나라도 있으면 수정된 것으로 간주
    const hasInsertedItems = resultList.value.some(item => item.cmd === 'I');

    if (isModified || hasInsertedItems) {
        confirmMsg('warning', '저장되지 않은 정보가 있습니다. \n 그래도 계속하시겠습니까?', null, { fun: orgnStatusStore.downloadReport, param: { checkedObjList: [orgnStatusStore.inputForm] } });
    } else {
        confirmMsg('warning', t('msgPrint'), null, { fun: orgnStatusStore.downloadReport, param: { checkedObjList: [orgnStatusStore.inputForm] } });
        // orgnStatusStore.downloadReport();
    }
});

import ISignature from '@/components/common/iSignature.vue';

onMounted(() => {
    const param = setRouterParam(); // 라우터에 담긴 정보 호출, 있으면 key value 형식, 없으면 null
    if (param) {
        // 상세보기 or 라우터 이동인 경우 state를 전달받음
        searchDetail(param, true);
    } else if (!orgnStatusStore.inputForm || !orgnStatusStore.inputForm.cmd) {
        // 새로고침이거나 데이터가 소실된 경우 카드 뷰로 강제 이동
        router.push('OrganizationStatus');
        return;
    } else {
        // 추가버튼으로 왔을 때
        layoutStore.useBtnList = ['btnBack', 'btnSave'];
        // 카드 하나 추가
        addOrgnStatus();
        // 현재 날짜로 세팅
        orgnStatusStore.inputForm.writeDt = getCurrentDate();
        saveParam.value.useYn = 'Y';
    }
    // 공통코드값 가져오기
    getCodeList();
    // 조직
    orgnStatusStore.orgnItem.value = [];
    // 조직 팝업 세팅
    orgnStatusStore.setRefs(orgnPopup);

    orgnStatusStore.signature = signatureComponent.value;
});

// 값이 바뀌면 해당 데이터 checked
const chkData = data => {
    data.checked = true;
};

// 구분자 공통코드에서 가져오기
const getCodeList = async () => {
    let responses = await Promise.all([
        getSystemCode({
            majorCd: 'C0004',
            compId: getCompId()
        })
    ]);
    gubunList.value = responses[0].list;
};

// 조직 팝업 열기
const addOrgn = () => {
    orgnPopup.value.onOpen();
    for (var result of resultList.value) {
        result.checked = true;
    }
};

// 팝업 닫기
const closePopup = () => {
    peoplePopup.value.onClose();
    signPopup.value.onClose();
};

//그리드 조회
const searchDetail = async (param, notify) => {
    console.log('##param', param);
    resultList.value = [];
    originData.value = [];
    openLoading();
    orgnStatusStore
        .getDetailOrgnStatusLists(param, notify)
        .then(res => {
            if (!res || (!res.list && !res.signList)) return;
            orgnStatusStore.orgnItem = [{ id: res.list[0].orgnId, name: res.list[0].orgnNm }];
            orgnStatusStore.inputForm = { ...orgnStatusStore.inputForm, ...res.list[0] };
            orgnStatusStore.inputForm.writeDt = formatDate(res.list[0].writeDt);
            orgnStatusStore.inputForm.useYn = res.list[0].useYnM;

            saveParam.value.useYn = res.list[0].useYnM;
            resultList.value = res.list;
            originData.value = _.cloneDeep(res.list);
            layoutStore.useBtnList = ['btnBack', 'btnSearch', 'btnAdd', 'btnSave', 'btnDelete', 'btnDownload'];
        })
        .catch(() => {
            endLoading();
        })
        .finally(() => {
            endLoading();
        });
};

// 추가
const addOrgnStatus = () => {
    resultList.value.push({
        writeYear: orgnStatusStore.inputForm.writeYear,
        docType: orgnStatusStore.inputForm.docType,
        docNo: orgnStatusStore.inputForm.docNo,
        compId: getCompId(),
        cmd: 'I',
        gubun: 'I',
        workDesc: '',
        issue: '',
        risk: '',
        chance: '',
        approvalStatus: '',
        useYn: 'Y',
        writeDt: orgnStatusStore.inputForm.writeDt,
        checked: true
    });
};

// 저장
const updateOrgnStatus = async () => {
    orgnStatusStore.signature = signatureComponent.value;
    var result = true;

    resultList.value.forEach(item => {
        item.orgnId = orgnStatusStore.inputForm.orgnId;
        item.writeYear = orgnStatusStore.inputForm.writeYear;
        item.writeDt = formatDateForDB(orgnStatusStore.inputForm.writeDt);
        item.useYnM = saveParam.value.useYn;
    });
    var saveList = resultList.value.filter(item => item.checked);

    if (saveList.length == 0) {
        saveList.push({
            orgnId: orgnStatusStore.inputForm.orgnId,
            docType: orgnStatusStore.inputForm.docType,
            docNo: orgnStatusStore.inputForm.docNo,
            writeYear: orgnStatusStore.inputForm.writeYear,
            writeDt: formatDateForDB(orgnStatusStore.inputForm.writeDt),
            useYnM: saveParam.value.useYn
        });
    }
    // 체크된 요소들만 저장
    openLoading();
    await orgnStatusStore
        .setOrgnStatusDetail(saveList, true)
        .then(async res => {
            if (!res.success) {
                result = false;
            } else {
                orgnStatusStore.inputForm.writeYear = res.result[0].writeYear;
                orgnStatusStore.inputForm.docType = res.result[0].docType;
                orgnStatusStore.inputForm.docNo = res.result[0].docNo;

                // 신규일때 서명정보 저장
                if (orgnStatusStore.inputForm.cmd === 'I') {
                    // orgnStatusStore.signature.setApprovalInfo(res.result[0].writeYear + res.result[0].docNo, orgnStatusStore.inputForm.docType + '_' + orgnStatusStore.inputForm.writeYear + '_' + orgnStatusStore.inputForm.docNo);
                    // orgnStatusStore.signature.setApprovalInfo(orgnStatusStore.inputForm.docType,orgnStatusStore.inputForm.writeYear,orgnStatusStore.inputForm.docNo);
                    const success = orgnStatusStore.signature.setApprovalInfo(orgnStatusStore.inputForm.docType, orgnStatusStore.inputForm.writeYear, orgnStatusStore.inputForm.docNo);
                    if (success) orgnStatusStore.signature.updateTaskUseYn(orgnStatusStore.inputForm.docType, orgnStatusStore.inputForm.writeYear, orgnStatusStore.inputForm.docNo);
                } else {
                    orgnStatusStore.signature.updateTaskUseYn(orgnStatusStore.inputForm.docType, orgnStatusStore.inputForm.writeYear, orgnStatusStore.inputForm.docNo);
                }
            }
        })
        .catch(() => {
            endLoading();
        })
        .finally(() => {
            endLoading();
        });

    if (!result) {
        //
    } else {
        // 신규 등록일 시 업데이트 상태로 변경
        if (orgnStatusStore.inputForm.cmd === 'I') {
            // orgnStatusStore.goBack();
            orgnStatusStore.inputForm.cmd = 'U';
            layoutStore.useBtnList = ['btnBack', 'btnSearch', 'btnAdd', 'btnSave', 'btnDelete', 'btnDownload'];
            searchDetail(orgnStatusStore.inputForm, false);
        } else {
            searchDetail(orgnStatusStore.inputForm, false);
        }
    }
};

// 삭제 로직
const delOrgnStatus = async () => {
    var result = true;

    // 체크가 되었고 신규 추가된 카드는 쿼리 거치지 않고 바로 삭제
    resultList.value = resultList.value.filter(item => !(item.cmd === 'I' && item.checked));
    // 체크되있고 기존에 있던 카드는 삭제 쿼리 실행
    if (resultList.value.filter(item => item.checked).length > 0) {
        openLoading();
        await orgnStatusStore
            .delOrgnStatusDetail(
                resultList.value.filter(item => item.checked),
                true
            )
            .then(res => {
                if (!res.success) {
                    result = false;
                }
            })
            .catch(() => {
                endLoading();
            })
            .finally(() => {
                endLoading();
            });
    }

    // 성공여부 알림
    if (!result) {
        /* empty */
    } else {
        if (orgnStatusStore.inputForm.cmd !== 'I') {
            // 기존에 있던 조직은 재조회
            searchDetail(orgnStatusStore.inputForm, false);
        }
    }
};
</script>
<style scoped lang="scss">
/* 공통 스타일 */
.form {
    table.responsive-type-one tbody tr:first-of-type {
        th,
        td {
            border-top: 1px solid #e1e6ed;
        }
        .sm-h50p {
            border-top: none;
        }
    }
}

/* 반응형 스타일: 화면 너비가 768px 이하일 때 */
@media (max-width: 768px) {
    .form {
        table.responsive-type-one tbody tr {
            th,
            td {
                border-left: 1px solid #e1e6ed;
                border-right: 1px solid #e1e6ed;
            }

            th {
                border-top: 1px solid #e1e6ed;
                border-radius: 4px 4px 0 0;
            }

            td {
                margin-bottom: 2.2rem;
                border-radius: 0 0 4px 4px;
            }

            &:last-child td {
                border-bottom: 1px solid #e1e6ed;
            }
        }

        table.responsive-type-two tbody tr th {
            border-top: 0;
        }
    }
}
</style>
