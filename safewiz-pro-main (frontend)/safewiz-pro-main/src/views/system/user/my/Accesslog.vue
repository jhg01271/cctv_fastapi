<template>
    <!-- 콘텐츠 영역 -->
    <div class="content">
        <!-- 카드 타이틀 -->
        <h3>사용자 토큰 관리</h3>
        <div class="control-field ui form mb2-4rem">
            <div class="row flex">
                <div class="col12-3 df sm-col12-12">
                    <select v-select class="w100p ul-minw0px ul-w100p" v-model="searchParam.delYn" @change="searchAccesslogGrid(searchParam)">
                        <option value="N">사용만</option>
                        <option value="">삭제포함</option>
                    </select>
                </div>
                <div class="col12-9 df pl6px sm-col12-12 sm-pl0 sm-mt6px">
                    <button v-button class="btn w60px radius davy-grey" @click="searchAccesslogGrid()">
                        <span>검색</span>
                    </button>
                </div>
            </div>
        </div>
        <!-- 토스트 그리드 -->
        <i-DataGrid ref="gridAccesslog" gridId="gridAccesslog" :columns="gridColumns" @rowDelete="deleteAccesslog" id="gridAccesslog" @rowCancel="cancelAccesslog" />

        <!-- 커스텀 버튼 영역 -->
        <div class="ui form mt1-2rem tac">
            <button v-button class="btn w50px radius green" @click="seeMore()">
                <span>더보기</span>
            </button>
        </div>
    </div>
</template>

<script setup>
import BaseView from '@/components/base/BaseView.js';
const { ref, onMounted, toastPopup, confirmMsg } = BaseView();
import { searchAccesslog, removeAccesslog } from '@/stores/system/user/my/Accesslog.js';

const gridAccesslog = ref(null);

const deleteAccesslog = function (data) {
    const row = gridAccesslog.value.tuiGrid.getRow(data); // rowKey로 행 데이터를 가져옴
    if (!row) {
        console.error('Row not found:', data);
        return;
    }
    const param = row;
    confirmMsg('warning', '삭제 하시겠습니까?', '', { fun: deleteAccesslogAction, param: param.refreshToken });
};

const deleteAccesslogAction = async id => {
    let vo = await removeAccesslog(id);

    toastPopup('삭제에 성공하였습니다.', vo.result.refreshToken, 'success');
};

const cancelAccesslog = async rowKey => {
    gridAccesslog.value.tuiGrid.setRow(rowKey, gridAccesslog.value.originData[rowKey]);
    gridAccesslog.value.updateIcons(gridAccesslog.value.tuiGrid, rowKey, ['view', 'delete']);
};

const seeMore = async () => {
    searchParam.value.curPage++;
    const res = await searchAccesslog(searchParam.value);
    if (res && res.list && gridAccesslog.value) {
        gridAccesslog.value.tuiGrid.appendRows(res.list);
    }
};

const listSize = ref(20);
const searchParam = ref({
    listSize: listSize.value,
    curPage: 1,
    searchCd: '',
    searchText: '',
    sortBy: 'expirationPeriodDesc',
    delYn: '',
    asc: true
});

const searchAccesslogGrid = async () => {
    searchParam.value.curPage = 1;
    const res = await searchAccesslog(searchParam.value);
    if (res && res.list && gridAccesslog.value) {
        gridAccesslog.value.resetData(res.list);
    }
};
const gridColumns = ref([
    {
        header: '토큰',
        name: 'refreshToken',
        align: 'left'
    },
    {
        header: '유효기간',
        name: 'expirationPeriod',
        width: 200,
        align: 'center'
    },
    {
        header: '삭제',
        name: 'delYn',
        width: 50,
        align: 'center'
    },
    {
        header: '기능',
        name: 'func',
        align: 'center',
        width: 150,
        renderer: {
            type: 'functionButton',
            button: ['delete']
        }
    }
]);

onMounted(async () => {
    try {
        searchAccesslogGrid(searchParam);
    } catch (error) {
        console.error('Error fetching data:', error);
    }
});
</script>
