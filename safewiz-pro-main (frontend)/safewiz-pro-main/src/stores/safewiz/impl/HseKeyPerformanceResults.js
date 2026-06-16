import { defineStore } from 'pinia';

import router from '@/router';
import BaseView from '@/components/base/BaseView';
const { ref, getCurrentDate, openLoading, endLoading, alertMsg, t, confirmMsg, baseDownload, formatDate } = BaseView();
import { delHseKeyPerformanceResult, getHseKeyPerformanceResultReport, getHseKeyPerformanceResultReportList, getSafetyAndHealthObjectives, getSafetyAndHealthObjectivesDetail, saveHseKeyPerformanceResult } from './api/HseKeyPerformanceResultsApi';

export const useHseKeyPerformanceStore = defineStore('hseKeyPerformance', () => {
    const searchParam = ref({});
    const inputForm = ref({});
    const orgnItem = ref([]); // 조직 아이템
    const mainList = ref([]); // 메인 화면 데이터
    const detailList = ref([]); // 상세 화면 데이터

    const writeYear = ref(getCurrentDate().substring(0, 4));

    const cmd = ref();

    // 아코디언
    const activeSegments = ref({});
    const segments = ref([]);

    const orgnPopup = ref();
    const signature = ref(null); // 결재

    // 초기화
    const initInputForm = async () => {
        searchParam.value = {};
        orgnItem.value = [];
        detailList.value = [];
    };

    const setRefs = orgn => {
        orgnPopup.value = orgn.value;
    };

    // 메인 조회
    const searchMain = async () => {
        openLoading();
        await getSafetyAndHealthObjectives({}, true)
            .then(res => {
                mainList.value = res.list;
                searchClientGrid(mainList.value);
            })
            .finally(endLoading());
    };

    // 상세 조회
    const searchDetail = async (param, notify = true) => {
        searchParam.value = {
            orgnId: param.orgnId,
            docType: 'SHO',
            docNo: param.docNo,
            writeYear: param.writeYear
        };
        return new Promise(resolve => {
            openLoading();
            getSafetyAndHealthObjectivesDetail(searchParam.value, notify)
                .then(async res => {
                    //데이터에 index 추가
                    let i = 0;
                    res.list.forEach(val => {
                        val.index = i;
                        i++;
                    });
                    detailList.value = res.list;
                    detailList.value.forEach(item => {
                      item.revisedDt = formatDate(item.revisedDt);
                      item.enactedDt = formatDate(item.enactedDt);
                      
                      item.result.forEach(res => {
                          res.checkDt = formatDate(res.checkDt);
                      });
                    });
                })
                .finally(() => {
                    resolve(true);
                    endLoading();
                });
        });
    };

    // 아코디언 세팅
    const searchClientGrid = async val => {
        segments.value = [];
        activeSegments.value = {};
        for (let i of val) {
            // i.docCount가 0 이하인 경우 건너뜀
            if (i.docCount <= 0) continue;

            const matchingYears = segments.value.filter(segment => segment.year.includes(i.writeYear));
            const progressRate = i.resCount > 0 ? Math.round(i.progressSum / i.resCount) : 0;
            i.detail = {
                [t('hseKeyPerformanceResults_enactedDt')]: formatDate(i.enactedDt),
                [t('hseKeyPerformanceResults_revisedDt')]: formatDate(i.revisedDt),
                [t('hseKeyPerformanceResults_docCount')]: i.docCount,
                [t('hseKeyPerformanceResults_resCount')]: i.resCount + `(${progressRate}%)`
                // ['결재 상태']: i.approvalStatus,
            };

            if (matchingYears.length === 0) {
                segments.value.push({
                    year: i.writeYear + '년도',
                    dataList: [i]
                });
            } else {
                matchingYears[0].dataList.push(i);
            }
        }
    };

    // 상세정보 보기
    const goDetail = async val => {
        await initInputForm();
        searchParam.value = {
            orgnId: val.orgnId,
            docType: val.docType,
            docNo: val.docNo,
            writeYear: val.writeYear
        };

        searchDetail(searchParam.value);
        cmd.value = 'U';
    };

    const addHseKeyPerformanceResult = selectedIndex => {
        if (selectedIndex == null) {
            alertMsg('warning', t('msgNoItem'));
        }
        let target = detailList.value[selectedIndex];
        target.result.push({
            cmd: 'I',
            useYn: 'Y',
            writeYear: target.writeYear,
            docType: 'KPR',
            docNo: target.docNo,
            docSeq: target.docSeq,
            percent: '0',
            content: '',
            checkDt: '',
            checkYn: 'Y',
            actionResult1: 'N',
            actionResult2: 'N',
            actionResult3: 'N',
            actionResult4: 'N',
            actionResult5: 'N',
            actionResult6: 'N',
            actionResult7: 'N',
            actionResult8: 'N',
            actionResult9: 'N',
            actionResult10: 'N',
            actionResult11: 'N',
            actionResult12: 'N'
        });
    };

    const deleteHseKeyPerformanceResult = arr => {
        openLoading();
        delHseKeyPerformanceResult(arr, true)
            .then(() => {
                searchDetail(searchParam.value);
            })
            .finally(() => {
                endLoading();
            });
    };

    const downloadMaster = checkedRow => {
        let searchVO = { checkedObjList: checkedRow.value };
        baseDownload(getHseKeyPerformanceResultReportList, searchVO);
        // openLoading()
        // getHseKeyPerformanceResultReportList({ checkedObjList: checkedRow.value })
        //     .then(res => {
        //       let link = document.createElement('a');

        //       let objectUrl = window.URL.createObjectURL(res.data);
        //       link.href = objectUrl;
        //       link.download = res.headers.filename;
        //       link.click();
        //     })
        //     .catch(err => {
        //       console.log('Error', err);
        //     }).finally(
        //         endLoading()
        // )
    };

    const downloadDetail = () => {
        let param = detailList.value.filter(el => {
            return el.checkYn === 'Y';
        });
        let checkedObjList;
        let writeYear;

        if (!param.length) {
            checkedObjList = detailList.value.map((item, index) => ({
                writeYear: item.writeYear,
                docType: item.docType,
                docNo: item.docNo,
                docSeq: item.docSeq,
                orgnId: item.orgnId
            }));
        } else {
            checkedObjList = param.map((item, index) => ({
                writeYear: item.writeYear,
                docType: item.docType,
                docNo: item.docNo,
                docSeq: item.docSeq,
                orgnId: item.orgnId
            }));
        }
        writeYear = checkedObjList != null && checkedObjList.length !== 0 ? checkedObjList[0].writeYear : null;

        confirmMsg('info', t('msgCheckedPrint'), null, {
            fun: () => {
                let searchVO = { writeYear: writeYear, checkedObjList: checkedObjList };
                baseDownload(getHseKeyPerformanceResultReport, searchVO);
                // openLoading();
                // getHseKeyPerformanceResultReport({ writeYear: writeYear,checkedObjList: checkedObjList })
                //     .then(res => {
                //       const link = document.createElement('a');
                //       const objectUrl = window.URL.createObjectURL(res.data);
                //       link.href = objectUrl;

                //       // 파일 이름이 없을 경우 기본값 설정
                //       const filename = res.headers?.filename || 'downloaded_file';
                //       link.download = filename;
                //       link.click();

                //       // URL 객체 해제
                //       window.URL.revokeObjectURL(objectUrl);
                //     })
                //     .catch(err => {
                //       console.error('Error:', err);
                //     })
                //     .finally(() => {
                //       endLoading();
                //     });
            }
        });
    };

    return {
        inputForm,
        searchParam,
        orgnItem,
        orgnPopup,
        initInputForm,
        goDetail,
        setRefs,
        searchClientGrid,
        searchMain,
        mainList,
        segments,
        searchDetail,
        detailList,
        signature,
        cmd,
        writeYear,
        saveHseKeyPerformanceResult,
        deleteHseKeyPerformanceResult,
        addHseKeyPerformanceResult,
        downloadMaster,
        downloadDetail
    };
});
