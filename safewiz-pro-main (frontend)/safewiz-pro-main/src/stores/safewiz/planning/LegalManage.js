import { defineStore } from "pinia";
import router from '@/router';
import _ from 'lodash';
import BaseView from '@/components/base/BaseView';
import { nextTick } from "vue";
import { gsap } from 'gsap';

const { confirmMsg, alertMsg, ref, t, baseDownload, formatDate, getCompId } = BaseView();

import { getLegalManageList, deleteLegalManage, getLegalManageReportAll, confirmLegalManage, getLegalManageListPopup } from '@/stores/safewiz/planning/api/LegalManageApi.js';
import { useLegalMgmtAndComplianceStore } from '@/stores/safewiz/planning/LegalMgmtAndComplianceEvaluation.js';
import { useButtonListStore } from '@/stores/buttonList';

import { createSignatureStore } from '@/stores/signature';
const signatureStore = createSignatureStore(); // 컴포넌트마다 고유한 Store 생성

const legalMgmtAndComplianceStore = useLegalMgmtAndComplianceStore();
const buttonListStore = useButtonListStore();

export const useLegalManageStore = defineStore("LegalManage", () => {
  const searchTerm = ref('');
  const legalManageList = ref([]);
  const filteredLegalManageList = ref([]);
  const searchDate = ref({});

  // 아코디언 관리
  const legalManageSegments = ref([]);
  const segments = ref([]);

  // 확정/미확정 리스트
  const confirmedLegal = ref([]);
  const unConfirmedLegal = ref([]);

  // 검색
  const applyFilter = () => {
    const term = searchTerm.value.toLowerCase();
    filteredLegalManageList.value = legalManageList.value.filter(item =>
      item.legalNm.toLowerCase().includes(term) ||
      item.docNo.toLowerCase().includes(term) ||
      item.divFg.toLowerCase().includes(term) ||
      item.createdAt.toLowerCase().includes(term) ||
      item.revisionAt.toLowerCase().includes(term)
    );
    initConfirmedLegal(filteredLegalManageList.value);
  };

  // 차수 데이터 적용
  const initConfirmedLegal = (list) => {
    confirmedLegal.value = list
    .filter(item => item.confirmedYn === 'Y')
    .map(el => ({
      ...el,
      detail: {
        문서번호: el.docNo,
        구분: el.divFg,
        확정일자: el.confirmedDt ? formatDate(el.confirmedDt) : '-',
        제정개정일자: formatDate(el.revisionAt)
      }
    }));

  unConfirmedLegal.value = list
    .filter(item => item.confirmedYn === 'N')
    .map(el => ({
      ...el,
      detail: {
        문서번호: el.docNo,
        구분: el.divFg,
        확정일자: el.confirmedDt ? formatDate(el.confirmedDt) : '-',
        제정개정일자: formatDate(el.revisionAt)
      }
    }));
  };

  // 조회 조건 초기화
  const initSearchDate = async () => {
    searchDate.value = {
      docType: null,
      docNo: null,
      cmd: 'U',
      dataType: '',
      compId: getCompId()
    };
    searchTerm.value = '';
  };

  // 조회 조건 적용
  const setSearchDate = async (item,writeYear) =>{
    if(!item){
      searchDate.value.writeYear = writeYear ? writeYear : legalMgmtAndComplianceStore.searchParam.searchText.substring(0, 4);
      searchDate.value.docNo = null;
      searchDate.value.docType = null;
      searchDate.value.cmd = 'I';  
      searchDate.value.dataType = '0001';
      searchDate.value.confirmedYn = 'N';
      searchDate.value.compId = getCompId();
    }else{
      searchDate.value.writeYear = item.writeYear;
      searchDate.value.docNo = item.docNo;
      searchDate.value.docType = item.docType;
      searchDate.value.cmd = 'U';
      searchDate.value.dataType = '0002';
      searchDate.value.compId = getCompId();

      console.log('writeYear', writeYear);
      console.log('searchDate.value', searchDate.value);
    }
  }

  // 조회 버튼
  const btnSearch = async (notify) => {
    const res = await getLegalManageList(searchDate.value, notify);
    legalManageList.value = res.list;
    filteredLegalManageList.value = _.cloneDeep(legalManageList.value);
    initConfirmedLegal(filteredLegalManageList.value);
  };

  // 아코디언 세팅
  const searchClientGrid = async (val) => {
    segments.value = []
    legalManageSegments.value = {}
    for (let i of val) {
      const matchingYears = segments.value.filter(segment => 
        segment.year.includes(i.writeYear)
      )

      i.detail = {
        ['문서번호']: i.docNo,
        ['구분']: i.divFg,
        ['확정일자']: i.confirmedDt ? formatDate(i.confirmedDt) : '-',
        ['제정/개정 일자']: formatDate(i.revisionAt)
      }

      if(matchingYears.length == 0) {
        segments.value.push({ 
          year: i.writeYear + '년도', 
          dataList: [i]
        })
      } else {
        matchingYears[0].dataList.push(i)
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

  // 위험성 평가 이행 팝업
  const popSearch = async (notify)=>{
    await getLegalManageListPopup(searchDate.value, notify).then(res => {
      
      legalManageList.value = res.list
      searchClientGrid(legalManageList.value);
      filteredLegalManageList.value = _.cloneDeep(legalManageList.value)
      console.log('searchDate.value',searchDate.value)
      console.log('filteredLegalManageList.value',filteredLegalManageList.value)
    })
  }

  const btnDetail = async (item, btnType) => {
    await setSearchDate(item, btnType);
  };

  const deleteAction = async (list) => {
    await deleteLegalManage(list, true);
    list.forEach(async item => {
      await signatureStore.forcedUpdateTaskUseYn('N',"LGM",item.writeYear, item.docNo);
    });
    await btnSearch();
    initConfirmedLegal();
  };

  const btnDelete = async () => {
    const unConfirmedParam = unConfirmedLegal.value.filter(item => item.checked);
    const confirmedParam = confirmedLegal.value.filter(item => item.checked);

    if (!unConfirmedParam.length && !confirmedParam.length) {
      alertMsg('warning', '선택된 항목이 없습니다.');
      return;
    }

    if(confirmedParam.length > 0){
      alertMsg('warning', '확정 차수는 삭제할 수 없습니다.');
      confirmedParam.forEach(item => {
        item.selected = false;
      })
      return;
    }

    if (unConfirmedParam.some(el => el.useYn === 'N')) {
      alertMsg('warning', '이미 삭제 처리된 항목입니다.');
      return;
    }
    confirmMsg('warning', '삭제 하시겠습니까?', '', { fun: deleteAction, param: unConfirmedParam });
  };

  const downloadReport = async () => {
    const unConfirmedParam = unConfirmedLegal.value.filter(item => item.checked);
    const confirmedParam = confirmedLegal.value.filter(item => item.checked);

    if(!unConfirmedParam.length && !confirmedParam.length){
      alertMsg('warning', t('msgNoItem'));
      return;
    }

    // 선택된 확정 차수 + 차수 리스트 병합 
    const param = [...unConfirmedParam, ...confirmedParam];

    confirmMsg('info', t('msgCheckedPrint'), null, { fun: downAction, param: param });
  };

  const downAction = (param) => {
    baseDownload(getLegalManageReportAll, { checkedObjList: param });
  };

  // 확정전환/확정취소 적용
  const updateConfirmLegalManage = async (item) => {
    await confirmLegalManage(item);
    await btnSearch();
  };

  return {
    // 아코디언
    toggleAccordion, legalManageSegments, legalManageList,

    // 카드
    confirmedLegal, unConfirmedLegal, searchTerm, filteredLegalManageList, searchDate,

    // function
    applyFilter, initSearchDate, updateConfirmLegalManage, popSearch,

    // api
    btnSearch, btnDetail, btnDelete, downloadReport
  };
});
