<template>
    <div class="login h100vh df aic jcc">
        <div class="segment w360px us-w300px">
            <h1 class="tac mb7rem"><img src="/assets/img/common/logo.svg" alt="" /></h1>
            <div class="field mb2rem" ref="idInput">
                <label for="id" class="db fs1-6rem c255 fw300">User ID</label>
                <input v-input vali-msg="*아이디가 제대로 입력되지 않았습니다." type="text" id="" class="w100p" v-model="userInfo.userId" @keyup.enter="signIn" @keydown="clearError('idInput')" placeholder="사용자 아이디를 입력해주세요." />
            </div>
            <div class="field" ref="passwordInput">
                <label for="pw" class="db fs1-6rem c255 fw300">Password</label>
                <input v-input vali-msg="*비밀번호가 제대로 입력되지 않았습니다." :type="isPasswordVisible ? 'text' : 'password'" id="" class="w100p" v-model="userInfo.userPs" @keyup.enter="signIn" @keydown="clearError('passwordInput')" placeholder="패스워드를 입력해주세요." />
                <button type="button" @click="isPasswordVisible = !isPasswordVisible">
                    <img :src="isPasswordVisible ? '/assets/img/common/icon_eye_close.svg' : '/assets/img/common/icon_eye.svg'" alt="Toggle Password Visibility" />
                </button>
            </div>
            <ul class="row flex ui form w100p mt2-5rem mb3rem pl2-5rem pr2-5rem">
                <li class="col12-6">
                    <input v-input="'자동로그인'" type="checkbox" v-model="userInfo.remember" />
                </li>
                <li class="col12-6 tar">
                    <button @click="toFindUserPs()" class="fs1-4rem c255">비밀번호 찾기</button>
                    <button @click="toSignUp()" class="fs1-4rem c43D491 pl6px">회원가입</button>
                </li>
            </ul>
            <div class="form ui">
                <button type="button" class="btn w100p lg radius green" v-button @click="signIn()"><span>로그인</span></button>
            </div>
        </div>
    </div>
</template>

<script setup>
import BaseView from '@/components/base/BaseView.js';
const { ref, onMounted, router } = BaseView();

import { useUserInfoStore } from '@/stores/user.js';
const userStore = useUserInfoStore();
import { useAuthStore } from '@/stores/auth.js';
const authStore = useAuthStore();

const idInput = ref(null);
const passwordInput = ref(null);
const idError = ref('');
const passwordError = ref('');

const userInfo = ref({
    userId: userStore.rememberUser() ? userStore.rememberUser() : '',
    userPs: '',
    remember: false,
    autoLogin: false
});

onMounted(() => {
    if (userStore.rememberUser()) {
        userInfo.value.remember = true;
    }
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

const signIn = () => {
    if (userInfo.value.userId !== '') {
        if (userInfo.value.userPs !== '') {
            authStore.handleLogout();
        } else {
            showError(passwordInput.value, '*비밀번호가 제대로 입력되지 않았습니다.', passwordError);
        }
    } else {
        showError(idInput.value, '*아이디가 제대로 입력되지 않았습니다.', idError);
    }
};

const toSignUp = () => {
    router.push({
        name: 'BaseSignUp'
    });
};

const toFindUserPs = () => {
    router.push({
        name: 'FindUserPs'
    });
};
</script>
