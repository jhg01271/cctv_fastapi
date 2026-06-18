<template>
    <div id="form" class="contents">
        <div class="box form ui">
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
                <!-- <ISignature/> -->
                <div class="pa2-2rem">
                    <ISignature ref="signatureComponent" :cmd="empStakeholdersStore.inputForm.cmd" targetType="EMPLY" :writeYear="empStakeholdersStore.inputForm.writeYear" :docNo="empStakeholdersStore.inputForm.docNo" :useYn="dataMain.mainYn" />
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
                                <input v-input type="text" v-model="empStakeholdersStore.inputForm.writeYear" id="writeYear" v-calendar="'yyyy'" class="datepicker w100p radius" year required :readonly="empStakeholdersStore.inputForm.cmd !== 'I'" />
                            </div>
                        </div>
                        <div class="grid12-3 es-grid12-6">
                            <div class="field">
                                <label for="writeDt" required>
                                    <span>작성일자</span>
                                </label>
                                <input v-input type="text" v-model="empStakeholdersStore.inputForm.writeDt" required id="writeDt" class="datepicker w100p radius" v-calendar="getDateFormat()" />
                            </div>
                        </div>
                        <div class="grid12-3 es-grid12-6">
                            <div class="field">
                                <label for="orgnNm" required>
                                    <span>조직</span>
                                </label>
                                <!-- 기존 칩스와 구조 동일합니다. -->
                                <i-chips :chips="empStakeholdersStore.inputForm.orgnSelectList" @popup="orgnOpenPopup()" :readonly="empStakeholdersStore.inputForm.cmd !== 'I'" required></i-chips>
                            </div>
                        </div>

                        <div class="grid12-3 es-grid12-6">
                            <div class="field">
                                <label>사용여부</label>
                                <div class="h4-4rem df aic">
                                    <input type="checkbox" v-input class="switch" :checked="dataMain.mainYn === 'Y'" v-model="dataMain.mainYn" true-value="Y" false-value="N" />
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- 선택된 경우 아래 .box 요소에 .selected 클래스를 토글 시켜주시면 됩니다. -->
                    <div v-for="(items, index) in empStakeholdersStore.dataList" :key="index" :class="['box', 'df', 'mt2-4rem', 'bcF9FAFF', 'sm-db', { selected: items.isSelected }]">
                        <div class="df aic jcc w5rem shrink0 bdr1pxsolidE1E6ED sm-w100p sm-h5rem sm-bdb1pxsolidE1E6ED sm-bdr0pxsolid000000">
                            <input type="checkbox" v-input v-model="items.isSelected" />
                        </div>
                        <div class="w100pcalc5rem pa2-2rem sm-pa1-5rem sm-w100p">
                            <div class="h4-4rem df aic jcfe">
                                <input type="checkbox" @change="chkData(items)" v-input class="switch" :checked="items.useYn === 'Y'" v-model="items.useYn" true-value="Y" false-value="N" />
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
                                            <th class="w20p minw14rem h5rem lg-w30p" rowspan="2">
                                                <div class="field">
                                                    <label :for="'gubun' + index" :required="items.isSelected">
                                                        <span>이해관계자 구분</span>
                                                    </label>
                                                </div>
                                            </th>
                                            <td>
                                                <div class="field">
                                                    <select @change="chkData(items)" name v-select v-model="items.gubun" :id="'gubun' + index">
                                                        <option v-for="itemSS in comboList" :key="itemSS.minorCd" :value="itemSS.minorCd">{{ itemSS.minorNm }}</option>
                                                    </select>
                                                </div>
                                            </td>
                                        </tr>

                                        <!-- 근로자 및 이해관계자 상세 -->
                                        <tr>
                                            <td>
                                                <div class="field">
                                                    <!-- 기존 칩스와 구조 동일합니다. -->
                                                    <label for="stakeholdersArr" v-show="false">
                                                        <span>이해관계자 구분</span>
                                                    </label>
                                                    <i-chips @change="chkData(items)" :chips="items.stakeholdersArr" @popup="addPeople(index)" @removeChip="removePeople(index, $event)" :required="items.isSelected"></i-chips>
                                                </div>
                                            </td>
                                        </tr>

                                        <tr>
                                            <th class="w20p h5rem">
                                                <div class="field">
                                                    <label :for="'requirements' + index" :required="items.isSelected">
                                                        <span>요구사항 및 기대</span>
                                                    </label>
                                                </div>
                                            </th>
                                            <td>
                                                <div class="field">
                                                    <textarea @input="chkData(items)" v-model="items.requirements" name :id="'requirements' + index" class="w100p radius minh10rem" placeholder="요구사항 및 기대를 입력하세요." :required="items.isSelected"></textarea>
                                                </div>
                                            </td>
                                        </tr>
                                        <tr>
                                            <th class="w20p h5rem">
                                                <div class="field">
                                                    <label :for="'obligation' + index" :required="items.isSelected">
                                                        <span>준수의무사항</span>
                                                    </label>
                                                </div>
                                            </th>
                                            <td>
                                                <div class="field">
                                                    <textarea @input="chkData(items)" v-model="items.obligation" name :id="'obligation' + index" class="w100p radius minh10rem" placeholder="준수의무사항을 입력하세요." :required="items.isSelected"></textarea>
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
                                                <textarea @input="chkData(items)" v-model="items.risk" name id class="w100p radius minh10rem" placeholder="리스크를 입력하세요."></textarea>
                                            </td>
                                            <td>
                                                <textarea @input="chkData(items)" v-model="items.chance" name id class="w100p radius minh10rem" placeholder="기회를 입력하세요."></textarea>
                                            </td>
                                        </tr>
                                    </tbody>
                                </table>
                            </OverlayScrollbarsComponent>
                        </div>
                    </div>

                    <div class="df aic jcfe mt2rem">
                        <button @click="addData" type="button" class="btn active radius">
                            <div class="px2rem">
                                <span class="mr1rem">근로자 및 이해관계자 신규 추가</span>
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
                            </div>
                        </button>
                    </div>
                </div>
            </OverlayScrollbarsComponent>
        </div>
        <teleport to="body">
            <!-- 조직 모달 팝업 컴포넌트 시작  -->
            <i-PopupDialog ref="orgnPopup">
                <!-- 단일 그리드 -->
                <div class="contents form ui w70rem md-w100p">
                    <base-select-popup :title="'조직'" :selectedIdList="empStakeholdersStore.inputForm.orgnIdList" uniqueKey="orgnId" filterKey="orgnNm" useYnKey="useYn" :excluded-value="'N'" :single="true" :fetch-data="getOrganization" @close="orgnClosePopup" />
                    <!--              <orgnComponent :inputForm="hrStore.inputForm" :single="true" :options="{ label: '조직', readonly: false }" @close="hrStore.closeOrgn" />-->
                    <!-- 버튼 콤포넌트 영역 시작 -->
                </div>
            </i-PopupDialog>
            <!-- 조직 모달 팝업 콤포넌트 끝  -->

            <!-- 인원 검색 팝업 컴포넌트 시작  -->
            <i-PopupDialog ref="peoplePopup">
                <!-- 단일 그리드 -->
                <div class="contents w500px md-w100p">
                    <selectUser @close="closePeoplePopup" :selected="empStakeholdersStore.dataList[peopleIndex].stakeholdersArr?.map(el => el.id)" @selected="applyManagerPopup"></selectUser>
                </div>
            </i-PopupDialog>
            <!-- 인원 검색 팝업 컴포넌트 끝  -->
            <!-- 서명용 인원 검색 팝업 컴포넌트 시작  -->
            <!--        <i-PopupDialog ref="peoplePopup2">-->
            <!--            &lt;!&ndash; 단일 그리드 &ndash;&gt;-->
            <!--            <div class="contents w50rem md-w100p">-->
            <!--                <selectUser :single="true" @selected="selectPeople" @close="closePeoplePopup2"></selectUser>-->

            <!--                <div class="form ui tar mt2-5rem">-->
            <!--                    &lt;!&ndash; <button type="button" class="btn w80px radius active" v-button @click="btnSave">-->
            <!--                        <span>저장</span>-->
            <!--                    </button>&ndash;&gt;-->
            <!--                    <button type="button" v-button class="btn w80px radius ml4px bright-grey" @click="closePeoplePopup2">-->
            <!--                        <span>{{ t('close') }}</span>-->
            <!--                    </button>-->
            <!--                </div>-->
            <!--            </div>-->
            <!--        </i-PopupDialog>-->
            <!-- 인원 검색 팝업 컴포넌트 끝  -->
        </teleport>
    </div>
</template>
<script setup>
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import { ref, onMounted, nextTick } from 'vue';
import BaseView from '@/components/base/BaseView';
import { useButtonListStore } from '@/stores/buttonList';
import { useEmpStakeholdersStore } from '@/stores/safewiz/orgstatus/employeesAndStakeholders.js';
import ISignature from '@/components/common/iSignature.vue';
import { getOrganization } from '@/stores/system/base/api/organizationApi.js';
import { getSystemCode } from '@/stores/system/setting/api/SystemCode.js';
import baseSelectPopup from '@/views/system/base/popup/BaseSelectPopup.vue';
import selectUser from '@/views/base/member/SelectUser/Index.vue';
import router from '@/router';
import _ from 'lodash';
import { getDateFormat } from '@/utils/dateUtil.js';

import { useUserInfoStore } from '@/stores/user.js';
import { useSelectUserStore } from '@/views/base/member/SelectUser/SelectUser';

const selectUserStore = useSelectUserStore();

// 사용자 정보
const userStore = useUserInfoStore();
//우측 버튼
const { setRouterParam, validationStore, t, alertMsg, formatDate, getCurrentDate, btnSearch, btnBack, btnAdd, btnSave, btnDelete, btnDownload, confirmMsg, getCompId, formatDateForDB } = BaseView();
const layoutStore = useButtonListStore();
// const overlayScrollbars = ref(null);
layoutStore.useBtnList = ['btnSearch', 'btnBack', 'btnAdd', 'btnSave', 'btnDelete', 'btnDownload'];
const empStakeholdersStore = useEmpStakeholdersStore();
const signatureComponent = ref();
const dataMain = ref({});
const originData = ref([]);
const comboList = ref([]);

onMounted(async () => {
    const param = setRouterParam(); // 라우터에 담긴 정보 호출, 있으면 key value 형식, 없으면 null
    if (param) {
        // 상세보기 or 라우터 이동인 경우 state를 전달받음
        layoutStore.useBtnList = ['btnSearch', 'btnBack', 'btnAdd', 'btnSave', 'btnDelete', 'btnDownload'];
        empStakeholdersStore.inputForm.cmd = 'U';
        empStakeholdersStore.inputForm.writeYear = param.writeYear;
        empStakeholdersStore.inputForm.docType = param.docType;
        empStakeholdersStore.inputForm.docNo = param.docNo;
    } else if (!empStakeholdersStore.inputForm.cmd) {
        // 새로고침이거나 데이터가 소실된 경우 카드 뷰로 강제 이동
        router.push('EmployeesAndStakeholders');
        return;
    } else {
        // 추가버튼으로 왔을 때
        layoutStore.useBtnList = ['btnBack', 'btnSave'];
    }
    await userStore.getUserInfo();
    //combobox list search
    let request = await Promise.all([
        getSystemCode({
            majorCd: 'C0004'
        })
    ]);
    empStakeholdersStore.signature = signatureComponent.value;
    comboList.value = request[0].list;

    await searchEmployee();
});

/***************************************** 버튼이벤트 ********************************/

//목록 버튼
btnBack(() => {
    // 두 배열 비교: docSeq가 같은 항목에 대해 gubun, issue, workDesc 값 비교
    const isModified = originData.value.some(originItem => {
        // 동일한 docSeq를 가진 항목 찾기
        const matchingItem = empStakeholdersStore.dataList.find(currentItem => currentItem.docSeq === originItem.docSeq);

        // gubun, issue, workDesc 값이 하나라도 다르면 수정된 것으로 간주
        return matchingItem && (originItem.gubun !== matchingItem.gubun || originItem.obligation !== matchingItem.obligation || originItem.requirements !== matchingItem.requirements || originItem.stakeholdersNm !== matchingItem.stakeholdersNm || originItem.stakeholdersNm !== matchingItem.stakeholdersNm || originItem.risk !== matchingItem.risk || originItem.chance !== matchingItem.chance);
    });
    // dataList.value에서 cmd 값이 'I'인 항목이 하나라도 있으면 수정된 것으로 간주
    const hasInsertedItems = empStakeholdersStore.dataList.some(item => item.detailCmd === 'I');

    if (isModified || hasInsertedItems) {
        confirmMsg('warning', '저장되지 않은 정보가 있습니다. \n 그래도 계속하시겠습니까?', null, { fun: empStakeholdersStore.goBack, param: '' });
    } else {
        empStakeholdersStore.goBack();
    }
});

//조회 버튼
btnSearch(async () => {
    await signatureComponent.value.Search(); // 조회시 서명 항목도 조회 되도록 수정 [JS.SIM 25.03.06]

    // 두 배열 비교: docSeq가 같은 항목에 대해 gubun, issue, workDesc 값 비교
    const isModified = originData.value.some(originItem => {
        // 동일한 docSeq를 가진 항목 찾기
        const matchingItem = empStakeholdersStore.dataList.find(currentItem => currentItem.docSeq === originItem.docSeq);

        // gubun, issue, workDesc 값이 하나라도 다르면 수정된 것으로 간주
        return matchingItem && (originItem.gubun !== matchingItem.gubun || originItem.obligation !== matchingItem.obligation || originItem.requirements !== matchingItem.requirements || originItem.stakeholdersNm !== matchingItem.stakeholdersNm || originItem.stakeholdersNm !== matchingItem.stakeholdersNm || originItem.risk !== matchingItem.risk || originItem.chance !== matchingItem.chance);
    });
    // dataList.value에서 cmd 값이 'I'인 항목이 하나라도 있으면 수정된 것으로 간주
    const hasInsertedItems = empStakeholdersStore.dataList.some(item => item.cmd === 'I');

    if (isModified || hasInsertedItems) {
        confirmMsg('warning', '저장되지 않은 정보가 있습니다. \n 그래도 계속하시겠습니까?', null, { fun: searchEmployee, param: '' });
    } else {
        searchEmployee();
    }
});

//저장 버튼
btnSave(async () => {
    empStakeholdersStore.dataList.forEach(el => {
        el.writeYear = empStakeholdersStore.inputForm.writeYear;
        el.writeDt = formatDateForDB(empStakeholdersStore.inputForm.writeDt);
        el.mainYn = dataMain.value.mainYn;
    });

    const isValid = await validationStore.validateAllFields('form', true);
    if (isValid) {
        confirmMsg('info', t('msgSave'), '', { fun: saveEmployee, param: empStakeholdersStore.dataList });
    }
});

//추가 버튼
btnAdd(() => {
    addData();
});

//삭제 버튼
btnDelete(() => {
    const chkData = empStakeholdersStore.dataList.filter(item => item.isSelected);
    if (chkData.length === 0) {
        alertMsg('warning', t('msgNoItem'));
        return;
    }
    confirmMsg('warning', t('msgDelete'), '', { fun: deleteEmployee, param: chkData });
});

//출력 버튼
btnDownload(() => {
    if (empStakeholdersStore.dataList.some(item => item.gubun === 'I')) {
        alertMsg('warning', '사용중인 항목이 없습니다.');
        return;
    }

    // pdf 출력
    if (empStakeholdersStore.dataList.filter(item => item.useYn === 'Y').length === 0) {
        alertMsg('warning', '사용중인 항목이 없습니다.');
        return;
    }

    // 두 배열 비교: docSeq가 같은 항목에 대해 gubun, issue, workDesc 값 비교
    const isModified = originData.value.some(originItem => {
        // 동일한 docSeq를 가진 항목 찾기
        const matchingItem = empStakeholdersStore.dataList.find(currentItem => currentItem.docSeq === originItem.docSeq);

        // gubun, issue, workDesc 값이 하나라도 다르면 수정된 것으로 간주
        return matchingItem && (originItem.gubun !== matchingItem.gubun || originItem.obligation !== matchingItem.obligation || originItem.requirements !== matchingItem.requirements || originItem.stakeholdersNm !== matchingItem.stakeholdersNm || originItem.stakeholdersNm !== matchingItem.stakeholdersNm || originItem.risk !== matchingItem.risk || originItem.chance !== matchingItem.chance);
    });
    // empStakeholdersStore.dataList에서 cmd 값이 'I'인 항목이 하나라도 있으면 수정된 것으로 간주
    const hasInsertedItems = empStakeholdersStore.dataList.some(item => item.cmd === 'I');

    if (isModified || hasInsertedItems) {
        confirmMsg('warning', '저장되지 않은 정보가 있습니다. \n 그래도 계속하시겠습니까?', null, { fun: empStakeholdersStore.downloadReport, param: '' });
    } else {
        empStakeholdersStore.downloadReport([empStakeholdersStore.inputForm]);
    }
});

/***************************************** 사용자함수 *******************************/
//서명 조회
const searchSign = () => {};
//조회 함수
const searchEmployee = async () => {
    let searchParams = {
        docNo: empStakeholdersStore.inputForm.docNo,
        docType: empStakeholdersStore.inputForm.docType,
        writeYear: empStakeholdersStore.inputForm.writeYear
    };
    empStakeholdersStore.dataList = [];
    originData.value = [];
    dataMain.value = {};
    empStakeholdersStore.searchESHDetail(searchParams).then(res => {
        originData.value = _.cloneDeep(res.list);
        if (res.list.length > 0) {
            if (empStakeholdersStore.inputForm.orgnSelectList.length <= 0) {
                empStakeholdersStore.inputForm.orgnId = res.list[0].orgnId;
                empStakeholdersStore.inputForm.orgnSelectList.push({
                    id: res.list[0].orgnId,
                    name: res.list[0].orgnNm
                });
            }
            empStakeholdersStore.inputForm.writeYear = res.list[0].writeYear;
            empStakeholdersStore.inputForm.writeDt = formatDate(res.list[0].writeDt);
            dataMain.value.mainYn = res.list[0].mainYn;

            let array = [];
            for (let item of res.list) {
                //하위 카드 없으면 return
                if (!item.obligation) {
                    return;
                }
                const stakeArr = item.stakeholders.split(',');
                const stakeArrNm = item.stakeholdersNm.split(',');

                const temp = [];

                for (let i = 0; i < stakeArr.length; i++) {
                    temp.push({
                        id: stakeArr[i],
                        name: stakeArrNm[i]
                    });
                }
                array.push({
                    mainCmd: 'U',
                    detailCmd: 'U',
                    writeYear: item.writeYear,
                    writeDt: item.writeDt,
                    docType: item.docType,
                    docNo: item.docNo,
                    docSeq: item.docSeq,
                    compId: item.compId,
                    orgnId: item.orgnId,
                    gubun: item.gubun,
                    stakeholders: item.stakeholders,
                    stakeholdersNm: item.stakeholdersNm,
                    stakeholdersArr: temp,
                    requirements: item.requirements,
                    obligation: item.obligation,
                    risk: item.risk,
                    chance: item.chance,
                    useYn: item.useYn,
                    mainYn: item.mainYn,
                    isSelected: false
                });
            }
            empStakeholdersStore.dataList = array;
            // empStakeholdersStore.dataList.push(...res.list)
        } else {
            dataMain.value.mainYn = 'Y';
            empStakeholdersStore.inputForm.writeYear = getCurrentDate().substring(0, 4);
            empStakeholdersStore.inputForm.writeDt = getCurrentDate();

            if (empStakeholdersStore.dataList.length > 0) {
                return;
            }
            if (empStakeholdersStore.inputForm.cmd === 'I') {
                // 마스터도 신규일 경우
                empStakeholdersStore.dataList.push({
                    mainCmd: 'I',
                    detailCmd: 'I', // detail Cmd 체크된 상태일때 I 부여
                    writeYear: getCurrentDate().substring(0, 4),
                    writeDt: getCurrentDate(),
                    docType: 'EMPLY',
                    docNo: '',
                    docSeq: '',
                    compId: '',
                    orgnId: '',
                    gubun: 'internal',
                    stakeholders: '',
                    stakeholdersNm: '',
                    stakeholdersArr: [],
                    requirements: '',
                    obligation: '',
                    risk: '',
                    chance: '',
                    useYn: 'Y',
                    mainYn: 'Y',
                    isSelected: false
                });
            }
        }
    });
};

//저장 함수
const saveEmployee = param => {
    empStakeholdersStore.signature = signatureComponent.value;
    let saveData = [];
    layoutStore.useBtnList = ['btnSearch', 'btnBack', 'btnAdd', 'btnSave', 'btnDelete', 'btnDownload'];

    for (let item of param) {
        // 조직은 하위 내용이 없어도 저장
        item.orgnId = empStakeholdersStore.inputForm.orgnSelectList[0].id;
        item.compId = getCompId();
        if (item.isSelected) {
            item.createdBy = userStore.userId;
            item.mainYn = dataMain.value.mainYn;
        }
        saveData.push(item);
    }

    empStakeholdersStore.addESHDetail(saveData).then(async res => {
        if (res.result.length > 0) {
            empStakeholdersStore.inputForm.docNo = res.result[0].docNo;
            empStakeholdersStore.inputForm.docType = res.result[0].docType;
            empStakeholdersStore.inputForm.writeYear = res.result[0].writeYear;

            // saveSign(empStakeholdersStore.inputForm);
            if (empStakeholdersStore.inputForm.cmd === 'I') {
                // await empStakeholdersStore.signature.setApprovalInfo(empStakeholdersStore.inputForm.writeYear + empStakeholdersStore.inputForm.docNo);
                const success = await empStakeholdersStore.signature.setApprovalInfo(empStakeholdersStore.inputForm.docType, empStakeholdersStore.inputForm.writeYear, empStakeholdersStore.inputForm.docNo);
                if (success) {
                    empStakeholdersStore.inputForm.cmd = 'U';
                    await empStakeholdersStore.signature.updateTaskUseYn(empStakeholdersStore.inputForm.docType, empStakeholdersStore.inputForm.writeYear, empStakeholdersStore.inputForm.docNo);
                }
            } else {
                await empStakeholdersStore.signature.updateTaskUseYn(empStakeholdersStore.inputForm.docType, empStakeholdersStore.inputForm.writeYear, empStakeholdersStore.inputForm.docNo);
            }

            searchEmployee();
        }
    });
};

//삭제 함수
const deleteEmployee = param => {
    empStakeholdersStore.deleteESHDetail(param).then(() => {
        searchEmployee();
    });
};
//카드 추가
const addData = () => {
    empStakeholdersStore.dataList.push({
        mainCmd: '',
        detailCmd: 'I',
        writeYear: empStakeholdersStore.inputForm.writeYear,
        writeDt: empStakeholdersStore.inputForm.writeDt,
        docType: 'EMPLY',
        docNo: empStakeholdersStore.inputForm.docNo == '' ? '' : empStakeholdersStore.inputForm.docNo,
        docSeq: '',
        compId: '',
        orgnId: '',
        gubun: 'internal', //내/외부 구분
        stakeholders: '', //이해관계자
        stakeholdersNm: '', //이해관계자 명
        requirements: '', //요구사항
        obligation: '', //준수 의무사항
        risk: '', //리스크
        chance: '', //기회
        useYn: 'Y', //사용여부
        mainYn: 'Y',
        isSelected: true
    });
};

//조직 팝업 ref
const orgnPopup = ref(null);
//조직 팝업 오픈
const orgnOpenPopup = () => {
    orgnPopup.value.onOpen();
};
//조직 팝업 닫기
const orgnClosePopup = el => {
    empStakeholdersStore.closeOrgn(el);
    orgnPopup.value.onClose();
};

//컨트롤 체크 값 변경시 자동 체크 되도록 하는 기능
const chkData = data => {
    data.isSelected = true;
    // if(data.useYn == 'N'){
    //   return
    // }else{
    // }
};

const peopleIndex = ref(0);
//인원 팝업 ref
const peoplePopup = ref(null);
const addPeople = el => {
    peopleIndex.value = el;
    selectUserStore.currentTab = 'orgn';
    peoplePopup.value.onOpen();
};
// 인원 요소 제거(x버튼 클릭)
const removePeople = index => {
    if (empStakeholdersStore.dataList[index].useYn === 'Y') {
        return;
    }
    empStakeholdersStore.dataList[index].stakeholders = empStakeholdersStore.dataList[index].stakeholdersArr
        .map(item => {
            return item.id;
        })
        .join(',');
    empStakeholdersStore.dataList[index].stakeholdersNm = empStakeholdersStore.dataList[index].stakeholdersArr
        .map(item => {
            return item.name;
        })
        .join(',');
    dataList.value[index].isSelected = true;

    console.log('empStakeholdersStore.dataList[index]', empStakeholdersStore.dataList[index]);
};

//인원 팝업 닫기
const applyManagerPopup = () => {
    // console.log('peoplePopup.value', selectUserStore.getSelectedUser())

    if (peoplePopup.value) {
        peoplePopup.value.onClose();

        const users = selectUserStore.getSelectedUser();

        if (users.length > 0) {
            // 선택값이 있을때만 처리
            const temp = [];

            users.forEach(user => {
                temp.push({
                    id: user.hrId,
                    name: user.hrNm
                });
            });

            empStakeholdersStore.dataList[peopleIndex.value].stakeholdersArr = temp;
            empStakeholdersStore.dataList[peopleIndex.value].stakeholders = users
                .map(user => {
                    return user.hrId;
                })
                .join(',');
            empStakeholdersStore.dataList[peopleIndex.value].isSelected = true;
        }
    }
};

const closePeoplePopup = () => {
    if (peoplePopup.value) {
        peoplePopup.value.onClose();
    }
};

//서명 정보 인원 팝업
// const signGubn = ref('');
// const peoplePopup2 = ref(null);

//인원 팝업 선택
// const selectPeople = e => {
//     if (peoplePopup2.value) {
//         peoplePopup2.value.onClose();
//     }
//
//     if (signGubn.value == signSetData.value[0].code || signGubn.value == signSetData.value[1].code || signGubn.value == signSetData.value[2].code) {
//         signatureStore.data[signGubn.value].hrId = e.hrId;
//         signatureStore.data[signGubn.value].hrNm = e.hrNm;
//     }
// };

//인원 팝업 닫기
// const closePeoplePopup2 = () => {
//     if (peoplePopup2.value) {
//         peoplePopup2.value.onClose();
//     }
// };
</script>

<style scoped lang="scss">
.form {
    table.responsive-type-one {
        tbody {
            tr:first-of-type {
                th,
                td {
                    border-top: 1px solid #e1e6ed;
                }
                .sm-h50p {
                    border-top: none;
                }
            }
        }
    }
}

@media (max-width: 768px) {
    .form {
        table.responsive-type-one {
            tbody {
                tr {
                    th,
                    td {
                        border-left: 1px solid #e1e6ed;
                        border-right: 1px solid #e1e6ed;
                    }
                    th {
                        border-top: 1px solid #e1e6ed;
                        border-top-left-radius: 4px;
                        border-top-right-radius: 4px;
                    }
                    td {
                        margin-bottom: 2.2rem;
                        border-bottom-left-radius: 4px;
                        border-bottom-right-radius: 4px;
                    }
                    &:last-child td {
                        border-bottom: 1px solid #e1e6ed;
                    }
                }
            }
        }

        table.responsive-type-two {
            tbody {
                tr {
                    th {
                        border-top: 0;
                    }
                }
            }
        }
    }
}
</style>
