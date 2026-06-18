<template>
    <!-- 기본 카드 -->
    <div :class="['i-chips']" class="df aic w100p" :id="chipsId" :tabindex="0" v-bind:data-chips="Array.isArray(props.chips) && props.chips.length > 0 && props.chips.some(chip => chip.id && (chip.name || chip.nm)) ? JSON.stringify(chips) : null" :data-required="required">
        <div class="chips df aic wsn" ref="chipsScroll" @wheel.stop.prevent="chipsWheel">
            <OverlayScrollbarsComponent
                class="w100p"
                :options="{
                    scrollbars: {
                        x: 'visible',
                        y: 'hidden'
                    }
                }"
            >
                <div class="df aic" v-if="Array.isArray(props.chips) && props.chips.length > 0 && props.chips.some(chip => chip.id && (chip.name || chip.nm))">
                    <div class="item mr8px shrink0" v-for="(chip, index) in props.chips" :key="index">
                        <div class="h100p df aic bcF8F9FB pl8px pr8px pt3px pb3px br4px">
                            <i class="w2-4rem h2-4rem br2px oh" v-if="chip.img">
                                <img :src="chip.img" alt class="w100p h100p ofc" />
                            </i>
                            <i class="w2-4rem h2-4rem br2px oh" v-else-if="chip.loading">
                                <div class="thumb-spinner"></div>
                            </i>
                            <i class="w2-4rem h2-4rem br2px oh" v-else>
                                <img src="/assets/img/common/icon_no_user.svg" />
                            </i>
                            <span class="mx8px fs1-4rem">{{ chip.name }}</span>
                            <button v-if="chip.name && !props.readonly" type="button" @click="removeChip(index)" class="fs0">
                                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none">
                                    <path d="M17 7L7 17M17 17L7 7.00001" stroke="#FF3534" stroke-linecap="round" />
                                </svg>
                            </button>
                        </div>
                    </div>
                </div>
            </OverlayScrollbarsComponent>
        </div>

        <button v-if="!props.readonly" type="button" class="shrink0" @click="popup">
            <img src="/assets/img/common/icon_search.svg" alt />
        </button>
    </div>
</template>

<script setup>
import { loadFileFromServer } from '@/utils/iFileLoader';
import { defineProps, onMounted, ref, watch } from 'vue';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';

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
    required: {
        // 추가
        type: Boolean,
        default: false
    },
    readonly: {
        type: Boolean,
        default: false
    }
});

const chips = ref([...props.chips]);
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
watch(
    () => props.chips,
    async newVal => {
        if (newVal) {
            await Promise.all(
                newVal.map(async el => {
                    el.loading = true;
                    if (el.fileId) {
                        el.img = await loadFileFromServer(el.fileId);
                    }
                    el.loading = false;
                })
            );
        }
    },
    { immediate: true }
);

const emit = defineEmits(['popup', 'removeChip']);
const popup = () => {
    emit('popup'); // 부모에게 이벤트 발생
};

const removeChip = index => {
    const newChips = [...props.chips]; // props 복사 후 수정
    newChips.splice(index, 1);
    emit('removeChip', newChips); // 부모로 변경된 chips 전달
};
</script>
