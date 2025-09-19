<template>
  <div>
    <board-wrap :share="share" @cancelMethod="onClickListButton" @saveMethod="onClickSaveButton">
      <template v-slot:heading>
        <h4>활용사례 수정</h4>
      </template>
      <template v-slot:caption>
        <caption>
          활용사례 수정 게시판
        </caption>
      </template>
      <template v-slot:thumb>
        <div class="mu-file-attach">
          <input type="text" id="thumb-file-name" class="mu-input"
            placeholder="썸네일 이미지 크기는 최대 10MB까지 가능합니다. 335px * 220px를 권장합니다." readonly="readonly" />
          <div class="mu-file-btn">
            <button type="button" class="mu-btn">
              <label for="thumb-file-find">파일찾기</label>
            </button>
            <input type="file" id="thumb-file-find" class="mu-btn-hide" @change="onChangeThumb" />
          </div>
        </div>
        <ul class="file-sch-list">
          <li v-for="(item, index) in thumbFiles" :key="index">
            <a @click="fileDownload(item)"><i class="mu-icon-svg file">썸네일있음</i>{{ item.name }}</a>
            <button type="button" class="mu-btn mu-btn-delete" @click="onClickDeleteButtonThumb">
              <i class="mu-icon-svg item-delete" :id="index"></i><span>썸네일 삭제</span>
            </button>
          </li>
        </ul>
        <!-- //썸네일 -->
      </template>
      <template v-slot:file>
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
        <ul class="file-sch-list">
          <li v-for="(item, index) in files" :key="index">
            <a @click="fileDownload(item)"><i class="mu-icon-svg file">파일 있음</i>{{ item.name }}</a>
            <button v-if="!item.delete_flag" type="button" class="mu-btn mu-btn-delete" @click="onClickDeleteButton">
              <i class="mu-icon-svg item-delete" :id="index"></i><span>파일 삭제</span>
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
import ObjectUtils from "@/utils/ObjectUtils";
import { successAlert, confirmAlert, errorAlert } from "@functional/alert/alert-default";
import { useNuxtApp, useRoute, useRouter } from "nuxt/app";
import { onMounted, ref, computed, ComputedRef } from "vue";
import BoardWrap from "@organisms/share/share-board-edit-inner";
import { useUserStore } from "@/store/user";

definePageMeta({
  name: "AdminSharePostUpdate",
  layout: "admin",
});

const { $ofetch } = useNuxtApp();
const route = useRoute();
const router = useRouter();

const userStore = useUserStore()

onMounted(() => {
  let id = route.query.id;
  getShareOne(id);
});

const thumbFiles = ref<Array<File>>([]);
const delete_thumb_flag = ref(false);
const files = ref<File>([]);
const delete_files = ref<Array<File>>([]);

interface File {
  file_id: number;
  file_location: string;
  file_size: string;
  name: string;
  share_id: number;
  reg_date: string;
  reg_user: string;
}

interface Share {
  id?: number;
  writer: string;
  title: string;
  contents: string;
  create_date?: string;
  update_date?: string;
}

interface EditorConfig {
  height: number;
  enterMode: string;
  uploadUrl: string;
  filebrowserUploadUrl: string;
  filebrowserUploadMethod: string;
  withCredentials: boolean;
  extraPlugins?: string;
  toolbarGroups: Array<{
    name: string;
    groups: string[];
  }>;
  removePlugins?: string;
  removeButtons?: string;
}

const required_flag = ref<boolean>(false);
const share = ref<Share>({
  writer: userStore.getUser.user_id,
  title: "",
  contents: "",
});

// 조회
const getShareOne = async (id: number) => {
  try {
    let { data: result } = await $ofetch(`/api/share/one?id=${id}`);
    files.value = result.fileList.map(el => ({
      ...el,
      is_new: false
    }));
    thumbFiles.value = result.thumbList.map(el => ({
      ...el,
      is_new: false
    }));;
    share.value = result.share;
  } catch (error) {
    console.log(error);
  }
};

// 취소 버튼 클릭 이벤트
const onClickListButton = async (): Promise<void> => {
  if (await confirmAlert("작성중인 내용을 취소하고 목록으로 돌아가시겠습니까?"))
    router.push("/admin/manager/share/board");
};

// 파일 삭제 버튼 클릭
const onClickDeleteButton = (e: Event) => {
  if (files.value[e.target.id] && files.value[e.target.id].is_new)
    files.value.splice(e.target.id, 1);
  // [10, 20] // 새로 추가된 파일이면 현재 파일 목록에서 제거
  else delete_files.value.push(files.value.splice(e.target.id, 1)[0]); // 기존 파일이면 id값을 삭제 목록에 추가
};

// 저장 버튼 클릭 이벤트
const onClickSaveButton = async () => {
  if (await confirmAlert("저장하시겠습니까?")) {
    let formData = new FormData();
    files.value.forEach((file) => {
      if (file.is_new) formData.append("files", file);
    });
    delete_files.value.forEach((file) => {
      formData.append("delete_files", file.file_id);
    });

    thumbFiles.value.forEach((thumb) => {
      if (delete_thumb_flag.value) formData.append("thumb_img", thumb);
    });

    formData.append("delete_thumb_flag", delete_thumb_flag.value);

    formData.append("share.id", share.value.id);
    formData.append("share.title", share.value.title);
    formData.append("share.contents", share.value.contents);

    $ofetch("/api/share/update/saveFile", {
      method: "POST",
      body: formData,
    }).then(async (res: { errorMessage: string }) => {
      if (res) {
        if (res.errorMessage && res.errorMessage != "") {
          await errorAlert("저장에 실패하였습니다.");
        } else {
          await successAlert("글이 저장되었습니다.");
          router.push("/admin/manager/share/board");
          return;
        }
      }
    });
  }
};

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

  e.target.files[0].is_new = true; // 첫번째 파일이 새파일임을 지정

  files.value.splice(files.value.length, 1, e.target.files[0]); // [10, 20] // 파일 크기의 인덱스부터 1개 요소 수정(파일 목록 맨 뒤에 추가)
  console.log(e.target.files)
  console.log(files.value)
};

// 썸네일 변경
const onChangeThumb = async (e: Event): Promise<void> => {
  if (e.target.files.length == 0) return // 파일 선택하지 않은 경우
  let maxSize = 10 * 1024 * 1024;
  let fileSize = e.target.files[0].size;
  let fileName = e.target.files[0].name.substring(
    e.target.files[0].name.lastIndexOf(".") + 1
  );
  let reg = /jpg|jpeg|png/i;

  if (!reg.test(fileName)) {
    await errorAlert(
      "JPG, JPEG, PNG 파일만 업로드 할 수 있습니다."
    );
    return;
  }
  if (fileSize > maxSize) {
    await errorAlert("썸네일 사이즈는 10MB 이내로 등록 가능합니다.");
    return;
  }

  e.target.files[0].is_new = true

  thumbFiles.value.splice(0, 1, e.target.files[0]);
  delete_thumb_flag.value = true;
};

// 첨부파일 다운로드
const fileDownload = async (item): Promise<void> => {
  // 파일첨부로 한 데이터 다운로드
  if (item.is_new) {
    const url = URL.createObjectURL(item);
    const a = document.createElement("a");
    a.href = url;
    a.download = item.name;
    document.body.appendChild(a);
    a.click();
    document.body.removeChild(a);
    URL.revokeObjectURL(url);
  } else { // 기존에 있던 파일 다운로드
    const config = useRuntimeConfig();

    const res = await $ofetch(`/api/service/file/fileDownload?fileId=${item.file_id}`, {
      headers: { responseType: "blob" },
    });

    const errorMessage = ObjectUtils.getPropertyValue(res, "data/errorMessage", "");
    if (!errorMessage) {
      const link = document.createElement("a");
      let url = "";

      if (document.location.href.indexOf("localhost") > -1) {
        url = config.public.baseURL + `api/service/file/fileDownload?fileId=${item.file_id}`;
      } else url = `/api/service/file/fileDownload?fileId=${item.file_id}`;

      link.href = url;
      link.style.cssText = "display:none";
      document.body.appendChild(link);
      link.click();
      link.remove();
    }
  }
};

//썸네일 삭제 버튼 클릭
const onClickDeleteButtonThumb = (e: Event) => {
  thumbFiles.value.splice(e.target.id, 1);
  delete_thumb_flag.value = true;
};
</script>
