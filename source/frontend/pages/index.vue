<template>
  <h3>모달 샘플입니다</h3>
  <button class="button button--primary" @click="onClickOpen">모달 OPEN</button>
  <modal v-model="show" title="모달 샘플"></modal>
</template>
<script lang="ts" setup>
import {ref} from "@vue/reactivity";
import Modal from "../components/modal.vue"
import {useRouter} from "vue-router";

const show = ref(false);
const router = useRouter();

function onClickOpen() {
  show.value = !show.value;
}
function onClickMain() {
  router.push("/main")
}
</script>
