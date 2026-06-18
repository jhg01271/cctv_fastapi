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
                                <span>SERVER ID</span>
                            </label>
                            <input v-model="safetyVideoServerManageStore.inputForm.server_id" v-input type="text" class="w100p radius" id="" disabled='false'/>
                        </div>
                    </div>                       
                    <div class="grid12-6 us-grid12-12">
                        <div class="field">
                            <label for="serverName" required>
                                <span>서버 이름</span>
                            </label>
                            <input v-model="safetyVideoServerManageStore.inputForm.server_name" v-input type="text" id="serverName" class="w100p radius" required/>
                        </div>

                    </div>
                    <div class="grid12-6 us-grid12-12">
                        <div class="field">
                            <label for="serverIp" required>
                                <span>서버 IP</span>
                            </label>
                            <input v-model="safetyVideoServerManageStore.inputForm.server_ip" v-input type="text" class="w100p radius" id="serverIp" required/>
                        </div>
                    </div>
                    <div class="grid12-6 us-grid12-12">
                        <div class="field">
                            <label for="restapiPort" required>
                                <span>플라스크 PORT</span>
                            </label>
                            <input v-model="safetyVideoServerManageStore.inputForm.restapi_port" v-input type="number" class="w100p radius" id="restapiPort" required/>
                        </div>
                    </div>
                    <div class="grid12-6 us-grid12-12">
                        <div class="field">
                            <label for="mediamtxPort" required>
                                <span>스트림서버 포트</span>
                            </label>
                            <input v-model="safetyVideoServerManageStore.inputForm.mediamtx_port" v-input type="number" class="w100p radius" id="mediamtxPort" required/>
                        </div>
                    </div>
                    <div class="grid12-12 us-grid12-12">
                        <div class="field">
                            <label for="">
                                <span>비고</span>
                            </label>
                            <textarea v-model="safetyVideoServerManageStore.inputForm.remark" class="w100p radius minh20rem" id="" rows="4"></textarea>
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

import { useSafetyVideoServerManageStore } from '@/stores/safewiz/realTimeVideoSafetyManagement/SafetyVideoServerManage.js';
const safetyVideoServerManageStore = useSafetyVideoServerManageStore();

onMounted(async () => {
    const param = setRouterParam();

    if (param) {
        // 상세보기 or 라우터 이동인 경우 state를 전달받음
        safetyVideoServerManageStore.resetDetailDataList();
        safetyVideoServerManageStore.inputForm.cmd = 'U';
        safetyVideoServerManageStore.inputForm.server_id = param.server_id;
        layoutStore.useBtnList = ['btnBack', 'btnSearch', 'btnSave', 'btnDelete'];
        await safetyVideoServerManageStore.getDataDetailGrid();
    } else if (!safetyVideoServerManageStore.inputForm || !safetyVideoServerManageStore.inputForm.cmd) {
        // 새로고침이거나 데이터가 소실된 경우 카드 뷰로 강제 이동
        router.push('SafetyVideoServerManage');
        return;
    } else {
        // 추가버튼으로 왔을 때
        layoutStore.useBtnList = ['btnBack', 'btnSave'];
    }
});

// 목록 버튼 이벤트 함수
btnBack(() => {
    if (!_.isEqual(safetyVideoServerManageStore.OrgDetailData, safetyVideoServerManageStore.inputForm)) {
        confirmMsg('warning', t('msgSaveContinue'), '', { fun: goBack });
    } else goBack();
});

const goBack = () => {
    router.push('/SafetyVideoServerManage');
};

// 조회 버튼 이벤트 함수
btnSearch(async () => {
    await safetyVideoServerManageStore.getDataDetailGrid();
});

// 저장 버튼 이벤트 함수
btnSave(async () => {
    const isValid = await validationStore.validateAllFields('form', true);
    if (isValid) {
        if (safetyVideoServerManageStore.inputForm.cmd === 'U' || safetyVideoServerManageStore.inputForm.cmd === 'I') {
            confirmMsg('info', t('msgSave'), '', { fun: saveAction });
        } else {
            alertMsg('warning', t('msgSaveFail'));
        }
    }
});

const saveAction = async () => {
    await safetyVideoServerManageStore.btnSave();
}

// 삭제 버튼 이벤트 함수
btnDelete(() => {
    safetyVideoServerManageStore.btnDelete('Detail');
});
</script>
