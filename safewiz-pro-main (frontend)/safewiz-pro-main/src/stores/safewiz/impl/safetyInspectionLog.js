import { defineStore } from 'pinia';

import router from '@/router';
import BaseView from '@/components/base/BaseView';
const { nextTick, openLoading, endLoading, ref, computed, alertMsg, baseDownload, confirmMsg, t, getCompId, getCurrentDate, formatDate, formatDateForDB } = BaseView();
import _ from 'lodash';
import { gsap } from 'gsap';

import { useButtonListStore } from '@/stores/buttonList';
const layoutStore = useButtonListStore();
import { getSafetyInspectionLogList, getSafetyInspectionLog, saveSafetyInspectionLog, deleteSafetyInspectionLog, getSafetyInspectionLogReport, getSafetyInspectionLogReportList } from './api/safetyInspectionLogApi';

export const useSafetyInspectionLogStore = defineStore('safetyInspectionLog', () => {
    const writeYear = ref(getCurrentDate().substring(0, 4));
    const checkedList = ref([]);
    const searchTerm = ref('');
    const myDataOnly = ref(true);
    const inputForm = ref({
        cmd: '',
        writeYear: getCurrentDate().substring(0, 4),
        stdEqId: '',
        equipmentId: ''
    });
    const stdEqList = ref([]);

    const inspector = ref('');
    const setRefs = inspectorComponent => {
        inspector.value = inspectorComponent.value;
    };

    // -------------------------------------------------
    const checkedDateList = ref([]);
    const btnSearch = notify => {
        stdEqList.value = [];
        filteredStdEqList.value = [];
        openLoading();
        getSafetyInspectionLogList({ writeYear: writeYear.value, compId: getCompId(), myDataOnly: myDataOnly.value }, notify)
            .then(res => {
                res.list.forEach(el => {
                    if (el.equipmentId) {
                        el.detail = {
                            설비유형: el.stdEqNm,
                            최근점검일: formatDate(el.checkDt),
                            점검주기: el.inspectionCycleNm
                        };
                        el.detailWithBtn = [
                            {
                                // 완료/미완료료
                                label: t('점검현황'),
                                value: el.inspectionYn,
                                button: [
                                    {
                                        value: 'Y',
                                        label: '완료',
                                        class: 'complete'
                                    },
                                    {
                                        value: 'N', // 안전점검일지 항목이 하나라도 Y 혹은 N이 없을 때
                                        label: '미완료',
                                        class: 'ready'
                                    }
                                ]
                            }
                        ];
                        const index = stdEqList.value.findIndex(dt => dt.id === el.stdEqId);
                        if (index < 0) {
                            stdEqList.value.push({ id: el.stdEqId, title: el.stdEqNm, dataList: [el] });
                        } else {
                            stdEqList.value[index].dataList.push(el);
                        }
                    } else {
                        const index = stdEqList.value.findIndex(dt => dt.id === el.stdEqId);
                        if (index < 0) {
                            stdEqList.value.push({ id: el.stdEqId, title: el.stdEqNm });
                        }
                    }
                });
            })
            .catch(() => {
                endLoading();
            })
            .finally(() => {
                filteredStdEqList.value = _.cloneDeep(stdEqList.value);
                endLoading();
            });
    };
    const groupedData = ref([]);
    const btnDetailSearch = async (eq, notify) => {
        try {
            inputForm.value.detailList = [];
            groupedData.value = [];
            const param = { stdEqId: eq.stdEqId, equipmentId: eq.equipmentId, checkDt: formatDateForDB(eq.checkDt) };
            const res = await getSafetyInspectionLog(param, notify);
            if (res && res.success) {
                //점검 항목 데이터 정렬 코드
                let i = 1;
                res.list.detailList.forEach(val => {
                    val.ordSeq = i++;
                });

                const detailList = res.list?.detailList || [];
                const writeYear = res.list?.writeYear;
                res.list.checkDt = formatDate(res.list.checkDt);
                if (!writeYear) {
                    // 조회된 데이터가 없을때 데이터는 초기화하고 detailList 만 넣어줌
                    Object.assign(inputForm.value, {
                        cmd: 'I',
                        checkTime: null,
                        title: null,
                        inspectionCycle: null,
                        checkStart: null,
                        checkEnd: null,
                        setupLocation: res.list.setupLocation,
                        mfSpec: res.list.mfSpec,
                        orgnNm: res.list.orgnList.map(el => el.nm).join(', '),
                        hrId: null,
                        hrNm: null,
                        hrOrgnNm: null,
                        nonComplianceAction: null,
                        signature: null
                    });
                    // detailList 는 전부 'I' 상태로 세팅 후
                    detailList.forEach(el => {
                        el.cmd = 'I';
                    });
                    // inputForm.value.detailList에 할당
                    inputForm.value.detailList = detailList;
                } else {
                    // 조회된 데이터가 있을 때 전체 데이터 할당
                    inputForm.value = res.list;
                }
                groupedData.value = getGroupedData();
            }
            return res; // 결과 반환
        } catch (error) {
            console.error('안전점검 데이터 조회 실패:', error);
            throw error;
        }
    };

    const getGroupedData = () => {
        let globalIndex = 1; // 전역 인덱스를 1로 초기화
        if (inputForm.value.detailList && inputForm.value.detailList[0] == null) {
            return;
        }
        const grouped = inputForm.value.detailList.reduce((acc, item) => {
            const key = item.checkItem;
            if (!acc[key]) {
                acc[key] = [];
            }
            // 전역 인덱스를 항목에 추가
            acc[key].push({ ...item, globalIndex });
            globalIndex++; // 전역 인덱스 증가
            return acc;
        }, {});

        // 객체를 배열로 변환하면서 `itemIndex`를 `globalIndex`로 동기화
        return Object.keys(grouped).map(key => ({
            checkItem: key,
            checkItemNm: grouped[key][0].checkItemNm,
            items: grouped[key].map(item => ({
                ...item,
                itemIndex: item.globalIndex // itemIndex와 globalIndex를 동기화
            }))
        }));
    };

    const btnSave = originData => {
        const param = _.cloneDeep(originData);
        openLoading();
        if (param.checkTime) {
            const [checkStart, checkEnd] = param.checkTime.split(' ~ ').map(time => time.trim());
            param.checkStart = checkStart;
            param.checkEnd = checkEnd;
        }
        return new Promise(resolve => {
            param.checkDt = formatDateForDB(param.checkDt);
            saveSafetyInspectionLog(param, false)
                .then(res => {
                    resolve({ result: res.result, success: res.success });
                    if (res.result) {
                        const param = {
                            writeYear: res.result.writeYear,
                            stdEqId: res.result.stdEqId,
                            equipmentId: res.result.equipmentId,
                            checkDt: res.result.checkDt
                        };
                        // hrchip사인 컴포넌트
                        // inspector.value.setHrChipsApprovalInfo(res.result.writeYear + res.result.docNo);
                        inspector.value.setHrChipsApprovalInfo('SIL', res.result.writeYear, res.result.stdEqId, res.result.equipmentId, formatDateForDB(res.result.checkDt));
                        btnDetailSearch(param, true);
                    }
                })
                .catch(() => {
                    endLoading();
                })
                .finally(() => endLoading());
        });
    };
    const btnDelete = param => {
        openLoading();
        return new Promise(resolve => {
            deleteSafetyInspectionLog(param, true)
                .then(res => {
                    groupedData.value = [];
                    resolve({ result: res.result, success: res.success });
                })
                .finally(() => endLoading());
        });
    };
    // 카드에서 출력
    const btnDownloadList = list => {
        const checkedObjList = list.map(el => ({
            writeYear: writeYear.value,
            searchText: el.stdEqId,
            searchText2: el.equipmentId
        }));
        let searchVO = { writeYear: writeYear.value, checkedObjList: checkedObjList };
        baseDownload(getSafetyInspectionLogReportList, searchVO);
    };
    const btnDownload = param => {
        const checkedObjList = param.map(el => ({
            writeYear: el.writeYear,
            docType: el.docType,
            docNo: el.docNo,
            searchText: el.stdEqId,
            searchText2: el.equipmentId,
            searchText3: formatDateForDB(el.checkDt)
        }));
        let searchVO = { writeYear: param.writeYear, checkedObjList: checkedObjList };
        baseDownload(getSafetyInspectionLogReport, searchVO);
    };

    const activeSegments = ref([]);
    const toggleAccordion = async (event, index) => {
        activeSegments.value[index] = !activeSegments.value[index];
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
    const initAccordion = async () => {
        await btnSearch(true);
        await nextTick();
        setTimeout(() => {
        activeSegments.value.forEach(async (el, index) => {
            if (activeSegments.value[index]) {
                const button = document.getElementById(`accordion-btn_${index}`);
                const segmentElement = button.nextElementSibling;

                const isOpen = true;

                await nextTick();

                gsap.to(segmentElement, {
                    height: isOpen ? 'auto' : 0,
                    duration: 0.5,
                    ease: 'customEase'
                });
                }
            });
        }, 100);
    };

    const filteredStdEqList = ref([]);
    const applyFilter = () => {
        filteredStdEqList.value = _.cloneDeep(stdEqList.value);
        stdEqList.value.forEach((el, index) => {
            const filteredData = el.dataList?.filter(item => item.stdEqNm?.toLowerCase().includes(searchTerm.value.toLocaleLowerCase()) || item.checkDt?.toLowerCase().includes(searchTerm.value.toLocaleLowerCase()) || item.inspectionCycleNm?.toLowerCase().includes(searchTerm.value.toLocaleLowerCase()));
            filteredStdEqList.value[index].dataList = filteredData ?? [];
        });
        filteredStdEqList.value = filteredStdEqList.value.filter(el => el.dataList?.length > 0);
    };
    const changeMyDataOnly = () => {
        myDataOnly.value = !myDataOnly.value;
        btnSearch();
    };
    return {
        checkedList,
        writeYear,
        searchTerm,
        inputForm,
        checkedDateList,
        stdEqList,
        filteredStdEqList,
        groupedData,
        activeSegments,
        myDataOnly,
        //버튼
        btnSearch,
        btnDetailSearch,
        btnSave,
        btnDelete,
        btnDownloadList,
        btnDownload,
        // 함수
        setRefs,
        applyFilter,
        toggleAccordion,
        changeMyDataOnly,
        initAccordion,
        //안전점검일지 api
        getSafetyInspectionLog,
        saveSafetyInspectionLog,
        deleteSafetyInspectionLog,
        getSafetyInspectionLogReport,
        getSafetyInspectionLogReportList
    };
});
