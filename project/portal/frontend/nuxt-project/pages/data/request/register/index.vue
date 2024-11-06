<template>
  <div class="contents">
    <div>
      <request-edit-inner
        :post="post"
        @cancelMethod="onClickCancelButton"
        @saveMethod="onClickSaveButton"
        :isUpdate="false"
      >
        <template v-slot:file>
          <div class="mu-item-vgroup">
            <!-- 첨부파일 : 파일찾기 , 삭제 편집모드 -->
            <div class="mu-file-attach">
              <input
                type="text"
                id="file-name"
                class="mu-input"
                placeholder="파일 첨부 크기는 최대 5MB까지 가능합니다."
                readonly
              />
              <div class="mu-file-btn" style="margin-left: 10px">
                <button type="button" class="mu-btn">
                  <label for="file-find">파일찾기</label>
                </button>
                <input
                  type="file"
                  id="file-find"
                  class="mu-btn-hide"
                  @change="onChange"
                />
              </div>
              <!-- <div class="mu-file-btn">
                <button type="button" class="mu-btn">
                  <label for="sample-file" @click="downloadSample">파일양식</label>
                </button>
              </div> -->
            </div>
            <!-- 첨부파일 : 리스트 개발삭제 가능함 -->
            <ul class="file-sch-list">
              <li v-for="(item, index) in files" :key="index">
                <a href="#none" title="첨부파일다운로드"
                  ><i class="mu-icon-svg file">첨부파일있음</i>{{ item.name }}</a
                >
                <button
                  type="button"
                  class="mu-btn mu-btn-delete"
                  @click="onClickDeleteButton"
                >
                  <i class="mu-icon-svg item-delete" :id="index"></i
                  ><span>첨부파일 삭제</span>
                </button>
              </li>
            </ul>
            <!-- // 첨부파일 -->
          </div>
        </template>
      </request-edit-inner>
    </div>
  </div>
</template>

<script setup lang="ts">
import RequestEditInner from "@organisms/post/request-edit-inner";
import { confirmAlert, errorAlert } from "@functional/alert/alert-default";
import { useUserStore } from "@/store/user";
import { useNuxtApp, useRouter, useRuntimeConfig } from "nuxt/app";
import { onMounted, ref } from "vue";
import { successAlert } from "@/components/functional/alert/alert-default";

definePageMeta({
  name: "DataRequestUpload",
  layout: "user",
});

const { $ofetch } = useNuxtApp();
const userStore = useUserStore();
const router = useRouter();

interface Post {
  admin_reply_status: boolean;
  benefit_content: string;
  category: string;
  data_name: string;
  dept_nm: string;
  id: number;
  is_open: boolean;
  post_content: string;
  post_type: string;
  process_status: string;
  title: string;
  upload_date: string;
  user_nm: string;
  view_count: number;
  writer: string;
  request_start?: string;
  request_end?: string;
}

interface File {
  file_id: number;
  file_location: string;
  file_size: string;
  name: string;
  post_id: number;
  reg_date: string;
  reg_user: string;
  isNew?: boolean;
  delete_flag?: boolean;
}

const files = ref<Array<File>>([]);
const sample_files = ref<Array<File>>([]);
const post = ref<Post>({
  admin_reply_status: false,
  benefit_content: "",
  category: "",
  data_name: "",
  dept_nm: "",
  id: 0,
  is_open: false,
  post_content: "",
  post_type: "data-request",
  process_status: "신청",
  title: "",
  upload_date: "",
  user_nm: "",
  view_count: 0,
  writer: "",
});

onMounted(() => getSampleFileList());

// 파일 변경 이벤트
const onChange = (e: any) => {
  if (e.target.files.length == 0) return // 파일 선택하지 않은 경우
  let maxSize = 100 * 1024 * 1024;
  let fileSize = e.target.files[0].size;
  let fileName = e.target.files[0].name.substring(
    e.target.files[0].name.lastIndexOf(".") + 1
  );
  let reg = /zip|pptx|pptm|ppt|xls|xlsx|xlsm|csv|pdf|jpg|jpeg|png/i;

  if (!reg.test(fileName)) {
    errorAlert("ZIP, PPT, EXCEL, PDF, JPG, JPEG, PNG 파일만 업로드 할 수 있습니다.");
    return false;
  }
  if (fileSize > maxSize) {
    errorAlert("첨부파일 사이즈는 100MB 이내로 등록 가능합니다.");
    return false;
  }

  files.value.splice(files.value.length, 1, e.target.files[0]); // [10, 20]
};

const getSampleFileList = async (): Promise<void> => {
  try {
    let { data: fileList } = await $ofetch("/api/service/file/list");
    sample_files.value = fileList;
  } catch (error) {
    console.log(error);
  }
};

// 목록 버튼 클릭 이벤트 핸들러
const onClickCancelButton = async (): Promise<void> => {
  if (await confirmAlert("아직 게시글을 등록하지 않았습니다. 정말로 취소하시겠습니까?")) {
    router.push({ path: "/data/request" });
  }
};

// 파일삭제 버튼 클릭 이벤트
const onClickDeleteButton = (e: any): void => {
  files.value.splice(e.target.id, 1); // [10, 20]
};

// 저장 버튼 클릭 이벤트
const onClickSaveButton = async (): Promise<void> => {
  if (await confirmAlert("저장하시겠습니까?")) {
    let formData = new FormData();
    files.value.forEach(function (file) {
      formData.append("files", file);
    });

    formData.append("post.post_type", post.value.post_type);
    formData.append("post.writer", userStore.getUser.user_id);
    formData.append("post.title", post.value.title);
    formData.append("post.data_name", post.value.data_name);
    formData.append("post.post_content", post.value.post_content);
    formData.append("post.process_status", post.value.process_status);
    formData.append("post.benefit_content", post.value.benefit_content);
    formData.append("post.category", post.value.category);
    formData.append(
      "post.request_start",
      post.value.request_start == null ? "" : post.value.request_start
    );
    formData.append(
      "post.request_end",
      post.value.request_end == null ? "" : post.value.request_end
    );

    $ofetch("/api/post/add/saveFile", {
      method: "POST",
      body: formData,
    }).then(async (res: { errorMessage: string }) => {
      if (res) {
        if (res.errorMessage && res.errorMessage != "") {
          await errorAlert("저장에 실패하였습니다.");
        } else {
          await successAlert("글이 저장되었습니다.");
          router.push({ path: "/data/request" });
        }
      }
    });
  }
};

const downloadSample = async (): Promise<void> => {
  sample_files.value.forEach(async (element) => {
    const res = await $ofetch(`/api/service/file/fileDownload?fileId=${element.file_id}`);

    if (res.errorMessage && res.errorMessage != "") {
      await errorAlert("샘플파일 다운로드에 실패하였습니다.");
    } else {
      const config = useRuntimeConfig();
      const link = document.createElement("a");
      let url = "";

      if (document.location.href.indexOf("localhost") > -1) {
        url =
          config.public.baseURL +
          `api/service/file/fileDownload?fileId=${element.file_id}`;
      } else {
        url = `/api/service/file/fileDownload?fileId=${element.file_id}`;
      }

      link.href = url;
      link.style.cssText = "display:none";
      document.body.appendChild(link);
      link.click();
      link.remove();
    }
  });
};
</script>
