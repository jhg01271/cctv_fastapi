import { defineStore } from 'pinia';

import BaseView from '@/components/base/BaseView';
const { openLoading, endLoading, formatDate, onMounted, router, ref, btnAdd, getCompId, btnDelete, btnSearch, confirmMsg, alertMsg, getCurrentDate } = BaseView();
import _ from 'lodash';

import { getEmergencyOrgChartList, confirmEmergencyOrgChart, saveEmergencyOrgChart, getEmergencyRoleList, deleteEmergencyOrgChart } from '@/stores/safewiz/impl/api/emerCtrlOrgChartApi.js';
export const useEmerCtrlOrgChartStore = defineStore('emerCtrlOrgChart', () => {
    const compId = getCompId();
    const searchParam = ref({
        compId: compId,
        writeYear: getCurrentDate().substring(0, 4)
    });
    const inputForm = ref({
        cmd: '',
        chartId: '',
        useYn: 'Y',
        confirmAt: '',
        createdAt: getCurrentDate()
    });
    const initInputForm = () => {
        inputForm.value = {
            cmd: 'I',
            compId: compId,
            chartId: '',
            confirmYn: 'N',
            useYn: 'Y',
            confirmAt: null,
            createdAt: getCurrentDate()
        };
    };

    // 버튼 이벤트

    const confirmTypeList = ref([]);
    const degreeList = ref([]);
    const filteredDegreeList = ref([]);
    const btnSearch = notify => {
        confirmTypeList.value = [];
        openLoading();
        getEmergencyOrgChartList(searchParam.value, notify)
            .then(res => {
                res.list.forEach(el => {
                    el.detail = {
                        생성일시: formatDate(el.createdAt),
                        확정일시: el.confirmAt ? formatDate(el.confirmAt) : '-',
                        비상사태유형: el.emergencyTypeNm,
                        '조직 수': el.orgCount
                    };
                    if (el.confirmYn == 'Y') {
                        if (!confirmTypeList.value.includes(el.emergencyType)) {
                            confirmTypeList.value.push(el.emergencyType);
                        }
                    }
                });
                degreeList.value = _.cloneDeep(res.list);
                filteredDegreeList.value = _.cloneDeep(res.list);
            })
            .catch(() => {
                endLoading();
            })
            .finally(() => {
                endLoading();
            });
    };
    const originChartData = ref('');
    const btnDetail = (chartId, confirmed, notify = true) => {
        let param = {
            compId: compId,
            searchText: chartId
        };
        openLoading();
        return new Promise(resolve => {
            getEmergencyOrgChartList(param, false)
                .then(res => {
                    inputForm.value = res.list[0];
                    inputForm.value.cmd = confirmed ? 'C' : 'U';
                    originChartData.value = _.cloneDeep(res.list[0].chartData);
                    param.typeId = inputForm.value.emergencyType;
                    getEmergencyRoleList(param, notify).then(res => {
                        roleList.value = res.list;
                    });
                    resolve(true)
                })
                .catch(() => {
                    endLoading();
                })
                .finally(() => {
                    endLoading();
                    // router.push('/EmergencyControlOrganChartDetail');
                });
        });
    };

    const btnConfirm = (param, notify) => {
        openLoading();
        confirmEmergencyOrgChart(param, notify)
            .then(res => {
                btnSearch(false);
            })
            .catch(() => {
                endLoading();
            })
            .finally(() => {
                endLoading();
            });
    };
    const btnSave = notify => {
        openLoading();
        saveEmergencyOrgChart(inputForm.value, notify)
            .then(res => {
                btnSearch(false);
            })
            .catch(() => {
                endLoading();
            })
            .finally(() => {
                endLoading();
            });
    };

    const btnDetailSave = notify => {
        let param = {};
        openLoading();
        return new Promise(resolve => {
            saveEmergencyOrgChart(inputForm.value, notify)
                .then(res => {
                    inputForm.value = res.result;
                    param = {
                        compId: compId,
                        searchText: res.result.chartId
                    };
                    resolve({ success: res.success });
                })
                .catch(() => {
                    endLoading();
                })
                .finally(() => {
                    getEmergencyOrgChartList(param, false)
                        .then(res => {
                            inputForm.value = res.list[0];
                        })
                        .catch(() => {
                            endLoading();
                        })
                        .finally(() => {
                            endLoading();
                        });
                });
        });
    };
    const btnDelete = checkedList => {
        openLoading();
        deleteEmergencyOrgChart(checkedList, true)
            .then(res => {
                btnSearch(false);
            })
            .catch(() => {
                endLoading();
            })
            .finally(() => {
                endLoading();
            });
    };

    const orgnList = ref([]);
    const roleList = ref([]);
    // 검색 필터
    const searchTerm = ref('');
    const applyFilter = () => {
        const filteredData = degreeList.value.filter(item => item.chartNm.toLowerCase().includes(searchTerm.value.toLowerCase()) || formatDate(item.createdAt).toLowerCase().includes(searchTerm.value.toLowerCase()) || formatDate(item.confirmAt).toLowerCase().includes(searchTerm.value.toLowerCase()) || item.emergencyTypeNm.toLowerCase().includes(searchTerm.value.toLowerCase()) || item.orgCount.toString().toLowerCase().includes(searchTerm.value.toLowerCase()));
        filteredDegreeList.value = filteredData;
    };
    return {
        inputForm,
        confirmTypeList,
        degreeList,
        filteredDegreeList,
        searchTerm,
        searchParam,
        orgnList,
        roleList,
        originChartData,
        // function
        initInputForm,
        applyFilter,
        // button
        btnSearch,
        btnDetail,
        btnConfirm,
        btnSave,
        btnDetailSave,
        btnDelete
    };
});
