<template>
  <div class="py-1 full app-tag"
    style="position: relative;">
    <div class="mx-auto pr-6">
      <div class="tagview" v-if="tagsStore.visitedViews">
        <ul class="tag-list">
          <li v-for="tag in tagsStore.visitedViews" :key="tag.path" class="tag-item" :class="isActive(tag)?'active':''">
              <router-link
                class="tag-title"
                :to="{ path: tag.path, query: tag.query || {}, fullPath: tag.fullPath }"
                :name="tag.name"
              >
              <!-- @click="onTag(tag)" -->
                {{ $t(getMenuTitle(tag)) }}
              </router-link>
            <button class="tag-close" @click.stop="closeTag($event, tag)">✕</button>
          </li>
        </ul>
        <div v-if="tagsStore.visitedViews.length > 0" class="tag-actions">
          <button class="summary-btn" @click.stop="closeAllTag">모두 닫기</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { watch } from "vue";
import { useTagsStore } from "@/stores/tags";
import { useRoute, useRouter } from "vue-router";
import { useI18n } from "vue-i18n";
import { useMenuStore } from '@/stores/menu.js';
import BaseView from '@/components/base/BaseView';

const menuStore = useMenuStore();

const route = useRoute();
const router = useRouter();
const tagsStore = useTagsStore();
const { t } = useI18n();
const { confirmMsg } = BaseView();
const menus = menuStore?.menu ?? [];

const getMenuTitle = (tag) => {
  console.log('getMenuTitle', tag);
  // const targetMenu = find(menuStore.menus, { href: tag.path });
  const targetMenu = menus.find(item => item.href === tag.path);

  return !targetMenu ? tag.meta.menuNm : targetMenu.menu_id;
};

// const getMenuIcon = (tag) => {
//   const targetMenu = find(menuStore.menus, { href: tag.path });

//   return !targetMenu || !targetMenu.icon_path
//     ? config.verticalNav.defaultNavItemIconProps.icon
//     : targetMenu.icon_path;
// };

const isActive = (tag) => {
  console.log('isActive', tag);
  return tag.path === router.currentRoute.value.path;
};

// const onTag = (tag) => {
//   console.log('onTag', tag);
//   // const targetMenu = find(menuStore.menu, { href: tag.path });
//   if(tag.path == "/dashboard") {
//     return;
//   }

  // const setParentsMenu = (target) => {
  //   console.log('target', target.meta.menuNm, target.meta.menuId);
    // if (target.menulevel > 1) {
    //   // const menu = find(menuStore.menu, { menu_id: target.prnts_menu_id })
    //   const menu = menus.find(item => item.menu_id === target.prnts_menu_id);;
    //   setParentsMenu(menu);
    // } else {
    //   menuStore.setSelectedMenu(target.title, target.menu_id);
    // }
    // menuStore.setSelectedMenu(target.meta.menuNm, target.meta.menuId);
  // };
  // setParentsMenu(tag);
// };

const closeTag = (event, tag) => {
  console.log('closeTag');
  event.preventDefault();
  tagsStore.deleteVisitedView(tag);
  tagsStore.deleteCachedView(tag);
  if (isActive(tag)) {
    const latestView = tagsStore.visitedViews.slice(-1)[0];
    if (latestView) {
      // onTag(latestView);
      router.push({
        path: latestView.path,
        query: latestView.query,
        fullPath: latestView.fullPath,
      });
    } else {
      // menuStore.setSelectedMenu(null, null);
      router.push("/dashboard");
    }
  }
};

const closeAllTag = async () => {
  console.log('closeAllTag');
  await confirmMsg('warning', '열려진 메뉴를 모두 닫으시겠습니까?', null, { fun: sendEmergencyMessage, param: '' });
};

const sendEmergencyMessage = () => {
  tagsStore.reset();
  router.push("/dashboard");
}

watch(() => route.path, () => {
  console.log('watch', route.path);
  if(route.path !== '/dashboard') {
    tagsStore.addVisitedView(route, t)
    tagsStore.addCachedView(route)
    // onTag(router.currentRoute.value)
  } 
  // else {
  //   menuStore.setSelectedMenu(null, null)
  // }
  
}, { immediate: true })
</script>
<style scoped>
  .tagview { display:flex; align-items:center; gap:12px; background:#fafafa; border-bottom:1px solid #eee; }
  .tag-list {
    display:flex;
    gap:8px;
    list-style:none;
    padding:8px;
    margin:0;
    align-items:center;

    overflow-x: auto;        /* 가로 스크롤 허용 */
    overflow-y: hidden;
    white-space: nowrap;     /* 줄바꿈 방지 (안전장치) */
    -webkit-overflow-scrolling: touch; /* 모바일 스무스 스크롤 */
  }

  /* 스크롤바 숨기기(선택) */
  .tag-list::-webkit-scrollbar { height: 8px; }
  .tag-list::-webkit-scrollbar-thumb { background: rgba(0,0,0,0.2); border-radius: 4px; }

  .tag-item { background:#fff; border:1px solid #ddd; padding:4px 8px; border-radius:4px; display:flex; gap:6px; align-items:center; }
  .tag-item.active { background:#007bff; color:#fff; border-color:#007bff; }
  .tag-title { cursor:pointer; }
  .tag-close {
    background: transparent;
    border: 1px solid #ddd;
    border-radius: 50%;
    background-color: #666;
    color: #fff;
    cursor:pointer;
    padding: 0px 3px;
  }
  .tag-item.active .tag-close {
    background: #fff;
    border: 1px solid rgba(255,255,255,0.2);
    color: #000;
  }
  .tag-pin { background:transparent; border:0; cursor:pointer; }
  .tag-actions button {
    width: 80px;
    margin-left:8px;
    background-color: #3248F6;
    color: #fff;
    border-radius: 4px;
    padding: 0 1rem;
    line-height: 2.2rem;
    word-break: keep-all;
  }
</style>