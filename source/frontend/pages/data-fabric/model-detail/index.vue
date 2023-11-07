<template>
  <div class="l-common">
    <h2 class="hidden-text">데이터모델 상세</h2>
    <BaseNotification theme="warning">
      <strong class="notification-title">
        연결정보가 변동되어 데이터모델 정보 를 다운로드 받을 수 없습니다.
      </strong>
      <a href="#" class="notification-link">
        바로가기
      </a>
    </BaseNotification>
    <section class="l-section">
      <div class="page-subject border-none">
        <h3 class="page-title">
          <span class="hidden-text">TODO: [개발] '데이터 모델 이름'이 들어가야 합니다.</span></h3>
        <div class="h-group">
          <BaseButton class="button-normal">
            <svg-icon name="edit" class="svg-icon" />
            <span class="button-text">수정</span>
          </BaseButton>
          <BaseButton class="button-primary">
            <svg-icon name="save" class="svg-icon" />
            <span class="button-text">저장</span>
          </BaseButton>
          <BaseButton class="button-danger">
            <span class="button-text">초기화</span>
          </BaseButton>
        </div>
      </div>
      <Card class="card-lg w-full"></Card>
    </section>
    <section class="l-section gap-[16px]">
      <div class="anchor">
        <a class="anchor-link" title="이동" href="#status-info">상태 정보</a>
        <a class="anchor-link" title="이동" href="#default-info">기본 정보</a>
        <a class="anchor-link" title="이동" href="#collect-info">수집 정보</a>
      </div>
      <div class="v-group gap-[48px] px-4 w-full">
        <article class="category-article">
          <div class="category-subject is-vertical">
            <h3 class="category-title color-primary">기본 정보</h3>
            <p class="category-description">데이터 모델 상세 설명 영역 (작성된 값이 있는 경우 출력 되는 영역임) </p>
          </div>
          <h4 class="page-subtitle">메타 정보</h4>
          <div class="table-row w-full">
            <table class="w-full">
              <caption class="caption-out">메타 정보</caption>
              <tbody>
              <tr>
                <th scope="row">연결정보 이름</th>
                <td>저장소 A</td>
                <th scope="row">Title2</th>
                <td>Description1-2</td>
              </tr>
              <tr>
                <th scope="row">Title3</th>
                <td>Description2-3</td>
                <th scope="row">Title4</th>
                <td>Description1-4</td>
              </tr>
              <tr>
                <th scope="row">Title5</th>
                <td>Description3-5</td>
                <th scope="row">Title6</th>
                <td>Description1-6</td>
              </tr>
              <tr>
                <th scope="row">Title7</th>
                <td>Description4-7</td>
                <th scope="row">Title8</th>
                <td>Description1-8</td>
              </tr>
              </tbody>
            </table>
          </div>
          <h4 class="page-subtitle">수집 정보</h4>
          <div class="table-row w-full">
            <table class="w-full">
              <caption class="caption-out">Description Row</caption>
              <tbody>
              <tr>
                <th scope="row">Title1</th>
                <td>Description1-1</td>
                <th scope="row">Title2</th>
                <td>Description1-2</td>
              </tr>
              </tbody>
            </table>
          </div>
          <h4 class="page-subtitle">스키마 정보</h4>
          <div class="table-col w-full">
            <table class="w-full">
              <caption class="caption-out">
                Description Column
              </caption>
              <colgroup>
                <col />
                <col />
              </colgroup>
              <thead>
              <tr>
                <!-- 세로 => col, 가로 => row -->
                <th scope="col">항목</th>
                <th scope="col">내용</th>
              </tr>
              </thead>
              <tbody>
              <tr>
                <td>연결정보이름</td>
                <td>연결정보B</td>
              </tr>
              <tr>
                <td>데이터 저장소 유형</td>
                <td>MySQL</td>
              </tr>
              <tr>
                <td>Driver (Adapter)</td>
                <td>MySQL 5</td>
              </tr>
              <tr>
                <td>Host</td>
                <td>foo.mobigen.com</td>
              </tr>
              <tr>
                <td>Port</td>
                <td>3305</td>
              </tr>
              </tbody>
            </table>
          </div>
        </article>
        <article class="category-article">
          <div class="category-subject is-vertical">
            <h3 class="category-title color-primary">Knowledge Graph</h3>
            <p class="category-description">데이터 모델 상세 설명 영역 (작성된 값이 있는 경우 출력 되는 영역임) </p>
          </div>
          <h4 class="page-subtitle">연관데이터 모델</h4>
          <div class="no-result h-[200px]">
            <storng class="no-result-title">표시할 데이터 모델이 없습니다.</storng>
          </div>
        </article>
        <article class="category-article">
          <div class="category-subject is-vertical">
            <h3 class="category-title color-primary">데이터리니지</h3>
            <p class="category-description">데이터 모델 상세 설명 영역 (작성된 값이 있는 경우 출력 되는 영역임) </p>
          </div>
          <div class="h-group w-full gap-[60px]">
            <div class="v-group gap-[12px] flex-1">
              <h4 class="page-subtitle">연관 데이터 모델</h4>
              <div class="no-result h-[200px]">
                <storng class="no-result-title">표시할 데이터 모델이 없습니다.</storng>
              </div>
            </div>
            <div class="v-group gap-[12px] flex-1">
              <h4 class="page-subtitle">추천 데이터 모델</h4>
              <div class="no-result h-[200px]">
                <storng class="no-result-title">표시할 데이터 모델이 없습니다.</storng>
              </div>
            </div>
          </div>
        </article>
        <article class="category-article">
          <div class="category-subject is-vertical">
            <h3 class="category-title color-primary">연간 / 추천 데이터모델</h3>
            <p class="category-description">데이터 모델 상세 설명 영역 (작성된 값이 있는 경우 출력 되는 영역임) </p>
          </div>
          <div class="h-group w-full gap-[60px]">
            <div class="v-group gap-[12px] flex-1">
              <h4 class="page-subtitle">연관 데이터 모델</h4>
              <ul class="card-list">
                <li class="card-item" v-for="index in 2">
                  <Card></Card>
                </li>
              </ul>
            </div>
            <div class="v-group gap-[12px] flex-1">
              <h4 class="page-subtitle">추천 데이터 모델</h4>
              <ul class="card-list">
                <li class="card-item" v-for="index in 2">
                  <Card></Card>
                </li>
              </ul>
            </div>
          </div>
        </article>
        <article class="category-article">
          <div class="category-subject is-vertical">
            <h3 class="category-title color-primary">연간 / 추천 데이터모델</h3>
            <p class="category-description">데이터 모델 상세 설명 영역 (작성된 값이 있는 경우 출력 되는 영역임) </p>
          </div>
          <div class="h-group w-full gap-[60px]">
            <div class="v-group gap-[12px] flex-1">
              <h4 class="page-subtitle">메타 정보</h4>
              <ul class="card-simple-list">
                <li class="card-simple-item" v-for="index in 3">
                  <CardSimple></CardSimple>
                </li>
              </ul>
            </div>
            <div class="v-group gap-[12px] flex-1">
              <h4 class="page-subtitle">메타 정보</h4>
              <ul class="card-simple-list">
                <li class="card-simple-item" v-for="index in 3">
                  <CardSimple></CardSimple>
                </li>
              </ul>
            </div>
          </div>
        </article>
        <article class="category-article">
          <div class="category-subject is-vertical">
            <h3 class="category-title color-primary">별점 리뷰</h3>
            <p class="category-description">데이터 모델 상세 설명 영역 (작성된 값이 있는 경우 출력 되는 영역임) </p>
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
                  <span class="review-score">4.0</span>
                </div>
                <div class="h-group">
                  <span class="review-id">admin</span>
                </div>
              </div>
              <div class="review-textarea">
                <baseTextarea
                  class="min-h-[110px]"
                  placeholder="권리침해, 욕설 특정소유자를 비방하는 내용 및 개인정보가 포함된 내용을 게시하는 경우 운영정책과 이용약관 및 관련 법률에 의해 게시물의 제재 및 사전 통보 없이 삭제 될 수 있습니다."></baseTextarea>
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
                  <baseRating></baseRating>
                  <span class="review-score">4.0</span>
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
                  <span class="review-score">4.0</span>
                </div>
                <div class="h-group">
                  <span class="review-id">admin</span>
                </div>
              </div>
              <div class="review-textarea">
                <baseTextarea
                  class="min-h-[110px]"
                  value="별점 리뷰합니다."
                  placeholder="권리침해, 욕설 특정소유자를 비방하는 내용 및 개인정보가 포함된 내용을 게시하는 경우 운영정책과 이용약관 및 관련 법률에 의해 게시물의 제재 및 사전 통보 없이 삭제 될 수 있습니다."></baseTextarea>
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
                  <span class="review-score">4.0</span>
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
                  <span class="review-score">4.0</span>
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
      </div>
    </section>
  </div>
  <ScrollTop></ScrollTop>
</template>
<script lang="ts" setup>

definePageMeta({
  layout: "default-layout"
});

const state = reactive({
  isOpen: true
});
</script>
