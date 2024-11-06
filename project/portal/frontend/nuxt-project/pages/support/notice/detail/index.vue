<template>
  <div class="contents">
    <div>
      <!-- 제목위치가 컨텐츠 최상단에 위치해야함. -->
      <strong class="board-title"><em class="data-label">공지사항</em> {{ post.title }}</strong>

      <div class="inner">
        <board-wrap :fileList="fileList" :post="post" />

        <!-- mu-button-wrap -->
        <div class="mu-button-wrap">
          <button
            type="button"
            class="mu-btn mu-btn-lg mu-btn-primary"
            @click="onClickListButton"
          >
            <span>목록</span>
          </button>
        </div>
        <!-- //mu-button-wrap -->
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import BoardWrap from "@organisms/post/notice-detail-board-wrap";
import { useNuxtApp, useRoute, useRouter } from "nuxt/app";
import { onMounted, ref } from "vue";

definePageMeta({
  name: "SupportNoticePost",
  layout: "user",
});

const route = useRoute();
const router = useRouter();
const { $ofetch } = useNuxtApp();

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

// id값 변경 쿼리 변화 감지
watch(
  () => route.query.id,
  (newId, oldId) => {
    // 쿼리 id가 변경되었을 때 새로고침
    if (newId !== oldId) {
      getPost(newId)
    }
  }
)

const post = ref<Post>({});
const fileList = ref<Array<File>>([]);

const getPost = async (pid: number = 1): Promise<void> => {
  try {
    let { data: result } = await $ofetch(`/api/post/one?postId=${pid}`);
    result.post.post_content = result.post.post_content ? result.post.post_content : "";
    fileList.value = result.fileList;
    post.value = result.post;
  } catch (error) {
    console.log(error);
  }
};

// 목록 버튼 클릭 이벤트 핸들러
const onClickListButton = (): void => {
  router.push({ path: "/support/notice" });
};
</script>
