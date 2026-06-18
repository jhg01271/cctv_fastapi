<template>
    <div class="contents df fdc">
        <div class="box">
            <OverlayScrollbarsComponent
                ref="overlayScrollbars"
                class="h100p pa2-2rem"
                :options="{
                    scrollbars: {
                        autoHide: 'hover',
                        x: 'hidden',
                        y: 'visible'
                    }
                }"
            >
                <div id="form" class="control-field ui form">
                    <form @submit.prevent ref="myForm">
                        <div class="row flex gutters1rem">
                            <div class="grid12-4 es-grid12-12">
                                <div class="field">
                                    <label for="title" required>
                                        <span>제목</span>
                                    </label>
                                    <input class="br4px" v-model="shCommPlanStore.inputForm.title" type="text" id="title" v-input required placeholder="제목을 입력하세요." />
                                </div>
                            </div>
                            <div class="grid12-2 es-grid12-6">
                                <div class="field">
                                    <label for required>
                                        <span>작성년도</span>
                                    </label>
                                    <input v-model="shCommPlanStore.inputForm.writeYear" v-input type="text" v-calendar="'yyyy'" class="datepicker w100p radius" year :readonly="shCommPlanStore.inputForm.cmd === 'U'" />
                                </div>
                            </div>
                            <div class="grid12-2 es-grid12-6">
                                <div class="field">
                                    <label for required>
                                        <span>작성자</span>
                                    </label>
                                    <i-chips :chips="[{ id: shCommPlanStore.inputForm.hrId, nm: shCommPlanStore.inputForm.hrNm }]" @popup="addPeople" @removeChip="removePeople" required></i-chips>
                                </div>
                            </div>
                            <div class="grid12-2 es-grid12-6">
                                <div class="field">
                                    <label for>
                                        <span>등록일자</span>
                                    </label>
                                    <input :value="formatDate(shCommPlanStore.inputForm.createdAt)" type="text" v-calendar="getDateFormat()" class="datepicker w100p br4px" readonly />
                                </div>
                            </div>
                            <div class="grid12-2 es-grid12-6">
                                <div class="field">
                                    <label for="useYn">사용여부</label>
                                    <div class="df aic h4-4rem">
                                        <input :checked="shCommPlanStore.inputForm.useYn === 'Y'" v-input="'사용'" type="checkbox" class="df switch" @change="shCommPlanStore.toggleUseYn" />
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="segment oh mt2-2rem">
                            <div class="accordion df fdc rg8px">
                                <div class="list">
                                    <button type="button" class="radius df jcsb aic" @click="toggleAccordion">
                                        <!--🐸 내부 의사소통 -->
                                        <div class="init df aic gap2rem">
                                            <input type="checkbox" v-input @change="accordionChecked('0001', $event)" :checked="shCommPlanStore.innerList?.filter(el => el.checked).length === shCommPlanStore.innerList?.length && shCommPlanStore.innerList?.length !== 0" />
                                            <em>{{ `내부 의사소통` }}</em>
                                        </div>
                                        <svg xmlns="http://www.w3.org/2000/svg" width="10" height="6" viewBox="0 0 10 6" fill="none">
                                            <path d="M9 1L5.4714 4.79026C5.21106 5.06991 4.78894 5.06991 4.5286 4.79026L1 1" stroke="black" stroke-linecap="round" />
                                        </svg>
                                    </button>
                                    <div class="segment bcF8F9FB">
                                        <div v-for="(item, index) in shCommPlanStore.innerList" :key="item.docSeq" class="pa2-2rem">
                                            <!-- 🦖 내부 의사소통 Content -->
                                            <div class="df bd1pxsolidE1E6ED br4px bcFFFFFF es-fww">
                                                <div class="w5rem df aic jcc bdr1pxsolidE1E6ED es-h5rem es-w100p es-bdr0pxsolidE1E6ED es-bdb1pxsolidE1E6ED">
                                                    <input v-model="item.checked" type="checkbox" v-input />
                                                </div>
                                                <div class="w100p pa2-2rem">
                                                    <div class="row flex gutters1rem">
                                                        <div class="grid12-12">
                                                            <div class="field">
                                                                <label :for="`content${index}`" required>
                                                                    <span>내용</span>
                                                                </label>
                                                                <textarea :id="`content${index}`" v-model="item.content" type="text" v-input class="w100p minh10rem radius" :required="item.checked" placeholder="내용을 입력하세요" @change="onValueChanged(item)"></textarea>
                                                            </div>
                                                        </div>

                                                        <div class="grid12-6 es-grid12-12">
                                                            <div class="field">
                                                                <label for>
                                                                    <span>시기</span>
                                                                </label>
                                                                <input v-model="item.period" class="br4px" type="text" @change="onValueChanged(item)" />
                                                            </div>
                                                        </div>

                                                        <div class="grid12-6 es-grid12-12">
                                                            <div class="field">
                                                                <label for>
                                                                    <span>대상자</span>
                                                                </label>
                                                                <input v-model="item.subject" class="br4px" type="text" @change="onValueChanged(item)" />
                                                            </div>
                                                        </div>

                                                        <div class="grid12-6 ul-grid12-6 es-grid12-12">
                                                            <div class="field">
                                                                <label for>
                                                                    <span>방법</span>
                                                                </label>
                                                                <input v-model="item.method" class="br4px" type="text" @change="onValueChanged(item)" />
                                                            </div>
                                                        </div>

                                                        <div class="grid12-2 es-grid12-6">
                                                            <div class="field">
                                                                <label for>
                                                                    <span>정렬</span>
                                                                </label>
                                                                <input v-model="item.ordSeq" class="br4px" v-input type="number" min="0" max="99" step="1" placeholder="99" oninvalid="return false;" oninput="return false;" @change="onValueChanged(item, 'number')" />
                                                            </div>
                                                        </div>

                                                        <div class="grid12-1 es-grid12-6">
                                                            <div class="field wbka">
                                                                <label for="useYn">사용여부</label>
                                                                <div class="df aic h4-4rem">
                                                                    <input :checked="item.useYn === 'Y'" v-input="'사용'" type="checkbox" class="df switch" @change="onValueChanged(item, 'toggle')" />
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <!-- 추가 버튼 -->
                                        <div class="tac py1rem">
                                            <button v-button class="radius" @pointerdown="btnAddDetail('0001')">
                                                <svg class="vam" xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none">
                                                    <rect x="0.5" y="0.5" width="23" height="23" rx="11.5" fill="#EBEDFF" />
                                                    <rect x="0.5" y="0.5" width="23" height="23" rx="11.5" stroke="#3248F6" />
                                                    <path d="M17 12L7 12M12 17L12 7" stroke="#3248F6" stroke-linecap="round" />
                                                </svg>
                                                <span class="ml8px wsn">내부 의사소통 추가</span>
                                            </button>
                                        </div>
                                    </div>
                                </div>

                                <div class="list">
                                    <button type="button" class="radius df jcsb aic" @click="toggleAccordion">
                                        <!--🐸 외부 의사소통 -->
                                        <div class="init df aic gap2rem">
                                            <input type="checkbox" v-input @change="accordionChecked('0002', $event)" :checked="shCommPlanStore.externalList?.filter(el => el.checked).length === shCommPlanStore.externalList?.length && shCommPlanStore.externalList?.length !== 0" />
                                            <em>{{ `외부 의사소통` }}</em>
                                        </div>
                                        <svg xmlns="http://www.w3.org/2000/svg" width="10" height="6" viewBox="0 0 10 6" fill="none">
                                            <path d="M9 1L5.4714 4.79026C5.21106 5.06991 4.78894 5.06991 4.5286 4.79026L1 1" stroke="black" stroke-linecap="round" />
                                        </svg>
                                    </button>
                                    <div class="segment bcF8F9FB">
                                        <div v-for="(item, index) in shCommPlanStore.externalList" :key="item.docSeq" class="pa2-2rem">
                                            <!-- 🦖 외부 의사소통 Content -->
                                            <div class="df bd1pxsolidE1E6ED br4px bcFFFFFF es-fww">
                                                <div class="w5rem df aic jcc bdr1pxsolidE1E6ED es-h5rem es-w100p es-bdr0pxsolidE1E6ED es-bdb1pxsolidE1E6ED">
                                                    <input v-model="item.checked" type="checkbox" v-input />
                                                </div>
                                                <div class="w100p pa2-2rem">
                                                    <div class="row flex gutters1rem">
                                                        <div class="grid12-12">
                                                            <div class="field">
                                                                <label :for="`externalContent${index}`" required>
                                                                    <span>내용</span>
                                                                </label>
                                                                <textarea v-model="item.content" type="text" v-input :id="`externalContent${index}`" class="w100p minh10rem radius" placeholder="내용을 입력하세요" :required="item.checked" @change="onValueChanged(item)"></textarea>
                                                            </div>
                                                        </div>

                                                        <div class="grid12-6 es-grid12-12">
                                                            <div class="field">
                                                                <label for>
                                                                    <span>시기</span>
                                                                </label>
                                                                <input v-model="item.period" class="br4px" type="text" @change="onValueChanged(item)" />
                                                            </div>
                                                        </div>

                                                        <div class="grid12-6 es-grid12-12">
                                                            <div class="field">
                                                                <label for>
                                                                    <span>대상자</span>
                                                                </label>
                                                                <input v-model="item.subject" class="br4px" type="text" @change="onValueChanged(item)" />
                                                            </div>
                                                        </div>

                                                        <div class="grid12-6 ul-grid12-6 es-grid12-12">
                                                            <div class="field">
                                                                <label for>
                                                                    <span>방법</span>
                                                                </label>
                                                                <input v-model="item.method" class="br4px" type="text" @change="onValueChanged(item)" />
                                                            </div>
                                                        </div>

                                                        <div class="grid12-2 es-grid12-6">
                                                            <div class="field">
                                                                <label for>
                                                                    <span>정렬</span>
                                                                </label>
                                                                <input v-model="item.ordSeq" class="br4px" v-input type="number" min="0" max="99" step="1" placeholder="99" oninvalid="return false;" oninput="return false;" @change="onValueChanged(item, 'number')" />
                                                            </div>
                                                        </div>

                                                        <div class="grid12-1 es-grid12-6">
                                                            <div class="field wbka">
                                                                <label for="useYn">사용여부</label>
                                                                <div class="df aic h4-4rem">
                                                                    <input :checked="item.useYn === 'Y'" v-input="'사용'" type="checkbox" class="df switch" @change="onValueChanged(item, 'toggle')" />
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <!-- 추가 버튼 -->
                                        <div class="tac py1rem">
                                            <button v-button class="radius" @pointerdown="btnAddDetail('0002')">
                                                <svg class="vam" xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none">
                                                    <rect x="0.5" y="0.5" width="23" height="23" rx="11.5" fill="#EBEDFF" />
                                                    <rect x="0.5" y="0.5" width="23" height="23" rx="11.5" stroke="#3248F6" />
                                                    <path d="M17 12L7 12M12 17L12 7" stroke="#3248F6" stroke-linecap="round" />
                                                </svg>
                                                <span class="ml8px wsn">외부 의사소통 추가</span>
                                            </button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
            </OverlayScrollbarsComponent>
        </div>
        <!-- 팝업 컴포넌트 -->
        <teleport to="body">
            <i-PopupDialog ref="peoplePopup">
                <!-- 단일 그리드 -->
                <div class="contents w500px md-w100p">
                    <selectUser :single="true" @selected="selectPeople" @close="closePeoplePopup"></selectUser>
                </div>
            </i-PopupDialog>
        </teleport>
    </div>
</template>

<script setup>
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import { gsap } from 'gsap';
import router from '@/router';
import { getDateFormat } from '@/utils/dateUtil.js';

import BaseView from '@/components/base/BaseView';
const { openLoading, endLoading, ref, toastPopup, confirmMsg, alertMsg, computed, getCurrentDate, validationStore, onMounted, t, formatDate, watch, btnSearch, btnBack, btnAdd, btnSave, btnDelete, btnDownload, formatDateForDB } = BaseView();

import { useButtonListStore } from '@/stores/buttonList';
const layoutStore = useButtonListStore();

import { useShCommPlanStore } from '@/stores/safewiz/support/safetyAndHealthCommPlan.js';
const shCommPlanStore = useShCommPlanStore();

const iButtonList = ['btnBack', 'btnSave'];
const uButtonList = ['btnSearch', 'btnBack', 'btnAdd', 'btnSave', 'btnDelete', 'btnDownload'];
import { saveShCommPlan } from '@/stores/safewiz/support/api/safetyAndHealthCommPlanApi.js';
const activeSegments = ref({});
let isReverting = false; // revert 상태 여부를 추적하기 위한 플래그

watch(
    () => shCommPlanStore.inputForm.writeDt,
    (newVal, oldVal) => {
        if (newVal && shCommPlanStore.inputForm.cmd === 'U') {
            if (isReverting) {
                // 이전 값으로 되돌리는 중이면 watch를 실행하지 않음
                isReverting = false;
                return;
            }

            if (newVal === oldVal) return;

            if (newVal.substring(0, 4) !== oldVal.substring(0, 4)) {
                toastPopup('작성년도는 수정이 불가합니다.', 'error');

                // 값 재설정 시 무한 루프 방지를 위해 플래그 설정
                isReverting = true;
                shCommPlanStore.inputForm.writeDt = oldVal;
            }
        }
    }
);
// ========================================================
// Accordion
import { nextTick } from 'vue';

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

// 개별 아코디언 토글 함수
const toggleAccordion = async event => {
    const button = event.currentTarget;
    const segmentElement = button.nextElementSibling;

    const isOpen = button.classList.toggle('active');
    await nextTick(); // DOM 업데이트 후 애니메이션 실행 보장
    animateAccordion(segmentElement, isOpen);
};
// ========================================================
// Accordion
const btnAddDetail = type => {
    if (type === '0001') {
        // 내부 의사소통
        shCommPlanStore.innerList.push(shCommPlanStore.getInitDetailSet(type));
    } else if (type === '0002') {
        // 외부 의사소통
        shCommPlanStore.externalList.push(shCommPlanStore.getInitDetailSet(type));
    }
};

btnSearch(() => {
    shCommPlanStore.btnDetail(shCommPlanStore.inputForm, true);
});
btnBack(() => {
    confirmMsg('info', '저장하지 않은 정보가 있습니다. \n그래도 계속하시겠습니까?', '', { fun: goBack, param: '' });
});
const goBack = () => {
    router.push('/SafetyAndHealthCommPlan');
};
const inner = ref([]);
const external = ref([]);
btnSave(async () => {
    inner.value = shCommPlanStore.getCheckedDetailList('0001');
    external.value = shCommPlanStore.getCheckedDetailList('0002');
    // if (inner.value.length === 0 && external.value.length === 0) {
    //     toastPopup('선택된 항목이 없습니다.', 'error');
    //     return;
    // }
    // let innerValid = inner.value.filter(el => !el.content);
    // if (innerValid.length > 0) {
    //     alertMsg('warning', '(내부 의사소통) 내용은 필수 입력입니다.');
    //     return;
    // }
    // let externalValid = external.value.filter(el => !el.content);
    // if (externalValid.length > 0) {
    //     alertMsg('warning', '(외부 의사소통) 내용은 필수 입력입니다.');
    //     return;
    // }
    const isValid = await validationStore.validateAllFields('form', true);
    if (isValid) {
        // if (inner.value.filter(el => el.checked).length > 0 && inner.value.filter(el => el.checked).length > 0) return;
        confirmMsg('info', '저장하시겠습니까?', '', { fun: save, param: true });
    }
});
const save = notify => {
    shCommPlanStore.inputForm.innerList = inner.value;
    shCommPlanStore.inputForm.externalList = external.value;

    shCommPlanStore.inputForm.writeDt = formatDateForDB(shCommPlanStore.inputForm.writeDt);

    // shCommPlanStore.btnSave(notify);
    openLoading();
    saveShCommPlan(shCommPlanStore.inputForm, notify)
        .then(res => {
            layoutStore.useBtnList = uButtonList;
            shCommPlanStore.btnDetail(res.result, false);
        })
        .catch(() => {
            endLoading();
        })
        .finally(() => {
            endLoading();
        });
};

btnAdd(() => {
    confirmMsg('info', '저장하지 않은 정보가 있습니다. \n그래도 계속하시겠습니까?', '', { fun: addAction, param: '' });
});
const addAction = () => {
    shCommPlanStore.initInputForm();
};

btnDelete(() => {
    inner.value = shCommPlanStore.getCheckedDetailList('0001');
    external.value = shCommPlanStore.getCheckedDetailList('0002');
    if (inner.value.length === 0 && external.value.length === 0) {
        alertMsg('warning', t('msgNoItem'));
        return;
    }
    shCommPlanStore.btnDeleteDetail();
});
btnDownload(() => {
    shCommPlanStore.btnDownloadDetail();
});
// 팝업 열기 함수
const openPopup = popupRef => {
    if (popupRef.value) {
        popupRef.value.onOpen();
    }
};

// 팝업 닫기 함수
const closePopup = popupRef => {
    if (popupRef.value) {
        popupRef.value.onClose();
    }
};

// 인원관리 팝업
import selectUser from '@/views/base/member/SelectUser/Index.vue';
const peoplePopup = ref(null);
const addPeople = () => openPopup(peoplePopup);
const closePeoplePopup = () => closePopup(peoplePopup);
const removePeople = () => {
    shCommPlanStore.inputForm.hrId = '';
    shCommPlanStore.inputForm.hrNm = '';
};
const selectPeople = e => {
    if (peoplePopup.value) {
        peoplePopup.value.onClose();
    }
    if (e && e.hrId) {
        shCommPlanStore.inputForm.hrId = e.hrId;
        shCommPlanStore.inputForm.hrNm = e.hrNm;
    }
};

const accordionChecked = (type, event) => {
    if (type === '0001') {
        // 내부 의사소통 아코디언
        shCommPlanStore.innerList.forEach(el => {
            el.checked = event.target.checked;
        });
    } else if (type === '0002') {
        // 외부 의사소통 아코디언
        shCommPlanStore.externalList.forEach(el => {
            el.checked = event.target.checked;
        });
    }
};
const onValueChanged = (item, type) => {
    console.log('아이템', item);
    console.log('타입', type);
    if (type === 'toggle') {
        item.useYn = item.checked ? 'Y' : 'N';
    } else if (type === 'number') {
        item.ordSeq = parseInt(item.ordSeq.toString().replaceAll('-', '').replaceAll('.', ''));
    }
    item.checked = true;
};
onMounted(async () => {
    if (!shCommPlanStore.inputForm.cmd) {
        // 새로고침시 이전 화면으로 이동.
        router.go(-1);
        return;
    }
    if (shCommPlanStore.inputForm.cmd === 'I') {
        layoutStore.useBtnList = iButtonList;
    } else {
        layoutStore.useBtnList = uButtonList;
    }
});
</script>
