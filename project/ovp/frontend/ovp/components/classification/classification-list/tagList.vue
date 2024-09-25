<template>
  <div class="l-top-bar">
    <button class="button button-secondary ml-auto" @click="openModal">
      태그추가
    </button>
  </div>
  <table class="align-center">
    <colgroup>
      <col style="width: 25%" />
      <col style="width: 25%" />
      <col style="width: 25%" />
    </colgroup>
    <tr>
      <th>태그</th>
      <th>설명</th>
      <th>관리</th>
    </tr>
    <tr
      v-if="classificationTagList.length !== 0"
      v-for="tag in classificationTagList"
      :key="tag.id"
    >
      <td>{{ tag.name }}</td>
      <td>{{ tag.description }}</td>
      <td>
        <div class="button-group">
          <button
            class="button button button-secondary-stroke"
            @click="openModifyModal(tag.id, tag.name)"
          >
            편집
          </button>
          <button
            class="button button button-error-stroke"
            @click="confirmDelete(tag.id)"
          >
            삭제
          </button>
        </div>
      </td>
    </tr>
    <tr v-else>
      <td colspan="3">
        <div class="no-result">
          <div class="notification">
            <svg-icon class="notification-icon" name="info"></svg-icon>
            <p class="notification-detail">등록된 정보가 없습니다.</p>
          </div>
        </div>
      </td>
    </tr>
  </table>
  <tag-create :modal-id="MODAL_ID" @close-modal="closeModal"></tag-create>
  <tag-modify
    :modal-id="MODAL_MODIFY_ID"
    @close-modal="closeModifyModal"
    :formInfo="formInfo"
  ></tag-modify>
</template>

<script setup lang="ts">
import { classificationStore } from "@/store/classification/index";
import { useModal } from "vue-final-modal";
import tagCreate from "@/components/classification/modal/tag-create.vue";

const useClassificationStore = classificationStore();
const { classificationTagList } = storeToRefs(useClassificationStore);
const {
  deleteClassificationTag,
  getClassificationTags,
  editFormInfo,
  setTagId,
  setTagName,
} = useClassificationStore;

// 태그 추가 모달 ID
const MODAL_ID = "modal-classificationTag";
// 태그 수정 모달 ID
const MODAL_MODIFY_ID = "modal-classificationTag-modify";

const { open, close } = useModal({
  component: tagCreate,
  attrs: {
    modalId: MODAL_ID,
    onClose() {
      close();
    },
  },
});

function openModal() {
  open();
}

// 수정할 태그의 정보
let formInfo = ref({ name: "", description: "" });

async function openModifyModal(tagId: string, tagName: string) {
  // 선택한 태그의 id, name값을 스토어에 저장
  setTagId(tagId);
  setTagName(tagName);

  // 태그정보 가져오기
  formInfo.value = editFormInfo();
  $vfm.open(MODAL_MODIFY_ID);
}
function closeModifyModal() {
  $vfm.close(MODAL_MODIFY_ID);
}

const confirmDelete = (tagId: string) => {
  if (confirm("삭제하시겠습니까?")) {
    deleteClassificationTag(tagId)
      .then(() => {
        alert("삭제되었습니다.");
        getClassificationTags(); // 태그 정보 API 호출
      })
      .catch((error) => {
        console.error("삭제 중 오류 발생: ", error);
      });
  }
};
</script>

<style lang="scss" scoped></style>
