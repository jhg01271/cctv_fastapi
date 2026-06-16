<template>
    <div v-if="isVisible">
        <div ref="popup" class="igns-pop df jcc aic">
            <div ref="section" class="section form ui w60rem df fdc br4px md-w100p">
                <div class="contents pa1rem">
                    <div class="df jcc aic">
                        <img src="/assets/img/common/swal-icon_02.svg" alt="에러 아이콘" />
                    </div>
                    <div class="my1-2rem h4-5rem df jcc aic">
                        <h2 class="fs2rem fw600 lh1-5">{{ errorTitle }}</h2>
                    </div>
                    <OverlayScrollbarsComponent
                        class="maxh20rem"
                        :options="{
                            scrollbars: {
                                x: 'visible',
                                y: 'visible'
                            }
                        }"
                    >
                        <div class="accordion">
                            <div class="list">
                                <button class="df jcsb aic w100p p1rem" @click="toggleAccordion" :title="errorMessage">
                                    <em class="w95p wbka ellipsis">{{ errorMessage }}</em>
                                    <svg class="shrink0" xmlns="http://www.w3.org/2000/svg" width="10" height="6" viewBox="0 0 10 6" fill="none">
                                        <path d="M9 1L5.4714 4.79026C5.21106 5.06991 4.78894 5.06991 4.5286 4.79026L1 1" stroke="black" stroke-linecap="round" />
                                    </svg>
                                </button>
                                <div class="segment oh" ref="accordionSegment">
                                    <div class="pa1rem df">
                                        <textarea class="fg1 minh10rem wbka radius fs1-2rem" readonly :value="errorMessage"></textarea>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </OverlayScrollbarsComponent>
                </div>
                <button type="button" class="w100p btn radius mt1rem" @click="closeErrorPopup">확인</button>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, nextTick } from 'vue';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import { gsap } from 'gsap';
import CustomEase from 'gsap/CustomEase';

gsap.registerPlugin(CustomEase);
CustomEase.create('customEase', 'M0,0 C0.9,0 0.2,1 1,1');

const isVisible = ref(false);
const errorTitle = ref('');
const errorMessage = ref('');
const popup = ref(null);
const section = ref(null);
const accordionSegment = ref(null);
let closeCallback = null;

// 📩 확인용 임시코드 START
const defaultErrorTitle = '서버 에러 (Internal Server Error)';
const defaultErrorMessage = `FCM 전송 실패: 네트워크 오류 발생. 네트워크 연결을 할 수 없습니다.

FCM 전송 실패: 네트워크 오류 발생.
네트워크 연결을 확인하세요
IOException :
2025-06-16 13:43:10.335  INFO 3272 
[io-9000-exec-10]
k.c.i.f.c.a.LoggerAspect     : Controller ===>
kr.co.igns.business.alarm.controller.AlarmController.getAlarm`;
// 📩 확인용 임시코드 END

const animatePopupIn = async () => {
    await nextTick();
    gsap.set(popup.value, { opacity: 0 });
    gsap.set(section.value, { scale: 0 });

    gsap.timeline().to(popup.value, {
        opacity: 1,
        duration: 0.2,
        onComplete: () => {
            gsap.to(section.value, { scale: 1, duration: 0.5, ease: 'back.out(1.7)' });
        }
    });
};

const animatePopupOut = async () => {
    await gsap.timeline().to(section.value, { scale: 0, duration: 0.3, ease: 'back.in(1.7)' }).to(popup.value, { opacity: 0, duration: 0.3 }, '<');

    isVisible.value = false;

    // 닫기
    if (closeCallback && typeof closeCallback === 'function') {
        closeCallback();
    }
};

const openErrorPopup = (title = null, message = null, onCloseCallback = null) => {
    errorTitle.value = title /* 📩  확인용 */ || defaultErrorTitle;
    errorMessage.value = message /* 📩  확인용 */ || defaultErrorMessage;
    closeCallback = onCloseCallback;
    isVisible.value = true;

    nextTick(() => {
        animatePopupIn();
    });
};

const closeErrorPopup = () => {
    animatePopupOut();
};

const animateAccordion = (el, open) => {
    gsap.to(el, { height: open ? 'auto' : 0, duration: 0.5, ease: 'customEase' });
};

const toggleAccordion = async e => {
    const btn = e.currentTarget;
    const seg = accordionSegment.value;
    const open = btn.classList.toggle('active');
    await nextTick();
    animateAccordion(seg, open);
};

defineExpose({
    openErrorPopup,
    closeErrorPopup
});
</script>

<style lang="scss" scoped>
.accordion .list {
    > button {
        padding: 1.3rem;
        em {
            font-size: 1.6rem;
        }
    }
    .segment {
        textarea {
            font-size: 1.3rem;
        }
    }
}
</style>
