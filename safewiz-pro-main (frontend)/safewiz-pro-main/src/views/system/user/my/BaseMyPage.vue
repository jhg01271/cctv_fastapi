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
                <MyPageForm ref="myPageForm" :inputForm="userInfo" :options="{ readonly: true }" />
                <!-- <div class="scroll">
                    <div class="col12-6 lg-col12-12">
                        <div class="ui form h100p lg-ha">
                            <MyPageForm
                                ref="myPageForm"
                                :inputForm="userInfo"
                                :options="{ label: '마이페이지', readonly: true }"
                            />

                            <div class="field mt8px">
                                <label for="userId" required>사용자 사진</label>
                                <div class="box df aic jcc h14-4rem">
                                    <iFileImage ref="fileList" targetType="partnerLogo" />
                                </div>
                            </div>
                        </div>
                    </div>
                </div>-->
            </OverlayScrollbarsComponent>
        </div>
        <div class="btn-group pa r0 t0 fdc">
            <button type="button" v-button>
                <i>
                    <svg
                        width="24"
                        height="24"
                        viewBox="0 0 24 24"
                        fill="none"
                        xmlns="http://www.w3.org/2000/svg"
                    >
                        <path
                            d="M22.6492 19.3562C22.8459 19.55 23.1625 19.5476 23.3562 19.3508C23.55 19.1541 23.5476 18.8375 23.3508 18.6438L22.6492 19.3562ZM1 5.5C0.723858 5.5 0.5 5.72386 0.5 6C0.5 6.27614 0.723858 6.5 1 6.5L1 5.5ZM7.6 6.5C7.87614 6.5 8.1 6.27614 8.1 6C8.1 5.72386 7.87614 5.5 7.6 5.5L7.6 6.5ZM1 10.9167C0.723858 10.9167 0.5 11.1405 0.5 11.4167C0.5 11.6928 0.723858 11.9167 1 11.9167L1 10.9167ZM5.4 11.9167C5.67614 11.9167 5.9 11.6928 5.9 11.4167C5.9 11.1405 5.67614 10.9167 5.4 10.9167V11.9167ZM1 16.3333C0.723858 16.3333 0.5 16.5572 0.5 16.8333C0.5 17.1095 0.723858 17.3333 1 17.3333L1 16.3333ZM7.6 17.3333C7.87614 17.3333 8.1 17.1095 8.1 16.8333C8.1 16.5572 7.87614 16.3333 7.6 16.3333L7.6 17.3333ZM20.1706 11.3529C20.1706 14.026 17.9682 16.2059 15.2353 16.2059V17.2059C18.506 17.2059 21.1706 14.5926 21.1706 11.3529H20.1706ZM15.2353 16.2059C12.5024 16.2059 10.3 14.026 10.3 11.3529H9.3C9.3 14.5926 11.9645 17.2059 15.2353 17.2059V16.2059ZM10.3 11.3529C10.3 8.67991 12.5024 6.5 15.2353 6.5V5.5C11.9645 5.5 9.3 8.11328 9.3 11.3529H10.3ZM15.2353 6.5C17.9682 6.5 20.1706 8.67991 20.1706 11.3529H21.1706C21.1706 8.11328 18.506 5.5 15.2353 5.5V6.5ZM18.7668 15.5327L22.6492 19.3562L23.3508 18.6438L19.4685 14.8202L18.7668 15.5327ZM1 6.5L7.6 6.5L7.6 5.5L1 5.5L1 6.5ZM1 11.9167L5.4 11.9167V10.9167L1 10.9167L1 11.9167ZM1 17.3333L7.6 17.3333L7.6 16.3333L1 16.3333L1 17.3333Z"
                            fill="black"
                        />
                    </svg>
                </i>
                <span>저장</span>
            </button>
        </div>
        <i-PopupDialog ref="dialogCheckUserPs">
            <!-- 단일 그리드 -->
            <div class="contents form ui w500px">
                <CheckUserPsForm
                    :inputForm="inputForm"
                    :options="{ label: '비밀번호 확인', readonly: false }"
                />
                <!-- <i-PopupButtonList
                    :btnInfo="{ saveBtn: true, deleteBtn: false, closeBtn: true }"
                    :options="{ saveBtn: { label: '탈퇴' } }"
                    :localInputForm="inputForm"
                    @save="withdrawalUserAction"
                    @close="closePopup"
                />-->
            </div>
        </i-PopupDialog>
    </div>
</template>
<script setup>
import { Buffer } from 'buffer';
import MyPageForm from '@/views/system/user/my/MyPageForm.vue';
import CheckUserPsForm from '@/views/system/user/my/CheckUserPsForm.vue';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import { searchMyPage, modifyUser, withdrawal } from '@/stores/system/user/my/MyPage.js';
import BaseView from '@/components/base/BaseView.js'; // BaseView
const { ref, onMounted, router, alertMsg, confirmMsg, toastPopup, btnSave } = BaseView();
import { useButtonListStore } from '@/stores/buttonList';
const layoutStore = useButtonListStore();
layoutStore.useBtnList = ['btnSave'];

const fileList = ref();

const userInfo = ref({
    userId: '',
    userNm: '',
    phone: '',
    lastPsDt: '',
    signature: ''
});

const dialogCheckUserPs = ref(null); // PopupDialog에 대한 ref

const defaultInputForm = {
    userPs: ''
};
const inputForm = ref({ ...defaultInputForm });

// ****************** 버튼 이벤트 ****************** //
btnSave(async () => {
    // const isValid = await validationStore.validateAllFields('form', true);
    //   console.log('@@ isValid', isValid);
    //   if (isValid) {
    //       await workplaceStore.btnSave();
    //   }

    dialogCheckUserPs.value.onClose();
});

const initCheckUserPs = () => {
    inputForm.value = { ...defaultInputForm };
};

// 팝업 닫기
btnSave(async () => {});

const openPopup = () => {
    initCheckUserPs();
    dialogCheckUserPs.value.onOpen();
};

const toChangePassword = () => {
    router.push({
        name: 'modifyUserPs'
    });
};

const toAccesslog = () => {
    router.push({
        name: 'Accesslog'
    });
};

const withdrawalUser = () => {
    confirmMsg('warning', '탈퇴 하시겠습니까?', null, { fun: openPopup });
};

// import { useAuthStore } from '@/stores/auth.js';
// const authStore = useAuthStore();
const withdrawalUserAction = async () => {
    const data = {
        password: Buffer.from(userInfo.value.userPs).toString('base64'),
        userId: userInfo.value.userId
    };
    await withdrawal(data).then(res => {
        if (res.data) {
            alertMsg('info', '탈퇴가 완료되었습니다.');
            // authStore.handleUserCheck();
            router.push({
                name: 'BaseLogin'
            });
        } else {
            alertMsg('warning', '비밀번호가 일치하지 않습니다.');
        }
    });
};

const save = () => {
    if ((userInfo.value.phone !== '' && userInfo.value.phone.length < 12) || (userInfo.value.phone !== '' && userInfo.value.phone.length > 13)) {
        toastPopup('실패', '휴대폰 번호 13자리를 입력해주세요', 'info');
        return;
    }

    confirmMsg('warning', '저장 하시겠습니까?', null, { fun: saveAction });
};

const saveAction = () => {
    console.log();

    modifyUser(userInfo.value).then(() => {
        fileList.value.fnSave().then(() => {
            toastPopup('저장에 성공하였습니다.', userInfo.value.userId, 'info');
        });
    });
};

onMounted(async () => {
    try {
        let res = await searchMyPage();
        userInfo.value = res.result;
        // fileList.value.fnSearch(userInfo.value.userId);
    } catch (error) {
        console.error('Error fetching data:', error);
    }
});
</script>
