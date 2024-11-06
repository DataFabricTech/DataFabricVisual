<template>
  <div class="board-wrap">
    <div v-if="props.isAdmin" class="board-head">
        <strong class="board-title">{{ props.post.title }}</strong>
        <div class="board-desc">
            <div class="data-info">
              <dl>
                <dt>등록자</dt>
                <dd>{{ props.post.user_nm }}</dd>
                <dt>등록일</dt>
                <dd>{{ props.post.upload_date }}</dd>
                <dt>조회수</dt>
                <dd>{{ props.post.view_count }}</dd>
              </dl>
            </div>
        </div>
    </div>
    <div v-else class="board-head">
      <div class="board-desc">
        <div class="data-info">
            <dl>
              <dt>등록자</dt>
              <dd>{{ props.post.user_nm }}</dd>
              <dt>등록일</dt>
              <dd>{{ props.post.upload_date }}</dd>
              <dt>조회수</dt>
              <dd>{{ props.post.view_count }}</dd>
            </dl>
<!--          <dl>-->
<!--            <dt>등록일</dt>-->
<!--            <dd>{{ props.post.upload_date }}</dd>-->
<!--          </dl>-->
        </div>
      </div>
    </div>
    <div class="board-body">
      <div class="inner-cms">
        <p class="inner-cms-share" v-html="props.post.post_content"></p>
      </div>
      <div v-if="props.fileList.length" class="file-sch-box">
        <ul class="file-sch-list">
          <li v-for="(item, index) in props.fileList" :key="index">
            <a @click="fileDownload(item.file_id)" title="첨부파일다운로드">
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

const { $ofetch } = useNuxtApp();

const props = defineProps(["post", "fileList", "isAdmin"]);

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
.inner-cms img {
  width: unset;
}
</style>
