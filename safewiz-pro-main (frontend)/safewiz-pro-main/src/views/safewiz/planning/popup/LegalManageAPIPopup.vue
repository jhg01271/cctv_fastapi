<template>
    <!-- 콘텐츠 영역 -->
    <h3>한국산업안전보건공단_안전보건법령</h3>
    <div class="form ui">
        <div class="df gap8px es-fww">
            <div class="df fg1 es-w60p">
                <input v-input type="text" class="radius search" placeholder="검색어를 입력하세요" v-model="searchTerm" @change="applyFilter" @keyup.enter="applyFilter" />
                <button type="submit">
                    <img src="/assets/img/common/icon_search.svg" alt />
                </button>
            </div>
            <button class="btn radius active w7-4rem" type="submit" @click="btnSearch">조회</button>
        </div>
        <!-- 전체 선택 / 해제 -->
        <div class="my1-2rem df aic">
            <label class="fg1 c3248f6 fs1-7rem fw600">{{ props.title }}</label>
            <input type="checkbox" id="checkAll" v-model="checkAll" @change="toggleCheckAll" v-input="'전체 선택'" />
        </div>

        <OverlayScrollbarsComponent
            class="maxh50rem br4px"
            :options="{
                scrollbars: {
                    x: 'hidden',
                    y: 'visible'
                }
            }"
        >
            <div id="form">
                <!-- 아코디언 영역 -->
                <div class="accordion df fdc gap1-2rem wbka">
                    <!-- 1 -->
                    <div v-for="(item, index) in props.popupDataList" :key="item.docId" class="list lgmd" :class="{ disabled: item.useYn === 'N' }">
                        <button type="button" class="df jcsb aic" @click="toggleAccordion(index)" :class="{ active: isAccordionState[index] }">
                            <div class="df aic gap1rem es-w90p">
                                <div @click.stop>
                                    <input type="checkbox" v-input v-model="item.checked" />
                                </div>
                                <em class="ellipsis">{{ item.update ? `${item.title} (Update)` : item.title }}</em>
                            </div>
                            <svg class="shrink0" xmlns="http://www.w3.org/2000/svg" width="10" height="6" viewBox="0 0 10 6" fill="none">
                                <path d="M9 1L5.4714 4.79026C5.21106 5.06991 4.78894 5.06991 4.5286 4.79026L1 1" stroke="black" stroke-linecap="round" />
                            </svg>
                        </button>

                        <div :ref="el => (accordionRefs[index] = el)" class="segment oh">
                            <!-- 아코디언 래핑 요소 -->
                            <div class="pa1rem bcF8F9FB">
                                <div class="control-field mb1-2rem" :id="item.checked ? 'formDetail' + index : null">
                                    <div class="row flex gutters1rem">
                                        <div class="grid12-12">
                                            <div class="field">
                                                <label :for="'articleTitle' + index">
                                                    <span class="fs1-4rem">{{ t('articleTitle') }}</span>
                                                </label>
                                                <input class="br4px mt4px" :id="'articleTitle' + index" :value="item.title" v-input type="text" placeholder="제2조2항해당 조항" @input="item.title = $event.target.value" readOnly />
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <div class="field">
                                    <div class="field df fdc">
                                        <label>법규 내용</label>
                                        <textarea class="mt4px br4px minh14rem" rows="5" placeholder="법규 규제치 및 기타 요구사항" :value="item.content" readOnly></textarea>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </OverlayScrollbarsComponent>
    </div>
    <div class="pr df jcfe gap8px mt2rem">
        <button class="btn w7-4rem radius active mr8px" @click="btnSave">{{ t('apply') }}</button>
        <button class="btn w7-4rem radius" @click="btnClose">{{ t('close') }}</button>
    </div>
</template>

<script setup>
import { ref, defineProps, defineEmits, nextTick, watch } from 'vue';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import { gsap } from 'gsap';
import CustomEase from 'gsap/CustomEase';
import BaseView from '@/components/base/BaseView';
const { validationStore, t } = BaseView();
const emit = defineEmits(['filter', 'search', 'sample', 'add', 'delete', 'save', 'close']);

const searchTerm = ref('');

const props = defineProps({
    popupDataList: { type: Array, default: () => [] },
    title: { type: String, default: '' },
    selectedList: { type: Array, default: () => [] }
});

// 전체 선택/해제 기능
const checkAll = ref(false);
const toggleCheckAll = () => {
    props.popupDataList.forEach(item => {
        item.checked = checkAll.value;
    });
};

// index별 열림 상태를 저장
const isAccordionState = ref([]);
const toggleAccordion = async index => {
    clearValidationStore();

    isAccordionState.value[index] = !isAccordionState.value[index];

    await nextTick();
    await accordionSet(index, 0.5);
};

// 아코디언 토글 이벤트
gsap.registerPlugin(CustomEase);
CustomEase.create('customEase', 'M0,0 C0.9,0 0.2,1 1,1');
const accordionRefs = ref([]);
const accordionSet = async (index, duration) => {
    const segment = accordionRefs.value[index];
    if (segment) {
        gsap.to(segment, {
            height: isAccordionState.value[index] ? segment.scrollHeight + 'px' : 0,
            duration: duration,
            ease: 'customEase'
        });
    } else {
        console.warn(`GSAP target for index ${index} not found`);
    }
};

// 체크 상태 및 업데이트 상태 동기화
const updateCheckState = () => {
    props.popupDataList.forEach(item => {
        const existingItem = props.selectedList.find(selected => selected.refId === item.doc_id);
        
        if (existingItem) {
            item.checked = true;
            
            item.update = existingItem.articleTitle !== item.title || existingItem.articleContent !== item.content;
        } else {
            item.checked = false;
            item.update = false;
        }
    });
};

// selectedList가 변경될 때도 체크 상태 동기화
watch(() => props.selectedList, () => {
    if (props.popupDataList && props.popupDataList.length > 0) {
        updateCheckState();
    }
}, { immediate: true, deep: true });


// 검색
const applyFilter = () => {
    clearValidationStore();
    checkAll.value = false;
    // 검색어를 emit하여 보냄
    emit('filter', searchTerm.value);
};

// 조회
const btnSearch = async () => {
    clearValidationStore();
    emit('search');
};

// 저장
const btnSave = async () => {
    clearValidationStore();
    checkAll.value = false;
    const selectedItems = props.popupDataList.filter(item => item.checked);
    emit('save', selectedItems);
};

// 닫기
const btnClose = () => {
    clearValidationStore();
    emit('close');
};

const clearValidationStore = () => {
    validationStore.clearInvalidClasses();
    validationStore.clearAllErrors();
};
</script>

<style scoped lang="scss">
.form.ui {
    .accordion {
        .list {
            > button {
                padding: 1rem;
                em {
                    font-size: 1.5rem;
                }
            }
        }
    }
    .field {
        > label {
            font-size: 1.4rem;
            height: 21px;
            line-height: 21px;
        }
    }
    textarea,
    input {
        padding: 4px;
        font-size: 1.3rem;
    }
}
</style>
