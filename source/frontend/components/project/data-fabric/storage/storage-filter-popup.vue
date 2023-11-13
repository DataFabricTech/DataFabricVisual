<template>
  <modal
    name="storage-filter-popup"
    title="연결정보 상세 필터"
    :use-click-outside="false"
    @close="onCancel"
    @beforeOpen="onBeforeOpen"
  >
    <template v-slot:body>
      <base-checkbox-list
        title="저장소 유형"
        class="w-[326px]"
        :items="data.typeList"
        :use-search-filter="true"
        @change="filter.types = $event"
      >
      </base-checkbox-list>
      <base-checkbox-list
        class="w-[326px]"
        title="연결 상태"
        :enable-check-all="false"
        :items="data.statusTypeList"
        @change="filter.status = $event"
      >
        <template v-slot:head>
          <div class="list-head">
            <div class="list-head-title">연결 상태</div>
          </div>
        </template>
      </base-checkbox-list>
    </template>
    <template v-slot:foot>
      <baseButton class="button-danger button-lg" @click="initFilter">
        <span class="button-text">초기화</span>
      </baseButton>
      <div class="h-group ml-auto">
        <baseButton class="button-normal button-lg" @click="onCancel">
          <span class="button-text">취소</span>
        </baseButton>
        <baseButton class="button-primary button-lg" @click="onSave">
          <span class="button-text">저장</span>
        </baseButton>
      </div>
    </template>
  </modal>
</template>
<script lang="ts" setup>
import { useStorageStore } from "~/store/data-fabric/storage/storage";
import type { BaseCheckBoxListItem } from "~/components/base/checkbox-list";

const store = useStorageStore();
const { storage, setStorageTypeFilter, setStatusFilter } = store;

const filter: {
  status: Array<string>;
  types: Array<string>;
  typeText: string;
} = reactive({
  status: [],
  types: [],
  typeText: ""
});

const data: {
  typeList: Array<BaseCheckBoxListItem>;
  statusTypeList: Array<BaseCheckBoxListItem>;
} = reactive({
  typeList: [],
  statusTypeList : []
});

watch(
  () => storage.filter.storageType,
  (nValue, oValue) => {
    if (_isEqual(nValue, oValue)) {
      return;
    }
    makeStorageTypeList();
  },
  { immediate: true, deep: true }
);
watch(
  () => storage.filter.status,
  (nValue, oValue) => {
    if (_isEqual(nValue, oValue)) {
      return;
    }
    makeStatusTypeList();
  },
  { immediate: true, deep: true }
);

/**
 * 저장소 유형 원본 데이터 생성
 */
function makeStorageTypeList() {
  data.typeList = _map(storage.types, (item) => {
    const findItem = _includes(storage.filter.storageType, item.value);
    item.checked = findItem;
    return item;
  });
}

/**
 * 연결상태 원본 데이터 생성
 */
function makeStatusTypeList() {
  data.statusTypeList = _map(storage.statusTypes, (item) => {
    const findItem = _includes(storage.filter.status, item.value);
    item.checked = findItem;
    return item;
  });
}

/**
 * 필터 초기화 - 초기값(전체 선택 값)으로 복구
 */
function initFilter() {
  filter.types = _map(storage.types, "value");
  filter.status = _map(storage.statusTypes, "value");
}

/**
 * 필터 초기화 - 현 페이지에 저장된 상태로 복구
 */
function resetFilter() {
  filter.types = storage.filter.storageType;
  filter.status = storage.filter.status;
}

/**
 * 필터 초기화 - storage.filter를 다른곳에서도 제어하기 때문에 modal 열기전에 확인
 */
function onBeforeOpen() {
  resetFilter();
}

const { $vfm } = useNuxtApp();
function onCancel() {
  $vfm.close("storage-filter-popup");
}
function onSave() {
  // 필터 저장
  setStorageTypeFilter(filter.types);
  setStatusFilter(filter.status);
  $vfm.close("storage-filter-popup");
}
</script>
