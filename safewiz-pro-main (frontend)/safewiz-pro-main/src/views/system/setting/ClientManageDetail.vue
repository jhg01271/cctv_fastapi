<template>
    <div v-if="clientStore && clientStore.inputForm" class="contents">
        <div id="form" class="box form ui">
            <OverlayScrollbarsComponent
                class="h100p pa2-2rem md-pa1-2rem"
                :options="{
                    scrollbars: {
                        autoHide: 'hover',
                        x: 'hidden',
                        y: 'visible'
                    }
                }"
            >
                <div @submit.prevent ref="myForm">
                    <div class="row gutters1rem">
                        <div class="grid12-3 sm-grid12-6 us-grid12-12">
                            <div class="field">
                                <label for="clntNm" required>
                                    <span>{{ t('clntName') }}</span>
                                </label>
                                <input v-input type="text" class="w100p radius" id="clntNm" v-model="clientStore.inputForm.clntNm" required :placeholder="'ex) 펨스산업'" />
                            </div>
                        </div>

                        <div class="grid12-3 sm-grid12-6 us-grid12-12">
                            <div class="field">
                                <label for="rpstNm" required>
                                    <span>{{ t('exponent') }}</span>
                                </label>
                                <input v-input type="text" class="w100p radius" id="rpstNm" v-model="clientStore.inputForm.rpstNm" required :placeholder="'ex) 김철수'" />
                            </div>
                        </div>

                        <div class="grid12-3 sm-grid12-6">
                            <div class="field">
                                <label for="createdAt">{{ t('createdAt') }}</label>
                                <input v-input type="text" class="w100p radius" id="createdAt" :value="formatDate(clientStore.inputForm.createdAt)" disabled />
                            </div>
                        </div>

                        <div class="grid12-3 sm-grid12-6">
                            <div class="field">
                                <label>{{ t('useYn') }}</label>
                                <div class="h4-4rem df aic">
                                    <input type="checkbox" v-input="'사용'" :true-value="'Y'" :false-value="'N'" class="switch" v-model="clientStore.inputForm.useYn" :checked="clientStore.inputForm.useYn == 'Y'" />
                                </div>
                            </div>
                        </div>

                        <div class="grid12-12">
                            <div class="row flex gutters1rem">
                                <div class="grid12-6 es-grid12-12">
                                    <div class="field h100p df fdc">
                                        <label for="clntId" required>{{ t('customerLogo') }}</label>
                                        <div class="box fg1 df aic jcc w100p">
                                            <iFileImage ref="fileList" targetType="logo" :targetId="clientStore.inputForm.fileId" />
                                        </div>
                                    </div>
                                </div>

                                <div class="grid12-6 es-grid12-12">
                                    <div class="row flex gutters1rem">
                                        <div class="grid12-6 sm-grid12-6 us-grid12-12">
                                            <div class="field">
                                                <label for="rgstNo" required>
                                                    <span>{{ t('businessRegistrationNumber') }}</span>
                                                </label>
                                                <input v-input type="text" class="w100p radius" id="rgstNo" v-model="clientStore.inputForm.rgstNo" required :placeholder="'ex) 123-45-67890'" />
                                            </div>
                                        </div>

                                        <div class="grid12-6 sm-grid12-6 us-grid12-12">
                                            <div class="field">
                                                <label for="telNo">{{ t('telNo') }}</label>
                                                <input v-input type="text" class="w100p radius" id="telNo" v-model="clientStore.inputForm.telNo" :placeholder="'02-123-4567'" />
                                            </div>
                                        </div>

                                        <div class="grid12-6 sm-grid12-6 us-grid12-12">
                                            <div class="field">
                                                <label for="chrgPrsn">{{ t('email') }}</label>
                                                <input v-input type="text" class="w100p radius" id="chrgPrsn" v-model="clientStore.inputForm.email" :placeholder="'ex) ccc@company.com'" />
                                            </div>
                                        </div>

                                        <div class="grid12-6 sm-grid12-6 us-grid12-12">
                                            <div class="field">
                                                <label for>{{ t('industry') }}</label>
                                                <i-chips :chips="[{ id: clientStore.inputForm.bizCd, name: clientStore.inputForm.bizNm }]" @popup="addIndustry" @removeChip="removeIndustry"></i-chips>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="grid12-3 ul-grid12-4 sm-grid12-6 es-grid12-12">
                            <div class="field">
                                <label for="zipCd" required>
                                    <span>우편번호</span>
                                </label>
                                <div class="df aic w100p">
                                    <input v-input type="text" class="w100p radius search" id="zipCd" v-model="clientStore.inputForm.zipCd" :readonly="true" :placeholder="'우편번호 검색'" required />
                                    <button type="submit" class="shrink0 bcf1f3f8" @click="clientStore.emitOpenPostcodeDialog">
                                        <img src="/assets/img/common/icon_search.svg" alt />
                                    </button>
                                </div>
                            </div>
                        </div>
                        <div class="grid12-3 ul-grid12-8 sm-grid12-6 es-grid12-12">
                            <div class="field">
                                <label for required>
                                    <span>주소</span>
                                </label>
                                <div class="df aic w100p">
                                    <input v-input type="text" class="w100p radius" id="addrs1" v-model="clientStore.inputForm.addrs1" :readonly="true" required :placeholder="'ex) 경기 성남시 분당구 판교역로 166'" />
                                </div>
                            </div>
                        </div>
                        <div class="grid12-6 ul-grid12-12">
                            <div class="field">
                                <label for>상세주소</label>
                                <div class="df aic w100p">
                                    <input v-input type="text" class="w100p radius" id="addrs2" v-model="clientStore.inputForm.addrs2" :placeholder="'ex) 101동 101호 '" />
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </OverlayScrollbarsComponent>
        </div>
        <teleport to="body">
            <!-- 모달 팝업 컴포넌트 시작  -->
            <i-PopupDialog ref="dialogPostNo">
                <div>
                    <VueDaumPostcode @complete="clientStore.onPostcodeComplete" />

                    <div class="form ui pr">
                        <button type="button" class="btn w100p bright-grey" @click="clientStore.emitClosePostcodeDialog">
                            <span>닫기</span>
                        </button>
                    </div>
                </div>
            </i-PopupDialog>
            <!-- 모달 팝업 콤포넌트 끝  -->

            <!-- 업종 모달 팝업 컴포넌트 시작  -->
            <i-PopupDialog ref="industryPopup">
                <!-- 단일 그리드 -->
                <div class="contents form ui w40rem md-w100p">
                    <base-select-popup :title="'업종'" uniqueKey="detailCd" filterKey="detailNm" useYnKey="useYn" :excluded-value="'N'" :single="true" :fetch-data="getTypeofbusinessList" @close="closeIndustry" />
                </div>
            </i-PopupDialog>
            <!-- 모달 팝업 콤포넌트 끝  -->
        </teleport>
    </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import BaseView from '@/components/base/BaseView';
const { validationStore, t, btnSearch, btnSave, btnDelete, btnBack, formatDate } = BaseView();
import iFileImage from '@/components/file/iFileImage.vue';
import router from '@/router';
import { useClientStore } from '@/stores/system/setting/client.js';
import { useButtonListStore } from '@/stores/buttonList';
import { getTypeofbusinessList } from '@/stores/safewiz/dataset/api/datasetApi';
import baseSelectPopup from '@/views/system/base/popup/BaseSelectPopup.vue';

const layoutStore = useButtonListStore();
layoutStore.useBtnList = ['btnSearch', 'btnBack', 'btnSave', 'btnDelete'];
const clientStore = useClientStore();

//주소팝업
const dialogPostNo = ref(null);
const fileList = ref();

//업종팝업
const industryPopup = ref(null); // PopupDialog에 대한 ref

// 폼 초기화 후 팝업 열기 2024.07.01
const handlePopupOpen = popupRef => {
    if (popupRef.value) popupRef.value.onOpen();
};
// 폼 초기화 후 팝업 열기 2024.07.01
const addIndustry = () => handlePopupOpen(industryPopup);
const detailSave = async () => {
    const isValid = await validationStore.validateAllFields('form', true);
    if (isValid) await clientStore.btnSave();
};

btnSearch(() => {
    clientStore.btnSearch(true);
});

// detailSave 함수 선언 이후에 btnSave 호출
btnSave(detailSave);

btnDelete(() => {
    clientStore.btnDelete('D');
});

btnBack(() => {
    clientStore.goBack();
});

onMounted(async () => {
    //set ref(주소, 파일)
    clientStore.setRef(dialogPostNo, fileList);
    if (!clientStore.inputForm) {
        // 새로고침시 이전 화면으로 이동.
        router.go(-1);
        return;
    }
    //파일조회
    fileList.value.fnSearch();
    if (clientStore.inputForm.cmd === 'U') {
        layoutStore.useBtnList = ['btnSearch', 'btnBack', 'btnSave', 'btnDelete'];
    } else {
        layoutStore.useBtnList = ['btnBack', 'btnSave'];
    }
});

const handlePopupClose = popupRef => {
    if (popupRef.value) popupRef.value.onClose();
};
const removeIndustry = index => {
    clientStore.inputForm.bizCd = '';
    clientStore.inputForm.bizNm = '';
};

// 팝업 닫기
const closeIndustry = row => {
    handlePopupClose(industryPopup);
    if (row[0] && row[0].detailCd) {
        clientStore.inputForm.bizCd = row[0].detailCd;
        clientStore.inputForm.bizNm = row[0].detailNm;
    }
};
</script>
