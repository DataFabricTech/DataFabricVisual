<template>
  <div class="work-list">
    <div class="l-top-bar">
      <span class="title">서비스 목록</span>
      <button class="button button-secondary-stroke" @click="openModal">
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
              @click="keyword = ''"
            >
              <span class="hidden-text">지우기</span>
              <svg-icon class="button-icon" name="close"></svg-icon>
            </button>
          </div>
          <button class="button button-neutral-ghost" @click="getServiceList">
            <span class="hidden-text">리셋</span>
            <svg-icon class="svg-icon" name="reset"></svg-icon>
          </button>
        </div>
      </div>
      <ul class="menu-list">
        <li
          :class="menuSelectedClass(service)"
          v-for="service in store.serviceList"
          @click="getTabContent(`test_df3`)"
        >
          <button class="menu-button">
            <svg-icon
              class="svg-icon menu-data-icon"
              name="resource"
            ></svg-icon>
            <span class="menu-text">{{ service.name }}</span>
            <span class="menu-subtext">({{ service.owner.name }})</span>
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
import { onMounted } from "vue";
import { _ } from "lodash";
import type { Service } from "~/type/service";
import { useServiceStore } from "@/store/admin/service";
import { useServiceCollectionLogStore } from "@/store/admin/service/collection-log/index";
const serviceCollectionLogStore = useServiceCollectionLogStore();
const { setServiceName, getRepositoryDescriptionAPI } =
  serviceCollectionLogStore;

const { getServiceList, searchServiceList } = useServiceStore();
const store = useServiceStore();

const keyword = ref<string>("");

const menuSelectedClass = (value: Service) => {
  return _.isEqual(store.service, value)
    ? "menu-item is-menu-item-selected"
    : "menu-item";
};

onMounted(() => {
  getServiceList();
});

function search() {
  searchServiceList(keyword.value);
}

function openModal() {}

// 서비스 항목에 서비스를 클릭시 실행되는 함수 ( 저장소 탭 - 설명조회API / 표 조회API 호출함수 필요)
function getTabContent(name: string): void {
  setServiceName(name); // store에 클릭한 서비스 name값 set
  getRepositoryDescriptionAPI(); // 클릭한 서비스의 설명조회하는 API호출
  //TODO : 저장소 탭 > 표 데이터 조회 API 호출추가 예정
}
</script>
