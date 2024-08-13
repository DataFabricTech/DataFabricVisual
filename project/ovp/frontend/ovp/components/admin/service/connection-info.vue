<template>
  <div class="px-4">
    <div class="flex justify-end gap-2">
      <button class="button button-neutral-stroke">연결정보 수정</button>
      <button class="button button-secondary">연결 테스트</button>
    </div>
    <table class="mt-3">
      <colgroup>
        <col style="width: 20%" />
        <col />
      </colgroup>
      <tr>
        <th>scheme</th>
        <td>mysal+pymsal</td>
      </tr>
      <tr>
        <th>username</th>
        <td>root</td>
      </tr>
      <tr>
        <th>password</th>
        <td>******</td>
      </tr>
      <tr>
        <th>hostPort</th>
        <td>192.168.105.81:30001</td>
      </tr>
      <tr>
        <th>databaseName</th>
        <td>ABC</td>
      </tr>
      <tr>
        <th>databaseSchema</th>
        <td></td>
      </tr>
      <tr>
        <th>prefix</th>
        <td></td>
      </tr>
      <tr>
        <th>bucketName</th>
        <td></td>
      </tr>
      <tr>
        <th>overwirteData</th>
        <td>true</td>
      </tr>
      <tr>
        <th>filePathPattern</th>
        <td></td>
      </tr>
    </table>
    <!-- 결과 없을 시 no-result 표시 -->
    <div class="no-result max-h-40" style="display: none">
      <div class="notification">
        <svg-icon class="notification-icon" name="info"></svg-icon>
        <p class="notification-detail">데이터 리스트가 없습니다.</p>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts"></script>
