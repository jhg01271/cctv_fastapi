import { defineStore } from 'pinia';



import BaseView from '@/components/base/BaseView';
const { ref, confirmMsg, alertMsg } = BaseView();
import { getTask, updateTaskChk } from '@/stores/safewiz/task/api/taskApi.js';
import { useRoute, useRouter } from 'vue-router';
import _ from 'lodash';

import { useMenuStore } from '@/stores/menu.js';
import { AesEncrypt /* AesDecrypt */ } from '@/utils/aes256';

export const useTaskStore = defineStore('Task', () => {
    const router = useRouter(); //
    const route = useRoute(); // 현재 라우트 정보 가져오기

    //Task 데이터
    const taskList = ref({});

    // 조회 기간
    const startDay = ref();
    const endDay = ref();

    //Task 데이터 조회
    const getTaskList = searchParam => {
        return getTask(searchParam, false).then(res => {
            if (res && res.success) {
                taskList.value = res.list;
                filteredTaskList.value = _.cloneDeep(res.list);
                return res.list;
            }
            return res;
        });
    };

    //Task 저장
    const saveTaskChk = param => {
        return updateTaskChk(param, false).then(res => {
            if (res && res.success) {
                return res.list;
            }
            return res;
        });
    };

    //#region ===== 작업(task)과 관련된 경로로 이동 =====
    const menuStore = useMenuStore();

    const goRoute = task => {
        task.taskDetailUserChk = 'Y';
        saveTaskChk(task);
        const splitKey = task.reqInfoKey.split('^&');
        if (task.reqMenuId === '2242') {
            // 협력업체 등록대장 예외처리
            task.routerNm = 'Procurement';
        }
        const param = {
            docType: splitKey[0] ?? '',
                writeYear: splitKey[1] ?? '',
                docNo: splitKey[2] ?? '',
                docSeq: splitKey[3] ?? '',
                docDetailSeq: splitKey[4] ?? '',
                date: task.reqDt.substring(0, 10) // 안전점검일지 사용용도
        }
        router.push({
            name: task.routerNm,
            state: param
        });
    };

    const goToTask = task => {
        const isAuthRoute = router.getRoutes().find(route => route.name === task.routerNm);
        if(!isAuthRoute) {
            alertMsg('warning', '메뉴 접근 권한이 없습니다. \n 관리자에게 권한을 요청하세요.', task.reqMenuNm)
            return
        }
        if (menuStore.selectedCompany === task.compId) {
            goRoute(task);
        } else {
            // 사업장이 변경되어야 할 경우 메시지 출력
            confirmMsg('warning', `[${task.compNm}] \n 사업장으로 이동합니다.`, null, {
                fun: () => {
                    menuStore.isTaskRoute = true;
                    menuStore.selectedCompany = task.compId;
                    menuStore.selectedCompanyName = task.compNm;
                    const aesCompId = AesEncrypt(menuStore.selectedCompany);
                    sessionStorage.setItem('COMP_ID', aesCompId);
                    goRoute(task);
                }
            });
        }
    };
    //#region ===== 현재 해당 페이지가 아니라면 이동 =====
    const goToPageIfNotCurrent = task => {
        if (route.name !== task) {
            // 현재 페이지의 name을 비교
            router.push({
                name: task
            });
        }
    };

    //#endregion

    // 상세 검색 패널
    const searchTermTask = ref('');
    const filteredTaskList = ref([]);
    const applyFilterTask = () => {
        const filteredData = taskList.value.filter(item => item.reqMenuNm.toLowerCase().includes(searchTermTask.value.toLowerCase()) || item.taskDetailContent.toLowerCase().includes(searchTermTask.value.toLowerCase()) || item.reqUserNm.toLowerCase().includes(searchTermTask.value.toLowerCase()));
        filteredTaskList.value = filteredData;
    };

    //#endregion

    return {
        taskList,
        startDay,
        endDay,
        getTaskList,
        saveTaskChk,
        searchTermTask,
        filteredTaskList,
        applyFilterTask,
        //===== 작업(task)과 관련된 경로로 이동 =====
        goToTask,
        //===== 현재 해당 페이지가 아니라면 이동 =====
        goToPageIfNotCurrent
    };
});
