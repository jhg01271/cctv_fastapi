<template>
    <div class="contents">
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
                <div class="row gutters1rem">
                    <div class="grid12-12 us-grid12-12">
                        <div class="field">
                            <label for="">
                                <span>CCTV ID</span>
                            </label>
                            <input v-model="safetyDetectionCameraManageStore.inputForm.cctv_id" v-input type="text" class="w100p radius" id="" disabled="false" />
                        </div>
                    </div>
                    <div class="grid12-8 us-grid12-12">
                        <div class="field">
                            <label for="cctvName" required>
                                <span>CCTV 명</span>
                            </label>
                            <input v-model="safetyDetectionCameraManageStore.inputForm.cctv_name" v-input type="text" class="w100p radius" id="cctvName" required/>
                        </div>
                    </div>
                    <div class="grid12-4 us-grid12-12">
                        <div class="field">
                            <label for="serverId" required>
                                <span>서버 ID</span>
                            </label>
                            <select title="서버 ID" id="serverId" v-select class="radius" v-model="safetyDetectionCameraManageStore.inputForm.server_id" required>
                                <option v-for="item in serverIdList" :key="item.server_id" :value="item.server_id">
                                    {{ item.server_name }}
                                </option>
                            </select>
                        </div>
                    </div>
                    <div class="grid12-12 us-grid12-12">
                        <div class="field">
                            <label for="">
                                <span>CCTV 설명</span>
                            </label>
                            <input v-model="safetyDetectionCameraManageStore.inputForm.camera_desc" v-input type="text" class="w100p radius" id="" required/>
                        </div>
                    </div>
                    <div class="grid12-12 us-grid12-12">
                        <div class="field">
                            <label for="rtspAdd" required>
                                <span>CCTV 주소</span>
                            </label>
                            <input v-model="safetyDetectionCameraManageStore.inputForm.rtsp_add" v-input type="text" class="w100p radius" id="rtspAdd" required/>
                        </div>
                    </div>
                    <div class="grid12-12 us-grid12-12">
                        <div class="field">
                            <label for="">
                                <span>비고</span>
                            </label>
                            <textarea v-model="safetyDetectionCameraManageStore.inputForm.remark" class="w100p radius minh20rem" id="" rows="4"></textarea>
                        </div>
                    </div>
                </div>
            </OverlayScrollbarsComponent>
        </div>
    </div>
</template>

<script setup>
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import BaseView from '@/components/base/BaseView';
const { ref, toastPopup, openLoading, endLoading, confirmMsg, alertMsg, validationStore, onMounted, t, formatDate, watch, btnSearch, btnBack, btnSave, btnDelete, setRouterParam, getCompId } = BaseView();
import router from '@/router';
import { useButtonListStore } from '@/stores/buttonList';
const layoutStore = useButtonListStore();
import _ from 'lodash';

import { useSafetyDetectionCameraManageStore } from '@/stores/safewiz/realTimeVideoSafetyManagement/SafetyDetectionCameraManage.js';
const safetyDetectionCameraManageStore = useSafetyDetectionCameraManageStore();

onMounted(async () => {
    const param = setRouterParam();
    console.log('# param 정보', param);

    if (param) {
        // 상세보기 or 라우터 이동인 경우 state를 전달받음
        safetyDetectionCameraManageStore.inputForm.cmd = 'U';
        safetyDetectionCameraManageStore.inputForm.cctv_id = param.cctv_id;
        layoutStore.useBtnList = ['btnBack', 'btnSearch', 'btnSave', 'btnDelete'];
        await safetyDetectionCameraManageStore.getDataDetailGrid();
        // 컴포넌트 리스트 호출
        initComponentDataList();
    } else if (!safetyDetectionCameraManageStore.inputForm || !safetyDetectionCameraManageStore.inputForm.cmd) {
        // 새로고침이거나 데이터가 소실된 경우 카드 뷰로 강제 이동
        router.push('SafetyDetectionCameraManage');
        return;
    } else {
        // 추가버튼으로 왔을 때
        layoutStore.useBtnList = ['btnBack', 'btnSave'];
        await initComponentDataList();
    }
});

// 서버 리스트 ID 코드
const serverIdList = ref([]);

// 컴포넌트 리스트 호출
const initComponentDataList = async () => {
    openLoading();

    serverIdList.value = await safetyDetectionCameraManageStore.getServerList(safetyDetectionCameraManageStore.userCd);

    endLoading();
};

// 목록 버튼 이벤트 함수
btnBack(() => {
    if (!_.isEqual(safetyDetectionCameraManageStore.OrgDetailData, safetyDetectionCameraManageStore.inputForm)) {
        confirmMsg('warning', t('msgSaveContinue'), '', { fun: goBack });
    } else goBack();
});

const goBack = () => {
    router.push('/SafetyDetectionCameraManage');
};

// 조회 버튼 이벤트 함수
btnSearch(async () => {
    await safetyDetectionCameraManageStore.getDataDetailGrid();
});

// 저장 버튼 이벤트 함수
btnSave(async () => {
    const isValid = await validationStore.validateAllFields('form', true);
    if (isValid) {
        if (safetyDetectionCameraManageStore.inputForm.cmd === 'U' || safetyDetectionCameraManageStore.inputForm.cmd === 'I') {
            confirmMsg('info', t('msgSave'), '', { fun: saveAction });
        } else {
            alertMsg('warning', t('msgSaveFail'));
        }
    }
});

const saveAction = async () => {
    await safetyDetectionCameraManageStore.btnSave();
};

// 삭제 버튼 이벤트 함수
btnDelete(() => {
    safetyDetectionCameraManageStore.btnDelete('Detail');
});
</script>
