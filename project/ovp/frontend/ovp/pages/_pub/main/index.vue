<template>
  <!--  TODO: [개발] 권한 없는 메뉴 진입시 -->
  <div class="section-contents" style="diplay:none;">
    <div class="no-result no-result-lg">
      <svg-icon class="svg-icon" name="denied"></svg-icon>
      <div class="no-result-group">
        <span class="no-result-title">접근할 수 없습니다.</span>
        <span class="no-result-desc">권한을 얻으려면 관리자에게 문의하세요.</span>
      </div>
    </div>
  </div>
  <div class="section-contents">
    <div class="l-split">
      <div class="main-content">
        <div class="l-top-bar">
          <span class="main-content-title">최근 탐색 데이터</span>
          <button class="button link-button-support">
            <span class="button-title">모두 보기</span>
          </button>
        </div>
        <!-- 최대 3개까지 표시 -->
        <template v-for="card in 3" :key="card">
          <!--  TODO: [개발] resource-box 컴포넌트 적용 -->
          <div class="resource-box">
            <div class="resource-box-function">
              <div class="resource-box-model">
                <img/>
                <div class="breadcrumb">
                  <ul class="breadcrumb-list">
                    <li class="breadcrumb-item">
                      <span class="breadcrumb-text">1depth</span>
                    </li>
                    <li class="breadcrumb-item">
                      <span class="breadcrumb-text">2depth</span>
                    </li>
                    <li class="breadcrumb-item">
                      <span class="breadcrumb-text">data</span>
                    </li>
                  </ul>
                </div>
              </div>
            </div>
            <div class="resource-box-initial-name">최초 데이터 이름</div>
            <div class="editable-group">
              <a title="상세 보기" href="" class="editable-group-title">세종특별자치시 상하수도요금표</a>
              <span class="editable-group-desc">한국교통안전공단에서 교통카드를 한국교통안전공단에서 교통카드를한국교통안전공단에서 교통카드를한국교통안전공단에서 교통카드를한국교통안전공단에서 교통카드를한국교통안전공단에서 교통카드를한국교통안전공단에서 교통카드를한국교통안전공단에서 교통카드를</span>
            </div>
            <div class="resource-box-info">
              <dl class="resource-box-list">
                <dt>소유자</dt>
                <dd>Owner</dd>
              </dl>
              <dl class="resource-box-list">
                <dt>도메인</dt>
                <dd>Domain</dd>
              </dl>
            </div>
          </div>
        </template>
      </div>
      <div class="main-content">
        <div class="l-top-bar">
          <span class="main-content-title">북마크 한 데이터</span>
          <button class="button link-button-support">
            <span class="button-title">모두 보기</span>
          </button>
        </div>
        <template v-for="card in 2" :key="card">
          <!--  TODO: [개발] resource-box 컴포넌트 적용 -->
          <div class="resource-box">
            <div class="resource-box-function">
              <div class="resource-box-model">
                <img/>
                <div class="breadcrumb">
                  <ul class="breadcrumb-list">
                    <li class="breadcrumb-item">
                      <span class="breadcrumb-text">1depth</span>
                    </li>
                    <li class="breadcrumb-item">
                      <span class="breadcrumb-text">2depth</span>
                    </li>
                    <li class="breadcrumb-item">
                      <span class="breadcrumb-text">data</span>
                    </li>
                  </ul>
                </div>
              </div>
            </div>
            <div class="resource-box-initial-name">최초 데이터 이름</div>
            <div class="editable-group">
              <a title="상세 보기" href="" class="editable-group-title">세종특별자치시 상하수도요금표</a>
              <span class="editable-group-desc">한국교통안전공단에서 교통카드를 한국교통안전공단에서 교통카드를한국교통안전공단에서 교통카드를한국교통안전공단에서 교통카드를한국교통안전공단에서 교통카드를한국교통안전공단에서 교통카드를한국교통안전공단에서 교통카드를한국교통안전공단에서 교통카드를</span>
            </div>
            <div class="resource-box-info">
              <dl class="resource-box-list">
                <dt>소유자</dt>
                <dd>Owner</dd>
              </dl>
              <dl class="resource-box-list">
                <dt>도메인</dt>
                <dd>Domain</dd>
              </dl>
            </div>
          </div>
        </template>
      </div>
    </div>

    <div class="l-split">
      <div class="main-content">
        <div class="l-top-bar">
          <span class="main-content-title">추천 많은 데이터</span>
          <button class="button link-button-support">
            <span class="button-title">모두 보기</span>
          </button>
        </div>
        <template v-for="card in 2" :key="card">
          <!--  TODO: [개발] resource-box 컴포넌트 적용 -->
          <div class="resource-box">
            <div class="resource-box-function">
              <div class="resource-box-model">
                <img/>
                <div class="breadcrumb">
                  <ul class="breadcrumb-list">
                    <li class="breadcrumb-item">
                      <span class="breadcrumb-text">1depth</span>
                    </li>
                    <li class="breadcrumb-item">
                      <span class="breadcrumb-text">2depth</span>
                    </li>
                    <li class="breadcrumb-item">
                      <span class="breadcrumb-text">data</span>
                    </li>
                  </ul>
                </div>
              </div>
            </div>
            <div class="resource-box-initial-name">최초 데이터 이름</div>
            <div class="editable-group">
              <a title="상세 보기" href="" class="editable-group-title">세종특별자치시 상하수도요금표</a>
              <span class="editable-group-desc">한국교통안전공단에서 교통카드를 한국교통안전공단에서 교통카드를한국교통안전공단에서 교통카드를한국교통안전공단에서 교통카드를한국교통안전공단에서 교통카드를한국교통안전공단에서 교통카드를한국교통안전공단에서 교통카드를한국교통안전공단에서 교통카드를</span>
            </div>
            <div class="resource-box-info">
              <dl class="resource-box-list">
                <dt>소유자</dt>
                <dd>Owner</dd>
              </dl>
              <dl class="resource-box-list">
                <dt>도메인</dt>
                <dd>Domain</dd>
              </dl>
            </div>
          </div>
        </template>
      </div>
      <div class="main-content">
        <div class="l-top-bar">
          <span class="main-content-title">나의 데이터</span>
          <button class="button link-button-support">
            <span class="button-title">모두 보기</span>
          </button>
        </div>
        <!-- 결과 없을 시 no-result 표시 -->
        <div class="no-result">
          <div class="notification">
            <svg-icon class="notification-icon" name="info"></svg-icon>
            <p class="notification-detail">출력할 정보가 없습니다.</p>
          </div>
        </div>
      </div>
    </div>


  </div>
</template>

<script lang="ts">

export default {
  name: "main",
  components: { }
};


</script>

<style scoped>
</style>