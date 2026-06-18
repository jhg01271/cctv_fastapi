<template>
    <div class="form ui">
        <!-- 콘텐츠 영역 -->
        <div class="df aic gap8px">
            <h3>{{ title }}</h3>
            <button class="mb1rem" v-if="infoMsg" v-tooltip="infoMsg">
                <!-- 팝업 info 아이콘 -->
                <img class="vam neg-tty1px es-w1-7rem es-h1-7rem" src="/assets/img/common/icon_warning.svg" alt />
            </button>
        </div>
        <div class="df mb1-2rem">
            <input v-if="props.calendarInfo?.key" v-model="searchDate" class="maxw20rem mr1rem datepicker radius" v-input type="text" v-calendar="props.calendarInfo?.type" year @input="changeDate" />
            <input v-input type="text" class="radius search" placeholder="검색어를 입력하세요" v-model="searchTerm" @keyup.enter="applyFilter" />
            <button type="submit">
                <img src="/assets/img/common/icon_search.svg" alt @click="applyFilter" />
            </button>
        </div>

        <div class="df aic mb1-2rem" v-show="!single">
            <div class="df aic">
                <input type="checkbox" id="baseCheckAll" @change="handleAllCheck" v-input="'전체 선택'" />
            </div>
        </div>

        <div class="bd1pxsolide1e6ed br4px">
            <OverlayScrollbarsComponent class="pa1-6rem maxh30rem">
                <div v-if="dataList?.length === 0" class="df aic jcc minh20rem fs2rem c999999">
                    {{ '데이터가 없습니다.' }}
                </div>
                <component v-else :is="props.component" :key="renderingKey" :dataList="dataList" :filterKey="props.filterKey" :desc="props.desc" @select="selectItem" :fields="props.fields" />
            </OverlayScrollbarsComponent>
        </div>
        <i-PopupButtonList :btnInfo="{ closeBtn: btnInfo.close, saveBtn: btnInfo.save, deleteBtn: btnInfo.delete, applyBtn: !single }" :localInputForm="localInputForm" @close="close" @apply="apply" />
    </div>
</template>

<script setup>
import { defineEmits, defineModel, onMounted, ref } from 'vue';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import BaseSelectComponet from '@/views/system/base/popup/popupComponent/BaseSelectComponet.vue';

import BaseView from '@/components/base/BaseView';
const { alertMsg, t, openLoading, endLoading, computed } = BaseView();
const emit = defineEmits(['close', 'selected']);

const props = defineProps({
    title: {
        type: String,
        default: null
    },
    //true : 싱글모드, false : 멀티
    single: {
        type: Boolean,
        default: true
    },
    filterKey: {
        // 필터링 기준을 props에 추가
        type: String,
        required: true
    },
    desc: {
        // 아코디언 내부 데이터 props에 추가
        type: String,
        default: 'desc'
    },
    // 검색 방식 설정: 'input' 또는 'button'
    useSearch: {
        type: String,
        default: 'input' // 기본값은 'input'
    },
    btnInfo: {
        // Todo: 버튼 설정 (저장, 닫기) 현재 미구현
        type: Object,
        default: () => ({ close: true, save: false, delete: false, apply: false }) // 기본값 설정
    },
    fetchData: {
        type: Function,
        required: true // 반드시 API 호출 함수를 받아야 함
    },
    fetchParam: {
        type: Object, // API 호출 시 필요한 매개변수
        required: false
    },
    useFilter: {
        // useYn 필터링 여부를 위한 속성 추가
        type: Boolean,
        default: false
    },
    uniqueKey: {
        // 데이터 필드 키 값
        type: String,
        default: '' // 기본적으로 useYn으로 설정
    },
    useYnKey: {
        // useYn 필터링 키를 위한 속성 추가
        type: String,
        default: 'useYn' // 기본적으로 useYn으로 설정
    },
    excludedValue: {
        // 제외할 값 (예: 'N')
        type: String,
        default: 'Y' // 기본적으로 'N'으로 설정
    },
    selectedIdList: {
        // 이미 선택된 값
        type: Array,
        default: () => []
    },
    infoMsg: {
        // 팝업 창 관련 안내 메시지
        type: String,
        default: null
    },
    component: {
        // 팝업 창 관련 안내 메시지
        type: [Object, Function],
        default: BaseSelectComponet
    },
    checkFetchDataEmpty: {
        // fetchData가 비었는지 확인
        type: Boolean,
        default: false
    },
    calendarInfo: {
        // 검색조건 캘린더 사용여부
        type: Object,
        default: null // 기본적으로 미사용,
    },
    fields: {
        // 컴포넌트 필드 커스터마이징
        type: Array,
        default: () => [] // 기본적으로 미사용
    }
});

const searchDate = computed({
    get: () => props.fetchParam[props.calendarInfo?.key],
    set: val => emit('update:fetchParam', { ...props.fetchParam, searchDate: val })
});

const localInputForm = defineModel('inputForm');
const dataList = ref([]);
const searchTerm = ref('');

onMounted(async () => {
    // selectUserStore.init(props.single)\
    let responses = null;
    if (props.fetchParam) {
        openLoading();
        responses = await Promise.all([props.fetchData(props.fetchParam)]);
        await endLoading();
    } else {
        openLoading();
        responses = await Promise.all([props.fetchData()]);
        await endLoading();
    }
    setDataList(responses);
});

// 데이터리스트 세팅 함수
const setDataList = responses => {
    if (responses) {
        dataList.value = responses[0].list;
        initData();

        if (responses[0].list.length === 0 && props.checkFetchDataEmpty) {
            alertMsg('warning', '등록 가능한 샘플 데이터가 없습니다.');
            emitCloseWithSelectedItems();
        }
    }
};

// 필터 적용 함수
const applyFilter = () => {
    // dataList가 null이면 무조건 종료
    if (!dataList.value) return;

    const searchTermLower = searchTerm.value.toLowerCase(); // 검색어를 소문자로 변환
    const filterKey = props.filterKey; // props에서 필터링 기준을 가져옴
    const desc = props.desc; // props에서 필터링 기준을 가져옴
    const useYnKey = props.useYnKey; // useYn을 판단할 키를 가져옴
    const excludedValue = props.excludedValue; // 필터링할 제외 값 (예: 'N')

    // props.filterKey 또는 props.useYnKey, props.excludedValue 중 하나라도 존재하면 필터링 진행
    if (!filterKey && !useYnKey && !excludedValue) return;

    // 필터링된 데이터로 업데이트
    dataList.value.forEach(item => {
        const matchesFilterKey = filterKey ? Boolean(item[filterKey]?.toLowerCase().includes(searchTermLower)) || Boolean(item[desc]?.toLowerCase().includes(searchTermLower)) : true; // 동적으로 필터링
        const matchesUseYn = useYnKey ? Boolean(item[useYnKey] !== excludedValue) : true; // useYn 필터링 여부
        item.visible = matchesFilterKey && matchesUseYn;
    });
};

//#region =====[F] 배열 뿐 아니라 Object 처리도 추가 하여 input 에서 선택된 값 찾게 처리 - 초기 데이터 설정 (예: API 호출 후) =====
const renderingKey = ref(0);
const initData = () => {
    if (Array.isArray(props.selectedIdList) && dataList.value.length > 0 && props.uniqueKey) {
        let selectedIds = props.selectedIdList.map(item => (typeof item === 'object' && item !== null ? item[props.uniqueKey] : item));

        dataList.value = dataList.value.map(data => ({
            ...data,
            selected: selectedIds.includes(data[props.uniqueKey]) // `inspectionId` 리스트와 비교
        }));
        renderingKey.value++; // 초기화 시 화면을 다시 렌더링하여 체크박스를 새로 그림
    }
    applyFilter(); // 필터를 처음에 적용하여 초기 데이터로 그리드를 채움
};
//#endregion

// 선택된 아이템을 필터링하는 헬퍼 함수
const getSelectedItems = () => dataList.value.filter(item => item.selected);

// 'close' 이벤트를 발생시키고 선택된 아이템 목록을 전달하는 함수
const emitCloseWithSelectedItems = () => {
    emit('close', getSelectedItems());
};

// 'close' 이벤트를 발생시키는 함수
const close = emitCloseWithSelectedItems;

// 'apply' 이벤트를 발생시키고 선택된 아이템 목록을 전달하는 함수
const emitApplyWithSelectedItems = () => {
    if (getSelectedItems().length === 0) {
        alertMsg('warning', t('msgNoItem'));
        return;
    }
    emit('apply', getSelectedItems());
};

// 'apply' 이벤트를 발생시키는 함수
const apply = emitApplyWithSelectedItems;

//#region [아이템 선택 처리 함수 변경]
const selectItem = item => {
    if (props.single) {
        // 모든 항목의 `selected`를 `false`로 초기화
        dataList.value.forEach(data => {
            data.selected = false;
        });
        // 현재 클릭한 항목만 `selected` = `true`
        item.selected = true;
        // 선택 후 창 닫기
        emitCloseWithSelectedItems();
    } else {
        // 멀티 선택 모드에서는 기존 로직 유지
        item.selected = !item.selected;
        emit('selected', getSelectedItems());
    }
};

// 전체 선택/해제 처리 함수
const handleAllCheck = event => {
    const isChecked = event.target.checked;

    if (dataList.value && dataList.value.length > 0) {
        dataList.value.forEach(item => {
            item.selected = isChecked;
            item.checked = isChecked;
        });
    }
};

// date 변경 시 데이터리스트 재조회
const changeDate = async event => {
    let responses = null;
    const param = props.fetchParam;
    const before = param[props.calendarInfo.key];
    param[props.calendarInfo.key] = event.target.value;
    openLoading();
    responses = await Promise.all([props.fetchData(param)]);
    await endLoading();

    setDataList(responses);
    param[props.calendarInfo.key] = before;
};

defineExpose({ initData });
//#endregion
</script>
