<template>
  <div class="form-body">
    <div class="form-item">
      <label for="" class="form-label"> Name <span class="required">*</span></label>
      <div class="form-detail flex flex-col">
        <div class="search-input">
          <label class="hidden-text" for="text-input-example-4">label</label>
          <input
            id="text-input-example-database-profiler"
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
            id="manager-sw-database-profiler-eld"
            class="switch-input"
            :checked="isEnableDebug"
            @click="changeEnableDebug"
          />
          <label for="manager-sw-database-meta-eld" class="switch-label">
            <div class="switch-control"></div>
          </label>
        </div>
      </div>
    </div>
    <h3 class="form-sub-title">Database Filter Pattern</h3>
    <div class="form-item form-item-expand">
      <div class="form-label-group">
        <label for="" class="form-label"> Includes </label>
        <button
          class="button button-primary button-sm"
          type="button"
          @click="addInput('DFP_includes')"
        >
          <span class="hidden-text">추가</span>
          <svg-icon class="button-icon" name="plus"></svg-icon>
        </button>
      </div>
      <div
        class="form-detail"
        v-for="(item, index) in DFP_includes"
        :key="index"
      >
        <div class="form-delete-group">
          <div class="search-input">
            <input
              :id="'text-input-database-profiler-dfp-includes-' + index"
              class="text-input text-input-lg"
              v-model="item.name"
            />
            <button
              class="search-input-action-button button button-neutral-ghost button-sm"
              type="button"
              @click="clearInput('DFP_includes', index)"
            >
              <span class="hidden-text">지우기</span>
              <svg-icon class="button-icon" name="close"></svg-icon>
            </button>
          </div>
          <button
            class="button button-ghost button-sm"
            type="button"
            @click="removeInput('DFP_includes', index)"
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
          @click="addInput('DFP_excludes')"
        >
          <span class="hidden-text">추가</span>
          <svg-icon class="button-icon" name="plus"></svg-icon>
        </button>
      </div>
      <div
        class="form-detail"
        v-for="(item, index) in DFP_excludes"
        :key="index"
      >
        <div class="form-delete-group">
          <div class="search-input">
            <input
              :id="'text-input-database-profiler-dfp-excludes-' + index"
              class="text-input text-input-lg"
              v-model="item.name"
            />
            <button
              class="search-input-action-button button button-neutral-ghost button-sm"
              type="button"
              @click="clearInput('DFP_excludes', index)"
            >
              <span class="hidden-text">지우기</span>
              <svg-icon class="button-icon" name="close"></svg-icon>
            </button>
          </div>
          <button
            class="button button-ghost button-sm"
            type="button"
            @click="removeInput('DFP_excludes', index)"
          >
            <span class="hidden-text">삭제</span>
            <svg-icon class="button-icon" name="close"></svg-icon>
          </button>
        </div>
      </div>
    </div>

    <h3 class="form-sub-title">Schema Filter Pattern</h3>
    <div class="form-item form-item-expand">
      <div class="form-label-group">
        <label for="" class="form-label"> Includes </label>
        <button
          class="button button-primary button-sm"
          type="button"
          @click="addInput('SFP_includes')"
        >
          <span class="hidden-text">추가</span>
          <svg-icon class="button-icon" name="plus"></svg-icon>
        </button>
      </div>
      <div
        class="form-detail"
        v-for="(item, index) in SFP_includes"
        :key="index"
      >
        <div class="form-delete-group">
          <div class="search-input">
            <input
              :id="'text-input-database-profiler-sfp-includes-' + index"
              class="text-input text-input-lg"
              v-model="item.name"
            />
            <button
              class="search-input-action-button button button-neutral-ghost button-sm"
              type="button"
              @click="clearInput('SFP_includes', index)"
            >
              <span class="hidden-text">지우기</span>
              <svg-icon class="button-icon" name="close"></svg-icon>
            </button>
          </div>
          <button
            class="button button-ghost button-sm"
            type="button"
            @click="removeInput('SFP_includes', index)"
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
          @click="addInput('SFP_excludes')"
        >
          <span class="hidden-text">추가</span>
          <svg-icon class="button-icon" name="plus"></svg-icon>
        </button>
      </div>
      <div
        class="form-detail"
        v-for="(item, index) in SFP_excludes"
        :key="index"
      >
        <div class="form-delete-group">
          <div class="search-input">
            <input
              :id="'text-input-database-profiler-sfp-excludes-' + index"
              class="text-input text-input-lg"
              v-model="item.name"
            />
            <button
              class="search-input-action-button button button-neutral-ghost button-sm"
              type="button"
              @click="clearInput('SFP_excludes', index)"
            >
              <span class="hidden-text">지우기</span>
              <svg-icon class="button-icon" name="close"></svg-icon>
            </button>
          </div>
          <button
            class="button button-ghost button-sm"
            type="button"
            @click="removeInput('SFP_excludes', index)"
          >
            <span class="hidden-text">삭제</span>
            <svg-icon class="button-icon" name="close"></svg-icon>
          </button>
        </div>
      </div>
    </div>

    <h3 class="form-sub-title">Table Filter Pattern</h3>
    <div class="form-item form-item-expand">
      <div class="form-label-group">
        <label for="" class="form-label"> Includes </label>
        <button
          class="button button-primary button-sm"
          type="button"
          @click="addInput('TFP_includes')"
        >
          <span class="hidden-text">추가</span>
          <svg-icon class="button-icon" name="plus"></svg-icon>
        </button>
      </div>
      <div
        class="form-detail"
        v-for="(item, index) in TFP_includes"
        :key="index"
      >
        <div class="form-delete-group">
          <div class="search-input">
            <input
              :id="'text-input-database-profiler-tfp-includes-' + index"
              class="text-input text-input-lg"
              v-model="item.name"
            />
            <button
              class="search-input-action-button button button-neutral-ghost button-sm"
              type="button"
              @click="clearInput('TFP_includes', index)"
            >
              <span class="hidden-text">지우기</span>
              <svg-icon class="button-icon" name="close"></svg-icon>
            </button>
          </div>
          <button
            class="button button-ghost button-sm"
            type="button"
            @click="removeInput('TFP_includes', index)"
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
          @click="addInput('TFP_excludes')"
        >
          <span class="hidden-text">추가</span>
          <svg-icon class="button-icon" name="plus"></svg-icon>
        </button>
      </div>
      <div
        class="form-detail"
        v-for="(item, index) in TFP_excludes"
        :key="index"
      >
        <div class="form-delete-group">
          <div class="search-input">
            <input
              :id="'text-input-database-profiler-tfp-excludes-' + index"
              class="text-input text-input-lg"
              v-model="item.name"
            />
            <button
              class="search-input-action-button button button-neutral-ghost button-sm"
              type="button"
              @click="clearInput('TFP_excludes', index)"
            >
              <span class="hidden-text">지우기</span>
              <svg-icon class="button-icon" name="close"></svg-icon>
            </button>
          </div>
          <button
            class="button button-ghost button-sm"
            type="button"
            @click="removeInput('TFP_excludes', index)"
          >
            <span class="hidden-text">삭제</span>
            <svg-icon class="button-icon" name="close"></svg-icon>
          </button>
        </div>
      </div>
    </div>

    <div class="form-item">
      <label for="" class="form-label">Include Views</label>
      <div class="form-detail">
        <div class="switch">
          <input
            type="checkbox"
            id="manager-sw-database-profiler-iv"
            class="switch-input"
            :checked="isIncludeViews"
            @click="changeIncludeViews"
          />
          <label for="manager-sw-database-profiler-iv" class="switch-label">
            <div class="switch-control"></div>
          </label>
        </div>
      </div>
    </div>
    <div class="form-item">
      <label for="" class="form-label">Generate Sample Data</label>
      <div class="form-detail">
        <div class="switch">
          <input
            type="checkbox"
            id="manager-sw-database-profiler-gsd"
            class="switch-input"
            :checked="isGenerateSampleData"
            @click="changeGenerateSampleData"
          />
          <label for="manager-sw-database-profiler-gsd" class="switch-label">
            <div class="switch-control"></div>
          </label>
        </div>
      </div>
    </div>
    <div class="form-item">
      <label for="" class="form-label">Compute Metric</label>
      <div class="form-detail">
        <div class="switch">
          <input
            type="checkbox"
            id="manager-sw-database-profiler-cm"
            class="switch-input"
            :checked="isComputeMetric"
            @click="changeComputeMetric"
          />
          <label for="manager-sw-database-profiler-cm" class="switch-label">
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
            for="text-input-example-database-profiler-ps"
            >label</label
          >
          <input
            type="number"
            id="text-input-example-database-profiler-ps"
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
            for="text-input-example-database-profiler-sdrc"
            >label</label
          >
          <input
            id="text-input-example-database-profiler-sdrc"
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
  isIncludeViews,
  isGenerateSampleData,
  isComputeMetric,
  sampleTypeOptions,
  selectedSampleTypeItem,
  profileSample,
  sampleDataRowsCount,
  DFP_includes,
  DFP_excludes,
  SFP_includes,
  SFP_excludes,
  TFP_includes,
  TFP_excludes,
} = storeToRefs(collectionAddStore);
const {
  handleNameInput,
  clearNameInput,
  addInput,
  clearInput,
  removeInput,
  changeEnableDebug,
  changeIncludeViews,
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
