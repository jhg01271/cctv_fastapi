<template>
    <!-- 비밀번호 초기화 -->
    <div class="join df aic jcc">
        <div class="segment w47rem es-w90vw">
            <div class="box">
                <h2>비밀번호 변경</h2>
                <div class="field" ref="userPsInput">
                    <label for="pw" required>현재 비밀번호</label>
                    <input v-input v-model="userInfo.userPs" vali-msg="*비밀번호가 제대로 입력되지 않았습니다." :type="isPasswordVisible ? 'text' : 'password'" id="" class="w100p" placeholder="패스워드를 입력해주세요." @keydown="clearError('userPsInput')" />
                    <button type="button" @click="isPasswordVisible = !isPasswordVisible">
                        <img :src="isPasswordVisible ? '/assets/img/common/icon_eye_close_dark.svg' : '/assets/img/common/icon_eye_dark.svg'" alt="Toggle Password Visibility" />
                    </button>
                </div>
                <div class="field" ref="newUserPsInput">
                    <label for="pw" required>새로운 비밀번호</label>
                    <input v-input v-model="userInfo.newUserPs" vali-msg="*비밀번호가 제대로 입력되지 않았습니다." :type="isNewPasswordVisible ? 'text' : 'password'" id="" class="w100p" placeholder="패스워드를 입력해주세요." @keydown="clearError('newUserPsInput')" />
                    <button type="button" @click="isNewPasswordVisible = !isNewPasswordVisible">
                        <img :src="isNewPasswordVisible ? '/assets/img/common/icon_eye_close_dark.svg' : '/assets/img/common/icon_eye_dark.svg'" alt="Toggle Password Visibility" />
                    </button>
                </div>
                <div class="field" ref="newUserPsCheckInput">
                    <label for="pw" required>비밀번호 확인</label>
                    <input v-input v-model="userInfo.newUserPsCheck" vali-msg="*비밀번호가 일치하지 않습니다." :type="isNewPasswordCheckVisible ? 'text' : 'password'" id="" class="w100p" placeholder="패스워드를 입력해주세요." @keydown="clearError('newUserPsCheckInput')" />
                    <button type="button" @click="isNewPasswordCheckVisible = !isNewPasswordCheckVisible">
                        <img :src="isNewPasswordCheckVisible ? '/assets/img/common/icon_eye_close_dark.svg' : '/assets/img/common/icon_eye_dark.svg'" alt="Toggle Password Visibility" />
                    </button>
                </div>
            </div>
            <div class="form ui df mt12px">
                <button type="button" @click="toMypage()" v-button class="btn ul radius w50p mr6px dark"><span>취소</span></button>
                <button type="button" @click="modify()" v-button class="btn ul radius w50p ml6px green"><span>저장</span></button>
            </div>
        </div>
    </div>
</template>

<script setup>
import { Buffer } from 'buffer';
import BaseView from '@/components/base/BaseView.js'; // BaseView
const { ref, router, alertMsg } = BaseView();
import { modifyPassword } from '@/stores/system/user/my/MyPage.js';

const isPasswordVisible = ref(false);
const isNewPasswordVisible = ref(false);
const isNewPasswordCheckVisible = ref(false);

const userPsInput = ref(null);
const newUserPsInput = ref(null);
const newUserPsCheckInput = ref(null);

const userInfo = ref({
    userPs: '',
    newUserPs: '',
    newUserPsCheck: ''
});

const modify = () => {
    if (userInfo.value.newUserPs !== '' && userInfo.value.newUserPs.length >= 8) {
        if (userInfo.value.newUserPs === userInfo.value.newUserPsCheck) {
            const data = {
                password: Buffer.from(userInfo.value.userPs).toString('base64'),
                newPassword: Buffer.from(userInfo.value.newUserPs).toString('base64'),
                userId: '' //로그인한 user id 들고 와야댐
            };
            modifyPassword(data).then(res => {
                if (res.data) {
                    alertMsg('info', '비밀번호가 변경되었습니다.');
                    toMypage();
                } else {
                    alertMsg('warning', '현재 비밀번호가 일치하지 않습니다.');
                    showError(userPsInput.value);
                }
            });
        } else {
            showError(newUserPsCheckInput.value);
        }
    } else {
        showError(newUserPsInput.value);
    }
};

const toMypage = () => {
    router.push({
        name: 'BaseMyPage'
    });
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
