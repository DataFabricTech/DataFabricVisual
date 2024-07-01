<template>
  <div class="resource-box" :class="props.class" @click="previewClick">
    <div class="resource-box-function">
      <div class="resource-box-model">
        <img :src="props.dataObj.serviceIcon" />
        <div class="breadcrumb">
          <ul class="breadcrumb-list">
            <li
              class="breadcrumb-item"
              v-for="(item, index) in props.dataObj.depth"
              :key="index"
            >
              <a class="breadcrumb-link">{{ item }}</a>
            </li>
          </ul>
        </div>
      </div>
    </div>

    <!-- TODO : 퍼블 첫번째 데이터 명 -->
    <div v-if="useFirModelNm">{{ props.dataObj.firModelNm }}</div>

    <template v-if="props.useDataNmLink">
      <a
        href="javascript:void(0);"
        class="resource-box-title"
        title="상세 보기"
        @click="modelNmClick"
        >{{ props.dataObj.modelNm }}</a
      >
    </template>
    <template v-else>
      <div>{{ props.dataObj.modelNm }}</div>
    </template>

    <!-- TODO: 수정 기능 구현 & 퍼블 -->
    <template v-if="isEditMode.modelDesc">
      <input :value="newValue.modelDesc" />
      <button @click="editCancel('modelDesc')">취소</button>
      <button @click="editDone('modelDesc')">확인</button>
    </template>
    <template v-else>
      <span class="resource-box-desc">{{ props.dataObj.modelDesc }}</span>
      <div class="custom-fa" v-if="props.editable">
        <fa @click="editIconClick('modelDesc')" icon="fa-solid fa-pen" />
      </div>
    </template>
    <div class="resource-box-info">
      <dl v-if="props.showOwner" class="resource-box-list">
        <dt>소유자</dt>
        <dd>{{ props.dataObj.owner }}</dd>
      </dl>
      <dl v-if="props.showCategory" class="resource-box-list">
        <dt>도메인</dt>
        <dd>{{ props.dataObj.category }}</dd>
      </dl>
    </div>
  </div>
</template>

<script setup lang="ts">
import { defineEmits } from "vue";
import type { DataModel } from "./resource-box-common-props";
import type { ResourceBoxProps } from "./resource-box-props";

const isEditMode = ref<Record<string, boolean>>({});
const newValue = ref<Record<string, any>>({});

const props = withDefaults(defineProps<ResourceBoxProps>(), {
  useDataNmLink: false,
  editable: false,
});

const emit = defineEmits<{
  (e: "previewClick", id: string | number): void;
  (e: "modelNmClick", id: string | number): void;
}>();

const previewClick = () => {
  if (props.usePrvBtn) {
    emit("previewClick", props.dataObj.id);
  }
};
const modelNmClick = () => {
  emit("modelNmClick", props.dataObj.id);
};

const editIconClick = (key: keyof DataModel) => {
  newValue.value[key] = props.dataObj[key];
  isEditMode.value[key] = true;
};

const editCancel = (key: string) => {
  isEditMode.value[key] = false;
};
const editDone = (key: string) => {
  console.log(newValue.value[key]);
};
</script>

<style lang="scss" scoped>
.custom-fa {
  width: 30px;
  height: 30px;
}
</style>
