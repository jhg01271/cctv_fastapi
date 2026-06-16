import { defineStore } from 'pinia';

import BaseView from '@/components/base/BaseView';
const { baseDownload, confirmMsg, toastPopup, alertMsg, ref, t, getCompId, getCurrentDate, openLoading, endLoading } = BaseView();
import router from '@/router';
import { getAnnualTrainingPlan, saveAnnualTrainingPlan, deleteAnnualTrainingPlans, getAnnualReport, getAnnualTrainingCurrentAndPreviousYear, saveAnnualTrainingCurrentAndPreviousYear } from '@/stores/safewiz/support/api/annualTrainingPlanApi.js';
import _ from 'lodash';
import { getSystemCode } from '@/stores/system/setting/api/SystemCode.js';
export const useAnnualTrainingPlanStore = defineStore('annualTrainingPlan', () => {
    const compId = getCompId();
    const cmd = ref('I');
    const docType = ref('ATP');
    const docNo = ref('');

    const originData = ref([]);

    const writeYear = getCurrentDate().substring(0, 4);
    const searchParam = ref({
        compId: compId,
        writeYear: writeYear
    });

    const signatureComponent = ref({});

    //전년도 불러오기 flag
    const loadPreviousYn = ref(false);

    const eduTimeTitle = ref(''); // 교육 시간 툴팁 버튼
    const eduContentTitle = ref(''); // 교육 내용 툴팁 버튼

    const inputForm = ref({});
    const initInputForm = () => {
        inputForm.value = {
            checked: false,
            cmd: '',
            compId: compId, // 사업장 ID
            writeYear: '', //작성년도
            docType: docType.value,
            docNo: '',
            trainingCourse: '',
            trainingDuration: null,
            trainingInstitute: '',
            trainingInstructor: '',
            trainingMonth1: 'N',
            trainingMonth2: 'N',
            trainingMonth3: 'N',
            trainingMonth4: 'N',
            trainingMonth5: 'N',
            trainingMonth6: 'N',
            trainingMonth7: 'N',
            trainingMonth8: 'N',
            trainingMonth9: 'N',
            trainingMonth10: 'N',
            trainingMonth11: 'N',
            trainingMonth12: 'N',
            hrList: [],
            peopleItem: [],
            subjectItem: [],
            useYn: 'Y',
            updatedAt: '',
            updatedBy: '',
            createdAt: '',
            createdBy: '' // 수정일시
        };
    };

    //API 연결-------------------------------------------------------------------------------
    //연간교육 계획서 조회
    const getAnnualTrainingPlanList = notify => {
        openLoading();
        return getAnnualTrainingPlan(searchParam.value, notify)
            .then(async res => {
                if (res && res.success) {
                    res.list.forEach(hr => hr.hrList.forEach(el => Object.assign(el, { id: el.hrId, name: el.hrNm })));
                    inputForm.value = res.list;
                    if (res.list.length === 0) cmd.value = 'I';
                    else cmd.value = 'U';
                    await signatureComponent.value[searchParam.value.writeYear].Search();
                    originData.value = _.cloneDeep(res.list);
                    loadPreviousYn.value = false;

                    // 교육시간/교육내용 툴팁 타이틀 지정
                    getSystemCode({ majorCd: 'CF001' }).then(async res => {
                        eduTimeTitle.value = res.list.find(el => el.minorCd === 'edu01').minorNm;
                        eduContentTitle.value = res.list.find(el => el.minorCd === 'edu02').minorNm;
                    });
                }
                return res; // res 반환
            })
            .catch(() => {
                endLoading();
            })
            .finally(() => {
                endLoading();
            });
    };

    //연간교육 계획서 저장
    const saveTrainingPlanApi = (data, notify) => {
        openLoading();
        saveAnnualTrainingPlan(data, true)
            .then(async res => {
                if (res && res.success) {
                    const success = await signatureComponent.value[searchParam.value.writeYear].setApprovalInfo('ATP', res.result.writeYear, res.result.compId);
                    if (success) await getAnnualTrainingPlanList();
                }
            })
            .catch(() => {
                endLoading();
            })
            .finally(() => {
                endLoading();
            });
    };

    //세부항목 삭제
    const deleteTrainingPlansApi = data => {
        openLoading();
        deleteAnnualTrainingPlans(data, true)
            .then(res => {
                if (res && res.success) {
                    getAnnualTrainingPlanList(searchParam.value);
                }
            })
            .catch(() => {
                endLoading();
            })
            .finally(() => {
                endLoading();
            });
    };

    //전년도 및 현년도 안전보건 연간교육 조회
    const getATPCurrentAndPreviousYearList = notify => {
        openLoading();
        return getAnnualTrainingCurrentAndPreviousYear(searchParam.value, notify)
            .then(res => {
                if (res && res.success) {
                    if (res.list.length === 0) {
                        alertMsg('warning', '전년도 데이터가 없습니다.'); // 메시지 띄우기
                        loadPreviousYn.value = false;
                        return
                    } else {
                        res.list.forEach(item => {
                            item.hrList.forEach(el => Object.assign(el, { id: el.hrId, name: el.hrNm }));
                            if (item.writeYear !== searchParam.value.writeYear) {
                                item.checked = true;
                            }
                        });
                        inputForm.value.push(...res.list)
                        loadPreviousYn.value = true;
                    }
                    console.log('## inputform', inputForm.value)
                }
                return res; // res 반환
            })
            .catch(() => {
                endLoading();
            })
            .finally(() => {
                endLoading();
            });
    };

    //전년도 불러오기 저장
    const saveATPCurrentAndPreviousYear = (data, notify) => {
        openLoading();
        saveAnnualTrainingCurrentAndPreviousYear(data, true)
            .then(async res => {
                if (res && res.success) {
                    loadPreviousYn.value = false;
                    await signatureComponent.value[searchParam.value.writeYear].setApprovalInfo('ATP', res.result.writeYear, res.result.compId);
                    getAnnualTrainingPlanList();
                }
            })
            .catch(() => {
                endLoading();
            })
            .finally(() => {
                endLoading();
            });
    };

    const btnBack = () => {
        //목록이동
        router.push({ name: 'EducationTraining' });
    };

    const btnSearch = () => {
        loadPreviousYn.value = false;
        getAnnualTrainingPlanList(true);
    };

    const btnAdd = () => {
        //추가
        inputForm.value.push({
            checked: true,
            cmd: 'I',
            compId: compId, // 사업장 ID
            writeYear: writeYear, //작성년도
            docType: docType.value,
            docNo: docNo.value,
            trainingCourse: null,
            trainingDuration: null,
            trainingInstitute: null,
            trainingInstructor: null,
            trainingMonth1: 'N',
            trainingMonth2: 'N',
            trainingMonth3: 'N',
            trainingMonth4: 'N',
            trainingMonth5: 'N',
            trainingMonth6: 'N',
            trainingMonth7: 'N',
            trainingMonth8: 'N',
            trainingMonth9: 'N',
            trainingMonth10: 'N',
            trainingMonth11: 'N',
            trainingMonth12: 'N',
            hrList: [],
            peopleItem: [],
            subjectItem: [],
            ordSeq: null,
            useYn: 'Y',
            updatedAt: '',
            updatedBy: '',
            createdAt: getCurrentDate(),
            createdBy: '' // 수정일시
        });
    };

    const btnSave = () => {
        let saveData = inputForm.value.filter(el => el.checked === true);
        saveData.forEach(el => {
            el.writeYear = searchParam.value.writeYear
            if (!el.ordSeq) {
                el.ordSeq = 99;
            }
        });
        if (saveData.length === 0) {
            alertMsg('warning', t('msgNoItem'));
            return;
        }
        if (saveData.length > 0) {
            confirmMsg('info', '저장 하시겠습니까?', '', { fun: loadPreviousYn.value ? saveATPCurrentAndPreviousYear : saveTrainingPlanApi, param: saveData });
        }
    };

    const btnDelete = () => {
        let saveData = inputForm.value.filter(el => el.checked === true);
        deleteTrainingPlansApi(saveData)
    };

    // 목표 및 추진사항 레포트 출력
    const btnDownload = () => {
        let checkedListParam = inputForm.value.filter(el => el.checked === true);
        if (checkedListParam.length > 0) {
            confirmMsg('info', t('msgCheckedPrint'), null, { fun: downloadReports });
        } else {
            confirmMsg('info', t('msgPrint'), null, { fun: downloadReports });
        }
    };

    const downloadReports = () => {
        let checkedListParam = inputForm.value.filter(el => el.checked === true);
        let checkedYn = true;
        if (checkedListParam.length === 0) {
            checkedListParam = inputForm.value;
            checkedYn = false;
        }

        const checkDocSeqList = checkedListParam.map(({ docNo }) => docNo);
        const param = { compId: compId, writeYear: searchParam.value.writeYear, checkedList: checkDocSeqList, checkedPrint: checkedYn };
        baseDownload(getAnnualReport, param);
        // openLoading()
        // getAnnualReport({ compId: compId, writeYear: searchParam.value.writeYear, checkedList: checkDocSeqList, checkedPrint : checkedYn }, false).then(res => {
        //   downloadReport(res)
        // }).finally(() => {
        //   endLoading();
        // });
    };

    const btnPreYear = async () => {
        if (loadPreviousYn.value) {
            confirmMsg('info', '전년도 불러오기를 하시겠습니까?\n(전년도 불러오기 진행중)', '', {
                fun: () => {
                    getATPCurrentAndPreviousYearList(true);
                }
            });
            return;
        }
        //조회
        getATPCurrentAndPreviousYearList(true);
    };

    return {
        cmd,
        signatureComponent,
        inputForm,
        initInputForm,
        originData,
        compId,
        docType,
        writeYear,
        searchParam,
        eduTimeTitle,
        eduContentTitle,
        loadPreviousYn,
        // function
        btnBack,
        btnSearch,
        btnAdd,
        btnSave,
        btnDelete,
        btnDownload,
        btnPreYear,
        // api
        getAnnualTrainingPlanList,
        getATPCurrentAndPreviousYearList,
        saveATPCurrentAndPreviousYear
    };
});
