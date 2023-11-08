<template>
  <section class="l-side">
    <h3 class="hidden-text">연결정보 목록</h3>
    <div class="v-group w-full">
      <div class="h-group justify-between w-full">
        <BaseButton class="button-icon button-link button-sm" title="초기화" @click="resetSearch">
          <span class="hidden-text">초기화</span>
          <svg-icon name="reset" class="svg-icon"></svg-icon>
        </BaseButton>
        <div class="h-group">
          <div class="kebab-menu">
            <BaseButton @click="isOpen = true" class="button button-icon button-lg" title="열기">
              <span class="hidden-text">정렬 열기</span>
              <svg-icon name="sort" class="svg-icon" />
            </BaseButton>
            <BaseContext
              v-if="isOpen"
              v-on-click-outside="() => isOpen = false"
              class="kebab-context"
              :items="storageSortList"
              @click="onClickSort"
            ></BaseContext>
          </div>
          <BaseButton class="button-icon button-link button-sm" title="필터" @click="onClickOpen">
            <span class="hidden-text">필터</span>
            <svg-icon name="filter" class="svg-icon"></svg-icon>
          </BaseButton>
        </div>
      </div>
      <baseTextInput v-model:model-value="storage.filter.name" placeholder="연결정보 이름을 입력하세요"></baseTextInput>
    </div>
    <div class="search-tree">
      <template v-for="(item, key) in storageList" :key="key">
        <BaseButton class="button button-lg" v-if="item.show">
          <span class="button-text">{{ item.name }}</span>
        </BaseButton>
      </template>
    </div>
    <BaseButton class="button-normal button-lg ml-auto">
      <span class="button-text">연결정보 등록</span>
    </BaseButton>
  </section>
</template>
<script lang="ts" setup>
import { useNuxtApp } from "nuxt/app";
import type { StorageSortContextItem } from "~/components/project/data-fabric/storage/overview/storage-overview";
import { useStorageStore } from "~/store/data-fabric/storage/storage";
import { storeToRefs } from "pinia";
import { vOnClickOutside } from "@vueuse/components";

const store = useStorageStore();
const { storage, storageSortList, storageList } = storeToRefs(store);
const { getStorage, setSort, resetSearch } = store;

onMounted(() => {
  getStorage();
});

const isOpen = ref(false);
function onClickSort(item: StorageSortContextItem) {
  isOpen.value = false;
  setSort(item);
}

const { $vfm } = useNuxtApp();
function onClickOpen() {
  $vfm.open("storage-filter-popup");
}
</script>
