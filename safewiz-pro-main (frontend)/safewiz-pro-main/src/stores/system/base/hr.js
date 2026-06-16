import { defineStore } from 'pinia';
import { computed, nextTick } from 'vue';
import router from '@/router';
import BaseView from '@/components/base/BaseView';
const { ref, toastPopup, alertMsg, confirmMsg, getCompId, getCurrentDate, t, formatDate, openLoading, endLoading, formatDateForDB } = BaseView();

import { getHr, getHrDetail, getOrgHistoryList, insertHr, updateHr, deleteHr, deleteHrs, insertHrExcel, downloadHrExcelTemplate, resetPassword } from '@/stores/system/base/api/hrApi';
import { useButtonListStore } from '@/stores/buttonList';
import { getOrganization } from '@/stores/system/base/api/organizationApi.js';
import { getPartner } from '@/stores/system/base/api/partnerApi.js';
import { getCompList } from '@/stores/system/setting/api/compApi.js';
import { getSystemCode } from '@/stores/system/setting/api/SystemCode.js';

import { getHrJbrp } from '@/stores/system/base/api/hrApi';
import { Buffer } from 'buffer';
const layoutStore = useButtonListStore();

export const useHrStore = defineStore('Hr', () => {
    //조직 리스트
    const hrList = ref([]);
    const orgHistory = ref([]);

    //상위 조직
    const compId = getCompId();
    const hrId = ref('');

    //현재날짜
    const currentDate = ref(getCurrentDate());

    const partnerPopup = ref();
    const mainCompPopup = ref();
    const compPopup = ref();
    const orgnPopup = ref();
    const systemPopup = ref();

    //파일관련
    const fileInfo = ref();

    const dialogPostNo = ref();

    //엑셀 업로드
    const excelData = ref();

    const setRefs = (partner, mainComp, comp, orgn, post, file1, system) => {
        partnerPopup.value = partner.value;
        mainCompPopup.value = mainComp.value;
        compPopup.value = comp.value;
        orgnPopup.value = orgn.value;
        dialogPostNo.value = post.value;
        fileInfo.value = file1.value;
        systemPopup.value = system.value;
    };

    const inputForm = ref({});
    const initInputForm = () => {
        (inputForm.value = {
            cmd: 'I',
            cmdArray: '',
            compId: compId,
            compList: [],
            userId: '',
            hrId: null,
            hrNm: null,
            jbrp: null, // 직위
            jbrpNm: null, // 직위명
            orgnId: null,
            orgnNm: null,
            ordSeq: null,
            partCompId: null,
            partCompNm: null,
            upPrnthrId: null,
            prnthrId: null,
            headHrId: null,
            headHrNm: null,
            secondHrId: null,
            secondHrNm: null,
            updatedAt: null,
            updatedBy: null,
            createdAt: null,
            createdBy: null,
            checked: '',
            useYn: 'Y',
            attachId: '', // 첨부 아이디
            riskAssRole: '',
            riskAssRoleNm: '',
            deleteFiles: [], //삭제할 파일 id,
            clntId: ''
        }),
            (affCompList.value = []),
            (mgrCompList.value = []);
    };

    //-----------------------------------------------//조직 조회
    const getHrList = notify => {
        openLoading();
        return getHr(searchParam.value, notify)
            .then(res => {
                if (res && res.success) {
                    searchTerm.value = '';
                    hrList.value = res.list.map(item => {
                        const detail = {};

                        detail[t('affCompNm')] = item.affCompNm;
                        // 조직 값이 있으면 본사 인원 => 조직 표시
                        if (item.orgnNm) {
                            detail[t('orgn')] = item.orgnNm;
                        }
                        // 업체 값이 있으면 협력사 인원 => 협력사 표시
                        if (item.partCompNm) {
                            detail[t('partner')] = item.partCompNm;
                        }
                        detail[t('position')] = item.jbrpNm;
                        detail[t('telNo')] = item.phone;
                        detail[t('email')] = item.email;

                        return {
                            ...item,
                            detail
                        };
                    });

                    initData();
                }
                endLoading();
                return res; // res 반환
            })
            .catch(() => {
                endLoading();
            });
    };

    const getHrDetailList = (data, notify) => {
        return getHrDetail(data, notify).then(res => {
            if (res && res.success) {
                getOrgHistory(data, false);
                inputForm.value = res.list;
                inputForm.value.workingAt = formatDate(res.list.workingAt);
                inputForm.value.fireAt = formatDate(res.list.fireAt);
                inputForm.value.birthDt = formatDate(res.list.birthDt);

                // 소속 사업장 배열
                affCompList.value.splice(0, affCompList.value.length, {
                    id: res.list.affCompId,
                    name: res.list.affCompNm
                });

                // 조직 배열
                orgnPopupSelectedList.value = [
                    {
                        id: res.list.orgnId,
                        name: res.list.orgnNm
                    }
                ];

                orgnPopupSelectedList.value.splice(0, orgnPopupSelectedList.value.length, {
                    id: res.list.orgnId,
                    name: res.list.orgnNm
                });

                // 직위 배열
                jbrpPopupSelectedList.value = [
                    {
                        id: res.list.jbrp,
                        name: res.list.jbrpNm
                    }
                ];

                jbrpPopupSelectedList.value.splice(0, jbrpPopupSelectedList.value.length, {
                    id: res.list.jbrp,
                    name: res.list.jbrpNm
                });

                // 업체 배열
                partnerPopupSelectedList.value = [
                    {
                        id: res.list.partCompId,
                        name: res.list.partCompNm
                    }
                ];

                partnerPopupSelectedList.value.splice(0, partnerPopupSelectedList.value.length, {
                    id: res.list.partCompId,
                    name: res.list.partCompNm
                });

                // 관리 사업장 배열
                if (res.list.mgrCompId && res.list.mgrCompNm) {
                    const mgrCompIds = res.list.mgrCompId.split(';').map(id => id.trim()); // ID 배열
                    const mgrCompNms = res.list.mgrCompNm.split(';').map(name => name.trim()); // Name 배열

                    // ID와 Name을 매칭하여 객체 리스트 생성
                    mgrCompList.value = mgrCompIds.map((id, index) => ({
                        id: id,
                        name: mgrCompNms[index]
                    }));
                }

                // 협력사 partCompId가 존재하는 경우 false, 존재하지 않는 경우 true
                if (partnerPopupSelectedList.value[0].id && partnerPopupSelectedList.value[0].id !== null) {
                    isHeadOffice.value = false;
                } else {
                    isHeadOffice.value = true;
                }
            }
            return res; // res 반환
        });
    };

    //인원 개별삭제
    const deleteHrList = data => {
        deleteHr(data, true).then(res => {
            if (res && res.success) {
                router.push({ name: 'HRManage' });
            }
        });
    };

    //인원 일괄 삭제
    const deleteHrLists = data => {
        return new Promise(() => {
            deleteHrs(data, true).then(res => {
                if (res && res.success) {
                    // 검색어 초기화
                    searchTerm.value = '';
                    //리스트 초기화
                    dataFilterList.value = [];
                    //체크리스트 초기화
                    checkedList.value = [];
                    getHrList(true);
                }
            });
        });
    };

    const orgnList = ref([]);
    const parterCompList = ref([]);
    const mgrCompList = ref([]);
    const affCompList = ref([]);
    const compList = ref([]);
    const jbrpList = ref([]);
    const sexDivList = ref([]);
    const riskAssRoleList = ref([]);

    //엑셀 인원 추가
    const createHrExcel = async () => {
        let data;

        //엑셀 데이터 없는 경우,
        if (excelData.value == null || excelData.value.length === 0 || excelData.value.length === 1) {
            alertMsg('warning', t('저장할 데이터가 없습니다.'));
            return;
        }

        openLoading();
        //#region 팝업관련 데이터 리스트 전부 조회
        let responses = await Promise.all([getOrganization({}, false)]);
        if (responses) {
            orgnList.value = responses[0].list;
        }
        responses = await Promise.all([getPartner({}, false)]);
        if (responses) {
            parterCompList.value = responses[0].list;
        }
        responses = await Promise.all([getCompList({}, false)]);
        if (responses) {
            compList.value = responses[0].list;
        }
        responses = await Promise.all([getHrJbrp(false)]);
        if (responses) {
            jbrpList.value = responses[0].list;
        }
        responses = await getSystemCode(
            {
                majorCd: 'C0048',
                compId: '999999999999'
            },
            false
        );
        if (responses) {
            sexDivList.value = responses.list;
        }
        responses = await getSystemCode(
            {
                majorCd: 'C0006',
                compId: compId
            },
            false
        );
        if (responses) {
            riskAssRoleList.value = responses.list;
        }
        //#endregion
        if (excelData.value.length <= 2) {
            // 헤더, 예시 데이터 제외
            alertMsg('error', '입력된 데이터가 없습니다.');
            endLoading();
            return;
        }
        excelData.value.shift(); // 예시데이터 제거
        const invalidItems = excelData.value.slice(1).filter(item => {
            // 필수값 체크
            if (item[1] === null || item[1] === '') return item; // 이름
            if (item[2] === null || item[2] === '') return item; // 아이디
            if (item[4] === null || item[4] === '') return item; // 이메일
            if (item[5] === null || item[5] === '') return item; // 전화번호
            if ((item[9] === null || item[9] === '') && (item[10] === null || item[10] === '')) return item; // 조직, 업체
        });

        if (invalidItems.length > 0) {
            alertMsg('error', '엑셀의 필수값을 입력해주세요.');
            endLoading();
            return;
        }

        let error = false;
        //인원 정보 백엔드 param으로 가공
        data = excelData.value.slice(1).map(item => {
            //조직
            try {
                const sexMatch = item[6].trim() !== '' ? sexDivList.value.find(el => el.minorCd === item[6].trim()) : '';
                if (sexMatch == null) {
                    alertMsg('error', t('아이디 : ') + item[1].trim() + '\n' + t('성별을 찾을 수 없습니다.') + '\n' + t('데이터셋 시트에 맞춰 작성하세요.'));
                    error = true;
                    endLoading();
                    return;
                }
                const jbrpMatch = item[7].toString().trim() !== '' ? jbrpList.value.find(el => el.jbrpId === item[7].trim()) : '';
                if (jbrpMatch == null) {
                    alertMsg('error', t('아이디 : ') + item[1].trim() + '\n' + t('직위를 찾을 수 없습니다.') + '\n' + t('데이터셋 시트에 맞춰 작성하세요.'));
                    error = true;
                    endLoading();
                    return;
                }

                if((item[9].trim() !== null && item[9].trim() !== '') && (item[10].trim() !== null && item[10].trim() !== '')) {
                    console.log('@ ', item[9].trim())
                    console.log('@ ', item[10].trim())
                    alertMsg('error', t('아이디 : ') + item[1].trim() + '\n' + t('조직, 업체는 하나만 입력 가능합니다.') + '\n' + t('데이터셋 시트에 맞춰 작성하세요.'));
                    error = true;
                    endLoading();
                    return;
                }
                const orgnMatch = orgnList.value.find(el => el.orgnId === item[9].trim());
                if (orgnMatch == null && (item[10].trim() === null || item[10].trim() === '')) {
                    alertMsg('error', t('아이디 : ') + item[1].trim() + '\n' + t('조직을 찾을 수 없습니다.') + '\n' + t('데이터셋 시트에 맞춰 작성하세요.'));
                    error = true;
                    endLoading();
                    return;
                }
                const parterCompMatch = parterCompList.value.find(el => el.partCompId === item[10].trim());
                if (parterCompMatch == null && (item[9].trim() === null || item[9].trim() === '')) {
                    alertMsg('error', t('아이디 : ') + item[1].trim() + '\n' + t('업체를 찾을 수 없습니다.') + '\n' + t('데이터셋 시트에 맞춰 작성하세요.'));
                    error = true;
                    endLoading();
                    return;
                }
                // if (orgnMatch == null && parterCompMatch == null) {
                //     alertMsg('error', t('아이디 : ') + item[1].trim() + '\n' + t('조직 혹은 업체를 찾을 수 없습니다.') + '\n' + t('데이터셋 시트에 맞춰 작성하세요.'));
                //     error = true;
                //     endLoading();
                //     return;
                // }

                const emailPattern = /^[A-Za-z0-9_\\.\\-]+@[A-Za-z0-9\\-]+\.[A-za-z0-9\\-]+/;
                if (item[4].trim() && !emailPattern.test(item[4].trim())) {
                    alertMsg('error', t('아이디 : ') + item[1].trim() + '\n' + t('유효하지 않은 형식의 이메일입니다.'));
                    error = true;
                    endLoading();
                }

                const phonePattern = /^0\d{1,2}-\d{3,4}-\d{4}$/;
                if (item[5].trim() && !phonePattern.test(item[5].trim())) {
                    alertMsg('error', t('아이디 : ') + item[1].trim() + '\n' + t('유효하지 않은 형식의 번호입니다.'));
                    error = true;
                    endLoading();
                }

                let birthDt = item[8].toString() === '' ? null : item[8].toString();
                let workingAt = item[11].toString() === '' ? null : item[11].toString();

                //날짜 유효성 검사
                if (!isValidDate(birthDt) || !isValidDate(workingAt)) {
                    alertMsg('error', t('아이디 : ') + item[1].trim() + ' ' + t('날짜 입력 형식(yyyyMMdd)에 맞게 입력해주세요.'));
                    error = true;
                    endLoading();
                    return;
                }
                return {
                    cmd: 'I',
                    userId: String(item[2]).trim(),
                    userPs: Buffer.from(String(item[2]).trim()).toString('base64'),
                    hrNm: String(item[1]).trim(),
                    orgnId: orgnMatch ? orgnMatch.orgnId: null,
                    email: item[4].trim(),
                    phone: item[5].trim(),
                    affCompId: compId,
                    useYn: 'Y',
                    sexDiv: sexMatch ? sexMatch.minorCd : null,
                    birthDt: birthDt,
                    jbrp: jbrpMatch ? jbrpMatch.jbrpId : null,
                    partCompId: parterCompMatch ? parterCompMatch.partCompId : null,
                    workingAt: workingAt,
                    ordSeq: typeof item[3] === 'number' ? parseInt(item[3].toString().trim()) : 99
                };
            } catch (err) {
                alertMsg('error', t('아이디 : ') + String(item[1]).trim() + '\n' + t('데이터를 확인하세요.'));
                error = true;
                endLoading();
                return null;
            }
        });
        console.log('## 저장될 엑셀 데이터 => ', data);
        if (!error) {
            return new Promise(resolve => {
                insertHrExcel(data, true)
                    .then(res => {
                        if (res.success) {
                            resolve({ result: res.result });
                            // 검색어 초기화
                            searchTerm.value = '';
                            //리스트 초기화
                            dataFilterList.value = [];
                            //체크리스트 초기화
                            checkedList.value = [];
                        }
                    })
                    .catch(() => {
                        endLoading();
                    })
                    .finally(() => {
                        getHrList(true);
                        endLoading();
                    });
            });
        }
    };
    const getHrTemplate = id => {
        downloadHrExcelTemplate(id).then(res => {
            console.log('## 템플릿 다운로드 ', res);
        });
    };

    //날짜 형식 유효성 검사
    const isValidDate = dateStr => {
        if (dateStr === null) return true;

        // 정규 표현식을 이용해 yyyyMMdd 형식 확인
        const datePattern = /^\d{8}$/; // 정확히 8자리 숫자인지 확인
        if (!datePattern.test(dateStr)) return false;

        const year = parseInt(dateStr.substring(0, 4), 10);
        const month = parseInt(dateStr.substring(4, 6), 10) - 1; // 월은 0부터 시작
        const day = parseInt(dateStr.substring(6, 8), 10);

        const date = new Date(year, month, day);

        return date.getFullYear() === year && date.getMonth() === month && date.getDate() === day;
    };

    const isHeadOffice = ref(true); // true = 본사, false = 협력사

    const inputValue = ref('본사'); // 본사로 시작

    // 체크박스를 클릭하면 본사/협력사 값 변경
    const updateCompType = () => {
        if (isHeadOffice.value) {
            inputValue.value = '본사';
        } else {
            inputValue.value = '협력사';
        }
        isHeadOffice.value = !isHeadOffice.value; // 상태 변경
    };

    //인원 추가
    const createHr = data => {
        let editFiles = fileInfo.value.editFiles;
        let formData = new FormData();

        data.workingAt = formatDateForDB(data.workingAt);
        data.fireAt = formatDateForDB(data.fireAt);
        data.birthDt = formatDateForDB(data.birthDt);
        // 초기 인원 생성 시 userId를 인코딩하여 전달
        // 후에 백엔드에서 Spring Security 에서 비밀번호 인코딩 처리
        data.userPs = Buffer.from(data.userId).toString('base64');

        // 소속 사업장 데이터가 있는 경우만 추가
        if (affCompList.value !== null && affCompList.value.length > 0) {
            data.affCompId = affCompList.value[0].id;
        }

        // 직위 데이터가 있는 경우만 추가
        if (jbrpPopupSelectedList.value.length > 0 && jbrpPopupSelectedList.value) {
            data.jbrp = jbrpPopupSelectedList.value[0].id;
        }
        data.mgrCompId = mgrCompList.value.map(item => item.id).join(';');

        if (isHeadOffice.value) {
            data.orgnId = orgnPopupSelectedList.value[0].id;

            // 본사 선택 시 협력사 데이터 초기화
            inputForm.partCompId = '';
            inputForm.partCompNm = '';
            partnerPopupSelectedList.value = [];
        } else if (!isHeadOffice.value) {
            data.partCompId = partnerPopupSelectedList.value[0].id;

            // 협력사 선택 시 본사 데이터 초기화
            inputForm.orgnId = '';
            inputForm.orgnNm = '';
            orgnPopupSelectedList.value = [];
        }

        // 순번 데이터가 안들어가있는 경우 99로 설정정
        if (inputForm.ordSeq === null) {
            data.ordSeq = 99;
        }
        formData.append('info', new Blob([JSON.stringify(data)], { type: 'application/json' }));
        editFiles.insert.forEach(element => {
            formData.append('files', element ? element : new Blob([], { type: 'application/octet-stream' }));
        });

        return new Promise(resolve => {
            insertHr(formData, true).then(res => {
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
                    layoutStore.useBtnList = ['btnSearch', 'btnBack', 'btnAdd', 'btnSave', 'btnDelete'];

                    if (res.result.fileId != null) fileInfo.value.resetEditFiles(res.result.fileId);

                    hrId.value = res.result.hrId;
                    getHrDetailList(res.result.hrId, false);
                }
            });
        });
    };

    // 인원 수정
    const updateHrList = data => {
        let editFiles = fileInfo.value.editFiles;
        let formData = new FormData();
        
        data.workingAt = formatDateForDB(data.workingAt);
        data.fireAt = formatDateForDB(data.fireAt);
        data.birthDt = formatDateForDB(data.birthDt);

        if (affCompList.value !== null && affCompList.value.length > 0) {
            data.affCompId = affCompList.value[0].id;
        }

        if (jbrpPopupSelectedList.value.length > 0 && jbrpPopupSelectedList.value) {
            data.jbrp = jbrpPopupSelectedList.value[0].id;
        } else {
            data.jbrp = '';
        }
        data.mgrCompId = mgrCompList.value.map(item => item.id).join(';');

        if (isHeadOffice.value) {
            data.orgnId = orgnPopupSelectedList.value[0].id;

            // 본사 선택 시 협력사 데이터 초기화
            inputForm.partCompId = '';
            inputForm.partCompNm = '';
            partnerPopupSelectedList.value = [];
            data.partCompId = '';
        } else if (!isHeadOffice.value) {
            data.partCompId = partnerPopupSelectedList.value[0].id;

            // 협력사 선택 시 본사 데이터 초기화
            inputForm.orgnId = '';
            inputForm.orgnNm = '';
            orgnPopupSelectedList.value = [];
            data.orgnId = '';
        }

        // 순번 데이터가 안들어가있는 경우 99로 설정정
        if (inputForm.ordSeq === null) {
            data.ordSeq = 99;
        }

        //파일이 수정될 때, 기존 파일을 삭제함
        data.deleteFiles = editFiles.delete;
        formData.append('info', new Blob([JSON.stringify(data)], { type: 'application/json' }));
        editFiles.insert.forEach(element => {
            formData.append('files', element ? element : new Blob([], { type: 'application/octet-stream' }));
        });

        return new Promise(resolve => {
            updateHr(inputForm.value.hrId, formData, true).then(res => {
                if (res && res.success) {
                    //파일 파라미터 초기화
                    if (res.result.fileId != null) fileInfo.value.resetEditFiles(res.result.fileId);

                    resolve({ result: res.result });
                    getHrDetailList(res.result.hrId, false);
                }
            });
        });
    };

    // 인원 변경 이력
    const getOrgHistory = (data, notify = true) => {
        return new Promise(resolve => {
            getOrgHistoryList(data, notify).then(res => {
                if (res && res.success) {
                    resolve({ result: res.result });
                    orgHistory.value = res.list;
                }
            });
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
        router.push({ name: 'HRManage' });
    };

    //-----------------------------------------------

    //이동버튼
    const goHr = () => {
        //페이지 이동
        router.push({
            path: 'HRManage'
        });
    };

    //-----------------------------------------------

    // 주소 검색 팝업 열기
    const emitOpenPostcodeDialog = () => {
        dialogPostNo.value.onOpen();
    };

    // 주소 검색 팝업 닫기
    const closePostcodeDialog = () => {
        dialogPostNo.value.onClose();
    };

    // 주소 선택 및 업데이트 후 팝업 닫기
    const onPostcodeComplete = data => {
        inputForm.value.zipCd = data.zonecode;
        inputForm.value.addrs1 = data.address;

        dialogPostNo.value.onClose();
    };

    //-----------------------------------------------

    const searchTerm = ref('');
    const dataFilterList = ref(null);
    const filteredData = ref([]);

    const initData = () => {
        if (!searchTerm.value) {
            dataFilterList.value = [];
        }
        applyFilter(); // 필터를 처음에 적용하여 초기 데이터로 채움
    };

    // 필터 적용 함수
    const applyFilter = () => {
        // Y 값을 우선 정렬
        const sortedList = [...hrList.value].sort((a, b) => b.useYn.localeCompare(a.useYn));

        // 검색어를 기반으로 필터링된 데이터
        filteredData.value = sortedList.filter(item => 
            item.hrNm?.toLowerCase().includes(searchTerm.value.toLowerCase()) || 
            item.phone?.toLowerCase().includes(searchTerm.value.toLowerCase()) || 
            item.birthDt?.toLowerCase().includes(searchTerm.value.toLowerCase()) || 
            item.email?.toLowerCase().includes(searchTerm.value.toLowerCase()) || 
            item.jbrpNm?.toLowerCase().includes(searchTerm.value.toLowerCase()) || 
            item.orgnNm?.toLowerCase().includes(searchTerm.value.toLowerCase()) || 
            item.remark?.toLowerCase().includes(searchTerm.value.toLowerCase()) ||
            item.affCompNm?.toLowerCase().includes(searchTerm.value.toLowerCase())
        );

        // 검색어가 없을 경우, 전체 리스트 사용
        const dataList = searchTerm.value ? filteredData.value : sortedList;

        // activeFilter에 따라 데이터 정렬 및 그룹화 적용
        switch (activeFilter.value) {
            case 'orgn':
                dataFilterList.value = groupAndSort(dataList, 'orgnId', ' 협력사');
                break;
            case 'com':
                dataFilterList.value = filterByCompany(dataList);
                break;
            case 'jbrp':
            default:
                dataFilterList.value = groupAndSortB(dataList, 'jbrpNm', 'jbrpSeq');
                break;
        }
    };

    //-----------------------------------------------

    const activeFilter = ref('jbrp');

    const filterTypeBtn = filterType => {
        activeFilter.value = filterType;
        filterBtn(filterType);
    };

    const filterBtn = filterType => {
        // Y 값을 우선 정렬
        const sortedList = [...hrList.value]

        switch (filterType) {
            case 'orgn':
                dataFilterList.value = groupAndSort(sortedList, 'orgnId', ' 협력사');
                break;
            case 'jbrp':
                dataFilterList.value = groupAndSortB(sortedList, 'jbrpNm', 'jbrpSeq');
                break;
            case 'com':
                dataFilterList.value = filterByCompany(sortedList);
                break;
            default:
                console.log('Unknown filter type:', filterType);
        }
    };

    // 공통 그룹핑 및 정렬 함수
    const groupAndSort = (list, groupKey, defaultKey = '미설정') => {
        const grouped = list.reduce((acc, item) => {
            const key = item[groupKey] || defaultKey;
            acc[key] = acc[key] || [];
            acc[key].push(item);
            return acc;
        }, {});

        return Object.keys(grouped)
            .map(key => ({
                title: grouped[key][0]?.orgnNm || defaultKey,
                key: key,
                data: grouped[key],
                seq: grouped[key][0]?.seq
            }))
            .sort((a, b) => {
                // defaultKey가 있는 경우 맨 뒤로
                if (a.key === defaultKey) return 1;
                if (b.key === defaultKey) return -1;
                // seq로 조직 그룹들을 정렬
                return a.seq - b.seq;
            });
    };

    const groupAndSortB = (list, groupKey, seq='ordSeq') => {
        if (!list || list.length === 0) return [];
        const defaultKey = '미설정'
        // 그룹핑 수행
        const grouped = list.reduce((acc, item) => {
            const key = item[groupKey] || defaultKey;
            if (!acc[key]) acc[key] = [];
            acc[key].push(item);
            return acc;
        }, {});
        // 그룹핑된 데이터를 map, 각 그룹 내에서 데이터 정렬
        return Object.keys(grouped)
            .map(key => {
                const groupData = grouped[key];
                // 그룹 내 데이터 정렬 (useYn, seq)
                const sortedData = groupData.sort((a, b) => {
                    if (a.useYn !== b.useYn) {
                        return b.useYn.localeCompare(a.useYn); // 'Y'가 먼저
                    }
                    return a[seq]- b[seq]; // seq 오름차순
                });

                return {
                    title: key,
                    data: sortedData,
                    // 그룹 정렬을 위한 seq 값 추출
                    [seq]: groupData[0][seq]
                };
            })
            // 그룹 자체를 seq 기준으로 정렬
            .sort((a, b) => {
                if (a.title === defaultKey) return 1;
                if (b.title === defaultKey) return -1;
                // seq가 없는 경우를 대비하여 기본값 99로 설정
                return a[seq] - b[seq];
            });
    };

    // 본사와 협력사 구분 필터링
    const filterByCompany = list => {
        const nonNullValues = list.filter(item => item.partCompNm !== null);
        const nullValues = list.filter(item => item.partCompNm == null);
        return [
            { title: '본사', data: nullValues },
            { title: '협력사', data: nonNullValues }
        ];
    };

    //-----------------------------------------------

    //추가버튼

    const btnAdd = () => {
        layoutStore.useBtnList = ['btnBack', 'btnSave'];
        initInputForm();
        //조직변경이력 초기화
        orgHistory.value = [];
        // 추가(I) 플래그 cmd
        // default 값 세팅
        inputForm.value.useYn = 'Y';
        inputForm.value.createdAt = currentDate;
        inputForm.value.orgSeq = 99;
        //파일초기화
        if (fileInfo.value) {
            fileInfo.value.fnRemove();
        }

        // 초기 화면 팝업 초기화
        mgrCompList.value = [];
        inputForm.value.affCompList = [];
        orgnPopupSelectedList.value = [];
        partnerPopupSelectedList.value = [];
        jbrpPopupSelectedList.value = [];

        isHeadOffice.value = true;

        //페이지 이동
        router.push({
            path: '/HRManageDetail'
        });
    };

    //-----------------------------------------------

    //저장
    const btnSave = async () => {
        if (!inputForm.value.ordSeq) {
            // 정렬 null 값일때 기본값 99 넣어줌
            inputForm.value.ordSeq = 99;
        }
        inputForm.value.userId = inputForm.value.userId.trim();

        if (inputForm.value.cmd === 'I') {
            //추가
            confirmMsg('info', '저장 하시겠습니까?', '', { fun: createHr, param: inputForm.value });
        } else {
            //수정
            confirmMsg('info', '저장 하시겠습니까?', '', { fun: updateHrList, param: inputForm.value });
        }
    };

    //-----------------------------------------------
    const checkedList = ref([]);

    //일괄 삭제
    const btnDelete = async item => {
        if (item == 'D') {
            //개별삭제
            confirmMsg('warning', '삭제 하시겠습니까?', '', { fun: deleteHrList, param: inputForm.value.hrId });
        } else {
            let param = checkedList.value; // rowKey로 행 데이터를 가져옴

            if (!param.length) {
                alertMsg('warning', t('msgNoItem'));
                return;
            }
            if (param.some(el => el.useYn === 'N')) {
                toastPopup('이미 삭제 처리된 항목입니다.', 'error');
                return;
            }
            confirmMsg('warning', '삭제 하시겠습니까?', ``, { fun: deleteHrLists, param: param });
        }
    };

    const btnResetPassword = () => {
        openLoading()
        const param = {
            hrId: inputForm.value.hrId
        }
        resetPassword(param, true).then(res=>{
            alertMsg('warning', '비밀번호가 아이디로 초기화되었습니다. \n 보안에 유의하세요.');
            endLoading()
        }).catch(()=>{
            endLoading()
        })

    }

    //-----------------------------------------------
    // 팝업 닫기
    //#region [소속 사업장 팝업 종료] Sim 25. 3. 13.

    const closeMainComp = e => {
        if (e && e.length && e.length > 0) {
            // single 일때
            const refinedItems = [
                {
                    id: e[0].compId,
                    name: e[0].compNm
                }
            ];       

            // 기존에 선택한 소속 사업장이 아닌 경우 데이터 초기화
            if (affCompList.value !== null && affCompList.value && affCompList.value.length > 0) {
                if (affCompList.value[0].id !== e[0].compId) {
                    mgrCompList.value = [];
                    jbrpPopupSelectedList.value = [];
                    orgnPopupSelectedList.value = [];
                    inputForm.orgnId = '';
                    inputForm.orgnNm = '';
                }
            }

            Object.assign(affCompList.value, refinedItems);
            inputForm.clntId = affCompList.value[0].id;
            inputForm.value.maxHrCount = e[0].maxHrCount;
        }
        mainCompPopup.value.onClose();
    };
    //#endregion

    const applyComp = e => {
        if (e && e.length && e.length > 1) {
            // multi 일때
            mgrCompList.value = e.map(el => ({
                id: el.compId,
                name: el.compNm
            }));
        } else {
            // single 일때
            const refinedItems = [
                {
                    id: e[0].compId,
                    name: e[0].compNm
                }
            ];
            Object.assign(mgrCompList.value, refinedItems);
        }

        compPopup.value.onClose();
    };
    const closeComp = () => {
        compPopup.value.onClose();
    };

    // 협력사 팝업
    const partnerPopupSelectedList = ref([]);
    const closePartner = e => {
        if (e && e.length && e.length > 0) {
            const refinedItems = [
                {
                    id: e[0].partCompId,
                    name: e[0].partCompNm
                }
            ];
            Object.assign(partnerPopupSelectedList.value, refinedItems);
        }
        partnerPopup.value.onClose();
    };

    // 조직 팝업
    const orgnPopupSelectedList = ref([]);

    const closeOrgn = async e => {
        if (e && e.length && e.length > 0) {
            const refinedItems = [
                {
                    id: e[0].orgnId,
                    name: e[0].orgnNm
                }
            ];
            Object.assign(orgnPopupSelectedList.value, refinedItems);
        }
        orgnPopup.value.onClose();
    };

    // 직위 팝업
    const jbrpPopupSelectedList = ref([]);

    const closeSystemPopup = e => {
        if (e && e.length && e.length > 0) {
            const refinedItems = [
                {
                    id: e[0].jbrpId,
                    name: e[0].jbrpNm
                }
            ];
            Object.assign(jbrpPopupSelectedList.value, refinedItems);
        }
        systemPopup.value.onClose();
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

    return {
        initInputForm,
        inputForm,
        hrId,
        setRefs,
        filterBtn,
        affCompList,
        mgrCompList,
        onPostcodeComplete,
        dialogPostNo,
        emitOpenPostcodeDialog,
        initData,
        compPopup,
        partnerPopup,
        mainCompPopup,
        systemPopup,
        closeSystemPopup,
        filterTypeBtn,
        activeFilter,
        closeComp,
        applyComp,
        closePartner,
        orgnPopup,
        closeOrgn,
        closeMainComp,
        orgnPopupSelectedList,
        partnerPopupSelectedList,
        jbrpPopupSelectedList,
        searchParam,
        toggleUseYn,
        checkedList,
        searchTerm,
        applyFilter,
        dataFilterList,
        //라우터
        btnAdd,
        goBack,
        goHr,
        //버튼
        btnSave,
        btnDelete,
        btnResetPassword,
        //api
        getHrList,
        getHrDetailList,
        deleteHrList,
        deleteHrLists,
        createHr,
        updateHrList,
        currentDate,
        getOrgHistory,
        orgHistory,
        checkNumberInput,
        closePostcodeDialog,
        //excel
        excelData,
        createHrExcel,
        getHrTemplate,
        updateCompType,
        inputValue,
        isHeadOffice
    };
});
