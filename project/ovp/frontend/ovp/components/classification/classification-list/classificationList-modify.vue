<template>
  <div class="work-page">
    <div class="l-top-bar">
      <div class="v-group gap-[20px]">
        <editable-group
          :parent-edit-mode="isNameEditable"
          compKey="displayName"
          :editable="true"
          @editCancel="editCancel"
          @editDone="editDone"
          @editIcon="() => editIconClick('displayName')"
        >
          <template #edit-slot>
            <label class="hidden-text" for="title-modify">분류명 입력</label>
            <input
              v-model="newData.displayName"
              placeholder="분류명에 대한 영역입니다."
              required
              id="title-modify"
              class="text-input"
            />
          </template>
          <template #view-slot>
            <h3 class="editable-group-title">{{ newData.displayName }}</h3>
          </template>
        </editable-group>
      </div>
      <button class="button button-error-lighter" @click="confirmDelete">
        삭제
      </button>
    </div>
    <div class="work-contents">
      <div class="no-result" style="display: none">
        <div class="notification">
          <svg-icon class="notification-icon" name="info"></svg-icon>
          <p class="notification-detail">등록된 정보가 없습니다.</p>
        </div>
      </div>
      <editable-group
        :parent-edit-mode="isDescEditable"
        compKey="description"
        :editable="true"
        @editCancel="editCancel"
        @editDone="editDone"
        @editIcon="() => editIconClick('description')"
      >
        <template #edit-slot>
          <label class="hidden-text" for="textarea-modify">textarea 입력</label>
          <textarea
            class="textarea"
            v-model="newData.description"
            placeholder="해당 분류 설명에 대한 영역입니다."
            required
            id="textarea-modify"
          ></textarea>
        </template>
        <template #view-slot>
          <p class="editable-group-desc">{{ newData.description }}</p>
        </template>
      </editable-group>
      <div>
        <tag-list></tag-list>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from "vue";
import _ from "lodash";
import { classificationStore } from "@/store/classification/index";
import tagList from "@/components/classification/classification-list/tagList.vue";
import EditableGroup from "@extends/editable-group/EditableGroup.vue";

const useClassificationStore = classificationStore();
const { editClassificationDetail, deleteClassification } =
  useClassificationStore;
const { classificationDetailData, isNameEditable, isDescEditable } =
  storeToRefs(useClassificationStore);

interface ClassificationDetail {
  id: string;
  name: string | null;
  displayName: string | null;
  description: string | null;
}

let defaultData: ClassificationDetail = {
  id: "",
  name: "",
  displayName: "",
  description: "",
};

const newData = computed(() => {
  return _.cloneDeep(classificationDetailData.value);
});

interface JsonPatchOperation {
  op: string;
  path: string;
  value: any;
}

// 수정될 내용 형식 만드는 함수
const createJsonPatch = (oldData: any, newData: any): JsonPatchOperation[] => {
  const patch: JsonPatchOperation[] = [];
  if (oldData.displayName !== newData.displayName) {
    patch.push({
      op: "replace",
      path: "/displayName",
      value: newData.displayName,
    });
  }
  if (oldData.description !== newData.description) {
    patch.push({
      op: "replace",
      path: "/description",
      value: newData.description,
    });
  }
  return patch;
};

const editIconClick = (key: string) => {
  if (key === "displayName") {
    isNameEditable.value = true;
  } else if (key === "description") {
    isDescEditable.value = true;
  }
  defaultData = _.cloneDeep(newData.value);
};

const editCancel = () => {
  classificationDetailData.value = _.cloneDeep(defaultData);
};

const editDone = async () => {
  // API호출시 보낼 수정 내용
  const patchData = createJsonPatch(defaultData, newData.value);

  // 분류 displayName or description 수정 API 호출
  try {
    const res = await editClassificationDetail(patchData);
    if (res.result === 1) {
      // 서버 수정 성공시,
      classificationDetailData.value = _.cloneDeep(newData.value);
      return;
    } else {
      // 서버 수정 실패시, 기존 값으로 복구
      classificationDetailData.value = defaultData;
    }
  } catch (error) {
    console.error("API 호출 중 오류 발생: ", error);
  }
};

// 분류 삭제 버튼 클릭
const confirmDelete = () => {
  if (confirm("삭제하시겠습니까?")) {
    deleteClassification()
      .then(() => {
        alert("삭제되었습니다.");
      })
      .catch((error) => {
        console.error("삭제 중 오류 발생: ", error);
      });
  }
};
</script>

<style lang="scss" scoped></style>
