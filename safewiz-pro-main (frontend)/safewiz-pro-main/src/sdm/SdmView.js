
import globalEnvs from '@/global'
import router from '@/router'
import { useRoute } from 'vue-router'
import { storeToRefs } from 'pinia'
import { 
  ref, reactive, shallowRef,
  provide, inject, nextTick,
  onBeforeMount, onMounted, onBeforeUpdate, onUpdated, onBeforeUnmount, onUnmounted,
  watch, watchEffect, computed,
  mergeProps, defineExpose 
} from "vue"

import { usePopupStore } from '@/sdm/stores/popupStore';
import { useToastPopupStore } from '@/sdm/stores/toastPopupStore';
import { useActionPopupStore } from '@/sdm/stores/actionPopupStore';
import { useValidationStore } from '@/sdm/stores/validationStore';


export default function SdmView(){
  /** ✏️ 라우트 */
  // const route = useRoute()

  
  // 유효성검증 사용
  const validationStore = useValidationStore();
  const { errors, focusElement } = storeToRefs(validationStore);

  // 팝업 열기
  const popupDialog = (popupTitle, callbacks, content, componentProps) => {
    const popupStore = usePopupStore();
    popupStore.showPopup(popupTitle, callbacks, content, componentProps);
  };

  // 토스트 팝업 열기
  const toastPopup = (title, message, className, callbacks = {}) => {
    const toastPopupStore = useToastPopupStore();
    toastPopupStore.addToast(title, message, className, callbacks);
  };

  // 액션 팝업 열기 
  const onConfirmCallback = ref(null);
  const onCancelCallback = ref(null);

  const actionPopup = (title, message, onConfirm = null, onCancel = null) => {
    console.log('BaseView에서 actionPopup 호출됨');
    onConfirmCallback.value = onConfirm;
    onCancelCallback.value = onCancel;
    const actionPopupStore = useActionPopupStore();
    actionPopupStore.showActionPopup(title, message);
  };


  /** ✏️ return */
  return {
    ref, reactive, shallowRef,
    provide, inject, nextTick,
    onBeforeMount, onMounted, onBeforeUpdate, onUpdated,
    onBeforeUnmount, onUnmounted, 
    watch, watchEffect, computed, mergeProps, defineExpose,
    router,
    storeToRefs,
    toastPopup,
    popupDialog,
    globalEnvs,
    actionPopup,
    onConfirmCallback,
    onCancelCallback,
    validationStore,
    errors,
    focusElement,
  }
}
