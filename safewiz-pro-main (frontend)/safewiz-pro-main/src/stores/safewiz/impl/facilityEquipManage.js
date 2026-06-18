import { defineStore } from 'pinia'

import BaseView from '@/components/base/BaseView';
const { computed, formatDate, confirmMsg, alertMsg, toastPopup, ref, t, getCompId, getCurrentDate, getPreMonth } = BaseView()
import router from '@/router';
import { getFacilityEquipManage, getFacilityEquipManageDetail,
    setFacilityEquipManage,
    deleteFacilityEquipManage
  } from '@/stores/safewiz/impl/api/facilityEquipManageApi.js'
import _ from 'lodash';
export const useFacilityEquipManage = defineStore('facilityEquipManage', () => {
  const compId = getCompId();
  const inputForm = ref({});

  const searchParam = ref({
    compId: compId,
    searchText: getCurrentDate().substring(0, 4),
    facilitisId: ''
  })

  const initInputForm = () => {
    inputForm.value = {
      cmd: '', // 신규 추가 : 'I', 기존 데이터 조회 : 'U'
      facilitisId: '',
      orderNum: '99',
      facilitisDesc: '',
      createdAt: '', // 등록일시
      createdBy: '', // 작성자
      updatedAt: '',
      updatedBy: '',
      useYn: 'Y',
      files: []
    }
  };
  const menuList = ref([]);

  // 조회
  const btnSearch = async (notify) => {
      return new Promise((resolve) => {
          getFacilityEquipManage(searchParam.value, notify).then(res => {
              resolve({ result: res.list, success: res.success });
          })
      });
  };

  // 상세 조회
  const searchDetail = async (notify) => {
    return new Promise((resolve) => {
        getFacilityEquipManageDetail(searchParam.value, notify).then(res => {
            resolve({ result: res.list, success: res.success });
        })
    });
  };

  // 저장
  const saveFacilityEquip = (data, notify) => {
    return new Promise((resolve) => {
        setFacilityEquipManage(data, notify).then(res => {
            searchParam.value.facilitisId = res.facilitisId;
            resolve({ facilitisId: res.facilitisId, success: res.success });
        })
    });
  };

  // 삭제
  const deleteFacilityEquip = (data, notify) => {
    return new Promise((resolve) => {
        deleteFacilityEquipManage(data, notify).then(res => {
            resolve({ result: res.list, success: res.success });
        })
    });
  };

  // 상세보기 이동
  const goDetail = (e) => {
      initInputForm();
      searchParam.value.facilitisId = e.facilitisId;
      inputForm.value.cmd = 'U';      

      router.push({ 
          name: 'FacilityEquipManageDetail'
      });
  };

  // 신규 추가 이동
  const goInsert = () => {
    initInputForm();
    inputForm.value.cmd = 'I';
    inputForm.value.createdAt = getCurrentDate();
    router.push({ 
        name: 'FacilityEquipManageDetail'
    });
  };


  return {
    searchParam, menuList, inputForm,
    // function
    // api
    initInputForm,
    btnSearch, searchDetail, saveFacilityEquip, deleteFacilityEquip,
    goDetail, goInsert
  }
})