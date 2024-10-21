<template>
  <div class="preview" v-if="isShowPreview">
    <div class="ml-auto">
      <button
        class="button button-neutral-ghost button-sm"
        @click="setPreviewClose(false)"
      >
        <svg-icon class="svg-icon" name="close"></svg-icon>
        <span class="hidden-text">닫기</span>
      </button>
    </div>
    <div class="preview-contents">
      <div class="preview-item">
        <a
          href="javascript:void(0)"
          class="preview-title"
          @click="gotoDetail"
          >{{
            previewData.modelInfo.model.displayName ??
            previewData.modelInfo.model.name
          }}</a
        >
        <div class="preview-desc">
          {{ checkEmptyValues(previewData.modelInfo.model.desc) }}
        </div>
        <table>
          <colgroup>
            <col style="width: 30%" />
            <col />
          </colgroup>
          <tr>
            <th class="align-left">
              {{ isStructuredModelType ? "테이블 유형" : "확장자" }}
            </th>
            <td>
              {{
                isStructuredModelType
                  ? checkEmptyValues(previewData.modelInfo.model.tableType)
                  : checkEmptyValues(previewData.modelInfo.model.ext)
              }}
            </td>
          </tr>
          <tr>
            <th class="align-left">
              {{ isStructuredModelType ? "Columns" : "전체 행" }}
            </th>
            <td>
              {{
                isStructuredModelType
                  ? checkEmptyValues(previewData.modelInfo.model.cnt)
                  : checkEmptyValues(previewData.modelInfo.model.size)
              }}
            </td>
          </tr>
        </table>
      </div>
      <div class="preview-item">
        <div class="preview-title">태그</div>
        <div class="preview-group">
          <template v-for="(tag, index) in previewData.tags" :key="index">
            <div
              class="tag tag-primary tag-sm"
              v-show="!tag.category.includes('ovp_category')"
            >
              <span class="tag-text">{{ tag.name }}</span>
            </div>
          </template>
        </div>
      </div>
      <div class="preview-item">
        <div class="preview-title">용어</div>
        <div class="preview-group">
          <div
            class="tag tag-primary tag-sm"
            v-for="(glossary, index) in previewData.glossaries"
            :key="index"
          >
            <span class="tag-text">{{ glossary.name }}</span>
          </div>
        </div>
      </div>
      <div class="preview-item">
        <div class="preview-title">스키마</div>
        <div class="v-group gap-2 w-full">
          <div
            class="preview-schema"
            v-for="(column, index) in previewData.modelInfo.columns"
            :key="index"
          >
            <div class="h-group gap-1">
              <span class="schema-title">{{ column.name }}</span>
              <span class="schema-subtitle">({{ column.dataType }})</span>
            </div>
            <div class="schema-desc">
              {{ checkEmptyValues(column.desc) }}
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from "vue";
import type { PreviewData } from "@/type/common";

import { useRouter, useRoute } from "nuxt/app";

const router = useRouter();
const route = useRoute();

interface Props {
  previewData: PreviewData;
  isShowPreview: boolean;
  modelType: string;
}

const props = defineProps<Props>();

const isStructuredModelType = computed(() => {
  return props.previewData.modelType === "structured";
});

const emit = defineEmits<{ (e: "change", option: boolean): void }>();

const checkEmptyValues = (value: string | number) => {
  let removedWhitespaceString: string | null = null;

  if (typeof value === "string") {
    removedWhitespaceString = value.trim();
  }

  const isEmptyValue =
    removedWhitespaceString === "" ||
    value === 0 ||
    value === null ||
    value === undefined;

  return isEmptyValue ? "-" : value;
};

const setPreviewClose = (option: boolean) => {
  emit("change", option);
};
const gotoDetail = () => {
  const { id, fqn, index } = props.previewData as unknown as {
    id: string;
    fqn: string;
    index: string;
  };

  const detailPath = "/portal/search/detail";
  if (route.path === detailPath) {
    // 동일 경로로 이동할 때 강제로 새로고침
    router
      .push({
        path: detailPath,
        query: { type: index, id, fqn },
      })
      .then(() => {
        router.go(0); // 페이지 새로고침
      });
  } else {
    // 경로가 다르면 그냥 라우팅
    router.push({
      path: detailPath,
      query: { type: index, id, fqn },
    });
  }
};
</script>

<style lang="scss" scoped></style>
