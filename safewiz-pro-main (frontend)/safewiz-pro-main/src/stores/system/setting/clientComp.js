import { defineStore } from 'pinia'
import {getClient, getCompByClient, saveCompByClient, deleteCompByClient } from '@/stores/system/setting/api/clientCompApi.js';

import BaseView from '@/components/base/BaseView.js';
import { getJobCompAssessList } from '@/stores/safewiz/support/api/JobCompAssessApi.js';
const { mediaSize, ref, t, getCompId, alertMsg, confirmMsg, openLoading, endLoading } = BaseView()

export const useClientCompStore = defineStore('clientcomp', () => {

    const inputForm = ref({});
    const initInputForm = () => {
        // inputForm.value = {
        //     majorCd: '',
        //     minorCd: '',
        //     compId:'',
        //     name: '',
        //     remark: '',
        //     ordSeq:99,
        //     __created__:true
        // };
    };

    //통합검색 변수
    const searchClient = ref()
    const searchComp = ref()

    const gridClient = ref(null);
    const gridComp = ref(null);
    const dialogComp = ref(null);

    const setRefs=(grid1, grid2, popup)=>{
        gridClient.value = grid1.value;
        gridComp.value = grid2.value;
        dialogComp.value = popup.value;
    }

    const searchParam = ref({
        searchText: ''  //clnt_id
    });

    //고객사 조회
    const searchSystemCodeGrid = async (notify) => {
        openLoading();
        getClient(searchParam.value, notify)
            .then(res => {
                if (res && res.list && gridClient.value) {
                    if(searchClient.value !== undefined && searchClient.value !== "" ){
                        res.list = res.list.filter(item =>
                            item.clntNm.toLowerCase().includes(searchClient.value.toLowerCase()) ||
                            item.rpstNm.toLowerCase().includes(searchClient.value.toLowerCase()) ||
                            item.telNo.toLowerCase().includes(searchClient.value.toLowerCase())
                        );
                    }
                    gridClient.value.resetData(res.list);
                    gridClient.value.tuiGrid.focus(0);
                    if(res.list < 1) {
                        //고객사 데이터 없을때 그리드 초기화
                        gridComp.value.tuiGrid.resetData([]);
                    }
                }
            })
            .finally(() => {
                endLoading();
            });
    };

    //고객사별 사업장 조회
    const searchGroupMenuGrid = async (notify) => {
        openLoading();
        getCompByClient(searchParam.value, notify)
            .then(res => {
                if (res && res.list && gridComp.value) {
                    if(searchComp.value !== undefined && searchComp.value !== "" ){
                        res.list = res.list.filter(item =>
                            item.compNm.toLowerCase().includes(searchComp.value.toLowerCase()) ||
                            item.rpstNm.toLowerCase().includes(searchComp.value.toLowerCase()) ||
                            item.telNo.toLowerCase().includes(searchComp.value.toLowerCase())
                        );
                    }
                    inputForm.value = res.list
                    gridComp.value.resetData(res.list);
                    gridComp.value.tuiGrid.expandAll();
                }
            })
            .finally(() => {
                endLoading();
            });
    };

    //고객사별 사업장 저장
    const btnSave = async () =>{
        confirmMsg('warning', t('msgSave'), null, { fun: saveCode});
    }
    const saveCode = async () => {
        let param = gridComp.value.tuiGrid.getCheckedRows(); // rowKey로 행 데이터를 가져옴
        if (!param.length) {
            alertMsg('warning', t('msgNoItem'));
            return;
        }
        openLoading()
        saveCompByClient(param,true).then((res) => {
            searchGroupMenuGrid(false)
        })
        endLoading()
    }

    //고객사별 사업장 삭제
    const btnDelete = async () =>{
        confirmMsg('warning', t('msgDelete'), null, { fun: deleteCode});
    }
    const deleteCode = async () => {
        let param = gridComp.value.tuiGrid.getCheckedRows(); // rowKey로 행 데이터를 가져옴
        if (!param.length) {
            alertMsg('warning', t('msgNoItem'));
            return;
        }
        if (param.some(el => el.delYn === 'Y')) {
            alertMsg('warning', t('msgDeletedItem'));
            return;
        }
        openLoading()
        deleteCompByClient(param,true).then((res) => {
            searchGroupMenuGrid(false)
        })
        endLoading()
    };

    //사업장 팝업
    const closeComp = () => {
        dialogComp.value.onClose();
    };

    const applyComp = e => {
        dialogComp.value.onClose();

        inputForm.value = e.map(el => {
            // inputForm.value에서 el.compId가 이미 존재하는지 확인
            const exists = inputForm.value.some(item => item.clntCompId === el.compId);

            return {
                compClntId: searchParam.value.searchText,
                clntCompId: el.compId,
                compNm: el.compNm,
                rpstNm: el.rpstNm,
                telNo: el.telNo,
                useYn: el.useYn,
                cmd: exists ? 'U' : 'I' // 추가된거 'I'
            };
        });

        let preData = gridComp.value.tuiGrid.getCheckedRows();  //기존 체크 데이터
        gridComp.value.resetData(inputForm.value);

        inputForm.value.forEach(element => {
            // 이미 체크되어 있는지 확인
            const isChecked = preData.some(row => row.rowKey === element.rowKey);
            if (element.cmd === 'I' || isChecked) {    //추가거나
                gridComp.value.tuiGrid.check(element.rowKey);
            }
        });
    };

    const currentMajor=ref()
    const openComp = () => {
        dialogComp.value.onOpen()
    };

    /////////////////////그리드 관련 /////////////////////
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

    const focusGridClient = ev => {
        const row = gridClient.value.tuiGrid.getRow(ev.rowKey);
        currentMajor.value = row;
        searchParam.value.searchText = row.clntId;
        //if(searchParam.value.searchText !== row.clntId) {
            searchGroupMenuGrid(true);
            oldCellsInitialize();
        //}
    };


    // const editRow=ref()
    // const editCode = (rowKey,type) => {
    //     popType.value=type
    //     initInputForm();
    //     inputForm.value.__created__=false
    //     if(type=='M'){
    //         const editRow = gridClient.value.tuiGrid.getRow(rowKey)
    //         inputForm.value.majorCd= editRow.majorCd
    //         inputForm.value.name= editRow.majorNm
    //         inputForm.value.remark = editRow.remark
    //         inputForm.value.compId = editRow.compId
    //     }else{
    //
    //         const editRow = gridComp.value.tuiGrid.getRow(rowKey)
    //         inputForm.value.majorCd= editRow.majorCd
    //         inputForm.value.minorCd= editRow.minorCd
    //         inputForm.value.name= editRow.minorNm
    //         inputForm.value.remark= editRow.remark
    //         inputForm.value.ordSeq = editRow.ordSeq
    //         inputForm.value.compId = editRow.compId
    //     }
    //     dialogComp.value.onOpen()
    // };

    const gridClientColumns = ref([
        // {
        //     column: 'clntId',
        //     name: 'clntId',
        //     align: 'center',
        //     width: 50,
        //     hidden: mediaSize.
        // },
        {
            column: 'clinetComp_clntNm',
            name: 'clntNm',
            align: 'center'
        },
        {
            column: 'clinetComp_rpstNm',
            name: 'rpstNm',
            align: 'center'
        },
        {
            column: 'clinetComp_telNo',
            name: 'telNo',
            align: 'center'
        },
        {
            column: 'use',
            name: 'useYn',
            align: 'center',
            width: 50,
            renderer: {
                type: 'checkbox'
            },
            editor: true,
            disable: true
        },
    ]);
    const gridCompColumns = ref([
        // {
        //     column: 'compClntId',
        //     name: 'clinetComp_compClntId',
        //     align: 'center',
        //     width: 50
        //
        // },
        // {
        //     column: 'clntCompId',
        //     name: 'clinetComp_clntCompId',
        //     align: 'center'
        // },
        {
            column: 'clinetComp_compNm',
            name: 'compNm',
            align: 'center'
        },
        {
            column: 'clinetComp_rpstNm',
            name: 'rpstNm',
            align: 'center'
        },
        {
            column: 'clinetComp_telNo',
            name: 'telNo',
            align: 'center'
        },
        {
            column: 'use',
            name: 'useYn',
            align: 'center',
            width: 50,
            renderer: {
                type: 'checkbox'
            },
            editor: true,
            disable: true
        },
]);

  return {
      inputForm,openComp,btnSave, btnDelete, searchParam,
      setRefs, gridCompColumns, searchGroupMenuGrid,
      gridClientColumns,searchSystemCodeGrid,focusGridClient,
      searchClient, searchComp,
      closeComp, applyComp
  }
})