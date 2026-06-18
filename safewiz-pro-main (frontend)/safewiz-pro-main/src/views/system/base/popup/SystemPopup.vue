<template>
    <h3>{{ options.label }}</h3>

    <div class="form ui">
        <div class="df">
            <input v-input type="text" class="radius w100p search" v-model="searchTerm" @input="applyFilter" @keyup.enter="applyFilter" />
            <button type="submit" @click="applyFilter" class="btn w7rem active radius shrink0">
                <span>등록</span>
            </button>
        </div>

        <!-- <div class="segment mb1rem"> -->
        <div class="df my2-2rem">
            <input type="text" placeholder="Search" class="br4px" />
            <button type="button" class="w4-4rem h4-4rem pa r3rem cp shrink0">
                <img src="/assets/img/common/icon_search.svg" alt />
            </button>
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
        <!-- 토스트 그리드 -->
        <i-DataGrid
            ref="gridSystemPopup"
            gridId="gridSystemPopup"
            :options="{
                rowHeaders: [{ type: 'rowNum', width: 60 }],
                columns: gridColumns,
                rowHeight: 44
            }"
            :columns="gridColumns"
            @rowClick="clickGridEquipment"
        />
    </OverlayScrollbarsComponent>

    <!-- <hr class="w100p h1px bce1e6ed mt0 mb0" />
        <OverlayScrollbarsComponent
            class="maxh22-4rem"
            :options="{
                scrollbars: {
                    autoHide: 'hover',
                    x: 'hidden',
                    y: 'visible'
                }
            }"
        >
            <ul class="form ui">
                <li class="pa1rem border">
                    <input type="checkbox" v-input="'설비유형1'" />
                </li>
                <li class="pa1rem border">
                    <input type="checkbox" v-input="'설비유형2'" />
                </li>
                <li class="pa1rem border">
                    <input type="checkbox" v-input="'설비유형3'" />
                </li>
                <li class="pa1rem border">
                    <input type="checkbox" v-input="'설비유형4'" />
                </li>
                <li class="pa1rem border">
                    <input type="checkbox" v-input="'설비유형5'" />
                </li>
                <li class="pa1rem border">
                    <input type="checkbox" v-input="'설비유형6'" />
                </li>
            </ul>
        </OverlayScrollbarsComponent>
    </div>-->
</template>

<script setup>
import { ref, onMounted, computed, defineModel, defineEmits } from 'vue';
import BaseView from '@/components/base/BaseView';
const { t, getCompId } = BaseView();
import FemsUtils from '@/components/base/FemsUtils';

import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';

import { getSystemCode } from '@/stores/system/setting/api/SystemCode.js';

const { deepMerge } = FemsUtils();

const emit = defineEmits(['close']);
const options = { label: '', readonly: false, majorCd: '' };

const props = defineProps({
    options: {
        type: Object,
        default: () => ({})
    }
});
const localInputForm = defineModel('inputForm');
const finalOptions = deepMerge(options, props.options);
const readonlyYn = finalOptions.readonly;

const gridSystemPopup = ref(null);
const gridData = ref([]);
const searchTerm = ref('');

//사업장id
const compId = getCompId();

onMounted(async () => {
    console.log('거래품', options.majorCd);
    let responses = await Promise.all([
        getSystemCode({
            majorCd: props.options.majorCd,
            compId: compId
        })
    ]);
    if (responses) {
        gridData.value = responses[0].list;
        gridSystemPopup.value.resetData(gridData.value);
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
    const filteredData = gridData.value.filter(item => item.minorNm.toLowerCase().includes(searchTerm.value.toLowerCase()));
    // 필터링된 데이터로 그리드를 업데이트
    if (gridSystemPopup.value) {
        gridSystemPopup.value.resetData(filteredData);
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
        column: options.label,
        name: 'minorNm',
        align: 'center'
    }
]);
</script>
