<template>
  <div class="l-top-bar">
    <button class="button button-secondary ml-auto" @click="openModal">
      용어추가
    </button>
  </div>
  <agGrid
      v-if="terms.length > 0"
      :style="'width: 100%; height: 300px'"
      class="ag-theme-alpine ag-theme-quartz"
      domLayout="autoHeight"
      :columnDefs="termColumnDefs"
      :rowData="terms"
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
import agGrid from "@extends/grid/Grid.vue";
import { useGlossaryStore } from "@/store/glossary";
import { useModal } from "vue-final-modal";
import ModalGlossary from "@/components/govern/glossary/modal/modal-glossary.vue";
import EditDeleteButtonRenderer from "@/components/classification/classification-list/editDeleteButtonRenderer.vue";
import { useNuxtApp } from "nuxt/app";

const { $alert, $confirm } = useNuxtApp();

const {
  terms,
  openEditTermComponent,
  deleteTerm,
  getTerms,
  getTerm
} = useGlossaryStore();

onMounted(() => {
  getTerms();
});

const openModalId = "modal-glossary";
const { open, close } = useModal({
  component: ModalGlossary,
  attrs: {
    modalId: openModalId,
    onClose() {
      close();
    }
  }
});
function openModal(): void {
  open();
}

// 수정/삭제를 위한 context
const gridContext = {
  tagModifyOpen: async (term: any) => {
    await getTerm(term.name);
    openEditTermComponent("term");
  },
  confirmDelete: async ({ id }: { id: string }) => {
    if (await $confirm("용어를 삭제하시겠습니까?")) {
      const res = await deleteTerm(id);
      if (res.result === 1) {
        await getTerms();
        $alert("삭제되었습니다.", "success");
      } else {
        $alert(res.errorMessage, "error");
      }
    }
  }
};

// 컬럼 정의
const termColumnDefs = ref([
  {
    headerName: "용어",
    headerClass: "ag-header-center",
    field: "name",
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
      alignItems: "center"
    },
    cellRenderer: EditDeleteButtonRenderer
  }
]);
</script>

<style scoped></style>
