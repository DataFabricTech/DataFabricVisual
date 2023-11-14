<template>
  <article class="category-article">
    <div class="category-subject is-vertical">
      <h3 class="category-title color-primary">별점 리뷰</h3>
      <p class="category-description">데이터 모델 상세 설명 영역 (작성된 값이 있는 경우 출력 되는 영역임)</p>
    </div>
    <div class="review">
      <div class="review-header">
        <strong class="review-title">별점 <em class="color-negative">4</em></strong>
        <span class="review-count">(1명)</span>
      </div>
      <div class="review-body">
        <div class="review-setting">
          <div class="review-rating">
            <baseRating></baseRating>
          </div>
          <div class="h-group">
            <span class="review-id">admin</span>
          </div>
        </div>
        <div class="review-textarea">
          <baseTextarea
            class="min-h-[110px]"
            placeholder="권리침해, 욕설 특정소유자를 비방하는 내용 및 개인정보가 포함된 내용을 게시하는 경우 운영정책과 이용약관 및 관련 법률에 의해 게시물의 제재 및 사전 통보 없이 삭제 될 수 있습니다."
          ></baseTextarea>
          <span class="review-limit">30/500</span>
        </div>
        <div class="h-group justify-end">
          <baseButton class="button-primary">
            <span class="button-text">등록</span>
          </baseButton>
        </div>
      </div>
    </div>
    <div class="review">
      <div class="review-header">
        <strong class="review-title">별점 <em class="color-negative">4</em></strong>
        <span class="review-count">(1명)</span>
      </div>
      <div class="review-body">
        <div class="review-setting">
          <div class="review-rating">
            <baseRating :star="2.0" :disabled="true"></baseRating>
          </div>
          <div class="h-group">
            <span class="review-id">admin</span>
            <baseButton class="button-sm button-link-primary">
              <span class="button-text">수정하기</span>
            </baseButton>
            <baseButton class="button-sm button-link-negative">
              <span class="button-text">삭제하기</span>
            </baseButton>
          </div>
        </div>
        <p class="review-detail">별점 리뷰합니다.</p>
      </div>
    </div>
    <div class="review">
      <div class="review-header">
        <strong class="review-title">별점 <em class="color-negative">4</em></strong>
        <span class="review-count">(1명)</span>
      </div>
      <div class="review-body">
        <div class="review-setting">
          <div class="review-rating">
            <baseRating></baseRating>
          </div>
          <div class="h-group">
            <span class="review-id">admin</span>
          </div>
        </div>
        <div class="review-textarea">
          <baseTextarea
            class="min-h-[110px]"
            value="별점 리뷰합니다."
            placeholder="권리침해, 욕설 특정소유자를 비방하는 내용 및 개인정보가 포함된 내용을 게시하는 경우 운영정책과 이용약관 및 관련 법률에 의해 게시물의 제재 및 사전 통보 없이 삭제 될 수 있습니다."
          ></baseTextarea>
          <span class="review-limit">30/500</span>
        </div>
        <div class="h-group justify-end">
          <div class="h-group ml-auto">
            <baseButton class="button-tertiary">
              <span class="button-text">수정</span>
            </baseButton>
            <baseButton class="button-negative">
              <span class="button-text">초기화</span>
            </baseButton>
          </div>
        </div>
      </div>
    </div>
    <div class="review">
      <div class="review-header">
        <strong class="review-title">별점 <em class="color-negative">4</em></strong>
        <span class="review-count">(1명)</span>
      </div>
      <div class="review-body">
        <div class="review-setting">
          <div class="review-rating">
            <baseRating></baseRating>
          </div>
        </div>
        <div class="h-group justify-end">
          <baseButton class="button-primary">
            <span class="button-text">로그인</span>
          </baseButton>
        </div>
      </div>
    </div>
    <div class="review">
      <div class="review-header">
        <strong class="review-title">별점 <em class="color-negative">4</em></strong>
        <span class="review-count">(1명)</span>
      </div>
      <div class="review-body">
        <div class="review-setting">
          <div class="review-rating">
            <baseRating></baseRating>
          </div>
          <div class="h-group">
            <span class="review-id">admin</span>
            <baseButton class="button-sm button-link-primary">
              <span class="button-text">수정하기</span>
            </baseButton>
            <baseButton class="button-sm button-link-negative">
              <span class="button-text">삭제하기</span>
            </baseButton>
          </div>
        </div>
        <div class="no-result border-none">
          <storng class="no-result-title">등록된 리뷰가 없습니다.</storng>
        </div>
      </div>
    </div>
  </article>
</template>

<script lang="ts" setup></script>
