<template>
    <div id="form" class="contents">
        <div class="control-field ui form br4px box df fdc">
            <OverlayScrollbarsComponent
                ref="overlayScrollbars"
                :options="{
                    scrollbars: {
                        autoHide: 'hover',
                        x: 'hidden',
                        y: 'visible'
                    }
                }"
            >
                <form @submit.prevent ref="myForm">
                    <div class="pa2-2rem bcFFFFFF">
                        <div class="row flex gutters1rem">
                            <div class="grid12-3 lg-grid12-6">
                                <div class="field">
                                    <label required>
                                        <span>{{ t('equipment') }}</span>
                                    </label>
                                    <i-chips :chips="[{ id: safetyMgmtStore.inputForm.equipmentId, name: safetyMgmtStore.inputForm.equipmentNm }]" :readonly="safetyMgmtStore.cmd === 'U'" @popup="addEquipment" @removeChip="removeEquipment" aria-required="true" required></i-chips>
                                </div>
                            </div>

                            <div class="grid12-9">
                                <div class="field">
                                    <label for>{{ t('use') + ' ' + t('organizational') }}</label>
                                    <i-chips :chips="safetyMgmtStore.inputForm.orgnList" :type="'multi'" :readonly="true"></i-chips>
                                </div>
                            </div>
                            <div class="grid12-3 lg-grid12-5 es-grid12-6">
                                <div class="field">
                                    <label for>{{ t('mgmtNum') }}</label>
                                    <input class="br4px" type="text" v-input v-model="safetyMgmtStore.inputForm.equipmentMgmtNum" :readonly="safetyMgmtStore.inputForm.equipmentId === ''" />
                                </div>
                            </div>

                            <div class="grid12-3 lg-grid12-5 es-grid12-12">
                                <div class="field">
                                    <label for>{{ t('mfSpec') }}</label>
                                    <input class="br4px" type="text" v-input v-model="safetyMgmtStore.inputForm.equipmentMfSpec" :readonly="safetyMgmtStore.inputForm.equipmentId === ''" />
                                </div>
                            </div>
                            <div class="grid12-3 lg-grid12-6 us-grid12-12">
                                <div class="field">
                                    <label for="useYn">{{ t('inspectionCycle') }}</label>
                                    <select v-select class="br4px" v-model="safetyMgmtStore.inputForm.inspectionCycle" :disabled="safetyMgmtStore.inputForm.equipmentId === ''">
                                        <option v-for="(period, index) in safetyMgmtStore.periods" :key="index" :value="period.id">{{ period.id }}</option>
                                    </select>
                                </div>
                            </div>
                            <div class="grid12-3 lg-grid12-2 es-grid12-6">
                                <div class="field">
                                    <label for="useYn">{{ t('useYn') }}</label>
                                    <div class="df aic h4-4rem">
                                        <input v-input="'사용'" type="checkbox" class="df switch br4px" v-bind:checked="safetyMgmtStore.inputForm.useYn === 'Y'" @change="safetyMgmtStore.switchUseYn(safetyMgmtStore.inputForm, 'useYn')" :disabled="safetyMgmtStore.inputForm.equipmentId === ''" />
                                    </div>
                                </div>
                            </div>
                            <div class="mt2-2rem grid12-12 bcF9FAFF br4px" v-if="safetyMgmtStore.cmd === 'U'">
                                <!-- 신규일 때는 화면에 출력되지 않도록 수정 -->
                                <div class="pa2-2rem df fdc rg8px">
                                    <!-- 검사 카드 -->
                                    <div :id="`card${index}`" class="bd1pxsolidE1E6ED br4px df fww bcFFFFFF" v-for="(inspection, index) in safetyMgmtStore.inputForm.inspectionList" :key="index" :class="['box', 'df', 'mt2-4rem', 'bcF9FAFF', 'sm-db', { selected: inspection.checked }]">
                                        <div class="w5rem df jcc aic bdr1pxsolidE1E6ED sm-w100p sm-h5rem sm-bdr0pxsolidE1E6ED sm-bdb1pxsolidE1E6ED">
                                            <input type="checkbox" v-input v-model="inspection.checked" />
                                        </div>
                                        <div class="w100pcalc5rem sm-w100p">
                                            <div class="pa2-2rem">
                                                <div class="row flex gutters1rem">
                                                    <div class="grid10-2 us-grid10-10">
                                                        <div class="field">
                                                            <!-- 검사일 -->
                                                            <label>{{ t('inspectionDate') }}</label>
                                                            <input class="datepicker br4px" :id="'inspectionDate' + index" type="text" v-model="inspection.inspectionDate" v-calendar="getDateFormat()" placeholder="2024.10.20" :readonly="inspection.useYn === 'N'" @input="onValueChanged(inspection)" />

                                                            <!-- <iFileList :ref="el => (fileList[index] = el)" targetType="SMOHM" :targetId="item.files" @change="item.checked = true" />-->
                                                        </div>
                                                    </div>
                                                    <div class="grid10-4 es-grid10-10">
                                                        <div class="field">
                                                            <label for>
                                                                <span>검사 유효기간</span>
                                                            </label>
                                                            <div class="df aic">
                                                                <label :for="'inspectionDate' + index" required v-show="false">검사 유효기간(시작)</label>
                                                                <input id="inspectionValidFromDt" v-model="inspection.inspectionValidFromDt" v-input type="text" v-calendar="getDateFormat()" placeholder="2024.10.20" class="datepicker w100p radius mr1rem" @input="onValueChanged(inspection)"/>
                                                                 &sim;
                                                                <label :for="'inspectionDate' + index" required v-show="false">검사 유효기간(종료)</label>
                                                                <input id="inspectionValidToDt" v-model="inspection.inspectionValidToDt" v-input type="text" v-calendar="getDateFormat()" placeholder="2024.10.20" class="datepicker w100p radius ml1rem" @input="onValueChanged(inspection)"/>
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="grid10-2 us-grid10-10">
                                                        <div class="field">
                                                            <!-- 차기 검사일-->
                                                            <label :for="'nextInspectionDate' + index" required>{{ t('nextInspectionDate') }}</label>
                                                            <input class="datepicker br4px" type="text" :id="'nextInspectionDate' + index" v-model="inspection.nextInspectionDate" v-calendar="getDateFormat()" placeholder="2024.10.20" required :readonly="inspection.useYn === 'N'" @input="onValueChanged(inspection)" />
                                                        </div>
                                                    </div>
                                                    <div class="field grid10-2 us-grid10-10">
                                                        <!-- 사용여부 -->
                                                        <label class="sm-mt5px" :for="'useYn' + index" required style="padding-top: 6px;">{{ t('useYn') }}</label>
                                                        <div class="ml4px df" style="align-items: center; height: 50px;">
                                                            <input v-input="'사용'" type="checkbox" class="df switch br4px" @change="safetyMgmtStore.switchUseYn(inspection, 'useYn')" :checked="inspection.useYn === 'Y'" />
                                                        </div>
                                                    </div>
                                                    <div class="grid12-12 us-grid12-12">
                                                        <iFileList :ref="el => (fileList[index] = el)" targetType="SMOHM" :targetId="inspection.files && inspection.files.length > 0 ? inspection.files : []" @change="inspection.checked = true" />
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="w100p df aic jcc">
                                        <button type="button" class="radius df aic" @click="safetyMgmtStore.addCard">
                                            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none">
                                                <rect x="0.5" y="0.5" width="23" height="23" rx="11.5" fill="#EBEDFF" />
                                                <rect x="0.5" y="0.5" width="23" height="23" rx="11.5" stroke="#3248F6" />
                                                <path d="M17 12L7 12M12 17L12 7" stroke="#3248F6" stroke-linecap="round" />
                                            </svg>
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </form>
            </OverlayScrollbarsComponent>
        </div>
        <teleport to="body">
            <!-- 설비 선택 팝업 (단일 선택) -->
            <i-PopupDialog ref="equipmentPopup">
                <div class="contents form ui sm">
                    <base-select-popup :title="t('equipment')" uniqueKey="equipmentId" filterKey="equipmentNm" useYnKey="useYn" :excluded-value="'N'" :single="true" :fetchParam="{ equipmentId: equipmentStore.inputForm.equipmentId }" :fetch-data="safetyMgmtStore.getFilteredEquip" @close="closeEquipChecklist" />
                </div>
            </i-PopupDialog>
        </teleport>
    </div>
</template>
<script setup>
import BaseView from '@/components/base/BaseView';
import { useButtonListStore } from '@/stores/buttonList';
import { gsap } from 'gsap';
import CustomEase from 'gsap/CustomEase';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import baseSelectPopup from '@/views/system/base/popup/BaseSelectPopup.vue';
import { useEquipmentStore } from '@/stores/system/base/equipment';
import router from '@/router';
import { getDateFormat } from '@/utils/dateUtil.js';
import { useSafetyMgmtOfHazardousMachineryStore } from '@/stores/safewiz/improvement/safetyMgmtOfHazardousMachinery';
const { ref, btnSearch, btnBack, btnAdd, btnSave, t, onMounted, btnDelete, btnDownload, watch, validationStore, confirmMsg, alertMsg } = BaseView();
const equipmentStore = useEquipmentStore();
const safetyMgmtStore = useSafetyMgmtOfHazardousMachineryStore();

// ==============파일==============
import IFileList from '@/components/file/iFileList.vue';
const fileList = ref([]);

// ======== 설비 팝업 ========
const equipmentPopup = ref();
// 팝업 오픈
const addEquipment = () => {
    equipmentPopup.value.onOpen();
};
// 팝업 클로즈(팝업에서 가져온 정보 Store에 저장)
const closeEquipChecklist = data => {
    if (data && data[0]) {
        validationStore.clearAllErrors();
        safetyMgmtStore.initInputForm();
        const { equipmentId, equipmentNm, orgnList, useYn, mfSpec, managementId, compId } = data[0];
        safetyMgmtStore.inputForm.cmd = safetyMgmtStore.cmd;
        safetyMgmtStore.inputForm.compId = compId;
        safetyMgmtStore.inputForm.equipmentId = equipmentId;
        safetyMgmtStore.inputForm.equipmentNm = equipmentNm;
        safetyMgmtStore.inputForm.useYn = useYn;
        safetyMgmtStore.inputForm.orgnList = orgnList.length === 0 ? [{ id: '', nm: '미분류' }] : orgnList.map(item => ({ id: item.id, nm: item.nm }));
        safetyMgmtStore.inputForm.equipmentMgmtNum = managementId;
        safetyMgmtStore.inputForm.equipmentMfSpec = mfSpec;
    }

    equipmentPopup.value.onClose();
};
// Store 정보 초기화
const removeEquipment = () => {
    safetyMgmtStore.initInputForm();
};

gsap.registerPlugin(CustomEase);
CustomEase.create('customEase', 'M0,0 C0.9,0 0.2,1 1,1');

onMounted(async () => {
    if (safetyMgmtStore.inputForm.cmd === undefined) {
        // 새로고침시 이전 화면으로 이동.
        router.go(-1);
        return;
    }
    await safetyMgmtStore.buttonChange();
    await equipmentStore.initInputForm();
    await safetyMgmtStore.btnDetailSearch();
    await safetyMgmtStore.file(fileList);
});

// 우측 사이드 버튼 리스트
// const layoutStore = useButtonListStore();
// const uButtonList = [ 'btnBack', 'btnSearch', 'btnAdd', 'btnSave', 'btnDelete', 'btnDownload' ];
// const iButtonList = ['btnBack', 'btnSave'];
// layoutStore.useBtnList = safetyMgmtStore.buttonList;

btnBack(() => {
    safetyMgmtStore.btnBack();
    // router.go(-1);
});

btnAdd(() => {
    safetyMgmtStore.addCard();
});

watch(
    safetyMgmtStore.inputForm,
    newVal => {
        const newCopy = JSON.parse(JSON.stringify(newVal));
        const result = JSON.stringify(safetyMgmtStore.validForm.value) === JSON.stringify(newCopy);
        safetyMgmtStore.isUpdated = result;
    },
    { deep: true }
);

btnSave(async () => {
    safetyMgmtStore.fileInfo = fileList.value;
    await safetyMgmtStore.btnSave();
});

btnSearch(async () => {
    safetyMgmtStore.btnDetailSearch();
    safetyMgmtStore.file(fileList);
});

btnDelete(() => {
    safetyMgmtStore.btnDelInspection();
});

btnDownload(() => {
    confirmMsg('warning', t('msgPrint'), null, { fun: safetyMgmtStore.btnDownloadDetail, param: '' });
});

// 검사일 데이터 변경 되는 경우 체크 박스 선택
const onValueChanged = item => {
    item.checked = true;
};
</script>
