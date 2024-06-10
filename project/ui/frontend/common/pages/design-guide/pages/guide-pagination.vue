<template>
  <section class="guide-section">
    <p class="section-type">Component</p>
    <h2 class="section-title">Pagination</h2>
    <p class="section-summary">
      페이지네이션은 대량의 콘텐츠를 여러 페이지로 나누어 작은 단위로 표시해줍니다. 양쪽으로 이전 버튼, 다음 버튼 기능을 제공합니다.
    </p>
    <article class="mb-10">
      <h3 class="section-subtitle">Basic Pagination</h3>
      <div class="flex flex-col gap-5">
        <Pagination :totalCount="60" :perPage="10" :currentPageNumber="4" @change="checkCurrentPage"></Pagination>
      </div>
    </article>
    <article class="mb-10">
      <h3 class="section-subtitle">Numerous Pagination</h3>
      <div class="flex flex-col gap-5">
        <Pagination :totalCount="100" :perPage="10" :currentPageNumber="1" @change="checkCurrentPage"></Pagination>
      </div>
    </article>
  </section>
</template>

<script>
export default {
  name: "guide-pagination"
};
</script>

<style scoped>

</style>