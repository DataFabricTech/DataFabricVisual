<template>
  <div class="pagination">
    <button
        class="pagination-icon-button button button-neutral-ghost"
        type="button"
        @click="movePage(--currentPage)"
        :disabled="currentPage === 1"
    >
      <span class="hidden-text">이전 페이지 이동</span>
      <svg-icon class="button-icon" name="chevron-left-medium"></svg-icon>
    </button>
    <ul v-if="paginationTotalCount <= 7" class="pagination-list">
      <li
          class="pagination-item"
          v-for="(item, index) in paginationList"
          :key="index"
      >
        <button
            type="button"
            class="pagination-button"
            @click="movePage(item)"
            :class="item === currentPage ? 'button-primary' : 'button-neutral-ghost'"
        >
          {{ item }}
        </button>
      </li>
    </ul>
    <ul v-else class="pagination-list">
      <li class="pagination-item">
        <button
            type="button"
            class="pagination-button"
            @click="movePage(paginationList[0])"
            :class="paginationList[0] === currentPage ? 'button-primary' : 'button-neutral-ghost'"
            key="0"
        >
          {{ paginationList[0] }}
        </button>
      </li>
      <li class="pagination-item">
        <span class="pagination-ellipsis" v-if="currentPage > 4">...</span>
        <button
            v-else
            type="button"
            class="pagination-button"
            @click="movePage(paginationList[1])"
            :class="paginationList[1] === currentPage ? 'button-primary' : 'button-neutral-ghost'"
            key="0"
        >
          {{ paginationList[1] }}
        </button>
      </li>
      <li
          class="pagination-item"
          v-for="(item, index) in calculatedPaginationList"
          :key="index"
      >
        <button
            type="button"
            class="pagination-button"
            @click="movePage(item)"
            :class="item === currentPage ? 'button-primary' : 'button-neutral-ghost'"
        >
          {{ item }}
        </button>
      </li>
      <li class="pagination-item">
        <span class="pagination-ellipsis" v-if="paginationTotalCount - currentPage > 3"
        >...</span
        >
        <button
            v-else
            type="button"
            class="pagination-button"
            @click="movePage(paginationList[paginationTotalCount - 2])"
            :class="
            paginationList[paginationTotalCount - 2] === currentPage
              ? 'button-primary'
              : 'button-neutral-ghost'
          "
            :key="paginationTotalCount - 1"
        >
          {{ paginationList[paginationTotalCount - 2] }}
        </button>
      </li>
      <li class="pagination-item">
        <button
            type="button"
            class="pagination-button"
            @click="movePage(paginationList[paginationTotalCount - 1])"
            :class="
            paginationList[paginationTotalCount - 1] === currentPage
              ? 'button-primary'
              : 'button-neutral-ghost'
          "
            :key="paginationTotalCount - 1"
        >
          {{ paginationList[paginationTotalCount - 1] }}
        </button>
      </li>
    </ul>
    <button
        class="pagination-icon-button button button-neutral-ghost"
        type="button"
        @click="movePage(++currentPage)"
        :disabled="currentPage === paginationTotalCount"
    >
      <span class="hidden-text">다음 페이지 이동</span>
      <svg-icon class="button-icon" name="chevron-right-medium"></svg-icon>
    </button>
  </div>
</template>

<script setup lang="ts">
import type {PaginationProps} from "./PaginationProps";
import {PaginationComposition} from "./PaginationComposition";

const props = withDefaults(defineProps<PaginationProps>(), {
  totalCount: 0,
  perPage: 20,
  currentPageNumber: 1
});

const emit = defineEmits<{ (e: "change", page: number): void }>();

function onChange(page: number): void {
  emit("change", page);
}

const {
  currentPage,
  paginationTotalCount,
  paginationList,
  calculatedPaginationList,
  movePage
} = PaginationComposition(props, onChange);

</script>

<style></style>