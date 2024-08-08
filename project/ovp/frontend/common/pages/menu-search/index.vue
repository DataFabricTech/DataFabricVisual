<template>
  <div class="sample-container">
    <div class="sample">
      <h1>메뉴 선택 - Tag 컴포넌트 예시</h1>
      <menu-search-tag
        :data="tag_sample_data"
        :selected-items="tag_selected_sample_data"
        label-key="name"
        value-key="id"
        :is-multi="true"
        title="값을 선택하세요"
        @multiple-change="changeTag"
      ></menu-search-tag>
    </div>
    <div class="sample">
      <h1>메뉴 선택 - 버튼 컴포넌트 예시</h1>
      <div class="button-container">
        <menu-search-button
          :data="sample_data"
          :selected-items="single_sample_selected_data"
          label-key="name"
          value-key="id"
          title="단일 선택"
          @single-change="changeSingle"
        ></menu-search-button>
        <menu-search-button
          :data="multiple_sample_data2"
          :selected-items="multiple_sample_selected_data"
          label-key="name"
          value-key="id"
          title="다중 선택"
          :is-multi="true"
          @multiple-change="changeMultiple"
        ></menu-search-button>
        <button class="button button-secondary" @click="onClickChangData">값 변경</button>
        <br />
        <menu-search-button
          :data="multiple_sample_data2"
          :selected-items="[]"
          label-key="name"
          value-key="id"
          title="버튼 클릭 이벤트"
          :is-multi="true"
          @open="onOpenMenuSearch"
          @multiple-change="changeMultiple"
        ></menu-search-button>
      </div>
    </div>
    <div class="sample">
      <h1>메뉴 선택 - Tree 컴포넌트 예시</h1>
      <div class="button-container">
        <menu-search-tree
          label-key="name"
          value-key="id"
          :data="items"
          :isCheckable="true"
          :hideGuideLines="false"
          :firExpandAll="true"
          :show-open-all-btn="false"
          :show-close-all-btn="false"
          :use-draggable="true"
          :selected-items="tree_selectedItem"
          mode="view"
          :dropValidator="dropValidator"
          @onItemChecked="onNodeChecked"
          @onItemSelected="onNodeClicked"
          @addSibling="addSibling"
          @addChild="addChild"
        ></menu-search-tree>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, Ref } from "vue";
import { TreeViewItem } from "~/components/extends/tree/TreeProps";

const sample_data: Ref<any> = ref([
  {
    id: "1",
    name: "AAA"
  },
  {
    id: "2",
    name: "BBB"
  },
  {
    id: "3",
    name: "CCC"
  },
  {
    id: "4",
    name: "DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD"
  }
]);
const multiple_sample_data2: Ref<any> = ref([
  {
    id: "1",
    name: "AAA"
  },
  {
    id: "2",
    name: "BBB"
  },
  {
    id: "3",
    name: "CCC"
  },
  {
    id: "4",
    name: "DDD"
  }
]);
const multiple_sample_new_data = [
  {
    id: "1",
    name: "QQQ"
  },
  {
    id: "2",
    name: "WWW"
  },
  {
    id: "3",
    name: "EEE"
  },
  {
    id: "4",
    name: "RRR"
  }
];
const tag_sample_data = [
  {
    id: "1",
    name: "ZZZ"
  },
  {
    id: "2",
    name: "XXX"
  },
  {
    id: "3",
    name: "CCC"
  },
  {
    id: "4",
    name: "VVV"
  }
];
const tag_selected_sample_data: Ref<any> = ref([
  {
    id: "1",
    name: "ZZZ"
  }
]);
const multiple_sample_selected_data: Ref<any> = ref([
  {
    id: "1",
    name: "AAA"
  },
  {
    id: "2",
    name: "BBB"
  },
  {
    id: "3",
    name: "CCC"
  }
]);

const single_sample_selected_data: Ref<any> = ref({
  id: "2",
  name: "BBB"
});

const changeMultiple: (value: any[] | {}) => void = (value) => {
  console.log("changeMultiple", value);
};

const onClickChangData: (value: any[] | {}) => void = (value) => {
  multiple_sample_selected_data.value = [];
  console.log("onClickChangData", value);
};
const onOpenMenuSearch: () => void = () => {
  multiple_sample_data2.value = multiple_sample_new_data;
  console.log("onClickChangData", multiple_sample_data2);
};

const changeSingle: (value: any[] | {}) => void = (value) => {
  console.log("changeSingle", value);
};
const changeTag: (value: any[] | {}) => void = (value) => {
  console.log("changeTag", value);
};

// Tree
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

const checkedIds: Ref<string[]> = ref<string[]>([]);
const tree_selectedItem: any[] = [
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
  }
];

const onNodeChecked = (checkedNodeIds: TreeViewItem[]) => {
  checkedIds.value = checkedNodeIds.map((node: TreeViewItem) => node.id);
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

<style scoped>
.sample {
  width: 360px;
  display: flex;
  flex-direction: column;
  gap: 16px;
  padding: 30px;
}

.button-container,
.tag-container {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.sample-container {
  display: flex;
  flex-direction: row;
  gap: 16px;
}
</style>
