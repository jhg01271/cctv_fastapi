import { defineStore } from 'pinia';
import { searchSystemCode, getSystemCodePage, upsertMasterCode, upsertDetailCode, removeMasterCode, removeDetailCode } from '@/stores/system/setting/api/SystemCode.js';

import BaseView from '@/components/base/BaseView';
const { toastPopup, ref, t, getCompId, alertMsg, confirmMsg, openLoading, endLoading } = BaseView();

export const useCommonCodeStore = defineStore('commonCode', () => {
    const inputForm = ref({});
    const initInputForm = () => {
        inputForm.value = {
            majorCd: '',
            minorCd: '',
            compId: '',
            name: '',
            remark: '',
            files:[],
            ordSeq: 99,
            __created__: true
        };
    };
    const saveFiles = ref({
        insert: [],
        delete: [],
        deleteFileId: []
    });
    const currentMajor = ref();
    const popType = ref('M');
    const addCode = param => {
        initInputForm();
        inputForm.value.__created__ = true;
        popType.value = param;
        inputForm.value.compId = getCompId();
        if (param == 'D') {
            inputForm.value.majorCd = currentMajor.value.majorCd;
        }
        dialogGroup.value.onOpen();
    };

    // const editRow=ref()
    const editCode = (rowKey, type) => {
        popType.value = type;
        initInputForm();
        inputForm.value.__created__ = false;
        if (type == 'M') {
            const editRow = gridGroup.value.tuiGrid.getRow(rowKey);
            inputForm.value.majorCd = editRow.majorCd;
            inputForm.value.name = editRow.majorNm;
            inputForm.value.remark = editRow.remark;
            inputForm.value.compId = editRow.compId;
        } else {
            const editRow = gridGroupMenu.value.tuiGrid.getRow(rowKey);
            inputForm.value.majorCd = editRow.majorCd;
            inputForm.value.minorCd = editRow.minorCd;
            inputForm.value.name = editRow.minorNm;
            inputForm.value.remark = editRow.remark;
            inputForm.value.ordSeq = editRow.ordSeq;
            inputForm.value.compId = editRow.compId;
            inputForm.value.files = editRow.files;
            inputForm.value.extra1 = editRow.extra1;
            inputForm.value.extra2 = editRow.extra2;
            inputForm.value.extra3 = editRow.extra3;
            inputForm.value.extra4 = editRow.extra4;
            inputForm.value.extra5 = editRow.extra5;
        }
        dialogGroup.value.onOpen();
    };

    const saveCode = async () => {
        // const isValid = await validationStore.validateAllFields('form', true)
        // if (isValid) {
        if (popType.value == 'M') {
            if (inputForm.value.majorCd) {
                upsertMasterCode(inputForm.value, true).then(res => {
                    // toastPopup('저장에 성공하였습니다.', res.result.majorCd, 'success')
                    searchSystemCodeGrid(true);
                });
                dialogGroup.value.onClose();
            } else {
                alertMsg('warning', '마스터 코드는 필수 입니다.');
            }
        } else {
            if (inputForm.value.minorCd) {
                const formData = new FormData();
                inputForm.value.deleteFiles = saveFiles.value.delete;
                console.log('### inputForm.value',inputForm.value)
                formData.append('info', new Blob([JSON.stringify(inputForm.value)], { type: 'application/json' }));
                // formData.deleteFiles = editFiles.delete;

                saveFiles.value.insert.forEach(file => {
                    if (file) {
                        formData.append('files', file); // 파일이 있을 경우 파일 추가
                    } else {
                        formData.append('files', new Blob([], { type: 'application/octet-stream' })); // 빈 파일 추가
                    }
                });
                upsertDetailCode(formData, true).then(res => {
                    // toastPopup('저장에 성공하였습니다.', res.result.minorCd, 'success')
                    searchGroupMenuGrid(true);
                });
                dialogGroup.value.onClose();
            } else {
                alertMsg('warning', '디테일 코드는 필수 입니다.');
            }
        }
    };

    const deleteCode = async () => {
        const masterRow = gridGroup.value.tuiGrid.getCheckedRows();
        const detailRow = gridGroupMenu.value.tuiGrid.getCheckedRows();
        // masterRow와 detailRow가 모두 비어 있는 경우 경고 메시지 표시
        if (masterRow.length === 0 && detailRow.length === 0) {
            alertMsg('warning', t('msgNoItem'));
            return;
        }

        masterRow.forEach(async item => {
            const data = {
                majorCd: item.majorCd,
                compId: item.compId,
                majorYn: 'Y'
            };
            confirmMsg('warning', t('msgDelete'), '', { fun: deleteAction, param: data });
        });
        detailRow.forEach(async item => {
            const data = {
                majorCd: item.majorCd,
                minorCd: item.minorCd,
                compId: item.compId,
                majorYn: 'N'
            };
            confirmMsg('warning', t('msgDelete'), '', { fun: deleteAction, param: data });
        });
    };
    const deleteAction = (data) =>{
        if(data.majorYn === 'Y') {
            removeMasterCode(data)
        } else {
            removeDetailCode(data)
        }
        searchSystemCodeGrid(true);
    }

    const searchSystemCodeGrid = async setTotal => {
        const res = await searchSystemCode(searchParam.value, true);
        if (res && res.list && gridGroup.value) {
            gridGroup.value.resetData(res.list);
            gridGroup.value.tuiGrid.focus(0);
            if (setTotal && gridPageMaster.value) {
                gridPageMaster.value.pagination.reset(res.totalCount);
            }
            if (res.list < 1) {
                //마스터 코드값이 없을 시 상세코드 그리드 초기화
                gridGroupMenu.value.tuiGrid.resetData([]);
            }
        }
    };

    const searchGroupMenuGrid = async setTotal => {
        openLoading()
        const res = await getSystemCodePage(searchParamDetail.value);
        if (res && res.list && gridGroupMenu.value) {
            gridGroupMenu.value.resetData(res.list);
            gridGroupMenu.value.tuiGrid.expandAll();
            if (setTotal && gridPageDetail.value) {
                gridPageDetail.value.pagination.reset(res.totalCount);
            }
        }
        endLoading()
    };

    let oldCells = ref('');
    const oldCellsInitialize = () => {
        if (oldCells.value) {
            oldCells.value.forEach(function (cell) {
                const rowKey = cell.getAttribute('data-row-key');
                const color = rowKey % 2 === 1 ? '#fafafb' : 'white';
                cell.style.backgroundColor = color;
            });
        }
    };

    const focusGridGroup = ev => {
        const row = gridGroup.value.tuiGrid.getRow(ev.rowKey);
        currentMajor.value = row;
        searchParamDetail.value.searchCd = row.majorCd;
        searchGroupMenuGrid(searchParamDetail.value);
        searchParam.value.searchText = '';
        oldCellsInitialize();
    };

    const beforeMovePageMaster = async ev => {
        pageOptions.value.page = ev.page;
        searchParam.value.curPage = ev.page;
        searchSystemCodeGrid(false);
    };

    const beforeMovePageDetail = async ev => {
        pageOptionsDetail.value.page = ev.page;
        searchParamDetail.value.curPage = ev.page;
        searchGroupMenuGrid(false);
    };

    const gridGroup = ref(null);
    const gridGroupMenu = ref(null);
    const dialogGroup = ref(null);
    const gridPageMaster = ref(null);
    const gridPageDetail = ref(null);

    const setRefs = (grid1, grid2, popup, page1, page2) => {
        gridGroup.value = grid1.value;
        gridGroupMenu.value = grid2.value;
        dialogGroup.value = popup.value;
        gridPageMaster.value = page1.value;
        gridPageDetail.value = page2.value;
    };

    const listSize = ref(13);
    const pageOptions = ref({
        id: 'pageMaster',
        totalItems: 0,
        itemsPerPage: listSize.value,
        visiblePages: 10,
        page: 1
    });

    const searchParam = ref({
        listSize: listSize.value,
        curPage: 1,
        searchText: '',
        compId: getCompId(),
        sortKey: '',
        asc: true
    });

    const pageOptionsDetail = ref({
        id: 'pageDetail',
        totalItems: 0,
        itemsPerPage: listSize.value,
        visiblePages: 10,
        page: 1
    });

    const searchParamDetail = ref({
        listSize: listSize.value,
        curPage: 1,
        searchText: '',
        searchCd: '', //majorCd
        compId: getCompId()
    });

    const gridGroupColumns = ref([
        {
            column: 'func',
            name: 'func',
            align: 'center',
            width: 80,
            renderer: {
                type: 'functionButton',
                button: ['detail']
            }
        },
        {
            column: 'systemCode',
            name: 'majorCd',
            align: 'center'
        },
        {
            column: 'systemCodeNm',
            name: 'majorNm',
            align: 'center'
        },
        {
            column: 'remark',
            name: 'remark',
            align: 'center'
        }
    ]);
    const gridGroupMenuColumns = ref([
        {
            column: 'func',
            name: 'func',
            align: 'center',
            width: 80,
            renderer: {
                type: 'functionButton',
                button: ['detail']
            }
        },
        {
            column: 'order',
            name: 'ordSeq',
            align: 'center',
            width: 50
        },
        {
            column: 'systemCode',
            name: 'minorCd',
            align: 'center'
        },
        {
            column: 'systemCodeNm',
            name: 'minorNm',
            align: 'center'
        },
        {
            column: 'extra1',
            name: 'extra1',
            align: 'center'
        },
        {
            column: 'extra2',
            name: 'extra2',
            align: 'center'
        },
        {
            column: 'extra3',
            name: 'extra3',
            align: 'center'
        },
        {
            column: 'extra4',
            name: 'extra4',
            align: 'center'
        },
        {
            column: 'extra5',
            name: 'extra5',
            align: 'center'
        },
        {
            column: 'remark',
            name: 'remark',
            align: 'center'
        }
    ]);

    return { inputForm, saveFiles, addCode, editCode, popType, saveCode, deleteCode, searchParam, searchParamDetail, setRefs, gridGroupMenuColumns, searchGroupMenuGrid, gridGroupColumns, pageOptionsDetail, pageOptions, searchSystemCodeGrid, focusGridGroup, beforeMovePageMaster, beforeMovePageDetail };
});
