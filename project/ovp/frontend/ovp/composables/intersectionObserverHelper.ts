import { ref, onMounted, onBeforeUnmount } from "vue";
import { IntersectionObserverHandler } from "@/utils/intersection-observer";
import { usePagingStore } from "~/store/common/paging";

export function useIntersectionObserver(
  realCallback: (params?: any) => Promise<void>,
  diffTargetId?: string,
  from?: number,
  size?: number,
) {
  const targetId = diffTargetId || "dataList";
  const scrollTrigger = ref<HTMLElement | null>(null);
  const loaderId = "loader";
  let intersectionHandler: IntersectionObserverHandler | null = null;

  const pagingStore = usePagingStore();
  const {
    setFrom,
    setSize,
    setIntersectionHandler,
    updateIntersectionHandler,
  } = pagingStore;
  const { from: pageStoreFrom, size: pageStoreSize } = storeToRefs(pagingStore);

  if (from !== undefined) {
    setFrom(from);
  }
  if (size !== undefined) {
    setSize(size);
  }

  const getDataCallback = async (count: number, loader: HTMLElement | null) => {
    // loader start
    if (loader) {
      loader.style.display = "flex";
    }

    setScrollOptions(count);

    await realCallback();

    if (loader) {
      loader.style.display = "none";
    }
  };

  onMounted(() => {
    intersectionHandler = new IntersectionObserverHandler(
      targetId,
      scrollTrigger.value,
      loaderId,
      pageStoreFrom.value,
      pageStoreSize.value,
      getDataCallback,
    );
    setIntersectionHandler(intersectionHandler);
  });

  onBeforeUnmount(() => {
    if (intersectionHandler) {
      intersectionHandler.disconnect();
    }
  });

  const setScrollOptions = (count: number) => {
    setFrom(count);
    updateIntersectionHandler(count);
  };

  return {
    scrollTrigger,
    setScrollOptions: setScrollOptions,
  };
}
