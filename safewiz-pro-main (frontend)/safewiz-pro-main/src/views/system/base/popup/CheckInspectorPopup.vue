<template>
    <h3 class="mb1rem" for>안전점검 담당자 관리</h3>
    <div class="form ui">
        <div class="df gap8px es-fww">
            <div class="df fg1 es-w60p">
                <input v-input type="text" class="radius search" placeholder="검색어를 입력하세요" v-model="searchTerm" @keyup.enter="btnSearch" />
                <button type="submit">
                    <img src="/assets/img/common/icon_search.svg" alt />
                </button>
            </div>
            <button class="btn radius active w7-4rem" type="submit" @click="btnSearch">조회</button>
            <button v-if="props.type === 'manage'" class="btn radius w11rem active es-w100p" type="submit" @click="btnSearchInspector">담당자 불러오기</button>
        </div>

        <h4 class="fs2rem fw600 mt1rem" :class="props.typeName ? 'py1rem' : 'mb1rem'">설비 유형 : {{ props.typeName }}</h4>
        <div class="row flex">
            <div class="grid12-12">
                <OverlayScrollbarsComponent
                    class="maxw65rem maxh35-4rem br4px table-sticky"
                    :options="{
                        scrollbars: {
                            x: 'hidden',
                            y: 'visible'
                        }
                    }"
                >
                    <table class="wbka tac md-minw70rem">
                        <colgroup>
                            <col class="w5rem" />
                            <col class="minw20rem" />
                            <col />
                        </colgroup>
                        <thead>
                            <tr>
                                <th>번호</th>
                                <th>설비명</th>
                                <th>담당자</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr v-for="(item, index) in dataFilter" :key="item.equipmentId">
                                <td>
                                    {{ index + 1 }}
                                </td>
                                <td>
                                    <input v-model="item.equipmentNm" v-input type="text" class="w100p radius" id placeholder :readonly="true" />
                                </td>
                                <td>
                                    <i-chips :chips="item.hrList" @popup="addPeople(index)" @removeChip="removeInspector" />
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </OverlayScrollbarsComponent>
            </div>
        </div>
        <div class="df jcc gap1rem mt2-5rem" v-if="props.type === 'usage'">
            <button class="btn radius w15rem" @click="popupClose">닫기</button>
        </div>
        <div class="df jcfe gap1rem mt2-5rem" v-else-if="props.type === 'manage'">
            <button class="btn w8rem radius active" @click="btnSave">저장</button>
            <button class="btn w8rem radius" @click="popupClose">닫기</button>
        </div>

        <!-- 인원 검색 팝업 컴포넌트 시작  -->
        <teleport to="body">
            <i-PopupDialog ref="peoplePopup">
                <div class="contents w50rem md-w100p">
                    <selectUser :single="false" :selected="dataFilter[selectUserIdx].hrList?.map(el => el.id)" @selected="selectPeople" @close="closePeoplePopup"></selectUser>
                </div>
            </i-PopupDialog>
        </teleport>
        <!-- 인원 검색 팝업 컴포넌트 끝  -->
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { getSafetyCheckInspector, saveSafetyCheckInspector, getEqInspector } from '@/stores/safewiz/impl/api/safetyChecklistApi.js';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import BaseView from '@/components/base/BaseView';
const { computed, confirmMsg, alertMsg, t, getCompId } = BaseView();

const searchTerm = ref('');
const dataFilter = ref([]);
const dataList = ref([]);

const props = defineProps({
    single: { type: Boolean, default: true },
    type: { type: String, default: 'manage' },
    typeId: { type: String, default: '' },
    typeName: { type: String, default: '' }
});

const emit = defineEmits(['close', 'select']);

//TODO: 변경되었을 때 감지하는 코드 만들어야됨
const checkedValues = computed(() => {
    // let list = dataFilter.value.filter(el => el.checked === true);
    // return list;
});

// 버튼 이벤트
const btnSearch = notify => {
    getSafetyCheckInspector({ searchText: props.typeId, compId: getCompId() }, notify)
        .then(res => {
            dataList.value = res.list;
            applyFilter();
        })
        .finally(() => {});
};

// 담당자 불러오기
const btnSearchInspector = () => {
    confirmMsg('info', '담당자를 불러오시겠습니까?', '', { fun: getInspector, param: '' });
};

const getInspector = async () => {
    searchTerm.value = '';
    getEqInspector({ searchText: props.typeId, compId: getCompId() }, false)
        .then(res => {
            dataList.value = res.list;
            applyFilter();
        })
        .finally(() => {});
};

// 저장
const btnSave = () => {
    if (dataFilter.value.length === 0) {
        alertMsg('warning', t('msgSaveNoItem'));
        return;
    }
    confirmMsg('info', '저장하시겠습니까?', '', { fun: emitSave, param: '' });
};

const emitSave = () => {
    saveSafetyCheckInspector(dataFilter.value, true).then(res => {
        if (res && res.success) {
            btnSearch(false);
        }
    });
};

// 닫기
const popupClose = () => {
    // if (checkedValues.value.length > 0) {
    //     confirmMsg('info', '저장하지 않은 정보가 있습니다. \n그래도 계속하시겠습니까?', '', { fun: emitClose, param: '' });
    // } else {
    emit('close');
    //}
};
//TODO: 변경되었을 때 감지하는 코드 만들어야됨
// const emitClose = () => {
//     emit('close');
// };

// 필터 적용 함수
const applyFilter = () => {
    // const filteredData = dataList.value.filter(item => item.equipmentNm.toLowerCase().includes(searchTerm.value.toLowerCase()));
    const filteredData = dataList.value.filter(item => item.equipmentNm.toLowerCase().includes(searchTerm.value.toLowerCase()) || item.hrList.some(hr => hr.nm.toLowerCase().includes(searchTerm.value.toLowerCase())));
    dataFilter.value = filteredData;
};

onMounted(async () => {
    btnSearch(true);
});

//-----------------------------------------------
// [인원 팝업]

import selectUser from '@/views/base/member/SelectUser/Index.vue';
import { useSelectUserStore } from '@/views/base/member/SelectUser/SelectUser';

const selectUserStore = useSelectUserStore();

const peoplePopup = ref(null); // PopupDialog에 대한 ref
const selectUserIdx = ref(null);

const addPeople = idx => {
    selectUserIdx.value = idx;
    peoplePopup.value.onOpen();
};

const closePeoplePopup = () => {
    if (peoplePopup.value) {
        peoplePopup.value.onClose();
    }
};

const selectPeople = () => {
    if (peoplePopup.value) {
        peoplePopup.value.onClose();

        const users = selectUserStore.getSelectedUser();
        dataFilter.value[selectUserIdx.value].hrList = users.map(user => ({
            id: user.hrId,
            nm: user.hrNm
        }));
    }
};

//-----------------------------------------------
</script>
