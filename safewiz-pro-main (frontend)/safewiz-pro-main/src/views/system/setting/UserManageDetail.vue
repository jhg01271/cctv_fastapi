<template>
    <div v-if="userManageStore && userManageStore.inputForm" class="contents df fdc">
        <div id="form" class="box form ui">
            <OverlayScrollbarsComponent
                class="h100p pa2-2rem"
                :options="{
                    scrollbars: {
                        autoHide: 'hover',
                        x: 'hidden',
                        y: 'visible'
                    }
                }"
            >
                <div @submit.prevent ref="form">
                    <div class="row gutters1rem">
                        <div class="grid12-3 ul-grid12-6 sm-grid12-12">
                            <div class="field">
                                <label required>
                                    <span>{{ t('clntName') }}</span>
                                </label>
                                <i-chips :chips="userManageStore.clientItem" @popup="userManageStore.addClient" required></i-chips>
                            </div>
                        </div>

                        <div class="grid12-3 ul-grid12-6 sm-grid12-12">
                            <div class="field">
                                <label required>
                                    <span>{{ t('compNm') }}</span>
                                </label>
                                <i-chips :chips="userManageStore.compItem" @popup="userManageStore.addComp" required :readonly="userManageStore.clientItem.length === 0"></i-chips>
                            </div>
                        </div>

                        <div class="grid12-3 ul-grid12-6 sm-grid12-12">
                            <div class="field">
                                <label for="createdAt"
                                    ><span>{{ t('createdAt') }}</span></label
                                >
                                <input id="createdAt" type="text" v-model="formattedCreatedAt" v-calendar="getDateFormat()" class="datepicker w100p br4px" :disabled="true" />
                            </div>
                        </div>

                        <div class="grid12-3 ul-grid12-6 sm-grid12-12">
                            <div class="field">
                                <label for="useYn">사용여부</label>
                                <div class="df aic h4-4rem">
                                    <input :checked="userManageStore.inputForm.useYn === 'Y'" v-input="'사용'" type="checkbox" class="df switch" @change="userManageStore.toggleUseYn" />
                                </div>
                            </div>
                        </div>

                        <div class="grid12-3 ul-grid12-6 sm-grid12-12">
                            <div class="field">
                                <label for="userId" required>
                                    <span>{{ t('id') }}</span>
                                </label>
                                <input v-input type="text" class="w100p radius" id="userId" :disabled="userManageStore.inputForm.cmd === 'U'" required v-model="userManageStore.inputForm.userId" :placeholder="'ex) ID'" />
                            </div>
                        </div>

                        <div class="grid12-3 ul-grid12-6 sm-grid12-12">
                            <div class="field">
                                <label for="userNm" required>
                                    <span>{{ t('userNm') }}</span>
                                </label>
                                <input v-input type="text" class="w100p radius" id="userNm" :disabled="userManageStore.inputForm.cmd === 'U' && userManageStore.inputForm.authGroupNm === '사용자'" required v-model="userManageStore.inputForm.userNm" :placeholder="'ex) 김철수'" />
                            </div>
                        </div>

                        <div class="grid12-3 sm-grid12-12">
                            <div class="field">
                                <label for="email" required>
                                    <span>{{ t('email') }}</span>
                                </label>
                                <input v-input type="text" class="w100p radius" id="email" required v-model="userManageStore.inputForm.email" :placeholder="'ex) ccc@company.com'" />
                            </div>
                        </div>

                        <div class="grid12-3 sm-grid12-12">
                            <div class="field">
                                <label for="telNo" required>
                                    <span>{{ t('telNo') }}</span>
                                </label>
                                <input v-input class="w100p radius" id="telNo" type="text" required v-model="userManageStore.inputForm.phone" :placeholder="'02-123-4567'" />
                            </div>
                        </div>

                        <div class="grid12-3 ul-grid12-6 sm-grid12-12" :key="userManageStore.inputForm.authGroupCd">
                            <div class="field">
                                <label for="authGroupCd" required>
                                    <span>{{ t('authority') }}</span>
                                </label>
                                <select v-select class="w100p radius" id="authGroupCd" v-model="userManageStore.inputForm.authGroupCd" required :disabled="userManageStore.inputForm.authGroupNm === '사용자' || userManageStore.inputForm.authGroupNm === '마스터'">
                                    <option v-for="item in userManageStore.authList" :key="item.authGroupCd" :value="item.authGroupCd" :disabled="item.authGroupNm === '사용자' || item.authGroupNm === '마스터'">{{ item.authGroupNm }}</option>
                                </select>
                            </div>
                        </div>

                        <!-- 비밀번호 -->
                        <div class="grid12-3 ul-grid12-6 sm-grid12-12" v-show="isPasswordChange || userManageStore.inputForm.cmd === 'I'">
                            <div class="field">
                                <label for="userPs" required>
                                    <span>{{ t('password') }}</span>
                                </label>
                                <div class="pr">
                                    <input v-input vali-msg="*비밀번호가 제대로 입력되지 않았습니다." :type="isPasswordVisible ? 'text' : 'password'" id="userPs" class="w100p radius" :required="userManageStore.inputForm.cmd !== 'U' || isPasswordChange" placeholder="패스워드를 입력해주세요." :disabled="userManageStore.inputForm.cmd === 'U' && !isPasswordChange" v-model="userManageStore.inputForm.userPs" />
                                    <button class="pa w20px t50p r10px neg-tty50p" type="button" @click="isPasswordVisible = !isPasswordVisible">
                                        <img :src="isPasswordVisible ? '/assets/img/common/icon_eye_close_dark.svg' : '/assets/img/common/icon_eye_dark.svg'" alt="Toggle Password Visibility" />
                                    </button>
                                </div>
                            </div>
                        </div>

                        <div :class="[userManageStore.inputForm.cmd === 'U' ? 'grid12-3 ul-grid12-12 sm-grid12-1' : 'grid12-6 ul-grid12-6 sm-grid12-12']" v-show="isPasswordChange || userManageStore.inputForm.cmd === 'I'">
                            <div class="field">
                                <label for="userPsCheck" required>
                                    <span>{{ t('verifyPassword') }}</span>
                                </label>
                                <div class="pr">
                                    <input v-input :type="isPasswordConfirmVisible ? 'text' : 'password'" maxlength="16" class="w100p radius" id="userPsCheck" :required="userManageStore.inputForm.cmd !== 'U' || isPasswordChange" v-model="userManageStore.inputForm.userPsConfirm" />
                                    <button class="pa w20px t50p r10px neg-tty50p" type="button" @click="isPasswordConfirmVisible = !isPasswordConfirmVisible">
                                        <img :src="isPasswordConfirmVisible ? '/assets/img/common/icon_eye_close_dark.svg' : '/assets/img/common/icon_eye_dark.svg'" alt="Toggle Password Visibility" />
                                    </button>
                                </div>
                            </div>
                        </div>

                        <!-- 비밀번호 변경 체크박스 -->
                        <div :class="[isPasswordChange ? 'grid12-3 es-grid12-4 us-grid12-12' : 'grid12-8 es-grid12-4 us-grid12-12']" v-show="userManageStore.inputForm.cmd === 'U'">
                            <div class="field">
                                <label for="changePassword">
                                    <span>{{ t('mypage_changePassword') }}</span>
                                </label>
                                <div class="h4-4rem df aic">
                                    <input id="changePassword" v-input type="checkbox" class="w100p radius" v-model="isPasswordChange" />
                                </div>
                            </div>
                        </div>

                        <!-- 로그인 이력 -->
                        <div class="grid12-2 ul-grid12-6 sm-grid12-12" v-show="userManageStore.inputForm.cmd === 'U'">
                            <div class="field">
                                <label for="lastAccessDate">
                                    <span>{{ t('lastAccessDate') }}</span>
                                </label>
                                <input id="lastAccessDate" type="text" v-calendar="getDateFormat()" class="datepicker w100p br4px" :value="formatDate(userManageStore.inputForm.lastLoginDate)" :disabled="userManageStore.inputForm.cmd == 'U'" />
                            </div>
                        </div>

                        <div class="grid12-2 ul-grid12-6 sm-grid12-12" v-show="userManageStore.inputForm.cmd === 'U'">
                            <div class="field">
                                <label for="lastAccessOs">
                                    <span>{{ t('lastAccessOs') }}</span>
                                </label>
                                <input id="lastAccessOs" v-input type="text" class="w100p radius" v-model="userManageStore.inputForm.lastLoginOs" :disabled="userManageStore.inputForm.cmd == 'U'" />
                            </div>
                        </div>

                        <div class="grid12-2 ul-grid12-6 sm-grid12-12" v-show="userManageStore.inputForm.cmd === 'U'">
                            <div class="field">
                                <label for="loginFailCnt">
                                    <span>로그인 실패 횟수</span>
                                </label>
                                <input id="loginFailCnt" v-input type="text" class="w100p radius" v-model="userManageStore.inputForm.loginFailCnt" :disabled="userManageStore.inputForm.cmd == 'U'" />
                            </div>
                        </div>

                        <div :class="[userManageStore.inputForm.cmd === 'U' ? 'grid12-6 sm-grid12-12' : 'grid12-12']">
                            <div class="field">
                                <label for="remark">{{ t('note') }}</label>
                                <input type="text" class="w100p radius" id="remark" v-model="userManageStore.inputForm.remark" placeholder="비고" />
                            </div>
                        </div>
                    </div>
                </div>
            </OverlayScrollbarsComponent>
        </div>
    </div>
    <teleport to="body">
        <i-PopupDialog ref="clientPopup">
            <!-- 고객사 단일 그리드 -->
            <div class="contents w500px md-w100p">
                <base-select-popup :title="'고객사'" :inputForm="userManageStore.inputForm" uniqueKey="clntId" filterKey="clntNm" useYnKey="useYn" :excluded-value="'N'" :single="true" :fetch-data="getClientList" :selectedIdList="userManageStore.clientItem.map(el => el.id)" @close="userManageStore.closeClient" />
            </div>
        </i-PopupDialog>

        <i-PopupDialog ref="compPopup">
            <!-- 사업장 멀티 그리드 -->
            <div class="contents w500px md-w100p">
                <base-select-popup :title="'사업장'" :inputForm="userManageStore.inputForm" uniqueKey="compId" filterKey="compNm" useYnKey="useYn" :excluded-value="'N'" :single="false" :fetch-data="() => userManageStore.getCompListByClientId(userManageStore.clientItem?.[0]?.id || '')" :selectedIdList="userManageStore.compItem.map(el => el.id)" @close="userManageStore.closeComp" @apply="userManageStore.applyComp" />
            </div>
        </i-PopupDialog>
    </teleport>
</template>

<script setup>
import { ref, onMounted, computed, defineModel } from 'vue';
import { getDateFormat } from '@/utils/dateUtil.js';
import { searchCompany } from '@/api/admin/comp/company/Company';
import BaseView from '@/components/base/BaseView';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
const { validationStore, confirmMsg, toastPopup, t, btnSearch, btnSave, btnDelete, btnBack, formatDate } = BaseView();
import router from '@/router';
import { useUserManageStore } from '@/stores/system/setting/userManage.js';
const userManageStore = useUserManageStore();

import { useButtonListStore } from '@/stores/buttonList';
const layoutStore = useButtonListStore();
layoutStore.useBtnList = ['btnBack', 'btnSave', 'btnDelete'];

import BaseSelectPopup from '@/views/system/base/popup/BaseSelectPopup.vue';

import { getClientList } from '@/stores/system/setting/api/clientApi'; // 고객사 데이터 Api

const clientPopup = ref(); // 고객사 팝업
const compPopup = ref(); //사업장 팝업

const isPasswordChange = ref(false); // 비밀번호 변경 체크박스 상태
const isPasswordVisible = ref(false); // 비밀번호 볼 수 있는 상태
const isPasswordConfirmVisible = ref(false); // 비밀번호 확인 볼 수 있는 상태

//--------------------------------------------

onMounted(async () => {
    //권한처리 로직
    let responses = await Promise.all([userManageStore.getAuthGroup()]);
    if (responses[0].list) {
        responses[0].list.forEach(el => {
            el.authGroupCd = el.grpCd;
            el.authGroupNm = el.grpNm;
        });
    }
    userManageStore.authList = responses[0].list;
    if (!userManageStore.inputForm) {
        // 새로고침시 이전 화면으로 이동.
        router.go(-1);
        return;
    }

    if (userManageStore.inputForm?.cmd === 'U') {
        layoutStore.useBtnList = ['btnSearch', 'btnBack', 'btnSave', 'btnDelete'];

        // 수정 시 비밀번호 초기화
        userManageStore.inputForm.userPs = '';
        userManageStore.inputForm.userPsConfirm = '';
    } else {
        layoutStore.useBtnList = ['btnBack', 'btnSave'];
    }

    // 고객사 팝업 세팅
    userManageStore.setRefsClient(clientPopup);

    // 사업장 팝업 세팅
    userManageStore.setRefsComp(compPopup);

    // 로그인 이력 호출
    userManageStore.getLoginHistoryDetail();
});

btnSearch(() => {
    validationStore.clearAllErrors(); // 오류 메시지 제거
    validationStore.clearInvalidClasses(); // 포커스 제거
    userManageStore.btnSearch();
});

btnSave(async () => {
    const isValid = await validationStore.validateAllFields('form', true, userManageStore.inputForm.userPs, userManageStore.inputForm.userPsConfirm);
    if (isValid) {
        userManageStore.btnSave();
    }
});

btnDelete(() => {
    userManageStore.btnDelete('D');
});

btnBack(() => {
    userManageStore.goBack();
});

//--------------------------------------------

// 등록 일자 날짜 형식 변경
const formattedCreatedAt = computed({
    get() {
        // formatDate 메소드에 원하는 포맷(예: 'yyyy.MM.dd')을 전달하여 시분초 제거
        return formatDate(userManageStore.inputForm.createdAt);
    },
    set(newVal) {
        // setter에서 새로운 값(newVal)을 등록일자로 업데이트
        userManageStore.inputForm.createdAt = newVal;
    }
});
</script>
