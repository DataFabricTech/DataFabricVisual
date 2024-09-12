import { ref, onMounted, onBeforeUnmount } from "vue";
import { IntersectionObserverHandler } from "@/utils/intersection-observer";
import { usePagingStore } from "~/store/common/paging";

export function useIntersectionObserver({
  callback,
  targetId,
  loaderId,
  from,
  size,
}: {
  callback: (params?: any) => Promise<void>;
  targetId?: string;
  loaderId?: string;
  from?: number;
  size?: number;
}) {
  targetId = targetId || "dataList";
  loaderId = loaderId || "loader";
  const scrollTrigger = ref<HTMLElement | null>(null);
  let intersectionHandler: IntersectionObserverHandler | null = null;

  const pagingStore = usePagingStore();
  const {
    setFrom,
    setSize,
    setIntersectionHandler,
    updateIntersectionHandler,
  } = pagingStore;
  const {
    from: pageStoreFrom,
    size: pageStoreSize,
    isDataLoadDone,
  } = storeToRefs(pagingStore);

  // 초기값 설정
  setFrom(from ?? 0);
  setSize(size ?? 20);

  const getDataCallback = async (count: number, loader: HTMLElement | null) => {
    // loader start
    if (loader) {
      loader.style.display = "flex";
    }

    setScrollOptions(count);

    await callback();

    if (loader) {
      loader.style.display = "none";
    }
  };

  // modal 사용 방식 변경 후,
  // onMounted 보다 dom 생성이 늦어 IntersectionObserverHandler 가 제대로 동작하지 않는 오류가 있음.
  // modal 에서 infinite scroll 을 사용하는 경우 mount 를 이용해서 호출해줘야함.
  const mountIntersectionObserver = () => {
    if (intersectionHandler) {
      intersectionHandler.disconnect();
    }

    setFrom(0);
    setSize(20);

    intersectionHandler = new IntersectionObserverHandler(
      targetId,
      scrollTrigger.value,
      loaderId,
      pageStoreFrom.value,
      pageStoreSize.value,
      getDataCallback,
    );
    setIntersectionHandler(intersectionHandler);
  };
  onMounted(() => {
    mountIntersectionObserver();
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

  // 데이터 loading 이 끝나면 observer를 mount 해준다.
  // infinite scroll 적용되는 페이지 마다 mount 되는 시점이 다르기 때문에 watch 로 처리.
  watch(
    () => isDataLoadDone.value,
    () => {
      mountIntersectionObserver();
    },
  );

  return {
    scrollTrigger,
    setScrollOptions: setScrollOptions,
    mount: mountIntersectionObserver,
  };
}
