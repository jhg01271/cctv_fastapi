import { defineStore } from 'pinia';

import router from '@/router';
import BaseView from '@/components/base/BaseView';
const { ref, alertMsg, confirmMsg, t, getCompId, getCurrentDate, formatDate } = BaseView();
import { getMyJobAssign, getJobAssignDetail, saveJobAssign, deleteJobAssign, deleteJobAssigns } from '@/stores/safewiz/participation/api/hseJobAssignmentApi';
import { useButtonListStore } from '@/stores/buttonList';
const layoutStore = useButtonListStore();
import _ from 'lodash';
export const useMyHseJobAssignmentStore = defineStore('myHseJobAssignment', () => {
    const docType = ref('JOB');

    const compId = getCompId();

    const checkedList = ref([]);

    const jobAssignList = ref({});

    const currentDate = ref(getCurrentDate());

    //버튼 리스트
    const buttonList = ref([]);

    const inputForm = ref({
        cmd: '',
        compId: compId, // 사업장 ID
        writeYear: '', //작성년도
        docType: '',
        docNo: '',
        hrId: '',
        hrNm: '',
        backupHrId: '',
        backupHrNm: '',
        orgnNm: '',
        jbrpNm: '',
        assignTask: '',
        useYn: 'Y',
        updatedAt: '',
        updatedBy: '',
        createdAt: '',
        createdBy: '', // 수정일시
        userId: ''
    });

    //API 연결-------------------------------------------------------------------------------

    //HSE 업무분장 조회
    const getJobAssignList = async flag => {
        if (flag) {
            searchParam.value.writeYear = searchParam.value.writeYear.slice(0, 4);
        }
        searchParam.value.docNo = null
        return getMyJobAssign(searchParam.value, true).then(res => {
            if (res && res.success) {
                jobAssignList.value = res.list.map(item => ({
                    ...item,
                    detail: {
                        [t('createdAt')]: formatDate(item.createdAt),
                        [t('whmg_group')]: item.orgnNm || '-',
                        [t('position')]: item.jbrpNm || '-',
                        [t('hseAssign_assignTask')]: item.assignTask || '-',
                        [t('hseAssign_backupHrNm')]: item.backupHrNm || '-',
                        [t('hseAssign_notCompletedTasks')]: item.notCompletedCnt
                    }
                }));
                applyFilter();
            }
            return res; // res 반환
        });
    };

    const originData = ref({});

    //HSE 업무분장 상세조회
    const getMyJobAssignDetailList = notify => {
        if (!searchParam.value.writeYear) {
            router.push('/MyHseJobAssignment');
            return;
        }
        searchParam.value.writeYear = searchParam.value.writeYear.slice(0, 4);
        // searchParam.value.docNo = null
        return getJobAssignDetail(searchParam.value, notify).then(res => {
            if (res && res.success) {
                inputForm.value = res.list;
                originData.value = _.cloneDeep(res.list);
            }
            return res; // res 반환
        });
    };

    //HSE 업무분장 개별삭제
    const deleteJobAssignList = data => {
        return new Promise(resolve => {
            deleteJobAssign(data, true).then(res => {
                if (res && res.success) {
                    resolve({ result: res.result });
                    router.push({ name: 'HseJobAssignment' });
                }
            });
        });
    };

    //HSE 업무분장 일괄 삭제
    const deleteJobAssignLists = data => {
        return new Promise(resolve => {
            deleteJobAssigns(data, true).then(res => {
                if (res && res.success) {
                    resolve({ result: res.result });
                    // 검색어 초기화
                    searchTerm.value = '';
                    //리스트 초기화
                    dataFilterList.value = [];
                    //체크리스트 초기화
                    checkedList.value = [];
                    //조회
                    getJobAssignList(true);
                }
            });
        });
    };

    // HSE 업무분장 추가, 수정
    const saveJobAssignItem = data => {
        return new Promise(resolve => {
            saveJobAssign(data, true)
                .then(res => {
                    if (res.success) {
                        resolve({ result: res.result });
                        // 검색어 초기화
                        searchTerm.value = '';
                        //리스트 초기화
                        dataFilterList.value = [];
                        //체크리스트 초기화
                        checkedList.value = [];
                        // insert 된 데이터는 'U'로 변경
                        inputForm.value.cmd = 'U';
                        //신규일 경우, 추가된 키값 세팅
                        inputForm.value.docNo = res.result.docNo;
                        searchParam.value.docNo = res.result.docNo;
                        getMyJobAssignDetailList(false);
                    }
                    return res;
                })
                .finally(() => {
                    layoutStore.useBtnList = ['btnSearch', 'btnBack', 'btnAdd', 'btnSave', 'btnDelete'];
                });
        });
    };

    //-----------------------------------------------

    // 검색기능

    const searchTerm = ref('');
    const dataFilterList = ref(null);

    const searchParam = ref({
        //compId: compId,
        writeYear: '',
        docType: 'JOB',
        docNo: ''
    });

    const initData = () => {
        // 여기서 API 호출을 통해 데이터를 가져올 수 있습니다.
        applyFilter(); // 필터를 처음에 적용하여 초기 데이터로 그리드를 채움
    };

    // 필터 적용 함수
    const applyFilter = () => {
        const search = (searchTerm.value || '').toLowerCase(); // 검색어가 없을 경우 빈 문자열 사용

        dataFilterList.value = jobAssignList.value.filter(item => {
            const hrNm = item.hrNm ? item.hrNm.toLowerCase() : ''; // hrNm이 null/undefined일 경우 빈 문자열 사용
            const orgnNm = item.orgnNm ? item.orgnNm.toLowerCase() : ''; // orgnNm이 null/undefined일 경우 빈 문자열 사용
            const jbrpNm = item.jbrpNm ? item.jbrpNm.toLowerCase() : ''; // jbrpNm null/undefined일 경우 빈 문자열 사용
            const assignTask = item.assignTask ? item.assignTask.toLowerCase() : '';
            const backupHrNm = item.backupHrNm ? item.backupHrNm.toLowerCase() : '';
            const createdAt = formatDate(item.createdAt) ? formatDate(item.createdAt).toLowerCase() : '';

            return hrNm.includes(search) || orgnNm.includes(search) || jbrpNm.includes(search) || assignTask.includes(search) || backupHrNm.includes(search) || createdAt.includes(search); // 필터 조건에 맞는 항목 반환
        });
    };

    //-----------------------------------------------
    //목록으로 이동
    const goBack = () => {
        //검색어 초기화
        searchTerm.value = '';
        router.push({ name: 'HseJobAssignment' });
    };

    //이동버튼
    const goObject = () => {
        //검색어 초기화
        searchTerm.value = '';
        //페이지 이동
        router.push({
            path: 'MyHseJobAssignment'
        });
    };

    //-----------------------------------------------

    //추가버튼
    const btnAdd = () => {
        buttonList.value = ['btnBack', 'btnSave'];
        inputForm.value = ref({});
        // 추가(I) 플래그 cmd
        inputForm.value.cmd = 'I';
        inputForm.value.useYn = 'Y';
        inputForm.value.createdAt = currentDate;
        inputForm.value.compId = compId;
        inputForm.value.docType = 'JOB';
        inputForm.value.writeYear = searchParam.value.writeYear; //조회된 년도로 데이터 생성

        //페이지 이동
        router.push({
            path: 'MyHseJobAssignmentDetail'
        });
    };

    //-----------------------------------------------

    //저장
    const btnSave = async () => {
        //임시 작성년도 포맷 변경
        inputForm.value.writeYear = inputForm.value.writeYear.substring(0, 4);
        confirmMsg('info', t('msgSave'), '', { fun: saveJobAssignItem, param: [inputForm.value] });
    };

    //-----------------------------------------------

    //삭제
    const btnDelete = async item => {
        if (item == 'D') {
            if (inputForm.value.useYn === 'N') {
                alertMsg('warning', '이미 삭제 처리된 항목입니다.');
                return;
            }
            // detail 에서 삭제
            confirmMsg('warning', t('msgDelete'), '', { fun: deleteJobAssignList, param: inputForm.value });
        } else {
            let param = checkedList.value; // rowKey로 행 데이터를 가져옴
            if (param.length == 0) {
                alertMsg('warning', t('msgNoItem'));
                return;
            }
            if (param.some(el => el.useYn === 'N')) {
                alertMsg('warning', '이미 삭제 처리된 항목입니다.');
                return;
            }
            confirmMsg('warning', t('msgDelete'), ``, { fun: deleteJobAssignLists, param: checkedList.value });
        }
    };

    //useYn 체크
    const toggleUseYn = event => {
        // 체크박스의 체크 상태에 따라 'Y' 또는 'N'을 설정합니다.
        inputForm.value.useYn = event.target.checked ? 'Y' : 'N';
    };
    //-----------------------------------------------

    return {
        inputForm,
        originData,
        jobAssignList,
        buttonList,
        searchParam,
        toggleUseYn,
        checkedList,
        initData,
        searchTerm,
        dataFilterList,
        applyFilter,
        currentDate,
        //라우터
        btnAdd,
        goBack,
        goObject,
        //버튼
        btnDelete,
        btnSave,
        //api
        getJobAssignList,
        getMyJobAssignDetailList
    };
});
