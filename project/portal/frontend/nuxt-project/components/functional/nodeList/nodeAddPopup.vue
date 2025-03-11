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
            <li v-bind:class="{ active: currentTab == '데이터베이스' }" @click="changeTab1('데이터베이스')">
              <a>데이터베이스</a>
            </li>
            <li v-bind:class="{ active: currentTab == '직접입력' }" @click="changeTab1('직접입력')">
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
                        <option value="node_id">노드 ID</option>
                        <option value="node_name">노드 이름</option>
                      </select>
                    </div>
                    <div class="mu-input-icon">
                      <input type="text" v-model="searchWord" placeholder="Search" title="검색어입력" class="mu-input"
                        @keypress.enter.prevent="searchNode(0)" />
                      <button type="button" class="mu-btn mu-btn-sch" @click="searchNode(0)">
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
            <div class="board-list table-scroll h490 slim-scroll" ref="tableWrapper" @scroll="handleScroll">
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
                        <input type="checkbox" id="ckbAdd" title="전체선택" :checked="nodeList.length > 0 &&
                          selectedAddData.length === nodeList.length
                          " @click.stop="(e) => onToggleAddCheckbox(e, true, null)" />
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
                <tbody v-if="nodeList && nodeList.length > 0">
                  <tr v-for="(node, index) in nodeList" :key="index">
                    <td v-if="isNodeValid(node)">
                      <div class="mu-checkbox">
                        <input type="checkbox" title="행선택" :id="'chkAdd_' + index" :checked="isAddChecked(node)"
                          @click.stop="(e) => onToggleAddCheckbox(e, false, node)" />
                        <label :for="'chkAdd_' + index">
                          <span class="blind">행선택</span>
                        </label>
                      </div>
                    </td>
                    <td>
                      <span class="mu-ellipsis">
                        {{ node.meta_id }}
                      </span>
                    </td>
                    <td>
                      <span class="mu-ellipsis">
                        {{ node.meta_name }}
                      </span>
                    </td>
                    <td>
                      <div class="mu-tooltip-icon">
                        <i :id="'img' + index" class="mu-icon-svg view" @mouseover="showTooltip(index)"
                          @mouseleave="hideTooltip()"></i>
                        <div class="mu-tooltip arrow-ct w400" v-if="isTooltipVisible && hoveredAddIndex == index"
                          :style="{
                            position: 'fixed',
                            left: mouseX - 400 + 'px',
                            top: mouseY + 'px',
                          }">
                          <div class="mu-tooltip-inner">
                            <div class="data-info2">
                              <dl>
                                <dt>메타 명</dt>
                                <dd>{{ node.meta_name }}</dd>
                              </dl>
                              <dl>
                                <dt>메타 설명</dt>
                                <dd>{{ node.description ? node.description : '-' }}</dd>
                              </dl>
                              <dl>
                                <dt>DB 명</dt>
                                <dd>{{ node.qualified_name ? (node.qualified_name.split(".")[1] ?
                                  node.qualified_name.split(".")[1] : "-") : "-" }}</dd>
                              </dl>
                              <dl>
                                <dt>DB 종류</dt>
                                <dd>{{ node.system_type ? node.system_type : "-" }}</dd>
                              </dl>
                              <dl>
                                <dt>시스템 ID</dt>
                                <dd>{{ node.system_id ? node.system_id : "-" }}</dd>
                              </dl>
                              <dl>
                                <dt>시스템 명</dt>
                                <dd>{{ node.system_name ? node.system_name : "-" }}</dd>
                              </dl>
                            </div>
                          </div>
                        </div>
                      </div>
                    </td>
                  </tr>
                </tbody>
                <tbody v-else>
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
              <col style="width: 130px" />
              <col />
            </colgroup>
            <tbody>
              <tr>
                <th scope="row">노드 ID<strong class="required">필수</strong></th>
                <td>
                  <div class="mu-item-group">
                    <input type="text" v-model="add_node_id" placeholder="노드ID를 입력하세요." @input="resetCheckDuplicate"
                      class="mu-input" />
                    <button v-if="add_node_id && add_node_id.length > 0" type="button" class="mu-btn"
                      @click="searchSameThing()">
                      <span>중복확인</span>
                    </button>
                    <button v-else type="button" class="mu-btn" @click="createNewSystemId()">
                      <span>ID 생성</span>
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
                  <input type="text" value="" v-model="add_node_name" placeholder="노드이름을 입력하세요." class="mu-input" />
                </td>
              </tr>
              <tr>
                <th scope="row">DB 이름</th>
                <td>
                  <input type="text" value="" v-model="add_db_name" placeholder="DB 이름을 입력하세요." class="mu-input" />
                </td>
              </tr>
              <tr>
                <th scope="row">DB 종류</th>
                <td>
                  <div class="mu-selectbox">
                    <select v-model="add_db_type">
                      <option v-for="(dbname, idx) in dbList" :value="dbname">{{ dbname }}</option>
                    </select>
                  </div>
                </td>
              </tr>
              <tr>
                <th scope="row">테이블 설명<br />(데이터 설명)</th>
                <td>
                  <textarea name="" id="" cols="30" rows="6" class="mu-area h100" maxlength="50"
                    v-model="add_table_description" placeholder="테이블 설명을 입력하세요. (최대 50자)"></textarea>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
    <pagination v-show="currentTab == '데이터베이스'" :paging="pagingObj" @onClickPage="onClickPage" />

    <div class="mu-dialog-foot">
      <button v-if="props.addValue == 'from' && currentTab == '데이터베이스'" @click="AddFromNode()" type="button"
        class="mu-btn mu-btn-primary">
        <span>추가</span>
      </button>
      <button v-if="props.addValue == 'to' && currentTab == '데이터베이스'" @click="AddToNode()" type="button"
        class="mu-btn mu-btn-primary">
        <span>추가</span>
      </button>
      <button v-if="props.addValue == 'from' && currentTab == '직접입력'" @click="addSelfFromNode()"
        class="mu-btn mu-btn-primary" :disabled="!(!isDuplicate && add_node_id != '' && checkDuplicate)
          ">
        추가
      </button>
      <button v-if="props.addValue == 'to' && currentTab == '직접입력'" @click="addSelfToNode()"
        class="mu-btn mu-btn-primary" :disabled="!(!isDuplicate && add_node_id != '' && checkDuplicate)
          ">
        추가
      </button>
    </div>
    <!-- 접근성 : 내용을 읽고 나서 닫기 나중으로 빼준다  -->
    <div class="mu-dialog-close-btn">
      <button type="button" class="mu-btn mu-btn-close" title="해당 팝업 닫기" @click="onAddClose()">
        <i class="mu-icon-svg close"></i><span>팝업닫기</span>
      </button>
    </div>
  </div>
</template>
<script setup lang="ts">
import ObjectUtils from "@/utils/ObjectUtils";
import pagination from "@functional/pagination/pagination.vue";
import { onMounted, onUnmounted, ref, computed } from 'vue';
import { useNuxtApp } from "nuxt/app";
import { v4 as uuidv4 } from 'uuid';

// 페이징
const pagingObj = ref({
  total: 0, // 전체 데이터 건수
  limit: 10, // 한 페이지에 보여줄 데이터 건수
  block: 10, // 한 화면에서 보여줄 페이지 수
  currentPage: 1, // 현재 페이지 번호
});

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
const showMoreButton = ref<boolean>(false);
const add_node_id = ref<string>('');
const add_node_name = ref<string>('');
const add_db_name = ref<string>('');
const add_table_description = ref<string>('');
const isDuplicate = ref<boolean>(false);
const checkDuplicate = ref<boolean>(false);
const newListLength = ref<number>(1);
const dbList = ref(["Postgres"])
const add_db_type = ref<string>('Postgres');
const mouseX = ref<number>(0);
const mouseY = ref<number>(0);

const tableWrapper = ref();

onMounted(async () => {
  addClear()
  window.addEventListener("mousemove", handleMousemove);

  // DB 종류 조회
  await getDbType();
});

onUnmounted(() => window.removeEventListener("mousemove", handleMousemove));

watch(() => props.addValue, (newVal, oldVal) => {
  // 이전 노드 정보, 이후 노드 정보 추가 버튼 클릭 시 초기화
  if (newVal != oldVal) {
    addClear()
  }
})

const getDbType = async () => {
  await $ofetch(`/api/node/getDbList`).then((res) => {
    if (res && !res.errorMessage) {
      dbList.value = res.data
    }
  }).catch((err) => {
    console.log(err)
  });
}

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
  add_db_type.value = "Postgres";
  add_table_description.value = "";
  emit("close");
};

const addClear = () => {
  selectedAddData.value = [];
  add_node_id.value = "";
  add_node_name.value = "";
  add_db_name.value = "";
  add_db_type.value = "Postgres";
  add_table_description.value = "";
  nodeList.value = []
  pagingObj.value = {
    total: 0, // 전체 데이터 건수
    limit: 10, // 한 페이지에 보여줄 데이터 건수
    block: 10, // 한 화면에서 보여줄 페이지 수
    currentPage: 1, // 현재 페이지 번호
  }
};

const isNodeValid = async (node) => {
  if (node == undefined) {
    return true;
  } else {
    let id = ObjectUtils.getPropertyValue(node, "id", '');

    if (id !== '') {
      return id !== props.selectedId;
    } else {
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
  const matched = selectedAddData.value.find((el) => el.meta_id === node.meta_id);
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
      const targetIndex = newItems.findIndex((el) => el.meta_id === node.meta_id);
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

const changeTab1 = (param) => {
  currentTab.value = param;
};

const searchNode = async (offset = 0) => {
  const params = {
    selectedId: props.selectedId,
    offset: offset * 10,
    searchType: searchType.value,
    searchWord: searchWord.value,
  };

  await $ofetch(`/api/node/search`, {
    method: "POST",
    body: params,
  }).then((res) => {
    if (res && !res.errorMessage) {
      nodeList.value = ObjectUtils.getPropertyValue(res, "data/list", []);
      pagingObj.value = {
        ...pagingObj.value,
        total: ObjectUtils.getPropertyValue(res, "data/total", 0), // 테이블 총 카운트
        currentPage: offset + 1
      }
    }
  });
};

// 이전노드 추가 직접입력
const addSelfFromNode = () => {
  const selfInsertNode = {
    node_id: add_node_id.value,
    id: add_node_id.value,
    node_name: add_node_name.value,
    type_name: add_db_type.value,
    db_name: add_db_name.value,
    self: true,
    description: add_table_description.value,
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

// 이후노드 추가 직접입력
const addSelfToNode = () => {
  const selfInsertNode = {
    node_id: add_node_id.value,
    id: add_node_id.value,
    node_name: add_node_name.value,
    type_name: add_db_type.value,
    db_name: add_db_name.value,
    self: true,
    description: add_table_description.value,
  };

  // 데이터베이스
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

const onClickPage = (page) => {
  const goPage = typeof page === 'number' ? page : 1
  searchNode(goPage - 1)
}

// 시스템 ID 신규생성
const createNewSystemId = () => {
  add_node_id.value = uuidv4()
}

defineExpose({
  addClear
});
</script>
