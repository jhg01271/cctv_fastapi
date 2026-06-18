<template>
    <!-- 콘텐츠 영역 -->
    <div class="content">
        <!-- 카드 타이틀 -->
        <h3>감사 로그 관리</h3>
        <div class="control-field ui form mb2-4rem">
            <div class="row flex">
                <div class="col12-12 df aic lg-fww">
                    <input type="checkbox" v-input="'일시'" class="shrink0 w15rem lg-w100p lg-mb5px" :true-value="'Y'" :false-value="'N'" v-model="isSearchDt" />
                    <div class="df aic lg-w100p">
                        <input v-input type="test" v-calendar="'yyyy-MM-dd'" class="datapicker mr5px radius w20rem lg-w50p" v-model="searchParam.searchFrom" :readonly="isSearchDt === 'N'" />
                        <input v-input type="test" v-calendar="'yyyy-MM-dd'" class="datapicker radius w20rem lg-w50p" v-model="searchParam.searchTo" :readonly="isSearchDt === 'N'" />
                    </div>
                </div>
                <div class="col12-12 df aic mt1rem lg-fww lg-mt2rem">
                    <input v-input clearable type="text" class="radius mr6px w711px ul-w100p search" placeholder="내용을 입력해주세요." v-model="searchParam.searchWords" @keyup.enter="searchAuditGrid()" />
                    <button v-button class="btn w60px radius davy-grey" @click="searchAuditGrid()">
                        <span>검색</span>
                    </button>
                </div>
            </div>
        </div>
        <!-- 토스트 그리드 -->
        <i-DataGrid ref="gridAudit" gridId="gridAudit" :columns="gridColumns" />

        <!-- 커스텀 버튼 영역 -->
        <div class="ui form mt1-2rem tac">
            <button v-button class="btn w50px radius green" @click="seeMore()">
                <span>더보기</span>
            </button>
        </div>
    </div>
</template>

<script setup>
import BaseView from '@/components/base/BaseView';
const { ref, onMounted, watch } = BaseView();
import { searchAudit } from '@/stores/system/setting/api/Audit.js';

const gridAudit = ref(null);

const isSearchDt = ref('N');
watch(isSearchDt, () => {
    if (isSearchDt.value === 'N') {
        searchParam.value.searchFrom = '';
        searchParam.value.searchTo = '';
    }
});

const seeMore = async () => {
    searchParam.value.curPage++;
    const res = await searchAudit(searchParam.value);
    if (res && res.list && gridAudit.value) {
        gridAudit.value.tuiGrid.appendRows(res.list);
    }
};

const listSize = ref(20);
const searchParam = ref({
    listSize: listSize.value,
    curPage: 1,
    searchWords: '',
    searchFrom: '',
    searchTo: ''
});

const searchAuditGrid = async () => {
    searchParam.value.curPage = 1;
    const res = await searchAudit(searchParam.value);
    if (res && res.list && gridAudit.value) {
        gridAudit.value.resetData(res.list);
    }
};
const gridColumns = ref([
    {
        column: '일시',
        name: 'createdAt',
        width: 200,
        align: 'left'
    },
    {
        column: '브라우저',
        name: 'browser',
        width: 100,
        align: 'center'
    },
    {
        column: 'IP',
        name: 'ipAddress',
        width: 100,
        align: 'center'
    },
    {
        column: '사용자',
        name: 'userId',
        width: 250,
        align: 'center'
    },
    {
        column: '접근 URL',
        name: 'referer',
        align: 'left'
    },
    {
        column: 'API URL',
        name: 'url',
        align: 'left'
    }
]);

onMounted(async () => {
    try {
        searchAuditGrid(searchParam);
    } catch (error) {
        console.error('Error fetching data:', error);
    }
});
</script>
