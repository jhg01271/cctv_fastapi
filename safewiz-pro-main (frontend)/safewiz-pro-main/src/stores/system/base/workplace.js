import { defineStore } from 'pinia';

import router from '@/router';
import BaseView from '@/components/base/BaseView';
const { openLoading, endLoading, ref, alertMsg, t, confirmMsg, getCompId, getCurrentDate } = BaseView();
import { getWp, getWpDetail, insertWp, updateWp, deleteWp, deleteWps, downloadWpExcelTemplate, insertWpExcel } from '@/stores/system/base/api/workplaceApi';

import { getHr } from '@/stores/system/base/api/hrApi';
import _ from 'lodash'

export const useWorkplaceStore = defineStore('workplace', () => {
    //작업장 리스트
    const workplaceList = ref([]);
    // 현재 선택된 작업장 인덱스
    const selectedWorkplaceIdx = ref(-1);
    //상위 조직
    const compId = getCompId();

    const fileInfo = file => {
        fileInfo.value = file.value;
    };

    const inputForm = ref({});
    const workplaceId = ref('');
    const initInputForm = () => {
        inputForm.value = {
            // workplaceNm: '', // 작업장 명
            compId: compId,
            workplaceArea: '', // 구역명
            workplaceNm: '', // 작업장 명
            headHrList: [],
            secondHrList: [],
        };
    };

    //-----------------------------------------------//작업장 조회
    const getWorkplaceList = notify => {
        searchTerm.value = '';
        return getWp(searchParam.value, notify).then(res => {
            if (res && res.success) {
                // workplaceList.value = res.list
                workplaceList.value = res.list.map(item => ({
                    ...item
                    // detail: {
                    //   [t('orgn_createdAt')]: formatDate(item.createdAt), // 다국어 지원 속성 제목
                    //   [t('orgn_resultCnt')]: item.resultCnt, // 다국어 지원 속성 제목
                    // }
                }));
                applyFilter();
            }
            return res; // res 반환
        });
    };
    const originData = ref({});
    const getWorkplaceDetail = (workplaceId, notify) => {
        return getWpDetail(workplaceId, notify).then(res => {
            if (res && res.success) {
                originData.value = res.list;
                inputForm.value = _.cloneDeep(res.list)
                // Object.assign(inputForm.value, res.list);
                selectedWorkplaceIdx.value = filteredWpList.value.findIndex(el => el.workplaceId === res.list.workplaceId);
                // 현재 상세 작업장 데이터가 필터링된 리스트의 몇번째 인덱스인지 값을 저장
                // 해당하는 인덱스는 이전/다음 버튼 클릭 이벤트에 활용될 구분 값이됨
                originData.value = res.list;
            }
            return res; // res 반환
        });
    };

    //저장
    //작업장 추가
    const insertWorkplace = inputForm => {
        const editFiles = fileInfo.value.editFiles;

        // inputForm에 JSON 데이터 추가
        const formData = new FormData();
        formData.append('info', new Blob([JSON.stringify(inputForm)], { type: 'application/json' }));

        // 수정할 파일 추가
        editFiles.insert.forEach(file => {
            if (file) {
                formData.append('files', file); // 파일이 있을 경우 파일 추가
            } else {
                formData.append('files', new Blob([], { type: 'application/octet-stream' })); // 빈 파일 추가
            }
        });

        // insertWp 호출 후 결과 처리
        return insertWp(formData, true).then(res => {
            if (res && res.success) {
                // 성공 시 결과 반환
                fileInfo.value.resetEditFiles(res.result.fileId);
                return { result: res.result };
            }
            return res; // 실패 시도 그대로 res 반환
        });
    };

    //작업장 수정
    const updateWorkplace = inputForm => {
        const editFiles = fileInfo.value.editFiles;

        // 파일이 수정될 때 기존 파일을 삭제하도록 설정
        inputForm.deleteFiles = editFiles.delete;

        // 기존의 inputForm을 FormData로 변환 (이미 FormData라면 생략 가능)
        const formData = new FormData();
        formData.append('info', new Blob([JSON.stringify(inputForm)], { type: 'application/json' }));

        // 수정할 파일들 추가
        editFiles.insert.forEach(file => {
            if (file) {
                formData.append('files', file); // 파일이 있을 경우 파일 추가
            } else {
                formData.append('files', new Blob([], { type: 'application/octet-stream' })); // 빈 파일 추가
            }
        });

        // 서버에 업데이트 요청을 보냄
        return updateWp(inputForm.workplaceId, formData, true).then(res => {
            if (res && res.success) {
                // 파일 파라미터 초기화
                if (res.result.fileId != null) {
                    fileInfo.value.resetEditFiles(res.result.fileId);
                }
                return { result: res.result }; // 성공 시 결과 반환
            }
            return res; // 실패 시에도 res 반환
        });
    };
    //작업장 삭제
    const deleteWorkplace = workplaceId => {
        return deleteWp(workplaceId, true).then(res => {
            if (res && res.success) {
                router.push({ name: 'WorkplaceManage' });
            }
            return res; // res 반환
        });
    };
    //작업장 일괄 삭제
    const deleteWorkplaces = inputForm => {
        return deleteWps(inputForm, true).then(res => {
            if (res && res.success) {
                // 검색어 초기화
                searchTerm.value = '';
                //리스트 초기화
                // filteredWpList.value = [];
                //체크리스트 초기화
                checkedList.value = [];
                getWorkplaceList();
            }
            return res; // res 반환
        });
    };

    //-----------------------------------------------

    const currentDate = ref(getCurrentDate());

    //-----------------------------------------------

    // const checkNumberInput = (event) => {
    //   // 입력된 값이 숫자가 아니면 빈 문자열로 변환
    //   event.target.value = event.target.value.replace(/[^0-9]/g, '');
    //   // v-model로 바인딩된 값도 업데이트
    //   inputForm.value.ordSeq = event.target.value;
    // };

    //-----------------------------------------------
    //목록으로 이동
    const goBack = () => {
        //검색어 초기화
        searchTerm.value = '';
        router.push({ name: 'workplaceManage' });
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

    const searchTerm = ref('');
    const filteredWpList = ref([]);

    const initData = () => {
        // 여기서 API 호출을 통해 데이터를 가져올 수 있습니다.
        // 예제에서는 초기 데이터로 설정합니다.
        applyFilter(); // 필터를 처음에 적용하여 초기 데이터로 채움
    };

    // 필터 적용 함수
    const applyFilter = () => {
        const filteredData = workplaceList.value.filter(item => item.workplaceNm.toLowerCase().includes(searchTerm.value.toLowerCase()));
        // 필터링된 데이터로 업데이트
        if (filteredWpList.value) {
            filteredWpList.value = filteredData;
        }
    };
    //-----------------------------------------------

    //-----------------------------------------------

    //저장
    const btnSave = async () => {
        if (inputForm.value.cmd === 'I') {
            //추가
            confirmMsg('info', t('msgSave'), '', { fun: insertWorkplace, param: inputForm.value });
        } else {
            //수정
            confirmMsg('info', t('msgSave'), '', { fun: updateWorkplace, param: inputForm.value });
        }
    };

    //-----------------------------------------------
    const checkedList = ref([]);

    //일괄 삭제
    const btnDelete = async item => {
        if (item == 'D') {
            //개별삭제
            confirmMsg('warning', t('msgDelete'), '', { fun: deleteWorkplace, param: inputForm.value.workplaceId });
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
            confirmMsg('warning', t('msgDelete'), ``, { fun: deleteWorkplaces, param: param });
        }
    };
    //추가
    const btnAdd = id => {
        initInputForm();
        inputForm.value.workplaceId = id;
        // 추가(I) 플래그 cmd
        inputForm.value.cmd = 'I';
        // default 값 세팅
        inputForm.value.useYn = 'Y';
        inputForm.value.createdAt = currentDate;

        router.push({
            name: 'WorkplaceManageDetail'
        });
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

    //-----------------------------------------------
    // 양식 다운로드 및 업로드
    const getTemplate = id => {
        downloadWpExcelTemplate(id).then(res => {
            console.log('## 템플릿 다운로드 ', res);
        });
    };
    const excelData = ref(null);
    const hrList = ref(null);
    const createExcel = async () => {
        //#region 팝업관련 데이터 리스트 전부 조회
        openLoading();
        let responses = await Promise.all([getHr({})]);
        if (responses) {
            hrList.value = responses[0].list;
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
            if (item[1] === null || item[1] === '') return item; // 작업장 명
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
                const headInputList = item[3]
                    ? new Set(
                          item[3]
                              .split(',')
                              .map(v => v.trim())
                              .filter(v => v !== '')
                      )
                    : new Set();
                if (headInputList.size > 0) {
                    const validHrIds = new Set(hrList.value.map(hr => hr.hrId));
                    for (const inputId of headInputList) {
                        if (!validHrIds.has(inputId)) {
                            alertMsg('error', t('작업장 : ') + item[2].trim() + '\n' + t('인원을 찾을 수 없습니다.') + '\n' + t('데이터셋 시트에 맞춰 작성하세요.'));
                            error = true;
                            endLoading();
                            break; // 하나라도 없으면 탈출
                        }
                    }
                }

                const secondInputList = item[4]
                    ? new Set(
                          item[4]
                              .split(',')
                              .map(v => v.trim())
                              .filter(v => v !== '')
                      )
                    : new Set();
                if (secondInputList.size > 0) {
                    const validHrIds = new Set(hrList.value.map(hr => hr.hrId));
                    for (const inputId of secondInputList) {
                        if (!validHrIds.has(inputId)) {
                            alertMsg('error', t('작업장 : ') + item[2].trim() + '\n' + t('인원을 찾을 수 없습니다.') + '\n' + t('데이터셋 시트에 맞춰 작성하세요.'));
                            error = true;
                            endLoading();
                            break; // 하나라도 없으면 탈출
                        }
                    }
                }
                if (item[2] == null || item[2] == '') {
                    item[2] = 99;
                }
                return {
                    cmd: 'I',
                    workplaceNm: item[1],
                    ordSeq: item[2],
                    headHrList: item[3].length > 0 ? item[3].split(',').map(item => ({hrId: item.trim()})) : [],
                    secondHrList: item[4].length > 0 ? item[4].split(',').map(item => ({hrId: item.trim()})) : [],
                    workplaceArea: item[5],
                    workplaceAlias: item[6],
                    remark: item[7],
                    useYn: 'Y'
                };
            } catch (err) {
                alertMsg('error', t('작업장 : ') + String(item[1]).trim() + '\n' + t('데이터를 확인하세요.'));
                error = true;
                endLoading();
                return null;
            }
        });
        console.log('#data', data);
        if (!error) {
            return new Promise(resolve => {
                insertWpExcel(data, true)
                    .then(res => {
                        if (res.success) {
                            resolve({ result: res.result });
                            // 검색어 초기화
                            searchTerm.value = '';
                            //리스트 초기화
                            getWorkplaceList(false);
                        }
                    })
                    .catch(() => {
                        endLoading();
                    })
                    .finally(() => {
                        getWorkplaceList(true);
                        endLoading();
                    });
            });
        }
    };
    //-----------------------------------------------

    return {
        initInputForm,
        inputForm,
        initData,
        searchParam,
        toggleUseYn,
        checkedList,
        searchTerm,
        applyFilter,
        filteredWpList,
        workplaceId,
        //api
        workplaceList,
        originData,
        selectedWorkplaceIdx,
        // 파일
        fileInfo,
        //라우터
        btnAdd,
        goBack,
        goHr,
        //버튼
        btnSave,
        btnDelete,
        getWorkplaceList,
        getWorkplaceDetail,
        deleteWorkplace,
        deleteWorkplaces,
        insertWorkplace,
        updateWorkplace,
        getTemplate,
        createExcel,
        excelData
    };
});
