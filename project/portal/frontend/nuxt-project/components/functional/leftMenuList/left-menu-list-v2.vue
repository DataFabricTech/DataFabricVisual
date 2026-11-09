<template>
  <div class="category-wrap">
    <div class="category-head">
      <h4>상세필터</h4>
    </div>
    <dl class="category-body">
      <div v-for="(key, idx) in keyList">
        <dt>{{ getDescription(key) }}</dt>
        <dd v-if="props.currentTab == 0">
          <ul v-if="props.detailFilters[key] && props.detailFilters[key].all" class="selected-category">
            <li>전체선택</li>
          </ul>
          <ul v-else class="selected-category">
            <li v-for="(df, idx) in props.detailFilters[key].content.filter((el) => el.checked)">
              <a href="#" style="pointer-events: none;">{{ df['description'] }}</a>
              <button type="button" class="mu-btn mu-btn-delete" @click="onClickDelete(key, df)"><i
                  class="mu-icon-svg delete"></i>
              </button>
            </li>
          </ul>
        </dd>
        <dd v-else>
          <ul v-if="(props.detailFilters[key] && props.detailFilters[key].all)" class="selected-category">
            <li>전체선택</li>
          </ul>
          <ul v-else class="selected-category">
            <li v-if="idx == 'SYS' && props.currentTab == 1">전체선택</li>
            <li v-else v-for="(df, idx) in props.detailFilters[key].content.filter((el) => el.checked)">
              <a href="#" style="pointer-events: none;">{{ df['description'] }}</a>
              <button type="button" class="mu-btn mu-btn-delete" @click="onClickDelete(key, df)"><i
                  class="mu-icon-svg delete"></i>
              </button>
            </li>
          </ul>
        </dd>
        <br>
      </div>
    </dl>
  </div>
</template>

<script setup lang="ts">
const emit = defineEmits([
  "onClickMenuItem", // 메뉴 클릭 이벤트 핸들러
  "onClickCategoryOrderChange",
  "onToggleMetaDataTooltip",
  "onClickOptionalButtons", // 메타 데이터 통합 관리 화면용 추가 버튼 클릭 이벤트 핸들러
  "onClickFromTreeMap",
  'onClickDelete'
])
const props = defineProps(['filterItem', 'detailFilters', 'currentTab'])
const keyList = ref([])

onMounted(() => {
  keyList.value = Object.keys(props.detailFilters).filter((el) => el != 'DATE')
})

const getDescription = (key) => {
  if (key == 'CAT') return "카테고리"
  if (key == 'SYS') return "시스템"
  return key
}

// 항목 제거
const onClickDelete = (opt, item) => {
  const deleteItem = props.detailFilters[opt].content.filter((el) => el.name == item.name)
  emit('onClickDelete', deleteItem[0])
}

</script>
