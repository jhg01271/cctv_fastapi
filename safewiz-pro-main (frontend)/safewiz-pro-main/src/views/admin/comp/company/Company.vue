<template>
    <!-- 콘텐츠 영역 -->
    <div class="content">
        <!-- 카드 타이틀 -->
        <h3>모든 사업장 관리</h3>
        <div class="control-field ui form mb2-4rem">
            <div class="row flex">
                <div class="col12-3 df aic lg-col12-4 sm-col12-12 sm-fww">
                    <label for="" class="wsn mr1rem fs1-4rem shrink0 sm-w100p sm-mb5px"><span>라벨명</span></label>
                    <select v-select class="w100p ul-minw0px ul-w100p" v-model="searchParam.delYn" @change="searchCompanyGrid(searchParam)">
                        <option value="N">사용만</option>
                        <option value>삭제포함</option>
                    </select>
                </div>
                <div class="col12-9 df pl6px lg-col12-8 sm-col12-12 sm-pl0 sm-mt4px">
                    <input v-input type="text" class="radius mr6px w711px ul-w100p search" placeholder="사업장 ID, 사업장 명, 대표자, 담당자" v-model="searchParam.searchWords" @keyup.enter="searchCompanyGrid(searchParam)" />
                    <button v-button class="btn w60px radius davy-grey" @click="searchCompanyGrid(searchParam)">
                        <span>검색</span>
                    </button>
                </div>
            </div>
        </div>

        <!-- 커스텀 버튼 영역 -->
        <div class="ui form mb1-2rem tar">
            <button v-button class="btn w50px radius green" @click="addCompany">
                <span>추가</span>
            </button>
        </div>

        <!-- 토스트 그리드 -->
        <i-DataGrid ref="gridCompany" gridId="gridCompany" :options="{ treeColumnOptions: { name: 'compId' } }" :columns="gridColumns" @rowView="viewCompany" @rowDelete="deleteCompany" id="gridCompany" />

        <!-- 팝업 -->
        <i-PopupDialog ref="dialogCompany">
            <!-- 단일 그리드 -->
            <div class="contents form ui w1200px">
                <compCompanyCompanyForm :inputForm="inputForm" :options="{ label: '사업장정보', readonly: false }" />
                <iFileList ref="fileList" targetType="company" :targetId="inputForm.attachId" />
                <i-ButtonList :btnInfo="{ saveBtn: true, deleteBtn: true, closeBtn: true }" :localInputForm="inputForm" @save="saveCompany" @delete="deleteCompany" @close="closePopupCompany" />
            </div>
        </i-PopupDialog>
    </div>
</template>

<script setup>
import BaseView from '@/components/base/BaseView';
import IFileList from '@/components/file/iFileList.vue';
const { ref, onBeforeMount, onMounted, toastPopup, confirmMsg, dateFormat, numberFormat, mediaSize } = BaseView();
import { createCompany, treeCompany, getCompany, modifyCompany, removeCompany } from '@/api/admin/comp/company/Company';
import compCompanyCompanyForm from '@/views/comp/company/CompanyForm.vue';

const gridCompany = ref(null);
const dialogCompany = ref(null); // PopupDialog에 대한 ref

const compIdList = ref([]);

const fileList = ref(null);

const inputForm = ref({
    cmd: 'I', // No
    compId: '', // 사업장 아이디
    compNm: '', // 사업장 이름 (사업장)
    compRmk: '', // 사업장 소개
    upCompId: '', // 상위 사업장
    rgstNo: '', // 사업자 등록번호
    corpNo: '', // 법인 등록번호
    rpstNm: '', // 대표자
    zipCd: '', // 우편번호
    addrs1: '', // 주소
    addrs2: '', // 상세주소
    chrgPrsn: '', // 담당자
    telNo: '', // 연락처
    bizNm: '', // 업종
    bizCd: '', // 업종코드
    mainPrdt: '', // 주요 생산품
    cmpltDe: '', // 준공년월
    flArea: '', // 연면적
    salesAmt: '', // 매출 금액
    fullEmplCnt: '', // 상시 종업원 수
    prdAmt: '', // 연간 생산량
    avrgWorkHr: '', // 일평균 근무 시간
    attachId: '', // 첨부 아이디
    inviteKey: '', // 초대 키
    createdBy: '', // 등록자
    createdAt: '', // 등록일시
    updatedBy: '', // 수정자
    updatedAt: '' // 수정일시
});

const initCompanyInputForm = () => {
    inputForm.value = {
        cmd: 'I', // No
        compId: '', // 사업장 아이디
        compNm: '', // 사업장 이름 (사업장)
        compRmk: '', // 사업장 소개
        upCompId: '', // 상위 사업장
        rgstNo: '', // 사업자 등록번호
        corpNo: '', // 법인 등록번호
        rpstNm: '', // 대표자
        zipCd: '', // 우편번호
        addrs1: '', // 주소
        addrs2: '', // 상세주소
        chrgPrsn: '', // 담당자
        telNo: '', // 연락처
        bizNm: '', // 업종
        bizCd: '', // 업종코드
        mainPrdt: '', // 주요 생산품
        cmpltDe: '', // 준공년월
        flArea: '', // 연면적
        salesAmt: '', // 매출 금액
        fullEmplCnt: '', // 상시 종업원 수
        prdAmt: '', // 연간 생산량
        avrgWorkHr: '', // 일평균 근무 시간
        attachId: '',
        inviteKey: '', // 초대 키
        createdBy: '', // 등록자
        createdAt: '', // 등록일시
        updatedBy: '', // 수정자
        updatedAt: '' // 수정일시
    };
};

const addCompany = () => {
    initCompanyInputForm();
    dialogCompany.value.onOpen();
};

// 팝업 닫기
const closePopupCompany = () => {
    if (dialogCompany.value) {
        dialogCompany.value.onClose();
    }
};

const viewCompany = async function (rowKey) {
    const row = gridCompany.value.tuiGrid.getRow(rowKey); // rowKey로 행 데이터를 가져옴
    if (!row) {
        console.error('Row not found:', rowKey);
        return;
    }
    const item = await getCompany(row.compId); // 실제 데이터 호출
    Object.assign(inputForm.value, item.result);
    dialogCompany.value.onOpen();
};

const deleteCompany = function (obj, dv) {
    let compId;
    if (dv === 'form') {
        compId = obj.compId;
    } else {
        const row = gridCompany.value.tuiGrid.getRow(obj); // rowKey로 행 데이터를 가져옴
        if (!row) {
            console.error('Row not found:', obj);
            return;
        }
        compId = row.compId;
    }

    confirmMsg('warning', '삭제 하시겠습니까?', '', { fun: deleteCompanyAction, param: compId });
};

const deleteCompanyAction = async id => {
    //confirmMsg callback을 위한 분리
    await removeCompany(id).then(() => {
        //deleteFiles(id);
    });
    dialogCompany.value.onClose();
    searchCompanyGrid(searchParam); //그리드 새로 고침
    toastPopup('info', '삭제', '삭제에 성공하였습니다.');
};

const saveCompany = async dv => {
    let item = null;
    if (dv === 'form') {
        item = inputForm.value;
    } else {
        item = gridCompany.value.tuiGrid.getRow(dv); // dv = rowKey로 행 데이터를 가져옴
    }
    if (!item.compId) {
        toastPopup('실패', '필수 값을 넣으세요.', 'info');
        return false;
    } else if (item.compId.length < 3) {
        toastPopup('실패', '사업장 ID는 3자 이상이어야 합니다.', 'info');
        return false;
    } else if (!item.compNm) {
        toastPopup('실패', '필수 값을 넣으세요.', 'info');
        return false;
    }
    saveCompanyAction(dv, item);
};

const saveCompanyAction = async (rowKey, item) => {
    let fileResult = await fileList.value.fnSave(); // 파일 저장
    if (fileResult[0] && fileResult[0].result) {
        item.attachId = fileResult[0].result.data;
    }
    let vo = null;
    console.log('=========콤마 오류 확인 : ', item.flArea);
    if (item.cmd === 'I') {
        vo = await createCompany(item);
    } else {
        vo = await modifyCompany(item.compId, item);
    }
    Object.assign(inputForm.value, vo.result);

    if (dialogCompany.value) {
        //모달 창이 열려 있으면
        closePopupCompany();
        searchCompanyGrid(searchParam); //그리드 새로 고침
    } else {
        gridCompany.value.updateIcons(gridCompany.value.tuiGrid, rowKey, ['view', 'delete']);
    }

    toastPopup('저장', '저장에 성공하였습니다.');
};

const listSize = ref(20);
const searchParam = ref({
    searchWords: '',
    sortKey: '',
    asc: true
});
const searchCompanyGrid = async () => {
    searchParam.value.curPage = 1;
    const res = await treeCompany(searchParam.value);
    console.log('전달', searchParam);
    console.log(res);
    if (res && res.list && gridCompany.value) {
        gridCompany.value.resetData(res.list);
        gridCompany.value.tuiGrid.expandAll();
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
        align: 'center',
        hidden: mediaSize.mobile
    },
    {
        header: '사업장 명',
        name: 'compNm',
        align: 'center',
        hidden: mediaSize.mobile
    },
    {
        header: '사업장 소개',
        name: 'compRmk',
        align: 'left',
        hidden: mediaSize.mobile
    },
    {
        header: '상위 사업장',
        name: 'upCompId',
        align: 'center',
        width: 100,
        hidden: mediaSize.mobile
    },
    {
        header: '사업자등록번호',
        name: 'rgstNo',
        align: 'center',
        width: 150,
        hidden: mediaSize.tablet
    },
    {
        header: '대표자',
        name: 'rpstNm',
        align: 'center',
        width: 150,
        hidden: mediaSize.tablet
    },
    {
        header: '주소',
        name: 'addrs1',
        align: 'center',
        hidden: mediaSize.pc
    },
    {
        header: '담당자',
        name: 'chrgPrsn',
        align: 'center',
        width: 150,
        hidden: mediaSize.tablet
    },
    {
        header: '연락처',
        name: 'telNo',
        align: 'center',
        hidden: mediaSize.pc
    },
    {
        header: '업종',
        name: 'bizNm',
        align: 'center',
        width: 150,
        hidden: mediaSize.pc
    },
    {
        header: '매출금액',
        name: 'salesAmt',
        align: 'center',
        width: 120,
        hidden: mediaSize.pc,
        formatter: numberFormat
    },
    {
        header: '상시 종업원 수',
        name: 'fullEmplCnt',
        align: 'center',
        width: 120,
        hidden: mediaSize.pc,
        formatter: numberFormat
    },
    {
        header: '삭제',
        name: 'delYn',
        align: 'center',
        width: 50,
        hidden: mediaSize.tablet
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

onBeforeMount(async () => {
    try {
        // console.log('onBeforeMount')
    } catch (error) {
        // console.error('Error fetching data:', error)
    }
});

onMounted(async () => {
    try {
        // let responses = await Promise.all([getSystemCode('comp_id')]);
        // compIdList.value = responses[0].list;

        searchCompanyGrid(searchParam);
        console.log('부모 검색후');
    } catch (error) {
        console.error('Error fetching data:', error);
    }
});
</script>
