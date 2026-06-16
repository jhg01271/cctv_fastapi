<template>
    <div class="contents df fdc">
        <div class="box form ui">
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
                <div id="form">
                    <div class="row flex gutters1rem">
                        <div class="grid12-6">
                            <div class="field">
                                <label for="facilitisNm" required>
                                    <span>{{ t('facilityEquip_facilitisNm') }}</span>
                                </label>
                                <input type="text" class="br4px" placeholder="시설/설비명을 입력하세요." v-model="saveParam.facilitisNm" id="facilitisNm" required />
                            </div>
                        </div>

                        <div class="grid12-2 lg-grid12-6">
                            <div class="field">
                                <label for="" required>
                                    <span>{{ t('facilityEquip_regDt') }}</span>
                                </label>
                                <input type="text" v-calendar="getDateFormat()" class="datepicker w100p br4px" placeholder="2024.09.20" v-model="saveParam.createdAt" readonly />
                            </div>
                        </div>

                        <div class="grid12-2 lg-grid12-6">
                            <div class="field">
                                <label for="">{{ t('facilityEquip_ordSeq') }}</label>
                                <input class="br4px" type="number" v-input value="99" min="0" placeholder="99" v-model="saveParam.ordSeq" />
                            </div>
                        </div>

                        <div class="grid12-2 lg-grid12-6">
                            <div class="field">
                                <label for="useYn">{{ t('facilityEquip_useYn') }}</label>
                                <div class="df aic h4-4rem">
                                    <input v-input="'사용'" type="checkbox" class="df switch" :true-value="'Y'" :false-value="'N'" :checked="saveParam.useYn == 'Y'" v-model="saveParam.useYn" />
                                </div>
                            </div>
                        </div>

                        <div class="grid12-12">
                            <div class="field">
                                <label for="">{{ t('facilityEquip_facilitisDesc') }}</label>
                                <textarea v-input class="br4px minh10rem" placeholder="시설/설비 설명을 입력하세요." v-model="saveParam.facilitisDesc"></textarea>
                            </div>
                        </div>

                        <div class="grid12-12">
                            <div class="field">
                                <label for="">{{ t('facilityEquip_emergencyInfo') }}</label>
                            </div>
                            <div class="br4px pa2rem bd1pxsolidE1E6ED bcFFFFFF">
                                <div class="field">
                                    <iFileList ref="fileList" targetType="facilityEquip" :targetId="saveParam.files" />
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </OverlayScrollbarsComponent>
        </div>
    </div>
</template>

<script setup>
import IFileList from '@/components/file/iFileList.vue';
import { ref, nextTick } from 'vue';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import BaseView from '@/components/base/BaseView';
import router from '@/router';
import { useButtonListStore } from '@/stores/buttonList';
import { useFacilityEquipManage } from '@/stores/safewiz/impl/facilityEquipManage.js';
import { getDateFormat } from '@/utils/dateUtil.js';

const layoutStore = useButtonListStore();
const { t, validationStore, toastPopup, formatDate, getCurrentDate, confirmMsg, alertMsg, onMounted, /* validationStore,   t, watch, */ btnSearch, btnBack, btnAdd, btnSave, btnDelete } = BaseView();
const fileList = ref(null);
const facilityEquipManageStore = useFacilityEquipManage();
const initData = ref({});
const saveParam = ref({});
layoutStore.useBtnList = ['btnSearch', 'btnBack', 'btnAdd', 'btnSave', 'btnDelete'];

//-----------------------------------------------
onMounted(async () => {
    if (!facilityEquipManageStore.inputForm) {
        // 새로고침시 이전 화면으로 이동.
        router.go(-1);
        return;
    }

    saveParam.value = facilityEquipManageStore.inputForm;
    initData.value = JSON.parse(JSON.stringify(saveParam.value));
    if (facilityEquipManageStore.inputForm.cmd === 'I') {
        layoutStore.useBtnList = ['btnBack', 'btnSave'];
    } else {
        layoutStore.useBtnList = ['btnSearch', 'btnBack', 'btnAdd', 'btnSave', 'btnDelete'];
        searchDetail();
        toastPopup('조회성공', '조회되었습니다.');
    }
});

// 목록 클릭
btnBack(() => {
    if (JSON.stringify(saveParam.value) !== JSON.stringify(initData.value)) {
        confirmMsg('info', '저장되지 않은 정보가 있습니다. 그래도 진행하시겠습니까?', '', { fun: goBack });
    } else {
        goBack();
    }
});

btnAdd(() => {
    if (JSON.stringify(saveParam.value) !== JSON.stringify(initData.value)) {
        confirmMsg('info', '저장되지 않은 정보가 있습니다. 그래도 진행하시겠습니까?', '', { fun: convertInsert });
    } else {
        convertInsert();
    }
});

// 조회 클릭
btnSearch(() => {
    if (JSON.stringify(saveParam.value) !== JSON.stringify(initData.value)) {
        confirmMsg('info', '저장되지 않은 정보가 있습니다. 그래도 조회하시겠습니까?', '', { fun: searchDetail });
    } else {
        searchDetail();
        toastPopup('조회성공', '조회되었습니다.');
    }
});

// 저장 클릭
btnSave(async () => {
    // validation check
    const isValid = await validationStore.validateAllFields('form', true);
    if (!isValid) {
        return;
    }

    // 설비/기구 명이 빈 값이면 return
    if (saveParam.value.facilitisNm == '' || saveParam.value.facilitisNm == null) {
        alertMsg('warning', '시설/설비명을 입력하십시오.');
        return;
    }

    confirmMsg('info', '저장 하시겠습니까?', '', { fun: saveDetail, param: true });
});

// 삭제 클릭
btnDelete(() => {
    if (saveParam.value.useYn === 'N') {
        alertMsg('warning', t('msgDeletedItem'));
        return;
    }
    confirmMsg('info', '삭제 하시겠습니까?', '', { fun: deleteDetail, param: true });
});

// 상세 내역 조회
const searchDetail = () => {
    facilityEquipManageStore.searchDetail(false).then(async res => {
        if (res.result && res.result.length > 0) {
            saveParam.value.facilitisId = res.result[0].facilitisId;
            saveParam.value.facilitisNm = res.result[0].facilitisNm;
            saveParam.value.createdAt = formatDate(res.result[0].createdAt);
            saveParam.value.ordSeq = res.result[0].ordSeq;
            saveParam.value.useYn = res.result[0].useYn;
            saveParam.value.facilitisDesc = res.result[0].facilitisDesc;
            saveParam.value.files = res.result[0].files;
            initData.value = JSON.parse(JSON.stringify(saveParam.value));

            // DOM 업데이트까지 기다린다.
            await nextTick();
            fileList.value.fnSearch();
        }
    });
};

// 추가로 변환
const convertInsert = async () => {
    facilityEquipManageStore.initInputForm();
    facilityEquipManageStore.inputForm.cmd = 'I';
    facilityEquipManageStore.inputForm.createdAt = getCurrentDate();
    saveParam.value = facilityEquipManageStore.inputForm;
    // DOM 업데이트까지 기다린다.
    await nextTick();
    fileList.value.fnSearch();
    initData.value = JSON.parse(JSON.stringify(saveParam.value));
};

// 내용 저장
const saveDetail = () => {
    const formData = new FormData();
    saveParam.value.deleteFiles = fileList.value.editFiles.delete;
    formData.append('info', new Blob([JSON.stringify(saveParam.value)], { type: 'application/json' }));
    fileList.value.editFiles.insert.forEach(file => {
        if (file) {
            formData.append('files', file); // 파일이 있을 경우 파일 추가
        } else {
            formData.append('files', new Blob([], { type: 'application/octet-stream' })); // 빈 파일 추가
        }
    });

    facilityEquipManageStore.saveFacilityEquip(formData, true).then(res => {
        if (res.success) {
            layoutStore.useBtnList = ['btnBack', 'btnSearch', 'btnAdd', 'btnSave', 'btnDelete'];
            facilityEquipManageStore.inputForm.cmd = 'U';
            searchDetail();
        }
    });
};

// 삭제
const deleteDetail = () => {
    facilityEquipManageStore.deleteFacilityEquip([saveParam.value], true).then(res => {
        if (res.success) {
            toastPopup('성공적으로 삭제하였습니다.', 'info');
            goBack();
        }
    });
};

// 뒤로가기
const goBack = () => {
    router.go(-1);
};
</script>
