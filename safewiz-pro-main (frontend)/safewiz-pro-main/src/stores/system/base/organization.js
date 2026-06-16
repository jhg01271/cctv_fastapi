import { defineStore } from 'pinia';
import router from '@/router';
import BaseView from '@/components/base/BaseView';
const { openLoading, endLoading, alertMsg, ref, toastPopup, confirmMsg, getCompId, getCurrentDate, t, formatDate } = BaseView();
import { getOrganizationDetail, getOrganization, insertOrganization, updateOrganization, deleteOrganization, deleteOrganizations } from '@/stores/system/base/api/organizationApi';
import { getSafetyOrgList } from '@/stores/safewiz/participation/api/safetyOrgChartApi';
import { getHr } from '@/stores/system/base/api/hrApi';
import { loadFileFromServer } from '@/utils/iFileLoader.js';
import { getSystemCodeCustom } from '@/stores/system/setting/api/SystemCode';
import { downloadOrgnExcelTemplate, insertOrgnExcel } from '@/stores/system/base/api/organizationApi.js';

import { useButtonListStore } from '@/stores/buttonList';
const layoutStore = useButtonListStore();

export const useOrganizationStore = defineStore('organization', () => {
    // 조직 리스트
    const organizationList = ref([]);
    const headerList = ref([]);
    const compId = getCompId();
    const orgnId = ref('');
    const peopleList = ref([]);
    const inputForm = ref({});

    //유저 리스트
    const userList = ref([]);

    // 초기화 함수
    const initInputForm = () => {
        inputForm.value = {
            cmd: '',
            cmdArray: '',
            compId,
            orgnId: '',
            orgnNm: '',
            orgnDesc: '',
            upPrntOrgnId: '',
            prntOrgnId: '',
            orgnLv: '',
            ordSeq: null,
            headHrId: '',
            headHrNm: '',
            primaryId: null,
            secondHrId: '',
            secondHrNm: '',
            secondaryId: null,
            updatedAt: null,
            updatedBy: null,
            createdAt: null,
            createdBy: null,
            checked: '',
            useYn: 'Y'
        };
    };

    // 데이터 초기화 함수
    const initData = async () => {
        await getOrgnList(false); // 조직 리스트를 가져옵니다.
        // 추가적으로 필요한 초기화 작업을 수행할 수 있습니다.
    };

    //엑셀 업로드 데이터
    const excelData = ref();

    const currentDate = ref(getCurrentDate());
    const searchTerm = ref('');
    const orgnList = ref(null);
    const checkedList = ref([]);

    const manager = ref({ id: null, hrNm: '', signature: '', img: '' });
    const subManager = ref({ id: null, hrNm: '', signature: '', img: '' });

    // 조직 조회
    const getOrgnList = async notify => {
        openLoading();
        const res = await getOrganization(searchParam.value, notify);
        if (res?.success) {
            organizationList.value = res.list.map(item => ({
                ...item,
                detail: {
                    [t('orgn_createdAt')]: formatDate(item.createdAt),
                    [t('orgn_resultCnt')]: item.resultCnt
                }
            }));
            applyFilter();
        }
        endLoading();
        return res; // res 반환
    };

    // 인원 조회
    const getUserList = async notify => {
        const res = await getHr(searchParam.value, notify);
        if (res?.success) {
            userList.value = res.list.filter(item => item.useYn === 'Y' && (item.partCompId === '' || item.partCompId === null));
        }
        return res; // res 반환
    };

    // 조직 상세 조회
    const getOrgnDetail = async (data, notify) => {
        const res = await getOrganizationDetail(data, notify);
        if (res?.success) {
            inputForm.value = res.list;

            manager.value = {
                id: inputForm.value.headHrId,
                hrNm: inputForm.value.headHrNm,
                img: await loadFileFromServer(inputForm.value.primaryId)
            };

            subManager.value = {
                id: inputForm.value.secondHrId,
                hrNm: inputForm.value.secondHrNm,
                img: await loadFileFromServer(inputForm.value.secondaryId)
            };
        }
        return res; // res 반환
    };

    // 조직 개별 삭제
    const deleteOrgn = async data => {
        const res = await deleteOrganization(data, true);
        if (res?.success) {
            router.push({ name: 'organizationManage' });
        }
    };

    // 조직 일괄 삭제
    const deleteOrgns = async data => {
        const res = await deleteOrganizations(data, true);
        if (res?.success) {
            searchTerm.value = ''; // 검색어 초기화
            orgnList.value = []; // 리스트 초기화
            checkedList.value = []; // 체크리스트 초기화
            getOrgnList(true);
        }
    };

    // 조직 추가
    const createOrgn = async data => {
        const res = await insertOrganization(data, true);
        if (res?.success) {
            // inputForm.value.cmd = 'U'; // 저장 성공 시 cmd 값을 'U'로 변경
            await getOrgnDetail(res.result.orgnId, false);
            await getHrList({ orgnId: res.result.orgnId });
            layoutStore.useBtnList = ['btnBack', 'btnAdd', 'btnSave', 'btnDelete'];
            return { result: res.result };
        }
        return null;
    };

    // 조직 수정
    const updateOrgn = async data => {
        const res = await updateOrganization(inputForm.value.orgnId, data, true);
        return res?.success ? { result: res.result } : null;
    };

    // 인원 조회
    const getHrList = async data => {
        try {
            const res = await getHr(data);
            if (res?.success) {
                peopleList.value = await Promise.all(
                    res.list.map(async item => {
                        const image = await loadFileFromServer(item.logoId);
                        return {
                            id: item.hrId,
                            hrNm: item.hrNm,
                            signature: item.signature,
                            img: image || null,
                            isDragged: false
                        };
                    })
                );
            }
            return res; // 결과 반환
        } catch (error) {
            console.error('Error fetching HR list or images', error);
            return null;
        }
    };

    // 입력 검증
    const checkNumberInput = event => {
        event.target.value = event.target.value.replace(/[^0-9]/g, '');
        inputForm.value.ordSeq = event.target.value;
    };

    // 목록으로 이동
    const goBack = () => {
        searchTerm.value = '';

        router.push({ name: 'organizationManage' });
    };

    // 이동 버튼
    const goHr = () => {
        router.push({ path: 'HRManage' });
    };

    // 필터 적용
    const applyFilter = () => {
        const filteredData = organizationList.value.filter(item => item.orgnNm.toLowerCase().includes(searchTerm.value.toLowerCase()) || (item.createdAt && formatDate(item.createdAt).toLowerCase().includes(searchTerm.value.toLowerCase())) || (item.resultCnt !== null && item.resultCnt.toString().includes(searchTerm.value)));
        orgnList.value = filteredData;
    };

    // 추가 버튼
    const addOrg = () => {
        layoutStore.useBtnList = ['btnBack', 'btnAdd', 'btnSave'];
        initInputForm(); // 초기화
        peopleList.value = []; // 인원 리스트 초기화
        manager.value = {}; // 조직장 초기화
        subManager.value = {}; // 부장 초기화
        inputForm.value.cmd = 'I'; // 추가 플래그
        inputForm.value.useYn = 'Y';
        inputForm.value.createdAt = currentDate;
        orgnId.value = '';
        router.push({ path: '/organizationManageDetail' });
    };

    // 저장
    const btnSave = async () => {
        const message = t('msgSave');
        const action = inputForm.value.cmd === 'I' ? createOrgn : updateOrgn;
        if (!inputForm.value.ordSeq) {
            inputForm.value.ordSeq = 99;
        }
        confirmMsg('info', message, '', { fun: action, param: inputForm.value });
    };

    // 일괄 삭제
    const btnDelete = async item => {
        if (item === 'D') {
            confirmMsg('warning', t('msgDelete'), '', { fun: deleteOrgn, param: inputForm.value.orgnId });
        } else {
            let param = checkedList.value;
            if (!param.length) {
                alertMsg('warning', t('msgNoItem'));
                return;
            }
            if (param.some(el => el.useYn === 'N')) {
                alertMsg('warning', t('msgDeletedItem'));
                return;
            }
            confirmMsg('warning', t('msgDelete'), '', { fun: deleteOrgns, param });
        }
    };

    // useYn 체크
    const toggleUseYn = event => {
        inputForm.value.useYn = event.target.checked ? 'Y' : 'N';
    };

    const searchParam = ref({ compId });
    const safetyOrgList = ref([]);
    const btnSearch = async (notify = true) => {
        let responses = await Promise.all([getSafetyOrgList({ compId: getCompId() }, true)]);
        if (responses) {
            safetyOrgList.value = responses[0].list.filter(item => item.useYn === 'Y');
        }
    };
    // 초기 데이터 로드
    initData(); // 이 부분을 함수 정의 아래로 이동했습니다.

    const getOrgnTemplate = id => {
        openLoading();
        downloadOrgnExcelTemplate(id).then(res => {
            console.log('## 템플릿 다운로드 ', res);
            endLoading();
        });
    };

    //엑셀 인원 추가
    const createOrgnExcel = async () => {
        openLoading();
        let data;
        let pass = true;

        excelData.value = await excelData.value.slice(2); //excelData.value[2] 부터 입력된 데이터 시작

        //엑셀 데이터 없는 경우
        if (excelData.value == null || excelData.value.length === 0) {
            alertMsg('warning', t('저장할 데이터가 없습니다.'));
            endLoading();
            return;
        }
        const invalidItems = excelData.value.filter(item => {
            // 필수값 체크
            if (item[1] === null || item[1] === '') return item; // 조직명
        });

        if (invalidItems.length > 0) {
            alertMsg('error', '엑셀의 필수값을 입력해주세요.');
            endLoading();
            return;
        }
        data = await excelData.value.map(item => {
            item[2] = item[2] !== '' ? item[2] : 99;

            return {
                orgnNm: item[1],
                ordSeq: item[2],
                orgnDesc: item[3]
            };
        });
        if (pass) {
            insertOrgnExcel(data, true).then(res => {
              if(res.success){
                getOrgnList(true)
              }
            }).catch((err) => {
                endLoading();
            })
        }
        endLoading();
    };

    return {
        initInputForm,
        inputForm,
        orgnId,
        manager,
        subManager,
        initData,
        searchParam,
        toggleUseYn,
        checkedList,
        searchTerm,
        applyFilter,
        orgnList,
        addOrg,
        goBack,
        goHr, // 라우터
        btnSave,
        btnDelete, // 버튼
        organizationList,
        userList,
        createOrgn,
        updateOrgn,
        deleteOrgn,
        deleteOrgns,
        getOrgnList,
        getUserList,
        getOrgnDetail,
        headerList,
        currentDate,
        checkNumberInput,
        getHrList,
        peopleList,
        btnSearch,
        safetyOrgList,
        excelData,
        getOrgnTemplate,
        createOrgnExcel
    };
});
