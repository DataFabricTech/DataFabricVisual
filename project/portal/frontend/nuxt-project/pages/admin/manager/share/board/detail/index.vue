<template>
  <div class="contents">
    <div>
      <div class="inner">
        <board-wrap :fileList="fileList" :post="post" :isAdmin="true">
          <template v-slot:thumb>
            <div class="inner-data visual">
              <div class="data-thumb" v-html="post.image_html"></div>
            </div>
          </template>
        </board-wrap>

        <!-- mu-button-wrap -->
        <div class="mu-button-wrap">
          <button type="button" class="mu-btn mu-btn-lg" @click="onClickDeleteButton">
            <span>삭제</span>
          </button>
          <button type="button" class="mu-btn mu-btn-lg" @click="onClickUpdateButton">
            <span>수정</span>
          </button>
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
import BoardWrap from "@organisms/post/share-detail-board-wrap";
import { successAlert, confirmAlert, errorAlert } from "@functional/alert/alert-default";
import { useNuxtApp, useRoute, useRouter } from "nuxt/app";
import { onMounted, ref } from "vue";

definePageMeta({
  name: "AdminSharePost",
  layout: "admin",
});

interface Post {
  admin_reply_status: boolean;
  file_cnt: number;
  id: number;
  image: string;
  is_open: boolean;
  new_yn: boolean;
  post_content: string;
  post_type: string;
  rownum: number;
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

const { $ofetch } = useNuxtApp();
const route = useRoute();
const router = useRouter();

onMounted(() => {
  let id = route.query.id;
  getPost(id);
});

const post = ref<Post>({
  admin_reply_status: false,
  file_cnt: 0,
  id: 0,
  image: "",
  is_open: false,
  new_yn: false,
  post_content: "",
  post_type: "",
  rownum: 0,
  title: "",
  upload_date: "",
  user_nm: "",
  view_count: 0,
  writer: "",
});
const fileList = ref<Array<File>>([]);

const getPost = async (pid: number = 1): Promise<void> => {
  try {
    let { data: result } = await $ofetch(`/api/post/one?postId=${pid}`);
    fileList.value = result.fileList;
    post.value = result.post;
    
    // 백엔드에서 서빙되는 이미지 경로로 변경
    let response = null
    try {
        // 백엔드에서 서빙되는 이미지 경로로 변경
        response = await $ofetch(`/api/images?path=${encodeURIComponent(post.value.image)}`, {
            responseType: 'blob' // 응답을 Blob 형태로 요청
          });
        // 응답이 유효한 경우에만 Blob 생성
        if (response) {
            const imageBlob = new Blob([response], {
                type: response.type
            });
            const imageUrl = URL.createObjectURL(imageBlob);
            post.value.image_html = `<img alt='활용 사례 썸네일 이미지' src='${imageUrl}' style='width:335px; height:220px;'/>`;
        } else {
            // 응답이 유효하지 않은 경우 기본 이미지로 설정
            // post.value.image_html = "<img alt='활용사례' src='/share_basic.png'></img>";
        }
    } catch (error) {
        // console.error("파일 등록 API 호출 중 오류 발생:", error);
        // 썸네일 파일 못 찾는 경우 기본값 설정
        // post.value.image_html = "<img alt='활용사례' src='/share_basic.png'></img>"
    }
  } catch (error) {
    console.log(error);
  }
};

// 삭제버튼 클릭 이벤트
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
          router.push("/admin/manager/share/board");
        }
      }
    });
  }
};

// 목록 버튼 클릭 이벤트 핸들러
const onClickListButton = (): void => {
  router.push("/admin/manager/share/board");
};

// 수정버튼 클릭 이벤트 핸들러
const onClickUpdateButton = (): void => {
  router.push("/admin/manager/share/board/update?id=" + post.value.id);
};
</script>
