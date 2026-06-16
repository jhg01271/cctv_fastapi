import { defineStore } from "pinia";
import { ref } from "vue";

export const useSafetyOrgnChartStore = defineStore("safetyOrgnChart", () => {

    const inputData = ref([]) 
    const cmd = ref(null)

    return {inputData, cmd}
})
