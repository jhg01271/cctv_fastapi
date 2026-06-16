import { defineStore } from 'pinia';
import BaseView from '@/components/base/BaseView';
import { getNonParticipation } from '@/stores/safewiz/improvement/api/nonconformityCorrectiveApi';
const { ref, getCompId, t, getCurrentDate, computed, toastPopup } = BaseView();

export const useNonconformityCStore = defineStore('nonconformityc', () => {
    const compId = getCompId();
    const searchParam = ref({
        compId: compId,
        writeYear: getCurrentDate().substring(0, 4)
    });
    const menuList = ref([]);

    const getCardListAndSearch = async () => {
        const param = {
            searchText: searchParam.value.writeYear
        };
        await getNonParticipation(param, true).then(res => {
            console.log('@@Res', res);
            menuList.value = res.list;
        });
    };

    return {
        searchParam,
        getCardListAndSearch,
        menuList
    };
});
