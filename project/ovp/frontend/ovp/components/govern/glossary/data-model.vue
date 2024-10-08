<template>
  <div class="category-search">
    <div class="l-top-bar">
      <div class="search-input">
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
          @click="clearKeyword"
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
            v-model="allCheck"
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
    <div v-if="dataModels.length !== 0" class="l-resource-box l-split mt-3">
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
          <div ref="scrollTrigger" class="w-full h-[1px] mt-px"></div>
          <Loading
            id="loader"
            :use-loader-overlay="true"
            class="loader-lg is-loader-inner"
            style="display: none"
          ></Loading>
        </div>
      </div>
      <Preview
        :preview-data="previewData"
        :is-show-preview="isShowPreview"
        @change="showPreview"
      ></Preview>
    </div>
    <div v-else class="no-result mt-3">
      <div class="notification">
        <svg-icon class="notification-icon" name="info"></svg-icon>
        <p class="notification-detail">등록된 정보가 없습니다.</p>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { useModal } from "vue-final-modal";
import DataModelAddModal from "~/components/govern/common/modal/add-data-model.vue";
import { useGlossaryStore } from "@/store/glossary";
import { useSearchCommonStore } from "@/store/search/common";
import { onMounted, ref } from "vue";
import Preview from "~/components/common/preview/preview.vue";
import Loading from "@base/loading/Loading.vue";
import { useIntersectionObserver } from "~/composables/intersectionObserverHelper";
import { useNuxtApp } from "nuxt/app";

const { getDataModels, resetDataModels, updateTerm, dataModels, term } =
  useGlossaryStore();
const { getPreviewData } = useSearchCommonStore();
const searchCommonStore = useSearchCommonStore();
const { previewData } = storeToRefs(searchCommonStore);
const { $alert } = useNuxtApp();

onMounted(async () => {
  resetDataModels();
  await getDataModels(term.fullyQualifiedName, keyword.value);
});

const DATA_MODEL_ADD_MODAL = "data-model-add-modal";

const { open, close } = useModal({
  component: DataModelAddModal,
  attrs: {
    modalId: DATA_MODEL_ADD_MODAL,
    currentPageType: "glossary",
    onConfirm() {
      resetDataModels();
      getDataModels(term.fullyQualifiedName, keyword.value);
      close();
    },
    onClose() {
      close();
    },
  },
});

const keyword = ref("");
const allCheck = ref(false);

const showDataModelAddModal = () => {
  open();
};

function searchDataModel(): void {
  resetDataModels();
  getDataModels(term.fullyQualifiedName, keyword.value);
  isShowPreview.value = false;
}

function clearKeyword() {
  keyword.value = "";
  searchDataModel();
}

const isShowPreview = ref(false);

function showPreview(): void {
  isShowPreview.value = !isShowPreview.value;
}

function clickPreview(data: object): void {
  getPreviewData(data.fullyQualifiedName);
  isShowPreview.value = true;
}

const selectedDataModels = ref<string[]>([]);

function checkDataModel(ids: string[]): void {
  selectedDataModels.value = [...ids];
}

function toggleAllCheck(allCheck: boolean): void {
  if (allCheck) {
    selectedDataModels.value = dataModels.map(
      (dataModel) => dataModel.id,
    ) as string[];
  } else {
    selectedDataModels.value = [];
  }
}

async function deleteDataModel(): Promise<void> {
  if (selectedDataModels.value.length === 0) {
    $alert("데이터모델을 선택해주세요.", "info");
    return;
  }

  const requestBody: object[] = [];
  selectedDataModels.value.forEach((id) => {
    requestBody.push({ id: id, type: "table" });
  });
  await updateTerm(term.id, requestBody);
  resetDataModels();
  await getDataModels(term.fullyQualifiedName, keyword.value);
  isShowPreview.value = false;
  allCheck.value = false;
}

const { scrollTrigger } = useIntersectionObserver(searchDataModel);
</script>
