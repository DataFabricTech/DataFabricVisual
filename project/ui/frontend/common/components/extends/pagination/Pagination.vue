<template>
  <div class="pagination">
    <button
        class="pagination-move-button"
        type="button"
        @click="movePage(--currentPage)"
        :disabled="currentPage === 1"
    >
      <
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
            :class="item === currentPage ? 'is-active' : ''"
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
            :class="paginationList[0] === currentPage ? 'is-active' : ''"
            key="0"
        >
          {{ paginationList[0] }}
        </button>
      </li>
      <li class="pagination-item">
        <span class="ellipsis" v-if="currentPage > 4">...</span>
        <button
            v-else
            type="button"
            class="pagination-button"
            @click="movePage(paginationList[1])"
            :class="paginationList[1] === currentPage ? 'is-active' : ''"
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
            :class="item === currentPage ? 'is-active' : ''"
        >
          {{ item }}
        </button>
      </li>
      <li class="pagination-item">
        <span class="ellipsis" v-if="paginationTotalCount - currentPage > 3"
        >...</span
        >
        <button
            v-else
            type="button"
            class="pagination-button"
            @click="movePage(paginationList[paginationTotalCount - 2])"
            :class="
            paginationList[paginationTotalCount - 2] === currentPage
              ? 'is-active'
              : ''
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
              ? 'is-active'
              : ''
          "
            :key="paginationTotalCount - 1"
        >
          {{ paginationList[paginationTotalCount - 1] }}
        </button>
      </li>
    </ul>
    <button
        class="pagination-move-button"
        type="button"
        @click="movePage(++currentPage)"
        :disabled="currentPage === paginationTotalCount"
    >
      >
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

<style scoped>
.pagination,
.pagination-move-button,
.pagination-list {
  display: flex;
}

.pagination,
.pagination-list {
  gap: 16px;
}

.pagination-move-button,
.pagination-button {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 50px;
  height: 50px;
  background-color: #fff;
  border: none;
  border-radius: 4px;
  font-size: 20px;
  cursor: pointer;
}

.ellipsis {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 50px;
  height: 50px;
  background-color: #fafafa;
  border: none;
  border-radius: 4px;
  font-size: 20px;
  cursor: default;
}

.pagination-button.is-active {
  background-color: royalblue;
  color: #fff;
}

button:disabled {
  background-color: lightgray;
  cursor: not-allowed;
}

</style>
