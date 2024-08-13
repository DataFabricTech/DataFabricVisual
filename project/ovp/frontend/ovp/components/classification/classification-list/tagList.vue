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
    <tr v-for="tag in classificationTagList" :key="tag.id">
      <td>{{ tag.displayName }}</td>
      <td>{{ tag.description }}</td>
      <td>
        <div class="button-group">
          <!--          <button-->
          <!--            class="button button button-secondary-stroke"-->
          <!--            @click="showModifyTag = true"-->
          <!--          >-->
          <!--            편집-->
          <!--          </button>-->
          <button
            class="button button button-error-stroke"
            @click="confirmDelete(tag.id)"
          >
            삭제
          </button>
        </div>
      </td>
    </tr>
  </table>
  <tag-create :modal-id="MODAL_ID" @close-modal="closeModal"></tag-create>
  <!--  <tag-modify-->
  <!--    v-if="showModifyTag == true"-->
  <!--    @close-modal="closeModifyTag"-->
  <!--  ></tag-modify>-->
</template>

<script setup lang="ts">
import { classificationStore } from "@/store/classification/index";
const { $vfm } = useNuxtApp();

const useClassificationStore = classificationStore();
const { classificationTagList } = storeToRefs(useClassificationStore);
const { deleteClassificationTag } = useClassificationStore;

// 태그 추가 모달 ID
const MODAL_ID = "modal-classificationTag";

function openModal() {
  $vfm.open(MODAL_ID);
}
function closeModal() {
  $vfm.close(MODAL_ID);
}

watchEffect(() => {
  console.log(classificationTagList);
});

const confirmDelete = (tagId: string) => {
  if (confirm("삭제하시겠습니까?")) {
    deleteClassificationTag(tagId)
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
