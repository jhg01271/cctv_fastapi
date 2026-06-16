import { getFormattedTargetId, getTargetId } from '@/utils/documentUtils.js';
import { getSystemCode } from '@/stores/safewiz/dataset/api/datasetApi';

import {
    // deleteApprovalInfo,
    // updateApprovalInfo Task 안들어 있음
    nullUpdateApprovalInfo,
    insertApprovalInfo,
    insertApprovalInfoList,
    updateTaskUseYnApi,
    searchApprovalInfo,
    updateApprovalInfoSign,
    updateApprovalInfoSignCancel
    // updateUserSignature
} from '@/api/base/common/utils.js';
import { useUserInfoStore } from '@/stores/user.js';
import BaseView from '@/components/base/BaseView.js';

const { reactive, ref, router, alertMsg } = BaseView();

const userInfoStore = useUserInfoStore(); // 현재 사용자 정보
const initialUseYn = ref('');
export const createSignatureStore = () => {
    const signatureData = ref([]);
    const signatureIndex = ref(null);
    const systemCodeData = ref(null);

    //#region 시스템 코드 가져오기( 작성, 검토, 승인 ) 관련
    const getSystemCodeList = async param => {
        // openLoading();
        try {
            return await getSystemCode(param);
        } catch (error) {
            console.error('getSystemCode API 호출 오류:', error);
            throw error;
        } finally {
            // endLoading();
        }
    };
    //#endregion

    //#region 초기값 설정
    const init = async props => {
        systemCodeData.value = await getSystemCodeList({ majorCd: 'C0045' });

        // `ordSeq` → `{ minorCd, minorNm }` 매핑을 위한 Map 생성
        const systemCodeMap = new Map(
            systemCodeData.value.list.map(item => [
                Number(item.ordSeq), //  key를 숫자로 변환
                { minorCd: item.minorCd, minorNm: item.minorNm } // value를 객체로 저장
            ])
        );
        let targetId = getTargetId(props.writeYear, props.docNo, props.docSeq, props.docDetailSeq);
        signatureData.value = props.types.map((data, index) => {
            const mappedData = systemCodeMap.get(Number(data.sysCodeOrdSeq)); // key를 숫자로 변환 후 조회
            return {
                hrId: props.isWriter && index === 0 ? userInfoStore.userId : data.hrId, // 첫 번째 항목만 로그인 ID 사용
                hrNm: props.isWriter && index === 0 ? userInfoStore.userName : data.hrNm, // 첫 번째 항목만 로그인 사용자 이름 사용
                // hrId: props.isWriter && data.sysCodeOrdSeq === 1 ? userInfoStore.userId : data.hrId, // 첫 번째 항목만 로그인 ID 사용
                // hrNm: props.isWriter && data.sysCodeOrdSeq === 1 ? userInfoStore.userName : data.hrNm, // 첫 번째 항목만 로그인 사용자 이름 사용
                signature: null,
                targetType: props.targetType,
                targetId: targetId,
                displayName: data.name || mappedData?.minorNm, // `sysCodeOrdSeq` 값에 맞는 `minorNm`
                param: mappedData?.minorCd || null, // `sysCodeOrdSeq` 값에 맞는 `minorCd`
                type: props.type,
                userId: props.isWriter && index === 0 ? userInfoStore.userId : null,
                useYn: props.useYn
                // userId: props.isWriter && data.sysCodeOrdSeq === 1 ? userInfoStore.userId :null,
                // reqMenuId : router.currentRoute.value.meta.menuId,
                // formattedTargetId : getFormattedTargetId(props.targetType, props.writeYear, props.docNo, props.docSeq, props.docDetailSeq),
            };
        });
    };

    //#endregion

    //#region 서명자 삭제
    const removePeople = async (index, cmd) => {
        if (cmd === 'I') {
            // 신규 인 경우, 변경된 서명자 목록 정보 반환
            signatureData.value[index].hrId = null;
            signatureData.value[index].hrNm = null;
            return signatureData.value.filter(row => row.hrId != null);
        } else {
            // 서명자 제거
            await nullUpdateApprovalInfo(signatureData.value[index], false);
            signatureData.value[index].hrId = null;
            signatureData.value[index].hrNm = null;
            signatureData.value[index].signature = null;
            return null; // emit이 필요 없는 경우 null 반환
        }
    };
    //#endregion

    //#region 사용자 팝업 - 서명자 추가/선택
    const selectPeople = async (el, props) => {
        // await getInitSignature(props);
        let remove = null;
        if (signatureIndex.value !== null) {
            if (signatureData.value[signatureIndex.value].hrId) {
                remove = { ...signatureData.value[signatureIndex.value] }; // 객체 복사
            }
            signatureData.value[signatureIndex.value].hrId = el.hrId;
            signatureData.value[signatureIndex.value].hrNm = el.hrNm;
            signatureData.value[signatureIndex.value].hrUserId = el.userId;
            signatureData.value[signatureIndex.value].reqMenuId = router.currentRoute.value.meta.menuId;
            signatureData.value[signatureIndex.value].formattedTargetId = getFormattedTargetId(props.targetType, props.writeYear, props.docNo, props.docSeq, props.docDetailSeq);
            signatureData.value[signatureIndex.value].useYn = props.useYn;
            // signatureData.value[signatureIndex.value].signature = el.signature; // TODO: 추후 백엔드 처리 필요
            if (signatureData.value[signatureIndex.value].reqMenuId === '2241') {
                // 협력업체 등록대장 예외처리
                signatureData.value[signatureIndex.value].reqMenuId = '2242';
            }
            if (props.cmd === 'I') {
                return signatureData.value.filter(row => row.hrId != null);
            } else {
                if (remove) {
                    await nullUpdateApprovalInfo(remove, false);
                }

                signatureData.value.forEach(ds => {
                    if (!ds.displayName) {
                        alertMsg(warn, '[signature] displayName 오류');
                    }
                });

                signatureData.value[signatureIndex.value].cmd = 'U';
                let result = await insertApprovalInfo(signatureData.value[signatureIndex.value], false);
                if (result) {
                    console.log(result);
                    return true;
                }
                return null; // emit이 필요 없는 경우 null 반환
            }
        } else return null;
    };
    //#endregion

    //#region 해당 문서의 서명 정보 조회
    const getInitSignature = async (props, isInit = false) => {
        if (!props.docNo) {
            console.warn('🚨 docNo 값이 없어 함수 실행을 중단합니다.');
            return; // docNo가 없으면 함수 실행 중단
        }
        let targetId = getTargetId(props.writeYear, props.docNo, props.docSeq, props.docDetailSeq);
        let params = {
            targetType: props.targetType,
            targetId: targetId,
            type: props.type
        };
        await searchApprovalInfo(params, false).then(async res => {
            if (props.cmd === 'AUTO') {
                if (res.list.length > 0) {
                    props.cmd = 'U';
                } else {
                    props.cmd = 'I';
                }
            }
            let missingSignatures = []; // DB에 해당 정보가 없는 경우 (Insert)
            let matchedSignatures = []; // DB에 해당 정보가 있지만 인원이 다른 경우 (Update)
            let tempCount = 0;
            // 서명 정보 업데이트 (조회된 데이터와 비교)
            signatureData.value.forEach(ds => {
                // const existingSignature = res.list[index]; // 같은 index 위치 값을 가져옴
                let count = signatureData.value.filter(item => item.param === ds.param).length;
                const existingSignature = res.list.filter(api => ds.param === api.param && ds.type === api.type);

                //signatureData.value.
                if (existingSignature.length > 0) {
                    if (count === 1) {
                        tempCount = 0;
                    }
                    if (existingSignature[tempCount]) {
                        let matchedSignature = existingSignature[tempCount]; // 첫
                        if (matchedSignature.hrId == null && ds.hrId != matchedSignature.hrId) {
                            // DB와 해당 정보가 다를 경우
                            ds.seq = existingSignature[tempCount].seq;
                            matchedSignatures.push(ds);
                        } else {
                            // 기존 데이터가 존재하고 동일한 param과 type이면 값 업데이트
                            ds.hrId = existingSignature[tempCount].hrId;
                            ds.hrNm = existingSignature[tempCount].hrNm;
                            ds.signature = existingSignature[tempCount].signature;
                            ds.reqMenuId = router.currentRoute.value.meta.menuId;
                            ds.formattedTargetId = getFormattedTargetId(props.targetType, props.writeYear, props.docNo, props.docSeq, props.docDetailSeq);
                            ds.type = existingSignature[tempCount].type;
                            ds.param = existingSignature[tempCount].param;
                            ds.seq = existingSignature[tempCount].seq;
                            if (ds.reqMenuId === '2241') {
                                // 협력업체 등록대장 예외처리
                                ds.reqMenuId = '2242';
                            }
                            tempCount++;
                        }
                    }
                } else {
                    // DB에 해당 정보가 없을 경우
                    // if (ds.hrId && ds.param && !res.list.some((resData) => ds.param === resData.param && ds.type === resData.type)) {
                    missingSignatures.push(ds); //  기존에 없는 서명 데이터가 있는 경우 따로 저장 필요!
                    // }
                }
            });

            // 저장이 필요한 서명이 있을 경우에만 setApprovalInfo 실행
            if (missingSignatures.length > 0 && isInit) {
                console.log('신규 서명이 발견됨! `setApprovalInfo` 실행');
                await setApprovalInfo('I', props.targetType, props.writeYear, props.docNo, props.docSeq, props.docDetailSeq, true, missingSignatures, props.useYn);
            }
            if (matchedSignatures.length > 0 && isInit) {
                console.log('서명 인원이 다름! `setApprovalInfo` 실행');
                await setApprovalInfo('U', props.targetType, props.writeYear, props.docNo, props.docSeq, props.docDetailSeq, true, matchedSignatures, props.useYn);
            }
        });
    };
    //#endregion

    //#region 서명자 목록 저장
    const setApprovalInfo = async (cmd, targetType, writeYear, docNo, docSeq, docDetailSeq, isTask, missingSignatures, useYn = 'Y') => {
        try {
            if (!writeYear || !docNo) {
                console.warn('⚠️ 누락된 필수 값이 있음:', { writeYear, docNo });
                return { success: false };
            }
            if (cmd === 'I') {
                if (docNo) {
                    signatureData.value.forEach(ds => {
                        ds.targetId = getTargetId(writeYear, docNo, docSeq, docDetailSeq);
                        ds.formattedTargetId = getFormattedTargetId(targetType, writeYear, docNo, docSeq, docDetailSeq, isTask);
                        ds.reqMenuId = router.currentRoute.value.meta.menuId;
                        ds.hrUserId = userInfoStore.userId;
                        ds.useYn = useYn;
                        ds.cmd = 'I';
                        if (ds.reqMenuId === '2241') {
                            // 협력업체 등록대장 예외처리
                            ds.reqMenuId = '2242';
                        }
                    });

                    const result = await insertList(missingSignatures ? missingSignatures : signatureData.value);
                    return { success: result };
                }
            } else if (cmd === 'U' && missingSignatures) {
                missingSignatures.forEach(ds => {
                    ds.targetId = getTargetId(writeYear, docNo, docSeq, docDetailSeq);
                    ds.formattedTargetId = getFormattedTargetId(targetType, writeYear, docNo, docSeq, docDetailSeq, isTask);
                    ds.reqMenuId = router.currentRoute.value.meta.menuId;
                    ds.hrUserId = userInfoStore.userId;
                    ds.useYn = useYn;
                    if (ds.reqMenuId === '2241') {
                        // 협력업체 등록대장 예외처리
                        ds.reqMenuId = '2242';
                    }
                });
                const result = await insertList(missingSignatures);
                return { success: result };
            }

            return { success: false };
        } catch (error) {
            console.error('서명 저장 중 오류 발생:', error);
            return { success: false };
        }
    };
    //#endregion

    const insertList = async param => {
        try {
            signatureData.value.forEach(ds => {
                if (!ds.displayName) {
                    alertMsg('warn', '[signature] displayName 오류');
                }
            });

            let result = await insertApprovalInfoList(param, false);
            if (result) {
                //
                // param 배열을 순회하면서 cmd 값을 "U"로 설정
                param.forEach(item => {
                    item.cmd = 'U'; // cmd 값을 "U"로 변경

                    // result 배열에서 해당 param과 매칭되는 seq 값을 찾기
                    let matchingResult = result.result.find(resItem => resItem.param === item.param && resItem.type === item.type);

                    // seq 값이 존재하면 param 객체에 추가
                    if (matchingResult && matchingResult.seq !== undefined) {
                        item.seq = matchingResult.seq;
                    }
                });
            }
            console.log('# 서명 저장완료');
            return true; // 성공 시 true 반환
        } catch (error) {
            console.log('# 서명 저장실패', error);
            return false; // 실패 시 false 반환
        }
    };

    /* 마스터 문서의 useYn이 변경되었을 때 Task의 use_yn 값을 변경시켜줌 (각각 개별 호출) */
    const updateTaskUseYn = async (targetType, writeYear, docNo, docSeq, docDetailSeq, useYn) => {
        const param = {
            useYn: useYn,
            formattedTargetId: getFormattedTargetId(targetType, writeYear, docNo, docSeq, docDetailSeq)
        };
        console.log('✅ TASK 변경 처리를 진행합니다:', { writeYear, docNo });
        console.log('##param', param);

        return new Promise(resolve => {
            updateTaskUseYnApi(param, false)
                .then(res => {
                    resolve(res.success);
                })
                .catch(() => {
                    resolve(false);
                });
        });
    };

    /* 목록에서 삭제할 경우 강제로 값 업데이트 */
    const forcedUpdateTaskUseYn = async (useYn, targetType, writeYear, docNo, docSeq, docDetailSeq) => {
        const param = {
            useYn: useYn,
            formattedTargetId: getFormattedTargetId(targetType, writeYear, docNo, docSeq, docDetailSeq)
        };
        console.log('✅ TASK 변경 처리를 진행합니다:', { writeYear, docNo });
        console.log('##param', param);

        return new Promise(resolve => {
            updateTaskUseYnApi(param, false)
                .then(res => {
                    resolve(res.success);
                    initialUseYn.value = useYn;
                })
                .catch(() => {
                    resolve(false);
                });
        });
    };

    //#region 서명 추가
    const addSign = index => {
        signatureIndex.value = index;
        const signatureItem = signatureData.value[signatureIndex.value];
        if (userInfoStore.signature) {
            signatureItem.signature = userInfoStore.signature;
            updateApprovalInfoSign(signatureItem, false);
            return true;
        } else {
            return false;
        }
    };
    //#endregion

    //#region 서명 삭제
    const removeSign = index => {
        signatureData.value[index].signature = null;
        // 서명자 서명 등록 취소
        updateApprovalInfoSignCancel(signatureData.value[index], false);
    };
    //#endregion

    //#region 특정 서명 정보만 변경 하고 싶을때
    const setPeople = async (index, hrId, hrNm, props) => {
        await removePeople(index, props.cmd);
        if (signatureData.value[index]) {
            signatureData.value[index].hrId = hrId;
            signatureData.value[index].hrNm = hrNm;
        }

        signatureIndex.value = index;

        const success = await selectPeople(
            { hrId, hrNm }, // userId는 알 수 없으므로 기본값 null 설정
            props
        );
        return success;
    };
    //#endregion

    //#region 서명 저장
    const saveSign = async signaturePad => {
        const { isEmpty, data } = signaturePad.value.saveSignature();

        if (!isEmpty) {
            signatureData.value[signatureIndex.value].signature = data;
            await nullUpdateApprovalInfo(signatureData.value[signatureIndex.value], false);
            signatureData.value[signatureIndex.value].cmd = 'U';
            // 사용자 서명 등록
            await insertApprovalInfo(signatureData.value[signatureIndex.value], false).then(() => {
                // 서명자 서명 등록
                updateApprovalInfoSign(signatureData.value[signatureIndex.value], false);
            });
        }
    };
    //#endregion

    return reactive({
        // (조회) 시스템 코드
        getSystemCodeList,
        // (조회) 해당 문서의 서명 정보
        getInitSignature,
        // 서명 정보
        signatureData,
        // 서명 저장
        saveSign,
        // 서명인원 삭제
        removePeople,
        // 서명자 위치에 넣기
        setPeople,
        // 서명 추가
        addSign,
        // 서명 삭제
        removeSign,
        // [인원 팝업]에서 서명자 선택
        selectPeople,
        // 저장 cmd, targetId, formattedTargetId
        setApprovalInfo,
        // 처음 처리 작업
        init,
        // 서명위치 인덱스
        signatureIndex,
        // 마스터 문서의 useYn이 변경되었을 때 Task의 use_yn 값을 변경시켜줌 (각각 개별 호출)
        updateTaskUseYn,
        // 마스터 문서의 useYn이 변경되었을 때 Task의 use_yn 값을 변경시켜줌 (목록에서 삭제 시 호출)
        forcedUpdateTaskUseYn,
        // 초기 useYn 값
        initialUseYn
    });
};
