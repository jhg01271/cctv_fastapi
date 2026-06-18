<template>
    <div class="login h100vh df aic jcc">
        <div class="login-bg w100p h100p df">
            <div class="w30p h100p bcFFFFFF lg-w100p"></div>
            <div class="w70p h100p bc3248F6 pr lg-dn">
                <div class="df aic jcc h100p pl15rem ul-fdc">
                    <img src="/assets/img/common/login.png" />
                    <div>
                        <p class="fs2-8rem lh1-2 cffffff">
                            세이프위즈 프로를 통해
                            <br />
                            <em class="fs4rem">안전에 안전을 더하세요!</em>
                        </p>
                        <p class="fs1-6rem c49FFBF mt6-6rem">ADD SAFETY TO SAFETY WITH SAFEWIZ PRO!</p>
                    </div>
                </div>
                <div class="marquee pf t0 r3rem h100vh w228px">
                    <img src="/assets/img/common/safewizpro.svg" alt="safewiz pro" class="w228px h1250px" />
                </div>
            </div>
        </div>

        <img src="/assets/img/common/logo.svg" alt="Logo" class="pa maxw30rem t6rem l6rem lg-l50p lg-neg-ttx50p md-w50vw es-w60vw us-w70vw" />

        <div class="segment w680px us-w300px h80p bcF8F9FB pa b0 l30p neg-ttx50p px14rem pt15rem el-maxw500px el-px8rem el-pt10rem lg-maxh600px lg-h80p lg-l50p us-w100p us-px4rem lg-bc">
            <h1 class="fs2-2rem fw600 tac mb5rem">Login to safewiz pro</h1>
            <div class="field mb1-2rem" ref="idInput">
                <input ref="inputIdRef" v-input vali-msg="*아이디를 입력하세요." type="text" id class="w100p" v-model="userInfo.userId" @keyup.enter="signInAction" @keydown="clearError('idInput')" placeholder="아이디" />
            </div>
            <div class="field mb3-2rem" ref="passwordInput">
                <input ref="inputPwRef" v-input vali-msg="*비밀번호를 입력하세요." class="password w100p" :type="isPasswordVisible ? 'text' : 'password'" id v-model="userInfo.userPs" @keyup.enter="signInAction" @keydown="clearError('passwordInput')" placeholder="비밀번호" />
                <button type="button" @click="isPasswordVisible = !isPasswordVisible">
                    <img :src="isPasswordVisible ? '/assets/img/common/icon_eye_close_dark.svg' : '/assets/img/common/icon_eye_dark.svg'" alt="Toggle Password Visibility" />
                </button>
            </div>

            <div class="form ui">
                <button type="button" class="btn w100p lg radius active" v-button @click="signInAction()">
                    <span>LOGIN</span>
                </button>
            </div>
            <ul class="df jcsb aic ui form w100p mt3rem">
                <li>
                    <input v-input="'아이디 저장'" type="checkbox" v-model="userInfo.autoId" />
                </li>
                <!-- TODO 추후 기능 오픈 예정 -->
                <!-- <li>
                    <button @click="toFindUserId()" class="fs1-6rem c255">
                        <span>아이디 찾기</span>
                    </button>
                    <div class="dib w1px h9px bc000000 mx1-2rem"></div>
                    <button @click="toFindUserPs()" class="fs1-6rem c255">
                        <span>비밀번호 찾기</span>
                    </button>
                </li> -->
            </ul>
            <p class="tac fs1-4rem pa b5rem l50p neg-ttx50p">Copyright © safewizpro All Rights reserved</p>
        </div>
    </div>

    <!-- <span class="login-footer-menu" @click="handleRouterName('B')">{{ t('TermsService') }}</span>
    <span class="login-footer-divider"></span>
    <span class="login-footer-menu" @click="handleRouterName('P')">{{ t('UserPrivacy') }}</span>-->
</template>

<script setup>
import BaseView from '@/components/base/BaseView';
const { ref, onMounted, alertMsg } = BaseView();
import { Buffer } from 'buffer';
import { signin } from '@/api/base/common/auth';
import router from '@/router';
import { useRoute } from 'vue-router';
const route = useRoute();

const idInput = ref(null);
const passwordInput = ref(null);
const idError = ref('');
const passwordError = ref('');

const userInfo = ref({
    userId: '',
    userPs: '',
    remember: false,
    autoId: false
});

const inputIdRef = ref(null);
const inputPwRef = ref(null);
onMounted(() => {
    let data = customCookies('get', 'AUTOID');
    if (data) {
        userInfo.value.autoId = AesDecrypt(data) == 'Y';
        if (userInfo.value.autoId) {
            userInfo.value.userId = AesDecrypt(customCookies('get', 'USERID'));
        }
    }
    if (AesDecrypt(customCookies('get', 'USERID'))) {
        if (inputPwRef.value) {
            inputPwRef.value.focus(); // 페이지 마운트 시점에 ID 입력란으로 포커싱 이동
        }
    } else if (inputIdRef.value) {
        inputIdRef.value.focus(); // 페이지 마운트 시점에 ID 입력란으로 포커싱 이동
    }
    console.log(userInfo);
});

const showError = (el, message, errorRef) => {
    if (!el) return;
    el.classList.add('error');
    errorRef.value = message;
};

const clearError = refName => {
    const el = eval(refName).value;
    if (el.classList.contains('error')) {
        el.classList.remove('error');
    }
};

const isPasswordVisible = ref(false);

import { customCookies } from '@/utils/token';
import { AesEncrypt, AesDecrypt } from '@/utils/aes256'; // 프레임워크 전역 글로벌 설정
import { setToken } from '@/utils/token';
import { useFcmStore } from '@/firebase/fcmService.js'; //FCM TOKEN 권한 허용 여부
const fcmStore = useFcmStore();
// 조회조건 : 기존 사업장 조회api(paging)랑 같이 사용
const searchParam = ref({ listSize: 1000, curPage: 1, searchText: '' });
const signInAction = () => {
    if (userInfo.value.userId !== '') {
        if (userInfo.value.userPs !== '') {
            const autoId = AesEncrypt(userInfo.value.autoId ? 'Y' : 'N');
            customCookies('set', 'AUTOID', autoId);
            if (userInfo.value.autoId) {
                customCookies('set', 'USERID', AesEncrypt(userInfo.value.userId));
            } else {
                customCookies('set', 'USERID', '');
            }
            let isToken = false;
            signin({ userId: userInfo.value.userId, userPs: Buffer.from(userInfo.value.userPs).toString('base64') }, false)
                .then(res => {
                    console.log('@@res', res);
                    if (res.success) {
                        isToken = true;
                        setToken(res.result.accessToken);
                        window.sessionStorage.setItem('grpAuthCd', res.result.grpAuthCd);
                    } else {
                        // Login 실패 메시지 출력
                        alertMsg('warning', res.msg);
                    }
                })
                .catch(e => {
                    console.log(e);

                    // Login 실패 메시지 출력
                    alertMsg('warning', e.response.data.msg ? e.response.data.msg : '서버 연결 오류 \n 잠시 후 시도해주세요.');
                })
                .finally(() => {
                    if (isToken) {
                        try {
                            // 사용자에 해당하는 사업장 리스트를 불러와서 세션에 사업장 ID를 암호화 하여 저장함
                            new Promise(resolve => {
                                getCompListByHr(searchParam.value).then(res => {
                                    if (res.success) {
                                        if (res.list.length > 0) {
                                            // 사업장 id , 이름 가져오기
                                            const compId = res.list[0].compId;
                                            const compNm = res.list[0].compNm;
                                            // compId 암호화
                                            const encryptedCompId = AesEncrypt(compId);
                                            sessionStorage.setItem('COMP_ID', encryptedCompId);
                                        }
                                        resolve({ list: res.list, totalCount: res.totalCount });
                                    }
                                });
                            }).then(res => {
                                if (route.redirectedFrom) {
                                    router.push('/dashboard');
                                } else {
                                    router.push('/dashboard');
                                }
                                fcmStore.requestPermission();
                            });
                        } catch (error) {
                            console.error('Error:', error);
                        }
                    }
                });
        } else {
            alertMsg('warning', '비밀번호를 입력하세요.');
            // showError(passwordInput.value, '*비밀번호가 제대로 입력되지 않았습니다.', passwordError);
        }
    } else {
        alertMsg('warning', '아이디를 입력하세요.');
        // showError(idInput.value, '*아이디가 제대로 입력되지 않았습니다.', idError);
    }
};

const toSignUp = () => {
    router.push({
        name: 'BaseSignUp'
    });
};

const toFindUserId = () => {
    router.push({
        name: 'FindUserId'
    });
};

const toFindUserPs = () => {
    router.push({
        name: 'FindUserPs'
    });
};
/**
 * marquee 애니메이션
 */
const speed = 2; // 이동 속도
let clones = []; // 클론된 이미지 배열
import { nextTick } from 'vue';
import { getCompListByHr } from '@/stores/system/setting/api/compApi.js';
onMounted(() => {
    nextTick(() => {
        const marquee = document.querySelector('.marquee');
        const image = document.querySelector('.marquee img');
        const imgHeight = 1250 + 100;

        image.style.top = '0px';
        clones.push(image);

        // 이미지 클론 생성 및 추가
        const clone = image.cloneNode(true);
        clone.style.top = `${imgHeight}px`;
        marquee.appendChild(clone);
        clones.push(clone);

        // 애니메이션 함수
        function animate() {
            clones.forEach(clone => {
                let top = parseFloat(clone.style.top);
                top -= speed; // 위로 이동
                // console.log('clones', top, imgHeight, top + imgHeight <= 0);
                if (top <= -imgHeight) {
                    // 가장 아래 이미지의 위치를 가져와 그 뒤로 이동
                    const maxTop = Math.max(...clones.map(c => parseFloat(c.style.top)));
                    top = maxTop + imgHeight;
                }
                clone.style.top = `${top}px`;
            });

            requestAnimationFrame(animate);
        }

        // 애니메이션 시작
        animate();
    });
});
</script>
