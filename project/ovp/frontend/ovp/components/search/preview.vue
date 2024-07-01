<template>
  <div class="preview" v-if="!isPreviewClosed">
    <div class="ml-auto">
      <button
        class="button button-neutral-ghost button-sm"
        @click="setPreviewClose(true)"
      >
        <svg-icon class="svg-icon" name="close"></svg-icon>
        <span class="hidden-text">닫기</span>
      </button>
    </div>
    <div class="preview-contents">
      <div class="preview-item">
        <!--        TODO: [개발] 추후 해당 모델 페이지로 이동하는 url 추가 필요-->
        <a href="javascript:void(0)" class="preview-title">{{
          previewData.modelInfo.model.name
        }}</a>
        <div class="preview-desc">{{ previewData.modelInfo.model.desc }}</div>
        <table>
          <colgroup>
            <col style="width: 30%" />
            <col />
          </colgroup>
          <tr>
            <th>
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
            <th>
              {{ isStructuredModelType ? "Columns" : "전체 행" }}
            </th>
            <td>
              {{ checkEmptyValues(previewData.modelInfo.model.cnt) }}
            </td>
          </tr>
        </table>
      </div>
      <div class="preview-item">
        <div class="preview-title">태그</div>
        <div class="preview-group">
          <div
            class="tag tag-primary tag-sm"
            v-for="(tag, index) in previewData.tags"
            :key="index"
          >
            <!--            TODO: [퍼블리싱] a 요소 외에 새로운 요소 추가 후 적용 예정-->
            <a class="tag-link" href="javascript:void(0)">{{ tag.name }}</a>
          </div>
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
            <!--            TODO: [퍼블리싱] a 요소 외에 새로운 요소 추가 후 적용 예정-->
            <a class="tag-link" href="javascript:void(0)">{{
              glossary.name
            }}</a>
          </div>
        </div>
      </div>
      <!--        TODO: [개발] url기능 사용하지 않을 수 있다. 기획 검토 중 -->
      <div class="preview-item" v-if="!isStructuredModelType">
        <div class="preview-title">상세 설명</div>
        <div class="preview-desc">
          {{ previewData.modelInfo.details }}
        </div>
      </div>
      <div class="preview-item" v-if="!isStructuredModelType">
        <div class="preview-title">URL</div>
        <a
          :href="previewData.modelInfo.url"
          class="preview-link"
          target="_blank"
        >
          {{ checkEmptyValues(previewData.modelInfo.url) }}
        </a>
      </div>
      <div class="preview-item" v-if="isStructuredModelType">
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

interface Props {
  previewData: any;
  isPreviewClosed: boolean;
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
</script>

<style lang="scss" scoped></style>
