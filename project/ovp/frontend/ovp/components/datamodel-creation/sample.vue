<template>
  <div class="work-list">
    <!-- TODO: [개발] tab 컴포넌트  -->
    <div class="tab tab-line">
      <ul class="tab-list">
        <li class="tab-item is-tab-item-selected">
          <button class="tab-button">
            <p class="tab-button-text">샘플데이터</p>
          </button>
        </li>
        <li class="tab-item">
          <button class="tab-button">
            <p class="tab-button-text">데이터 프로파일링</p>
          </button>
        </li>
      </ul>
      <div class="tab-contents">
        <div class="data-summary">
          <span class="data-summary-title">데이터 모델 요약</span>
          <span class="data-summary-name"
            >데이터 모델 길어지면 말줄임 합니다</span
          >
          <span class="data-summary-text">(소유자)</span>
        </div>
        <!-- 샘플데이터 탭 시작 -->
        <div class="table-scroll" style="display: none">
          <table>
            <thead>
              <tr>
                <th>NAME</th>
                <th>DATA TYPE</th>
                <th>NAME</th>
                <th>DATA TYPE</th>
                <th>NAME</th>
                <th>DATA TYPE</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td>항목</td>
                <td>항목</td>
                <td>항목</td>
                <td>항목</td>
                <td>항목</td>
                <td>항목</td>
              </tr>
              <tr>
                <td>항목</td>
                <td>항목</td>
                <td>항목</td>
                <td>항목</td>
                <td>항목</td>
                <td>항목</td>
              </tr>
              <tr>
                <td>항목</td>
                <td>항목</td>
                <td>항목</td>
                <td>항목항목항목항목항목항목항목항목</td>
                <td>항목</td>
                <td>항목</td>
              </tr>
              <tr>
                <td>항목</td>
                <td>항목</td>
                <td>항목</td>
                <td>항목</td>
                <td>항목</td>
                <td>항목</td>
              </tr>
            </tbody>
          </table>
        </div>
        <!-- 샘플데이터 탭 끝 -->
        <!-- 데이터 프로파일링 탭 시작 -->
        <div class="profiling-detail">
          <span class="profiling-detail-title">컬럼 상세</span>
          <select-box class="select-sm"></select-box>
          <div class="profiling-list">
            <dl>
              <dt>DATA TYPE</dt>
              <dd>integer</dd>
            </dl>
            <dl>
              <dt>Null %</dt>
              <dd>0%</dd>
            </dl>
            <dl>
              <dt>Null %</dt>
              <dd>0%</dd>
            </dl>
            <dl>
              <dt>Null %</dt>
              <dd>0%</dd>
            </dl>
          </div>
        </div>
        <!-- 결과 없을 시 no-result 표시 -->
        <div class="no-result" style="display: none">
          <div class="notification">
            <svg-icon class="notification-icon" name="info"></svg-icon>
            <p class="notification-detail">샘플 데이터가 없습니다.</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts"></script>
<style lang="scss" scoped>
/* @import "./index.scss"; */
</style>
