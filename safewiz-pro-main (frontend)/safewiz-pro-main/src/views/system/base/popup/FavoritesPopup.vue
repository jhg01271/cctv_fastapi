<!-- favorites 추가 팝업창 (저장, 닫기) -->
<template>
    <!-- 콘텐츠 영역 -->
    <h3>{{ t('favorites') }}</h3>
    <!-- <label class="fw800" for>{{ options.label }}</label> -->
    <div class="form ui">
        <!-- class="gnb" -->
        <div class="gnbna">
            <ul>
                <template v-for="menu in props.favoritesDataList" :key="menu.menuId">
                    <li :class="{ active: activeMenuId === menu.menuId }" @click="selectMenu(menu)">
                        <div>
                            <i>
                                <component :is="getIcon(menu.menuIcon)" />
                            </i>
                            <span class="ml8px">{{ menu.menuNm }}</span>
                        </div>
                    </li>
                </template>
            </ul>
            <div class="pr df jcsb gap8px mt2rem">
                <div></div>
                <div>
                    <button class="btn w7-4rem radius active mr8px" @click="btnSave">{{ t('save') }}</button>
                    <button class="btn w7-4rem radius" @click="btnClose">{{ t('close') }}</button>
                </div>
            </div>
        </div>

    </div>
</template>

<script setup>
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import { ref, onMounted, defineProps, defineEmits, defineAsyncComponent } from 'vue';
import BaseView from '@/components/base/BaseView';
import MenuListGroup from '@/layouts/navi/MenuListGroup.vue';
import MenuListItem from '@/layouts/navi/MenuListItem.vue';
import DefaultPopupComponent from '@/views/system/base/popup/popupComponent/DefaultDataSetManageComponet.vue';
const { t, confirmMsg, validationStore, alertMsg } = BaseView();

const props = defineProps({
    favoritesDataList: { type: Array, default: () => [] }, // 🔹 popupDataList를 props로 받음
});

const emit = defineEmits(['save', 'close']);
const searchTerm = ref('');

onMounted(async () => {
    // btnSearch();
});

const activeMenuId = ref(null);
const activeMenu = ref(null);
const selectMenu = (menu) => {
    activeMenu.value = activeMenu.value === menu ? null : menu;
    activeMenuId.value = activeMenuId.value === menu.menuId ? null : menu.menuId;
}
const modules = import.meta.glob('/src/assets/img/menu/*.svg');
const getIcon = (menu) => {
    const fallbackPath = `/src/assets/img/menu/default.svg`;

    if (!menu?.menuIcon) {
        return defineAsyncComponent(modules[fallbackPath]);
    }

    const iconPath = `/src/assets/img/menu/${menu.menuIcon}.svg`;
    return defineAsyncComponent(modules[iconPath] || modules[fallbackPath]);
};

// ---------------------------------------------------
// 이벤트
// ---------------------------------------------------
// 저장
const btnSave = async () => {
    if (activeMenu.value) {
        emit('save', activeMenu.value);
    } else {
        alertMsg('warning', t('msgNoItem'));
        return;
    }
};
// 닫기
const btnClose = () => {
    emit('close');
};

</script>

<style lang="scss" scoped>
.gnbna {
    // padding: 3rem 0;
    max-height: 100%;

    >ul {

        // 전체
        div {
            display: block;
            cursor: pointer;
            /* transition: color 0.3s ease; */
        }

        &>li {
            transition: border-color 0.4s;

            &.active>div {
                color: #3248F6;

                span {
                    font-weight: 700;
                }

                i {
                    background: #3248F6;
                    border-color: #3248F6;

                    :deep(svg *) {
                        &[fill] {
                            fill: #fff !important;
                        }

                        &[stroke] {
                            stroke: #fff !important;
                        }
                    }
                }
            }

            &:hover {
                >div {
                    color: #3248F6;

                    span {
                        font-weight: 700;
                    }

                    i {
                        background: #3248F6;
                        border-color: #3248F6;

                        :deep(svg *) {
                            &[fill] {
                                fill: #fff !important;
                            }

                            &[stroke] {
                                stroke: #fff !important;
                            }
                        }
                    }
                }
            }

            // 1차
            &>div {
                position: relative;
                display: flex;
                height: 5rem;
                padding-left: 3rem;
                align-items: center;
                gap: 1rem;
                font-size: 0;
                border-bottom: 1px solid transparent;
                transition: all 0.4s;

                i {
                    display: flex;
                    width: 24px;
                    height: 24px;
                    border: 1px solid #E1E6ED;
                    line-height: 22px;
                    border-radius: 4px;
                    text-align: center;
                    justify-content: center;
                    align-items: center;

                    svg {
                        vertical-align: middle;
                    }
                }

                // &::after {
                //     display: inline-block;
                //     width: 5rem;
                //     height: 100%;
                //     background: url(/assets/img/common/icon_lnb_arrow.svg) no-repeat center;
                //     right: 0;
                //     top: 0;
                //     transform: rotate(180deg);
                // }

                &.active::after {
                    transform: rotate(180deg);
                }

                img {
                    margin-right: 1.5rem;
                    vertical-align: middle;
                }

                span {
                    font-size: 1.6rem;
                    font-weight: 400;
                    vertical-align: middle;
                }

                &+ul {
                    display: none;
                    padding: 0 3rem;
                    background: rgba(235, 237, 255, 0.30);
                    overflow: hidden;
                    border-radius: 5px;
                    overflow: hidden;
                    border-top: 1px solid #E1E6ED;
                    border-bottom: 1px solid #E1E6ED;

                    li:first-child {
                        margin-top: 1.5rem;
                    }

                    li:last-child {
                        margin-bottom: 1.5rem;
                    }

                    >li {
                        &.active>a {
                            background: rgba(50, 72, 246, 0.05);
                            color: #3248F6;
                            font-weight: 700;

                            &::before {
                                background: #3248F6;
                            }
                        }

                        // & > a::after{
                        //   display:inline-block; width:5rem; height:100%; right:0; top:0; background:url(assets/img/common/icon_lnb_child.svg) no-repeat center;
                        // }
                        // & > a.active::after{
                        //   transform:rotate(180deg);
                        // }
                        >div {
                            position: relative;
                            height: 4.6rem;
                            padding-left: 3rem;
                            line-height: 4.6rem;
                            font-size: 1.6rem;
                            font-weight: 300;
                            border-radius: 8px;

                            //  &.active{
                            //    color:#43D491;
                            //    &::before{background:#43D491 !important;}
                            //  }
                            &::before {
                                display: inline-block;
                                width: 3px;
                                height: 3px;
                                margin-top: -2px;
                                background: #222;
                                border-radius: 3px;
                                left: 1.8rem;
                                top: 50%;
                            }

                            &:hover {
                                background: rgba(50, 72, 246, 0.05);
                                color: #3248F6;
                                font-weight: 700;

                                &::before {
                                    background: #3248F6;
                                }
                            }

                            //  & + ul{
                            //    display:none; padding-left:2rem; overflow:hidden;
                            //    > li{
                            //      > a{
                            //        & + ul{
                            //          display:none; padding-left:1.4rem; overflow:hidden;
                            //          > li {
                            //            > a{
                            //            }
                            //          }
                            //        }
                            //      }
                            //    }
                            //  }
                        }
                    }
                }
            }
        }
    }
}
</style>


<!-- <div class="segment">
            <div class="row flex">
                <div class="grid12-12">
                    <div id="dataSetPopupForm" class="wbka tac">
                        <OverlayScrollbarsComponent
                            class="maxh35rem br4px"
                            :options="{
                                scrollbars: {
                                    x: 'hidden',
                                    y: 'visible'
                                }
                            }"
                        >
                        
                        </OverlayScrollbarsComponent>

                        <div class="bcEBEDFF">
                            <button class="init w100p pa1rem" type="button" @click="btnAdd">
                                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none">
                                    <rect x="0.5" y="0.5" width="23" height="23" rx="11.5" fill="#EBEDFF" />
                                    <rect x="0.5" y="0.5" width="23" height="23" rx="11.5" stroke="#3248F6" />
                                    <path d="M17 12L7 12M12 17L12 7" stroke="#3248F6" stroke-linecap="round" />
                                </svg>
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div> -->