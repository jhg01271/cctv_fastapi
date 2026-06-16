import { defineStore } from 'pinia';
import { computed, nextTick } from 'vue';
import router from '@/router';
import BaseView from '@/components/base/BaseView';
const { ref, openLoading, endLoading, toastPopup, alertMsg, confirmMsg, getCompId, getCurrentDate, t, formatToAmt, baseDownload } = BaseView();

import { getActPlanMaster, getActPlan, getActPlanDetail, getObjective, saveActPlan, deleteActPlan, deleteActPlans, getActionObjectiveReport, getActionObjectiveOrgnList, getSafetyHealthObjOrgnList } from '@/stores/safewiz/participation/api/actionPlanApi';
import _ from 'lodash';
import { gsap } from 'gsap';
import { useButtonListStore } from '@/stores/buttonList';

const layoutStore = useButtonListStore();

// 아코디언 순서
const accordionIdx = ref(null);

export const useActionPlanStore = defineStore('ActionPlan', () => {
    //목표 및 추진사항 및 추진사항 리스트
    const actPlanList = ref([]);
    const cardList = ref([]);
    const originData = ref({});

    // 서명 컴포넌트
    const signature = ref(null);

    const isInputActive = ref(false); // 체크박스 상태

    const newYn = ref('Y');
    const writeYear = ref('');
    //상위 조직
    const compId = getCompId();
    const objectiveId = ref('');

    //사인컴포넌트
    const signCmd = ref('I');
    //현재날짜
    const currentDate = ref(getCurrentDate());

    const actionKey = ref();
    const param = ref();

    // 예산이 이미 등록된 담당조직 리스트
    const actPerformanceOrgnList = ref([]);

    // 안전보건목표가 이미 등록된 담당조직 리스트
    const safetyHealthObjOrgnList = ref([]);

    const inputForm = ref({});
    const initInputForm = () => {
        inputForm.value = {
            cmd: 'I',
            cmdArray: '',
            compId: compId,
            performanceType: '',
            performanceRepeat: '',
            performanceCnt: '',
            performanceCustom: '',
            newYn: 'N',
            actionObjective: '',
            actionSchedule1: 'N',
            actionSchedule2: 'N',
            actionSchedule3: 'N',
            actionSchedule4: 'N',
            budget: '',
            objectiveId: '',
            compObjectiveId: '',
            companyObjective: '',
            orgnId: '',
            orgnNm: '',
            upPrntobjectiveId: '',
            prntobjectiveId: '',
            updatedAt: null,
            updatedBy: null,
            createdAt: currentDate,
            createdBy: null,
            checked: '',
            useYn: 'Y',
            attachId: '', // 첨부 아이디
            deleteFiles: [], //삭제할 파일 id
            writeYear: currentDate.value.slice(0, 4),
            docType: 'OBJ',
            docNo: null
        };
    };

    const targetId = ref(null);
    const initKey = ref({
        writeYear: null,
        docType: 'OBJ',
        docNo: null
    });

    //-----------------------------------------------//조직 조회

    const getActPlanMasterList = notify => {
        return new Promise(resolve => {
            getActPlanMaster(searchParam.value, notify).then(res => {
                if (res && res.success) {
                    if (res.list[0]) {
                        inputForm.value = res.list[0];
                        initKey.value.writeYear = res.list[0].writeYear;
                        initKey.value.docType = res.list[0].docType;
                        initKey.value.docNo = res.list[0].docNo;
                        getActPlanList(false);
                        resolve({writeYear: initKey.value.writeYear, docNo: initKey.value.docNo});
                    } else {
                        dataFilterList.value = null;
                        // initKey 초기화
                        initKey.value.docNo = '';
                        initKey.value.writeYear = '';
                    }
                }
            });
        });
    };

    const getActPlanList = notify => {
        return getActPlan(searchParam.value, notify).then(res => {
            if (res && res.success) {
                if (res.list.length > 0) {
                    signCmd.value = 'U';
                    searchParam.value.writeYear = res.list[0].writeYear;
                    searchParam.value.docNo = res.list[0].docNo;
                } else {
                    layoutStore.useBtnList = ['btnSearch', 'btnBack', 'btnAdd'];
                }
                //targetId 값 세팅
                searchTerm.value = '';
                actPlanList.value = res.list;
                const grouped = {};
                res.list.forEach(item => {
                    const objective = item.compObjectiveId;

                    // 그룹이 없으면 새로 생성
                    if (!grouped[objective]) {
                        grouped[objective] = {
                            name: item.companyObjective, // 그룹화된 이름
                            id: item.compObjectiveId, // 그룹화된 이름
                            data: [], // 값을 담을 배열
                            totalAmt: 0 // cnt 합산 값을 저장할 필드
                        };
                    }
                    grouped[objective].totalAmt += item.useYn === 'Y' ? item.budget : 0; // cnt 값 합산

                    // 목표 및 추진사항일정 (분기) 계산
                    const schedule = [];
                    if (item.actionSchedule1 === 'Y') schedule.push('1분기');
                    if (item.actionSchedule2 === 'Y') schedule.push('2분기');
                    if (item.actionSchedule3 === 'Y') schedule.push('3분기');
                    if (item.actionSchedule4 === 'Y') schedule.push('4분기');

                    const transformedItem = {
                        ...item,
                        detail: {
                            [t('objectives_schedule')]: schedule.join(', ') || '해당없음',
                            [t('objectives_performance')]: item.performanceDiv,
                            [t('objectives_budget_won')]: formatToAmt(item.budget) === '0' ? '-' : formatToAmt(item.budget) + '만원'
                        }
                    };

                    grouped[objective].data.forEach(el => {
                        el.checked = false;
                    });
                    grouped[objective].data.push(transformedItem);
                });
                // 객체를 배열로 변환하여 최종적으로 dataFilterList에 저장
                dataFilterList.value = Object.values(grouped);
                cardList.value = Object.values(grouped);
                initData();
            }
            return res; // res 반환
        });
    };

    const getActPlanDetailList = (param, notify) => {
        return getActPlanDetail(param, notify).then(res => {
            if (res && res.success) {
                // 체크박스값 초기화
                isInputActive.value = false;
                inputForm.value = res.list;
                originData.value = _.cloneDeep(res.list);
            }
            return res; // res 반환
        });
    };

    //전사 목표 및 추진사항 리스트 조회
    const getObjectiveList = notify => {
        return getObjective(notify).then(res => {
            if (res && res.success) {
                targetList.value = res.list;
            }
            return res; // res 반환
        });
    };

    //목표 및 추진사항 개별삭제
    const deleteActPlanList = (data, notify) => {
        deleteActPlan(data, notify).then(res => {
            if (res && res.success) {
                router.push({ name: 'ObjectivesAndActionPlan' });
            }
        });
    };

    //목표 및 추진사항 일괄 삭제
    const deleteActPlanLists = data => {
        return new Promise(() => {
            deleteActPlans(data, true).then(res => {
                if (res && res.success) {
                    // 검색어 초기화
                    searchTerm.value = '';
                    //리스트 초기화
                    dataFilterList.value = [];
                    //체크리스트 초기화
                    checkedList.value = [];
                    getActPlanList(true);
                }
            });
        });
    };

    //목표 및 추진사항 추가
    const saveActPlanList = data => {
        // console.log('#data', data)
        return new Promise(resolve => {
            saveActPlan(data, true)
                .then(res => {
                    if (res.success) {
                        // 검색어 초기화
                        searchTerm.value = '';
                        //리스트 초기화
                        dataFilterList.value = [];
                        //체크리스트 초기화
                        checkedList.value = [];
                        //신규 체크
                        isInputActive.value = false;
                        inputForm.value.newYn = 'Y';
                        // insert 된 데이터는 'U'로 변경
                        inputForm.value.cmd = 'U';
                        if (res.result) {
                            let param = {
                                writeYear: res.result.writeYear,
                                docType: res.result.docType,
                                docNo: res.result.docNo,
                                compId: res.result.compId,
                                docSeq: res.result.docSeq
                            };
                            getActPlanDetailList(param);
                        }
                    }
                })
                .finally(() => {
                    // 사인이 이전 화면에 있어서 detail에서 signature를 찾을 수 없음. 그래서 사인저장로직을 백엔드로 뺌
                    // if (signCmd.value == 'I') {
                    //   signature.value.setApprovalInfo(inputForm.value.writeYear + inputForm.value.docNo);
                    // }
                    layoutStore.useBtnList = ['btnSearch', 'btnBack', 'btnAdd', 'btnSave', 'btnDelete'];
                });
        });
    };

    // 목표 및 추진사항 레포트 출력
    const btnDownload = () => {
        if (checkedList.value.length === 0) {
            alertMsg('warning', t('msgNoItem'));
            return;
        }
        confirmMsg('info', t('msgCheckedPrint'), null, { fun: downloadReports });
    };

    const downloadReports = () => {
        let checkedListParam = checkedList.value;
        let checkedYn = true;
        if (checkedList.value.length === 0) {
            checkedListParam = actPlanList.value;
            checkedYn = false;
        }

        const checkDocSeqList = checkedListParam.map(({ docSeq }) => docSeq);

        // openLoading()
        // return new Promise((resolve) => {
        //   getActionObjectiveReport({ compId: compId, writeYear: inputForm.value.writeYear, checkedList: checkDocSeqList, checkedPrint : checkedYn}, true)
        //       .then(res => {
        //         downloadReport(res)
        //         resolve({ result: res.result })
        //       })
        //       .finally(() => {
        //         endLoading();
        //       });
        // });
        baseDownload(getActionObjectiveReport, { compId: compId, writeYear: inputForm.value.writeYear, checkedList: checkDocSeqList, checkedPrint: checkedYn });
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
            path: 'ObjectivesAndActionPlan'
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
        // searchTerm의 띄어쓰기를 제거
        const sanitizedSearchTerm = searchTerm.value.replace(/\s+/g, '').toLowerCase();
        filteredData.value = cardList.value
            .map(el => ({
                ...el,
                data: el.data.filter(item => item.actionObjective.replace(/\s+/g, '').toLowerCase().includes(sanitizedSearchTerm) || (item.performanceTypeNm && item.performanceTypeNm.replace(/\s+/g, '').toLowerCase().includes(sanitizedSearchTerm)))
            }))
            .filter(el => el.data.length > 0); // data에 필터된 항목이 있는 경우만 포함
        dataFilterList.value = filteredData.value;

        let index = 0;

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

    //추가버튼

    const btnAdd = async data => {
        layoutStore.useBtnList = ['btnBack', 'btnSave'];
        router.push({
            path: '/ObjectivesAndActionPlanDetail'
        });

        //폼 초기화
        initInputForm();
        inputForm.value.cmd = 'I';
        inputForm.value.newYn = 'N';
        inputForm.value.useYn = 'Y';
        inputForm.value.performanceType = '0001';
        inputForm.value.performanceRepeat = '';
        inputForm.value.performanceCnt = '';
        inputForm.value.writeYear = initKey.value.writeYear;
        inputForm.value.docNo = initKey.value.docNo;

        inputForm.value.docType = 'OBJ';

        if (data) {
            console.log('데이터', data);
            isInputActive.value = false;
            inputForm.value.compObjectiveId = data.compObjectiveId;
            inputForm.value.companyObjective = data.companyObjective;
        }
    };

    //-----------------------------------------------

    //inputForm.value
    const btnSave = async () => {
        if (isInputActive.value) {
            // 신규 상태일때
            inputForm.value.newYn = 'Y'; // 플래그값 보내줌
        }
        //추진일정 vaildation (체크된게 하나도 없으면 저장 불가 x )
        const hasValidSchedule = [inputForm.value.actionSchedule1, inputForm.value.actionSchedule2, inputForm.value.actionSchedule3, inputForm.value.actionSchedule4].includes('Y');

        if (!hasValidSchedule) {
            alertMsg('warning', `추진일정을 입력해주세요.`);
            return; // 저장 중단
        }
        if (inputForm.value.orgnList.length > 0) {
            inputForm.value.orgnIdList = inputForm.value.orgnList.map(orgn => orgn.id);
        }

        //사인관련 컴포넌트
        if (!inputForm.value.docNo) {
            signCmd.value = 'I';
        } else {
            signCmd.value = 'U';
        }
        if (!inputForm.value.writeYear) {
            inputForm.value.writeYear = writeYear.value;
        }

        if (inputForm.value.cmd === 'U') {
            // 기존 조직 정보에서 삭제된 조직 정보가 있는 경우, 관련된 화면의 데이터 확인
            const param = {
                compId: getCompId(),
                writeYear: inputForm.value.writeYear,
                docType: inputForm.value.docType,
                docNo: inputForm.value.docNo,
                docSeq: inputForm.value.docSeq
            };

            // 1.안전보건목표
            // 1-1. 해당 목표에 등록되어있으면서 이미 안전보건목표가 등록된 조직 조회
            await getSafetyHealthObjOrgn(param);

            // 1-2.조회 조직리스트와 비교해서 inputForm.value.orgnIdList에 없는 조직이 있는지 판별
            const removedSafetyOrgnList = safetyHealthObjOrgnList.value.filter(orgn => !inputForm.value.orgnIdList.includes(orgn.orgnId));

            // 1-3.삭제하려는 조직에 예산 정보가 등록된 경우, 예산 정보 삭제 confirmMsg
            if (removedSafetyOrgnList.length > 0) {
                // removedOrgnList의 orgnNm 변환
                const removedOrgnText = removedSafetyOrgnList.map((orgn, idx) => `- ${orgn.orgnNm}`).join('\n');
                alertMsg('warning', '삭제하려는 담당조직으로\n정보가 등록되어 있어 삭제할 수 없습니다.\n아래 내용을 확인해주세요.', `안전보건목표\n${removedOrgnText}`);
                return;
            }

            // 2.안전보건환경 예산
            // 2-1.해당 목표에 등록되어있으면서 이미 예산이 등록된 조직 조회
            await getActionObjectiveOrgn(param);

            // 2-2.조회 조직리스트와 비교해서 inputForm.value.orgnIdList에 없는 조직이 있는지 판별
            const removedOrgnList = actPerformanceOrgnList.value.filter(orgn => !inputForm.value.orgnIdList.includes(orgn.orgnId));

            // 2-3.삭제하려는 조직에 예산 정보가 등록된 경우, 예산 정보 삭제 confirmMsg
            if (removedOrgnList.length > 0) {
                // removedOrgnList의 orgnNm 변환
                const removedOrgnText = removedOrgnList.map((orgn, idx) => `- ${orgn.orgnNm}`).join('\n');
                confirmMsg('info', '아래 삭제하려는 담당조직에 \n안전보건환경 예산 정보가 등록되어 있습니다.\n저장 시 해당 정보도 함께 삭제됩니다.\n그래도 계속하시겠습니까?', removedOrgnText, { fun: saveActPlanList, param: [inputForm.value] });
            } else {
                confirmMsg('info', t('msgSave'), '', { fun: saveActPlanList, param: [inputForm.value] });
            }
        } else {
            confirmMsg('info', t('msgSave'), '', { fun: saveActPlanList, param: [inputForm.value] });
        }
    };

    const getActionObjectiveOrgn = async param => {
        return getActionObjectiveOrgnList(param, false).then(res => {
            if (res && res.success) {
                actPerformanceOrgnList.value = res.list;
            }
            return res; // res 반환
        });
    };

    const getSafetyHealthObjOrgn = async param => {
        return getSafetyHealthObjOrgnList(param, false).then(res => {
            if (res && res.success) {
                safetyHealthObjOrgnList.value = res.list;
            }
            return res; // res 반환
        });
    };

    //-----------------------------------------------
    const checkedList = ref([]);

    //일괄 삭제
    const btnDelete = async item => {
        if (item == 'D') {
            //개별삭제
            confirmMsg('warning', t('msgDelete'), '', { fun: deleteActPlanList, param: inputForm.value });
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
            confirmMsg('warning', t('msgDelete'), ``, { fun: deleteActPlanLists, param: param });
        }
    };

    //-----------------------------------------------
    //성과구분 이벤트
    const changeHandle = item => {
        if (item !== '0001') {
            //기간별 아니면 반복 초기화
            inputForm.value.performanceRepeat = '';
        }
        if (item !== '0001' && item !== '0002') {
            //기간별 ,작업별 아니면 건수 초기화
            inputForm.value.performanceCnt = '';
        }
        if (item !== '0000') {
            //직접입력 아니면 입력값 초기화
            inputForm.value.performanceCustom = '';
        }
    };

    //-----------------------------------------------

    const searchParam = ref({
        compId: compId,
        searchText: '',
        writeYear: writeYear,
        docType: null,
        docNo: null
    });

    //-----------------------------------------------
    //useYn 체크

    const toggleUseYn = event => {
        // 체크박스의 체크 상태에 따라 'Y' 또는 'N'을 설정합니다.
        inputForm.value.useYn = _.cloneDeep(event.target.checked ? 'Y' : 'N');
    };

    //-----------------------------------------------

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
        objectiveId,
        writeYear,
        originData,
        initData,
        actPlanList,
        newYn,
        isInputActive,
        accordionIdx,
        actionKey,
        param,
        searchParam,
        toggleUseYn,
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
        changeHandle,
        checkNumberInput,
        signCmd,
        targetId,
        //라우터
        btnAdd,
        goBack,
        goObject,
        //버튼
        btnSave,
        btnDelete,
        btnDownload,
        //api
        getActPlanMasterList,
        saveActPlanList,
        getActPlanList,
        getActPlanDetailList,
        getObjectiveList,
        deleteActPlanList,
        deleteActPlanLists,
        currentDate,
        signature
    };
});
