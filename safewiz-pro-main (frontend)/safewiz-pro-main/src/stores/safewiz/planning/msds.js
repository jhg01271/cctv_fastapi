import { defineStore } from 'pinia';

import router from '@/router';
import BaseView from '@/components/base/BaseView';

const { ref, t, alertMsg, toastPopup, confirmMsg, getCompId, getCurrentDate, baseDownload, formatDate } = BaseView();

import { getMsds, getMsdsDetail, insertMsds, updateMsds, deleteMsds, deleteMsdss, getMsdsReport } from '@/stores/safewiz/planning/api/msdsApi';

import _ from 'lodash';

export const useMsdsStore = defineStore('msds', () => {

    const compId = getCompId();
  const writeYear = ref(getCurrentDate().substring(0, 4));

    const checkedList = ref([]);
    const sortList = ref([])
    const currentFilter = ref('workplace');

    //MSDS 리스트
    const msdsList = ref([]);

    const fileList = ref({
        editFiles: {
            insert: [],  // 새로 추가된 파일들
            delete: []   // 삭제할 파일들
        },
    });

    // 현재 선택된 MSDS 인덱스
    const selectedMsdsId = ref(-1);

    const searchParam = ref({
        compId: compId,
        writeYear: writeYear.value,
        docNo: '',
        docType: 'ER'
      })

    // 상단 필터링 목록 (작업장, 공정)
    const msdsDivList = ref([
        { id: 'workplace', name: '작업장' },
        { id: 'process', name: '공정' }
    ]);

    // const usageTypeList = ref([]); // 사용처 시스템 코드 저장
    const initData = async () => {
        filteredByPrcsMsdsListBySearch.value = {}
        filteredByPrcsMsdsList.value = {}
        // let responses = await Promise.all([
        //   getSystemCode({
        //     majorCd: 'C0010',
        //     compId: compId
        //   })
        // ]);
        // if (responses) {
        //   usageTypeList.value = responses[0].list;
        // }
    };
    const inputForm = ref({});
    const initInputForm = () => {
        inputForm.value = {
            compId: getCompId(),
            msdsId: '',
            ordSeq: '99',
            processList: [],
            processIdList: [],
            workplaceList: [],
            workplaceIdList: [],
            hrList: [],
            unitVo: [{id: '', nm:''}],
            casNo: '',
            createdAt: currentDate,
            useYn: 'Y'
        };
    };
    const workItems = ref([]);
    const currentDate = ref(getCurrentDate());

    const filteredByPrcsMsdsList = ref({});

    function addPrcsList() {
        filteredByPrcsMsdsList.value = {};
        // 결과를 저장할 객체 (조직별로 데이터 1차 분할)
        const result = {};
        // 데이터 처리 함수
        msdsList.value.forEach(msds => {
            // 각 부모의 list를 순회하며 그룹화 진행
            // 공정을 등록안했을 경우 '미설정' 그룹 생성
            if (msds.workList.length === 0) {
                if (result['미설정']) {
                    result['미설정'].push(msds);
                } else {
                    result['미설정'] = [msds];
                }
            }

            msds.workList.forEach(item => {
                /// 그룹명을 공정명/작업내용 형식으로 변경
                const nm = item.processNm + ' / ' + item.workContent;
                // 중복되는 data가 있을 경우 해당 그룹으로 합치기
                if (result[nm]) {
                    result[nm].push(msds);
                } else {
                    result[nm] = [msds];
                }
            });
        });

        // 중복 그룹들을 하나의 키로 합치는 부분
        // 모든 조직과 그에 해당하는 msdsId를 순회
        for (const [, msdsList] of Object.entries(result)) {
            msdsList.forEach(msds => {
                const msdsId = msds.msdsId;

                // 같은 msdsId를 가진 조직들을 찾아서 그룹화
                const matchingOrgs = Object.keys(result).filter(org => result[org].some(msds => msds.msdsId === msdsId));

                // 그룹으로 묶인 조직명을 '|'로 연결하여 key 생성
                const groupKey = matchingOrgs.sort().join(' | ');

                // 그룹 키가 이미 존재하면, msdsId를 추가
                if (!filteredByPrcsMsdsList.value[groupKey]) {
                    filteredByPrcsMsdsList.value[groupKey] = [];
                }

                // 동일한 msdsId를 가진 장비가 중복되지 않도록 추가
                if (!filteredByPrcsMsdsList.value[groupKey].some(msds => msds.msdsId === msdsId)) {
                    filteredByPrcsMsdsList.value[groupKey].push(msds);
                }
            });
        }

        // "미설정" 항목 제거: 데이터가 없을 경우 삭제
        const { 미설정, ...rest } = _.cloneDeep(filteredByPrcsMsdsList.value);
        if (미설정 && 미설정.length > 0) {
            filteredByPrcsMsdsListBySearch.value = { ...rest, 미설정 };
        } else {
            filteredByPrcsMsdsListBySearch.value = rest;
        }
    }

    let filteredByWpMsdsList = ref({});

    function addWpList() {
        filteredByWpMsdsList.value = {};
        // 결과를 저장할 객체 (작업장별로 데이터 1차 분할)
        const result = {};

        // 데이터 처리 함수
        msdsList.value.forEach(msds => {
            // 각 부모의 list를 순회하며 그룹화 진행

            // 작업장을 등록안했을 경우 '미설정' 그룹 생성
            if (msds.workplaceList.length === 0) {
                if (result['미설정']) {
                    result['미설정'].push(msds);
                } else {
                    result['미설정'] = [msds];
                }
            }

            msds.workplaceList.forEach(item => {
                // 중복되는 data가 있을 경우 해당 그룹으로 합치기
                if (result[item.nm]) {
                    result[item.nm].push(msds);
                } else {
                    result[item.nm] = [msds];
                }
            });
        });
        // 중복 그룹들을 하나의 키로 합치는 부분
        // 모든 작업장과 그에 해당하는 workplaceId를 순회
        for (const [, msdsList] of Object.entries(result)) {
            msdsList.forEach(msds => {
                const msdsId = msds.msdsId;

                // 같은 msdsId를 가진 조직들을 찾아서 그룹화
                const matchingOrgs = Object.keys(result).filter(org => result[org].some(msds => msds.msdsId === msdsId));

                // 그룹으로 묶인 작업장명을 '|'로 연결하여 key 생성
                const groupKey = matchingOrgs.sort().join(' | ');

                // 그룹 키가 이미 존재하면, msdsId 추가
                if (!filteredByWpMsdsList.value[groupKey]) {
                    filteredByWpMsdsList.value[groupKey] = [];
                }

                // 동일한 msdsId를 가진 장비가 중복되지 않도록 추가
                if (!filteredByWpMsdsList.value[groupKey].some(msds => msds.msdsId === msdsId)) {
                    filteredByWpMsdsList.value[groupKey].push(msds);
                }
            });
        }
        // "미설정" 항목 제거: 데이터가 없을 경우 삭제
        const { 미설정, ...rest } = _.cloneDeep(filteredByWpMsdsList.value);
        if (미설정 && 미설정.length > 0) {
            filteredByWpMsdsListBySearch.value = { ...rest, 미설정 };
        } else {
            filteredByWpMsdsListBySearch.value = rest;
        }
        const sorted = Object.fromEntries(Object.entries(filteredByWpMsdsListBySearch.value).sort(([a], [b]) =>{
            if(a === '미설정') return 1
            if(b === '미설정') return -1
            return a.localeCompare(b)
        }));
        sortList.value = Object.keys(sorted)
    }

    const btnSearch = async notify => {
        searchTerm.value = '';
        return getMsds(searchParam.value, notify).then(res => {
            if (res && res.success) {
                msdsList.value = res.list;
                addWpList();
                addPrcsList();
            }
            return res; // res 반환
        });
    };

    const btnAdd = (msdsId, list = []) => {
        initInputForm();
        inputForm.value.msdsId = msdsId;
        // 추가(I) 플래그 cmd
        inputForm.value.cmd = 'I';
        // default 값 세팅
        if (list.length > 0) {
            if (currentFilter.value === 'process') {
                inputForm.value.workIdList = list.map(el => el.id);
                inputForm.value.workList = list;

                // workList 데이터에 nm 과 id 가 없기 때문에 수동으로 적용
                inputForm.value.workList.forEach(el => {
                    el.title = el.processNm;
                    el.id = el.prcsWorkId;
                    el.nm = el.workContent;
                });
            } else if (currentFilter.value === 'workplace') {
                inputForm.value.workplaceIdList = list.map(el => el.id);
                inputForm.value.workplaceList = list;
            }
        } else {
            inputForm.value.processIdList = [];
            inputForm.value.workplaceIdList = [];
        }
        router.push({
            name: 'MsdsManageDetail'
        });
    };

    // 상세 보기
    const mergedMsdsByType = ref([]); // 조직/작업장/본사 타입에 따른 합병리스트
    const btnDetail = msdsId => {
        return getMsdsDetail(msdsId, true).then(res => {
            if (res && res.success) {
                if(res.list.unitVo[0] == null)  { 
                    res.list.unitVo = [
                            {
                                id: '',
                                nm: ''
                            }
                        ];
                    
                }
                inputForm.value = res.list;
                console.log(inputForm.value)
                inputForm.value.workplaceIdList = inputForm.value.workplaceList.map(el => el.id);
                inputForm.value.mfDate = formatDate(inputForm.value.mfDate);
                inputForm.value.setupAt = formatDate(inputForm.value.setupAt);

                // 공정/작업내용 목록 생성
                res.list.workList.forEach(el => {
                    el.id = el.prcsWorkId;
                    el.title = el.processNm;
                    el.nm = el.workContent;
                });

                // 공정/작업내용 삭제 index 생성을 위한 Id 목록 생성
                inputForm.value.workIdList = inputForm.value.workList.map(el => el.id);

                switch (currentFilter.value) {
                    case 'process': {
                        mergedMsdsByType.value = Object.values(filteredByPrcsMsdsListBySearch.value).flat();
                        selectedMsdsId.value = mergedMsdsByType.value.findIndex(el => el.msdsId === inputForm.value.msdsId);
                        break;
                    }
                    case 'workplace': {
                        mergedMsdsByType.value = Object.values(filteredByWpMsdsListBySearch.value).flat();
                        selectedMsdsId.value = mergedMsdsByType.value.findIndex(el => el.msdsId === inputForm.value.msdsId);
                        break;
                    }
                }
                router.push({
                    name: 'MsdsManageDetail'
                });
            }
            return res; // res 반환
        });
    };

    // 추가
    const insertMsdsData = param => {
        let editImageFiles = imageFileInfo.value.editFiles;

        let formData = new FormData();
        formData.append('info', new Blob([JSON.stringify(param)], { type: 'application/json' }));
        editImageFiles.insert.forEach(element => {
            formData.append('imageFiles', element ? element : new Blob([], { type: 'application/octet-stream' }));
        });
        fileList.value.editFiles.insert.forEach(file => {
            formData.append('files', file || new Blob([], { type: 'application/octet-stream' }));
        });
        return new Promise(resolve => {
            insertMsds(formData, true).then(res => {
                if (res && res.success) {
                    inputForm.value.msdsId = res.result.msdsId;
                    resolve({ result: res.result });
                } else {
                    resolve(false); 
                }
            }).catch(() => resolve(false));
        });
        
    };

    // 수정
    const updateMsdsData = param => {
        let editFiles = imageFileInfo.value.editFiles;
        let formData = new FormData();
        inputForm.value.deleteFiles = editFiles.delete;
        inputForm.value.deleteFiles2 = fileList.value.editFiles.delete;
        formData.append('info', new Blob([JSON.stringify(param)], { type: 'application/json' }));
        editFiles.insert.forEach(element => {
            formData.append('imageFiles', element ? element : new Blob([], { type: 'application/octet-stream' }));
        });
        fileList.value.editFiles.insert.forEach(file => {
            formData.append('files', file || new Blob([], { type: 'application/octet-stream' }));
        });

        return new Promise(resolve => {
            updateMsds(inputForm.value.msdsId, formData, true).then(res => {
                if (res && res.success) {
                    resolve({ result: res.result });
                } else {
                    resolve(false);
                }
            }).catch(() => resolve(false));
        });
        
    };

    const btnSave = () => {
        return new Promise((resolve) => {
            if (inputForm.value.workList === undefined) inputForm.value.workList = [];
    
            const saveAction = async (param) => {
                let result;
                if (inputForm.value.cmd === 'I') {
                    result = await insertMsdsData(param);
                } else {
                    result = await updateMsdsData(param);
                }
                resolve(result); // 외부로 결과 반환
            };
    
            confirmMsg('info', '저장 하시겠습니까?', '', {
                fun: saveAction,
                param: inputForm.value,
            });
    
            filteredByPrcsMsdsListBySearch.value = {};
            filteredByPrcsMsdsList.value = {};
        });
    };
    
      const btnDownload = async (list) => {
        if (list.length > 0) {
          let searchVO = _.cloneDeep(searchParam.value)
          searchVO.docType = 'ER'
          searchVO.checkedObjList = list
          baseDownload(getMsdsReport, searchVO)
          // getEvaluationReportReport(searchVO).then(res => {
          //   downloadReport(res)
          // })
        } else {
          alertMsg('warning', t('msgNoItem'));
          return;
        }
      }

    //MSDS 삭제
    const deleteMsdsData = msdsId => {
        return deleteMsds(msdsId, true).then(res => {
            if (res && res.success) {
                router.push({ name: 'MsdsManage' });
            }
            return res; // res 반환
        });
    };

    //MSDS 일괄 삭제
    const deleteMsdsDatas = inputForm => {
        return deleteMsdss(inputForm, true).then(res => {
            if (res && res.success) {
                //체크리스트 초기화
                checkedList.value = [];
                btnSearch();
            }
            return res; // res 반환
        });
    };

    const imageFileInfo = file => {
        imageFileInfo.value = file.value;
    };
    
    const toggleUseYn = event => {
        // 체크박스의 체크 상태에 따라 'Y' 또는 'N'을 설정합니다.
        inputForm.value.useYn = event.target.checked ? 'Y' : 'N';
    };

    //일괄 삭제
    const btnDelete = async item => {
        if (item === 'D') {
            //개별삭제
            confirmMsg('warning', '삭제 하시겠습니까?', '', {
                fun: deleteMsdsData,
                param: inputForm.value.msdsId
            });
        } else {
            let param = checkedList.value; // rowKey로 행 데이터를 가져옴

            if (!param.length) {
                alertMsg('warning',  t('msgNoItem'));
                return;
            }
            if (param.some(el => el.useYn === 'N')) {
                alertMsg('warning', '이미 삭제 처리된 항목입니다.');
                return;
            }
            confirmMsg('warning', '삭제 하시겠습니까?', ``, { fun: deleteMsdsDatas, param: param });
        }
    };
    const searchTerm = ref('');
    const filteredByPrcsMsdsListBySearch = ref({});
    const filteredByWpMsdsListBySearch = ref({});
    // 필터 적용 함수
    const applyFilter = () => {
        // 공정
        for (const key in filteredByPrcsMsdsList.value) {
            filteredByPrcsMsdsListBySearch.value[key] = filteredByPrcsMsdsList.value[key].filter(item => item.msdsNm.toLowerCase().trim().includes(searchTerm.value.toLowerCase().trim()));
        }
        // 작업장
        for (const key in filteredByWpMsdsList.value) {
            filteredByWpMsdsListBySearch.value[key] = filteredByWpMsdsList.value[key].filter(item => item.msdsNm.toLowerCase().trim().includes(searchTerm.value.toLowerCase().trim()));
        }
    };

    return {
        initInputForm,
        inputForm,
        checkedList,
        currentFilter,
        msdsDivList,
        searchTerm,
        mergedMsdsByType,
        selectedMsdsId,
        fileList,
        imageFileInfo,
        workItems,
        sortList,
        // function
        initData,
        toggleUseYn,
        applyFilter,
        msdsList,
        filteredByPrcsMsdsList,
        filteredByPrcsMsdsListBySearch,
        filteredByWpMsdsList,
        filteredByWpMsdsListBySearch,
        //버튼
        btnSearch,
        btnAdd,
        btnDelete,
        btnSave,
        btnDetail,
        btnDownload
    };
});
