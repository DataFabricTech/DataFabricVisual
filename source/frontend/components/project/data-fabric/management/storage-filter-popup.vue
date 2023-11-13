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
        class="w-[326px]"
        :items="storage.types"
        :checked-items="filter.types"
        @change="filter.types = $event"
      >
        <template v-slot:head>
          <div class="list-head">
            <div class="list-head-title">저장소 유형</div>
            <div class="text-input-group">
              <span class="text-input-icon">
                <svg-icon class="svg-icon" name="search"></svg-icon>
              </span>
              <baseTextInput id="inp-group" value="" placeholder="검색어를 입력하세요."></baseTextInput>
              <Basebutton class="text-input-clear-button button-icon button-link" title="초기화">
                <svg-icon class="svg-icon" name="close"></svg-icon>
              </Basebutton>
            </div>
          </div>
        </template>
      </base-checkbox-list>
      <base-checkbox-list
        class="w-[326px]"
        :enable-check-all="false"
        :items="storage.statusTypes"
        :checked-items="filter.status"
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

const store = useStorageStore();
const { storage, setStorageTypeFilter, setStatusFilter } = store;

const filter: {
  status: Array<string>;
  types: Array<string>;
} = reactive({
  status: [],
  types: []
});

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
