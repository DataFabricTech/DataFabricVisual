<template>
  <div class="inner">
    <!-- board-wrap -->
    <div class="board-wrap">
      <!-- mu-heading -->
      <div class="mu-heading">
        <slot name="heading"></slot>
        <div class="mu-item-group">
          <p class="txt-info">
            <strong class="required">필수</strong>표시는 필수입력 항목입니다.
          </p>
        </div>
      </div>
      <!-- //mu-heading -->
      <!-- board-body -->
      <div class="board-body">
        <!-- mu-formbox 상세폼테이블 -->
        <table class="mu-formbox">
          <slot name="caption"></slot>
          <colgroup>
            <col style="width: 180px" />
            <col />
          </colgroup>
          <tbody>
            <tr>
              <th scope="row">제목 <strong class="required">필수</strong></th>
              <td>
                <textarea
                    id="textarea01"
                    placeholder="제목은 40자 이내로 입력 가능합니다."
                    title="제목 입력 필수"
                    rows="5"
                    class="mu-input"
                    v-model="props.share.title"
                    v-bind:class="[!titleValid ? 'error' : '', 'mu-area']"
                    maxlength="40"
                  ></textarea>
                  <p v-if="!titleValid" class="complete-msg error bul-refer">
                    제목을 입력해주세요.
                  </p>
              </td>
            </tr>
            <tr>
              <th scope="row">
                내용
                <strong class="required">필수</strong>
              </th>
              <td>
                <ckeditor :editor="editor" v-model="props.share.contents"></ckeditor>
                <p v-if="!shareContentReplyValid" class="complete-msg error bul-refer">
                  내용을 입력해주세요.
                </p>
              </td>
            </tr>
            <tr>
              <th scope="row">썸네일</th>
              <td>
                <slot name="thumb"></slot>
              </td>
            </tr>
            <tr>
              <th scope="row">첨부파일</th>
              <td>
                <slot name="file"></slot>
              </td>
            </tr>
          </tbody>
        </table>
        <!-- //mu-formbox -->
      </div>
      <!-- //board-body -->
    </div>

    <!-- mu-button-wrap -->
    <div class="mu-button-wrap">
      <button type="button" class="mu-btn mu-btn-lg" @click="onClickListButton">
        <span>취소</span>
      </button>
      <button
        type="button"
        class="mu-btn mu-btn-lg mu-btn-primary"
        @click="onClickSaveButton"
      >
        <slot name="savebutton"></slot>
      </button>
    </div>
    <!-- //mu-button-wrap -->
  </div>
</template>

<script setup lang="ts">
import { successAlert, confirmAlert, errorAlert } from "@functional/alert/alert-default";
import { useUserStore } from "@/store/user";
import { useNuxtApp, useRouter } from "nuxt/app";
import { ref, computed, ComputedRef } from "vue";

const { $ofetch } = useNuxtApp();
const { $ckeditor5  } = useNuxtApp();
const editor = $ckeditor5.customEditor5;

const radioList = ref<Array<string>>(["공지", "시스템", "이벤트", "안내", "기타"]);
const required_flag = ref<boolean>(false);

const emit = defineEmits(["cancelMethod", "update:share", "saveMethod"]);
const props = defineProps(["share", "cancelMethod", "saveMethod"]);

const files = ref<Array<File>>([]);
  interface File {
  lastModified: number;
  lastModifiedDate: Date;
  name: string;
  size: number;
  type: string;
  webkitRelativePath: string;
}

interface Share {
  id?: number;
  writer: string;
  title: string;
  contents: string;
  create_date?: string;
  update_date?: string;
}

const shareContentReplyValid: ComputedRef<boolean> = computed(() => {
  // 필수값 체크
  if (!required_flag.value) return true;

  // props.share나 props.share.contents가 없는 경우를 대비해 안전하게 접근
  return props.share && props.share.contents && props.share.contents.length > 0 ? true : false;
});

const titleValid: ComputedRef<boolean> = computed(() => {
  if (!required_flag.value) return true;
  else return props.share.title.length > 0 ? true : false;
});
const shareContentValid: ComputedRef<boolean> = computed(() => {
  if (!required_flag.value) return true;
  else return props.share.contents.length > 0 ? true : false;
});

const onClickListButton = (): void => {
  emit("cancelMethod");
};


// 파일 저장 버튼 클릭
const onClickSaveButton = async (): Promise<void> => {
  if (!titleValid.value || !shareContentValid.value) return;

  emit("update:share", props.share);
  emit("saveMethod");
};

</script>
