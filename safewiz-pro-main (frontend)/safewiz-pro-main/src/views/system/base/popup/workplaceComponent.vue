<template>
    <!-- 콘텐츠 영역 -->
    <h3>{{ options.label }}</h3>

    <div class="form ui field df aic mb2-2rem">
        <input v-input type="text" class="radius w100p search" placeholder="검색어를 입력하세요" v-model="searchTerm" @input="applyFilter" @keyup.enter="applyFilter" />
        <button type="submit">
            <img src="/assets/img/common/icon_search.svg" alt />
        </button>
    </div>

    <div class="segment mt1-2rem">
        <div class="row flex">
            <div class="grid12-12">
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
                    <ul class="ma1-6rem tal form ui">
                        <li class="df aic mb1rem cp" v-for="(item, index) in dataFilter" :key="index">
                            <span class="df aic">
                                <input type="checkbox" v-input class="mr1rem" v-model="item.selected" />
                                <i class="w22px h22px lh22px mr5px br4px bc000000 tac fs1-5rem cffffff bsc oh bd1pxsolidE1E6ED">
                                    <img src="/public/assets/img/common/icon_profile_thumbs.jpg" alt class="w100p h100p ofc" />
                                </i>
                                <em class="fs1-5rem fw500">{{ item.workplaceNm }}</em>
                            </span>
                        </li>
                    </ul>
                </OverlayScrollbarsComponent>
            </div>
        </div>
    </div>
  <!-- 버튼 콤포넌트 영역 시작 -->
  <div class="form ui tar mt2-5rem">
    <button type="button" class="btn w80px radius active" @click="close">
      <span>저장</span>
    </button>
    <button type="button" v-button class="btn w80px radius ml4px bright-grey" @click="close">
      <span>{{ t('close') }}</span>
    </button>
  </div>
</template>

<script setup>
import { ref, onMounted, computed, defineModel, defineEmits } from 'vue';
import BaseView from '@/components/base/BaseView';
const { t, getCompId } = BaseView();
import FemsUtils from '@/components/base/FemsUtils';

import { getWp } from '@/stores/system/base/api/workplaceApi';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';

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
    },
    // 선택된 id 리스트
    selectedIdList: {
        type: Array,
        default: []
    }
});
const localInputForm = defineModel('inputForm');
const finalOptions = deepMerge(options, props.options);
const readonlyYn = finalOptions.readonly;
const dataFilter = ref(null);
const dataList = ref([]);
const searchTerm = ref('');

onMounted(async () => {
    let responses = await Promise.all([getWp({ compId: getCompId })]);
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
    const filteredData = dataList.value.filter(item => item.workplaceNm.toLowerCase().includes(searchTerm.value.toLowerCase()));
    // 필터링된 데이터로 그리드를 업데이트
    if (dataFilter.value) {
        // dataFilter.value.resetData(filteredData);
        dataFilter.value = filteredData;
    }
};

// 초기 데이터 설정 (예: API 호출 후)
const initData = () => {
    if (props.selectedIdList.length > 0) {
        const result = dataFilter.value.map(workplace => ({
            ...workplace,
            selected: props.selectedIdList.includes(workplace.workplaceId)
        }));
        dataFilter.value.splice(0, dataFilter.value.length, ...result);
    }
    // 여기서 API 호출을 통해 데이터를 가져올 수 있습니다.
    // 예제에서는 초기 데이터로 설정합니다.
    applyFilter(); // 필터를 처음에 적용하여 초기 데이터로 그리드를 채움
};

//그리드 컬럼
const gridColumns = ref([
    // 버튼 컬럼 추가
    {
        column: 'workplaceNm',
        name: 'workplaceNm',
        align: 'center'
    }
]);
</script>

<style lang="scss" scoped>
.segment {
    border: 1px solid #e1e6ed;
    border-radius: 4px;

    .grid12-4 {
        position: relative;

        &::after {
            display: inline-block;
            width: 1px;
            height: 100%;
            right: 0;
            top: 0;
            background: #e1e6ed;
        }
        ul {
            border-right: 1px solid #e1e6ed;
            padding-bottom: 5rem;

            li {
                padding: 8px;
                border-bottom: 1px solid #e1e6ed;

                button {
                    display: block;
                    padding: 0 1.3rem;
                    width: 100%;
                    height: 3.4rem;
                    color: #9ea1a6;
                    font-size: 1.5rem;
                    border-radius: 4px;
                    text-align: left;
                }

                &.selected {
                    button {
                        background: #f8f9fb;
                        color: #3248f6;
                    }
                }
            }
        }
    }

    .grid12-8 {
        ul {
            li {
                span {
                    position: relative;
                    width: 100%;
                    padding: 8px 12px 8px 12px;
                    border: 1px solid #e1e6ed;
                    border-radius: 4px;
                    font-size: 0;

                    // &::before {
                    //     display: inline-block;
                    //     width: 22px;
                    //     height: 22px;
                    //     border-radius: 4px;
                    //     left: 12px;
                    //     top: 50%;
                    //     margin-top: -11px;
                    //     border: 1px solid #e1e6ed;
                    //     background: #fff url('data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSIxMiIgaGVpZ2h0PSI4IiB2aWV3Qm94PSIwIDAgMTIgOCIgZmlsbD0ibm9uZSI+CjxwYXRoIGQ9Ik0xMC42NjY2IDEuMDgzNUw0LjY2MjQgNy4wODc2OEM0LjQzNDU5IDcuMzE1NDkgNC4wNjUyNSA3LjMxNTQ5IDMuODM3NDQgNy4wODc2OEwxLjMzMzI1IDQuNTgzNSIgc3Ryb2tlPSIjRTFFNkVEIiBzdHJva2Utd2lkdGg9IjEuNSIgc3Ryb2tlLWxpbmVjYXA9InJvdW5kIi8+Cjwvc3ZnPg==') no-repeat center;
                    // }
                }

                &.selected {
                    span {
                        border-color: #3248f6;
                        background: #ebedff;

                        // &::before {
                        //     border-color: #3248f6;
                        //     background: #3248f6 url('data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSIxNCIgaGVpZ2h0PSIxNCIgdmlld0JveD0iMCAwIDE0IDE0IiBmaWxsPSJub25lIj4KPHBhdGggZD0iTTExLjY2NjYgNC4wODM1TDUuNjYyNCAxMC4wODc3QzUuNDM0NTkgMTAuMzE1NSA1LjA2NTI1IDEwLjMxNTUgNC44Mzc0NCAxMC4wODc3TDIuMzMzMjUgNy41ODM1IiBzdHJva2U9IndoaXRlIiBzdHJva2Utd2lkdGg9IjEuNSIgc3Ryb2tlLWxpbmVjYXA9InJvdW5kIi8+Cjwvc3ZnPg==') no-repeat center;
                        // }
                    }
                }
            }
        }
    }
}
</style>
