<template>
  <div class="lineage">
    <div class="filters">
      <div class="select select-clean">
        <menuSearchButton
          :data="categoryList"
          :selected-items="selectedCateList"
          label-key="id"
          value-key="value"
          title="카테고리"
          :is-multi="true"
          @multiple-change="cateApplyFilter"
        ></menuSearchButton>
      </div>
      <div class="select select-clean">
        <menuSearchButton
          :data="ownerList"
          :selected-items="selectedOwnerList"
          label-key="id"
          value-key="value"
          title="소유자"
          :is-multi="true"
          @multiple-change="ownerApplyFilter"
        ></menuSearchButton>
      </div>
      <div class="select select-clean">
        <menuSearchButton
          :data="tagList"
          :selected-items="selectedTagList"
          label-key="id"
          value-key="value"
          title="태그"
          :is-multi="true"
          @multiple-change="tagApplyFilter"
        ></menuSearchButton>
      </div>
      <div class="select select-clean">
        <menuSearchButton
          :data="serviceList"
          :selected-items="selectedSerivceList"
          label-key="id"
          value-key="value"
          title="서비스"
          :is-multi="true"
          @multiple-change="serviceApplyFilter"
        ></menuSearchButton>
      </div>
      <button
        class="button button-error-lighter button-sm"
        type="button"
        @click="reset"
      >
        <svg-icon class="button-icon" name="reset"></svg-icon>
        <span class="button-title">초기화</span>
      </button>
    </div>
    <Preview
      :preview-data="previewData"
      @change="getPreviewOn"
      :is-show-preview="previewOn"
    ></Preview>
    <LineageGraph
      ref="lineageRef"
      :lineageData="lineageData"
      :previewOn="previewOn"
      @change="modelChoose"
    ></LineageGraph>
    <div class="lineage-control">
      <button
        class="button button-lg button-neutral-stroke lineage-control-zoom-in"
        type="button"
        @click="handleZoomIn"
      >
        <span class="hidden-text">확대</span>
        <svg-icon class="button-icon" name="plus"></svg-icon>
      </button>
      <button
        class="button button-lg button-neutral-stroke lineage-control-zoom-out"
        type="button"
        @click="handleZoomOut"
      >
        <span class="hidden-text">축소</span>
        <svg-icon class="button-icon" name="minus"></svg-icon>
      </button>
      <button
        class="button button-lg button-neutral-stroke mt-2"
        type="button"
        @click="resetView"
      >
        <span class="hidden-text">원좌표</span>
        <svg-icon class="button-icon" name="target-lock"></svg-icon>
      </button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { type Ref, ref } from "vue";
import type { NodeData } from "@/components/search/lineage/lineage";
import { useLineageStore } from "@/store/lineage/lineageStore";
import { useDataModelDetailStore } from "@/store/search/detail/index";

import LineageGraph from "~/components/search/lineage/lineage-graph.vue";
import Preview from "~/components/common/preview/preview.vue";
import menuSearchButton from "@extends/menu-seach/button/menu-search-button.vue";

// TODO: fqn을 호출 할 수 있는 store 필요

const lineageStore = useLineageStore();
const {
  lineageData,
  previewData,
  categoryList,
  ownerList,
  tagList,
  serviceList,
} = storeToRefs(lineageStore);
const {
  getLineageData,
  getPreviewData,
  getCateList,
  getOwnerList,
  getTagList,
  getServiceList,
} = lineageStore;

const dataModelDetailStore = useDataModelDetailStore();
const { getDataModelFqn, getDataModelType } = dataModelDetailStore;

onBeforeMount(async () => {
  // TODO: param => (fqn(외부스토어에서 호출), 필터) 추가 필요
  // await getLineageData(dataModelType, dataModelFqn);

  await getCateList();

  await getOwnerList();

  await getTagList();

  await getServiceList();
});

const lineageRef = ref(null);

const previewOn: Ref<boolean> = ref<boolean>(false);

const getPreviewOn = (isPreviewClosed: boolean) => {
  if (isPreviewClosed === false) {
    lineageRef.value.handleNodeOff();
    previewOn.value = false;
  }
};

const modelChoose = async (nodeData: NodeData) => {
  if (nodeData) {
    previewOn.value = true;
    console.log(nodeData.fqn);
    await getPreviewData(nodeData.fqn);
    previewData.value.modelInfo.model.name = nodeData.label;
  }
};

const handleZoomIn = () => {
  if (lineageRef.value) {
    lineageRef.value.zoomIn();
  }
};

const handleZoomOut = () => {
  if (lineageRef.value) {
    lineageRef.value.zoomOut();
  }
};

const resetView = () => {
  if (lineageRef.value) {
    lineageRef.value.resetView();
  }
};

const reset = async () => {
  if (lineageRef.value) {
    selectedCateList.value = [];
    selectedOwnerList.value = [];
    selectedTagList.value = [];
    selectedSerivceList.value = [];

    /*
     * TODO: param => (fqn, 필터) 추가 필요
     * fqn -> 외부스토어에서 호출
     * 필터 ex
     * {}
     * */
    await getLineageData(getDataModelType(), getDataModelFqn());

    lineageRef.value.reset();
  }

  previewOn.value = false;
};

const selectedCateList: Ref<any> = ref([]);
const selectedOwnerList: Ref<any> = ref([]);
const selectedTagList: Ref<any> = ref([]);
const selectedSerivceList: Ref<any> = ref([]);

const cateApplyFilter = async (value) => {
  selectedCateList.value = value;

  /*
    * TODO: param => (fqn, 필터) 추가 필요
    * fqn -> 외부스토어에서 호출
    * 필터 ex
    * {
      category: selectedCateList.value,
      owner: selectedOwnerList.value,
      tag: selectedTagList.value,
      service: selectedSerivceList.value,
       }
    * */
  await getLineageData();
};
const ownerApplyFilter = async (value) => {
  selectedOwnerList.value = value;

  /*
    * TODO: param => (fqn, 필터) 추가 필요
    * fqn -> 외부스토어에서 호출
    * 필터 ex
    * {
      category: selectedCateList.value,
      owner: selectedOwnerList.value,
      tag: selectedTagList.value,
      service: selectedSerivceList.value,
       }
    * */
  await getLineageData(getDataModelType(), getDataModelFqn());
};
const tagApplyFilter = async (value) => {
  selectedTagList.value = value;

  /*
    * TODO: param => (fqn, 필터) 추가 필요
    * fqn -> 외부스토어에서 호출
    * 필터 ex
    * {
      category: selectedCateList.value,
      owner: selectedOwnerList.value,
      tag: selectedTagList.value,
      service: selectedSerivceList.value,
       }
    * */
  await getLineageData(getDataModelType(), getDataModelFqn());
};
const serviceApplyFilter = async (value) => {
  selectedSerivceList.value = value;

  /*
    * TODO: param => (fqn, 필터) 추가 필요
    * fqn -> 외부스토어에서 호출
    * 필터 ex
    * {
      category: selectedCateList.value,
      owner: selectedOwnerList.value,
      tag: selectedTagList.value,
      service: selectedSerivceList.value,
       }
    * */
  await getLineageData(getDataModelType(), getDataModelFqn());
};
</script>

<style scoped></style>
