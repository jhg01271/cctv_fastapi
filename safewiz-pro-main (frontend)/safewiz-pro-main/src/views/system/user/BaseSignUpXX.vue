<template>
    <!-- 회원가입 -->
    <div class="join df aic jcc">
        <div class="segment w47rem es-w90vw">
            <div class="box">
                <h2>회원가입</h2>
                <div class="field" ref="userIdInput">
                    <label for="userid" required>아이디 (이메일)</label>
                    <input type="email" vali-msg="*아이디가 제대로 입력되지 않았습니다." v-model="userInfo.userId" placeholder="아이디를 입력해주세요" @keydown="clearError('userIdInput')" @input="validateEmail" :readonly="isVerification" />
                    <button type="button" @click="sendVerificationMail" :disabled="isVerification">{{ isSend ? '재발송' : '인증' }}</button>
                </div>
                <div class="field" v-if="isSend && !isVerification">
                    <label for="certification">인증번호</label>
                    <input type="text" v-model="userInfo.certificationCode" placeholder="인증번호를 입력해주세요" :readonly="isVerification" />
                    <button type="button" @click="verify" :disabled="isVerification">확인</button>
                </div>
                <div class="field" ref="userPsInput">
                    <label for="pw" required>비밀번호</label>
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
                <div class="field" ref="userNmInput">
                    <label for="userNm" required>이름</label>
                    <input type="text" vali-msg="*이름이 입력되지 않았습니다." placeholder="이름을 입력해주세요." v-model="userInfo.userNm" @keydown="clearError('userNmInput')" />
                </div>
                <div class="field">
                    <label for="phone">연락처</label>
                    <input type="text" v-model="userInfo.phone" placeholder="연락처를 입력해주세요." />
                </div>
                <div class="field">
                    <label for="inviteKey">사업장 키</label>
                    <input type="text" v-model="userInfo.inviteKey" placeholder="사업장 키를 입력해주세요" />
                </div>
                <!-- <ul class="form ui">
                    <li class="df aic">
                        <input type="checkbox" v-input class="shrink0" id="checkbox3" @change="toggleActive($event, 1)" />
                        <label for="checkbox1" class="w100p ml1rem mr1rem us-fs1-4rem"><span>[필수] 14세 이상 약관에 동의합니다.</span></label>
                        <button type="button" class="shrink0 es-fs1-5rem" @click="openPopup('age')"><span>[보기]</span></button>
                    </li>
                    <li class="df aic">
                        <input type="checkbox" v-input class="shrink0" id="checkbox3" @change="toggleActive($event, 2)" />
                        <label for="checkbox2" class="w100p ml1rem mr1rem us-fs1-4rem"><span>[필수] 서비스 이용약관에 동의합니다. </span></label>
                        <button type="button" class="shrink0 es-fs1-5rem" @click="openPopup('service')"><span>[보기]</span></button>
                    </li>
                    <li class="df aic">
                        <input type="checkbox" v-input class="shrink0" id="checkbox3" @change="toggleActive($event, 3)" />
                        <label for="checkbox3" class="w100p ml1rem mr1rem us-fs1-4rem"><span>[필수] 개인정보 이용에 동의합니다.</span></label>
                        <button type="button" class="shrink0 es-fs1-5rem" @click="openPopup('personalInfo')"><span>[보기]</span></button>
                    </li>
                </ul> -->
            </div>
            <div class="form ui df mt12px">
                <button type="button" v-button class="btn ul radius w50p mr6px dark" @click="toLogin"><span>취소</span></button>
                <!-- disabled 처리 시 스타일 적용 되어있습니다. -->
                <button type="button" v-button class="btn ul radius w50p ml6px green" @click="signUp"><span>회원가입</span></button>
            </div>
        </div>
    </div>

    <!-- 약관 팝업 -->
    <i-PopupDialog ref="age" @close="closePopup('age')" @open="openPopup('age')">
        <div class="contents w472px ul-w100p">
            <h3 class="mb3remi">[필수] 14세 이상 약관동의서</h3>
            <div class="h4 oya h30rem pr3rem">
                <p>
                    네이버는 www.naver.com을 비롯한 네이버 도메인의 웹사이트 및 응용프로그램(어플리케이션, 앱)을 통해 정보 검색, 다른 이용자와의 커뮤니케이션, 콘텐츠 제공, 상품 쇼핑 등 여러분의 생활에 편리함을 더할 수 있는 다양한 서비스를 제공하고 있습니다. 여러분은 PC, 휴대폰 등 인터넷 이용이 가능한 각종 단말기를 통해 각양각색의 네이버 서비스를 자유롭게 이용하실 수 있으며, 개별 서비스들의 구체적인 내용은 각 서비스 상의 안내, 공지사항, 네이버 웹고객센터(이하‘고객센터’) 도움말 등에서 쉽게 확인하실 수 있습니다. <br /><br />

                    네이버는 기본적으로 여러분 모두에게 동일한 내용의 서비스를 제공합니다. 다만,'청소년보호법'등 관련 법령이나 기타 개별 서비스 제공에서의 특별한 필요에 의해서 연령 또는 일정한 등급을 기준으로 이용자를 구분하여 제공하는 서비스
                </p>
            </div>

            <div class="form ui df mt12px">
                <button type="button" v-button class="btn ul radius w100p dark" @click="closePopup('age')"><span>닫기</span></button>
            </div>
        </div>
    </i-PopupDialog>

    <!-- 약관 팝업 -->
    <i-PopupDialog ref="service" @close="closePopup('service')" @open="openPopup('service')">
        <div class="contents w472px ul-w100p">
            <h3 class="mb3remi">[필수] 서비스 이용약관 동의서</h3>
            <div class="h4 oya h30rem pr3rem">
                <p>
                    네이버는 www.naver.com을 비롯한 네이버 도메인의 웹사이트 및 응용프로그램(어플리케이션, 앱)을 통해 정보 검색, 다른 이용자와의 커뮤니케이션, 콘텐츠 제공, 상품 쇼핑 등 여러분의 생활에 편리함을 더할 수 있는 다양한 서비스를 제공하고 있습니다. 여러분은 PC, 휴대폰 등 인터넷 이용이 가능한 각종 단말기를 통해 각양각색의 네이버 서비스를 자유롭게 이용하실 수 있으며, 개별 서비스들의 구체적인 내용은 각 서비스 상의 안내, 공지사항, 네이버 웹고객센터(이하‘고객센터’) 도움말 등에서 쉽게 확인하실 수 있습니다. <br /><br />

                    네이버는 기본적으로 여러분 모두에게 동일한 내용의 서비스를 제공합니다. 다만,'청소년보호법'등 관련 법령이나 기타 개별 서비스 제공에서의 특별한 필요에 의해서 연령 또는 일정한 등급을 기준으로 이용자를 구분하여 제공하는 서비스
                </p>
            </div>

            <div class="form ui df mt12px">
                <button type="button" v-button class="btn ul radius w100p dark" @click="closePopup('service')"><span>닫기</span></button>
            </div>
        </div>
    </i-PopupDialog>

    <!-- 약관 팝업 -->
    <i-PopupDialog ref="personalInfo" @close="closePopup('personalInfo')" @open="openPopup('personalInfo')">
        <div class="contents w472px ul-w100p">
            <h3 class="mb3remi">[필수] 개인정보 이용 동의서</h3>
            <div class="h4 oya h30rem pr3rem">
                <p>
                    네이버는 www.naver.com을 비롯한 네이버 도메인의 웹사이트 및 응용프로그램(어플리케이션, 앱)을 통해 정보 검색, 다른 이용자와의 커뮤니케이션, 콘텐츠 제공, 상품 쇼핑 등 여러분의 생활에 편리함을 더할 수 있는 다양한 서비스를 제공하고 있습니다. 여러분은 PC, 휴대폰 등 인터넷 이용이 가능한 각종 단말기를 통해 각양각색의 네이버 서비스를 자유롭게 이용하실 수 있으며, 개별 서비스들의 구체적인 내용은 각 서비스 상의 안내, 공지사항, 네이버 웹고객센터(이하‘고객센터’) 도움말 등에서 쉽게 확인하실 수 있습니다. <br /><br />

                    네이버는 기본적으로 여러분 모두에게 동일한 내용의 서비스를 제공합니다. 다만,'청소년보호법'등 관련 법령이나 기타 개별 서비스 제공에서의 특별한 필요에 의해서 연령 또는 일정한 등급을 기준으로 이용자를 구분하여 제공하는 서비스
                </p>
            </div>

            <div class="form ui df mt12px">
                <button type="button" v-button class="btn ul radius w100p dark" @click="closePopup('personalInfo')"><span>닫기</span></button>
            </div>
        </div>
    </i-PopupDialog>
</template>

<script setup>
import BaseView from '@/components/base/BaseView.js'; // BaseView
const { ref, router, alertMsg } = BaseView();
import { sendMail, verifyCertificaiton } from '@/stores/system/user/User.js';
// import { useAuthStore } from '@/stores/auth.js';
// const authStore = useAuthStore();

const age = ref(null); // 나이 PopupDialog에 대한 ref
const service = ref(null); // 서비스 이용 PopupDialog에 대한 ref
const personalInfo = ref(null); // 개인정보 이용 PopupDialog에 대한 ref

const userIdInput = ref(null);
const userNmInput = ref(null);
const userPsInput = ref(null);
const userPsCheckInput = ref(null);

const isPasswordVisible = ref(false);
const isPasswordCheckVisible = ref(false);

const isSend = ref(false);
const isVerification = ref(false); // email 인증 관련

const userInfo = ref({
    userId: '',
    userPs: '',
    userPsCheck: '',
    userNm: '',
    phone: '',
    certificationCode: '',
    check1: 'N',
    check2: 'N',
    check3: 'N',
    inviteKey: ''
});

const signUp = () => {
    console.log(userInfo.value.userPs);
    if (isVerification.value) {
        if (userInfo.value.userPs !== '' && userInfo.value.userPs.length >= 8) {
            if (userInfo.value.userPs === userInfo.value.userPsCheck) {
                if (userInfo.value.userNm !== '') {
                    // authStore.handleSignUp(userInfo.value).then(res => {
                    //     if (res.success) {
                    //         alertMsg('info', '회원가입이 완료되었습니다.');
                    //         router.push({ path: '/' });
                    //     }
                    // });
                } else {
                    showError(userNmInput.value);
                }
            } else {
                showError(userPsCheckInput.value);
            }
        } else {
            showError(userPsInput.value);
        }
    } else {
        showError(userIdInput.value);
    }
};

const certificationToken = ref('');

const sendVerificationMail = () => {
    if (userInfo.value.userId !== '') {
        const emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
        if (emailPattern.test(userInfo.value.userId)) {
            isSend.value = true;
            certificationToken.value = '';
            alertMsg('info', '인증번호가 이메일로 전송되었습니다.');
            //email 형식 확인 필요
            sendMail(userInfo.value.userId)
                .then(res => {
                    if (res.success) {
                        certificationToken.value = res.data;
                    }
                })
                .catch(error => {
                    console.log('error', error);
                });
        } else {
            showError(userIdInput.value);
        }
    } else {
        showError(userIdInput.value);
    }
};

const verify = () => {
    if (!certificationToken.value) return;
    const data = {
        certificationCode: userInfo.value.certificationCode,
        certificationToken: certificationToken.value
    };
    verifyCertificaiton(data).then(res => {
        if (res.success) {
            if (res.data) {
                isVerification.value = res.data;
                alertMsg('info', '인증되었습니다.');
            } else {
                alertMsg('warning', '인증번호가 일치하지 않습니다.');
            }
        }
    });
};

const toLogin = () => {
    router.push({
        name: 'BaseLogin'
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

const openPopup = type => {
    if (type === 'age') {
        age.value.onOpen();
    }
    if (type === 'service') {
        service.value.onOpen();
    }
    if (type === 'personalInfo') {
        personalInfo.value.onOpen();
    }
};

const closePopup = type => {
    if (type === 'age') {
        age.value.onClose();
    }
    if (type === 'service') {
        service.value.onClose();
    }
    if (type === 'personalInfo') {
        personalInfo.value.onClose();
    }
};

const toggleActive = (event, seq) => {
    const el = event.target.closest('li');
    if (el) {
        el.classList.toggle('active');
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
