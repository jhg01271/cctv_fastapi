<template>
    <!-- 콘텐츠 영역 -->
    <h3>공정 구분</h3>

    <div class="form ui">
        <div class="field df aic mb2-2rem">
            <input v-input type="text" class="radius w100p search" placeholder="검색어를 입력하세요" v-model="filterValue" @keydown.enter="filterFromString" />
            <button type="submit" v-button @click="filterFromString">
                <img src="/assets/img/common/icon_search.svg" alt />
            </button>
        </div>

        <div class="bd1pxsolide1e6ed br4px">
            <div class="row flex minh40rem">
                <div class="grid12-4 bdr1pxsolide1e6ed">
                    <OverlayScrollbarsComponent
                        class="maxh35rem"
                        :options="{
                            scrollbars: {
                                autoHide: 'hover',
                                x: 'hidden',
                                y: 'visible'
                            }
                        }"
                    >
                        <ul class="pb5rem">
                            <li class="pa8px bdb1pxsolide1e6ed" v-for="(item, index) in segments" :key="index" :class="{ selected: item === selectedMaster }">
                                <button class="px1rem w100p h4-4rem c9ea1a6 fs1-5rem br4px tal" type="button" v-button @click="clickMaster(item)">
                                    <span>{{ item }}</span>
                                </button>
                            </li>
                        </ul>
                    </OverlayScrollbarsComponent>
                </div>

                <div class="grid12-8">
                    <OverlayScrollbarsComponent
                        class="maxh35rem"
                        :options="{
                            scrollbars: {
                                autoHide: 'hover',
                                x: 'hidden',
                                y: 'visible'
                            }
                        }"
                    >
                        <div>
                            <ul class="ma1-6rem tal form ui" v-if="filteredList().length > 0">
                                <li v-for="(item, index) in filteredList()" :key="index" class="df aic mb1rem cp" @click="selectItem(item)">
                                    <!-- <span class="df aic"><i class="w22px h22px lh22px mr5px br4px bc000000 tac fs1-5rem cffffff ofc bsc">홍</i> {{ item.hrNm }} (과장)</span> -->
                                    <span class="w100p py8px px1-2rem bd1pxsolide1e6ed br4px fs0 df aic">
                                        <input 
                                            type="checkbox" 
                                            v-input 
                                            class="mr1rem" 
                                            v-model="item.selected" 
                                            @click.stop
                                        />
                                        <em class="fs1-5rem fw500">{{ item.processNm }}</em>
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

        <div class="tar mt2rem">
            <button type="button" v-button class="btn w80px radius ml4px bright-grey" @click="closePopup">
                <span>{{ t('close') }}</span>
            </button>
        </div>
    </div>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue';

import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import BaseView from '@/components/base/BaseView';
import { getAuditResult } from '@/stores/safewiz/evaluation/api/auditResultReportApi';
import { getProcessList } from '@/stores/safewiz/planning/api/ClassificationOfHazardsApi';
import { useClassificationOfHardsStore } from '@/stores/safewiz/planning/classificationOfHazrds';
const { getCurrentDate, t, getCompId } = BaseView();
const emit = defineEmits(['close', 'selected']);
const classificationOfHardsStore = useClassificationOfHardsStore();
const props = defineProps({
    single: {
        type: Boolean,
        default: false
    }
});

const filterValue = ref('');
const filterFromString = () => {
    getProcessList(searchParam.value, true).then(res => {
        if (!filterValue.value == '') {
            list.value = res.list;
        }
        list.value = res.list.filter(el => {
            return el.processNm.includes(filterValue.value);
        });
        segments.value = [];
        list.value.forEach(item => {
            item.orgnList.forEach(el => {
                if (!segments.value.includes(el.nm)) {
                    segments.value.push(el.nm);
                }
            });
        });
        selectedMaster.value = segments.value[0];
    });
};

const closePopup = () => {
    emit('close', []);
};

const selectItem = item => {
    emit('close', [item]);
};

const searchParam = ref({
    compId: getCompId(),
    writeYear: classificationOfHardsStore.writeYear
});

const segments = ref([]);
const list = ref([]);
onMounted(() => {
    getProcessList(searchParam.value, true).then(res => {
        list.value = res.list;
        segments.value = [];
        res.list.forEach(item => {
            item.orgnList.forEach(el => {
                if (!segments.value.includes(el.nm)) {
                    segments.value.push(el.nm);
                }
            });
        });
        selectedMaster.value = segments.value[0];
    });
});
const selectedMaster = ref('');
const filteredList = () => {
    let data = list.value.filter(el => {
        let include = false;
        el.orgnList.forEach(orgn => {
            if (orgn.nm == selectedMaster.value) {
                include = true;
            }
        });
        return include;
    });
    return data;
};

const clickMaster = item => {
    selectedMaster.value = item;
};
</script>
<style lang="scss" scoped>
.form.ui {
    ul {
        li {
            &.selected {
                button {
                    background: #f8f9fb;
                    color: #3248f6;
                }
                span {
                    border-color: #3248f6;
                    background: #ebedff;
                }
            }
        }
    }
}
</style>
