<template>
  <div>
    <div class="inner">
      <board-wrap :fileList="fileList" :post="post" :isAdmin="true" />

      <!-- mu-button-wrap -->
      <div class="mu-button-wrap">
        <button type="button" class="mu-btn mu-btn-lg" @click="onClickDeleteButton">
          <span>삭제</span>
        </button>
        <button type="button" class="mu-btn mu-btn-lg" @click="onClickUpdateButton">
          <span>수정</span>
        </button>
        <button type="button" class="mu-btn mu-btn-lg mu-btn-primary" @click="onClickListButton">
          <span>목록</span>
        </button>
      </div>
      <!-- //mu-button-wrap -->
    </div>
  </div>
</template>

<script setup lang="ts">
import BoardWrap from "@organisms/post/notice-detail-board-wrap";
import { successAlert, confirmAlert, errorAlert } from "@functional/alert/alert-default";
import { useNuxtApp, useRoute, useRouter } from "nuxt/app";
import { onMounted, ref } from "vue";

definePageMeta({
  name: "AdminManagerNoticePost",
  layout: "admin",
});

const { $ofetch } = useNuxtApp();
const router = useRouter();
const route = useRoute();

interface Post {
  admin_reply_status: boolean;
  category: string;
  dept_nm: string;
  id: number;
  is_open: boolean;
  post_content: string;
  post_type: string;
  title: string;
  upload_date: string;
  user_nm: string;
  view_count: number;
  writer: string;
}

interface File {
  file_id: number;
  file_location: string;
  file_size: string;
  name: string;
  post_id: number;
  reg_date: string;
  reg_user: string;
}

onMounted(() => {
  let id = route.query.id;
  getPost(id);
});

const post = ref<Post>({
  admin_reply_status: false,
  category: "",
  dept_nm: "",
  id: 0,
  is_open: false,
  post_content: "",
  post_type: "",
  title: "",
  upload_date: "",
  user_nm: "",
  view_count: 0,
  writer: "",
});
const fileList = ref<Array<File>>([]);

// 공지사항 조회
const getPost = async (id: number): Promise<void> => {
  try {
    let { data } = await $ofetch(`/api/post/one?postId=${id}`);
    data.post.post_content = data.post.post_content ? data.post.post_content : "";
    fileList.value = data.fileList;
    post.value = data.post;
  } catch (error) {
    console.log(error);
  }
};

// 삭제버튼 클릭
const onClickDeleteButton = async (): Promise<void> => {
  if (await confirmAlert("정말로 삭제하시겠습니까?")) {
    let param = {
      deleteList: [post.value.id],
      post_type: post.value.post_type,
    };

    $ofetch("/api/post/delete", {
      method: "POST",
      body: param,
    }).then(async (res: { errorMessage: string }) => {
      if (res) {
        if (res.errorMessage && res.errorMessage != "") {
          await errorAlert("삭제에 실패하였습니다.");
        } else {
          await successAlert("삭제되었습니다.");
          router.push({ path: "/admin/manager/user/notice" });
        }
      }
    });
  }
};

// 목록버튼 클릭
const onClickListButton = (): void => {
  router.push({ path: "/admin/manager/user/notice" });
};

// 수정버튼 클릭
const onClickUpdateButton = (): void => {
  router.push({
    path: "/admin/manager/user/notice/update",
    query: { id: post.value.id },
  });
};
</script>
