<template>
  <div class="px-4">
    <div class="v-group gap-2">
      <div class="font-semibold text-neutral-700">설명</div>
      <div class="editable-group">
        <span class="editable-group-desc">
          서비스 A에 대한 설명이 기재됩니다.
        </span>
        <button class="button button-neutral-ghost button-sm" type="button">
          <span class="hidden-text">수정</span>
          <svg-icon class="svg-icon" name="pen"></svg-icon>
        </button>
      </div>
    </div>
    <table class="mt-4">
      <colgroup>
        <col />
        <col />
        <col style="width: 35%" />
      </colgroup>
      <tr>
        <th>구분</th>
        <th class="align-center">이름</th>
        <th>설명</th>
        <th>소유자</th>
        <th>USAGE</th>
      </tr>
      <tr v-for="tr in 3" :key="tr">
        <td>DB기본정보</td>
        <td>
          <a
            href=""
            class="link-button link-button link-button-underline"
            title="상세 페이지 이동"
            >DB Table1</a
          >
        </td>
        <td>DB Table에 대한 설명입니다.</td>
        <td>-</td>
        <td>0th pctile</td>
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
