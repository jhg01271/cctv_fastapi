<template>
    <div v-if="compStore && compStore.inputForm" class="contents">
        <div id="form" class="box form ui df fdc">
            <OverlayScrollbarsComponent
                class="pa2-2rem"
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
                        <!-- readonly 속성일 경우 드롭다운 메뉴를 숨기고 텍스트로 표기 (compdetail에서 사용)-->
                        <div class="grid12-6 sm-grid12-6 us-grid12-12">
                            <div class="field">
                                <label for="compNm" required>
                                    <span>{{ t('compNm') }}</span>
                                </label>
                                <input v-input type="text" class="w100p radius" id="compNm" v-model="compStore.inputForm.compNm" required :placeholder="'ex) 펨스산업'" />
                            </div>
                        </div>

                        <div class="grid12-3 sm-grid12-6 us-grid12-12">
                            <div class="field">
                                <label for="createdAt">{{ t('createdAt') }}</label>
                                <input v-input type="text" class="w100p radius" id="createdAt" :value="formatDate(compStore.inputForm.createdAt)" disabled />
                            </div>
                        </div>

                        <div class="grid12-1 sm-grid12-6 us-grid12-12">
                            <div class="field">
                                <label for="email">{{ t('array') }}</label>
                                <input v-input type="text" class="w100p radius" id="ordSeq" v-model="compStore.inputForm.ordSeq" :placeholder="'99'" />
                            </div>
                        </div>

                        <div class="grid12-2 sm-grid12-6 us-grid12-12">
                            <div class="field">
                                <label for="useYn" required>{{ t('useYn') }}</label>
                                <div class="df aic h4-4rem">
                                    <input v-input type="checkbox" class="w100p radius switch" required :checked="compStore.inputForm.useYn === 'Y'" @change="compStore.toggleUseYn" />
                                </div>
                            </div>
                        </div>

                        <div class="grid12-12">
                            <div class="row flex gutters1rem">
                                <div class="grid12-6 es-grid12-12">
                                    <div class="field h100p df fdc">
                                        <label for="clntId">{{ t('compLogo') }}</label>
                                        <div class="box fg1 df aic jcc">
                                            <iFileImage ref="fileList" targetType="logo" :targetId="compStore.inputForm.fileId" />
                                        </div>
                                    </div>
                                </div>
                                <div class="grid12-6 es-grid12-12">
                                    <div class="row flex gutters1rem">
                                        <div class="grid12-6">
                                            <div class="field">
                                                <label for="rpstNm" required>
                                                    <span>{{ t('exponent') }}</span>
                                                </label>
                                                <input v-input type="text" class="radius" id="rpstNm" v-model="compStore.inputForm.rpstNm" required :placeholder="'ex) 김철수'" />
                                            </div>
                                        </div>

                                        <div class="grid12-6">
                                            <div class="field">
                                                <label for="rgstNo" required>
                                                    <span>{{ t('businessRegistrationNumber') }}</span>
                                                </label>
                                                <input v-input type="text" class="radius" id="rgstNo" v-model="compStore.inputForm.rgstNo" required :placeholder="'ex) 123-45-67890'" />
                                            </div>
                                        </div>

                                        <div class="grid12-6">
                                            <div class="field">
                                                <label for="telNo">{{ t('telNo') }}</label>
                                                <input v-input type="text" class="radius" id="telNo" v-model="compStore.inputForm.telNo" :placeholder="'02-123-4567'" />
                                            </div>
                                        </div>

                                        <div class="grid12-6">
                                            <div class="field">
                                                <label for="email">{{ t('email') }}</label>
                                                <input v-input type="text" class="radius" id="email" v-model="compStore.inputForm.email" :placeholder="'xxx@company.com'" />
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="grid12-6 sm-grid12-6 us-grid12-12">
                            <div class="field">
                                <label for="bizCd">{{ t('industry') }}</label>
                                <i-chips :chips="[{ id: compStore.inputForm.bizCd, name: compStore.inputForm.bizNm }]" @popup="addIndustry" @removeChip="removeInds"></i-chips>
                            </div>
                        </div>

                        <div class="grid12-6 sm-grid12-6 us-grid12-12">
                            <div class="field">
                                <label for="constClassCd">{{ t('constructionClassification') }}</label>
                                <template v-if="compStore.filteredBusinessList">
                                    <select v-select class="w100p radius" id="constClassCd" v-model="compStore.inputForm.constClassCd">
                                        <option v-for="item in filteredConstruction" :key="item.constClassCd" :value="item.constClassCd">{{ item.constClassNm }}</option>
                                    </select>
                                </template>
                            </div>
                        </div>

                        <!-- 다음 행부터 우편번호 출력 -->
                        <div class="grid12-3 ul-grid12-6 us-grid12-12">
                            <div class="field">
                                <label for="zipCd" required>
                                    <span>{{ t('zipCode') }}</span>
                                </label>
                                <div class="df aic w100p">
                                    <input v-input type="text" class="w100p radius search" id="zipCd" v-model="compStore.inputForm.zipCd" :readonly="true" required :placeholder="'우편번호 검색'" />
                                    <button type="submit" class="shrink0 bcf1f3f8" @click="openPostcodeDialog">
                                        <img src="/assets/img/common/icon_search.svg" alt />
                                    </button>
                                </div>
                            </div>
                        </div>
                        <div class="grid12-3 ul-grid12-6 us-grid12-12">
                            <div class="field">
                                <label for="addrs1" required>
                                    <span>{{ t('address') }}</span>
                                </label>
                                <input v-input type="text" class="w100p radius" id="addrs1" v-model="compStore.inputForm.addrs1" :readonly="true" required :placeholder="'ex) 경기 성남시 분당구 판교역로 166'" />
                            </div>
                        </div>
                        <div class="grid12-6 us-grid12-12">
                            <div class="field">
                                <label for="addrs2">{{ t('detailAddress') }}</label>
                                <input v-input type="text" class="w100p radius" id="addrs2" v-model="compStore.inputForm.addrs2" :placeholder="'ex) 101동 101호 '" />
                            </div>
                        </div>
                        <div class="grid12-12 ul-grid12-6 us-grid12-12">
                            <div class="field">
                                <label for="clntId">
                                    <span>{{ t('client') }}</span>
                                </label>
                                <i-chips :chips="compStore.clntList" readonly></i-chips>
                            </div>
                        </div>
                        <div class="grid12-3 ul-grid12-6 us-grid12-12">
                            <div class="field">
                                <label for="maxHrCount" required>
                                    <span>{{ t('maxHrCount') }}</span>
                                </label>
                                <input v-input type="number" class="w100p radius" id="maxHrCount" v-model="compStore.inputForm.maxHrCount" required :placeholder="'ex) 30'" max="9999"/>
                            </div>
                        </div>
                    </div>
                </div>
            </OverlayScrollbarsComponent>
        </div>
        <teleport to="body">
            <!-- 모달 팝업 컴포넌트 시작  -->
            <i-PopupDialog ref="dialogPostNo">
                <i-post-code @complete="onPostcodeComplete" @close="closePostcodeDialog"> </i-post-code>
                <!-- <VueDaumPostcode @complete="compStore.onPostcodeComplete" /> -->
            </i-PopupDialog>
            <!-- 모달 팝업 콤포넌트 끝  -->
            <!-- 업종 모달 팝업 컴포넌트 시작  -->
            <i-PopupDialog ref="industryPopup">
                <!-- 단일 그리드 -->

                <div class="contents w500px md-w100p">
                    <base-select-popup :title="'업종'" uniqueKey="detailCd" filterKey="detailNm" useYnKey="useYn" :excluded-value="'N'" :single="true" :fetch-data="getTypeofbusinessList" @close="closeIndustry" readonly="true"/>
                </div>
            </i-PopupDialog>
            <!-- 업종 모달 팝업 컴포넌트 끝  -->
        </teleport>
    </div>
</template>

<script setup>
import { ref, onMounted, computed, defineProps, defineModel } from 'vue';
import BaseView from '@/components/base/BaseView';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
const { validationStore, formatDate, t, btnSearch, btnSave, btnDelete, btnBack } = BaseView();
import { getTypeofbusinessList } from '@/stores/safewiz/dataset/api/datasetApi';
import baseSelectPopup from '@/views/system/base/popup/BaseSelectPopup.vue';
import router from '@/router';
import iPostCode from '@/components/common/iPostCode.vue';
// 사업장 store
import { useCompStore } from '@/stores/system/setting/comp.js';
const compStore = useCompStore();

import { useButtonListStore } from '@/stores/buttonList';
const layoutStore = useButtonListStore();
layoutStore.useBtnList = ['btnSearch', 'btnBack', 'btnSave', 'btnDelete'];

const fileList = ref();

//주소
const dialogPostNo = ref(null);

// 주소 검색 팝업 열기
const openPostcodeDialog = () => {
    dialogPostNo.value.onOpen();
};

// 주소 검색 팝업 닫기
const closePostcodeDialog = () => {
    dialogPostNo.value.onClose();
};

// 주소 선택 및 업데이트 후 팝업 닫기
const onPostcodeComplete = data => {
    compStore.inputForm.zipCd = data.zonecode;
    compStore.inputForm.addrs1 = data.address;

    dialogPostNo.value.onClose();
};

//업종팝업
const industryPopup = ref(null);

// 폼 초기화 후 팝업 열기
const addIndustry = () => industryPopup.value.onOpen()

//업종 팝업 닫기
const closeIndustry = row => {
    industryPopup.value.onClose()
    if (row[0]) {
        compStore.inputForm.bizCd = null;
        compStore.inputForm.bizNm = null;
        compStore.inputForm.bizCd = row[0].detailCd;
        compStore.inputForm.bizNm = row[0].detailNm;
    }
};

//업종 chips 제거
const removeInds = () => {
    compStore.inputForm.bizCd = null;
    compStore.inputForm.bizNm = null;
};

btnSearch(() => {
    compStore.btnSearch(true);
});

btnSave(async () => {
    const isValid = await validationStore.validateAllFields('form', true);
    if (isValid) {
        compStore.btnSave();
    }
});

btnDelete(() => {
    compStore.btnDelete('D');
});

btnBack(() => {
    compStore.goBack();
});

onMounted(async () => {
    compStore.setRef(dialogPostNo, fileList);

    if (!compStore.inputForm?.cmd) {
        // 새로고침시 이전 화면으로 이동.
        router.go(-1);
    }

    if (compStore.inputForm?.cmd === 'U') {
        layoutStore.useBtnList = ['btnSearch', 'btnBack', 'btnSave', 'btnDelete'];
    } else {
        layoutStore.useBtnList = ['btnBack', 'btnSave'];
    }
});

//공사 추후 기능 개발 필요
const filteredConstruction = [];
</script>
