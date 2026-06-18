<template>
    <!-- 차수 카드 -->
    <div class="card" :class="{ disabled: props.disabled, selected: props.selected }">
        <div class="df jcsb aic gap1rem">
            <input v-if="!props.confirm || (props.confirm && useCheck)" type="checkbox" v-input class="shrink0" v-model="selected" :disabled="props.disabled" />
            <em class="ellipsis w100p fs2rem" :title="props.title">{{ props.title }}</em>
            <button v-if="props.usePreview" type="button" class="shrink0 tty2px" @click="btnPreview">
                <svg xmlns="http://www.w3.org/2000/svg" width="22" height="22" viewBox="0 0 22 22" fill="none">
                    <path d="M3.4192 20.5837L3.6462 20.1382H3.6462L3.4192 20.5837ZM1.41621 18.5808L1.86171 18.3538H1.86171L1.41621 18.5808ZM18.5808 1.41618L18.3538 1.86168V1.86168L18.5808 1.41618ZM20.5838 3.41917L20.1383 3.64616V3.64617L20.5838 3.41917ZM3.4192 1.41618L3.6462 1.86168L3.4192 1.41618ZM1.41621 3.41917L1.86171 3.64616L1.41621 3.41917ZM20.5833 11C20.5833 11.2761 20.8072 11.5 21.0833 11.5C21.3595 11.5 21.5833 11.2761 21.5833 11H20.5833ZM11 21.5833C11.2761 21.5833 11.5 21.3594 11.5 21.0833C11.5 20.8072 11.2761 20.5833 11 20.5833V21.5833ZM0.916656 6.83329C0.640514 6.83329 0.416656 7.05715 0.416656 7.33329C0.416656 7.60944 0.640514 7.83329 0.916656 7.83329V6.83329ZM21.0833 7.83329C21.3595 7.83329 21.5833 7.60944 21.5833 7.33329C21.5833 7.05715 21.3595 6.83329 21.0833 6.83329V7.83329ZM20.7298 21.4368C20.925 21.6321 21.2416 21.6321 21.4369 21.4368C21.6321 21.2416 21.6321 20.925 21.4369 20.7297L20.7298 21.4368ZM5.08332 4.58329C5.08332 4.30715 4.85947 4.08329 4.58332 4.08329C4.30718 4.08329 4.08332 4.30715 4.08332 4.58329H5.08332ZM4.08332 4.59246C4.08332 4.8686 4.30718 5.09246 4.58332 5.09246C4.85947 5.09246 5.08332 4.8686 5.08332 4.59246H4.08332ZM7.83332 4.58329C7.83332 4.30715 7.60947 4.08329 7.33332 4.08329C7.05718 4.08329 6.83332 4.30715 6.83332 4.58329H7.83332ZM6.83332 4.59246C6.83332 4.8686 7.05718 5.09246 7.33332 5.09246C7.60947 5.09246 7.83332 4.8686 7.83332 4.59246H6.83332ZM10.5833 4.58329C10.5833 4.30715 10.3595 4.08329 10.0833 4.08329C9.80718 4.08329 9.58332 4.30715 9.58332 4.58329H10.5833ZM9.58332 4.59246C9.58332 4.8686 9.80718 5.09246 10.0833 5.09246C10.3595 5.09246 10.5833 4.8686 10.5833 4.59246H9.58332ZM8.24999 1.41663H13.75V0.416626H8.24999V1.41663ZM1.41666 13.75V8.24996H0.416656V13.75H1.41666ZM8.24999 20.5833C6.95829 20.5833 6.01958 20.5829 5.28038 20.5225C4.54702 20.4626 4.05418 20.3461 3.6462 20.1382L3.1922 21.0292C3.76465 21.3209 4.40375 21.4542 5.19895 21.5192C5.98831 21.5837 6.97479 21.5833 8.24999 21.5833V20.5833ZM0.416656 13.75C0.416656 15.0252 0.416268 16.0116 0.480761 16.801C0.545732 17.5962 0.67903 18.2353 0.970707 18.8077L1.86171 18.3538C1.65384 17.9458 1.53736 17.4529 1.47744 16.7196C1.41705 15.9804 1.41666 15.0417 1.41666 13.75H0.416656ZM3.6462 20.1382C2.87787 19.7468 2.2532 19.1221 1.86171 18.3538L0.970707 18.8077C1.45806 19.7642 2.23571 20.5419 3.19221 21.0292L3.6462 20.1382ZM13.75 1.41663C15.0417 1.41663 15.9804 1.41701 16.7196 1.47741C17.453 1.53733 17.9458 1.65381 18.3538 1.86168L18.8078 0.970676C18.2353 0.678999 17.5962 0.545701 16.801 0.480731C16.0117 0.416237 15.0252 0.416626 13.75 0.416626V1.41663ZM21.5833 8.24996C21.5833 6.97476 21.5837 5.98828 21.5192 5.19892C21.4543 4.40372 21.321 3.76462 21.0293 3.19217L20.1383 3.64617C20.3461 4.05415 20.4626 4.54699 20.5225 5.28035C20.5829 6.01955 20.5833 6.95826 20.5833 8.24996H21.5833ZM18.3538 1.86168C19.1221 2.25317 19.7468 2.87784 20.1383 3.64616L21.0293 3.19217C20.5419 2.23568 19.7643 1.45803 18.8078 0.970676L18.3538 1.86168ZM8.24999 0.416626C6.97479 0.416626 5.98831 0.416237 5.19895 0.480731C4.40375 0.545701 3.76465 0.678999 3.1922 0.970676L3.6462 1.86168C4.05418 1.65381 4.54702 1.53733 5.28038 1.47741C6.01958 1.41701 6.95829 1.41663 8.24999 1.41663V0.416626ZM1.41666 8.24996C1.41666 6.95826 1.41705 6.01955 1.47744 5.28035C1.53736 4.54699 1.65384 4.05415 1.86171 3.64616L0.970707 3.19217C0.67903 3.76462 0.545732 4.40372 0.480761 5.19892C0.416268 5.98828 0.416656 6.97476 0.416656 8.24996H1.41666ZM3.1922 0.970676C2.23571 1.45803 1.45806 2.23568 0.970707 3.19217L1.86171 3.64616C2.2532 2.87784 2.87787 2.25317 3.6462 1.86168L3.1922 0.970676ZM20.5833 8.24996V11H21.5833V8.24996H20.5833ZM11 20.5833H8.24999V21.5833H11V20.5833ZM0.916656 7.83329H21.0833V6.83329H0.916656V7.83329ZM18.8039 15.1519C18.8039 17.1688 17.1689 18.8039 15.152 18.8039V19.8039C17.7212 19.8039 19.8039 17.7211 19.8039 15.1519H18.8039ZM15.152 18.8039C13.135 18.8039 11.5 17.1688 11.5 15.1519H10.5C10.5 17.7211 12.5827 19.8039 15.152 19.8039V18.8039ZM11.5 15.1519C11.5 13.135 13.135 11.5 15.152 11.5V10.5C12.5827 10.5 10.5 12.5827 10.5 15.1519H11.5ZM15.152 11.5C17.1689 11.5 18.8039 13.135 18.8039 15.1519H19.8039C19.8039 12.5827 17.7212 10.5 15.152 10.5V11.5ZM17.7641 18.4712L20.7298 21.4368L21.4369 20.7297L18.4712 17.7641L17.7641 18.4712ZM4.08332 4.58329V4.59246H5.08332V4.58329H4.08332ZM6.83332 4.58329V4.59246H7.83332V4.58329H6.83332ZM9.58332 4.58329V4.59246H10.5833V4.58329H9.58332Z" fill="black" />
                </svg>
            </button>
        </div>

        <hr class="w100p h1px bcE1E6ED my2-2rem" />
        <template v-for="(value, key) in props.data.detail" :key="key">
            <dl>
                <dt>
                    <em>{{ key }}</em>
                </dt>
                <dd :title="value">{{ value }}</dd>
            </dl>
        </template>
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

        <div class="df aic jcsb mt2-4rem">
            <div class="df aic">
                <input v-if="props.useUseYN" type="checkbox" v-input="'사용'" class="switch" :checked="!props.disabled" disabled />
            </div>
        </div>
        <div class="df jcsb gap8px mt2-2rem">
            <button v-if="props.confirm" type="button" class="w50p confirm b-type-cancel" @click="btnConfirm('N')">
                <span>확정취소</span>
            </button>
            <button v-else type="button" class="w50p confirm b-type-diver" @click="btnConfirm('Y')" :disabled="!props.confirmable || props.disabled">
                <span>확정전환</span>
            </button>
            <button type="button" class="h4-6rem btn w50p radius br4px" @click="btnDetail">
                <span>상세정보 보기</span>
            </button>
        </div>
    </div>
</template>

<script setup>
import { defineProps, ref, watch, onMounted } from 'vue';

const props = defineProps({
    title: { type: String }, // 타이틀

    data: {
        type: Object,
        default: () => {
            return {
                detail: {}
            };
        }
    },
    useApprovalStatus: { type: Boolean, default: false }, // 결재 상태 사용 여부
    approvalStatus: { type: String, default: null }, // 결재 상태 : I, Y, C
    disabled: { type: Boolean, default: false }, // 활성화, 비활성화
    useUseYN: { type: Boolean, default: true }, // 사용 유무 스위치 사용 유무
    usePreview: { type: Boolean, default: true }, // 미리보기 사용유무
    confirmable: { type: Boolean, default: true }, // 확정 가능 유무
    confirm: { type: Boolean, default: false }, // 확정 여부
    useCheck: { type: Boolean, default: false }, // 확정 차수 일 때, checkbox 사용 여부
    modelValue: { type: Boolean, default: false }
});

const selected = ref(props.modelValue);
const emit = defineEmits(['update:modelValue', 'confirm', 'preview', 'editor']);

// 컴포넌트가 마운트될 때 이미지 로드
onMounted(() => {});

// 선택 상태 변화 감지
watch(
    () => props.modelValue,
    newVal => {
        selected.value = newVal;
    }
);

watch(selected, e => {
    emit('update:modelValue', e);
    props.data.checked = e;
    props.data.selected = e;
    emit('handle', props.data);
});

// ***********************************************//
// TODO. 2024.10.30 SJS [체크박스 값 변경에 대한 이벤트 처리]
// 아래 주석을 해제하면 체크박스는 정상적으로 동작함
// 하지만 .card &.selected 클래스가 inputWrapper.js 의 change 이벤트에 감지되지 않아 값은 변경되어도 클래스가 변경되지 않는 문제가 있음
// watch(
//     () => props.modelValue,
//     newVal => {
//         selected.value = newVal;
//     }
// );

// 확정취소(N) / 확정전환(Y)
const btnConfirm = type => {
    if (props.disabled) {
        console.log('@@');
    }
    emit('confirm', type); // Y, N 값
};
// 미리보기
const btnPreview = () => {
    emit('preview'); // 미리보기 이벤트
};
const btnDetail = () => {
    emit('editor');
};
</script>
