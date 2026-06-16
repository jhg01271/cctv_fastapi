import { defineStore } from "pinia"
// import { ref } from "vue"
import router from '@/router';
import BaseView from '@/components/base/BaseView';
const { ref, formatDate, baseDownload  } = BaseView();

import {
  searchEmployeesStakeholders,
  searchYear,
  deleteEmployeesStakeholdersList,
  searchEmployeesStakeholdersDetail,
  addEmployeesStakeholdersDetail,
  modifyEmployeesStakeholdersDetail,
  deleteEmployeesStakeholdersDetail,
  getReport,
} from "@/stores/safewiz/orgstatus/api/employeesAndStakeholdersApi"


const dataList = ref([]);

export const useEmpStakeholdersStore = defineStore("employeesAndStakeholders", () => {

  const inputForm = ref({
    cmd: '',
    writeYear: '',
    writeDt: '',
    docType: '',
    docNo: '',
    docSeq: '',
    orgnId: '',
    orgnSelectList: [],
    orgnIdList: [],
    signList: [],
  });
  const checkedList = ref([]);
  const searchedData = ref([]);
  const filteredData = ref([]);
  const searchText = ref();
  const signature = ref(null);
  const initInputForm = () => {
    inputForm.value = {
      cmd: '',
      writeYear: '',
      writeDt: '',
      docType: '',
      docNo: '',
      docSeq: '',
      orgnId: '',
      orgnSelectList: [],
      orgnIdList: [],
      signList: [],
      // cmd: '', // 신규 추가 : 'I', 기존 데이터 조회 : 'U'
      // searchFrom: '', // 년도
      // orgnId: '', // 조직코드
      // orgnIdList: [], 
      // gubun: '', // 구분
      // stakeholders: '', // 이해관계자
      // requirements: '', // 요구사항
      // obligation: '', // 준수 의무사항
      // risk: '', // 리스크
      // chance: '', // 기회
      // approvalKey: '', // 결재 키
      // approvalStatus: '', // 결재 상태
      // createAt: '', // 등록일시
      // createdBy: '', // 작성자
      // comp_id: '', // 사업장ID
      // head1: [], // 작성
      // head2: [], // 검토
      // head3: []  // 결재
    }
  };

  // 년도 조회
  const year = (searchParam) => {
    return new Promise((resolve) => {
      searchYear(searchParam).then(res => {
        resolve({ list: res.list, totalCount: res.totalCount })
      })
    })
  }

  // 데이터 조회
  const searchEmployeesAndStakeholders = (searchParam, notify) => {
    return new Promise((resolve) => {
      searchEmployeesStakeholders(searchParam, notify).then(res => {
        searchedData.value = res.list
        resolve({ list: searchedData.value, totalCount: res.totalCount })
      })
    })
  };


  // 삭제(리스트)
  const deleteEmployeesStakeholders2 = (saveParam) => {
    return new Promise((resolve) => {
      deleteEmployeesStakeholdersList(saveParam, 'delete').then(res => {
        // console.log('deleteEmployeesStakeholdersList res', res)
        // resolve({ list: res.list, totalCount: res.totalCount })
        resolve(res)
      })
    })
  }

  //detail 조회
  const searchESHDetail = (searchParam) => {    
    return new Promise((resolve) => {
      searchEmployeesStakeholdersDetail(searchParam).then(res => {
        resolve({ list: res.list, list2: res.list2, totalCount: res.totalCount })
      })
    })
  }

  //detail 저장
  const addESHDetail = (saveParam) => {
    return new Promise((resolve) => {
      addEmployeesStakeholdersDetail(saveParam, 'post').then(res => {
        resolve(res)
      })
    })
  }

  //detail 수정
  const modifyESHDetail = (saveParam) => {
    return new Promise((resolve) => {
      modifyEmployeesStakeholdersDetail(saveParam, 'post').then(res => {
        resolve(res)
      })
    })
  }

  //detail 삭제
  const deleteESHDetail = (deleteParam) => {
    return new Promise((resolve) => {
      deleteEmployeesStakeholdersDetail(deleteParam, 'delete').then(res => {
        resolve(res)
      })
    })
  }

  // 신규 추가 이동
  const goInsert = () => {
    inputForm.value.cmd = 'I';
    router.push({
      name: 'EmployeesAndStakeholdersDetail'
    });
  };

  //목록으로 이동
  const goBack = () => {
    //검색어 초기화
    initInputForm();
    router.push({ name: 'EmployeesAndStakeholders' });
  };

  // 상세보기 이동
  const goDetail = param => {
    inputForm.value.cmd = 'U';
    inputForm.value.writeYear = param.writeYear;
    inputForm.value.docType = param.docType;
    inputForm.value.docNo = param.docNo;
    inputForm.value.orgnSelectList = [{ id: param.orgnId, name: param.orgnNm}]
    inputForm.value.writeDt = formatDate(param.writeDt);
  };

  // 출력물 다운로드
  const downloadReport = (list) => {
    // openLoading()
    // return new Promise((resolve) => {
    //   getReport(inputForm.value, false).then(res => {

    //     resolve({ result: res.result, success: res.success })
    //     let link = document.createElement('a')
    //     // 객체를 만들어서 생성
    //     console.log('response', res)

    //     let objectUrl = window.URL.createObjectURL(res.data)
    //     link.href = objectUrl
    //     link.download = res.headers.filename
    //     link.click()
    //   }).catch(() => {
    //     endLoading()
    //   }).finally(() => {
    //     endLoading()
    //   })
    // })
    baseDownload(getReport, {writeYear: searchText.value, checkedObjList: list})
  }

  //팝업 닫기
  const closeOrgn = (el) => {
    console.log(' el ', el)
    if (el.length > 0) {

      inputForm.value.orgnSelectList = []
      inputForm.value.orgnId = ''

      for (let i of el) {
        inputForm.value.orgnId = i.orgnId
        inputForm.value.orgnSelectList.push({
          id: i.orgnId,
          name: i.orgnNm
        })
      }
    }
  };

  const dataFilter = () => {
    // console.log('searchText', searchText.value)

    return new Promise((resolve) => {
      if(searchText.value.length > 0) {
        const temp = []
        searchedData.value.forEach(row => {
          let isExist = false
          Object.keys(row).forEach(col => {
            console.log('필터', col )
            if (col === 'createdAt' || col === 'createdBy' || col === 'orgnNm' || col === 'writeYear') {
              if(row[col].toLowerCase().includes(searchText.value.toLowerCase())) {
                isExist = true
                return
              }
            }
          })

          // console.log('isExist', isExist)
          if(isExist) {
            temp.push(row)
          }
        })

        filteredData.value = temp

        resolve(filteredData.value)
      } else {
        resolve(searchedData.value)
      }
    })
  };

  return {
    inputForm, initInputForm,
    searchYear, dataList,
    searchEmployeesAndStakeholders, deleteEmployeesStakeholders2,
    searchESHDetail, addESHDetail,modifyESHDetail, deleteESHDetail,
    goInsert, goBack, goDetail, checkedList, closeOrgn,//, btnDelete
    dataFilter, searchText, downloadReport,
    signature  }
})
