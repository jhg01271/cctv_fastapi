import { defineStore } from 'pinia';

import BaseView from '@/components/base/BaseView';
const { computed, confirmMsg, alertMsg, toastPopup, getCompId, ref, t, getCurrentDate, formatDate, getPreMonth } = BaseView();
import router from '@/router';
import { usePlanningCtrlStore } from '@/stores/safewiz/impl/planningAndControl.js';
const planningCtrlStore = usePlanningCtrlStore();

import _ from 'lodash';
export const useContractorRegisterStore = defineStore('contractorRegister', () => {
    const compId = getCompId();
    const writeYear = ref(planningCtrlStore.searchParam.searchText);
    const dataList = ref([]);
    const inputForm = ref({
        cmd: 'I',
        writeYear: writeYear.value,
        docType: 'CTR',
        useYn: 'Y',
        writeDt: formatDate(getCurrentDate()),
        hrList: [],
        isCompDirectlyWrite: false,
        isHrDirectlyWrite: false,
        searchText: '',     //텍스트 조회조건
    }
    );

    return {
        compId,
        writeYear,
        dataList,
        inputForm,
        // function
    };
});
