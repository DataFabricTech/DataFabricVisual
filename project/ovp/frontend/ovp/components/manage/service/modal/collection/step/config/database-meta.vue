<template>
  <div class="form-body">
    <div class="form-item">
      <label for="" class="form-label">
        Name <span class="required">*</span></label
      >
      <div class="form-detail flex flex-col">
        <div class="search-input">
          <label class="hidden-text" for="text-input-example-4">label</label>
          <input
            id="text-input-example-database-meta"
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
            id="manager-sw-database-meta-eld"
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
              :id="'text-input-database-meta-dfp-includes-' + index"
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
              :id="'text-input-database-meta-dfp-excludes-' + index"
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
              :id="'text-input-database-meta-sfp-includes-' + index"
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
              :id="'text-input-database-meta-sfp-excludes-' + index"
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
              :id="'text-input-database-meta-tfp-includes-' + index"
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
              :id="'text-input-database-meta-tfp-excludes-' + index"
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
      <label for="" class="form-label">Mark Deleted Tables</label>
      <div class="form-detail">
        <div class="switch">
          <input
            type="checkbox"
            id="manager-sw-database-meta-mdt"
            class="switch-input"
            :checked="isMarkDeletedTables"
            @click="changeIsMarkDeletedTables"
          />
          <label for="manager-sw-database-meta-mdt" class="switch-label">
            <div class="switch-control"></div>
          </label>
        </div>
      </div>
    </div>
    <div class="form-item">
      <label for="" class="form-label">Include Tables</label>
      <div class="form-detail">
        <div class="switch">
          <input
            type="checkbox"
            id="manager-sw-database-meta-it"
            class="switch-input"
            :checked="isIncludeTables"
            @click="changeIsIncludeTables"
          />
          <label for="manager-sw-database-meta-it" class="switch-label">
            <div class="switch-control"></div>
          </label>
        </div>
      </div>
    </div>
    <div class="form-item">
      <label for="" class="form-label">Include Views</label>
      <div class="form-detail">
        <div class="switch">
          <input
            type="checkbox"
            id="manager-sw-database-meta-iv"
            class="switch-input"
            :checked="isIncludeViews"
            @click="changeIncludeViews"
          />
          <label for="manager-sw-database-meta-iv" class="switch-label">
            <div class="switch-control"></div>
          </label>
        </div>
      </div>
    </div>
    <div class="form-item">
      <label for="" class="form-label">Include Owners</label>
      <div class="form-detail">
        <div class="switch">
          <input
            type="checkbox"
            id="manager-sw-database-meta-io"
            class="switch-input"
            :checked="isIncludeOwners"
            @click="changeIsIncludeOwners"
          />
          <label for="manager-sw-database-meta-io" class="switch-label">
            <div class="switch-control"></div>
          </label>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
import { useServiceCollectionAddStore } from "@/store/manage/service/collection-add/index";
const collectionAddStore = useServiceCollectionAddStore();
const {
  ingestionPipeLine,
  isEnableDebug,
  isMarkDeletedTables,
  isIncludeTables,
  isIncludeViews,
  isIncludeOwners,
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
  changeIsMarkDeletedTables,
  changeIsIncludeTables,
  changeIncludeViews,
  changeIsIncludeOwners,
} = collectionAddStore;
</script>
<style lang="scss" scoped>
/* @import "./index.scss"; */
</style>
