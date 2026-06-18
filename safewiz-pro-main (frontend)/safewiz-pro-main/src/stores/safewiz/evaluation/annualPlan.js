import { defineStore } from 'pinia';

import BaseView from '@/components/base/BaseView';
const { computed, confirmMsg, alertMsg, formatDate, toastPopup, ref, t, getCompId, getCurrentDate, getPreMonth, downloadReport } = BaseView();
import router from '@/router';
import _ from 'lodash';

import { useAuditStore } from '@/stores/safewiz/evaluation/internalAudit.js';
const auditStore = useAuditStore();
export const useAnnualPlanStore = defineStore('annualPlan', () => {
    const compId = ref(getCompId());
    const writeYear = ref(auditStore.writeYear);
    const dataList = ref([]);
    return {
        compId,
        writeYear,
        dataList,
    };
});
