export const useLayoutHeaderStore = defineStore("layoutHeader", () => {
  const searchInputValue = ref("");
  return { searchInputValue };
});
