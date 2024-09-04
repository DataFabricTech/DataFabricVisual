<template>
  <div class="form-body">
    <div class="form-item">
      <label for="" class="form-label">
        Name <span class="required">*</span></label
      >
      <div class="form-detail flex flex-col">
        <div class="search-input">
          <label
            class="hidden-text"
            for="text-input-example-storage-profiler-name"
            >label</label
          >
          <input
            id="text-input-example-storage-profiler-name"
            class="text-input text-input-lg"
            v-model="ingestionPipeLine.displayName"
            @input="handleNameInput"
          />
          <button
            class="search-input-action-button button button-neutral-ghost button-sm"
            type="button"
            @click="clearNameInput"
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
          <input
            type="checkbox"
            id="manager-sw-storage-profiler-eld"
            class="switch-input"
            :checked="isEnableDebug"
            @click="changeEnableDebug"
          />
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
          :data="sampleTypeOptions"
          :selectedItem="selectedSampleTypeItem"
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
const {
  ingestionPipeLine,
  isEnableDebug,
  isGenerateSampleData,
  isComputeMetric,
  sampleTypeOptions,
  selectedSampleTypeItem,
  profileSample,
  sampleDataRowsCount,
  CFP_includes,
  CFP_excludes,
  BFP_includes,
  BFP_excludes,
} = storeToRefs(collectionAddStore);
const {
  handleNameInput,
  clearNameInput,
  addInput,
  clearInput,
  removeInput,
  changeEnableDebug,
  changeGenerateSampleData,
  changeComputeMetric,
  selectSampleType,
  handleProfileSampleInput,
  handleRowCount,
} = collectionAddStore;
</script>
<style lang="scss" scoped>
/* @import "./index.scss"; */
</style>
