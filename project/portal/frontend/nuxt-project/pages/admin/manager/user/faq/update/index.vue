<template>
  <div>
    <div class="inner">
      <div class="board-wrap">
        <!-- mu-heading -->
        <div class="mu-heading">
          <h4>FAQ 등록</h4>
          <div class="mu-item-group">
            <p class="txt-info">(필수<strong class="required">필수</strong>)</p>
          </div>
        </div>
        <!-- //mu-heading -->
        <!-- board-body -->
        <div class="board-body">
          <!-- mu-formbox 상세폼테이블 -->
          <table class="mu-formbox">
            <caption>
              FAQ 등록 게시판
            </caption>
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
                        v-model="post.category"
                        :value="item"
                      />
                      <label :for="index"
                        ><em class="data-label">{{ item }}</em></label
                      >
                    </div>
                  </div>
                </td>
              </tr>

              <tr>
                <th scope="row">질문 내용 <strong class="required">필수</strong></th>
                <td>
                  <textarea
                    id="textarea01"
                    placeholder="질문내용을 입력하세요"
                    title="FAQ 질문내용 입력"
                    rows="5"
                    v-model="post.post_content"
                    v-bind:class="[!postContentValid ? 'error' : '', 'mu-area']"
                  ></textarea>
                  <p v-if="!postContentValid" class="complete-msg error">
                    내용을 입력해주세요.
                  </p>
                  <!-- 필수 항목을 입력하지 않았을 때 문구노출 -->
                </td>
              </tr>
              <tr>
                <th scope="row">답변 내용 <strong class="required">필수</strong></th>
                <td>
                  <!-- <textarea
                    id="textarea02"
                    placeholder="답변내용을 입력하세요"
                    title="FAQ 답변내용 입력"
                    rows="10"
                    v-model="post.admin_reply"
                    v-bind:class="[!adminReplyValid ? 'error' : '', 'mu-area']"
                  ></textarea> -->
                  <ckeditor :editor="editor" v-model="post.admin_reply"></ckeditor>
                  <p v-if="!adminReplyValid" class="complete-msg error">
                    내용을 입력해주세요.
                  </p>
                  <!-- 필수 항목을 입력하지 않았을 때 문구노출 -->
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
          <span>수정</span>
        </button>
      </div>
      <!-- //mu-button-wrap -->
    </div>
  </div>
</template>

<script setup lang="ts">
import { successAlert, confirmAlert, errorAlert } from "@functional/alert/alert-default";
import { useNuxtApp, useRoute, useRouter } from "nuxt/app";
import { onMounted, ref, computed, ComputedRef } from "vue";

definePageMeta({
  name: "AdminFaqUpdate",
  layout: "admin",
});

const { $ofetch, $ckeditor5 } = useNuxtApp();
const editor = $ckeditor5.customEditor5;

const route = useRoute();
const router = useRouter();

interface Post {
  post_type: string;
  title: string;
  post_content: string;
  category: string;
  admin_reply: string;
}

const radioList = ref<Array<string>>(["공지", "시스템", "이벤트", "기타"]);
const post = ref<Post>({
  post_type: "faq",
  title: "",
  post_content: "",
  category: "공지",
  admin_reply: "",
});
const required_flag = ref<boolean>(false);

onMounted(() => {
  let id = route.query.id;
  getPost(id);
});

const postContentValid: ComputedRef<boolean> = computed(() => {
  if (!required_flag.value) return true;
  else return post.value.post_content.length > 0 ? true : false;
});
const adminReplyValid: ComputedRef<boolean> = computed(() => {
  if (!required_flag.value) return true;
  else return post.value.admin_reply.length > 0 ? true : false;
});

const getPost = async (id: number): Promise<void> => {
  try {
    let { data } = await $ofetch(`/api/post/one?postId=${id}`);
    return (post.value = data.post);
  } catch (error) {
    console.log(error);
  }
};

// 취소버튼 클
const onClickListButton = (): void => {
  router.push({ path: "/admin/manager/user/faq" });
};

// 수정버튼 클릭
const onClickSaveButton = async (): Promise<void> => {
  required_flag.value = true;
  if (!postContentValid.value || !adminReplyValid.value) return;

  if (await confirmAlert("저장하시겠습니까?")) {
    $ofetch("/api/post/update", {
      method: "POST",
      body: post.value,
    }).then(async (res: { errorMessage: string }) => {
      if (res) {
        if (res.errorMessage && res.errorMessage != "") {
          await errorAlert("저장에 실패하였습니다.");
        } else {
          await successAlert("저장되었습니다.");
          router.push({ path: "/admin/manager/user/faq" });
        }
      }
    });
  }
};
</script>
