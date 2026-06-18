import { defineStore } from "pinia"
import { nextTick } from "vue"
import router from '@/router';
import BaseView from '@/components/base/BaseView';
const { ref, t, formatDate, confirmMsg, alertMsg, getCompId, getCurrentDate, openLoading, endLoading, formatDateForDB } = BaseView();
import { gsap } from 'gsap';

import { getManageDoc, getManageDocDetail, saveManageDoc, deleteManageDoc, deleteManageDocs } from "@/stores/safewiz/support/api/docMngApi"
import { Exception } from "sass";
import { getSystemCode } from '@/stores/safewiz/dataset/api/datasetApi';
import _ from 'lodash';
export const useDocMngStore = defineStore("docMng", () => {
  
  const compId = getCompId();
  const writeYear = ref(getCurrentDate().substring(0, 4));
  const currentDate = ref(getCurrentDate());
  const fileInfo = ref([]);
  // 파일 ref
  const file = (file) => {
    fileInfo.value = file.value;
  };

  const inputForm = ref([]);
  const checkedList = ref([]);
  const searchedData = ref([]);
  const filteredData = ref([]);
  const searchText = ref('');

  const searchParam = ref({
    compId: compId,
    writeYear: writeYear.value,
    docNo: '',
    docType: 'DOC'
  })

  const signature = ref(null);
  const initInputForm = () => {
    inputForm.value =[ 
      {
      cmd: 'I',
      writeYear: '',
      writerId: '',
      docType: 'DOC',
      docNo: '',
      docSeq: '',
      useYn: 'Y',
      createdAt:'',
      manageDocNm:'',
      files:[]
    }
  ]
  };

  // 데이터 조회
  const getMngDoc = () => {
    return new Promise((resolve) => {
      openLoading();
      getManageDoc(searchParam.value, true).then(res => {
        searchedData.value = res.list
        const groupedData = groupByYear(searchedData.value);

        //년도를 순차적으로 넣기위해 데이터 재정렬
        let sortGroupedData = []
        for(let i = groupedData.length; i > 0; i--){
          sortGroupedData.push(groupedData[i-1])
        }
        filteredData.value = sortGroupedData

        filter();
        // resolve({ list: searchedData.value, totalCount: res.totalCount })
      }).catch(() => {
        endLoading()
      }).finally(() => {
        endLoading()
      })
    });
  }

  // 배열 내 writeDt 값의 앞 4자리를 추출하여 같은 값끼리 그룹화하는 함수
  const groupByYear = (searchedData) => {
    const grouped = searchedData.reduce((groups, item) => {
      const year = item.writeDt.substring(0, 4);
      if (!groups[year]) {
        groups[year] = [];
      }
      groups[year].push(item);
      return groups;
    }, {});
    // 그룹화된 데이터를 원하는 형식으로 변환

    return Object.keys(grouped).map(year => ({
      year: year,
      data: grouped[year]
    }));

  };


  // 디테일 조회
  const getMngDocDetail = (notify) => {
    return new Promise((resolve) => {
      openLoading();
      getManageDocDetail(searchParam.value, notify).then(async res => {
        // First forEach
        inputForm.value = res.list;
        inputForm.value .forEach(el =>{
          el.writeDt = formatDate(el.writeDt)
          el.count = el.files.length;
        })
        
        // Second forEach with Promise.all to wait for all fnSearch calls
        await Promise.all(inputForm.value .map((val, index) => {
          return fileInfo.value[index].fnSearch(val.files);
        }));
        
      }).catch((err) => {
        console.error(err);
      }).finally(() => {
        endLoading();
      });
    })
  };


  // 저장(리스트)
  const saveMngDoc = (saveParam) => {
    
    return new Promise((resolve) => {
      openLoading();
      saveManageDoc(saveParam, true).then(res => {
        if (res.success) {
          searchParam.value.docNo = res.result.docNo
          getMngDocDetail()
        }
      }).catch(() => {
        getMngDocDetail()
        endLoading()
      }).finally(() => {
        getMngDocDetail()
        endLoading();
      })
    })
  }

  // 삭제(리스트)
  const deleteMngDocs = (saveParam) => {
    return new Promise((resolve) => {
      openLoading();
      deleteManageDocs(saveParam, true).then(res => {
        getMngDoc(false);
        resolve(res);
      }).catch(() => {
        endLoading()
      }).finally(() => {
        endLoading();
      })
    })
  }

  const deleteDetailDocs = (saveParam) => {
    return new Promise((resolve) => {
      openLoading();
      deleteManageDocs(saveParam, true).then(res => {
        getMngDocDetail(false);
        resolve(res);
      }).catch(() => {
        endLoading()
      }).finally(() => {
        endLoading();
      })
    })
  }
// -------------------------------------------------

  // 신규 추가 이동
  const btnAdd = () => {
    router.push({
      name: 'DocumentManageDetail'
    });
  };

  //목록으로 이동
  const goBack = () => {
    initInputForm();
    router.push({ name: 'DocumentManage' });
  };

  // 상세보기 이동
  const goDetail = param => {
    inputForm.value.forEach(el => {
      el.cmd = 'U',
      el.writeYear = param.writeYear,
      el.docType = param.docType,
      el.docNo = param.docNo
    });
    searchParam.value.writeYear = param.writeYear;
    searchParam.value.docType = param.docType;
    searchParam.value.docNo = param.docNo;
    router.push({
      name: 'DocumentManageDetail'
    });
    getMngDocDetail();
   
  };
// -------------------------------------------------
  const btnDelete = async () => {
    checkedList.value = []
    segments.value.forEach(i => {
      i.data.forEach(el => {
        if (el.checked) {
          checkedList.value.push(el)
        } 
      })
     
    })
    if (checkedList.value.length < 1) {
      alertMsg('warning', t('msgNoItem'));
      return;
    }
    if (checkedList.value.some(el => el.useYn === 'N')) {
      alertMsg('warning', t('msgDeletedItem'));
      return;
    }
    confirmMsg('warning', t('msgDelete'), ``, {
      fun: deleteMngDocs,
      param: checkedList.value
    });
  }
  // -------------------------------------------------
//저장
  const btnSave  = async () => {
    const formData = new FormData();
    const fileKeys = [];
    let cloneWriteDt = _.cloneDeep(inputForm.value[0].writeDt)
    inputForm.value.forEach((el, index) => {
      //작성일 '.' 제거하고 저장
      el.writeDt = formatDateForDB(el.writeDt);
      // 년도 = 작성일의 년도
      el.writeYear = el.writeDt.substring(0, 4);

      // 파일 삭제 정보 추가
      el.deleteFiles = fileInfo.value[index] ?.editFiles ?.delete || [];

      // 파일 추가 처리
      fileInfo.value[index] ?.editFiles ?.insert.forEach(file => {
        if (file) {
          formData.append(`files`, file);
          fileKeys.push(el.manageDocType);
        }
      });
    });

    formData.append('fileKeys', new Blob([JSON.stringify(fileKeys)], { type: 'application/json' }));

    // 상세 데이터 추가
    formData.append('info', new Blob([JSON.stringify(inputForm.value)], { type: 'application/json' }));
    
    // 데이터 저장 함수 호출
    confirmMsg('info', t('msgSave'), null, { fun: saveMngDoc, param: formData });

    //writeDt에서 제거된 . 복구
    inputForm.value.forEach((el) => {
      el.writeDt = cloneWriteDt
    })
  };



// -------------------------------------------------

  const segments = ref([]);
  const activeSegments = ref({});

  // 현재 년도 가져오기
  const currentYear = new Date().getFullYear();

  const filter = async() => {
    // 데이터 변환을 한번에 처리하여 성능 개선
    filteredData.value.forEach(el => {
      el.data = el.data.map(id => ({
        ...id,
        detail: {
          [t('doc_seq')]: id.docNo,
          [t('doc_writeDt')]: formatDate(id.writeDt),
          [t('doc_writer')]: id.writerNm,
          [t('doc_cnt')]: `${id.fileCount}건`
        }
      }));
    });
//  검색기능 추가
    let responses = await Promise.all([getSystemCode({ compId: getCompId(), majorCd: 'C0029' })]);
    let docCnt = responses[0].list;

    // inputForm.value를 docCnt 길이만큼 생성
    inputForm.value = docCnt.map(doc => ({
      cmd: 'I',
      writeYear: '',
      docType: 'DOC',
      docNo: '',
      docSeq: '',
      useYn: 'Y',
      writeDt: currentDate.value,
      writerId:'',
      createdAt: '',
      manageDocNm: '',
      files: [],
      manageDocType: doc.minorCd,
      manageDocTypeNm: doc.minorNm
    }));

    segments.value = filteredData.value;

    const currentYear = new Date().getFullYear(); // 현재 년도

    // 현재 년도에 해당하는 index를 찾기
    let index = filteredData.value.findIndex(item => item.year.slice(0, 4) == currentYear);

    // 만약 일치하는 항목이 없으면 index를 0으로 설정
    if (index === -1) {
        // 현재 연도를 구함
      const currentYear = String(new Date().getFullYear());
        // 조회 데이터가 없을 시 현재 연도 빈 아코디언 세팅
        filteredData.value.unshift({ year: currentYear });
     

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

  
  const dataFilter = () => {
    //검색기능
    if (searchText.value) {
        const temp = [];
        filteredData.value.forEach(row => {
          let isExist = false;
          row.data.forEach(col => {
            if (col.manageDocNm && col.manageDocNm.toLowerCase().includes(searchText.value.toLowerCase())) {
              isExist = true;
              return;
            }
          });

          if (isExist) {
            temp.push(row);
          }
        });
      segments.value = temp
    } else {
      segments.value = filteredData.value
    }
  };
  
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


  return {
    toggleAccordion,
    inputForm, initInputForm, checkedList,btnDelete,
    filter, searchText, signature, 
    dataFilter,
    searchParam,
    currentDate,
    file, fileInfo,
    segments, activeSegments,
    //버튼
    btnAdd, goBack, goDetail,
    btnSave,
    //API
    getMngDoc, getMngDocDetail,
    saveMngDoc, deleteMngDocs,
    deleteDetailDocs

  }
})
