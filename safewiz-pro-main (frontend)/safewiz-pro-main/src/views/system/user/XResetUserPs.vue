<template>
    <!-- 비밀번호 초기화 -->
    <div class="join df aic jcc">
        <div class="segment w47rem es-w90vw">
            <div class="box">
                <h2>비밀번호 초기화</h2>
                <div class="field" ref="userPsInput">
                    <label for="pw" required>새로운 비밀번호</label>
                    <input v-input v-model="userInfo.userPs" vali-msg="*비밀번호가 제대로 입력되지 않았습니다." :type="isPasswordVisible ? 'text' : 'password'" id="" class="w100p" placeholder="패스워드를 입력해주세요." @keydown="clearError('userPsInput')" />
                    <button type="button" @click="isPasswordVisible = !isPasswordVisible">
                        <img :src="isPasswordVisible ? '/assets/img/common/icon_eye_close_dark.svg' : '/assets/img/common/icon_eye_dark.svg'" alt="Toggle Password Visibility" />
                    </button>
                </div>
                <div class="field" ref="userPsCheckInput">
                    <label for="pw" required>비밀번호 확인</label>
                    <input v-input v-model="userInfo.userPsCheck" vali-msg="*비밀번호가 일치하지 않습니다." :type="isPasswordCheckVisible ? 'text' : 'password'" id="" class="w100p" placeholder="패스워드를 입력해주세요." @keydown="clearError('userPsCheckInput')" />
                    <button type="button" @click="isPasswordCheckVisible = !isPasswordCheckVisible">
                        <img :src="isPasswordCheckVisible ? '/assets/img/common/icon_eye_close_dark.svg' : '/assets/img/common/icon_eye_dark.svg'" alt="Toggle Password Visibility" />
                    </button>
                </div>
            </div>
            <div class="form ui df mt12px">
                <button type="button" @click="toLogin()" v-button class="btn ul radius w50p mr6px dark"><span>취소</span></button>
                <button type="button" @click="initialize()" v-button class="btn ul radius w50p ml6px green"><span>저장</span></button>
            </div>
        </div>
    </div>
</template>

<script setup>
import { Buffer } from 'buffer';
import BaseView from '@/components/base/BaseView.js'; // BaseView
const { ref, onMounted, route, router, alertMsg } = BaseView();
import { getFindUserPs, initPassword } from '@/stores/system/user/User.js';

const isPasswordVisible = ref(false);
const isPasswordCheckVisible = ref(false);

const userPsInput = ref(null);
const userPsCheckInput = ref(null);

const userInfo = ref({
    userPs: '',
    userPsCheck: ''
});

const initialize = () => {
    if (userInfo.value.userPs !== '' && userInfo.value.userPs.length >= 8) {
        if (userInfo.value.userPs === userInfo.value.userPsCheck) {
            initPassword(route.query.key, Buffer.from(userInfo.value.userPs).toString('base64')).then(res => {
                if (res.data) {
                    alertMsg('info', '비밀번호가 변경되었습니다.');
                    toLogin();
                } else {
                    alertMsg('warning', '유효하지 못한 접근입니다.');
                }
            });
        } else {
            showError(userPsCheckInput.value);
        }
    } else {
        showError(userPsInput.value);
    }
};

const toLogin = () => {
    router.push({
        path: '/'
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

onMounted(async () => {
    try {
        getFindUserPs(route.query.key).then(res => {
            if (!res.data) {
                router.push({
                    path: '/'
                });
                alertMsg('warning', '유효하지 못한 접근입니다.');
            }
        });
    } catch (error) {
        console.error('Error fetching data:', error);
    }
});
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
