<template>
  <div class="mu-modal" v-if="props.toggle">
    <div class="mu-dialog-background"></div>
    <!-- 모달팝업 : 데이터흐름수정 -->
    <div class="mu-dialog w1100 h920">
      <div class="mu-dialog-head">
        <p class="title">데이터 흐름 수정</p>
      </div>
      <div class="mu-dialog-body">
        <div class="board-list-wrap">
          <div class="mu-heading">
            <h4>현재 선택된 데이터 노드 정보</h4>
          </div>
          <div class="board-list">
            <table class="mu-grid">
              <colgroup>
                <col style="width: auto" span="3" />
              </colgroup>
              <thead>
                <tr>
                  <th scope="col">노드 ID</th>
                  <th scope="col">노드 이름</th>
                  <th scope="col">노드 선택</th>
                </tr>
              </thead>
              <tbody>
                <tr>
                  <td>{{ id }}</td>
                  <td>{{ name }}</td>
                  <td>
                    <button
                      type="button"
                      class="mu-btn mu-btn-sm"
                      @click="selectEntity()"
                    >
                      <span>선택</span>
                    </button>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
          <div class="mu-heading">
            <h4>현재 선택된 노드 정보</h4>
          </div>
          <div class="board-list">
            <table class="mu-grid">
              <colgroup>
                <col style="width: auto" span="2" />
              </colgroup>
              <thead>
                <tr>
                  <th scope="col">노드 ID</th>
                  <th scope="col">노드 이름</th>
                </tr>
              </thead>
              <tbody>
                <tr>
                  <td>{{ selectedId ? selectedId : "-" }}</td>
                  <td>{{ selectedName ? selectedName : "-" }}</td>
                </tr>
              </tbody>
            </table>
          </div>
          <div class="mu-row gap-30">
            <nodeList
              :NodeLinks="fromNodeLinks"
              @addModalShow="addFromModalShow"
              @deleteNodelinks="deleteFromNodelinks"
              @selectEntity="selectFromEntity"
              @refreshbtn="fromRefreshbtn"
              nodeField="from"
              :selectedId="selectedId"
            />
            <nodeList
              :NodeLinks="toNodeLinks"
              @addModalShow="addToModalShow"
              @deleteNodelinks="deleteToNodelinks"
              @selectEntity="selectToEntity"
              @refreshbtn="toRefreshbtn"
              node-field="to"
              :selectedId="selectedId"
            />
          </div>
        </div>
      </div>
      <div class="mu-dialog-foot">
        <button type="button" class="mu-btn" @click="onClose">
          <span>취소</span>
        </button>
        <button
          type="button"
          class="mu-btn mu-btn-primary"
          @click="onClickConfirm"
          :disabled="isValid"
        >
          <span>저장</span>
        </button>
      </div>
      <!-- 접근성 : 내용을 읽고 나서 닫기 나중으로 빼준다 -->
      <div class="mu-dialog-close-btn">
        <button
          type="button"
          class="mu-btn mu-btn-close"
          title="해당 팝업 닫기"
          @click="onClose"
        >
          <i class="mu-icon-svg close"></i>
          <span>팝업닫기</span>
        </button>
      </div>
    </div>
    <NodeAddPopup
      v-if="addShow"
      @close="onAddClose"
      :addValue="addValue"
      :selectedId="selectedId"
      :selectedName="selectedName"
      @AddFromNode="AddFromNode"
      @AddToNode="AddToNode"
      @addSelfFromNode="addSelfFromNode"
      @addSelfToNode="addSelfToNode"
    />
  </div>
</template>

<script setup lang="ts">
import ObjectUtils from "@/utils/ObjectUtils";
import { confirmAlert, errorAlert } from "@functional/alert/alert-default";
import nodeList from "@functional/nodeList/nodeList";
import NodeAddPopup from "@functional/nodeList/nodeAddPopup";
import { ref, computed } from 'vue';
import { useNuxtApp } from "nuxt/app";
import { useEventStore  } from "~/store/event.ts";

const { $ofetch } = useNuxtApp();
const eventStore = useEventStore();

const emit = defineEmits(["onCloseModal", "onChangeLineage"]);
const props = defineProps(["toggle", "entity", "userDataParam"]);
const nodeLinks = ref([]);
const toNodeLinks = ref([]);
const fromNodeLinks = ref([]);
const originalNodeLinks = ref([]);
const selectedFromData = ref([]);
const selectedToData = ref([]);
const selectedAddData = ref([]);
const guid = ref("");
const selectedIdData = ref("");
const selectedNameData = ref("");
const addShow = ref<boolean>(false);
const addValue = ref("");
const fromOriginalNodeLinks = ref([]);
const toOriginalNodeLinks = ref([]);
const add_self_list = ref([]);

// const nodeAddPopup = ref<InstanceType<typeof NodeAddPopup> | null>(null);

const id = computed(() => ObjectUtils.getPropertyValue(props.entity, "meta_id", ""));
const name = computed(() =>
  ObjectUtils.getPropertyValue(props.entity, "meta_name", "")
);
const selectedId = computed(() => selectedIdData.value);
const selectedName = computed(() => selectedNameData.value);
const isValid = computed(() => {
  const deleted = getDeletedNodeLinks();
  const inserted = getInsertedNodeLinks();
  return deleted.length == 0 && inserted.length == 0;
});

watch(
  () => props.toggle,
  (newVal, oldVal) => {
    if (newVal && !oldVal) {
      // 모달 open 시점에 조회 API 호출
      if (newVal) {
        selectEntity();
      }
    }
  }
);
//이전 목록 다시불러오기
const fromRefreshbtn = async () => {
  if (guid.value) {
    const result = await $ofetch(`/api/node/${guid.value}`);
    const links = ObjectUtils.getPropertyValue(result, "data/links", []);
    fromNodeLinks.value = links.filter((link) => link.to_node_id === selectedId.value);

    for (const element of fromNodeLinks.value) {
      let guid = element.from_node_id;

      const params = {
        guid: guid,
      };

      const tooltipList = await $ofetch(`/api/node/tooltip`, {
        method: "POST",
        body: params,
      });

      const tooltip = ObjectUtils.getPropertyValue(tooltipList, "data", []); // 테이블 데이터

      element.tooltip = tooltip;

      if (!element.tooltip) {
        let dbTooltipList = await $ofetch(`/api/node/detail/tooltip/${guid}`);

        let dataTooltiplist = ObjectUtils.getPropertyValue(dbTooltipList, "data", []);

        element.tooltip = dataTooltiplist[0];
      }
    }
  }
};

//이후 목록 다시불러오기
const toRefreshbtn = async () => {
  if (guid.value) {
    const result = await $ofetch(`/api/node/${guid.value}`);
    const links = ObjectUtils.getPropertyValue(result, "data/links", []);
    toNodeLinks.value = links.filter((link) => link.from_node_id === selectedId.value);

    for (const element of toNodeLinks.value) {
      let guid = element.to_node_id;

      const params = {
        guid: guid,
      };
      const tooltipList = await $ofetch(`/api/node/tooltip`, {
        method: "POST",
        body: params,
      });

      const tooltip = ObjectUtils.getPropertyValue(tooltipList, "data", []); // 테이블 데이터

      element.tooltip = tooltip;

      if (!element.tooltip) {
        let dbTooltipList = await $ofetch(`/api/node/detail/tooltip/${guid}`);

        let dataTooltiplist = ObjectUtils.getPropertyValue(dbTooltipList, "data", []);

        element.tooltip = dataTooltiplist[0];
      }
    }
  }
};

//이전 노드 추가 모달창 보여주기
const addFromModalShow = async () => {
  // if (addShow.value && NodeAddPopup)
  //   NodeAddPopup.addClear();

  addValue.value = "from";
  addShow.value = true;
};

//이후 노드 추가 모달창 보여주기
const addToModalShow = async () => {
  // if (addShow.value && NodeAddPopup)
  //   NodeAddPopup.addClear();

  addValue.value = "to";
  addShow.value = true;
};
//이전 노드 추가 이벤트 핸들러
const AddFromNode = async (selectedAddData) => {
  const promiseList = [];
  for (const element of selectedAddData) {
    const params = {
      guid: element.meta_id,
    };
    const tooltipPromise = $ofetch(`/api/node/tooltip`, {
      method: "POST",
      body: params,
    });

    promiseList.push(tooltipPromise);
  }
  const responseList = await Promise.all(promiseList);
  for (let j = 0; j < responseList.length; j++) {
    const tooltip = ObjectUtils.getPropertyValue(responseList[j], "data", {});
    const node = {
      from_node_id: selectedAddData[j].meta_id,
      from_node_name: selectedAddData[j].meta_name,
      link_id: null,
      to_node_id: selectedId.value,
      to_node_name: selectedName.value,
      tooltip,
    };
    fromNodeLinks.value.push(node);
  }
  fromNodeLinks.value = Object.values(
    fromNodeLinks.value.reduce((acc, link) => {
      const fromNodeId = link.from_node_id;
      if (!acc[fromNodeId] || acc[fromNodeId].node_id !== link.node_id) {
        acc[fromNodeId] = link;
      }
      return acc;
    }, {})
  );
};
//이후 노드 추가 이벤트 핸들러
const AddToNode = async (selectedAddData) => {
  const promiseList = [];
  for (const element of selectedAddData) {
    const params = {
      guid: element.meta_id,
    };
    const tooltipPromise = $ofetch(`/api/node/tooltip`, {
      method: "POST",
      body: params,
    });

    promiseList.push(tooltipPromise);
  }
  const responseList = await Promise.all(promiseList);
  for (let j = 0; j < responseList.length; j++) {
    const tooltip = ObjectUtils.getPropertyValue(responseList[j], "data", {});
    const node = {
      from_node_id: selectedId.value,
      from_node_name: selectedName.value,
      link_id: null,
      to_node_id: selectedAddData[j].meta_id,
      to_node_name: selectedAddData[j].meta_name,
      tooltip,
    };
    toNodeLinks.value.push(node);
  }
  toNodeLinks.value = Object.values(
    toNodeLinks.value.reduce((acc, link) => {
      const toNodeId = link.to_node_id;
      if (!acc[toNodeId] || acc[toNodeId].node_id !== link.node_id) {
        acc[toNodeId] = link;
      }
      return acc;
    }, {})
  );
};
//선택 이벤트 핸들러
const selectEntity = () => {
  selectedIdData.value = id.value;
  selectedNameData.value = name.value;
  guid.value = id.value;
  getNodeLinks();
};
//선택 이벤트 핸들러
const selectFromEntity = (node) => {
  selectedIdData.value = node.from_node_id;
  selectedNameData.value = node.from_node_name;
  guid.value = node.from_node_id;
  getNodeLinks();
};
//선택 이벤트 핸들러
const selectToEntity = (node) => {
  selectedIdData.value = node.to_node_id;
  selectedNameData.value = node.to_node_name;
  guid.value = node.to_node_id;
  getNodeLinks();
};
// 체크박스 체크 표시 여부 계산
const isFromChecked = (node) => {
  const matched = selectedFromData.value.find(
    (el) => el.from_node_id === node.from_node_id
  );
  return matched;
};
// 체크박스 체크 표시 여부 계산
const isToChecked = (node) => {
  const matched = selectedToData.value.find((el) => el.to_node_id === node.to_node_id);
  return matched;
};
// 체크박스 클릭 이벤트 핸들러
const onToggleFromCheckbox = (e, isAll, node) => {
  const {
    target: { checked },
  } = e;
  let newItems = [...selectedFromData.value];
  if (isAll) {
    // 테이블 최상단 체크박스 클릭 시 전체 선택/해제
    newItems = checked ? fromNodeLinks.value : [];
  } else {
    // 각 행별 체크박스 클릭 시
    if (checked) {
      newItems.push(node);
    } else {
      const targetIndex = newItems.findIndex(
        (el) => el.from_node_id === node.from_node_id
      );
      newItems.splice(targetIndex, 1);
    }
  }
  selectedFromData.value = newItems;
};
// 체크박스 클릭 이벤트 핸들러
const onToggleToCheckbox = (e, isAll, node) => {
  const {
    target: { checked },
  } = e;
  let newItems = [...selectedToData.value];
  if (isAll) {
    // 테이블 최상단 체크박스 클릭 시 전체 선택/해제
    newItems = checked ? toNodeLinks.value : [];
  } else {
    // 각 행별 체크박스 클릭 시
    if (checked) {
      newItems.push(node);
    } else {
      const targetIndex = newItems.findIndex((el) => el.to_node_id === node.to_node_id);
      newItems.splice(targetIndex, 1);
    }
  }
  selectedToData.value = newItems;
};
// 데이터 흐름을 구성하는 node link 목록 조회
const getNodeLinks = async () => {
  if (guid.value) {
    const result = await $ofetch(`/api/node/${guid.value}`);

    const links = ObjectUtils.getPropertyValue(result, "data/links", []);

    nodeLinks.value = links;
    toNodeLinks.value = links.filter((link) => link.from_node_id === selectedId.value);
    fromNodeLinks.value = links.filter((link) => link.to_node_id === selectedId.value);

    for (const element of fromNodeLinks.value) {
      let guid = element.from_node_id;

      const params = {
        guid: guid,
      };

      const tooltipList = await $ofetch(`/api/node/tooltip`, {
        method: "POST",
        body: params,
      });

      const tooltiplist = ObjectUtils.getPropertyValue(tooltipList, "data", []); // 테이블 데이터

      element.tooltip = tooltiplist[0];

      if (!element.tooltip) {
        let dbTooltipList = await $ofetch(`/api/node/detail/tooltip/${guid}`);
        let dataTooltiplist = ObjectUtils.getPropertyValue(dbTooltipList, "data", []);

        element.tooltip = dataTooltiplist[0];
      }
    }

    for (const element of toNodeLinks.value) {
      let guid = element.to_node_id;

      const params = {
        guid: guid,
      };

      const tooltipList = await $ofetch(`/api/node/tooltip`, {
        method: "POST",
        body: params,
      });

      const tooltiplist = ObjectUtils.getPropertyValue(tooltipList, "data", []); // 테이블 데이터

      element.tooltip = tooltiplist[0];

      if (!element.tooltip) {
        let dbTooltipList = await $ofetch(`/api/node/detail/tooltip/${guid}`);
        let dataTooltiplist = ObjectUtils.getPropertyValue(dbTooltipList, "data", []);

        element.tooltip = dataTooltiplist[0];
      }
    }

    originalNodeLinks.value = JSON.parse(JSON.stringify(links));
    toOriginalNodeLinks.value = JSON.parse(JSON.stringify(toNodeLinks.value));
    fromOriginalNodeLinks.value = JSON.parse(JSON.stringify(fromNodeLinks.value));
  }
};

//toNodeLinks에서 선택된 데이터 삭제
const deleteToNodelinks = async (selectedData) => {
  toNodeLinks.value = toNodeLinks.value.filter((link) => !selectedData.includes(link));
};

const deleteFromNodelinks = async (selectedData) => {
  fromNodeLinks.value = fromNodeLinks.value.filter(
    (link) => !selectedData.includes(link)
  );
};

// 삭제된 항목 추출
const getDeletedNodeLinks = () => {
  const Deletelist = [];
  for (const fromOriginalNodeLink of fromOriginalNodeLinks.value) {
    const foundIndex = fromNodeLinks.value.findIndex(
      (link) => link.link_id === fromOriginalNodeLink.link_id
    );
    if (foundIndex === -1) {
      Deletelist.push(fromOriginalNodeLink);
    }
  }
  for (const originalLink of toOriginalNodeLinks.value) {
    const foundIndex = toNodeLinks.value.findIndex(
      (link) => link.link_id === originalLink.link_id
    );
    if (foundIndex === -1) {
      Deletelist.push(originalLink);
    }
  }

  return Deletelist;
};

//추가된 항목 추출
const getInsertedNodeLinks = () => {
  const Addlist = [];
  for (const link of fromNodeLinks.value) {
    const foundIndex = fromOriginalNodeLinks.value.findIndex(
      (originalLink) => originalLink.link_id === link.link_id
    );
    if (foundIndex === -1) {
      Addlist.push(link);
    }
  }
  for (const link of toNodeLinks.value) {
    const foundIndex = toOriginalNodeLinks.value.findIndex(
      (originalLink) => originalLink.link_id === link.link_id
    );
    if (foundIndex === -1) {
      Addlist.push(link);
    }
  }
  return Addlist;
};

//확인 버튼 이벤트 핸들러
const onClickConfirm = async () => {
  if (await confirmAlert("현재 리니지 정보를 저장합니다. 정말 저장 하시겠습니까?")) {
    try {
      const deletedLinks = getDeletedNodeLinks(); // 삭제할 리니지 정보
      const insertedLinks = getInsertedNodeLinks(); // 추가할 리니지 정보
      const basicResult = await $ofetch("/api/node/check", { // meta_id 중복체크
        params: { data: id.value },
      });

      if (!basicResult) {
        const params = {
          node_id: props.userDataParam.guid,
          node_name: props.userDataParam.meta_name,
          type_name: props.userDataParam.type,
          description: props.userDataParam.comment,
          qualified_name: props.userDataParam.qualified_name,
          db_name: props.userDataParam.qualified_name.split(".")[1],
        };
        await $ofetch("/api/node/self", {
          method: "PUT",
          body: params,
        });
      }

      if (deletedLinks.length > 0) {
        await Promise.all(
          deletedLinks.map((link) =>
            $ofetch(`/api/node/${link.link_id}`, {
              method: "DELETE",
            })
          )
        );
      }

      if (insertedLinks.length > 0) {
        await Promise.all(
          insertedLinks.map((link) =>
            $ofetch("/api/node/insert", {
              method: "POST",
              body: link,
            })
          )
        );
      }

      if (deletedLinks.length <= 0 && insertedLinks.length <= 0) {
        errorAlert("수정된 데이터가 없습니다.");
      } else {
        onChangeNodesInfo();
      }
    } catch (error) {
      console.error(error);
      errorAlert("오류가 발생하여 저장에 실패하였습니다.");
    }
  }
};

// 데이터 흐름도 영역 재구성
const onChangeNodesInfo = async () => {
  // await emit("onChangeLineage");
  eventStore.emit('onChangeLineage');
  emit("onCloseModal");
  onClear();
};

// state 초기화
const onClear = () => {
  originalNodeLinks.value = [];
  nodeLinks.value = [];
  toOriginalNodeLinks.value = [];
  toNodeLinks.value = [];
  fromOriginalNodeLinks.value = [];
  fromNodeLinks.value = [];
  selectedAddData.value = [];
  addShow.value = false;
  selectedIdData.value = "";
  selectedNameData.value = "";
  // add_node_id.value = "";
  // add_node_name.value = "";
  // add_db_name.value = "";
  add_self_list.value = [];
};

const addClear = () => {
  // add_node_id.value = "";
  // add_node_name.value = "";
  // add_db_name.value = "";
  add_self_list.value = [];
  selectedAddData.value = [];
};

// 모달 닫기 이벤트 핸들러
const onClose = async () => {
  if (
    JSON.stringify(toNodeLinks.value) !== JSON.stringify(toOriginalNodeLinks.value) ||
    JSON.stringify(fromNodeLinks.value) !== JSON.stringify(fromOriginalNodeLinks.value)
  ) {
    if (await confirmAlert("저장되지 않은 정보가 있습니다. 창을 닫으시겠습니까?")) {
      onClear();
      selectedFromData.value = [];
      selectedToData.value = [];
      selectedIdData.value = "";
      selectedNameData.value = "";
      emit("onCloseModal");
    }
  } else {
    onClear();
    selectedFromData.value = [];
    selectedToData.value = [];
    selectedIdData.value = "";
    selectedNameData.value = "";
    emit("onCloseModal");
  }
};
//노드 추가 모달창 닫기
const onAddClose = () => {
  selectedAddData.value = [];
  addShow.value = false;
};
//이전노드추가 직접입력
const addSelfFromNode = (data) => {
  fromNodeLinks.value.push(data.selfNode);

  add_self_list.value.push(data.selfInsertNode);

  add_self_list.value = Object.values(
    add_self_list.value.reduce((acc, link) => {
      const NodeId = link.node_id;
      if (!acc[NodeId] || acc[NodeId].node_id !== link.node_id) {
        acc[NodeId] = link;
      }
      return acc;
    }, {})
  );

  fromNodeLinks.value = Object.values(
    fromNodeLinks.value.reduce((acc, link) => {
      const fromNodeId = link.from_node_id;
      if (!acc[fromNodeId] || acc[fromNodeId].node_id !== link.node_id) {
        acc[fromNodeId] = link;
      }
      return acc;
    }, {})
  );
};
//이후노드추가 직접입력
const addSelfToNode = (data) => {
  toNodeLinks.value.push(data.selfNode);

  add_self_list.value.push(data.selfInsertNode);

  add_self_list.value = Object.values(
    add_self_list.value.reduce((acc, link) => {
      const NodeId = link.node_id;
      if (!acc[NodeId] || acc[NodeId].node_id !== link.node_id) {
        acc[NodeId] = link;
      }
      return acc;
    }, {})
  );

  toNodeLinks.value = Object.values(
    toNodeLinks.value.reduce((acc, link) => {
      const toNodeId = link.to_node_id;
      if (!acc[toNodeId] || acc[toNodeId].node_id !== link.node_id) {
        acc[toNodeId] = link;
      }
      return acc;
    }, {})
  );
};
</script>
