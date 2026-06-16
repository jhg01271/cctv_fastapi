import { defineStore } from 'pinia';
import { ref } from 'vue';

export const useToastPopupStore = defineStore('toastPopupStore', () => {
  const isVisible = ref(false);
  const sections = ref([]);

  const addToast = (title, content, className, callbacks = {}) => {
    const defaultCallbacks = {
      onCancel: () => {},
      onConfirm: () => {}
    };
    const finalCallbacks = { ...defaultCallbacks, ...callbacks };

    sections.value.push({
      id: Date.now(),
      title,
      content,
      visible: true,
      className,
      onCancel: finalCallbacks.onCancel,
      onConfirm: finalCallbacks.onConfirm
    });
    isVisible.value = true;
  };

  const hideToast = (id) => {
    const sectionIndex = sections.value.findIndex((section) => section.id === id);
    if (sectionIndex !== -1) {
      sections.value.splice(sectionIndex, 1);
    }
    if (sections.value.length === 0) {
      isVisible.value = false;
    }
  };

  return { isVisible, sections, addToast, hideToast };
});
