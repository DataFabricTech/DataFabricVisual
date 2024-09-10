<template>
  <div>
    <div class="l-top-bar">
      <div class="search-input w-[541px]">
        <label class="hidden-text" for="text-input-example-11">label</label>
        <input
          id="text-input-example-11"
          class="text-input"
          placeholder="검색어 입력"
          v-model="keyword"
          @keypress.enter="searchDataModel"
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
      <div class="h-group w-full">
        <div class="checkbox">
          <input
            type="checkbox"
            id="checkbox-menu-1"
            class="checkbox-input"
            @change="toggleAllCheck($event.target.checked)"
          />
          <label for="checkbox-menu-1" class="checkbox-label"> 전체선택 </label>
        </div>
        <div class="h-group ml-auto gap-2">
          <button
            class="button button-secondary-stroke"
            @click="deleteDataModel"
          >
            데이터모델 제외
          </button>
          <button
            class="button button-secondary"
            @click="showDataModelAddModal"
          >
            데이터모델추가
          </button>
        </div>
      </div>
    </div>
    <div class="l-resource-box l-split mt-3">
      <div class="data-page">
        <div class="data-list">
          <resource-box-list
            :data-list="dataModels"
            :is-box-selected-style="true"
            :selected-model-list="selectedDataModels"
            :show-owner="true"
            :show-category="true"
            :use-prv-btn="true"
            :use-list-checkbox="true"
            @preview-click="clickPreview"
            @checked-value-changed="checkDataModel"
          ></resource-box-list>
        </div>
      </div>
      <preview
        :preview-data="dataModel"
        :is-show-preview="isShowPreview"
        @change="showPreview"
      ></preview>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { useModal } from "vue-final-modal";
import DataModelAddModal from "~/components/govern/common/modal/add-data-model.vue";
import { useGlossaryStore } from "@/store/glossary";

const { getDataModels, getDataModel, updateTerm, dataModels, dataModel, term } =
  useGlossaryStore();
getDataModels(term.fullyQualifiedName);

const { open, close } = useModal({
  component: DataModelAddModal,
  attrs: {
    currentPageType: "glossary",
    onConfirm() {
      // TODO: 데이터모델 목록 조회 및 초기화
      close();
    },
    onClose() {
      close();
    },
  },
});

const keyword = ref("");

const showDataModelAddModal = () => {
  open();
};

function searchDataModel() {
  getDataModels(term.fullyQualifiedName, keyword.value);
}

const isShowPreview = ref(false);

function showPreview(): void {
  isShowPreview.value = !isShowPreview.value;
}

function clickPreview(data: any): void {
  getDataModel(data.fullyQualifiedName);
  isShowPreview.value = true;
}

const selectedDataModels = ref<string[]>([]);

function checkDataModel(ids: string[]) {
  selectedDataModels.value = [...ids];
}

function toggleAllCheck(allCheck: boolean) {
  if (allCheck) {
    selectedDataModels.value = dataModels.map(
      (dataModel) => dataModel.id,
    ) as string[];
  } else {
    selectedDataModels.value = [];
  }
}

async function deleteDataModel() {
  const requestBody: object[] = [];
  selectedDataModels.value.forEach((id) => {
    requestBody.push({ id: id, type: "table" });
  });
  await updateTerm(term.id, requestBody);
  await getDataModels(term.fullyQualifiedName);
}
</script>
