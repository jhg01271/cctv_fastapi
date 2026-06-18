<template>
    <div :class="[iChips ? ['w100pcalc1rem', 'bcF9FAFF', 'pr1rem'] : '']" class="i-chips w100p br4px oh" v-bind="getDivAttributes()">
        <div v-if="userList.length > 0" :class="[!iChips ? ['bdl1pxsolide1e6ed', 'bdr1pxsolide1e6ed'] : '']" class="w100p">
            <OverlayScrollbarsComponent
                :options="{
                    scrollbars: {
                        x: 'visible',
                        y: 'hidden'
                    }
                }"
            >
                <div class="item df aic gap1rem" v-if="userList.filter(user => user.cmd !== 'D').length > 0">
                    <div class="h4-4rem px1-2rem df aic fg1 bcffffff bd1pxsolide1e6ed br4px jcsa" :class="[!iChips ? ['bdl0pxsolide1e6ed', 'bdr0pxsolide1e6ed'] : '']" v-for="item in userList.filter(user => user.cmd === 'I' || user.cmd === 'U')" :key="item.hrId">
                        <i class="w2-4rem h2-4rem br2px oh" v-if="item.img">
                            <img :src="item.img" alt class="w100p h100p ofc" />
                        </i>
                        <i class="w2-4rem h2-4rem br2px oh" v-else>
                            <img src="/assets/img/common/icon_no_user.svg" />
                        </i>
                        <span class="wsn mx10px sm-mr1rem fs1-4rem">{{ item.hrNm }}</span>
                        <button type="button" @click="removeChip(item.hrId)" v-if="!props.signOnly">
                            <svg class="shrink0" xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none">
                                <path d="M17 7L7 17M17 17L7 7.00001" stroke="#FF3534" stroke-linecap="round" />
                            </svg>
                        </button>
                        <div class="h4-4rem pl2rem lh4-4rem bdl1pxsolidE1E6ED">
                            <div v-if="item.signature" class="df aic gap8px fs1-4rem wsn oh">
                                서명
                                <img :src="item.signature" alt class="maxw60px" style="max-height: 40px; object-fit: contain" />
                                <button type="button" class="ttx5px" @click="removeSignature(item)" v-show="item.signature">
                                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none">
                                        <path d="M17 7L7 17M17 17L7 7.00001" stroke="#FF3534" stroke-linecap="round" />
                                    </svg>
                                </button>
                            </div>
                            <button type="button" v-else-if="item.hrId && item.hrId == userInfoStore.userId && props.cmd != 'I'" class="h100p df aic gap8px fs1-4rem wsn cp" @click="addSign(item)">
                                서명
                                <svg class="shrink0" xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none">
                                    <path d="M16.5 9.13608C14.682 9.7421 12.2579 7.31804 12.8639 5.5M13.6109 4.75306L9.69403 8.66988C7.99123 10.3727 6.78322 12.5063 6.19917 14.8425L6.00991 15.5995C5.95094 15.8354 6.1646 16.0491 6.40049 15.9901L7.15752 15.8008C9.49375 15.2168 11.6273 14.0088 13.3301 12.306L17.2469 8.38914C17.7291 7.90697 18 7.253 18 6.5711C18 5.15112 16.8489 4 15.4289 4C14.747 4 14.093 4.27088 13.6109 4.75306Z" stroke="black" stroke-width="1.5" />
                                    <path d="M13.6114 4.75306C14.0936 4.27088 14.7475 4 15.4294 4C16.8494 4 18.0005 5.15112 18.0005 6.5711C18.0005 7.253 17.7296 7.90697 17.2475 8.38914L16.5005 9.13609C14.6825 9.7421 12.2584 7.31804 12.8644 5.5L13.6114 4.75306Z" stroke="#3248F6" stroke-width="1.5" />
                                    <path d="M19 20H5" stroke="#3248F6" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
                                </svg>
                            </button>

                            <div v-else class="df aic gap1-5rem fs1-4rem wsn">
                                서명
                                <svg class="shrink0" xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none">
                                    <path d="M16.5 9.13608C14.682 9.7421 12.2579 7.31804 12.8639 5.5M13.6109 4.75306L9.69403 8.66988C7.99123 10.3727 6.78322 12.5063 6.19917 14.8425L6.00991 15.5995C5.95094 15.8354 6.1646 16.0491 6.40049 15.9901L7.15752 15.8008C9.49375 15.2168 11.6273 14.0088 13.3301 12.306L17.2469 8.38914C17.7291 7.90697 18 7.253 18 6.5711C18 5.15112 16.8489 4 15.4289 4C14.747 4 14.093 4.27088 13.6109 4.75306Z" stroke="#9EA1A6" stroke-width="1.5" />
                                    <path d="M13.6114 4.75306C14.0936 4.27088 14.7475 4 15.4294 4C16.8494 4 18.0005 5.15112 18.0005 6.5711C18.0005 7.253 17.7296 7.90697 17.2475 8.38914L16.5005 9.13609C14.6825 9.7421 12.2584 7.31804 12.8644 5.5L13.6114 4.75306Z" stroke="#9EA1A6" stroke-width="1.5" />
                                    <path d="M19 20H5" stroke="#9EA1A6" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
                                </svg>
                            </div>
                        </div>
                    </div>
                </div>
            </OverlayScrollbarsComponent>
        </div>

        <!-- <button v-if="!props.readonly" type="button" class="shrink0 fg0" @click="popup">
            <img src="/assets/img/common/icon_search.svg" alt />
        </button> -->
    </div>

    <i-PopupDialog ref="signPopup" class>
        <div class="contents w500px md-w100p">
            <VueSignaturePad id="signature" width="100%" height="156px" ref="signaturePad" />
            <div class="form ui tar mt2-5rem">
                <button type="button" class="btn w80px radius active" v-button @click="saveSign">
                    <span>저장</span>
                </button>
                <button type="button" v-button class="btn w80px radius ml4px bright-grey" @click="closePopup">
                    <span>닫기</span>
                </button>
            </div>
        </div>
    </i-PopupDialog>
</template>

<script setup>
import { defineProps, ref, onMounted, computed, nextTick } from 'vue';
const { router } = BaseView();
import { useUserInfoStore } from '@/stores/user';

const userInfoStore = useUserInfoStore();
import { deleteApprovalInfo, insertApprovalInfo, insertApprovalInfoList, nullUpdateApprovalInfo, searchApprovalInfo, updateApprovalInfoSign, updateApprovalInfoSignCancel } from '@/api/base/common/utils';
import { useSelectUserStore } from '@/views/base/member/SelectUser/SelectUser';
import { loadFileFromServer } from '@/utils/iFileLoader';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import { getFormattedTargetId, getTargetId } from '@/utils/documentUtils.js';
import BaseView from '@/components/base/BaseView.js';
import { getSystemCode } from '@/stores/safewiz/dataset/api/datasetApi.js';

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
        console.log(item);
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
    }
});

const iChips = computed(() => {
    const targetTypes = ['OHC', 'TBM', 'RORAT', 'PAC', 'TRR', 'HCL', 'PTW', 'RAO', 'RAP'];
    return targetTypes.includes(props.targetType);
});

// watch(() => props.docNo, async (newVal) => {
//     if (newVal) {
//         await Search(props);
//     }
// }, { immediate: true });

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
        return JSON.stringify(userList.value);
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
        chipsId.value = `chips-${generateUUID()}`;
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
const Search = async params => {
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
    searchApprovalInfo(params ? params : param, false).then(async res => {
        userList.value = res.list.filter(el => {
            // return el.param == mappedData?.minorCd;
            return (el.type === props.type && el.param === mappedData?.minorCd) || null;
        });
        for (const user of userList.value) {
            user.formattedTargetId = getFormattedTargetId(props.targetType, props.writeYear, props.docNo, props.docSeq, props.docDetailSeq);
            user.reqMenuId = router.currentRoute.value.meta.menuId;
            user.img = await loadFileFromServer(user.logoId);
        }
    });
};
//#endregion

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
                console.log('삭제할 사용자' + user.hrNm);
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
    selectedUsers.forEach(user => {
        if (!existingUsersMap.has(user.hrId)) {
            console.log('추가할 사용자' + user.hrNm);
            inputData.push({
                cmd: 'I', // 삽입 (Insert)
                hrId: user.hrId,
                hrNm: user.hrNm,
                hrUserId: user.userId,
                orgnId: user.orgnId,
                orgnNm: user.orgnNm || user.partCompNm,
                jbrpNm: user.jbrpNm,
                img: user.img,
                targetType: props.targetType,
                param: mappedData?.minorCd || null,
                type: props.type,
                formattedTargetId: getFormattedTargetId(props.targetType, props.writeYear, props.docNo, props.docSeq, props.docDetailSeq)
            });
        }
    });
    userList.value.push(...inputData);
    // await selectUserStore.init();
};

const initPeople = async user => {
    let inputData = [];
    const systemCodeMap = await getSystemCodeList({ majorCd: 'C0045' });
    const mappedData = systemCodeMap.get(Number(props.sysCodeOrdSeq)); // key를 숫자로 변환 후 조회
    let deletedList = [];
    for (const user of userList.value) {
        user.cmd = 'D';
        deletedList.push(user);
    }
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
                img: el.img ? el.img : await loadFileFromServer(el.logoId),
                targetType: props.targetType,
                param: mappedData?.minorCd || null,
                type: props.type
            });
        }
    }
    // userList.value = inputData;
    userList.value.splice(0, userList.value.length, ...deletedList, ...inputData);
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
const saveSign = async () => {
    // 서명 정보 저장
    const { isEmpty, data } = signaturePad.value.saveSignature();
    // 서명이 저장 되었다면
    if (isEmpty == false) {
        div.value.signature = data;
        await nullUpdateApprovalInfo(div.value, false);
        div.value.cmd = 'U';
        await insertApprovalInfo(div.value, false)
            .then(() => {
                console.log('# 서명 저장완료');
            })
            .catch(() => {
                console.log('# 서명 저장실패');
            });
    }

    signPopup.value.onClose();
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
            seq: user.seq
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

defineExpose({
    setHrChipsApprovalInfo,
    selectPeople,
    Search,
    userList,
    selectedUserList,
    // 현재 select User Pop업에서 선택되어야 할 유저 목록
    getSelectedHrId,
    initPeople
});
const closePopup = () => {
    signPopup.value.onClose();
};

const removeChip = hrId => {
    console.log(hrId);
    let item = userList.value.find(user => user.hrId === hrId && user.cmd !== 'D');
    item.cmd = 'D';
    emit('chipRemoved', item);
};
</script>
<style scoped>
.i-chips {
    position: relative;

    &.invalid {
        outline: 5px solid rgba(255, 0, 0, 0.15);
        border-color: rgba(255, 0, 0, 0.5);

        .error-message {
            position: absolute;
            left: 0;
            bottom: -1.5rem;
            font-size: 13px;
            line-height: 1.3;
            color: #cc0000;
        }
    }

    &.valid {
        //outline: 5px solid rgba(0, 255, 0, 0.15);
        //border-color: rgba(0, 255, 0, 0.5);
    }
}
</style>
