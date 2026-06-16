<template>
    <div>
        <h3>{{ options.label }}</h3>
        <div class="col12-9 df pl6px lg-col12-8 sm-col12-12 sm-pl0 sm-mt4px">
            <input
                v-input
                type="text"
                class="radius mr6px w711px ul-w100p search"
                placeholder="검색어를 입력하세요"
                v-model="searchTerm"
                @input="applyFilter"
                @keyup.enter="applyFilter"
            />
            <button type="submit">
                <img src="/assets/img/common/icon_search.svg" alt />
            </button>
        </div>

        <i-DataGrid
            ref="gridIndustry"
            gridId="gridIndustry"
            style="height: 500px; overflow-y: auto"
            :options="{ rowHeaders: ['rowNum'] }"
            :columns="gridColumns"
            @rowClick="clickGridEquipment"
            id="gridIndustry"
        />
    </div>
</template>

<script setup>
import { ref, onMounted, computed, defineModel, defineEmits } from 'vue';
import BaseView from '@/components/base/BaseView';
const { t } = BaseView();
import FemsUtils from '@/components/base/FemsUtils';

import { useDatasetStore } from '@/stores/safewiz/dataset/dataset.js';
const datasetStore = useDatasetStore();

const { deepMerge } = FemsUtils();

const emit = defineEmits(['close']);
const options = { label: '', readonly: false };

const props = defineProps({
    options: {
        type: Object,
        default: () => ({})
    }
});
const localInputForm = defineModel('inputForm');
const finalOptions = deepMerge(options, props.options);
const readonlyYn = finalOptions.readonly;
const gridIndustry = ref(null);
const gridData = ref([]);
const searchTerm = ref('');

onMounted(async () => {
    let responses = await Promise.all([datasetStore.getTypeofbusinessLists()]);
    if (responses) {
        gridData.value = responses[0].list;
        gridIndustry.value.resetData(gridData.value);
        initData();
    }
});

//-----------------------------------------------
//클릭 이벤트

const clickGridEquipment = ev => {
    const { instance, rowKey } = ev;
    const row = instance.getRow(rowKey);
    //팝업종료
    emit('close', row);
};

// 필터 적용 함수
const applyFilter = () => {
    const filteredData = gridData.value.filter(item => item.detailNm.toLowerCase().includes(searchTerm.value.toLowerCase()));
    // 필터링된 데이터로 그리드를 업데이트
    if (gridIndustry.value) {
        gridIndustry.value.resetData(filteredData);
    }
};

// 초기 데이터 설정 (예: API 호출 후)
const initData = () => {
    // 여기서 API 호출을 통해 데이터를 가져올 수 있습니다.
    // 예제에서는 초기 데이터로 설정합니다.
    applyFilter(); // 필터를 처음에 적용하여 초기 데이터로 그리드를 채움
};

//그리드 컬럼
const gridColumns = ref([
    // 버튼 컬럼 추가
    {
        column: 'industry',
        name: 'detailNm',
        align: 'center'
    }
]);
</script>
