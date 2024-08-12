<template>
  <div class="l-top-bar">
    <!-- TODO 모달 용어 추가 -->
    <button class="button button-secondary ml-auto" @click="showModalPwChange">
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
    <tr v-for="term in terms">
      <td>{{ term.name }}</td>
      <td>{{ term.description }}</td>
      <td>
        <div class="button-group">
          <button
            class="button button button-secondary-stroke"
            @click="onClickTerm(term)"
          >
            편집
          </button>
          <!-- TODO: 삭제 모달 -->
          <button class="button button button-error-stroke">삭제</button>
        </div>
      </td>
    </tr>
  </table>
  <modal-glossary :modal-id="''"></modal-glossary>
</template>

<script setup lang="ts">
import { useGlossaryStore } from "@/store/glossary";
import type { Term } from "~/type/glossary";
const { terms, openEditTermComponent, changeCurrentTerm } = useGlossaryStore();

function onClickTerm(source: Term) {
  changeCurrentTerm(source);
  openEditTermComponent("term");
}
const showModalPwChange: () => void = () => {};
</script>
