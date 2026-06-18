<template>
    <div v-if="hrStore && hrStore.inputForm" class="contents">
        <div id="form" class="box form ui df fdc">
            <OverlayScrollbarsComponent
                :options="{
                    scrollbars: {
                        autoHide: 'hover',
                        x: 'hidden',
                        y: 'visible'
                    }
                }"
            >
                <div class="pa2-2rem">
                    <div @submit.prevent ref="myForm">
                        <div class="row flex gutters1rem">
                            <div class="grid12-3 lg-grid12-6">
                                <div class="field">
                                    <label for="hrNm" required>
                                        <span>성명</span>
                                    </label>

                                    <input v-input v-model="hrStore.inputForm.hrNm" type="text" class="w100p radius" id="hrNm" required placeholder="성명을 입력해주세요." />
                                </div>
                            </div>
                            <div :class="isPwChangeAuth ? 'grid12-2 lg-grid12-6' : 'grid12-3 lg-grid12-6'">
                                <div class="field">
                                    <label for="userId" required>
                                        <span>아이디</span>
                                    </label>
                                    <input v-input v-model="hrStore.inputForm.userId" type="text" class="w100p radius" id="userId" required :disabled="hrStore.inputForm.cmd == 'U'" placeholder="아이디를 입력해주세요." />
                                </div>
                            </div>
                            <div v-if="isPwChangeAuth" class="grid12-1 lg-grid12-4 us-grid12-6">
                                <div class="field">
                                    <label for>비밀번호</label>
                                    <button type="button" class="w100p btn radius active" v-button @click="resetPassword">
                                        <span>초기화</span>
                                    </button>
                                </div>
                            </div>
                            <div class="grid12-2 lg-grid12-4 us-grid12-6">
                                <div class="field">
                                    <label for>{{ t('createdAt') }}</label>
                                    <input v-input type="text" class="w100p radius datepicker" id="createdAt" :value="formatDate(hrStore.inputForm.createdAt)" disabled :placeholder="'ex) 2024.01.01'" />
                                </div>
                            </div>
                            <div class="grid12-2 lg-grid12-4 us-grid12-6">
                                <div class="field">
                                    <label for>{{ t('array') }}</label>
                                    <input v-input type="number" v-model="hrStore.inputForm.ordSeq" class="w100p radius" id min="0" max="99" placeholder="99" />
                                </div>
                            </div>
                            <div class="grid12-2 lg-grid12-4 us-grid12-6">
                                <div class="field">
                                    <label for="useYn">{{ t('useYn') }}</label>
                                    <div class="df aic h4-4rem">
                                        <input v-input="'사용'" type="checkbox" class="df switch" :checked="hrStore.inputForm.useYn === 'Y'" @change="hrStore.toggleUseYn" />
                                    </div>
                                </div>
                            </div>

                            <div class="grid12-3 lg-grid12-12">
                                <div class="field h100p df fdc">
                                    <label for>사진</label>
                                    <div class="box df aic jcc ul-pr ul-t0 ul-mt0 fg1">
                                        <iFileImage ref="fileList" targetType="hrImg" :targetId="hrStore.inputForm.fileId" />
                                    </div>
                                </div>
                            </div>
                            <div class="grid12-9 lg-grid12-12">
                                <div class="row flex gutters1rem">
                                    <div class="grid12-4 us-grid12-6">
                                        <div class="field">
                                            <label for="email" required>
                                                <span>이메일</span>
                                            </label>
                                            <input v-input v-model="hrStore.inputForm.email" type="email" class="w100p radius" id="email" required placeholder="ex) example@i-gns.com" />
                                        </div>
                                    </div>
                                    <div class="grid12-4 us-grid12-6">
                                        <div class="field">
                                            <label for="phone" required>
                                                <span>전화번호</span>
                                            </label>
                                            <input v-input v-model="hrStore.inputForm.phone" type="phone" class="w100p radius" id="phone" required placeholder="ex) 010-1234-1234" />
                                        </div>
                                    </div>
                                    <div class="grid12-4 us-grid12-6" :key="hrStore.inputForm.sexDiv">
                                        <div class="field">
                                            <label for="sexDiv">성별</label>
                                            <select name id v-select v-model="hrStore.inputForm.sexDiv">
                                                <option v-for="(item, index) in hrStore.sexDivList" :key="index" :value="item.minorCd">{{ item.minorNm }}</option>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="grid12-4 us-grid12-6">
                                        <div class="field">
                                            <label for="jbrpNm">직위</label>
                                            <i-chips :chips="hrStore.jbrpPopupSelectedList" @popup="addSystemPopup"></i-chips>
                                        </div>
                                    </div>
                                    <div class="grid12-4 us-grid12-6">
                                        <div class="field">
                                            <label for>위험성평가담당</label>
                                            <input v-input v-model="hrStore.inputForm.riskAssRoleNm" type="text" class="w100p radius" readonly />
                                        </div>
                                    </div>
                                    <div class="grid12-4 us-grid12-6">
                                        <div class="field">
                                            <label for>생년월일</label>
                                            <input v-input type="text" v-calendar="getDateFormat()" v-model="hrStore.inputForm.birthDt" class="datepicker w100p radius" id />
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="fow flex gutters1rem">
                            <div class="grid12-3 ul-grid12-12">
                                <div class="field">
                                    <label for="compType">본사/협력사 구분</label>
                                    <div class="h4-4rem df aic">
                                        <input v-input="['본사', '협력사']" type="checkbox" class="df switch" v-model="hrStore.isHeadOffice" :checked="hrStore.isHeadOffice" @click="hrStore.updateCompType" />
                                    </div>
                                </div>
                            </div>

                            <div class="grid12-3 ul-grid12-12">
                                <div class="field">
                                    <label required>
                                        <span>소속 사업장</span>
                                    </label>
                                    <i-chips :chips="hrStore.affCompList" @popup="addMainComp" required @removeChip="removeMainComp" :removeConfirm="true" :removeConfirmMsg="'직위, 조직, 관리 사업장 정보가 초기화 됩니다.\n계속 하시겠습니까?'"></i-chips>
                                </div>
                            </div>

                            <div class="grid12-3 ul-grid12-12">
                                <div class="field">
                                    <label required>
                                        <span>조직</span>
                                    </label>

                                    <i-chips :chips="hrStore.orgnPopupSelectedList" @popup="addOrgn" :required="hrStore.isHeadOffice" :disabled="!hrStore.isHeadOffice"></i-chips>
                                </div>
                            </div>

                            <div class="grid12-3 ul-grid12-12">
                                <div class="field">
                                    <label for="partNm" required>
                                        <span>업체</span>
                                    </label>
                                    <i-chips for="partNm" :chips="hrStore.partnerPopupSelectedList" @popup="addPartner" :disabled="hrStore.isHeadOffice" :required="!hrStore.isHeadOffice"></i-chips>
                                </div>
                            </div>

                            <div class="grid12-6 ul-grid12-12">
                                <div class="field">
                                    <label for>
                                        <span>관리 사업장</span>
                                    </label>
                                    <i-chips :chips="hrStore.mgrCompList" @popup="addComp"></i-chips>
                                </div>
                            </div>

                            <div class="grid12-3 ul-grid12-6">
                                <div class="field">
                                    <label for>입사일</label>
                                    <input v-input v-model="hrStore.inputForm.workingAt" type="text" v-calendar="getDateFormat()" class="datepicker w100p radius" id />
                                </div>
                            </div>
                            <div class="grid12-3 ul-grid12-6">
                                <div class="field">
                                    <label for>퇴사일</label>
                                    <input v-input v-model="hrStore.inputForm.fireAt" type="text" v-calendar="getDateFormat()" class="datepicker w100p radius" id />
                                </div>
                            </div>

                            <div class="grid12-3 ul-grid12-4 sm-grid12-12">
                                <div class="field">
                                    <label for>우편번호</label>
                                    <div class="df aic w100p">
                                        <input v-input type="text" class="w100p radius search" id="zipCd" v-model="hrStore.inputForm.zipCd" :readonly="true" :placeholder="'ex) 12345'" />
                                        <button type="submit" class="shrink0 bcf1f3f8" @click="hrStore.emitOpenPostcodeDialog">
                                            <img src="/assets/img/common/icon_search.svg" alt />
                                        </button>
                                    </div>
                                </div>
                            </div>
                            <div class="grid12-3 ul-grid12-8 sm-grid12-12">
                                <div class="field">
                                    <label for>주소</label>
                                    <div class="df aic w100p">
                                        <input v-input type="text" class="w100p radius" id="addrs1" v-model="hrStore.inputForm.addrs1" :readonly="true" :placeholder="'ex) 경기 성남시 분당구 판교역로 166'" />
                                    </div>
                                </div>
                            </div>
                            <div class="grid12-6 ul-grid12-12">
                                <div class="field">
                                    <label for>상세주소</label>
                                    <div class="df aic w100p">
                                        <input v-input type="text" class="w100p radius" id="addrs2" v-model="hrStore.inputForm.addrs2" :placeholder="'ex) 101동 101호 '" />
                                    </div>
                                </div>
                            </div>

                            <div class="grid12-12">
                                <div class="field mb2rem">
                                    <label for>조직변경이력</label>

                                    <table class="tac">
                                        <thead>
                                            <tr>
                                                <th>조직명</th>
                                                <th>시작일</th>
                                                <th>종료일</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <tr v-for="(item, index) in hrStore.orgHistory" :key="index">
                                                <td>{{ item.orgnNm }}</td>
                                                <td>{{ formatDate(item.stDate) }}</td>
                                                <td>{{ formatDate(item.edDate) }}</td>
                                            </tr>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </OverlayScrollbarsComponent>
        </div>
        <!-- 주소 모달 팝업 컴포넌트 시작  -->
        <teleport to="body">
            <!-- 우편번호 검색 팝업  -->
            <i-PopupDialog ref="dialogPostNo">
                <div class="">
                    <OverlayScrollbarsComponent
                        class="es-maxh45rem"
                        :options="{
                            scrollbars: {
                                autoHide: 'hover',
                                x: 'hidden',
                                y: 'visible'
                            }
                        }"
                    >
                        <div class="oh">
                            <i-post-code @complete="hrStore.onPostcodeComplete" @close="hrStore.closePostcodeDialog"></i-post-code>
                        </div>
                    </OverlayScrollbarsComponent>
                </div>
            </i-PopupDialog>
            <!-- 모달 팝업 콤포넌트 끝  -->

            <!-- 직위 모달 팝업 컴포넌트 시작  -->

            <i-PopupDialog ref="systemPopup">
                <!-- 단일 그리드 -->
                <div class="contents form ui w70rem md-w100p">
                    <!-- trainingInstitute -->
                    <base-select-popup :title="'직위'" uniqueKey="jbrpId" filterKey="jbrpNm" useYnKey="useYn" :excluded-value="'N'" :single="true" :fetch-data="fetchHrJbrp" :selectedIdList="hrStore.jbrpPopupSelectedList.map(el => el.id)" @close="hrStore.closeSystemPopup" />
                </div>
            </i-PopupDialog>

            <!-- 모달 팝업 콤포넌트 끝  -->
            <!-- 소속 사업장 모달 팝업 컴포넌트 시작  -->
            <i-PopupDialog ref="mainCompPopup">
                <!-- 단일 그리드 -->

                <div class="contents form ui w70rem md-w100p">
                    <base-select-popup :title="'사업장'" :inputForm="hrStore.inputForm" uniqueKey="compId" filterKey="compNm" useYnKey="useYn" :excluded-value="'N'" :single="true" :selectedIdList="hrStore.affCompList.map(el => el.id)" :fetch-data="getCompListByClnt" @close="hrStore.closeMainComp" />
                    <!-- 버튼 콤포넌트 영역 시작 -->
                </div>
            </i-PopupDialog>

            <!-- 관리 사업장 모달 팝업 컴포넌트 시작  -->
            <i-PopupDialog ref="compPopup">
                <!-- 단일 그리드 -->

                <div class="contents form ui w70rem md-w100p">
                    <base-select-popup :title="'사업장'" uniqueKey="compId" filterKey="compNm" useYnKey="useYn" :excluded-value="'N'" :single="false" :fetch-data="filteredCompList" :selectedIdList="hrStore.mgrCompList.map(el => el.id)" @close="hrStore.closeComp" @apply="hrStore.applyComp" />

                    <!-- 버튼 콤포넌트 영역 시작 -->
                </div>
            </i-PopupDialog>
            <!-- 모달 팝업 콤포넌트 끝  -->
            <!-- 업체 모달 팝업 컴포넌트 시작  -->
            <i-PopupDialog ref="partnerPopup">
                <!-- 단일 그리드 -->

                <div class="contents form ui w70rem md-w100p">
                    <base-select-popup :title="'협력사'" :inputForm="hrStore.inputForm" uniqueKey="partCompId" filterKey="partCompNm" useYnKey="useYn" :excluded-value="'N'" :single="true" :fetch-data="getPartner" :selectedIdList="hrStore.partnerPopupSelectedList.map(el => el.id)" @close="hrStore.closePartner" />
                    <!-- 버튼 콤포넌트 영역 시작 -->
                </div>
            </i-PopupDialog>
            <!-- 모달 팝업 콤포넌트 끝  -->

            <!-- 조직 모달 팝업 컴포넌트 시작  -->
            <i-PopupDialog ref="orgnPopup">
                <!-- 단일 그리드 -->

                <div class="contents form ui w70rem md-w100p">
                    <base-select-popup :title="'조직'" :inputForm="hrStore.inputForm" uniqueKey="orgnId" filterKey="orgnNm" useYnKey="useYn" :excluded-value="'N'" :single="true" :fetch-data="fetchOrganization" :selectedIdList="hrStore.orgnPopupSelectedList.map(el => el.id)" @close="hrStore.closeOrgn" />
                    <!--              <orgnComponent :inputForm="hrStore.inputForm" :single="true" :options="{ label: '조직', readonly: false }" @close="hrStore.closeOrgn" />-->
                    <!-- 버튼 콤포넌트 영역 시작 -->
                </div>
            </i-PopupDialog>
            <!-- 모달 팝업 콤포넌트 끝  -->
        </teleport>
    </div>
</template>

<script setup>
import { ref, onMounted, onBeforeMount, computed, watch } from 'vue';
import BaseView from '@/components/base/BaseView';
const { validationStore, t, getCompId, confirmMsg, formatDate, btnSearch, btnBack, btnAdd, btnSave, btnDelete, alertMsg } = BaseView();
import router from '@/router';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import iPostCode from '@/components/common/iPostCode.vue';
import { checkExceedMaxHrCount } from '@/stores/system/base/api/hrApi';
import { getDateFormat } from '@/utils/dateUtil.js';
import { getGrpAuthCd } from '@/utils/token'; // 쿠키
import localStore from '@/utils/localStore';
import { AesDecrypt } from '@/utils/aes256.js';
//파일
import iFileImage from '@/components/file/iFileImage.vue';
const fileList = ref(null);

const isPwChangeAuth = computed(() => {
    let auth = false;
    if (window.sessionStorage.getItem('grpAuthCd')) {
        auth = AesDecrypt(window.sessionStorage.getItem('grpAuthCd')) === 'MANAGER' && hrStore.inputForm?.cmd === 'U';
    }
    return auth;
});

//소속 사업장 컴포넌트
const mainCompPopup = ref(null);

//관리 사업장 컴포넌트
const compPopup = ref(null);

//조직 컴포넌트
const orgnPopup = ref(null);

//협력사 컴포넌트
const partnerPopup = ref(null);

//직위 컴포넌트
const systemPopup = ref(null);

//store
import { useHrStore } from '@/stores/system/base/hr.js';
const hrStore = useHrStore();

import { useButtonListStore } from '@/stores/buttonList';
const layoutStore = useButtonListStore();

layoutStore.useBtnList = ['btnSearch', 'btnBack', 'btnAdd', 'btnSave', 'btnDelete'];

//시스템코드 조회
import { getSystemCode } from '@/stores/system/setting/api/SystemCode.js';
import { getHrJbrpByCompId } from '@/stores/system/base/api/hrApi';
// 해당 소속 사업장의 직위 데이터 호출
const fetchHrJbrp = () => {
    return getHrJbrpByCompId(hrStore.affCompList[0].id);
};

// 해당 소속 사업장의 조직 데이터 호출
const fetchOrganization = () => {
    return getOrganization({ compId: hrStore.affCompList[0].id });
};

//주소
const dialogPostNo = ref(null);

// 조회 버튼 이벤트 함수
btnSearch(() => {
    hrStore.getHrDetailList(hrStore.hrId, true);
    fileList.value.fnSearch();
});

btnBack(() => {
    confirmMsg('warning', '저장되지 않은 정보가 있습니다. \n 그래도 계속하시겠습니까?', null, { fun: hrStore.goBack, param: '' });
});

btnSave(() => {
    detailSave();
});

btnAdd(() => {
    //파일 초기화
    if (hrStore.inputForm) {
        // 추가 페이지로 이동
        confirmMsg('info', '저장되지 않은 정보가 있습니다. 그래도 이동하시겠습니까?', '', { fun: hrStore.btnAdd, param: '' });
    }
});

btnDelete(() => {
    hrStore.btnDelete('D');
});

const compId = getCompId();

onBeforeMount(async () => {
    await getSystemCode({
        majorCd: 'C0048',
        compId: '999999999999'
    }).then(res => {
        hrStore.sexDivList = res.list;
    });
});
onMounted(async () => {
    if (hrStore.inputForm?.cmd === 'I') {
        layoutStore.useBtnList = ['btnBack', 'btnSave'];
    } else if (!hrStore.inputForm || !hrStore.inputForm.cmd) {
        // 새로고침이거나 데이터가 소실된 경우 카드 뷰로 강제 이동
        router.push('HRManage');
        return;
    } else {
        layoutStore.useBtnList = ['btnBack', 'btnSearch', 'btnAdd', 'btnSave', 'btnDelete'];
    }

    //위험성평가 담당자 select box 리스트
    let responses = await Promise.all([
        getSystemCode({
            majorCd: 'C0006',
            compId: compId
        })
    ]);
    hrStore.headerList = responses[0].list;
    hrStore.headerList.unshift({ minorCd: '', minorNm: '-' });
    hrStore.partnerPopupList = null;

    if (hrStore.inputForm) {
        hrStore.beforeUseYn = hrStore.inputForm.useYn;
        //파일관련
        fileList.value.fnSearch();
        hrStore.setRefs(partnerPopup, mainCompPopup, compPopup, orgnPopup, dialogPostNo, fileList, systemPopup);
    }
});

// ---------------------------------------------------

//시스템 코드 팝업
import BaseSelectPopup from '@/views/system/base/popup/BaseSelectPopup.vue';
import { getOrganization } from '@/stores/system/base/api/organizationApi.js';
import { getPartner } from '@/stores/system/base/api/partnerApi.js';
import { getCompList, getCompListByClnt } from '@/stores/system/setting/api/compApi.js';
import SystemPopup from './popup/SystemPopup.vue';

// 폼 초기화 후 팝업 열기
const addSystemPopup = () => {
    if (hrStore.affCompList.length == 0) {
        alertMsg('warning', '소속 사업장을 먼저 입력하세요.');
    } else {
        hrStore.systemPopup.onOpen();
    }
};

// ---------------------------------------------------

// 폼 초기화 후 팝업 열기 2024.07.01
const addMainComp = () => {
    hrStore.mainCompPopup.onOpen();
};

const addComp = () => {
    if (hrStore.affCompList.length == 0) {
        alertMsg('warning', '소속 사업장을 먼저 입력하세요.');
    } else {
        hrStore.compPopup.onOpen();
    }
};

// 폼 초기화 후 팝업 열기 2024.07.01
const addPartner = () => {
    hrStore.partnerPopup.onOpen();
};

// 폼 초기화 후 팝업 열기 2024.07.01
const addOrgn = () => {
    if (hrStore.affCompList.length == 0) {
        alertMsg('warning', '소속 사업장을 먼저 입력하세요.');
    } else {
        hrStore.orgnPopup.onOpen();
    }
};

const removeMainComp = async index => {
    hrStore.mgrCompList = [];
    hrStore.orgnPopupSelectedList = [];
    hrStore.jbrpPopupSelectedList = [];
};

// 선택한 소속 사업장을 제외한 관리 사업장 리스트 필터
const filteredCompList = async () => {
    const res = await getCompListByClnt();

    const excludeId = hrStore.affCompList[0]?.id;

    // affCompList에서 선택된 compId 제외 필터링
    return {
        ...res,
        list: excludeId ? res.list.filter(item => item.compId !== excludeId) : res.list
    };
};

// 관리자 계정 > 비밀번호 변경 버튼 클릭 시
const resetPassword = () => {
    confirmMsg('warning', '비밀번호가 아이디로 초기화됩니다. \n 그래도 계속하시겠습니까?', null, { fun: resetPasswordAction, param: '' });
};

const resetPasswordAction = () => {
    hrStore.btnResetPassword();
};

// ---------------------------------------------------

//Detail 저장 버튼
const detailSave = async () => {
    const isValid = await validationStore.validateAllFields('form', true);
    if (isValid) {
        const param = {
            compId: hrStore.inputForm.compId,
            hrId: hrStore.inputForm.hrId,
            useYn: hrStore.inputForm.useYn
        };
        const isCheckHrCount = await checkExceedMaxHrCount(param);

        // 기준 인원 초과 판별
        if (isCheckHrCount.result) {
            alertMsg('warning', `최대 접속 인원 ${hrStore.inputForm.maxHrCount}명을 초과하였습니다.`);
            return;
        } else {
            await hrStore.btnSave();
        }
    }
};

onMounted(async () => {
    if (!hrStore.inputForm) {
        // 새로고침시 이전 화면으로 이동.
        router.go(-1);
        return;
    }
});
</script>
