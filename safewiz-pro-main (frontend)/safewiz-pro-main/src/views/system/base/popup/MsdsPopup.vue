<template>
    <div>
        <h3>{{ props.options.label }}</h3>
        <div class="form ui field mb2-2rem">
            <div class="row flex gutters1rem">
                <div class="grid12-5 us-grid12-12">
                    <select class="br4px" v-select v-model="searchCd">
                        <option value="0">화학물질명</option>
                        <option value="1">CAS NO.</option>
                    </select>
                </div>
                <div class="grid12-7 df aic us-grid12-12">
                    <input v-input type="text" class="radius w70p search" required placeholder="검색어를 입력해주세요." v-model="searchWrd" @keyup.enter="btnSearch" />
                    <button type="button" @click="btnSearch">
                        <img src="/assets/img/common/icon_search.svg" alt />
                    </button>
                </div>
            </div>
        </div>
        <OverlayScrollbarsComponent
            class="maxh398px"
            :options="{
                scrollbars: {
                    autoHide: 'hover',
                    x: 'hidden',
                    y: 'visible'
                }
            }"
        >
            <!-- TODO -->
            <!-- 퍼블리싱 작업이 필요함 -->
            <table>
                <colgroup>
                    <col style="width: 100px" />
                    <col style="width: 100px" />
                    <col style="width: 100px" />
                </colgroup>
                <thead>
                    <tr>
                        <th>화학물질명</th>
                        <th>CAS No.</th>
                        <th>선택</th>
                    </tr>
                </thead>
                <!-- 데이터가 없을 경우 컬럼을 병합하여 하나의 컬럼안에 안내문구 출력되도록 함-->
                <tbody v-if="!filteredData.item">
                    <tr>
                        <td colspan="4">데이터가 존재하지 않습니다.</td>
                    </tr>
                </tbody>
                <tbody>
                    <tr v-for="data in filteredData.item" :key="data.chemId">
                        <td>
                            {{ data.chemNameKor }}
                        </td>
                        <td>
                            {{ data.casNo }}
                        </td>
                        <td>
                            <button type="button" v-button class="btn w80px radius bright-grey" @click="btnSelect(data.chemNameKor, data.casNo)">
                                <span>{{ t('select') }}</span>
                            </button>
                        </td>
                    </tr>
                </tbody>
            </table>
        </OverlayScrollbarsComponent>
        <div class="ui-pagination"></div>
        <div class="form ui tar mt1rem">
            <button type="button" v-button class="btn w80px radius bright-grey" @click="btnClose">
                <span>{{ t('close') }}</span>
            </button>
        </div>
    </div>
</template>

<script setup>
import { defineEmits, ref } from 'vue';
import BaseView from '@/components/base/BaseView';
import { getChemList } from '@/stores/safewiz/planning/api/msdsApi';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';

const { t, toastPopup } = BaseView();
const filteredData = ref({});
const emit = defineEmits(['close', 'selected']);

const props = defineProps({
    searchCndCd: {
        // 0 : '물질명(관용명/동의어)', 1 : CAS NO
        type: Number,
        default: () => 0
    },
    options: {
        type: Object,
        default: () => ({}),
        label: ''
    }
});

const searchWrd = ref('');
const searchCd = ref(0);

// 조회
const btnSearch = async () => {
    let searchParams = {
        // 검색 키워드
        searchText: searchWrd.value ? searchWrd.value : '',
        // 검색 구분 0: 화학물질명, 1: CAS No.
        searchCd: searchCd.value ? searchCd.value : 0,
        // RowNum
        searchText2: '9999',
        // pageNum
        searchText3: 1
    };

    getChemList(searchParams, false).then(res => {
        toastPopup(t('msgSuccess'), t('msgSearched'));
        // 호출한 값 JSON 형태로 변경
        const chemData = JSON.parse(res);

        // 필요한 데이터 가져오기
        if (chemData.response.body.items == '') {
            filteredData.value = [];
        } else {
            if (Array.isArray(chemData.response.body.items.item)) {
                filteredData.value = chemData.response.body.items;
            } else {
                filteredData.value = {
                    item: [chemData.response.body.items.item]
                };
            }
        }
    });
};

const btnSelect = (chemNameKor, casNo) => {
    emit('selected', chemNameKor, casNo);
};

const btnClose = () => {
    emit('close');
};
</script>
