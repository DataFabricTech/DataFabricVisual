<template>
  <div class="board-list-search">
    <form action="">
      <fieldset>
        <legend>게시물검색</legend>
        <div class="inner-search">
          <div class="mu-selectbox w160">
            <select v-model="props.searchObj.searchType">
              <option value="all">전체</option>
              <option value="term">대표어</option>
              <option value="synonym">동의어</option>
            </select>
          </div>
          <div class="mu-input-icon">
            <input type="text" class="mu-input" placeholder="Search" title="검색어입력" v-model="props.searchObj.searchWord"
              @keypress.enter.prevent='onSearch()' />
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
const props = defineProps(["searchObj"]);
const emit = defineEmits(['update:searchObj', 'searchMethod'])

const onSearch = () => {
  emit('update:searchObj', props.searchObj);
  emit('searchMethod');
}
</script>
