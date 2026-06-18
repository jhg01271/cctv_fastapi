import { defineStore } from 'pinia';
import { computed } from 'vue';
import router from '@/router';
import BaseView from '@/components/base/BaseView';
const { openLoading, endLoading, ref, alertMsg, toastPopup, confirmMsg, getCompId, getCurrentDate, t, groupAndSortByNm } = BaseView();

import { getPrcs, getPrcsDetail, insertPrcs, updatePrcs, deletePrcs, deletePrcss, downloadPrcsExcelTemplate, insertPrcsExcel } from '@/stores/system/base/api/processApi';
import { getPrcsCnfmWorkList } from '@/stores/system/base/api/procWorkApi';
import _ from 'lodash';

import { useButtonListStore } from '@/stores/buttonList';
const layoutStore = useButtonListStore();
layoutStore.useBtnList = ['btnSearch', 'btnBack', 'btnAdd', 'btnSave', 'btnDelete'];

import { getSystemCode } from '@/stores/system/setting/api/SystemCode.js';
import { getHr } from '@/stores/system/base/api/hrApi';
import { getOrganization } from '@/stores/system/base/api/organizationApi';
import { getWp } from '@/stores/system/base/api/workplaceApi';

export const useProcessStore = defineStore('Process', () => {
    //조직 리스트
    const prcsList = ref([]);
    const prcsWork = ref([]);

    const filteredWpList = ref([]);

    //상위 조직
    const compId = getCompId();
    const processId = ref('');

    //현재날짜
    const currentDate = ref(getCurrentDate());

    const headItem = ref([]); // Chip 리스트
    const secondItem = ref([]); // Chip 리스트

    //파일관련
    const fileInfo = ref();

    const setRefs = file1 => {
        fileInfo.value = file1.value;
    };

    const inputForm = ref({});
    const initInputForm = () => {
        inputForm.value = {
            cmd: '',
            cmdArray: '',
            compId: compId,
            compList: [
                {
                    compId: '',
                    nm: ''
                }
            ],
            userId: '',
            processId: '',
            processNm: '',
            fileId: '',
            jbrp: '', // 직위
            jbrpNm: '', // 직위명
            ordSeq: null,
            orgnIdList: [],
            workplaceIdList: [],
            partCompId: '',
            headHrList: [],
            secondHrList: [],
            updatedAt: null,
            updatedBy: null,
            createdAt: null,
            createdBy: null,
            checked: '',
            useYn: 'Y',
            attachId: '', // 첨부 아이디
            riskAssRole: '',
            riskAssRoleNm: '',
            deleteFiles: [] //삭제할 파일 id
        };
    };

    // 현재 선택된 작업장 인덱스
    const selectedWorkplaceIdx = ref(-1);

    //-----------------------------------------------//조직 조회
    const combinedList = []; // 페이지 이동시 필요한 공정리스트
    const getPrcsList = notify => {
        return getPrcs(searchParam.value, notify).then(res => {
            if (res && res.success) {
                // searchTerm.value = ''
                prcsList.value = res.list.map(item => ({
                    ...item
                }));
                initData();
                filteredWpList.value = _.cloneDeep(dataFilterList.value);
                // 한배열에 공정 리스트만 가공하여 담음
                combinedList.value = filteredWpList.value.map(item => item.data).flat();
            }
            return res; // res 반환
        });
    };

    //-----------------------------------------------

    const getPrcsDetailList = (data, notify) => {
        return getPrcsDetail(data, notify).then(res => {
            if (res && res.success) {
                inputForm.value = res.list;

                //chip 변경
                headItem.value = [
                    {
                        id: inputForm.value.headHrId,
                        name: inputForm.value.headHrNm
                    }
                ];
                secondItem.value = [
                    {
                        id: inputForm.value.secondHrId,
                        name: inputForm.value.secondHrNm
                    }
                ];

                // 이전버튼, 다음버튼 기능 수행 시 신규로 추가된 데이터인지 판별 후 없는 경우 추가
                const filteredCombinedList = combinedList.value.filter(item => item.processId === data);

                if(filteredCombinedList.length === 0){
                    combinedList.value.push(inputForm.value);
                }

                selectedWorkplaceIdx.value = combinedList.value.findIndex(item => item.processId === data);
                getCnfmWork(data);
            }
            return res; // res 반환
        });
    };

    //공정 개별삭제
    const deletePrcsList = data => {
        deletePrcs(data, true).then(res => {
            if (res && res.success) {
                router.push({ name: 'ProcessManage' });
            }
        });
    };

    //공정 일괄 삭제
    const deletePrcsLists = data => {
        return new Promise(() => {
            deletePrcss(data, true).then(res => {
                if (res && res.success) {
                    //리스트 초기화
                    dataFilterList.value = [];
                    //체크리스트 초기화
                    checkedList.value = [];
                    getPrcsList(true);
                }
            });
        });
    };

    //공정 추가
    const createPrcs = data => {
        let editFiles = fileInfo.value.editFiles;
        let formData = new FormData();
        formData.append('info', new Blob([JSON.stringify(data)], { type: 'application/json' }));
        editFiles.insert.forEach(element => {
            formData.append('files', element ? element : new Blob([], { type: 'application/octet-stream' }));
        });

        return new Promise(resolve => {
            insertPrcs(formData, true).then(res => {
                if (res.success) {
                    resolve({ result: res.result });
                    // 검색어 초기화
                    searchTerm.value = '';
                    //리스트 초기화
                    dataFilterList.value = [];
                    //체크리스트 초기화
                    checkedList.value = [];
                    searchTerm.value = '';
                    //insert한 데이터는 cmd 'U'로 상태로 변경
                    inputForm.value.cmd = 'U';
                    processId.value = res.result.processId;
                    layoutStore.useBtnList = ['btnBack', 'btnSearch', 'btnAdd', 'btnSave', 'btnDelete'];
                    if (res.result.fileId != null) fileInfo.value.resetEditFiles(res.result.fileId);
                    getPrcsDetailList(res.result.processId);
                }
            });
        });
    };

    // 공정 수정
    const updatePrcsList = data => {
        let editFiles = fileInfo.value.editFiles;
        let formData = new FormData();

        //파일이 수정될 때, 기존 파일을 삭제함
        data.deleteFiles = editFiles.delete;
        formData.append('info', new Blob([JSON.stringify(data)], { type: 'application/json' }));
        editFiles.insert.forEach(element => {
            formData.append('files', element ? element : new Blob([], { type: 'application/octet-stream' }));
        });

        return new Promise(resolve => {
            updatePrcs(inputForm.value.processId, formData, true).then(res => {
                if (res && res.success) {
                    //파일 파라미터 초기화
                    if (res.result.fileId != null) fileInfo.value.resetEditFiles(res.result.fileId);
                    resolve({ result: res.result });
                    getPrcsDetailList(res.result.processId);
                }
            });
        });
    };

    //[작업내용 조회]
    const getCnfmWork = data => {
        return getPrcsCnfmWorkList(data).then(res => {
            if (res && res.success) {
                prcsWork.value = res.list;
            }
            return res; // res 반환
        });
    };

    //-----------------------------------------------

    const checkNumberInput = event => {
        // 입력된 값이 숫자가 아니면 빈 문자열로 변환
        event.target.value = event.target.value.replace(/[^0-9]/g, '');
        // v-model로 바인딩된 값도 업데이트
        inputForm.value.ordSeq = event.target.value;
    };

    //-----------------------------------------------
    //목록으로 이동
    const goBack = () => {
        //검색어 초기화
        searchTerm.value = '';
        router.push({ name: 'ProcessManage' });
    };

    //-----------------------------------------------

    //이동버튼
    const goHr = () => {
        //페이지 이동
        router.push({
            path: 'ProcessManage'
        });
    };

    //-----------------------------------------------

    const searchTerm = ref('');
    const dataFilterList = ref(null);
    const filteredData = ref([]);

    const initData = () => {
        if (!searchTerm.value) {
            //검색어가 없을 시 리스트를 다시 그리도록 dataFilterList 비워줌
            dataFilterList.value = [];
        }

        applyFilter(); // 필터를 처음에 적용하여 초기 데이터로 채움
    };

    const activeFilter = ref('orgn');

    const filterTypeBtn = filterType => {
        activeFilter.value = filterType;
        filterBtn(filterType);
    };

    // 필터 적용 함수
    const applyFilter = () => {
        filteredData.value = prcsList.value.filter(item => item.processNm.toLowerCase().includes(searchTerm.value.toLowerCase()) || item.usageTypeNm.toLowerCase().includes(searchTerm.value.toLowerCase()) || item.orgnList.some(org => org.nm.toLowerCase().includes(searchTerm.value.toLowerCase())) || item.workplaceList.some(org => org.nm.toLowerCase().includes(searchTerm.value.toLowerCase())));

        // activeFilter.value = 'orgn'
        if (activeFilter.value == 'orgn') {
            dataFilterList.value = groupAndSortByNm(filteredData.value, 'orgnList');
        } else if (activeFilter.value == 'work') {
            dataFilterList.value = groupAndSortByNm(filteredData.value, 'workplaceList');
        } else {
            // 본사, 도급, 혼재 구분
            const value1 = filteredData.value.filter(item => item.usageType === '0001');
            const value2 = filteredData.value.filter(item => item.usageType === '0002');
            const value3 = filteredData.value.filter(item => item.usageType === '0003');

            dataFilterList.value = [
                { title: '본사', data: value1 },
                { title: '도급', data: value2 },
                { title: '혼재', data: value3 }
            ];
        }
    };
    if (filteredWpList.value.length > 0) {
        filteredWpList.value = filteredData.value;
    }

    //-----------------------------------------------

    // 오른쪽 상단 정렬 버튼
    const filterBtn = e => {
        searchTerm.value = '';
        // 기존 필터링 데이터를 초기화

        if (e === 'orgn') {
            dataFilterList.value = groupAndSortByNm(prcsList.value, 'orgnList');
        } else if (e === 'work') {
            dataFilterList.value = groupAndSortByNm(prcsList.value, 'workplaceList');
        } else if (e === 'com') {
            // 본사, 도급, 혼재 구분
            const value1 = prcsList.value.filter(item => item.usageType === '0001');
            const value2 = prcsList.value.filter(item => item.usageType === '0002');
            const value3 = prcsList.value.filter(item => item.usageType === '0003');

            dataFilterList.value = [
                { title: '본사', data: value1 },
                { title: '도급', data: value2 },
                { title: '혼재', data: value3 }
            ];
        }
    };

    //-----------------------------------------------

    //추가버튼

    const btnAdd = () => {
        layoutStore.useBtnList = ['btnBack', 'btnSave'];
        inputForm.value = ref();
        // 추가(I) 플래그 cmd
        inputForm.value.cmd = 'I';
        // chip 초기화
        headItem.value = [];
        secondItem.value = [];
        // 사용처 select default 값 세팅
        inputForm.value.usageType = '0001';

        // default 값 세팅
        inputForm.value.useYn = 'Y';
        inputForm.value.createdAt = currentDate;
        inputForm.value.processId = 'temp';
        inputForm.value.headHrList = [];
        inputForm.value.secondHrList = [];
        prcsWork.value = [];
        //파일초기화
        if (fileInfo.value) {
            fileInfo.value.fnRemove();
        }
        //페이지 이동
        router.push({
            path: '/ProcessManageDetail'
        });
    };

    //-----------------------------------------------

    //저장
    const btnSave = async () => {
        console.log('저장', inputForm.value);
        //chip 저장
        if (inputForm.value.orgnList?.length > 0) {
            inputForm.value.orgnIdList = inputForm.value.orgnList.map(item => item.id);
        } else {
            inputForm.value.orgnIdList = [];
        }

        if (inputForm.value.workplaceList?.length > 0) {
            inputForm.value.workplaceIdList = inputForm.value.workplaceList.map(item => item.id);
        } else {
            inputForm.value.workplaceIdList = [];
        }

        if (!inputForm.value.ordSeq) {
            inputForm.value.ordSeq = 99;
        }

        if (inputForm.value.cmd === 'I') {
            inputForm.value.compId = compId;
            //추가
            confirmMsg('info', '저장 하시겠습니까?', '', { fun: createPrcs, param: inputForm.value });
        } else {
            //수정
            confirmMsg('info', '저장 하시겠습니까?', '', { fun: updatePrcsList, param: inputForm.value });
        }
    };

    //-----------------------------------------------
    const checkedList = ref([]);

    //일괄 삭제
    const btnDelete = async item => {
        if (item == 'D') {
            //개별삭제
            confirmMsg('warning', '삭제 하시겠습니까?', '', { fun: deletePrcsList, param: inputForm.value.processId });
        } else {
            let param = checkedList.value; // rowKey로 행 데이터를 가져옴

            if (!param.length) {
                toastPopup('선택된 항목이 없습니다.', 'error');
                return;
            }
            if (param.some(el => el.useYn === 'N')) {
                toastPopup('이미 삭제 처리된 항목입니다.', 'error');
                return;
            }
            confirmMsg('warning', '삭제 하시겠습니까?', ``, { fun: deletePrcsLists, param: param });
        }
    };

    //-----------------------------------------------

    const searchParam = ref({
        compId: compId
    });

    //-----------------------------------------------
    //useYn 체크

    const toggleUseYn = event => {
        // 체크박스의 체크 상태에 따라 'Y' 또는 'N'을 설정합니다.
        inputForm.value.useYn = event.target.checked ? 'Y' : 'N';
    };

    //-----------------------------------------------

    //성별 필터

    const sexDivList = ref([
        { sexDiv: 'M', sexDivNm: '남' },
        { sexDiv: 'W', sexDivNm: '여' }
    ]);

    const filteredSexList = computed(() => {
        let list = sexDivList.value.filter(element => {
            if (element.sexDivList === '') return true;
            return element.sexDiv !== sexDivList.value.sexDivNm;
        });
        return list;
    });

    //-----------------------------------------------

    //사용처 시스템 코드 필터
    const useList = ref([]);
    const filteredUseList = computed(() => {
        let list = useList.value.filter(element => {
            if (element.minorCd === '') return true;
            return element.minorCd !== useList.value.minorNm;
        });
        return list;
    });

    //-----------------------------------------------

    // 양식 다운로드 및 업로드
    const getTemplate = id => {
        downloadPrcsExcelTemplate(id).then(res => {
            console.log('## 템플릿 다운로드 ', res);
        });
    };
    const excelData = ref(null);
    const usageTypeList = ref(null);
    const hrList = ref(null);
    const orgnList = ref(null);
    const wpList = ref(null);
    const createExcel = async () => {
        //#region 팝업관련 데이터 리스트 전부 조회
        openLoading();
        let responses = await getSystemCode({
            majorCd: 'C0010',
            compId: '999999999999'
        });
        if (responses) {
            usageTypeList.value = responses.list;
        }
        responses = await Promise.all([getHr({})]);
        if (responses) {
            hrList.value = responses[0].list;
        }
        responses = await Promise.all([getOrganization({})]);
        if (responses) {
            orgnList.value = responses[0].list;
        }
        responses = await Promise.all([getWp({})]);
        if (responses) {
            wpList.value = responses[0].list;
        }

        let data;
        //엑셀 데이터 없는 경우,
        if (excelData.value == null || excelData.value.length === 0 || excelData.value.length === 1) {
            alertMsg('warning', t('저장할 데이터가 없습니다.'));
            endLoading();
            return;
        }
        if (excelData.value.length <= 2) {
            // 헤더, 예시 데이터 제외
            alertMsg('error', '입력된 데이터가 없습니다.');
            endLoading();
            return;
        }
        excelData.value.shift(); // 예시데이터 제거
        const invalidItems = excelData.value.slice(1).filter(item => {
            // 필수값 체크
            if (item[1] === null || item[1] === '') return item; // 공정명
        });
        if (invalidItems.length > 0) {
            alertMsg('error', '엑셀의 필수값을 입력해주세요.');
            endLoading();
            return;
        }

        let error = false;
        //작업장 정보 백엔드 param으로 가공
        data = excelData.value.slice(1).map(item => {
            try {
                const usageMatch = item[2].trim() !== '' ? usageTypeList.value.find(el => el.minorCd === item[2].trim()) : '';
                if (usageMatch == null) {
                    alertMsg('error', t('공정 : ') + item[1].trim() + '\n' + t('사용처를 찾을 수 없습니다.') + '\n' + t('데이터셋 시트에 맞춰 작성하세요.'));
                    error = true;
                    endLoading();
                    return;
                }

                const headInputList = item[4]
                    ? new Set(
                          item[4]
                              .split(',')
                              .map(v => v.trim())
                              .filter(v => v !== '')
                      )
                    : new Set();
                if (headInputList.size > 0) {
                    const validHrIds = new Set(hrList.value.map(hr => hr.hrId));
                    for (const inputId of headInputList) {
                        if (!validHrIds.has(inputId)) {
                            alertMsg('error', t('공정 : ') + item[1].trim() + '\n' + t('인원을 찾을 수 없습니다.') + '\n' + t('데이터셋 시트에 맞춰 작성하세요.'));
                            error = true;
                            endLoading();
                            break; // 하나라도 없으면 탈출
                        }
                    }
                }

                const secondInputList = item[5]
                    ? new Set(
                          item[5]
                              .split(',')
                              .map(v => v.trim())
                              .filter(v => v !== '')
                      )
                    : new Set();
                if (secondInputList.size > 0) {
                    const validHrIds = new Set(hrList.value.map(hr => hr.hrId));
                    for (const inputId of secondInputList) {
                        if (!validHrIds.has(inputId)) {
                            alertMsg('error', t('공정 : ') + item[1].trim() + '\n' + t('인원을 찾을 수 없습니다.') + '\n' + t('데이터셋 시트에 맞춰 작성하세요.'));
                            error = true;
                            endLoading();
                            break; // 하나라도 없으면 탈출
                        }
                    }
                }

                const orgnInputList = item[6]
                    ? new Set(
                          item[6]
                              .split(',')
                              .map(v => v.trim())
                              .filter(v => v !== '')
                      )
                    : new Set();
                if (orgnInputList.size > 0) {
                    const validOrgnIds = new Set(orgnList.value.map(org => org.orgnId));
                    for (const inputId of orgnInputList) {
                        if (!validOrgnIds.has(inputId)) {
                            alertMsg('error', t('공정 : ') + item[1].trim() + '\n' + t('조직을 찾을 수 없습니다.') + '\n' + t('데이터셋 시트에 맞춰 작성하세요.'));
                            error = true;
                            endLoading();
                            break; // 하나라도 없으면 탈출
                        }
                    }
                }
                const wpInputList = item[7]
                    ? new Set(
                          item[7]
                              .split(',')
                              .map(v => v.trim())
                              .filter(v => v !== '')
                      )
                    : new Set();
                if (wpInputList.size > 0) {
                    const validWpIds = new Set(wpList.value.map(wp => wp.workplaceId));

                    for (const inputId of wpInputList) {
                        if (!validWpIds.has(inputId)) {
                            alertMsg('error', t('공정 : ') + item[1].trim() + '\n' + t('작업장을 찾을 수 없습니다.') + '\n' + t('데이터셋 시트에 맞춰 작성하세요.'));
                            error = true;
                            endLoading();
                            break; // 하나라도 없으면 탈출
                        }
                    }
                }

                if (item[3] == null || item[3] == '') {
                    item[3] = 99;
                }
                return {
                    cmd: 'I',
                    processNm: item[1],
                    usageType: item[2],
                    ordSeq: item[3],
                    headHrList: item[4].length > 0 ? item[4].split(',').map(item => ({hrId: item.trim()})) : [],
                    secondHrList: item[5].length > 0 ? item[5].split(',').map(item => ({hrId: item.trim()})) : [],
                    orgnIdList: item[6].length > 0 ? item[6].split(',').map(item => item.trim()) : [],
                    workplaceIdList: item[7].length > 0 ? item[7].split(',').map(item => item.trim()) : [],
                    remark: item[8],
                    useYn: 'Y'
                };
            } catch (err) {
                alertMsg('error', t('공정 : ') + String(item[1]).trim() + '\n' + t('데이터를 확인하세요.'));
                error = true;
                endLoading();
                return null;
            }
        });
        if (!error) {
            return new Promise(resolve => {
                insertPrcsExcel(data, true)
                    .then(res => {
                        if (res.success) {
                            resolve({ result: res.result });
                            // 검색어 초기화
                            searchTerm.value = '';
                        }
                    })
                    .catch(() => {
                        endLoading();
                    })
                    .finally(() => {
                        getPrcsList(true);
                        endLoading();
                    });
            });
        }
    };

    return {
        initInputForm,
        inputForm,
        processId,
        setRefs,
        filterBtn,
        filterTypeBtn,
        activeFilter,
        initData,
        selectedWorkplaceIdx,
        filteredWpList,
        headItem,
        secondItem,
        combinedList,
        searchParam,
        toggleUseYn,
        checkedList,
        searchTerm,
        applyFilter,
        dataFilterList,
        filteredSexList,
        filteredUseList,
        useList,
        prcsList,
        //라우터
        btnAdd,
        goBack,
        goHr,
        //버튼
        btnSave,
        btnDelete,
        //api
        getPrcsList,
        getPrcsDetailList,
        deletePrcsList,
        deletePrcsLists,
        createPrcs,
        updatePrcsList,
        getCnfmWork,
        prcsWork,
        currentDate,
        checkNumberInput,
        getTemplate,
        createExcel,
        excelData
    };
});
