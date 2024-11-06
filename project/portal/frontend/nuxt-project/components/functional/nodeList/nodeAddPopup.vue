<template>
  <div class="mu-dialog w700 h920">
    <div class="mu-dialog-head">
      <p class="title" v-if="props.addValue == 'from'">이전 노드 추가</p>
      <p class="title" v-if="props.addValue == 'to'">이후 노드 추가</p>
    </div>
    <div class="mu-dialog-body">
      <div class="tab-area">
        <div class="mu-tab-box sub-tab">
          <ul class="mu-tab">
            <li
              v-bind:class="{ active: currentTab == '데이터베이스' }"
              @click="changeTab1('데이터베이스')"
            >
              <a>데이터베이스</a>
            </li>
            <li
              v-bind:class="{ active: currentTab == '직접입력' }"
              @click="changeTab1('직접입력')"
            >
              <a>직접입력</a>
            </li>
          </ul>
        </div>
        <div>
          <div v-show="currentTab === '데이터베이스'" class="tab-content on">
            <div class="mu-heading">
              <h4>노드 찾기</h4>
            </div>
            <div class="board-list-search">
              <form action="">
                <fieldset>
                  <legend>게시물검색</legend>
                  <div class="inner-search">
                    <div class="mu-selectbox w120">
                      <select v-model="searchType">
                        <option value="all">전체</option>
                        <option value="__guid">노드 ID</option>
                        <option value="name">노드 이름</option>
                        <option value="">데이터 이름</option>
                        <option value="description">데이터 설명</option>
                      </select>
                    </div>
                    <div class="mu-input-icon">
                      <input
                        type="text"
                        v-model="searchWord"
                        placeholder="Search"
                        title="검색어입력"
                        class="mu-input"
                        @keypress.enter.prevent="searchNode()"
                      />
                      <button
                        type="button"
                        class="mu-btn mu-btn-sch"
                        @click="searchNode()"
                      >
                        <i class="mu-icon-svg search"></i>
                        <span>검색</span>
                      </button>
                    </div>
                  </div>
                </fieldset>
              </form>
            </div>
            <div class="mu-heading">
              <h4>노드 선택</h4>
            </div>
            <div
              class="board-list table-scroll h490 slim-scroll"
              ref="tableWrapper"
              @scroll="handleScroll"
            >
              <table class="mu-grid">
                <colgroup>
                  <col style="width: 40px" />
                  <col style="width: auto" span="2" />
                  <col style="width: 50px" />
                </colgroup>
                <thead>
                  <tr>
                    <th scope="col">
                      <div class="mu-checkbox">
                        <input
                          type="checkbox"
                          id="ckbAdd"
                          title="전체선택"
                          :checked="
                            nodeList.length > 0 &&
                            selectedAddData.length === nodeList.length
                          "
                          @click.stop="(e) => onToggleAddCheckbox(e, true)"
                        />
                        <label for="ckbAdd">
                          <span class="blind">전체선택</span>
                        </label>
                      </div>
                    </th>
                    <th scope="col">노드 ID</th>
                    <th scope="col">노드 이름</th>
                    <th scope="col">상세</th>
                  </tr>
                </thead>
                <tbody>                  
                  <tr
                    v-for="(node, index) in nodeList"
                    :key="index"
                    v-if="isNodeValid(node)" 
                  >
                    <td>
                      <div class="mu-checkbox">                        
                        <input
                          type="checkbox"
                          title="행선택"
                          :id="index"
                          :checked="isAddChecked(node)"
                          @click.stop="(e) => onToggleAddCheckbox(e, false, node)"
                        />
                        <label :for="index">
                          <span class="blind">행선택</span>
                        </label>
                      </div>
                    </td>
                    <td>
                      <span class="mu-ellipsis">
                        {{ node.guid }}
                      </span>
                    </td>
                    <td>
                      <span class="mu-ellipsis">
                        {{ node.displayText }}
                      </span>
                    </td>
                    <td>
                      <div class="mu-tooltip-icon">
                        <i
                          :id="'img' + index"
                          class="mu-icon-svg help"
                          @mouseover="showTooltip(index)"
                          @mouseleave="hideTooltip()"
                        ></i>
                        <div
                          class="mu-tooltip arrow-ct w250"
                          v-if="isTooltipVisible && hoveredAddIndex == index"
                          :style="{
                            position: 'fixed',
                            left: mouseX - 125 + 'px',
                            top: mouseY + 'px',
                          }"
                        >
                          <div class="mu-tooltip-inner">
                            <div class="data-info2">
                              <dl>
                                <dt>데이터 이름</dt>
                                <dd>{{ node.displayText }}</dd>
                              </dl>
                              <dl>
                                <dt>데이터 설명</dt>
                                <dd>{{ node.attributes.comment }}</dd>
                              </dl>
                              <dl>
                                <dt>데이터 유형</dt>
                                <dd>{{ node.typeName }}</dd>
                              </dl>
                            </div>
                          </div>
                        </div>
                      </div>
                    </td>
                  </tr>
                </tbody>
                <tbody v-if="nodeList.length == 0">
                  <tr>
                    <td colspan="4">
                      <p class="nodata">노드 정보가 없습니다.</p>
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>
        <div v-show="currentTab === '직접입력'" class="tab-content on">
          <table class="mu-formbox">
            <caption>
              노드 추가 게시판
            </caption>
            <colgroup>
              <col style="width: 120px" />
              <col />
            </colgroup>
            <tbody>
              <tr>
                <th scope="row">노드 ID<strong class="required">필수</strong></th>
                <td>
                  <div class="mu-item-group">
                    <input
                      type="text"
                      v-model="add_node_id"
                      placeholder="노드ID를 입력하세요."
                      @input="resetCheckDuplicate"
                      class="mu-input"
                    />
                    <button type="button" class="mu-btn" @click="searchSameThing()">
                      <span>중복확인</span>
                    </button>
                  </div>
                  <p v-if="isDuplicate" class="complete-msg error">
                    이미 존재하는 노드 ID 입니다.
                  </p>
                  <p v-if="add_node_id == ''" class="complete-msg error">
                    입력된 노드 ID가 없습니다.
                  </p>
                  <p v-if="!checkDuplicate" class="complete-msg error">
                    노드 ID 중복확인이 필요합니다.
                  </p>
                </td>
              </tr>
              <tr>
                <th scope="row">노드 이름</th>
                <td>
                  <input
                    type="text"
                    value=""
                    v-model="add_node_name"
                    placeholder="노드이름을 입력하세요."
                    class="mu-input"
                  />
                </td>
              </tr>
              <tr>
                <th scope="row">DB 이름</th>
                <td>
                  <input
                    type="text"
                    value=""
                    v-model="add_db_name"
                    placeholder="DB 이름을 입력하세요."
                    class="mu-input"
                  />
                </td>
              </tr>
              <tr>
                <th scope="row">DB 종류</th>
                <td>
                  <div class="mu-selectbox">
                    <select v-model="add_db_type">
                      <option value="hive_table">hive_table</option>
                    </select>
                  </div>
                </td>
              </tr>
              <tr>
                <th scope="row">테이블 설명<br />(데이터 설명)</th>
                <td>
                  <textarea
                    name=""
                    id=""
                    cols="30"
                    rows="6"
                    class="mu-area h100"
                    maxlength="50"
                    v-model="add_table_description"
                    placeholder="테이블 설명을 입력하세요. (최대 50자)"
                  ></textarea>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
    <div class="mu-dialog-foot">
      <button
        v-if="props.addValue == 'from' && currentTab == '데이터베이스'"
        @click="AddFromNode()"
        type="button"
        class="mu-btn mu-btn-primary"
      >
        <span>추가</span>
      </button>
      <button
        v-if="props.addValue == 'to' && currentTab == '데이터베이스'"
        @click="AddToNode()"
        type="button"
        class="mu-btn mu-btn-primary"
      >
        <span>추가</span>
      </button>
      <button
        v-if="props.addValue == 'from' && currentTab == '직접입력'"
        @click="addSelfFromNode()"
        class="mu-btn mu-btn-primary"
        :disabled="
          !(!isDuplicate && add_node_id != '' && checkDuplicate)
        "
      >
        추가
      </button>
      <button
        v-if="props.addValue == 'to' && currentTab == '직접입력'"
        @click="addSelfToNode()"
        class="mu-btn mu-btn-primary"
        :disabled="
          !(!isDuplicate && add_node_id != '' && checkDuplicate)
        "
      >
        추가
      </button>
      <div v-if="isLoadingMore">로딩 중...</div>
      <button v-if="showMoreButton" @click="handleMoreButtonClick">더보기</button>
    </div>
    <!-- 접근성 : 내용을 읽고 나서 닫기 나중으로 빼준다  -->
    <div class="mu-dialog-close-btn">
      <button
        type="button"
        class="mu-btn mu-btn-close"
        title="해당 팝업 닫기"
        @click="onAddClose()"
      >
        <i class="mu-icon-svg close"></i><span>팝업닫기</span>
      </button>
    </div>
  </div>
</template>
<script setup lang="ts">
import ObjectUtils from "@/utils/ObjectUtils";
import { onBeforeMount, onMounted, onUnmounted, ref, computed } from 'vue';
import { useNuxtApp } from "nuxt/app";

const { $ofetch } = useNuxtApp();

const emit = defineEmits([
  "close",
  "AddFromNode",
  "AddToNode",
  "addSelfFromNode",
  "addSelfToNode",
]);
const props = defineProps(["addValue", "selectedId", "selectedName"]);

const selectedAddData = ref<any>([]);
const hoveredAddIndex = ref<number>(-1);;
const isTooltipVisible = ref<boolean>(false);
const nodeList = ref<any>([]);
const currentTab = ref<string>('데이터베이스');
const searchType = ref<string>('all');
const searchWord = ref<string>("");
const isLoadingMore = ref<boolean>(false);
const showMoreButton = ref<boolean>(false);
const count = ref<number>(0);
const add_node_id = ref<string>('');
const add_node_name = ref<string>('');
const add_db_name = ref<string>('');
const add_table_description = ref<string>('');
const isDuplicate = ref<boolean>(false);
const checkDuplicate = ref<boolean>(false);
const newListLength = ref<number>(1);
const add_db_type = ref<string>('hive_table');
const tooltipStyle = ref<any>(null);
const mouseX = ref<number>(0);
const mouseY = ref<number>(0);
const checkSelectIdCount = ref<number>(0);

const tableWrapper = ref();

onMounted(() => {
  window.addEventListener("mousemove", handleMousemove);  
});

onUnmounted(() => window.removeEventListener("mousemove", handleMousemove));

//마우스 위치
const handleMousemove = (event) => {
  mouseX.value = event.clientX;
  mouseY.value = event.clientY + 10;
};

const resetCheckDuplicate = () => {
  checkDuplicate.value = false; // checkDuplicate를 false로 재설정
};

//모달창 닫기시
const onAddClose = () => {
  selectedAddData.value = [];
  add_node_id.value = "";
  add_node_name.value = "";
  add_db_name.value = "";
  add_db_type.value = "";
  add_table_description.value = "";
  emit("close");
};

const addClear = () => {
  selectedAddData.value = [];
  add_node_id.value = "";
  add_node_name.value = "";
  add_db_name.value = "";
  add_db_type.value = "";
  add_table_description.value = "";
};

const isNodeValid = async (node) => {  
  if(node == undefined){
    return true;
  }else{
    let guid = ObjectUtils.getPropertyValue(node, "guid", ''); 

    if(guid !== ''){
      return guid !== props.selectedId;
    }else{
      return true;
    }
  }
}

//이전 노드 추가 이벤트 핸들러
const AddFromNode = async () => {
  emit("AddFromNode", selectedAddData.value);
};

//이후 노드 추가 이벤트 핸들러
const AddToNode = async () => {
  emit("AddToNode", selectedAddData.value);
};

const isAddChecked = (node) => {
  const matched = selectedAddData.value.find((el) => el.guid === node.guid);
  return matched;
};

const onToggleAddCheckbox = (e, isAll, node) => {
  const {
    target: { checked },
  } = e;
  let newItems = [...selectedAddData.value];
  if (isAll) {
    // 테이블 최상단 체크박스 클릭 시 전체 선택/해제
    newItems = checked ? nodeList.value : [];
  } else {
    // 각 행별 체크박스 클릭 시
    if (checked) {
      newItems.push(node);
    } else {
      const targetIndex = newItems.findIndex((el) => el.guid === node.guid);
      newItems.splice(targetIndex, 1);
    }
  }
  selectedAddData.value = newItems;
};

const showTooltip = (index) => {
  isTooltipVisible.value = true;
  hoveredAddIndex.value = index;
};

const hideTooltip = () => {
  isTooltipVisible.value = false;
  hoveredAddIndex.value = null;
};

const handleMoreButtonClick = async () => {
  // 추가 데이터 로딩 중이면 중복 요청 방지
  if (isLoadingMore.value) {
    return;
  }
  // 추가 데이터 로딩 중 표시
  isLoadingMore.value = true;

  const params = {
    offset: count.value,
    searchType: searchType.value,
    searchWord: searchWord.value,
  };

  // 서버에 추가 데이터 요청
  const result = await $ofetch(`/api/node/search`, {
    method: "POST",
    body: params,
  });

  const list = ObjectUtils.getPropertyValue(result, "data/entities", []); // 테이블 데이터

  newListLength.value = list.length;

  // 받아온 데이터 리스트에 추가
  nodeList.value = [...nodeList.value, ...list];
  // 추가 데이터 로딩 완료
  isLoadingMore.value = false;
  // 현재 데이터 개수 업데이트
  count.value = nodeList.value.length;
  // 더보기 버튼 표시 여부 업데이트
  showMoreButton.value = false;
};

const changeTab1 = (param) => {
  currentTab.value = param;
};

const searchNode = async (offset = 0) => {
  checkSelectIdCount.value = 0;
  const params = {
    offset,
    searchType: searchType.value,
    searchWord: searchWord.value,
  };
  const result = await $ofetch(`/api/node/search`, {
    method: "POST",
    body: params,
  });

  const list = ObjectUtils.getPropertyValue(result, "data/entities", []); // 테이블 데이터

  nodeList.value = list;

  count.value = list.length;
};

searchNode();

const addSelfFromNode = () => {
  const selfInsertNode = {
    node_id: add_node_id.value,
    guid: add_node_id.value,
    displayText: add_node_name.value,
    typeName: add_db_type.value,
    dbName: add_db_name.value,
    self: true,
    // description: add_table_description.value,
    attributes: {
      comment: add_table_description.value,
    },
  };

  const selfNode = {
    from_node_id: add_node_id.value,
    from_node_name: add_node_name.value,
    link_id: null,
    to_node_id: props.selectedId,
    to_node_name: props.selectedName,
    tooltip: selfInsertNode,
  };

  const data = {
    selfInsertNode,
    selfNode,
  };
  emit("addSelfFromNode", data);
};

const addSelfToNode = () => {
  const selfInsertNode = {
    node_id: add_node_id.value,
    guid: add_node_id.value,
    displayText: add_node_name.value,
    typeName: add_db_type.value,
    dbName: add_db_name.value,
    self: true,
    //  description: add_table_description.value,
    attributes: {
      comment: add_table_description.value,
    },
  };

  const selfNode = {
    from_node_id: props.selectedId,
    from_node_name: props.selectedName,
    link_id: null,
    to_node_id: add_node_id.value,
    to_node_name: add_node_name.value,
    tooltip: selfInsertNode,
  };

  const data = {
    selfInsertNode,
    selfNode,
  };

  emit("addSelfToNode", data);
};

const searchSameThing = async () => {
  const result = await $ofetch(`/api/node/check`, {
    params: {
      data: add_node_id.value, // 중복을 체크할 데이터
    },
  });

  isDuplicate.value = result;
  checkDuplicate.value = true;
};

const handleScroll = () => {
  const wrapper = tableWrapper.value;
  if (Math.abs(wrapper.scrollTop + wrapper.clientHeight - wrapper.scrollHeight) < 1) {
    showMoreButton.value = true;
    if (newListLength.value == 0) {
      showMoreButton.value = false;
    }
  } else {
    showMoreButton.value = false;
  }
};

defineExpose({
  addClear
});
</script>
