<template>
    <div v-if="partnerStore && partnerStore.inputForm" class="contents">
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
                <div @submit.prevent ref="myForm">
                    <div class="row flex gutters1rem">
                        <div class="grid12-3 sm-grid12-12">
                            <div class="field">
                                <label for="partCompNm" required>
                                    <span>{{ t('partnerNm') }}</span>
                                </label>
                                <input v-input type="text" class="w100p radius" id="partCompNm" v-model="partnerStore.inputForm.partCompNm" required :placeholder="'ex) 펨스산업'" />
                            </div>
                        </div>

                        <div class="grid12-3 sm-grid12-12">
                            <div class="field">
                                <label for="rpstNm" required>
                                    <span>대표자명</span>
                                </label>
                                <input v-input type="text" class="w100p radius" id="rpstNm" v-model="partnerStore.inputForm.rpstNm" required :placeholder="'ex) 김철수'" />
                            </div>
                        </div>
                        <div class="grid12-2 sm-grid12-12">
                            <div class="field">
                                <label for="regDt" required
                                    ><span>{{ t('partnerRegDt') }}</span></label
                                >
                                <input v-input type="text" class="w100p radius datepicker" v-calendar="getDateFormat()" id="regDt" v-model="partnerStore.inputForm.regDt" @input="regDtChanged" required :placeholder="'ex) 2024.01.01'" />
                            </div>
                        </div>
                        <div class="grid12-2 sm-grid12-6">
                            <div class="field">
                                <label for="email">
                                    <span>{{ t('array') }}</span>
                                </label>
                                <input v-input type="number" class="w100p radius" id="ordSeq" v-model="partnerStore.inputForm.ordSeq" :placeholder="99" min="0" max="99" />
                            </div>
                        </div>
                        <div class="grid12-2 sm-grid12-6">
                            <div class="field">
                                <label for="useYn">{{ t('useYn') }}</label>
                                <div class="df aic h4-4rem">
                                    <input v-input="'사용'" type="checkbox" class="df switch" :checked="partnerStore.inputForm.useYn === 'Y'" @change="partnerStore.toggleUseYn" />
                                </div>
                            </div>
                        </div>
                        <div class="grid12-6 sm-grid12-12">
                            <div class="field h100p df fdc">
                                <label for>협력사 로고</label>

                                <div class="box fg1 df aic jcc w100p">
                                    <iFileImage ref="fileList" targetType="partnerLogo" :targetId="partnerStore.inputForm.fileId" />
                                </div>
                            </div>
                        </div>

                        <div class="grid12-6 sm-grid12-12">
                            <div class="row flex gutters1rem">
                                <div class="grid12-6 sm-grid12-12">
                                    <div class="field">
                                        <label for="rgstNo" required>
                                            <span>사업자 등록번호</span>
                                        </label>
                                        <input v-input type="bizRegNum" class="w100p radius" id="rgstNo" v-model="partnerStore.inputForm.rgstNo" required :placeholder="'ex) 123-45-67890'" />
                                    </div>
                                </div>

                                <div class="grid12-6 sm-grid12-12">
                                    <div class="field">
                                        <label for="telNo">전화번호</label>
                                        <input v-input type="phone" class="w100p radius" id="telNo" v-model="partnerStore.inputForm.telNo" placeholder="ex) 010-1234-1234" />
                                    </div>
                                </div>

                                <div class="grid12-6 sm-grid12-12">
                                    <div class="field">
                                        <label for="faxNo">FAX번호</label>
                                        <input v-input type="phone" class="w100p radius" id="faxNo" v-model="partnerStore.inputForm.faxNo" placeholder="ex) 02-1234-1234" />
                                    </div>
                                </div>

                                <div class="grid12-6 sm-grid12-12">
                                    <div class="field">
                                        <label for="email">이메일</label>
                                        <input v-input type="email" class="w100p radius" id="email" v-model="partnerStore.inputForm.email" placeholder="ex) example@i-gns.com" />
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="grid12-6 sm-grid12-12">
                            <div class="field h100p df fdc">
                                <label for="useYn">협력사 소재지</label>
                                <div class="box df aic jcc w100p fg1">
                                    <iFileImage ref="fileListLoc" targetType="partnerLocation" :targetId="partnerStore.inputForm.fileId2" />
                                </div>
                            </div>
                        </div>

                        <div class="grid12-6 sm-grid12-12">
                            <div class="row flex gutters1rem">
                                <div class="grid12-6 sm-grid12-12">
                                    <div class="field">
                                        <label for>주요사업</label>
                                        <input v-input type="text" class="w100p radius" v-model="partnerStore.inputForm.desc" placeholder="주요 사업을 입력해주세요." />
                                    </div>
                                </div>

                                <div class="grid12-6 sm-grid12-12">
                                    <div class="field">
                                        <label for>업종</label>
                                        <i-chips :chips="[{ id: partnerStore.inputForm.bizCd, name: partnerStore.inputForm.bizNm }]" @popup="addIndustry" @removeChip="removeInds"></i-chips>
                                    </div>
                                </div>
                                <div class="grid12-12 sm-grid12-12">
                                    <div class="field">
                                        <label for>
                                            공사와의 거래 품목
                                            <sub class="ml5px fs1-3rem">(서비스 내용)</sub>
                                        </label>
                                        <i-chips :chips="partnerStore.chipsItem" @popup="openSystemPopup"></i-chips>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="grid12-3 ul-grid12-4 sm-grid12-12">
                            <div class="field">
                                <label for>우편번호</label>
                                <div class="pr df aic w100p">
                                    <input v-input type="text" class="w100p radius search" id="zipCd" v-model="partnerStore.inputForm.zipCd" :readonly="true" :placeholder="'ex) 12345'" />
                                    <button type="button" class="pa r0 shrink0" @click="partnerStore.emitOpenPostcodeDialog">
                                        <img src="/assets/img/common/icon_search.svg" alt />
                                    </button>
                                </div>
                            </div>
                        </div>
                        <div class="grid12-3 ul-grid12-8 sm-grid12-12">
                            <div class="field">
                                <label for>주소</label>
                                <div class="df aic w100p">
                                    <input v-input type="text" class="w100p radius" id="addrs1" v-model="partnerStore.inputForm.addrs1" :readonly="true" :placeholder="'ex) 경기 성남시 분당구 판교역로 166'" />
                                </div>
                            </div>
                        </div>
                        <div class="grid12-6 ul-grid12-12">
                            <div class="field">
                                <label for>상세주소</label>
                                <div class="df aic w100p">
                                    <input v-input type="text" class="w100p radius" id="addrs2" v-model="partnerStore.inputForm.addrs2" :placeholder="'ex) 101동 101호 '" />
                                </div>
                            </div>

                            <div class="grid12-9 ul-grid12-6 sm-grid12-12"></div>
                        </div>
                    </div>

                    <hr class="w100p h1px bcE1E6ED mt2-2rem mb2-2rem" />
                    <div class="row flex gutters2rem">
                        <div class="grid12-6 ul-grid12-12">
                            <div class="field">
                                <label for>안전보건 조직 구성도</label>
                                <div class="box h30-5rem"></div>
                            </div>
                        </div>
                        <div class="grid12-6 ul-grid12-12">
                            <div class="field mt4rem pt4px">
                                <div class="box fs1-5rem">
                                    <div class="df aic single-chips">
                                        <em class="w20rem lh1-3 shrink0 sm-w12-8rem">
                                            본사
                                            <br class="dn sm-db" />담당자 (정)
                                        </em>

                                        <div class="df pr w100p jcsb oh">
                                            <i-hrChips :chips="partnerStore.inputForm.hrListH" @popup="addPeople('hrListH')" @removeChip="removePeople('hrListH', $event)"></i-hrChips>
                                        </div>
                                    </div>
                                    <div class="df aic single-chips">
                                        <em class="w20rem lh1-3 shrink0 sm-w12-8rem">
                                            본사
                                            <br class="dn sm-db" />담당자 (부)
                                        </em>
                                        <div class="df pr w100p jcsb oh">
                                            <i-hrChips :chips="partnerStore.inputForm.hrListS" @popup="addPeople('hrListS')" @removeChip="removePeople('hrListS', $event)"></i-hrChips>
                                        </div>
                                    </div>
                                    <div class="df aic single-chips">
                                        <em class="w20rem lh1-3 shrink0 sm-w12-8rem">
                                            협력사
                                            <br class="dn sm-db" />담당자 (정)
                                        </em>
                                        <div class="df pr w100p jcsb oh">
                                            <i-hrChips :chips="partnerStore.inputForm.partnerHrListH" @popup="addPeople('partnerHrListH')" @removeChip="removePeople('partnerHrListH', $event)"></i-hrChips>
                                        </div>
                                    </div>
                                    <div class="df aic single-chips">
                                        <em class="w20rem lh1-3 shrink0 sm-w12-8rem">
                                            협력사
                                            <br class="dn sm-db" />담당자 (부)
                                        </em>
                                        <div class="df pr w100p jcsb oh">
                                            <i-hrChips :chips="partnerStore.inputForm.partnerHrListS" @popup="addPeople('partnerHrListS')" @removeChip="removePeople('partnerHrListS', $event)"></i-hrChips>
                                        </div>
                                    </div>
                                    <div class="df aic single-chips">
                                        <em class="w20rem lh1-3 shrink0 sm-w12-8rem">
                                            협력사 안전보건
                                            <br class="dn sm-db" />관리 책임자
                                        </em>
                                        <div class="df pr w100p jcsb oh">
                                            <i-hrChips :chips="partnerStore.inputForm.partShHr" @popup="addPeople('partShHr')" @removeChip="removePeople('partShHr', $event)"></i-hrChips>
                                            <!-- <button type="submit" @click="addPeople('partShHr')">
                                                <img src="/assets/img/common/icon_search.svg" alt />
                                            </button> -->
                                        </div>
                                    </div>
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
                <i-post-code @complete="partnerStore.onPostcodeComplete" @close="partnerStore.closePostcodeDialog"></i-post-code>
            </i-PopupDialog>
            <!-- 모달 팝업 콤포넌트 끝  -->
            <!-- 업종 모달 팝업 컴포넌트 시작  -->
            <i-PopupDialog ref="industryPopup">
                <!-- 단일 그리드 -->

                <div class="contents w500px md-w100p">
                    <base-select-popup :title="'업종'" uniqueKey="detailCd" filterKey="detailNm" useYnKey="useYn" :excluded-value="'N'" :single="true" :fetch-data="getTypeofbusinessList" @close="closeIndustry" />
                </div>
            </i-PopupDialog>

            <i-PopupDialog ref="peoplePopup">
                <!-- 단일 그리드 -->
                <div class="contents w500px md-w100p">
                    <h3>인원</h3>
                    <selectUser :single="gubun === 'partShHr'" :selected="selectedUserByGubun" @selected="selectPeople" @close="closePeoplePopup"></selectUser>
                </div>
            </i-PopupDialog>

            <!-- 모달 팝업 콤포넌트 끝  -->
            <!-- 공사와의 거래 모달 팝업 컴포넌트 시작  -->
            <i-PopupDialog ref="commonPopup">
                <div class="contents w70rem md-w100p">
                    <base-select-popup :title="'공사와의 거래 품목'" uniqueKey="partCompItemId" filterKey="partCompItemNm" useYnKey="useYn" :excluded-value="'N'" :single="true" :fetch-data="getPartCompItemDataset" @close="closeSystemPopup" />
                </div>
            </i-PopupDialog>
            <!-- 모달 팝업 콤포넌트 끝  -->
        </teleport>
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import router from '@/router'; // router는 상단에 위치시킵니다
import BaseView from '@/components/base/BaseView';
const { validationStore, confirmMsg, t, formatDate, btnBack, btnSearch, btnSave, btnAdd, btnDelete, alertMsg } = BaseView();
import iFileImage from '@/components/file/iFileImage.vue';
import selectUser from '@/views/base/member/SelectUser/Index.vue';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import iPostCode from '@/components/common/iPostCode.vue';
import { getDateFormat } from '@/utils/dateUtil.js';
import { usePartnerStore } from '@/stores/system/base/partner.js';
const partnerStore = usePartnerStore();
import { useButtonListStore } from '@/stores/buttonList';
const layoutStore = useButtonListStore();
layoutStore.useBtnList = ['btnSearch', 'btnBack', 'btnAdd', 'btnSave', 'btnDelete'];
const dialogPostNo = ref(null);
const fileList = ref();
const fileListLoc = ref();
const industryPopup = ref(null);
const peoplePopup = ref(null);
const gubun = ref();

import { getTypeofbusinessList } from '@/stores/safewiz/dataset/api/datasetApi';
import { getPartCompItemDataset } from '@/stores/system/base/api/partnerApi.js';
import baseSelectPopup from '@/views/system/base/popup/BaseSelectPopup.vue';

const handlePopupOpen = popupRef => {
    if (popupRef.value) popupRef.value.onOpen();
};

const handlePopupClose = popupRef => {
    if (popupRef.value) popupRef.value.onClose();
};

const addPeople = type => {
    gubun.value = type;
    selectedUserByGubun.value = partnerStore.inputForm[type]?.map(el => el.id);
    handlePopupOpen(peoplePopup);
};
const selectedUserByGubun = ref([]);
const selectPeople = e => {
    console.log('이미지', e);
    if (e.length < 1) return;
    if (gubun.value === 'partShHr') {
        partnerStore.inputForm.partShHrId = e.hrId;
        partnerStore.inputForm.partShHrNm = e.hrNm;
        partnerStore.inputForm.partShHrImgId = e.img;
        partnerStore.inputForm.partShHr = [{ id: e.hrId, name: e.hrNm, img: e.img }];
    } else {
        let inputData = [];
        e.forEach(el => {
            let duplicate = partnerStore.inputForm[gubun.value]?.find(user => {
                return user.id == el.hrId || user.hrId == el.hrId;
            });
            if (duplicate == undefined) {
                inputData.push({
                    id: el.hrId,
                    hrId: el.hrId,
                    hrNm: el.hrNm,
                    img: el.img,
                    name: el.hrNm
                });
            }
        });
        partnerStore.inputForm[gubun.value].push(...inputData);
    }
    handlePopupClose(peoplePopup);
};

const removePeople = (field, e) => {
    gubun.value = field;
    if (gubun.value === 'partShHr') {
        partnerStore.inputForm.partShHrId = null;
        partnerStore.inputForm.partShHrImgId = null;
        partnerStore.inputForm.partShHrNm = null;
        partnerStore.inputForm.partShHr = [];
        return;
    }
    partnerStore.inputForm[gubun.value] = e;
};

const addIndustry = () => handlePopupOpen(industryPopup);
const removeInds = () => {
    partnerStore.inputForm.bizCd = null;
    partnerStore.inputForm.bizNm = null;
};

const closeIndustry = row => {
    handlePopupClose(industryPopup);
    if (row[0]) {
        partnerStore.inputForm.bizCd = null;
        partnerStore.inputForm.bizNm = null;
        partnerStore.inputForm.bizCd = row[0].detailCd;
        partnerStore.inputForm.bizNm = row[0].detailNm;
    }
};

//-----------------------------------------------
// [관리 공통 팝업]
import dataSetCommon from '@/views/system/base/popup/DataSelectCommonPopup.vue';
const commonPopup = ref(null);

// 팝업 열기 함수
const openPopup = popupRef => {
    if (popupRef.value) {
        popupRef.value.onOpen();
    }
};

// 팝업 닫기 함수
const closePopup = popupRef => {
    if (popupRef.value) {
        popupRef.value.onClose();
    }
};

const openSystemPopup = () => openPopup(commonPopup);
const closeSystemPopup = row => {
    if (row.length > 0) {
        partnerStore.chipsItem = [{ id: row[0].partCompItemId, name: row[0].partCompItemNm }];
        partnerStore.inputForm.partCompItem = row[0].partCompItemId;
        partnerStore.inputForm.partCompItemNm = row[0].partCompItemNm;
    }
    closePopup(commonPopup);
};
//-----------------------------------------------

// const addSystemPopup = () => handlePopupOpen(systemPopup);

// import { getSystemCode } from '@/stores/system/setting/api/SystemCode.js';

// 인원 팝업 닫기
const closePeoplePopup = () => {
    if (peoplePopup.value) {
        peoplePopup.value.onClose();
    }
};
const initializeItems = async () => {
    const { partCompItem, partCompItemNm } = partnerStore.inputForm || {};

    if (partnerStore.inputForm?.cmd === 'I') {
        layoutStore.useBtnList = ['btnBack', 'btnSave'];
    } else {
        layoutStore.useBtnList = ['btnBack', 'btnSearch', 'btnAdd', 'btnSave', 'btnDelete'];
    }

    partnerStore.chipsItem = [{ id: partCompItem, name: partCompItemNm }];

    partnerStore.setRef(dialogPostNo);
    partnerStore.setRefs(fileList, fileListLoc);

    const searchFiles = () => {
        if (fileList.value && fileList.value.fnSearch) fileList.value.fnSearch();
        if (fileListLoc.value && fileListLoc.value.fnSearch) fileListLoc.value.fnSearch();
    };

    searchFiles();
};

const detailSave = async () => {
    const isValid = await validationStore.validateAllFields('form', true);
    if (isValid) await partnerStore.btnSave();
};

onMounted(() => {
    if (!partnerStore.inputForm) {
        router.go(-1);
        return;
    }
    initializeItems();
});

btnSearch(() => {
    partnerStore.getPartDetail(partnerStore.partCompId, true).then(async res => {
        if (fileList.value && fileList.value.fnSearch) fileList.value.fnSearch();
        if (fileListLoc.value && fileListLoc.value.fnSearch) fileListLoc.value.fnSearch();
    });
});
btnBack(() => {
    confirmMsg('warning', '저장되지 않은 정보가 있습니다. \n 그래도 계속하시겠습니까?', null, { fun: partnerStore.goBack, param: '' });
});

btnSave(detailSave);
btnAdd(() => {
    if (partnerStore.inputForm) {
        confirmMsg('info', '저장되지 않은 정보가 있습니다. 그래도 이동하시겠습니까?', '', { fun: partnerStore.btnAdd, param: '' });
    }
});
btnDelete(() => {
    if (partnerStore.inputForm.cmd === 'U') {
        partnerStore.btnDelete('D');
    }
});

const regDtChanged = item => {
    partnerStore.inputForm.regDt = item.target.value;
};
</script>
