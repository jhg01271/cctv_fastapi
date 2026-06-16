import { defineStore } from 'pinia';
import router from '@/router';
import BaseView from '@/components/base/BaseView';
import { nextTick, reactive } from 'vue';
import { gsap } from 'gsap';
import { deletePermitToWork, deletePermitToWorkList, getPermitToWork, getPermitToWorkDetail, getPermitToWorkReport, savePermitToWork, savePermitToWorkFiles } from '@/stores/safewiz/impl/api/permitToWorkApi';
import _ from 'lodash';


import { useButtonListStore } from '@/stores/buttonList';
import { getSystemCode } from '@/stores/safewiz/dataset/api/datasetApi';

const { ref, t, formatDate, baseDownload, alertMsg, confirmMsg, getCompId, getCurrentDate, openLoading, endLoading, formatDateForDB } = BaseView();

const layoutStore = useButtonListStore();

export const usePermitToWorkStore = defineStore('permitToWork', () => {
    const compId = getCompId();
    const writeYear = ref(getCurrentDate().substring(0, 4));
    const currentDate = ref(getCurrentDate());
    const originData = ref({});

    const inputForm = ref({});
    const checkedList = ref([]);
    const searchedData = ref([]);
    const filteredData = ref([]);
    const searchText = ref('');
    // const workTypeList = ref([]);
    const state = reactive({
        workTypeList:[]
    });

    const workplace = ref();
    const supervisor = ref();
    const supplier = ref();
    const approval = ref();

    const setRefs = (workplaceRepresentative, fieldSupervisor, supplierRepresentative, approvalAuthority) => {
        workplace.value = workplaceRepresentative.value;
        supervisor.value = fieldSupervisor.value;
        supplier.value = supplierRepresentative.value;
        approval.value = approvalAuthority.value;
    };

    const searchParam = ref({
        compId: compId,
        writeYear: writeYear.value,
        docNo: '',
        docType: 'PTW'
    });

    const signature = ref(null);
    const initInputForm = () => {
        inputForm.value = {
            cmd: 'I',
            writeYear: '',
            docType: 'PTW',
            docNo: '',
            compId: compId,
            hrList: [],
            workTypeList: [],
            detailList: [],
            deleteList: [],
            workDt: currentDate.value, //작업일자
            workTime: null,
            workStart: null, //작업 시작시간
            workEnd: null, //작업 종료시간
            workplace: null, //작업장소
            equipmentId: null, //작업설비
            workContent: null, //작업내용
            partCompId: null, //협력사
            inspectionList: [], //점검사항
            overallOpinion: null, //종합의견
            useYn: 'Y',
            deleteFiles: []
        };
    };
    const fileList = ref({
        editFiles: {
          insert: [],  // 새로 추가된 파일들
          delete: []   // 삭제할 파일들
        },
      });
    
    const files = ref([]) // 조회된 파일

    // 데이터 조회
    const getPermitToWorkList = notify => {
        openLoading();
        return new Promise(() => {
            getPermitToWork(searchParam.value, notify)
                .then(res => {
                    searchedData.value = res.list;
                    const groupedData = groupByYear(searchedData.value);
                    filteredData.value = groupedData;
                    filter();
                })
                .finally(() => endLoading());
        });
    };
    // 배열 내 workDt 값의 month 2자리를 추출하여 같은 값끼리 그룹화하는 함수
    const groupByYear = searchedData => {
        const grouped = searchedData.reduce((groups, item) => {
            const month = item.workDt.substring(4, 6);
            if (!groups[month]) {
                groups[month] = [];
            }
            groups[month].push(item);
            return groups;
        }, {});

        // 그룹화된 데이터를 원하는 형식으로 변환
        return Object.keys(grouped).map(month => ({
            month: month + '월',
            data: grouped[month]
        }));
    };


    //#region =====[F] 조회-안전작업 허가서 상세 화면 =====
    const getPermitToWorkDetailList = () => {
        openLoading();
        return new Promise((resolve, reject) => { 
            getPermitToWorkDetail(searchParam.value, false)
                .then(res => {
                    if (res.list.workTypeList.length > 0) {
                        state.workTypeList.forEach(workType => {
                            const matchingItem = res.list.workTypeList.find(item => item.workTypeId === workType.minorCd);
                            if (matchingItem) {
                                workType.checkYn = matchingItem.checkYn;
                                workType.writeYear = matchingItem.writeYear;
                                workType.docNo = matchingItem.docNo;
                                workType.docType = 'PTW';
                                workType['additionalInfo'] = matchingItem.additionalInfo;
                            }
                        });
                    }
                    if (res.list.hrList.length > 0) {
                        res.list.hrList.forEach(el => {
                            el.id = el.hrId;
                            el.name = el.hrNm;
                        });
                    }
                    if (res.list.detailList.length > 0) {
                        res.list.detailList.forEach(row => {
                            if (row.safetyEqList) {
                                row.safetyEqList.forEach(el => {
                                    el.id = el.safetyEqItemId;
                                    el.name = el.safetyEqItemNm;
                                });
                            }
                        });
                    }
    
                    res.list.workDt = formatDate(res.list.workDt);
                    inputForm.value = res.list;
                    files.value = res.list.files;
                    originData.value = _.cloneDeep(res.list);
    
                    resolve();  // 성공 시 resolve
                })
                .catch(err => {
                    reject(err); // 실패 시 reject
                })
                .finally(() => endLoading());
        });
    };    
    //#endregion

    //#region =====[F] 조회-시스템 코드 가져오는 API =====
    const getSystemCodeList = async param => {
        openLoading();
        try {
            return await getSystemCode(param);
        } catch (error) {
            console.error('getSystemCode API 호출 오류:', error);
            throw error;
        } finally {
            endLoading();
        }
    };
    //#endregion

    //#region =====[F] 작업종류 시스템 코드 가져와서 변수에 할당 =====
    const getWorkTypeList = async () => {
        let responses = await getSystemCodeList({ majorCd: 'C0032', compId: getCompId() });
        if (responses) {
            responses.list.forEach(el => {
                el.workTypeId = el.minorCd;
                el.workTypeNm = el.minorNm;
                el.checkYn = 'N';
            });
            state.workTypeList = responses.list;
        }
    };
    //#endregion

    // 저장(리스트)
    const btnSavePermitToWork = async saveParam => {
        inputForm.value.workDt = formatDateForDB(inputForm.value.workDt);
    
        return new Promise((resolve, reject) => {
            saveParam.detailList = saveParam.detailList.filter(item => !(item.cmd === 'D' && (!item.writeYear || item.writeYear === '')));
            openLoading();
    
            savePermitToWork(saveParam, true)
                .then(async res => {
                    if (res.success && res.result) {
                        const { docNo, writeYear, compId, docType } = res.result;
                        
                        // 파라미터 및 inputForm 동기화
                        searchParam.value = { docNo, writeYear, compId, docType };
                        inputForm.value.docNo = docNo;
                        inputForm.value.writeYear = writeYear;
                        inputForm.value.compId = compId;
                        inputForm.value.docType = docType;
    
                        const formData = new FormData();
                        formData.append('info', new Blob([JSON.stringify(inputForm.value)], { type: 'application/json' }));
    
                        fileList.value.editFiles.insert.forEach(file => {
                            formData.append('files', file || new Blob([], { type: 'application/octet-stream' }));
                        });
    
                        await savePermitToWorkFiles(formData); // 파일 저장

                        // 서명자 세팅
                        await workplace.value.setHrChipsApprovalInfo(res.result.docType, res.result.writeYear, res.result.docNo);
                        await supervisor.value.setHrChipsApprovalInfo(res.result.docType, res.result.writeYear, res.result.docNo);
                        await supplier.value.setHrChipsApprovalInfo(res.result.docType, res.result.writeYear, res.result.docNo);
                        await approval.value.setHrChipsApprovalInfo(res.result.docType, res.result.writeYear, res.result.docNo);
                        resolve(true); 
                    } else {
                        resolve(false);
                    }
                })
                .catch(err => {
                    console.error('저장 실패:', err);
                    reject(err);
                })
                .finally(() => {
                    getPermitToWorkDetailList();
                    layoutStore.useBtnList = ['btnBack', 'btnSearch', 'btnSave', 'btnDelete', 'btnDownload'];
                    endLoading();
                });
        });
    };

    //#region [안전작업 허가서 삭제]
    const delPermitToWorkList = saveParam => {
        openLoading();
        return new Promise(resolve => {
            deletePermitToWorkList(saveParam, true).then(res => {
                getPermitToWorkList(false);
                // resolve({ list: res.list, totalCount: res.totalCount })
                resolve(res);
            });
        }).finally(() => endLoading());
    };
    //#endregion

    //#region [안전작업 허가서 점검사항 삭제]
    const delPermitToWork = saveParam => {
        openLoading();
        return new Promise(resolve => {
            deletePermitToWork(saveParam, true)
                .then(res => {
                    getPermitToWorkDetailList(false);
                    // resolve({ list: res.list, totalCount: res.totalCount })
                    resolve(res);
                })
                .finally(() => endLoading());
        });
    };
    //#endregion

    // -------------------------------------------------

    // 신규 추가 이동
    const btnAdd = () => {
        initInputForm();
        layoutStore.useBtnList = ['btnBack', 'btnSearch', 'btnSave', 'btnDelete', 'btnDownload'];
        router.push({
            name: 'PermitToWorkDetail'
        });
    };

    //목록으로 이동
    const goBack = () => {
        router.push({ name: 'SafetyMgmtGuidelines', state: { activeTab: 'opinion' } });
    };

    // 상세보기 이동
    const goDetail = () => {
        (inputForm.value.cmd = 'U'),
            router.push({
                name: 'PermitToWorkDetail'
            });
        // getPermitToWorkDetailList(false);
    };

    const btnDownload = async list => {
        if (list.length > 0) {
            let searchVO = _.cloneDeep(searchParam.value);
            searchVO.docType = 'PTW';
            searchVO.checkedList = list;
            baseDownload(getPermitToWorkReport, searchVO);
            // openLoading();
            // getPermitToWorkReport(searchVO).then(res => {
            //   downloadReport(res)
            // }).finally(() => endLoading());
        } else {
            alertMsg('warning', t('msgNoItem'));
            return;
        }
    };

    const btnDetailDownload = () => {
        let checkDocSeqList = [inputForm.value.docNo];
        let searchVO = { compId: compId, writeYear: inputForm.value.writeYear, docNo: inputForm.value.docNo, docType: inputForm.value.docType, checkedList: checkDocSeqList };
        baseDownload(getPermitToWorkReport, searchVO);
        // openLoading();
        // return new Promise((resolve) => {
        //   getPermitToWorkReport({ compId: compId, writeYear: inputForm.value.writeYear,docNo: inputForm.value.docNo,docType: inputForm.value.docType, checkedList: checkDocSeqList }, true)
        //     .then(res => {
        //       downloadReport(res)
        //       resolve({ result: res.result })
        //     }).finally(() => endLoading());
        // });
    };

    // -------------------------------------------------
    const btnDelete = async () => {
        const checkedList = segments.value.flatMap(el => el.data.filter(id => id.checked));
        segments.value.forEach(el => el.data.forEach(id => id.checked == true));

        if (checkedList.length == 0) {
            alertMsg('warning', t('msgNoItem'));
            return;
        }

        confirmMsg('warning', t('msgDelete'), '', {
            fun: delPermitToWorkList,
            param: checkedList
        });
    };

    // -------------------------------------------------
    const btnDetailDelete = async () => {
        confirmMsg('warning', t('msgDelete'), '', {
            fun: delPermitToWork,
            param: inputForm.value
        });
    };
    // -------------------------------------------------

    //저장
    const btnSave = async () => {
        // `workTypeList` 배열에서 `checkYn` 값이 모두 'N'인지 확인
        const hasChecked = state.workTypeList.some(item => item.checkYn === 'Y');

        if (!hasChecked) {
            alertMsg('warning', '작업종류를 선택해주세요.');
            return;
        }

        //작업시간
        if (inputForm.value.workTime) {
            const [workStart, workEnd] = inputForm.value.workTime.split(' ~ ').map(time => time.trim());

            // 추출된 값을 saveParam에 저장
            inputForm.value.workStart = workStart;
            inputForm.value.workEnd = workEnd;
        }
        inputForm.value.writeYear = inputForm.value.workDt.substring(0, 4);
        //작업종류 세팅
        state.workTypeList.forEach(el => {
            (el.writeYear = inputForm.value.writeYear), (el.docType = inputForm.value.docType), (el.docNo = inputForm.value.docNo), (el.compId = inputForm.value.compId);
        });

        inputForm.value.workTypeList = state.workTypeList;
        //점검사항 세팅
        inputForm.value.detailList.forEach(el => {
            (el.writeYear = inputForm.value.writeYear), (el.docType = inputForm.value.docType), (el.docNo = inputForm.value.docNo), (el.compId = inputForm.value.compId);
        });

        inputForm.value.deleteFiles = fileList.value.editFiles.delete;
        // confirmMsg 내부에서 비동기 흐름 제어
        return new Promise((resolve) => {
            confirmMsg('info', t('msgSave'), '', {
                fun: async () => {
                    const result = await btnSavePermitToWork(inputForm.value);
                    resolve(result); // 저장 결과 리턴
                },
            });
        });
    };

    // -------------------------------------------------

    const segments = ref([]);
    const activeSegments = ref({});

    const filter = async () => {
        // 데이터 변환을 한번에 처리하여 성능 개선
        filteredData.value.forEach(el => {
            el.data = el.data.map(id => ({
                ...id,
                detail: {
                    [t('work_jobType')]: id.workTypeList
                        ? id.workTypeList.map(work => work.workTypeNm).join(', ') // workTypeNm 값만 추출 후 문자열로 변환
                        : '-',
                    [t('work_jobTime')]: id.workTime,
                    [t('work_jobPlace')]: id.workplace,
                    [t('work_jobEquipment')]: id.equipmentNm ? id.equipmentNm : '-'
                }
            }));
        });
        if (filteredData.value.length === 0) {
            // 현재 월을 구함 (0부터 시작하므로 +1 필요)
            const currentMonth = String(new Date().getMonth() + 1).padStart(2, '0') + '월';
            // 조회 데이터가 없을 시 현재 월 빈 아코디언 세팅
            filteredData.value[0] = { month: currentMonth };
        }

        segments.value = filteredData.value;

        const currentMonth = new Date().getMonth() + 1; // 현재 월 (0부터 시작하므로 +1을 해야 실제 월이 나옵니다)
        // 현재 해당하는 월에 해당하는 index를 찾기
        let index = filteredData.value.findIndex(item => item.month.slice(0, 2) == currentMonth);

        // 만약 일치하는 항목이 없으면 index를 0으로 설정
        if (index === -1) {
            index = 0;
        }

        // nextTick을 사용하여 DOM이 업데이트된 후 작업 실행
        nextTick(() => {
            const btn = document.getElementById(`accordion-btn_${index}`);
            if (btn != null) {
                const isActive = btn.classList.contains('active');

                if (!isActive) {
                    btn.click();
                }
            }
        });
    };

    //-----------------------------------------------
    //검색기능

    const dataFilter = () => {
        if (searchText.value) {
            const lowerCaseSearchText = searchText.value.toLowerCase(); // 검색어를 소문자로 변환
            const temp = filteredData.value
                .map(group => {
                    const filteredItems = group.data.filter(item => {
                        return (item.workDt && formatDate(item.workDt).toLowerCase().includes(lowerCaseSearchText)) || (item.workplace && item.workplace.toLowerCase().includes(lowerCaseSearchText)) || (item.equipmentNm && item.equipmentNm.toLowerCase().includes(lowerCaseSearchText)) || (item.partCompNm && item.partCompNm.toLowerCase().includes(lowerCaseSearchText)) || (item.workContent && item.workContent.toLowerCase().includes(lowerCaseSearchText)) || (item.detail && Object.values(item.detail).some(detailValue => detailValue && detailValue.toLowerCase().includes(lowerCaseSearchText)));
                    });

                    if (filteredItems.length > 0) {
                        return { ...group, data: filteredItems };
                    }
                    return null;
                })
                .filter(Boolean); // null 값을 제거하여 반환

            segments.value = temp;
        } else {
            segments.value = filteredData.value; // 검색어가 없으면 원본 데이터를 유지
        }
    };

    //-----------------------------------------------
    //useYn 체크

    const toggleUseYn = (field, event) => {
        // 체크박스의 체크 상태에 따라 'Y' 또는 'N'을 설정합니다.
        inputForm.value[field] = _.cloneDeep(event.target.checked ? 'Y' : 'N');
    };

    //-----------------------------------------------

    // [토글버튼]
    const toggleCheckYn = (index, event) => {
        state.workTypeList[index].checkYn = true;
        const isChecked = event.target.checked;

        state.workTypeList[index].checkYn = isChecked ? 'Y' : 'N';
        state.workTypeList[index].workTypeId = state.workTypeList[index].minorCd;
        state.workTypeList[index].workTypeNm = state.workTypeList[index].minorNm;

        // 추가 조건: minorCd가 '0006'이고 checkYn이 'N'일 때 additionalInfo 초기화
        if (state.workTypeList[index].minorCd === '0006' && state.workTypeList[index].checkYn === 'N') {
            state.workTypeList[index].additionalInfo = null; // 기존 inputForm 대신 workTypeList를 직접 수정
        }
    };

    const toggleAccordion = async event => {
        const button = event.currentTarget;
        const segmentElement = button.nextElementSibling;

        const isOpen = button.classList.toggle('active');

        await nextTick();

        gsap.to(segmentElement, {
            height: isOpen ? 'auto' : 0,
            duration: 0.5,
            ease: 'customEase'
        });
    };

    return {
        inputForm,
        originData,
        initInputForm,
        checkedList,
        fileList,
        files,
        btnDelete,
        filter,
        dataFilter,
        searchText,
        signature,
        toggleUseYn,
        searchParam,
        state,
        currentDate,
        segments,
        activeSegments,
        toggleCheckYn,
        setRefs,
        //버튼
        btnAdd,
        goBack,
        goDetail,
        btnSave,
        btnDetailDelete,
        btnDownload,
        btnDetailDownload,
        //API
        getPermitToWorkList,
        getPermitToWorkDetailList,
        btnSavePermitToWork,
        delPermitToWorkList,
        delPermitToWork,
        toggleAccordion,

        // 작업종류 시스템 코드에서 가져오기
        getWorkTypeList
    };
});
