<template>
    <div id="form">
        <div class="oh">
            <!-- 아코디언 영역 -->
            <div class="accordion df fdc rg8px wbka">
                <!-- 1 -->
                <template v-for="(segment, index) in dataList" :key="index">
                    <div v-show="segment.visible !== false" class="list wsn">
                        <button type="button" class="h4-4rem df jcsb aic radius" :id="`accordion-btn_${index}`" @click="toggleAccordion" :class="{ active: activeSegments[index] }">
                            <div class="df aic gap1-2rem ui w80p init">
                                <input v-input type="checkbox" v-model="segment.checked" @click="selectItem(segment)" />
                                <em class="ellipsis fw600 fs1-5rem" :title="segment[filterKey]">{{ segment[filterKey] }}</em>
                            </div>
                            <div class="df aic">
                                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none">
                                    <path d="M16 11L12.4714 14.7903C12.2111 15.0699 11.7889 15.0699 11.5286 14.7903L8 11" stroke="black" stroke-linecap="round" />
                                </svg>
                            </div>
                        </button>
                        <div ref="accordionRefs" class="segment oh">
                            <!-- 아코디언 래핑 요소 -->
                            <div class="pa1rem bcF8F9FB">
                                <div class="field df">
                                    <textarea v-model="segment[desc]" v-input type="text" class="w100p radius minh15rem fg1" readonly />
                                </div>
                            </div>
                        </div>
                    </div>
                </template>
            </div>
        </div>
    </div>
</template>

<script setup>
import { defineProps, onMounted, ref, nextTick } from 'vue';
import { gsap } from 'gsap';

const emit = defineEmits(['close', 'select']);

const props = defineProps({
    dataList: { type: Array, default: () => [] }, // 🔹 dataList props로 받음
    filterKey: { type: String, required: true }, // 🔹 필터링 기준을 props에 추가
    desc: { type: String, default: 'desc' }
});
onMounted(() => {});

const selectItem = item => {
    emit('select', item);
};

const activeSegments = ref({});

const toggleAccordion = async event => {
    //체크박스를 클릭했을 때는 아코디언 애니메이션 실행 x)
    if (event.target.tagName !== 'INPUT' && event.target.tagName !== 'LABEL') {
        const button = event.currentTarget;
        const segmentElement = button.nextElementSibling;
        const isOpen = button.classList.toggle('active');

        await nextTick();

        gsap.to(segmentElement, {
            height: isOpen ? 'auto' : 0,
            duration: 0.5,
            ease: 'customEase'
        });
    }
};
</script>
<style scoped lang="scss">
textarea {
    font-size: 1.3rem;
    line-height: 1.3;
}
.accordion {
    .list {
        > button {
            em {
                font-size: 1.5rem;
            }
        }
    }
}
</style>
