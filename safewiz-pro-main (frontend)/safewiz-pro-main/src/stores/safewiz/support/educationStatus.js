import { defineStore } from 'pinia';

import BaseView from '@/components/base/BaseView';
const { openLoading, endLoading, confirmMsg, alertMsg, ref, t, getCurrentDate } = BaseView();
import router from '@/router';
import _ from 'lodash';

import { useEducationStore } from '@/stores/safewiz/support/education.js';
const educationStore = useEducationStore();

import { getEducationStatus } from '@/stores/safewiz/support/api/educationStatusApi';
export const useEducationStatusStore = defineStore('educationStatus', () => {
    const originData = ref([]);
    const groupedDataList = ref([]);

    const activeSegments = ref([]);

    const filterDivList = ref([
        { id: 'orgnRoleId', name: 'orgnRoleNm', title: '안전보건 조직 역할' },
        { id: 'orgnId', name: 'orgnNm', title: '조직' }
    ]);
    const currentFilter = ref('orgnRoleId');

    const btnSearch = notify => {
        return new Promise(resolve => {
            originData.value = [];
            groupedDataList.value = [];
            openLoading();
            getEducationStatus({ writeYear: educationStore.searchParam.searchText }, notify)
                .then(async res => {
                    if (res.list.length > 0) {
                        originData.value = _.cloneDeep(res.list);
                        groupedDataList.value = await getGroupedData(res.list);
                    }
                })
                .catch(() => {
                    endLoading();
                })
                .finally(() => {
                    resolve(true);
                    endLoading();
                });
        });
    };

    // 필터에 맞게 그루핑
    const getGroupedData = originData => {
        openLoading();
        const defaultGrp = currentFilter.value === 'orgnRoleId' ? '근로자' : '미설정';
        const filterKey = filterDivList.value.find(el => el.id === currentFilter.value);
        if (!filterKey) return;
        const groupedData = Object.values(
            originData.reduce((acc, curr) => {
                curr.visible = true;
                const key = curr[filterKey.id];
                if (!acc[key]) {
                    acc[key] = {
                        title: curr[filterKey.name] || defaultGrp, // orgn_role_nm이 없을 때
                        ordSeq: filterKey.id === 'orgnRoleId' ? curr.orgnRoleOrdSeq : curr.orgnOrdSeq,
                        visible: true,
                        dataList: []
                    };
                }

                const duplicatedIndex = acc[key].dataList.findIndex(el => el.hrId === curr.hrId);
                if (duplicatedIndex < 0) {
                    curr.totalTrainingTime = curr.trainingMinutes;
                    curr.totalTrainingTimeY = curr.educationStatus === 'Y' ? curr.trainingMinutes : 0;
                    if (curr.educationStatus === 'Y') {
                        // 교육 이수 시
                        curr.trainingListY = [formattedTrainingData(curr)];
                        curr.trainingListN = [];
                    } else {
                        // 교육 미이수 시
                        curr.trainingListY = [];
                        curr.trainingListN = [formattedTrainingData(curr)];
                    }
                    curr.orgnRoleNm = curr.orgnRoleNm ? curr.orgnRoleNm : '근로자';
                    acc[key].dataList.push(curr);
                } else {
                    // 중복되었을 때
                    acc[key].dataList[duplicatedIndex].totalTrainingTime += curr.trainingMinutes; // 총 이수 시간
                    if (curr.educationStatus === 'Y') {
                        acc[key].dataList[duplicatedIndex].trainingListY.push(formattedTrainingData(curr)); // 교육 이수
                        acc[key].dataList[duplicatedIndex].totalTrainingTimeY += curr.trainingMinutes; // 교육 이수 건수
                    } else {
                        acc[key].dataList[duplicatedIndex].trainingListN.push(formattedTrainingData(curr)); // 교육 이수
                    }
                }
                return acc;
            }, {})
        );
        groupedData.sort((a, b) => a.ordSeq - b.ordSeq);
        endLoading();
        return groupedData;
    };
    const formattedTrainingData = data => {
        const { writeYear, docNo, hrId, trainingNm, trainingMinutes, educationStatus } = data;
        return { writeYear: writeYear, docNo: docNo, hrId: hrId, trainingNm: trainingNm, trainingMinutes: trainingMinutes, educationStatus: educationStatus };
    };
    const toggleDiv = async id => {
        currentFilter.value = id;
        groupedDataList.value = await getGroupedData(originData.value);
    };

    //######################################################################################################################
    //----------------------------------------------------------------------------------------------------------------------
    //######################################################################################################################
    return {
        filterDivList,
        currentFilter,
        groupedDataList,
        activeSegments,
        // function
        toggleDiv,
        // api
        btnSearch
    };
});
