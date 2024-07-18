<template>
  <div class="sample">
    <h1>메뉴 선택 - Editable Group 컴포넌트 예시</h1>
    <div class="v-group gap-[20px]">
      <!--      예시-1. text input-->
      <editable-group
        :partKey="'title'"
        :isEditMode="isEditMode.title"
        :editable="true"
        @editCancel="editCancel"
        @editDone="editDone"
        @editIcon="editIconClick"
      >
        <template #edit-slot>
          <label class="hidden-text" for="title-modify">text input 입력</label>
          <input
            v-model="newData.title"
            @input="editInput('title', $event)"
            placeholder="모델 설명에 대한 영역입니다."
            required
            id="title-modify"
            class="text-input"
          />
        </template>
        <template #view-slot>
          <h3 class="editable-group-title">{{ newData.title }}</h3>
        </template>
      </editable-group>

      <!--      예시-2. textarea -->
      <editable-group
        :partKey="'description'"
        :isEditMode="isEditMode.description"
        :editable="true"
        @editCancel="editCancel"
        @editDone="editDone"
        @editIcon="editIconClick"
      >
        <template #edit-slot>
          <label class="hidden-text" for="textarea-modify">textarea 입력</label>
          <textarea
            class="textarea"
            v-model="newData.description"
            @input="editInput('description', $event)"
            placeholder="모델 설명에 대한 영역입니다."
            required
            id="textarea-modify"
          ></textarea>
        </template>
        <template #view-slot>
          <p class="editable-group-desc">{{ newData.description }}</p>
        </template>
      </editable-group>

      <!--      예시-3. checkbox -->
      <editable-group
        :partKey="'checkKey'"
        :isEditMode="isEditMode.checkKey"
        :editable="true"
        :useEditButtons="false"
        @editCancel="editCancel"
        @editDone="editDone"
        @editIcon="editIconClick"
      >
        <template #edit-slot>
          <div class="h-group">
            <div class="switch">
              <input type="checkbox" id="manager-sw" class="switch-input" v-model="newData.check" />
              <label for="manager-sw" class="switch-label">
                관리자
                <div class="switch-control"></div>
              </label>
            </div>
            <button class="button button-secondary" type="button" id="button1" @click="editDone('checkKey')">
              완료
            </button>
          </div>
        </template>
        <template #view-slot>
          <span>사용자</span>
          <span>{{ newData.check }}</span>
        </template>
      </editable-group>

      <!--      예시-4. 다중선택 -->
      <editable-group
        :partKey="'tagsKey'"
        :isEditMode="isEditMode.tagsKey"
        :editable="true"
        :useEditButtons="false"
        @editCancel="editCancel"
        @editDone="editDone"
        @editIcon="editIconClick"
      >
        <template #edit-slot>
          <div class="select select-clean">
            <button class="select-button" @click.stop="editCancel('tagsKey')">
              <div class="tag tag-primary tag-sm" v-for="tag in newData.tags">
                <span class="tag-text">{{ tag.label }}</span>
                <button class="tag-delete-button" @click.stop="deleteTag(tag)">
                  <span class="hidden-text">삭제</span>
                  <svg-icon class="svg-icon" name="close"></svg-icon>
                </button>
              </div>
              <svg-icon class="svg-icon select-indicator" name="chevron-down-medium"></svg-icon>
            </button>
            <menu-search
              :data="tags"
              :label-key="'label'"
              :value-key="'value'"
              :selected-items="newData.tags"
              :is-multi="true"
              @multiple-change="handleMultipleChange"
              @cancel="editCancel('tagsKey')"
            ></menu-search>
          </div>
        </template>
        <template #view-slot>
          <div class="tag tag-primary tag-sm" v-for="tag in newData.tags">
            <span class="tag-text">{{ tag.label }}</span>
          </div>
        </template>
      </editable-group>

      <!--      예시-5. 단일선택 -->
      <editable-group
        :partKey="'singleKey'"
        :isEditMode="isEditMode.singleKey"
        :editable="true"
        :useEditButtons="false"
        @editCancel="editCancel"
        @editDone="editDone"
        @editIcon="editIconClick"
      >
        <template #edit-slot>
          <div class="select select-clean">
            <button class="select-button" @click.stop="editCancel('singleKey')">
              <span>{{ newData.owners.name }}</span>
              <svg-icon class="svg-icon select-indicator" name="chevron-down-medium"></svg-icon>
            </button>
            <menu-search
              :data="owners"
              :selected-items="newData.owners"
              label-key="name"
              value-key="id"
              :title="newData.owners.name"
              @single-change="changeSingle"
              @cancel="editCancel('singleKey')"
            ></menu-search>
          </div>
        </template>
        <template #view-slot>
          <span>{{ newData.owners.name }}</span>
        </template>
      </editable-group>
    </div>
  </div>
</template>

<script setup lang="ts">
import EditableGroup from "@/components/extends/editable-group/EditableGroup.vue";
import menuSearch from "@/components/extends/menu-seach/menu-search.vue";
import type { MenuSearchItemImpl } from "@/components/extends/menu-seach/MenuSearchComposition";
import { ref } from "vue";
import _ from "lodash";

// 받아올 API 데이터. 완료를 누르자마자 새로 받아와야 함.
const defaultData = ref<Record<string, any>>({
  title: "Editable Group Title",
  description:
    "Lorem ipsum dolor sit amet, consectetur adipisicing elit. Amet, aspernatur ducimus facere laboriosam nemo nobis nostrum sapiente sunt. Omnis, quidem.",
  tags: [
    { label: "tag 03", value: "tag 03" },
    { label: "tag 04", value: "tag 04" }
  ],
  owners: {
    id: "2",
    name: "BBB"
  },
  check: false
});

const owners = [
  {
    id: "1",
    name: "AAA"
  },
  {
    id: "2",
    name: "BBB"
  },
  {
    id: "3",
    name: "CCC"
  },
  {
    id: "4",
    name: "DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD"
  }
];

const tags = [
  { label: "tag 01", value: "tag 01" },
  { label: "tag 02", value: "tag 02" },
  { label: "tag 03", value: "tag 03" },
  { label: "tag 04", value: "tag 04" },
  { label: "tag 05", value: "tag 05" }
];

// partKey 별로 편집 모드 상태 체크
const isEditMode = ref<Record<string, boolean>>({});
// 변경/수정할 데이터
const newData = ref<Record<string, any>>(_.cloneDeep(defaultData.value));

// textarea, text input
const editInput = (key: string, event: Event) => {
  //   필요시 사용
  const target = event.target as HTMLInputElement | HTMLTextAreaElement;
  console.log(`${key} : ${target.value}`);
};

// 다중선택
const deleteTag = (tag: object) => {
  const index = newData.value.tags.indexOf(tag);
  if (index !== -1) {
    newData.value.tags.splice(index, 1);
  }
};

const changeTags = (val: MenuSearchItemImpl[]) => {
  newData.value.tags = [...val];
};

const handleMultipleChange = (val: MenuSearchItemImpl[]) => {
  changeTags(val);
  editDone("tagsKey");
};

// 단일선택
const changeSingle: (value: any[] | {}) => void = (value) => {
  newData.value.owners = value;
  editDone("singleKey");
};

// EditorGroup
const editIconClick = (key: string) => {
  isEditMode.value[key] = true;
};

const editCancel = (key: string) => {
  isEditMode.value[key] = false;

  if (key === "title" || key === "description") {
    newData.value[key] = defaultData.value[key];
  }

  console.log("defaultData", defaultData.value);
  console.log("newData: ", newData.value);
};

const editDone = (key: string) => {
  isEditMode.value[key] = false;

  defaultData.value = _.cloneDeep(newData.value);

  // TODO: [개발] 적용/완료 버튼 클릭 시 API 전달 필요
  console.log("defaultData", defaultData.value);
  console.log("newData: ", newData.value);
};
</script>

<style scoped></style>
