import { defineStore } from 'pinia';
import router from '@/router';
import BaseView from '@/components/base/BaseView';
import { useLoadingPanelStore } from '@/stores/loadingPanel.js';
import { getHazardsList, saveHazards, deleteHazards, getHazardsDetail, getClassificationOfHazardsReports, getSampleClassData, getClassData, deleteClassData, saveClassData, savePrevHazards } from './api/ClassificationOfHazardsApi';
// 공정/설비/물질 구분 관련 API
import { getPrcsItemList } from '@/stores/safewiz/planning/api/classificationProcessEquipMsdsApi';
const { openLoading, endLoading, alertMsg, confirmMsg, ref, t, getCompId, toastPopup, getCurrentDate, validationStore, baseDownload, formatDate, formatDateForDB } = BaseView();
const loadingPanelStore = useLoadingPanelStore();
export const useClassificationOfHardsStore = defineStore('ClassificationOfHardsStore', () => {
    const compId = getCompId(); // 사업장 id
    const writeYear = ref(''); // 생성년도
    const cmd = ref(null); // 상태 값 (I: 신규생성, U: 수정)
    // 유해위험요인 메인페이지 관련 로직 시작
    const factorCode = ref(null); //유해위험요인 분류관리 팝업에서 선택한 요인 코드값
    const dataFilterList = ref([]); //조회값이 들어가는 변수
    const dataList = ref([]);
    const checkedList = ref([]); //체크된 데이터의 정보가 들어가는 변수
    const docNo = ref(''); //출력시 필요한 값
    const currentPageName = ref(null);
    const writeDt = ref(null);
    const useYn = ref('Y');
    const loadPreviousYn = ref(false);

    //유해위험요인 조회버튼
    const mainSearch = () => {
        loadingPanelStore.openLoading();
        //  값 초기화
        dataFilterList.value = [];
        dataList.value = [];
        let param = {
            compId: getCompId(),
            writeYear: writeYear.value
        };
        getHazardsList(param).then(res => {
            dataFilterList.value = [];
            if (res && res.list) {
                //조회성공시 화면에 출력할 데이터를 설정한다.
                for (let data of res.list) {
                    data.detail = {
                        [t('hazardsClassification_writeDt')]: formatDate(data.writeDt),
                        [t('hazardsClassification_revNm')]: data.processWorkRevNm,
                        [t('hazardsClassification_cnt')]: data.hazardsClassificationCnt
                    };
                }
            }

            dataFilterList.value = res.list;
            dataList.value = res.list;
        });
        loadPreviousYn.value = false;
        loadingPanelStore.endLoading();
    };

    const mainPrevSearch = () => {
        loadingPanelStore.openLoading();

        let param = {
            compId: getCompId(),
            writeYear: writeYear.value - 1
        };
        
        getHazardsList(param).then(res => {
            if (res && res.list) {
                if(res.list.length > 0){
                    //조회성공시 화면에 출력할 데이터를 설정한다.
                    for (let data of res.list) {
                        data.checked = true;
                        data.compId = getCompId(),
                        data.writeDt = formatDateForDB(getCurrentDate()),
                        data.orderSeq = 99,
                        data.detail = {
                            [t('hazardsClassification_writeDt')]: formatDate(getCurrentDate()),
                            [t('hazardsClassification_revNm')]: data.processWorkRevNm,
                            [t('hazardsClassification_cnt')]: data.hazardsClassificationCnt
                        };
                    }
                    
                    const filteredList = res.list.filter(item => item.useYn === 'Y');

                    // 동일한 차수가 해당 년도에 이미 등록 되어있는 경우 불러오지 않도록 중복 제거
                    const filteredForDataFilterList = filteredList.filter(newItem => {
                        return !dataFilterList.value.some(item => 
                            item.processId === newItem.processId &&
                            item.processWorkRevNo === newItem.processWorkRevNo &&
                            item.useYn === newItem.useYn && 
                            item.useYn === 'Y'
                        );
                    });
                    
                    if(filteredForDataFilterList.length > 0){
                        dataFilterList.value = [...dataFilterList.value, ...filteredForDataFilterList];
                        dataList.value = dataFilterList.value;
                        loadPreviousYn.value = true;
                    }else{
                        alertMsg('warning', t('msgPreviouseNoData'));
                        loadPreviousYn.value = false;
                    }
                } else {
                    alertMsg('warning', t('msgPreviouseNoData'));
                }
            }
        }).finally(() => {
            loadingPanelStore.endLoading();
        });
    };

    const btnSave = () => {
        checkedList.value = dataFilterList.value.filter(item => item.checked);

        if (checkedList.value.length === 0) {
            // 선택된 항목이 없는 경우
            return alertMsg('warning', t('msgNoItem'));
        }

        confirmMsg('info', t('msgSave'), '', {
            fun: saveAction,
            param: checkedList.value
        });

    }

    const saveAction = saveParam => {
        openLoading();
        return savePrevHazards(saveParam, true)
            .then(res => {
                if (res && res.success) {
                    mainSearch();
                }
                endLoading();
            }).finally(() => {
                loadPreviousYn.value = false;
                endLoading();
            });
    }

    //유해위험요인 삭제버튼
    const btnDelete = () => {
        if (checkedList.value.length === 0) {
            // 선택된 항목이 없는 경우
            return alertMsg('warning', t('msgNoItem'));
        }

        let deleteParam = [];

        for (let i = 0; i < checkedList.value.length; i++) {
            let deleteData = {
                writeYear: checkedList.value[i].writeYear,
                docType: checkedList.value[i].docType,
                docNo: checkedList.value[i].docNo,
                compId: getCompId()
            };
            deleteParam.push(deleteData);
        }

        confirmMsg('info', '삭제 하시겠습니까?', '', {
            fun: deleteAction,
            param: deleteParam
        });
    };

    // 삭제 api 호출
    const deleteAction = deleteParam => {
        loadingPanelStore.openLoading();
        deleteHazards(deleteParam, true).then(() => {
            // 삭제 후 재조회
            mainSearch();
        });
        loadingPanelStore.endLoading();
    };

    // 추가버튼 클릭시 유해 위험요인 분류상세 페이지로 이동
    const btnAdd = () => {
        loadPreviousYn.value = false;
        readonlyValue.value = false; //공정 선택 가능하도록
        saveFactorData.value = [];
        //페이지 이동
        router.push({
            path: '/ClassificationOfHazardsDetail'
        });
    };

    //목록 버튼 클릭시 위험성평가 페이지로 이동
    const btnBack = () => {
        router.push({
            path: '/RiskAssessment'
        });
    };

    //상세보기 클릭시 유해위험요인 상세 페이지로 이동
    const btnDetail = async e => {
        loadingPanelStore.openLoading();
        readonlyValue.value = true;
        saveFactorData.value = [];
        docNo.value = e.docNo; //출력시 필요한 docNo를 넣어줌
        //공정에 대한 데이터를 조회
        let param = {
            compId: getCompId(),
            writeYear: writeYear.value,
            docNo: e.docNo
        };

        const checkDupe = (arr, value) => {
            for (var a of arr) {
                if (a.name == value.name) return;
            }
            arr.push(value);
        };

        getHazardsDetail(param)
            .then(res => {
                cmd.value = 'U';
                // msds, equip 초기화
                selectedProcessEquipMsds.value.equipment = [];
                selectedProcessEquipMsds.value.msds = [];
                prcsList.value = [
                    {
                        id: res.list[0]?.processId, // 공정 id
                        name: res.list[0]?.processNm, // 공정 이름
                        revNo: res.list[0].revNo, // 작업차수
                        revNm: res.list[0].revNm, // 작업차수명
                        createdAt: res.list[0].resistedAt, // 등록일시
                        docNo: res.list[0].docNo, // 문서번호(수정시 필요)
                        orderSeq: res.list[0]?.orderSeq || '', // 정렬순서
                    }
                ];

                useYn.value = res.list[0].useYn, // 사용여부
                writeDt.value = formatDate(res.list[0]?.writeDt) || getCurrentDate() // 작성일자

                // equip, msds 데이터가 있는 경우 세팅
                if (res.list[0].itemList.length > 0) {
                    res.list[0].itemList.forEach(item => {
                        let row = {
                            id: item.workDetailId,
                            name: item.content
                        };
                        if (item.workType == 'equipment') {
                            checkDupe(selectedProcessEquipMsds.value.equipment, row);
                        } else if (item.workType == 'msds') {
                            checkDupe(selectedProcessEquipMsds.value.msds, row);
                        }
                    });
                }

                // 위험요인 데이터가 있는 경우 세팅
                if (res.list[0].factorList.length > 0) {
                    factorList.value.forEach(master => {
                        master.forEach(detail => {
                            res.list[0].factorList.forEach(data => {
                                // code와 type이 동일하면 checked true
                                if (detail.classId == data.hazardsClassification && detail.factorId == data.hazardsType) {
                                    detail.checked = 'Y';
                                    saveFactorData.value.push(detail);
                                }
                            });
                        });
                    });
                }

                prcsListItem.value = JSON.parse(JSON.stringify(prcsList.value));
            })
            .then(() => {
                toastPopup('조회성공', '조회되었습니다.');
            });
        loadingPanelStore.endLoading();
    };

    const btnPrint = async () => {
        loadingPanelStore.openLoading();
        //상세페이지에서 출력시
        if (currentPageName.value === 'ClassificationOfHazardsDetail') {
            const param = {
                writeYear: writeYear.value,
                checkedObjList: [
                    {
                        writeYear: writeYear.value,
                        docNo: docNo.value,
                        docType: 'CHD'
                    }
                ]
            };

            baseDownload(getClassificationOfHazardsReports, param);
            // return new Promise((resolve) => {
            //     getClassificationOfHazardsReports([param], false)
            //         .then((res) => {
            //             loadingPanelStore.endLoading();
            //             resolve({ result: res.result, success: res.success });
            //             let link = document.createElement('a');

            //             let objectUrl = window.URL.createObjectURL(res.data);
            //             link.href = objectUrl;
            //             link.download = res.headers.filename;
            //             link.click();
            //         })
            //         .catch((err) => {
            //             loadingPanelStore.endLoading();
            //             alertMsg('warning', err);
            //         });
            // });
        } else {
            //메인화면에서 출력시
            let printList = checkedList.value;

            const param = {
                writeYear: writeYear.value,
                checkedObjList: printList.map(el => ({
                    writeYear: el.writeYear,
                    docNo: el.docNo,
                    docType: el.docType,
                    compId: el.compId
                }))
            };
            baseDownload(getClassificationOfHazardsReports, param);

            // return new Promise((resolve) => {
            //     getClassificationOfHazardsReports(printList, false)
            //         .then((res) => {
            //             loadingPanelStore.endLoading();
            //             resolve({ result: res.result, success: res.success });

            //             let link = document.createElement('a');
            //             // 객체를 만들어서 생성
            //             let objectUrl = window.URL.createObjectURL(res.data);
            //             link.href = objectUrl;
            //             link.download = res.headers.filename;
            //             link.click();
            //         })
            //         .catch((err) => {
            //             loadingPanelStore.endLoading();
            //             alertMsg('warning', err);
            //         });
            // });
        }
    };

    //유해위험요인 메인페이지 관련 로직 끝

    //유해위험요인 상세페이지 관련 로직 시작
    const prcsList = ref([
        {
            id: '', //공정 id
            name: '', //공정 이름
            revNo: '', //작업차수
            useYn: '', //사용여부
            createdAt: '', //등록일시
            docNo: '', //문서번호(수정시 필요)
            orderSeq: '' // 정렬
        }
    ]); // 공정정보

    let factorList = ref([]); //조회된 분류요인들
    let saveFactorData = ref([]); //선택된 분류요인과 저장값
    const readonlyValue = ref(null); //신규등록시에는 공정 선택가능, 수정시에는 공정 수정 불가하는 플래그값
    const selectedProcessEquipMsds = ref({
        equipment: [], //설비
        msds: [] //위험물질
    });

    let prcsListItem = ref([]); // copy data

    //공정 팝업 닫기
    const closePrcs = e => {
        // equip, msds 데이터 초기화
        selectedProcessEquipMsds.value.equipment = [];
        selectedProcessEquipMsds.value.msds = [];
        //공정id,공정이름,작업내용차수,등록일자,사용여부
        prcsList.value[0] = e;

        searchProcessInfo();
    };

    //공정팝업 닫은후에 공정에 해당하는 작업내용차수,등로일자,정렬,사용여부,설비,사용물질 조회
    const searchProcessInfo = () => {
        loadingPanelStore.openLoading();
        selectedProcessEquipMsds.value.equipment = [];
        selectedProcessEquipMsds.value.msds = [];

        // let param = {
        //     processId: prcsList.value[0].id
        // };
        let equipmentData = [];
        let msdsData = [];
        getPrcsItemList(writeYear.value, [prcsList?.value?.[0].revNo], [prcsList.value[0].id], false)
            .then(res => {
                // getProcessEquipmentAndMsds(param).then((res) => {
                if (res && res.list) {
                    for (let i = 0; i < res.list.length; i++) {
                        //workType이 equipment면 설비
                        if (res.list[i].workType === 'equipment') {
                            equipmentData.push({ name: res.list[i].content, id: i + 1 });
                        } else {
                            //workType가 msds면 사용물질
                            msdsData.push({ name: res.list[i].content, id: i + 1 });
                        }
                    }
                }
            })
            .finally(() => {
                selectedProcessEquipMsds.value.equipment = equipmentData;
                selectedProcessEquipMsds.value.msds = msdsData;
            });
        loadingPanelStore.endLoading();
    };

    //최초 등록시 요인에 등록된 분류 조회
    const initFactorSearch = async () => {
        loadingPanelStore.openLoading();
        await Promise.all([searchFactorClassData('factor_1'), searchFactorClassData('factor_2'), searchFactorClassData('factor_3'), searchFactorClassData('factor_4'), searchFactorClassData('factor_5'), searchFactorClassData('factor_6')]).then(res => {
            if (res) {
                for (let i = 0; i < res.length; i++) {
                    factorList.value.push(res[i].list.filter(item => item.useYn != 'N'));
                }
            }
        });
        /*
    최초 화면 오픈시 마지막으로 조회되는게 factor_6이라서 분류관리 팝업 열었을때 factor_6으로 조회되는 버그가 있어서
    factor_1로 초기화 시켜줌
    */
        factorCode.value = 'factor_1';
        // loadingPanelStore.endLoading();
    };

    //유해 위험요인 분류 상세 저장 전 validation
    const saveValidation = () => {
        if (saveFactorData.value.length === 0) {
            alertMsg('warning', '유해위험요인을 선택해주세요.');
            return false;
        }
        return true;
    };

    // 저장 api 호출
    const saveHazardsAction = () => {
        loadingPanelStore.openLoading();
        //요인데이터
        let saveFactorDataParam = [];
        for (let i = 0; i < saveFactorData.value.length; i++) {
            saveFactorDataParam.push({
                //2024.11.06 김현재 작성 지금은 writeYear에 현재년도만 들어가지만 전년도불러오기 기능개발뒤에는 현재년도가 아닐수도 있기에 추후에 writeYear가 변경될수도 있음
                writeYear: writeYear.value, //작성년도
                docType: 'CHD', //문서타입
                docNo: '', //문서번호
                docSeq: '', //유해위험요인분류상세 문서번호,
                orderSeq: prcsList[0]?.orderSeq,
                useYn: prcsList[0]?.useYn,
                hazardsType: saveFactorData.value[i]?.factorId || saveFactorData.value[i].hazardsType, //위험요인 구분 ID
                hazardsClassification: saveFactorData.value[i]?.classId || saveFactorData.value[i].hazardsClassification //위험요인 분류ID
            });
        }

        let param = {
            cmd: cmd.value,
            writeYear: writeYear.value, //작성년도
            docType: 'CHD', //문서타입
            docNo: prcsList.value[0]?.docNo || '', //문서번호
            compId: getCompId(), //사업장ID
            processId: prcsList.value[0].id, //공정ID
            processWorkRevNo: prcsList.value[0].revNo, //작업차수
            useYn: prcsList.value[0].useYn,
            orderSeq: prcsList.value[0]?.orderSeq || null,
            factorList: saveFactorDataParam,
            writeDt: prcsList.value[0].writeDt
        };

        return new Promise(resolve => {
            saveHazards(param, true).then(res => {
                loadingPanelStore.endLoading();
                // 저장 후 공정 선택 안 되게 수정
                // readonlyValue.value = true
                // cmd.value = 'U'
                resolve({ result: res.result, success: res.success });
            });
        });
    };

    //유해위험요인 상세페이지 관련 로직 끝

    //유해위험요인 분류관리 팝업 로직 시작

    //유해위험요인 분류관리 팝업에서 위험요인을 선택했을때 요인에 해당하는 위험분류 목룍 조회
    const searchFactorClassData = code => {
        openLoading();
        factorCode.value = code;
        return new Promise(resolve => {
            getClassData({ searchText: code }).then(res => {
                endLoading();
                resolve(res);
            });
        });
        // return getSystemCodeCustom({
        //     compId: compId,
        //     majorCd: code,
        // });
    };

    //유해위험요인 분류관리팝업 삭제
    const deleteFactorSystemCode = async deleteList => {
        openLoading()
        return new Promise(resolve=>{
            deleteClassData(deleteList, true).then(res=>{
                endLoading()
                resolve(res)
            }).catch(()=>{
                endLoading()
            })
        })
    };

    //유해위험요인 분류관리팝업 저장
    const saveFactorSystemCode = async saveList => {
        console.log('### save', saveList)
        openLoading()
        return new Promise(resolve=>{
            saveClassData(saveList, true).then(res=>{
                endLoading()
                resolve(res)
            }).catch(()=>{
                endLoading()
            })
        })
    };

    //예시불러오기
    const getSampleFactorData = () => {
        openLoading();
        return new Promise(resolve => {
            getSampleClassData({ searchText: factorCode.value }).then(res => {
                endLoading();
                resolve(res);
            });
        });
        // return getSystemCode({
        //     compId: compId,
        //     majorCd: factorCode.value,
        // });
    };

    //유해위험요인 분류관리 팝업 로직 끝

    return {
        btnAdd,
        saveValidation,
        saveHazardsAction,
        searchFactorClassData,
        prcsList,
        closePrcs,
        compId,
        factorCode,
        deleteFactorSystemCode,
        saveFactorSystemCode,
        initFactorSearch,
        factorList,
        saveFactorData,
        getSampleFactorData,
        mainSearch,
        dataFilterList,
        dataList,
        selectedProcessEquipMsds,
        writeYear,
        docNo,
        btnDelete,
        checkedList,
        btnBack,
        btnDetail,
        cmd,
        readonlyValue,
        btnPrint,
        currentPageName,
        prcsListItem,
        writeDt,
        useYn,
        mainPrevSearch,
        btnSave,
        loadPreviousYn
    };
});
