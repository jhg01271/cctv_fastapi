import { defineStore } from 'pinia';
import { ref } from 'vue';

export const useActionPopupStore = defineStore('actioPopupStore', () => {
  const isVisible = ref(false);
  const title = ref('');
  const content = ref('');

  const showActionPopup = (popupTitle, popupContent) => {
    title.value = popupTitle;
    content.value = popupContent;
    isVisible.value = true;
  };

  const hideActionPopup = () => {
    isVisible.value = false;
  };

  return { isVisible, title, content, showActionPopup, hideActionPopup };
});
