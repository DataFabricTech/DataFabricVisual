<template>
  <div class="pagination">
    <ul class="pagination-list">
      <li>
        <BaseButton class="button-icon pagination-button" title="첫 페이지로 이동">
          <span class="hidden">첫 페이지로 이동</span>
          <svg-icon aria-hidden="true" class="svg-icon" name="arrow-double-left"></svg-icon>
        </BaseButton>
      </li>
      <li>
        <BaseButton class="button-icon pagination-button" title="이전 페이지로 이동">
          <span class="hidden">이전 페이지로 이동</span>
          <svg-icon aria-hidden="true" class="svg-icon" name="arrow-left"></svg-icon>
        </BaseButton>
      </li>
      <li v-for="item in state.pageList" :key="item.id">
        <!--        현재 위치인 경우 is-selected 클래스 추가 -->
        <BaseButton class="pagination-button button-icon" :class="item.isSelected ? 'is-selected':''" :title="item.page + ' 페이지로 이동'">
          <span class="button-text">{{item.page}}</span>
        </BaseButton>
      </li>
      <li>
        <BaseButton
          class="button-icon pagination-button"
          title="다음 페이지로 이동"
        >
          <span class="hidden">다음 페이지로 이동</span>
          <svg-icon aria-hidden="true" class="svg-icon" name="arrow-right"></svg-icon>
        </BaseButton>
      </li>
      <li>

        <BaseButton
          class="button-icon pagination-button"
          title="마지막 페이지로 이동"
        >
          <span class="hidden">마지막 페이지로 이동</span>
          <svg-icon aria-hidden="true" class="svg-icon" name="arrow-double-right"></svg-icon>
        </BaseButton>
      </li>
    </ul>

  </div>
</template>

<script setup lang="ts">
const state = reactive({
  pageList: [
    {
      id: 1,
      page: "1",
      isSelected: true
    },
    {
      id: 2,
      page: "2",
      isSelected: false
    },
    {
      id: 3,
      page: "3",
      isSelected: false
    },
    {
      id: 4,
      page: "4",
      isSelected: false
    }
  ]
});


</script>
