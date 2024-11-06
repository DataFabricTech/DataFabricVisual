<template>
  <div>
    <board-wrap
      :post="post"
      @cancelMethod="onClickListButton"
      @saveMethod="onClickSaveButton"
    >
      <template v-slot:heading><h4>공지사항 수정</h4></template>
      <template v-slot:caption
        ><caption>
          공지사항 수정 게시판
        </caption></template
      >
      <template v-slot:file>
        <!-- 첨부파일 : 파일찾기 , 삭제 편집모드 -->
        <div class="mu-file-attach">
          <input
            type="text"
            id="file-name"
            class="mu-input"
            placeholder="파일 첨부 크기는 최대 1GB까지 가능합니다."
            readonly="readonly"
          />
          <div class="mu-file-btn">
            <button type="button" class="mu-btn">
              <label for="file-find">파일찾기</label>
            </button>
            <input type="file" id="file-find" class="mu-btn-hide" @change="onChange" />
          </div>
        </div>
        <!-- 첨부파일 : 리스트 개발삭제 가능함 -->
        <ul class="file-sch-list">
          <li v-for="(item, index) in files" :key="index">
            <a v-if="!item.delete_flag" href="#none" title="첨부파일다운로드"
              ><i class="mu-icon-svg file">첨부파일있음</i>{{ item.name }}</a
            >
            <button
              v-if="!item.delete_flag"
              type="button"
              class="mu-btn mu-btn-delete"
              @click="onClickDeleteButton"
            >
              <i class="mu-icon-svg item-delete" :id="index"></i
              ><span>첨부파일 삭제</span>
            </button>
          </li>
        </ul>
        <!-- //첨부파일 -->
      </template>
      <template v-slot:savebutton><span>확인</span></template>
    </board-wrap>
  </div>
</template>

<script setup lang="ts">
import BoardWrap from "@organisms/post/notice-edit-inner";
import { successAlert, confirmAlert, errorAlert } from "@functional/alert/alert-default";
import { useNuxtApp, useRoute, useRouter } from "nuxt/app";
import { onMounted, ref } from "vue";

definePageMeta({
  name: "AdminManagerNoticePostUpdate",
  layout: "admin",
});

const { $ofetch } = useNuxtApp();
const route = useRoute();
const router = useRouter();

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

const files = ref<File>([]);
const delete_files = ref<Array<File>>([]);
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

// 파일 변경
const onChange = async (e: Event): Promise<void> => {
  if (e.target.files.length == 0) return // 파일 선택하지 않은 경우
  let maxSize = 1024 * 1024 * 1024;
  let fileSize = e.target.files[0].size;
  let fileName = e.target.files[0].name.substring(
    e.target.files[0].name.lastIndexOf(".") + 1
  );
  let reg = /zip|pptx|pptm|ppt|xls|xlsx|xlsm|csv|pdf|jpg|jpeg|png/i;

  if (!reg.test(fileName)) {
    await errorAlert(
      "ZIP, PPT, EXCEL, PDF, JPG, JPEG, PNG 파일만 업로드 할 수 있습니다."
    );
    return;
  }
  if (fileSize > maxSize) {
    await errorAlert("첨부파일 사이즈는 1GB 이내로 등록 가능합니다.");
    return;
  }

  e.target.files[0].isNew = true; // 첫번째 파일이 새파일임을 지정
  files.value.splice(files.value.length, 1, e.target.files[0]); // [10, 20] // 파일 크기의 인덱스부터 1개 요소 수정(파일 목록 맨 뒤에 추가)
};

// 공지사항 조회
const getPost = async (id: number): Promise<void> => {
  try {
    let { data } = await $ofetch(`/api/post/one?postId=${id}`);
    files.value = data.fileList;
    post.value = data.post;
  } catch (error) {
    console.log(error);
  }
};

// 취소 버튼 클릭
const onClickListButton = async (): Promise<void> => {
  if (await confirmAlert("작성중인 내용을 취소하고 목록으로 돌아가시겠습니까?")) {
    router.push({ path: "/admin/manager/user/notice" });
  }
};

// 파일 삭제 버튼 클릭
const onClickDeleteButton = (e: Event) => {
  if (files.value[e.target.id] && files.value[e.target.id].isNew)
    files.value.splice(e.target.id, 1);
  // [10, 20] // 새로 추가된 파일이면 현재 파일 목록에서 제거
  else delete_files.value.push(files.value.splice(e.target.id, 1)[0]); // 기존 파일이면 id값을 삭제 목록에 추가
};

// 저장 버튼 클릭 이벤트
const onClickSaveButton = async () => {
  if (await confirmAlert("저장하시겠습니까?")) {
    let formData = new FormData();
    files.value.forEach((file) => {
      if (file.isNew) formData.append("files", file);
    });
    delete_files.value.forEach((file) => {
      formData.append("delete_files", file.file_id);
    });

    formData.append("post.id", post.value.id);
    formData.append("post.post_type", post.value.post_type);
    formData.append("post.title", post.value.title);
    formData.append("post.post_content", post.value.post_content);
    formData.append("post.is_open", post.value.is_open);
    formData.append("post.category", post.value.category);

    $ofetch("/api/post/update/saveFile", {
      method: "POST",
      body: formData,
    }).then(async (res: { errorMessage: string }) => {
      if (res) {
        if (res.errorMessage && res.errorMessage != "") {
          await errorAlert("저장에 실패하였습니다.");
        } else {
          await successAlert("글이 저장되었습니다.");
          router.push({ path: "/admin/manager/user/notice" });
          return;
        }
      }
    });
  }
};
</script>
