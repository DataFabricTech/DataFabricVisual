import { IntersectionObserverHandler } from "@/utils/intersection-observer";

export const usePagingStore = defineStore("pagingStore", () => {
  let intersectionHandler: IntersectionObserverHandler | null = null;

  const page = ref(0);
  const from = ref(0);
  const size = ref(20);

  const setFrom = (newFrom: number) => {
    from.value = newFrom;
  };

  const setSize = (newSize: number) => {
    size.value = newSize;
  };

  // pagination 을 사용하는것이 아니기 때문에 특정 page 를 조회하는 등의 기능이 없음
  // -> page 값은 from 값이 변경 되면 자동으로 계산되도록 처리.
  watch(from, () => {
    page.value = from.value / size.value;
  });

  const updateIntersectionHandler = (count: number) => {
    if (count < 1) {
      if (intersectionHandler !== null) {
        intersectionHandler.updateChangingInitialCount(from.value);
        intersectionHandler.scrollToFirElement();
      }
    }
  };

  const setIntersectionHandler = (ih: any) => {
    intersectionHandler = ih;
  };

  return {
    page,
    from,
    size,
    setFrom,
    setSize,
    updateIntersectionHandler,
    setIntersectionHandler,
  };
});
