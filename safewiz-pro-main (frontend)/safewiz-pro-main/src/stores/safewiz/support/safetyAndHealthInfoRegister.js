import { defineStore } from 'pinia';

import BaseView from '@/components/base/BaseView';
const { openLoading, endLoading, formatDate, computed, confirmMsg, alertMsg, toastPopup, ref, t, getCompId, getCurrentDate, baseDownload, formatDateForDB } = BaseView();
import router from '@/router';
import _ from 'lodash';
import { getShInfoRegisterList, getMyShInfoRegisterList, saveShInfoRegister, deleteShInfoRegister, getShInfoRegisterReport } from '@/stores/safewiz/support/api/safetyAndHealthInfoRegisterApi.js';

import { useCommunicationStore } from '@/stores/safewiz/support/communication.js';
const communicationStore = useCommunicationStore();

import { useUserInfoStore } from '@/stores/user';
const userInfoStore = useUserInfoStore(); // 현재 사용자 정보

export const useShInfoRegisterStore = defineStore('shInfoRegister', () => {
    const compId = getCompId();
    const searchParam = ref({
        compId: compId,
        writeYear: communicationStore.searchParam.searchText ? communicationStore.searchParam.searchText : getCurrentDate().substring(0, 4)
    });
    const checkedList = ref([]);
    const filterDivList = ref([
        { id: 'month', name: '월' },
        { id: 'orgn', name: '조치조직' }
    ]);
    const currentFilter = ref('month');
    const inputForm = ref({});
    const initInputForm = () => {
        const today = new Date();
        const month = (today.getMonth() + 1).toString().padStart(2, '0'); // 01~12
        const day = today.getDate().toString().padStart(2, '0');          // 01~31
        const receiptDt = formatDate(searchParam.value.writeYear  + month + day)
        inputForm.value = {
            cmd: 'I',
            compId: compId,
            docType: 'SHR',
            hrId: userInfoStore.userId,
            orgnId: userInfoStore.userOrgnId,
            orgnNm: userInfoStore.userOrgnNm,
            jbrpId: userInfoStore.userJbrpId,
            jbrpNm: userInfoStore.userJbrpNm,
            hrNm: userInfoStore.userName,
            useYn: 'Y',
            writeYear: communicationStore.searchParam.searchText,
            receiptDt: receiptDt,
            createdAt: getCurrentDate(),
            deleteFiles: []
        };
    };
    const fileList = ref({
        editFiles: {
            insert: [], // 새로 추가된 파일들
            delete: [] // 삭제할 파일들
        }
    });

    const files = ref([]); // 조회된 파일

    // 버튼 이벤트
    const registerList = ref([]);
    const filteredByMonthList = ref({});
    const filteredByOrgnList = ref({});
    const btnSearch = async notify => {
        searchParam.value = {
            compId: compId,
            writeYear: communicationStore.searchParam.searchText
        };
        openLoading();
        getShInfoRegisterList(searchParam.value, notify)
            .then(res => {
                res.list.forEach(el => {
                    el.docTitle = `${el.writeYear}-${el.docType}-${el.docNo}`;
                    (el.actionOrgnNmTitle = el.actionOrgnNm ? el.actionOrgnNm : '-'),
                        (el.actionDtTitle = el.actionDt ? formattingDate(el.actionDt) : '-'),
                        (el.replyDtTitle = el.replyDt ? formattingDate(el.replyDt) : '-'),
                        (el.detail = {
                            접수번호: el.docTitle,
                            작성자: el.hrNm,
                            조치조직: el.actionOrgnNmTitle,
                            조치일자: el.actionDtTitle,
                            회신일자: el.replyDtTitle
                        });
                    el.cardTitle = formattingDate(el.receiptDt);
                    el.receiptDt = formattingDate(el.receiptDt);
                    el.actionDt = formattingDate(el.actionDt);
                    el.replyDt = formattingDate(el.replyDt);
                });
                registerList.value = res.list;
                addMonthList();
                addOrgList();
            })
            .catch(() => {
                endLoading();
            })
            .finally(() => {
                endLoading();
            });
    };
    const addMonthList = () => {
        filteredByMonthList.value = {};
        const result = {};
        // 데이터 처리 함수
        registerList.value.forEach(list => {
            // 각 부모의 list를 순회하며 그룹화 진행
            if (result[list.receiptDt.substring(5, 7) + '월']) {
                result[list.receiptDt.substring(5, 7) + '월'].push(list);
            } else {
                result[list.receiptDt.substring(5, 7) + '월'] = [list];
            }
        });
        filteredByMonthList.value = _.cloneDeep(result);
        filteredByMonthListBySearch.value = _.cloneDeep(result);
    };
    const addOrgList = () => {
        filteredByOrgnList.value = {};
        const result = {};
        // 데이터 처리 함수
        registerList.value.forEach(list => {
            // 각 부모의 list를 순회하며 그룹화 진행
            if (!list.actionOrgnNm) {
                if (result['미설정']) {
                    result['미설정'].push(list);
                } else {
                    result['미설정'] = [list];
                }
            } else {
                if (result[list.actionOrgnNm]) {
                    result[list.actionOrgnNm].push(list);
                } else {
                    result[list.actionOrgnNm] = [list];
                }
            }
        });
        filteredByOrgnList.value = _.cloneDeep(result);
        filteredByOrgnListBySearch.value = _.cloneDeep(result);
    };
    const btnDetail = async (data, notify = true) => {
        console.log('@@ 상세 조회', data);
        let param = _.cloneDeep(searchParam.value);
        param.writeYear = data.writeYear;
        param.docType = data.docType;
        param.docNo = data.docNo;
        openLoading();
        getShInfoRegisterList(param, notify)
            .then(res => {
                inputForm.value = res.list[0];
                inputForm.value.receiptDt = formattingDate(inputForm.value.receiptDt);
                inputForm.value.actionDt = formattingDate(inputForm.value.actionDt);
                inputForm.value.replyDt = formattingDate(inputForm.value.replyDt);
                files.value = res.list[0].files;
            })
            .catch(() => {
                endLoading();
            })
            .finally(() => {
                router.push('/SafetyAndHealthInfoRegisterDetail');
                endLoading();
            });
    };
    const formattingDate = date => {
        if (!date) {
            return '';
        }
        return `${date.substring(0, 4)}.${date.substring(4, 6)}.${date.substring(6, 8)}`;
    };
    const btnAdd = async (type, data) => {
        initInputForm();
        if (data) {
            if (type === 'month') {
                console.log('@data.receiptDt', data.receiptDt);
                const [year, month, day] = data.receiptDt.split('.');
                inputForm.value.receiptDt = `${year}.${month}.01`;
            } else if (type === 'actionOrgn') {
                inputForm.value.actionOrgnId = data.actionOrgnId;
                inputForm.value.actionOrgnNm = data.actionOrgnNm;
            }
        }
        router.push('/SafetyAndHealthInfoRegisterDetail');
    };

    const btnSave = async notify => {
        openLoading();
        inputForm.value.deleteFiles = fileList.value.editFiles.delete;

        inputForm.value.receiptDt = formatDateForDB(inputForm.value.receiptDt);
        inputForm.value.actionDt = formatDateForDB(inputForm.value.actionDt);
        inputForm.value.replyDt = formatDateForDB(inputForm.value.replyDt);

        const formData = new FormData();
        formData.append('info', new Blob([JSON.stringify(inputForm.value)], { type: 'application/json' }));

        fileList.value.editFiles.insert.forEach(file => {
            formData.append('files', file || new Blob([], { type: 'application/octet-stream' }));
        });

        try {
            const res = await saveShInfoRegister(formData, notify);

            let param = {
                compId: res.result.compId,
                writeYear: res.result.writeYear,
                docType: res.result.docType,
                docNo: res.result.docNo
            };

            const response = await getShInfoRegisterList(param, false);
            inputForm.value = response.list[0];
            inputForm.value.receiptDt = formattingDate(inputForm.value.receiptDt);
            inputForm.value.actionDt = formattingDate(inputForm.value.actionDt);
            inputForm.value.replyDt = formattingDate(inputForm.value.replyDt);
            files.value = response.list[0].files;

            return res; // 저장 성공 시 결과 반환
        } catch (err) {
            console.error(err);
        } finally {
            endLoading();
        }
    };

    const btnDelete = async type => {
        if (type === 'D') {
            let param = [inputForm.value];
            confirmMsg('warning', t('msgDelete'), ``, { fun: deleteOneApi, param: param });
        } else {
            let param = getCheckedList();
            if (!param.length) {
                alertMsg('warning', t('msgNoItem'));
                return;
            }
            if (param.some(el => el.useYn === 'N')) {
                alertMsg('warning', t('msgDeletedItem'));
                return;
            }
            confirmMsg('warning', t('msgDelete'), ``, { fun: deleteApi, param: param });
        }
    };
    const deleteOneApi = async param => {
        openLoading();
        deleteShInfoRegister(param, true)
            .then(res => {
                const searchParam = {
                    writeYear: res.result.writeYear,
                    docType: res.result.docType,
                    docNo: res.result.docNo
                };
                getShInfoRegisterList(searchParam, false).then(response => {
                    inputForm.value = response.list[0];
                    inputForm.value.receiptDt = formattingDate(inputForm.value.receiptDt);
                    inputForm.value.actionDt = formattingDate(inputForm.value.actionDt);
                    inputForm.value.replyDt = formattingDate(inputForm.value.replyDt);
                });
            })
            .catch(() => {
                endLoading();
            })
            .finally(() => {
                // router.push('/SafetyAndHealthInfoRegister')
                endLoading();
            });
    };
    const deleteApi = async param => {
        openLoading();
        deleteShInfoRegister(param, true)
            .then(res => {
                registerList.value = {};
            })
            .catch(() => {
                endLoading();
            })
            .finally(() => {
                btnSearch(false);
                endLoading();
            });
    };

    const btnDownload = async (list, msg = 'msgCheckedPrint') => {
        console.log('@@param', list);
        if (list.length === 0) {
            alertMsg('warning', t('msgNoItem'));
            return;
        }
        confirmMsg('info', t(msg), null, { fun: downloadAction, param: list });
    };
    const downloadAction = list => {
        let searchVO = _.cloneDeep(searchParam.value);
        searchVO.docType = 'SHR';
        searchVO.checkedList = list;
        baseDownload(getShInfoRegisterReport, searchVO);
        // openLoading();
        // getShInfoRegisterReport(searchVO)
        //   .then(res => {
        //     downloadReport(res);
        //   })
        //   .catch(() => {
        //     endLoading();
        //   })
        //   .finally(() => {
        //     endLoading();
        //   });
    };

    const getCheckedList = () => {
        let checkedData = [];
        if (currentFilter.value === 'month') {
            Object.keys(filteredByMonthListBySearch.value).forEach(el => {
                checkedData = [...checkedData, ...filteredByMonthListBySearch.value[el]];
            });
            checkedData = checkedData.filter(el => el.checked);
        } else if (currentFilter.value === 'orgn') {
            Object.keys(filteredByOrgnListBySearch.value).forEach(el => {
                checkedData = [...checkedData, ...filteredByOrgnListBySearch.value[el]];
            });
            checkedData = checkedData.filter(el => el.checked);
        }
        return checkedData;
    };

    const toggleUseYn = event => {
        // 체크박스의 체크 상태에 따라 'Y' 또는 'N'을 설정합니다.
        inputForm.value.useYn = event.target.checked ? 'Y' : 'N';
    };
    const filteredByMonthListBySearch = ref({});
    const filteredByOrgnListBySearch = ref({});
    const searchTerm = ref('');
    const applyFilter = () => {
        // 월
        for (const key in filteredByMonthList.value) {
            const filteredData = filteredByMonthList.value[key].filter(item => item.receiptDt.toLowerCase().includes(searchTerm.value.toLowerCase()) || item.docTitle.toLowerCase().includes(searchTerm.value.toLowerCase()) || item.hrNm.toLowerCase().includes(searchTerm.value.toLowerCase()) || item.actionOrgnNmTitle.toLowerCase().includes(searchTerm.value.toLowerCase()) || formatDate(item.actionDtTitle).toLowerCase().includes(searchTerm.value.toLowerCase()) || formatDate(item.replyDtTitle).toLowerCase().includes(searchTerm.value.toLowerCase()));
            filteredByMonthListBySearch.value[key] = filteredData;
        }
        // 작업장
        for (const key in filteredByOrgnList.value) {
            const filteredData = filteredByOrgnList.value[key].filter(item => item.receiptDt.toLowerCase().includes(searchTerm.value.toLowerCase()) || item.docTitle.toLowerCase().includes(searchTerm.value.toLowerCase()) || item.hrNm.toLowerCase().includes(searchTerm.value.toLowerCase()) || item.actionOrgnNmTitle.toLowerCase().includes(searchTerm.value.toLowerCase()) || formatDate(item.actionDtTitle).toLowerCase().includes(searchTerm.value.toLowerCase()) || formatDate(item.replyDtTitle).toLowerCase().includes(searchTerm.value.toLowerCase()));
            filteredByOrgnListBySearch.value[key] = filteredData;
        }
    };

    // My 안전보건 관리 대장 조회(사용자가 작성한 글, 사용자가 속한 조치 조직)
    const btnMySearch = async notify => {
        searchParam.value = {
            compId: compId,
            writeYear: communicationStore.searchParam.searchText,
            hrId: userInfoStore.userId,
            orgnId: userInfoStore.userOrgnId
        };
        openLoading();
        getMyShInfoRegisterList(searchParam.value, notify)
            .then(res => {
                res.list.forEach(el => {
                    el.docTitle = `${el.writeYear}-${el.docType}-${el.docNo}`;
                    (el.actionOrgnNmTitle = el.actionOrgnNm ? el.actionOrgnNm : '-'),
                        (el.actionDtTitle = el.actionDt ? formattingDate(el.actionDt) : '-'),
                        (el.replyDtTitle = el.replyDt ? formattingDate(el.replyDt) : '-'),
                        (el.detail = {
                            접수번호: el.docTitle,
                            작성자: el.hrNm,
                            조치조직: el.actionOrgnNmTitle,
                            조치일자: el.actionDtTitle,
                            회신일자: el.replyDtTitle
                        });
                    el.cardTitle = formattingDate(el.receiptDt);
                    el.receiptDt = formattingDate(el.receiptDt);
                    el.actionDt = formattingDate(el.actionDt);
                    el.replyDt = formattingDate(el.replyDt);
                });
                registerList.value = res.list;
                addMonthList();
                addOrgList();
            })
            .catch(() => {
                endLoading();
            })
            .finally(() => {
                endLoading();
            });
    };

    // My 안전보건 관리 대장 상세 조회
    const btnMyDetail = async (data, notify = true) => {
        console.log('@@ 상세 조회', data);
        let param = _.cloneDeep(searchParam.value);
        param.writeYear = data.writeYear;
        param.docType = data.docType;
        param.docNo = data.docNo;
        param.hrId = userInfoStore.userId;
        param.orgnId = userInfoStore.userOrgnId;

        openLoading();
        getMyShInfoRegisterList(param, notify)
            .then(res => {
                inputForm.value = res.list[0];
                inputForm.value.receiptDt = formattingDate(inputForm.value.receiptDt);
                inputForm.value.actionDt = formattingDate(inputForm.value.actionDt);
                inputForm.value.replyDt = formattingDate(inputForm.value.replyDt);
                files.value = res.list[0].files;
            })
            .catch(() => {
                endLoading();
            })
            .finally(() => {
                router.push('/MySafetyAndHealthInfoRegisterDetail');
                endLoading();
            });
    };

    // My 안전보건 관리 대장 저장
    const btnMySave = async notify => {
        openLoading();
        inputForm.value.deleteFiles = fileList.value.editFiles.delete;

        inputForm.value.receiptDt = formatDateForDB(inputForm.value.receiptDt);
        inputForm.value.actionDt = formatDateForDB(inputForm.value.actionDt);
        inputForm.value.replyDt = formatDateForDB(inputForm.value.replyDt);

        const formData = new FormData();
        formData.append('info', new Blob([JSON.stringify(inputForm.value)], { type: 'application/json' }));

        fileList.value.editFiles.insert.forEach(file => {
            formData.append('files', file || new Blob([], { type: 'application/octet-stream' }));
        });

        try {
            const res = await saveShInfoRegister(formData, notify);

            let param = {
                compId: res.result.compId,
                writeYear: res.result.writeYear,
                docType: res.result.docType,
                docNo: res.result.docNo,
                hrId: userInfoStore.userId,
                orgnId: userInfoStore.orgnId
            };

            const response = await getMyShInfoRegisterList(param, false);
            inputForm.value = response.list[0];
            inputForm.value.receiptDt = formattingDate(inputForm.value.receiptDt);
            inputForm.value.actionDt = formattingDate(inputForm.value.actionDt);
            inputForm.value.replyDt = formattingDate(inputForm.value.replyDt);
            files.value = response.list[0].files;

            return res; // 저장 성공 시 결과 반환
        } catch (err) {
            console.error(err);
        } finally {
            endLoading();
        }
    };

    // My 안전보건 관리 대장 추가
    const btnMyAdd = async (type, data) => {
        initInputForm();
        if (data) {
            if (type === 'month') {
                console.log('@data.receiptDt', data.receiptDt);
                const [year, month, day] = data.receiptDt.split('.');
                inputForm.value.receiptDt = `${year}.${month}.01`;
            } else if (type === 'actionOrgn') {
                inputForm.value.actionOrgnId = data.actionOrgnId;
                inputForm.value.actionOrgnNm = data.actionOrgnNm;
            }
        }

        router.push('/MySafetyAndHealthInfoRegisterDetail');
    };

    // My 안전보건 관리 대장 삭제
    const btnMyDelete = async type => {
        if (type === 'D') {
            let param = [inputForm.value];
            confirmMsg('warning', t('msgDelete'), ``, { fun: deleteMyApi, param: param });
        } else {
            let param = getCheckedList();
            if (!param.length) {
                alertMsg('warning', t('msgNoItem'));
                return;
            }
            if (param.some(el => el.useYn === 'N')) {
                alertMsg('warning', t('msgDeletedItem'));
                return;
            }
            confirmMsg('warning', t('msgDelete'), ``, { fun: deleteMyApi, param: param });
        }
    };

    const deleteMyApi = async param => {
        openLoading();
        deleteShInfoRegister(param, true)
            .then(res => {
              const searchParam = {
                writeYear: res.result.writeYear,
                docType: res.result.docType,
                docNo: res.result.docNo,
                hrId: userInfoStore.userId,
                orgnId: userInfoStore.userOrgnId
            };
        
            getMyShInfoRegisterList(searchParam, false).then(response => {
                inputForm.value = response.list[0];
                inputForm.value.receiptDt = formattingDate(inputForm.value.receiptDt);
                inputForm.value.actionDt = formattingDate(inputForm.value.actionDt);
                inputForm.value.replyDt = formattingDate(inputForm.value.replyDt);
            });
            })
            .catch(() => {
                endLoading();
            })
            .finally(() => {
                btnMySearch(false);
                endLoading();
            });
    };

    return {
        searchParam,
        filterDivList,
        inputForm,
        registerList,
        searchTerm,
        checkedList,
        fileList,
        files,
        currentFilter,
        filteredByMonthListBySearch,
        filteredByOrgnListBySearch,
        // function
        initInputForm,
        toggleUseYn,
        applyFilter,
        getCheckedList,
        formattingDate,
        // api
        btnSearch,
        btnDetail,
        btnAdd,
        btnSave,
        btnDelete,
        btnDownload,
        btnMySearch,
        btnMyDetail,
        btnMyAdd,
        btnMySave,
        btnMyDelete
    };
});
