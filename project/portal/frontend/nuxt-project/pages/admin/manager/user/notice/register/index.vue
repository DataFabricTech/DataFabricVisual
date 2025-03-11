<template>
  <div>
    <board-wrap :post="post" @cancelMethod="onClickListButton" @saveMethod="onClickSaveButton">
      <template v-slot:heading>
        <h4>공지사항 등록</h4>
      </template>
      <template v-slot:caption>
        <caption>
          공지사항 등록 게시판
        </caption>
      </template>
      <template v-slot:file>
        <!-- 첨부파일 : 파일찾기 , 삭제 편집모드 -->
        <div class="mu-file-attach">
          <input type="text" id="file-name" class="mu-input" placeholder="파일 첨부 크기는 최대 1GB까지 가능합니다."
            readonly="readonly" />
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
            <a href="#none" title="첨부파일다운로드"><i class="mu-icon-svg file">첨부파일있음</i>{{ item.name }}</a>
            <button type="button" class="mu-btn mu-btn-delete" @click="onClickDeleteButton">
              <i class="mu-icon-svg item-delete" :id="index"></i><span>첨부파일 삭제</span>
            </button>
          </li>
        </ul>
        <!-- //첨부파일 -->
      </template>
      <template v-slot:savebutton><span>등록</span></template>
    </board-wrap>
  </div>
</template>

<script setup lang="ts">
import BoardWrap from "@organisms/post/notice-edit-inner";
import { successAlert, confirmAlert, errorAlert } from "@functional/alert/alert-default";
import { useNuxtApp, useRouter } from "nuxt/app";
import { ref } from "vue";

definePageMeta({
  name: "AdminManagerNoticePostRegister",
  layout: "admin",
});

const { $ofetch } = useNuxtApp();
const router = useRouter();

interface Post {
  category: string;
  post_content: string;
  post_type: string;
  title: string;
}

interface File {
  lastModified: number;
  lastModifiedDate: Date;
  name: string;
  size: number;
  type: string;
  webkitRelativePath: string;
}

const files = ref<Array<File>>([]);
const post = ref<Post>({
  post_type: "notice",
  title: "",
  post_content: "",
  category: "공지",
  is_open: false
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

  files.value.splice(files.value.length, 1, e.target.files[0]); // [10, 20]
};

// 목록 버튼 클릭
const onClickListButton = async (): Promise<void> => {
  if (await confirmAlert("작성중인 내용을 취소하고 목록으로 돌아가시겠습니까?")) {
    router.push({ path: "/admin/manager/user/notice" });
  }
};

// 삭제 버튼 클릭
const onClickDeleteButton = (e: Event) => {
  files.value.splice(e.target.id, 1); // [10, 20]
};

// 저장 버튼 클릭
const onClickSaveButton = async (): Promise<void> => {
  if (await confirmAlert("저장하시겠습니까?")) {
    let formData = new FormData();
    files.value.forEach((file) => {
      formData.append("files", file);
    });

    formData.append("post.post_type", post.value.post_type);
    formData.append("post.title", post.value.title);
    formData.append("post.post_content", post.value.post_content);
    formData.append("post.category", post.value.category);
    formData.append("post.is_open", post.value.is_open);

    $ofetch("/api/post/add/saveFile", {
      method: "POST",
      body: formData,
    }).then(async (res: { errorMessage: string }) => {
      if (res) {
        if (res.errorMessage && res.errorMessage != "") {
          await errorAlert("저장에 실패하였습니다.");
        } else {
          await successAlert("글이 저장되었습니다.");
          router.push({ path: "/admin/manager/user/notice" });
        }
      } else {
        await errorAlert("저장에 실패하였습니다.");
      }
    });
  }
};
</script>
