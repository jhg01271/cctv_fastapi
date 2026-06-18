<template>
    <!-- 콘텐츠 영역 -->
    <div class="px2-2rem pt2-2rem pb1-2rem">
        <h2>내부심사원 자격인증 평가표 관리</h2>
    </div>
    <hr />
    <div class="pa2-2rem">
        <div class="form ui segment bd1pxsolidE1E6ED br4px">
            <div class="row flex">
                <div class="grid12-3 bdr1pxsolidE1E6ED lg-grid12-3 us-grid12-4">
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
                        <ul>
                            <li class="py1-4rem px1-2rem bdb1pxsolidE1E6ED us-py1rem us-px1rem" v-for="(item, index) in qualificationCertStore.columnList" :key="index">
                                <button class="w100p fs1-5rem fw400 us-fs1-4rem tal" type="button" v-button @click="qualificationCertStore.clickColumn(item)">
                                    <span>{{ item.title }}</span>
                                </button>
                            </li>
                        </ul>
                    </OverlayScrollbarsComponent>
                </div>

                <div class="grid12-9 lg-grid12-9 us-grid12-8">
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
                        <ul class="df fdc rg8px pa2-2rem us-pa1rem">
                            <li class="pa2-2rem bcF8F9FB br4px us-pa1rem" v-for="(item, index) in qualificationCertStore.filteredDocList" :key="index" :ref="el => setListRef(el, index)">
                                <!-- <span class="df aic"><i class="w22px h22px lh22px mr5px br4px bc000000 tac fs1-5rem cffffff ofc bsc">홍</i> {{ item.hrNm }} (과장)</span> -->
                                <div class="row flex gutters1rem">
                                    <div class="grid12-6 sm-grid12-12">
                                        <div class="field">
                                            <label> 평가기준 </label>
                                            <input class="br4px" v-input type="text" v-model="item.content" />
                                        </div>
                                    </div>
                                    <div class="grid12-2 sm-grid12-4 us-grid12-6">
                                        <div class="field">
                                            <label for=""> 배점 </label>
                                            <input class="br4px" v-input type="text" v-model="item.point" />
                                        </div>
                                    </div>
                                    <div class="grid12-2 sm-grid12-4 us-grid12-6">
                                        <div class="field">
                                            <label for=""> 순서 </label>
                                            <input class="br4px" v-input type="text" v-model="item.ordSeq" />
                                        </div>
                                    </div>
                                    <div class="grid12-2 sm-grid12-4 us-grid12-12">
                                        <div class="field">
                                            <label for=""> 사용여부 </label>
                                            <div class="df aic h4-4rem">
                                                <input class="df switch" v-input="'사용'" type="checkBox" :checked="item.useYn == 'Y'" @change="toggleCheckYn(item, $event)" />
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </li>
                        </ul>
                    </OverlayScrollbarsComponent>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted, defineExpose } from 'vue';

import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';

import { useQualificationCertStore } from '@/stores/safewiz/support/QualificationCertificationAssessment';

const qualificationCertStore = useQualificationCertStore();

const listRefs = ref([]);

onMounted(() => {
    qualificationCertStore.initColumn();
});
const setListRef = (el, index) => {
    if (el) listRefs.value[index] = el;
};
const toggleCheckYn = (item, event) => {
    const isChecked = event.target.checked;

    item.useYn = isChecked ? 'Y' : 'N';
};
defineExpose({
    listRefs
});
</script>
