<template>
    <!-- validation 체크를 위해 i-chips 클래스 적용 -->
    <div class="i-chips" v-bind="getDivAttributes()">
        <OverlayScrollbarsComponent
            class="maxh26rem"
            :options="{
                scrollbars: {
                    x: 'hidden',
                    y: 'visible'
                }
            }"
        >
            <div :class="`${userList.filter(u => u.cmd !== 'D').length === 0 && single ? 'h4-4rem df aic bcffffff bd1pxsolide1e6ed br4px jcfe' : ''}`">
                <div class="item w100p" v-if="userList.filter(user => user.cmd !== 'D').length > 0">
                    <div :class="`${props.single ? 'single' : 'multi gap8px '}`">
                        <div class="w100p df gap8px df aic h4-4rem df aic bcffffff bd1pxsolide1e6ed br4px px8px" v-for="item in props.single ? userList.filter(user => user.cmd === 'I' || user.cmd === 'U').slice(0, 1) : userList.filter(user => (user.cmd === 'I' || user.cmd === 'U') && user.visible != false)" :key="item.hrId">
                            <div class="df aic gap8px">
                                <i class="w2-4rem h2-4rem br2px oh shrink0" v-if="item.img">
                                    <img :src="item.img" alt class="w100p h100p ofc" />
                                </i>
                                <i class="w2-4rem h2-4rem br2px oh shrink0" v-else>
                                    <img src="/assets/img/common/icon_no_user.svg" />
                                </i>
                                <span class="wsn fs1-4rem lh2">{{ item.hrNm }}</span>
                                <button class="df" type="button" @click="removeChip(item.hrId)" v-if="!props.signOnly">
                                    <svg class="shrink0" xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none">
                                        <path d="M17 7L7 17M17 17L7 7.00001" stroke="#FF3534" stroke-linecap="round" />
                                    </svg>
                                </button>
                            </div>
                            <hr class="w1px h50p mx5px mya bdl1pxsolide1e6ed" />
                            <div class="w50p df jcsb aic">
                                <div v-if="item.signature" class="df aic jcsb fg1 wsn oh">
                                    <span v-if="showSignTime" class="w35px tac fs13px" v-html="formattedSignedTime(item.updatedAt)"></span>
                                    <span v-else class="fs13px">서명</span>

                                    <!-- 사인 이미지 사이즈 확장 -->
                                    <div class="oh">
                                        <img :src="item.signature" alt class="h100p maxh4rem maxw100p h4-4rem ofc" />
                                    </div>

                                    <!-- 기존 사인 이미지 -->
                                    <!-- <img :src="item.signature" alt class="maxw50px es-w40px" style="object-fit: contain" /> -->
                                    <button type="button" class="df jcc aic shrink0" @click="removeSignature(item)" v-show="item.signature">
                                        <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none">
                                            <path d="M17 7L7 17M17 17L7 7.00001" stroke="#FF3534" stroke-linecap="round" />
                                        </svg>
                                    </button>
                                </div>
                                <button type="button" v-else-if="item.hrId && item.hrId == userInfoStore.userId && props.cmd != 'I' && item.cmd != 'I'" class="h100p df aic gap3rem wsn cp jcsb" @click="addSign(item)">
                                    <span class="fs13px">서명</span>
                                    <svg class="shrink0" xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none">
                                        <path d="M16.5 9.13608C14.682 9.7421 12.2579 7.31804 12.8639 5.5M13.6109 4.75306L9.69403 8.66988C7.99123 10.3727 6.78322 12.5063 6.19917 14.8425L6.00991 15.5995C5.95094 15.8354 6.1646 16.0491 6.40049 15.9901L7.15752 15.8008C9.49375 15.2168 11.6273 14.0088 13.3301 12.306L17.2469 8.38914C17.7291 7.90697 18 7.253 18 6.5711C18 5.15112 16.8489 4 15.4289 4C14.747 4 14.093 4.27088 13.6109 4.75306Z" stroke="black" stroke-width="1.5" />
                                        <path d="M13.6114 4.75306C14.0936 4.27088 14.7475 4 15.4294 4C16.8494 4 18.0005 5.15112 18.0005 6.5711C18.0005 7.253 17.7296 7.90697 17.2475 8.38914L16.5005 9.13609C14.6825 9.7421 12.2584 7.31804 12.8644 5.5L13.6114 4.75306Z" stroke="#3248F6" stroke-width="1.5" />
                                        <path d="M19 20H5" stroke="#3248F6" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
                                    </svg>
                                </button>

                                <div v-else class="df aic gap3rem fg1 wsn es-gap5rem us-gap3rem">
                                    <span class="fs13px">서명</span>
                                    <svg class="shrink0" xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none">
                                        <path d="M16.5 9.13608C14.682 9.7421 12.2579 7.31804 12.8639 5.5M13.6109 4.75306L9.69403 8.66988C7.99123 10.3727 6.78322 12.5063 6.19917 14.8425L6.00991 15.5995C5.95094 15.8354 6.1646 16.0491 6.40049 15.9901L7.15752 15.8008C9.49375 15.2168 11.6273 14.0088 13.3301 12.306L17.2469 8.38914C17.7291 7.90697 18 7.253 18 6.5711C18 5.15112 16.8489 4 15.4289 4C14.747 4 14.093 4.27088 13.6109 4.75306Z" stroke="#9EA1A6" stroke-width="1.5" />
                                        <path d="M13.6114 4.75306C14.0936 4.27088 14.7475 4 15.4294 4C16.8494 4 18.0005 5.15112 18.0005 6.5711C18.0005 7.253 17.7296 7.90697 17.2475 8.38914L16.5005 9.13609C14.6825 9.7421 12.2584 7.31804 12.8644 5.5L13.6114 4.75306Z" stroke="#9EA1A6" stroke-width="1.5" />
                                        <path d="M19 20H5" stroke="#9EA1A6" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
                                    </svg>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- 단일선택에서 유저가 없을경우 출력되는 검색 버튼 (모달 추가 작업 필요) -->
                <button type="button" class="w4-4rem h4-4rem df jcc aic" v-if="props.single && userList.filter(u => u.cmd !== 'D').length === 0" @click="addPeople">
                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none">
                        <path d="M11 18C14.866 18 18 14.866 18 11C18 7.13402 14.866 4 11 4C7.13402 4 4 7.13402 4 11C4 14.866 7.13402 18 11 18Z" stroke="black" stroke-linejoin="round" />
                        <path d="M16 16L19 19" stroke="black" stroke-linecap="round" stroke-linejoin="round" />
                    </svg>
                </button>
            </div>
        </OverlayScrollbarsComponent>
    </div>
    <!-- 서명이 없는 사용자가 서명하려 할 때 생성되는 팝업 -->
    <!-- 서명패드가 생기고, 서명 저장과 동시에 서명데이터 삽입 -->
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
import { defineProps } from 'vue';
const { router } = BaseView();
import { useUserInfoStore } from '@/stores/user';

import { deleteApprovalInfo, insertApprovalInfo, insertApprovalInfoList, nullUpdateApprovalInfo, updateTaskUseYnApi, searchApprovalInfo, updateApprovalInfoSign, updateApprovalInfoSignCancel, updateUserSignature } from '@/api/base/common/utils';
const userInfoStore = useUserInfoStore();
import { useSelectUserStore } from '@/views/base/member/SelectUser/SelectUser';
import { loadFileFromServer } from '@/utils/iFileLoader';
import { getFormattedTargetId, getTargetId } from '@/utils/documentUtils.js';
import BaseView from '@/components/base/BaseView.js';
import { getSystemCode } from '@/stores/safewiz/dataset/api/datasetApi.js';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';

import { createSignatureStore } from '@/stores/signature';
const signatureStore = createSignatureStore(); // 컴포넌트마다 고유한 Store 생성

import _ from 'lodash';
const { openLoading, endLoading, ref, nextTick, computed, onMounted, watch, t, getCompId } = BaseView();

const selectUserStore = useSelectUserStore();

const emit = defineEmits(['popup', 'chipRemoved']);

const div = ref();
const signPopup = ref();
const addSign = item => {
    div.value = item;
    if (userInfoStore.signature) {
        item.signature = userInfoStore.signature;
        updateApprovalInfoSign(item, false)
            .then(() => {
                console.log('# 서명 저장완료');
            })
            .catch(() => {
                console.log('# 서명 저장실패');
            });
    } else {
        // console.log(item);
        signPopup.value.onOpen();
    }
};
const props = defineProps({
    chips: {
        type: Array,
        default: () => []
    },
    // 구분 타입 보통은 여기 타입 넣어서 사용하지 않는걸 권장
    type: {
        type: String,
        default: () => 'hr'
    },
    targetType: {
        type: String,
        default: ''
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
    cmd: {
        type: String,
        default: 'I' // 신규문서 I, 기존문서 U
    },
    single: {
        type: Boolean,
        default: false
    },
    signOnly: {
        type: Boolean,
        default: false
    },
    required: {
        // 추가
        type: Boolean,
        default: false
    },
    // hr Chips 마다 결재 순서가 빌요할때 여기 번호를 넣어주면 된다 숫자가 클수록 앞에 번호 이후 결재 (시스템 코드 영향 받음)
    sysCodeOrdSeq: {
        type: Number,
        default: 1
    },
    showSignTime: {
        type: Boolean,
        default: false
    }
});

// 생성 시 useYn값
const initialUseYn = ref('');
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

const getDivAttributes = () => {
    return {
        id: chipsId.value,
        tabindex: 0,
        'data-chips': formatChips(),
        'data-required': props.required
    };
};
// chips 데이터 변환 로직 함수
const formatChips = () => {
    if (Array.isArray(userList.value) && userList.value.length > 0 && userList.value.some(chip => chip.hrId && chip.hrNm)) {
        return JSON.stringify(userList?.value?.map(el => ({ cmd: el.cmd })));
    }
    return null;
};
// 동적 ID 생성
const chipsId = ref(null);

const generateUUID = () => {
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (char) {
        const random = (Math.random() * 16) | 0;
        const value = char === 'x' ? random : (random & 0x3) | 0x8;
        return value.toString(16);
    });
};

onMounted(async () => {
    console.log('📌 hrChipsSign.vue 마운트됨. key 값:', props.key);
    await nextTick();

    if (!chipsId.value) {
        chipsId.value = `chipsSign-${generateUUID()}`;
    }
    if (props.cmd === 'U') {
        let params = {
            targetType: props.targetType,
            targetId: getTargetId(props.writeYear, props.docNo, props.docSeq, props.docDetailSeq),
            type: props.type
        };
        await Search(params);
    }
});
const userList = ref([]);

//#region 시스템 코드 가져오기( 작성, 검토, 승인 ) 관련
const getSystemCodeList = async param => {
    // openLoading();
    try {
        let systemCodeData = await getSystemCode(param);

        return new Map(
            systemCodeData.list.map(item => [
                Number(item.ordSeq), //  key를 숫자로 변환
                {
                    minorCd: item.minorCd,
                    minorNm: item.minorNm
                } // value를 객체로 저장
            ])
        );
    } catch (error) {
        console.error('getSystemCode API 호출 오류:', error);
        throw error;
    } finally {
        // endLoading();
    }
};
//#endregion

//#region [조회] Sim 25. 3. 7.
const Search = async (params) => {
    // await nextTick();
    let createTargetId = getTargetId(props.writeYear, props.docNo, props.docSeq, props.docDetailSeq);
    if (!createTargetId) {
        //서명 데이터가 올바르지 않을 경우 데이터 초기화 [류원진 / 2025.03.25]
        return (userList.value = []);
    }
    let param = {
        targetType: props.targetType,
        targetId: createTargetId,
        type: props.type
    };
    console.log('📌 [search] 기본 조회 파라미터:', param); // 기본 파라미터 로그
    console.log('📌 [search] 전달된 params:', params); // 호출 시 전달된 params 로그
    const systemCodeMap = await getSystemCodeList({ majorCd: 'C0045' });
    const mappedData = systemCodeMap.get(Number(props.sysCodeOrdSeq)); // key를 숫자로 변환 후 조회    
    const res = await searchApprovalInfo(param, false);
    userList.value = res.list.filter(el => el.type === props.type && el.param === mappedData?.minorCd);
    for (const user of userList.value) {
        user.formattedTargetId = getFormattedTargetId(props.targetType, props.writeYear, props.docNo, props.docSeq, props.docDetailSeq);
        user.reqMenuId = router.currentRoute.value.meta.menuId;
        user.img = null;
    }

    // 이미지 로딩은 Search 완료 이후 백그라운드로 처리
    loadUserImagesAsync(userList.value);
};
//#endregion

// 이미지 로딩
const loadUserImagesAsync = users => {
  
    users.forEach(async user => {
        if (user.img) return;
        if (!user.logoId) return;

        try {
            user.img = await loadFileFromServer(user.logoId);
        } catch (e) {
            console.warn('[서명 이미지 로딩 실패]', {
                hrId: user.hrId,
                logoId: user.logoId,
                error: e
            });
            user.img = null;
        }
    });
};

// 서명 시간 포맷
const formattedSignedTime = time => {
    if (!time) return '';

    const [date, timeFull] = time.split(' ');
    const [, month, day] = date.split('-');
    const [hour, minute] = timeFull.split(':');

    return `${month}.${day}<br/>${hour}:${minute}`;
};
const selectPeople = async () => {
    const systemCodeMap = await getSystemCodeList({ majorCd: 'C0045' });
    const mappedData = systemCodeMap.get(Number(props.sysCodeOrdSeq)); // key를 숫자로 변환 후 조회
    let selectedUsers = selectUserStore.getSelectedUser(); // 현재 팝업에서 선택된 사용자 목록
    let inputData = [];

    // 기존 DB에 있는 사용자 (`cmd: 'U'`)를 Map으로 저장 (빠른 조회용)
    let existingUsersMap = new Map();
    userList.value.forEach(user => {
        if (user.cmd === 'U') {
            existingUsersMap.set(user.hrId, user);
        }
    });

    // 선택된 사용자 목록을 Set으로 변환 (빠른 조회를 위해)
    let selectedUserIds = new Set(selectedUsers.map(user => user.hrId));

    // 1. 기존 DB(`U`)에 있던 사용자 중 선택되지 않은 사용자 → D (삭제)
    userList.value = userList.value.filter(user => {
        if (user.cmd === 'U') {
            if (!selectedUserIds.has(user.hrId)) {
                // console.log('삭제할 사용자' + user.hrNm);
                inputData.push({ ...user, cmd: 'D' }); // 삭제 대상
            } else {
                inputData.push({ ...user }); // 삭제 대상
            }
            return false; // userList에서 제거
        } else if (user.cmd === 'I') {
            return false; // userList에서 제거
        }
        return true; // 유지
    });

    // 2. 새로운 사용자(`I`) 추가 (기존 DB 데이터가 아닌 경우만)
    for (const user of selectedUsers) {
        if (existingUsersMap.has(user.hrId)) continue;

        inputData.push({
            cmd: 'I',
            hrId: user.hrId,
            hrNm: user.hrNm,
            hrUserId: user.userId,
            orgnId: user.orgnId,
            orgnNm: user.orgnNm || user.partCompNm,
            jbrpNm: user.jbrpNm,
            logoId: user.logoId,
            img: user.img ? user.img : null,
            targetType: props.targetType,
            param: mappedData?.minorCd || null,
            type: props.type,
            formattedTargetId: getFormattedTargetId(props.targetType, props.writeYear, props.docNo, props.docSeq, props.docDetailSeq)
        });
    }
    userList.value.push(...inputData);
    
    // 이미지 로딩은 이후 백그라운드로 처리
    loadUserImagesAsync(userList.value);
    // await selectUserStore.init();
};

const initPeople = async user => {
    let inputData = [];
    const systemCodeMap = await getSystemCodeList({ majorCd: 'C0045' });
    const mappedData = systemCodeMap.get(Number(props.sysCodeOrdSeq)); // key를 숫자로 변환 후 조회

    for (const el of user) {
        let duplicate = userList.value.find(user => {
            return user.hrId == null;
        });

        if (duplicate == undefined) {
            inputData.push({
                cmd: 'I',
                hrId: el.hrId,
                hrNm: el.hrNm,
                orgnId: el.orgnId,
                orgnNm: el.orgnNm || el.partCompNm,
                jbrpNm: el.jbrpNm,
                logoId: el.logoId,
                img: el.img,
                targetType: props.targetType,
                param: mappedData?.minorCd || null,
                type: props.type
            });
        }
    }
    // userList.value = inputData;
    userList.value.splice(0, userList.value.length, ...inputData);    

    // 이미지 로딩은 이후 백그라운드로 처리
    loadUserImagesAsync(userList.value);
};

// 추후 삭제해야 할 내용인듯
const selectedUserList = () => {
    return selectUserStore.getSelectedUser();
};

const removeSignature = item => {
    item.signature = null;
    // 서명자 서명 등록 취소
    updateApprovalInfoSignCancel(item, false);
};
const signaturePad = ref();
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
        // console.log('Base64 결과 -> ', base64String.length);
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
    // 서명 정보 저장
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
        div.value.signature = signData;
        div.value.cmd = 'U';
        openLoading();
        await updateUserSignature({ userId: userInfoStore.userId, signature: signData }, false)
            .then(async res => {
                if (res) {
                    await updateApprovalInfoSign(div.value, false)
                        .then(() => {
                            userInfoStore.signature = div.value.signature;
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

        // div.value.signature = data;
        // await nullUpdateApprovalInfo(div.value, false);
        // div.value.cmd = 'U';
        // await insertApprovalInfo(div.value, false)
        //     .then(() => {
        //         console.log('# 서명 저장완료');
        //     })
        //     .catch(() => {
        //         console.log('# 서명 저장실패');
        //     });
    }
};

const getSelectedHrId = computed(() => {
    return (
        userList.value
            .filter(el => el.cmd !== 'D') // 🔹 cmd가 'D'가 아닌 항목만 필터링
            .map(el => el.hrId) || []
    ); // 🔹 필터링된 데이터에서 hrId만 추출
});

// 저장시 처리 해야 삭제도 처리됨
const setHrChipsApprovalInfo = async (docType, writeYear, docNo, docSeq, docDetailSeq, isTask = true) => {
    const systemCodeMap = await getSystemCodeList({ majorCd: 'C0045' });
    const mappedData = systemCodeMap.get(Number(props.sysCodeOrdSeq));
    let targetId = getTargetId(writeYear, docNo, docSeq, docDetailSeq);
    let formattedTargetId = getFormattedTargetId(props.targetType || docType, writeYear, docNo, docSeq, docDetailSeq, isTask);
    let routerMenuId = router.currentRoute.value.meta.menuId;

    const deletedUsers = userList.value.filter(user => user.cmd === 'D');

    //  2. 삭제 작업을 비동기적으로 처리
    for (const user of deletedUsers) {
        let param = {
            // `await`을 추가하여 삭제가 완료될 때까지 기다림
            targetId,
            targetType: props.targetType,
            formattedTargetId,
            reqMenuId: routerMenuId,
            param: mappedData?.minorCd || null,
            type: props.type,
            hrId: user.hrId, // 삭제할 사용자 ID 추가
            seq: user.seq,
            useYn: props.useYn ? props.useYn : 'Y'
        };
        await deleteApprovalInfo(param);
    }

    // 모든 사용자에 대해 targetId 설정 (불필요한 중복 제거)
    userList.value.forEach(ds => {
        ds.targetId = targetId;
        ds.formattedTargetId = formattedTargetId;
        ds.reqMenuId = routerMenuId;
    });
    // 서명 저장 로직 실행
    try {
        const filteredUserList = userList.value.filter(row => row.hrId != null && (props.single ? row.cmd === 'I' : true) && row.cmd === 'I');
        if (filteredUserList.length > 0) {
            filteredUserList.forEach(user => {
                user.useYn = props.useYn;
            });
            const result = await insertApprovalInfoList(filteredUserList, false);
            if (result) {
                // insert 성공 시 처리
                console.log('✅ 서명 데이터 삽입 완료');

                filteredUserList.forEach(ds => {
                    ds.cmd = 'U'; // cmd 상태 변경
                });

                console.log('🔄 cmd 상태 변경 완료:', filteredUserList);

                // 필요 시 서명 정보 다시 조회
                // let param = {
                //     targetType: props.targetType,
                //     targetId: targetId,
                //     type: props.type
                // };
                // await search(param);
                console.log('# 서명 저장완료');
                return true;
            } else {
                console.warn('⚠️ insertApprovalInfoList 실행 후 result 값이 없음!');
                return true;
            }
        }
        return true;
    } catch (error) {
        console.error('# 서명 저장실패', error);
        return false;
    }
};

/* 마스터 문서의 useYn이 변경되었을 때 Task의 use_yn 값을 변경시켜줌 (각각 개별 호출) */
const updateTaskUseYn = async (docType, writeYear, docNo, docSeq, docDetailSeq) => {
    // 업데이트할 필요가 없으면 return하여 리소스를 줄임
    console.log('### i-hr-chips-sign / update task use yn');
    console.log('변경 전 : ', initialUseYn.value);
    console.log('변경 후 : ', props.useYn);
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

const addPeople = () => {
    emit('popup');
};
const applyFilter = searchTerm => {
    userList.value.forEach(user => {
        if (user.hrNm.toLowerCase().includes(searchTerm.toLowerCase())) {
            user.visible = true;
        } else user.visible = false;
    });
};
defineExpose({
    setHrChipsApprovalInfo,
    updateTaskUseYn,
    selectPeople,
    Search,
    userList,
    selectedUserList,
    // 현재 select User Pop업에서 선택되어야 할 유저 목록
    getSelectedHrId,
    initPeople,
    applyFilter
});
const closePopup = () => {
    signPopup.value.onClose();
};

const removeChip = hrId => {
    // console.log(hrId);
    let item = userList.value.find(user => user.hrId === hrId && user.cmd !== 'D');
    item.cmd = 'D';
    emit('chipRemoved', item);
};
</script>

<style lang="scss" scoped>
.multi {
    display: grid;
    grid-template-columns: repeat(4, 1fr);
    gap: 8px;

    @media (max-width: 1280px) {
        grid-template-columns: repeat(3, 1fr);
    }

    @media (max-width: 1080px) {
        grid-template-columns: repeat(2, 1fr);
    }

    @media (max-width: 560px) {
        grid-template-columns: repeat(1, 1fr);
    }
}
</style>
