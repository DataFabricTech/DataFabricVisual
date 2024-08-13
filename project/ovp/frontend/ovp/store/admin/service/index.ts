import { defineStore } from "pinia";
import { ref } from "vue";

export const useServiceStore = defineStore("service", () => {
  const tab = ref<string>("repository");

  function changeTab(value: string) {
    tab.value = value;
  }

  return {
    tab,
    changeTab,
  };
});
