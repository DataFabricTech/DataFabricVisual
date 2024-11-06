<template>
  <div class="board-list-search">
    <form action="">
      <fieldset>
        <legend>게시물검색</legend>
        <div class="inner-search">
          <div class="mu-input-icon">
              <input type="text" class="mu-input" placeholder="Search" title="검색어입력"
                v-model="searchObj.searchWord" @keypress.enter.prevent='onSearch()'
              />

              <button type="button" class="mu-btn mu-btn-sch"><i class="mu-icon-svg search"
                @click="onSearch()">
                </i><span>검색</span>
              </button>
          </div>
        </div>
      </fieldset>
    </form>
  </div>
</template>

<script setup lang="ts">
const emit = defineEmits(['update:searchObj', 'searchMethod'])
const props = defineProps(['searchObj', 'searchList' ])

// 데이터 조회
const onSearch = () => {
  emit('update:searchObj',  props.searchObj);
  emit('searchMethod');
}
</script>
