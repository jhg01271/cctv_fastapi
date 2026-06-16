<template>
    <VueDraggable tag="ul" v-model="menuGroup" item-key="menuId" handle=".group-handle" @update:modelValue="updateParentOrder" :group="{ name: 'menu-group', pull: false, put: false }" @end="onDragEnd">
        <li :class="['group-handle', { active: anyChildActive(menu) }]" v-for="menu in menuGroup" :key="menu.menuId">
            <a @click.prevent="handleClick(menu.menuId)" :class="['pr df aic', { empty: !menu._children?.length }]" :title="menu.menuNm">
                <i class="menu-icon fs0">
                    <component :is="getIcon()" />
                </i>
                <!-- v-model="tempName[menu.menuId]" -->
                <input v-show="(menu.newYn == 'Y' || isRename[menu.menuId]) && props.isFavorites" :ref="el => (editInput[menu.menuId] = el)" @change="e => (tempName[menu.menuId] = e.target.value)" :value="tempName[menu.menuId]" @blur="finishEdit(menu.menuId)" @keyup.enter="finishEdit(menu.menuId)" class="w100px" />
                <span v-show="!((menu.newYn == 'Y' || isRename[menu.menuId]) && props.isFavorites)" class="ellipsis maxw100p">{{ menu.menuNm }}</span>
                <button type="button" v-if="props.isFavorites" class="pr zi2 cAAAAAA fs0" @click.stop.prevent="toggleFavoritesOption(menu.menuId)" :class="{ active: showSelect[menu.menuId] }">
                    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-settings-icon lucide-settings">
                        <path d="M9.671 4.136a2.34 2.34 0 0 1 4.659 0 2.34 2.34 0 0 0 3.319 1.915 2.34 2.34 0 0 1 2.33 4.033 2.34 2.34 0 0 0 0 3.831 2.34 2.34 0 0 1-2.33 4.033 2.34 2.34 0 0 0-3.319 1.915 2.34 2.34 0 0 1-4.659 0 2.34 2.34 0 0 0-3.32-1.915 2.34 2.34 0 0 1-2.33-4.033 2.34 2.34 0 0 0 0-3.831A2.34 2.34 0 0 1 6.35 6.051a2.34 2.34 0 0 0 3.319-1.915" />
                        <circle cx="12" cy="12" r="3" />
                    </svg>
                </button>
                <Transition name="dropdown">
                    <div class="option-dropdown" v-show="showSelect[menu.menuId]" @click.stop>
                        <button type="button" @click.stop.prevent="optionRenameClick(menu.menuId)" title="이름바꾸기">이름 바꾸기</button>
                        <button type="button" @click.stop.prevent="optionDeleteClick(menu.menuId)" title="삭제">삭제</button>
                    </div>
                </Transition>
            </a>
            <!-- ref="submenu" -->
            <!-- v-if="menu._children && menu._children.length > 0" -->
            <VueDraggable v-show="(menu._children && menu._children.length > 0) || isDragging" tag="ul" class="submenu" :ref="el => (submenuRefs[menu.menuId] = el?.$el || el)" :class="{ 'drag-active': isDragging }" v-model="menu._children" :group="{ name: 'menu-items', pull: true, put: true }" item-key="menuId" @start="onChildDragStart" @end="onChildDragEnd" @change="event => onChildChange(event, menu.menuId)">
                <template v-for="child in menu._children" :key="child.menuId">
                    <MenuListItem :menuItem="child" :isFavorites="props.isFavorites" @deleteItem="deleteItem" />
                </template>
            </VueDraggable>
        </li>
    </VueDraggable>
</template>

<script setup>
import { ref, nextTick, defineAsyncComponent, watch, watchEffect, onUnmounted, onMounted } from 'vue';
import { gsap } from 'gsap';
import { CustomEase } from 'gsap/CustomEase';
import { useRoute } from 'vue-router';

gsap.registerPlugin(CustomEase); // GSAP CustomEase 플러그인 등록
CustomEase.create('cubicEase', '0.85, 0, 0.15, 1'); // cubicEase 커스텀 이징 함수 생성

const modules = import.meta.glob('/src/assets/img/menu/*.svg');
const getIcon = () => {
    const fallbackPath = `/src/assets/img/menu/default.svg`;
    if (!menuGroup.value?.menuId) {
        return defineAsyncComponent(modules[fallbackPath]);
    }

    const iconPath = `/src/assets/img/menu/${menuGroup.value.menuId}.svg`;
    return defineAsyncComponent(modules[iconPath] || modules[fallbackPath]);
};
/** ✏️ props */
const props = defineProps({
    menuGroup: { type: Object, default: () => ({}) },
    isCollapsed: { type: Boolean, default: false /*required: true*/ }, // 2025.05.12 BHJ [Vue warn]: Missing required prop: "isCollapsed" 오류 해결을 위해 required 대신 default 설정
    isFavorites: { type: Boolean, default: false }
});
const menuGroup = ref(props.menuGroup); //= computed(() => props.menuGroup);
const submenuRefs = ref({});
const isDragging = ref(false);

/** ✏️ tree menu component */

import MenuListItem from '@/layouts/navi/MenuListFavoritesItem.vue';
import { VueDraggable } from 'vue-draggable-plus';

// 즐겨찾기 메뉴 열림 상태를 localStorage에 저장/복원하는 헬퍼
const OPEN_FAV_KEY = 'nav_openFavMenuIds';
const getOpenFavIds = () => {
    try {
        return new Set(JSON.parse(localStorage.getItem(OPEN_FAV_KEY) || '[]'));
    } catch {
        return new Set();
    }
};
const saveOpenFavId = (menuId, isOpen) => {
    const ids = getOpenFavIds();
    if (isOpen) ids.add(menuId);
    else ids.delete(menuId);
    localStorage.setItem(OPEN_FAV_KEY, JSON.stringify([...ids]));
};

const showSelect = ref({});
const openedMenus = ref(new Set());
watch(
    () => props.isCollapsed,
    collapsed => {
        if (collapsed) {
            // 열린 서브메뉴 상태 저장
            openedMenus.value.clear();
            Object.keys(submenuRefs.value).forEach(menuId => {
                const el = submenuRefs.value[menuId];
                if (el && el.style.display === 'block') {
                    openedMenus.value.add(menuId);
                }
            });
            // 드롭다운 및 서브메뉴 닫기
            Object.keys(showSelect.value).forEach(key => {
                showSelect.value[key] = false;
            });
            Object.keys(submenuRefs.value).forEach(menuId => {
                const el = submenuRefs.value[menuId];
                if (el) {
                    el.style.display = 'none';
                    el.style.maxHeight = '0px';
                }
            });
        } else {
            // 이전에 열려있던 서브메뉴 복원
            openedMenus.value.forEach(menuId => {
                const el = submenuRefs.value[menuId];
                if (el) {
                    el.style.display = 'block';
                    el.style.maxHeight = 'none';
                    el.style.overflow = '';
                }
            });
        }
    }
);
const emit = defineEmits(['expandSidebar', 'rename', 'preDragEnd', 'deleteItem']);
const optionRenameClick = menuId => {
    closeOption(menuId);
    isRename.value[menuId] = true;
};

const deleteItem = menuId => {
    emit('delete', menuId);
};

const optionDeleteClick = menuId => {
    closeOption(menuId);
    emit('delete', menuId);
};

const onDragEnd = () => {
    emit('preDragEnd', menuGroup.value);
    // console.log('끝 : ', menuGroup.value)
};

const onChildDragStart = async () => {
    isDragging.value = true;

    // v-show 반응성 업데이트가 완료된 후 수동으로 display를 설정해야
    // Vue의 v-show와 충돌하지 않음 (race condition 방지)
    await nextTick();

    // 모든 submenu 열기 (다른 그룹으로 드래그 이동 가능하도록)
    Object.keys(submenuRefs.value).forEach(menuId => {
        const submenuEl = submenuRefs.value[menuId];
        if (submenuEl) {
            submenuEl.style.display = 'block';
            submenuEl.style.maxHeight = 'none'; // 드래그 중에는 높이 제한 해제
            submenuEl.style.overflow = '';
        }
    });
};

// const updateParentOrder = newOrder => {
//     // emit('updateOrder', { type: 'parent', data: newOrder });
// };

// 자식 change 이벤트 처리
const onChildChange = async (evt, menuId) => {
    await nextTick();

    // submenu가 열려있는 상태라면 높이 재조정
    const submenuEl = submenuRefs.value[menuId];
    if (submenuEl && submenuEl.style.display === 'block') {
        // 강제로 높이를 다시 계산
        submenuEl.style.maxHeight = 'none';
        const height = submenuEl.scrollHeight;
        submenuEl.style.maxHeight = '';

        gsap.to(submenuEl, {
            maxHeight: height,
            duration: 0.3,
            ease: 'cubicEase'
        });
    }
};

// 자식 드래그 종료 핸들러
const onChildDragEnd = () => {
    isDragging.value = false;

    emit('preDragEnd', menuGroup.value);
};

// 옵션 메뉴 토글
const toggleFavoritesOption = menuId => {
    // 다른 모든 열린 옵션들을 닫기
    for (let id in showSelect.value) {
        if (id !== menuId && showSelect.value[id]) {
            showSelect.value[id] = false;
        }
    }
    // 현재 옵션 토글
    showSelect.value[menuId] = !showSelect.value[menuId];
};

const closeOption = menuId => {
    showSelect.value[menuId] = false;
};

// 2026-01-08 즐겨찾기 추가
const isEditing = ref(false); //false
const isRename = ref({});
const isNewYn = ref({});
const editInput = ref({});
// const tempName = ref(menuGroup.value.menuNm);
const tempName = ref({});

const finishEdit = menuId => {
    isEditing.value = false;

    const isNew = menuGroup.value.find(item => item.newYn === 'Y');

    if (isNew?.menuId) {
        const menuOdr = menuGroup.value.find(item => item.menuId === isNew?.menuId).menuOdr;
        emit('rename', {
            menuId: isNew?.menuId,
            newName: tempName.value[isNew?.menuId],
            menuOdr: menuOdr,
            faveId: isNew?.faveId
        });
        return;
    }

    const editingMenuId = Object.keys(isRename.value).find(menuId => isRename.value[menuId] === true);

    if (editingMenuId) {
        const menu = menuGroup.value.find(item => item.menuId === editingMenuId);
        emit('rename', {
            menuId: editingMenuId,
            newName: tempName.value[editingMenuId],
            menuOdr: menu.menuOdr,
            faveId: menu.faveId
        });
    }
    isRename.value[menuId] = false;
};

const handleBeforeUnload = () => {
    finishEdit();
};

const closeIfOption = event => {
    if (event.target.closest('.submenu-option')) {
        return;
    }

    if (event.target.closest('.option-list')) {
        return;
    }

    Object.entries(showSelect.value).forEach(([menuId, isOpen]) => {
        if (isOpen) {
            closeOption(menuId);
        }
    });
};

window.addEventListener('beforeunload', handleBeforeUnload);

onMounted(async () => {
    document.addEventListener('click', closeIfOption);

    menuGroup.value.forEach(menu => {
        isRename.value[menu.menuId] = false;
        isNewYn.value[menu.menuId] = menu.newYn;
        tempName.value[menu.menuId] = menu.menuNm;
        showSelect.value[menu.menuId] = false;
    });

    // 레이아웃 전환(대시보드↔일반) 시에도 드롭다운 열림 상태를 유지하도록
    // localStorage에 저장된 열림 상태 또는 활성 자식이 있으면 서브메뉴를 자동으로 펼침
    // 단, 사이드바가 접힌 상태면 서브메뉴도 접힌 상태 유지
    await nextTick();
    if (!props.isCollapsed) {
        const openIds = getOpenFavIds();
        menuGroup.value.forEach(menu => {
            const shouldOpen = openIds.has(menu.menuId) || anyChildActive(menu);
            if (shouldOpen && submenuRefs.value[menu.menuId]) {
                const el = submenuRefs.value[menu.menuId];
                el.style.display = 'block';
                el.style.maxHeight = 'none';
                el.style.overflow = '';
                saveOpenFavId(menu.menuId, true);
            }
        });
    }
});

onUnmounted(() => {
    window.removeEventListener('beforeunload', handleBeforeUnload);
});

const submenu = ref(null);
// 메뉴 클릭 처리
const handleClick = async menuId => {
    if (props.isCollapsed) {
        emit('expandSidebar');
    }
    await toggleMenu(menuId);
};

// 현재 라우트 정보 가져 옴
const route = useRoute();

const anyChildActive = menu => {
    // const menu = menuGroup.value[index];

    if (!menu || !menu._children || menu._children.length === 0) {
        return false;
    }

    if (typeof route.name !== 'string' || !route.meta?.routerPath) {
        return false;
    }

    return menu._children.some(child => {
        return route.meta.menuId.includes(child.menuId);
    });
};

watchEffect(async () => {
    if (!props.isFavorites) return;
    menuGroup.value.forEach(async menu => {
        if (menu.newYn === 'Y' || isRename.value[menu.menuId]) {
            tempName.value[menu.menuId] = menu.menuNm;

            await nextTick();

            const inputEl = editInput.value[menu.menuId];
            if (inputEl) {
                inputEl.focus();
                inputEl.select();
            }
        }
    });
});

watch(
    () => props.menuGroup,
    newVal => {
        menuGroup.value = newVal.map(menu => ({
            ...menu,
            _children: menu._children || [] // ← 핵심: undefined 방지
        }));
    },
    { immediate: true, deep: true }
);

watch(
    () => props.isCollapsed,
    newValue => {
        if (newValue && submenu.value) {
            gsap.to(submenu.value, {
                eight: 0,
                duration: 0.3,
                ease: 'power2.inOut',
                onComplete: () => {
                    submenu.value.style.display = 'none';
                    submenu.value.style.height = '';
                }
            });
        }
    }
);

const toggleMenu = async menuId => {
    const menu = menuGroup.value.find(m => m.menuId === menuId);
    if (!menu?._children?.length) return;

    if (submenuRefs.value[menuId]) {
        if (submenuRefs.value[menuId].style.display === 'block') {
            saveOpenFavId(menuId, false);
            gsap.to(submenuRefs.value[menuId], {
                maxHeight: 0,
                duration: 0.5,
                ease: 'cubicEase',
                onComplete: () => {
                    submenuRefs.value[menuId].style.display = 'none'; //limhs11 항상 열어 두기 none --> block 로 바꿈
                    submenuRefs.value[menuId].style.height = '';
                }
            });
        } else {
            saveOpenFavId(menuId, true);
            submenuRefs.value[menuId].style.display = 'block';
            await nextTick(() => {
                const height = submenuRefs.value[menuId].scrollHeight;
                gsap.fromTo(
                    submenuRefs.value[menuId],
                    { maxHeight: 0, overflow: 'hidden' },
                    {
                        maxHeight: height,
                        duration: 0.5,
                        ease: 'cubicEase',
                        onComplete: () => {
                            submenuRefs.value[menuId].style.height = '';
                        }
                    }
                );
            });
        }
    }
};

const expandAll = () => {
    Object.keys(submenuRefs.value).forEach(menuId => {
        const menu = menuGroup.value.find(m => m.menuId === menuId);
        if (!menu?._children?.length) return;
        const el = submenuRefs.value[menuId];
        if (el && el.style.display !== 'block') {
            el.style.display = 'block';
            el.style.maxHeight = 'none';
            el.style.overflow = '';
        }
    });
};

const collapseAll = () => {
    Object.keys(submenuRefs.value).forEach(menuId => {
        const el = submenuRefs.value[menuId];
        if (el) {
            el.style.display = 'none';
            el.style.maxHeight = '0px';
        }
    });
};

defineExpose({ expandAll, collapseAll });
</script>
<style lang="scss" scoped>
.option-dropdown {
    position: absolute;
    right: 0;
    top: 100%;
    z-index: 10;
    display: flex;
    flex-direction: column;
    min-width: 120px;
    background: #fff;
    border: 1px solid #3248f6;
    color: #3248f6;
    border-radius: 4px;
    overflow: hidden;

    button {
        display: block;
        width: 100%;
        padding: 8px 0;
        color: #3248f6;
        border-bottom: 1px solid #3248f6;

        font-size: 13px;
        font-weight: 400;

        &:last-child {
            border-bottom: 0;
        }

        &:hover {
            background: #f5f6fe;
            font-weight: 700;
        }
    }
}

// 드래그 중 다른 그룹의 서브메뉴를 드롭 대상으로 표시
// CSS의 a + ul { display: none } 규칙을 오버라이드하고, 빈 그룹에도 드롭 영역 확보
.drag-active {
    display: block !important;
    min-height: 40px;
    border: 1px dashed #c0c8f6 !important;
    border-radius: 5px;
    background: rgba(235, 237, 255, 0.3);
}

.dropdown-enter-active,
.dropdown-leave-active {
    transition:
        opacity 0.2s ease,
        transform 0.2s ease;
}

.dropdown-enter-from,
.dropdown-leave-to {
    opacity: 0;
    transform: translateY(-4px);
}
</style>
