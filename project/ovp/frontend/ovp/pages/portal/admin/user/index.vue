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
      <button class="button button-secondary ml-auto" @click="showModal = true">
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
  <!--  TODO: Modal 카테고리 추가  width:480px height:516px-->
  <div class="modal-fixed vfm--fixed vfm--inset" v-if="showModal">
    <div class="modal modal-padding-16" style="width: 480px">
      <div class="modal-head">
        <div class="modal-head-text">
          <span class="modal-head-title">서비스 추가</span>
        </div>
        <button
          class="button link-button button-sm"
          type="button"
          @click="showModal = false"
        >
          <span class="hidden-text">닫기</span>
          <svg-icon class="button-icon" name="close"></svg-icon>
        </button>
      </div>
      <div class="modal-body" style="height: 400px; overflow: auto">
        <div class="form form-vertical">
          <div class="form-body">
            <div class="form-item">
              <label for="" class="form-label">
                이메일
                <span class="required">*</span>
              </label>
              <div class="form-detail">
                <div class="search-input">
                  <label class="hidden-text" for="service-add-input-email"
                    >label</label
                  >
                  <input
                    id="service-add-input-email"
                    class="text-input text-input-lg"
                    placeholder="이메일"
                  />
                  <button
                    class="search-input-action-button button button-neutral-ghost button-sm"
                    type="button"
                  >
                    <span class="hidden-text">지우기</span>
                    <svg-icon class="button-icon" name="close"></svg-icon>
                  </button>
                </div>
                <div class="notification notification-sm notification-error">
                  <svg-icon class="notification-icon" name="error"></svg-icon>
                  <p class="notification-detail">얼럿 메세지를 입력해주세요.</p>
                </div>
              </div>
            </div>
            <div class="form-item">
              <label for="" class="form-label"> 이름표시 </label>
              <div class="form-detail flex flex-col">
                <div class="search-input">
                  <label class="hidden-text" for="service-add-input-name"
                    >label</label
                  >
                  <input
                    id="service-add-input-name"
                    class="text-input text-input-lg"
                    placeholder="이름"
                  />
                  <button
                    class="search-input-action-button button button-neutral-ghost button-sm"
                    type="button"
                  >
                    <span class="hidden-text">지우기</span>
                    <svg-icon class="button-icon" name="close"></svg-icon>
                  </button>
                </div>
                <div class="notification notification-sm notification-error">
                  <svg-icon class="notification-icon" name="error"></svg-icon>
                  <p class="notification-detail">얼럿 메세지를 입력해주세요.</p>
                </div>
              </div>
            </div>
            <div class="form-item">
              <label class="form-label" for="service-add-description">
                설명
              </label>
              <div class="form-detail">
                <textarea
                  id="service-add-description"
                  class="textarea h-28"
                  placeholder="설명을 입력하세요."
                ></textarea>
                <div class="notification notification-sm notification-error">
                  <svg-icon class="notification-icon" name="error"></svg-icon>
                  <p class="notification-detail">설명을 입력하세요.</p>
                </div>
              </div>
            </div>
            <div class="form-item">
              <label for="" class="form-label">비밀번호</label>
              <div class="form-detail gap-2">
                <div class="radio-group">
                  <div class="radio">
                    <input
                      type="radio"
                      id="radiosm"
                      class="radio-input"
                      name="radio2"
                      checked
                    />
                    <label for="radiosm" class="radio-label">
                      비밀번호 자동생성
                    </label>
                  </div>
                  <div class="radio">
                    <input
                      type="radio"
                      id="radiosm3"
                      class="radio-input"
                      name="radio2"
                    />
                    <label for="radiosm3" class="radio-label"
                      >비밀번호 직접생성
                    </label>
                  </div>
                </div>
                <!-- 비밀번호 자동생성 -->
                <div class="text-input-group">
                  <div class="v-group w-full">
                    <div class="search-input">
                      <label
                        class="hidden-text"
                        for="service-add-input-password"
                        >label</label
                      >
                      <input
                        id="service-add-input-password"
                        type="password"
                        class="text-input text-input-lg"
                        value="prefix icon + button"
                      />
                      <button
                        class="search-input-action-button button button-neutral-ghost button-sm"
                        type="button"
                      >
                        <span class="hidden-text">지우기</span>
                        <svg-icon
                          class="button-icon"
                          name="eye-hide"
                        ></svg-icon>
                      </button>
                    </div>
                    <div
                      class="notification notification-sm notification-error"
                    >
                      <svg-icon
                        class="notification-icon"
                        name="error"
                      ></svg-icon>
                      <p class="notification-detail">
                        얼럿 메세지를 입력해주세요.
                      </p>
                    </div>
                  </div>
                  <button class="button button-neutral-stroke button-xl">
                    <span class="hidden-text"></span>
                    <svg-icon class="icons button-icon" name="reset"></svg-icon>
                  </button>
                  <button class="button button-neutral-stroke button-xl">
                    <span class="hidden-text"></span>
                    <svg-icon class="icons button-icon" name="copy"></svg-icon>
                  </button>
                </div>
                <!-- 비밀번호 직접생성 -->
                <div class="search-input">
                  <label
                    class="hidden-text"
                    for="service-add-input-password-direct"
                    >label</label
                  >
                  <input
                    id="service-add-input-password-direct"
                    type="password"
                    class="text-input text-input-lg"
                    value="prefix icon + button"
                  />
                  <button
                    class="search-input-action-button button button-neutral-ghost button-sm"
                    type="button"
                  >
                    <span class="hidden-text">지우기</span>
                    <svg-icon class="button-icon" name="eye-hide"></svg-icon>
                  </button>
                </div>
              </div>
            </div>
            <!-- 비밀번호 직접생성 선택시 노출 -->
            <div class="form-item">
              <label for="" class="form-label">비밀번호 확인</label>
              <div class="form-detail">
                <div class="search-input">
                  <label
                    class="hidden-text"
                    for="service-add-input-password-check"
                    >label</label
                  >
                  <input
                    id="service-add-input-password-check"
                    type="password"
                    class="text-input text-input-lg"
                    value="prefix icon + button"
                  />
                  <button
                    class="search-input-action-button button button-neutral-ghost button-sm"
                    type="button"
                  >
                    <span class="hidden-text">지우기</span>
                    <svg-icon class="button-icon" name="eye-hide"></svg-icon>
                  </button>
                </div>
                <div class="notification notification-sm notification-error">
                  <svg-icon class="notification-icon" name="error"></svg-icon>
                  <p class="notification-detail">얼럿 메세지를 입력해주세요.</p>
                </div>
              </div>
            </div>
            <div class="form-item">
              <label for="" class="form-label">관리자</label>
              <div class="form-detail">
                <div class="switch">
                  <input type="checkbox" id="manager-sw" class="switch-input" />
                  <label for="manager-sw" class="switch-label">
                    <div class="switch-control"></div>
                  </label>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="modal-foot">
        <div class="modal-foot-group">
          <button
            class="button button-neutral-ghost button-lg"
            @click="showModal = false"
          >
            취소
          </button>
          <button class="button button-primary button-lg">생성</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from "#vue-router";
import { useUserStore } from "@/store/user/userStore";
import { useIntersectionObserver } from "~/composables/intersectionObserverHelper";
import SearchInput from "@extends/search-input/SearchInput.vue";
import agGrid from "@extends/grid/Grid.vue";
import Loading from "@base/loading/Loading.vue";

const router = useRouter();
const userStore = useUserStore();
const { setSearchKeyword, getUserList, addUserList, deleteUser } = userStore;
const { userList } = storeToRefs(userStore);

const showModal = ref(false);

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
