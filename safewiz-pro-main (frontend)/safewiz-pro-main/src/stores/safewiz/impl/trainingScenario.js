import { defineStore } from 'pinia';

import BaseView from '@/components/base/BaseView';
const { openLoading, endLoading, computed, confirmMsg, alertMsg, toastPopup, ref, t, getCompId, getCurrentDate, getPreMonth } = BaseView();
import router from '@/router';
import _ from 'lodash';
import { deleteTraining } from '@/stores/safewiz/impl/api/trainingScenarioApi.js';

import { useEmergencyResponseStore } from '@/stores/safewiz/impl/emergencyResponse.js';
const emergencyResponseStore = useEmergencyResponseStore();

import { getTrainingDetail } from '@/stores/safewiz/impl/api/trainingScenarioApi.js';
import { endOfDay } from 'date-fns';

export const useTrainingScenarioStore = defineStore('trainingScenario', () => {
    const compId = getCompId();
    const searchParam = ref({
        compId: compId,
        searchText: getCurrentDate().substring(0, 4)
    });
    const inputForm = ref({});
    const initInputForm = () => {
        inputForm.value = {
            cmd: 'I',
            docType: 'ERT',
            writeYear: emergencyResponseStore.searchParam.searchText,
            scenarioList: [
                {
                    cmd: 'I',
                    situation: '',
                    targetTime: 0,
                    performer: '',
                    methodAction: '',
                    remark: ''
                }
            ],
            useYn: 'Y'
        };
    };
    const originTrainingList = ref([]);
    const btnDetail = async (data, notify) => {
        let param = {
            writeYear: data.writeYear,
            docType: data.docType,
            docNo: data.docNo
        };
        openLoading();
        return new Promise(resolve => {
            getTrainingDetail(param, notify)
                .then(res => {
                    Object.assign(inputForm.value, res.list); // 반응형 유지
                    Object.assign(originTrainingList.value, _.cloneDeep(res.list.scenarioList)); // 반응형 유지
                    // originTrainingList.value = _.cloneDeep(res.list.scenarioList)
                    resolve(true);
                })
                .catch(() => {
                    endLoading();
                })
                .finally(() => {
                    endLoading();
                });
        });
    };
    const btnDelete = (list, notify) => {
        openLoading();
        deleteTraining(list, notify)
            .then(res => {
                //
            })
            .catch(() => {
                endLoading();
            })
            .finally(() => {
                endLoading();
            });
    };
    const btnDetailDelete = (list, notify) => {
        openLoading();
        deleteTraining(list, notify)
            .then(res => {
                btnDetail(res.result);
            })
            .catch(() => {
                endLoading();
            })
            .finally(() => {
                endLoading();
            });
    };
    return {
        searchParam,
        inputForm,
        originTrainingList,
        // function
        initInputForm,
        // api
        btnDetail,
        btnDelete,
        btnDetailDelete
    };
});
