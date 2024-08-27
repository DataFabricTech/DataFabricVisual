<template>
  <div class="form-body">
    <div class="form-item">
      <label for="" class="form-label"> Name </label>
      <div class="form-detail flex flex-col">
        <div class="search-input">
          <label class="hidden-text" for="text-input-example-4">label</label>
          <input
              id="text-input-example-4"
              class="text-input text-input-lg"
              v-model="ingestionPipeLine.displayName"
              @input="handleNameInput"
          />
          <button
              class="search-input-action-button button button-neutral-ghost button-sm"
              type="button"
          >
            <span class="hidden-text">지우기</span>
            <svg-icon class="button-icon" name="close"></svg-icon>
          </button>
        </div>
      </div>
    </div>
    <div class="form-item">
      <label for="" class="form-label">Enable Debug Log</label>
      <div class="form-detail">
        <div class="switch">
          <input type="checkbox" id="manager-sw-storage-profiler-eld" class="switch-input" />
          <label for="manager-sw-storage-profiler-eld" class="switch-label">
            <div class="switch-control"></div>
          </label>
        </div>
      </div>
    </div>
    <h3 class="form-sub-title">Bucket Filter Pattern</h3>
    <div class="form-item form-item-expand">
      <div class="form-label-group">
        <label for="" class="form-label"> Includes </label>
        <button
            class="button button-primary button-sm"
            type="button"
            @click="addInput('BFP_includes')"
        >
          <span class="hidden-text">추가</span>
          <svg-icon class="button-icon" name="plus"></svg-icon>
        </button>
      </div>
      <div
          class="form-detail"
          v-for="(item, index) in BFP_includes"
          :key="index"
      >
        <div class="form-delete-group">
          <div class="search-input">
            <input
                :id="'text-input-storage-profiler-bfp-includes-' + index"
                class="text-input text-input-lg"
                v-model="item.name"
            />
            <button
                class="search-input-action-button button button-neutral-ghost button-sm"
                type="button"
                @click="clearInput('BFP_includes', index)"
            >
              <span class="hidden-text">지우기</span>
              <svg-icon class="button-icon" name="close"></svg-icon>
            </button>
          </div>
          <button
              class="button button-ghost button-sm"
              type="button"
              @click="removeInput('BFP_includes', index)"
          >
            <span class="hidden-text">삭제</span>
            <svg-icon class="button-icon" name="close"></svg-icon>
          </button>
        </div>
      </div>
    </div>
    <div class="form-item form-item-expand">
      <div class="form-label-group">
        <label for="" class="form-label"> Excludes </label>
        <button
            class="button button-primary button-sm"
            type="button"
            @click="addInput('BFP_excludes')"
        >
          <span class="hidden-text">추가</span>
          <svg-icon class="button-icon" name="plus"></svg-icon>
        </button>
      </div>
      <div
          class="form-detail"
          v-for="(item, index) in BFP_excludes"
          :key="index"
      >
        <div class="form-delete-group">
          <div class="search-input">
            <input
                :id="'text-input-storage-profiler-bfp-excludes-' + index"
                class="text-input text-input-lg"
                v-model="item.name"
            />
            <button
                class="search-input-action-button button button-neutral-ghost button-sm"
                type="button"
                @click="clearInput('BFP_excludes', index)"
            >
              <span class="hidden-text">지우기</span>
              <svg-icon class="button-icon" name="close"></svg-icon>
            </button>
          </div>
          <button
              class="button button-ghost button-sm"
              type="button"
              @click="removeInput('BFP_excludes', index)"
          >
            <span class="hidden-text">삭제</span>
            <svg-icon class="button-icon" name="close"></svg-icon>
          </button>
        </div>
      </div>
    </div>

    <h3 class="form-sub-title">Container Filter Pattern</h3>
    <div class="form-item form-item-expand">
      <div class="form-label-group">
        <label for="" class="form-label"> Includes </label>
        <button
            class="button button-primary button-sm"
            type="button"
            @click="addInput('CFP_includes')"
        >
          <span class="hidden-text">추가</span>
          <svg-icon class="button-icon" name="plus"></svg-icon>
        </button>
      </div>
      <div
          class="form-detail"
          v-for="(item, index) in CFP_includes"
          :key="index"
      >
        <div class="form-delete-group">
          <div class="search-input">
            <input
                :id="'text-input-storage-profiler-cfp-includes-' + index"
                class="text-input text-input-lg"
                v-model="item.name"
            />
            <button
                class="search-input-action-button button button-neutral-ghost button-sm"
                type="button"
                @click="clearInput('CFP_includes', index)"
            >
              <span class="hidden-text">지우기</span>
              <svg-icon class="button-icon" name="close"></svg-icon>
            </button>
          </div>
          <button
              class="button button-ghost button-sm"
              type="button"
              @click="removeInput('CFP_includes', index)"
          >
            <span class="hidden-text">삭제</span>
            <svg-icon class="button-icon" name="close"></svg-icon>
          </button>
        </div>
      </div>
    </div>
    <div class="form-item form-item-expand">
      <div class="form-label-group">
        <label for="" class="form-label"> Excludes </label>
        <button
            class="button button-primary button-sm"
            type="button"
            @click="addInput('CFP_excludes')"
        >
          <span class="hidden-text">추가</span>
          <svg-icon class="button-icon" name="plus"></svg-icon>
        </button>
      </div>

      <div
          class="form-detail"
          v-for="(item, index) in CFP_excludes"
          :key="index"
      >
        <div class="form-delete-group">
          <div class="search-input">
            <input
                :id="'text-input-storage-profiler-cfp-excludes-' + index"
                class="text-input text-input-lg"
                v-model="item.name"
            />
            <button
                class="search-input-action-button button button-neutral-ghost button-sm"
                type="button"
                @click="clearInput('CFP_excludes', index)"
            >
              <span class="hidden-text">지우기</span>
              <svg-icon class="button-icon" name="close"></svg-icon>
            </button>
          </div>
          <button
              class="button button-ghost button-sm"
              type="button"
              @click="removeInput('CFP_excludes', index)"
          >
            <span class="hidden-text">삭제</span>
            <svg-icon class="button-icon" name="close"></svg-icon>
          </button>
        </div>
      </div>
    </div>

    <div class="form-item">
      <label for="" class="form-label">Generative Sample Data</label>
      <div class="form-detail">
        <div class="switch">
          <input
              type="checkbox"
              id="manager-sw-storage-profiler-gsd"
              class="switch-input"
              :checked="isGenerateSampleData"
              @click="changeGenerateSampleData"
          />
          <label for="manager-sw-storage-profiler-gsd" class="switch-label">
            <div class="switch-control"></div>
          </label>
        </div>
      </div>
    </div>
    <div class="form-item">
      <label for="" class="form-label">Compute Metrics</label>
      <div class="form-detail">
        <div class="switch">
          <input
              type="checkbox"
              id="manager-sw-storage-profiler-cm"
              class="switch-input"
              :checked="isComputeMetric"
              @click="changeComputeMetric"
          />
          <label for="manager-sw-storage-profiler-cm" class="switch-label">
            <div class="switch-control"></div>
          </label>
        </div>
      </div>
    </div>

    <div class="form-item">
      <label for="" class="form-label">Profile Sample Type </label>
      <div class="form-detail">
        <select-box
            class="select-lg w-full"
            :data="selectOptions"
            :selectedItem="selectedItem"
            label-key="label"
            value-key="value"
            @select="selectSampleType"
        ></select-box>
      </div>
    </div>
    <div class="form-item">
      <label for="" class="form-label">
        Profile Sample<span class="required">*</span></label
      >
      <div class="form-detail flex flex-col">
        <div class="search-input">
          <label
              class="hidden-text"
              for="text-input-example-storage-profiler-ps"
          >label</label
          >
          <input
              type="number"
              id="text-input-example-storage-profiler-ps"
              class="text-input text-input-lg"
              v-model="profileSample"
              @input="handleProfileSampleInput"
          />
        </div>
      </div>
    </div>

    <div class="form-item">
      <label for="" class="form-label">
        Sample Data Rows Count<span class="required">*</span></label
      >
      <div class="form-detail flex flex-col">
        <div class="search-input">
          <label
              class="hidden-text"
              for="text-input-example-storage-profiler-sdrc"
          >label</label
          >
          <input
              type="number"
              id="text-input-example-storage-profiler-sdrc"
              class="text-input text-input-lg"
              v-model="sampleDataRowsCount"
              @input="handleRowCount"
          />
        </div>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
import SelectBox from "@extends/select-box/SelectBox.vue";
import { useServiceCollectionAddStore } from "@/store/manage/service/collection-add/index";
const collectionAddStore = useServiceCollectionAddStore();
const { ingestionPipeLine } = storeToRefs(collectionAddStore);
const {
  setProfileSample,
  setSampleDataRowsCount,
  // set_CFP_includes,
  // set_CFP_excludes,
  // set_BFP_includes,
  // set_BFP_excludes,
  // setIsEnableDebug,
  // setIsGenerateSampleData,
  // setIsComputeMetric,
} = collectionAddStore;

const name = ref("");
const isEnableDebug = ref(false);
const isGenerateSampleData = ref(true);
const isComputeMetric = ref(true);

// NOTE: CFP = container filter pattern
const CFP_includes = reactive<Pattern[]>([]);
const CFP_excludes = reactive<Pattern[]>([]);

// NOTE: BFP = bucket Filter pattern
const BFP_includes = reactive<Pattern[]>([]);
const BFP_excludes = reactive<Pattern[]>([]);

const profileSample = ref(50);
const sampleDataRowsCount = ref(50);

console.log("ingestionPipeLine보자: " + ingestionPipeLine.value.name);

const addInput = (patternAlias: any) => {
  if (patternAlias === "CFP_includes") {
    CFP_includes.push({ name: "" });
  }
  if (patternAlias === "CFP_excludes") {
    CFP_excludes.push({ name: "" });
  }
  if (patternAlias === "BFP_includes") {
    BFP_includes.push({ name: "" });
  }
  if (patternAlias === "BFP_excludes") {
    BFP_excludes.push({ name: "" });
  }
};

const clearInput = (patternAlias: any, index: any) => {
  if (patternAlias === "CFP_includes") {
    CFP_includes[index].name = "";
  }
  if (patternAlias === "CFP_excludes") {
    CFP_excludes[index].name = "";
  }
  if (patternAlias === "BFP_includes") {
    BFP_includes[index].name = "";
  }
  if (patternAlias === "BFP_excludes") {
    BFP_excludes[index].name = "";
  }
};

const removeInput = (patternAlias: any, index: number) => {
  if (patternAlias === "CFP_includes") {
    CFP_includes.splice(index, 1);
  }
  if (patternAlias === "CFP_excludes") {
    CFP_excludes.splice(index, 1);
  }
  if (patternAlias === "BFP_includes") {
    BFP_includes.splice(index, 1);
  }
  if (patternAlias === "BFP_excludes") {
    BFP_excludes.splice(index, 1);
  }
};

const selectOptions = [
  {
    label: "Percentage",
    value: "persentage",
  },
  {
    label: "Rows",
    value: "rows",
  },
];

const selectedItem = ref(selectOptions[0].value);

const selectSampleType = (value: any) => {
  console.log("샘플타입 선택: ", value);
};

const handleNameInput = (e) => {
  name.value = e.target.value;
  setName(name.value);
};

const clearNameInput = () => {
  name.value = "";
  setName("");
};

const changeEnableDebug = () => {
  isEnableDebug.value = !isEnableDebug.value;
};

const changeGenerateSampleData = () => {
  isGenerateSampleData.value = !isGenerateSampleData.value;
};

const changeComputeMetric = () => {
  isComputeMetric.value = !isComputeMetric.value;
};

const handleProfileSampleInput = (e) => {
  console.log("profileSample.value: ", profileSample.value);
  profileSample.value = e.target.value;
  setProfileSample(profileSample.value);
};

const handleRowCount = (e: any) => {
  sampleDataRowsCount.value = e.target.value;
  setSampleDataRowsCount(sampleDataRowsCount.value);
};
</script>
<style lang="scss" scoped>
/* @import "./index.scss"; */
</style>

