<template>
    <li :class="{ active: anyChildActive }">
        <a @click.prevent="handleClick">
            <i class="menu-icon">
                <component :is="getIcon()" />
            </i>
            <input v-if="(menuGroup.newYn == 'Y' || isRename) && props.isFavorites" ref="editInput" v-model="tempName" @blur="finishEdit" @keyup.enter="finishEdit" class="menu-title" />
            <span v-else class="menu-title">{{ menuGroup.menuNm }}</span>
            <span v-if="props.isFavorites" class="submenu-action option-select">
                <span class="submenu-arrow" />
                <button class="submenu-option" @click.stop.prevent="toggleFavoritesOption" :class="{ active: showSelect }"></button>
                <ul ref="optionList" class="option-list">
                    <li>
                        <button @click.stop.prevent="optionRenameClick">
                            <span>{{ '이름 바꾸기' }}</span>
                        </button>
                    </li>
                    <li>
                        <button @click.stop.prevent="optionDeleteClick">
                            <span>{{ '삭제' }}</span>
                        </button>
                    </li>
                </ul>
            </span>
        </a>

        <ul class="submenu" ref="submenu" v-if="menuGroup._children && menuGroup._children.length > 0">
            <template v-for="child in menuGroup._children" :key="child.menuId">
                <!-- <MenuListGroup v-if="child.children && child.children.length > 0" :menuGroup="child" /> -->
                <!-- <MenuListItem v-else :menuItem="child" /> -->
                <MenuListItem :menuItem="child" />
            </template>
        </ul>
    </li>
</template>

<script setup>
import { ref, computed, nextTick, defineAsyncComponent, watch, onUnmounted, onMounted } from 'vue';
import { gsap } from 'gsap';
import { CustomEase } from 'gsap/CustomEase';
import { useRoute } from 'vue-router';

gsap.registerPlugin(CustomEase); // GSAP CustomEase 플러그인 등록
CustomEase.create('cubicEase', '0.85, 0, 0.15, 1'); // cubicEase 커스텀 이징 함수 생성

const modules = import.meta.glob('/src/assets/img/menu/*.svg');
const getIcon = () => {
    const fallbackPath = `/src/assets/img/menu/default.svg`;
    if (!menuGroup?.value.menuId) {
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
const menuGroup = computed(() => props.menuGroup);

/** ✏️ tree menu component */

import MenuListItem from '@/layouts/navi/MenuListItem.vue';

// 메뉴 열림 상태를 localStorage에 저장/복원하는 헬퍼
const OPEN_MENUS_KEY = 'nav_openMenuIds';
const getOpenMenuIds = () => {
    try { return new Set(JSON.parse(localStorage.getItem(OPEN_MENUS_KEY) || '[]')); }
    catch { return new Set(); }
};
const saveOpenMenuId = (menuId, isOpen) => {
    const ids = getOpenMenuIds();
    if (isOpen) ids.add(menuId); else ids.delete(menuId);
    localStorage.setItem(OPEN_MENUS_KEY, JSON.stringify([...ids]));
};

const showSelect = ref(false);
const optionList = ref(null);
const emit = defineEmits(['expandSidebar', 'rename']);
const optionRenameClick = () => {
    closeOption();
    isRename.value = true;
};

const optionDeleteClick = () => {
    closeOption();
    emit('delete', props.menuGroup.menuId);
};

const toggleFavoritesOption = async () => {
    if (optionList.value) {
        if (showSelect.value) {
            closeOption();
        } else {
            // 메뉴 열기 애니메이션
            optionList.value.style.display = 'block';
            await nextTick();
            const height = optionList.value.scrollHeight;
            gsap.fromTo(
                optionList.value,
                { height: 0, overflow: 'hidden' },
                {
                    height: height,
                    duration: 0.5,
                    ease: 'cubicEase',
                    onComplete: () => {
                        optionList.value.style.height = '';
                        showSelect.value = true;
                    }
                }
            );
        }
    }
};
const closeOption = () => {
    const el = optionList.value;
    if (!el) return;

    gsap.to(el, {
        height: 0,
        duration: 0.5,
        ease: 'cubicEase',
        onComplete: () => {
            el.style.display = 'none';
            showSelect.value = false;
            // isRename.value = false;
        }
    });
};

// 2026-01-08 즐겨찾기 추가
const isEditing = ref(false); //false
const isRename = ref(false);
const editInput = ref(null);
const tempName = ref(props.menuGroup.menuNm);

// const startEdit = () => {
//     isEditing.value = true;
//     tempName.value = props.menuGroup.menuNm;
// };

const finishEdit = () => {
    isEditing.value = false;
    isRename.value = false;
    emit('rename', {
        menuId: props.menuGroup.menuId,
        newName: tempName.value,
        menuOdr: props.menuGroup.menuOdr
    });
};

const handleBeforeUnload = () => {
    finishEdit();
};

const closeIfOption = () => {
    if (!optionList.value) return;

    if (!optionList.value.contains(editInput.value.target)) {
        closeOption();
    }
};

window.addEventListener('beforeunload', handleBeforeUnload);

onMounted(async () => {
    document.addEventListener('click', closeIfOption);

    // 레이아웃 전환(대시보드↔일반) 시에도 드롭다운 열림 상태를 유지하도록
    // localStorage에 저장된 열림 상태 또는 활성 자식이 있으면 서브메뉴를 자동으로 펼침
    // 단, 사이드바가 접힌 상태면 서브메뉴도 접힌 상태 유지
    await nextTick();
    const shouldOpen = !props.isCollapsed && (getOpenMenuIds().has(props.menuGroup.menuId) || anyChildActive.value);
    if (shouldOpen && submenu.value) {
        submenu.value.style.display = 'block';
        submenu.value.style.maxHeight = 'none';
        submenu.value.style.overflow = '';
        saveOpenMenuId(props.menuGroup.menuId, true);
    }
});

onUnmounted(() => {
    window.removeEventListener('beforeunload', handleBeforeUnload);
});

const submenu = ref(null);
// 메뉴 클릭 처리
const handleClick = async () => {
    if (props.isCollapsed) {
        emit('expandSidebar');
    }
    await toggleMenu();
};

// 현재 라우트 정보 가져 옴
const route = useRoute();

// route.name === child.routerNm 이면 부모도 active
const anyChildActive = computed(() => {
    // 자식이 없으면 false
    // if (!props.menuGroup._children) return false;

    // // 자식이 있으면
    // return props.menuGroup._children.some(child => {
    //     console.log(route.name, child.routerNm);

    //     // 현재 라우트 명과 자식의 라우터 명이 일치하면 true
    //     // return route.name === child.routerNm;
    //     return typeof route.name === 'string' && route.name.includes(child.routerNm);
    // });

    // 자식이 없으면 false
    if (!props.menuGroup._children) return false;
    else if (typeof route.name !== 'string' || !route.meta.routerPath) {
        // 메뉴바에는 없지만 라우터에는 존재하는 대시보드에 대한 예외처리로 routerPath로 비교함
        return false;
    }
    // 자식이 있으면
    return props.menuGroup._children.some(child => {
        // 부모 메뉴의 라우터 경로와 하위(depth1)라우터 경로를 비교하여 포핟되는 경로인지 확인
        // child의 _children를 순회하며 menuNm을 비교해가면서 찾아야하기 때문에 routerPath로 비교함
        // 부모 하위 자식메뉴는 동일한 routerPath로 정의되어 있음
        return route.meta.routerPath.includes(child.routerPath);
    });
});

watch(
    () => [menuGroup.value.newYn, isRename.value],
    async ([newVal, rename]) => {
        if ((newVal === 'Y' || rename) && props.isFavorites) {
            tempName.value = menuGroup.value.menuNm;
            await nextTick();
            if (editInput.value) {
                editInput.value.focus();
                editInput.value.select();
            }
        }
    },
    { immediate: true }
);

// 라우터 변경 시 사이드바가 축소 상태면 2차 메뉴 강제 닫기
watch(
    () => route.fullPath,
    () => {
        if (props.isCollapsed && submenu.value) {
            submenu.value.style.display = 'none';
            submenu.value.style.maxHeight = '0px';
        }
    }
);

const wasOpen = ref(false);
watch(
    () => props.isCollapsed,
    newValue => {
        if (newValue && submenu.value) {
            // 열린 상태 저장
            wasOpen.value = submenu.value.style.display === 'block';
            gsap.to(submenu.value, {
                maxHeight: 0,
                duration: 0.3,
                ease: 'power2.inOut',
                onComplete: () => {
                    submenu.value.style.display = 'none';
                    submenu.value.style.height = '';
                }
            });
        } else if (!newValue && submenu.value && wasOpen.value) {
            // 이전에 열려있었으면 복원
            submenu.value.style.display = 'block';
            submenu.value.style.maxHeight = 'none';
            submenu.value.style.overflow = '';
        }
    }
);

const toggleMenu = async () => {
    if (submenu.value) {
        if (submenu.value.style.display === 'block') {
            saveOpenMenuId(props.menuGroup.menuId, false);
            gsap.to(submenu.value, {
                maxHeight: 0,
                duration: 0.5,
                ease: 'cubicEase',
                onComplete: () => {
                    submenu.value.style.display = 'none'; //limhs11 항상 열어 두기 none --> block 로 바꿈
                    submenu.value.style.height = '';
                }
            });
        } else {
            saveOpenMenuId(props.menuGroup.menuId, true);
            submenu.value.style.display = 'block';
            await nextTick(() => {
                const height = submenu.value.scrollHeight;
                gsap.fromTo(
                    submenu.value,
                    { maxHeight: 0, overflow: 'hidden' },
                    {
                        maxHeight: height,
                        duration: 0.5,
                        ease: 'cubicEase',
                        onComplete: () => {
                            submenu.value.style.height = '';
                        }
                    }
                );
            });
        }
    }
};
</script>
<style lang="scss" scoped>
.submenu-action {
    margin-left: auto;
    display: flex;
    align-items: center;
    gap: 0.5rem;
    height: 100%;
    position: relative;
}

.option-list {
    position: absolute;
    top: 100%;
    /* 버튼 바로 아래 */
    right: 0;
    z-index: 1000;

    min-width: 140px;
    background: #fff;
    border-radius: 8px;
    box-shadow: 0 8px 20px rgba(0, 0, 0, 0.15);

    display: none;
    /* 기본 숨김 */
}

.submenu-arrow {
    /* 이 한 줄이 핵심 */
    width: 1.5rem;
    height: 100%;
    background: url(/assets/img/common/icon_lnb_arrow.svg) no-repeat center;
    transform: rotate(180deg);
}

.submenu-option {
    /* 이 한 줄이 핵심 */
    width: 2.5rem;
    height: 100%;
    background: url(/assets/img/common/icon_settings.svg) no-repeat center;
    cursor: pointer;
}

.menu-title {
    // white-space: nowrap; 2026.02.10 이 스타일로 메뉴의 단락변경이 이루어지지 않아 가로 스크롤이 생김
    // flex: 1 1 auto; 2026.02.02 이 스타일로 레프트 네비게이션 레이아웃 틀어짐 현상 원복 (다른 부분이 필요할 경우 해당 부분에 대한 별도의 수정 작업 필요)
    min-width: 0;
}

.option-select {
    position: relative;

    > button {
        & + ul {
            z-index: 1;
            position: absolute;
            width: 100%;
            padding: 0 1.4rem;
            background: #1c2431;
            right: 0;
            top: 4rem;
            border-radius: 0 0 4px 4px;
            overflow: hidden;

            li {
                margin: 1rem 0;
                line-height: 1.5;

                &:first-child {
                    margin-top: 1.5rem;
                }

                &:last-child {
                    margin-bottom: 1.5rem;
                }

                button {
                    display: block;
                    width: 100%;
                    padding: 0 1rem;
                    line-height: 1.5;
                    font-size: 1.3rem;
                    color: rgba(255, 255, 255, 0.5);
                    text-align: left;
                    border-radius: 4px;
                    transition: all 0.3s ease;

                    &:hover {
                        background: rgba(255, 255, 255, 0.05);
                        color: #fff;
                    }
                }
            }
        }
    }
}
</style>
