import { defineStore } from "pinia";
import { ref } from "vue";

export const useOrganizationChartStore = defineStore("organizationChart", () => {

    const inputData = ref([]) 
    const cmd = ref(null)

    return {inputData, cmd}
})
