<template>
    <!-- 콘텐츠 영역 -->
    <div class="content">
        <!-- 카드 타이틀 -->
        <div class="row flex gutters12px">
            <div class="col12-6 md-col12-12">
                <h3>사용자 목록</h3>
                <div class="control-field ui form mb2-4rem">
                    <div class="col12-6 lg-col12-12 df sm-mt6px">
                        <input v-model="searchParam.searchWords" v-input="{ type: ['reset'] }" type="text" class="radius mr6px w711px ul-w100p search" placeholder="사용자 ID, 사용자명" @keyup.enter="searchUserGrid()" />
                        <button v-button class="btn w60px radius davy-grey" @click="searchUserGrid(1)">
                            <span>검색</span>
                        </button>
                    </div>
                </div>

                <!-- 커스텀 버튼 영역 -->
                <div class="ui form mb1-2rem tar">
                    <button v-button class="btn w50px radius green" @click="addUser">
                        <span>추가</span>
                    </button>
                </div>
                <!-- 테이블 시작 -->
                <i-DataGrid ref="gridUser" gridId="gridUser" :columns="gridUserColumns" @focusChange="focusGridUser" @rowView="viewUser" @rowDelete="deleteUser" />
                <i-DataGridPage ref="gridPageUser" :pageOptions="pageOptions" @beforeMove="beforeMovePageUser" />
                <!-- 테이블 끝
        <div class="row flex g -->
            </div>
            <div class="col12-6 md-col12-12 md-mt12px">
                <h3>사업장 목록</h3>
                <div class="control-field ui form mb2-4rem">
                    <div class="col12-6 lg-col12-12 df sm-mt6px">
                        <input id="searchText" v-model="searchParam.searchText" v-input="{ type: ['reset'] }" type="text" class="radius mr6px w711px ul-w100p search" placeholder="사업장 명" @keyup.enter="searchItems()" />
                        <button v-button class="btn w60px radius davy-grey" @click="searchItems()">
                            <span>검색</span>
                        </button>
                    </div>
                </div>

                <!-- 커스텀 버튼 영역 -->
                <div class="ui form mb1-2rem tar">
                    <button v-button class="btn w50px radius green" @click="saveCompanyByUser">
                        <span>저장</span>
                    </button>
                </div>
                <!-- 테이블 시작 -->
                <i-DataGrid ref="gridCompanyUser" gridId="gridCompanyUser" :options="{ rowHeaders: ['checkbox'], treeColumnOptions: { name: 'compId', useCascadingCheckbox: false } }" :columns="gridCompanyUserColumns" @rowView="viewCompanyUser" />
                <!-- 테이블 끝 -->
            </div>
        </div>

        <!-- 팝업 -->
        <i-PopupDialog ref="dialogUser">
            <!-- 단일 그리드 -->
            <div class="contents form ui w500px">
                <UserForm :inputForm="inputForm" :options="{ label: '유저 정보', readonly: false }" />

                <i-ButtonList :btnInfo="{ saveBtn: true, deleteBtn: true, closeBtn: true }" :localInputForm="inputForm" @save="saveUser" @delete="deleteUser" @close="closePopupUser" />
            </div>
        </i-PopupDialog>

        <!-- 팝업 -->
        <i-PopupDialog ref="dialogCompanyUser">
            <!-- 단일 그리드 -->
            <div class="contents form ui w500px">
                <CompanyUserForm :inputForm="companyUserInputForm" :options="{ label: '사업장별 권한 정보', readonly: false }" />

                <i-ButtonList :btnInfo="{ saveBtn: true, closeBtn: true }" :options="{ saveBtn: { label: '적용' } }" :localInputForm="companyUserInputForm" @save="saveCompanyByUser" @close="closePopupCompanyUser" />
            </div>
        </i-PopupDialog>
    </div>
</template>

<script setup>
import BaseView from '@/components/base/BaseView';
import UserForm from '@/views/system/user/XUserForm.vue';
import CompanyUserForm from '@/views/system/user/XCompanyUserForm.vue';
import { createUser, searchUser, getUser, modifyUser, removeUser } from '@/stores/system/user/User.js';
import { removeCompanyUser, saveCompanyUser, treeCompanyUser, searchByCompIdAndUserId } from '@/api/admin/comp/company/CompanyUser';
const { ref, toastPopup, confirmMsg, onMounted, searchPage } = BaseView();

const dialogUser = ref(null); // PopupDialog에 대한 ref
const dialogCompanyUser = ref(null); // 사업장 권한 PopupDialog에 대한 ref

const companyUserInputForm = ref({
    cmd: 'I',
    compId: '',
    compNm: '',
    rgstNo: '',
    grpSeq: '',
    grpNm: ''
});

const defaultInputForm = {
    cmd: 'I',
    userId: '',
    userNm: '',
    userPs: '',
    email: '',
    role: '',
    lastPsDt: '',
    userStatusCd: '',
    loginFailCnt: '',
    createdBy: '', // 등록자
    createdAt: '', // 등록일시
    updatedBy: '', // 수정자
    updatedAt: '' // 수정일시
};
const inputForm = ref({ ...defaultInputForm });
const initInputForm = () => {
    inputForm.value = { ...defaultInputForm };
};

// 폼 초기화 후 팝업 열기
const addUser = () => {
    initInputForm();
    dialogUser.value.onOpen();
};

// 팝업 닫기
const closePopupUser = () => {
    dialogUser.value.onClose();
};
// 사업장 권한 팝업 닫기
const closePopupCompanyUser = () => {
    dialogCompanyUser.value.onClose();
};

const viewUser = async function (rowKey) {
    const row = gridUser.value.tuiGrid.getRow(rowKey); // rowKey로 행 데이터를 가져옴
    if (!row) {
        console.error('Row not found:', rowKey);
        return;
    }
    const item = await getUser(row.userId); // 실제 데이터 호출
    inputForm.value = { ...item.result };
    dialogUser.value.onOpen();
};

const viewCompanyUser = async function (rowKey) {
    const cell = gridUser.value.tuiGrid.getFocusedCell();
    const userRow = gridUser.value.tuiGrid.getRow(cell.rowKey);
    const row = gridCompanyUser.value.tuiGrid.getRow(rowKey); // rowKey로 행 데이터를 가져옴
    if (!row) {
        console.error('Row not found:', rowKey);
        return;
    }

    const param = {
        compId: row.compId,
        userId: userRow.userId
    };
    const item = await searchByCompIdAndUserId(param); // 실제 데이터 호출
    companyUserInputForm.value = { ...item.result };
    dialogCompanyUser.value.onOpen();
};

const deleteUser = function (data, dv) {
    let param;
    if (dv === 'form') {
        param = data;
    } else {
        param = gridUser.value.tuiGrid.getRow(data); // rowKey로 행 데이터를 가져옴
        if (!param) {
            console.error('Row not found:', data);
            return;
        }
    }

    confirmMsg('warning', '삭제 하시겠습니까?<br/>하위 목록도 함께 비활성화 됩니다.', '', { fun: deleteUserAction, param: param.userId });
};
const deleteUserAction = async userId => {
    let vo = await removeUser(userId);
    if (dialogUser.value) {
        //모달 창이 열려 있으면
        closePopupUser();
        searchUserGrid(); //그리드 새로 고침
    }
    toastPopup('삭제에 성공하였습니다.', vo.result.userNm, 'success');
};

const saveUser = async dv => {
    let item = null;
    if (dv === 'form') {
        item = inputForm.value;
    } else {
        item = gridUser.value.tuiGrid.getRow(dv); // dv = rowKey로 행 데이터를 가져옴
    }
    if (!item.userId) {
        toastPopup('실패', '필수 값을 넣으세요.', 'info');
        return false;
    }
    saveUserAction(dv, item);
};

const saveUserAction = async (rowKey, item) => {
    let vo = null;
    if (item.cmd === 'I') {
        vo = await createUser(item);
    } else {
        vo = await modifyUser(item.userId, item);
    }
    inputForm.value = { ...vo.result };

    if (dialogUser.value) {
        //모달 창이 열려 있으면
        closePopupUser();
        searchUserGrid(); //그리드 새로 고침
    }

    toastPopup('저장에 성공하였습니다.', vo.result.userNm, 'success');
};

const saveCompanyByUser = async dv => {
    if (dv === 'form') {
        const cell = gridCompanyUser.value.tuiGrid.getFocusedCell();
        const updatedRow = {
            ...gridCompanyUser.value.tuiGrid.getRow(cell.rowKey),
            ...companyUserInputForm.value
        };
        gridCompanyUser.value.tuiGrid.setRow(cell.rowKey, updatedRow);
        gridCompanyUser.value.tuiGrid.check(cell.rowKey);
        closePopupCompanyUser();
    } else {
        let items = gridCompanyUser.value.tuiGrid.getCheckedRows();
        saveCompanyUserAction(items);
    }
};

const saveCompanyUserAction = async items => {
    const cell = gridUser.value.tuiGrid.getFocusedCell();
    const row = gridUser.value.tuiGrid.getRow(cell.rowKey);
    if (items.length === 0) {
        await removeCompanyUser(row.userId);
    } else {
        items.forEach(item => (item.userId = row.userId));
        await saveCompanyUser(items); //업무
    }
    searchCompanyUserGrid(row.userId); //그리드 새로 고침

    toastPopup('저장에 성공하였습니다.', row.userNm, 'success');
};

const listSize = ref(20);
const pageOptions = ref({
    id: 'pageUser',
    totalItems: 0,
    itemsPerPage: listSize.value,
    visiblePages: 10,
    page: 1
});

const searchParam = ref({
    listSize: listSize.value,
    curPage: 1,
    searchText: '',
    id: '',
    sortBy: 'userIdAsc',
    asc: true
});

const searchUserGrid = async setPage => {
    if (setPage) {
        searchParam.value.curPage = setPage;
    }
    const res = await searchUser(searchParam.value);
    searchPage(res, gridUser, gridPageUser, searchParam, setPage);
    gridUser.value.tuiGrid.focus(0);
};

const searchCompanyUserGrid = async userId => {
    searchParam.value.userId = userId;
    const res = await treeCompanyUser(searchParam.value);
    if (res && res.list && gridCompanyUser.value) {
        gridCompanyUser.value.resetData(res.list);
        gridCompanyUser.value.tuiGrid.expandAll();
    }
};

let oldSearchText = ref('');
let searchData = ref([]);

const searchItems = () => {
    oldCellsInitialize();

    if (!searchParam.value.searchText) return true;

    const grid = gridCompanyUser.value.tuiGrid.getData();
    if (!searchData.value || searchData.value.length === 0 || oldSearchText.value !== searchParam.value.searchText) {
        oldSearchText.value = searchParam.value.searchText;
        searchData.value = grid.filter(row => {
            return new RegExp(searchParam.value.searchText).test(row.compNm);
        });
    }

    let itemSearch = searchData.value.shift();
    if (itemSearch) {
        const cells = document.querySelectorAll(`#${gridCompanyUser.value.tuiGrid.el.id} td[data-row-key="${itemSearch.rowKey}"]`);
        if (cells) {
            cells.forEach(cell => {
                cell.classList.add('searched-row');
            });
        }
    } else {
        searchData.value = [];
    }
    const input = document.getElementById('searchText');
    input.focus();
};

const oldCellsInitialize = () => {
    const cells = document.querySelectorAll(`#${gridCompanyUser.value.tuiGrid.el.id} td`);

    if (cells) {
        cells.forEach(cell => {
            cell.classList.remove('searched-row');
        });
    }
};

const focusGridUser = async ev => {
    const { rowKey } = ev;
    const row = gridUser.value.tuiGrid.getRow(rowKey);
    searchCompanyUserGrid(row.userId);

    searchParam.value.searchText = '';
    oldCellsInitialize();
};

const beforeMovePageUser = async ev => {
    pageOptions.value.page = ev.page;
    searchParam.value.curPage = ev.page;
    const res = await searchUser(searchParam.value);

    gridUser.value.resetData(res.list);
};

onMounted(async () => {
    try {
        searchUserGrid(1);
    } catch (error) {
        console.error('Error fetching data:', error);
    }
});

const gridUser = ref(null);
const gridPageUser = ref(null);
const gridCompanyUser = ref(null);
const gridUserColumns = ref([
    {
        header: 'CMD',
        name: 'cmd',
        align: 'center',
        hidden: true
    },
    {
        header: '사용자 ID',
        name: 'userId',
        align: 'center'
    },
    {
        header: '사용자명',
        name: 'userNm',
        align: 'center'
    },
    {
        header: '사용자 상태 코드',
        name: 'userStatusCd',
        align: 'center'
    },
    {
        header: '로그인 실패 카운트',
        name: 'loginFailCnt',
        align: 'center',
        width: 130
    },
    {
        header: '기능',
        name: 'func',
        align: 'center',
        width: 120,
        renderer: {
            type: 'functionButton',
            button: ['view', 'delete']
        }
    }
]);
const gridCompanyUserColumns = ref([
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
        header: '사업장 등록번호',
        name: 'rgstNo',
        align: 'center'
    },
    {
        header: '권한',
        name: 'grpNm',
        align: 'center'
    },
    {
        header: '기능',
        name: 'func',
        align: 'center',
        width: 120,
        renderer: {
            type: 'functionButton',
            button: ['view']
        }
    }
]);
</script>
<style lang="scss">
.searched-row {
    background-color: #68d196a6 !important;
}
</style>
