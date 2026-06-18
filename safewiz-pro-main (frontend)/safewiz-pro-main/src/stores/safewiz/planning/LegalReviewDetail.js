import { defineStore } from 'pinia';

import router from '@/router';
import BaseView from '@/components/base/BaseView';
const { validationStore, ref, alertMsg, watch, getCompId, getCurrentDate, nextTick, confirmMsg, toastPopup, formatDate, formatDateForDB } = BaseView();

import { getLegalReviewDetailMasterList, getLegalReviewDetailList, saveLegalReview, deleteLegalReviewDetail, getLegalReviewReport } from '@/stores/safewiz/planning/api/LegalReviewApi.js';
import _, { forEach } from 'lodash';
import { useLegalReviewStore } from '@/stores/safewiz/planning/LegalReview';
const legalReviewStore = useLegalReviewStore();

import { useLegalManageStore } from '@/stores/safewiz/planning/LegalManage.js';
const legalManageStore = useLegalManageStore();

import { useLegalManageDetailStore } from '@/stores/safewiz/planning/LegalManageDetail';
const legalManageDetailStore = useLegalManageDetailStore();
import { useUserInfoStore } from '@/stores/user.js';
const userStore = useUserInfoStore();

import { gsap } from 'gsap';

import { useLoadingPanelStore } from '@/stores/loadingPanel.js';
const loadingPanelStore = useLoadingPanelStore();

import { useButtonListStore } from '@/stores/buttonList';
const buttonListStore = useButtonListStore();

export const useLegalReviewDetailStore = defineStore('LegalReviewDetail', () => {
    const popSearchTerm = ref('');

    let searchCmd = ref('');
    let dataChange = ref(false);
    let detailWatchTrigger = ref(false);
    let copyTrigger = ref(false);

    const compId = getCompId();
    // APIs
    const legalReviewList = ref({}); // 법규관리
    const legalReviewDetailList = ref({}); // 법규관리 상세
    const legalManageList = ref({});
    const legalManageDetailList = ref([]);

    const filteredLegalReviewList = ref({});
    const fileterLegalReviewData = ref({});
    const filteredLegalReviewDetailList = ref([]);

    const legalReviewDetailSegments = ref([]);
    const segments = ref([]);

    const filteredLegalManageList = ref({});
    const filteredLegalManageDetailList = ref([]);
    const originalDetailList = ref([]); // 검색 필터 전 원본 저장

    const selectedSegments = ref([]);
    const selectedFlag = ref('N');
    const changeLegalMainFlag = ref(false);

    const sampleList = ref({});

    //let previousArrayState = JSON.parse(JSON.stringify(filteredLegalReviewDetailList.value));
    let previousArrayState = JSON.parse(JSON.stringify(filteredLegalReviewDetailList.value));

    watch(
        filteredLegalReviewList,
        (newValue, oldValue) => {
            if (oldValue.cmd && newValue.cmd) {
                dataChange.value = true;
                filteredLegalReviewList.value.change = true;
            }
        },
        { deep: true }
    );

    watch(
        filteredLegalReviewDetailList,

        (newArray, oldArray) => {
            if (detailWatchTrigger.value) {
                return;
            }
            if (oldArray.length > 0 && newArray.length) {
                dataChange.value = true;
                newArray.forEach((newItem, index) => {
                    const oldItem = previousArrayState[index];
                    if (JSON.stringify(newItem) !== JSON.stringify(oldItem) && !newItem.change) {
                        //
                        if (newItem.checked == false && oldItem?.checked == true && !copyTrigger.value) {
                            segments.value[index].dataList[0].checked = false;
                            //filteredLegalReviewDetailList.value[index].checked = false;
                            Changecheck(false, index);
                        } else {
                            segments.value[index].dataList[0].checked = true;
                            //filteredLegalReviewDetailList.value[index].checked = true;
                            Changecheck(true, index);
                        }

                        nextTick();
                    }
                    filteredLegalReviewDetailList.value[index].change = null;
                });
            }
            previousArrayState = JSON.parse(JSON.stringify(newArray));
        },
        { deep: true }
    );

    const searchData = ref({});

    const initSearchData = async () => {
        searchData.value = {
            writeYear: legalReviewStore.searchDate.writeYear,
            docType: legalReviewStore.searchDate.docType,
            docNo: legalReviewStore.searchDate.docNo,
            docDetailSeq: null
        };
    };

    const clearData = async () => {
        legalReviewList.value = {};
        legalReviewDetailList.value = {};
        fileterLegalReviewData.value = {};
        filteredLegalReviewList.value = {};
        filteredLegalReviewList.value.useYn = 'Y';
        filteredLegalReviewDetailList.value = [];
        legalReviewDetailSegments.value = [];
        segments.value = [];
        dataChange.value = false;
    };

    const popClearData = async () => {
        legalManageList.value = {};
        legalManageDetailList.value = [];
        filteredLegalManageDetailList.value = [];
    };

    const initfilteredLegalReviewList = async dataType => {
        filteredLegalReviewList.value = {
            writeYear: legalReviewStore.searchDate.writeYear ? legalReviewStore.searchDate.writeYear.substring(0, 4) : getCurrentDate().substring(0, 4),
            docType: 'LGR',
            docNo: legalReviewStore.searchDate.docNo,
            compId: compId,
            dataType: dataType,
            useYn: 'Y',
            cmd: 'I',
            createdAt: getCurrentDate(),
            createdBy: '',
            updatedBy: ''
        };
    };

    const initfilteredLegalReviewDetailList = async list => {
        list.value = {
            writeYear: legalReviewStore.searchDate.writeYear,
            docType: 'LGR',
            docNo: legalReviewStore.searchDate.docNo,
            docSeq: '',
            legalWriteYear: '',
            legalDocType: '',
            legalDocNo: '',
            legalDocSeq: '',
            legalNm: '',
            legalArticleNm: '',
            revisionAt: getCurrentDate(),
            currentlaws: '',
            facility: '',
            orgnId: '',
            orgnNm: '',
            remarkDc: '',
            useYn: 'Y',
            cmd: 'I',
            createdAt: getCurrentDate(),
            createdBy: '',
            updatedBy: '',
            change: false
        };
    };

    const initLegalReviewOrgnList = async () => {
        filteredLegalReviewDetailList.value.forEach(detail => {
            detail.legalReviewOrgnList?.forEach(el => {
                el.id = el.orgnId;
                el.nm = el.orgnNm;
            });
        });
    };

    //조회
    const btnSearch = async notify => {
        validationStore.clearInvalidClasses();
        validationStore.clearAllErrors();
        if (dataChange.value) {
            confirmMsg('warning', '저장되지 않은 정보가 있습니다. \n 그래도 계속하시겠습니까?', null, { fun: searchAction, param: notify });
        } else {
            searchAction(notify);
        }
    };

    const searchAction = async notify => {
        loadingPanelStore.openLoading();
        await initSearchData();
        await clearData();

        searchCmd.value = legalReviewStore.searchDate.cmd;

        if (searchCmd.value == 'I') {
            initfilteredLegalReviewList();
            return;
        }

        const [masterRes, detailRes] = await Promise.all([getLegalReviewDetailMasterList(searchData.value, notify), getLegalReviewDetailList(searchData.value, notify)]);
        legalReviewList.value = masterRes.list || [];
        legalReviewDetailList.value = detailRes.list || [];

        formatRevisionAt(legalReviewDetailList.value);

        await nextTick();

        if (legalReviewList.value.length > 0) {
            fileterLegalReviewData.value = _.cloneDeep(legalReviewList.value);
            filteredLegalReviewList.value = fileterLegalReviewData.value[0];
        } else {
            initfilteredLegalReviewList();
        }

        filteredLegalReviewDetailList.value = _.cloneDeep(legalReviewDetailList.value);
        for (let i = 0; i < filteredLegalReviewDetailList.value.length; i++) {
            filteredLegalReviewDetailList.value[i] = { ...filteredLegalReviewDetailList.value[i], checked: false, change: false, originUseYn: filteredLegalReviewDetailList.value[i].useYn };
        }

        await initLegalReviewOrgnList();

        await searchClientGrid(filteredLegalReviewDetailList.value);

        nextTick(() => {
            loadingPanelStore.endLoading();
        });
    };

    // 제정/개정 일자 포맷팅
    const formatRevisionAt = list => {
        list.forEach(item => {
            item.revisionAt = formatDate(item.revisionAt);
        });
    };

    const btnBack = async () => {
        validationStore.clearInvalidClasses();
        validationStore.clearAllErrors();
        if (dataChange.value) {
            confirmMsg('warning', '저장되지 않은 정보가 있습니다. \n 그래도 계속하시겠습니까?', null, {
                fun: async () => {
                    await backAction();
                }
            });
        } else {
            await backAction();
        }
    };

    const backAction = async () => {
        router.push('LegalReview');
    };

    const btnAdd = async scrollbarRef => {
        // if(dataChange.value){
        //   confirmMsg('warning', '저장되지 않은 정보가 있습니다. \n 그래도 계속하시겠습니까?', null, {fun: addAction, param: scrollbarRef});
        // }else{
        //   await addAction();
        // }
        validationStore.clearInvalidClasses();
        validationStore.clearAllErrors();
        await addAction(scrollbarRef);
    };

    const addAction = async scrollbarRef => {
        // filteredLegalReviewDetailList.value = filteredLegalReviewDetailList.value.filter(item =>
        //   item.docSeq.length > 0
        // );
        loadingPanelStore.openLoading();
        await initfilteredLegalReviewDetailList(sampleList);

        const newItem = { ...sampleList.value, checked: true, isNew: true };
        filteredLegalReviewDetailList.value.unshift(newItem);

        // 새로운 항목을 추가하면서 selectedSegments에 해당 인덱스를 추가
        const newIndex = segments.value.length; // 새로운 항목의 인덱스
        selectedSegments.value.unshift(newIndex); // selectedSegments에 인덱스를 추가
        selectedFlag.value = 'Y';

        legalReviewDetailSegments.value.unshift(true);

        previousArrayState = JSON.parse(JSON.stringify(filteredLegalReviewDetailList.value));

        detailWatchTrigger.value = true;
        await nextTick();
        segments.value.unshift({
            legalNm: newItem.legalNm,
            legalArticleNm: newItem.legalArticleNm,
            revisionAt: newItem.revisionAt,
            dataList: [newItem]
        });

        for (let i = 0; i < filteredLegalReviewDetailList.value.length; i++) {
            const item = filteredLegalReviewDetailList.value[i];

            if (!item.isNew) {
                continue; // 기존 항목은 건너뜀
            }
            filteredLegalReviewDetailList.value[i] = { ...filteredLegalReviewDetailList.value[i], change: true };

            delete filteredLegalReviewDetailList.value[i].isNew;
        }

        // for(let i in filteredLegalReviewDetailList.value){

        //   filteredLegalReviewDetailList.value[i] = {...filteredLegalReviewDetailList.value[i], change:true};

        // }

        //await searchClientGrid(filteredLegalReviewDetailList.value);

        await nextTick();

        for (let i = 0; i < segments.value.length; i++) {
            await accordionSet(i, 0.5);
        }

        if (scrollbarRef) {
            const element = scrollbarRef.value?.$el?.querySelector('.list'); // 바로 첫 번째 요소를 가져옴

            if (element) {
                element.scrollIntoView({ behavior: 'auto', block: 'start' });
            }
        }

        detailWatchTrigger.value = false;
        dataChange.value = true;
        nextTick(() => {
            loadingPanelStore.endLoading();
        });
    };

    const btnSave = async () => {
        // 메인 제목 유효성 검사
        const mainValid = await validationStore.validateAllFields('form', true);
        if (mainValid) {
            // 제목과 사용여부가 변경된 경우
            if (changeLegalMainFlag.value) {
                return true;
            }

            // 선택 항목 없고 변경도 없으면 경고
            if (segments.value.length > 0) {
                if (selectedFlag.value === 'N' && !changeLegalMainFlag.value) {
                    alertMsg('warning', '선택된 항목이 없습니다.');
                    return false;
                }

                // 세부 항목 유효성 검증
                for (let i in segments.value) {
                    const segment = segments.value[i];
                    if (!segment.dataList[0]?.checked) continue;
                    const valid = await validationStore.validateAllFields('formDetail' + i, true);
                    if (valid) {
                        if (searchCmd.value === 'I' && !copyTrigger.value) {
                            filteredLegalReviewList.value.createdBy = userStore.userId;
                        }
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        }

        return false;
    };

    const saveAction = async () => {
        loadingPanelStore.openLoading();

        filteredLegalReviewList.value.updatedBy = userStore.userId;

        const formData = new FormData();
        const getParam = getCheckedList();

        // 제정/개정 일자 포맷팅
        getParam.forEach(item => {
            item.revisionAt = formatDateForDB(item.revisionAt);
        });

        const jsonData = [filteredLegalReviewList.value, getParam.reverse()];

        formData.append('data', new Blob([JSON.stringify(jsonData)], { type: 'application/json' }));
        try {
            const saveResponse = await saveLegalReview(formData, true);
            if (saveResponse?.success) {
                const { writeYear, docType, docNo } = saveResponse.result;
                const param = { writeYear, docType, docNo, docDetailSeq: null };

                searchCmd.value = 'U';

                await clearData();

                const [masterList, detailList] = await Promise.all([getLegalReviewDetailMasterList(param, false), getLegalReviewDetailList(param, false)]);

                legalReviewList.value = masterList.list || [];
                legalReviewDetailList.value = detailList.list || [];

                formatRevisionAt(legalReviewDetailList.value);

                fileterLegalReviewData.value = _.cloneDeep(legalReviewList.value);
                filteredLegalReviewList.value = fileterLegalReviewData.value[0] || {};
                filteredLegalReviewDetailList.value = _.cloneDeep(legalReviewDetailList.value);

                await initLegalReviewOrgnList();
                await searchClientGrid(filteredLegalReviewDetailList.value);

                dataChange.value = false;
                changeLegalMainFlag.value = false;
                return true;
            }
        } catch (error) {
            console.error('Error saving data:', error);
            return false;
        } finally {
            nextTick(() => {
                loadingPanelStore.endLoading();
            });
        }
    };

    const btnDelete = () => {
        if (!deleteValidationCheck()) return;

        confirmMsg('warning', '선택한 법규 항목을 삭제하시겠습니까?', null, { fun: deleteAction, param: '' });
    };

    const deleteAction = async () => {
        const getParam = getCheckedList();

        try {
            loadingPanelStore.openLoading();

            // 삭제 요청
            const deleteResponse = await deleteLegalReviewDetail(getParam, true);
            if (deleteResponse?.success) {
                searchCmd.value = 'U';

                // 화면 데이터 초기화
                await clearData();
                // 최신 데이터 가져오기
                const [masterList, detailList] = await Promise.all([getLegalReviewDetailMasterList(searchData.value, false), getLegalReviewDetailList(searchData.value, false)]);

                // 상태 업데이트
                legalReviewList.value = masterList.list || [];
                legalReviewDetailList.value = detailList.list || [];

                formatRevisionAt(legalReviewDetailList.value);

                fileterLegalReviewData.value = _.cloneDeep(legalReviewList.value);
                filteredLegalReviewList.value = fileterLegalReviewData.value[0] || {};
                filteredLegalReviewDetailList.value = _.cloneDeep(legalReviewDetailList.value);

                await initLegalReviewOrgnList();
                // 클라이언트 그리드 업데이트
                await searchClientGrid(filteredLegalReviewDetailList.value);
            }
            nextTick(() => {
                loadingPanelStore.endLoading();
            });
        } catch (error) {
            console.error('Error deleting data:', error);
        } finally {
            // 데이터 변경 상태 초기화
            dataChange.value = false;
        }
    };

    const btnCopy = async forceRerenderKey => {
        try {
            validationStore.clearInvalidClasses();
            validationStore.clearAllErrors();
            let result = null;

            loadingPanelStore.openLoading();

            // Promise를 반환하고, 그 결과를 기다린다.
            const confirmation = new Promise(resolve => {
                if (dataChange.value) {
                    confirmMsg('info', '저장되지 않은 정보가 있습니다.\n 그래도 계속하시겠습니까?', null, {
                        fun: async () => {
                            result = await copyAction(forceRerenderKey);
                            resolve(result); // 비동기 처리 후 resolve
                        }
                    });
                } else {
                    confirmMsg('info', '현재 법규검토서 항목을 복사하여 신규 작성하시겠습니까?', null, {
                        fun: async () => {
                            result = await copyAction(forceRerenderKey);
                            resolve(result); // 비동기 처리 후 resolve
                        }
                    });
                }
                nextTick(() => {
                    loadingPanelStore.endLoading();
                });
            });

            return await confirmation; // 비동기 작업이 끝날 때까지 기다림
        } catch (err) {
            console.log('Copy Error:', err);
            return false;
        }
    };

    const copyClear = async () => {
        filteredLegalReviewList.value.writeYear = legalReviewStore.searchDate.writeYear.substring(0, 4);
        filteredLegalReviewList.value.docNo = '';
        filteredLegalReviewList.value.createdAt = getCurrentDate();
        filteredLegalReviewList.value.createdBy = userStore.userId;
        filteredLegalReviewList.value.updatedBy = userStore.userId;
        filteredLegalReviewList.value.cmd = 'I';
        legalReviewStore.searchDate.cmd = 'I';
        filteredLegalReviewDetailList.value = _.cloneDeep(legalReviewDetailList.value);

        copyTrigger.value = true;

        for (let i = 0; i < filteredLegalReviewDetailList.value.length; i++) {
            filteredLegalReviewDetailList.value[i] = {
                ...filteredLegalReviewDetailList.value[i],
                checked: true,
                writeYear: legalReviewStore.searchDate.writeYear.substring(0, 4),
                docNo: '',
                createdAt: getCurrentDate(),
                createdBy: userStore.userId,
                updatedBy: userStore.userId,
                cmd: 'I'
            };
        }
        searchCmd.value = 'I';

        dataChange.value = true;
    };
    const copyAction = async forceRerenderKey => {
        try {
            copyTrigger.value = true;
            await Promise.all([await copyClear()]);
            copyTrigger.value = false;
            forceRerenderKey.value += 1;

            return true;
        } catch {
            return false;
        }
    };

    const downloadReport = () => {
        validationStore.clearInvalidClasses();
        validationStore.clearAllErrors();
        let param = null;
        if (getCheckedList().length > 0) {
            param = getCheckedList();
        } else {
            detailWatchTrigger.value = true;
            param = getAllList();
            previousArrayState = JSON.parse(JSON.stringify(filteredLegalReviewDetailList.value));
            detailWatchTrigger.value = false;
        }
        // if (!param.length) {
        //   alertMsg('warning', '선택된 항목이 없습니다.');
        //   return false;
        // }
        param[0].type = buttonListStore.downloadType;
        confirmMsg('info', '출력 하시겠습니까?', null, {
            fun: async () => {
                loadingPanelStore.openLoading();
                await getLegalReviewReport(param, true).then(res => {
                    let link = document.createElement('a');
                    // 객체를 만들어서 생성

                    let objectUrl = window.URL.createObjectURL(res.data);
                    link.href = objectUrl;
                    link.download = res.headers.filename;
                    link.click();
                });
                nextTick(() => {
                    loadingPanelStore.endLoading();
                });
            }
        });
    };

    const validationCheck = async () => {
        let vo = true;
        if (filteredLegalReviewDetailList.value.length > 0) {
            let param = getCheckedList(); // rowKey로 행 데이터를 가져옴

            if (!param.length) {
                //!filteredLegalManageList.value.change
                alertMsg('warning', '선택된 항목이 없습니다.');
                return false;
            }

            if (param.length) {
                filteredLegalReviewDetailList.value.forEach((el, index) => {
                    if (filteredLegalReviewDetailList.value[index].checked == true) {
                        if (filteredLegalReviewDetailList.value[index].legalDocNo == '' || filteredLegalReviewDetailList.value[index].legalDocNo == null) {
                            toastPopup('저장에 실패하였습니다.', `${index + 1}번째 항목 관련 법규를 입력해주세요.`, 'error');
                            vo = false;
                        }
                        if (filteredLegalReviewDetailList.value[index].revisionAt == '' || filteredLegalReviewDetailList.value[index].revisionAt == null) {
                            toastPopup('저장에 실패하였습니다.', `${index + 1}번째 시행일자를 입력해주세요.`, 'error');
                            vo = false;
                        }
                    }
                });
            }
        } else {
            alertMsg('warning', '선택된 항목이 없습니다.');

            return false;
        }

        return vo;
    };

    const deleteValidationCheck = () => {
        let vo = true;
        let param = getCheckedList(); // rowKey로 행 데이터를 가져옴
        if (!param.length) {
            alertMsg('warning', '선택된 항목이 없습니다.');
            return false;
        }
        for (let i in param) {
            if (param[i].useYn === 'N' && param[i].originUseYn === 'N') {
                alertMsg('warning', '이미 삭제 처리된 항목입니다.');
                return false;
            }
        }
        return vo;
    };

    const getCheckedList = () => {
        let checkedData = [];
        if (filteredLegalReviewDetailList.value.length > 0) {
            filteredLegalReviewDetailList.value.forEach(el => {
                if (el.checked) {
                    el.checkedYn = 'Y';
                    checkedData.push(el);
                }
            });
        }

        return checkedData;
    };

    const getAllList = () => {
        if (filteredLegalReviewDetailList.value.length > 0) {
            filteredLegalReviewDetailList.value.forEach(el => {
                el.checked = true;
            });
        }

        return filteredLegalReviewDetailList.value;
    };

    const getLegalManageList = async () => {
        await legalManageStore.initSearchDate();
        await legalManageDetailStore.initSearchData();

        legalManageStore.searchDate.writeYear = searchData.value.writeYear ? searchData.value.writeYear : getCurrentDate().substring(0, 4);

        await legalManageStore.popSearch(true);
        legalManageList.value = legalManageStore.legalManageList;
        filteredLegalManageList.value = _.cloneDeep(legalManageList.value);
        await clickItem(filteredLegalManageList.value[0]);
    };

    // const clickItem = async (item) => {
    //   // 대분류 항목 클릭 시 소분류 데이터 로드
    //   await legalManageDetailStore.getLegalMAnageDetailList(item);
    //
    //   // 로드된 소분류 데이터를 상태에 저장
    //   legalManageDetailList.value = legalManageDetailStore.legalManageDetailList;
    //
    //   // 검색어가 없는 경우 전체 소분류 데이터를 표시
    //   if (!popSearchTerm.value || popSearchTerm.value.trim() === '') {
    //     filteredLegalManageDetailList.value = _.cloneDeep(legalManageDetailList.value);
    //   } else {
    //     // 검색어가 있을 경우 필터링
    //     await popApplyDetailFilter();
    //   }
    // };
    // const popApplyDetailFilter = async () => {
    //   // 소분류 데이터를 검색어로 필터링
    //   const filteredDetailData = legalManageDetailList.value.filter(detailItem =>
    //       detailItem.articleTitle?.toLowerCase().includes(popSearchTerm.value.toLowerCase())
    //   );
    //
    //   // 필터링된 결과를 소분류 데이터에 반영
    //   filteredLegalManageDetailList.value = filteredDetailData;
    //
    //   await nextTick();
    // };
    // const popApplyFilter = async() => {
    //   // const filteredData = legalManageList.value.filter(item =>
    //   //   item.legalNm.toLowerCase().includes(popSearchTerm.value.toLowerCase()) ||
    //   //   item.docNo.toLowerCase().includes(popSearchTerm.value.toLowerCase())
    //   // );
    //   const filteredData = legalManageList.value
    //   filteredLegalManageList.value = filteredData;
    //
    //   // if (filteredLegalManageList.value.length > 0){
    //   for (const item of legalManageList.value) {
    //     await clickItem(item);
    //   }
    //   await nextTick();
    // }

    const clickItem = async item => {
        if (!item) {
            return;
        }

        loadingPanelStore.openLoading();

        // 이미 선택된 경우 해제
        if (item.selected) {
            item.selected = false;
            loadingPanelStore.endLoading();
            return;
        }

        // 다른 항목 선택 해제 후 현재 항목 선택
        filteredLegalManageList.value.forEach(elem => {
            elem.selected = false;
        });
        item.selected = true;

        // 대분류 클릭 시 소분류 데이터 로드
        await legalManageDetailStore.getLegalMAnageDetailList(item);
        legalManageDetailList.value = legalManageDetailStore.legalManageDetailList;

        // 원본 저장 (검색 복원용)
        originalDetailList.value = _.cloneDeep(legalManageDetailList.value);

        // 현재 검색어 기준으로 필터링
        const keyword = popSearchTerm.value.trim().toLowerCase();
        const filteredDetailData = !keyword ? [...originalDetailList.value] : originalDetailList.value.filter(detailItem => detailItem.articleTitle?.toLowerCase().includes(keyword) || detailItem.articleContent?.toLowerCase().includes(keyword));

        // 필터링된 리스트 적용
        filteredLegalManageDetailList.value = filteredDetailData;

        loadingPanelStore.endLoading();
        return {
            item,
            filteredDetailData
        };
    };

    const filterItem = async item => {
        // 대분류 항목 클릭 시 소분류 데이터 로드
        await legalManageDetailStore.getLegalMAnageDetailList(item);

        // 로드된 소분류 데이터를 상태에 저장
        legalManageDetailList.value = legalManageDetailStore.legalManageDetailList;

        // 소분류 데이터를 필터링
        const filteredDetailData = legalManageDetailList.value.filter(
            detailItem =>
                !popSearchTerm.value ||
                popSearchTerm.value.trim() === '' || // 검색어가 없으면 모두 포함
                detailItem.articleTitle?.toLowerCase().includes(popSearchTerm.value.toLowerCase())
        );
        // filteredLegalManageDetailList.value = filteredDetailData;
        // 소분류 데이터 필터링 결과를 반환
        return {
            item, // 대분류 데이터
            filteredDetailData // 필터링된 소분류 데이터
        };
        //Todo : Sims 해당 코드는 두번씩 타는 문제가 있음 임시로 작동하기 위해 구성하였지만 수정 하여야 한다.
    };

    const popApplyFilter = async () => {
        const keyword = popSearchTerm.value.trim().toLowerCase();

        if (!keyword) {
            filteredLegalManageDetailList.value = [...originalDetailList.value];
            return;
        }

        const result = originalDetailList.value.filter(item => item.articleTitle?.toLowerCase().includes(keyword) || item.articleContent?.toLowerCase().includes(keyword));

        filteredLegalManageDetailList.value = result;

        await nextTick();
    };

    const singleSelect = item => {
        // filteredLegalManageDetailList.value.forEach((el) => {
        //   el.selected = false;
        // });
        item.selected = true;
    };

    const setLegalManageData = async (item, index) => {
        filteredLegalReviewDetailList.value[index].legalWriteYear = item.writeYear;
        filteredLegalReviewDetailList.value[index].legalDocType = item.docType;
        filteredLegalReviewDetailList.value[index].legalDocNo = item.docNo;
        filteredLegalReviewDetailList.value[index].legalDocSeq = item.docSeq;
        filteredLegalReviewDetailList.value[index].legalArticleNm = item.articleTitle;
        filteredLegalReviewDetailList.value[index].currentlaws = item.articleContent;
        filteredLegalReviewDetailList.value[index].legalNm = item.legalNm;
        filteredLegalReviewDetailList.value[index].legalId = item.legalId;

        // 대분류에서 개정일 가져오기
        const revisionAt = filteredLegalManageList.value.filter(filterdItem => filterdItem.writeYear === item.writeYear && filterdItem.docNo === item.docNo)[0].revisionAt;

        filteredLegalReviewDetailList.value[index].revisionAt = revisionAt;

        searchClientGrid(filteredLegalReviewDetailList.value);
    };

    const resetLegalManageData = async index => {
        if (filteredLegalReviewDetailList.value[index]) {
            filteredLegalReviewDetailList.value[index].legalWriteYear = '';
            filteredLegalReviewDetailList.value[index].legalDocType = '';
            filteredLegalReviewDetailList.value[index].legalDocNo = '';
            filteredLegalReviewDetailList.value[index].legalDocSeq = '';
            filteredLegalReviewDetailList.value[index].legalArticleNm = '';
        }
    };

    const popChangeYear = async () => {
        await popClearData();
        await legalManageStore.popSearch(true);
        legalManageList.value = legalManageStore.legalManageList;
        filteredLegalManageList.value = _.cloneDeep(legalManageList.value);
        await clickItem(filteredLegalManageList.value[0]);
    };

    const toggleUseYn = event => {
        // 체크박스의 체크 상태에 따라 'Y' 또는 'N'을 설정합니다.
        filteredLegalReviewList.value.useYn = event.target.checked ? 'Y' : 'N';
        changeLegalMainFlag.value = true;
    };

    const toggleDetailUseYn = (event, index) => {
        // 체크박스의 체크 상태에 따라 'Y' 또는 'N'을 설정합니다.
        filteredLegalReviewDetailList.value[index].useYn = event.target.checked ? 'Y' : 'N';
    };

    const Changecheck = (checked, index) => {
        //const isChecked = event.target.checked

        if (checked) {
            // 체크된 항목이 없으면 selectedSegments에 추가
            if (!selectedSegments.value.includes(index)) {
                selectedSegments.value.push(index);
            }
        } else {
            // 체크 해제된 항목은 selectedSegments에서 제거
            const idx = selectedSegments.value.indexOf(index);
            if (idx !== -1) {
                selectedSegments.value.splice(idx, 1);
            }
        }

        // selectedFlag 업데이트
        selectedFlag.value = selectedSegments.value.length > 0 ? 'Y' : 'N';
    };

    const accordionRefs = ref([]);

    const accordionSet = async (index, duration) => {
        const segment = accordionRefs.value.value[index];

        if (segment) {
            gsap.to(segment, {
                height: legalReviewDetailSegments.value[index] ? 'auto' : 0,
                duration: duration,
                ease: 'customEase'
            });
        } else {
            console.warn(`GSAP target for index ${index} not found`);
        }
    };

    // 아코디언 세팅
    const searchClientGrid = async val => {
        segments.value = [];

        for (let i of val) {
            segments.value.push({
                legalTitle: i.legalDocNo ? i.legalNm + ' - ' + i.legalArticleNm + ' (' + formatDate(i.revisionAt) + ')' : '',
                legalNm: i.legalNm,
                legalArticleNm: i.legalArticleNm,
                revisionAt: i.revisionAt,
                dataList: [i]
            });
        }
    };

    // 같은 법규가 있으면 index, 없으면 -1
    const checkDupe = value => {
        for (var i = 0; i < segments.value.length; i++) {
            if (segments.value[i].legalNm == value.legalNm && segments.value[i].legalArticleNm == value.articleTitle) {
                return i;
            }
        }
        return -1;
    };

    const initAccordion = async () => {
        segments.value.forEach((el, index) => {
            legalReviewDetailSegments.value[index] = false;
            accordionSet(index, 0.5);
        });
    };

    return {
        filteredLegalReviewList,
        filteredLegalReviewDetailList,
        searchCmd,
        filteredLegalManageDetailList,
        accordionRefs,
        searchData,
        legalManageList,
        legalReviewDetailList,
        filteredLegalManageList,
        legalReviewDetailSegments,
        segments,
        selectedSegments,
        selectedFlag,
        changeLegalMainFlag,
        clearData,
        searchAction,
        getLegalManageList,
        clickItem,
        singleSelect,
        popChangeYear,
        accordionSet,
        initAccordion,
        popApplyFilter,
        resetLegalManageData,
        saveAction,
        toggleUseYn,
        toggleDetailUseYn,
        searchClientGrid,
        initfilteredLegalReviewList,
        setLegalManageData,
        popClearData,
        popSearchTerm,
        checkDupe,
        btnSearch,
        btnBack,
        btnSave,
        btnAdd,
        btnDelete,
        btnCopy,
        downloadReport,
        Changecheck
    };
});
