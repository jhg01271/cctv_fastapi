<template>
    <div class="df jcfe">
        <!-- 우측 상단 서명 리스트 -->
        <div class="approval w100p maxw45rem">
            <div class="row flex">
                <!-- class="grid-container"
                :style="{
                    display: 'grid',
                    gridTemplateColumns: `repeat(${Math.min(props.types.length, 6)}, 1fr)`,
                    width: '100%'
                    grid-item
                }" -->
                <div :class="'grid12-4 fg1'" v-for="(item, index) in props.types" :key="index">
                    <label for class="db fs1-5rem c9EA1A6 bcF8F9FB tac py8px">{{ item.name }}</label>
                    <hr class="w100p h1px bcE1E6ED mt0 mb0" />
                    <div class="sign df aic jcc h7-3rem px1rem">
                        <div class="segment pr">
                            <!-- 신규 입력(cmd = 'I') 인 경우 서명 등록은 불가 -->
                            <!-- 서명이 등록 되어 있는 경우, 서명 표시, 서명 등록 취소 버튼 표시-->
                            <div class="segment pr" v-if="signatureStore.signatureData[index]?.signature && props.cmd !== 'I'">
                                <img :src="signatureStore.signatureData[index].signature" alt />
                                <button v-if="signatureStore.signatureData[index].hrId === userInfoStore.userId" type="button" class="pa neg-r1rem neg-t1rem w18px h18px lh18px br18px bcFFD7D6 init m" @click="removeSign(index)">
                                    <svg xmlns="http://www.w3.org/2000/svg" width="8" height="8" viewBox="0 0 8 8" fill="none">
                                        <path d="M7 1L1 6.99999M7 7L1 1.00001" stroke="#FF3534" stroke-linecap="round" />
                                    </svg>
                                </button>
                            </div>
                            <!-- 서명자는 지정되어 있으나 서명이 등록 되어 있지 않은 경우 현재 사용자와 서명자의 Hr_id가 동일하다면, 서명 추가 버튼 표시-->
                            <div class="segment pr df jcc aic" v-else-if="signatureStore.signatureData[index]?.hrId && signatureStore.signatureData[index].hrId === userInfoStore.userId && props.cmd !== 'I'" @click="addSign(index)">
                                <svg xmlns="http://www.w3.org/2000/svg" width="34" height="34" viewBox="0 0 34 34" fill="none">
                                    <path d="M23.375 12.9431C20.7994 13.8016 17.3654 10.3676 18.2239 7.79199M19.282 6.73382L13.7332 12.2827C11.3209 14.695 9.60957 17.7175 8.78216 21.0272L8.51404 22.0996C8.4305 22.4338 8.73319 22.7365 9.06736 22.653L10.1398 22.3848C13.4495 21.5574 16.472 19.8461 18.8843 17.4338L24.4332 11.8849C25.1162 11.2019 25.5 10.2754 25.5 9.30938C25.5 7.29775 23.8692 5.66699 21.8576 5.66699C20.8916 5.66699 19.9651 6.05074 19.282 6.73382Z" stroke="black" stroke-width="1.5" />
                                    <path d="M19.2821 6.73382C19.9652 6.05074 20.8916 5.66699 21.8577 5.66699C23.8693 5.66699 25.5001 7.29775 25.5001 9.30938C25.5001 10.2754 25.1163 11.2019 24.4332 11.8849L23.3751 12.9431C20.7995 13.8016 17.3654 10.3676 18.2239 7.79199L19.2821 6.73382Z" stroke="#3248F6" stroke-width="1.5" />
                                    <path d="M26.9167 28.333H7.08337" stroke="#3248F6" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
                                </svg>
                            </div>
                            <!-- 서명자가 지정되어 있지 않은 경우 -->
                            <div class="segment pr df jcc aic" v-else>
                                <svg xmlns="http://www.w3.org/2000/svg" width="34" height="34" viewBox="0 0 34 34" fill="none">
                                    <g opacity="0.5">
                                        <rect width="34" height="34" fill="white" />
                                        <path d="M23.375 12.9431C20.7994 13.8016 17.3654 10.3676 18.2239 7.79199M19.282 6.73382L13.7332 12.2827C11.3209 14.695 9.60957 17.7175 8.78216 21.0272L8.51404 22.0996C8.4305 22.4338 8.73319 22.7365 9.06736 22.653L10.1398 22.3848C13.4495 21.5574 16.472 19.8461 18.8843 17.4338L24.4332 11.8849C25.1162 11.2019 25.5 10.2754 25.5 9.30938C25.5 7.29775 23.8692 5.66699 21.8576 5.66699C20.8916 5.66699 19.9651 6.05074 19.282 6.73382Z" stroke="#9EA1A6" stroke-width="1.5" />
                                        <path d="M19.2821 6.73382C19.9652 6.05074 20.8916 5.66699 21.8577 5.66699C23.8693 5.66699 25.5001 7.29775 25.5001 9.30938C25.5001 10.2754 25.1163 11.2019 24.4332 11.8849L23.3751 12.9431C20.7995 13.8016 17.3654 10.3676 18.2239 7.79199L19.2821 6.73382Z" stroke="#9EA1A6" stroke-width="1.5" />
                                        <path d="M26.9167 28.333H7.08337" stroke="#9EA1A6" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
                                    </g>
                                </svg>
                            </div>
                        </div>
                    </div>
                    <hr class="w100p h1px bcE1E6ED mt0 mb0" />
                    <div class="df aic">
                        <!-- 서명자 제거 버튼 -->
                        <div class="chips">
                            <ul class="init m">
                                <li class="wsn" v-if="signatureStore.signatureData[index]?.hrId">
                                    <span class="mr8px fs1-5rem lg-fs1-4rem lg-mr4px">{{ signatureStore.signatureData[index].hrNm }}</span>
                                    <button type="button" @click="removePeople(index, props.cmd)" v-if="(index != 0 || item.hrId == null) && !item.writerOnly && router.currentRoute.value?.meta?.savAh == 'Y'">
                                        <svg width="8" height="8" viewBox="0 0 8 8" fill="none" xmlns="http://www.w3.org/2000/svg">
                                            <path d="M7 1L1 6.99999M7 7L1 1.00001" stroke="#FF3534" stroke-linecap="round" />
                                        </svg>
                                    </button>
                                </li>
                            </ul>
                        </div>
                        <!-- 서명자 지정 버튼 -->
                        <button type="button" @click="addPeople(index)" v-if="(index != 0 || item.hrId == null) && !item.writerOnly && router.currentRoute.value?.meta?.savAh == 'Y'">
                            <img src="/assets/img/common/icon_search.svg" alt />
                        </button>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <!-- 서명자 선택을 위한 인원 Popup -->
    <teleport to="body">
        <i-PopupDialog ref="peoplePopup" v-if="!changeSelectPopup">
            <div class="contents w500px md-w100p">
                <selectUser :single="true" @selected="selectPeople" @close="closePopup"></selectUser>
            </div>
        </i-PopupDialog>
        <i-PopupDialog ref="peoplePopup" v-if="changeSelectPopup">
            <!-- 단일 그리드 -->
            <div class="contents w500px md-w100p">
                <base-select-popup :title="selectPopupTitle" filterKey="hrNm" useYnKey="useYn" :excluded-value="'N'" :single="true" :fetch-data="fetchData" :selectedIdList="signatureStore.signatureData.map(el => el.hrId).filter(Boolean)" uniqueKey="hrId" @close="selectPeopleWithName" :btnInfo="{ close: true, apply: true }" />
            </div>
        </i-PopupDialog>
    </teleport>
    <!-- 사용자의 서명 등록을 위한 Popup -->
    <teleport to="body">
        <i-PopupDialog ref="signPopup" class>
            <div class="contents w45rem md-w100p">
                <h3>{{ '서명 등록' }}</h3>
                <VueSignaturePad v-if="imgSign == null" class="bd1pxsolidE1E6ED" id="signature" width="100%" height="156px" ref="signaturePad" />
                <div v-else id="signature" class="w100p h156px oh bd1pxsolidE1E6ED df jcc">
                    <img class="w100p h100p db" :src="imgSign" alt="서명 이미지" style="object-fit: contain" />
                </div>
                <div class="form ui df jcsb mt2-5rem">
                    <div class="tal">
                        <button type="button" v-button class="btn w74px radius delete" @click="clearSign">
                            <span>{{ t('btnClear') }}</span>
                        </button>
                    </div>
                    <div class="tar">
                        <button type="button" class="btn w74px radius mr8px active" v-button @click="loadSign">
                            <span>{{ t('load') }}</span>
                        </button>
                        <button type="button" class="btn w74px radius mr8px active" v-button @click="saveSign">
                            <span>{{ t('apply') }}</span>
                        </button>
                        <button type="button" v-button class="btn w74px radius bright-grey" @click="closePopup">
                            <span>{{ t('close') }}</span>
                        </button>
                    </div>
                </div>
            </div>
        </i-PopupDialog>
        <!-- 이미지 서명 (직인 등) 업로드를 위함 -->
        <div>
            <input ref="fileInput" type="file" @change="fileChanged" accept=".png" class="dn" />
        </div>
    </teleport>
</template>
<script setup>
import BaseView from '@/components/base/BaseView';
import selectUser from '@/views/base/member/SelectUser/Index.vue';
import BaseSelectPopup from '@/views/system/base/popup/BaseSelectPopup.vue';
import { defineExpose, defineProps } from 'vue';
import { useUserInfoStore } from '@/stores/user.js';
import router from '@/router';
const userInfoStore = useUserInfoStore(); // 현재 사용자 정보
import { createSignatureStore } from '@/stores/signature';
const signatureStore = createSignatureStore(); // 컴포넌트마다 고유한 Store 생성
import { updateApprovalInfoSign, updateUserSignature } from '@/api/base/common/utils';
const { t, ref, onMounted, watch, openLoading, endLoading } = BaseView();
import _ from 'lodash';
// const SIGNATURE_ENUM = Object.freeze({
//     WRITER: { code: 'writer', order: 1 }, // 작성 (1순위)
//     REVIEWER: { code: 'reviewer', order: 2 }, // 검토 (2순위)
//     APPROVER: { code: 'approver', order: 3 }, // 승인 (3순위)
//     AUDITOR: { code: 'auditor', order: 4 } // 감사 (4순위, 추가 가능)
// });
// const SIGNATURE_MAP = new Map(SIGNATURE_ENUM.map(item => [item.code, item.order]));

const props = defineProps({
    cmd: {
        type: String,
        default: 'I' // 신규문서 I, 기존문서 U
    },
    // 구분 타입 보통은 여기 타입 넣어서 사용하지 않는걸 권장
    type: {
        type: String,
        default: () => 'sign'
    },
    types: {
        type: Array,
        default: () => [
            {
                name: '작성', // UI에 표시될 이름은 여거 작성해주세요
                sysCodeOrdSeq: 1 //시스템 코드의 C0045 코드의 ordSeq 번호와 맵핑 됩니다.
            },
            {
                name: '검토', // UI에 표시될 이름은 여거 작성해주세요
                sysCodeOrdSeq: 2 //시스템 코드의 C0045 코드의 ordSeq 번호와 맵핑 됩니다.
            },
            {
                name: '승인', // UI에 표시될 이름은 여거 작성해주세요
                sysCodeOrdSeq: 3 //시스템 코드의 C0045 코드의 ordSeq 번호와 맵핑 됩니다.
            }
        ]
    },
    isWriter: {
        // 첫번째 서명자 = 작성자로 하는 경우, 자동으로 정보를 가져와 설정
        type: Boolean,
        default: true
    },
    targetType: {
        // doc_type
        type: String,
        default: null
    },
    writeYear: {
        type: String,
        default: null
    },
    docNo: {
        type: String,
        default: null
    },
    docSeq: {
        type: String,
        default: null
    },
    docDetailSeq: {
        type: String,
        default: null
    },
    useYn: {
        type: String,
        default: ''
    },
    selectPopupTitle: {
        // base-select-popup 타이틀
        type: String,
        default: null
    },
    changeSelectPopup: {
        // base-select-popup 사용 여부(true = 사용, false = selectUser 사용)
        type: Boolean,
        default: false
    },
    fetchData: {
        // base-select-popup 데이터 함수 호출
        type: Function,
        required: false
    }

    // fetch-data props 추가, true/false props 추가
});

watch(
    () => props.useYn,
    async (newVal, oldVal) => {
        if (oldVal === '') {
            // 최초
            initialUseYn.value = _.cloneDeep(newVal); // 후에 업데이트 분기를 위해 처음 생성될때 useYn 값 저장
        } else if (signatureStore.initialUseYn !== '') {
            //삭제했을 경우 값 갱신
            initialUseYn.value = signatureStore.initialUseYn;
        }
    }
);

const emit = defineEmits(['signatureList']);

// const signatureIndex = ref();
// 인원 Popup
const peoplePopup = ref();
// 서명 생성용 Popup
const signPopup = ref();
// 서명 생성용 PAD
const signaturePad = ref();
// 생성 시 useYn값
const initialUseYn = ref('');
// types 변경에 따른 초기화
onMounted(async () => {
    try {
        validateTypes(props.types); // props.types 검증
        await signatureStore.init(props);
        await signatureStore.getInitSignature(props, true);
        initialUseYn.value = _.cloneDeep(props.useYn); // 후에 업데이트 분기를 위해 처음 생성될때 useYn 값 저장
    } catch (error) {
        console.error(error.message);
        alert(error.message); // 사용자에게 알림
    }
});

//#region 서명자 선택 popup 열기
const addPeople = index => {
    signatureStore.signatureIndex = index;
    peoplePopup.value.onOpen();
};
//#endregion

//#region 서명자 목록 저장
const setApprovalInfo = async (docType, writeYear, docNo, docSeq, docDetailSeq, isTask = true) => {
    try {
        if (!writeYear || !docNo) {
            console.warn('⚠️ 누락된 필수 값이 있음:', { writeYear, docNo });
            return false;
        }

        // 순차적 실행을 위해 Promise 반환
        return new Promise(async resolve => {
            const result = await signatureStore.setApprovalInfo(props.cmd, props.targetType ? props.targetType : docType, writeYear, docNo, docSeq, docDetailSeq, isTask, null, props.useYn);
            if (result?.success) {
                resolve(true);
            } else {
                resolve(false);
            }
        });
    } catch (error) {
        console.error('서명 저장 중 오류 발생:', error);
        return false;
    }
};
//#endregion

/* 마스터 문서의 useYn이 변경되었을 때 Task의 use_yn 값을 변경시켜줌 (각각 개별 호출) */
const updateTaskUseYn = async (docType, writeYear, docNo, docSeq, docDetailSeq) => {
    console.log('### i-signature / update task use yn');
    console.log('변경 전 : ', initialUseYn.value);
    console.log('변경 후 : ', props.useYn);
    // 업데이트할 필요가 없으면 return하여 리소스를 줄임
    if (props.cmd === 'I' || initialUseYn.value === props.useYn) return;
    return new Promise(async resolve => {
        const isSucceed = await signatureStore.updateTaskUseYn(props.targetType ? props.targetType : docType, writeYear, docNo, docSeq, docDetailSeq, props.useYn);
        if (isSucceed) {
            initialUseYn.value = _.cloneDeep(props.useYn);
            resolve(true);
        } else {
            resolve(false);
        }
    });
};
//#region 'props.types`가 올바르게 설정되었는지 검증하는 함수
const validateTypes = types => {
    const requiredKeys = ['name', 'sysCodeOrdSeq'];

    types.forEach((type, index) => {
        requiredKeys.forEach(key => {
            if (!(key in type)) {
                throw new Error(`🚨 오류: types[${index}] 객체에 '${key}' 속성이 없습니다!`);
            }
        });
    });
};

//#endregion

//#region 인원 팝업에 서명자 선택
const selectPeople = el => {
    const updatedSignatureList = signatureStore.selectPeople(el, props);

    // cmd가 'I'인 경우에만 emit 실행
    if (updatedSignatureList !== null) {
        emit('signatureList', updatedSignatureList);
    }

    peoplePopup.value.onClose();
};

// 기존 selectPeople 이용해서 base-select-popup의 서명 추가
const selectPeopleWithName = selectedItems => {
    if (selectedItems.length > 0) {
        const selectedItem = selectedItems[0];
        const fullName = selectedItem.hrNm;

        // 괄호 및 괄호 안 제거
        const noParentheses = fullName.replace(/\([^)]*\)/g, '').trim();

        // 특수문자 기준 마지막 부분 추출 (없으면 전체 그대로)
        const matched = noParentheses.match(/.*[^a-zA-Z가-힣0-9]+([a-zA-Z가-힣0-9]+)\s*$/);
        const cleanName = matched ? matched[1] : noParentheses;

        const updatedEl = {
            ...selectedItem,
            hrNm: cleanName,
            name: cleanName,
            hrUserId: selectedItem.userId
        };

        selectPeople(updatedEl);
    }
    peoplePopup.value.onClose();
};

//#endregion

//#region 서명자 제거
const removePeople = index => {
    const updatedSignatureList = signatureStore.removePeople(index, props.cmd);

    // cmd가 'I'인 경우에만 emit 실행
    if (updatedSignatureList !== null) {
        emit('signatureList', updatedSignatureList);
    }
};
//#endregion

//#region 서명 추가
const addSign = index => {
    if (!signatureStore.addSign(index, props)) {
        // 사용자 정보에 서명이 없다면
        signPopup.value.onOpen();
    }
};
//#endregion

//#region 서명 정보 등록(저장)하기
const imgSign = ref(null);
const clearSign = () => {
    if (imgSign.value == null) {
        // 수기 서명인 경우
        signaturePad.value.clearSignature();
    } else {
        // 이미지 서명인 경우
        imgSign.value = null;
    }
};
const fileInput = ref(null);
const loadSign = () => {
    fileInput.value.click();
};
const fileChanged = e => {
    const file = e.target.files[0];
    if (!file) return;

    const reader = new FileReader();

    reader.onload = () => {
        const base64String = reader.result;
        console.log('Base64 결과 -> ', base64String.length);
        imgSign.value = base64String;
        // 필요한 로직: 예) 미리보기, API 전송 등
    };

    reader.onerror = error => {
        console.log('파일 읽기 에러:', error);
    };

    e.target.value = null; // 이벤트를 다시 받을 수 있도록 초기화
    reader.readAsDataURL(file); // Base64 문자열로 읽기
};

const saveSign = async () => {
    let signIsEmpty = true;
    let signData = '';
    if (imgSign.value === null) {
        const { isEmpty, data } = signaturePad.value.saveSignature();
        signIsEmpty = isEmpty;
        signData = data;
    } else {
        signIsEmpty = false;
        signData = _.cloneDeep(imgSign.value);
    }
    // 서명이 저장 되었다면
    if (signIsEmpty == false) {
        signatureStore.signatureData[signatureStore.signatureIndex].signature = signData;
        signatureStore.signatureData[signatureStore.signatureIndex].cmd = 'U';
        openLoading();
        await updateUserSignature({ userId: userInfoStore.userId, signature: signData }, false)
            .then(async res => {
                if (res) {
                    await updateApprovalInfoSign(signatureStore.signatureData[signatureStore.signatureIndex], false)
                        .then(() => {
                            userInfoStore.signature = signatureStore.signatureData[signatureStore.signatureIndex].signature;
                            console.log('# 서명 저장완료');
                            endLoading();
                        })
                        .catch(() => {
                            console.log('# 서명 저장실패');
                            signPopup.value.onClose();
                            endLoading();
                        });
                }
            })
            .finally(() => {
                signPopup.value.onClose();
            });
    }
};
//#endregion

//#region 서명 정보 등록 취소(제거) 하기
const removeSign = index => {
    signatureStore.removeSign(index);
};
//#endregion

const getSignatureData = () => {
    return signatureStore.signatureData;
};

// 팝업 닫기
const closePopup = () => {
    peoplePopup.value.onClose();
    signPopup.value.onClose();
};

// 특정 위치의 서명 인원 변경을 위해 사용
const setHrInfo = async (index, hrId, hrNm) => {
    const success = await signatureStore.setPeople(index, hrId, hrNm, props);
    return success;
};
const Search = async () => {
    await signatureStore.init(props);
    await signatureStore.getInitSignature(props);
};

defineExpose({
    setApprovalInfo,
    updateTaskUseYn,
    getSignatureData,
    setHrInfo,
    // 조회 버튼
    Search,
    removeSign,
    removePeople
});
</script>
