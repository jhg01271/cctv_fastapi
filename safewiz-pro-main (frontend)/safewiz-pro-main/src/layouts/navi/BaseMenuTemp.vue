<template>
    <div style="margin-bottom: 5px" v-for="(route, index) in $router.getRoutes()" :key="index">
        <a @click="$router.push(route.path)">
            <span>{{ route.name }} 이동</span>
        </a>
    </div>
</template>

<script setup>
import { ref, computed, nextTick } from 'vue';
import { gsap } from 'gsap';
import { CustomEase } from 'gsap/CustomEase';

gsap.registerPlugin(CustomEase); // GSAP CustomEase 플러그인 등록
CustomEase.create('cubicEase', '0.85, 0, 0.15, 1'); // cubicEase 커스텀 이징 함수 생성

/** ✏️ props */
const props = defineProps({
    menuGroup: { type: Object, default: () => ({}) }
});
const menuGroup = computed(() => props.menuGroup);

/** ✏️ tree menu component */

import MenuListGroup from '@/layouts/navi/MenuListGroup.vue';
import MenuListItem from '@/layouts/navi/MenuListItem.vue';

const submenu = ref(null);

const toggleMenu = async () => {
    if (submenu.value) {
        if (submenu.value.style.display === 'block') {
            gsap.to(submenu.value, {
                height: 0,
                duration: 0.5,
                ease: 'cubicEase',
                onComplete: () => {
                    submenu.value.style.display = 'none'; //limhs11 항상 열어 두기 none --> block 로 바꿈
                    submenu.value.style.height = '';
                }
            });
        } else {
            submenu.value.style.display = 'block';
            await nextTick(() => {
                const height = submenu.value.scrollHeight;
                gsap.fromTo(
                    submenu.value,
                    { height: 0, overflow: 'hidden' },
                    {
                        height: height,
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
