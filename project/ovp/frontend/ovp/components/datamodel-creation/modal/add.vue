<template>
  <div class="modal-fixed vfm--fixed vfm--inset">
    <div class="modal" style="width: 900px">
      <div class="modal-head">
        <div class="modal-head-text">
          <span class="modal-head-title">데이터 모델 추가</span>
        </div>
        <button
          class="button link-button button-sm"
          type="button"
          @click="addDataModel(false)"
        >
          <span class="hidden-text">닫기</span>
          <svg-icon class="button-icon" name="close"></svg-icon>
        </button>
      </div>
      <div class="modal-body">
        <div class="data-add">
          <div class="transfer">
            <div class="transfer-box">
              <Tab
                class="h-full"
                :data="$constants.DATAMODEL_CREATION.ADD.TAB"
                label-key="label"
                value-key="value"
                current-item-type="value"
                :current-item="currTab"
                @change="onChangeTab"
              >
                <template #all>
                  <!-- 전체 탭 시작  -->
                  <data-model-api-list
                    class="h-full"
                    :filter="filter"
                    :data="dataModelList"
                    :sort-list="$constants.COMMON.SORT_FILTER"
                    :selected-items="selectedModelList"
                    label-key="modelNm"
                    value-key="id"
                    :use-item-delete-btn="false"
                    :is-multi="true"
                    :use-sort="true"
                    :use-infinite="true"
                    :use-live-search="false"
                    list-type="non-selected"
                    no-data-msg="데이터 모델이 없습니다."
                    @delete="onDeleteDataModel"
                  >
                    <template v-slot:tab>
                      <div class="tab tab-line mb-3">
                        <ul class="tab-list">
                          <li class="tab-item is-tab-item-selected">
                            <button class="tab-button">
                              <p class="tab-button-text">테이블</p>
                            </button>
                          </li>
                          <li class="tab-item">
                            <button class="tab-button">
                              <p class="tab-button-text">스토리지</p>
                            </button>
                          </li>
                          <li class="tab-item">
                            <button class="tab-button">
                              <p class="tab-button-text">융합모델</p>
                            </button>
                          </li>
                        </ul>
                      </div>
                    </template>
                  </data-model-api-list>
                </template>
                <template #my>
                  <data-model-accordian-list
                    :data="myModelList"
                    :selected-items="selectedModelList"
                    label-key="modelNm"
                    value-key="id"
                    :use-item-delete-btn="false"
                    :is-multi="true"
                    :use-live-search="false"
                    list-type="non-selected"
                    no-data-msg="데이터 모델이 없습니다."
                  ></data-model-accordian-list>
                </template>
              </Tab>
            </div>
            <div class="transfer-handle">
              <button class="button button-neutral-stroke">
                <svg-icon
                  class="button-icon"
                  name="chevron-right-medium"
                ></svg-icon>
                <span class="hidden-text">오른쪽 이동</span>
              </button>
              <button class="button button-neutral-stroke">
                <svg-icon
                  class="button-icon"
                  name="chevron-left-medium"
                ></svg-icon>
                <span class="hidden-text">왼쪽 이동</span>
              </button>
            </div>
            <div class="transfer-box">
              <div class="transfer-head">
                <span>선택된 데이터 모델({{ selectedListLength }})</span>
              </div>
              <data-model-list
                :filter="filter"
                :data="selectedModelList"
                label-key="modelNm"
                value-key="id"
                :use-item-delete-btn="true"
                :is-multi="true"
                :use-sort="false"
                :use-infinite="false"
                :use-live-search="true"
                list-type="selected"
                no-data-msg="선택된 데이터 모델이 없습니다."
                @delete="onDeleteDataModel"
              ></data-model-list>
            </div>
          </div>
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
              <div class="table-scroll" style="display: none">
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
              <div class="l-split">
                <div class="knowledge">knowledge</div>
                <div class="data-list">
                  <div class="menu menu-data w-full">
                    <ul class="menu-list">
                      <li class="menu-item is-menu-item-selected">
                        <button class="menu-button">
                          <div class="type-img type-img-oracle"></div>
                          <span class="menu-text">데이터 모델</span>
                          <span class="menu-subtext">(소유자)</span>
                        </button>
                        <div class="menu-button-group">
                          <div class="relative">
                            <button
                              class="button button-neutral-ghost button-sm"
                            >
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
                          <div class="type-img type-img-oracle"></div>
                          <span class="menu-text">데이터 모델</span>
                          <span class="menu-subtext">(소유자)</span>
                        </button>
                        <div class="menu-button-group">
                          <div class="relative">
                            <button
                              class="button button-neutral-ghost button-sm"
                            >
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
                      <svg-icon
                        class="notification-icon"
                        name="info"
                      ></svg-icon>
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
      </div>
      <div class="modal-foot">
        <div class="modal-foot-group">
          <button class="button button-primary button-lg">확인</button>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
import Tab from "@extends/tab/Tab.vue";
import $constants from "~/utils/constant";
import DataModelApiList from "~/components/datamodel-creation/list/api/data-model-api-list.vue";
import DataModelList from "~/components/datamodel-creation/list/base/data-model-list.vue";
import DataModelAccordianList from "~/components/datamodel-creation/list/api/accoridan/data-model-accordian-list.vue";

const props = defineProps({
  dataModelList: {
    type: Array,
    required: true,
  },
  selectedModelList: {
    type: Array,
    required: true,
  },
  myModelList: {
    type: Object,
    required: true,
  },
  filter: {
    type: Object,
    required: true,
  },
});

const currTab = ref("all");

const emit = defineEmits<{ (e: "change", option: boolean): void }>();
const selectedListLength = computed(() => {
  return props.selectedModelList ? props.selectedModelList.length : 0;
});

const addDataModel = (option: boolean) => {
  emit("change", option);
};
const onDeleteDataModel = (option: boolean) => {
  console.log("onDeleteDataModel");
};
function onChangeTab(item: number | string) {
  currTab.value = item;
}
</script>
<style lang="scss" scoped>
/* @import "./index.scss"; */
</style>
