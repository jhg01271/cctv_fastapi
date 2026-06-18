<template>
    <!-- 콘텐츠 영역 -->
    <div class="contents df fdc form ui">
        <!-- 검색 필드 -->
        <div class="row flex gutters1rem df aic jcsb">
            <div class="grid12-3 ul-grid12-4 lg-grid12-6 es-grid12-12">
                <div class="control-field ui form mb2-2rem">
                    <div class="field">
                        <input v-input type="text" v-calendar="'yyyy'" year v-model="outputStore.writeYear" class="datepicker w20rem radius es-w100p" @input="outputStore.btnSearch(true)" />
                    </div>
                </div>
            </div>
            <div class="df">
                <div class="fs1-5rem fw600" v-if="outputStore.isPrinting">{{ '문서를 제작중입니다. 잠시만 기다려주십시오.' }}</div>
                <div class="fs1-5rem fw400" v-if="outputStore.isPrinting">{{ ` (${outputStore.minute}분 ${outputStore.second}초 경과)` }}</div>
            </div>
        </div>

        <OverlayScrollbarsComponent
            :options="{
                scrollbars: {
                    autoHide: 'hover',
                    x: 'hidden',
                    y: 'visible'
                }
            }"
        >
            <div class="accordion df fdc rg1rem">
                <!-- 1 -->
                <div v-for="(card, index) in outputStore.outputList" :key="index" class="list">
                    <button type="button" class="radius df jcsb aic pr es-aifs" @click="toggleAccordion(index, $event)">
                        <div>
                            <div class="df aic gap1-2rem es-aifs">
                                <input class="tty2px" v-input type="checkbox" :checked="getMasterCheckedStatus(card)" disabled />
                                <div class="df aic es-fdc es-aifs">
                                    <em class="wsn us-tal us-mb1rem us-fs1-7rem">{{ card.nm }}</em>
                                    <span class="w1px h1-3rem mx1-2rem bce1e6ed us-dn"></span>
                                    <span v-html="printText(card)"></span>
                                </div>
                            </div>
                            <div class="wbka tal fw400">
                                <p :class="card.desc ? 'mt1-2rem pl3-3rem c9ea1a6' : ''">{{ card.desc }}</p>
                                <span class="wsn" :class="cardClass(card)"></span>
                            </div>
                        </div>
                        <div class="df aic gap1rem es-gap0 es-mt4px">
                            <div @click="go(card, $event)">
                                <span class="shortcut fs1-6rem fw500 es-pa es-t2-2rem es-r6rem us-t5-5rem us-r2rem">{{ '바로가기' }}</span>
                            </div>
                            <svg v-if="card.detailCd" xmlns="http://www.w3.org/2000/svg" width="10" height="6" viewBox="0 0 10 6" fill="none">
                                <path d="M9 1L5.4714 4.79026C5.21106 5.06991 4.78894 5.06991 4.5286 4.79026L1 1" stroke="black" stroke-linecap="round" />
                            </svg>
                        </div>
                    </button>
                    <div v-if="card.detailCd" ref="accordionRefs" class="segment oh">
                        <!-- 아코디언 래핑 요소 -->
                        <div class="thumbs pa2-2rem" :class="{ sthumbs: [1, 7, 8].includes(index) }">
                            <!-- sthumbs에 월 표기 일 경우 idx Nm 추가 하면 됩니다. -->
                            <div class="df aic gap8px fs1-6rem fw400 wsn">
                                <input class="tty2px" v-input="`전체선택`" type="checkbox" :checked="getCheckedStatus(card)" @click="allCheck(card, $event)" />
                            </div>
                            <div v-for="(detail, index) in card.detailList" :key="detail.id">
                                <div class="df aic gap8px fs1-6rem fw400">
                                    <input class="tty2px" v-input="t(detail.nm)" type="checkbox" v-model="detail.checked" :disabled="detail.printableCnt == 0" :readonly="detail.printableCnt == 0" />
                                    <span class="ellipsis fs1-3rem c3248f6 bc50-72-246-10 px5px py3px br4px" v-if="detail.printableCnt > 0">{{ detail.printableCnt + t('') }}</span>
                                    <!-- <em class="ellipsis fw600">{{ detail.printableCnt > 0 ? '출력가능' : '출력불가' }}</em> -->
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </OverlayScrollbarsComponent>
    </div>
</template>
<script setup>
import BaseView from '@/components/base/BaseView';
import { nextTick } from 'vue';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
const { ref, alertMsg, confirmMsg, onMounted, onUnmounted, t, btnSearch, btnDownload, formatDate } = BaseView();
import { useOutputStore } from '@/stores/safewiz/notice/output.js';
import { useButtonListStore } from '@/stores/buttonList';
import router from '@/router';
import _ from 'lodash';
const outputStore = useOutputStore();
const layoutStore = useButtonListStore();
layoutStore.useBtnList = ['btnSearch', 'btnDownload'];

onMounted(async () => {
    outputStore.btnSearch(true);
});
onUnmounted(() => {
    outputStore.clearTimer();
});

btnSearch(() => {
    outputStore.btnSearch(true);
});
btnDownload(() => {
    const printYnList = outputStore.outputList.filter(item => item.printYn === 'Y')
    if(printYnList.length !== 0){
        outputStore.btnDownload();
    }else{
        alertMsg('error', '출력 가능한 데이터가 없습니다.')
        return
    }
    
});

import { gsap } from 'gsap';
import CustomEase from 'gsap/CustomEase';
gsap.registerPlugin(CustomEase);
CustomEase.create('customEase', 'M0,0 C0.9,0 0.2,1 1,1');

// 공통 애니메이션 함수
const animateAccordion = (element, isOpen) => {
    gsap.to(element, {
        height: isOpen ? 'auto' : 0,
        duration: 0.5,
        ease: 'customEase'
    });
};

// 아코디언
const toggleAccordion = async (index, event) => {
    if(event.target.tagName !== 'INPUT' && event.target.tagName !== 'LABEL'){
        if (outputStore.outputList[index].detailCd) {
            const button = event.currentTarget;
            const segmentElement = button.nextElementSibling;

            const isOpen = button.classList.toggle('active');
            await nextTick(); // DOM 업데이트 후 애니메이션 실행 보장
            animateAccordion(segmentElement, isOpen);
        }
    }
};

// 전체선택 클릭
const allCheck = (card, event) => {
    card.allCheck = event.target.checked;
    if (card.detailCd) {
        console.log('#1', card.checked);
        card.detailList.forEach(el => {
            if (el.printableCnt > 0) {
                el.checked = card.allCheck;
            }
        });
    }
};

// 카드 선택값 제어 계산
const getMasterCheckedStatus = card => {
    card.checked = card.detailCd != null ? card.detailList.filter(el => el.checked).length > 0 : card.checked;
    return card.detailCd != null ? card.detailList.filter(el => el.checked).length > 0 : card.checked;
};
// 전체선택 체크여부 확인 값
const getCheckedStatus = card => {
    // 디테일 길이가 0 이상이고, 출력가능한 항목이 모두 선택되었을 때 true 리턴
    return card.totalPrintableCnt != 0 && card.detailList.length != 0 && card.detailList.filter(el => el.checked && el.printableCnt > 0).length === card.detailList.filter(el => el.printableCnt > 0).length;
};
// 바로가기 버튼 클릭 시
const go = (card, event) => {
    event.stopPropagation(); // 이벤트 전파 차단
    confirmMsg('warning', t(`[${card.nm}] <br /> 작성 페이지로 이동하시겠습니까?`), '', { fun: goRouter, param: card.route });
};
const goRouter = route => {
    router.push(`/${route}`);
};

// 출력 가능 여부에 따라 다른 텍스트 출력
const printText = card => {
    if (card.detailList.filter(el => el.printableCnt > 0).length > 0) {
        card.printYn = 'Y';
        return `<span class="fs1-3rem c3248f6 bc50-72-246-10 px5px py3px br4px">출력가능</span> <span class="">${card.totalPrintableCnt}건</span>`;
    } else {
        card.printYn = 'N';
        return `<span class="fs1-3rem cff3534 bc255-53-52-10 px5px py3px br4px">출력불가</span>`;
    }
};
// 출력 가능여부에 따라 다른 클래스 부여
const cardClass = card => {
    if (card.printableCnt > 0) return 'printable';
    return card.detailList.filter(el => el.printableCnt > 0).length > 0 ? 'printable' : 'unprintable';
};
</script>

<style lang="scss" scoped>
.thumbs {
    display: grid;
    grid-template-columns: repeat(4, 1fr);
    grid-auto-flow: row;
    gap: 1rem;
}
@media (max-width: 1280px) {
    .thumbs {
        grid-template-columns: repeat(3, 1fr);
    }
}

@media (max-width: 640px) {
    .thumbs {
        grid-template-columns: repeat(1, 1fr);
    }
    .sthumbs {
        grid-template-columns: repeat(3, 1fr);
    }
}
</style>
