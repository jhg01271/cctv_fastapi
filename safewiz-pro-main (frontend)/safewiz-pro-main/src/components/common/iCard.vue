<template>
    <!-- 기본 카드 -->
    <div class="grid12-4 ul-grid12-6 lg-grid12-12">
        <div class="card us-w100p df fdc" :class="{ disabled: props.disabled, selected: selected }" :key="selected">
            <div class="df aic jcsb">
                <div class="profile w6rem h6rem br4px oh" v-if="props.type === 'profile' && isLoading">
                    <div class="w100p h100p ofc">
                        <div class="img-spinner"></div>
                    </div>
                </div>
                <!-- <div class="profile w6rem h6rem br4px oh" v-else if="props.type === 'profile'"> -->
                <!--              인원관리에서 사용-->
                <div class="profile w6rem h6rem br4px oh dif aic jcc" :class="{ 'no-user': !logoImageUrl }" v-else-if="props.type === 'profile'">
                    <img :src="logoImageUrl ? logoImageUrl : '/assets/img/common/icon_no_user.svg'" :class="logoImageUrl ? 'w100p h100p ofc' : 'no-user w30px '" />
                </div>
                <!--              협력사에서 사용-->
                <div class="profile w6rem h6rem br4px oh dif aic jcc" :class="{ 'no-image': !logoImageUrl }" v-else-if="props.type === 'partner'">
                    <img :src="logoImageUrl ? logoImageUrl : '/assets/img/common/icon_no_image.svg'" :class="logoImageUrl ? 'w100p h100p ofc' : 'no-user w30px '" />
                </div>
                <div class="profile w6rem h6rem br4px oh" v-else-if="logoImageUrl">
                    <img :src="logoImageUrl" alt class="w100p h100p ofc" />
                </div>
                <em class="ellipsis fs2rem" v-if="props.title" :title="props.title">{{ props.title }}</em>

                <input v-if="useCheck" type="checkbox" v-input class="shrink0" v-model="selected" @change="updateValue" />
            </div>

            <hr class="w100p h1px bcE1E6ED mt2-2rem mb2-2rem" />
            <div class="w100p" v-if="useThumbnail" :class="{ 'no-image h20rem ': !thumbnailImageUrl }">
                <div class="w6rem h6rem br4px oh" v-if="isLoading">
                    <div class="thumb-spinner"></div>
                </div>
                <img v-else-if="thumbnailImageUrl" :src="thumbnailImageUrl" alt class="w100p ofc" style="height: 200px" />
                <img v-else :src="'/assets/img/common/icon_no_image.svg'" width="80" />
            </div>

            <div class="fg1" v-if="props.unregistered">
                <div class="minh11-8rem df aic jcc mt2-4rem">
                    <dl>
                        <dd class="c9EA1A6">등록된 내용이 없습니다</dd>
                    </dl>
                </div>
            </div>
            <div class="fg1" v-else-if="props.data">
                <div v-if="props.data.detail">
                    <template v-for="(value, key) in props.data.detail" :key="key">
                        <dl>
                            <dt>
                                <em>{{ key }}</em>
                            </dt>
                            <dd :title="value">{{ value }}</dd>
                        </dl>
                    </template>
                </div>
                <div class="df aic jcsb mt2rem" v-if="props.useApprovalStatus">
                    <dl>
                        <dt>
                            <em>결재 상태</em>
                        </dt>
                        <dd>
                            <!-- C: 기안, W: 작성, I: 검토, Y: 승인 -->
                            <!-- C: 서명이 모두 없는 상태 -->
                            <!-- W: 작성자 서명만 완료된 상태 -->
                            <!-- I: 작성자, 검토자 서명이 완료된 상태 -->
                            <!-- Y: 승인자까지의 서명이 완료된 상태 -->
                            <!-- 해당 플래그값은 조회결과 기반으로 넣어주셔야 합니다. -->
                            <span v-if="props.approvalStatus === 'W'" class="state progress">작성</span>
                            <span v-else-if="props.approvalStatus === 'I'" class="state progress">검토</span>
                            <span v-else-if="props.approvalStatus === 'Y'" class="state complete">승인</span>
                            <span v-else class="state ready">기안</span>
                        </dd>
                    </dl>
                </div>
                <template v-for="(item, index) in props.data.detailWithBtn" :key="index">
                    <div class="df aic jcsb mt6px" v-if="props.data.detailWithBtn">
                        <dl>
                            <dt>
                                <em>{{ item.label }}</em>
                            </dt>
                            <dd>
                                <template v-for="(el, i) in item.button" :key="i">
                                    <span v-if="el.value == item.value" class="state" :class="el.class">{{ el.label }}</span>
                                </template>
                            </dd>
                        </dl>
                    </div>
                </template>
                <div class="df aic jcsb mt2-4rem">
                    <div class="df aic">
                        <input v-if="useUseYN" type="checkbox" v-input="'사용'" class="switch" :checked="!props.disabled" disabled />
                    </div>

                    <div class="admin df aic jcc gap4px">
                        <!-- 첫 번째 프로필 -->
                        <div class="admin df aic jcc gap4px">
                            <i class="profile pr" v-if="firstImageUrl">
                                <img :src="firstImageUrl" />
                                <!-- 담당자(정) 여러 명인 경우 뱃지 표시 -->
                                <span v-if="props.headHrList.length > 1" class="pa neg-r5px neg-t5px w2rem h2rem df jcc aic bc000950 fs12px fw700 br50p es-w1-8rem es-h1-8rem es-fs10px">{{ props.headHrList.length }}</span>
                            </i>
                            <i class="profile pr w5-4rem h5-4rem br5-4rem df aic jcc bc5cccec" :class="{ yellow: userHr }" v-else-if="props.headHrList.length > 0" :title="props.headHrList[0].hrNm">
                                {{ props.headHrList[0].hrNm ? props.headHrList[0].hrNm.charAt(0) : '' }}
                                <!-- 담당자(정 여러 명인 경우 뱃지 표시 -->
                                <span v-if="props.headHrList.length > 1" class="pa neg-r5px neg-t5px w2rem h2rem df jcc aic bc000950 fs12px fw700 br50p es-w1-8rem es-h1-8rem es-fs10px">{{ props.headHrList.length }}</span>
                            </i>
                            <i class="profile" :class="{ 'no-thumbs': userHr }" v-else-if="userHr">
                                <img src="/assets/img/common/icon_no_thumbs.svg" class="no-thumbs" width="24" />
                            </i>

                            <div class="test w35px h35px br35px oh ml12px mr12px" v-else></div>
                        </div>
                        <!-- 두 번째 프로필 -->
                        <div class="admin df aic jcc gap4px">
                            <i class="profile pr" v-if="secondImageUrl" v-tooltip="'123123'">
                                <img :src="secondImageUrl" />
                                <!-- 담당자(부) 여러 명인 경우 뱃지 표시 -->
                                <span v-if="props.secondHrList.length > 1" class="pa neg-r5px neg-t5px w2rem h2rem df jcc aic bc000950 fs12px fw700 br50p es-w1-8rem es-h1-8rem es-fs10px">{{ props.secondHrList.length }}</span>
                            </i>
                            <i class="profile pr w5-4rem h5-4rem br5-4rem df aic jcc bc5cccec" :class="{ yellow: userHr }" v-else-if="props.secondHrList.length > 0" :title="props.secondHrList[0].hrNm">
                                {{ props.secondHrList[0].hrNm ? props.secondHrList[0].hrNm.charAt(0) : '' }}
                                <!-- 담당자(부) 여러 명인 경우 뱃지 표시 -->
                                <span v-if="props.secondHrList.length > 1" class="pa neg-r5px neg-t5px w2rem h2rem df jcc aic bc000950 fs12px fw700 br50p es-w1-8rem es-h1-8rem es-fs10px">{{ props.secondHrList.length }}</span>
                            </i>
                            <i class="profile pr" :class="{ 'no-thumbs': userHr }" v-else-if="userHr">
                                <img src="/assets/img/common/icon_no_thumbs.svg" class="no-thumbs" width="24" />
                                <!-- 유저 없음 -->
                                <!-- <span v-if="props.secondHrList.length > 1" class="pa neg-r5px neg-t5px w2rem h2rem df jcc aic bc000950 fs12px fw700 br50p es-w1-8rem es-h1-8rem es-fs10px">{{ props.secondHrList.length }}</span> -->
                            </i>
                            <div class="test w35px h35px br35px oh ml12px mr12px" v-else></div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="df form ui mt2-2rem">
                <button v-if="props.btnMode" type="button" class="btn w100p radius lg mr1rem" @click="btnMove">
                    <span>{{ props.btnMode }}</span>
                </button>
                <button type="button" class="btn w100p radius lg" @click="btnDetail">
                    <span>상세정보 보기</span>
                </button>
            </div>
            <!-- 2024.10.29 위험성평가 사전준비자료 - 안전보건정보조사 카드 내 버튼 -->
            <!-- <div class="df form ui mt2-2rem gap0-8rem">
                <button type="button" class="btn w100p radius line lg">
                    <span>기준정보변경</span>
                </button>
                <button type="button" class="btn w100p radius lg">
                    <span>상세정보 보기</span>
                </button>
            </div>-->
        </div>
    </div>
</template>

<script setup>
import { defineProps, ref, watch, onMounted } from 'vue';
import { loadFileFromServer } from '@/utils/iFileLoader.js';

const props = defineProps({
    title: { type: String }, // 타이틀
    disabled: { type: Boolean, default: false },
    data: { type: Object, default: null },
    unregistered: { type: Boolean, default: false },
    modelValue: { type: Boolean, default: false },
    userHr: { type: Boolean, default: false }, // 정/부 사용 여부
    useApprovalStatus: { type: Boolean, default: false }, // 결재 상태 사용 여부
    useThumbnail: { Boolean, default: false }, // 섬머리 사용 여부
    approvalStatus: { type: String, default: null }, // 결재 상태 : I, Y, C
    headHrList: { type: Array, default: () => [] }, // 정 인원목록록
    secondHrList: { type: Array, default: () => [] }, // 부 인원목록
    logImg: { type: String, default: null }, // 타이틀 이미지 ID
    detail: { type: Object }, // 상세 보기 이미지
    thumbnailImg: { type: String, default: null }, // 섬머리 이미지
    type: { type: String, default: null }, // 카드 타입
    useCheck: { type: Boolean, default: true }, // 체크 사용 여부
    useUseYN: { type: Boolean, default: true }, // 사용 표시 여부
    btnMode: { type: String, default: null } // 버튼 사용 표시 여부
});

const selected = ref(props.modelValue);
const logoImageUrl = ref(null); // 동적으로 이미지 URL을 저장할 변수
const isLoading = ref(false); // 이미지 로딩 상태 상태값
const firstImageUrl = ref(null); // 동적으로 이미지 URL을 저장할 변수
const secondImageUrl = ref(null); // 동적으로 이미지 URL을 저장할 변수
const thumbnailImageUrl = ref(null); // 동적으로 이미지 URL을 저장할 변수
const emit = defineEmits(['update:modelValue', 'handle', 'editor']);
const imageLog = ref(null);
const imageFirst = ref(null);
const imageSencond = ref(null);
const imageThumbnail = ref(null);

// 공통 로드 함수
const loadImageData = async (propName, propValue) => {
    if (propValue) {
        isLoading.value = true;
        switch (propName) {
            case 'logImg':
                if (imageLog.value !== propValue) {
                    imageLog.value = propValue;
                    await loadFileFromServer(propValue)
                        .then(res => {
                            logoImageUrl.value = res;
                        })
                        .catch(() => {
                            isLoading.value = false;
                        })
                        .finally(() => {
                            isLoading.value = false;
                        });
                }
                break;
            case 'firstHrImageId':
                if (imageFirst.value !== propValue) {
                    imageFirst.value = propValue;
                    await loadFileFromServer(propValue)
                        .then(res => {
                            firstImageUrl.value = res;
                        })
                        .catch(() => {
                            isLoading.value = false;
                        })
                        .finally(() => {
                            isLoading.value = false;
                        });
                }
                break;
            case 'secondHrImageId':
                if (imageSencond.value !== propValue) {
                    imageSencond.value = propValue;
                    await loadFileFromServer(propValue)
                        .then(res => {
                            secondImageUrl.value = res;
                        })
                        .catch(() => {
                            isLoading.value = false;
                        })
                        .finally(() => {
                            isLoading.value = false;
                        });
                }
                break;
            case 'thumbnailImg':
                if (imageThumbnail.value !== propValue) {
                    imageThumbnail.value = propValue;
                    await loadFileFromServer(propValue)
                        .then(res => {
                            thumbnailImageUrl.value = res;
                        })
                        .catch(() => {
                            isLoading.value = false;
                        })
                        .finally(() => {
                            isLoading.value = false;
                        });
                }
                break;
        }
    }
};

// 상태 초기화 함수
const resetState = () => {
    // 다시 그려지면 초기화 처리
    selected.value = false;
    logoImageUrl.value = null;
    firstImageUrl.value = null;
    secondImageUrl.value = null;
    thumbnailImageUrl.value = null;
    imageLog.value = null;
    imageFirst.value = null;
    imageSencond.value = null;
    imageThumbnail.value = null;
};

// 모든 props가 한 번에 변경될 때 감지
watch(
    () => ({
        logImg: props.logImg,
        firstHrImageId: props.headHrList.length > 0 ? props.headHrList[0].logoId : null,
        secondHrImageId: props.secondHrList.length > 0 ? props.secondHrList[0].logoId : null,
        thumbnailImg: props.thumbnailImg
    }),
    async (newProps, oldProps) => {
        if (JSON.stringify(newProps) !== JSON.stringify(oldProps)) {
            resetState(); // 상태 초기화
            await loadImages(); // 이미지 로드
        }
    }
);

// 이미지 로드
const loadImages = async () => {
    await loadImageData('logImg', props.logImg);
    await loadImageData('firstHrImageId', props.headHrList.length > 0 ? props.headHrList[0].logoId : null);
    await loadImageData('secondHrImageId', props.secondHrList.length > 0 ? props.secondHrList[0].logoId : null);
    await loadImageData('thumbnailImg', props.thumbnailImg);
};

// 컴포넌트가 마운트될 때 이미지 로드
onMounted(() => {
    loadImages();
});

// 선택 상태 변화 감지
watch(selected, e => {
    emit('update:modelValue', e);
    props.data.checked = e;
    emit('handle', props.data);
});
// ***********************************************//
// TODO. 2024.10.30 SJS [체크박스 값 변경에 대한 이벤트 처리]
// 아래 주석을 해제하면 체크박스는 정상적으로 동작함
// 하지만 .card &.selected 클래스가 inputWrapper.js 의 change 이벤트에 감지되지 않아 값은 변경되어도 클래스가 변경되지 않는 문제가 있음
watch(
    () => props.modelValue,
    newVal => {
        selected.value = newVal;
    }
);

const btnMove = () => {
    emit('move', props.data);
};

const btnDetail = () => {
    emit('editor', props.data);
};
</script>
<style lang="scss" scoped>
.img-spinner {
    border: 4px solid #f3f3f3;
    border-top: 4px solid #16223b;
    border-radius: 50%;
    width: 50%;
    height: 50%;
    margin: 0 auto;
    animation: spin 1s linear infinite;
}

.thumb-spinner {
    border: 4px solid #f3f3f3;
    border-top: 4px solid #16223b;
    border-radius: 50%;
    width: 50%;
    height: 50%;
    margin: 0 auto;
    animation: spin 1s linear infinite;
}

/* 스피너 회전 애니메이션 */
@keyframes spin {
    0% {
        transform: rotate(0deg);
    }
    100% {
        transform: rotate(360deg);
    }
}
</style>
