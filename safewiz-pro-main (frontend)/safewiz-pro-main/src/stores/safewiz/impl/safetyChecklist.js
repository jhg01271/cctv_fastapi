import { defineStore } from 'pinia';

import router from '@/router';
import BaseView from '@/components/base/BaseView';
const { nextTick, openLoading, endLoading, ref, computed, alertMsg, baseDownload, confirmMsg, t, getCompId, getCurrentDate, formatDate } = BaseView();
import _ from 'lodash';
import { gsap } from 'gsap';

import { useButtonListStore } from '@/stores/buttonList';
const layoutStore = useButtonListStore();
import { usePlanningCtrlStore } from '@/stores/safewiz/impl/planningAndControl.js';
const planningCtrlStore = usePlanningCtrlStore();

import { getSafetyChecklist, getSafetyChecklistDetail, hasSafetyChecklist, saveSafetyChecklist, deleteSafetyChecklist, deleteSafetyChecklistDetail } from './api/safetyChecklistApi';

import { getSafetyInspectionLog, saveSafetyInspectionLog, deleteSafetyInspectionLog, getSafetyInspectionLogReport } from './api/safetyInspectionLogApi';

export const useSafetyChecklistStore = defineStore('safetyChecklist', () => {
    const compId = getCompId();
    const writeYear = ref(getCurrentDate().substring(0, 4));
    const checkedList = ref([]);
    const safetyHealthInfoList = ref({});
    const currentDate = ref(getCurrentDate());
    //버튼 리스트
    const buttonList = ref([]);
    const originData = ref({});

    //검색 기능
    const searchTerm = ref('');
    const dataFilterList = ref(null);
    const searchParam = ref({
        compId: getCompId(),
        writeYear: '',
        docType: '',
        docNo: ''
    });

    const searchParam2 = ref({
        writeYear: null,
        stdEqId: null,
        stdEqNm: null,
        equipmentId: null,
        equipmentNm: null,
        checkDt: null,
        docNo: null
    });

    // const signCmd = ref();
    const inspector = ref('');
    const setRefs = inspectorComponent => {
        inspector.value = inspectorComponent.value;
    };

    const initData = () => {
        // 여기서 API 호출을 통해 데이터를 가져올 수 있습니다.
        applyFilter(); // 필터를 처음에 적용하여 초기 데이터로 그리드를 채움
    };
    // 초기화
    const inputForm = ref({});
    const initInputForm = async year => {
        inputForm.value = {
            cmd: 'I',
            compId: compId, // 사업장 ID
            writeYear: year ? year : getCurrentDate().substring(0, 4), //작성년도
            docType: 'SCL',
            docNo: '',
            stdEq: [],
            stdEqId: '',
            stdEqNm: '',
            title: '',
            inspectionCycle: '',
            itemList: [],
            detailList: null,
            useYn: 'Y',
            updatedAt: '',
            updatedBy: '',
            createdAt: currentDate.value,
            createdBy: '' // 수정일시
        };
        // signCmd.value = 'I'
    };

    const currentTime = computed(() => {
        const now = new Date();
        const hours = now.getHours().toString().padStart(2, '0'); // 시: 두 자리 포맷
        const minutes = now.getMinutes().toString().padStart(2, '0'); // 분: 두 자리 포맷
        return `${hours}:${minutes} ~ ${hours}:${minutes}`; // HH:mm 형식
    });

    const segments = ref([]);
    const activeSegments = ref({});
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

    //안전관리 리스트 조회
    const searchSafetyChecklist = notify => {
        openLoading();
        safetyHealthInfoList.value = [];
        return new Promise(resolve => {
            getSafetyChecklist(searchParam.value, notify)
                .then(res => {
                    if (res && res.list) {
                        for (var data of res.list) {
                            data.detail = {
                                [t('typeofEquipment')]: data.stdEqNm,
                                [t('createdAt')]: formatDate(data.createdAt),
                                [t('safety_itemCnt')]: data.itemCnt,
                                [t('safety_detailCnt')]: data.listCnt
                            };
                            // const index = safetyHealthInfoList.value.findIndex(el => el.year === data.writeYear);
                            // if (index < 0) {
                            //     safetyHealthInfoList.value.push({ year: data.writeYear, dataList: [data] });
                            // } else {
                            //     safetyHealthInfoList.value[index].dataList.push(data);
                            // }
                        }
                        safetyHealthInfoList.value = res.list;
                        // if(safetyHealthInfoList.value.length === 0){
                        //     const currentYear = String(new Date().getFullYear());
                        //     safetyHealthInfoList.value.push({ year: currentYear });
                        // }
                        applyFilter();
                        resolve({ result: res.result, success: res.success });
                    }
                })
                .catch(() => {
                    endLoading();
                })
                .finally(() => {
                    nextTick(() => {
                        const btn = document.getElementById(planningCtrlStore.searchParam.searchText);
                        if (btn != null) {
                            const isActive = btn.classList.contains('active');

                            if (!isActive) {
                                btn.click();
                            }
                        }
                    });
                    endLoading();
                });
        });
    };

    // 필터 적용 함수
    const applyFilter = () => {
        dataFilterList.value = _.cloneDeep(safetyHealthInfoList.value);
        safetyHealthInfoList.value.forEach((el, index) => {
            const filteredData = el.filter(item => item.stdEqNm?.toLowerCase().includes(searchTerm.value.toLocaleLowerCase()) || item.createdAt?.toLowerCase().includes(searchTerm.value.toLocaleLowerCase()) );
            dataFilterList.value = filteredData ?? [];
        });
    };

    //안전점검표 상세조회
    const getChecklistDetail = (param,notify) => {
        openLoading();
        return getSafetyChecklistDetail(param, notify)
            .then(res => {
                if (res && res.success) {
                    //점검항목 list
                    yesNoList.value = res.list.itemList
                        .filter(el => el.useYn === 'Y')
                        .map(el => ({
                            checkItemSeq: el.docSeq,
                            checkItemNm: el.checkItem
                        }));
                    res.list.itemList.forEach(el => {
                        el.itemCmd = 'U';
                    });
                    inputForm.value = res.list;
                    originData.value = _.cloneDeep(res.list);
                    console.log('상세', inputForm.value);
                }
                return res; // res 반환
            })
            .finally(() => endLoading());
    };

    //안전점검표 상세조회
    const getSafetyCheckYn = (param, notify) => {
        openLoading();
        return hasSafetyChecklist(param, notify)
            .then(res => {
                if (res && res.success) {
                    if (res.list) {
                        alertMsg('warning', t('이미 등록된 데이터가 존재합니다.'));
                        inputForm.value.stdEqId = null;
                        inputForm.value.stdEqNm = null;
                        return;
                    }
                    console.log('데이터유무', res.list);
                }
                return res; // res 반환
            })
            .finally(() => endLoading());
    };

    //안전점검표 일괄 삭제
    const deleteSafetyCheck = (param) => {
        openLoading();
        return new Promise(resolve => {
            deleteSafetyChecklist(param, false)
                .then(res => {
                    console.log(res);
                    resolve({ result: res.result, success: res.success });
                    searchSafetyChecklist(true);
                })
                .finally(() => endLoading());
        });
    };

    // 안전점검표 상세 삭제
    const detailDelete = data => {
        openLoading();
        return new Promise(resolve => {
            deleteSafetyChecklistDetail(data, true)
                .then(res => {
                    resolve({ result: res.result, success: res.success });
                    getChecklistDetail(searchParam.value,false);
                })
                .finally(() => endLoading());
        });
    };

    // 수정, 저장
    const saveChecklist = param => {
        return new Promise(resolve => {
            param.itemList.forEach(el => {
                if (!el.ordSeq) {
                    el.ordSeq = 99;
                }
            });
            param.detailList.forEach(el => {
                if (!el.ordSeq) {
                    el.ordSeq = 99;
                }
            });
            // inspectionCycle === '0001' - 일별
            // inspectionCycle === '0002' - 월별
            console.log('######### param ', param);
            openLoading();
            if(param.ordSeq === null) {
                param.ordSeq = 99;
            }
            saveSafetyChecklist(param, true)
                .then(res => {
                    resolve({ result: res.result, success: res.success });
                    if (res.result) {
                        searchParam.value = {
                            docNo: res.result.docNo,
                            writeYear: res.result.writeYear,
                            compId: res.result.compId,
                            docType: res.result.docType,
                            searchText: res.result.stdEqId
                        };
                    }
                    getChecklistDetail(searchParam.value,false)
                })
                .finally(() => {
                    endLoading();
                    layoutStore.useBtnList = ['btnBack', 'btnSearch', 'btnSave', 'btnDelete', 'btnCopy'];
                });
        });
    };

    //목록으로 이동
    const goBack = () => {
        const toRouter = {
            name: 'SafetyMgmtGuidelines',
            state: {
                activeTab: 'info',
            }
        };
        router.push(toRouter);
    };

    //-----------------------------------------------------------------------

    //추가버튼
    const btnCopy = param => {
        layoutStore.useBtnList = ['btnBack', 'btnSave'];
        console.log('param', param);
        param.historyList = []
        param.stdEqId = '';
        param.stdEqNm = '';
        param.cmd = 'I';
        param.itemList.forEach(el => {
            el.cmd = 'I';
            inputForm.value = param;
            //페이지 이동
            router.push({
                path: 'SafetyChecklistDetail'
            });
        });
    };

    //추가버튼
    const btnAdd = () => {
        layoutStore.useBtnList = ['btnBack', 'btnSave'];
        initInputForm(getCurrentDate().substring(0, 4));

        //페이지 이동
        router.push({
            path: 'SafetyChecklistDetail'
        });
    };

    // 상세보기 버튼
    const btnDetail = async e => {
        searchParam.value.docNo = e.docNo;
        searchParam.value.compId = e.compId;
        searchParam.value.writeYear = e.writeYear;
        searchParam.value.docType = 'SCL';
        await getChecklistDetail(searchParam.value,true)
        //페이지 이동
        router.push({
            path: 'SafetyChecklistDetail'
        });
    };

    //점검항목 추가
    const itemAdd = () => {
        console.log('추가');
        let item = {
            checked: true,
            checkItem: '',
            ordSeq: null,
            useYn: 'Y',
            cmd: 'I',
            writeYear: searchParam.value.writeYear
        };
        inputForm.value.itemList.push(item);
    };
    //점검사항 추가
    const detailAdd = () => {
        if (yesNoList.value.length < 1) {
            console.log('점검항목이 없습니다.');
            return;
        }
        let item = {
            checked: true,
            checkItemSeq: inputForm.value.itemList[0].docSeq,
            checkList: '',
            ordSeq: null,
            itemUseYn: 'Y',
            cmd: 'I',
            writeYear: searchParam.value.writeYear
        };
        console.log('추가', inputForm.value.itemList[0]);
        inputForm.value.detailList.push(item);
    };

    //저장
    const btnSave = async () => {
        const newInputForm = {
            ...inputForm.value, // 기존 필드를 복사
            itemList: [], // 새로 정의할 itemList
            detailList: [] // 새로 정의할 detailList
        };

        // 선택된 항목만 저장 checked가 true인 항목을 추가
        if (inputForm.value.itemList?.length > 0) {
            newInputForm.itemList = [...inputForm.value.itemList.filter(item => item.checked)];
        }

        if (inputForm.value.detailList?.length > 0) {
            newInputForm.detailList = [...inputForm.value.detailList.filter(item => item.checked)];
        }

        //임시 작성년도 포맷 변경
        newInputForm.writeYear = inputForm.value.createdAt.substring(0, 4);
        if (newInputForm.cmd == 'U') {
            if (newInputForm.itemList.length) {
                newInputForm.itemList.forEach(el => {
                    el.writeYear = inputForm.value.writeYear;
                    el.compId = inputForm.value.compId;
                    el.docType = inputForm.value.docType;
                    el.docNo = inputForm.value.docNo;
                });
            }
            if (newInputForm.detailList.length) {
                newInputForm.detailList.forEach(el => {
                    el.writeYear = inputForm.value.writeYear;
                    el.compId = inputForm.value.compId;
                    el.docType = inputForm.value.docType;
                    el.docNo = inputForm.value.docNo;
                });
            }
        }

        confirmMsg('warning', t('msgSave'), '', {
            fun: saveChecklist,
            param: newInputForm
        });
    };

    const btnDetailDelete = async () => {
        const itemList = inputForm.value.itemList.filter(item => item.checked);
        const detailList = inputForm.value.detailList.filter(item => item.checked);

        if (itemList.length === 0 && detailList.length === 0) {
            alertMsg('warning', t('msgNoItem'));
            return;
        }

        // 가공된 데이터 초기화
        const modifiedInputForm = {
            itemList: inputForm.value.itemList ? itemList : [],
            detailList: inputForm.value.detailList ? detailList : []
        };

        // itemList의 길이가 1 이상일 경우 추가 메시지 설정
        let additionalMessage = '';
        if (modifiedInputForm.itemList.length > 0) {
            additionalMessage = '항목 삭제시 해당 점검사항도 사용할 수 없습니다.';
        }

        // 삭제 확인 메시지 호출
        confirmMsg('warning', t('msgDelete')+"\n" + additionalMessage, '', {
            fun: detailDelete,
            param: modifiedInputForm
        });
    };

    //삭제
    const btnDelete = async () => {
        let param = [];
        dataFilterList.value.forEach(el => {
            param = [...param, ...el.dataList.filter(item => item.checked)]; // rowKey로 행 데이터를 가져옴
        });
        if (!param.length) {
            alertMsg('warning', t('msgNoItem'));
            return;
        }
        console.log('삭제', param);
        confirmMsg('warning', t('msgDelete'), '', {
            fun: deleteSafetyCheck,
            param: param
        });
    };

    // 사용 미사용(useYn) 체크 핸들러 통합
    const toggleUseYn = event => {
        // 체크박스의 체크 상태에 따라 'Y' 또는 'N'을 설정합니다.
        inputForm.value.useYn = event.target.useYn ? 'Y' : 'N';
    };

    //-----------------------------------------------

    //점검항목 필터
    const yesNoList = ref([]);

    const filteredYN = computed(() => {
        let list = yesNoList.value.filter(element => {
            if (element.yesNoList === '') return true;
            return element.checkItemSeq !== yesNoList.value.checkItemNm;
        });
        return list;
    });

    // -------------------------------------------------

    return {
        initInputForm,
        inputForm,
        safetyHealthInfoList,
        buttonList,
        searchParam,
        toggleUseYn,
        itemAdd,
        detailAdd,
        checkedList,
        initData,
        searchTerm,
        dataFilterList,
        applyFilter,
        currentDate,
        originData,
        filteredYN,
        searchParam2,
        setRefs,
        writeYear,
        goBack,
        toggleAccordion,
        segments,
        activeSegments,
        // signCmd,
        currentTime,
        //버튼1
        btnAdd,
        btnDelete,
        btnDetailDelete,
        btnSave,
        btnDetail,
        //안전점검표 api
        searchSafetyChecklist,
        getChecklistDetail,
        getSafetyCheckYn,
        detailDelete,
        btnCopy
        //버튼2
        // btnInspDelete,
        // btnDownload,
        //안전점검일지 api
        // getSafetyInspection,
        // saveSafetyInspection,
        // deleteSafetyInspection
    };
});
