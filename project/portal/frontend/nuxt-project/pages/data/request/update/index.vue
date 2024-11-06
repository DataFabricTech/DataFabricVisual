<template>
  <div class="contents">
    <div>
      <request-edit-inner
        :post="post"
        @cancelMethod="onClickCancelButton"
        @saveMethod="onClickSaveButton"
        :isUpdate="true"
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
              <div class="mu-file-btn">
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
            </div>
            <!-- 첨부파일 : 리스트 개발삭제 가능함 -->
            <ul class="file-sch-list">
              <li v-for="(item, index) in files" :key="index">
                <a v-if="!item.delete_flag" href="#none" title="첨부파일다운로드">
                  <i class="mu-icon-svg file">첨부파일있음</i>{{ item.name }}</a
                >
                <button
                  v-if="!item.delete_flag"
                  type="button"
                  class="mu-btn mu-btn-delete"
                  @click="onClickDeleteButton"
                >
                  <i class="mu-icon-svg item-delete"></i><span>첨부파일 삭제</span>
                </button>
              </li>
            </ul>
            <!-- //첨부파일 -->
          </div>
        </template>
      </request-edit-inner>
    </div>
  </div>
</template>

<script setup lang="ts">
import RequestEditInner from "@organisms/post/request-edit-inner";
import { confirmAlert, errorAlert } from "@functional/alert/alert-default";
import { useNuxtApp, useRoute, useRouter } from "nuxt/app";
import { onMounted, ref } from "vue";
import { successAlert } from "@/components/functional/alert/alert-default";

definePageMeta({
  name: "DataRequestPostUpdate",
  layout: "user",
});

const { $ofetch } = useNuxtApp();
const route = useRoute();
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

onMounted(() => {
  let id = route.query.id;
  getPost(id);
});

const files = ref<Array<File>>([]);
const delete_files = ref<Array<File>>([]);
const post = ref<Post>({
  admin_reply_status: false,
  benefit_content: "",
  category: "",
  data_name: "",
  dept_nm: "",
  id: 0,
  is_open: false,
  post_content: "",
  post_type: "",
  process_status: "",
  title: "",
  upload_date: "",
  user_nm: "",
  view_count: 0,
  writer: "",
});

const getPost = async (id: number): Promise<void> => {
  try {
    let { data: result } = await $ofetch(`/api/post/one?postId=${id}`);
    files.value = result.fileList;
    post.value = result.post;

    // 데이터 요청의 경우 앞에 구분 설정
    const start_category = post.value.title.indexOf("[");
    const end_category = post.value.title.indexOf("]");
    post.value.category = post.value.category;
    post.value.request_start = post.value.request_start
      ? post.value.request_start.replace(/([.])/g, "-")
      : "";
    post.value.request_end = post.value.request_end
      ? post.value.request_end.replace(/([.])/g, "-")
      : "";
    // this.$refs.requestEditInner.request_start = post.value.request_start;
    // this.$refs.requestEditInner.request_end = post.value.request_end;
  } catch (error) {
    console.log(error);
  }
};

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

  e.target.files[0].isNew = true;
  files.value.splice(files.value.length, 1, e.target.files[0]); // [10, 20]
};

// 취소버튼 클릭 이벤트
const onClickCancelButton = async (): Promise<void> => {
  if (await confirmAlert("아직 게시글을 등록하지 않았습니다. 정말로 취소하시겠습니까?")) {
    router.push({ path: "/data/request/detail", query: { id: route.query.id } });
  }
};

// 삭제버튼 클릭 이벤트
const onClickDeleteButton = (e: any): void => {
  if (files.value[e.target.id] && files.value[e.target.id].isNew) files.value.splice(e.target.id, 1);
  // [10, 20]
  else delete_files.value.push(files.value.splice(e.target.id, 1)[0]);
};

// 목록버튼 클릭 이벤트
const onClickListButton = (): void => {
  router.push("/data/request");
};

// 저장버튼 클릭 이벤트
const onClickSaveButton = async (): Promise<void> => {
  if (await confirmAlert("저장하시겠습니까?")) {
    let formData = new FormData();
    files.value.forEach(function (file) {
      if (file.isNew) formData.append("files", file);
    });
    delete_files.value.forEach(function (file) {
      formData.append("delete_files", file.file_id);
    });

    formData.append("post.id", post.value.id);
    formData.append("post.post_type", post.value.post_type);
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

    $ofetch("/api/post/update/saveFile", {
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
</script>
