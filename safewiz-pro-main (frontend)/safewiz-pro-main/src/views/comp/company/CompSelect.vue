<template>
    <!-- 콘텐츠 영역 -->
    <div class="content">
        <!-- 카드 타이틀 -->
        <h3>사업장 목록</h3>
        <div class="control-field ui form mb2-4rem">
            <div class="col12-9 df pl6px lg-col12-8 sm-col12-12 sm-pl0 sm-mt4px">
                <input v-input id="searchText" type="text" class="radius mr6px w711px ul-w100p search" placeholder="사업장 ID, 사업장 명" v-model="searchParam.searchText" @keyup.enter="searchItems()" />
                <button v-button class="btn w60px radius davy-grey" @click="searchItems()">
                    <span>검색</span>
                </button>
            </div>
        </div>

        <!-- 토스트 그리드 -->
        <i-DataGrid ref="gridComp" gridId="gridComp" :options="{ treeColumnOptions: { name: 'compId' } }" :columns="gridColumns" @rowView="viewCompany" id="gridComp" />
    </div>
</template>

<script setup>
import BaseView from '@/components/base/BaseView';
const { ref, onMounted, router } = BaseView();
import { treeComp } from '@/api/comp/company/Company';

const gridComp = ref(null);

const viewCompany = async function (rowKey) {
    const row = gridComp.value.tuiGrid.getRow(rowKey); // rowKey로 행 데이터를 가져옴
    if (!row) {
        console.error('Row not found:', rowKey);
        return;
    }

    // 사업장 상세 화면으로 이동 예정
    // router.push({
    //     name: 'CompDetail',
    //     data: { compId: row.compId }
    // });
};

const listSize = ref(20);
const searchParam = ref({
    listSize: listSize.value,
    curPage: 1,
    compId: [],
    searchText: '',
    sortKey: '',
    asc: true
});
const searchCompGrid = async () => {
    searchParam.value.curPage = 1;
    searchParam.value.userId = 'as3003'; // 임시 하드코딩
    const res = await treeComp(searchParam.value);
    if (res && res.list && gridComp.value) {
        gridComp.value.resetData(res.list);
        gridComp.value.tuiGrid.expandAll();
    }
};

let oldSearchText = ref('');
let searchData = ref([]);
let oldCells = ref('');
const searchItems = () => {
    oldCellsInitialize();

    if (!searchParam.value.searchText) return true;

    const grid = gridComp.value.tuiGrid.getData();
    if (!searchData.value || searchData.value.length === 0 || oldSearchText.value !== searchParam.value.searchText) {
        oldSearchText.value = searchParam.value.searchText;
        searchData.value = grid.filter(row => {
            let searchTextRegex = new RegExp(searchParam.value.searchText, 'i');
            return searchTextRegex.test(row.compId) || searchTextRegex.test(row.compNm);
        });
    }

    let itemSearch = searchData.value.shift();
    if (itemSearch) {
        // data-grid-id 는 메뉴 그리드 id
        var cells = document.querySelectorAll(`[data-grid-id="1"] table tr td[data-row-key="${itemSearch.rowKey}"]`);
        oldCells.value = cells;

        // 선택된 각 요소에 스타일을 적용합니다
        cells.forEach(function (cell) {
            cell.style.backgroundColor = '#68d196a6';
        });
    } else {
        searchData.value = [];
    }
    const input = document.getElementById('searchText');
    input.focus();
};

//
const oldCellsInitialize = () => {
    if (oldCells.value) {
        oldCells.value.forEach(function (cell) {
            const rowKey = cell.getAttribute('data-row-key');
            const color = rowKey % 2 === 1 ? '#fafafb' : 'white';
            cell.style.backgroundColor = color;
        });
    }
};

const gridColumns = ref([
    {
        header: 'CMD',
        name: 'cmd',
        align: 'center',
        hidden: true
    },
    {
        header: '사업장 ID',
        name: 'compId',
        align: 'center'
    },
    {
        header: '사업장 명',
        name: 'compNm',
        align: 'center'
    },
    {
        header: '대표자',
        name: 'rpstNm',
        align: 'center',
        width: 150
    },
    {
        header: '사업자등록번호',
        name: 'rgstNo',
        align: 'center',
        width: 150
    },
    {
        header: '업종',
        name: 'bizNm',
        align: 'center',
        width: 150
    },
    {
        header: '담당자',
        name: 'chrgPrsn',
        align: 'center',
        width: 150
    },
    {
        header: '연락처',
        name: 'telNo',
        align: 'center'
    },
    {
        header: '우편번호',
        name: 'zipCd',
        align: 'center'
    },
    {
        header: '주소',
        name: 'addrs1',
        align: 'left'
    },
    {
        header: '기능',
        name: 'func',
        align: 'center',
        className: 'form ui', // 버튼의 스타일링을 위해 커스텀 클래스 정의
        width: 120,
        renderer: {
            type: 'functionButton',
            button: ['view']
        }
    }
]);

onMounted(async () => {
    try {
        searchCompGrid(searchParam);
        console.log('부모 검색후');
    } catch (error) {
        console.error('Error fetching data:', error);
    }
});
</script>
