<template>
  <!--  용어 탭 시작  -->
  <div class="l-top-bar">
    <button class="button button-secondary ml-auto" @click="showModal = true">
      용어추가
    </button>
  </div>
  <table class="align-center">
    <colgroup>
      <col style="width: 25%" />
      <col style="width: 25%" />
      <col style="width: 25%" />
    </colgroup>
    <tr>
      <th>용어</th>
      <th>설명</th>
      <th>관리</th>
    </tr>
    <tr>
      <td>용어 01</td>
      <td>용어에 대한 설명입니다.</td>
      <td>
        <div class="button-group">
          <button class="button button button-secondary-stroke">편집</button>
          <button class="button button button-error-stroke">삭제</button>
        </div>
      </td>
    </tr>
    <tr>
      <td>용어 02</td>
      <td>용어에 대한 설명입니다.</td>
      <td>
        <div class="button-group">
          <button class="button button button-secondary-stroke">편집</button>
          <button class="button button button-error-stroke">삭제</button>
        </div>
      </td>
    </tr>
  </table>
  <!--  용어 탭 끝  -->
  <modal-glossary></modal-glossary>
</template>
<script setup lang="ts"></script>
