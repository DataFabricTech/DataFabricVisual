<template>
  <div class="mu-pagination">
    <button type="button" class="mu-btn mu-btn-paging" v-show="showPrevPageBtn"
      @click="() => onClickMoveButton('first')">
      <i class="mu-icon-svg first"></i><span>맨 처음으로 이동</span>
    </button>
    <button type="button" class="mu-btn mu-btn-paging" v-show="showPrevPageBtn"
      @click="() => onClickMoveButton('prev')">
      <i class="mu-icon-svg prev"></i><span>이전 페이지로 이동</span>
    </button>
    <ul>
      <li v-for="page in pages" :key="page" :class="{ active: page === paging.currentPage }"
        @click="onClickPageNum(page)">
        <a href="javascript:void(0);">{{ page }}</a>
      </li>
    </ul>
    <button type="button" class="mu-btn mu-btn-paging" v-show="showNextPageBtn"
      @click="() => onClickMoveButton('next')">
      <i class="mu-icon-svg next"></i><span>다음 페이지로 이동</span>
    </button>
    <button type="button" class="mu-btn mu-btn-paging" v-show="showNextPageBtn"
      @click="() => onClickMoveButton('last')">
      <i class="mu-icon-svg last"></i><span>맨 마지막으로 이동</span>
    </button>
  </div>
</template>

<script setup lang="ts">
const emit = defineEmits(["onClickPage"]);
const props = defineProps({
  // 페이징 처리 관리 객체
  paging: {
    type: Object,
    default: () => ({
      total: 0, // 전체 데이터 건수
      limit: 25, // 한 페이지에 보여줄 데이터 건수
      block: 10, // 한 화면에서 보여줄 페이지 수
      currentPage: 1, // 현재 페이지 번호
    }),
  },
});

const pages = ref<Array<number>>([]);

// 맨앞/이전 페이지로 이동 버튼 생성 여부
const showPrevPageBtn = computed(() => {
  if (props.paging) {
    const { currentPage } = props.paging;
    return currentPage > 1;
  } else {
    return false;
  }
});

// 다음/마지막 페이지로 이동 버튼 생성 여부
const showNextPageBtn = computed(() => {
  if (props.paging) {
    const { total, currentPage } = props.paging;
    return total > 0 && getTotalPages() !== currentPage;
  } else {
    return false;
  }
});

// 페이징 제어 데이터 변경에 따라 페이지 번호 목록 계산
watch(
  () => props.paging,
  (newVal, oldVal) => {
    const { block = 10, currentPage = 1 } = newVal;
    const newPages = [];

    const startPage = (Math.ceil(currentPage / block) - 1) * block + 1;
    const totalPages = getTotalPages();
    const endPage = startPage + block > totalPages ? totalPages : startPage + block - 1;

    for (let index = startPage; index <= endPage; index++) {
      newPages.push(index);
    }

    pages.value = newPages;
  }
);

// 전체 페이지수 계산 및 반환
const getTotalPages = (): number => {
  const { total = 0, limit = 25 } = props.paging;
  return Math.ceil(total / limit);
};

// 페이지 번호 클릭 이벤트 핸들러
const onClickPageNum = (page) => {
  emit("onClickPage", page);
};

// 최초/이전/다음/마지막 페이지 이동 버튼 클릭 이벤트 핸들러
const onClickMoveButton = (to) => {
  const { currentPage = 1 } = props.paging;
  let pageNum = currentPage;

  switch (to) {
    case "first": // '맨 처음 페이지로 이동' 버튼 클릭 시
    case "prev": // '이전 페이지로 이동' 버튼 클릭 시
      if (pageNum > 1) {
        pageNum = to === "first" ? 1 : pageNum - 1;
      }
      break;
    case "next": // '맨 마지막 페이지로 이동' 버튼 클릭 시
    case "last": // '다음 페이지로 이동' 버튼 클릭 시
      const totalPages = getTotalPages();
      if (totalPages > currentPage) {
        pageNum = to === "last" ? totalPages : pageNum + 1;
      }
      break;
  }

  emit("onClickPage", pageNum);
};
</script>
