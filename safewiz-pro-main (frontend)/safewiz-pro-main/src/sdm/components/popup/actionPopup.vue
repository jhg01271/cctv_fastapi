<template>
    <div v-if="isVisible">
        <div ref="popup" class="igns-pop" @click.self="onBackgroundClick">
            <div ref="section" class="section w30rem br4px">
                <div class="contents form ui tac">
                    <img v-if="!title" src="/assets/img/common/icon_confirm_popup.svg" alt />
                    <h3 v-if="title">{{ title }}</h3>
                    <p class="mt1rem">{{ content }}</p>
                    <div class="row mt1rem">
                        <div class="grid2-2 df">
                            <button type="button" @click="onCancel" class="btn w50p radius">
                                <span>취소</span>
                            </button>
                            <button type="button" @click="onConfirm" class="btn w50p green radius ml4px">
                                <span>확인</span>
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, watch, onMounted, nextTick, defineEmits } from 'vue';
import { useActionPopupStore } from '@/sdm/stores/actionPopupStore';
import { gsap } from 'gsap';

const emit = defineEmits(['close', 'open', 'confirm', 'cancel']);

const store = useActionPopupStore();
const isVisible = ref(store.isVisible);
const title = ref(store.title);
const content = ref(store.content);
const popup = ref(null);
const section = ref(null);

watch(
    () => store.title,
    newVal => {
        title.value = newVal;
    }
);

watch(
    () => store.content,
    newVal => {
        content.value = newVal;
    }
);

const animatePopupIn = async () => {
    await nextTick();
    gsap.set(popup.value, { opacity: 0 });
    gsap.set(section.value, { scale: 0 });

    gsap.timeline().to(popup.value, {
        opacity: 1,
        duration: 0.2,
        onComplete: () => {
            gsap.to(section.value, { scale: 1, duration: 0.5, ease: 'back.out(1.7)' });
            emit('open'); // 팝업이 열릴 때 'open' 이벤트 발생
        }
    });
};

const animatePopupOut = async () => {
    await gsap.timeline().to(section.value, { scale: 0, duration: 0.3, ease: 'back.in(1.7)' }).to(popup.value, { opacity: 0, duration: 0.3 }, '<');
    store.hideActionPopup();
    emit('close'); // 팝업이 닫힐 때 'close' 이벤트 발생
};

watch(
    () => store.isVisible,
    async newVal => {
        isVisible.value = newVal;
        if (newVal) {
            animatePopupIn();
        }
    }
);

const onBackgroundClick = () => {
    animatePopupOut();
};

const onCancel = () => {
    console.log('ActionPopup에서 cancel 이벤트 발생');
    emit('cancel'); // 취소 버튼 클릭 시 'cancel' 이벤트 발생
    animatePopupOut();
};

const onConfirm = () => {
    console.log('ActionPopup에서 confirm 이벤트 발생');
    emit('confirm'); // 확인 버튼 클릭 시 'confirm' 이벤트 발생
    animatePopupOut();
};

onMounted(() => {
    if (isVisible.value) {
        animatePopupIn();
    }
});
</script>
