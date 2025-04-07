<template>
  <div class="l-top-bar">
    <button class="button button-secondary ml-auto" @click="openTagCreateModal">
      태그추가
    </button>
  </div>
  <agGrid
    v-if="classificationTagList.length > 0"
    :style="'width: 100%; height: 300px'"
    class="ag-theme-alpine ag-theme-quartz"
    domLayout="autoHeight"
    :columnDefs="historyColumnDefs"
    :rowData="classificationTagList"
    :context="gridContext"
    :setColumnFit="true"
    :useColumnResize="true"
  />
  <div v-else>
    <div class="no-result">
      <div class="notification">
        <svg-icon class="notification-icon" name="info" />
        <p class="notification-detail">등록된 정보가 없습니다.</p>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { classificationStore } from "@/store/classification/index";
import { useModal } from "vue-final-modal";
import tagCreate from "@/components/classification/modal/tag-create.vue";
import tagModify from "@/components/classification/modal/tag-modify.vue";
import agGrid from "@extends/grid/Grid.vue";

const useClassificationStore = classificationStore();
const { classificationTagList } = storeToRefs(useClassificationStore);
const { deleteClassificationTag, getClassificationTags } =
  useClassificationStore;
import { useNuxtApp } from "nuxt/app";
import EditDeleteButtonRenderer from "@/components/classification/classification-list/editDeleteButtonRenderer.vue"
const { $alert, $confirm } = useNuxtApp();

// 태그 추가 모달 ID
const MODAL_ID = "modal-classificationTag";
// 태그 수정 모달 ID
const MODAL_MODIFY_ID = "modal-classificationTag-modify";

// 수정할 태그의 정보
const formInfo = ref({ name: "", description: "", id: "" });

const { open: openTagCreateModal, close: closeTagCreateModal } = useModal({
  component: tagCreate,
  attrs: {
    modalId: MODAL_ID,
    onClose() {
      closeTagCreateModal();
    },
  },
});

const { open: tagModifyOpen, close: closeTagEditModal } = useModal({
  component: tagModify,
  attrs: {
    modalId: MODAL_MODIFY_ID,
    formInfo: formInfo,
    onClose() {
      closeTagEditModal();
    },
  },
});

const gridContext = {
  tagModifyOpen: (tag: object) => {
    formInfo.value.id = tag.id;
    formInfo.value.name = tag.name;
    formInfo.value.description = tag.description;
    tagModifyOpen(); // 모달 열기
  },
  confirmDelete: async ({ id }: { id: string }) => {
    if (await $confirm("삭제하시겠습니까?")) {
      deleteClassificationTag(id)
          .then(() => {
            $alert("삭제되었습니다.", "success").then(() => {
              getClassificationTags(); // 태그 정보 API 호출
            });
          })
          .catch((err: any) => {
            console.log("err: ", err);
          });
    }
  }
};

const historyColumnDefs = ref([
  {
    headerName: "태그",
    headerClass: "ag-header-center",
    field: "tag",
    cellStyle: {textAlign: "center"},
    valueGetter: ({data}: { data: { displayName: string; name: string }; }) => {
      return data.displayName || data.name;
    },
  },
  {
    headerName: "설명",
    headerClass: "ag-header-center",
    field: "description",
    cellStyle: { textAlign: "center" },
    valueGetter: ({ data }: { data: { description: string } }) => data.description
  },
  {
    headerName: "관리",
    field: "",
    cellStyle: {
      display: "flex",
      justifyContent: "center",
      alignItems: "center",
    },
    cellRenderer: EditDeleteButtonRenderer,
  }
])

</script>

<style lang="scss" scoped></style>
