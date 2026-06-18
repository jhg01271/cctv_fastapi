import { defineStore } from "pinia"

import router from '@/router';
import BaseView from '@/components/base/BaseView';
const { openLoading, endLoading, ref, reactive, toastPopup, confirmMsg, t, getCompId, getCurrentDate, alertMsg, formatDate, formatDateForDB } = BaseView();
import { getPartner, getPartnerDetail, addPartner, updatePartner, deletePartner, deletePartners, downloadPartnerExcelTemplate, insertPartnerExcel} from "@/stores/system/base/api/partnerApi"
import { useButtonListStore } from '@/stores/buttonList';

import { getTypeofbusinessList } from '@/stores/safewiz/dataset/api/datasetApi';
const layoutStore = useButtonListStore();

layoutStore.useBtnList = ['btnSearch', 'btnBack', 'btnAdd', 'btnSave', 'btnDelete'];

import _ from 'lodash'
export const usePartnerStore = defineStore("partner", () => {

  //협력사 리스트
  const compId = getCompId();
  
  const partnerList = ref([]);
  
  const partCompId = ref('');
  
  //현재날짜
  const currentDate = ref(getCurrentDate());

  // 파일 컴포넌트
  const fileInfo = ref(null)
  const fileInfo2 = ref(null)

  const setRefs = (file, file2) => {
    fileInfo.value = file.value;
    fileInfo2.value = file2.value;
  };

  const selectedPartners = reactive([]);

  const inputForm = ref({});
  const initInputForm = () => {
    inputForm.value = {
      cmd: '',
      compId: compId, // 사업장 ID
      partCompId:'', //협력사 ID
      partCompItem: '', //공사와의 거래품목
      partCompItemNm: '', //공사와의 거래품목명
      partCompNm: '', //업체명
      ordSeq: '',//정렬순서
      rpstNm: '',//사업장등록번호
      corpNo: '',//법인등록번호
      bizCd:'', // 업종코드
      bizNm: '',// 업종명
      email: '', //이메일
      telNo: '', //전화번호
      faxno: '', // FAX번호
      zipCd: '', //우편번호
      addrs1: '', //주소1
      addrs2: '' , //주소2
      addrsEn1: '', //영문주소1
      addrsEn2: '' , //영문주소2
      natnCd :'' , //국가코드
      desc: '' , // 주요사업
      useYn: '', //사용여부
      checked: '',
      hrListH: [], //  본사 정
      hrListS: [],  // 본사 부
      partnerHrListH:[], // 협력사 정
      partnerHrListS: [], // 협력사 부
      partShHrId: '', //안전보건 관리 책임자
      partShHrImgId: '', //안전보건 관리 책임자
      partShHrNm: '', //안전보건 관리 책임자
      updatedAt: '',
      updatedBy: '',
      regDt: '',     //등록일자
      createdAt: '',
      createdBy: '', // 수정일시
      attachId: '', // 첨부 아이디
      deleteFiles:[]  //삭제할 파일 id
    }
  };

  //-----------------------------------------------
  //협력사 조회
  const getPartnerList = () => {
    return getPartner(searchParam.value, true)
    .then(res => {
      if (res && res.success) {
        partnerList.value = res.list.map(item => ({
          ...item,
          detail: {
            [t('exponent')]: item.rpstNm,
            [t('telNo')]: item.telNo,
            [t('fax')]: item.faxNo,
            [t('partnerTransactionItem')]: item.partCompItemNm,
          }
        }))
        applyFilter();
      }
      return res; // res 반환
    });
  };
  const setItem = async item => {
    const resultData = await Promise.all(
        item.map(async el => {
            return {
                id: el?.hrId,
                name: el?.hrNm,
                fileId: el.logoId
            };
        })
    );
    return resultData;
};
  //협력사 상세조회
  const getPartDetail = (data, notify) => {
    return getPartnerDetail(data, notify)
    .then(async res => {
      if (res && res.success) {
        inputForm.value = _.cloneDeep(res.list);
        inputForm.value.hrListH = await setItem(res.list.hrListH);
        
        inputForm.value.hrListS = await setItem(res.list.hrListS);
        inputForm.value.partnerHrListH = await setItem(res.list.partnerHrListH);
        inputForm.value.partnerHrListS = await setItem(res.list.partnerHrListS);
        inputForm.value.partShHr = [{ id: res.list.partShHrId, name: res.list.partShHrNm, fileId: res.list.partShHrImgId }];
        inputForm.value.regDt = formatDate(res.list.regDt);
      }
        return res; // res 반환
      });
  };
  //협력사 개별삭제
  const deletePart = (data) => {
    return new Promise((resolve) => {
      deletePartner(data, true)
      .then(res => {
        if (res && res.success) {
          resolve({ result: res.result })
          router.push({ name: 'partnerManage' });
        }
      })
    });
  };

  //협력사 일괄 삭제
 const deleteParts = (data) => {
    return new Promise((resolve) => {
      deletePartners(data, true)
        .then(res => {
          if (res && res.success) {
          resolve({ result: res.result })
            // 검색어 초기화
            searchTerm.value = '';
            //리스트 초기화
            dataFilterList.value = [];
            //체크리스트 초기화
            checkedList.value = [];
            //조회
            getPartnerList(true);
        }
    })
  });
};

  //협력사 추가
  const insertPartner = (data) => {
    
    if(!inputForm.value.ordSeq) {
      inputForm.value.ordSeq = 99;
    }
    let editFiles = fileInfo.value.editFiles
    let editFilesLoc = fileInfo2.value.editFiles
    
    let formData = new FormData();
    data.regDt = formatDateForDB(data.regDt);
    formData.append('info', new Blob([JSON.stringify(data)], { type: 'application/json' }));

    editFiles.insert.forEach(element => {
        formData.append('files', element ? element : new Blob([], { type: 'application/octet-stream' }));
    });
    editFilesLoc.insert.forEach(element => {
      formData.append('files2', element ? element : new Blob([], { type: 'application/octet-stream' }));
    });
    return new Promise((resolve) => {
      addPartner(formData, true)
      .then(res => {
        if (res && res.success) {
          resolve({ result: res.result });
          inputForm.value.cmd = 'U';
          layoutStore.useBtnList = ['btnBack', 'btnAdd', 'btnSave', 'btnDelete'];
          if (res.result.fileId != null)
            fileInfo.value.resetEditFiles(res.result.fileId)
          if (res.result.fileId2 != null)
            fileInfo2.value.resetEditFiles(res.result.fileId2)
          getPartDetail(res.result.partCompId, true)
        }
      })
    });
  };

  // 협력사 수정
  const updatePart = (data) => {
    if(!inputForm.value.ordSeq) {
      inputForm.value.ordSeq = 99;
    }
    let editFiles = fileInfo.value.editFiles
    let editFilesLoc = fileInfo2.value.editFiles
    let formData = new FormData();
    //파일이 수정될 때, 기존 파일을 삭제함(logo,location)
    data.deleteFiles = editFiles.delete
    data.deleteFiles2 = editFilesLoc.delete
    data.regDt = formatDateForDB(data.regDt);
    formData.append('info', new Blob([JSON.stringify(data)], { type: 'application/json' }));

    editFiles.insert.forEach(element => {
        formData.append('files', element ? element : new Blob([], { type: 'application/octet-stream' }));
    });

    editFilesLoc.insert.forEach(element => {
        formData.append('files2', element ? element : new Blob([], { type: 'application/octet-stream' }));
    });
    return new Promise((resolve) => {
      updatePartner(data.partCompId, formData, true)
       .then(res => {
         if (res && res.success) {
              //파일 파라미터 초기화
              inputForm.value.regDt = formatDate(res.result.regDt);
              if(res.result.fileId != null)
                fileInfo.value.resetEditFiles(res.result.fileId)
              if(res.result.fileId2 != null)
                fileInfo2.value.resetEditFiles(res.result.fileId2)
              resolve({ result: res.result })
            }
         })
    });
  };

  
  //-----------------------------------------------

  // 검색기능

  const searchTerm = ref('');
  const dataFilterList = ref(null);


  const initData = () => {
    // 여기서 API 호출을 통해 데이터를 가져올 수 있습니다.
    // 예제에서는 초기 데이터로 설정합니다.
    applyFilter(); // 필터를 처음에 적용하여 초기 데이터로 그리드를 채움
  };

  // 필터 적용 함수
  const applyFilter = () => {
    const filteredData = partnerList.value.filter(item => 
      item.partCompNm.toLowerCase().includes(searchTerm.value.toLowerCase()) ||
      (item.rpstNm && item.rpstNm.toLowerCase().includes(searchTerm.value.toLowerCase())) ||
      (item.telNo && item.telNo.toLowerCase().includes(searchTerm.value.toLowerCase())) ||
      (item.faxNo && item.faxNo.toLowerCase().includes(searchTerm.value.toLowerCase())) ||
      (item.partCompItemNm && item.partCompItemNm.toLowerCase().includes(searchTerm.value.toLowerCase()))
      );
    // 필터링된 데이터로 그리드를 업데이트
    if (dataFilterList.value) {
      dataFilterList.value = filteredData;
    }
  };
  //-----------------------------------------------
  //목록으로 이동
  const goBack = () => {
    //검색어 초기화
    searchTerm.value = '';
    router.push({ name: 'partnerManage' });
  };

  //-----------------------------------------------

  //추가버튼
  const btnAdd = () => {
    layoutStore.useBtnList = ['btnBack','btnSave'];
    inputForm.value = ref();
    // 코드 초기화
    partCompId.value = ''
    chipsItem.value = [];

    // 추가(I) 플래그 cmd
    inputForm.value.cmd = 'I';
    inputForm.value.useYn = 'Y';
    inputForm.value.createdAt = currentDate;
    inputForm.value.regDt = currentDate
    // 담당자 초기화
    inputForm.value.hrListH = []
    inputForm.value.hrListS = []
    inputForm.value.partnerHrListH = []
    inputForm.value.partnerHrListS = []
    inputForm.value.partShHr = []
    inputForm.value.partShHrId = ''
    inputForm.value.partShHrNm = ''
    inputForm.value.partShHrImgId = ''
    //파일초기화
    if (fileInfo.value) {
      fileInfo.value.fnRemove();
    }
    if (fileInfo2.value) {
      fileInfo2.value.fnRemove();
    }
    //페이지 이동
    router.push({
      path: 'partnerManageDetail'
    });
  };

  //-----------------------------------------------

  //저장
  const btnSave = async () => {
    

    // 정, 부 담당자 저장로직
    console.log('@@@ 저장데이터 ', inputForm.value)
    if (inputForm.value.cmd === 'I') {
      //협력사 추가
      confirmMsg('info', '저장 하시겠습니까?', '', { fun: insertPartner, param: inputForm.value });
    } else {
      //협력사 수정
      confirmMsg('info', '저장 하시겠습니까?', '', { fun: updatePart, param: inputForm.value });
    }
  }

  //-----------------------------------------------

  const checkedList = ref([]);

  //일괄 삭제
  const btnDelete = async item => {
    if (item == 'D') {
      // detail 에서 삭제 
      confirmMsg('warning', '삭제 하시겠습니까?', '', { fun: deletePart, param: inputForm.value.partCompId });

    } else {
      let param = checkedList.value; // rowKey로 행 데이터를 가져옴
      if (!param.length) {
        alertMsg('warning','선택된 항목이 없습니다.', '');
        return;
      }
      if (param.some(el => el.useYn === 'N')) {
        alertMsg('warning', '이미 삭제 처리된 항목입니다.', '');
        return;
      }
      confirmMsg('warning', t('msgDelete'), ``, { fun: deleteParts, param: param });

    }
  }

  //-----------------------------------------------

  const searchParam = ref({
    compId: compId
  });

  //-----------------------------------------------

  // 주소 팝업
  const dialogPostNo = ref(null);

  const setRef = (post) => {
    dialogPostNo.value = post.value;
  }
  // 주소 검색 팝업 열기
  const emitOpenPostcodeDialog = () => {
    dialogPostNo.value.onOpen();
  };

  // 주소 검색 팝업 닫기
  const closePostcodeDialog = () => {
    dialogPostNo.value.onClose();
  };
    
  // 주소 선택 및 업데이트 후 팝업 닫기
  const onPostcodeComplete = data => {
    inputForm.value.zipCd = data.zonecode;
    inputForm.value.addrs1 = data.address;

    dialogPostNo.value.onClose();
  };


  //-----------------------------------------------

  //useYn 체크

  const toggleUseYn = event => {
    // 체크박스의 체크 상태에 따라 'Y' 또는 'N'을 설정합니다.
    inputForm.value.useYn = event.target.checked ? 'Y' : 'N';
  };

  //-----------------------------------------------

  const chipsItem = ref([]); // Chip 리스트


  //엑셀 관련 기능-------------------------------------------------------------

  const excelData = ref()

  const getPartnerTemplate = id => {
      openLoading()
      downloadPartnerExcelTemplate(id).then(res => {
        console.log('## 템플릿 다운로드 ', res);
        endLoading()
      });
  };

  const businessList = ref([]);
  //엑셀 인원 추가
  const createPartnerExcel = async () => {
    openLoading()
    let data;
    let pass = true

    excelData.value = await excelData.value.slice(2) //excelData.value[2] 부터 입력된 데이터 시작

    //엑셀 데이터 없는 경우
    if (excelData.value == null || excelData.value.length === 0) {
      alertMsg('warning', t('저장할 데이터가 없습니다.'));
      endLoading()
      return;
    }

    let responses = await Promise.all([getTypeofbusinessList({},false)]);
    if (responses) {
        businessList.value = responses[0].list;
    }

    const invalidItems = excelData.value.filter(item => {
      // 필수값 체크
      if (item[1] === null || item[1] === '') return item;   // 협력사명
      if (item[2] === null || item[2] === '') return item;   // 대표자명
      if (item[3] === null || item[3] === '') return item;   // 등록일자
      if (item[5] === null || item[5] === '') return item;   // 사업자 등록번호
    });

    if (invalidItems.length > 0) {
      alertMsg('error', '엑셀의 필수값을 입력해주세요.');
      endLoading();
      return;
    }

    data = await excelData.value.map(item => {
      try{
        if(item[10] !== null && item[10] !== ''){
          const orgnMatch = businessList.value.find(el => el.detailCd === item[10].toString().trim()); //업종코드가 없거나 잘못된 코드일 경우 오류 발생
          if(orgnMatch == null){
            alertMsg('error', t('협력사명 : ') + item[10].trim() + '\n' + t('업종을 찾을 수 없습니다.') + '\n' + t('데이터셋 시트에 맞춰 작성하세요.'));
            pass = false
            endLoading()
            return
          }
        }

        const bizRegNumPattern = /^\d{3}-\d{2}-\d{5}$/;
        if (item[5].trim() && !bizRegNumPattern.test(item[5].trim())) {
          alertMsg('error', t('협력사명 : ') + item[1].trim() + '\n' + t('유효하지 않은 형식의 사업자등록번호입니다.'))
          pass = false
          endLoading()
          return;
        }

        const phonePattern = /^0\d{1,2}-\d{3,4}-\d{4}$/;
        if (item[6].trim() && !phonePattern.test(item[6].trim())) {
          alertMsg('error', t('협력사명 : ') + item[1].trim() + '\n' + t('유효하지 않은 형식의 전화번호입니다.'))
          pass = false
          endLoading()
          return;
        }

        if (item[7].trim() && !phonePattern.test(item[7].trim())) {
          alertMsg('error', t('협력사명 : ') + item[1].trim() + '\n' + t('유효하지 않은 형식의 FAX번호입니다.'))
          pass = false
          endLoading()
          return;
        }

        const emailPattern = /^[A-Za-z0-9_\\.\\-]+@[A-Za-z0-9\\-]+\.[A-za-z0-9\\-]+/;
        if (item[8].trim() && !emailPattern.test(item[8].trim())) {
          alertMsg('error', t('협력사명 : ') + item[1].trim() + '\n' + t('유효하지 않은 형식의 이메일입니다.'));
          pass = false
          endLoading()
          return;
        }
        
        let regDt = item[3].toString() === '' ? null : item[3].toString();

        //날짜 유효성 검사
        if (!isValidDate(regDt) ) {
            alertMsg('error', t('협력사명 : ') + item[1].trim() + '\n' + t('날짜 입력 형식(yyyyMMdd)에 맞게 입력해 주세요.'));
            pass = false;
            endLoading();
            return;
        }

        item[4] = item[4] !== "" ? item[4] : 99 //정렬값 없을 경우 99로 삽입
        return{
          partCompNm : item[1], //협력사명
          rpstNm : item[2],     //대표자명
          regDt : item[3],      //등록일자
          ordSeq : item[4],     //정렬
          rgstNo : item[5],     //사업자 등록번호
          telNo : item[6],      //전화번호
          faxNo : item[7],      //팩스번호
          email : item[8],      //이메일
          desc : item[9],       //주요사업
          bizCd : item[10],     //업종
          zipCd : item[11],     //우편번호
          addrs1 : item[12],    //주소
          addrs2 : item[13],    //상세주소
        }
      } catch (err) {
        alertMsg('error', t('협력사명 : ') + String(item[1]).trim() + '\n' + t('데이터를 확인하세요.'));
        pass = false;
        endLoading();
        return null;
      }

        
    })
    if(pass){
      insertPartnerExcel(data, true).then(res => {
        if(res.success){
          getPartnerList()
        }
      })
    }
    endLoading()
  }

    //날짜 형식 유효성 검사
  const isValidDate = dateStr => {
      if (dateStr === null) return true;

      // 정규 표현식을 이용해 yyyyMMdd 형식 확인
      const datePattern = /^\d{8}$/; // 정확히 8자리 숫자인지 확인
      if (!datePattern.test(dateStr)) return false;

      const year = parseInt(dateStr.substring(0, 4), 10);
      const month = parseInt(dateStr.substring(4, 6), 10) - 1; // 월은 0부터 시작
      const day = parseInt(dateStr.substring(6, 8), 10);

      const date = new Date(year, month, day);

      return date.getFullYear() === year && date.getMonth() === month && date.getDate() === day;
  };
 

  return {
    initInputForm, inputForm, setRef, partCompId, setRefs,
    onPostcodeComplete, dialogPostNo, emitOpenPostcodeDialog, closePostcodeDialog,
    searchParam, toggleUseYn,  checkedList,
    selectedPartners, initData, searchTerm, dataFilterList, applyFilter,
    currentDate, 
    //라우터
    btnAdd, goBack, compId,
    //버튼
    btnSave, btnDelete,
    //api
    partnerList, insertPartner, deletePart, updatePart, deleteParts,
    getPartnerList, getPartDetail,
    chipsItem, 
    //엑셀
    excelData, getPartnerTemplate, createPartnerExcel
  }
})
