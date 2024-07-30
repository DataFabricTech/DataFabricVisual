<template>
  <div class="work-page">
    <div class="l-top-bar py-3">
      <div class="h-group gap-2">
        <span class="font-semibold">실행 결과</span>
        <!-- TODO: [개발] 실행 성공시 .badge-green으로 변경, icon name="success"로 변경-->
        <div class="badge badge-red">
          <svg-icon class="badge-icon" name="error"></svg-icon>
          <p class="badge-text">error</p>
        </div>
      </div>
      <div class="result-info">
        <dl class="h-group gap-3">
          <dt>
            <div class="badge badge-secondary-lighter">
              <p class="badge-text">실행시간</p>
            </div>
          </dt>
          <dd>103ms</dd>
        </dl>
        <dl class="h-group gap-3">
          <dt>
            <div class="badge badge-secondary-lighter">
              <p class="badge-text">실행시각</p>
            </div>
          </dt>
          <dd>2024-06-05 13:23:53</dd>
        </dl>
        <dl class="h-group gap-3">
          <dt>
            <div class="badge badge-secondary-lighter">
              <p class="badge-text">레코드 수</p>
            </div>
          </dt>
          <dd></dd>
        </dl>
      </div>
    </div>
    <!-- TODO: [개발] 실행 성공시-->
    <div class="result">
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
    <!-- TODO: [개발] 실행 error의 경우-->
    <div class="result result-error" style="display: none">
      <p>
        Line 1 ~ 6 : Unknown error. ( TableNotExistsError() [/*+ LOCATION (
        PARTITION >= '20240605131200' AND PARTITION <= '20240605131500' ) */
        SELECT CATEGORY, BOUNDARY,SIDO_ENG, SIDO_KOR G_CO FROM ROOT.DTST limit
        5000;] )
      </p>
    </div>
  </div>
</template>
<script setup lang="ts"></script>
<style lang="scss" scoped>
/* @import "./index.scss"; */
</style>
