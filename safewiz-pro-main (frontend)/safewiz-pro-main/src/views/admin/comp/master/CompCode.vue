<template>
    <!-- 콘텐츠 영역 -->
    <div class="content">
        <!-- 카드 타이틀 -->
        <h3>사업장 코드 목록</h3>
        <div class="control-field ui form mb2-4rem">
            <div class="row flex">
                <div class="col12-3 df sm-col12-12">
                    <select v-select class="w100p ul-minw0px ul-w100p" v-model="searchParam.delYn" @change="searchCompCodeGrid(searchParam)">
                        <option value="N">사용만</option>
                        <option value="">삭제포함</option>
                    </select>
                </div>
                <div class="col12-9 df pl6px sm-col12-12 sm-pl0 sm-mt6px">
                    <input v-input type="text" class="radius mr6px w711px ul-w100p" placeholder="사업장 코드명" v-model="searchParam.searchWords" @keyup.enter="searchCompCodeGrid(searchParam)" />
                    <button v-button class="btn w60px radius davy-grey" @click="searchCompCodeGrid(searchParam)">
                        <span>검색</span>
                    </button>
                </div>
            </div>
        </div>

        <!-- 커스텀 버튼 영역 -->
        <div class="ui form mb1-2rem tar">
            <button v-button class="btn w50px radius green" @click="addCompCode">
                <span>추가</span>
            </button>
        </div>

        <!-- 토스트 그리드 -->
        <i-DataGrid ref="gridCompCode" gridId="gridCompCode" :options="{ treeColumnOptions: { name: 'code' } }" :columns="gridColumns" @rowView="viewCompCode" @rowDelete="deleteCompCode" id="gridCompCode" />

        <!-- 팝업 -->
        <i-PopupDialog ref="dialogCompCode">
            <!-- 단일 그리드 -->
            <div class="contents form ui es">
                <CompCodeForm :inputForm="inputForm" :options="{ label: '사업장 코드 정보', readonly: false }" />

                <i-ButtonList :btnInfo="{ saveBtn: true, deleteBtn: true, closeBtn: true }" :localInputForm="inputForm" @save="saveCompCode" @delete="deleteCompCode" @close="closePopupCompCode" />
            </div>
        </i-PopupDialog>
    </div>
</template>

<script setup>
import BaseView from '@/components/base/BaseView';
const { ref, onMounted, toastPopup, confirmMsg } = BaseView();
import { useCompanyStore } from '@/stores/comp/master/company';
import { createCompCode, treeCompCode, getCompCode, modifyCompCode, removeCompCode } from '@/api/admin/comp/master/CompCode';
import CompCodeForm from '@/views/comp/master/CompCodeForm.vue';

const gridCompCode = ref(null);
const dialogCompCode = ref(null); // PopupDialog에 대한 ref

const defaultCompCodeForm = {
    cmd: 'I',
    codeSeq: '',
    code: '',
    codeNm: '',
    codeOdr: '',
    codeRmk: '',
    upCodeSeq: ''
};

const inputForm = ref({ ...defaultCompCodeForm });

const initCompCodeInputForm = () => {
    inputForm.value = { ...defaultCompCodeForm };
};

// 폼 초기화 후 팝업 열기 2024.07.01
const addCompCode = () => {
    initCompCodeInputForm();
    dialogCompCode.value.onOpen();
};

// 팝업 닫기
const closePopupCompCode = () => {
    dialogCompCode.value.onClose();
};

const viewCompCode = async function (rowKey) {
    const row = gridCompCode.value.tuiGrid.getRow(rowKey); // rowKey로 행 데이터를 가져옴
    if (!row) {
        console.error('Row not found:', rowKey);
        return;
    }
    const item = await getCompCode(row.codeSeq); // 실제 데이터 호출
    Object.assign(inputForm.value, item.result);
    dialogCompCode.value.onOpen();
};

const deleteCompCode = function (data, dv) {
    let param;
    if (dv === 'form') {
        param = data;
    } else {
        const row = gridCompCode.value.tuiGrid.getRow(data); // rowKey로 행 데이터를 가져옴
        if (!row) {
            console.error('Row not found:', data);
            return;
        }
        param = row;
    }

    confirmMsg('warning', '삭제 하시겠습니까?<br/>하위 목록도 함께 비활성화 됩니다.', '', { fun: deleteCompCodeAction, param: param.codeSeq });
};
const deleteCompCodeAction = async id => {
    let vo = await removeCompCode(id);
    if (dialogCompCode.value) {
        //모달 창이 열려 있으면
        closePopupCompCode();
        searchCompCodeGrid(searchParam); //그리드 새로 고침
    }
    toastPopup('삭제에 성공하였습니다.', vo.result.codeNm, 'success');
};

const saveCompCode = async dv => {
    let item = null;
    if (dv === 'form') {
        item = inputForm.value;
    }

    if (!item.code || !item.codeNm) {
        toastPopup('저장에 실패하였습니다.', '필수 값을 넣으세요.', 'info');
        return false;
    }
    saveCompCodeAction(dv, item);
};

const saveCompCodeAction = async (rowKey, item) => {
    let vo = null;
    item.compId = getCurrentCompId();
    if (item.cmd === 'I') {
        vo = await createCompCode(item);
    } else {
        vo = await modifyCompCode(item.codeSeq, item);
    }

    if (dialogCompCode.value) {
        //모달 창이 열려 있으면
        closePopupCompCode();
        searchCompCodeGrid(searchParam); //그리드 새로 고침
    }

    toastPopup('저장에 성공하였습니다.', vo.result.codeNm, 'success');
};

const searchParam = ref({
    searchText: '',
    sortKey: '',
    asc: true
});
const searchCompCodeGrid = async () => {
    searchParam.value.compId = getCurrentCompId();
    searchParam.value.curPage = 1;

    const res = await treeCompCode(searchParam.value);
    if (res && res.list && gridCompCode.value) {
        gridCompCode.value.resetData(res.list);
        gridCompCode.value.tuiGrid.expandAll();
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
        header: '사업장 코드',
        name: 'code',
        align: 'left'
    },
    {
        header: '사업장 코드명',
        name: 'codeNm',
        align: 'left'
    },
    {
        header: '비고',
        name: 'codeRmk',
        align: 'left',
        editor: 'text'
    },
    {
        header: '순서',
        name: 'codeOdr',
        align: 'center',
        width: 50
    },
    {
        header: '삭제',
        name: 'delYn',
        align: 'center',
        width: 50
    },
    {
        header: '기능',
        name: 'func',
        align: 'center',
        width: 150,
        renderer: {
            type: 'functionButton',
            button: ['view', 'delete']
        }
    }
]);

onMounted(async () => {
    try {
        searchCompCodeGrid(searchParam);
    } catch (error) {
        console.error('Error fetching data:', error);
    }
});

// 사업장 관련
const companyStore = useCompanyStore();
const getCurrentCompId = () => {
    return companyStore.handleCurrentCompId('get');
};
</script>
