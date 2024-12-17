import { defineStore } from "pinia";

export const sampleStore = defineStore("project", () => {
  const sampleInfo = ref("");

  function getSample() {
    sampleInfo.value = "안녕";
    return sampleInfo;
  }

  return {
    sampleInfo,
    getSample,
  };
});
