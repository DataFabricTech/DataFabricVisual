<!--TODO:[개발] 모달클래스 .modal-padding-16 추가-->
<template>
  <Modal
    title="데이터 모델 추가"
    :modal-id="props.modalId"
    background="non-interactive"
    displayDirective="show"
    overlayTransition="vfm-fade"
    contentTransition="vfm-fade"
    :clickToClose="true"
    :escToClose="true"
    :width="900"
    :height="681"
    :lockScroll="true"
    swipeToClose="none"
    @before-open="onOpenModal"
    @click-outside="onCancelModal"
    @cancel="onCancelModal"
    @confirm="onConfirmModal"
  >
    <template v-slot:body>
      <div class="data-add">
        <add-transfer></add-transfer>
        <Tab
          class="h-full"
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
          <template #kg></template>
        </Tab>
      </div>
    </template>
  </Modal>
</template>
<script setup lang="ts">
import Modal from "@extends/modal/Modal.vue";
import { useCreationStore } from "~/store/datamodel-creation";
import AddTransfer from "~/components/datamodel-creation/modal/add-transfer.vue";
import { useDataModelSearchStore } from "~/store/datamodel-creation/search";
import { storeToRefs } from "pinia";
import Tab from "@extends/tab/Tab.vue";
import $constants from "~/utils/constant";
import AddDetailGrid from "~/components/datamodel-creation/modal/add-detail-grid.vue";

const props = defineProps({
  modalId: {
    type: String,
    required: true,
  },
});

// 탐색 > 데이터 모델 조회 Store
const dataModelSearchStore = useDataModelSearchStore();
const {
  currDetailTab,
  sampleData,
  profileData,
  selectedItemOwner,
  selectedModelList,
  nSelectedListData,
} = storeToRefs(dataModelSearchStore);
const { resetReloadList, getFilters, changeDetailTab, resetDetailBox } =
  dataModelSearchStore;

const onOpenModal = async () => {
  // 전체+MY / 필터 / 내부 선택 목록 데이터 초기화
  nSelectedListData.value = $_cloneDeep(selectedModelList.value);
  await resetReloadList();
  await getFilters();
  await resetDetailBox();
};

const emit = defineEmits<{
  (e: "close"): void;
}>();

const onCancelModal = () => {
  emit("close");
};

const onConfirmModal = () => {
  // 내부 데이터 저장
  selectedModelList.value = nSelectedListData.value;
  emit("close");
};
</script>
<style lang="scss" scoped></style>
