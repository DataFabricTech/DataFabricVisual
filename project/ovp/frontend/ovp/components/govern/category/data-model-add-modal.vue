<template>
  <Modal
    title="데이터 모델 추가"
    class="modal modal-padding-16"
    :modal-id="props.modalId"
    background="non-interactive"
    displayDirective="show"
    overlayTransition="vfm-fade"
    contentTransition="vfm-fade"
    :clickToClose="true"
    :escToClose="true"
    :width="480"
    :height="548"
    :top="410"
    :lockScroll="false"
    swipeToClose="none"
    @cancel="onCancel"
    @confirm="onConfirm"
  >
    <template v-slot:body>
      <div class="data-info">
        <search-input
          class="search-input-lg"
          :is-search-input-default-type="false"
          :placeholder="'검색어를 입력하세요.'"
          @on-input="onInput"
        ></search-input>
        <div class="filters">
          <div class="select select-clean">
            <button class="select-button">
              <span class="select-button-title">소유자</span>
              <svg-icon
                class="svg-icon select-indicator"
                name="chevron-down-medium"
              ></svg-icon>
            </button>
          </div>
          <div class="select select-clean">
            <button class="select-button">
              <span class="select-button-title">태그</span>
              <svg-icon
                class="svg-icon select-indicator"
                name="chevron-down-medium"
              ></svg-icon>
            </button>
          </div>
          <div class="select select-clean">
            <button class="select-button">
              <span class="select-button-title">서비스</span>
              <svg-icon
                class="svg-icon select-indicator"
                name="chevron-down-medium"
              ></svg-icon>
            </button>
          </div>
          <div class="select select-clean">
            <button class="select-button">
              <span class="select-button-title">서비스 타입</span>
              <svg-icon
                class="svg-icon select-indicator"
                name="chevron-down-medium"
              ></svg-icon>
            </button>
          </div>
          <button class="button button-error-lighter button-sm" type="button">
            <svg-icon class="button-icon" name="reset"></svg-icon>
            <span class="button-title">초기화</span>
          </button>
        </div>
        <Tab
          style="width: 100%"
          :data="tabOptions2"
          :label-key="'label'"
          :value-key="'value'"
          :current-item="currentTab2"
          :current-item-type="'index'"
          @change="changeTab2"
        ></Tab>
        <!-- 탭 시작  -->
        <strong>선택 <em class="primary">0개</em></strong>
        <div class="table-scroll">
          <table>
            <colgroup>
              <col style="width: 10%" />
              <col />
            </colgroup>
            <thead>
              <tr>
                <th>
                  <div class="checkbox">
                    <input
                      type="checkbox"
                      id="checkbox-all-select"
                      class="checkbox-input"
                      checked
                    />
                    <label for="checkbox-all-select" class="checkbox-label">
                      <span class="hidden-text"> 전체 선택 </span>
                    </label>
                  </div>
                </th>
                <th class="text-center">데이터 모델 정보</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td>
                  <div class="checkbox">
                    <input
                      type="checkbox"
                      id="checkbox-01-data"
                      class="checkbox-input"
                    />
                    <label for="checkbox-01-data" class="checkbox-label">
                      <span class="hidden-text"> 첫번째 데이터 모델 </span>
                    </label>
                  </div>
                </td>
                <td>
                  <div class="table-data">
                    <div class="l-between">
                      <div class="breadcrumb">
                        <ul class="breadcrumb-list">
                          <li class="breadcrumb-item">
                            <span class="breadcrumb-text">1depth</span>
                          </li>
                          <li class="breadcrumb-item">
                            <span class="breadcrumb-text">2depth</span>
                          </li>
                          <li class="breadcrumb-item">
                            <span class="breadcrumb-text">데이터 모델</span>
                          </li>
                        </ul>
                      </div>
                      <button class="button link-button-support button-sm">
                        상세보기
                      </button>
                    </div>
                    <div class="h-group w-full">
                      <img src="" />
                      <span class="table-data-title"
                        >세종특별자치시 상하수도요금표</span
                      >
                    </div>
                  </div>
                </td>
              </tr>
              <tr>
                <td>
                  <div class="checkbox">
                    <input
                      type="checkbox"
                      id="checkbox-01-data"
                      class="checkbox-input"
                    />
                    <label for="checkbox-01-data" class="checkbox-label">
                      <span class="hidden-text"> 첫번째 데이터 모델 </span>
                    </label>
                  </div>
                </td>
                <td>
                  <div class="table-data">
                    <div class="l-between">
                      <div class="breadcrumb">
                        <ul class="breadcrumb-list">
                          <li class="breadcrumb-item">
                            <span class="breadcrumb-text">1depth</span>
                          </li>
                          <li class="breadcrumb-item">
                            <span class="breadcrumb-text">2depth</span>
                          </li>
                          <li class="breadcrumb-item">
                            <span class="breadcrumb-text">데이터 모델</span>
                          </li>
                        </ul>
                      </div>
                      <button class="button link-button-support button-sm">
                        상세보기
                      </button>
                    </div>
                    <div class="h-group w-full">
                      <img src="" />
                      <span class="table-data-title"
                        >세종특별자치시
                        상하수도요금표상하수도요금표상하수도요금표상하수도요금표상하수도요금표</span
                      >
                    </div>
                  </div>
                </td>
              </tr>
              <tr>
                <td>
                  <div class="checkbox">
                    <input
                      type="checkbox"
                      id="checkbox-01-data"
                      class="checkbox-input"
                    />
                    <label for="checkbox-01-data" class="checkbox-label">
                      <span class="hidden-text"> 첫번째 데이터 모델 </span>
                    </label>
                  </div>
                </td>
                <td>
                  <div class="table-data">
                    <div class="l-between">
                      <div class="breadcrumb">
                        <ul class="breadcrumb-list">
                          <li class="breadcrumb-item">
                            <span class="breadcrumb-text">1depth</span>
                          </li>
                          <li class="breadcrumb-item">
                            <span class="breadcrumb-text">2depth</span>
                          </li>
                          <li class="breadcrumb-item">
                            <span class="breadcrumb-text">데이터 모델</span>
                          </li>
                        </ul>
                      </div>
                      <button class="button link-button-support button-sm">
                        상세보기
                      </button>
                    </div>
                    <div class="h-group w-full">
                      <img src="" />
                      <span class="table-data-title"
                        >세종특별자치시 상하수도요금표</span
                      >
                    </div>
                  </div>
                </td>
              </tr>
              <tr>
                <td>
                  <div class="checkbox">
                    <input
                      type="checkbox"
                      id="checkbox-01-data"
                      class="checkbox-input"
                    />
                    <label for="checkbox-01-data" class="checkbox-label">
                      <span class="hidden-text"> 첫번째 데이터 모델 </span>
                    </label>
                  </div>
                </td>
                <td>
                  <div class="table-data">
                    <div class="l-between">
                      <div class="breadcrumb">
                        <ul class="breadcrumb-list">
                          <li class="breadcrumb-item">
                            <span class="breadcrumb-text">1depth</span>
                          </li>
                          <li class="breadcrumb-item">
                            <span class="breadcrumb-text">2depth</span>
                          </li>
                          <li class="breadcrumb-item">
                            <span class="breadcrumb-text">데이터 모델</span>
                          </li>
                        </ul>
                      </div>
                      <button class="button link-button-support button-sm">
                        상세보기
                      </button>
                    </div>
                    <div class="h-group w-full">
                      <img src="" />
                      <span class="table-data-title"
                        >세종특별자치시 상하수도요금표</span
                      >
                    </div>
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
        <!-- 탭 끝  -->
        <!-- 결과 없을 시 no-result 표시 -->
        <div class="no-result" style="display: none">
          <div class="notification">
            <svg-icon class="notification-icon" name="info"></svg-icon>
            <p class="notification-detail">정보가 없습니다.</p>
          </div>
        </div>
      </div>
    </template>
  </Modal>
</template>

<script setup lang="ts">
import Modal from "@extends/modal/Modal.vue";
import { useNuxtApp } from "nuxt/app";
import { useGovernCategoryStore } from "~/store/governance/Category";
import { storeToRefs } from "pinia";
import SearchInput from "@extends/search-input/SearchInput.vue";
import Tab from "@extends/tab/Tab.vue";
import { ref } from "vue";

const { $vfm } = useNuxtApp();

const categoryStore = useGovernCategoryStore();
const { selectedModelList } = storeToRefs(categoryStore);

const props = defineProps({
  modalId: {
    type: String,
    required: true,
  },
});

// search-input
const onInput = (value: string) => {
  console.log("value", value);
};

// tab
const tabOptions2 = [
  { label: "테이블", value: 1 },
  { label: "스토리지", value: 2 },
  { label: "융합모델", value: 3 },
];

const currentTab2 = ref();
currentTab2.value = 1;

const changeTab2 = (item: number | string) => {
  console.log("tab item", item);
  currentTab2.value = item;
};

// modal
const onCancel = () => {
  $vfm.close(props.modalId);
};

const onConfirm = async () => {
  $vfm.close(props.modalId);
};
</script>

<style scoped></style>
