<template>
    <!-- 비밀번호 찾기 (기능만 동작) -->
    <div class="join df aic jcc">
        <!-- <img src="/assets/img/common/safewiz_pro_logo.svg" alt="Logo" class="pa t6rem l6rem lg-l50p lg-neg-ttx50p" /> -->
        <div v-if="!changePw" class="segment w47rem es-w90vw">
            <div class="box w680px us-w300px h80p bcF8F9FB pa b0 l50p neg-ttx50p px14rem pt9rem el-w500px el-px8rem el-pt8rem lg-maxh600px lg-h80p us-w100p us-px4rem lg-bc">
                <h2>비밀번호 찾기</h2>
                <div class="field" ref="userIdInput">
                    <label for="userid" required>아이디</label>
                    <input vali-msg="*아이디가 제대로 입력되지 않았습니다." v-model="userInfo.userId" placeholder="아이디를 입력해주세요" @keydown="clearError('userIdInput')" />
                </div>
                <div class="field" ref="emailInput">
                    <label for="email" required>이메일</label>
                    <input type="email" vali-msg="*이메일이 제대로 입력되지 않았습니다." class="cert" v-model="userInfo.email" placeholder="이메일을 입력해주세요" @keydown="clearError('emailInput')" @input="validateEmail" />
                    <!-- active 클래스 토글 -->
                    <button type="button" @click="sendMail()" class="cert-btn active"><span>인증 요청</span></button>
                </div>
                <div class="field" ref="authenticationInput">
                    <label for="authentication" required>인증코드(현재 사용안함)</label>
                    <input vali-msg="*인증코드가 제대로 입력되지 않았습니다." class="cert" v-model="userInfo.authentication" placeholder="인증코드 6자리" @keydown="clearError('authenticationInput')" disabled />
                    <!-- active 클래스 토글 -->
                    <button type="button" class="cert-btn"><span>인증 확인</span></button>
                </div>
                <div class="form ui df mt3-2rem">
                    <button type="button" @click="toLogin()" v-button class="btn radius w50p mr4px"><span>취소</span></button>
                    <button type="button" v-button class="btn active radius w50p ml4px"><span>비밀번호 변경</span></button>
                </div>
            </div>
        </div>
        <div v-if="changePw" class="segment w47rem es-w90vw">
            <div class="box w680px us-w300px h80p bcF8F9FB pa b0 l50p neg-ttx50p px14rem pt9rem el-w500px el-px8rem el-pt8rem lg-maxh600px lg-h80p us-w100p us-px4rem lg-bc">
                <h2>비밀번호 변경</h2>
                <template v-if="true">
                    <div class="field" ref="newUserPsInput">
                        <label for="pw" required>새로운 비밀번호</label>
                        <input v-input v-model="userInfo.newUserPs" vali-msg="*비밀번호가 제대로 입력되지 않았습니다." :type="isNewPasswordVisible ? 'text' : 'password'" id="newUserPs" class="password w100p" placeholder="패스워드를 입력해주세요." @keydown="clearError('newUserPsInput')" />
                        <button type="button" @click="isNewPasswordVisible = !isNewPasswordVisible">
                            <img :src="isNewPasswordVisible ? '/assets/img/common/icon_eye_close_dark.svg' : '/assets/img/common/icon_eye_dark.svg'" alt="Toggle Password Visibility" />
                        </button>
                    </div>
                    <div class="field" ref="newUserPsCheckInput">
                        <label for="pw" required>비밀번호 확인</label>
                        <input v-input v-model="userInfo.newUserPsCheck" vali-msg="*비밀번호가 일치하지 않습니다." :type="isNewPasswordCheckVisible ? 'text' : 'password'" id="newUserPsCheckInput" class="password w100p" placeholder="패스워드를 입력해주세요." @keydown="clearError('newUserPsCheckInput')" />
                        <button type="button" @click="isNewPasswordCheckVisible = !isNewPasswordCheckVisible">
                            <img :src="isNewPasswordCheckVisible ? '/assets/img/common/icon_eye_close_dark.svg' : '/assets/img/common/icon_eye_dark.svg'" alt="Toggle Password Visibility" />
                        </button>
                    </div>
                    <p class="fs1-4rem fw300 lh1-5">* 비밀번호는 X ~ X자로 영문 대 소문자, 숫자, 특수기호를 조합해서 13자 이상 입력해주세요.</p>
                    <div class="form ui df mt3-2rem">
                        <button type="button" @click="toLogin()" v-button class="btn radius w50p mr4px"><span>취소</span></button>
                        <button type="button" @click="modify()" v-button class="btn active radius w50p ml4px"><span>비밀번호 변경</span></button>
                    </div>
                </template>

                <template v-else>
                    <div class="br4px bcF8F9FB h15rem df aic jcc fs1-6rem lh1-5">
                        <p class="tac">
                            회원님의<br />
                            비밀번호 변경이 완료되엇습니다.
                        </p>
                    </div>
                    <div class="form ui df mt3-2rem">
                        <button type="button" @click="toLogin()" v-button class="btn active radius w100p"><span>취소</span></button>
                    </div>
                </template>
            </div>
        </div>
        <p class="tac fs1-4rem pa b5rem l50p neg-ttx50p">Copyright © safewizpro All Rights reserved</p>
    </div>
</template>
<script setup>
import { Buffer } from 'buffer';
import BaseView from '@/components/base/BaseView.js';
import { findPassword } from '@/stores/system/user/User.js';
import { modifyPassword } from '@/stores/system/user/my/MyPage.js';
const { ref, router, alertMsg } = BaseView();

const isNewPasswordVisible = ref(false);
const isNewPasswordCheckVisible = ref(false);

const userIdInput = ref(null);
const emailInput = ref(null);
const newUserPsInput = ref(null);
const newUserPsCheckInput = ref(null);

const changePw = ref(false);

const userInfo = ref({
    userId: '',
    email: '',
    authentication: '',
    newUserPs: '',
    newUserPsCheck: ''
});

const toLogin = () => {
    router.push('/login');
};

const sendMail = () => {
    if (userInfo.value.userId !== '') {
        if (userInfo.value.email !== '') {
            const emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
            if (emailPattern.test(userInfo.value.email)) {
                //TODO: 메일 인증 들어가야됨
                // sendLink(userInfo.value.userId)
                //     .then(res => {
                //         alertMsg('info', '비밀번호 초기화 링크가</br>이메일로 전송되었습니다.');
                //         toLogin();
                //     })
                //     .catch(() => {
                //         alertMsg('warning', '존재하지않는 사용자입니다.');
                //         return;
                //     });
                const data = {
                    email: userInfo.value.email,
                    userId: userInfo.value.userId
                };

                findPassword(data).then(res => {
                    console.log('data:::', res);
                    if (res) {
                        alertMsg('info', '인증이 완료되었습니다.\n비밀번호를 변경해주세요.');
                        changePw.value = true;
                    } else {
                        alertMsg('warning', '존재하지않는 사용자입니다.');
                    }
                });
            } else {
                showError(emailInput.value);
            }
        } else {
            showError(emailInput.value);
        }
    } else {
        showError(userIdInput.value);
    }
};

const modify = () => {
    if (userInfo.value.newUserPs !== '' && userInfo.value.newUserPs.length >= 8) {
        if (userInfo.value.newUserPs === userInfo.value.newUserPsCheck) {
            const data = {
                password: '', //password는 빈값으로 보내면 됨
                newPassword: Buffer.from(userInfo.value.newUserPs).toString('base64'),
                userId: '' //로그인한 user id 들고 와야댐
            };
            modifyPassword(data).then(res => {
                console.log('res', res);
                if (res.result) {
                    alertMsg('info', '비밀번호가 변경되었습니다.\n로그인해주세요.');
                    toLogin();
                } else {
                    alertMsg('warning', '오류가 발생하였습니다.\n관리자에게 문의하세요.');
                }
            });
        } else {
            showError(newUserPsCheckInput.value);
        }
    } else {
        showError(newUserPsInput.value);
    }
};

const showError = el => {
    if (!el) return;
    el.classList.add('error');
};

const clearError = refName => {
    const el = eval(refName).value;
    if (el.classList.contains('error')) {
        el.classList.remove('error');
    }
};
</script>
<style lang="scss">
label[required] {
    position: relative;

    &::after {
        display: inline-block;
        right: 2px;
        top: -2px;
        content: '*';
        color: #ff4f4f;
    }
}

.error {
    color: red;
}
.valid {
    color: green;
}
</style>
