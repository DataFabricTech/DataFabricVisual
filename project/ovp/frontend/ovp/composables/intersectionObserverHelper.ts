import { ref, onMounted, onBeforeUnmount } from "vue";
import { IntersectionObserverHandler } from "@/utils/intersection-observer";
import { usePagingStore } from "~/store/common/paging";

export function useIntersectionObserver(
  realCallback: (params?: any) => Promise<void>,
  from?: number,
  size?: number,
) {
  const targetId = "dataList";
  const scrollTrigger = ref<HTMLElement | null>(null);
  const loaderId = "loader";
  let intersectionHandler: IntersectionObserverHandler | null = null;

  const pagingStore = usePagingStore(); // pagingStore 사용
  if (from !== undefined) {
    pagingStore.state.from = from;
  }
  if (size !== undefined) {
    pagingStore.state.size = size;
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
      pagingStore.state.from,
      pagingStore.state.size,
      getDataCallback,
    );
    pagingStore.setIntersectionHandler(intersectionHandler);
  });

  onBeforeUnmount(() => {
    if (intersectionHandler) {
      intersectionHandler.disconnect();
    }
  });

  const setScrollOptions = (count: number) => {
    pagingStore.setScrollFrom(count);
    pagingStore.updateIntersectionHandler(count);
  };

  return {
    scrollTrigger,
    setScrollOptions: setScrollOptions,
  };
}
