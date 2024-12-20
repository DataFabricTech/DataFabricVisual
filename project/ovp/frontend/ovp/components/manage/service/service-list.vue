<template>
  <div class="work-list">
    <div class="l-top-bar">
      <span class="title">서비스 목록</span>
      <button
        class="button button-secondary-stroke"
        @click="modalOpen(props.addModalId)"
      >
        서비스 추가
      </button>
    </div>
    <div class="menu menu-data menu-lg">
      <div class="menu-head">
        <div class="h-group">
          <div class="search-input">
            <label class="hidden-text" for="data-menu-search"
              >데이터 모델 검색</label
            >
            <input
              id="data-menu-search"
              class="text-input"
              placeholder="검색어 입력"
              v-model="keyword"
              @keypress.enter="search"
            />
            <svg-icon class="text-input-icon" name="search"></svg-icon>
            <button
              class="search-input-action-button button button-neutral-ghost button-sm"
              type="button"
              @click="reset"
            >
              <span class="hidden-text">지우기</span>
              <svg-icon class="button-icon" name="close"></svg-icon>
            </button>
          </div>
          <button class="button button-neutral-ghost" @click="reset">
            <span class="hidden-text">리셋</span>
            <svg-icon class="svg-icon" name="reset"></svg-icon>
          </button>
        </div>
      </div>
      <ul class="menu-list">
        <li
          :class="menuSelectedClass(service)"
          v-for="service in store.serviceList"
          @click="changeService(service)"
        >
          <button class="menu-button">
            <img
              v-if="servicesWithTrinoById[service.serviceType]"
              :src="servicesWithTrinoById[service.serviceType].imgUrl"
              :alt="servicesWithTrinoById[service.serviceType].label"
              :width="25"
            />
            <span class="menu-text">{{ service.name }}</span>
            <span class="menu-subtext"
              >({{
                service.owner && service.owner.name
                  ? service.owner.name
                  : "없음"
              }})</span
            >
          </button>
        </li>
      </ul>
      <!-- 결과 없을 시 no-result 표시 -->
      <div class="no-result" v-if="store.serviceList.length == 0">
        <div class="notification">
          <svg-icon class="notification-icon" name="info"></svg-icon>
          <p class="notification-detail">등록된 서비스가 없습니다.</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import {
  ref,
  onMounted,
  defineProps,
  defineEmits,
  watch,
  onUnmounted,
} from "vue";
import type { Service } from "@/type/service";
import { useServiceStore } from "@/store/manage/service";
import $constants from "@/utils/constant";
import { debounce } from "lodash";
import { useRouter } from "nuxt/app";

const router = useRouter();
const {
  getServiceList,
  searchServiceList,
  changeCurrentService,
  emptyService,
  changeTab,
  servicesWithTrinoById,
} = useServiceStore();
const store = useServiceStore();
const TAB_INGESTION = $constants.SERVICE.TAB[0].value;

const keyword = ref<string>("");

const menuSelectedClass = (value: Service): string => {
  return store.service.id == value.id
    ? "menu-item is-menu-item-selected"
    : "menu-item";
};

onMounted(() => {
  getServiceList();
});

onUnmounted(() => {
  emptyService();
});

function createDebouncedSearch() {
  return debounce(async () => {
    await search();
  }, 300);
}

const debouncedSearch = createDebouncedSearch();

watch(
  () => keyword.value,
  () => {
    debouncedSearch();
  },
);

async function search(): Promise<void> {
  emptyService();
  await searchServiceList(keyword.value, "0");
}

async function reset(): Promise<void> {
  keyword.value = "";
  emptyService();
  await getServiceList();
}

function changeService(service: Service): void {
  changeCurrentService(service);
  changeTab(TAB_INGESTION);
  router.push(`/portal/manage/service?id=${service.id}`);
}

const props = defineProps({
  addModalId: { type: String, default: "" },
});

const emit = defineEmits<{
  (e: "modalOpen", modalId: string): void;
}>();

const modalOpen = (modalId: string): void => {
  emit("modalOpen", modalId);
};
</script>
