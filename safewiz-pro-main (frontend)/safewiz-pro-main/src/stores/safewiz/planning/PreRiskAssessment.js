import { defineStore } from 'pinia';

import BaseView from '@/components/base/BaseView';
const { ref } = BaseView();
export const usePreRiskAssessmentStore = defineStore('preRiskAssessment', () => {
    const activeTab = ref('');

    return {
        activeTab
    };
});
