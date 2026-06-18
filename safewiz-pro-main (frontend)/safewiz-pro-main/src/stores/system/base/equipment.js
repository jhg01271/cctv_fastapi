import { defineStore } from 'pinia';

import router from '@/router';
import BaseView from '@/components/base/BaseView';
const { ref, t, alertMsg, toastPopup, confirmMsg, getCompId, getCurrentDate, openLoading, endLoading, groupAndSortByNm, formatDate, formatDateForDB } = BaseView();

import { getEquip, getEquipDetail, getStdEquips, insertEquip, updateEquip, saveStdEquips, deleteEquip, deleteEquips, downloadEquipExcelTemplate, insertEquipExcel } from '@/stores/system/base/api/equipmentApi';

import { getSystemCode } from '@/stores/system/setting/api/SystemCode.js';
import { getHr } from '@/stores/system/base/api/hrApi';
import { getOrganization } from '@/stores/system/base/api/organizationApi';
import { getWp } from '@/stores/system/base/api/workplaceApi';
import { getPrcsWorkList } from '@/stores/system/base/api/procWorkApi';

import _ from 'lodash';
export const useEquipmentStore = defineStore('equipment', () => {
    const compId = getCompId();

    const checkedList = ref([]);

    const currentFilter = ref('orgn');

    //설비 리스트
    const equipmentList = ref([]);

    // 현재 선택된 설비 인덱스
    const selectedEquipmentIdx = ref(-1);

    const searchParam = ref({
        compId: compId
    });

    // 상단 필터링 목록 (조직, 작업장, 본사/도급/혼재)
    const equipDivList = ref([
        { id: 'orgn', name: t('equip_div_orgn') },
        { id: 'workplace', name: t('equip_div_workplace') },
        { id: 'etc', name: t('equip_div_etc') }
    ]);

    const usageTypeList = ref([]); // 사용처 시스템 코드 저장
    const initData = async () => {
        let responses = await Promise.all([
            getSystemCode({
                majorCd: 'C0010',
                compId: compId
            })
        ]);
        if (responses) {
            usageTypeList.value = responses[0].list;
        }
    };
    const inputForm = ref({});
    const initInputForm = () => {
        workItems.value = [];
        inputForm.value = {
            equipmentId: '',
            orgnList: [],
            orgnIdList: [],
            workplaceList: [],
            workplaceIdList: [],
            workList: [],
            createdAt: currentDate
        };
    };
    const workItems = ref([]);
    const selectedWorkValues = ref([]);
    const currentDate = ref(getCurrentDate());

    const dataFilterList = ref([]);
    const applyGrouping = (listToFilter) => {
        const sourceList = listToFilter || equipmentList.value;
        if (currentFilter.value == 'orgn') {
            dataFilterList.value = groupAndSortByNm(sourceList, 'orgnList');
        } else if (currentFilter.value == 'workplace') {
            dataFilterList.value = groupAndSortByNm(sourceList, 'workplaceList');
        } else {
            // 본사, 도급, 혼재 구분
            const value1 = sourceList.filter(item => item.usageType === '0001');
            const value2 = sourceList.filter(item => item.usageType === '0002');
            const value3 = sourceList.filter(item => item.usageType === '0003');

            dataFilterList.value = [
                { title: '본사', data: value1 },
                { title: '도급', data: value2 },
                { title: '혼재', data: value3 }
            ];
        }
    };

    const btnSearch = async notify => {
        openLoading();
        searchTerm.value = '';
        return getEquip(searchParam.value, notify).then(res => {
            if (res && res.success) {
                equipmentList.value = res.list;
                applyGrouping(equipmentList.value);
            }
            endLoading();
            return res; // res 반환
        });
    };

    const btnAdd = (equipmentId, list = []) => {
        initInputForm();
        const form = {
            cmd: 'I',
            equipmentId: equipmentId,
            orgnList: [],
            orgnIdList: [],
            workplaceList: [],
            workplaceIdList: [],
            headHrList: [],
            secondHrList: [],
            workList: [],
            useYn: 'Y',
            createdAt: currentDate,
            usageType: usageTypeList.value[0]?.minorCd || null
        };

        if (list.length > 0) {
            const item = list[0];

            switch (currentFilter.value) {
                case 'orgn':
                    form.orgnIdList = item.orgnList.map(el => el.id);
                    form.orgnList = item.orgnList;
                    break;
                case 'workplace':
                    form.workplaceIdList = item.workplaceList?.map(el => el.id) || [];
                    form.workplaceList = item.workplaceList || [];
                    break;
                case 'etc':
                    form.usageType = item.usageType;
                    break;
            }
        }

        inputForm.value = form;
        console.log('## input', inputForm.value);
        router.push({
            name: 'EquipmentManageDetail'
        });
    };

    const originData = ref({});
    // 상세 보기
    const mergedEquipmentByType = ref([]); // 조직/작업장/본사 타입에 따른 합병리스트
    const btnDetail = (equipmentId, notify = true) => {
        workItems.value = [];

        openLoading();
        return getEquipDetail(equipmentId, notify).then(res => {
            if (res && res.success) {
                inputForm.value = res.list;
                inputForm.value.workplaceIdList = inputForm.value.workplaceList.map(el => el.id);
                inputForm.value.orgnIdList = inputForm.value.orgnList.map(el => el.id);
                inputForm.value.mfDate = formatDate(inputForm.value.mfDate);
                inputForm.value.setupAt = formatDate(inputForm.value.setupAt);
                inputForm.value.stdEq = [{ id: inputForm.value.stdEqId, name: inputForm.value.stdEqNm }];
                res.list.workList.forEach(el => {
                    workItems.value.push({
                        title: el.processNm,
                        id: el.prcsWorkId,
                        nm: el.workContent
                    });
                });
                switch (currentFilter.value) {
                    case 'orgn': {
                        mergedEquipmentByType.value = dataFilterList.value.map(item => item.data).flat();
                        selectedEquipmentIdx.value = mergedEquipmentByType.value.findIndex(el => el.equipmentId === inputForm.value.equipmentId);
                        break;
                    }
                    case 'workplace': {
                        mergedEquipmentByType.value = dataFilterList.value.map(item => item.data).flat();
                        selectedEquipmentIdx.value = mergedEquipmentByType.value.findIndex(el => el.equipmentId === inputForm.value.equipmentId);
                        break;
                    }
                }
                originData.value = _.cloneDeep(inputForm.value);
                router.push({
                    name: 'EquipmentManageDetail'
                });
            }

            endLoading();
            return res; // res 반환
        });
    };

    // 추가
    const insertEquipment = param => {
        console.log('###', param);
        let editFiles = fileInfo.value.editFiles;
        let formData = new FormData();
        param.mfDate = formatDateForDB(param.mfDate);
        param.setupAt = formatDateForDB(param.setupAt);
        formData.append('info', new Blob([JSON.stringify(param)], { type: 'application/json' }));
        editFiles.insert.forEach(element => {
            formData.append('files', element ? element : new Blob([], { type: 'application/octet-stream' }));
        });
        openLoading();
        return new Promise(resolve => {
            resolve({ result: { equipmentId: '123' } });
            insertEquip(formData, true)
                .then(res => {
                    if (res && res.success) {
                        fileInfo.value.resetEditFiles(res.result.fileId);
                        btnDetail(res.result.equipmentId, false);
                        endLoading();
                        resolve({ result: res.result });
                    }
                })
                .finally(() => {
                    endLoading();
                });
        });
    };

    // 수정
    const updateEquipment = param => {
        let editFiles = fileInfo.value.editFiles;
        let formData = new FormData();
        inputForm.value.deleteFiles = editFiles.delete;
        param.mfDate = formatDateForDB(param.mfDate);
        param.setupAt = formatDateForDB(param.setupAt);
        editFiles.insert.forEach(element => {
            formData.append('files', element ? element : new Blob([], { type: 'application/octet-stream' }));
        });
        formData.append('info', new Blob([JSON.stringify(param)], { type: 'application/json' }));
        openLoading();
        let equipmentId = ''
        return new Promise(resolve => {
            updateEquip(inputForm.value.equipmentId, formData, true)
                .then(res => {
                    if (res && res.success) {
                        if (res.result.fileId != null) {
                            fileInfo.value.resetEditFiles(res.result.fileId);
                        }
                        equipmentId = res.result.equipmentId
                        // fileInfo.value.resetEditFiles(res.result.fileId);
                        resolve({ result: res.result });
                        // btnSearch();
                    }
                })
                .finally(() => {
                    btnDetail(equipmentId, false);
                    endLoading();
                });
        });
    };

    const btnSave = () => {
        console.log('@@ 저장 파라미터 ==> ', inputForm.value);
        if (inputForm.value.cmd === 'I') {
            //추가
            confirmMsg('info', t('msgSave'), '', { fun: insertEquipment, param: inputForm.value });
        } else {
            //수정
            confirmMsg('info', t('msgSave'), '', { fun: updateEquipment, param: inputForm.value });
        }
    };

    const btnSaveTypeofEquipment = values => {
        // values.forEach(el => {
        //   el.compId = compId
        // })
        openLoading();
        saveStdEquips(compId, values, true)
            .then(res => {
                if (res && res.success) {
                    // resolve({ result: res.result })
                }
            })
            .finally(() => {
                endLoading();
            });
    };

    //설비 삭제
    const deleteEquipment = equipmentId => {
        openLoading();
        return deleteEquip(equipmentId, true).then(res => {
            if (res && res.success) {
                router.push({ name: 'EquipmentManage' });
            }
            endLoading();
            return res; // res 반환
        });
    };
    //설비 일괄 삭제
    const deleteEquipments = inputForm => {
        openLoading();
        return deleteEquips(inputForm, true).then(res => {
            if (res && res.success) {
                //체크리스트 초기화
                checkedList.value = [];
                btnSearch();
            }
            endLoading();
            return res; // res 반환
        });
    };

    const fileInfo = file => {
        fileInfo.value = file.value;
    };
    const toggleUseYn = event => {
        // 체크박스의 체크 상태에 따라 'Y' 또는 'N'을 설정합니다.
        inputForm.value.useYn = event.target.checked ? 'Y' : 'N';
    };

    //일괄 삭제
    const btnDelete = async item => {
        if (item == 'D') {
            //개별삭제
            confirmMsg('warning', t('msgDelete'), '', { fun: deleteEquipment, param: inputForm.value.equipmentId });
        } else {
            let param = checkedList.value; // rowKey로 행 데이터를 가져옴

            if (!param.length) {
                alertMsg('warning', t('msgNoItem'));
                return;
            }
            if (param.some(el => el.useYn === 'N')) {
                alertMsg('warning', t('msgDeletedItem'));
                return;
            }
            confirmMsg('warning', t('msgDelete'), ``, { fun: deleteEquipments, param: param });
        }
    };
    const searchTerm = ref('');
    const filteredData = ref([]);
    // 필터 적용 함수
    const applyFilter = () => {
        const list = equipmentList.value.filter(item => item.equipmentNm.toLowerCase().includes(searchTerm.value.toLowerCase()));
        applyGrouping(list);
    };

    //-----------------------------------------------

    // 양식 다운로드 및 업로드
    const getTemplate = id => {
        downloadEquipExcelTemplate(id).then(res => {
            console.log('## 템플릿 다운로드 ', res);
        });
    };
    const excelData = ref(null);
    const stdEquipList = ref(null);
    const hrList = ref(null);
    const orgnList = ref(null);
    const wpList = ref(null);
    const prcsWorkList = ref(null);
    const createExcel = async () => {
        //#region 팝업관련 데이터 리스트 전부 조회
        openLoading();
        let responses = await Promise.all([getStdEquips({})]);
        if (responses) {
            stdEquipList.value = responses[0].list;
        }
        responses = await getSystemCode({
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
        responses = await Promise.all([getPrcsWorkList({})]);
        if (responses) {
            prcsWorkList.value = responses[0].list;
        }

        let data;
        const matchedPrcsWorks = [];
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
            if (item[1] === null || item[1] === '') return item; // 설비유형명
            if (item[2] === null || item[2] === '') return item; // 설비명
        });
        if (invalidItems.length > 0) {
            alertMsg('error', '엑셀의 필수값을 입력해주세요.');
            endLoading();
            return;
        }

        let error = false;
        // 설비 정보 백엔드 param으로 가공
        data = excelData.value.slice(1).map(item => {
            try {
                const stdEquipMatch = item[1].trim() !== '' ? stdEquipList.value.find(el => el.stdEqId === item[1].trim()) : '';
                if (stdEquipMatch == null) {
                    alertMsg('error', t('설비 : ') + item[2].trim() + '\n' + t('설비 유형을 찾을 수 없습니다.') + '\n' + t('데이터셋 시트에 맞춰 작성하세요.'));
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
                            alertMsg('error', t('설비 : ') + item[2].trim() + '\n' + t('인원을 찾을 수 없습니다.') + '\n' + t('데이터셋 시트에 맞춰 작성하세요.'));
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
                            alertMsg('error', t('설비 : ') + item[2].trim() + '\n' + t('인원을 찾을 수 없습니다.') + '\n' + t('데이터셋 시트에 맞춰 작성하세요.'));
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
                            alertMsg('error', t('설비 : ') + item[2].trim() + '\n' + t('조직을 찾을 수 없습니다.') + '\n' + t('데이터셋 시트에 맞춰 작성하세요.'));
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
                            alertMsg('error', t('설비 : ') + item[2].trim() + '\n' + t('작업장을 찾을 수 없습니다.') + '\n' + t('데이터셋 시트에 맞춰 작성하세요.'));
                            error = true;
                            endLoading();
                            break; // 하나라도 없으면 탈출
                        }
                    }
                }
                const prcsWorkInputList = item[8]
                    ? new Set(
                          item[8]
                              .split(',')
                              .map(v => v.trim())
                              .filter(v => v !== '')
                      )
                    : new Set();
                if (prcsWorkInputList.size > 0) {
                    const validPrcsWorkIds = new Set(prcsWorkList.value.map(prcsWork => prcsWork.prcsWorkId));

                    for (const inputId of prcsWorkInputList) {
                        if (!validPrcsWorkIds.has(inputId)) {
                            alertMsg('error', t('설비 : ') + item[2].trim() + '\n' + t('공정 작업 내용을 찾을 수 없습니다.') + '\n' + t('데이터셋 시트에 맞춰 작성하세요.'));
                            error = true;
                            endLoading();
                            break;
                        } else {
                            const matched = prcsWorkList.value.find(prcsWork => prcsWork.prcsWorkId === inputId);
                            if (matched) {
                                matchedPrcsWorks.push(matched);
                            }
                        }
                    }
                }

                const usageMatch = item[10].trim() !== '' ? usageTypeList.value.find(el => el.minorCd === item[10].trim()) : '';
                if (usageMatch == null) {
                    alertMsg('error', t('공정 : ') + item[2].trim() + '\n' + t('사용처를 찾을 수 없습니다.') + '\n' + t('데이터셋 시트에 맞춰 작성하세요.'));
                    error = true;
                    endLoading();
                    return;
                }
                let mfDate = item[14].toString().trim() === '' ? null : item[14].toString().trim();
                let setupAt = item[15].toString().trim() === '' ? null : item[15].toString().trim();

                //날짜 유효성 검사
                if (!isValidDate(mfDate) || !isValidDate(setupAt)) {
                    alertMsg('error', t('설비 : ') + item[2].trim() + '\n' + t('날짜 입력 형식(yyyyMMdd)에 맞게 입력해주세요.'));
                    error = true;
                    endLoading();
                    return;
                }

                if (item[3] == null || item[3] == '') {
                    item[3] = 99;
                }
                return {
                    cmd: 'I',
                    stdEqId: item[1],
                    equipmentNm: item[2],
                    ordSeq: item[3],
                    headHrList: item[4].length > 0 ? item[4].split(',').map(item => ({ hrId: item.trim() })) : [],
                    secondHrList: item[5].length > 0 ? item[5].split(',').map(item => ({ hrId: item.trim() })) : [],
                    orgnIdList: item[6].length > 0 ? item[6].split(',').map(item => item.trim()) : [],
                    workplaceIdList: item[7].length > 0 ? item[7].split(',').map(item => item.trim()) : [],
                    workList: _.uniqBy(matchedPrcsWorks, 'prcsWorkId'),
                    setupLocation: item[9],
                    usageType: item[10],
                    mgmtNum: item[11],
                    mfComp: item[12],
                    mfSpec: item[13],
                    mfDate: item[14],
                    setupAt: item[15],
                    remark: item[16],
                    useYn: 'Y'
                };
            } catch (err) {
                alertMsg('error', t('설비 : ') + String(item[1]).trim() + '\n' + t('데이터를 확인하세요.'));
                error = true;
                endLoading();
                return null;
            }
        });
        console.log('#data', data);
        if (!error) {
            return new Promise(resolve => {
                insertEquipExcel(data, true)
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
                        //리스트 초기화
                        btnSearch(true);
                        endLoading();
                    });
            });
        }
    };
    //날짜 형식 유효성 검사
    const isValidDate = dateStr => {
        if (dateStr === null || dateStr == '') return true;

        // 정규 표현식을 이용해 yyyyMMdd 형식 확인
        const datePattern = /^\d{8}$/; // 정확히 8자리 숫자인지 확인
        if (!datePattern.test(dateStr)) return false;

        const year = parseInt(dateStr.substring(0, 4), 10);
        const month = parseInt(dateStr.substring(4, 6), 10) - 1; // 월은 0부터 시작
        const day = parseInt(dateStr.substring(6, 8), 10);

        const date = new Date(year, month, day);

        return date.getFullYear() === year && date.getMonth() === month && date.getDate() === day;
    };

    return {
        initInputForm,
        inputForm,
        checkedList,
        currentFilter,
        equipDivList,
        searchTerm,
        mergedEquipmentByType,
        selectedEquipmentIdx,
        usageTypeList,
        fileInfo,
        workItems,
        selectedWorkValues,
        originData,
        // function
        initData,
        toggleUseYn,
        applyFilter,
        equipmentList,
        dataFilterList,
        applyGrouping,
        // api
        deleteEquip,
        deleteEquips,
        //버튼
        btnSearch,
        btnAdd,
        btnDelete,
        btnSave,
        btnSaveTypeofEquipment,
        btnDetail,
        insertEquipment,
        getTemplate,
        createExcel,
        excelData
    };
});