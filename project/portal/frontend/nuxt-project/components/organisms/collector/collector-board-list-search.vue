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
              <option v-for="item in props.searchObj.searchTypeList" :value="item.name">{{ item.description }}</option>
            </select>
          </div>
          <div class="mu-input-icon">
            <input
              type="text"
              class="mu-input"
              style="max-width: 380px;"
              placeholder="Search"
              title="검색어입력"
              v-model="props.searchObj.searchWord"
              @keypress.enter.prevent="onSearch()"
            />
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
const props = defineProps(["searchMethod", "searchObj", "searchTypeList"]);

// 조회 이벤트
const onSearch = (): void => {
  emit("update:searchObj", props.searchObj);
  emit("searchMethod");
};

</script>
