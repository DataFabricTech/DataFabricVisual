<template>
  <div>
    <div class="inner">
      <!-- board-wrap -->
      <div class="board-wrap">
        <div class="board-head">
          <strong class="board-title">{{ post.title }}</strong>
          <div class="board-desc">
            <div class="data-info">
              <dl>
                <dt class="blind">게시일</dt>
                <dd>{{ post.upload_date }}</dd>
              </dl>
            </div>
          </div>
        </div>
        <div class="board-body">
          <!-- mu-formbox -->
          <table class="mu-formbox">
            <caption>
              데이터 요청 게시판
            </caption>
            <colgroup>
              <col style="width: 180px" />
              <col />
            </colgroup>
            <tbody>
              <tr>
                <th scope="row">분류</th>
                <td>
                  <span v-for="cat in getClassification(post?.category)" :style="'margin-right: 15px;'">{{ cat }}</span>
                </td>
              </tr>
              <tr>
                <th scope="row">신청 데이터명</th>
                <td>{{ post.data_name }}</td>
              </tr>
              <tr>
                <th scope="row">신청자</th>
                <td>
                  <div class="user-info">
                    <span class="name">{{ post.user_nm }}</span
                    ><span class="team">({{ getDeptNm(post) }})</span>
                  </div>
                </td>
              </tr>
              <tr>
                <th scope="row">신청 내용</th>
                <td>
                  <!-- inner-cms : cms영역 -->
                  <div class="inner-cms">
                    <p>{{ post.post_content }}</p>
                  </div>
                  <!-- //inner-cms : cms영역 -->
                </td>
              </tr>
              <tr>
                <th scope="row">요청기간</th>
                <td>{{ post.request_start }} ~ {{ post.request_end }}</td>
              </tr>
              <tr>
                <th scope="row">활용 목적 및 기대효과</th>
                <td>
                  <!-- inner-cms : cms영역 -->
                  <div class="inner-cms">
                    <p>{{ post.benefit_content }}</p>
                  </div>
                  <!-- //inner-cms : cms영역 -->
                </td>
              </tr>
              <tr>
                <th scope="row">첨부파일</th>
                <td>
                  <ul class="file-sch-list">
                    <li v-for="(item, index) in files" :key="index">
                      <a @click="fileDownload(item.file_id)">
                        <i class="mu-icon-svg file">첨부파일있음</i>{{ item.name }}
                      </a>
                    </li>
                  </ul>
                </td>
              </tr>
            </tbody>
          </table>
          <!-- //mu-formbox -->
          <!-- mu-button-wrap -->
          <div class="mu-button-wrap">
            <button
              type="button"
              class="mu-btn mu-btn-lg"
              @click="onClickDeletePostButton"
            >
              <span>삭제</span></button
            ><!-- 게시글 삭제용 -->
          </div>
        </div>
      </div>

      <!-- //mu-button-wrap -->
      <div class="reply-wrap">
        <div v-if="!hasDeletedAnswers" class="mu-heading">
          <h4>처리내용</h4>
        </div>

        <div v-for="(item, index) in answerList" :key="index" class="reply-form">
          <!-- mu-formbox 답변 등록 -->
          <table class="mu-formbox">
            <caption>
              데이터 요청 답변 게시판
            </caption>
            <colgroup>
              <col style="width: 180px" />
              <col />
            </colgroup>
            <tbody>
              <tr>
                <th scope="row">처리 상태</th>
                <td>
                  <div class="reply-info">
                    <em
                      v-bind:class="[
                        item.process_status == '검토중'
                          ? 'review'
                          : item.process_status == '진행중'
                          ? 'ing'
                          : item.process_status == '제공'
                          ? 'supply'
                          : item.process_status == '미제공'
                          ? 'unsupply'
                          : '',
                        'step-process',
                      ]"
                      >{{ item.process_status }}</em
                    >
                    <span class="name">{{ item.user_nm }}</span
                    ><span class="team">({{ getDeptNm(item) }})</span>
                    <span class="date"> {{ yyyyMMddHHmmSS(item.upload_date) }} </span>
                  </div>
                </td>
              </tr>
              <tr>
                <th scope="row">답변</th>
                <td>
                  <div class="inner-cms">
                    <p>{{ item.admin_reply }}</p>
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
          <!-- //mu-formbox 답변 등록 -->

          <!-- mu-button-wrap 이미 답변이 등록된 상태에서는 수정 / 삭제 기능만 수행 가능 -->
          <div class="modify-btn">
            <button
              type="button"
              class="mu-btn"
              @click="onClickUpdateReplyButton(item.id, item.answer_id)"
            >
              <span>답변 수정</span></button
            ><!-- 작성된 답변이 등록된 상태로 등록하기 페이지의 입력폼으로 이동 -->
            <button
              type="button"
              class="mu-btn"
              @click="onClickDeleteReplyButton(item.answer_id)"
            >
              <span>답변 삭제</span>
            </button>
          </div>

          <div v-if="index === lastVisibleAnswerIndex" class="mu-button-wrap">
            <button
              type="button"
              class="mu-btn mu-btn-lg"
              @click="onClickRegisterReplyButton(item.id)"
            >
              <span>답변 추가</span></button
            ><!-- 작성된 답변이 등록된 상태로 등록하기 페이지의 입력폼으로 이동 -->
            <button
              type="button"
              class="mu-btn mu-btn-lg mu-btn-primary"
              @click="onClickListButton()"
            >
              <span>목록</span>
            </button>
            123
          </div>
        </div>
      </div>

      <div class="mu-button-wrap" v-if="hasDeletedAnswers">
        <button
          type="button"
          class="mu-btn mu-btn-lg"
          @click="onClickRegisterReplyButton(post.id)"
        >
          <span>답변 추가</span></button
        ><!-- 작성된 답변이 등록된 상태로 등록하기 페이지의 입력폼으로 이동 -->
        <button
          type="button"
          class="mu-btn mu-btn-lg mu-btn-primary"
          @click="onClickListButton()"
        >
          <span>목록</span>
        </button>
      </div>

      <!-- //board-wrap -->
    </div>
  </div>
</template>

<script setup lang="ts">
import { successAlert, confirmAlert, errorAlert } from "@functional/alert/alert-default";
import { useUserStore } from "@/store/user";
import { useNuxtApp, useRoute, useRouter, useRuntimeConfig } from "nuxt/app";
import { onMounted, ref, computed, ComputedRef } from "vue";

definePageMeta({
  name: "AdminDataRequestPost",
  layout: "admin",
});

const { $ofetch } = useNuxtApp();
const route = useRoute();
const router = useRouter();
const userStore = useUserStore();

interface Post {
  admin_reply_status: boolean;
  benefit_content: string;
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
}

interface Answer {
  admin_reply: string;
  answer_id?: number;
  create_date?: string;
  dept_nm?: string;
  id: number;
  process_status: string;
  upload_date?: string;
  user_nm?: string;
  writer?: string;
  post_type?: string;
  newFlag?: string;
  lastFlag?: string;
}

onMounted(() => {
  let id = route.query.id;
  getPost(id);
});

const lastVisibleAnswerIndex: ComputedRef<number> = computed(() => {
  // 마지막 답변 인덱스
  let index = answerList.value.length - 1;
  while (index >= 0 && answerList.value[index].process_status === "삭제됨") {
    index--;
  }
  return index;
});

const hasDeletedAnswers: ComputedRef<boolean> = computed(() => {
  // 답변 전체 삭제 여부 확인
  if (answerList.value.length === 0) {
    return true;
  }
  let index = answerList.value.length - 1;
  while (index >= 0 && answerList.value[index].process_status === "삭제됨") {
    index--;
  }
  return index === -1;
});

const post = ref<Post>({});
const files = ref<Array<File>>([]);
const answerList = ref<Array<Answer>>([]);

const getDeptNm = (item) => {
  if (item) {
    if (item.dept_nm) return item.dept_nm
    else return "부서없음"
  }
  return ""
}

const getPost = async (id: number): Promise<void> => {
  try {
    let { data: result } = await $ofetch(`/api/post/one?postId=${id}`);
    result.post.post_content = result.post.post_content ? result.post.post_content : "";
    result.post.benefit_content = result.post.benefit_content
      ? result.post.benefit_content
      : "";
    files.value = result.fileList;
    answerList.value = result.answerList.filter(
      (answer: Answer) => answer.process_status != "삭제됨"
    );
    for (let i = 0; i < answerList.value.length; i++) {
      answerList.value[i].admin_reply = answerList.value[i].admin_reply
        ? answerList.value[i].admin_reply
        : "";
    }
    post.value = result.post;
  } catch (error) {
    console.log(error);
  }
};

// 목록 버튼 클릭 이벤트 핸들러
const onClickListButton = (): void => {
  router.push("/admin/manager/user/response");
};

// 삭제 버튼 클릭 이벤트 핸들러
const onClickDeletePostButton = async (): Promise<void> => {
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
          await errorAlert("삭제되었습니다.");
          router.push("/admin/manager/user/response");
        }
      }
    });
  }
};

// 답변 추가 버튼 클릭 이벤트 핸들러
const onClickRegisterReplyButton = (id: number): void => {
  userStore.setAnswerId(-1);
  router.push("/admin/manager/user/response/update?id=" + id);
};

// 답변 수정 버튼 클릭 이벤트 핸들러
const onClickUpdateReplyButton = (id: number, answer_id: number): void => {
  userStore.setAnswerId(answer_id);
  router.push("/admin/manager/user/response/update?id=" + id);
};

// 답변 삭제 버튼 클릭 이벤트 핸들러
const onClickDeleteReplyButton = async (answer_id: number): Promise<void> => {
  if (await confirmAlert("답변을 정말로 삭제하시겠습니까?")) {
    let param = {
      id: post.value.id,
      post_type: post.value.post_type,
      process_status: post.value.process_status,
      admin_reply: "",
      answer_id: answer_id,
    };

    $ofetch("/api/post/deleteAdminReply", {
      method: "POST",
      body: param,
    }).then(async (res: { errorMessage: string }) => {
      if (res) {
        if (res.errorMessage && res.errorMessage != "") {
          await errorAlert("답변 삭제에 실패하였습니다.");
        } else if (res.data == 0) {
          await errorAlert("답변 삭제에 실패하였습니다.");
        } else {
          await successAlert("답변 삭제되었습니다.");
          getPost(route.query.id);
        }
      }
    });
  }
};

// 첨부파일 다운로드
const fileDownload = async (file_id: number): Promise<void> => {
  const config = useRuntimeConfig();
  const res = await $ofetch(`/api/service/file/fileDownload?fileId=${file_id}`, {
    headers: { responseType: "blob" },
  });

  if (!res.errorMessage) {
    const link = document.createElement("a");
    let url = "";

    if (document.location.href.indexOf("localhost") > -1) {
      url = config.public.baseURL + `api/service/file/fileDownload?fileId=${file_id}`;
    } else url = `/api/service/file/fileDownload?fileId=${file_id}`;

    link.href = url;
    link.style.cssText = "display:none";
    document.body.appendChild(link);
    link.click();
    link.remove();
  }
};

const getClassification = (category) => {
  let list = []

  if (category) {
    category.split(",").forEach((el) => {
      if (el.includes("수집")) list.push(" □ 데이터 수집 ");
      if (el.includes("권한")) list.push(" □ 데이터 권한 ");
    })
  }

  return list
}

const yyyyMMddHHmmSS = (value: string): string => {
  let date = new Date(value);
  let year = date.getFullYear();
  let month = String(date.getMonth() + 1).padStart(2, "0");
  let day = String(date.getDate()).padStart(2, "0");
  let hours = String(date.getHours()).padStart(2, "0");
  let min = String(date.getMinutes()).padStart(2, "0");
  let sec = String(date.getSeconds()).padStart(2, "0");
  if (value == null || value == undefined) {
    return "";
  }
  return year + "-" + month + "-" + day + " " + hours + ":" + min + ":" + sec;
};
</script>
