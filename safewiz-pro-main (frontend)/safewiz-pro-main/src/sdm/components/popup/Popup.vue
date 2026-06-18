<template>
    <!-- 에니메이션이 시점 분기를 위해서 isVisible이 true일 때 먼저 보여지는 요소입니다.  -->
    <div v-if="isVisible">
        <div ref="popup" class="igns-pop invisible-scroll df aic jcc">
            <div ref="section" class="section br4px">
                <slot></slot>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, watch, onMounted, nextTick, defineExpose } from 'vue';
import { usePopupStore } from '@/sdm/stores/popupStore';
import { gsap } from 'gsap';

// store 가져오기
const store = usePopupStore();
const popupId = ref(Symbol('popup')); // 각 팝업에 고유한 ID 부여
const isVisible = ref(false);
const title = ref(store.title);
const slotContent = ref(null);
const componentProps = ref({});
const popup = ref(null);
const section = ref(null);

// 부모 컴포넌트에서 사용할 수 있는 이벤트
const emit = defineEmits(['close', 'open']);

// store의 slotContent가 변경될 때 slotContent를 업데이트
watch(
    () => store.slotContent,
    newVal => {
        slotContent.value = newVal;
    }
);

// store의 componentProps가 변경될 때 componentProps를 업데이트
watch(
    () => store.componentProps,
    newVal => {
        componentProps.value = newVal;
    }
);

// 팝업이 열릴 때 애니메이션을 실행
const animatePopupIn = async () => {
    await nextTick();
    gsap.set(popup.value, { opacity: 0 });
    gsap.set(section.value, { scale: 0 });

    await gsap
        .timeline()
        .to(popup.value, {
            opacity: 1,
            duration: 0.2
        })
        .to(section.value, {
            scale: 1,
            duration: 0.5,
            ease: 'back.out(1.7)',
            onComplete: () => {
                emit('open');
            }
        });
};

// 팝업 닫기
const animatePopupOut = async () => {
    if (!isVisible.value) return;
    await gsap.timeline().to(section.value, { scale: 0, duration: 0.3, ease: 'back.in(1.7)' }).to(popup.value, { opacity: 0, duration: 0.3 }, '<');
    store.hidePopup(popupId.value);
    emit('close');
};

// 팝업 열기
const onOpen = async () => {
    if (!isVisible.value) {
        store.showPopup(popupId.value);
        await animatePopupIn();
    }
};

// 팝업 닫기
const onClose = async () => {
    if (isVisible.value) {
        await animatePopupOut();
    }
};

// store의 isVisible이 변경될 때 애니메이션을 실행
watch(
    () => store.isVisible[popupId.value],
    async newVal => {
        isVisible.value = newVal;
        if (newVal) {
            await animatePopupIn();
        } else {
            await animatePopupOut();
        }
    }
);

// store의 title이 변경될 때 title을 업데이트
watch(
    () => store.title,
    newVal => {
        title.value = newVal;
    }
);

// 배경 클릭 시 팝업 닫기
// const onBackgroundClick = () => {
//     if (store.onClose) {
//         store.onClose(popupId.value);
//     }
//     onClose();
// };

// 컴포넌트가 마운트될 때 팝업이 표시 상태라면 애니메이션을 실행
onMounted(async () => {
    if (isVisible.value) {
        await animatePopupIn();
    }
});

defineExpose({ onClose, onOpen });
</script>
