<template>
  <h1>Tree</h1>
  <div>
    <div>
      <p>선택한 노드 ID : {{ selectedNode.id }}</p>
      <p>선택한 노드 NM : {{ selectedNode.name }}</p>
    </div>
    <tree-vue
      :items="items"
      :isCheckable="true"
      :hideGuideLines="false"
      :firExpandAll="true"
      :show-open-all-btn="true"
      :show-close-all-btn="true"
      :use-draggable="true"
      mode="edit"
      :dropValidator="dropValidator"
      @onItemChecked="onNodeChecked"
      @onItemSelected="onNodeClicked"
      @addSibling="addSibling"
      @addChild="addChild"
    />
  </div>
</template>

<script setup lang="ts">
import TreeVue from "@/components/extends/tree/Tree.vue";
import { TreeViewItem } from "~/components/extends/tree/TreeProps";

const items: any[] = [
  {
    id: "58615558-f39c-46d9-b5f3-d7884b1e25dd",
    name: "카테고리 01",
    order: 1,
    desc: "설명",
    parentId: "root",
    expanded: false,
    selected: false,
    disabled: false,
    children: []
  },
  {
    id: "19b89e77-6a1c-4214-8a86-433878949b74",
    name: "카테고리 02",
    order: 2,
    desc: "설명",
    parentId: "root",
    expanded: false,
    selected: false,
    disabled: false,
    children: [
      {
        id: "c111b158-47cb-419c-a6b1-0a29eab162b9",
        name: "카테고리 02 - 01",
        order: 1,
        desc: "설명",
        parentId: "19b89e77-6a1c-4214-8a86-433878949b74",
        expanded: false,
        selected: false,
        disabled: false,
        children: []
      },
      {
        id: "f96f792a-5f55-46a0-ab29-586a221afa2f",
        name: "카테고리 02 - 02",
        order: 2,
        desc: "설명",
        parentId: "19b89e77-6a1c-4214-8a86-433878949b74",
        expanded: false,
        selected: false,
        disabled: false,
        children: []
      },
      {
        id: "76b2bda2-31a3-4f7d-927a-c2ddd6354741",
        name: "카테고리 02 - 03",
        order: 3,
        desc: "설명",
        parentId: "19b89e77-6a1c-4214-8a86-433878949b74",
        expanded: false,
        selected: false,
        disabled: false,
        children: [
          {
            id: "868928fc-4be3-46a3-8f07-95b516a59b92",
            name: "카테고리 02 - 03 - 01",
            order: 1,
            desc: "설명",
            expanded: false,
            selected: false,
            disabled: false,
            children: []
          }
        ]
      }
    ]
  },
  {
    id: "3b738522-3f7b-4e0e-9457-fdbfaab11524",
    name: "카테고리 03",
    order: 3,
    desc: "설명",
    parentId: "root",
    expanded: false,
    selected: false,
    disabled: false,
    children: []
  },
  {
    id: "e65fa255-98d6-4b5c-ac9e-b517e235ab07",
    name: "카테고리 04",
    order: 4,
    desc: "설명",
    parentId: "root",
    expanded: false,
    selected: false,
    disabled: false,
    children: []
  },
  {
    id: "6fb83657-2a41-4100-8193-5df10cce6e9e",
    name: "카테고리 05",
    order: 5,
    desc: "설명",
    parentId: "root",
    expanded: false,
    selected: false,
    disabled: false,
    children: [
      {
        id: "4911b731-8573-4b74-a082-4cb7aa9a0c2f",
        name: "카테고리 05 - 01",
        order: 1,
        desc: "설명",
        parentId: "6fb83657-2a41-4100-8193-5df10cce6e9e",
        expanded: false,
        selected: false,
        disabled: false,
        children: []
      }
    ]
  }
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
  children: []
});

const onNodeChecked = (froms: TreeViewItem[]) => {
  const ids = froms.map((from: TreeViewItem) => from.id);
  console.log(ids);
};

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
const dropValidator = (thisNode: TreeViewItem, targetNode: TreeViewItem, newNode: TreeViewItem): boolean => {
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

<style lang="scss" scoped></style>
