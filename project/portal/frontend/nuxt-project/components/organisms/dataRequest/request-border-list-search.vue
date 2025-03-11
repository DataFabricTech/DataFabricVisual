<template>
  <!-- board-list-search -->
  <div class="board-list-search">
    <form action="">
      <fieldset>
        <legend>게시물검색</legend>
        <div class="inner-search">
          <!-- mu-selectbox -->
          <div class="mu-selectbox w160">
            <select v-model="props.searchObj.searchType">
              <option value="all">전체</option>
              <option value="data_name">데이터명</option>
              <option value="writer">신청자</option>
              <option value="contents">신청내용</option>
            </select>
          </div>
          <div class="mu-input-icon">
            <input type="text" v-model="props.searchObj.searchWord" @keypress.enter.prevent="onSearch()"
              class="mu-input" placeholder="Search" title="검색어입력" />
            <button type="button" class="mu-btn mu-btn-sch" @click="onSearch()">
              <i class="mu-icon-svg search"></i>
              <span>검색</span>
            </button>
          </div>
        </div>
      </fieldset>
    </form>
  </div>
</template>

<script setup lang="ts">
const emit = defineEmits(["update:searchObj", "searchMethod"]);
const props = defineProps(["searchObj"]);

const onSearch = (): void => {
  emit("update:searchObj", props.searchObj);
  emit("searchMethod");
};
</script>
