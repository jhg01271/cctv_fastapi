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

        <div v-if="props.single">
            <ul>
                <li v-for="(item, index) in dataFilter" :key="index">
                    <span @click="clickItem(item)">{{ item.partCompNm }}</span>
                    <!-- 필요한 추가 필드도 여기에 추가할 수 있습니다 -->
                </li>
            </ul>
        </div>
        <div v-else>
            <ul>
                <li v-for="(item, index) in dataFilter" :key="index">
                    <span>{{ item.partCompNm }}</span>
                    <input type="checkbox" v-input class="shrink0" v-model="item.selected" />
                </li>
            </ul>
        </div>

        <button type="button" class="btn w50px ml4px radius dark shrink0" v-button @click="close">
            <span>닫기</span>
        </button>
    </div>
</template>

<script setup>
import { ref, onMounted, computed, defineModel, defineEmits } from 'vue';
import BaseView from '@/components/base/BaseView';
const { t } = BaseView();
import FemsUtils from '@/components/base/FemsUtils';

import { getPartner } from '@/stores/system/base/api/partnerApi';

const { deepMerge } = FemsUtils();

const emit = defineEmits(['close', 'selected']);

const options = { label: '', readonly: false };

const props = defineProps({
    options: {
        type: Object,
        default: () => ({})
    },
    //true : 싱글모드, false : 멀티
    single: {
        type: Boolean,
        default: true
    }
});
const localInputForm = defineModel('inputForm');
const finalOptions = deepMerge(options, props.options);
const readonlyYn = finalOptions.readonly;
const dataFilter = ref(null);
const dataList = ref([]);
const searchTerm = ref('');

onMounted(async () => {
    let responses = await Promise.all([getPartner()]);
    if (responses) {
        dataList.value = responses[0].list;
        // dataFilter.value.resetData(dataList.value);
        dataFilter.value = dataList.value;
        initData();
    }
});

//-----------------------------------------------
//클릭 이벤트

const clickItem = item => {
    //그리드 select
    // const { instance, rowKey } = ev;
    // const row = instance.getRow(rowKey);
    //팝업종료
    emit('close', item);
};

const close = () => {
    // check true 인것만 보내줌
    const selectedItems = dataFilter.value.filter(item => item.selected === true);
    emit('close', selectedItems);
};

// 필터 적용 함수
const applyFilter = () => {
    const filteredData = dataList.value.filter(item => item.partCompNm.toLowerCase().includes(searchTerm.value.toLowerCase()));
    // 필터링된 데이터로 그리드를 업데이트
    if (dataFilter.value) {
        // dataFilter.value.resetData(filteredData);
        dataFilter.value = filteredData;
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
        column: 'partCompNm',
        name: 'partCompNm',
        align: 'center'
    }
]);
</script>
