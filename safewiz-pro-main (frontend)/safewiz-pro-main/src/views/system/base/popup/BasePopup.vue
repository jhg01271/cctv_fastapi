<template>
    <h3>{{ options.label }}</h3>
    <!-- 
    <div class="segment mb1rem df aic jcsb">
        <button type="button" class="w35p h4rem fs1-5rem c3248F6">
            <span>조회</span>
        </button>
        <hr class="w1px h4rem bcE1E6ED ma0" />
        <button type="button" class="w35p h4rem fs1-5rem c3248F6">
            <span>저장</span>
        </button>
        <hr class="w1px h4rem bcE1E6ED ma0" />
        <button type="button" class="w35p h4rem fs1-5rem c3248F6">
            <span>삭제</span>
        </button>
    </div>

    <div class="form ui field df aic mb2-2rem">
        <input
            v-input
            type="text"
            class="radius w100p search"
            v-model="searchTerm"
            @input="applyFilter"
            @keyup.enter="applyFilter"
        />
        <button type="submit" @click="applyFilter" class="btn w7rem active radius shrink0">
            <span>등록</span>
        </button>
    </div>-->

    <!-- <div class="segment mb1rem"> -->
    <div class="form ui field df aic mb2-2rem">
        <input type="text" placeholder="Search" class="w100p h4-4rem pl1rem pr1rme" />
        <button type="button" class="w4-4rem shrink0">
            <img src="/assets/img/common/icon_search.svg" alt />
        </button>
    </div>
    <div class="segment mt1-2rem">
        <div class="row flex minh40rem">
            <div class="grid12-12">
                <OverlayScrollbarsComponent class="maxh40rem">
                    <ul v-if="gridData" class="ma1-6rem tal form ui">
                        <li
                            class="df aic mb1rem cp"
                            v-for="(item, index) in gridData"
                            :key="index"
                            @click="selectItem(item)"
                        >
                            <span class="df aic">
                                <input
                                    type="checkbox"
                                    v-input
                                    class="mr1rem"
                                    v-model="item.selected"
                                />

                                <em class="fs1-5rem fw500">{{ item[props.fieldNm] }}</em>
                            </span>
                        </li>
                    </ul>
                </OverlayScrollbarsComponent>
            </div>
        </div>
    </div>
    <i-PopupButtonList
        :btnInfo="{ closeBtn: btnInfo.close, saveBtn: btnInfo.save, deleteBtn: btnInfo.delete }"
        :localInputForm="localInputForm"
        @close="close"
    />
</template>

<script setup>
import { ref, onMounted, computed, defineModel, defineEmits } from 'vue';
import BaseView from '@/components/base/BaseView';
const { t, getCompId } = BaseView();
import FemsUtils from '@/components/base/FemsUtils';

import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';

import { getSystemCode } from '@/stores/system/setting/api/SystemCode.js';

const { deepMerge } = FemsUtils();
import { getObjective, getActionObjective } from '@/stores/safewiz/participation/api/actionPlanApi.js';
import { getDetailItem } from '@/stores/safewiz/participation/api/detailedActionApi.js';

const emit = defineEmits(['close']);
const options = { label: '', readonly: false };

const props = defineProps({
    options: {
        type: Object,
        default: () => ({})
    },
    dataList: {
        type: Array,
        default: []
    },
    btnInfo: {
        // Todo: 버튼 설정 (저장, 닫기) 현재 미구현
        type: Object,
        default: () => ({ close: true, save: false, delete: false }) // 기본값 설정
    },
    fieldNm: {
        type: String,
        default: () => ''
    }
});
const localInputForm = defineModel('inputForm');
const finalOptions = deepMerge(options, props.options);
const readonlyYn = finalOptions.readonly;

const gridData = ref([]);
const searchTerm = ref('');

//사업장id
const compId = getCompId();

onMounted(() => {
    console.log('리스트', props.dataList);
    if (props.dataList.length > 0) {
        gridData.value = deepMerge(props.dataList);
    }
    initData();
});

//----------------------------------------------
//클릭 이벤트

const clickGridEquipment = ev => {
    const { instance, rowKey } = ev;
    const row = instance.getRow(rowKey);
    //팝업종료
    emit('close', row);
};

const gridSystemPopup = ref(null);
// 필터 적용 함수
const applyFilter = () => {
    // dataList가 null이면 무조건 종료
    if (!props.dataList) return;

    const searchTermLower = searchTerm.value.toLowerCase(); // 검색어를 소문자로 변환
    const filterKey = props.filterKey; // props에서 필터링 기준을 가져옴
    const useYnKey = props.useYnKey; // useYn을 판단할 키를 가져옴
    const excludedValue = props.excludedValue; // 필터링할 제외 값 (예: 'N')

    // props.filterKey 또는 props.useYnKey, props.excludedValue 중 하나라도 존재하면 필터링 진행
    if (!filterKey && !useYnKey && !excludedValue) return;

    // 필터링된 데이터로 업데이트
    dataFilter.value = dataList.value.filter(item => {
        const matchesFilterKey = filterKey ? item[filterKey]?.toLowerCase().includes(searchTermLower) : true; // 동적으로 필터링
        const matchesUseYn = useYnKey ? item[useYnKey] !== excludedValue : true; // useYn 필터링 여부

        return matchesFilterKey && matchesUseYn; // 두 조건 모두 만족해야 함
    });
};

const initData = () => {
    if (props.dataList.length > 0) {
        gridData.value = props.dataList; // 기본 데이터 설정
        applyFilter(); // 초기 필터링 적용
    }
};

// 아이템 선택을 처리하는 함수
const selectItem = item => {
    gridData.value.forEach(el => (el.selected = false));
    item.selected = true;
    emitCloseWithSelectedItems(); // 선택 후 창 닫기
};

// 'close' 이벤트를 발생시키고 선택된 아이템 목록을 전달하는 함수
const emitCloseWithSelectedItems = () => {
    emit('close', getSelectedItems());
};

// 선택된 아이템을 필터링하는 헬퍼 함수
const getSelectedItems = () => gridData.value.filter(item => item.selected);

// 'close' 이벤트를 발생시키는 함수
const close = emitCloseWithSelectedItems;

//그리드 컬럼
const gridColumns = ref([
    // 버튼 컬럼 추가
    {
        column: options.label,
        name: '목표',
        align: 'center'
    }
]);
</script>
<style lang="scss" scoped>
.segment {
    border: 1px solid #e1e6ed;
    border-radius: 4px;
    overflow: hidden;
}
.segment ul li {
    border-top: 1px solid #e1e6ed;

    &:first-child {
        border-top: 0;
    }
}
</style>
