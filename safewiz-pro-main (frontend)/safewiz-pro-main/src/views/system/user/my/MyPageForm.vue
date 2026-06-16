div
<template>
    <div class="contents">
        <div id="form" class="box form ui pr">
            <OverlayScrollbarsComponent
                class="h100p pa2-2rem pb10rem md-pa1-2rem"
                :options="{
                    scrollbars: {
                        autoHide: 'hover',
                        x: 'hidden',
                        y: 'visible'
                    }
                }"
            >
                <form @submit.prevent ref="myForm" id="form">
                    <div class="row flex gutters1rem">
                        <!-- 시스템 관리자 계정에만 보이는 기능 -->
                        <!-- 사용자 토큰 및 API 토큰 관리 팝업 호출 -->
                        <div v-if="saveParams.authGroupCd === 'MASTER'" class="grid12-12 es-grid12-12">
                            <div class="df aic h4-4rem gap4px">
                                <button type="button" class="btn radius active" v-button @click="clickTokenMng('user')">
                                    <span>사용자 토큰 관리</span>
                                </button>
                                <button type="button" class="btn radius active" v-button @click="clickTokenMng('api')">
                                    <span>API 토큰 관리</span>
                                </button>
                            </div>
                        </div>
                        <div class="grid12-2 es-grid12-6">
                            <div class="field">
                                <label for="userNm">
                                    <span>{{ t('userNm') }}</span>
                                </label>
                                <input v-input type="text" class="w100p radius" id="userNm" readonly v-model="saveParams.userNm" />
                            </div>
                        </div>

                        <div class="grid12-2 es-grid12-6">
                            <div class="field">
                                <label for="userId">
                                    <span>{{ t('id') }}</span>
                                </label>
                                <input v-input type="text" class="w100p radius" id="userId" readonly v-model="saveParams.userId" />
                            </div>
                        </div>

                        <!-- TODO 비밀번호 변경 기능 추가 개발 필요 -->
                        <div class="grid12-2 es-grid12-4 us-grid12-12">
                            <div class="field">
                                <label for>
                                    <span>{{ t('mypage_changePassword') }}</span>
                                </label>
                                <div class="h4-4rem df aic">
                                    <input v-input type="checkbox" class="w100p radius" id="passwordChange" required v-model="isPasswordChange" />
                                </div>
                            </div>
                        </div>

                        <div class="grid12-3 es-grid12-4 us-grid12-6" v-if="isPasswordChange">
                            <div class="field">
                                <label for="password" required>
                                    <span>{{ t('mypage_newPassword') }}</span>
                                </label>
                                <!-- <input v-input type="text" class="w100p radius" id="password" required /> -->
                                <div class="pr">
                                    <input v-input class="password w100p br4px" :type="isPasswordVisible ? 'text' : 'password'" id="password" v-model="saveParams.userPs" required />
                                    <button class="pa w20px t50p r10px neg-tty50p" type="button" @click="isPasswordVisible = !isPasswordVisible">
                                        <img :src="isPasswordVisible ? '/assets/img/common/icon_eye_close_dark.svg' : '/assets/img/common/icon_eye_dark.svg'" alt="Toggle Password Visibility" />
                                    </button>
                                </div>
                            </div>
                        </div>

                        <div class="grid12-3 es-grid12-4 us-grid12-6" v-if="isPasswordChange">
                            <div class="field">
                                <label for="passwordConfirm" required>
                                    <span>{{ t('mypagee_confirmNewPassword') }}</span>
                                </label>
                                <div class="pr">
                                    <input v-input class="password w100p br4px" :type="isPasswordConfirmVisible ? 'text' : 'password'" id="passwordConfirm" v-model="saveParams.passwordConfirm" required />
                                    <button class="pa w20px t50p r10px neg-tty50p" type="button" @click="isPasswordConfirmVisible = !isPasswordConfirmVisible">
                                        <img :src="isPasswordConfirmVisible ? '/assets/img/common/icon_eye_close_dark.svg' : '/assets/img/common/icon_eye_dark.svg'" alt="Toggle Password Visibility" />
                                    </button>
                                </div>
                            </div>
                        </div>

                        <div class="row flex gutters1rem">
                            <div class="grid12-4 es-grid12-12">
                                <div class="field df fdc h100p">
                                    <label for="signature">
                                        <span>{{ t('mypage_signature') }}</span>
                                    </label>
                                    <div class="pr pa1rem df aic jcc box fg1" style="width: 100%; height: 142px">
                                        <img :src="saveParams.signature" alt style="max-width: 100%; max-height: 100%; object-fit: contain" />
                                        <button v-if="saveParams.signature != null && saveParams.signature !== ''" type="button" class="pa t1rem r1rem w18px h18px br50p bcFFD7D6 init m" @click="removeSignature">
                                            <svg xmlns="http://www.w3.org/2000/svg" width="8" height="8" viewBox="0 0 8 8" fill="none">
                                                <path d="M7 1L1 6.99999M7 7L1 1.00001" stroke="#FF3534" stroke-linecap="round" />
                                            </svg>
                                        </button>
                                        <div class="segment pr cp" v-else @click="addSign">
                                            <svg xmlns="http://www.w3.org/2000/svg" width="34" height="34" viewBox="0 0 34 34" fill="none">
                                                <path d="M23.375 12.9431C20.7994 13.8016 17.3654 10.3676 18.2239 7.79199M19.282 6.73382L13.7332 12.2827C11.3209 14.695 9.60957 17.7175 8.78216 21.0272L8.51404 22.0996C8.4305 22.4338 8.73319 22.7365 9.06736 22.653L10.1398 22.3848C13.4495 21.5574 16.472 19.8461 18.8843 17.4338L24.4332 11.8849C25.1162 11.2019 25.5 10.2754 25.5 9.30938C25.5 7.29775 23.8692 5.66699 21.8576 5.66699C20.8916 5.66699 19.9651 6.05074 19.282 6.73382Z" stroke="black" stroke-width="1.5" />
                                                <path d="M19.2821 6.73382C19.9652 6.05074 20.8916 5.66699 21.8577 5.66699C23.8693 5.66699 25.5001 7.29775 25.5001 9.30938C25.5001 10.2754 25.1163 11.2019 24.4332 11.8849L23.3751 12.9431C20.7995 13.8016 17.3654 10.3676 18.2239 7.79199L19.2821 6.73382Z" stroke="#3248F6" stroke-width="1.5" />
                                                <path d="M26.9167 28.333H7.08337" stroke="#3248F6" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
                                            </svg>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="grid12-8 es-grid12-12">
                                <div class="row flex gutters1rem">
                                    <div class="grid12-6">
                                        <div class="field">
                                            <label for="phone" required>
                                                <span>{{ t('phoneNumber') }}</span>
                                            </label>
                                            <input v-input type="text" class="w100p radius" id="phone" required placeholder="전화번호를 입력해주세요." v-model="saveParams.phone" />
                                        </div>
                                    </div>

                                    <div class="grid12-6">
                                        <div class="field">
                                            <label for="email" required>
                                                <span>{{ t('email') }}</span>
                                            </label>
                                            <input v-input type="text" class="w100p radius" id="email" required placeholder="이메일을 입력해주세요." v-model="saveParams.email" />
                                        </div>
                                    </div>

                                    <div class="grid12-6" :key="saveParams.sexDiv">
                                        <div class="field">
                                            <label for="sexDiv">
                                                <span>{{ t('gender') }}</span>
                                            </label>
                                            <select name id="sexDiv" v-select v-model="saveParams.sexDiv" :disabled="saveParams.authGroupCd !== 'USER'">
                                                <option v-for="item in sexDivList" :key="item.sexDiv" :value="item.sexDiv">{{ item.sexDivNm }}</option>
                                            </select>
                                        </div>
                                    </div>

                                    <div class="grid12-6">
                                        <div class="field">
                                            <label for="birthDt">
                                                <span>{{ t('birthDate') }}</span>
                                            </label>
                                            <input v-input type="text" v-calendar="getDateFormat()" :value="getFormattedDate(saveParams.birthDt)" @input="onDateInput" class="datepicker w100p radius" id="생년월일" />
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="grid12-4 us-grid12-12">
                                <div class="field">
                                    <label for="zipCd">
                                        <span>{{ t('zipCode') }}</span>
                                    </label>
                                    <div class="df aic w100p">
                                        <input v-input type="text" class="w100p radius search" id="zipCd" v-model="saveParams.zipCd" :readonly="true" :placeholder="'ex) 12345'" />
                                        <!-- <i
                                        class="cp shrink0"
                                        @click="closePost"
                                    >
                                        <img src="/assets/img/common/icon_delete.svg" alt width="15" height="15" />
                                    </i> -->
                                        <button type="submit" class="shrink0 bcf1f3f8" @click="openPostcodeDialog">
                                            <img src="/assets/img/common/icon_search.svg" alt />
                                        </button>
                                    </div>
                                </div>
                            </div>

                            <div class="grid12-8 us-grid12-12">
                                <div class="field">
                                    <label for="addrs1">
                                        <span>{{ t('address') }}</span>
                                    </label>
                                    <div class="df aic w100p">
                                        <input v-input type="text" class="w100p radius" id="addrs1" v-model="saveParams.addrs1" :readonly="true" :placeholder="'ex) 경기 성남시 분당구 판교역로 166'" />
                                    </div>
                                </div>
                            </div>

                            <div class="grid12-12">
                                <div class="field">
                                    <label for="addrs2">
                                        <span>{{ t('detailAddress') }}</span>
                                    </label>
                                    <div class="df aic w100p">
                                        <input v-input type="text" class="w100p radius" id="addrs2" v-model="saveParams.addrs2" :placeholder="'ex) 101동 101호 '" />
                                    </div>
                                </div>
                            </div>
                            <div class="grid12-2 es-grid12-4 us-grid12-6">
                                <div class="field">
                                    <label for>
                                        <span>{{ t('크롬(Chrome) 알림') }}</span>
                                    </label>
                                    <div class="h4-4rem df aic">
                                        <div v-if="saveParams.chromeYn === 'Y' || saveParams.chromeYn === 'N'" @click="chromeYnClick">
                                            <input v-input="['허용', '차단']" :true-value="'Y'" :false-value="'N'" type="checkbox" class="df switch" id="chromeYn" v-model="saveParams.chromeYn" :checked="saveParams.chromeYn === 'Y'" disabled="true" />
                                        </div>
                                        <button v-else type="button" class="h4-6rem btn w80p radius active br4px" @click="allowPermission()">
                                            <span>{{ t('알림 허용') }}</span>
                                        </button>
                                    </div>
                                </div>
                            </div>
                            <div class="grid12-2 es-grid12-4 us-grid12-6">
                                <div class="field">
                                    <label for>
                                        <span>{{ t('Safewiz Pro 알림') }}</span>
                                    </label>
                                    <div class="h4-4rem df aic">
                                        <input v-input="['허용', '차단']" :true-value="'Y'" :false-value="'N'" type="checkbox" class="df switch" id="safewizProYn" v-model="saveParams.safewizProYn" :checked="saveParams.safewizProYn === 'Y'" />
                                    </div>
                                </div>
                            </div>
                            <div class="grid12-2 es-grid12-4 us-grid12-12">
                                <div class="field">
                                    <label for>
                                        <span>{{ t('마지막으로 접속한 기기') }}</span>
                                    </label>
                                    <div class="df aic w100p">
                                        <input v-input type="text" class="w100p radius" id="addrs2" v-model="saveParams.loginDevice" readonly />
                                    </div>
                                </div>
                            </div>
                            <div class="grid12-2 es-grid12-4 us-grid12-12">
                                <div class="field">
                                    <label for>
                                        <span>{{ t('권한') }}</span>
                                    </label>
                                    <div class="df aic w100p">
                                        <input v-input type="text" class="w100p radius" id="addrs2" v-model="saveParams.authGroupNm" readonly />
                                    </div>
                                </div>
                            </div>
                            <div class="grid12-4 es-grid12-8 us-grid12-12">
                                <div class="field">
                                    <label for>
                                        <span>{{ t('그룹') }}</span>
                                    </label>
                                    <div class="df aic w100p">
                                        <input v-input type="text" class="w100p radius" id="addrs2" v-model="saveParams.grpNm" readonly />
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </form>
            </OverlayScrollbarsComponent>
        </div>
        <!-- <div class="btn-group pa r0 t0 fdc">
            <button type="button" v-button @click="searchData">
                <i>
                    <svg width="24" height="24" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                        <path d="M22.6492 19.3562C22.8459 19.55 23.1625 19.5476 23.3562 19.3508C23.55 19.1541 23.5476 18.8375 23.3508 18.6438L22.6492 19.3562ZM1 5.5C0.723858 5.5 0.5 5.72386 0.5 6C0.5 6.27614 0.723858 6.5 1 6.5L1 5.5ZM7.6 6.5C7.87614 6.5 8.1 6.27614 8.1 6C8.1 5.72386 7.87614 5.5 7.6 5.5L7.6 6.5ZM1 10.9167C0.723858 10.9167 0.5 11.1405 0.5 11.4167C0.5 11.6928 0.723858 11.9167 1 11.9167L1 10.9167ZM5.4 11.9167C5.67614 11.9167 5.9 11.6928 5.9 11.4167C5.9 11.1405 5.67614 10.9167 5.4 10.9167V11.9167ZM1 16.3333C0.723858 16.3333 0.5 16.5572 0.5 16.8333C0.5 17.1095 0.723858 17.3333 1 17.3333L1 16.3333ZM7.6 17.3333C7.87614 17.3333 8.1 17.1095 8.1 16.8333C8.1 16.5572 7.87614 16.3333 7.6 16.3333L7.6 17.3333ZM20.1706 11.3529C20.1706 14.026 17.9682 16.2059 15.2353 16.2059V17.2059C18.506 17.2059 21.1706 14.5926 21.1706 11.3529H20.1706ZM15.2353 16.2059C12.5024 16.2059 10.3 14.026 10.3 11.3529H9.3C9.3 14.5926 11.9645 17.2059 15.2353 17.2059V16.2059ZM10.3 11.3529C10.3 8.67991 12.5024 6.5 15.2353 6.5V5.5C11.9645 5.5 9.3 8.11328 9.3 11.3529H10.3ZM15.2353 6.5C17.9682 6.5 20.1706 8.67991 20.1706 11.3529H21.1706C21.1706 8.11328 18.506 5.5 15.2353 5.5V6.5ZM18.7668 15.5327L22.6492 19.3562L23.3508 18.6438L19.4685 14.8202L18.7668 15.5327ZM1 6.5L7.6 6.5L7.6 5.5L1 5.5L1 6.5ZM1 11.9167L5.4 11.9167V10.9167L1 10.9167L1 11.9167ZM1 17.3333L7.6 17.3333L7.6 16.3333L1 16.3333L1 17.3333Z" fill="black" />
                    </svg>
                </i>
                <span>조회</span>
            </button>
            <button type="button" v-button @click="chkSave">
                <i>
                    <svg width="24" height="24" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                        <path d="M7 21V14C7 12.8954 7.89543 12 9 12H15C16.1046 12 17 12.8954 17 14V21M10 16H14M5 21H19C20.1046 21 21 20.1046 21 19V5C21 3.89543 20.1046 3 19 3H8.82843C8.29799 3 7.78929 3.21071 7.41421 3.58579L3.58579 7.41421C3.21071 7.78929 3 8.29799 3 8.82843V19C3 20.1046 3.89543 21 5 21Z" stroke="black" stroke-linecap="round" />
                    </svg>
                </i>
                <span>저장</span>
            </button>
        </div> -->
        <teleport to="body">
            <i-PopupDialog ref="dialogPostNo">
                <div class="maxh80rem oya">
                    <VueDaumPostcode @complete="postcodeComplete" />
                </div>
                <div class="form ui">
                    <button type="button" v-button class="btn w100p radius bright-grey" @click="closePost">
                        <span>{{ t('close') }}</span>
                    </button>
                </div>
            </i-PopupDialog>
            <i-PopupDialog ref="signPopup">
                <div class="contents w45rem md-w100p">
                    <h3>{{ t('mypage_signpopup_title') }}</h3>
                    <VueSignaturePad v-if="imgSign == null" class="bd1pxsolidE1E6ED" id="signature" width="100%" height="156px" ref="signaturePad" />
                    <div v-else id="signature" class="bd1pxsolidE1E6ED df jcc" style="width: 100%; height: 156px; overflow: hidden">
                        <img :src="imgSign" alt="서명 이미지" style="max-width: 100%; max-height: 100%; object-fit: contain; display: block" />
                    </div>
                    <div class="form ui df jcsb mt2-5rem">
                        <div class="tal">
                            <button type="button" v-button class="btn w74px radius delete" @click="clearSign">
                                <span>{{ t('btnClear') }}</span>
                            </button>
                        </div>
                        <div class="tar">
                            <button type="button" class="btn w74px radius mr8px active" v-button @click="loadSign">
                                <span>{{ t('load') }}</span>
                            </button>
                            <button type="button" class="btn w74px radius mr8px active" v-button @click="saveSign">
                                <span>{{ t('apply') }}</span>
                            </button>
                            <button type="button" v-button class="btn w74px radius bright-grey" @click="closeSign">
                                <span>{{ t('close') }}</span>
                            </button>
                        </div>
                    </div>
                </div>
            </i-PopupDialog>
            <div>
                <input ref="fileInput" type="file" @change="fileChanged" accept=".png" class="dn" />
            </div>
            <i-PopupDialog ref="tokenMngPopup">
                <div class="contents form ui w70rem md-w100p">
                    <TokenManageForm :type="tokenType" @close="closeTokenMngPopup" />
                </div>
            </i-PopupDialog>
        </teleport>
    </div>
</template>

<script setup>
import { defineProps, defineModel, ref, reactive, computed } from 'vue';
import { getDateFormat } from '@/utils/dateUtil.js';
import router from '@/router';
import FemsUtils from '@/components/base/FemsUtils.js';
import { searchMyInfo, saveMyInfo, createApiToken, removeApiToken } from '@/stores/system/user/my/MyPage.js';
import BaseView from '@/components/base/BaseView';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import { AesEncrypt, AesDecrypt } from '@/utils/aes256';
import { useButtonListStore } from '@/stores/buttonList';
const { onMounted, t, toastPopup, confirmMsg, getFormattedDate, validationStore, alertMsg, btnSearch, btnBack, btnSave } = BaseView();
import TokenManageForm from './TokenManageForm.vue';
import { useFcmStore } from '@/firebase/fcmService.js'; //FCM TOKEN 권한 허용 여부
const fcmStore = useFcmStore();
import { Buffer } from 'buffer';
import _ from 'lodash';
// 버튼 세팅
const layoutStore = useButtonListStore();
layoutStore.useBtnList = ['btnBack', 'btnSearch', 'btnSave'];
import { useUserInfoStore } from '@/stores/user.js';
const userInfoStore = useUserInfoStore();

const saveParams = reactive({
    userId: '',
    userNm: '',
    email: '',
    phone: '',
    sexDiv: '',
    birthDt: '',
    zipCd: '',
    addrs1: '',
    addrs2: '',
    signature: '',
    chromeYn: '',
    safewizProYn: ''
});
const imgSign = ref(null);

const sexDivList = computed(() => [
    { sexDiv: 'M', sexDivNm: t('male') },
    { sexDiv: 'W', sexDivNm: t('female') }
]);

const dialogPostNo = ref(null);
const signaturePad = ref();
const signPopup = ref();
const signature = ref({
    mode: 'display',
    isEmpty: false
});
const clearSign = () => {
    if (imgSign.value == null) {
        // 수기 서명인 경우
        signaturePad.value.clearSignature();
    } else {
        // 이미지 서명인 경우
        imgSign.value = null;
    }
};
// 서명 불러오기
const fileInput = ref(null);
const loadSign = () => {
    fileInput.value.click();
};
const fileChanged = e => {
    const file = e.target.files[0];
    if (!file) return;

    const reader = new FileReader();

    reader.onload = () => {
        const base64String = reader.result;
        console.log('Base64 결과 -> ', base64String.length);
        imgSign.value = base64String;
        // 필요한 로직: 예) 미리보기, API 전송 등
    };

    reader.onerror = error => {
        console.log('파일 읽기 에러:', error);
    };

    e.target.value = null; // 이벤트를 다시 받을 수 있도록 초기화
    reader.readAsDataURL(file); // Base64 문자열로 읽기
};
const saveSign = () => {
    if (imgSign.value == null) {
        // 수기 서명인 경우
        const { isEmpty, data } = signaturePad.value.saveSignature();
        signature.value.mode = 'display';
        if (isEmpty == false) {
            signature.value.isEmpty = true;
            saveParams.signature = data;
        }
    } else {
        // 이미지 불러오기 인 경우
        console.log('# ############# ');
        saveParams.signature = _.cloneDeep(imgSign.value);
    }
    signPopup.value.onClose();
};
const onDateInput = event => {
    const cleanedValue = event.target.value.replace(/\D/g, '').slice(0, 8);
    saveParams.birthDt = cleanedValue;
};

const searchData = notify => {
    validationStore.clearInvalidClasses();
    validationStore.clearAllErrors();
    searchMyInfo(notify).then(res => {
        const resResult = res.result;
        // console.log('searchMyInfo res', resResult);

        Object.keys(resResult).forEach(key => {
            resResult[key] = resResult[key] == null ? '' : resResult[key];
        });
        saveParams.userId = resResult.userId;
        saveParams.userNm = resResult.userNm;
        saveParams.email = AesDecrypt(resResult.email);
        saveParams.phone = AesDecrypt(resResult.phone);
        saveParams.sexDiv = resResult.sexDiv;
        saveParams.birthDt = resResult.birthDt;
        saveParams.zipCd = resResult.zipCd;
        saveParams.addrs1 = resResult.addrs1;
        saveParams.addrs2 = resResult.addrs2;
        saveParams.chromeYn = Notification.permission === 'granted' ? 'Y' : Notification.permission === 'denied' ? 'N' : '';
        saveParams.safewizProYn = resResult.fcmToken === null || resResult.fcmToken === '' ? 'N' : 'Y';
        saveParams.loginDevice = resResult.loginDevice;
        // console.log('resResult.signature', resResult.signature);
        saveParams.signature = resResult.signature;
        userInfoStore.signature = resResult.signature;
        saveParams.grpNm = resResult.grpNm;
        saveParams.authGroupNm = resResult.authGroupNm; // 권한
        saveParams.authGroupCd = resResult.authGroupCd; // 권한 코드
        userInfoStore.fcmToken = resResult.fcmToken; //토큰 값 여부에 따라 header의 알림 아이콘 변경
    });
};

const openPostcodeDialog = () => {
    dialogPostNo.value.onOpen();
};

const closePost = () => {
    dialogPostNo.value.onClose();
};

const postcodeComplete = data => {
    saveParams.zipCd = data.zonecode;
    saveParams.addrs1 = data.address;

    dialogPostNo.value.onClose();
};

const removeSignature = () => {
    saveParams.signature = '';
    imgSign.value = null;
};

const addSign = () => {
    signPopup.value.onOpen();
};

const closeSign = () => {
    signPopup.value.onClose();
    imgSign.value = null;
};

btnSearch(() => {
    searchData(true);
    initOption();
});

btnBack(() => {
    confirmMsg('warning', '저장되지 않은 정보가 있습니다. \n 그래도 계속하시겠습니까?', '', { fun: goBack });
});

const goBack = () => {
    router.push({ path: '/dashboard' });
};

btnSave(() => {
    chkSave();
});

const chkSave = async () => {
    const isValid = await validationStore.validateAllFields('form', true);
    if (isValid) {
        confirmMsg('info', t('msgSave'), '', { fun: saveData });
    }
};

const saveData = async () => {
    // console.log('Save Data1', saveParams);
    if (saveParams.safewizProYn === 'Y') {
        fcmStore.requestPermission();
    }

    const param = {
        userId: saveParams.userId,
        userNm: saveParams.userNm,
        email: AesEncrypt(saveParams.email),
        phone: AesEncrypt(saveParams.phone),
        sexDiv: saveParams.sexDiv,
        birthDt: saveParams.birthDt,
        zipCd: saveParams.zipCd,
        addrs1: saveParams.addrs1,
        addrs2: saveParams.addrs2,
        signature: saveParams.signature,
        chromeYn: saveParams.chromeYn,
        safewizProYn: saveParams.safewizProYn
    };

    if (isPasswordChange.value) {
        if (saveParams.userPs != saveParams.passwordConfirm) {
            alertMsg('warning', '비밀번호가 일치하지 않습니다.');
            return;
        }
        param.userPs = Buffer.from(saveParams.userPs).toString('base64');
    }
    if (param.safewizProYn == null) param.safewizProYn = saveParams.fcmToken === null || saveParams.fcmToken === '' ? 'N' : 'Y';
    saveMyInfo(param, true)
        .then(res => {
            searchData(false);
            initOption();
        })
        .finally(() => {});
};

const initOption = () => {
    isPasswordChange.value = false;
    isPasswordVisible.value = false;
    isPasswordConfirmVisible.value = false;
    saveParams.userPs = null;
    saveParams.passwordConfirm = null;
};

// 비밀번호 변경 관련
const isPasswordChange = ref(false);
const isPasswordVisible = ref(false);
const isPasswordConfirmVisible = ref(false);

const chromeYnClick = () => {
    alertMsg('warning', '크롬(Chrome) 브라우저 설정에서 알림 권한을\n설정하십시오.');
};

const allowPermission = async () => {
    try {
        const res = await fcmStore.requestPermission();
        //console.log("allowPermission :: res=",res);
        if (res === true || res === 'granted') {
            saveParams.chromeYn = 'Y';
        } else if (res === 'denied') {
            saveParams.chromeYn = 'N';
        } else if (res === 'default') {
            saveParams.chromeYn = '';
        }
    } catch (error) {
        // 사용자가 여러 번 알림 허용 팝업을 무시하거나 닫으면, 브라우저가 자동으로 권한 요청을 차단한 상태가 됨
        console.error('permission request failed:', error);
        saveParams.chromeYn = 'N';
    }
    //console.log("allowPermission::saveParams.chromeYn="+saveParams.chromeYn)
};

// 토큰 관리 관련 --------------------
const tokenMngPopup = ref(null);
// 사용자 토큰 관리 버튼 클릭
const tokenType = ref(null);
const clickTokenMng = type => {
    tokenType.value = type;
    tokenMngPopup.value.onOpen();
};

//토큰 팝업 닫기
const closeTokenMngPopup = () => {
    tokenMngPopup.value.onClose();
};

onMounted(async () => {
    searchData(true);
});
</script>
