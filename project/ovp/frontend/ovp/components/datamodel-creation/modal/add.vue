<template>
  <Modal
    title="데이터 모델 추가"
    background="non-interactive"
    displayDirective="show"
    overlayTransition="vfm-fade"
    contentTransition="vfm-fade"
    :clickToClose="true"
    :escToClose="true"
    :width="900"
    :height="837"
    :lockScroll="true"
    swipeToClose="none"
    @closed="onCloseModal"
    @before-open="onOpenModal"
    @click-outside="onCancelModal"
    @cancel="onCancelModal"
    @confirm="onConfirmModal"
  >
    <template v-slot:body>
      <div class="data-add">
        <add-transfer></add-transfer>
        <Tab
          class="h-[350px]"
          :data="$constants.DATAMODEL_CREATION.ADD.DETAIL_TAB"
          label-key="label"
          value-key="value"
          current-item-type="value"
          :current-item="currDetailTab"
          @change="changeDetailTab"
        >
          <template #sample>
            <add-detail-grid
              :data="sampleData"
              :owner="selectedItemOwner"
              no-data-msg="샘플 데이터가 없습니다."
            ></add-detail-grid>
          </template>
          <template #profile>
            <add-detail-grid
              :data="profileData"
              :owner="selectedItemOwner"
              no-data-msg="데이터 프로파일링 정보가 없습니다."
            ></add-detail-grid>
          </template>
          <template #recommend>
            <div class="data-list" v-if="recommendData.length > 0">
              <template v-for="Data in recommendData">
                <resource-box
                  class="is-resource-box-no-action"
                  :data-obj="Data"
                  :is-box-selected-style="true"
                  :show-owner="true"
                  :show-category="true"
                  :use-data-nm-link="true"
                  :use-context-box="false"
                  @model-nm-click="clickRecommendDataModel"
                />
              </template>
            </div>

            <div v-else class="no-result">
              <div class="notification">
                <svg-icon class="notification-icon" name="info"></svg-icon>
                <p class="notification-detail">
                  추천 데이터 모델 정보가 없습니다.
                </p>
              </div>
            </div>
          </template>
        </Tab>
      </div>
    </template>
  </Modal>
</template>
<script setup lang="ts">
import Modal from "@extends/modal/Modal.vue";
import AddTransfer from "~/components/datamodel-creation/modal/add-transfer.vue";
import { useDataModelSearchStore } from "~/store/datamodel-creation/search";
import { storeToRefs } from "pinia";
import Tab from "@extends/tab/Tab.vue";
import $constants from "~/utils/constant";
import AddDetailGrid from "~/components/datamodel-creation/modal/add-detail-grid.vue";
import RecommendModel from "@/components/search/detail-tab/recommend-model.vue";

import { useRouter } from "nuxt/app";

const router = useRouter();

// 탐색 > 데이터 모델 조회 Store
const dataModelSearchStore = useDataModelSearchStore();
const {
  currDetailTab,
  sampleData,
  profileData,
  recommendData,
  selectedItemOwner,
  selectedModelList,
  nSelectedListData,
  infiniteScrollSettingDone,
} = storeToRefs(dataModelSearchStore);
const {
  resetReloadList,
  getFilters,
  changeDetailTab,
  resetDetailBox,
  setNSelectedListData,
  setSearchKeyword,
  setSelectedItem,
  setSearchMyKeyword,
  setCurrTab,
  cancelAllSelection,
} = dataModelSearchStore;

Promise.all([resetReloadList(), getFilters(), resetDetailBox()]);

const onOpenModal = async () => {
  cancelAllSelection();
  // 전체+MY / 필터 / 내부 선택 목록 데이터 초기화
  setNSelectedListData($_cloneDeep(selectedModelList.value));
};

const emit = defineEmits<{
  (e: "close"): void;
}>();

const onCancelModal = () => {
  emit("close");
};

const onConfirmModal = () => {
  // 내부 데이터 저장modalId
  selectedModelList.value = nSelectedListData.value.filter((item) => {
    item.idShowDetail = false;
    return true;
  });
  emit("close");
};

const onCloseModal = () => {
  const TAB_DEFAULT = $constants.DATAMODEL_CREATION.ADD.TAB[0].value;
  setSearchKeyword("");
  setSearchMyKeyword("");
  setNSelectedListData([]);
  setSelectedItem({});
  setCurrTab(TAB_DEFAULT);
  infiniteScrollSettingDone.value = false;
};

const clickRecommendDataModel = (data: object) => {
  const { id, fqn, type } = data as { id: string; fqn: string; type: string };

  const queryParams = new URLSearchParams({
    type,
    id,
    fqn,
  }).toString();

  const detailPath = "/portal/search/detail";
  const fullPath = `${detailPath}?${queryParams}`;

  window.open(fullPath, "_blank", "noopener,noreferrer");
};
</script>
<style lang="scss" scoped></style>
