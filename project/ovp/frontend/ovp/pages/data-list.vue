<template>
  <div class="wrap">
    <Header />
    <main id="main">
      <Sidebar></Sidebar>
      <section id="section">
        <div class="filters">
          <div class="select select-clean" v-for="n in 5" :key="n">
            <button class="select-button">
              <span class="select-button-title">카테고리</span>
              <div class="badge badge-primary-lighter">
                <p class="badge-text">10</p>
              </div>
              <svg-icon class="svg-icon select-indicator" name="chevron-down-medium"></svg-icon>
            </button>
          </div>
          <button class="button button-error-lighter button-sm" type="button">
            <svg-icon class="button-icon" name="reset"></svg-icon>
            <span class="button-title">초기화</span>
          </button>
        </div>
        <div class="section-contents">
          <div class="l-top-bar">
            <span>총 <strong class="primary">68건</strong>의 검색 결과가 있습니다.</span>
            <div class="h-group gap-1">
              <div class="select select-clean">
                <button class="select-button">
                  <span class="select-button-title">인기많은순 ↓</span>
                  <svg-icon class="svg-icon select-indicator" name="chevron-down-medium"></svg-icon>
                </button>
              </div>
              <div class="select select-clean">
                <button class="select-button">
                  <span class="select-button-title">10개씩 보기</span>
                  <svg-icon class="svg-icon select-indicator" name="chevron-down-medium"></svg-icon>
                </button>
              </div>
              <div class="button-group">
                <input
                  type="radio"
                  id="button-groupprimary"
                  class="button-group-input"
                  name="button-group3"
                  checked
                />
                <label for="button-groupprimary" class="button-group-label">
                  <svg-icon class="svg-icon" name="list"></svg-icon>
                  <span class="hidden-text">리스트보기</span>
                </label>
                <input type="radio" id="button-groupprimary2" class="button-group-input" name="button-group3" />
                <label for="button-groupprimary2" class="button-group-label">
                  <svg-icon class="svg-icon" name="knowleage-graph"></svg-icon>
                  <span class="hidden-text">시각화 보기</span>
                </label>
              </div>
            </div>
          </div>
          <div class="l-split">
            <div class="data-page">
              <div class="v-group gap-4 grow">
                <div class="card" v-for="card in 3" :key="card">
                  <div class="card-function">
                    <div class="card-model">
                      <img class="card-model-img" src="../assets/images/common/resource.jpg"></img>
                      <div class="breadcrumb">
                        <ul class="breadcrumb-list">
                          <li class="breadcrumb-item">
                            <a href="#" class="breadcrumb-link">1depth</a>
                          </li>
                          <li class="breadcrumb-item">
                            <a href="#" class="breadcrumb-link">2depth</a>
                          </li>
                          <li class="breadcrumb-item is-breadcrumb-selected">
                            <a href="#" class="breadcrumb-link">데이터 모델</a>
                          </li>
                        </ul>
                      </div>
                    </div>
                    <button class="button button button-neutral-ghost button-sm">미리보기</button>
                  </div>
                  <a href="#" class="card-title" title="상세 보기">세종특별자치시 상하수도요금표</a>
                  <span class="card-depict">한국교통안전공단에서 교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다.</span>
                  <div class="card-info">
                    <dl class="card-info-list">
                      <dt class="card-info-title">소유자</dt>
                      <dd class="card-info-text">Owner</dd>
                    </dl>
                    <dl class="card-info-list">
                      <dt class="card-info-title">도메인</dt>
                      <dd class="card-info-text">Domain</dd>
                    </dl>
                  </div>
                </div >

              </div>
              <Pagination :totalCount="60" :perPage="10" :currentPageNumber="4" @change="checkCurrentPage"></Pagination>
            </div>
            <div class="side-preview" style=""></div>
          </div>
          </div>
      </section>
    </main>
  </div>
</template>

<script>
import Header from "../layouts/header.vue";
import Sidebar from "../layouts/sidebar.vue";
import Pagination from "../../common/components/extends/pagination/Pagination.vue";



export default {
  name: "data-list",
  components: { Sidebar, Header, Pagination }
};
</script>

<style scoped>

</style>