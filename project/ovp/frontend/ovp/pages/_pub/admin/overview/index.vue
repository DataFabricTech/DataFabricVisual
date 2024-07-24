<template>
  <div class="section-top-bar">
    <div class="l-top-bar">
      <h3 class="title">서비스</h3>
    </div>
  </div>
  <div class="section-top-bar section-top-bar-tab">
    <div class="tab tab-line">
      <ul class="tab-list">
        <li class="tab-item is-tab-item-selected">
          <button class="tab-button">
            <p class="tab-button-text">overview</p>
          </button>
        </li>
        <li class="tab-item">
          <button class="tab-button">
            <p class="tab-button-text">서비스</p>
          </button>
        </li>
      </ul>
    </div>
  </div>
  <div class="section-contents overflow-auto">
    <div class="overview">
      <div class="v-group gap-5">
        <h4 class="overview-title">요약 정보</h4>
        <div class="flex w-full gap-5">
          <div class="overview-summary">
            <span>서비스 타입 요약</span>
            <div class="overview-chart">

            </div>
          </div>
          <div class="overview-summary">
            <span>서비스 상태 요약</span>
            <div class="overview-chart">

            </div>
          </div>
          <div class="overview-summary">
            <span>서비스 응답시간</span>
            <div class="overview-chart p-3">
              <dl>
                <dt>서비스 CCC</dt>
                <dd>24.10 sec</dd>
              </dl>
              <dl>
                <dt>서비스 A</dt>
                <dd>22.54 sec</dd>
              </dl>
              <dl>
                <dt>서비스 F</dt>
                <dd>24.01 sec</dd>
              </dl>
            </div>
          </div>
        </div>
        <div class="flex w-full gap-5">
          <div class="overview-summary">
            <span>데이터 모델 추천 순위</span>
            <div class="overview-chart">

            </div>
          </div>
          <div class="overview-summary">
            <span>데이터 모델 등록 현황</span>
            <div class="overview-chart">

            </div>
          </div>
        </div>
      </div>
      <div class="v-group gap-5">
        <h4 class="overview-title">최근 등록/수정된 서비스</h4>
        <table>
          <thead>
          <tr>
            <th>서비스 이름</th>
            <th>데이터 저장소 유형</th>
            <th>상태</th>
            <th>등록일시</th>
            <th>수정일시</th>
            <th>내용</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="tr in 6" :key="tr">
            <td>서비스 B</td>
            <td>MySQL</td>
            <td>Disconnected</td>
            <td>2023-01-02 12:10:00</td>
            <td>2023-01-02 12:10:0</td>
            <td>수정</td>
          </tr>
          </tbody>
        </table>
      </div>
      <div class="v-group gap-5">
        <h4 class="overview-title">히스토리</h4>
        <table>
          <thead>
          <tr>
            <th>이벤트 발생 일시</th>
            <th>이벤트</th>
            <th>서비스 이름</th>
            <th>저장소 유형</th>
            <th>내용</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="tr in 6" :key="tr">
            <td>2023-01-02 12:10:00</td>
            <td>동기화 완료</td>
            <td>서비스 G</td>
            <td>MySQL</td>
            <td>데이터 모델 신규 등록</td>
          </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script>

</script>

<style scoped>

</style>