import { defineStore } from 'pinia';
import { nextTick } from 'vue';
import router from '@/router';
import BaseView from '@/components/base/BaseView';
const { ref, reactive, t, alertMsg, toastPopup, getCompId, getCurrentDate, validationStore, formatDate } = BaseView();
import { getSafetyAndHealthObjectives, getSafetyAndHealthObjectivesDetail, saveSafetyAndHealthObjectives, delSafetyAndHealthObjectives, delSafetyAndHealthObjectivesDetail, getSafetyHealthObjectivesReport, getSafetyHealthObjectivesReportUseYn, saveSafetyAndHealthObjectivesTop, getSafetyHealthObjectivesReportMain, getMainSafetyAndHealthObjective } from './api/safetyAndHealthObjectives';

import { gsap } from 'gsap';
// 사용자 정보
import { useUserInfoStore } from '@/stores/user.js';
import { useLoadingPanelStore } from '@/stores/loadingPanel.js';
import { useButtonListStore } from '@/stores/buttonList';
const userStore = useUserInfoStore();
const loadingPanelStore = useLoadingPanelStore();
const layoutStore = useButtonListStore();
import { createSignatureStore } from '@/stores/signature';
const signatureStore = createSignatureStore(); // 컴포넌트마다 고유한 Store 생성

export const useSafetyHealthStore = defineStore('safetyHealthGoal', () => {
    const searchParam = ref({});
    const readonlyValue = ref({}); //2024.12.10 김현재 작성 상세화면 작성년도 readonly값 (신규시에만 수정가능하도록)
    const readonlyValueEnactedDt = ref({}); //제정일 readonly
    const requiredEnactedDt = ref({}); //제정일 required
    const readonlyValueRevisedDt = ref({}); //개정일 readonly

    const inputForm = ref({});
    const orgnItem = ref([]); // 조직 아이템
    const mainList = ref([]); // 메인 화면 데이터
    const detailList = ref([]); // 상세 화면 데이터
    const isSaved = ref(false); //목록버튼을 눌렀을때 메세지를 띄워주는 플래그값

    // 아코디언
    const activeSegments = ref({});
    const segments = ref([]);

    const orgnPopup = ref();
    const signature = ref(null); // 결재

    const beforeUseYnMain = ref(null);

    // 초기화
    const initInputForm = async () => {
        inputForm.value = {
            cmd: 'I',
            docType: 'SHO',
            docNo: '',
            orgnId: '',
            compId: getCompId(),
            writeYear: '',
            revisedDt: '',
            enactedDt: '',
            safetyHealthGoal: '',
            detailGoalMethod: '',
            promotionSchedule: '',
            hrId: '',
            hrNm: '',
            requiredResource: '',
            evaluationMethod: '',
            remark: '',
            useYn: '',
            useYnMain: '',
            userId: userStore.userId,
            // 추진계획 데이터
            actionDocType: '',
            actionDocNo: '',
            actionDocSeq: '',
            actionDocDetailSeq: '',
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
            // 추진계획 데이터(선택가능항목)
            checkableActionSchedule1: '',
            checkableActionSchedule2: '',
            checkableActionSchedule3: '',
            checkableActionSchedule4: '',
            checkableActionSchedule5: '',
            checkableActionSchedule6: '',
            checkableActionSchedule7: '',
            checkableActionSchedule8: '',
            checkableActionSchedule9: '',
            checkableActionSchedule10: '',
            checkableActionSchedule11: '',
            checkableActionSchedule12: ''
        };

        searchParam.value = {};
        orgnItem.value = [];
    };

    const setRefs = orgn => {
        orgnPopup.value = orgn.value;
    };

    // 메인 조회
    const searchMain = async () => {
        const param = { compId: getCompId() };

        await getSafetyAndHealthObjectives(param, true).then(res => {
            console.log('mainList.value', res.list);
            mainList.value = res.list;
            searchClientGrid(mainList.value);
        });
    };

    // 상세 조회
    const searchDetail = async (param, notify = true) => {
        await getMainSafetyAndHealthObjective(param, false).then(async res => {
            console.log('상세 조회값', res);
            inputForm.value = res.list[0];
            beforeUseYnMain.value = res.list[0].useYnMain;
        });
        await getSafetyAndHealthObjectivesDetail(param, notify).then(async res => {
            console.log('상세 조회값', res);
            detailList.value = res.list;
        });
    };

    // 아코디언 세팅
    const searchClientGrid = async val => {
        segments.value = [];
        activeSegments.value = {};

        for (let i of val) {
            const matchingYears = segments.value.filter(segment => segment.year.includes(i.writeYear));

            i.detail = {
                [t('enactedDt')]: formatDate(i.enactedDt),
                [t('revisedDt')]: formatDate(i.revisedDt) ?? '-',
                [t('docCount')]: i.docCount
            };
            if (matchingYears.length == 0) {
                segments.value.push({
                    year: i.writeYear + '년도',
                    dataList: [i]
                });
            } else {
                matchingYears[0].dataList.push(i);
            }
        }

        const currentYear = new Date().getFullYear(); // 현재 년도

        // 현재 년도에 해당하는 index를 찾기
        let index = segments.value.findIndex(item => item.year.slice(0, 4) == currentYear);

        // 만약 일치하는 항목이 없으면 index를 0으로 설정
        if (index === -1) {
            const currentYear = String(new Date().getFullYear());
            // 조회 데이터가 없을 시 현재 연도 빈 아코디언 세팅
            segments.value.unshift({ year: currentYear + '년도' });

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

    // 저장
    const saveAction = data => {
        saveSafetyAndHealthObjectives(data)
            .then(async res => {
                // parameter 재정의
                searchParam.value.orgnId = res.result[0].orgnId;
                searchParam.value.docType = res.result[0].docType;
                searchParam.value.docNo = res.result[0].docNo;
                searchParam.value.writeYear = res.result[0].writeYear;
                inputForm.value.enactedDt = res.result[0].enactedDt;
                // signature.value.setApprovalInfo(res.result[0].writeYear + res.result[0].docNo);

                const success = await signature.value.setApprovalInfo('SHO', searchParam.value.writeYear, searchParam.value.docNo);
                console.log('@# success', success);
                if (success) {
                    isSaved.value = true;
                    //저장후 수정상태로 변경
                    inputForm.value.cmd = 'U';
                    layoutStore.useBtnList = ['btnBack', 'btnSearch', 'btnAdd', 'btnSave', 'btnDelete', 'btnDownload'];
                    readonlyValue.value = true;
                    readonlyValueEnactedDt.value = true; //제정일 편집불가
                    readonlyValueRevisedDt.value = false; //개정일 편집가능
                    // 재조회
                    searchDetail(searchParam.value, false);
                }
            })
            .catch(err => {
                // alertMsg('error', '에러', err);
            });
    };

    //수정시 아코디언을 체크안하고 저장했을때 실행되는 함수(상단에 있는 사용여부만 업데이트됨)
    const saveActionTop = data => {
        saveSafetyAndHealthObjectivesTop(data)
            .then(async res => {
                await signature.value.updateTaskUseYn('SHO', data.writeYear, data.docNo);
                console.log('res', res);
            })
            .catch(err => {
                // alertMsg('error', '에러', err);
            });
    };

    // 메인 데이터 삭제
    const deleteMainAction = async data => {
        for (let i of data) {
            i.userId = userStore.userId;
        }

        delSafetyAndHealthObjectives(data)
            .then(async res => {
                for(let item of data) {
                    await signatureStore.forcedUpdateTaskUseYn('N', 'SHO', item.writeYear, item.docNo);
                }
                searchMain();
            })
            .catch(err => {
                // alertMsg('error', '에러', err);
            });
    };

    // 상세 데이터 삭제(수정시)
    const deleteDetailAction = async data => {
        delSafetyAndHealthObjectivesDetail(data)
            .then(res => {
                searchDetail(searchParam.value);
                isSaved.value = true;
            })
            .catch(err => {
                // alertMsg('error', '에러', err);
            });
    };

    const deleteDetaionActionInsert = async notCheckedData => {
        detailList.value = [];
        //체크 안된 데이터를 다시 넣어줌
        for (let i = 0; i < notCheckedData.length; i++) {
            detailList.value.push(notCheckedData[i]);
        }
    };

    // 리포트 출력
    const downloadReport = el => {
        //로딩블럭 추가
        loadingPanelStore.openLoading();
        const param = [];

        for (let i of el) {
            param.push({
                orgnId: searchParam.value.orgnId,
                docType: searchParam.value.docType,
                docNo: searchParam.value.docNo,
                writeYear: inputForm.value.writeYear,
                docSeq: i.docSeq,
                type: layoutStore.downloadType
            });
        }

        getSafetyHealthObjectivesReport(param)
            .then(res => {
                loadingPanelStore.endLoading();
                let link = document.createElement('a');
                let objectUrl = window.URL.createObjectURL(res.data);

                link.href = objectUrl;
                link.download = res.headers.filename;
                link.click();
            })
            .catch(err => {
                loadingPanelStore.endLoading();
                // alertMsg('warning', err);
            });
    };

    const downloadReportUseYn = el => {
        loadingPanelStore.openLoading();
        const param = [];

        for (let i = 0; i < el.length; i++) {
            param.push({
                orgnId: searchParam.value.orgnId,
                docType: searchParam.value.docType,
                docNo: searchParam.value.docNo,
                writeYear: inputForm.value.writeYear,
                docSeq: el[i].docSeq,
                type: layoutStore.downloadType
            });
        }

        getSafetyHealthObjectivesReportUseYn(param)
            .then(res => {
                loadingPanelStore.endLoading();
                let link = document.createElement('a');
                let objectUrl = window.URL.createObjectURL(res.data);

                link.href = objectUrl;
                link.download = res.headers.filename;
                link.click();
            })
            .catch(err => {
                loadingPanelStore.endLoading();
                // alertMsg('warning', err);
            });
    };

    // 메인화면에서 리포트 출력
    const downloadReportMain = el => {
        console.log('el', el);
        //로딩블럭 추가
        loadingPanelStore.openLoading();
        const param = [];
        let fileNm = '';
        let fileNmYear = '';
        //다중선택일때
        if (el.length > 1) {
            let yearArr = [];
            for (let i of el) {
                yearArr.push(i.writeYear); //선택된 카드의 년도를 가져옴
            }
            let deletedYear = Array.from(new Set(yearArr)); //중복값 제거
            deletedYear.sort(); //년도를 오름차순으로 정렬
            //파일제목에 2023,2024,2025~~
            for (let i = 0; i < deletedYear.length; i++) {
                if (deletedYear.length - 1 === i) {
                    fileNmYear += deletedYear[i];
                } else {
                    fileNmYear += deletedYear[i] + ',';
                }
            }
            fileNm = `(${fileNmYear})안전보건 목표`;
        }
        //단일선택일때
        else {
            fileNm = `(${el[0].writeYear})안전보건 목표_${el[0].orgnNm}`;
        }

        for (let i of el) {
            param.push({
                orgnId: i.orgnId,
                docType: i.docType,
                docNo: i.docNo,
                writeYear: i.writeYear,
                fileNm: fileNm,
                type: layoutStore.downloadType
            });
        }

        getSafetyHealthObjectivesReportMain(param)
            .then(res => {
                loadingPanelStore.endLoading();
                let link = document.createElement('a');
                let objectUrl = window.URL.createObjectURL(res.data);

                link.href = objectUrl;
                link.download = res.headers.filename;
                link.click();
            })
            .catch(err => {
                loadingPanelStore.endLoading();
                // alertMsg('warning', err);
            });
    };

    // 상세정보 보기
    const goDetail = async val => {
        await initInputForm();
        // 상세정보 조회 파라미터
        searchParam.value = {
            orgnId: val.orgnId,
            docType: val.docType,
            docNo: val.docNo,
            writeYear: val.writeYear
        };
        // inputForm 바인딩
        for (const [key, value] of Object.entries(val)) {
            if (inputForm.value.hasOwnProperty(key)) {
                inputForm.value[key] = value;
            }
        }

        inputForm.value.cmd = 'U'; // 수정

        // 조직 바인딩
        orgnItem.value = [{ id: val.orgnId, name: val.orgnNm }];
    };

    // 조직팝업 닫기
    const closeOrgn = e => {
        //chips에 넣기위해 id:'', name:'' 으로 세팅
        if (e.length !== 0) {
            orgnItem.value = [];
            inputForm.value.orgnId = e[0].orgnId;
            for (var dt of e) {
                orgnItem.value.push({
                    id: dt.orgnId,
                    name: dt.orgnNm
                });
            }
        }
        orgnPopup.value.onClose();
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
        inputForm,
        searchParam,
        orgnItem,
        orgnPopup,
        initInputForm,
        goDetail,
        closeOrgn,
        setRefs,
        searchClientGrid,
        searchMain,
        mainList,
        segments,
        searchDetail,
        detailList,
        saveAction,
        deleteMainAction,
        deleteDetailAction,
        signature,
        downloadReport,
        downloadReportUseYn,
        readonlyValue,
        isSaved,
        deleteDetaionActionInsert,
        saveActionTop,
        downloadReportMain,
        readonlyValueEnactedDt,
        readonlyValueRevisedDt,
        requiredEnactedDt,
        beforeUseYnMain
    };
});
