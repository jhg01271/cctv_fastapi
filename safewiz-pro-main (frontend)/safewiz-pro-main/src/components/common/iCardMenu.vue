<template>
    <!-- gutters로 생긴 x축 스크롤을 방지하기위한 'oh' 태그 -->
    <!-- 디자인 착수가 필요하여 임시로 작업된 화면입니다. 상태의 경우 각 상태별로 클래스로 토글될 수 있도록 처리해주시면 될 것 같습니다. -->
    <template v-for="(menu, index) in props.menuList" :key="menu.router">
        <div v-if="menu.menuAuthYn" class="grid12-3 ul-grid12-4 lg-grid12-6 es-grid12-12">
            <div class="card-menu pr h40rem bdt1-5pxsolide1e6ed bdb1-5pxsolide1e6ed bcffffff o96" :class="cardClass(menu)">
                <a href="#" @click.prevent="btnClick(menu)">
                    <!--상단 제목 -->
                    <div class="pr mt2-2rem mb1-2rem px2rem">
                        <button class="w6-4rem h3-1rem df jcc aic fs1-4rem fw600 br100px">{{ printText(menu) }}</button>
                        <!-- 활성화 상태에 따른 출력 텍스트-->
                        <i v-if="tooltipText" class="tooltip pa t50p l10rem wbka o0 es-maxw18rem">
                            {{ tooltipText(menu) }}
                        </i>
                    </div>
                    <em class="px2rem fs2rem wbka lh1-3 wsn neg-ls1px">{{ t(menu.title) }}</em>

                    <hr class="w100p h1px mt2rem mb2rem bcE1E6ED" />

                    <!--------------------- TODO --------------------->
                    <!--  description 내용이 길어질 경우 스크롤 기능이 필요합니다. 2024.10.16-->
                    <!--------------------- ---- --------------------->
                    <div class="px2rem">
                        <ol class="fs1-5rem lh1-3 fw300">
                            <!-- 해당 카드에 대한 설명-->
                            <li v-html="t(menu.description)" class="lc10-2rem"></li>
                        </ol>
                    </div>

                    <span class="init m pa r2rem b2rem">
                        <i class="mr4px fs1-5rem">바로가기</i>
                        <img src="data:image/svg+xml;base64,PHN2ZyB4bWxucz0naHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmcnIHdpZHRoPScyNCcgaGVpZ2h0PScyNCcgdmlld0JveD0nMCAwIDI0IDI0JyBmaWxsPSdub25lJz48cGF0aCBkPSdNNSAxMkgxOE0xMyA2TDE4LjI5MjkgMTEuMjkyOUMxOC42ODM0IDExLjY4MzQgMTguNjgzNCAxMi4zMTY2IDE4LjI5MjkgMTIuNzA3MUwxMyAxOCcgc3Ryb2tlPScjMzI0OEY2JyBzdHJva2UtbGluZWNhcD0ncm91bmQnLz48L3N2Zz4=" />
                    </span>
                </a>
            </div>
        </div>
    </template>
</template>

<script setup>
import { defineProps, defineEmits, ref, onMounted, computed, watch, watchEffect } from 'vue';
import router from '@/router';

import BaseView from '@/components/base/BaseView';
const { t } = BaseView();
// import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
// import tooltip from '../../sdm/directives/tooltip';

// title: { type: String, default: 'iCardMenu' }, // 타이틀
// route: { type: String, default: '' }, // 라우터 경로 ('/dashboard', '/home', ...)
// description: { type: String, default: '' }, // 카드 설명
// activeYn: { type: String, default: 'N' }, // 활성화 상태 플래그값
// activeYnText: { type: Object, default: { Y: '등록', N: '미등록' } }, // 하단 활성화 상태 텍스트
// tooltipText: { type: Object, default: { Y: '문서가 등록되었습니다.', N: '등록된 문서가 없습니다.' } } // 하단 활성화 상태 툴팁 내용
const props = defineProps({
    menuList: { type: Array, default: () => [] }
});
const emit = defineEmits(['click']);

const printText = menu => {
    if (!menu.activeYnText) menu.activeYnText = { Y: t('menuY'), N: t('menuN') };
    // activeYnText에 정의된 값에 따라 activeYn값의 텍스트를 반환
    return menu.activeYnText[menu.activeYn];
};
const tooltipText = menu => {
    if (!menu.tooltipText) menu.tooltipText = { Y: t('menuTootipY'), N: t('menuTootipN') };
    // activeYnText에 정의된 값에 따라 activeYn값의 텍스트를 반환
    return menu.tooltipText[menu.activeYn];
};

const cardClass = menu => {
    return menu.activeYn === 'Y' ? 'registered' : 'not-registered';
};
const checkRouteAuth = menu => {
    // 사용자의 메뉴 리스트에 포함된 메뉴인지 확인하여 랜더링한다.
    const findRoute = router.getRoutes().find(el => el.path.toLowerCase() === menu.route.toLowerCase());
    if (findRoute) {
        menu.menuAuthYn = true;
    }
};
watchEffect(() => {
    props.menuList.forEach(menu => {
        checkRouteAuth(menu);
    });
});
onMounted(() => {});
const btnClick = menu => {
    // 라우터 경로에 /가 누락되었을 경우를 위한 예외처리
    if (menu.route) emit('click', menu.route[0] === '/' ? menu.route : '/' + menu.route[0]);
    else {
        console.warn('!! w a r n i n g !!');
        console.warn('!! 라우터 경로 X  !!');
    }
};
</script>

<style lang="scss" scoped>
// 공통
.card-menu {
    &::after {
        display: block;
        height: 4px;
        width: 100%;
        top: 0;
        left: 0;
    }

    button {
        border: 1px solid;
        background: rgba(50, 72, 246, 0.05);
        transition: all 0.4s;
    }

    .tooltip {
        font-size: 1.3rem;
        transform: translate(30px, -50%);
        transition: all 0.4s ease-in-out;
    }

    &:hover .tooltip {
        opacity: 1;
        transform: translate(-10px, -50%);
    }
}

.registered {
    &::after {
        background: var(--m2, #3248f6);
    }

    button {
        border-color: var(--m2, #3248f6);
        color: var(--m2, #3248f6);
    }

    &:hover button {
        background: var(--m2, #3248f6);
        color: #fff;
    }
}

.not-registered {
    &::after {
        background: var(--m3, #ff3534);
    }

    button {
        border-color: var(--m3, #ff3534);
        color: var(--m3, #ff3534);
    }

    &:hover button {
        background: var(--m3, #ff3534);
        color: #fff;
    }
}
</style>
