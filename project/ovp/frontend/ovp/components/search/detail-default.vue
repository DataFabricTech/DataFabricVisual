<template>
  <div class="interaction-buttons">
    <div class="button-group button-group-sm">
      <input
        type="checkbox"
        id="button-radio-count"
        class="button-group-input"
        name="button-group2"
        v-model="dataModel.isUpVote"
        @click="changeUpVote"
      />
      <label for="button-radio-count" class="button-group-label">
        <svg-icon class="svg-icon" name="thumbs-up"></svg-icon>
        <span class="count-text">추천</span>
        <span class="count-num">{{ dataModel.upVotes }}</span>
      </label>
      <input
        type="checkbox"
        id="button-radio-count-2"
        class="button-group-input"
        name="button-group2"
        v-model="dataModel.isDownVote"
        @click="changeDownVote"
      />
      <label for="button-radio-count-2" class="button-group-label">
        <svg-icon class="svg-icon" name="thumbs-down"></svg-icon>
        <span class="count-text">비추천</span>
        <span class="count-num">{{ dataModel.downVotes }}</span>
      </label>
    </div>
    <div class="checkbox-button checkbox-button-sm">
      <input
        type="checkbox"
        id="checkbox3"
        class="checkbox-button-input"
        v-model="dataModel.isFollow"
        @click="changeFollow"
      />
      <label for="checkbox3" class="checkbox-button-label">
        <svg-icon class="svg-icon" name="tag"></svg-icon>
        <span class="count-text">북마크</span>
        <span class="count-num">{{ dataModel.followers }}</span>
      </label>
    </div>
    <button class="button button-neutral-stroke button-sm" @click="copyLink()">
      <svg-icon class="button-icon" name="copy"></svg-icon>
      링크
    </button>
    <button
      class="button button-error-stroke button-sm"
      v-show="dataModel.owner?.id === user.id || user.admin"
      @click="deleteDataModel()"
    >
      <svg-icon class="button-icon" name="trash"></svg-icon>
      삭제
    </button>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from "vue-router";
import { useClipboard } from "@vueuse/core";
import { useNuxtApp } from "nuxt/app";
import { storeToRefs } from "pinia";

import { useDataModelDetailStore } from "@/store/search/detail/index";
import { useUserStore } from "@/store/user/userStore";

const router = useRouter();

const dataModelDetailStore = useDataModelDetailStore();
const userStore = useUserStore();

const { changeVote, changeFollow, removeDataModel } = dataModelDetailStore;
const { dataModel } = storeToRefs(dataModelDetailStore);

const { $alert, $confirm } = useNuxtApp();

const { user } = storeToRefs(userStore);
const currentUrl = ref("");
const { copy } = useClipboard({
  source: currentUrl,
  legacy: true,
});

const changeUpVote = async () => {
  const state: string = dataModel.value.isUpVote ? "unVoted" : "votedUp";
  await changeVote(state);
};
const changeDownVote = async () => {
  const state: string = dataModel.value.isDownVote ? "unVoted" : "votedDown";
  await changeVote(state);
};
function copyLink() {
  getURL();
  copy(currentUrl.value);
  $alert("링크가 복사되었습니다.");
}

async function deleteDataModel() {
  if (await $confirm("데이터모델을 삭제 하시겠습니까?")) {
    removeDataModel()
      .then((data) => {
        if (data.result === 1) {
          $alert("삭제되었습니다.");
          router.push("/portal/search");
        } else {
          $alert("삭제 실패했습니다. 잠시 후 다시 시도해주세요.");
        }
      })
      .catch((error) => {
        console.error(error);
      });
  }
}

// 메서드 정의
const getURL = () => {
  if (typeof window !== "undefined") {
    currentUrl.value = window.location.href; // 클라이언트 측에서 URL을 가져옴
  } else {
    console.error("window 객체를 찾을 수 없습니다.");
  }
};
</script>

<style lang="scss" scoped></style>
