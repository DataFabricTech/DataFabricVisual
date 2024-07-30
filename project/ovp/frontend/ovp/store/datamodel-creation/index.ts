import { defineStore } from "pinia";

export const creationStore = defineStore("creation", () => {
  const { $api } = useNuxtApp();

  const modelList = ref([]);

  // TODO: 임시 데이터 .. store 연동시 제거
  modelList.value = [
    {
      id: "1",
      label: "데이터 모델1",
      owner: "admin",
      bookmarked: true,
      category: "test",
      serviceType: "",
      tag: "1",
      fqn: "df2.test_db.test_db.Employee",
      type: "table",
    },
    {
      id: "2",
      label: "데이터 모델",
      owner: "b",
      bookmarked: false,
      category: "tel",
      serviceType: "",
      tag: "2",
      fqn: "df2.test_db.Employee2",
      type: "table",
    },
    {
      id: "3",
      label: "데이터 모델",
      owner: "b",
      bookmarked: false,
      category: "tel",
      serviceType: "",
      tag: "2",
      fqn: "df2.test_db.Employee3",
      type: "table",
    },
    {
      id: "4",
      label: "데이터 모델",
      owner: "admin",
      bookmarked: false,
      category: "tel",
      serviceType: "",
      tag: "3",
      fqn: "df2.test_db.Employee4",
      type: "container",
    },
    {
      id: "5",
      label: "데이터 모델",
      owner: "c",
      bookmarked: false,
      category: "test",
      serviceType: "",
      tag: "5",
      fqn: "df2.test_db.Employee5",
      type: "table",
    },
    {
      id: "6",
      label: "데이터 모델",
      owner: "jack",
      bookmarked: false,
      category: "test",
      serviceType: "",
      tag: "6",
      fqn: "df2.test_db.Employee6",
      type: "table",
    },
    {
      id: "7",
      label: "테스트",
      owner: "b",
      bookmarked: false,
      category: "test",
      serviceType: "",
      tag: "6",
      fqn: "df5.test_db.Employee7",
      type: "table",
    },
  ];

  return {
    modelList,
  };
});
