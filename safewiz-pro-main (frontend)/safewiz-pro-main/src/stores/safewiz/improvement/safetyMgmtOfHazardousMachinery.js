import BaseView from "@/components/base/BaseView";
import { defineStore } from "pinia";
import { deleteSafetyMgmtInspections, deleteSafetyMgmts, getHazardousMachineryReport, getHazardousMachineryReportDetail, getSafetyMgmtDetail, getSafetyMgmts, saveSafetyMgmt } from "./api/safetyMgmtOfHazardousMachinery";
import router from "@/router";
import { getEquip } from "@/stores/system/base/api/equipmentApi";
import { reactive } from "vue";
import { AesDecrypt, AesEncrypt } from "@/utils/aes256";
import { customCookies } from "@/utils/token";
import { useButtonListStore } from "@/stores/buttonList";
import _ from 'lodash';
import { useContinualImprovementStore } from "./continualImprovement";

export const useSafetyMgmtOfHazardousMachineryStore = defineStore('safetyMgmtOfHazardousMachinery', () => {

    // 위험기계 안전관리 테이블 데이터 (설비 데이터 가져옴) 

    // 위험기계 및 기구 안전관리 사용조직 테이블 데이터 (조직 데이터 가져옴)
    // 설비:조직 => 1:N (위험기계 안전관리 테이블의 docNo 참조)

    // 위험기계 및 기구 안전관리 검사 테이블 데이터
    // 설비:검사 => 1:N (위험기계 안전관리 테이블의 docNo 참조)


    const { getCurrentDate, confirmMsg, alertMsg, t, ref, validationStore, getCompId, openLoading, endLoading, formatDate, formatDateForDB } = BaseView();
    const continualImprovementStore = useContinualImprovementStore();
    
    const safetyMgmtList = ref([]); // 필터링을 위한 데이터 변수
    const orgnList = ref([]);       // 카드 카테고리 (조직)
    const equipList = ref([]);      // 카드 카테고리 (설비)
    const buttonList = ref([ 'btnBack', 'btnSave' ]);
    const fileInfo = ref([]);
    const layoutStore = useButtonListStore();
    
    // 리스트 출력용 변수(i-card 삽입)
    const safetyMgmtCardList = ref([]);

    const isUpdated = ref(true);

    // 메인 목록 필터링 버튼
    const useSafetyMgmtOfHazardousMachineryDivList = ref([
        { id: 'organization', 'name': '조직'},
        { id: 'equipment', 'name': '설비'},
    ]);

    // 점검주기, 검사 유효기간 옵션
    const periods = ref([
        { id: '6개월' },
        { id: '1년' },
        { id: '2년' },
        { id: '3년' },
    ]);

    const cmd = ref('U');           // 저장(Insert, Update) 트리거
    const docType = ref('SMOHM');   // 문서 타입
    const writeYear = ref(getCurrentDate().substring(0, 4));    // 작성 년도

    const searchParam = ref({
        compId: getCompId(),
        writeYear: writeYear.value,
        content: '',
        searchText :  new Date().getFullYear()
    });    // 검색어 변수

    // 필터링 기준
    const currentFilter = ref('organization');

    const initList = () => {
        safetyMgmtCardList.value = []
    }

    // 위험기계 및 기구 안전관리 데이터 폼
    // Insert할 데이터 출력 폼
    const inputForm = ref({
        equipmentId: '',
        equipmentNm: '', 
    });
    const validForm = reactive({});
    const initInputForm = () => {
        inputForm.value = {
            cmd: 'I',
            compId: '',
            equipmentId: '',
            equipmentNm: '',
            equipmentMgmtNum: '',
            equipmentMfSpec: '',
            inspectionCycle: periods.value[0].id,
            inspectionExpiryDate: periods.value[0].id,
            useYn: 'Y',
            orgnList: [],
            inspectionList: [],
        }
    };

    const file = (file) => {
        inputForm.value.inspectionList.forEach((inspection, i) => {
            const targetFiles = inspection.files;
            file.value[i]?.fnSearch?.(targetFiles);
        });
    };
    // 검색어 입력에 대한 필터링 함수
const search = () => {
    const content = searchParam.value.content?.toLowerCase() || '';

    const searchData = safetyMgmtList.value.filter(item => (
        item.safetyMgmt.equipmentNm?.toLowerCase().includes(content) ||
        item.safetyMgmt.equipmentMgmtNum?.toLowerCase().includes(content) || 
        item.safetyMgmtOrgnList?.some(orgn => 
            orgn.cardOrgnNm?.toLowerCase().includes(content)
        ))
    );

    cardListFamtting(searchData);
};

    // 사용여부 이벤트
    const switchUseYn = (obj, key) => {
        obj[key] = obj[key] === 'Y' ? 'N' : 'Y';
        obj.checked = true;
    }

    // 이미 저장된 안전관리 품목 제거 (상세 페이지 설비 팝업)
    const getFilteredEquip = async () => {
        return getEquip().then(res => {

            const filteredList = {
                list: res.list.filter(e => 
                    safetyMgmtList.value.every(s => e.equipmentId !== s.safetyMgmt.equipmentId)
                )
            }

            return filteredList;
        });
    }

    // 검사일 카드 추가
    const addCard = () => {
        if(inputForm.value.equipmentId === '') {
            validationStore.validateField('equipment', 'eqiupment', 'div', true);
            return;
        }

        inputForm.value.inspectionList.push({
            cmd: 'I',
            writeYear: writeYear.value,
            docType: docType.value,
            docSeq: '',
            docNo: cmd.value === 'U' ? inputForm.value.docNo : '',
            insepctionDate: '',
            nextInspectionDate: '',
            useYn: 'Y',
            checked: false
        });
    }

    // const insertValid = async () =>{
    //     let validTrigger = true;
    //     validTrigger = await validationStore.validateAllFields('form', true);
        
    //     if(inputForm.value.inspectionList.length === 0 && cmd.value === 'U') {
    //         alertMsg('warning', '카드 추가는 필수입니다.');
    //         validTrigger = false;
    //     }

    //     for(let index in inputForm.value.inspectionList) {
    //         const item = inputForm.value.inspectionList[index];
            
    //         validationStore.validateAllFields('card' + index, true);
    //         if(item.inspectionDate === '' || item.inspectionDate === undefined) {
    //             validTrigger = false;
    //             break;
    //         }
    //         if(item.nextInspectionDate === '' || item.nextInspectionDate === undefined) {
    //             validTrigger = false;
    //             break;
    //         }
    //     }

    //     return validTrigger;

    // }


    const btnSave = async () => {

        //const validTrigger = await insertValid();

        // if(!validTrigger) {
        //     return;
        // }

        if(inputForm.value.orgnList.length > 0){
            if(inputForm.value.orgnList[0].nm === '미분류') {
                inputForm.value.orgnList = [];
            }
        }

        const formData = new FormData();
        const fileKeys = [];
        
        inputForm.value.inspectionList.forEach((el, index) => {
            if (!el.checked) return;
            el.deleteFiles = fileInfo.value[index]?.editFiles?.delete || [];
            // 파일 저장 시, index를 키로 지정
            if (!el.docSeq) {
                el.docSeq = `new${index}`; // 신규인 경우 임시 키 값 설정
            }
            fileInfo.value[index]?.editFiles?.insert.forEach(file => {
                if (file) {
                    formData.append(`files`, file);
                    fileKeys.push(el.docSeq);
                }
            });
        });

        formData.append('fileKeys', new Blob([JSON.stringify(fileKeys)], { type: 'application/json' }));

        const data = {
            safetyMgmt: {
                cmd: cmd.value,
                writeYear: writeYear.value,
                docType: docType.value,
                docNo: inputForm.value.docNo,
                compId: inputForm.value.compId,
                equipmentId: inputForm.value.equipmentId,
                equipmentMgmtNum: inputForm.value.equipmentMgmtNum,
                equipmentMfSpec: inputForm.value.equipmentMfSpec,
                inspectionCycle: inputForm.value.inspectionCycle,
                inspectionExpiryDate: inputForm.value.inspectionExpiryDate,
                useYn: inputForm.value.useYn,
            },
            // safetyMgmtOrgnList: inputForm.value.orgnList.map(item => ({
            //     orgnId: item.id, orgnNm: item.nm,
            //     writeYear: writeYear.value,
            //     docType: docType.value,
            //     docNo: inputForm.value.docNo,
            // })),
            safetyMgmtOrgnList: inputForm.value.orgnList.map(item => ({
                orgnId: item.id, orgnNm: item.nm,
                writeYear: writeYear.value,
                docType: docType.value,
                docNo: inputForm.value.docNo,
            })),
            safetyMgmtInspectionList: inputForm.value.inspectionList.filter(item => item.checked === true).map(item => {
                return {
                    ...item,
                    inspectionDate: formatDateForDB(item.inspectionDate),
                    inspectionValidFromDt: formatDateForDB(item.inspectionValidFromDt),
                    inspectionValidToDt: formatDateForDB(item.inspectionValidToDt),
                    nextInspectionDate: formatDateForDB(item.nextInspectionDate),
                    writeYear: writeYear.value,
                    docType: docType.value,
                    docNo: inputForm.value.docNo,
                    deleteFiles: item.deleteFiles
                }
            })
        };
        
        // 상세 데이터 추가
        formData.append('info', new Blob([JSON.stringify(data)], { type: 'application/json' }));
        //t('msgSave') : 저장 하시겠습니까?
        confirmMsg('info', t('msgSave'), ``, { fun: doSave, param: formData });
    }

    const doSave = (data) => {
        return new Promise((resolve) => {
            saveSafetyMgmt(data, true).then(res => {
                resolve({ result: res.list, success: res.success });
                if(res && res.success) {
                    cmd.value = 'U';
                    isUpdated.value = true;
                    btnDetailSearch();
                }
    
            }).finally(()=> {
                layoutStore.useBtnList = [ 'btnBack', 'btnSearch', 'btnAdd', 'btnSave', 'btnDelete', 'btnDownload' ];
            });
        });
    }
    

    // 안전관리 조회
    const btnSearch = async (notify) => {
        return await getSafetyMgmts({writeYear: searchParam.value.searchText, compId: getCompId()}, notify).then(res => {
            continualImprovementStore.searchParam.searchText = searchParam.value.writeYear;
            safetyMgmtList.value = res.list;
            cardListFamtting(); // 전체에서 필터링 적용
        });
    };
    

    // 목록화면 데이터 포멧
    const cardListFamtting = async (data = safetyMgmtList.value) => {
        await initList(); // 리스트 초기화

        let filteredData = data.filter(item => {
            const content = searchParam.value.content?.toLowerCase() || '';
            const equipmentNm = item.safetyMgmt.equipmentNm?.toLowerCase() || '';
            const equipmentMgmtNum = item.safetyMgmt.equipmentMgmtNum?.toLowerCase() || '';
            
            return (
                (equipmentNm.includes(content) || 
                equipmentMgmtNum.includes(content) || 
                item.safetyMgmtOrgnList?.some(orgn => 
                    orgn.cardOrgnNm?.toLowerCase().includes(content)
                ))
            );
        });

        filteredData.forEach(item => {
            if (item.safetyMgmtOrgnList.length === 0) {
                item.safetyMgmtOrgnList.push({ orgnNm: '미분류', cardOrgnNm: '미분류' });
            }
        });

        // 데이터 출력 순서 유지
        const orgnNmArray = [];
        filteredData.forEach(item => {
            item.safetyMgmtOrgnList.forEach(orgn => {
                if (!orgnNmArray.includes(orgn.cardOrgnNm)) {
                    orgnNmArray.push(orgn.cardOrgnNm);
                }
            });
        });

        const equipNmSet = filteredData.reduce((acc, item) => {
        const { stdEqId, stdEqNm } = item.safetyMgmt;
        if (!acc.some(v => v.stdEqId === stdEqId)) {
            acc.push({ stdEqId, stdEqNm });
        }
        return acc;
        }, []);

        orgnList.value = orgnNmArray;

        equipList.value = Array.from(equipNmSet).sort((a, b) => (a.stdEqNm || '').localeCompare(b.stdEqNm || ''));
        filteredData = filteredData.map(item => {
            // inspectionDate <= searchText 인 데이터만 추출
            const filteredInspections = item.safetyMgmtInspectionList
                .filter(ins => ins.useYn === 'Y') // useYn이 Y인 것만
                .sort((a, b) => {
                    const aYear = a.inspectionDate?.slice(0, 4) || '0';
                    const bYear = b.inspectionDate?.slice(0, 4) || '0';
                    const searchYear = searchParam.value.searchText;

                    // searchText보다 높은 년도는 맨 아래로
                    if (aYear > searchYear && bYear <= searchYear) return 1;
                    if (bYear > searchYear && aYear <= searchYear) return -1;

                    // searchText 이하인 것들은 최신 날짜(desc)로 정렬
                    return b.inspectionDate.localeCompare(a.inspectionDate);
                });

            return {
                ...item,
                safetyMgmtInspectionList: filteredInspections
            };
        });
        filteredData.forEach(sf => {
            sf.safetyMgmtOrgnList.forEach(orgn => {
                if (!_.some(safetyMgmtCardList.value, row => row.writeYear === sf.safetyMgmt.writeYear && row.docType === sf.safetyMgmt.docType && row.docNo === sf.safetyMgmt.docNo)) {
                    safetyMgmtCardList.value.push({
                        detail: {
                            관리번호: sf.safetyMgmt.equipmentMgmtNum,
                            점검주기: sf.safetyMgmt.inspectionCycle,
                            검사일: sf.safetyMgmtInspectionList[0]?.inspectionDate ? formatDate(sf.safetyMgmtInspectionList[0].inspectionDate) : '-',
                            차기검사일: sf.safetyMgmtInspectionList[0]?.nextInspectionDate ? formatDate(sf.safetyMgmtInspectionList[0].nextInspectionDate) : '-'
                        },
                        title: sf.safetyMgmt.equipmentNm,
                        orgn: orgn.cardOrgnNm,
                        useYn: sf.safetyMgmt.useYn,
                        docNo: sf.safetyMgmt.docNo,
                        docType: sf.safetyMgmt.docType,
                        writeYear: sf.safetyMgmt.writeYear,
                        stdEqId : sf.safetyMgmt.stdEqId,
                        checked: false
                    });
                }
            });
        });
        safetyMgmtCardList.value.sort((a, b) => {
            const aY = (a.useYn || '').toUpperCase() === 'Y' ? 1 : 0;
            const bY = (b.useYn || '').toUpperCase() === 'Y' ? 1 : 0;
            if (aY !== bY) return bY - aY; // Y 먼저

            //useYn 같으면 title로 정렬
            return (a.title || '').localeCompare(b.title || '');
        });
    };


    // 상세화면 조회
    const btnDetailSearch = async () => {
        if(cmd.value === 'I') {
            return;
        }
        return new Promise((resolve) => {
            const data = {
                writeYear: searchParam.value.writeYear,
                docType: docType.value,
                compId: inputForm.value.compId,
                equipmentId: inputForm.value.equipmentId,
            };

            getSafetyMgmtDetail(data, true).then(res => {
                resolve({ result: res.list, success: res.success});

                if(res && res.success) {
                    detailFormatting(res.list);
                }
            });
        });
    }

    // 목록 버튼
    const btnBack = () => {
        const path = router.currentRoute.value.fullPath;
        if(path === '/SafetyMgmtOfHazardousMachinery') {
            goBack();
            return;
        }
        if(cmd.value === 'I') {
            if(inputForm.value.useYn !== '') {
                confirmMsg('warning', t('msgSaveContinue'), ``, { fun: goBack });
                return;
            }
        }else {
            if(!isUpdated.value) {
                confirmMsg('warning', t('msgSaveContinue'), ``, { fun: goBack });
                return;
            }
        }
        goBack();
    }

    const goBack = () => {
        isUpdated.value = true;
        router.go(-1);
    }
    
    // 삭제 버튼 클릭 이벤트 (useYn: Y => N)
    const btnDelete = () => {
        const deleteList = safetyMgmtCardList.value.filter(item => item.checked === true).map(item => ({
            'title': item.title,
            'writeYear': item.writeYear,
            'docType': item.docType,
            'docNo': item.docNo,
            'useYn': item.useYn
        })).filter((item, index, self) => index === self.findIndex((t) => t.writeYear === item.writeYear && t.docType === item.docType && t.docNo === item.docNo));

        const deletedAlready = deleteList.some(item => item.useYn === 'N');

        if(deletedAlready) {
            alertMsg('warning', t('msgDeletedItem'));
            return;
        }

        confirmMsg('warning', t('msgDelete'), ``, { fun: doDelete, param: deleteList });
    }

    // 1. insert update 구분 해야함
    // 2. inspectionList.cmd 로 판별 

    
    // 2. 있으면 검사일정 cmd가 I인지 판별 / 없으면 alertMsg
    // 3. I : 카드 삭제 / U : useYn 비활성화 (Backend 접속)

    const btnDelInspection = () => {
        
        const deleteList = inputForm.value.inspectionList.filter(item => item.checked);

        if(deleteList.length === 0) {
            alertMsg('warning', '선택된 항목이 없습니다.');
            return;
        }

        // const insertCard = deleteList.filter(item => item.cmd === 'I');
        const updateCard = deleteList.filter(item => item.cmd === 'U');
        
        if(updateCard.length > 0) {
            const deletedAlready = updateCard.some(item => item.useYn === 'N');
            
            // 이미 비활성화 된 카드일 경우
            if(deletedAlready) {
                alertMsg('warning', t('msgDeletedItem'));
                return;
            }
            
            if (deleteList.length === 1) {
                //t('msgDelete') : 삭제 하시겠습니까?
                confirmMsg('warning', t('msgDelete'), ``, { 
                    fun: doDelInspection, param: deleteList 
                });
            } else {
                confirmMsg('warning', t('msgDelete'), ``, {
                    fun: doDelInspection, param: deleteList
                });
            }
            
        }

        // 생성중인 카드일 경우
        inputForm.value.inspectionList = inputForm.value.inspectionList.filter(item => !(item.cmd === 'I' && item.checked === true));


    }

    const doDelete = async (data) => {
        return new Promise((resolve) => {
            deleteSafetyMgmts(data, true).then(res => {
                resolve({ result: res.list, success: res.success});
                if(res && res.success) {
                    btnSearch();
                }
            });
        });
    }

    const doDelInspection = async (data) => {
        return new Promise((resolve) => {
            deleteSafetyMgmtInspections(data, true).then(res => {
                resolve({ result: res.list, success: res.success});
                if(res && res.success) {
                    isUpdated.value = true;
                    btnDetailSearch();
                }
            });
        });
    }
    
    // 신규 추가 이동
    const goWrite = () => {
        initInputForm();
        cmd.value = 'I';
        router.push({ name: 'SafetyMgmtOfHazardousMachineryDetail'});
    }
    
    // 카드 클릭시 이동하는 상세 페이지
    const goDetail = (detail) => {
        const detailInfo = safetyMgmtList.value.find(item => item.safetyMgmt.docNo === detail.docNo);
        detailFormatting(detailInfo);
        router.push({ name: 'SafetyMgmtOfHazardousMachineryDetail'});
    };

    // 디테일 화면 데이터 포멧
    const detailFormatting = async (detailInfo) => {
        openLoading();
        cmd.value = 'U';
        initInputForm();
        if(detailInfo.safetyMgmt?.docNo == null){
            endLoading();
            return;
        }

        inputForm.value = {
            cmd: cmd.value,
            docNo: detailInfo.safetyMgmt.docNo,
            compId: detailInfo.safetyMgmt.compId,
            equipmentId: detailInfo.safetyMgmt.equipmentId,
            equipmentNm: detailInfo.safetyMgmt.equipmentNm,
            equipmentMgmtNum: detailInfo.safetyMgmt.equipmentMgmtNum === null ? '' : detailInfo.safetyMgmt.equipmentMgmtNum,
            equipmentMfSpec: detailInfo.safetyMgmt.equipmentMfSpec === null ? '' : detailInfo.safetyMgmt.equipmentMfSpec,
            inspectionCycle: detailInfo.safetyMgmt.inspectionCycle,
            inspectionExpiryDate: detailInfo.safetyMgmt.inspectionExpiryDate,
            useYn: detailInfo.safetyMgmt.useYn,

          orgnList: detailInfo.safetyMgmtOrgnList
            .filter(item => item.orgnId) // orgnId가 있는 항목만 필터링
            .map(item => ({ id: item.orgnId, nm: item.orgnNm })),
            inspectionList: detailInfo.safetyMgmtInspectionList.length === 0 ? [] : detailInfo.safetyMgmtInspectionList.map(item => ({
                ...item,
                inspectionDate: formatDate(item.inspectionDate),
                inspectionValidFromDt: formatDate(item.inspectionValidFromDt),
                inspectionValidToDt: formatDate(item.inspectionValidToDt),
                nextInspectionDate: formatDate(item.nextInspectionDate),
                checked: false
            }))
        }

        buttonChange();

        await Promise.all(inputForm.value.inspectionList.map((val, index) => {
            if (!fileInfo.value[index]){
                endLoading();
                return Promise.resolve
            }

            return fileInfo.value[index].fnSearch?.(val.files);
        }));
        endLoading();
    }

    // 다운로드 버튼 함수
    const btnDownload = async () => {
        const checkedList = safetyMgmtCardList.value.filter(item => item.checked)
        let keys;
        let type;
        if(checkedList.length === 0){
            type = 'All'
            keys = safetyMgmtCardList.value.map(item => {
                return {
                    type : layoutStore.downloadType,
                    writeYear : searchParam.value.searchText,
                    docNo : item.docNo,
                    docType : item.docType
                }

            })
        }else{
            keys = checkedList.map(item => {
                return {
                    type : layoutStore.downloadType,
                    writeYear : searchParam.value.searchText,
                    docNo : item.docNo,
                    docType : item.docType
                }
            })
        }
        doDownload(keys, type);  
    } 

    //Card 에서 출력 시
    const doDownload = async (downloadList, type) => {
        openLoading()
        if(type === 'All'){ //전체 데이터 출력
            return new Promise((resolve) => {
                getHazardousMachineryReport(downloadList, true).then(res => {
                    resolve({ result: res.result, success: res.success })
                    let link = document.createElement('a');
                    // 객체를 만들어서 생성
                    link.href = window.URL.createObjectURL(res.data);
                    link.download = res.headers.filename;
                    link.click();
                    endLoading()
                }).catch(()=>{
                    endLoading()
                });
            });
        }else{ //체크한 데이터만 출력
            return new Promise((resolve) => {
                getHazardousMachineryReportDetail(downloadList, true).then(res => {
                    resolve({ result: res.result, success: res.success })
                    let link = document.createElement('a');
                    // 객체를 만들어서 생성
                    link.href = window.URL.createObjectURL(res.data);
                    link.download = res.headers.filename;
                    link.click();
                    endLoading()
                }).catch(()=>{
                    endLoading()
                });
            });
        }
    }

    //Detail에서 출력 시
    const btnDownloadDetail = () => {
        openLoading()
        const downloadList = [{
            writeYear : new Date().getFullYear(),
            docNo : inputForm.value.docNo,
            type : layoutStore.downloadType
        }]
        return new Promise((resolve) => {
            getHazardousMachineryReportDetail(downloadList, true).then(res => {
                resolve({ result: res.result, success: res.success })
                let link = document.createElement('a');
                // 객체를 만들어서 생성
                link.href = window.URL.createObjectURL(res.data);
                link.download = res.headers.filename;
                link.click();
                endLoading()
            }).catch(()=>{
                endLoading()
            });
        });
    }

    const buttonChange = () => {
        if(cmd.value === 'I') {
            layoutStore.useBtnList = [ 'btnBack', 'btnSave' ];
        }else {
            layoutStore.useBtnList = [ 'btnBack', 'btnSearch', 'btnAdd', 'btnSave', 'btnDelete', 'btnDownload' ];
        }
    }

    return {
        useSafetyMgmtOfHazardousMachineryDivList, currentFilter, safetyMgmtCardList, validForm, buttonList, fileInfo,
        inputForm, periods, writeYear, safetyMgmtList, orgnList, equipList, cmd, searchParam, isUpdated,
        initInputForm, btnSave, goWrite, addCard, btnSearch, getFilteredEquip, btnDelInspection, buttonChange,
        initList, goDetail, btnDelete, switchUseYn, btnDetailSearch, search, btnBack, btnDownload, btnDownloadDetail, file
    };
    
});
