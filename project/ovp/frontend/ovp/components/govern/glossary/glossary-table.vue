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
  <div ref="scrollTrigger" class="w-full h-[1px] mt-px"></div>
  <Loading
    id="loader"
    :use-loader-overlay="true"
    class="loader-lg is-loader-inner"
    style="display: none"
  ></Loading>
  <modal-glossary :modal-id="MODAL_ID"></modal-glossary>
</template>

<script setup lang="ts">
import { watch, onMounted } from "vue";
import { useGlossaryStore } from "@/store/glossary";
import type { Term } from "~/type/glossary";
import Loading from "@base/loading/Loading.vue";
import { useIntersectionObserver } from "~/composables/intersectionObserverHelper";
const {
  glossary,
  terms,
  openEditTermComponent,
  resetTerms,
  changeCurrentTerm,
  deleteTerm,
  getTerms,
} = useGlossaryStore();
const store = useGlossaryStore();
const { $vfm } = useNuxtApp();

onMounted(() => getTerms());
watch(
  () => store.glossary,
  () => {
    resetTerms();
    getTerms();
  },
  { deep: true },
);

function onClickTerm(source: Term): void {
  changeCurrentTerm(source);
  openEditTermComponent("term");
}

const { scrollTrigger } = useIntersectionObserver(getTerms);

async function removeTerm(id: string): Promise<void> {
  if (confirm("용어를 삭제 하시겠습니까?")) {
    try {
      await deleteTerm(id);
    } catch (error) {
      alert(error);
    } finally {
      await getTerms();
    }
  }
}

const MODAL_ID = "modal-glossary";
function openModal(): void {
  $vfm.open(MODAL_ID);
}
</script>
