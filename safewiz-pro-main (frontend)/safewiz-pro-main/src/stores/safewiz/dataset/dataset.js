import { defineStore } from "pinia";
import { ref } from 'vue';
import { getTypeofbusinessList, getTypeofEquipmentList } from "@/stores/safewiz/dataset/api/datasetApi";

export const useDatasetStore = defineStore("dataset", () => {
  // 상태 정의
  const businessList = ref([]);  // 업종 리스트
  const equipmentList = ref([]); // 설비 리스트
  const businessPage = ref(1);   // 업종 리스트 페이지
  const equipmentPage = ref(1);  // 설비 리스트 페이지
  const hasMoreBusiness = ref(true); // 추가 업종 데이터 여부
  const hasMoreEquipment = ref(true); // 추가 설비 데이터 여부
  const loading = ref(false);  // 로딩 상태

  // 업종코드 조회 (검색어 포함)
  const getTypeofbusinessLists = async (searchTerm = '') => {
    if (!hasMoreBusiness.value || loading.value) return;

    loading.value = true; // 로딩 시작
    try {
      // 검색어가 있을 경우 페이지 초기화
      if (searchTerm) {
        resetBusinessList(); // 초기화 함수 호출
      }
      const params = {
        page: businessPage.value,
        pageSize: 50,
        searchTerm:searchTerm
      };
      const res = await getTypeofbusinessList(params, true); // 페이지와 검색어를 기반으로 데이터 요청
      if (res && res.success) {
        businessList.value = [...businessList.value, ...res.list]; // 기존 데이터에 추가
        if (res.list.length === 0) {
          hasMoreBusiness.value = false; // 추가 데이터가 없으면 더 이상 요청 안 함
        } else {
          businessPage.value += 50; // 다음 페이지로 이동
        }
      } else {
        console.error('API 호출 결과:', res); // API 호출 결과 출력
      }
    } catch (error) {
      console.error("Failed to fetch business list:", error); // 오류 출력
    } finally {
      loading.value = false; // 로딩 종료
    }
  };



  // 데이터 초기화 함수
  const resetBusinessList = () => {
    businessList.value = [];
    businessPage.value = 1;
    hasMoreBusiness.value = true;
  };

  const resetEquipmentList = () => {
    equipmentList.value = [];
    equipmentPage.value = 1;
    hasMoreEquipment.value = true;
  };

  return {
    businessList,
    equipmentList,
    getTypeofbusinessLists,
    resetBusinessList,
    resetEquipmentList,
    hasMoreBusiness,
    hasMoreEquipment,
    loading,
  };
});
