<template>
  <div class="contents">
    <div>
      <request-edit-inner :dataRequest="dataRequest" @cancelMethod="onClickCancelButton" @saveMethod="onClickSaveButton"
        :isUpdate="false">
        <template v-slot:file>
          <div class="mu-item-vgroup">
            <!-- 첨부파일 : 파일찾기 , 삭제 편집모드 -->
            <div class="mu-file-attach">
              <input type="text" id="file-name" class="mu-input" placeholder="파일 첨부 크기는 최대 100MB까지 가능합니다." readonly />
              <div class="mu-file-btn" style="margin-left: 10px">
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
            <!-- // 첨부파일 -->
          </div>
        </template>
      </request-edit-inner>
    </div>
  </div>
</template>

<script setup lang="ts">
import RequestEditInner from "@organisms/dataRequest/request-edit-inner";
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

interface DataRequest {
  id: number
  writer: string
  request_type: string
  title: string
  data_name: string
  contents: string
  request_start?: string
  request_end?: string
  benefit_content: string
  create_date: string
  update_date: string
  process_status: number
  finish_date: string
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
const dataRequest = ref<DataRequest>({
  id: 0,
  writer: "",
  request_type: 0,
  title: "",
  data_name: "",
  contents: "",
  request_start: "",
  request_end: "",
  benefit_content: "",
  create_date: "",
  update_date: "",
  process_status: 0,
  finish_date: ""
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

    formData.append("dataRequest.writer", userStore.getUser.user_id);
    formData.append("dataRequest.title", dataRequest.value.title);
    formData.append("dataRequest.data_name", dataRequest.value.data_name);
    formData.append("dataRequest.contents", dataRequest.value.contents);
    formData.append("dataRequest.process_status", dataRequest.value.process_status);
    formData.append("dataRequest.benefit_content", dataRequest.value.benefit_content);
    formData.append("dataRequest.request_type", dataRequest.value.request_type);
    formData.append(
      "dataRequest.request_start",
      dataRequest.value.request_start == null ? "" : dataRequest.value.request_start
    );
    formData.append(
      "dataRequest.request_end",
      dataRequest.value.request_end == null ? "" : dataRequest.value.request_end
    );

    $ofetch("/api/dataRequest/add/saveFile", {
      method: "POST",
      body: formData,
    }).then(async (res: { errorMessage: string }) => {
      if (res) {
        if ((res.errorMessage && res.errorMessage != "")) {
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
