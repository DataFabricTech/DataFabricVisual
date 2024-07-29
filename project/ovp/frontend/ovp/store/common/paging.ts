import { IntersectionObserverHandler } from "@/utils/intersection-observer";

export const usePagingStore = defineStore("pagingStore", () => {
  let intersectionHandler: IntersectionObserverHandler | null = null;

  const state = reactive({
    page: 0,
    from: 0,
    size: 20,
  });

  const setScrollFrom = (count: number) => {
    state.from = count;
    state.page = state.from / state.size;
  };

  const updateIntersectionHandler = (count: number) => {
    if (count < 1) {
      if (intersectionHandler !== null) {
        intersectionHandler.updateChangingInitialCount(state.from);
        intersectionHandler.scrollToFirElement();
      }
    }
  };

  const setIntersectionHandler = (ih: any) => {
    intersectionHandler = ih;
  };

  return {
    state,
    setScrollFrom,
    updateIntersectionHandler,
    setIntersectionHandler,
  };
});
