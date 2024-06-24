<template>
  <div class="card">
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
    <span class="card-desc">한국교통안전공단에서 교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다.</span>
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
  </div>
</template>

<script>
export default {
  name: "card"
};
</script>

<style scoped>

</style>