<template>
  <div class="pagination">
    <ul class="pagination-list">
      <li>
        <BaseButton class="button-icon pagination-button" title="첫 페이지로 이동" @click="movePage(0)">
          <span class="hidden-text">첫 페이지로 이동</span>
          <svg-icon aria-hidden="true" class="svg-icon" name="arrow-double-left"></svg-icon>
        </BaseButton>
      </li>
      <li>
        <BaseButton class="button-icon pagination-button" title="이전 페이지로 이동" @click="movePage(selectedPage - 1)">
          <span class="hidden-text">이전 페이지로 이동</span>
          <svg-icon aria-hidden="true" class="svg-icon" name="arrow-left"></svg-icon>
        </BaseButton>
      </li>
      <li v-for="item in pages" :key="item">
        <BaseButton
          class="pagination-button button-icon"
          :class="currentGroup * props.perPage + item - 1 === selectedPage ? 'is-selected': ''"
          :title="item + ' 페이지로 이동'"
          @click="movePage(currentGroup * props.perPage + item - 1)">
          <span class="button-text">{{ currentGroup * props.perPage + item }}</span>
        </BaseButton>
      </li>
      <li>
        <BaseButton class="button-icon pagination-button" title="다음 페이지로 이동" @click="movePage(selectedPage + 1)">
          <span class="hidden-text">다음 페이지로 이동</span>
          <svg-icon aria-hidden="true" class="svg-icon" name="arrow-right"></svg-icon>
        </BaseButton>
      </li>
      <li>
        <BaseButton class="button-icon pagination-button" title="마지막 페이지로 이동" @click="movePage(totalPage - 1)">
          <span class="hidden-text">마지막 페이지로 이동</span>
          <svg-icon aria-hidden="true" class="svg-icon" name="arrow-double-right"></svg-icon>
        </BaseButton>
      </li>
    </ul>
  </div>
</template>

<script setup lang="ts">
import { computed, defineEmits, defineProps, onMounted, ref } from "vue";

const props = defineProps({
  totalElements: {
    type: Number,
    default: 0
  },
  // 10개 씩 보기, 20개씩 보기
  pageSize: {
    type: Number,
    default: 10
  },
  // 현재 페이지
  currentPage: {
    type: Number,
    default: 0
  },
  // 페이지네이션에 몇개의 페이지가 보이게 할것인지
  perPage: {
    type: Number,
    default: 10
  }
});
const totalPage = ref()
const selectedPage = ref(null)
const currentGroup = ref(0)
const lastGroup = ref(0)
const startIndex = ref()
const lastIndex = ref()
const pageCnt = ref()
const emit = defineEmits(['click']);

const pages = computed(() => {
  if(selectedPage.value != null) {
    totalPage.value = Math.ceil(props.totalElements / props.pageSize);
    lastGroup.value = Math.ceil(totalPage.value / props.perPage); //4
    currentGroup.value = Math.floor(selectedPage.value/ props.perPage); //0
    startIndex.value = ( currentGroup.value * props.perPage) + 1

    let last = ((currentGroup.value + 1) * props.perPage)

    if(totalPage.value < last) {
      lastIndex.value = totalPage.value
      pageCnt.value = (totalPage.value % props.perPage)
    } else {
      lastIndex.value = last
      pageCnt.value = props.perPage
    }
    return pageCnt.value
  }
});

function movePage(page: number) {
  // page가 0일때 이전 버튼 누르거나 ||  page가 마지막일때 다음 버튼 누를떄
  if(page < 0 || (page > totalPage.value - 1 && currentGroup.value + 1 === lastGroup.value)) {
    return null
  }
  // 페이지가 0일때 맨처음버튼 누를떄
  if(selectedPage.value === 0 && page === 0) {
    return null;
  }
  //페이지가 마지막일때 마지막으로가기 버튼 누를때
  if(page === totalPage.value - 1 && selectedPage.value === totalPage.value - 1) {
    return null
  }
  selectedPage.value = page
  emit('click', page)
}
onMounted(() => {
  selectedPage.value = props.currentPage
})
</script>
