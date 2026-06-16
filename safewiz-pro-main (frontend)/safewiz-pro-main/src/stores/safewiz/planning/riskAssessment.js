import { defineStore } from "pinia"
import router from '@/router';
import BaseView from '@/components/base/BaseView';
import { getRiskAssessment } from '@/stores/safewiz/planning/api/riskAssessmentApi.js'
import _ from 'lodash';

const { ref, getCompId, getCurrentDate } = BaseView();


export const useRiskAStore = defineStore("riska", () => {
    const compId = getCompId();
    const searchParam = ref({
        compId: compId,
        searchText: getCurrentDate().substring(0, 4),
    })
    const menuList = ref([]);
    const btnSearch = async (notify) => {
        console.log('useRiskAStore btnSearch', searchParam.value)
        getRiskAssessment(searchParam.value, notify).then(res => {
            console.log('getRiskAssessment',res)
            menuList.value = res.list
        })
        // const data = ref([
        //     {
        //         title: 'preRiskAssessment',
        //         route: '/PreRiskAssessment',
        //         description: 'preRiskAssessmentDesc',
        //         activeYn: 'Y'
        //     },
        //     {
        //         title: 'classificationProcessEquipMsds',
        //         route: '/ClassificationProcessEquipMsds',
        //         description: 'classificationProcessEquipMsdsDesc',
        //         activeYn: 'Y'
        //     },
        //     {
        //         title: 'ResultOfRiskAssessmentTraining',
        //         route: '/ResultOfRiskAssessmentTraining',
        //         description: 'ResultOfRiskAssessmentTrainingDesc',
        //         activeYn: 'Y'
        //     },
        //     {
        //         title: 'riskAssessmentTable',
        //         route: '/RiskAssessmentTable',
        //         description: 'riskAssessmentTableDesc',
        //         activeYn: 'Y'
        //     },
        //     {
        //         title: 'improvementAndImplementation',
        //         route: '/ImprovementAndImplementation',
        //         description: 'improvementAndImplementationDesc',
        //         activeYn: 'Y'
        //     },
        // ])

        // menuList.value = data.value
    }
    return {
        searchParam, menuList,
        // function
        // api
        //버튼
        btnSearch
      }
});