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
                            v-model="committeeStore.writeYear"
                            @input="search(true)"
                            class="datepicker radius w100p"
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
const { getCompId, getCurrentDate, btnSearch } = BaseView();

import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import { watchEffect } from 'vue';
import { useCommitteeStore } from '@/stores/safewiz/participation/committee';

import { getParticipation, getConsultationAndParticipation } from '@/stores/safewiz/participation/api/participation';

const committeeStore = useCommitteeStore();
const { t, onMounted, computed, ref } = BaseView();
const data = ref([]);
const data2 = ref([]);
const compId = getCompId();
const searchParam = ref({
    compId: compId,
    searchText: getCurrentDate().substring(0, 4)
});

import { useButtonListStore } from '@/stores/buttonList';
const layoutStore = useButtonListStore();
layoutStore.useBtnList = ['btnSearch'];

// const menuList = ref([
//     {
//         title: '산업안전보건위원회',
//         route: '/OhsCommittee',
//         description: '1. 안전보건경영시스템을 운영하는 데 있어서 모든 계층과 관련 근로자 또는 근로자 대표의 참여 협의를 위한 책임사항 및 요구사항에 대한 관련 활동을 규정',
//         activeYn: 'Y'
//     },
//     {
//         title: '협력업체위원회',
//         route: '/PartnerCommittee',
//         description: '1. 안전보건경영시스템을 운영하는 데 있어서 모든 계층과 관련 근로자 또는 근로자 대표의 참여 협의를 위한 책임사항 및 요구사항에 대한 관련 활동을 규정',
//         activeYn: 'Y'
//     }
// ]);

const filteredData = computed(() => {
    let arr = [];
    if (data.value) {
        arr = data.value.filter(el => {
            return el.writeYear == committeeStore.writeYear;
        });
    }
    return arr.length ? true : false;
});
const filteredData2 = computed(() => {
    let arr = [];
    if (data2.value) {
        arr = data2.value.filter(el => {
            return el.writeYear == committeeStore.writeYear;
        });
    }
    return arr.length ? true : false;
});

onMounted(async () => {
    search(true);

    // getParticipation('OHC').then(res => {
    //     data.value = res.list;
    // });
    // getParticipation('PAC').then(res => {
    //     data2.value = res.list;
    // });
});

btnSearch(() => {
    search(true);
});

const menuList = ref([]);

const search = async notify => {
    searchParam.value.searchText = committeeStore.writeYear;
    getConsultationAndParticipation(searchParam.value, notify).then(res => {
        menuList.value = res.list;
    });
};

// watchEffect(() => {
//     menuList.value[0].activeYn = filteredData.value ? 'Y' : 'N';
//     menuList.value[1].activeYn = filteredData2.value ? 'Y' : 'N';
// });

const btnDetail = async route => {
    //페이지 이동
    router.push(route);
};
</script>
