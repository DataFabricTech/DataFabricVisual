<template>
  <div class="board-wrap">
    <div class="board-head">
        <strong v-if="props.isAdmin" class="board-title">{{ props.share.title }}</strong>
        <div class="board-desc">
            <div class="data-info">
              <dl>
                <dt v-if="props.isAdmin">등록자</dt>
                <dd v-if="props.isAdmin">{{ props.share.user_nm }}</dd>
                <dt>등록일</dt>
                <dd>{{ props.share.create_date }}</dd>
              </dl>
            </div>
        </div>
    </div>
    <div class="board-body">
      <!-- inner-cms : cms영역 -->
      <div class="inner-cms">
        <p class="inner-cms-share" v-html="props.share.contents"></p>
      </div>
      <!-- //inner-cms : cms영역 -->
      <!-- <slot name="thumb"></slot> -->
      <div v-if=" props.fileList.length " class="file-sch-box">
        <ul class="file-sch-list">
          <li v-for="(item, index) in props.fileList" :key="index">
            <a @click="fileDownload(item.file_id)">
              <i class="mu-icon-svg file">첨부파일있음</i>{{ item.name }}
            </a>
          </li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import ObjectUtils from "@/utils/ObjectUtils";
import { useNuxtApp, useRuntimeConfig } from "nuxt/app";
import { useUserStore } from "~/store/user";

const { $ofetch } = useNuxtApp();
const userStore = useUserStore();

const props = defineProps(["share", "fileList", "isAdmin"]);

// 첨부파일 다운로드
const fileDownload = async (file_id: number): Promise<void> => {
  const config = useRuntimeConfig();

  const res = await $ofetch(`/api/service/file/fileDownload?fileId=${file_id}`, {
    headers: { responseType: "blob" },
  });

  const errorMessage = ObjectUtils.getPropertyValue(res, "data/errorMessage", "");
  if (!errorMessage) {
    const link = document.createElement("a");
    let url = "";

    if (document.location.href.indexOf("localhost") > -1) {
      url = config.public.baseURL + `api/service/file/fileDownload?fileId=${file_id}`;
    } else url = `/api/service/file/fileDownload?fileId=${file_id}`;

    link.href = url;
    link.style.cssText = "display:none";
    document.body.appendChild(link);
    link.click();
    link.remove();
  }
};
</script>
<style>
.inner-cms-share img {
  width: unset;
}
</style>
