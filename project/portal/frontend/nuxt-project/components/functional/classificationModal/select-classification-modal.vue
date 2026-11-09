<template>
  <div class="mu-modal" v-if="props.toggleSelectCategoryModal">
    <div class="mu-dialog-background"></div>
    <div class="mu-dialog w500">
      <div class="mu-dialog-head">
        <p class="title">카테고리 정보</p>
      </div>
      <div class="mu-dialog-body">
        <div class="filter-wrap">
          <div class="mu-search-group" style="padding: 10px;">
            <div class="mu-search-item" style="padding: 0px;">
              <table>
                <caption>카테고리 검색 게시판</caption>
                <colgroup>
                  <col style="width: 80px" />
                  <col />
                </colgroup>
                <tbody>
                  <tr>
                    <th scope="row">카테고리 명</th>
                    <td>
                      <input type="text" placeholder="검색어를 입력하세요." class="mu-input w200" title="검색어입력" v-model="word"
                        @keypress.enter.prevent="onSearch" />
                      <button type="button" class="mu-btn mu-btn-primary" @click.prevent="onSearch"
                        style="margin-left:10px">
                        <i class="mu-icon-svg search"></i><span>검색</span>
                      </button>
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
            <div class="mu-search-btn"></div>
          </div>

          <div class="mu-heading" style="justify-content: space-between;margin-top: auto;">
            <h4>카테고리 목록</h4>
            <div class="detail-section" :class="showData ? 'open' : 'close'" style="height: auto;">
              <div class="mu-item-group">
                <button type="button" class="mu-btn mu-btn-bg-non btn-section-toggle" :title="toggleShowDataText"
                  @click="toggleShowData()" style="margin-left: 10px">
                  <span>{{ toggleShowDataText }}</span>
                </button>
              </div>
            </div>
          </div>

          <div class="category-tree" style="overflow: auto; border: 1px solid #cecece;height: 400px">
            <ul class="category-menu slim-scroll" style="padding: 10px;">
              <li :class="[toggleTargets.all ? 'open' : '', 'has-sub']">
                <button type="button" :class="[toggleTargets.all ? 'on' : '', 'item-toggle']" @click="onToggleAll">
                  <span>펼치기</span>
                </button>
                <a href="javascript:void(0);" :class="['' === selectedId ? 'active' : '', 'item-label']" title="카테고리 전체"
                  @click="() => onClick('all')">
                  카테고리 전체
                </a>
                <category-item v-if="toggleTargets.all" :list="searchedCategoryList" :toggle-targets="toggleTargets"
                  :selected-id="selectedId" @onToggleNode="onToggle" @onClickMenuItem="onClick" />
              </li>
            </ul>
          </div>

          <table class="mu-formbox mt20">
            <caption>선택된 카테고리</caption>
            <colgroup>
              <col style="width: 140px" />
              <col />
            </colgroup>
            <tbody>
              <tr>
                <th scope="row">선택된 카테고리</th>
                <td>
                  <span v-if="selected && selected.value" class="team color-primary">
                    {{ selected.description }}({{ selected.value }})
                  </span>
                  <span v-else class="team color-primary">
                    없음
                  </span>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>

      <div class="mu-dialog-foot">
        <button type="button" class="mu-btn" @click="onCancel">
          <span>취소</span>
        </button>
        <button type="button" class="mu-btn mu-btn-primary" @click="onConfirm">
          <span>확인</span>
        </button>
      </div>

      <div class="mu-dialog-close-btn">
        <button type="button" class="mu-btn mu-btn-close" title="해당 팝업 닫기" @click="onClose">
          <i class="mu-icon-svg close"></i><span>팝업닫기</span>
        </button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, onBeforeUnmount, ref } from 'vue';
import { useCategoryTreeStore } from "@store/categoryTree.ts";
import CategoryItem from "@functional/classificationModal/category-item";

interface SelectedCategory {
  description: string;
  value: string;
  level: string;
  parent_id: string;
  id: string;
}

const emit = defineEmits(["onCloseModal", "onSelect"]);
const props = defineProps([
  "toggleSelectCategoryModal",
  "name",
  "upperCategory"
]);

const word = ref("");
const selected = ref<SelectedCategory>({
  description: "",
  value: "",
  level: "",
  parent_id: "",
  id: "",
});

const searchedCategoryList = ref([])

const categoryTreeStore = useCategoryTreeStore();
const toggleTargets = categoryTreeStore.getToggleTargets;
const selectedId = categoryTreeStore.getSelectedItemId;

const isActive = ref(false);
const zIndex = ref(101);

const showData = ref(true);
const toggleShowDataText = ref("펼치기");

const toggleShowData = () => {
  if (showData.value) {
    toggleShowDataText.value = "접기";
    expandAll()
  } else {
    toggleShowDataText.value = "펼치기";
    collapseAll()
  }
  showData.value = !showData.value;
};

//
const onClear = () => {
  if (props.upperCategory && props.upperCategory.id) {
    selected.value = {
      description: props.upperCategory.description,
      value: props.upperCategory.name,
      level: props.upperCategory.level,
      parent_id: props.upperCategory.parent_id,
      id: props.upperCategory.id,
    }
    categoryTreeStore.setSelectedItemId(props.upperCategory.id);
  } else {
    selected.value = {
      description: "",
      value: "",
      level: "",
      parent_id: "",
      id: "",
    }
    categoryTreeStore.setSelectedItemId("");
  }

  word.value = "";
  collapseAll()
};

//
const onClose = () => {
  onClear();

  word.value = "";
  emit("onCloseModal");
};

//
const onCancel = () => {
  onClose();
};

//
const onConfirm = () => {
  if (props.toggleSelectCategoryModal) {
    if ((selected.value && selected.value.value) || selected.value.value == "") {
      emit("onSelect", selected.value);
    }
    onClose();
  }
};

//
onMounted(() => {
  openModal();
});

//
onBeforeUnmount(() => {
  closeModal();
});

//
const onClick = (item) => {
  if (item) {
    if (item == 'all') {

    } else {
      selected.value = {
        description: item.description,
        value: item.name,
        level: item.level,
        parent_id: item.parent_id,
        id: item.id,
      }
      categoryTreeStore.setSelectedItemId(item && item.id ? item.id : "");
    }
  }
};

//
const onToggle = (item) => {
  if (item) {
    categoryTreeStore.setToggleTargetOnClick(item && item.id ? item.id : "")
  } else {
    categoryTreeStore.setToggleTargetOnClickAll()
  }
};

const onToggleAll = () => {
  categoryTreeStore.setSelectedItemId("");
  selected.value = {
    description: "",
    value: "",
    level: "",
    parent_id: "",
    id: "",
  }
}

const expandAll = () => {
  toggleTargets.value = categoryTreeStore.expandAll()
}

const collapseAll = () => {
  toggleTargets.value = categoryTreeStore.collapseAll()
}

//
const openModal = () => {
  word.value = ""
  searchedCategoryList.value = categoryTreeStore.getCategoryList
  if (props.upperCategory) {
    selected.value = props.upperCategory
    categoryTreeStore.setSelectedItemId(props.upperCategory.id);
    // 현재 선택된 카테고리의 상위 메뉴 열기 처리
    toggleParents(selected.value)

  }

  isActive.value = true;
  zIndex.value = getNextZIndex();
};

// 해당 카테고리 검색
const onSearch = () => {
  let tmpList = categoryTreeStore.getCategoryList

  const filteredResult = filterData(tmpList, word.value)
  searchedCategoryList.value = filteredResult
}

const filterData = (data, searchTerm) => {
  // 소문자로 변환하여 검색 (대소문자 구분 없음)
  const lowerSearchTerm = searchTerm.toLowerCase();

  function recursiveFilter(items) {
    return items
      .map(item => {
        // 현재 항목이 검색어를 포함하는지 확인
        const match =
          item.name.toLowerCase().includes(lowerSearchTerm) ||
          item.description.toLowerCase().includes(lowerSearchTerm);

        // 하위 subTypes 필터링
        const filteredSubTypes = recursiveFilter(item.subTypes);

        // 현재 항목이 검색어와 일치하거나 하위 항목 중 하나라도 일치하면 유지
        if (match || filteredSubTypes.length > 0) {
          return { ...item, subTypes: filteredSubTypes };
        }
        return null;
      })
      .filter(item => item !== null); // null 제거
  }

  return recursiveFilter(data);
}

// 좌측 상위메뉴 펼치기 처리
const toggleParents = (matched) => {
  if (matched) {
    const parent_matched = categoryTreeStore.getOriginalList.find((el) => {
      return el.id === matched.parent_id
    })
    if (parent_matched) {
      categoryTreeStore.getToggleTargets.value[parent_matched.id] = true
      toggleParents(parent_matched)
    }
  }
}

//
const closeModal = () => {
  isActive.value = false;
};

//
const getNextZIndex = () => {
  const maxZIndex = Array.from(document.querySelectorAll('*'))
    .map(el => parseFloat(window.getComputedStyle(el).zIndex))
    .filter(zIndex => !isNaN(zIndex))
    .reduce((max, current) => Math.max(max, current), 100);

  return maxZIndex + 1;
};

</script>

<style scoped>
.mu-modal {
  position: fixed;
  z-index: 101;
  /* 기본 z-index 값 */
}
</style>
