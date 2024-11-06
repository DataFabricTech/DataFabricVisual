<template>
  <div class="mu-heading">
    <!-- board-list-search -->
    <div class="board-list-search">
      <form action="" @submit.prevent="onSearch">
        <fieldset>
          <legend>게시물검색</legend>
          <div class="inner-search">
            <div class="mu-selectbox w120">
              <select
                name="searchType1"
                :value="props.filterSearchObj.searchType1"
                @change="onChange"
              >
                <option value="hive_table">Table</option>
                <option value="hive_column">Column</option>
              </select>
            </div>
            <div class="mu-selectbox w120">
              <select
                name="searchType2"
                :value="props.filterSearchObj.searchType2"
                :disabled="props.filterSearchObj.searchType1 === '_ALL_ENTITY_TYPES'"
                @change="onChange"
              >
                <option
                  v-for="(option, index) in props.filterSearchObj.options"
                  :key="index"
                  :value="option.value"
                >
                  {{ option.desc }}
                </option>
              </select>
            </div>
            <div class="mu-input-icon">
              <input
                type="text"
                class="mu-input"
                placeholder="Search"
                title="검색어입력"
                name="word"
                :value="props.filterSearchObj.word"
                :disabled="props.filterSearchObj.searchType1 === '_ALL_ENTITY_TYPES'"
                @keyup="onChange"
              />
              <button type="button" class="mu-btn mu-btn-sch" @click.prevent="onSearch">
                <i class="mu-icon-svg search"></i><span>검색</span>
              </button>
            </div>
          </div>
        </fieldset>
      </form>
    </div>
    <!-- //board-list-search -->
    <div class="mu-item-group">
      <page-size v-model:pageSize="props.pagingObj.limit" @searchMethod="onSearch" />
      <button
        type="button"
        class="mu-btn mu-btn-primary"
        @click.stop="onClickButton('category')"
      >
        <span>분류맵핑</span>
      </button>
    </div>
  </div>
</template>

<script setup lang="ts">
import PageSize from "@/pages/admin/manager/system/user/page-size";

const emit = defineEmits(["onChangeFilter", "onClickButtons", "onFilterSearch"]);
const props = defineProps(["filterSearchObj", "pagingObj"]);

// 필터검색 input 변경 이벤트 핸들러
const onChange = (e) => {
  const {
    target: { name, value },
  } = e;
  emit("onChangeFilter", name, value);
};

//매핑 버튼 클릭
const onClickButton = (type) => {
  emit("onClickButtons", type);
};

// 데이터 필터 검색 이벤트 핸들러
const onSearch = () => {
  const { searchType, word } = props.filterSearchObj;
  // 검색 유형 미설정 또는 검색 유형 설정 & 검색어 입력 입력 상태에서만 조회
  if (!searchType || (searchType && word)) {
    emit("onFilterSearch");
  }
};
</script>
