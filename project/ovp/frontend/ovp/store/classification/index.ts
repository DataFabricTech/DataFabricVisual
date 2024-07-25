import {defineStore} from "pinia";
import type {Ref} from "vue";

interface Classification {
    id: string;
    name: string;
    displayName: string | null;
}

export const classificationStore = defineStore("classification", () => {
    const { $api} = useNuxtApp();

    const classificationList :Ref<Classification[]> = ref([]);
    const classificationListTotal :Ref<number | undefined> = ref();

    const getClassificationList = async () => {
        const data: any = await $api(`/api/classifications/list`);
        classificationList.value = data.data.classificationList;
        classificationListTotal.value = data.data.total;
    };

    return {
        classificationList,
        classificationListTotal,
        getClassificationList
    };
});
