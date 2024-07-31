<template>
  <div class="section-top-bar">
    <div class="l-top-bar">
      <h3 class="title">카테고리</h3>
      <button class="button button-primary w-20">저장</button>
    </div>
  </div>
  <div class="section-contents p-0 bg-white">
    <div class="l-split">
      <div class="work-list">
        <div class="l-top-bar">
          <span class="title">카테고리 목록</span>
          <button
            class="button button-secondary-stroke"
            @click="showModal = true"
          >
            카테고리 추가
          </button>
        </div>
        <!-- 결과 없을 시 no-result 표시 / 기본 .work-page로 컨텐츠 표시 -->
        <div class="no-result" style="display: none">
          <div class="notification">
            <svg-icon class="notification-icon" name="info"></svg-icon>
            <p class="notification-detail">등록된 정보가 없습니다.</p>
          </div>
        </div>
        <div class="p-3">
          <tree-vue
            :items="categories"
            :isCheckable="false"
            :hideGuideLines="false"
            :firExpandAll="true"
            :show-open-all-btn="true"
            :show-close-all-btn="true"
            :use-draggable="true"
            mode="view"
            :dropValidator="dropValidator"
            @onItemSelected="onNodeClicked"
            @addSibling="addSibling"
            @addChild="addChild"
          />
        </div>
      </div>
      <div class="work-page">
        <div class="l-top-bar">
          <div class="editable-group">
            <span class="editable-group-title">{{ selectedNode.name }}</span>
            <button class="button button-neutral-ghost button-sm" type="button">
              <span class="hidden-text">수정</span>
              <svg-icon class="button-icon" name="pen"></svg-icon>
            </button>
          </div>
          <!-- 수정 버튼 클릭시 아래 내용으로 전환됩니다 -->
          <div class="editable-group">
            <label class="hidden-text" for="title-modify"
              >카테고리 이름 수정
            </label>
            <input id="title-modify" class="text-input w-4/5" />
            <div class="h-group gap-1">
              <button class="button button-neutral-stroke" type="button">
                취소
              </button>
              <button class="button button-primary-lighter" type="button">
                완료
              </button>
            </div>
          </div>
          <button class="button button-error-lighter">삭제</button>
        </div>
        <div class="work-contents">
          <!-- 결과 없을 시 no-result 표시  -->
          <div class="no-result" style="display: none">
            <div class="notification">
              <svg-icon class="notification-icon" name="info"></svg-icon>
              <p class="notification-detail">등록된 정보가 없습니다.</p>
            </div>
          </div>
          <div class="editable-group">
            <span class="editable-group-desc">{{ selectedNode.desc }}</span>
            <button class="button button-neutral-ghost button-sm" type="button">
              <span class="hidden-text">수정</span>
              <svg-icon class="button-icon" name="pen"></svg-icon>
            </button>
          </div>
          <!-- 수정 버튼 클릭시 아래 내용으로 전환됩니다 -->
          <div class="editable-group">
            <label class="hidden-text" for="description-modify"
              >카테고리 설명 수정
            </label>
            <textarea
              id="description-modify"
              class="textarea"
              width="300px"
            ></textarea>
            <div class="h-group gap-1">
              <button class="button button-neutral-stroke" type="button">
                취소
              </button>
              <button class="button button-primary-lighter" type="button">
                완료
              </button>
            </div>
          </div>
          <!-- NOTE : v-if 로 할 경우, intersectionObserver 이 element 의 변화를 catch 하지 못해서 동작하지 않는 현상이 있어서 v-show 로 변환함. -->
          <div v-show="modelList.length > 0">
            <div class="l-top-bar">
              <div class="search-input w-[541px]">
                <label class="hidden-text" for="text-input-example-11"
                  >label</label
                >
                <input
                  id="text-input-example-11"
                  class="text-input"
                  placeholder="검색어 입력"
                />
                <svg-icon class="text-input-icon" name="search"></svg-icon>
                <button
                  class="search-input-action-button button button-neutral-ghost button-sm"
                  type="button"
                >
                  <span class="hidden-text">지우기</span>
                  <svg-icon class="button-icon" name="close"></svg-icon>
                </button>
              </div>
              <div class="h-group w-full">
                <div class="checkbox">
                  <input
                    type="checkbox"
                    id="checkbox-menu-1"
                    class="checkbox-input"
                  />
                  <label for="checkbox-menu-1" class="checkbox-label">
                    전체선택
                  </label>
                </div>
                <div class="h-group ml-auto gap-2">
                  <button
                    class="button button-secondary-stroke"
                    @click="showModalChange = true"
                  >
                    카테고리 변경
                  </button>
                  <button class="button button-secondary">
                    데이터모델추가
                  </button>
                </div>
              </div>
            </div>
            <div class="l-resource-box l-split mt-3">
              <div class="data-page" style="position: relative">
                <div id="dataList" class="data-list">
                  <resource-box-list
                    :data-list="modelList || []"
                    :use-list-checkbox="true"
                    :show-owner="true"
                    :show-category="true"
                    :is-box-selected-style="false"
                    @previewClick="previewClick"
                    @modelNmClick="modelNmClick"
                    @checkedValueChanged="checked"
                  />
                  <div ref="scrollTrigger" class="w-full h-[1px] mt-px"></div>
                  <!--                TODO: [퍼블리싱] loader UI 컴포넌트 추가 및 로딩 위치 검토 필요 -->
                  <Loading
                    id="loader"
                    :use-loader-overlay="true"
                    class="loader-lg is-loader-inner"
                    style="display: none"
                  ></Loading>
                </div>
              </div>
              <div class="preview">
                <div class="ml-auto">
                  <button class="button button-neutral-ghost button-sm">
                    <svg-icon class="svg-icon" name="close"></svg-icon>
                    <span class="hidden-text">닫기</span>
                  </button>
                </div>
                <div class="preview-contents">
                  <div class="preview-item">
                    <div class="preview-title">
                      세종특별자치시 상하수도요금표
                    </div>
                    <div class="preview-desc">
                      한국교통안전공단에서 교통카드를 이용한 대중교통 사용시 1회
                      이용요금 평균을 조사한 결과 입니다.
                    </div>
                    <table>
                      <colgroup>
                        <col style="width: 30%" />
                        <col />
                      </colgroup>
                      <tr>
                        <th>확장자</th>
                        <td>PDF</td>
                      </tr>
                      <tr>
                        <th>전체 행</th>
                        <td>10000</td>
                      </tr>
                    </table>
                  </div>
                  <div class="preview-item">
                    <div class="preview-title">태그</div>
                    <div class="preview-group">
                      <div
                        class="tag tag-primary tag-sm"
                        v-for="tag in 8"
                        :key="tag"
                      >
                        <a class="tag-link" href="#">DATA-tag</a>
                      </div>
                    </div>
                  </div>
                  <div class="preview-item">
                    <div class="preview-title">용어</div>
                    <div class="preview-group">
                      <div class="tag tag-primary tag-sm">
                        <a class="tag-link">관련 용어</a>
                      </div>
                    </div>
                  </div>
                  <div class="preview-item">
                    <div class="preview-title">상세 설명</div>
                    <div class="preview-desc">상세 설명을 하는 구간입니다.</div>
                  </div>
                  <div class="preview-item">
                    <div class="preview-title">URL</div>
                    <a href="#" class="preview-link">
                      https://sandbox.open-metadata.org/callback#state=49e9675588d3414f9585fb455e
                    </a>
                  </div>
                  <div class="preview-item">
                    <div class="preview-title">스키마</div>
                    <div class="v-group gap-2 w-full">
                      <div class="preview-schema">
                        <div class="h-group gap-1">
                          <span class="schema-title">payment_id</span>
                          <span class="schema-subtitle">(INT)</span>
                        </div>
                        <div class="schema-desc">schema description</div>
                      </div>
                      <div class="preview-schema">
                        <div class="h-group gap-1">
                          <span class="schema-title">payment_id</span>
                          <span class="schema-subtitle">(INT)</span>
                        </div>
                        <div class="schema-desc">schema description</div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
  <!--  TODO: Modal 카테고리 추가-->
  <div class="modal-fixed vfm--fixed vfm--inset" v-if="showModal">
    <div class="modal modal-padding-16" style="width: 480px">
      <div class="modal-head">
        <div class="modal-head-text">
          <span class="modal-head-title">카테고리 추가</span>
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
      <div class="modal-body">
        <div class="form form-lg">
          <div class="form-body">
            <div class="form-item">
              <label for="data-model-save-name" class="form-label">
                이름
                <span class="required">*</span>
              </label>
              <div class="form-detail">
                <input
                  id="data-model-save-name"
                  class="text-input text-input-lg"
                  placeholder="이름을 입력하세요."
                />
                <div class="notification notification-sm notification-error">
                  <svg-icon class="notification-icon" name="error"></svg-icon>
                  <p class="notification-detail">이름을 입력하세요.</p>
                </div>
              </div>
            </div>
            <div class="form-item">
              <label class="form-label" for="data-model-save-description">
                설명
                <span class="required">*</span>
              </label>
              <div class="form-detail">
                <textarea
                  id="data-model-save-description"
                  class="textarea h-28"
                  placeholder="설명을 입력하세요."
                ></textarea>
                <div class="notification notification-sm notification-error">
                  <svg-icon class="notification-icon" name="error"></svg-icon>
                  <p class="notification-detail">설명을 입력하세요.</p>
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
          <button class="button button-primary button-lg">저장</button>
        </div>
      </div>
    </div>
  </div>
  <!--  TODO: Modal 카테고리 변경-->
  <div class="modal-fixed vfm--fixed vfm--inset" v-if="showModalChange">
    <div class="modal modal-padding-16" style="width: 350px; height: 450px">
      <div class="modal-head">
        <div class="modal-head-text">
          <span class="modal-head-title">카테고리 변경</span>
        </div>
        <button
          class="button link-button button-sm"
          type="button"
          @click="showModalChange = false"
        >
          <span class="hidden-text">닫기</span>
          <svg-icon class="button-icon" name="close"></svg-icon>
        </button>
      </div>
      <div class="modal-body">
        <tree-vue
          :items="items"
          :isCheckable="false"
          :hideGuideLines="false"
          :firExpandAll="true"
          :show-open-all-btn="true"
          :show-close-all-btn="true"
          :use-draggable="true"
          mode="view"
          :dropValidator="dropValidator"
          @onItemSelected="onNodeClicked"
          @addSibling="addSibling"
          @addChild="addChild"
        />
      </div>
      <div class="modal-foot">
        <div class="modal-foot-group">
          <button
            class="button button-neutral-ghost button-lg"
            @click="showModalChange = false"
          >
            취소
          </button>
          <button class="button button-primary button-lg">선택</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import TreeVue from "@extends/tree/Tree.vue";
import type { TreeViewItem } from "@extends/tree/TreeProps";

import { storeToRefs } from "pinia";
import { useGovernCategoryStore } from "~/store/governance/Category";
import { useIntersectionObserver } from "~/composables/intersectionObserverHelper";
import Loading from "@base/loading/Loading.vue";

const categoryStore = useGovernCategoryStore();

const {
  getCategories,
  addModelList,
  getModelList,
  setSelectedNode,
  addCategory,
  editCategory,
  moveCategory,
  deleteCategory,
} = categoryStore;
const { categories, modelList } = storeToRefs(categoryStore);

const showModal = ref(false);
const showModalChange = ref(false);

const selectedNode: Ref<TreeViewItem> = ref<TreeViewItem>({
  id: "",
  name: "",
  desc: "",
  order: 0,
  parentId: "",
  expanded: false,
  selected: false,
  disabled: false,
  children: [],
});
const onNodeClicked = (node: TreeViewItem) => {
  selectedNode.value = node;

  // 선택한 노드정보 저장
  setSelectedNode(node);

  // 선택한 노드 기준 모델 목록을 조회한다.
  setScrollOptions(0);
  getModelList();
};
const addSibling = (newNode: TreeViewItem) => {
  // 형제 노드 추가
  // TODO : modal 창 띄워서 노드 추가 API  호출 (newNode 에 id(uuid), parentId 포함되어있음)
  console.log(`형제노드 추가 ${JSON.stringify(newNode)}`);
  addNewCategory(newNode);
};
const addChild = (newNode: TreeViewItem) => {
  // 자식 노드 추가
  // TODO : modal 창 띄워서 노드 추가 API  호출
  console.log(`자식노드 추가 ${JSON.stringify(newNode)}`);
  addNewCategory(newNode);
};
// TODO : [개발] 카테고리 등록 예 (등록, 수정 같은 코드 사용합니다.)
const addNewCategory = (newNode: TreeViewItem) => {
  const addNodeParam: TreeViewItem = {
    id: "f6a91e15-18c1-4920-ab2b-dd20a68f75bc",
    parentId: selectedNode.value.id,
    name: "카테고리 01 - 01",
    desc: "카테고리 설명이여요",
    children: [],
  };
  addCategory(addNodeParam);
};

const _editCategory = () => {
  const editNodeParam: TreeViewItem = {
    id: selectedNode.value.id,
    parentId: selectedNode.value.parentId,
    name: "카테고리 수정 테스트",
    desc: "카테고리 설명을 수정",
    children: [],
  };
  editCategory(editNodeParam);
};

// TODO : [개발] 카테고리 삭제 예 - function 명 겹쳐서 임의로 _deleteCategory 로 처리함. 추후에 store - deleteCategory 이용하여 처리.
const _deleteCategory = () => {
  deleteCategory(selectedNode.value.id);
};

let nodeMoved: Ref<boolean> = ref(false);
let dropMsg: Ref<any> = ref(null);
watch(
  () => nodeMoved.value,
  (newVal) => {
    if (newVal) {
      if (dropMsg.value !== null) {
        alert(dropMsg.value);
      }
      getCategories();
    }
    nodeMoved.value = false;
    dropMsg.value = null;
  },
);

const dropValidator = async (
  dropNode: TreeViewItem,
  targetNode: TreeViewItem,
): Promise<boolean> => {
  // 조건 처리 backend 에서 진행
  const resultMsg = await moveCategory(dropNode.id, targetNode.id);

  dropMsg.value = resultMsg ? null : "이동이 불가 합니다.";
  nodeMoved.value = true;

  // tree lib가 async-await 처리를 지원하지 않기 때문에 여기서는 true 로 던지고,
  // backend 동작이 끝나면 그때 결과에 따라 watch 항목에서 alert 처리, 목록을 갱신 or 유지 한다
  return true;
};

const previewClick = async (data: object) => {
  console.log("previewClick");
};

const modelNmClick = (data: object) => {
  console.log("modelClick");
};
const checked = (checkedList: any[]) => {
  console.log(checkedList);
};

await getCategories();

const { scrollTrigger, setScrollOptions } =
  useIntersectionObserver(addModelList);
</script>

<style scoped></style>
