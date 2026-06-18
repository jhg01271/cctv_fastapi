import { ref } from 'vue';
import { defineStore } from 'pinia';

import BaseView from '@/components/base/BaseView';
const { getCompId, openLoading, endLoading } = BaseView();

import { getPrcs } from '@/stores/system/base/api/processApi.js';
import { getPrcsWorkList } from '@/stores/system/base/api/procWorkApi.js';

import _ from 'lodash';

export const useSelectWorkStore = defineStore('selectWork', () => {
    const compId = getCompId();
    const processList = ref([]);
    const workList = ref([]);

    const menu = ref([]);
    const menuList = ref({
        orgnList: [],
        partCompList: []
    });
    const originProcWorkList = ref([]);

    const selectedIndex = ref(0);
    const currentTab = ref('orgn');

    const filteredProcWorkList = ref([]);
    const clickItem = el => {
        selectedIndex.value = el;
    };

    const filterValue = ref('');
    const filterFromString = () => {
        const filteredResult = originProcWorkList.value
            .map(item => ({
                ...item,
                workList: item.workList.filter(workItem => workItem.workContent.toString().toLowerCase().includes(filterValue.value.toLowerCase()))
            }))
            .filter(el => el.workList.length > 0);
        for(let i = 0; i < filteredResult.length; i++) {
            filteredResult[i].workList.forEach(workItem => {
                workItem.selected = selectedValues.value.some(selectedItem => selectedItem.prcsWorkId === workItem.prcsWorkId);
            });
        }
        clickItem(0);
        console.log('@@ filterValue', filteredResult);
        filteredProcWorkList.value = filteredResult;
    };

    const selectItem = () => {};

    const single = ref(false);

    const init = async (param, selectedList) => {
        filterValue.value = '';
        single.value = param;
        let processes = [];
        openLoading();
        getPrcs(false)
            .then(res => {
                processes = res.list;
            })
            .catch(() => {
                endLoading();
            })
            .finally(async () => {
                processes.forEach(process => {
                    process.workList = [];
                });
                let workResponses = await Promise.all([getPrcsWorkList({})]);
                workList.value = workResponses[0].list;
                workList.value.forEach(work => {
                    work.selected = selectedList.some(list => list.prcsWorkId === work.prcsWorkId);
                    let procIdx = processes.findIndex(prc => prc.processId === work.processId);
                    if (procIdx > -1) {
                        work.processNm = processes[procIdx].processNm;
                        processes[procIdx].workList.push(work);
                    }
                });
                processList.value = _.cloneDeep(processes.filter(el => el.workList.length > 0));
                originProcWorkList.value = _.cloneDeep(processList.value);
                filteredProcWorkList.value = _.cloneDeep(processList.value);
                // 전체 `originProcWorkList`의 선택 상태를 다시 반영
                selectedValues.value = []; // 초기화 후 다시 누적 추가
                originProcWorkList.value.forEach(process => {
                    let selectedVal = process.workList.filter(work => work.selected);
                    selectedValues.value = [...selectedValues.value, ...selectedVal];
                });
                endLoading()
            });

        // filterFromString()
    };

    const singleSelect = item => {
        item.selected = true;
    };
    const selectedValues = ref([]);

    const multiSelect = item => {
        // 선택된 item을 추가하기 전에 이미 존재하는지 확인
        if (item.selected) {
            // `item`이 선택된 경우 중복 없이 `selectedValues`에 추가
            if (!selectedValues.value.some(selectedItem => selectedItem.prcsWorkId === item.prcsWorkId)) {
                selectedValues.value.push(item);
            }
        } else {
            // `item`이 선택 해제된 경우 `selectedValues`에서 제거
            selectedValues.value = selectedValues.value.filter(selectedItem => selectedItem.prcsWorkId !== item.prcsWorkId);
        }

        console.log('@@ selectedValues', selectedValues.value);
    };

    return {
        processList,
        workList,
        originProcWorkList,
        menu,
        currentTab,
        menuList,
        selectedIndex,
        clickItem,
        selectItem,
        selectedValues,
        filteredProcWorkList,
        filterValue,
        filterFromString,
        init,
        singleSelect,
        multiSelect
    };
});
