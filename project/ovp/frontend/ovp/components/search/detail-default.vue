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
    >
      <svg-icon class="button-icon" name="trash"></svg-icon>
      삭제
    </button>
  </div>
</template>

<script setup lang="ts">
import { storeToRefs } from "pinia";

import { useDataModelDetailStore } from "@/store/search/detail/index";
import { useUserStore } from "@/store/user/userStore";

const dataModelDetailStore = useDataModelDetailStore();
const userStore = useUserStore();

const { changeVote, changeFollow } = dataModelDetailStore;
const { dataModel } = storeToRefs(dataModelDetailStore);

const { user } = storeToRefs(userStore);

const changeUpVote = async () => {
  const state: string = dataModel.value.isUpVote ? "unVoted" : "votedUp";
  await changeVote(state);
};
const changeDownVote = async () => {
  const state: string = dataModel.value.isDownVote ? "unVoted" : "votedDown";
  await changeVote(state);
};
function copyLink() {
  navigator.clipboard.writeText(window.location.href);
  alert("링크가 복사되었습니다.");
}
</script>

<style lang="scss" scoped></style>
