<template>
    <!-- 기본 카드 -->
    <div :class="['i-chips']" class="df aic w100p" :id="chipsId" :tabindex="props.readonly || props.disabled ? -1 : 0" v-bind:data-chips="Array.isArray(props.chips) && props.chips.length > 0 && props.chips.some(chip => chip[props.chipIdKey] && (chip[props.chipNameKey] || chip.nm)) ? JSON.stringify(chips) : null" :data-required="required">
        <div class="chips invisible-scroll wsn oyh oxa w100p" ref="chipsScroll" @wheel.stop.prevent="chipsWheel" :class="{ 'disabled-style': props.disabled }">
            <OverlayScrollbarsComponent
                :options="{
                    scrollbars: {
                        x: 'visible',
                        y: 'hidden'
                    }
                }"
            >
                <ul v-if="Array.isArray(props.chips) && props.chips.length > 0 && props.chips.some(chip => chip[props.chipIdKey] && (chip[props.chipNameKey] || chip.nm))" class="init m wsn">
                    <li v-for="(chip, index) in props.chips" :key="index">
                        <span v-if="chip.title" class="fs1-5rem">{{ chip.title + ' ' }}</span>
                        <span class="mr8px fs1-5rem">{{ chip[props.chipNameKey] ? chip[props.chipNameKey] : chip.nm }}</span>
                        <button v-show="!(props.readonly || props.readonlyType === 'chips' || props.disabled)" v-if="chip[props.chipNameKey] || chip.nm" type="button" @click="removeChip(index)" :disabled="props.readonly || props.disabled">
                            <svg width="8" height="8" viewBox="0 0 8 8" fill="none" xmlns="http://www.w3.org/2000/svg">
                                <path d="M7 1L1 6.99999M7 7L1 1.00001" stroke="#FF3534" stroke-linecap="round" />
                            </svg>
                        </button>
                    </li>
                </ul>
            </OverlayScrollbarsComponent>
        </div>

        <button v-if="!(props.readonly || props.readonlyType === 'search' || props.disabled)" type="button" class="shrink0" @click="popup">
            <img src="/assets/img/common/icon_search.svg" alt />
        </button>
    </div>
</template>

<script setup>
import { defineProps, ref, onMounted } from 'vue'; // onMounted 임포트 추가
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import BaseView from '../base/BaseView';
const { confirmMsg } = BaseView();

const props = defineProps({
    chips: {
        type: Array,
        default: () => []
    },
    //chips : 여러개 : multi, / 기본: single
    type: {
        type: String,
        default: 'single'
    },
    //chips : 여러개 : multi, / 기본: single
    readonly: {
        type: Boolean,
        default: false
    },
    readonlyType: {
        type: String,
        default: 'all' // all : 돋보기, Chip 둘다 읽기 전용, search: 돋보기 안보임, chips : chips x버튼 안보임
    },
    required: {
        // 추가
        type: Boolean,
        default: false
    },
    //#region 동적으로 Key값과 Value 값 처리 하도록 수정
    chipIdKey: {
        type: String,
        default: 'id' // 기본값을 'id'로 설정
    },
    chipNameKey: {
        type: String,
        default: 'name' // 기본값을 'name'으로 설정
    },
    disabled: {
        type: Boolean,
        default: false
    },
    removeConfirm: {
        // chips 삭제 여부 확인
        type: Boolean,
        default: false
    },
    removeConfirmMsg: {
        // chips 삭제 확인 세부 메시지 내용
        type: String,
        default: '삭제하시겠습니까?'
    }
    //#endregion
});

// 동적 ID 생성
const chipsId = ref(null);

const generateUUID = () => {
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (char) {
        const random = (Math.random() * 16) | 0;
        const value = char === 'x' ? random : (random & 0x3) | 0x8;
        return value.toString(16);
    });
};

onMounted(() => {
    if (!chipsId.value) {
        chipsId.value = `chips-${generateUUID()}`;
    }
});

//#region ===== 반환 되는 값 : chips의 배열번호 & 삭제되는 Chips =====
const emit = defineEmits(['popup', 'removeChip']);
//#endregion
const popup = () => {
    emit('popup'); // 부모에게 이벤트 발생
};

const removeChip = async index => {
    if (props.removeConfirm) {
        // i-chips를 제거할 때 확인 메시지를 받는 경우
        await confirmMsg('warning', props.removeConfirmMsg, null, { fun: removeChipByIndex, param: '' });
    } else {
        // 확인 없이 제거 하는 경우
        removeChipByIndex(index);
    }
};

// 실제 chip 데이터를 제거하는 함수
const removeChipByIndex = index => {
    let removeChips = props.chips[index];
    props.chips.splice(index, 1);
    emit('removeChip', index, removeChips); // 부모에게 이벤트 발생
};

const chipsScroll = ref(null);
const chipsWheel = event => {
    chipsScroll.value.scrollLeft += event.deltaY;
};
</script>
<style scoped lang="scss">
/* 비활성화 스타일 */
.chips.disabled-style {
    background-color: #ecf0f4 !important; /* 회색 배경 */
    border: 1px solid #cbcfd6; /* 기존보다 더 두껍게 설정 */
    opacity: 0.5 !important; /* 반투명 효과 */
    pointer-events: none !important; /* 클릭 방지 */
}
</style>
