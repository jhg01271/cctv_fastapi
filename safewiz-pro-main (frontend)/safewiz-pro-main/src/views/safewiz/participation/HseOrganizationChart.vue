<template>
    <!-- 콘텐츠 영역 -->
    <div class="contents df fdc">
        <!-- 검색 필드 -->
        <div class="control-field ui form mb2-2rem">
            <div class="row flex gutters1rem">
                <div class="grid12-3 ul-grid12-4 lg-grid12-6 es-grid12-12">
                    <div class="field">
                        <input
                            v-input
                            type="text"
                            v-calendar="'yyyy'"
                            year
                            v-model="writeYear"
                            @input="search(true)"
                            class="datepicker radius"
                        />
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
            <div class="oh">
                <!-- gutters로 생긴 x축 스크롤을 방지하기위한 'oh' 태그 -->
                <div class="row flex gutters1rem">
                    <i-card-menu :menuList="menuList" @click="btnDetail" />
                </div>
            </div>
        </OverlayScrollbarsComponent>
    </div>
</template>

<script setup>
import router from '@/router/index.js';
import BaseView from '@/components/base/BaseView';
import { watchEffect } from 'vue';
import { getOrganizationChart } from '@/stores/safewiz/participation/api/safetyOrgChartApi';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import { useButtonListStore } from '@/stores/buttonList';
import { getHseOrganizationChart } from '@/stores/safewiz/participation/api/hseObjectivesApi';
const { t, getCompId, onMounted, btnSearch, computed, getCurrentDate, ref } = BaseView();

const writeYear = ref(getCurrentDate().substring(0, 4));

const buttonListStore = useButtonListStore();
buttonListStore.useBtnList = ['btnSearch'];
const compId = getCompId();

const searchParam = ref({
    compId: compId,
    searchText: getCurrentDate().substring(0, 4)
});

btnSearch(async () => {
    search(true);
});

const menuList = ref([]);

const search = async notify => {
    searchParam.value.searchText = writeYear;
    getHseOrganizationChart(searchParam.value, notify).then(res => {
        menuList.value = res.list;
    });
};

const data = ref([]);
const filteredData = computed(() => {
    let arr = [];
    if (data.value) {
        arr = data.value.filter(el => {
            return el.chartId.substring(0, 4) == writeYear.value;
        });
    }
    return arr.length ? true : false;
});

onMounted(async () => {
    search(true);
});

const btnDetail = async route => {
    //페이지 이동
    router.push(route);
};
</script>
