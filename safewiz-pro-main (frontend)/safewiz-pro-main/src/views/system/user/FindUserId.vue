<template>
    <!-- 아이디 찾기 (기능만 동작)-->
    <div class="join df aic jcc">
        <!-- <img src="/assets/img/common/safewiz_pro_logo.svg" alt="Logo" class="pa t6rem l6rem lg-l50p lg-neg-ttx50p" /> -->
        <div class="segment w47rem es-w90vw">
            <div class="box w680px us-w300px h80p bcF8F9FB pa b0 l50p neg-ttx50p px14rem pt9rem el-w500px el-px8rem el-pt8rem lg-maxh600px lg-h80p us-w100p us-px4rem lg-bc">
                <h2>아이디 찾기</h2>

                <template v-if="!successFind || userInfo.userId == ''">
                    <div class="field" ref="userNmInput">
                        <label for="userid" required>이름</label>
                        <input vali-msg="*이름이 제대로 입력되지 않았습니다." v-model="userInfo.name" placeholder="이름을 입력해주세요" @keydown="clearError('userNmInput')" />
                    </div>
                    <div class="field" ref="emailInput">
                        <label for="userid" required>이메일</label>
                        <input type="email" vali-msg="*이메일이 제대로 입력되지 않았습니다." v-model="userInfo.email" placeholder="이메일을 입력해주세요" @keydown="clearError('userNmInput')" />
                    </div>
                </template>

                <div v-else class="br4px bcF8F9FB h15rem df aic jcc fs1-6rem lh1-5">
                    <p class="tac">
                        회원님의 아이디는 <br />
                        <span class="c3248F6">{{ userInfo.userId }}</span> 입니다
                    </p>
                </div>

                <div class="form ui df mt3-2rem">
                    <template v-if="!successFind">
                        <button type="button" @click="toLogin()" v-button class="btn radius w50p mr4px">
                            <span>{{ cancelBtnName }}</span>
                        </button>
                        <button type="button" @click="btnFindId()" v-button class="btn active radius w50p ml4px"><span>아이디 찾기</span></button>
                    </template>
                    <template v-else>
                        <button type="button" @click="toFindUserPs()" v-button class="btn radius w50p mr4px">
                            <span>비밀번호찾기</span>
                        </button>
                        <button type="button" @click="toLogin()" v-button class="btn active radius w50p ml4px">
                            <span>로그인화면으로</span>
                        </button>
                    </template>
                </div>
                <p class="tac fs1-4rem pa b5rem l50p neg-ttx50p">Copyright © safewizpro All Rights reserved</p>
            </div>
        </div>
    </div>
</template>
<script setup>
import BaseView from '@/components/base/BaseView.js';
import { findId } from '@/stores/system/user/User.js';
const { ref, router, alertMsg } = BaseView();

const userNmInput = ref(null);
const emailInput = ref(null);

const successFind = ref(false);
const cancelBtnName = ref('취소');
const userInfo = ref({
    email: '',
    name: '',
    userId: ''
});

const toLogin = () => {
    router.push('/login');
};

const toFindUserPs = () => {
    router.push({
        name: 'FindUserPs'
    });
};

const btnFindId = () => {
    if (userInfo.value.name !== '') {
        if (userInfo.value.email !== '') {
            const emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
            if (emailPattern.test(userInfo.value.email)) {
                const data = {
                    email: userInfo.value.email,
                    userNm: userInfo.value.name
                };

                findId(data).then(res => {
                    if (res) {
                        userInfo.value.userId = res.userId;
                        successFind.value = true;
                        cancelBtnName.value = '로그인 화면으로';
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
        showError(userNmInput.value);
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
