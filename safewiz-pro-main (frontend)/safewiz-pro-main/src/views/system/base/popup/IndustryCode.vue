<template>
    <h3>{{ finalOptions.label }}</h3>
    <div class="form ui field df aic mb2-2rem">
        <input v-input type="text" class="radius w100p search" placeholder="검색어를 입력하세요" v-model="searchTerm" />
        <button
            type="submit"
            @click.prevent="
                () => {
                    fetchData();
                }
            "
        >
            <img src="/assets/img/common/icon_search.svg" alt="검색" />
        </button>
    </div>
    <OverlayScrollbarsComponent ref="scrollContainer" class="bdt1pxsolide1e6ed" :options="scrollOptions" @osScroll="onScroll">
        <i-DataGrid
            ref="gridIndustry"
            gridId="gridIndustry"
            style="height: 270px"
            :options="{
                rowHeaders: [{ type: 'rowNum', width: 60 }],
                columns: gridColumns,
                rowHeight: 44,
                bodyHeight: 'fitToParent'
            }"
            :columns="gridColumns"
            @rowClick="clickGridEquipment"
        />
    </OverlayScrollbarsComponent>
    <span class="pa" v-if="loading">Loading...</span>
    <!-- 로딩 상태 표시 -->
    <span v-if="noMoreData">더 이상 데이터가 없습니다.</span>
    <!-- 더 이상 데이터가 없음을 표시 -->
</template>

<script setup>
import { ref, onMounted, watch } from 'vue';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import { useDatasetStore } from '@/stores/safewiz/dataset/dataset.js';
import FemsUtils from '@/components/base/FemsUtils';

// Pinia 스토어 불러오기
const datasetStore = useDatasetStore();
const { deepMerge } = FemsUtils();
const emit = defineEmits(['close']);

// Props 정의
const props = defineProps({
    options: {
        type: Object,
        default: () => ({})
    }
});

// 데이터 초기화 및 필터 상태 설정
const options = { label: '', readonly: false };
const finalOptions = deepMerge(options, props.options);
const searchTerm = ref('');
const gridData = ref([]); // 데이터 리스트를 담을 변수
const gridIndustry = ref(null);
const loading = ref(false);
const noMoreData = ref(false); // 더 이상 데이터 없음 여부

// 스크롤바 옵션
const scrollOptions = {
    scrollbars: {
        autoHide: 'hover',
        x: 'hidden',
        y: 'visible'
    }
};

// 컬럼 설정
const gridColumns = ref([
    {
        column: 'industry',
        name: 'detailNm',
        align: 'center'
    }
]);

// 컴포넌트가 마운트될 때 데이터 가져오기
onMounted(() => {
    datasetStore.resetBusinessList();
    fetchData();
});

// 데이터 필터링 함수
// 데이터 필터링 함수
const applyFilter = () => {
    let filteredData = gridData.value;

    // 번호를 매기는 로직
    const numberedData = filteredData.map((item, index) => ({
        ...item,
        number: index + 1 // 필터링 후의 데이터에 대해 번호 매기기
    }));

    if (gridIndustry.value) {
        gridIndustry.value.resetData(numberedData); // 그리드에 업데이트
    }
};

// Pinia 스토어에서 데이터 가져오기 (무한 스크롤 적용)
const fetchData = async () => {
    if (!datasetStore.hasMoreBusiness || loading.value) return; // 더 이상 가져올 데이터가 없으면 중단
    loading.value = true;

    try {
        await datasetStore.getTypeofbusinessLists(searchTerm.value); // 검색어를 포함하여 Pinia 스토어에서 데이터 가져오기
        const newItems = datasetStore.businessList.map((item, index) => ({
            ...item,
            number: gridData.value.length + index + 1 // 연속 번호 매기기
        }));

        gridData.value = [...newItems]; // 기존 데이터와 합치기
        applyFilter(); // 필터 적용
        noMoreData.value = !datasetStore.hasMoreBusiness; // 더 이상 데이터가 없는지 확인
    } catch (error) {
        console.error('Failed to fetch data:', error);
    } finally {
        loading.value = false;
    }
};

// 스크롤 이벤트 처리
const onScroll = async instance => {
    const scrollElement = instance.elements().viewport; // 뷰포트 요소 가져오기
    const nearBottom = scrollElement.scrollTop + scrollElement.clientHeight >= scrollElement.scrollHeight - 10; // 여유 공간 조정

    if (nearBottom && !loading.value && datasetStore.hasMoreBusiness) {
        await fetchData(); // 추가 데이터 로드
    }
};

// 그리드 아이템 클릭 핸들러
const clickGridEquipment = ev => {
    const { instance, rowKey } = ev;
    const row = instance.getRow(rowKey);
    emit('close', row);
};
</script>
