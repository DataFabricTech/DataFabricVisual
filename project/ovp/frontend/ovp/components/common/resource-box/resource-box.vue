<template>
  <div
    :class="[
      'resource-box',
      props.class,
      { 'is-resource-box-no-action': !props.usePrvBtn }, // preview 기능을 사용하지 않는 경우, 해당 class 를 추가해야함.
    ]"
    @click="previewClick"
  >
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
              <span class="breadcrumb-text">{{ item }}</span>
            </li>
          </ul>
        </div>
      </div>
    </div>
    <div class="resource-box-initial-name" v-if="useFirModelNm">
      {{ props.dataObj.firModelNm }}
    </div>

    <resource-box-edit-part
      partKey="modelNm"
      :dataObj="props.dataObj"
      :isEditMode="isEditMode.modelNm"
      :editable="props.editable"
      @editCancel="editCancel"
      @editDone="editDone"
      @editIcon="editIconClick"
    >
      <template #edit-slot>
        <input
          required
          value="세종특별자치시 상하수도요금"
          id="title-modify"
          class="text-input w-2/4"
          v-model="newData.modelNm"
          @input="editInput('modelNm', $event)"
        />
      </template>
      <template #view-slot>
        <template v-if="props.useDataNmLink">
          <a
            href="javascript:void(0);"
            class="editable-group-title"
            title="상세 보기"
            @click.stop="modelNmClick"
            >{{ props.dataObj.modelNm }}</a
          >
        </template>
        <template v-else>
          <h3 class="editable-group-title">{{ props.dataObj.modelNm }}</h3>
        </template>
      </template>
    </resource-box-edit-part>

    <resource-box-edit-part
      partKey="modelDesc"
      :dataObj="props.dataObj"
      :isEditMode="isEditMode.modelDesc"
      :editable="props.editable"
      @editCancel="editCancel"
      @editDone="editDone"
      @editIcon="editIconClick"
    >
      <template #edit-slot>
        <textarea
          id="description-modify"
          class="textarea"
          v-model="newData.modelDesc"
          @input="editInput('modelDesc', $event)"
        ></textarea>
      </template>
      <template #view-slot>
        <span class="editable-group-desc">{{ props.dataObj.modelDesc }}</span>
      </template>
    </resource-box-edit-part>

    <div class="resource-box-info">
      <resource-box-edit-part
        partKey="owner"
        :dataObj="props.dataObj"
        :isEditMode="isEditMode.owner"
        :editable="props.editable"
        :useEditButtons="false"
        @editCancel="editCancel"
        @editDone="editDone"
        @editIcon="editIconClick"
      >
        <template #edit-slot>
          <dl v-if="props.showOwner" class="resource-box-list">
            <dt>소유자</dt>
            <!-- TODO : [개발] 수정 component-->
            <dd>수정태그 여기에</dd>
          </dl>
        </template>
        <template #view-slot>
          <dl v-if="props.showOwner" class="resource-box-list">
            <dt>소유자</dt>
            <dd>{{ props.dataObj.owner }}</dd>
          </dl>
        </template>
      </resource-box-edit-part>

      <resource-box-edit-part
        partKey="category"
        :dataObj="props.dataObj"
        :isEditMode="isEditMode.category"
        :editable="props.editable"
        :useEditButtons="false"
        @editCancel="editCancel"
        @editDone="editDone"
        @editIcon="editIconClick"
      >
        <template #edit-slot>
          <!-- TODO : [개발] 수정 component-->
          <dl v-if="props.showCategory" class="resource-box-list">
            <dt>도메인</dt>
            <dd>수정태그 여기에</dd>
          </dl>
        </template>
        <template #view-slot>
          <dl v-if="props.showCategory" class="resource-box-list">
            <dt>도메인</dt>
            <dd>{{ props.dataObj.category }}</dd>
          </dl>
        </template>
      </resource-box-edit-part>
    </div>
  </div>
</template>

<script setup lang="ts">
import { defineEmits } from "vue";
import type { ResourceBoxProps } from "./resource-box-props";
import ResourceBoxEditPart from "./resource-box-edit-part.vue";

const isEditMode = ref<Record<string, boolean>>({});
const newData = ref<Record<string, any>>({});
const newValue = ref<Record<string, any>>({});

const props = withDefaults(defineProps<ResourceBoxProps>(), {
  useDataNmLink: false,
  editable: false,
});

const emit = defineEmits<{
  (e: "previewClick", id: string | number): void;
  (e: "modelNmClick", data: object): void;
}>();

const previewClick = () => {
  if (props.usePrvBtn) {
    emit("previewClick", props.dataObj.id);
  }
};
const modelNmClick = () => {
  emit("modelNmClick", props.dataObj);
};

const editIconClick = (key: string) => {
  newValue.value[key] = props.dataObj[key];
  isEditMode.value[key] = true;
};

const editCancel = (key: string) => {
  console.log("cancel");
  isEditMode.value[key] = false;
};
const editDone = (key: string) => {
  console.log(`${key} : ${newValue.value[key]}`);
};
const editInput = (key: string, event: Event) => {
  const target = event.target as HTMLInputElement;
  console.log(`${key} : ${target.value}`);
};
</script>

<style lang="scss" scoped>
.custom-fa {
  width: 30px;
  height: 30px;
}
</style>
