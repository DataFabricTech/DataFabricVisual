<template>
  <div class="contents">
    <div>
      <request-edit-inner :dataRequest="dataRequest" @cancelMethod="onClickCancelButton" @saveMethod="onClickSaveButton"
        :isUpdate="true">
        <template v-slot:file>
          <div class="mu-item-vgroup">
            <!-- 첨부파일 : 파일찾기 , 삭제 편집모드 -->
            <div class="mu-file-attach">
              <input type="text" id="file-name" class="mu-input" placeholder="파일 첨부 크기는 최대 100MB까지 가능합니다." readonly />
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
                <a v-if="!item.delete_flag" href="#none" title="첨부파일다운로드">
                  <i class="mu-icon-svg file">첨부파일있음</i>{{ item.name }}</a>
                <button v-if="!item.delete_flag" type="button" class="mu-btn mu-btn-delete"
                  @click="onClickDeleteButton">
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
import RequestEditInner from "@organisms/dataRequest/request-edit-inner";
import { confirmAlert, errorAlert } from "@functional/alert/alert-default";
import { useNuxtApp, useRoute, useRouter } from "nuxt/app";
import { onMounted, ref } from "vue";
import { successAlert } from "@/components/functional/alert/alert-default";

definePageMeta({
  name: "DataRequestUpdate",
  layout: "user",
});

const { $ofetch } = useNuxtApp();
const route = useRoute();
const router = useRouter();

interface DataRequest {
  id: number
  writer: string
  request_type: number
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

onMounted(() => {
  let id = route.query.id;
  getDataRequestOne(id);
});

const files = ref<Array<File>>([]);
const delete_files = ref<Array<File>>([]);
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

const getDataRequestOne = async (id: number): Promise<void> => {
  try {
    let { data: result } = await $ofetch(`/api/dataRequest/one?id=${id}`);
    files.value = result.fileList;
    dataRequest.value = result.dataRequest;

    // 데이터 요청의 경우 앞에 구분 설정
    dataRequest.value.request_start = dataRequest.value.request_start
      ? dataRequest.value.request_start.replace(/([.])/g, "-")
      : "";
    dataRequest.value.request_end = dataRequest.value.request_end
      ? dataRequest.value.request_end.replace(/([.])/g, "-")
      : "";
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
  else delete_files.value.push(files.value.splice(e.target.id, 1)[0]);
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

    formData.append("dataRequest.id", dataRequest.value.id);
    formData.append("dataRequest.request_type", dataRequest.value.request_type);
    formData.append("dataRequest.title", dataRequest.value.title);
    formData.append("dataRequest.data_name", dataRequest.value.data_name);
    formData.append("dataRequest.contents", dataRequest.value.contents);
    formData.append("dataRequest.process_status", dataRequest.value.process_status);
    formData.append("dataRequest.benefit_content", dataRequest.value.benefit_content);
    formData.append(
      "dataRequest.request_start",
      dataRequest.value.request_start == null ? "" : dataRequest.value.request_start
    );
    formData.append(
      "dataRequest.request_end",
      dataRequest.value.request_end == null ? "" : dataRequest.value.request_end
    );

    $ofetch("/api/dataRequest/update/saveFile", {
      method: "POST",
      body: formData,
    }).then(async (res: { errorMessage: string }) => {
      if (res) {
        if (res.errorMessage && res.errorMessage != "") {
          await errorAlert("수정에 실패하였습니다.");
        } else {
          await successAlert("글이 수정되었습니다.");
          router.push({ path: "/data/request" });
        }
      }
    });
  }
};
</script>
