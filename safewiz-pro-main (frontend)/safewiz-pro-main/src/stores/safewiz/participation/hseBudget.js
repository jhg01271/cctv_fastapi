import { defineStore } from 'pinia';
import router from '@/router';
import BaseView from '@/components/base/BaseView';
const { ref, alertMsg, confirmMsg, getCompId, getCurrentDate, t, formatToAmt, baseDownload, openLoading, endLoading } = BaseView();
import { getActionPerformance, getActionPerformanceDetail, saveActionPerformance, deleteActionPerformance, deleteActionPerformanceDetail, getHseBudgetReport } from '@/stores/safewiz/participation/api/hseBudgetApi.js';
import { useButtonListStore } from '@/stores/buttonList';
const layoutStore = useButtonListStore();
import { nextTick } from 'vue';
import { gsap } from 'gsap';

export const useHseBudgetStore = defineStore('HseBudget', () => {
    const budgetList = ref([]); //데이터1개 실적 자료가 performanceList로 들어감
    const checkedList = ref([]);
    const cardList = ref([]);

    //버튼 리스트
    const buttonList = ref(['btnSearch', 'btnBack', 'btnAdd', 'btnSave', 'btnDelete']);

    const writeYear = ref('');
    // 서명 컴포넌트
    const signature = ref(null);
    //사인컴포넌트
    const signCmd = ref('I');

    //상위 조직
    const compId = getCompId();
    const param = ref('');
    //현재날짜
    const currentDate = ref(getCurrentDate());

    const inputForm = ref({});
    const initInputForm = () => {
        inputForm.value = {
            cmd: 'I',
            cmdArray: '',
            compId: compId,
            //전사/추진 목표
            docSeqO: '',
            writeYearO: '',
            docTypeO: '',
            docNoO: '',
            docDetailSeqO: '',
            //
            writeYear: '',
            docType: '',
            docNo: '',
            docSeq: '',
            docDetailSeq: '',
            docPerformanceSeq: '',
            companyObjective: '',
            compObjectiveId: '',
            actionObjective: '',
            detailItemId: '',
            detailItem: '',
            detailPlan: '',
            performanceType: '',
            performanceTypeNm: '',
            performanceRepeat: '',
            performanceRepeatNm: '',
            performanceCnt: '',
            performanceCustom: '',
            remark: '',
            ordSeq: '',
            useYn: '',
            orgnId: '',
            orgnIdO: '',
            orgnNm: '',
            actionSchedule1: '',
            actionSchedule2: '',
            actionSchedule3: '',
            actionSchedule4: '',
            actionSchedule5: '',
            actionSchedule6: '',
            actionSchedule7: '',
            actionSchedule8: '',
            actionSchedule9: '',
            actionSchedule10: '',
            actionSchedule11: '',
            actionSchedule12: '',
            actionPerformance1: '',
            actionPerformance2: '',
            actionPerformance3: '',
            actionPerformance4: '',
            actionPerformance5: '',
            actionPerformance6: '',
            actionPerformance7: '',
            actionPerformance8: '',
            actionPerformance9: '',
            actionPerformance10: '',
            actionPerfrmance11: '',
            actionPerformance12: '',
            budget: '',
            performanceAmount: '',
            updatedAt: null,
            updatedBy: null,
            createdAt: null,
            createdBy: null,
            checked: '',
            performanceList: [
                {
                    cmd: 'I',
                    checked: 'N',
                    writeYear: '',
                    docNo: '',
                    docType: '',
                    docSeq: '',
                    docPerformanceSeq: '',
                    docPerformanceDetailSeq: '',
                    contents: '',
                    performanceAmount: '',
                    ordSeq: '',
                    useYn: '',
                    fileId: '', // 첨부 아이디
                    deleteFiles: [] //삭제할 파일 id
                }
            ]
        };
    };

    const searchParam = ref({
        compId: compId,
        writeYear: null,
        docType: '',
        docNo: '',
        docSeq: '',
        docDetailSeq: '',
        orgnId: ''
    });

    const fileInfoList = ref([]);
    const setRefs = file => {
        fileInfoList.value = file.value;
    };

    // 안전보건 목표 및 추진계획서 문서에 대한 문서 key 값 조회
    // 현재는 년도마다 문서번호가 하나만 생성되기 때문에 조회할 필요없음
    //-> 년도마다 여러개의 문서번호가 생성되면 조회 api 수정 및 해당 데이터 조회해야함

    // const getActPlanMasterList = (notify) => {
    //   return getActPlanMaster(searchParam.value, notify).then(res => {
    //     if (res && res.success) {
    //       inputForm.value = res.list[0];
    //     }
    //   })
    // };

    //조직별 계획 데이터 조회
    const getActionPerformanceList = notify => {
        return getActionPerformance(searchParam.value, notify).then(res => {
            if (res && res.success) {
                const filteredArray = res.list.filter(item => item.docNo);
                if (filteredArray.length > 0) {
                    signCmd.value = 'U';
                } else {
                    layoutStore.useBtnList = ['btnSearch'];
                }
                searchTerm.value = '';
                budgetList.value = res.list;
                //TODO: 추진 계획 조회에서 수정안함
                const groupedByCompany = {};
                res.list
                    .filter(item => item.detailPlan !== null && item.detailPlan !== '') // 세부계획이 있는 값만 사용
                    .forEach(item => {
                        const companyObjective = item.companyObjective;

                        // 1. companyObjective로 그룹이 없으면 새로 생성
                        if (!groupedByCompany[companyObjective]) {
                            groupedByCompany[companyObjective] = {
                                name: companyObjective, // 그룹화된 이름
                                data: [], // 값을 담을 배열
                                totalAmt: 0, // budget 합산 필드
                                totalPerformancet: 0 // 실적 합산 필드
                            };
                        }
                        // 2. budget 값 합산
                        groupedByCompany[companyObjective].totalAmt += item.budget;

                        // 3. 목표 일정 (분기) 계산
                        const schedule = [];
                        if (item.docNo) {
                            if (item.actionPerformance1 === 'Y') schedule.push('1월');
                            if (item.actionPerformance2 === 'Y') schedule.push('2월');
                            if (item.actionPerformance3 === 'Y') schedule.push('3월');
                            if (item.actionPerformance4 === 'Y') schedule.push('4월');
                            if (item.actionPerformance5 === 'Y') schedule.push('5월');
                            if (item.actionPerformance6 === 'Y') schedule.push('6월');
                            if (item.actionPerformance7 === 'Y') schedule.push('7월');
                            if (item.actionPerformance8 === 'Y') schedule.push('8월');
                            if (item.actionPerformance9 === 'Y') schedule.push('9월');
                            if (item.actionPerformance10 === 'Y') schedule.push('10월');
                            if (item.actionPerformance11 === 'Y') schedule.push('11월');
                            if (item.actionPerformance12 === 'Y') schedule.push('12월');
                        }
                        // 4. 변환된 item에 세부정보 추가
                        const transformedItem = {
                            ...item,
                            checked: 'N',
                            detail: {
                                //              docNo 값이 없으면 예산 입력이 하나도 안된 상태, 카드에서 입력유무를 알기위해 '-'로 표시
                                ['추진일정']: item.docNo ? schedule.join(', ') : '-',
                                ['성과목표']: item.docNo ? item.performanceDiv : '-',
                                [t('objectives_budget_won')]: formatToAmt(item.budget) === '0' ? '-' : formatToAmt(item.budget),
                                ['실적(만원)']: formatToAmt(item.performanceAmount) === '0' ? '-' : formatToAmt(item.performanceAmount)
                            }
                        };

                        // 5. actionObjective로 그룹화된 데이터를 추가
                        let actionObjectiveGroup = groupedByCompany[companyObjective].data.find(group => group.name === item.actionObjective);

                        if (actionObjectiveGroup) {
                            // 6. actionObjective 그룹이 이미 있으면 budget 합산 및 데이터 추가
                            actionObjectiveGroup.totalAmt += item.budget;
                        } else {
                            // 7. actionObjective 그룹이 없으면 새로 생성 후 추가
                            actionObjectiveGroup = {
                                name: item.actionObjective,
                                totalAmt: item.budget,
                                data: []
                            };
                            groupedByCompany[companyObjective].data.push(actionObjectiveGroup);
                        }

                        // 8. detailItem로 그룹화된 데이터를 추가
                        let detailPlanGroup = groupedByCompany[companyObjective].data.find(group => group.name === item.actionObjective).data.find(subGroup => subGroup.name === item.detailPlan);
                        if (item.detailPlan) {
                            if (detailPlanGroup) {
                                // 9. detailItemGroup 그룹이 이미 있으면 budget 합산 및 데이터 추가
                                detailPlanGroup.totalAmt += item.budget;
                                groupedByCompany[companyObjective].data
                                    .find(group => group.name === item.actionObjective)
                                    .data.find(group => group.name === item.detailPlan)
                                    .data.push(transformedItem);
                            } else {
                                // 10. detailItemGroup 그룹이 없으면 새로 생성 후 추가
                                detailPlanGroup = {
                                    name: item.detailPlan,
                                    subTitle: item.detailItem,
                                    totalAmt: item.budget,
                                    data: [transformedItem]
                                };
                                groupedByCompany[companyObjective].data.find(group => group.name === item.actionObjective).data.push(detailPlanGroup);
                            }
                        }
                    });

                // 11. 최종적으로 dataFilterList와 cardList에 배열 형식으로 저장
                dataFilterList.value = Object.values(groupedByCompany);
                cardList.value = Object.values(groupedByCompany);

                initData();
            }
            return res; // res 반환
        });
    };

    const detailList = ref([]);
    const getActionPerformanceDetailList = (data, notify) => {
        console.log("재조회")
        getActionPerformanceDetail(searchParam.value, notify)
            .then(res => {
                if (res && res.success) {
                  console.log('#res',res)
                    if (res.list == null) {
                        initInputForm();
                        inputForm.value = data;
                        inputForm.value.writeYear = data.writeYearO;
                        inputForm.value.docType = data.docTypeO;
                        inputForm.value.docNo = data.docNoO;
                        inputForm.value.docSeq = data.docSeqO;
                        inputForm.value.docDetailSeq = data.docDetailSeqO;
                        inputForm.value.orgnId = data.orgnIdO;
                        inputForm.value.performanceList = [];

                        buttonList.value = ['btnBack', 'btnSave'];
                        layoutStore.useBtnList = buttonList.value
                    } else {
                        inputForm.value = res.list;

                        buttonList.value = ['btnSearch', 'btnBack', 'btnAdd', 'btnSave', 'btnDelete'];
                        layoutStore.useBtnList = buttonList.value
                    }
                }
            })
            .finally(() => {
                if (notify === false) {
                    //페이지 이동
                    router.push({
                        name: 'HseBudgetDetail'
                    });
                } else {
                    fileSearch();
                }
            });
    };

    // 파일 조회
    const fileSearch = () => {
        for (let i = 0; i < inputForm.value.performanceList.length; i++) {
            let file = fileInfoList.value[i];
            if (file && file.fnSearch) {
                file.fnSearch();
            }
        }
    };

    //조직 실적 삭제(delete)
    const deleteActionPerformanceList = data => {
        return new Promise(() => {
            deleteActionPerformance(data, true).then(res => {
                if (res && res.success) {
                    // 검색어 초기화
                    searchTerm.value = '';
                    //체크리스트 초기화
                    checkedList.value = [];
                    //리스트 초기화
                    getActionPerformanceList();
                }
            });
        });
    };

    //조직별 실적 증빙 자료 삭제
    const deleteActionPerformanceDetailList = data => {
        deleteActionPerformanceDetail(data, true).then(res => {
            if (res && res.success) {
                getActionPerformanceDetailList();
            }
        });
    };

    //조직별 실적 추가
    const saveActionMonthlyList = data => {
        console.log('save data => ', data);
        let formData = new FormData();

        // 파일 true로 체크된 항목만 필터링
        const checkedFiles = fileInfoList.value.filter(file => file.checked);
        //실적 증빙 자료 카드 중 선택된 카드의 파일만 수정
        for (let i = 0; i < checkedFiles.length; i++) {
            const file = checkedFiles[i];
            //삭제파일 지정
            data.performanceList[i].deleteFiles = file.editFiles.delete;
            //저장파일 지정
            formData.append('files', file.editFiles.insert && file.editFiles.insert.length > 0 ? file.editFiles.insert[0] : new Blob([], { type: 'application/octet-stream' }));
        }
        formData.append('info', new Blob([JSON.stringify(data)], { type: 'application/json' }));
        saveActionPerformance(formData, true)
            .then(res => {
                if (res && res.success) {
                    // 검색어 초기화
                    searchTerm.value = '';
                    //리스트 초기화
                    dataFilterList.value = [];
                    //체크리스트 초기화
                    checkedList.value = [];
                    //파일 파라미터 초기화
                    checkedFiles.forEach((file, index) => {
                        if (res.result.performanceList[index].fileId) file.resetEditFiles(res.result.performanceList[index].fileId);
                    });
                    // if (signCmd.value == 'I') {
                    //   // detail 페이지 들어오기전 리스트 조회에서 signCmd.value 세팅되어 있음
                    //   // 사인은 해당년도의 최초값 단 하나기때문에 '00001' 고정해서 사용
                    //   signature.value.setApprovalInfo(res.result.writeYear + '00001');
                    // }
                    // 재조회
                    getActionPerformanceDetailList();
                }
            })
            .finally(() => {
                buttonList.value = ['btnSearch', 'btnBack', 'btnAdd', 'btnSave', 'btnDelete'];
            });
    };

    //목록으로 이동
    const goBack = () => {
        //검색어 초기화
        searchTerm.value = '';
        router.push({ name: 'HseBudget' });
    };

    //-----------------------------------------------

    //이동버튼
    const goObject = () => {
        //페이지 이동
        router.push({
            path: 'HseBudget'
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
        const filteredCards = cardList.value
            .map(card => {
                const filteredSections = card.data
                    .map(section => {
                        const keyword = searchTerm.value.trim().toLowerCase();

                        // section.name이 검색어와 일치하는 경우엔 전체 유지
                        const sectionNameMatched = section.name?.toLowerCase().includes(keyword);

                        // 각 item.name에서 검색어 포함 여부 확인
                        const filteredItems = section.data?.filter(item => item.name?.toLowerCase().includes(keyword)) || [];

                        return {
                            ...section,
                            data: sectionNameMatched ? section.data : filteredItems
                        };
                    })
                    .filter(section => section.data.length > 0);

                return {
                    ...card,
                    data: filteredSections
                };
            })
            .filter(card => card.data.length > 0);

        dataFilterList.value = filteredCards;

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
    const btnAdd = () => {
        inputForm.value.performanceList.push({
            cmd: 'I',
            checked: true,
            writeYear: inputForm.value.writeYearO,
            docNo: inputForm.value.docNoO,
            docType: inputForm.value.docTypeO,
            docSeq: inputForm.value.docSeqO,
            docDetailSeq: inputForm.value.docDetailSeqO,
            docPerformanceSeq: inputForm.value.docPerformanceSeq,
            docPerformanceDetailSeq: '',
            compId: compId,
            contents: '',
            performanceAmount: '',
            ordSeq: null,
            useYn: 'Y',
            fileId: ''
        });
    };
    //-----------------------------------------------

    //저장
    const btnSave = async () => {
        // performanceList 배열 가공
        const filteredPerformanceList = inputForm.value.performanceList
            .filter(el => el.checked) // checked가 true인 항목만 필터링
            .map(el => ({
                ...el,
                ordSeq: el.ordSeq || 99 // ordSeq가 없으면 99로 설정
            }));
        console.log('예산 저장', inputForm.value.performanceList);

        // const checkedList = inputForm.value.performanceList.filter(file => file.checked);
        // if (checkedList.length == 0) {
        //   alertMsg('warning', t('msgNoItem'));
        //   return;
        // }

        let sum = 0; // 누적 합계 변수 초기화

        filteredPerformanceList.forEach(el => {
            if (el.useYn === 'Y') {
                // useYn이 'Y'인 항목만 포함
                sum += el.performanceAmount; // 누적 합계
            }
        });

        confirmMsg('info', t('msgSave'), '', {
            fun: saveActionMonthlyList,
            param: {
                ...inputForm.value,
                performanceList: filteredPerformanceList // 가공된 배열 저장 (체크된 performanceList만)
            }
        });
    };

    //-----------------------------------------------

    //조직 실적, 예산 삭제
    const btnDelete = async item => {
        let checkedData = { ...inputForm.value }; // inputForm.value의 얕은 복사
        checkedData.performanceList = inputForm.value.performanceList.filter(el => el.checked);
        if (checkedData.performanceList.length === 0) {
            alertMsg('warning', t('msgNoItem'));
            return;
        }

        if (item === 'D') {
            confirmMsg('warning', t('msgDelete'), ``, { fun: deleteActionPerformanceDetailList, param: checkedData });
        }
    };

    // 예산 레포트 출력
    const btnDownload = () => {      
        confirmMsg('info', t('msgCheckedPrint'), null, { fun: downloadReports });
    };

    const downloadReports = () => {
        let checkedListParam = checkedList.value;
        let checkedYn = true;
        if (checkedList.value.length === 0) {
            checkedListParam = budgetList.value;
            checkedYn = false;
        }
        
        
        const checkDocSeqList = checkedListParam.map(({ writeYearO, docNoO, docSeqO, docDetailSeqO, docPerformanceSeq, orgnIdO }) => (docPerformanceSeq ? `${writeYearO}${docNoO}${docSeqO}${docDetailSeqO}${docPerformanceSeq}` : `${writeYearO}${docNoO}${docSeqO}${docDetailSeqO}${orgnIdO}`));        
        const docNoO = checkedListParam.length > 0 ? checkedListParam[0].docNoO : null;

        let param = {
            compId: searchParam.value.compId,
            writeYear: searchParam.value.writeYear,
            docNo: docNoO,
            checkedList: checkDocSeqList,
            checkedPrint: checkedYn
        };

        // openLoading()
        // getHseBudgetReport(param, false).then(res => {
        //   downloadReport(res)
        // }).finally(() => {
        //   endLoading();
        // });
        baseDownload(getHseBudgetReport, param);
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
        toggleAccordion,
        initInputForm,
        inputForm,
        param,
        setRefs,
        writeYear,
        initData,
        budgetList,
        searchParam,
        checkedList,
        searchTerm,
        applyFilter,
        dataFilterList,
        detailList,
        buttonList,
        fileSearch,
        //라우터
        btnAdd,
        goBack,
        goObject,
        //버튼
        btnSave,
        btnDelete,
        btnDownload,
        //api
        getActionPerformanceList,
        getActionPerformanceDetailList,
        currentDate,
        signature,
        signCmd
    };
});
