import { defineStore } from 'pinia';
import { ref } from 'vue';

export const usePopupStore = defineStore('popup', () => {
    const isVisible = ref({});
    const title = ref('');
    const slotContent = ref(null);
    const componentProps = ref({});

    const showPopup = (id) => {
        isVisible.value[id] = true;
    };

    const hidePopup = (id) => {
        isVisible.value[id] = false;
    };

    return {
        isVisible,
        title,
        slotContent,
        componentProps,
        showPopup,
        hidePopup
    };
});
