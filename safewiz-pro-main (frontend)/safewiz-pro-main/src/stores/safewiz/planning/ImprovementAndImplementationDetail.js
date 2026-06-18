import { defineStore } from 'pinia'
import router from '@/router'
import { nextTick } from 'vue'
import { searchDataDetailApi, getReportApi } from '@/stores/safewiz/planning/api/ImprovementAndImplementationApi.js'
import BaseView from '@/components/base/BaseView'
import { gsap } from 'gsap'
import { getSystemCode } from '@/stores/system/setting/api/SystemCode.js'
import _ from 'lodash'
const { t, ref, alertMsg, openLoading, endLoading, confirmMsg, baseDownload, formatDate } = BaseView()

export const useImprovementAndImplementationDetailStore = defineStore('improvementAndImplementationDetail', () => {
    const prevFileList = ref([])
    const afterFileList = ref([])
    const sysFrequencyList = ref([])
    const sysImpactList = ref([])
    const sysRiskScoreList = ref([])
    const sysRiskScorePopupList = ref([])
    const riskAllowance = ref('');
    const isRiskAllowance = ref(false);
    const riskAllowanceStandards = ref('');

    const searchForm = ref({
        writeYear: '',
        docType: '',
        docNo: '',
        docSeq: '',
    })
    const inputForm = ref({})

    const initInputForm = () => {
        inputForm.value = {
            writeYear: '',
            docType: '',
            docNo: '',
            docSeq: '',
            planNm: '',
            riskAssessmentStandards: '',
            riskAllowanceStandards: '',
            processNm: '',
            workContent: '',
            hazardsClassificationNm: '',
            hazardsCause: '',
            hazardsFactor: '',
            legalId: '',
            legalNm: '',
            relatedEvidence: '',
            frequencyScore: '',
            impactScore: '',
            afterFrequencyScore: '',
            afterImpactScore: '',
            afterRiskScore: '',
            riskScore: '',
            currentSafetyMeasures: '',
            workDetail: [],
        }
    }

    const toggleAccordion = async event => {
        const button = event.currentTarget
        const segmentElement = button.nextElementSibling
    
        const isOpen = button.classList.toggle('active')
    
        await nextTick()
        
        gsap.to(segmentElement, {
            height: isOpen ? 'auto' : 0,
            duration: 0.5,
            ease: 'customEase'
        })
    }

    const goBack = () => {
        initInputForm()
        router.push({ path: '/ImprovementAndImplementation' })
    }

    // 팝업 데이터 정규화
    const normalizeRiskScoreList = (rawList, standardCd) => {
        const groupedResult = {};
    
        rawList.forEach(item => {
            if (item.majorCd !== 'C0040') return;
            if (!item.minorCd.includes(standardCd)) return;
    
            const match = item.minorNm.match(/^(\d+)\[(.+)\]$/);
            if (!match) return;
    
            const num = parseInt(match[1]);
            const grade = match[2];
            const remark = item.remark;
    
            const key = `${grade}|${remark}`;
            if (!groupedResult[key]) groupedResult[key] = [];
            groupedResult[key].push(num);
        });
    
        return Object.entries(groupedResult).map(([key, numbers]) => {
            const [grade, remark] = key.split('|');
            numbers.sort((a, b) => a - b);
    
            const label = numbers.length === 1
                ? `${numbers[0]}[${grade}]`
                : `${numbers[0]}-${numbers[numbers.length - 1]}[${grade}]`;
    
            return {
                minorNm: label,
                remark: remark
            };
        });
    };
    

    const searchData = (showMsg, imgRef1, imgRef2) => {
        if (searchForm.value.writeYear.length < 1) {
            router.push('/ImprovementAndImplementation');
            return;
        }
    
        initInputForm();
    
        searchDataDetailApi(searchForm.value, showMsg).then(async res => {
            const result = res.list;
            if (result.length > 0) {
                Object.keys(inputForm.value).forEach(key => {
                    inputForm.value[key] = result[0][key];
                });
        
                // system code 먼저 조회
                const [frequencyRes, impactRes, riskScoreRes] = await Promise.all([
                    getSystemCode({ majorCd: 'C0038' }),
                    getSystemCode({ majorCd: 'C0039' }),
                    getSystemCode({ majorCd: 'C0040' }),
                ]);

                // riskAssessmentStandards를 기준으로 필터링
                const standardCd = inputForm.value.riskAssessmentStandards;

                sysFrequencyList.value = frequencyRes.list.filter(item => item.minorCd.includes(standardCd));
                sysImpactList.value = impactRes.list.filter(item => item.minorCd.includes(standardCd));
                sysRiskScoreList.value = riskScoreRes.list.filter(item => item.minorCd.includes(standardCd));

                // 정규화된 리스트
                sysRiskScorePopupList.value = standardCd === '3a' ? sysRiskScoreList.value : normalizeRiskScoreList(riskScoreRes.list, standardCd);
        
                // 점수 한글 이름으로 변환
                inputForm.value.riskScore = sysRiskScoreList.value.find(item => item.minorCd?.toLowerCase() === result[0].riskScore?.toLowerCase())?.minorNm || '';
                
                // workDetail 처리 (sysRiskScoreList가 준비된 이후)
                inputForm.value.workDetail?.forEach(wrkDtl => {
                    wrkDtl.completedDate = formatDate(wrkDtl.completedDate);
                    wrkDtl.expectedDate = formatDate(wrkDtl.expectedDate);
                    wrkDtl.workDetailHr?.forEach(hr => {
                        hr.id = hr.hrId;
                        hr.name = hr.hrNm;
                    });
                });
        
                // 허용 위험 여부 계산
                const riskScore = result[0].riskScore;

                if(riskScore){
                    const allowedStandard = result[0].riskAllowanceStandards; // 허용 가능 기준
                    if(allowedStandard !== '' && allowedStandard !== null ){
                        if (inputForm.value.riskAssessmentStandards === '3a') { // 상중하
                            riskAllowance.value = riskScore === '3a_l' ? '허용 가능한 위험' : '허용 불가능한 위험';
                            isRiskAllowance.value = riskScore === '3a_l' ? true : false;
                            const allowedRisk = allowedStandard?.split('_')[1];
                            const rankMap = { l: '하', m: '중', h: '상' };
                            riskAllowanceStandards.value = rankMap[allowedRisk];
                        } else { // 빈도/강도
                            const currentRisk = parseInt(riskScore?.split('_')[1]);
                            const allowedRisk = parseInt(allowedStandard?.split('_')[1]);
                            riskAllowance.value = currentRisk <= allowedRisk ? '허용 가능한 위험' : '허용 불가능한 위험';
                            isRiskAllowance.value = currentRisk <= allowedRisk ? true : false;
                            riskAllowanceStandards.value = allowedRisk; // 허용 기준 점수 출력
                        }
                    }else{
                        riskAllowance.value = '';
                    }
                    
                }else{
                    riskAllowance.value = '';
                }
        
                prevFileList.value = imgRef1.value;
                afterFileList.value = imgRef2.value;
        
                nextTick(() => {
                    prevFileList.value.forEach(imgItem => imgItem.fnSearch());
                    afterFileList.value.forEach(imgItem => imgItem.fnSearch());
        
                    const btn = document.getElementById('accordion-btn_0');
                    if (btn && !btn.classList.contains('active')) btn.click();
                });
            }
        });
    }
    

    const print = (param = null) => {

        if(param == null) {
            if (inputForm.value.docSeq.length < 1) {
                // toastPopup(t('tbm_msgNewData'), 'error')
                alertMsg('warning', t('msgNoItem'))
                return null
            }

            confirmMsg('info', t('msgPrint'), '', { fun: () => {
                const data = {
                    checkedObjList: [searchForm.value]
                }
                baseDownload(getReportApi, data)
                //     openLoading();
                //     getReportApi([searchForm.value], false).then(res => {
                //         const link = document.createElement('a')
                //         const objectUrl = window.URL.createObjectURL(res.data)

                //         link.href = objectUrl
                //         link.download = res.headers.filename
                //         link.click()
                //     }).finally(() => {
                //         endLoading();
                //     })
                } });
        } else {
            if (param.length < 1) {
                alertMsg('warning', t('msgNoItem'))
                return null
            }

            confirmMsg('info', t('msgCheckedPrint'), '', { fun: () => {
                const data = {
                    checkedObjList: param
                }
                baseDownload(getReportApi, data)
                    // openLoading();
                    // getReportApi(param, false).then(res => {
                    //     const link = document.createElement('a')
                    //     const objectUrl = window.URL.createObjectURL(res.data)

                    //     link.href = objectUrl
                    //     link.download = res.headers.filename
                    //     link.click()
                    // }).finally(() => {
                    //     endLoading();
                    // })
                } });
        }
    }

    return {
        searchForm,
        inputForm,
        initInputForm,
        toggleAccordion,
        goBack,
        searchData,
        print,
        sysFrequencyList,
        sysImpactList,
        sysRiskScoreList,
        sysRiskScorePopupList,
        riskAllowance,
        isRiskAllowance,
        riskAllowanceStandards
    }
})