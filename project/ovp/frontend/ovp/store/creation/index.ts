import { defineStore } from "pinia";

export const creationStore = defineStore("creation", () => {
    const { $api } = useNuxtApp();

    const modelList = ref([]);

    // TODO: store 연동시 제거
    modelList.value = [
        {
            label: "데이터 모델1",
            owner: "admin",
            checked: true,
            bookmarked: true,
            category: "test",
            tag: "1",
        },
        {
            label: "데이터 모델",
            owner: "b",
            checked: false,
            bookmarked: false,
            category: "tel",
            tag: "2",
        },
        {
            label: "데이터 모델",
            owner: "b",
            checked: false,
            bookmarked: false,
            category: "tel",
            tag: "2",
        },
        {
            label: "데이터 모델",
            owner: "admin",
            checked: false,
            bookmarked: false,
            category: "tel",
            tag: "3",
        },
        {
            label: "데이터 모델",
            owner: "c",
            checked: false,
            bookmarked: false,
            category: "test",
            tag: "5",
        },
        {
            label: "데이터 모델",
            owner: "jack",
            checked: false,
            bookmarked: false,
            category: "test",
            tag: "6",
        },
        {
            label: "테스트",
            owner: "b",
            checked: false,
            bookmarked: false,
            category: "test",
            tag: "6",
        },
    ];

    return {
        modelList
    };
});

