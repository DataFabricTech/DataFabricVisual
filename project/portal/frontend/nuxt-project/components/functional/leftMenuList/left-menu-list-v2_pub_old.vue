<template>
  <div class="category-wrap">
    <div class="category-head">
      <h4>전체 필터</h4>
      <!-- &nbsp; &nbsp; &nbsp; <button v-if="calledFrom === 'metaDataManage'" type="button" @click="onClickCategoryOrderChange()">순서 변경</button> -->
    </div>
    <div class="category-body">
      <div v-for="key in keyList">
        <div style="margin-top: 20px;" v-if="key != 'TAG' && props.detailFilters[key] && (props.detailFilters[key].all || props.detailFilters[key].count > 0)">
          <strong>{{ props.detailFilters[key].name }}</strong>
          <div class="slim-scroll" style="max-height: 150px; background-color: #f2f5f8; border: 1px solid lightgrey; margin-top: 5px; padding: 15px;">
            <div v-if="props.detailFilters[key] && props.detailFilters[key].all">전체선택</div>
            <div v-else>
              <div v-for="(df, idx) in props.detailFilters[key].content">
                <div v-if="df['checked']" id="descriptionDiv">
                  {{ df['description'] }}
                  <button
                    type="button"
                    class="mu-btn mu-btn-delete"
                    @click="onClickDelete(key, df)"
                  >
                    <i class="mu-icon-svg item-delete"></i>
                    <span>삭제</span>
                  </button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useCategoryStore } from "~/store/category";
import { useEventStore  } from "~/store/event.ts";
import { computed } from 'vue';

const emit = defineEmits([
  "onClickMenuItem", // 메뉴 클릭 이벤트 핸들러
  "onClickCategoryOrderChange",
  "onToggleMetaDataTooltip",
  "onClickOptionalButtons", // 메타 데이터 통합 관리 화면용 추가 버튼 클릭 이벤트 핸들러
  "onClickFromTreeMap",
  'onClickDelete'
])

const props = defineProps(['filterItem', 'detailFilters'])

const categoryStore = useCategoryStore();

const eventStore = useEventStore();

const keyList = ref([])

onMounted(() => {
  keyList.value = Object.keys(props.detailFilters).filter((el) => el != 'DATE')
})

// 항목 제거
const onClickDelete = (opt, item) => {
    const deleteItem = props.detailFilters[opt].content.filter((el) => el.name == item.name)
    emit('onClickDelete', deleteItem[0])
}

// 카테고리 순서 변경 클릭 이벤트 핸들러
const onClickCategoryOrderChange = () => {
  emit("onClickCategoryOrderChange");
}

// 카테고리 세팅 버튼 클릭 이벤트 핸들러
const onToggleTooltip = (e, item, type) => {
  eventStore.emit('onToggleMetaDataTooltip', e, item, type);
}

// 카테고리 목록 상단 추가/새로고침 버튼 클릭 이벤트 핸들러
const onClickButton = (type) => {
  emit("onClickOptionalButtons", type);
}

const onToggle = (value) => {
  categoryStore.setToggleTargetOnClick(value);
}

</script>
