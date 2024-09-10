<template>
  <div class="work-page">
    <div class="l-top-bar">
      <div class="v-group gap-[20px]">
        <editable-group
          :parent-edit-mode="isNameEditable"
          compKey="name"
          :editable="true"
          @editCancel="editCancel"
          @editDone="editDone"
          @editIcon="() => editIconClick('name')"
        >
          <template #edit-slot>
            <label class="hidden-text" for="title-modify">분류명 입력</label>
            <input
              v-model="newData.name"
              placeholder="분류명에 대한 영역입니다."
              required
              id="title-modify"
              class="text-input"
            />
          </template>
          <template #view-slot>
            <h3 class="editable-group-title">{{ newData.name }}</h3>
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
  if (classificationDetailData.value.description?.length === 0) {
    classificationDetailData.value.description.concat("-");
  }
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
  if (oldData.name !== newData.name) {
    patch.push({
      op: "replace",
      path: "/name",
      value: newData.name,
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
  if (key === "name") {
    isNameEditable.value = true;
  } else if (key === "description") {
    isDescEditable.value = true;
    // 수정버튼 클릭시, description의 수정상태에 '-'를 제거
    if (newData.value.description === "-") {
      newData.value.description = "";
    }
  }
  defaultData = _.cloneDeep(newData.value);
  // 설명값이 빈값일 경우에는, default값은 '-'로 설정
  if (defaultData.description?.length === 0) {
    defaultData.description = "-";
  }
};

const editCancel = () => {
  classificationDetailData.value = _.cloneDeep(defaultData);
};

const editDone = async () => {
  // 분류이름이 빈값으로 수정할 경우, 기존값으로 세팅
  if (!newData.value.name) {
    classificationDetailData.value = _.cloneDeep(defaultData);
    return;
  }

  // 이름이 있을 경우에만 API 호출 로직 수행
  const patchData = createJsonPatch(defaultData, newData.value);

  // 분류 displayName or description 수정 API 호출
  try {
    const res = await editClassificationDetail(patchData);
    if (res.result === 1) {
      // 서버 수정 성공시,
      // 설명에 값이 없을 경우, '-' 되도록 세팅
      if (res.data.description.length === 0) {
        newData.value.description = "-";
      }
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
