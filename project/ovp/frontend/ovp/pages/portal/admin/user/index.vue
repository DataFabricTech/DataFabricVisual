<template>
  <div class="section-top-bar">
    <div class="l-top-bar">
      <h3 class="title">사용자 관리</h3>
    </div>
  </div>
  <div class="section-contents bg-white">
    <div class="l-between mb-3">
      <!-- TODO: SearchInput 컴포넌트 접근성 개선 개발 완료되면 label/input의 for/id 값 "data-menu-search" 로 설정-->
      <SearchInput
        @onClickSearch="onClickSearch"
        :placeholder="'검색어를 입력하세요.'"
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
          style="width: 100%"
          domLayout="autoHeight"
          :columnDefs="columnDefs"
          :columnWidthList="[100, 100, 500, 100]"
          rowId="id"
          :rowData="userList"
          :buttons="[
            {
              headerText: '관리',
              rendererType: 'button',
              type: 'button',
              buttonTheme: 'button-error-stroke',
              buttonText: '삭제',
              order: 4,
              fn: deleteClicked,
              cellStyle: {
                display: 'flex',
                justifyContent: 'center',
              },
            },
          ]"
          :setColumnFit="true"
          :useColumnResize="true"
          @cellClicked="cellClicked"
        >
        </agGrid>
        <div ref="scrollTrigger" class="w-full h-[1px] mt-px"></div>
        <Loading
          id="loader"
          :use-loader-overlay="true"
          class="loader-lg is-loader-inner"
          style="display: none"
        ></Loading>
      </div>
    </div>
  </div>

  <AddUserModal></AddUserModal>
</template>

<script setup lang="ts">
import { useNuxtApp } from "nuxt/app";
import { useRouter } from "#vue-router";
import { useUserStore } from "@/store/user/userStore";
import { useIntersectionObserver } from "~/composables/intersectionObserverHelper";
import SearchInput from "@extends/search-input/SearchInput.vue";
import agGrid from "@extends/grid/Grid.vue";
import Loading from "@base/loading/Loading.vue";
import AddUserModal from "@/components/admin/user/modal/add.vue";

const { $vfm } = useNuxtApp();
const router = useRouter();
const userStore = useUserStore();
const { setSearchKeyword, getUserList, addUserList, deleteUser } = userStore;
const { userList } = storeToRefs(userStore);

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
]);

const onClickSearch = (value: string) => {
  setSearchKeyword(value);
  getUserList();
};

const openModal = () => {
  $vfm.open("addUserModal");
};

const cellClicked = ({
  data,
  column,
}: {
  data: { name: string };
  column: { colId: string };
}) => {
  // TODO: 마이페이지 개발 완료되면 확인 필요
  // 사용자 이름 컬럼을 클릭했을 때에만 사용자 마이페이지로 이동
  if (column.colId === "displayName") {
    router.push(`/portal/my-page?fqn=${data.name}`);
  }
};

const deleteClicked = ({
  data,
  rowId,
}: {
  data: { [key: string]: any };
  rowId: string;
}) => {
  // TODO: confirm alert 개발되면 변경
  if (confirm("사용자를 삭제하시겠습니까??")) {
    deleteUser(data[rowId])
      .then(() => {
        alert("삭제 되었습니다.");
      })
      .catch((err: any) => {
        console.log("err: ", err);
      });
  }
};

const { scrollTrigger } = useIntersectionObserver(addUserList);
</script>

<style>
/* TODO: ag-grid 에 header 중앙정렬 기능 추가? or 퍼블리싱 요청? */
.ag-header-cell-label {
  justify-content: center;
}
</style>
