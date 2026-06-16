import { defineStore } from 'pinia';

import router from '@/router';
import _ from 'lodash';
import { gsap } from 'gsap';
import CustomEase from 'gsap/CustomEase';

import BaseView from '@/components/base/BaseView';
const { ref, t, getCurrentDate, confirmMsg, alertMsg, nextTick, baseDownload, getCompId, formatDate } = BaseView();
import { getLegalReviewList, deleteLegalReview, getLegalReviewReportAll } from '@/stores/safewiz/planning/api/LegalReviewApi.js';

import { useLegalMgmtAndComplianceStore } from '@/stores/safewiz/planning/LegalMgmtAndComplianceEvaluation.js';
const legalMgmtAndComplianceStore = useLegalMgmtAndComplianceStore();

import { useLoadingPanelStore } from '@/stores/loadingPanel.js';
const loadingPanelStore = useLoadingPanelStore();

import { useButtonListStore } from '@/stores/buttonList';
const buttonListStore = useButtonListStore();

import { createSignatureStore } from '@/stores/signature';
const signatureStore = createSignatureStore(); // 컴포넌트마다 고유한 Store 생성


export const useLegalReviewStore = defineStore('LegalReview', () => {
    const searchTerm = ref('');
    const filteredLegalReviewList = ref([]);
    const applyFilter = async () => {
        const filteredData = legalReviewList.value.filter(item => item.docNo.toLowerCase().includes(searchTerm.value.toLowerCase()) || item.createdAt.toLowerCase().includes(searchTerm.value.toLowerCase()) || item.cnt.toLowerCase().includes(searchTerm.value.toLowerCase()));
        filteredLegalReviewList.value = filteredData;

        searchClientGrid(filteredLegalReviewList.value);
    };
    // 아코디언 관리
    const legalReviewSegments = ref([]);
    const segments = ref([]);

    // APIs
    const legalReviewList = ref({});
    const checkedList = ref([]);
    const searchDate = ref({});
    const accordionRefs = ref([]);

    const initSearchDate = async () => {
        searchDate.value = {
            writeYear: legalMgmtAndComplianceStore.searchParam.searchText.substring(0, 4),
            docType: null,
            docNo: null,
            cmd: 'U',
            compId: getCompId()
        };
        searchTerm.value = '';
        checkedList.value = [];
    };

    // // 아코디언 토글
    // gsap.registerPlugin(CustomEase);
    // CustomEase.create('customEase', 'M0,0 C0.9,0 0.2,1 1,1');

    // 아코디언 세팅
    const searchClientGrid = async (val, expandYear = null) => {
        segments.value = [];
        legalReviewSegments.value = {};

        for (let i of val) {
            const matchingYears = segments.value.filter(segment => segment.year.includes(i.writeYear));
            i.detail = {
                ['문서번호']: i.docNo,
                ['등록일자']: formatDate(i.createdAt),
                ['등록 건수']: i.cnt
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

        const targetYear = expandYear ?? new Date().getFullYear(); // 넘어온 연도 or 올해

        let index = segments.value.findIndex(item => item.year.slice(0, 4) == targetYear);

        if (index === -1) {
            segments.value.unshift({ year: `${targetYear}년도` });
            index = 0;
        }

        await nextTick();

        const btn = document.getElementById(`accordion-btn_${index}`);
        if (btn && !btn.classList.contains('active')) {
            btn.click();
        }
    };

    const btnSearch = async (notify, expandYear = null) => {
        loadingPanelStore.openLoading();

        await getLegalReviewList(searchDate.value, notify).then(res => {
            legalReviewList.value = res.list;
            searchClientGrid(legalReviewList.value, expandYear);
            filteredLegalReviewList.value = _.cloneDeep(legalReviewList.value);
        });

        nextTick(() => {
            loadingPanelStore.endLoading();
        });
    };

    const setSearchDate = async item => {
        if (!item) {
            searchDate.value.writeYear = legalMgmtAndComplianceStore.searchParam.searchText.substring(0, 4);
            searchDate.value.docNo = null;
            searchDate.value.docType = null;
            searchDate.value.cmd = 'I';
            searchDate.value.dataType = '0001';
        } else {
            searchDate.value.writeYear = item.writeYear;
            searchDate.value.docNo = item.docNo;
            searchDate.value.docType = item.docType;
            searchDate.value.cmd = 'U';
            searchDate.value.dataType = '0002';
        }
    };

    const btnDetail = async item => {
        await setSearchDate(item);
    };

    const fileInfo = file => {
        fileInfo.value = file.value;
    };

    const isCanAddPolicy = () => {
        let filteredKey = Object.keys(legalReviewList.value).filter(el => el.substring(0, 4) === getCurrentDate().substring(0, 4));
        if (filteredKey.length === 0) return true;
        if (legalReviewList.value[filteredKey].filter(el => el.useYn === 'Y').length > 0) {
            return false;
        } else return true;
    };

    const btnDelete = async () => {
        let param = checkedList.value;
        if (!param.length) {
            alertMsg('warning', '선택된 항목이 없습니다.');
            return;
        }
        if (param.some(el => el.useYn === 'N')) {
            alertMsg('warning', '이미 삭제 처리된 항목입니다.');
            return;
        }
        confirmMsg('warning', '삭제 하시겠습니까?', ``, { fun: deleteAction, param: param });
    };

    const deleteAction = async list => {
        loadingPanelStore.openLoading();

        deleteLegalReview(list, true).then(() => {
            
            list.forEach(async item => {
                await signatureStore.forcedUpdateTaskUseYn('N',item.docType,item.writeYear, item.docNo);
            });
            btnSearch();
        });
        nextTick(() => {
            loadingPanelStore.endLoading();
        });
    };

    const downloadReport = () => {
        let param = checkedList.value;

        if (!param.length) {
            alertMsg('warning', '선택된 항목이 없습니다.');
            return false;
        }
            confirmMsg('info', t('msgCheckedPrint'), '', {
                fun: async () => {
                    baseDownload(getLegalReviewReportAll, { checkedObjList: param });

                    // param[0].type = buttonListStore.downloadType;
                    // loadingPanelStore.openLoading();
                    // await getLegalReviewReportAll(param, true).then(res => {
                    //     let link = document.createElement('a');
                    //     // 객체를 만들어서 생성
                    //     let objectUrl = window.URL.createObjectURL(res.data);
                    //     link.href = objectUrl;
                    //     link.download = res.headers.filename;
                    //     link.click();
                    // });
                    // nextTick(() => {
                    //     loadingPanelStore.endLoading();
                    // });
                }
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

    return {
        // 아코디언
        toggleAccordion,
        legalReviewSegments,
        legalReviewList,
        fileInfo,
        searchTerm,
        filteredLegalReviewList,
        checkedList,
        segments,
        accordionRefs,
        // function
        applyFilter,
        isCanAddPolicy,
        initSearchDate,
        // api
        btnSearch,
        btnDetail,
        btnDelete,
        searchDate,
        downloadReport
    };
});
