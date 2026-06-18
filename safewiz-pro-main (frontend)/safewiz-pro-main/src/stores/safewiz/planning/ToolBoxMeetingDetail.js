import { defineStore } from 'pinia'
import router from '@/router'
import { searchDataDetailApi, saveDataApi, deleteDataApi, getReportApi } from '@/stores/safewiz/planning/api/ToolBoxMeetingApi.js'
import BaseView from '@/components/base/BaseView'
import _ from 'lodash'


import { createSignatureStore } from '@/stores/signature';
const signatureStore = createSignatureStore(); // 컴포넌트마다 고유한 Store 생성

const { computed, openLoading, endLoading, t, ref, getCurrentDate, getCompId, confirmMsg, watch, alertMsg, validationStore, baseDownload, formatDate, formatDateForDB } = BaseView()

export const useToolBoxMeetingDetailStore = defineStore('toolBoxMeetingDetail', () => {
    const inputForm = ref({})
    const inputFormOrg = ref({})
    const potentialRisk = ref([])
    const potentialRiskOrg = ref([])
    const keyRisk = ref([])
    const safeCheck = ref([])
    const leaderComp = ref(null)
    const attendComp = ref(null)
    const peopleLeaderComp = ref(null)
    const peopleAttendComp = ref(null)

    /**
     * 저장 중복 방지용 락 플래그
     *
     * - `openLoading()`은 UI 클릭을 막아줄 수는 있지만, 비동기 콜백/이벤트가 겹치면
     *   저장 함수가 거의 동시에 2번 진입하는 레이스를 완전히 막지는 못할 수 있습니다.
     * - 그래서 "저장 요청이 이미 진행 중"이면 이후 저장 요청은 즉시 무시하도록 논리 가드를 둡니다.
     */
    const isSaving = ref(false)

    const beforeInputForm = ref({});
    const beforePotentialRisk = ref([]);
    const beforeKeyRisk = ref([]);
    const beforeSafeCheck = ref([]);

    const selectRiskAssessment = ref([])
    const selectProcess = ref([])
    const selectRiskAllowanceStandards = ref('')

    const initInputForm = () => {
        const currentDate = getCurrentDate();
        const writeYear = inputForm.value.writeYear || currentDate.substring(0, 4);
        const month = inputForm.value.month
        // 월 아코디언 내부 추가 버튼으로 들어왔을 경우 tbmDate 세팅
        const tbmDate = month
                    ? `${writeYear}.${month}${currentDate.substring(7, 10)}`
                    : `${writeYear}${currentDate.substring(4, 10)}`;

        inputForm.value = {
            cmd: 'I',
            writeYear: writeYear,
            docType: 'TBM',
            docNo: '',
            compId: getCompId(),
            tbmDate: tbmDate,
            tbmTimeStt: '',
            tbmTimeEnd: '',
            tbmTime: '',
            sameDateYn: 'Y',
            workNm: '',
            workDesc: '',
            workPlace: '',
            riskYn: 'N',
            checkResult: '',
            workEndMeeting: '',
            useYn: 'Y',
            createdAt: currentDate,
        }

        inputFormOrg.value = _.cloneDeep(inputForm.value)

        potentialRisk.value = []
        potentialRiskOrg.value = []
        keyRisk.value = []
        safeCheck.value = []
        selectRiskAssessment.value = []
        selectProcess.value = []

        if(leaderComp.value != null) {
            leaderComp.value.userList = []
        }

        if(attendComp.value != null) {
            attendComp.value.userList = []
        }
    }

    const fileList = ref({
        editFiles: {
          insert: [],  // 새로 추가된 파일들
          delete: []   // 삭제할 파일들
        },
      });

    const files = ref([]) // 조회된 파일

    const setTbmTime = () => {
        const times = inputForm.value.tbmTime.split(' ~ ')

        if(times.length === 2) {
            const time1 = times[0].replace(/:/g, '')
            const time2 = times[1].replace(/:/g, '')

            if(time1 > time2) {
                inputForm.value.tbmTime = ''
                inputForm.value.tbmTimeStt = ''
                inputForm.value.tbmTimeEnd = ''
                // toastPopup(t('tbm_msgTimeRange'), 'error')
                alertMsg('warning', t('tbm_msgTimeRange'))
            } else {
                inputForm.value.tbmTimeStt = time1
                inputForm.value.tbmTimeEnd = time2
            }
        }
    }

    const goBack = () => {
        const result = checkChanged()

        if(result > 0) {
            confirmMsg('warning', t('msgSaveContinue'), '', { fun: goBackFunc })
        } else {
            goBackFunc()
        }
    }

    const goBackFunc = () => {
        router.push('/ToolBoxMeeting')
    }

    const searchData = async (showMsg) => {
        validationStore.clearInvalidClasses();
        validationStore.clearAllErrors();
        const result = checkChanged();
      
        if (showMsg && result > 0) {
          return new Promise(resolve => {
            confirmMsg('warning', t('msgSaveContinue'), '', {
              fun: async () => {
                await searchDataFunc(showMsg);
                resolve(); // resolve 호출
              }
            });
          });
        } else {
          await searchDataFunc(showMsg);
        }
      };

    const searchDataFunc = async (showMsg) => {
      potentialRisk.value = []
      potentialRiskOrg.value = []
      keyRisk.value = []
      safeCheck.value = []
      inputForm.value.deleteFiles = '';
      openLoading();
      return new Promise(resolve => {
        searchDataDetailApi(inputForm.value, showMsg).then(res => {
          if (res.list.length > 0) {
            // 데이터 세팅
            Object.keys(inputForm.value).forEach(key => {
              inputForm.value[key] = res.list[0][key];
              inputFormOrg.value[key] = res.list[0][key];
            });
    
            inputForm.value.createdAt = formatDate(inputForm.value.createdAt);
            inputForm.value.tbmDate = formatDate(inputForm.value.tbmDate);

            potentialRisk.value = res.list2;
            potentialRiskOrg.value = _.cloneDeep(res.list2);
            keyRisk.value = res.list3;
            safeCheck.value = res.list4;
            files.value = res.list[0].files;
            selectRiskAssessment.value = res.list5

            changeKeyRisk();
          } else {
            initInputForm();
          }

          beforeInputForm.value = _.cloneDeep(inputForm.value);
          beforePotentialRisk.value = _.cloneDeep(potentialRisk.value);
          beforeKeyRisk.value = _.cloneDeep(keyRisk.value);
          beforeSafeCheck.value = _.cloneDeep(safeCheck.value);

          selectProcess.value = []
          if(res.list5.length !== 0){
              selectRiskAssessment.value = [{ id: res.list5[0].riskDocNo, name: res.list5[0].riskNm, riskWriteYear : res.list5[0].writeYear, compId : getCompId(), riskDocNo : res.list5[0].riskDocNo, riskDocType : res.list5[0].riskDocType }];
              selectRiskAllowanceStandards.value = res.list5[0].riskAllowanceStandards;
              res.list5.forEach(val => {
                  selectProcess.value.push({id : val.processId, name : val.processNm, processId : val.processId, revNo : val.revNo})
              })
          }  

          resolve(); // 완료 알림
        }).finally(() => endLoading());
      });
    };

    /* TBM 불러오기 한 후 cmd='I'로 변경 처리 담당 */
    const normalizeLoadedTbmRows = () => {
      // 불러오기에서는 잠재위험요소에 종속된 safeCheck는 제외하던 기존 동작 유지
      safeCheck.value = safeCheck.value.filter(item => !item.potentialRiskSeq);
  
      const lists = [
          potentialRisk.value,
          keyRisk.value,
          safeCheck.value,
          selectRiskAssessment.value,
          selectProcess.value
      ];
  
      lists.forEach(list => {
          if (!Array.isArray(list)) return;
  
          list.forEach(row => {
              if (!row) return;
              row.cmd = 'I';
              row.docSeq = null;
          });
      });
    };

    const addData = () => {
        initInputForm();
    }

    const saveValidationCheck = async () => {
        // 저장 진행 중엔 추가 저장 요청을 막아 중복 저장(동시 저장)을 방지
        if (isSaving.value) {
            return false;
        }

        // 1. 유효성 검사
        const valid = await validationStore.validateAllFields('form', true);
        if (!valid) {
            return false;
        }
    
        // 2. PotentialRisk 중복 체크
        for (let i = 0; i < potentialRisk.value.length; i++) {
            const row = potentialRisk.value[i];
            const cnt = _.filter(potentialRisk.value, item => item.risk === row.risk && item.cmd !== 'D').length;

            if (cnt > 1) {
                await alertMsg('warning', t('tbm_msgOverlappingPotentialRiskFactor'));
                return false;
            }
        }

        // 3. KeyRisk 중복 체크
        for (let i = 0; i < keyRisk.value.length; i++) {
            const row = keyRisk.value[i];
            const cnt = _.filter(keyRisk.value, item => item.potentialRiskSeq === row.potentialRiskSeq && item.cmd !== 'D').length;

            if (cnt > 1) {
                await alertMsg('warning', t('tbm_msgOverlappingKeyRiskFactor'));
                return false;
            }
        }

        // 4. 참석자 중복 체크
        const leaderUser = leaderComp.value.userList;
        for (let i = 0; i < leaderUser.length; i++) {
            if (_.some(attendComp.value.userList, { hrId: leaderUser[i].hrId })) {
                await alertMsg('warning', t('tbm_msgDplLeaderAttend'));
                return false;
            }
        }

        // 모든 과정 순서대로 완료 후 true 리턴
        return true;
    };
    

    const saveData = () => {
        // 저장 중이면 중복 진입을 막음 (이 함수가 직접 호출되는 경우까지 방어)
        if (isSaving.value) return Promise.resolve(false);

        isSaving.value = true;
        openLoading();
    
        // docSeq가 값이 있을 경우 cmd를 'U'로 설정
        const updateCmdIfDocSeqExists = list => {
            return list.map(item => {
                // 이미 cmd가 'D'인 경우는 그대로 두고, 아닌 경우만 'U'로 갱신
                if (item.docSeq && item.cmd !== 'D') {
                    return { ...item, cmd: 'U' };
                }
                return item;
            });
        };

        const param = {
            main: {},
            potentialRisk: updateCmdIfDocSeqExists(potentialRisk.value),
            keyRisk: updateCmdIfDocSeqExists(keyRisk.value),
            safeCheck: updateCmdIfDocSeqExists(safeCheck.value),
            selectRiskAssessment : selectRiskAssessment.value,
            selectProcess : selectProcess.value
        };
        inputForm.value.deleteFiles = fileList.value.editFiles.delete;
        inputForm.value.tbmDate = formatDateForDB(inputForm.value.tbmDate);
    
        Object.keys(inputForm.value).forEach(key => {
            param.main[key] = inputForm.value[key];
        });
        const formData = new FormData();
        formData.append('info', new Blob([JSON.stringify(param)], { type: 'application/json' }));
    
        fileList.value.editFiles.insert.forEach(file => {
            formData.append('files', file || new Blob([], { type: 'application/octet-stream' }));
        });
    
        console.log('############ 저장 데이터 : ' , inputForm.value)
        console.log('############ 저장 데이터 : ' , param)
        return saveDataApi(formData, true).then(async res => {
            if (res?.success) {
                
                inputForm.value.docNo = res.result.docNo;
                inputForm.value.cmd = 'U';
                inputForm.value.writeYear = res.result.writeYear;
    
                await leaderComp.value.setHrChipsApprovalInfo("TBM", res.result.writeYear, res.result.docNo);
                await leaderComp.value.updateTaskUseYn("TBM", res.result.writeYear, res.result.docNo);
                await attendComp.value.setHrChipsApprovalInfo("TBM", res.result.writeYear, res.result.docNo);
                await attendComp.value.updateTaskUseYn("TBM", res.result.writeYear, res.result.docNo);
    
                // if (inputForm.value.layoutStore) {
                //     inputForm.value.layoutStore.useBtnList = ['btnBack', 'btnSearch', 'btnAdd', 'btnSave', 'btnDelete', 'btnDownload'];
                //     inputForm.value.layoutStore = {};
                // }
    
                await searchData();
    
                return true; // 성공 결과 리턴
            }
            return false;
        }).finally(() => {
            // 성공/실패/예외 모두에서 로딩 종료 + 락 해제 보장
            endLoading();
            isSaving.value = false;
        });
    };    
    

    const deleteMsg = () => {
        if (inputForm.value.docNo.length < 1) {
            // toastPopup(t('tbm_msgNewData'), 'error')
            alertMsg('warning', t('tbm_msgNewData'))
            return
        }
        confirmMsg('warning', t('msgDelete'), '', { fun: deleteData })
    }

    const deleteData = () => {
        openLoading();
        deleteDataApi([inputForm.value], true).then(async res => {
            if(res.success) {
                await signatureStore.forcedUpdateTaskUseYn('N', "TBM", inputForm.value.writeYear, inputForm.value.docNo);
                goBackFunc();
            }
        }).finally(() => endLoading());
    }

    const print = (param) => {
        param = {checkedObjList: param.map(el=>({
            writeYear: el.writeYear,
            docType: el.docType,
            docNo: el.docNo,
            compId: el.compId
        }))}
        baseDownload(getReportApi, param)
        // openLoading();
        // return new Promise((resolve) => {
        //     getReportApi(param, false).then(res => {
        //         resolve({
        //             result: res.result,
        //             success: res.success
        //         })

        //         const link = document.createElement('a')
        //         link.href = window.URL.createObjectURL(res.data)
        //         link.download = res.headers.filename
        //         link.click()
        //     }).finally(() => endLoading());
        // })
    }

    const addPotentialRisk = () => {
        potentialRisk.value.push({
            cmd: 'I',
            writeYear: inputForm.value.writeYear,
            docType: inputForm.value.docType,
            docNo: inputForm.value.docNo,
            docSeq: '',
            risk: '',
            measures: '',
        })
    }

    const deletePotentialRisk = row => {
        if(row.cmd === 'U') {
            row.cmd = 'D'
        } else {
            _.remove(potentialRisk.value, {
                rowNo: row.rowNo
            })
        }
    }

    const addKeyRisk = () => {
        if(_.filter(keyRisk.value, item => { return item.cmd !== 'D' }).length >= potentialRiskOrg.value.length) {
            return
        }

        const cnt = _.filter(potentialRiskOrg.value, { cmd: 'U' }).length

        if(cnt < 1) {
            // toastPopup(t('tbm_msgNoStorePotentialRiskFactor'), 'error')
            alertMsg('warning', t('tbm_msgNoStorePotentialRiskFactor'))
            return
        }

        let potentialSeq = ''
        for(let i = 0; i < potentialRiskOrg.value.length; i++) {
            if(!_.some(keyRisk.value, item => { return item.potentialRiskSeq === potentialRiskOrg.value[i].docSeq && item.cmd !== 'D' })) {
                potentialSeq = potentialRiskOrg.value[i].docSeq
                break
            }
        }

        keyRisk.value.push({
            cmd: 'I',
            writeYear: inputForm.value.writeYear,
            docType: inputForm.value.docType,
            docNo: inputForm.value.docNo,
            docSeq: '',
            potentialRiskSeq: potentialSeq,
            measures: '',
        })

        changeKeyRisk()
    }

    const deleteKeyRisk = row => {
        if(row.cmd === 'U') {
            row.cmd = 'D'
        } else {
            _.remove(keyRisk.value, {
                rowNo: row.rowNo
            })
        }

        changeKeyRisk()
    }

    const addSafeCheck = () => {
        safeCheck.value.push({
            cmd: 'I',
            writeYear: inputForm.value.writeYear,
            docType: inputForm.value.docType,
            docNo: inputForm.value.docNo,
            docSeq: '',
            potentialRiskSeq: '',
            risk: '',
            actionYn: 'N',
            action: '',
        })
    }

    const deleteSafeCheck = row => {
        if(row.cmd === 'U') {
            row.cmd = 'D'
        } else {
            _.remove(safeCheck.value, {
                rowNo: row.rowNo
            })
        }
    }

    const setRowNo = arr => {
        let i = 1
        arr.forEach(row => {
            if(row.cmd !== 'D') {
                row.rowNo = i
                i++
            }
        })
    }

    const setLeaderComp = comp => {
        leaderComp.value = comp
    }

    const setAttendComp = comp => {
        attendComp.value = comp
    }

    const setPeopleLeaderComp = comp => {
        peopleLeaderComp.value = comp
    }

    const setPeopleAttendComp = comp => {
        peopleAttendComp.value = comp
    }

    const leaderPopupOpen = () => {
        peopleLeaderComp.value.onOpen()
    }

    const showLeaderPopup = computed(() => {
        const list = leaderComp.value?.userList || [];
        const hasD = list.some(user => user.cmd === 'D');
        const hasI = list.some(user => user.cmd === 'I');
        const hasU = list.some(user => user.cmd === 'U');
        // 기존에 서명 데이터가 없거나, D(삭제서명) 데이터는 있지만 I(신규서명), U(기존서명) 데이터가 없는 경우만 + 버튼 활성화
        return list.length === 0 || (hasD && !hasI && !hasU);  
    });

    const attendPopupOpen = () => {
        peopleAttendComp.value.onOpen()
    }

    const selectLeaderPeople = () => {
        const selectedUser = leaderComp.value.selectedUserList()
      
        for(let i = 0; i < selectedUser.length; i++) {
            if(_.some(attendComp.value.userList, {
                hrId: selectedUser[i].hrId
            })) {
                // toastPopup(t('tbm_msgAlreadyAttend'), 'error')
                alertMsg('warning', t('tbm_msgAlreadyAttend'))
                return
            }
        }

        leaderComp.value.selectPeople()
        peopleLeaderComp.value.onClose()
    }

    const selectAttendPeople = () => {
        const selectedUser = attendComp.value.selectedUserList()

        for(let i = 0; i < selectedUser.length; i++) {
            if(_.some(leaderComp.value.userList, {
                hrId: selectedUser[i].hrId
            })) {
                selectedUser[i].selected = false;
                // toastPopup(t('tbm_msgAlreadyLeader'), 'error')
                alertMsg('warning', t('tbm_msgAlreadyLeader'))
                return
            }
        }

        attendComp.value.selectPeople()
        peopleAttendComp.value.onClose()
    }

    const closeLeaderPopup = () => {
        peopleLeaderComp.value.onClose()
    }

    const closeAttendPopup = () => {
        peopleAttendComp.value.onClose()
    }

    const checkChanged = () => {
        let cnt = 0;
        setRowNo(beforePotentialRisk.value);
        setRowNo(beforeKeyRisk.value);
        setRowNo(beforeSafeCheck.value);

        // lodash isEqual로 비교
        if (!_.isEqual(beforeInputForm.value, inputForm.value)) {
            cnt++;
        }

        if (!_.isEqual(beforePotentialRisk.value, potentialRisk.value)) {
            cnt++;
        }

        if (!_.isEqual(beforeKeyRisk.value, keyRisk.value)) {
            cnt++;
        }

        if (!_.isEqual(beforeSafeCheck.value, safeCheck.value)) {
            cnt++;
        }

        return cnt;
    }

    const changeKeyRisk = () => {
        potentialRiskOrg.value.forEach(item => {
            // console.log('item.cmd', item.cmd, item.risk, _.some(keyRisk.value, { potentialRiskSeq: item.docSeq }))

            if(_.some(keyRisk.value, item2 => {
                return item2.potentialRiskSeq === item.docSeq && item2.cmd !== 'D'
            })) {
                item.selYn = 'Y'
            } else {
                item.selYn = 'N'
            }
        })

        // potentialRiskOrg.value = _.cloneDeep(potentialRiskOrg.value)

        // console.log('potentialRiskOrg.value', potentialRiskOrg.value)
        // console.log('keyRisk.value', keyRisk.value)
    }

    watch(
        () => potentialRisk.value,
        () => { setRowNo(potentialRisk.value) },
        { immediate: true, deep: true }
    )

    watch(
        () => keyRisk.value,
        () => { setRowNo(keyRisk.value) },
        { immediate: true, deep: true }
    )

    watch(
        () => safeCheck.value,
        () => { setRowNo(safeCheck.value) },
        { immediate: true, deep: true }
    )

    return {
        inputForm,
        potentialRisk,
        potentialRiskOrg,
        keyRisk,
        safeCheck,
        initInputForm,
        setTbmTime,
        goBack,
        searchData,
        normalizeLoadedTbmRows,
        addData,
        saveValidationCheck,
        saveData,
        deleteMsg,
        print,
        addPotentialRisk,
        deletePotentialRisk,
        addKeyRisk,
        deleteKeyRisk,
        addSafeCheck,
        deleteSafeCheck,
        setLeaderComp,
        setAttendComp,
        setPeopleLeaderComp,
        setPeopleAttendComp,
        leaderPopupOpen,
        attendPopupOpen,
        selectLeaderPeople,
        selectAttendPeople,
        closeLeaderPopup,
        closeAttendPopup,
        changeKeyRisk,
        checkChanged,
        showLeaderPopup,
        leaderComp,
        fileList,
        files,
        selectRiskAssessment,
        selectProcess,
        selectRiskAllowanceStandards
    }
})
