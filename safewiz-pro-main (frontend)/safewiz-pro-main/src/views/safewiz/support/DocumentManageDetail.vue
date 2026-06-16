<template>
    <div id="form" class="contents df fdc">
        <OverlayScrollbarsComponent
            ref="overlayScrollbars"
            class="h100p box pa2-2rem"
            :options="{
                scrollbars: {
                    autoHide: 'hover',
                    x: 'hidden',
                    y: 'visible'
                }
            }"
        >
            <div @submit.prevent ref="myForm">
                <div v-if="docMngStore.inputForm[0]" class="control-field ui form">
                    <div class="row flex gutters1rem">
                        <div class="grid12-3 sm-grid12-6">
                            <div class="field">
                                <label for required>
                                    <span>작성일자</span>
                                </label>
                                <input type="text" v-calendar="getDateFormat()" class="datepicker w100p radius" placeholder="2024.03.20" v-model="docMngStore.inputForm[0].writeDt" />
                            </div>
                        </div>

                        <div class="grid12-3 sm-grid12-6">
                            <div class="field">
                                <label required>
                                    <span>작성자</span>
                                </label>
                                <i-chips :chips="[{ id: docMngStore.inputForm[0].writerId, name: docMngStore.inputForm[0].writerNm }]" @click="addPeople()" required></i-chips>
                            </div>
                        </div>

                        <div class="grid12-3 es-grid12-6">
                            <div class="field">
                                <label for="createdAt">{{ t('createdAt') }}</label>
                                <input type="text" class="datepicker w100p radius" id="createdAt" disabled :value="formatDate(docMngStore.inputForm[0].createdAt)" placeholder="ex) 2024.11.20" />
                            </div>
                        </div>

                        <div class="grid12-3 es-grid12-6">
                            <div class="field">
                                <label for="useYn">사용여부</label>
                                <div class="df aic h4-4rem">
                                    <input :checked="docMngStore.inputForm[0].useYn === 'Y'" v-input="'사용'" type="checkbox" class="df switch" @change="toggleUseYn" />
                                </div>
                            </div>
                        </div>

                        <div class="grid12-12 sm-grid12-12">
                            <div class="field">
                                <label required for="DN">
                                    <span>문서명</span>
                                </label>
                                <div class="df aic h4-4rem">
                                    <input v-input id="DN" type="text" required class="radius validation" v-model="docMngStore.inputForm[0].manageDocNm" placeholder="문서제목을 입력하세요." />
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="accordion df fdc gap8px mt2rem">
                        <div class="list" v-for="(accordion, index) in docMngStore.inputForm" :key="index">
                            <button type="button" class="radius w15rem df jcsb aic" @click="toggleAccordion(index)" :class="{ active: activeSegments[index] }">
                                <!--🐸 title -->
                                <div class="df aic es-fdc es-aifs">
                                    <em>{{ accordion.manageDocTypeNm }}</em>
                                    <span class="w1px h1-3rem mx1-2rem bce1e6ed us-dn"></span>
                                    <span>{{ accordion.count == undefined ? '0' : accordion.count }}</span>
                                </div>
                                <svg xmlns="http://www.w3.org/2000/svg" width="10" height="6" viewBox="0 0 10 6" fill="none">
                                    <path d="M9 1L5.4714 4.79026C5.21106 5.06991 4.78894 5.06991 4.5286 4.79026L1 1" stroke="black" stroke-linecap="round" />
                                </svg>
                            </button>
                            <div class="segment oh bcF8F9FB" :ref="setAccordionRef(index)">
                                <div class="pa2-2rem">
                                    <!-- 🦖 Content -->
                                    <iFileList :ref="el => (fileListRefs[index] = el)" targetType="DOC" :targetId="docMngStore.inputForm[index].files && docMngStore.inputForm[index].files.length > 0 ? docMngStore.inputForm[index].files : []" />
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <!-- 인원 검색 팝업 컴포넌트 시작  -->
            <teleport to="body">
                <i-PopupDialog ref="peoplePopup">
                    <!-- 단일 그리드 -->
                    <div class="contents w500px md-w100p">
                        <selectUser :single="true" @close="closePeoplePopup" @selected="selectPeople"></selectUser>
                    </div>
                </i-PopupDialog>
            </teleport>
            <!-- 인원 검색 팝업 컴포넌트 끝  -->
        </OverlayScrollbarsComponent>
    </div>
</template>

<script setup>
import IFileList from '@/components/file/iFileList.vue';
import { ref, onMounted } from 'vue';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import BaseView from '@/components/base/BaseView';
import { gsap } from 'gsap';
import router from '@/router';
import { useButtonListStore } from '@/stores/buttonList';
import _ from 'lodash';
import { getDateFormat } from '@/utils/dateUtil.js';
const layoutStore = useButtonListStore();
const fileListRefs = ref([]);

const { t, validationStore, formatDate, watch, btnBack, btnSearch, btnAdd, btnSave, btnDelete, toastPopup, confirmMsg, getCompId } = BaseView();

layoutStore.useBtnList = ['btnBack', 'btnSearch', 'btnSave', 'btnDelete'];

import { useDocMngStore } from '@/stores/safewiz/support/docMng.js';
const docMngStore = useDocMngStore();

// 아코디언 상태 관리
const activeSegments = ref({});
const accordionRefs = ref([]);

// 아코디언 참조 설정
const setAccordionRef = index => el => {
    accordionRefs.value[index] = el;
};

// 아코디언 토글 함수
const toggleAccordion = index => {
    activeSegments.value[index] = !activeSegments.value[index];
    const segment = accordionRefs.value[index];

    if (segment) {
        if (activeSegments.value[index]) {
            // 열림: scrollHeight를 사용하여 자연스럽게 열기
            gsap.to(segment, {
                height: segment.scrollHeight + 'px',
                duration: 0.5,
                ease: 'power1.out',
                onComplete: () => {
                    segment.style.height = 'auto'; // 열림 후 height 값을 'auto'로 변경
                }
            });
        } else {
            // 닫힘: 높이를 0으로 설정하여 슬라이드처럼 닫기
            gsap.to(segment, {
                height: 0,
                duration: 0.5,
                ease: 'power1.in'
            });
        }
    }
};

const accordionItems = ref([]);

onMounted(async () => {
    if (docMngStore.inputForm[0]?.cmd == 'I') {
        layoutStore.useBtnList = ['btnBack', 'btnSave'];
    } else {
        layoutStore.useBtnList = ['btnBack', 'btnSearch', 'btnSave', 'btnDelete'];
    }
    docMngStore.file(fileListRefs);
});

btnBack(() => {
    docMngStore.goBack();
});

btnSearch(() => {
    docMngStore.getMngDocDetail(false);
});
btnSave(async () => {
    const isValid = await validationStore.validateAllFields('form', true);
    if (isValid) {
        await docMngStore.btnSave();
    }
});
btnDelete(() => {
    confirmMsg('warning', t('msgDelete'), '', {
        fun: docMngStore.deleteDetailDocs,
        param: docMngStore.inputForm
    });
});

onMounted(() => {
    if (!docMngStore.inputForm[0]) {
        // 새로고침시 이전 화면으로 이동.
        router.go(-1);
        return;
    }
});
//-----------------------------------------------
//useYn 체크

const toggleUseYn = event => {
    // 체크박스의 체크 상태에 따라 'Y' 또는 'N'을 설정합니다.
    docMngStore.inputForm.forEach(el => {
        el.useYn = _.cloneDeep(event.target.checked ? 'Y' : 'N');
    });
};
//-----------------------------------------------
//-----------------------------------------------
// [인원 팝업]

import selectUser from '@/views/base/member/SelectUser/Index.vue';
import { useSelectUserStore } from '@/views/base/member/SelectUser/SelectUser';

const selectUserStore = useSelectUserStore();

const peoplePopup = ref(null); // PopupDialog에 대한 ref
const peopleIdx = ref('');

const addPeople = idx => {
    // peopleIdx.value = idx;
    peoplePopup.value.onOpen();
};

//인원 팝업 선택
const selectPeople = e => {
    console.log('selectPeople', e);

    docMngStore.inputForm.forEach(el => {
        el.writerId = e.hrId;
        el.writerNm = e.hrNm;
    });

    if (peoplePopup.value) {
        peoplePopup.value.onClose();
    }
};
const closePeoplePopup = () => {
    if (peoplePopup.value) {
        peoplePopup.value.onClose();
    }
};
//-----------------------------------------------
</script>

<style scoped lang="scss">
.container {
    .contents {
        .box {
            z-index: 15;
        }
    }
}
</style>
