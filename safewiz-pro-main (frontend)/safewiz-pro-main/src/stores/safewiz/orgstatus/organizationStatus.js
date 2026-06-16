import { defineStore } from "pinia"
import { ref } from "vue"
import router from '@/router';
import BaseView from '@/components/base/BaseView';
const {formatDate, getCurrentDate, baseDownload } = BaseView();
import { 
    getOrgnStatus, insertOrgnStatus, insertOrgnStatusDetail,
    updateOrgnStatus, deleteOrgnStatusDetail, deleteOrgnStatusAll,
    getDetailOrgnStatus, getReport, deleteOrgnStatus
 } from "@/stores/safewiz/orgstatus/api/organizationStatusApi"
import { useTaskStore } from '@/stores/safewiz/task/task.js';


export const useOrgnStatusStore = defineStore("organizationStatus", () => {

    // 조직 상황 리스트
    const orgnStatusLists = ref()

    const inputForm = ref({});
    const orgnPopup = ref();
    const orgnItem = ref([]);
    const head1 = ref([]); // 작성
    const head2 = ref([]); // 검토
    const head3 = ref([]); // 승인
    // 서명 컴포넌트
    const signature = ref(null);

    const initInputForm = () => {
      inputForm.value = {
        writeYear: getCurrentDate().substring(0,4), // 키(년도)
        docType : '', // 키(문서타입:orgSt)
        docNo: '', // 키(문서번호)
        wirteDt: '',
        cmd: '', // 신규 추가 : 'I', 기존 데이터 조회 : 'U'
        searchFrom: '', // 년도
        orgnId: '', // 조직코드
        gubun: '', // 구분
        workDesc: '', // 업무
        issue: '', // 이슈
        risk: '', // 리스크
        chance: '', // 기회
        approvalKey: '', // 결재 키
        approvalStatus: '', // 결재 상태
        createdAt: '', // 등록일시
        createdBy: '', // 작성자
        updatedAt: '',
        updatedBy: '',
        useYn: ''
      }
    };

    const setRefs = (orgn) => {
        orgnPopup.value = orgn.value;
    };

    // 조직 상태(집계) 조회
    const getOrgnStatusLists = (searchParam) => {
        return new Promise((resolve) => {
            getOrgnStatus(searchParam, true).then(res => {
                resolve({ list: res.list, totalCount: res.totalCount })
            })
        })
    }

    // 조직 상태(집계) 상세조회
    const getDetailOrgnStatusLists = (param, notify) => {
        return new Promise((resolve) => {
            getDetailOrgnStatus(param, notify).then(res => {
                resolve({ list: res.list, signList: res.signList })
            })
        })
    }

    // 추가
    const setOrgnStatus = () => {
        return new Promise((resolve) => {
            insertOrgnStatus(inputForm.value).then(res => {
                resolve({ result: res.result, success: res.success })
            })
        })
    }
    
    // 추가 Detail
    const setOrgnStatusDetail = (data, notify) => {
        return new Promise((resolve) => {
            insertOrgnStatusDetail(data, notify).then(res => {
                resolve({ result: res.result, success: res.success })
            })
        })
    }

    // 상세
    const changeOrgnStatus = () => {
        return new Promise((resolve) => {
            updateOrgnStatus(inputForm.value, true).then(res => {
                resolve({ result: res.result, success: res.success })
            })
        })
    }

    // 상세 삭제
    const delOrgnStatusDetail = (data) => {
        return new Promise((resolve) => {
            deleteOrgnStatusDetail(data, true).then(res => {
                resolve({ result: res.result, success: res.success })
            })
        })
    }
    
    // 삭제
    const delOrgnStatus = (data) => {
        return new Promise((resolve) => {
            deleteOrgnStatus(data, true).then(res => {
                resolve({ result: res.result, success: res.success })
            })
        })
    }
    
    // 삭제
    const delOrgnStatusAll = (data) => {
        return new Promise((resolve) => {
            deleteOrgnStatusAll(data, true).then(res => {
                resolve({ result: res.result, success: res.success })
            })
        })
    }
    // 출력물 다운로드
    const downloadReport = (param) => {
        baseDownload(getReport, param)
    }

    // 신규 추가 이동
    const goInsert = (year) => {
        initInputForm();
        inputForm.value.cmd = 'I';
        if(year) inputForm.value.writeYear = year.substring(0,4)

        orgnItem.value = [];
        router.push({ 
            name: 'OrganizationStatusDetail'
        });
    };

    //목록으로 이동
    const goBack = () => {
        //검색어 초기화
        initInputForm();
        router.push({ name: 'OrganizationStatus' });
    };

    const closeOrgn = e => {
        //chips에 넣기위해 id:'', name:'' 으로 세팅    
        if (e.length !== 0) {
            orgnItem.value = [];
            inputForm.value.orgnId = e[0].orgnId;
            // 어차피 single이라서 하나만 들어온다.
            for(var dt of e) {
                orgnItem.value.push({
                    id: dt.orgnId,
                    name: dt.orgnNm
                });
            }
        }
        orgnPopup.value.onClose();
    };

    return {
        orgnStatusLists, inputForm, orgnPopup, orgnItem, head1, head2, head3, signature,
        initInputForm, setRefs,
        getOrgnStatusLists, getDetailOrgnStatusLists, 
        setOrgnStatus, setOrgnStatusDetail,
        changeOrgnStatus,
        delOrgnStatusDetail, delOrgnStatusAll,
        downloadReport,
        goInsert, goBack, closeOrgn, delOrgnStatus
        
  }
})
