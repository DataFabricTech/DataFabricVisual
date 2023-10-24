<!-- 데이터패브릭포털 - 검색 -->
<template>
  <div class="l-common flex-grow">
    <h2 class="hidden">데이터패브릭포털 검색</h2>
    <section class="l-section flex-grow">
      <div class="main-search">
        <SearchField class="search-field-lg"></SearchField>
        <diV class="h-group gap-5">
          <baseTag># CCTV위치</baseTag>
          <baseTag># 주정차댠속</baseTag>
          <baseTag># 기후변화</baseTag>
          <baseTag># 안심지역</baseTag>
        </diV>
      </div>
      <div class="main-information">
        <!-- TODO:[퍼블리싱] 데이터패브릭 메인과 ui가 동일할경우 클래스명 article로 통일  -->
        <article class="portal-article w-2/6">
          <div class="portal-article-head">
            <h3 class="portal-article-title">데이터</h3>
            <p class="portal-article-text">다양한 신규/추천 데이터를 만나보세요.</p>
          </div>
          <div class="portal-article-body">
            <div class="tab">
              <ul class="tab-list">
                <li class="tab-item">
                  <button type="button" class="tab-button is-selected">
                    <span class="tab-title">신규데이터</span>
                  </button>
                </li>
                <li class="tab-item">
                  <button type="button" class="tab-button">
                    <span class="tab-title">추천데이터</span>
                  </button>
                </li>
              </ul>
            </div>
            <div class="ranking">
              <ul class="ranking-list">
                <li class="ranking-item" v-for="index in 4">
                  <a href="#" class="ranking-link" title="바로가기">
                    <span class="ranking-num" :key="index">{{ index }}</span>
                    <div class="ranking-description">
                      <span class="ranking-title"
                        >국세청_사업자등록정보 진위확인 및 상태조회 서비스서비스서비스서비스</span
                      >
                      <span class="ranking-text"
                        >세청에서 제공하는 사업자등록정보 진위확인 및 사업자등록 상태조회 상태조회</span
                      >
                    </div>
                  </a>
                </li>
              </ul>
              <div class="flex justify-end">
                <baseButton class="button-link-tertiary">더보기</baseButton>
              </div>
            </div>
          </div>
        </article>
        <article class="portal-article w-2/6">
          <div class="portal-article-head">
            <h3 class="portal-article-title">서비스</h3>
            <p class="portal-article-text">데이터패브릭 포털은 다양한 서비스와 결합하여 최적의 환경을 제공합니다.</p>
          </div>
          <div class="portal-article-body">
            <div class="swiper">
              <div class="swiper-results">
                <span class="results-page">
                  <em>1</em>
                  / 5
                </span>
                <baseButton title="재생" class="button-sm button-icon button-link">
                  <svg-icon class="svg-icon" name="resume" />
                </baseButton>
              </div>
              <div class="swiper-container">
                <div class="swiper-wrapper">
                  <div class="swiper-item">
                    <div class="bg-blue-100 flex-1 h-full">image</div>
                    <div class="v-group gap-4 flex-1">
                      <div class="swiper-item-title">IRIS Studio (분석 리포트 저작 도구)</div>
                      <span class="swiper-item-text">
                        No Code, Less Code를 기반으로 사용자가 원하는 다양한 유형의 대시보드/보고서를 손쉽게 작성할 수
                        있으며, 캔버스 상에 다양한 표와 차트를 옮겨 놓고 관련 데이터를 매핑함으로써 보고서를 쉽고 빠르게
                        생성할 수 있습니다.
                      </span>
                      <baseButton class="button-primary">서비스 바로가기</baseButton>
                    </div>
                  </div>
                  <div class="swiper-item"></div>
                </div>
              </div>
              <baseButton class="swiper-pre-button" title="이전">
                <svg-icon name="arrow-left" class="svg-icon" />
              </baseButton>
              <baseButton class="swiper-next-button" title="다음">
                <svg-icon name="arrow-right" class="svg-icon" />
              </baseButton>
            </div>
          </div>
        </article>
        <article class="portal-article w-2/6">
          <div class="portal-article-head">
            <h3 class="portal-article-title">커뮤니티</h3>
            <p class="portal-article-text">
              공지사항 및 이용문의를 통해서 데이터패브릭 포털을 더 잘 이용할 수 있습니다.
            </p>
          </div>
          <div class="portal-article-body">
            <div class="community">
              <div class="community-inner">
                <div class="community-head">
                  <div class="community-title">공지사항</div>
                  <baseButton class="button-link-tertiary">더보기</baseButton>
                </div>
                <div class="notices-notify">
                  <div class="notify-container">
                    <svg-icon name="speaker" class="svg-icon" />
                    <div class="notify-wrapper">
                      <a href="#" class="notify-item">
                        <em>[서비스 안내]</em>
                        <span class="notify-text">분석 서비스를 위한 저작도구 신규 서비스 도입</span>
                      </a>
                      <a href="#" class="notify-item">
                        <em>[서비스 안내]</em>
                        <span class="notify-text">분석 서비스를 위한 저작도구 신규 서비스 도입</span>
                      </a>
                    </div>
                  </div>
                  <div class="paging">
                    <ul class="paging-dots">
                      <li class="dot is-active">
                        <button class="dot-button">1</button>
                      </li>
                      <li class="dot">
                        <button class="dot-button">2</button>
                      </li>
                      <li class="dot">
                        <button class="dot-button">3</button>
                      </li>
                    </ul>
                  </div>
                </div>
                <div class="mt-7">
                  <ul class="community-list">
                    <li class="community-item" v-for="index in 6">
                      <baseBadge class="bg-marker-cyan">안내</baseBadge>
                      <a href="#" class="community-item-link"> [신규 인프라] 이미지 지원 종료 안내(2023/09/26) </a>
                    </li>
                  </ul>
                </div>
              </div>
              <div class="community-inner">
                <div class="community-head">
                  <div class="community-title">
                    자주하는 질문
                    <em>FAQ</em>
                  </div>
                  <baseButton class="button-link-tertiary">더보기</baseButton>
                </div>
                <div class="mt-0.5">
                  <ul class="community-list">
                    <li class="community-item" v-for="index in 8">
                      <a href="#" class="community-item-link">
                        [서비스문의] 서비스 신규 신청은 어떻게 진행하면 될까요?
                      </a>
                    </li>
                  </ul>
                </div>
              </div>
            </div>
          </div>
        </article>
      </div>
    </section>
  </div>
</template>
<script lang="ts" setup>
definePageMeta({
  layout: "default-portal-layout"
});
</script>
