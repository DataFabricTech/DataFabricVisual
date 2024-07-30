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
            <lable class="hidden-text" for="title-modify"
              >카테고리 이름 수정
            </lable>
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
            <lable class="hidden-text" for="description-modify"
              >카테고리 설명 수정
            </lable>
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
          <div>
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
                  <button class="button button-secondary-stroke" @click="showModalChange = true">
                    카테고리 변경
                  </button>
                  <button class="button button-secondary">
                    데이터모델추가
                  </button>
                </div>
              </div>
            </div>
            <div class="l-resource-box l-split mt-3">
              <div class="data-page">
                <div class="data-list">
                  <!-- TODO: [개발] resource-box 시작 컴포넌트화  -->
                  <div
                    class="resource-box resource-box is-resource-box-selected"
                  >
                    <div class="resource-box-function">
                      <div class="resource-box-model">
                        <div class="checkbox">
                          <input
                            type="checkbox"
                            id="checkbox-menu-2"
                            class="checkbox-input"
                          />
                          <label for="checkbox-menu-2" class="checkbox-label">
                          </label>
                        </div>
                        <img src="" alt="" />
                        <div class="breadcrumb">
                          <ul class="breadcrumb-list">
                            <li class="breadcrumb-item">
                              <span class="breadcrumb-link">1depth</span>
                            </li>
                            <li class="breadcrumb-item">
                              <span class="breadcrumb-link">2depth</span>
                            </li>
                            <li class="breadcrumb-item is-breadcrumb-selected">
                              <span class="breadcrumb-link">데이터 모델</span>
                            </li>
                          </ul>
                        </div>
                      </div>
                    </div>
                    <a href="#" class="editable-group-title" title="상세 보기"
                      >세종특별자치시 상하수도요금표</a
                    >
                    <span class="editable-group-desc"
                      >한국교통안전공단에서 교통카드를 이용한 대중교통 사용시
                      1회 이용요금 평균을 조사한 결과
                      입니다.한국교통안전공단에서 교통카드를 이용한 대중교통
                      사용시 1회 이용요금 평균을 조사한 결과
                      입니다한국교통안전공단에서 교통카드를 이용한 대중교통
                      사용시 1회 이용요금 평균을 조사한 결과
                      입니다한국교통안전공단에서 교통카드를 이용한 대중교통
                      사용시 1회 이용요금 평균을 조사한 결과
                      입니다한국교통안전공단에서 교통카드를 이용한 대중교통
                      사용시 1회 이용요금 평균을 조사한 결과
                      입니다한국교통안전공단에서 교통카드를 이용한 대중교통
                      사용시 1회 이용요금 평균을 조사한 결과
                      입니다한국교통안전공단에서 교통카드를 이용한 대중교통
                      사용시 1회 이용요금 평균을 조사한 결과 입니다</span
                    >
                    <div class="resource-box-info">
                      <dl class="resource-box-list">
                        <dt>소유자</dt>
                        <dd>Owner</dd>
                      </dl>
                      <dl class="resource-box-list">
                        <dt>카테고리</dt>
                        <dd>Domain</dd>
                      </dl>
                    </div>
                  </div>
                  <!-- resource-box 끝 -->
                  <!-- resource-box 시작 -->
                  <div class="resource-box" v-for="card in 9" :key="card">
                    <div class="resource-box-function">
                      <div class="resource-box-model">
                        <div class="checkbox">
                          <input
                            type="checkbox"
                            id="checkbox-menu-3"
                            class="checkbox-input"
                          />
                          <label for="checkbox-menu-3" class="checkbox-label">
                          </label>
                        </div>
                        <img src="" alt="" />
                        <div class="breadcrumb">
                          <ul class="breadcrumb-list">
                            <li class="breadcrumb-item">
                              <span class="breadcrumb-text">1depth</span>
                            </li>
                            <li class="breadcrumb-item">
                              <span class="breadcrumb-text">2depth</span>
                            </li>
                            <li class="breadcrumb-item is-breadcrumb-selected">
                              <span class="breadcrumb-text">데이터 모델</span>
                            </li>
                          </ul>
                        </div>
                      </div>
                    </div>
                    <a href="#" class="editable-group-title" title="상세 보기"
                      >세종특별자치시 상하수도요금표</a
                    >
                    <span class="editable-group-desc"
                      >한국교통안전공단에서 교통카드를 이용한 대중교통 사용시
                      1회 이용요금 평균을 조사한 결과
                      입니다.한국교통안전공단에서 교통카드를 이용한 대중교통
                      사용시 1회 이용요금 평균을 조사한 결과
                      입니다한국교통안전공단에서 교통카드를 이용한 대중교통
                      사용시 1회 이용요금 평균을 조사한 결과
                      입니다한국교통안전공단에서 교통카드를 이용한 대중교통
                      사용시 1회 이용요금 평균을 조사한 결과
                      입니다한국교통안전공단에서 교통카드를 이용한 대중교통
                      사용시 1회 이용요금 평균을 조사한 결과
                      입니다한국교통안전공단에서 교통카드를 이용한 대중교통
                      사용시 1회 이용요금 평균을 조사한 결과
                      입니다한국교통안전공단에서 교통카드를 이용한 대중교통
                      사용시 1회 이용요금 평균을 조사한 결과 입니다</span
                    >
                    <div class="resource-box-info">
                      <dl class="resource-box-list">
                        <dt>소유자</dt>
                        <dd>Owner</dd>
                      </dl>
                      <dl class="resource-box-list">
                        <dt>카테고리</dt>
                        <dd>Domain</dd>
                      </dl>
                    </div>
                  </div>
                  <!-- resource-box 끝 -->
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
    <div class="modal modal-padding-16" style="width:350px;height: 450px">
      <div class="modal-head">
        <div class="modal-head-text">
          <span class="modal-head-title">카테고리 변경</span>
        </div>
        <button class="button link-button button-sm" type="button" @click="showModalChange = false">
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
          <button class="button button-neutral-ghost button-lg" @click="showModalChange = false">취소</button>
          <button class="button button-primary button-lg">선택</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import TreeVue from "@extends/tree/Tree.vue";
import type { TreeViewItem } from "@extends/tree/TreeProps";

const showModal = ref(false);
const showModalChange = ref(false);


const items: any[] = [
  {
    id: "58615558-f39c-46d9-b5f3-d7884b1e25dd",
    name: "카테고리 01",
    order: 1,
    desc: "카테고리 01 설명",
    parentId: "root",
    expanded: false,
    selected: false,
    disabled: false,
    children: [],
  },
  {
    id: "19b89e77-6a1c-4214-8a86-433878949b74",
    name: "카테고리 02",
    order: 2,
    desc: "카테고리 02 설명",
    parentId: "root",
    expanded: false,
    selected: false,
    disabled: false,
    children: [
      {
        id: "c111b158-47cb-419c-a6b1-0a29eab162b9",
        name: "카테고리 02 - 01",
        order: 1,
        desc: "카테고리 02 - 01 설명",
        parentId: "19b89e77-6a1c-4214-8a86-433878949b74",
        expanded: false,
        selected: false,
        disabled: false,
        children: [],
      },
      {
        id: "f96f792a-5f55-46a0-ab29-586a221afa2f",
        name: "카테고리 02 - 02",
        order: 2,
        desc: "카테고리 02 - 02 설명",
        parentId: "19b89e77-6a1c-4214-8a86-433878949b74",
        expanded: false,
        selected: false,
        disabled: false,
        children: [],
      },
      {
        id: "76b2bda2-31a3-4f7d-927a-c2ddd6354741",
        name: "카테고리 02 - 03",
        order: 3,
        desc: "카테고리 02 - 03 설명",
        parentId: "19b89e77-6a1c-4214-8a86-433878949b74",
        expanded: false,
        selected: false,
        disabled: false,
        children: [
          {
            id: "868928fc-4be3-46a3-8f07-95b516a59b92",
            name: "카테고리 02 - 03 - 01",
            order: 1,
            desc: "카테고리 02 - 03 - 01 설명",
            expanded: false,
            selected: false,
            disabled: false,
            children: [],
          },
        ],
      },
    ],
  },
  {
    id: "3b738522-3f7b-4e0e-9457-fdbfaab11524",
    name: "카테고리 03",
    order: 3,
    desc: "카테고리 03 설명",
    parentId: "root",
    expanded: false,
    selected: false,
    disabled: false,
    children: [],
  },
  {
    id: "e65fa255-98d6-4b5c-ac9e-b517e235ab07",
    name: "카테고리 04",
    order: 4,
    desc: "카테고리 04 설명",
    parentId: "root",
    expanded: false,
    selected: false,
    disabled: false,
    children: [],
  },
  {
    id: "6fb83657-2a41-4100-8193-5df10cce6e9e",
    name: "카테고리 05",
    order: 5,
    desc: "카테고리 05 설명",
    parentId: "root",
    expanded: false,
    selected: false,
    disabled: false,
    children: [
      {
        id: "4911b731-8573-4b74-a082-4cb7aa9a0c2f",
        name: "카테고리 05 - 01",
        order: 1,
        desc: "카테고리 05 - 01 설명",
        parentId: "6fb83657-2a41-4100-8193-5df10cce6e9e",
        expanded: false,
        selected: false,
        disabled: false,
        children: [],
      },
    ],
  },
];
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
};
const addSibling = (newNode: TreeViewItem) => {
  // 형제 노드 추가
  // TODO : modal 창 띄워서 노드 추가 API  호출
  console.log(`형제노드 추가 ${JSON.stringify(newNode)}`);
};
const addChild = (newNode: TreeViewItem) => {
  // 자식 노드 추가
  // TODO : modal 창 띄워서 노드 추가 API  호출
  console.log(`자식노드 추가 ${JSON.stringify(newNode)}`);
};

const droppedNode: Ref<TreeViewItem> = ref<TreeViewItem>(<TreeViewItem>{});
const dropValidator = (
  thisNode: TreeViewItem,
  targetNode: TreeViewItem,
  newNode: TreeViewItem,
): boolean => {
  console.log(`drop validator`);
  console.log(`선택한 노드 ${JSON.stringify(thisNode)}`);
  console.log(`타겟 노드 ${JSON.stringify(targetNode)}`);
  console.log(`타겟 노드 ${JSON.stringify(newNode)}`);

  let isValid = false;
  // 조건 1: targetNode 에 데이터 모델이 설정되어 있으면 drop 불가능
  // TODO : targetNode 기준 데이터 모델 설정 여부 조회하는 API 호출

  // 조건 2: thisNode에 데이터 모델이 설정되어 있으면 targetNode 는 하위 노드일때만 가능.
  // TODO : thisNode 기준 데이터 모델 설정 여부 조회하는 API 호출 -> 2-1
  // TODO : 2-1 에서 thisNode에 데이터 모델이 설정되어 있는 경우, targetNode.children 에 값이 없는 노드여야 함.

  // 조건 만족시
  isValid = true;

  droppedNode.value = newNode;
  // TODO : isValid 가 true 면 update API 호출

  return isValid;
};
</script>

<style scoped></style>
