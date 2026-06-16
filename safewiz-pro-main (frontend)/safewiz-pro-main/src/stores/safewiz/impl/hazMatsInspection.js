import { defineStore } from 'pinia'
import { nextTick } from "vue"
import router from '@/router'
import BaseView from '@/components/base/BaseView'
const {
  ref,
  t,
  computed,
  formatDate, formatDateForDB,
  baseDownload,
  alertMsg,
  confirmMsg,
  getCompId,
  getCurrentDate,
  openLoading, endLoading, downloadReport
} = BaseView()


import { gsap } from 'gsap';

import {
  getHazmatChecklist,
  getHazmatChecklistDetail,
  saveHazmatChecklist,
  deleteHazmatChecklistDetail,
  getLastChecklist,
  getHazmatChecklistReport,
  getHazmatStatusReport
} from '@/stores/safewiz/impl/api/hazMatsInspectionApi'

import _ from 'lodash'

import { useButtonListStore } from '@/stores/buttonList'
const layoutStore = useButtonListStore()

export const useHazMatsInspectionStore = defineStore(
  'hazMatsInspection',
  () => {
    const compId = getCompId()
    const writeYear = ref()
    const currentDate = ref(getCurrentDate())

    const inputForm = ref({})
    const checkedList = ref([])
    const searchedData = ref([])
    const filteredData = ref([])
    const dateList = ref([])


    const searchText = ref('')

    const inspector = ref()
    const confirmer = ref()

    const setRefs = (ref1, ref2) => {
      inspector.value = ref1.value
      confirmer.value = ref2.value
    }

    const searchParam = ref({
      compId: compId,
      writeYear: writeYear.value,
      docNo: '',
      docType: 'HCL',
      searchText: ''
    })

    const originData = ref({});

    const signature = ref(null)
    const initInputForm = () => {
      inputForm.value = {
        cmd: 'I',
        writeYear: searchParam.value.writeYear,
        docType: 'HCL',
        docNo: '',
        compId: compId,
        workplaceId: '',
        workplaceNm: '',
        checklistDt: currentDate.value,
        checklistStart: null,
        checklistEnd: null,
        detailList: [],
        deleteList: [],
        msdsList: [],
        dataList: [],
        useYn: 'Y',
        createdAt: null,
        createdBy: null
      }
    }

    // 데이터 조회
    const getHazmatCheck = notify => {
      openLoading();
      return new Promise(resolve => {
        getHazmatChecklist(searchParam.value, notify).then(res => {
          searchedData.value = res.list
          const groupedData = groupByYear(searchedData.value)
          filteredData.value = groupedData
          filter()
          // resolve({ list: searchedData.value, totalCount: res.totalCount })
        }).finally(() => endLoading());
      })
    }
    // 배열 내 checklistDt 값의 month 2자리를 추출하여 같은 값끼리 그룹화하는 함수

    const groupByYear = searchedData => {
      const grouped = searchedData.reduce((groups, item) => {
        const month = item.checklistDt.substring(4, 6) // 월 추출
        const workplaceId = item.workplaceId // workplaceId 추출
        const workplaceNm = item.workplaceNm // workplaceNm 추출

        // 월별 그룹 생성
        if (!groups[month]) {
          groups[month] = {}
        }

        // workplaceId 별로 그룹 생성
        if (!groups[month][workplaceId]) {
          groups[month][workplaceId] = { workplaceNm, data: [] }
        }

        // 해당 월과 workplaceId에 데이터를 추가
        groups[month][workplaceId].data.push(item)
        groups[month][workplaceId].writeYear = item.writeYear
        groups[month][workplaceId].docType = item.docType
        groups[month][workplaceId].docNo = item.docNo

        return groups
      }, {})

      // 그룹화된 데이터를 원하는 형식으로 변환
      return Object.keys(grouped).map(month => ({
        month: month + '월',
        data: Object.keys(grouped[month]).map(workplaceId => ({
          workplaceId: workplaceId,
          workplaceNm: grouped[month][workplaceId].workplaceNm,
          writeYear: grouped[month][workplaceId].writeYear,
          docType: grouped[month][workplaceId].docType,
          docNo: grouped[month][workplaceId].docNo,
          data: grouped[month][workplaceId].data
        }))
      }))
    }

    // 디테일 조회
    const getHazmatChecklistDetailList = notify => {
      openLoading();
      return new Promise(resolve => {
        getHazmatChecklistDetail(searchParam.value, notify).then(res => {
          if (res.list.checklistDt) {
            res.list.checklistDt = formatDate(res.list.checklistDt)
          }
          // 위험물/유해화학물질 데이터 가공
          // 조회
          if (res.list.writeYear) {
            inputForm.value = res.list
            res.list.msdsList = res.list.msdsList.map(el => ({
              id: el.msdsId,
              name: el.msdsNm
            }))
             inputForm.value.cmd = 'U'
          } else {
            //조회 값이 없을시 초기화
            inputForm.value.cmd = 'I'
            inputForm.value.writeYear = writeYear
            inputForm.value.docType = 'HCL'
            inputForm.value.docNo = ''
            inputForm.value.compId = compId
            inputForm.value.checklistStart = null
            inputForm.value.checklistEnd = null
            inputForm.value.checklistTime = null
            inputForm.value.detailList = []
            inputForm.value.msdsList = res.list.msdsList.map(el => ({
              id: el.msdsId,
              name: el.msdsNm
            }))
            inputForm.value.useYn = 'Y'
          }
          originData.value = _.cloneDeep(inputForm.value);
          resolve();
        }).finally(() => {
          endLoading()
        
          inspector.value.Search();
          confirmer.value.Search();});
      })
    }

    const groupedDetailList = computed(() => {
      return inputForm.value.detailList.reduce((groups, item) => {
        if (!groups[item.checklistNm]) {
          groups[item.checklistNm] = [];
        }
        groups[item.checklistNm].push(item);
        return groups;
      }, {});
    });

    const getVisibleIndex = (groupIndex, index) => {
      let count = 0;
      for (let i = 0; i < groupIndex; i++) {
        count += groupedDetailList.value[Object.keys(groupedDetailList.value)[i]].length;
      }
      return count + index + 1;
    };


    // 마지막 점검사항 불러오기
    const btnLastChecklist = (param, notify) => {
      openLoading();
      return new Promise(resolve => {
        getLastChecklist(param, notify).then(res => {
          inputForm.value.detailList = res.list
        }).finally(() => endLoading());
      })
    }

    // 저장(리스트)
    const btnsaveHazmatChecklist = saveParam => {
      //저장시 상담일자 날자 포맷팅
      inputForm.value.checklistDt = inputForm.value.checklistDt ? formatDateForDB(inputForm.value.checklistDt) : null;
      return new Promise(resolve => {
        openLoading();
        saveHazmatChecklist(saveParam, true)
          .then(async res => {
            if (res.success) {
              // 파라미터 세팅
              searchParam.value.docNo = res.result.docNo
              searchParam.value.compId = res.result.compId
              searchParam.value.writeYear = res.result.writeYear
              searchParam.value.docType = res.result.docType
              searchParam.value.workplaceId = res.result.workplaceId
              searchParam.value.checklistDt = res.result.checklistDt
              
              // 순차적으로 실행
              await inspector.value.setHrChipsApprovalInfo("HCL",
                  res.result.writeYear, res.result.docNo
              );
              
              await confirmer.value.setHrChipsApprovalInfo("HCL",
                  res.result.writeYear, res.result.docNo
              );

              res.result.detailList?.forEach(item => {
                item.cmd = 'U';
              })
              inputForm.value = res.result;
              inputForm.value.checklistDt = formatDate(inputForm.value.checklistDt);
            }
            getHazmatChecklistDetailList(true)
          }).catch(()=>{
            endLoading()
          })
          .finally(() => {
            endLoading();
            layoutStore.useBtnList = [
              'btnBack',
              'btnSearch',
              'btnSave',
              'btnDelete',
              'btnDownload',
            ]
          })
      })
    }

    // 디테일 삭제
    const deleteHazmatCheckDetail = saveParam => {
      openLoading();
      return new Promise(resolve => {
        deleteHazmatChecklistDetail(saveParam, true).then(res => {
          getHazmatChecklistDetailList(false)
          // resolve({ list: res.list, totalCount: res.totalCount })
          resolve(res)
        })
      }).finally(() => endLoading());

    }

    // -------------------------------------------------

    // 신규 추가 이동`
    const btnAdd = async () => {
      await initInputForm();
      router.push({
        name: 'HazMatsInspectionDetail'
      })
    }

    //목록으로 이동
    const goBack = () => {
      searchText.value = null;
      router.push({ name: 'WorkplaceSafetyGuidelines', state: { activeTab: 'info' } });
    }

    // 상세보기 이동
    const goDetail = async e => {
      initInputForm();
      inputForm.value.cmd = 'U'
      inputForm.value.workplaceId = e.workplaceId;
      inputForm.value.workplaceNm = e.workplaceNm;
      inputForm.value.detailList = e.data[0].detailList;
      inputForm.value.msdsList = e.data[0].msdsList.map(el => ({
        id: el.msdsId,
        name: el.msdsNm
      }));
      dateList.value = JSON.stringify(
        e.data
          .map(item => {
            // checklistDt 값 변환 (YYYYMMDD -> YYYY-MM-DD)
            const date = item.checklistDt;
            return `${date.slice(0, 4)}-${date.slice(4, 6)}-${date.slice(6)}`;
          })
          .filter((date, index, self) => self.indexOf(date) === index) // 중복 제거
      );
      
      inputForm.value.checklistDt = formatDate(e.data[0].checklistDt);

    }

    // -------------------------------------------------
    // 상세 삭제
    const btnDetailDelete = () => {
      confirmMsg('warning', t('msgDelete') + '\n점검사항 데이터가 초기화됩니다.', '', {
        fun: deleteHazmatCheckDetail,
        param: inputForm.value
      })
    }
    // -------------------------------------------------
    // -------------------------------------------------
    const btnSearch = async() => {
      if (!inputForm.value.checklistDt) {
        inputForm.value.checklistDt = formatDateForDB(currentDate.value);
      }
      searchParam.value.workplaceId = inputForm.value.workplaceId;
      searchParam.value.checklistDt = formatDateForDB(inputForm.value.checklistDt);

      return getHazmatChecklistDetailList(true);
    }

    // -------------------------------------------------

    //저장
    const btnSave = async () => {
      //상담시간
      if (inputForm.value.checklistTime) {
        const [
          checklistStart,
          checklistEnd
        ] = inputForm.value.checklistTime.split(' ~ ').map(time => time.trim())

        // 추출된 값을 saveParam에 저장
        inputForm.value.checklistStart = checklistStart
        inputForm.value.checklistEnd = checklistEnd
      }

      //writeYear 점검일자년도
      inputForm.value.writeYear = inputForm.value.checklistDt.slice(0, 4);
      confirmMsg('info', t('msgSave'), null, {
        fun: btnsaveHazmatChecklist,
        param: inputForm.value
      })
    }

    // -------------------------------------------------

    const segments = ref([])
    const activeSegments = ref({})

    const filter = async () => {

      // 데이터 변환을 한번에 처리하여 성능 개선
      filteredData.value.forEach(el => {
        el.data = el.data.map(id => ({
          ...id,
          detail: {
            [t('workplace_inspectionCnt')]: id.data.length + '건',
            [t('workplace_msdsList')]: id.data[0].msdsList
                .map(msds => msds.msdsNm)
                .join(', ') || ''
          }
        }))
      })
      
      if (filteredData.value.length === 0) {
        // 현재 월을 구함 (0부터 시작하므로 +1 필요)
        const currentMonth = String(new Date().getMonth() + 1).padStart(2, '0') + '월';
        // 조회 데이터가 없을 시 현재 월 빈 아코디언 세팅
        filteredData.value[0] = { month: currentMonth };
      }

      segments.value = filteredData.value

      const currentMonth = new Date().getMonth() + 1; // 현재 월 (0부터 시작하므로 +1을 해야 실제 월이 나옵니다)
      // 현재 해당하는 월에 해당하는 index를 찾기
      let index = filteredData.value.findIndex(item => item.month.slice(0, 2) == currentMonth);

      // 만약 일치하는 항목이 없으면 index를 0으로 설정
      if (index === -1) {
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

    //-----------------------------------------------
    //검색기능

    const dataFilter = () => {
      if (searchText.value) {
        const temp = []
        filteredData.value.forEach(row => {
          // 각 row의 데이터를 필터링
          const filteredColumns = row.data.filter(col =>
            row.month?.toLowerCase().includes(searchText.value.toLowerCase())||
            col.workplaceNm
              ?.toLowerCase()
                .includes(searchText.value.toLowerCase()) ||
              col.detail.물질.replace(/[\s,]/g, "")
                  ?.toLowerCase()
                  .includes(searchText.value.toLowerCase())
          )

          // 필터링된 데이터가 있을 경우만 추가
          if (filteredColumns.length > 0) {
            temp.push({
              ...row,
              data: filteredColumns // 필터링된 데이터만 포함
            })
          }
        })
        segments.value = temp
      } else {
        segments.value = filteredData.value
      }
    }

    //-----------------------------------------------
    //useYn 체크

    const toggleUseYn = (field, event) => {
      // 체크박스의 체크 상태에 따라 'Y' 또는 'N'을 설정합니다.
      inputForm.value[field] = _.cloneDeep(event.target.checked ? 'Y' : 'N')
    }

    // -------------------------------------------------

    // 현황표
    const btnStatus = () => {
      let searchVO = _.cloneDeep(searchParam.value);

      if(!searchVO.workplaceId){
        alertMsg('warning', '작업장을 선택해주세요.');
        return;
      }

      if(!searchVO.checklistDt){
        alertMsg('warning', '등록된 위험물/유해화학물질이 없습니다.');
        return;
      }
      
      searchVO.searchText = searchVO.workplaceId; // workplaceId 값을 searchText로 대체
      searchVO.searchText2 = searchVO.checklistDt; // workplaceId 값을 searchText로 대체
      searchVO['type'] = 'xls';
      confirmMsg('warning', '현황표를 출력하시겠습니까?', null, { fun: onReportPrint, param: searchVO });
    }
    const onReportPrint = async searchVO => {
      openLoading()
      getHazmatStatusReport(searchVO).then((res) => {
        downloadReport(res);
      }).finally(()=>{
        endLoading()
      });
    }


    // 출력물
    const btnDownload = async list => {
      if (list.length > 0) {
        let searchVO = _.cloneDeep(searchParam.value)
        searchVO.docType = 'HCL'
        searchVO.checkedList = list
        baseDownload(getHazmatChecklistReport, searchVO);
        // openLoading()
        // getHazmatChecklistReport(searchVO).then(res => {
        //   downloadReport(res)
        // }).finally(()=>{
        //   endLoading()
        // });
      } else {
        alertMsg('warning', t('msgNoItem'))
        return
      }
    }

    const btnDetailDownload = async list => {
      if (list.length > 0) {
        let searchVO = _.cloneDeep(searchParam.value)
        searchVO.docType = 'HCL'
        searchVO.checkedList = list
        baseDownload(getHazmatChecklistReport, searchVO);
        // openLoading();
        // getHazmatChecklistReport(searchVO).then(res => {
        //   downloadReport(res)
        // }).finally(() => endLoading());
      } else {
        alertMsg('warning', t('msgNoItem'))
        return
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

    // -------------------------------------------------

    return {
      originData,
      dateList,
      toggleAccordion,
      inputForm,
      initInputForm,
      checkedList,
      filter,
      dataFilter,
      searchText,
      signature,
      setRefs,
      toggleUseYn,
      searchParam,
      currentDate,
      groupedDetailList,
      getVisibleIndex,
      segments,
      activeSegments,
      inspector,
      confirmer,
      //버튼
      btnAdd,
      goBack,
      goDetail,
      btnSearch,
      btnSave,
      btnDetailDelete,
      btnDownload,
      btnDetailDownload,
      btnStatus,
      //API
      getHazmatCheck,
      getHazmatChecklistDetailList,
      btnLastChecklist,
      btnsaveHazmatChecklist,
      deleteHazmatCheckDetail
    }
  }
)
