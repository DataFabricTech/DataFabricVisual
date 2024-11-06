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
              <th scope="row">구분 <strong class="required">필수</strong></th>
              <td>
                <div class="mu-item-group">
                  <div class="mu-radio" v-for="(item, index) in radioList" :key="index">
                    <input
                      type="radio"
                      :id="index"
                      v-model="props.post.category"
                      :value="item"
                    />
                    <label :for="index"
                      ><span>{{ item }}</span></label
                    >
                  </div>
                </div>
              </td>
            </tr>
            <tr>
              <th scope="row">메인 롤링 <strong class="required">필수</strong></th>
              <td>
                <div class="mu-item-group">
                  <div class="mu-radio">
                    <input type="radio" id="isOpenTrue" v-model="props.post.is_open" value="true"/>
                    <label for="isOpenTrue"><span>사용</span></label>
                  </div>
                  <div class="mu-radio">
                    <input type="radio" id="isOpenFalse" v-model="props.post.is_open" value="false"/>
                    <label for="isOpenFalse"><span>미사용</span></label>
                  </div>
                </div>
              </td>
            </tr>
            <tr>
              <th scope="row">제목 <strong class="required">필수</strong></th>
              <td>
                <input
                  type="text"
                  v-model="props.post.title"
                  placeholder="제목은 50자 이내로 입력 가능합니다."
                  v-bind:class="[!titleValid ? 'error' : '', 'mu-input']"
                  title="제목 입력"
                  maxlength="50"
                />
                <p v-if="!titleValid" class="complete-msg error">내용을 입력해주세요.</p>
              </td>
            </tr>
            <tr>
              <th scope="row">내용 <strong class="required">필수</strong></th>
              <td>
                <!-- <textarea
                  id="textarea01"
                  v-model="props.post.post_content"
                  placeholder="내용을 입력하세요"
                  v-bind:class="[!postContentValid ? 'error' : '', 'mu-area']"
                  title="공지사항 내용 입력"
                  rows="10"
                ></textarea> -->
                <ckeditor :editor="editor" v-model="props.post.post_content"></ckeditor>
                <p v-if="!postContentValid" class="complete-msg error">
                  내용을 입력해주세요.
                </p>
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
import { ref, computed, ComputedRef } from "vue";

const { $ckeditor5  } = useNuxtApp();
const editor = $ckeditor5.customEditor5;

const radioList = ref<Array<string>>(["공지", "시스템", "이벤트", "안내", "기타"]);
const required_flag = ref<boolean>(false);

const emit = defineEmits(["cancelMethod", "update:post", "saveMethod"]);
const props = defineProps(["post", "cancelMethod", "saveMethod"]);

const titleValid: ComputedRef<boolean> = computed(() => {
  if (!required_flag.value) return true;
  else return props.post.title.length > 0 ? true : false;
});
const postContentValid: ComputedRef<boolean> = computed(() => {
  if (!required_flag.value) return true;
  else return props.post.post_content.length > 0 ? true : false;
});

const onClickListButton = (): void => {
  emit("cancelMethod");
};

const onClickSaveButton = (): void => {
  required_flag.value = true;
  if (!titleValid.value || !postContentValid.value) return;

  emit("update:post", props.post);
  emit("saveMethod");
};
</script>
