<template>
    <div v-if="isVisible">
        <div :class="['toast-popup', toastType]">
            <div v-for="section in sections" :key="section.id" class="section" :ref="el => setSectionRef(el, section.id)" @click.self="onBackgroundClick">
                <h4>{{ section.title }}</h4>
                <p>{{ section.content }}</p>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, watch, nextTick } from 'vue';
import { useToastPopupStore } from '@/sdm/stores/toastPopupStore';
import { gsap } from 'gsap';

defineProps({
    toastType: {
        type: String,
        default: 'success',
        validator: value => ['success', 'error', 'info'].includes(value)
    }
});

const store = useToastPopupStore();
const isVisible = ref(store.isVisible);
const sections = ref(store.sections);
const sectionRefs = {};
const initializedSections = new Set();

const setSectionRef = (el, id) => {
    if (el) {
        sectionRefs[id] = el;
        if (!initializedSections.has(id)) {
            animatePopupIn(id);
            initializedSections.add(id);
            setTimeout(() => animatePopupOut(id), 1000); // 3초 후에 사라지도록 타이머 설정
        }
    } else {
        delete sectionRefs[id];
    }
};

const animatePopupIn = async id => {
    await nextTick();
    const sectionElement = sectionRefs[id];
    gsap.fromTo(sectionElement, { y: -30, opacity: 0 }, { y: 0, opacity: 1, duration: 0.5, ease: 'power2.out' });
};

const animatePopupOut = async id => {
    const sectionElement = sectionRefs[id];
    if (sectionElement) {
        await gsap.timeline().to(sectionElement, { y: -30, opacity: 0, duration: 0.2, ease: 'power2.in' }).to(sectionElement, { height: 0, duration: 0.2, ease: 'back.in(1)' }, '<');
        store.hideToast(id);
        initializedSections.delete(id);
    }
};

watch(
    () => store.sections,
    newVal => {
        sections.value = newVal;
    }
);

watch(
    () => store.isVisible,
    newVal => {
        isVisible.value = newVal;
    }
);

const onBackgroundClick = () => {
    sections.value.forEach(section => animatePopupOut(section.id));
};
</script>
