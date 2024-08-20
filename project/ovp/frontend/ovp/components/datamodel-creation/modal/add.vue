<template>
  <Modal
    title="데이터 모델 추가"
    class="modal"
    :modal-id="props.modalId"
    background="non-interactive"
    displayDirective="show"
    overlayTransition="vfm-fade"
    contentTransition="vfm-fade"
    :clickToClose="true"
    :escToClose="true"
    :width="900"
    :height="681"
    :lockScroll="true"
    swipeToClose="none"
    @closed="onCancelModal"
    @click-outside="onCancelModal"
    @before-open="onOpenModal"
    @cancel="onCancelModal"
    @confirm="onConfirmModal"
  >
    <template v-slot:body>
      <div class="data-add">
        <add-transfer @select="onSaveSelectData"></add-transfer>
        <div class="tab">
          <ul class="tab-list">
            <li class="tab-item is-tab-item-selected">
              <button class="tab-button">
                <p class="tab-button-text">샘플데이터</p>
              </button>
            </li>
            <li class="tab-item">
              <button class="tab-button">
                <p class="tab-button-text">데이터 프로파일링</p>
              </button>
            </li>
            <li class="tab-item">
              <button class="tab-button">
                <p class="tab-button-text">연관 데이터</p>
              </button>
            </li>
          </ul>
          <div class="tab-contents">
            <div class="data-summary">
              <span class="data-summary-title">데이터 모델 요약</span>
              <span class="data-summary-name"
                >데이터 모델 길어지면 말줄임 합니다</span
              >
              <span class="data-summary-text">(소유자)</span>
            </div>
            <!--  샘플데이터 탭 시작-->
            <div class="table-scroll">
              <table>
                <thead>
                  <tr>
                    <th class="relative">
                      NAME
                      <div class="dropdown" style="">
                        <ul class="dropdown-list">
                          <li class="dropdown-item">
                            <button class="dropdown-button">
                              <span class="dropdown-text"
                                >복사 (컬럼 이름)</span
                              >
                            </button>
                          </li>
                          <li class="dropdown-item">
                            <button class="dropdown-button">
                              <span class="dropdown-text"
                                >복사 (모든 컬럼 이름)</span
                              >
                            </button>
                          </li>
                        </ul>
                      </div>
                    </th>
                    <th class="relative">DATA TYPE</th>
                    <th class="relative">NAME</th>
                    <th class="relative">DATA TYPE</th>
                    <th class="relative">NAME</th>
                    <!-- TODO: [개발] 레이아웃 깨짐으로 인해 맨오른쪽 아이템은 예외로 right:0; 를 적용해서 오른쪽으로 배치시켜야 합니다. -->
                    <th class="relative">
                      DATA TYPE
                      <div class="dropdown" style="right: 0">
                        <ul class="dropdown-list">
                          <li class="dropdown-item">
                            <button class="dropdown-button">
                              <span class="dropdown-text"
                                >복사 (컬럼 이름)</span
                              >
                            </button>
                          </li>
                          <li class="dropdown-item">
                            <button class="dropdown-button">
                              <span class="dropdown-text"
                                >복사 (모든 컬럼 이름)</span
                              >
                            </button>
                          </li>
                        </ul>
                      </div>
                    </th>
                  </tr>
                </thead>
                <tbody>
                  <tr>
                    <td>항목</td>
                    <td>항목</td>
                    <td>항목</td>
                    <td>항목</td>
                    <td>항목</td>
                    <td>항목</td>
                  </tr>
                  <tr>
                    <td>항목</td>
                    <td>항목</td>
                    <td>항목</td>
                    <td>항목</td>
                    <td>항목</td>
                    <td>항목</td>
                  </tr>
                  <tr>
                    <td>항목</td>
                    <td>항목</td>
                    <td>항목</td>
                    <td>항목항목항목항목항목항목항목항목</td>
                    <td>항목</td>
                    <td>항목</td>
                  </tr>
                  <tr>
                    <td>항목</td>
                    <td>항목</td>
                    <td>항목</td>
                    <td>항목</td>
                    <td>항목</td>
                    <td>항목</td>
                  </tr>
                  <tr>
                    <td>항목</td>
                    <td>항목</td>
                    <td>항목</td>
                    <td>항목</td>
                    <td>항목</td>
                    <td>항목</td>
                  </tr>
                </tbody>
              </table>
            </div>
            <!--  샘플데이터 탭 끝-->
            <!--  데이터 프로파일링 시작-->
            <div class="ag-grid h-full" style="display: none"></div>
            <!--  데이터 프로파일링 끝-->
            <!--  연관데이터 시작-->
            <div class="l-split" style="display: none">
              <div class="knowledge">knowledge</div>
              <div class="data-list">
                <div class="menu menu-data w-full">
                  <ul class="menu-list">
                    <li class="menu-item is-menu-item-selected">
                      <button class="menu-button">
                        <svg-icon
                          class="svg-icon menu-data-icon"
                          name="resource"
                        ></svg-icon>
                        <span class="menu-text">데이터 모델</span>
                        <span class="menu-subtext">(소유자)</span>
                      </button>
                      <div class="menu-button-group">
                        <div class="relative">
                          <button class="button button-neutral-ghost button-sm">
                            <span class="hidden-text">메뉴보기</span>
                            <svg-icon
                              class="svg-icon"
                              name="kebab-menu"
                            ></svg-icon>
                          </button>
                          <div class="dropdown" style="right: 0">
                            <ul class="dropdown-list">
                              <li class="dropdown-item">
                                <button class="dropdown-button">
                                  <span class="dropdown-text"
                                    >데이터 모델 상세 조회</span
                                  >
                                </button>
                              </li>
                              <li class="dropdown-item">
                                <button class="dropdown-button">
                                  <span class="dropdown-text"
                                    >데이터 선택(해제)</span
                                  >
                                </button>
                              </li>
                            </ul>
                          </div>
                        </div>
                      </div>
                    </li>
                    <li class="menu-item" v-for="item in 5" :key="item">
                      <button class="menu-button">
                        <svg-icon
                          class="svg-icon menu-data-icon"
                          name="resource"
                        ></svg-icon>
                        <span class="menu-text">데이터 모델</span>
                        <span class="menu-subtext">(소유자)</span>
                      </button>
                      <div class="menu-button-group">
                        <div class="relative">
                          <button class="button button-neutral-ghost button-sm">
                            <span class="hidden-text">메뉴보기</span>
                            <svg-icon
                              class="svg-icon"
                              name="kebab-menu"
                            ></svg-icon>
                          </button>
                        </div>
                      </div>
                    </li>
                  </ul>
                </div>
                <div class="no-result" style="display: none">
                  <div class="notification">
                    <svg-icon class="notification-icon" name="info"></svg-icon>
                    <p class="notification-detail">샘플 데이터가 없습니다.</p>
                  </div>
                </div>
              </div>
            </div>
            <!-- 연관데이터 끝-->
            <!-- 샘플데이터, 데이터프로파일링 결과 없을 시 no-result 표시 -->
            <div class="no-result" style="display: none">
              <div class="notification">
                <svg-icon class="notification-icon" name="info"></svg-icon>
                <p class="notification-detail">샘플 데이터가 없습니다.</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </template>
  </Modal>
</template>
<script setup lang="ts">
import Modal from "@extends/modal/Modal.vue";
import { useCreationStore } from "~/store/datamodel-creation";
import AddTransfer from "~/components/datamodel-creation/modal/add-transfer.vue";
import { ref } from "vue";
import _ from "lodash";
import { useNuxtApp } from "nuxt/app";
import { useDataModelSearchStore } from "~/store/datamodel-creation/search";
import { storeToRefs } from "pinia";

const props = defineProps({
  modalId: {
    type: String,
    required: true,
  },
});

const creationStore = useCreationStore();
const { selectedModelList, nSelectedListData } = storeToRefs(creationStore);
const { setSelectedModelList } = creationStore;

// 데이터 목록, 필터 목록, 선택 필터 초기화
setSelectedModelList();

// 탐색 > 데이터 모델 조회 Store
const dataModelSearchStore = useDataModelSearchStore();
const { resetReloadList, getFilters } = dataModelSearchStore;

const { $vfm } = useNuxtApp();

const emit = defineEmits<{ (e: "change", option: boolean): void }>();

const selectedListData = ref([]);
const setSelectedListData = () => {
  selectedListData.value = _.cloneDeep(selectedModelList.value);
};

watchEffect(() => {
  setSelectedListData();
});

const onSaveSelectData = (value: []) => {
  selectedListData.value = value;
};

const onOpenModal = async () => {
  // 전체+MY / 필터 / 선택 목록 데이터 초기화
  nSelectedListData.value = $_cloneDeep(selectedModelList.value);
  await resetReloadList();
  await getFilters();
};

const onCancelModal = () => {
  // 내부 데이터 초기화 -> store 데이터
  selectedListData.value = selectedModelList.value;
  $vfm.close(props.modalId);
};

const onConfirmModal = () => {
  // 내부 데이터 저장
  selectedModelList.value = selectedListData.value;
  $vfm.close(props.modalId);
};
</script>
<style lang="scss" scoped></style>
