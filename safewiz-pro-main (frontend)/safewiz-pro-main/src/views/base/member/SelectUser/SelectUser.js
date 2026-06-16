import { ref, computed } from 'vue';
import { defineStore } from 'pinia';
import { getHr } from '@/stores/system/base/api/hrApi';
import { getOrganization } from '@/stores/system/base/api/organizationApi';
import { getPartner } from '@/stores/system/base/api/partnerApi';
import { loadFileFromServer } from '@/utils/iFileLoader.js';
import _ from 'lodash';
import BaseView from '@/components/base/BaseView';

export const useSelectUserStore = defineStore('selectUser', () => {
    const { openLoading, endLoading } = BaseView();

    const menu = ref([]);
    const menuList = ref({
        orgnList: [],
        partCompList: []
    });
    const userList = ref([]);
    const originUserList = ref([]);
    const originMenuList = ref([]);

    const selectedIndex = ref(0);
    const selectedIds = ref([]);
    const currentTab = ref('orgn');
    const clickTab = el => {
        currentTab.value = el;
        clickItem(0);
    };
    const filteredUserList = ref([]);
    const filterFromString = async () => {
        // initData();
        if (filterValue.value !== '') {
            // 검색어가 있을 경우 userList에서 필터링
            let filteredUsers = userList.value.filter(el => {
                return el.hrNm.includes(filterValue.value);
            });
            Object.keys(originMenuList.value).forEach(key => {
                // 조직명 또는 사람 이름으로 필터링
                const filterLowerCase = filterValue.value.toLowerCase();
                const filteredMenuList = originMenuList.value[key]?.filter(item => {
                    // 조직명/협력사명에 대한 필터링
                    const nameMatch = item.orgnNm?.toLowerCase().includes(filterLowerCase) || item.partCompNm?.toLowerCase().includes(filterLowerCase);
                    // 사용자 이름에 대한 필터링 - 해당 조직/협력사에 속한 사용자들 중 매칭되는지 확인
                    const userMatch = filteredUsers.some(user => (currentTab.value === 'orgn' && user.orgnId === item.orgnId) || (currentTab.value === 'partComp' && user.partCompId === item.partCompId));
                    return item.useYn !== 'N' && (nameMatch || userMatch);
                });
                menuList.value[key] = filteredMenuList; // 각 리스트를 갱신
            });
            clickItem(0);
        } else {
            menuList.value = _.cloneDeep(originMenuList.value);
        }
        initData(selectedIds.value);
    };

    const clickItem = async el => {
        openLoading();
        // UI 렌더링을 위한 약간의 지연 허용
        await new Promise(resolve => setTimeout(resolve, 10));

        selectedIndex.value = el;

        // 조직에 따른 사용자 목록 가져오기
        let filteredUsers = await getChild(selectedIndex.value);
        /* 1.직위(jbrpSeq), 2.순번(ordSeq)으로 정렬 */
        filteredUsers.sort((a, b) => {
            const aSeq = a.jbrp === '' ? 999 : a.jbrpSeq; // 직위가 지정안되어있으면 마지막으로 보냄
            const bSeq = b.jbrp === '' ? 999 : b.jbrpSeq;
            return aSeq - bSeq || a.ordSeq - b.ordSeq; 
          });
          
        // 조직에 해당하는 유저 필터링 결과와 검색 결과를 병합
        if (filterValue.value !== '') {
            // 검색어가 있을 경우, filteredUsers에 검색 필터 적용
            let filteredMenuList = menuList.value[`${currentTab.value}List`]?.filter(item => item.useYn !== 'N');
            const field = currentTab.value === 'orgn' ? 'orgnNm' : 'partCompNm';
            filteredUserList.value = filteredUsers.filter(user => user.hrNm.includes(filterValue.value) || (filteredMenuList[selectedIndex.value]?.[field]?.includes(filterValue.value) ?? false));
        } else {
            // 검색어가 없을 경우 조직별 필터링 결과를 그대로 사용
            filteredUserList.value = filteredUsers;
        }
        endLoading();
    };

    const getChild = parentIndex => {
        const listKey = `${currentTab.value}List`;
        const filteredMenuList = menuList.value[listKey]?.filter(item => item.useYn !== 'N');
        menuList.value[listKey] = filteredMenuList;

        const parentItem = filteredMenuList?.[parentIndex];
        if (!parentItem) {
            return [];
        }

        const filteredUsers = userList.value.filter(el => el[`${currentTab.value}Id`] === parentItem[`${currentTab.value}Id`] && el.useYn !== 'N');

        // isLoading 초기화 및 이미지 로딩 비동기 시작
        filteredUsers.forEach(user => {
            user.isLoading = true;
            loadImageAsyncPerUser(user); // 비동기로 개별 이미지 로딩
        });

        return filteredUsers; // 바로 반환, 이미지 로딩 기다리지 않음
    };
    const loadImageAsyncPerUser = async user => {
        user.isLoading = true;
        try {
            user.img = await loadFileFromServer(user.logoId);
        } catch (err) {
            console.error('이미지 로드 실패 => ', user.logoId, err);
            user.img = null;
        } finally {
            user.isLoading = false;
        }
    };

    const mergeToOriginData = () => {
        filteredUserList.value.forEach(item => {
            let user = originUserList.value.find(el => {
                return item.hrId == el.hrId;
            });
            if (user) user.selected = item.selected;
        });
    };

    const isDetailAllSelected = computed(() => {
        return filteredUserList.value?.length > 0 && filteredUserList.value.every(el => el.selected === true);
    });

    const detailSelectAll = () => {
        const newState = !isDetailAllSelected.value;
        filteredUserList.value.forEach(el => {
            el.selected = newState;
        });
    };

    /* 전 조직 선택 */
    const isMasterAllSelected = computed(() => {
        const copiedUserList = currentTab.value === 'orgn' ? userList.value.filter(el=>el.partCompId === ''||el.partCompId === null) : userList.value.filter(el=>el.partCompId !== ''&& el.partCompId !== null)
        return copiedUserList?.length > 0 && copiedUserList.every(el => el.selected === true);
    });

    const masterSelectAll = () => {
        const newState = !isMasterAllSelected.value;
        openLoading();
        setTimeout(() => {
            try {
                const isOrgnTab = currentTab.value === 'orgn';
                const isPartCompTab = currentTab.value === 'partComp';

                userList.value.forEach(el => {
                    const hasPartCompId = !!el.partCompId; // null, "" 모두 false로 처리됨

                    if (isOrgnTab && !hasPartCompId) {
                        el.selected = newState;
                    } else if (isPartCompTab && hasPartCompId) {
                        el.selected = newState;
                    }
                });
            } finally {
                endLoading();
            }
        }, 0);
    };

    const getSelectedUser = () => {
        return userList.value.filter(el => el.selected);
    };

    const initData = (selected = []) => {
        userList.value = [];
        userList.value = _.cloneDeep(originUserList.value.filter(el => (el.useYn = 'Y')));
        if (selected.length > 0) {
            // 멀티 선택일 경우 선택된 항목은 선택 유지
            userList.value.forEach(user => {
                if (selected.includes(user.hrId)) {
                    user.selected = true;
                }
            });
        }
        clickItem(0);
    };
    const filterValue = ref('');

    const single = ref(false);
    const validOrgnIdList = ref([]); // 초기 props로 조직 필터링
    const validHrIdList = ref([]); // 초기 props로 인원 필터링
    const init = async (param, selected, compId) => {
        flush();
        filterValue.value = '';
        single.value = param;
        const searchParam = {
            compId: compId
        };
        let responses = await Promise.all([getOrganization(searchParam, false), getPartner(searchParam, false), getHr(searchParam, false)]);
        if (validOrgnIdList.value.length > 0) {
            menuList.value.orgnList = responses[0].list.filter(el => validOrgnIdList.value.includes(el.orgnId));
            menuList.value.partCompList = responses[1].list.filter(el => validOrgnIdList.value.includes(el.partCompId));
        } else {
            menuList.value.orgnList = responses[0].list;
            menuList.value.partCompList = responses[1].list;
        }
        originMenuList.value = _.cloneDeep(menuList.value);
        if (validHrIdList.value.length > 0) {
            originUserList.value = responses[2].list.filter(el => validHrIdList.value.includes(el.hrId) && el.useYn === 'Y');
        } else {
            originUserList.value = responses[2].list.filter(el => el.useYn === 'Y');
        }
        initData(selected);
        selectedIds.value = selected;
        clickTab(currentTab.value ? currentTab.value : 'orgn');
    };

    const singleSelect = item => {
        originUserList.value.map(el => (el.selected = false));
        item.selected = true;
    };

    const flush = () => {
        originUserList.value = [];
        filteredUserList.value = [];
    };

    return {
        menu,
        currentTab,
        clickTab,
        menuList,
        selectedIndex,
        clickItem,
        selectedIds,
        userList,
        filteredUserList,
        isDetailAllSelected,
        detailSelectAll,
        isMasterAllSelected,
        masterSelectAll,
        getSelectedUser,
        filterValue,
        validOrgnIdList,
        validHrIdList,
        filterFromString,
        getChild,
        init,
        singleSelect,
        flush
    };
});
