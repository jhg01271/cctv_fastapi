import { defineStore } from 'pinia';
import { computed, nextTick } from 'vue';
import router from '@/router';
import BaseView from '@/components/base/BaseView';

const { ref, toastPopup, alertMsg, confirmMsg, getCompId, getCurrentDate, t, formatToAmt, baseDownload, openLoading, endLoading } = BaseView();
import { getActPlanMaster } from '@/stores/safewiz/participation/api/actionPlanApi';
import { getActionMonthly, getActionMonthlyDetail, saveActionDetailItem, saveActionMonthly, deleteDetailItems, deleteActionMonthlys, getDetailedActionPlanReport } from '@/stores/safewiz/participation/api/detailedActionApi';
import { getDetailItem } from '@/stores/safewiz/participation/api/detailedActionApi.js';
import { useButtonListStore } from '@/stores/buttonList';

import { gsap } from 'gsap';

const layoutStore = useButtonListStore();

layoutStore.useBtnList = ['btnSearch', 'btnBack', 'btnAdd', 'btnSave', 'btnDelete'];

export const useDetailedActionStore = defineStore('DetailedAction', () => {
    const actPlanList = ref([]);
    const cardList = ref([]);
    const checkedList = ref([]);

    // 서명 컴포넌트
    const signature = ref(null);

    const writeYear = ref(null);

    const popupNm = ref('');

    // 아코디언 순서
    const accordionIdx = ref(null);

    //상위 조직
    const compId = getCompId();
    const param = ref({});
    const actionKey = ref('');
    //현재날짜
    const currentDate = ref(getCurrentDate());

    //사인컴포넌트
    const signCmd = ref('I');

    const inputForm = ref({});
    const initInputForm = () => {
        inputForm.value = {
            cmd: 'I',
            cmdArray: '',
            compId: compId,
            performanceType: '',
            performanceRepeat: '',
            actionObjective: '',
            actionSchedule1: 'N',
            actionSchedule2: 'N',
            actionSchedule3: 'N',
            actionSchedule4: 'N',
            actionSchedule5: 'N',
            actionSchedule6: 'N',
            actionSchedule7: 'N',
            actionSchedule8: 'N',
            actionSchedule9: 'N',
            actionSchedule10: 'N',
            actionSchedule11: 'N',
            actionSchedule12: 'N',
            budget: '',
            companyObjective: '',
            orgnId: '',
            orgnList: [],
            orgnNm: '',
            upPrntobjectiveId: '',
            prntobjectiveId: '',
            updatedAt: null,
            updatedBy: null,
            createdAt: null,
            createdBy: null,
            checked: '',
            useYn: 'Y',
            writeYear: param.writeYear,
            docType: 'OBJ',
            docNo: param.docNo
        };
    };

    //-----------------------------------------------//마스터 조회

    const getActPlanMasterList = notify => {
        return new Promise(resolve => {
            getActPlanMaster(searchParam.value, notify).then(res => {
                if (res && res.success) {
                    inputForm.value = res.list[0];
                    if (res.list.length > 0) {
                        searchParam.value.docNo = res.list[0].docNo;
                        searchParam.value.docSeq = res.list[0].docSeq;
                        getActionMonthlyList(true);
                        resolve({writeYear: searchParam.value.writeYear, docNo: searchParam.value.docNo});
                    } else {
                        dataFilterList.value = null;
                    }
                }
            });
        });
    };

    //-----------------------------------------------//계획 조회
    const getActionMonthlyList = notify => {
        return getActionMonthly(searchParam.value, notify).then(res => {
            if (res && res.success) {
                if (res.list.length < 1) {
                    layoutStore.useBtnList = ['btnSearch', 'btnBack'];
                    return;
                }

                searchTerm.value = '';
                const hasDetailPlan = res.list.some(item => item.detailItem);

                // 조건에 따라 signCmd 설정
                signCmd.value = hasDetailPlan ? 'U' : 'I';
                actPlanList.value = res.list;

                const groupedByCompany = {};
                res.list.forEach(item => {
                    const companyObjective = item.companyObjective;

                    if (!groupedByCompany[companyObjective]) {
                        groupedByCompany[companyObjective] = {
                            name: companyObjective,
                            data: [],
                            totalAmt: 0 // budget 합산 필드
                        };
                    }
                    // 목표 일정 (분기) 계산
                    const schedule = [];

                    if (item.actionSchedule1 === 'Y') schedule.push('1월');
                    if (item.actionSchedule2 === 'Y') schedule.push('2월');
                    if (item.actionSchedule3 === 'Y') schedule.push('3월');
                    if (item.actionSchedule4 === 'Y') schedule.push('4월');
                    if (item.actionSchedule5 === 'Y') schedule.push('5월');
                    if (item.actionSchedule6 === 'Y') schedule.push('6월');
                    if (item.actionSchedule7 === 'Y') schedule.push('7월');
                    if (item.actionSchedule8 === 'Y') schedule.push('8월');
                    if (item.actionSchedule9 === 'Y') schedule.push('9월');
                    if (item.actionSchedule10 === 'Y') schedule.push('10월');
                    if (item.actionSchedule11 === 'Y') schedule.push('11월');
                    if (item.actionSchedule12 === 'Y') schedule.push('12월');

                    // 2. budget 값 합산
                    groupedByCompany[companyObjective].totalAmt += item.budget;
                    groupedByCompany[companyObjective].data.push(item);
                });

                // 2. 각 companyObjective 그룹에 대해 actionObjective로 그룹화
                Object.values(groupedByCompany).forEach(companyGroup => {
                    const groupedByActionObjective = {};

                    companyGroup.data.forEach(item => {
                        const actionObjective = item.actionObjective;

                        if (!groupedByActionObjective[actionObjective]) {
                            groupedByActionObjective[actionObjective] = {
                                name: actionObjective,
                                totalAmt1: 0,
                                data: []
                            };
                        }
                        groupedByActionObjective[actionObjective].data.push(item);
                    });

                    companyGroup.data = Object.values(groupedByActionObjective);
                });
                // 3. 각 actionObjective 그룹에 대해 detailItemId로 그룹화
                Object.values(groupedByCompany).forEach(companyGroup => {
                    companyGroup.data.forEach(actionGroup => {
                        const groupedByDetailItemId = {};

                        actionGroup.data.forEach(item => {
                            const detailItemId = item.detailItemId;
                            const transformedItem = {
                                ...item,
                                detail: {
                                    ['세부 계획']: item.detailItemCnt,
                                    [t('objectives_budget_won')]: formatToAmt(item.budget) === '0' ? '-' : formatToAmt(item.budget) + '만원'
                                }
                            };

                            if (!groupedByDetailItemId[detailItemId]) {
                                groupedByDetailItemId[detailItemId] = {
                                    name: item.detailItem,
                                    data: [],
                                    detail: {}, // 객체로 초기화
                                    totalAmt: 0 // 초기 값 설정
                                };
                            }
                            groupedByDetailItemId[detailItemId].totalAmt += item.budget;
                            groupedByDetailItemId[detailItemId].detail['세부 계획'] = item.detailItemCnt + '건';
                            groupedByDetailItemId[detailItemId].detail[t('objectives_budget_won')] = formatToAmt(groupedByDetailItemId[detailItemId].totalAmt) === '0' ? '-' : formatToAmt(groupedByDetailItemId[detailItemId].totalAmt) + '만원';
                            groupedByDetailItemId[detailItemId].data.push(transformedItem);
                        });
                        actionGroup.data = Object.values(groupedByDetailItemId);
                        actionGroup.totalAmt1 = actionGroup.data.reduce((sum, detailGroup) => sum + detailGroup.totalAmt, 0);
                    });
                });

                // 11. 최종적으로 dataFilterList와 cardList에 배열 형식으로 저장
                dataFilterList.value = Object.values(groupedByCompany);
                cardList.value = Object.values(groupedByCompany);
                initData();
            }
            return res; // res 반환
        });
    };

    const getActionMonthlyDetailList = (param, notify) => {
        return getActionMonthlyDetail(param, notify).then(res => {
            if (res && res.success) {
                inputForm.value = res.list;
            }

            return res;
        });
    };

    //세부항목 삭제
    const deleteDetailItemsList = data => {
        deleteDetailItems(data, true).then(res => {
            if (res && res.success) {
                // 검색어 초기화
                searchTerm.value = '';
                //체크리스트 초기화
                checkedList.value = [];
                getActionMonthlyList(param.value);
            }
        });
    };

    //세부계획 상세화면에서 삭제
    const deleteActionMonthlyLists = data => {
        return new Promise(() => {
            deleteActionMonthlys(data, true).then(res => {
                if (res && res.success) {
                    //리스트 초기화
                    getActionMonthlyDetailList(param.value);
                }
            });
        });
    };
    const saveActionDetailItemAction = data => {
        // console.log('data', data);
        openLoading();
        saveActionDetailItem(data, true)
            .then(res => {
                if (res && res.success) {
                    // 검색어 초기화
                    searchTerm.value = '';
                    //리스트 초기화
                    dataFilterList.value = [];
                    //체크리스트 초기화
                    checkedList.value = [];
                    // insert 된 데이터는 'U'로 변경
                    inputForm.value.forEach(el => {
                        el.cmd = 'U';
                    });

                    if (res.result) {
                        param.value.writeYear = res.result.writeYear;
                        param.value.compId = compId;
                        param.value.docNo = res.result.docNo;
                        param.value.docType = 'OBJ';
                        param.value.docSeq = res.result.docSeq;
                        param.value.detailItemId = res.result.detailItemId;
                    }

                    // back 에서 처리
                    // if (signCmd.value !== 'I') {
                    //     signature.value.setApprovalInfo('OBJP', res.result.writeYear + res.result.docNo);
                    // }

                    layoutStore.useBtnList = ['btnSearch', 'btnBack', 'btnAdd', 'btnDelete', 'btnSave'];
                    getActionMonthlyDetailList(param.value, false);
                    endLoading();
                }
            })
            .catch(() => {
                console.error('error', error);
                endLoading();
            });
    };
    //세부계획 추가
    const saveActionMonthlyList = data => {
        openLoading();
        saveActionMonthly(data, true)
            .then(res => {
                if (res && res.success) {
                    // 검색어 초기화
                    searchTerm.value = '';
                    //리스트 초기화
                    dataFilterList.value = [];
                    //체크리스트 초기화
                    checkedList.value = [];
                    // insert 된 데이터는 'U'로 변경
                    inputForm.value.forEach(el => {
                        el.cmd = 'U';
                    });

                    if (res.result) {
                        param.value.writeYear = res.result.writeYear;
                        param.value.compId = compId;
                        param.value.docNo = res.result.docNo;
                        param.value.docType = 'OBJ';
                        param.value.docSeq = res.result.docSeq;
                        param.value.detailItemId = res.result.detailItemId;
                    }

                    // back 에서 처리
                    // if (signCmd.value !== 'I') {
                    //     signature.value.setApprovalInfo('OBJP', res.result.writeYear + res.result.docNo);
                    // }

                    layoutStore.useBtnList = ['btnSearch', 'btnBack', 'btnAdd', 'btnDelete', 'btnSave'];
                    getActionMonthlyDetailList(param.value, false);
                    endLoading();
                }
            })
            .catch(error => {
                console.error('error ', error);
                endLoading();
            })
            .finally(() => {});
    };

    //-----------------------------------------------

    const checkNumberInput = event => {
        // 입력된 값이 숫자가 아니면 빈 문자열로 변환
        event.target.value = event.target.value.replace(/[^0-9]/g, '');
        // v-model로 바인딩된 값도 업데이트
        inputForm.value.ordSeq = event.target.value;
    };

    //-----------------------------------------------
    //목록으로 이동
    const goBack = () => {
        //검색어 초기화
        searchTerm.value = '';
        router.push({ name: 'HseObjectives' });
    };

    //-----------------------------------------------

    //이동버튼
    const goObject = () => {
        //페이지 이동
        router.push({
            path: 'DetailedActionPlan'
        });
    };

    //-----------------------------------------------

    const searchTerm = ref('');
    const dataFilterList = ref(null);
    const filteredData = ref([]);

    const initData = () => {
        applyFilter(); // 필터를 처음에 적용하여 초기 데이터로 채움
    };

    // 필터 적용 함수
    const applyFilter = () => {
        // 공백 제거된 검색어 생성
        const searchValue = searchTerm.value.replace(/\s+/g, '').toLowerCase();

        const filterItems = dataArray => {
            return dataArray
                .map(item => ({
                    ...item,
                    data: item.data
                        .map(subItem => ({
                            ...subItem,
                            data: subItem.data.filter(innerItem => innerItem.actionObjective.toLowerCase().replace(/\s+/g, '').includes(searchValue) || (innerItem.performanceTypeNm && innerItem.performanceTypeNm.toLowerCase().replace(/\s+/g, '').includes(searchValue)))
                        }))
                        .filter(subItem => subItem.data.length > 0)
                }))
                .filter(item => item.data.length > 0);
        };

        filteredData.value = cardList.value
            .map(el => ({
                ...el,
                data: filterItems(el.data) // 하위 데이터 필터링을 별도 함수로 처리
            }))
            .filter(el => el.data.length > 0); // 최상위 데이터 필터링

        dataFilterList.value = filteredData.value;
        // nextTick을 사용하여 DOM이 업데이트된 후 작업 실행
        nextTick(() => {
            const btn = document.getElementById(`accordion-btn_0`);
            if (btn != null) {
                const isActive = btn.classList.contains('active');

                if (!isActive) {
                    btn.click();
                }
            }
        });
    };

    //-----------------------------------------------
    const detailItemList = ref([]);

    //추가버튼
    const btnAdd = item => {
        //페이지 이동
        router.push({
            path: '/DetailedActionPlanDetail'
        });
        //폼 초기화
        initInputForm();
        inputForm.value = [
            {
                checked: true,
                compId: compId,
                cmd: 'I',
                useYn: 'Y',
                ordSeq: null,
                createdAt: currentDate,
                detailPlan: '',
                // performanceType: '0001',
                // performanceRepeat: '',
                // performanceCnt: '',
                remark: '',
                createdBy: '',
                writeYear: item.data[0].writeYearO,
                docNo: item.data[0].docNoO,
                docType: item.data[0].docTypeO,
                docSeq: item.data[0].docSeqO,
                //공통
                companyObjective: item.data[0].companyObjective,
                actionObjective: item.data[0].actionObjective,
                actionSchedule1O: item.data[0].actionSchedule1O,
                actionSchedule2O: item.data[0].actionSchedule2O,
                actionSchedule3O: item.data[0].actionSchedule3O,
                actionSchedule4O: item.data[0].actionSchedule4O,
                orgnList: item.data[0].orgnList,
                performanceType: item.data[0].performanceTypeO,
                performanceRepeat: item.data[0].performanceRepeatO,
                performanceCnt: item.data[0].performanceCntO,
                performanceCustom: item.data[0].performanceCustomO
            }
        ];

        fetchDetailItems(item.data[0]).then(responses => {
            detailItemList.value = responses[0].list;
        });
    };

    const fetchDetailItems = item => {
        return Promise.all([
            getDetailItem({
                writeYear: item.writeYearO,
                compId: compId,
                docNo: item.docNoO,
                docType: item.docTypeO,
                docSeq: item.docSeqO
            })
        ]);
    };

    //-----------------------------------------------

    //저장
    const btnSave = async () => {
        //세부추진 계획 입력중에 유효성 체크
        let hasInvalidSchedule = false;
        inputForm.value
            .filter(detailItem => detailItem.checked)
            .forEach(detailItem => {
                const hasValidSchedule = [detailItem.actionSchedule1, detailItem.actionSchedule2, detailItem.actionSchedule3, detailItem.actionSchedule4, detailItem.actionSchedule5, detailItem.actionSchedule6, detailItem.actionSchedule7, detailItem.actionSchedule8, detailItem.actionSchedule9, detailItem.actionSchedule10, detailItem.actionSchedule11, detailItem.actionSchedule12].includes('Y'); // 'Y'가 하나라도 있으면 true

                if (!hasValidSchedule) {
                    hasInvalidSchedule = true; // 유효하지 않은 항목이 있음을 표시
                }
            });

        if (inputForm.value[0].cmd === 'U' && hasInvalidSchedule) {
            alertMsg('warning', `추진일정을 입력해주세요.`);
            return; // 저장 중단
        }

        if (inputForm.value.cmd === 'I') {
            // detailItemList의 길이가 0보다 큰지 확인
            if (detailItemList.value.length > 0) {
                // 배열을 순회하면서 비교
                const isDuplicate = detailItemList.value.some(el => el.detailItem === inputForm.value.detailItem.trim());

                // 동일한 세부항목이 있으면 팝업 출력 후 저장 중단
                if (isDuplicate) {
                    alertMsg('warning', '동일한 세부항목이 있습니다.');
                    return; // 저장 중단
                }
            }
        }
        inputForm.value.forEach(el => {
            // 첫번째값에 세팅되어 있는 정보 하위에 넣어줌
            el.docNo = inputForm.value[0].docNo;
        });
        const checkList = inputForm.value.filter(el => el.checked === true);
        checkList.forEach(el => {
            if (!el.ordSeq) {
                el.ordSeq = 99;
            }
            el.detailItem = inputForm.value[0]?.detailItem;
        });
        console.log('저장값', checkList);
        if (checkList.length == 0 && inputForm.value[0].cmd === 'U') {
            // 세부항목만 변경할 때
            confirmMsg('info', t('msgSave'), '', {
                fun: saveActionDetailItemAction,
                param: inputForm.value[0]
            });
            return;
        }

        confirmMsg('info', t('msgSave'), '', {
            fun: saveActionMonthlyList,
            param: checkList
        });
    };

    //-----------------------------------------------

    //일괄 삭제
    const btnDelete = async item => {
        const deleteList = [];
        if (item == 'D') {
            inputForm.value.forEach(el => {
                if (el.checked == true) {
                    deleteList.push(el);
                }
            });
            //개별삭제
            confirmMsg('warning', t('msgDelete'), '', {
                fun: deleteActionMonthlyLists,
                param: deleteList
            });
        } else {
            let param = checkedList.value; // rowKey로 행 데이터를 가져옴

            if (!param.length) {
                alertMsg('warning', t('msgNoItem'));
                return;
            }
            if (param.some(el => el.useYn === 'N')) {
                alertMsg('warning', t('msgDeletedItem'));
                return;
            }
            confirmMsg('warning', t('msgDelete'), ``, {
                fun: deleteDetailItemsList,
                param: param
            });
        }
    };

    //-----------------------------------------------

    const searchParam = ref({
        compId: compId,
        searchText: '',
        writeYear: getCurrentDate().substring(0, 4),
        docType: 'OBJ',
        docNo: null,
        docSeq: null
    });

    //-----------------------------------------------
    // 추가
    const addPlan = () => {
        inputForm.value.push({
            checked: true,
            compId: compId,
            cmd: 'I',
            useYn: 'Y',
            ordSeq: null,
            createdAt: currentDate,
            detailPlan: '',
            // performanceType: '0001',
            // performanceRepeat: '',
            // performanceCnt: '',
            remark: '',
            createdBy: '',
            writeYear: searchParam.value.writeYear,
            docNo: searchParam.value.docNo,
            docType: 'OBJ',
            docSeq: inputForm.value[0].docSeq,
            //공통
            companyObjective: inputForm.value[0].companyObjective, //목표
            actionObjective: inputForm.value[0].actionObjective, //중점 추진 목표
            detailItemId: inputForm.value[0].detailItemId, // 세부항목 id
            detailItem: inputForm.value[0].detailItem, // 세부항목
            actionSchedule1O: inputForm.value[0].actionSchedule1O,
            actionSchedule2O: inputForm.value[0].actionSchedule2O,
            actionSchedule3O: inputForm.value[0].actionSchedule3O,
            actionSchedule4O: inputForm.value[0].actionSchedule4O,
            orgnList: inputForm.value[0].orgnList,
            performanceType: inputForm.value[0].performanceTypeO,
            performanceRepeat: inputForm.value[0].performanceRepeatO,
            performanceCnt: inputForm.value[0].performanceCntO,
            performanceCustom: inputForm.value[0].performanceCustomO
        });
    };

    //-----------------------------------------------

    //-----------------------------------------------
    // 삭제
    const removePlan = index => {
        inputForm.value.splice(index, 1);
    };

    //-----------------------------------------------

    // [목표 필터]

    const objectList = ref([]);
    const filteredObjectList = computed(() => {
        let list = objectList.value.filter(element => {
            if (element.compObjectiveId === '') return true;
            return element.compObjectiveId !== objectList.value.companyObjective;
        });
        return list;
    });
    //-----------------------------------------------
    //-----------------------------------------------
    // [추진 목표 리스트 조회]

    const actionObjectList = ref([]);
    // filteredActionObjectList를 computed 속성으로 정의합니다.
    const filteredActionObjectList = computed(() => {
        // compObjectiveId가 없으면 objectList의 첫 번째 compObjectiveId를 할당
        if (!inputForm.value.compObjectiveId) {
            inputForm.value.compObjectiveId = objectList.value.length > 0 ? objectList.value[0].compObjectiveId : null;
        }

        // actionObjectList가 비어 있으면 빈 배열 반환
        if (actionObjectList.value.length === 0) {
            return [];
        }

        // compObjectiveId가 일치하는 값만 필터링
        return actionObjectList.value.filter(element => element.compObjectiveId === inputForm.value.compObjectiveId);
    });
    //-----------------------------------------------
    //-----------------------------------------------
    // [세부 항목 리스트 필터]

    const detailObjectList = ref([]);

    const filteredDetailObjectList = computed(() => {
        let list = detailObjectList.value.filter(element => {
            if (element.detailItemId === '') return true;
            return element.detailItemId !== detailObjectList.value.detailItem;
        });
        return list;
    });

    //-----------------------------------------------

    //-----------------------------------------------
    // [성과구분 필터]

    const gubunList = ref([]);
    const filteredGubunList = computed(() => {
        let list = gubunList.value.filter(element => {
            if (element.minorCd === '') return true;
            return element.minorCd !== gubunList.value.minorNm;
        });
        return list;
    });

    //-----------------------------------------------
    // [성과반복 필터]

    const repeatList = ref([]);
    const filteredRepeatList = computed(() => {
        let list = repeatList.value.filter(element => {
            if (element.minorCd === '') return true;
            return element.minorCd !== repeatList.value.minorNm;
        });
        return list;
    });

    //-----------------------------------------------
    //성과구분 이벤트
    const changeHandle = (item, index) => {
        // 수정해야함
        if (item !== '0001') {
            //기간별 아니면 반복 초기화
            inputForm.value[index].performanceRepeat = '';
        }
        if (item !== '0001' && item !== '0002') {
            //기간별 ,작업별 아니면 건수 초기화
            inputForm.value[index].performanceCnt = '';
        }
        if (item !== '0000') {
            //직접입력 아니면 입력값 초기화
            inputForm.value[index].performanceCustom = '';
        }
    };

    // 중점추진사항별 세부계획 레포트 출력
    const btnDetailDownload = () => {
        let dataList = [];
        if (dataFilterList.value !== null && checkedList.value.length === 0) {
            // 아무것도 선택하지 않고 출력 시 use_yn 값이 Y인 항목 전체 출력
            dataFilterList.value.forEach(el => {
                el.data.forEach(mData => {
                    mData.data.forEach(dData => {
                        dataList = [...dataList, ...dData.data.filter(d => d.useYn === 'Y')];
                    });
                });
            });
            confirmMsg('info', t('msgPrint'), null, { fun: downloadReports, param: dataList });
        } else {
            dataList = checkedList.value;
            confirmMsg('info', t('msgCheckedPrint'), null, { fun: downloadReports, param: dataList });
        }
    };

    const downloadReports = dataList => {
        let checkedListParam = dataList;
        let checkedYn = true;
        if (dataList.length === 0) {
            checkedListParam = actPlanList.value;
            checkedYn = false;
        }

        const checkDocSeqList = checkedListParam.map(({ writeYear, docNo, docSeq, docDetailSeq }) => `${writeYear}${docNo}${docSeq}${docDetailSeq}`);

        baseDownload(getDetailedActionPlanReport, {
            compId: compId,
            writeYear: searchParam.value.writeYear,
            checkedList: checkDocSeqList,
            checkedPrint: checkedYn
        });
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

    //-----------------------------------------------

    return {
        toggleAccordion,
        initInputForm,
        inputForm,
        param,
        accordionIdx,
        actionKey,
        writeYear,
        initData,
        actPlanList,
        searchParam,
        checkedList,
        searchTerm,
        applyFilter,
        dataFilterList,
        filteredGubunList,
        gubunList,
        filteredRepeatList,
        repeatList,
        filteredObjectList,
        objectList,
        filteredDetailObjectList,
        detailObjectList,
        filteredActionObjectList,
        actionObjectList,
        addPlan,
        removePlan,
        changeHandle,
        signCmd,
        //라우터
        btnAdd,
        goBack,
        goObject,
        //버튼
        btnSave,
        btnDelete,
        btnDetailDownload,
        //api
        getActPlanMasterList,
        getActionMonthlyList,
        getActionMonthlyDetailList,
        deleteDetailItemsList,
        deleteActionMonthlyLists,
        saveActionMonthlyList,
        currentDate,
        checkNumberInput,
        popupNm,
        signature
    };
});
