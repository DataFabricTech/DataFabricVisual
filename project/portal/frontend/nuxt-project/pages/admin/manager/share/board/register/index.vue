<template>
  <div>
    <board-wrap :share="share" @cancelMethod="onClickListButton" @saveMethod="onClickSaveButton">
      <template v-slot:heading>
        <h4>활용사례 등록</h4>
      </template>
      <template v-slot:caption>
        <caption>
          활용사례 등록 게시판
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
            <i class="mu-icon-svg file">썸네일있음</i>{{ item.name }}
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
            <i class="mu-icon-svg file">파일있음</i>{{ item.name }}
            <button type="button" class="mu-btn mu-btn-delete" @click="onClickDeleteButton">
              <i class="mu-icon-svg item-delete" :id="index"></i><span>썸네일 삭제</span>
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
import { successAlert, confirmAlert, errorAlert } from "@functional/alert/alert-default";
import { useUserStore } from "@/store/user";
import { useNuxtApp, useRouter } from "nuxt/app";
import { ref, computed, ComputedRef } from "vue";
import BoardWrap from "@organisms/share/share-board-edit-inner";

definePageMeta({
  name: "AdminShareRegister",
  layout: "admin",
});

const { $ofetch } = useNuxtApp();
const router = useRouter();
const userStore = useUserStore();

const thumbFiles = ref<Array<File>>([]);
const files = ref<Array<File>>([]);

interface File {
  lastModified: number;
  lastModifiedDate: Date;
  name: string;
  size: number;
  type: string;
  webkitRelativePath: string;
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
  extraPlugins: string;
  toolbarGroups: Array<{
    name: string;
    groups: string[];
  }>;
  removePlugins: string;
  removeButtons: string;
}

const share = ref<Share>({
  writer: userStore.getUser.user_id,
  title: "",
  contents: "",
});

// 취소버튼 클릭 이벤트
const onClickListButton = (): void => {
  router.push("/admin/manager/share/board");
};

// 저장버튼 클릭 이벤트
const onClickSaveButton = async (): Promise<void> => {

  if (!(await confirmAlert("저장하시겠습니까?"))) return;


  let formData = new FormData();
  files.value.forEach((file) => {
    formData.append("files", file);
  });

  thumbFiles.value.forEach((thumb) => {
    formData.append("thumb_img", thumb);
  });

  formData.append("share.title", share.value.title);
  formData.append("share.contents", share.value.contents);

  $ofetch("/api/share/add/saveFile", {
    method: "POST",
    body: formData,
  }).then(async (res: { errorMessage: string }) => {
    if (res) {
      if (res.errorMessage && res.errorMessage != "") {
        await errorAlert("저장에 실패하였습니다.");
      } else {
        await successAlert("글이 저장되었습니다.");
        router.push("/admin/manager/share/board");
      }
    } else {
      await errorAlert("저장에 실패하였습니다.");
    }
  });
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

  files.value.splice(files.value.length, 1, e.target.files[0]); // [10, 20]
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

  thumbFiles.value.splice(thumbFiles.value.length, 1, e.target.files[0]); // [10, 20]
};


// 삭제 버튼 클릭
const onClickDeleteButton = (e: Event) => {
  files.value.splice(e.target.id, 1); // [10, 20]
};

//썸네일 삭제 버튼 클릭
const onClickDeleteButtonThumb = (e: Event) => {
  thumbFiles.value.splice(e.target.id, 1);
};


</script>
