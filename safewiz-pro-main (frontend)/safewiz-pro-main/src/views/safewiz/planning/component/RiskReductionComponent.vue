<!-- 
 작성자 : SI3팀 김현재
 작성일 : 2024.11.13
 화면개요 : 위험성평가 이행 화면에서 추가 버튼을 눌렀을때 추가되는 컴포넌트
  -->
<template>
    <div class="accordion form ui mt2rem df fdc gap8px">
        <div class="list">
            <button type="button" class="df jcsb aic" @click="toggleAccordion">
                <!--🐟 2-3 Accordion Title -->
                <div class="df aic gap8px es-fww es-tal">
                    <input type="checkbox" v-input />
                    <p>감소대책</p>
                    <span class="summary">완료일<i class="c00129F">2023.03.01</i></span>
                </div>
                <svg class="shrink0" xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none">
                    <path d="M16 11L12.4714 14.7903C12.2111 15.0699 11.7889 15.0699 11.5286 14.7903L8 11" stroke="black" stroke-linecap="round" />
                </svg>
            </button>
            <div class="segment oh">
                <div class="pa2-2rem pt1-4rem bcFFFFFF">
                    <!-- 🐳2-4 Accordion Content -->
                    <div class="field">
                        <label for="">
                            <span>감소대책</span>
                        </label>
                        <input v-model="props.dataList[0].reductionMeasures" type="text" v-input class="w100p radius" placeholder="유해·위험요인 원인을 입력해주세요." />
                    </div>
                    <div class="row flex gutters2rem sm-gutters0">
                        <div class="grid12-6 sm-grid12-12">
                            <div class="field">
                                <label for="">
                                    <span>담당자</span>
                                </label>
                                <i-chips></i-chips>
                            </div>
                        </div>
                        <div class="grid12-3 sm-grid12-12">
                            <div class="field">
                                <label for="">
                                    <span>개선 예정일</span>
                                </label>
                                <input v-input type="text" v-calendar="'yyyy.MM.dd'" class="datepicker w100p radius" placeholder="2023-04-01" />
                            </div>
                        </div>
                        <div class="grid12-3 sm-grid12-12">
                            <div class="field">
                                <label for="">
                                    <span>개선 완료일</span>
                                </label>
                                <input v-input type="text" v-calendar="'yyyy.MM.dd'" class="datepicker w100p radius" placeholder="2023-04-01" />
                            </div>
                        </div>
                    </div>
                    <div class="df aic gap1rem">
                        <div class="field">
                            <label for="">
                                <span>잔존 위험성 수준</span>
                            </label>
                        </div>
                        <button type="button" class="br4px bc3248F6 cffffff pa0-8rem" @click="riskEstimationCriteria5.onOpen()">
                            <span>위험성 추정기준 5 * 4</span>
                        </button>
                    </div>
                    <div class="bcF9FAFF br4px pa2-2rem pt1rem">
                        <div class="row flex gutters1rem">
                            <div class="grid12-4 sm-grid12-12">
                                <div class="field">
                                    <label for="">
                                        <span>가능성(빈도)</span>
                                    </label>
                                    <select v-select class="w100p">
                                        <option v-for="item in 5" :key="item" :value="item">{{ item }}</option>
                                    </select>
                                </div>
                            </div>
                            <div class="grid12-4 sm-grid12-12">
                                <div class="field">
                                    <label for="">
                                        <span>중대성(강도)</span>
                                    </label>
                                    <select v-select class="w100p">
                                        <option v-for="item in 5" :key="item" :value="item">{{ item }}</option>
                                    </select>
                                </div>
                            </div>
                            <div class="grid12-4 sm-grid12-12">
                                <div class="field">
                                    <label for="">
                                        <span>위험성 결정</span>
                                    </label>
                                    <input type="text" v-input class="w100p radius" placeholder="위험성 결정" />
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="field">
                        <label for="">
                            <span>개선 조치</span>
                        </label>
                        <table>
                            <tbody>
                                <tr>
                                    <th class="w50p">개선 전</th>
                                    <th class="w50p">개선 후</th>
                                </tr>
                                <tr class="tac">
                                    <td>
                                        <!-- 이미지 첨부 개발 후 퍼블리싱 예정 -->
                                    </td>
                                    <td>
                                        <!-- 이미지 첨부 개발 후 퍼블리싱 예정 -->
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                    <div class="field">
                        <label for="">
                            <span>실행내용</span>
                        </label>
                        <input type="text" v-input class="w100p radius" placeholder="실행내용을 입력해주세요." />
                    </div>
                </div>
            </div>
        </div>
    </div>
    <!-- 위험성 추정 기준 팝업 5*4 -->
    <teleport to="body">
        <i-PopupDialog ref="riskEstimationCriteria5">
            <div class="contents form ui sm minh500px maxh80vh h100p">
                <em class="fs1-8rem">위험성 추정 기준 5 * 4</em>
                <div class="df fdc jcsb h100p">
                    <div class="field">
                        <label for="">가능성 (빈도)</label>
                        <table class="w100p br4px">
                            <thead>
                                <tr>
                                    <th class="w50p">가능성</th>
                                    <th class="w50p">기준</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td></td>
                                    <td></td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                    <div class="field">
                        <label for="">중대성 (빈도)</label>
                        <table class="w100p br4px">
                            <thead>
                                <tr>
                                    <th class="w50p">중대성</th>
                                    <th class="w50p">기준</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td></td>
                                    <td></td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                    <div class="field">
                        <label for="">위험성 결정</label>
                        <table class="w100p br4px">
                            <thead>
                                <tr>
                                    <th class="w50p">위험성 수준</th>
                                    <th class="w50p">관리 기준</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td></td>
                                    <td></td>
                                </tr>
                                <tr>
                                    <td></td>
                                    <td></td>
                                </tr>
                                <tr>
                                    <td></td>
                                    <td></td>
                                </tr>
                                <tr>
                                    <td></td>
                                    <td></td>
                                </tr>
                                <tr>
                                    <td></td>
                                    <td></td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                    <div class="df aic jcfe">
                        <button type="button" class="btn radius w8rem fr mt2-2rem" @click="riskEstimationCriteria5.onClose()">닫기</button>
                    </div>
                </div>
            </div>
        </i-PopupDialog>
    </teleport>
</template>

<script setup>
import { nextTick, onMounted, defineProps, ref } from 'vue';
import { gsap } from 'gsap';
import CustomEase from 'gsap/CustomEase';
gsap.registerPlugin(CustomEase);
CustomEase.create('customEase', 'M0,0 C0.9,0 0.2,1 1,1');

onMounted(() => {
    console.log('data check !!!!!!!!!!!!!!!!!!!!: ', props.dataList);
});

//props 선언부

const props = defineProps({
    //컴포넌트의 개수
    dataList: {
        type: Array,
        default: []
    }
});

/******************************
 * 아코디언 관련
 ******************************/
// 공통 애니메이션 함수
const animateAccordion = (element, isOpen) => {
    gsap.to(element, {
        height: isOpen ? 'auto' : 0,
        duration: 0.5,
        ease: 'customEase'
    });
};

// 요소의 중첩 레벨을 구하는 함수
const getDepth = element => {
    let depth = 0;
    while (element.parentElement) {
        if (element.parentElement.classList.contains('accordion')) break;
        if (element.parentElement.classList.contains('list')) depth++;
        element = element.parentElement;
    }
    return depth;
};

// 개별 아코디언 토글 함수
const toggleAccordion = async event => {
    console.log('ddd : ', event);
    const button = event.currentTarget;
    const segmentElement = button.nextElementSibling;

    const isOpen = button.classList.toggle('active');
    await nextTick(); // DOM 업데이트 후 애니메이션 실행 보장
    animateAccordion(segmentElement, isOpen);
};

// 모든 아코디언 열기/닫기 토글 함수
const toggleAllAccordions = () => {
    const allButtons = Array.from(document.querySelectorAll('.accordion .list > button'));
    const allSegments = Array.from(document.querySelectorAll('.accordion .segment'));

    // 전체 열림/닫힘 상태 확인
    const allOpen = allButtons.every(button => button.classList.contains('active'));

    // 모든 버튼의 활성화 상태 설정
    allButtons.forEach(button => button.classList.toggle('active', !allOpen));

    // 세그먼트를 깊이별로 그룹화
    const segmentsByDepth = {};
    allSegments.forEach(segment => {
        const depth = getDepth(segment);
        if (!segmentsByDepth[depth]) segmentsByDepth[depth] = [];
        segmentsByDepth[depth].push(segment);
    });

    // 깊이 순서대로 그룹화된 세그먼트를 순차적으로 애니메이션
    Object.keys(segmentsByDepth)
        .sort((a, b) => a - b) // 깊이 순서대로 정렬
        .forEach((depth, depthIndex) => {
            segmentsByDepth[depth].forEach((segment, segmentIndex) => {
                // 깊이별로 딜레이를 적용하여 하위 아코디언이 이전 깊이가 완료된 후 열리도록 함
                const delay = depthIndex * 0.6 + segmentIndex * 0.1;
                gsap.delayedCall(delay, () => {
                    animateAccordion(segment, !allOpen);
                });
            });
        });
};

// 위험성추정기준표
const riskEstimationCriteria5 = ref(null);
</script>

<style lang="scss" scoped>
.accordion {
    // 아코디언 헤더 버튼 요약 정보
    .summary {
        font-size: 1.4rem;
        font-weight: 400;
        display: flex;
        align-items: center;
        gap: 6px;
        padding-left: 12px;
        margin-left: 12px;
        position: relative;
        &::before {
            display: inline-block;
            width: 1px;
            height: 0.8em;
            background-color: #e1e6ed;
            left: 0;
        }

        i {
            border-radius: 4px;
            padding: 3px 6px;
            position: relative;

            &::before {
                display: inline-block;
                width: 100%;
                height: 100%;
                left: 0;
                top: 0;
                border-radius: 4px;
                background-color: currentColor;
                opacity: 0.1;
            }
        }
    }
}
</style>
