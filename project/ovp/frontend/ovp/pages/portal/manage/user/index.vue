<template>
  <div class="section-top-bar">
    <div class="l-top-bar">
      <h3 class="title">사용자 관리</h3>
    </div>
  </div>
  <div class="section-contents bg-white">
    <div class="l-between mb-3">
      <SearchInput
        class="w-[256px]"
        inp-id="userSearchInp"
        label-text="사용자 정보 검색"
        :placeholder="'검색어를 입력하세요.'"
        :inp-value="searchInputValue"
        @update:value="updateSearchInputValue"
        @onClickSearch="onClickSearch"
      ></SearchInput>
      <button class="button button-secondary ml-auto" @click="openModal">
        사용자 추가
      </button>
    </div>

    <div class="data-page" style="position: relative">
      <div id="dataList" class="data-list">
        <!-- NOTE: domLayout 값을 autoHeight 으로 설정하여 scroll 제거 해줘야 인피니티 스크롤 기능이 정상적으로 작동 -->
        <agGrid
          class="ag-theme-alpine ag-theme-quartz"
          :style="'width: 100%'"
          domLayout="autoHeight"
          :columnDefs="columnDefs"
          :columnWidthList="[100, 100, 500, 100]"
          rowId="id"
          :rowData="userList"
          :setColumnFit="true"
          :useColumnResize="true"
          :context="gridContext"
          @cellClicked="cellClicked"
        >
        </agGrid>
        <div ref="scrollTrigger" class="w-full h-[1px] mt-px"></div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from "#vue-router";
import { useUserStore } from "@/store/user/userStore";
import { useIntersectionObserver } from "~/composables/intersectionObserverHelper";
import { useModal } from "vue-final-modal";
import SearchInput from "@extends/search-input/SearchInput.vue";
import agGrid from "@extends/grid/Grid.vue";
import AddUserModal from "@/components/manage/user/modal/add.vue";
import DeleteButtonRenderer from "@/components/manage/user/cell-renderer/delete-button.vue";

import { useNuxtApp } from "nuxt/app";
const { $alert, $confirm } = useNuxtApp();

const router = useRouter();
const userStore = useUserStore();
const { setSearchKeyword, getUserList, addUserList, deleteUser } = userStore;
const { userList } = storeToRefs(userStore);

const { open, close } = useModal({
  component: AddUserModal,
  attrs: {
    onClose() {
      close();
    },
    onUserAddedSuccess() {
      getUserList();
    },
  },
});
const columnDefs = ref([
  {
    headerName: "사용자 이름",
    field: "displayName",
    cellStyle: {
      textAlign: "center",
      cursor: "pointer",
      textDecoration: "underline",
    },
    valueGetter: ({
      data,
    }: {
      data: { displayName: string; name: string };
    }) => {
      return data.displayName || data.name;
    },
  },
  {
    headerName: "역할",
    field: "isAdmin",
    cellStyle: {
      textAlign: "center",
    },
    valueGetter: ({ data }: { data: { isAdmin: boolean } }) => {
      return data.isAdmin ? "관리자" : "사용자";
    },
  },
  {
    headerName: "설명",
    field: "description",
  },
  {
    headerName: "관리",
    field: "",
    cellStyle: {
      display: "flex",
      justifyContent: "center",
      alignItems: "center",
    },
    cellRenderer: DeleteButtonRenderer,
  },
]);

const gridContext = {
  deleteBtnClicked: async ({ id }: { id: string }) => {
    if (await $confirm("사용자를 삭제하시겠습니까?")) {
      deleteUser(id)
        .then(() => {
          $alert("삭제되었습니다.", "success").then(() => {
            getUserList();
          });
        })
        .catch((err: any) => {
          console.log("err: ", err);
        });
    }
  },
};

const searchInputValue = ref("");

const updateSearchInputValue = (newValue: string) => {
  searchInputValue.value = newValue;
};

const onClickSearch = (value: string) => {
  setSearchKeyword(value);
  getUserList();
};

const openModal = () => {
  open();
};

const cellClicked = ({
  data,
  column,
}: {
  data: { fqn: string };
  column: { colId: string };
}) => {
  // 사용자 이름 컬럼을 클릭했을 때에만 사용자 마이페이지로 이동
  if (column.colId === "displayName") {
    router.push(`/portal/my-page?fqn=${data.fqn}`);
  }
};

setSearchKeyword("");
await getUserList();
const { scrollTrigger } = useIntersectionObserver({ callback: addUserList });
</script>

<style>
/* TODO: ag-grid 에 header 중앙정렬 기능 추가? or 퍼블리싱 요청? */
.ag-header-cell-label {
  justify-content: center;
}
</style>
