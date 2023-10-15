<!-- 저장소 관리 > 연결정보 등록 Modal -->
<template>
  <div class="v-group">
    <h3>저장소 관리 > 연결정보 등록 Step 1</h3>
    <baseButton class="button-lg button-primary" @click="onClickOpen1">모달 OPEN</baseButton>
    <modal v-model="show1" title="연결정보 등록 Step 1">
      <template v-slot:body>
        Lorem ipsum dolor sit amet, consectetur adipisicing elit. Aspernatur aut, debitis doloribus ipsum possimus quae.
      </template>
    </modal>

    <h3>저장소 관리 > 연결정보 등록 Step 2</h3>
    <baseButton class="button-lg button-primary" @click="onClickOpen2">모달 OPEN</baseButton>
    <modal v-model="show2" title="연결정보 등록 Step 2">
      <template v-slot:body>
        가나다라마바사~가나다라마바사~가나다라마바사~가나다라마바사~
      </template>
    </modal>
  </div>
</template>
<script lang="ts" setup>
import { ref } from "@vue/reactivity";
import { useRouter } from "vue-router";

definePageMeta({
  layout: "default-empty-layout",
});

const show1 = ref(false);
const show2 = ref(false);
const router = useRouter();

function onClickOpen1() {
  show1.value = !show1.value;
}

function onClickOpen2() {
  show2.value = !show2.value;
}

function onClickMain() {
  router.push("/main");
}

</script>
