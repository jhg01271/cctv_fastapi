<template>
    <li :class="{ collapsed: isCollapsed, active: isActive }">
        <a @click="navigate">
            <i><img v-if="menuItem.menuIcon" :src="`/assets/img/common/${menuItem.menuIcon}.svg`" alt="" /></i>
            <span>{{ menuItem.menuNm }}</span>
        </a>
    </li>
</template>
<script setup>
import { onMounted, computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { clearImageCache } from '@/utils/iFileLoader.js';
const route = useRoute();
const router = useRouter();

const props = defineProps({
    menuItem: {
        type: Object,
        required: true
    },
    isCollapsed: {
        type: Boolean, // 사이드바 축소 상태
        default: true // required: true // 2025.05.12 BHJ [Vue warn]: Missing required prop: "isCollapsed" 오류 해결을 위해 required 대신 default 설정
    }
    // isPopup: {
    //     type: Boolean,
    //     default: false,
    // },
});

const isActive = computed(() => {
    // // route.name이 menuItem.routerNm와 같으면 true
    // // return route.name === props.menuItem.routerNm;
    // return typeof route.name === 'string' && route.name.includes(props.menuItem.routerNm);
    // console.log('######### ', props.menuItem);
    // console.log('######### route ', route);

    if (!props.menuItem._children) {
        // 하위메뉴가 더이상 없을경우 비교
        return route.name.includes(props.menuItem.routerNm);
    } else if (typeof route.name !== 'string') return false;

    // depth2 인 항목 탐색
    return props.menuItem._children.some(child => {
        // 자식메뉴가 있는 메뉴들에서 현재 라우터 기준으로 비교
        // route.name === props.menuItem.routerNm -> 현재 라우터 기준으로 부모 메뉴 active(card view와 같이 depth1 인 항목)
        // route.name === child.routerNm -> 현재 라우터 기준으로 부모 메뉴들 active(detail page과 같이 depth2 인 항목)
        // child._children?.findIndex(el => route.name.includes(el.routerNm)) > -1 -> 현재 라우터 기준으로 부모 메뉴들 active(card detail page과 같이 depth3 인 항목)

        return route.name === props.menuItem.routerNm || route.name === child.routerNm || child._children?.findIndex(el => route.name.includes(el.routerNm)) > -1;
    });
});

// onMounted(() => {
//     console.log('현재 라우트 name:', route.name);
//     console.log('메뉴 아이템:', props.menuItem.routerNm);
// });

// 서브 메뉴 클릭 여부부
import eventBus from '@/sdm/eventBus';
let menuClickCount = 0;
const navigate = () => {
    menuClickCount++;
    eventBus.emit('checkClick', menuClickCount);
    clearImageCache();
    if (props.menuItem.routerNm) {
        router.push({ name: props.menuItem.routerNm });
    } else {
        console.warn('router Name 정의되지 않았습니다:', props.menuItem);
    }
};
</script>
