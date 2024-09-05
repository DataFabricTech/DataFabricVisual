<template>
  <div class="l-top-bar">
    <button class="button button-secondary ml-auto" @click="openModal">
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
          <button
            class="button button button-error-stroke"
            @click="removeTerm(term.id)"
          >
            삭제
          </button>
        </div>
      </td>
    </tr>
  </table>
  <modal-glossary :modal-id="MODAL_ID"></modal-glossary>
</template>

<script setup lang="ts">
import { useGlossaryStore } from "@/store/glossary";
import type { Term } from "~/type/glossary";
const {
  glossary,
  terms,
  openEditTermComponent,
  changeCurrentTerm,
  deleteTerm,
  getTerms,
} = useGlossaryStore();
const { $vfm } = useNuxtApp();

function onClickTerm(source: Term) {
  changeCurrentTerm(source);
  openEditTermComponent("term");
}

async function removeTerm(id: string) {
  if (confirm("데이터모델을 삭제 하시겠습니까?")) {
    await deleteTerm(id);
    await getTerms(glossary.name);
  }
}

const MODAL_ID = "modal-glossary";
function openModal() {
  $vfm.open(MODAL_ID);
}
</script>
