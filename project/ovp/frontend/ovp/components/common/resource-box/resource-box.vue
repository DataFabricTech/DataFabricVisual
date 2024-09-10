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
        <div class="checkbox" v-if="props.useListCheckbox" @click.stop>
          <input
            type="checkbox"
            :id="`resource_box_list_${dataObj.id}`"
            :checked="isChecked"
            class="checkbox-input"
            @change="checked($event, dataObj.id)"
          />
          <label
            :for="`resource_box_list_${dataObj.id}`"
            class="checkbox-label"
          >
          </label>
        </div>

        <div class="type-img" :class="props.dataObj.serviceIcon" />
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
    <div
      class="editable-group"
      v-show="!user.admin && newData.owner?.id !== user.id"
    >
      <a
        href="javascript:void(0);"
        class="editable-group-title"
        :title="newData.displayName ?? newData.modelNm"
        @click.stop="modelNmClick"
        >{{ newData.displayName ?? newData.modelNm }}</a
      >
    </div>
    <editable-group
      compKey="modelNm"
      :editable="props.editable"
      @editCancel="editCancel"
      @editDone="editDoneForModel"
      v-show="newData.owner?.id === user.id || user.admin"
    >
      <template #edit-slot>
        <label class="hidden-text" for="title-modify">모델 명</label>
        <input
          v-model="tempData.modelNm"
          placeholder="모델명 대한 영역입니다."
          required
          id="title-modify"
          class="text-input w-2/4"
        />
      </template>
      <template #view-slot>
        <template v-if="props.useDataNmLink">
          <a
            href="javascript:void(0);"
            class="editable-group-title"
            :title="newData.displayName ?? newData.modelNm"
            @click.stop="modelNmClick"
            >{{ newData.displayName ?? newData.modelNm }}</a
          >
        </template>
        <template v-else>
          <h3
            class="editable-group-title"
            :title="newData.displayName ?? newData.modelNm"
          >
            {{ newData.displayName ?? newData.modelNm }}
          </h3>
        </template>
      </template>
    </editable-group>
    <div
      class="editable-group"
      v-show="!user.admin && newData.owner?.id !== user.id"
    >
      <span class="editable-group-desc" :title="props.dataObj.modelDesc">
        {{
          props.dataObj.modelDesc === "" ||
          props.dataObj.modelDesc === null ||
          props.dataObj.modelDesc === undefined
            ? "설명 없음"
            : props.dataObj.modelDesc
        }}</span
      >
    </div>
    <editable-group
      compKey="modelDesc"
      :editable="props.editable"
      @editCancel="editCancel"
      @editDone="editDoneForModel"
      v-show="newData.owner?.id === user.id || user.admin"
    >
      <template #edit-slot>
        <label class="hidden-text" for="textarea-modify">모델 설명</label>
        <textarea
          class="textarea"
          v-model="tempData.modelDesc"
          placeholder="모델 설명에 대한 영역입니다."
          required
          id="textarea-modify"
        ></textarea>
      </template>
      <template #view-slot>
        <span class="editable-group-desc" :title="props.dataObj.modelDesc">
          {{
            props.dataObj.modelDesc === "" ||
            props.dataObj.modelDesc === null ||
            props.dataObj.modelDesc === undefined
              ? "설명 없음"
              : props.dataObj.modelDesc
          }}</span
        >
      </template>
    </editable-group>
    <div class="resource-box-info">
      <div
        class="editable-group"
        v-show="!user.admin && newData.owner?.id !== user.id"
      >
        <dl class="resource-box-list">
          <dt>소유자</dt>
          <dd>
            {{ newData.owner ? newData.ownerDisplayName : "소유자 없음" }}
          </dd>
        </dl>
      </div>
      <editable-group
        :compKey="ownerKey"
        :parent-edit-mode="isEditMode[ownerKey]"
        :editable="props.editable"
        :useEditButtons="false"
        @editCancel="editCancel"
        @editIcon="editIconClick(ownerKey, true)"
        v-show="newData.owner?.id === user.id || user.admin"
      >
        <template #edit-slot>
          <dl v-if="props.showOwner" class="resource-box-list">
            <dt>소유자</dt>
            <dd>
              <menu-search-button
                v-on-click-outside="
                  () => {
                    updateIsEditMode('owner', false);
                  }
                "
                :data="userList"
                :selected-items="{ id: newData[ownerKey] }"
                label-key="displayName"
                value-key="id"
                title="소유자"
                @single-change="editDoneForOwner($event, ownerKey)"
                @close="updateIsEditMode(ownerKey, false)"
              >
                <template #button-text-slot>
                  <span class="select-button-title">{{
                    newData.owner ? newData.ownerDisplayName : "소유자 없음"
                  }}</span>
                </template>
              </menu-search-button>
            </dd>
          </dl>
        </template>
        <template #view-slot>
          <dl v-if="props.showOwner" class="resource-box-list">
            <dt>소유자</dt>
            <dd>
              {{ newData.owner ? newData.ownerDisplayName : "소유자 없음" }}
            </dd>
          </dl>
        </template>
      </editable-group>
      <div
        class="editable-group"
        v-show="!user.admin && newData.owner?.id !== user.id"
      >
        <dl class="resource-box-list">
          <dt>카테고리</dt>
          <dd>
            {{
              newData[categoryKey].name
                ? newData[categoryKey].name
                : "카테고리 없음"
            }}
          </dd>
        </dl>
      </div>
      <editable-group
        :compKey="categoryKey"
        :parent-edit-mode="isEditMode[categoryKey]"
        :editable="props.editable"
        :useEditButtons="false"
        @editCancel="editCancel"
        @editDone="editDone"
        @editIcon="editIconClick(categoryKey, true)"
        v-show="newData.owner?.id === user.id || user.admin"
      >
        <template #edit-slot>
          <dl v-if="props.showCategory" class="resource-box-list">
            <dt>카테고리</dt>
            <dd>
              <menu-search-tree
                label-key="name"
                value-key="id"
                :data="categoryList"
                :title="
                  newData[categoryKey].name
                    ? newData[categoryKey].name
                    : '카테고리 없음'
                "
                :is-multi="false"
                :is-show-length="false"
                :hideGuideLines="false"
                :firExpandAll="true"
                :show-open-all-btn="false"
                :show-close-all-btn="false"
                :use-draggable="false"
                :selected-items="newData.category"
                mode="view"
                @single-change="editDoneForCategory"
              />
            </dd>
          </dl>
        </template>
        <template #view-slot>
          <dl v-if="props.showCategory" class="resource-box-list">
            <dt>카테고리</dt>
            <dd>
              {{
                newData[categoryKey].name
                  ? newData[categoryKey].name
                  : "카테고리 없음"
              }}
            </dd>
          </dl>
        </template>
      </editable-group>
    </div>
  </div>
</template>

<script setup lang="ts">
import { vOnClickOutside } from "@vueuse/components";
import { defineEmits, ref, defineProps, type ComputedRef } from "vue";

import EditableGroup from "@extends/editable-group/EditableGroup.vue";
import MenuSearchButton from "@extends/menu-seach/button/menu-search-button.vue";

import type { DataModel } from "./resource-box-common-props";
import type { ResourceBoxProps } from "./resource-box-props";

import _ from "lodash";
import MenuSearchTree from "@extends/menu-seach/tree/menu-search-tree.vue";

const props = withDefaults(defineProps<ResourceBoxProps>(), {
  useDataNmLink: false,
  editable: false,
  filters: () => {
    return {};
  },
  userList: () => {
    return [];
  },
  categoryList: () => {
    return [];
  },
  ownerKey: "id",
  categoryKey: "category",
  useListCheckbox: false,
  user: () => {
    return {};
  },
});

const isChecked = computed(() => {
  return props.selectedModelList.includes(props.dataObj.id);
});

const isEditMode = ref<Record<string, boolean>>({
  title: false,
  desc: false,
  [props.ownerKey]: false,
  [props.categoryKey]: false,
});

// props.dataObj 값을 수정할 수 없기 때문에, 아래 두개의 값을 따로 두어 사용함.
// 이 컴포넌트 내에서 쓰는 임시 저장용. (v-model 연결)
const tempData: ComputedRef<DataModel> = computed(() => {
  return _.cloneDeep(props.dataObj);
});
// 부모 단으로 보낼 데이터 (데이터 수정 하고 '완료' 버튼 눌렀을때 v-model 데이터를 반영하는 용
// 혹은 반영 해 두었다가 '취소' 버튼 눌렀을때 마지막 저장된 데이터로 원복 시키기 위해 사용.
const newData: ComputedRef<DataModel> = computed(() => {
  return _.cloneDeep(props.dataObj);
});

const emit = defineEmits<{
  (e: "previewClick", data: object): void;
  (e: "modelNmClick", data: object): void;
  (e: "checked", data: any): void;
  (e: "editIconClick", id: string): void;
  (e: "editDoneForModel", data: object): void;
  (e: "editDone", data: object): void;
}>();

const previewClick = () => {
  if (props.usePrvBtn) {
    emit("previewClick", props.dataObj);
  }
};
const modelNmClick = () => {
  emit("modelNmClick", props.dataObj);
};

const editCancel = (key: string) => {
  tempData.value[key] = newData.value[key];
};

const editDoneForModel = (key: string) => {
  const data = {
    key: key,
    value: tempData.value[key],
    beforeValue: newData.value[key],
  };

  newData.value[key] = tempData.value[key];

  emit("editDone", data);
};

const editDoneForOwner = (value: any, key: string) => {
  const data = {
    key: "owner",
    op: "",
    id: value ? value.id : null,
  };
  const owner = tempData.value.owner;

  if (owner && value && owner.id === value.id) {
    return;
  }

  if (owner == null) {
    data.op = "add";
  } else if (owner && !value) {
    data.op = "remove";
  } else if (owner && value && value.id && owner.id !== value.id) {
    data.op = "replace";
  }

  emit("editDone", data);

  updateIsEditMode(key, false);
};

const editDoneForCategory = (value: any) => {
  const data: any = {
    key: "category",
    value: value,
  };

  emit("editDone", data);
};

const editDone = (key: string) => {
  const data = {
    key: key,
    value: tempData.value[key],
    beforeValue: newData.value[key],
  };

  newData.value[key] = tempData.value[key];

  emit("editDone", data);
};

const editIconClick = (key: string, value: boolean) => {
  updateIsEditMode(key, value);
  emit("editIconClick", key);
};
const updateIsEditMode = (key: string, value: boolean) => {
  isEditMode.value[key] = value;
};

const checked = ($evt: Event, id: string | number) => {
  const target = $evt.target as HTMLInputElement;
  emit("checked", { id: id, checked: target.checked });
};
</script>

<style lang="scss" scoped>
.custom-fa {
  width: 30px;
  height: 30px;
}
</style>
