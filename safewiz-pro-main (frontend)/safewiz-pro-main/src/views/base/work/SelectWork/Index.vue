<template>
    <!-- 콘텐츠 영역 -->
    <h3>공정 작업 내용</h3>

    <div class="form ui field">
        <div class="df aic">
            <input v-input type="text" class="radius w100p search" placeholder="검색어를 입력하세요" v-model="selectWorkStore.filterValue" @keydown.enter="selectWorkStore.filterFromString" />
            <button type="submit" v-button @click="selectWorkStore.filterFromString">
                <img src="/assets/img/common/icon_search.svg" alt />
            </button>
        </div>

        <div class="bd1pxsolide1e6ed br4px mt2rem wbka">
            <div class="row flex minh40rem">
                <div class="grid12-4 bdr1pxsolide1e6ed es-grid12-12 es-bdr0pxsolide1e6ed es-bdb1pxsolide1e6ed es-pb5rem">
                    <OverlayScrollbarsComponent
                        class="maxh40rem es-maxh20rem es-bdb1pxsolide1e6ed"
                        :options="{
                            scrollbars: {
                                autoHide: 'hover',
                                x: 'hidden',
                                y: 'visible'
                            }
                        }"
                    >
                        <ul>
                            <li class="pa8px bdb1pxsolide1e6ed" v-for="(item, index) in selectWorkStore.filteredProcWorkList" :key="index" :class="{ selected: selectWorkStore.selectedIndex == index }">
                                <button class="px1rem w100p c9ea1a6 fs1-5rem br4px tal" type="button" v-button @click="selectWorkStore.clickItem(index)">
                                    <span>{{ item.processNm }}</span>
                                </button>
                            </li>
                        </ul>
                    </OverlayScrollbarsComponent>
                </div>

                <div class="grid12-8 es-grid12-12" v-if="props.single">
                    <OverlayScrollbarsComponent
                        class="maxh40rem es-maxh20rem"
                        :options="{
                            scrollbars: {
                                autoHide: 'hover',
                                x: 'hidden',
                                y: 'visible'
                            }
                        }"
                    >
                        <div v-if="selectWorkStore.filteredProcWorkList.length > 0">
                            <ul v-if="selectWorkStore.filteredProcWorkList[selectWorkStore.selectedIndex].workList.length > 0" class="ma1-6rem tal form ui us-ma5px">
                                <li class="df aic mb1rem cp" v-for="(item, index) in selectWorkStore.filteredProcWorkList[selectWorkStore.selectedIndex].workList" :key="item.workContent" @click="selectItem(item)">
                                    <!-- <span class="df aic"><i class="w22px h22px lh22px mr5px br4px bc000000 tac fs1-5rem cffffff ofc bsc">홍</i> {{ item.hrNm }} (과장)</span> -->
                                    <input type="checkbox" class="fs1-5rem fw500" v-input="item[props.workContent]" v-model="item.selected" />
                                </li>
                            </ul>
                            <ul v-else class="ma1-6rem tal form ui us-ma5px">
                                <li class="w100p py8px px1-2rem df aic bd1pxsolide1e6ed br4px fs0 mb1rem cp">
                                    <!-- <span class="df aic"><i class="w22px h22px lh22px mr5px br4px bc000000 tac fs1-5rem cffffff ofc bsc">홍</i> {{ item.hrNm }} (과장)</span> -->
                                    <em class="fs1-5rem fw500">{{ '작업 내용이 없습니다.' }}</em>
                                </li>
                            </ul>
                        </div>
                    </OverlayScrollbarsComponent>
                </div>
                <div class="grid12-8 es-grid12-12 wbka" v-else>
                    <OverlayScrollbarsComponent
                        class="maxh40rem es-maxh20rem"
                        :options="{
                            scrollbars: {
                                autoHide: 'hover',
                                x: 'hidden',
                                y: 'visible'
                            }
                        }"
                    >
                        <div v-if="selectWorkStore.filteredProcWorkList.length > 0">
                            <ul v-if="selectWorkStore.filteredProcWorkList[selectWorkStore.selectedIndex].workList.length > 0" class="ma1-6rem tal form ui us-ma5px">
                                <li class="df aic mb1rem cp" v-for="(item, index) in selectWorkStore.filteredProcWorkList[selectWorkStore.selectedIndex].workList" :key="item.workContent">
                                    <!-- <span class="df aic"><i class="w22px h22px lh22px mr5px br4px bc000000 tac fs1-5rem cffffff ofc bsc">홍</i> {{ item.hrNm }} (과장)</span> -->
                                    <span class="df aic">
                                        <input type="checkbox" v-input="item.workContent" class="mr1rem fs1-5rem fw500" v-model="item.selected" @change="selectItem(item)" />
                                    </span>
                                </li>
                            </ul>
                            <ul v-else class="ma1-6rem tal form ui us-ma5px">
                                <li class="df aic mb1rem cp">
                                    <!-- <span class="df aic"><i class="w22px h22px lh22px mr5px br4px bc000000 tac fs1-5rem cffffff ofc bsc">홍</i> {{ item.hrNm }} (과장)</span> -->
                                    <em class="fs1-5rem fw500">{{ '작업 내용이 없습니다.' }}</em>
                                </li>
                            </ul>
                        </div>
                    </OverlayScrollbarsComponent>
                </div>
            </div>
        </div>
    </div>
    <!-- <div class="row flex gutters12px ui">
        <div class="col12-6 md-col12-12">
            <button type="button" class="btn radius w100p mb4px" :class="{ dark: selectWorkStore.selectedIndex == index }" v-button v-for="(item, index) in selectWorkStore.menuList[`${selectWorkStore.currentTab}List`]" :key="index" @click="selectWorkStore.clickItem(index)" v-show="selectWorkStore.getChild(index).length">
                <span>{{ item.orgnNm || item.partCompNm }}</span>
            </button>
    </div>
        <div class="col12-6 md-col12-12 md-mt12px" v-if="props.single">
            <div class="btn radius w100p mb4px active jcsb df" v-button v-for="(item, index) in selectWorkStore.filteredPrcList" :key="index" @click="selectItem(item)">
                <span>{{ item.hrNm }}111</span>
            </div>
    </div>
        <div class="col12-6 md-col12-12 md-mt12px">
            <div class="btn radius w100p mb4px active jcsb df" v-button v-for="(item, index) in selectWorkStore.filteredPrcList" :key="item.hrId">
                <span>{{ item.hrNm }}2222</span>
                <input type="checkbox" class="mr1rem" v-model="item.selected" />
            </div>
    </div>
    </div> -->
</template>

<script setup>
import { useSelectWorkStore } from './SelectWork';
import { onMounted } from 'vue';

import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';

const selectWorkStore = useSelectWorkStore();
const emit = defineEmits(['close', 'selected']);
const props = defineProps({
    single: {
        type: Boolean,
        default: false
    },
    selectedList: {
        type: Array,
        default: []
    }
});

onMounted(() => {
    selectWorkStore.init(props.single, props.selectedList);
});

const selectItem = item => {
    console.log('@@ ', item);

    if (props.single) {
        selectWorkStore.singleSelect(item);
        emit('selected', item);
    } else {
        selectWorkStore.multiSelect(item);
        emit('selected', selectWorkStore.selectedValues);
    }
};
</script>

<style lang="scss" scoped>
.form.ui {
    ul {
        li {
            &.selected {
                background: #f8f9fb;
                button {
                    color: #3248f6;
                }
            }
        }
    }
}
</style>
