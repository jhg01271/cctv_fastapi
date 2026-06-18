<template>
    <div class="contents df fdc">
        <div class="control-field ui form mb2-2rem">
            <div class="row flex gutters1rem">
                <div class="grid12-3 es-grid12-12">
                    <input v-input type="text" v-calendar="'yyyy'" year v-model="thisStore.searchParam.writeYear" class="datepicker w20rem radius es-w100p" @input="thisStore.searchData(true)" />
                </div>
                <div :class="searchGridClass">
                    <div class="df bcffffff">
                        <input v-model="thisStore.searchParam.searchText" v-input type="text" class="radius w100p search" :placeholder="t('placeHolderSearch')" @keyup.enter="thisStore.searchData(true)" />
                        <button type="submit" class="shrink0" @click.stop="thisStore.searchData(true)">
                            <img src="/assets/img/common/icon_search.svg" alt />
                        </button>
                    </div>
                </div>
                <div class="grid12-2 es-grid12-12" v-if="isSavAh">
                    <div class="df us-fww">
                        <label for=""> </label>
                        <button type="button" class="w100p btn radius active us-w100p" @click="settingOpen">
                            <span>TBM 설정</span>
                        </button>
                    </div>
                </div>
            </div>
        </div>
        <OverlayScrollbarsComponent
            :options="{
                scrollbars: {
                    autoHide: 'hover',
                    x: 'hidden',
                    y: 'visible'
                }
            }"
        >
            <div class="accordion df fdc rg8px">
                <div v-for="(segment, index) in thisStore.seg" :key="index" class="list">
                    <button type="button" :id="`accordion-btn_${index}`" class="df jcsb aic" @click="thisStore.toggleAccordion">
                        <em>{{ t(`tbm_${segment.tbmMonth}Month`) }}</em>
                        <svg xmlns="http://www.w3.org/2000/svg" width="10" height="6" viewBox="0 0 10 6" fill="none">
                            <path d="M9 1L5.4714 4.79026C5.21106 5.06991 4.78894 5.06991 4.5286 4.79026L1 1" stroke="black" stroke-linecap="round" />
                        </svg>
                    </button>

                    <div class="segment oh">
                        <div class="pa2-2rem">
                            <div class="row flex gutters2rem">
                                <template v-for="(data, idx) in segment.dataList" :key="`card_${idx}`">
                                    <i-card :v-model="data.checked" :data="data" :type="'basic'" :title="formatDate(data.tbmDate)" @handle="thisStore.chkEvent" @editor="btnDetail" :disabled="data.useYn == 'N'"></i-card>
                                </template>

                                <div class="grid12-4 ul-grid12-6 lg-grid12-12">
                                    <div class="card h100p df aic jcc cp" @click="btnMonthAdd(segment.tbmMonth)">
                                        <div class="tac">
                                            <svg width="46" height="46" viewBox="0 0 46 46" fill="none" xmlns="http://www.w3.org/2000/svg">
                                                <path d="M31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417M26.0875 9.11005L18.5802 16.6173C15.3165 19.881 13.0012 23.9703 11.8817 28.4481L11.519 29.8991C11.406 30.3512 11.8155 30.7607 12.2676 30.6477L13.7186 30.2849C18.1963 29.1655 22.2857 26.8502 25.5494 23.5865L33.0566 16.0792C33.9808 15.155 34.5 13.9016 34.5 12.5946C34.5 9.873 32.2937 7.66669 29.5721 7.66669C28.2651 7.66669 27.0116 8.18588 26.0875 9.11005Z" stroke="black" stroke-width="1.5" />
                                                <path d="M26.0874 9.11005C27.0116 8.18588 28.265 7.66669 29.572 7.66669C32.2936 7.66669 34.4999 9.873 34.4999 12.5946C34.4999 13.9016 33.9808 15.155 33.0566 16.0792L31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417L26.0874 9.11005Z" stroke="#3248F6" stroke-width="1.5" />
                                                <path d="M36.4167 38.3333H9.58337" stroke="#3248F6" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
                                            </svg>

                                            <span class="db mt1rem fs2rem c999999">{{ t('card_addTbm') }}</span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <!-- 리스트가 없을때 버튼만 보이도록 -->
            <div v-if="thisStore.seg.length === 0" class="list">
                <div class="grid12-4 ul-grid12-6 lg-grid12-12">
                    <div class="card h100p df aic jcc cp" @click="doAdd()">
                        <div class="tac">
                            <svg width="46" height="46" viewBox="0 0 46 46" fill="none" xmlns="http://www.w3.org/2000/svg">
                                <path d="M31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417M26.0875 9.11005L18.5802 16.6173C15.3165 19.881 13.0012 23.9703 11.8817 28.4481L11.519 29.8991C11.406 30.3512 11.8155 30.7607 12.2676 30.6477L13.7186 30.2849C18.1963 29.1655 22.2857 26.8502 25.5494 23.5865L33.0566 16.0792C33.9808 15.155 34.5 13.9016 34.5 12.5946C34.5 9.873 32.2937 7.66669 29.5721 7.66669C28.2651 7.66669 27.0116 8.18588 26.0875 9.11005Z" stroke="black" stroke-width="1.5" />
                                <path d="M26.0874 9.11005C27.0116 8.18588 28.265 7.66669 29.572 7.66669C32.2936 7.66669 34.4999 9.873 34.4999 12.5946C34.4999 13.9016 33.9808 15.155 33.0566 16.0792L31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417L26.0874 9.11005Z" stroke="#3248F6" stroke-width="1.5" />
                                <path d="M36.4167 38.3333H9.58337" stroke="#3248F6" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
                            </svg>
                            <span class="db mt1rem fs2rem c999999">{{ t('card_addTbm') }}</span>
                        </div>
                    </div>
                </div>
            </div>
        </OverlayScrollbarsComponent>
    </div>
    <teleport to="body">
        <i-PopupDialog ref="toolBoxMeetingSettingPopup">
            <!-- 단일 그리드 -->
            <div class="contents w300px md-w100p">
                <ToolBoxMeetingSettingPopup :title="'TBM 설정'" @onSave="settingSave" @onClose="settingClose()" :options="options" />
            </div>
        </i-PopupDialog>
    </teleport>
</template>

<script setup>
import BaseView from '@/components/base/BaseView';
import { useToolBoxMeetingStore } from '@/stores/safewiz/planning/ToolBoxMeeting.js';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import { useButtonListStore } from '@/stores/buttonList';
import { useToolBoxMeetingDetailStore } from '@/stores/safewiz/planning/ToolBoxMeetingDetail.js';
import ToolBoxMeetingSettingPopup from './popup/ToolBoxMeetingSettingPopup.vue';
import { searchSetting, saveSetting } from '@/stores/safewiz/planning/api/ToolBoxMeetingApi.js';

import router from '@/router';
const { ref, onMounted, alertMsg, confirmMsg, t, btnSearch, btnAdd, btnDelete, btnDownload, goRouter, formatDate, getCompId, openLoading, endLoading, computed } = BaseView();
const thisStore = useToolBoxMeetingStore();
const detailStore = useToolBoxMeetingDetailStore();
const layoutStore = useButtonListStore();
layoutStore.useBtnList = ['btnSearch', 'btnAdd', 'btnDelete', 'btnDownload'];

const toolBoxMeetingSettingPopup = ref(null);

btnSearch(() => {
    thisStore.searchData(true);
});

btnAdd(() => {
    doAdd();
});

const doAdd = () => {
    // 조회년도 기준으로 작성년도 설정
    detailStore.inputForm.writeYear = thisStore.searchParam.writeYear;
    thisStore.addData();
};

const btnMonthAdd = tbmMonth => {
    // tbmDate 세팅을 위한 월 데이터
    detailStore.inputForm.month = tbmMonth;
    doAdd();
};

btnDelete(() => {
    thisStore.deleteMsg();
});

btnDownload(() => {
    // 체크한 값이 없을경우 메세지 호출
    if (thisStore.searchedData.filter(data => data.checked).length === 0) {
        alertMsg('warning', t('msgNoItem'));
        return;
    }
    // 체크한 값만 다운로드
    confirmMsg('warning', t('msgCheckedPrint'), null, { fun: detailStore.print, param: thisStore.searchedData.filter(data => data.checked) });
});

const btnDetail = e => {
    const param = {
        writeYear: e.writeYear,
        docType: e.docType,
        docNo: e.docNo
    };
    thisStore.goDetail(e);
    goRouter('ToolBoxMeetingDetail', param);
};

const settingSave = () => {
    confirmMsg('info', t('msgSave'), '', { fun: settingSaveAction, param: options.value });
};

const settingSaveAction = async data => {
    openLoading();
    await saveSetting(data);
    toolBoxMeetingSettingPopup.value.onClose();
    endLoading();
};

const settingClose = () => {
    toolBoxMeetingSettingPopup.value.onClose();
};

const options = ref([]);

const settingOpen = async () => {
    options.value = [];
    // 조회 시 comp_id, key 기준으로 데이터가 없을경우 신규 로우 생성을 위해 name 함께
    const res = await searchSetting({ compId: getCompId(), settingKey: 'showSignTime', settingName: '서명 일시 표시' });

    if (res.list != null && res.list.length > 0) {
        const option = {
            settingName: res.list[0].settingName,
            settingKey: res.list[0].settingKey,
            settingValue: res.list[0].settingValue
        };
        options.value.push(option);
    }

    toolBoxMeetingSettingPopup.value.onOpen();
};

const isSavAh = computed(() => router.currentRoute.value?.meta?.savAh === 'Y');
const searchGridClass = computed(() => (isSavAh.value ? 'grid12-7 es-grid12-12' : 'grid12-9 es-grid12-12'));

onMounted(() => {
    thisStore.searchData(true);
});
</script>

<style lang="scss" scoped></style>
