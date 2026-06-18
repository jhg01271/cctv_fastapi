<template>
    <!-- 콘텐츠 영역 -->
    <h3>안전보건 내부심사 결과보고서 선택</h3>

    <div class="form ui field df aic mb2-2rem">
        <input v-input type="text" class="radius w100p search" placeholder="검색어를 입력하세요" v-model="filterValue" @keydown.enter="filterFromString" />
        <button type="submit" v-button @click="filterFromString">
            <img src="/assets/img/common/icon_search.svg" alt />
        </button>
    </div>

    <div class="segment mt1-2rem bd1pxsolide1e6ed br4px">
        <div class="row flex minh40rem">
            <div class="grid12-6 pr">
                <OverlayScrollbarsComponent
                    class="maxh40rem"
                    :options="{
                        scrollbars: {
                            autoHide: 'hover',
                            x: 'hidden',
                            y: 'visible'
                        }
                    }"
                >
                    <ul class="bdr1pxsolide1e6ed pb5rem">
                        <li class="pa8px bdb1pxsolide1e6ed wbka" v-for="(item, index) in dataList" :key="index">
                            <button class="w100p db py8px px1-3rem minh3-4rem c9ea1a6 fs1-5rem fw500 br4px tal lh1-5" type="button" v-button :class="{ selected: selectedMaster === item }" @click="clickMaster(item)">
                                <span>{{ item.auditNm }}</span>
                            </button>
                        </li>
                    </ul>
                </OverlayScrollbarsComponent>
            </div>

            <div class="grid12-6">
                <OverlayScrollbarsComponent
                    class="maxh40rem"
                    :options="{
                        scrollbars: {
                            autoHide: 'hover',
                            x: 'hidden',
                            y: 'visible'
                        }
                    }"
                >
                    <div>
                        <ul v-if="selectedMaster && selectedMaster.auditOrgnList && selectedMaster.auditOrgnList.length > 0" class="ma1-6rem tal form ui">
                            <li v-for="(item, index) in selectedMaster.auditOrgnList" :key="index" class="df aic mb1rem cp" @click="selectItem(item)">
                                <!-- <span class="df aic"><i class="w22px h22px lh22px mr5px br4px bc000000 tac fs1-5rem cffffff ofc bsc">홍</i> {{ item.hrNm }} (과장)</span> -->
                                <span class="df aic">
                                    <input type="checkbox" v-input class="mr1rem" v-model="item.selected" />
                                    <em class="fs1-5rem fw500">{{ item.orgnNm }}</em>
                                </span>
                            </li>
                        </ul>
                        <ul v-else class="ma1-6rem tal form ui">
                            <li class="df aic mb1rem cp">
                                <!-- <span class="df aic"><i class="w22px h22px lh22px mr5px br4px bc000000 tac fs1-5rem cffffff ofc bsc">홍</i> {{ item.hrNm }} (과장)</span> -->
                                <em class="fs1-5rem fw500">{{ '상세 정보가 없습니다.' }}</em>
                            </li>
                        </ul>
                    </div>
                </OverlayScrollbarsComponent>
            </div>
        </div>
    </div>

    <div class="form ui tar mt2-5rem">
        <button type="button" v-button class="btn w80px radius ml4px bright-grey" @click="closePopup">
            <span>{{ t('close') }}</span>
        </button>
    </div>
</template>

<script setup>
import { onMounted, ref } from 'vue';

import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import BaseView from '@/components/base/BaseView';
import { getAuditResult } from '@/stores/safewiz/evaluation/api/auditResultReportApi';
const { getCurrentDate, t } = BaseView();
const emit = defineEmits(['close', 'selected']);
const props = defineProps({
    single: {
        type: Boolean,
        default: false
    }
});

const filterValue = ref('');
const filterFromString = () => {};

const closePopup = () => {
    emit('close');
};

const selectItem = item => {
    item.auditNm = selectedMaster.value.auditNm;
    console.log(item);
    emit('selected', item);
};

import { useAuditStore } from '@/stores/safewiz/evaluation/internalAudit.js';
const auditStore = useAuditStore();

const dataList = ref();
const selectedMaster = ref({});
onMounted(() => {
    getAuditResult({ writeYear: auditStore.writeYear }, true).then(res => {
        dataList.value = res.list;
        for (let i = 0; i < dataList.value.length; i++) {
            dataList.value[i].auditOrgnList = dataList.value[i].auditOrgnList.filter(el => {
                return el.submitYn == 'Y';
            });
        }
        dataList.value = dataList.value.filter(el => {
            return el.auditOrgnList.length > 0;
        });
        selectedMaster.value = dataList.value[0];
    });
});

const clickMaster = item => {
    selectedMaster.value = item;
};
</script>

<style lang="scss" scoped>
.segment {
    .grid12-6 {
        &::after {
            display: inline-block;
            width: 1px;
            height: 100%;
            right: 0;
            top: 0;
            background: #e1e6ed;
        }
        ul {
            li {
                button {
                    &.selected {
                        background: #f8f9fb;
                        color: #3248f6;
                    }
                }
            }
        }
    }
}
</style>
