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
                    <span class="name">{{ post.user_nm }}</span>
                    <span class="team">({{ post.dept_nm }})</span>
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
                      <a href="#none" title="첨부파일다운로드"
                        ><i class="mu-icon-svg file">첨부파일있음</i>{{ item.name }}</a
                      >
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
          <!-- //mu-button-wrap -->
          <div class="reply-wrap">
            <div class="mu-heading">
              <h4>처리내용</h4>
            </div>
            <!-- mu-formbox 답변 등록 -->
            <div class="reply-form">
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
                    <th scope="row">처리 상태<strong class="required">필수</strong></th>
                    <td>
                      <div class="mu-item-group">
                        <div class="mu-radio">
                          <input
                            type="radio"
                            id="r1"
                            name="r1"
                            value="검토중"
                            v-model="answer.process_status"
                            checked="checked"
                          /><label for="r1"
                            ><em class="step-process review">검토중</em></label
                          >
                        </div>
                        <div class="mu-radio">
                          <input
                            type="radio"
                            id="r2"
                            name="r2"
                            value="진행중"
                            v-model="answer.process_status"
                          />
                          <label for="r2"><em class="step-process ing">진행중</em></label>
                        </div>
                        <div class="mu-radio">
                          <input
                            type="radio"
                            id="r3"
                            name="r3"
                            value="제공"
                            v-model="answer.process_status"
                          />
                          <label for="r3"
                            ><em class="step-process supply">제공</em></label
                          >
                        </div>
                        <div class="mu-radio">
                          <input
                            type="radio"
                            id="r4"
                            name="r4"
                            value="미제공"
                            v-model="answer.process_status"
                          />
                          <label for="r4"
                            ><em class="step-process unsupply">미제공</em></label
                          >
                        </div>
                      </div>
                    </td>
                  </tr>
                  <tr>
                    <th scope="row">답변<strong class="required">필수</strong></th>
                    <td>
                      <textarea
                        class="mu-area"
                        title="답변 내용 입력"
                        rows="10"
                        v-model="answer.admin_reply"
                      ></textarea>
                    </td>
                  </tr>
                </tbody>
              </table>
              <!-- //mu-formbox -->
              <div class="modify-btn">
                <button type="button" class="mu-btn" @click="onClickListButton">
                  <span>답변 취소</span></button
                ><!-- 답변취소용 -->
                <button
                  type="button"
                  class="mu-btn mu-btn-primary"
                  @click="onClickSaveButton"
                >
                  <span>답변 확인</span></button
                ><!-- 목록으로 돌아가기 -->
              </div>
            </div>
          </div>
          <div class="mu-button-wrap">
            <button
              type="button"
              class="mu-btn mu-btn-lg mu-btn-primary"
              @click="onClickListButton(post.id)"
            >
              <span>목록</span>
            </button>
          </div>
        </div>
      </div>
      <!-- //board-wrap -->
      <!-- mu-button-wrap -->

      <!-- //mu-button-wrap -->
    </div>
  </div>
</template>

<script setup lang="ts">
import { successAlert, confirmAlert, errorAlert } from "@functional/alert/alert-default";
import { useUserStore } from "@/store/user";
import { useNuxtApp, useRoute, useRouter } from "nuxt/app";
import { onMounted, ref } from "vue";

definePageMeta({
  name: "AdminDataRequestPostUpdate",
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

const post = ref<Post>({
  admin_reply_status: false,
  benefit_content: "",
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
const files = ref<Array<File>>([]);
const answerList = ref<Array<Answer>>([]);
const answer = ref<Answer>({
  admin_reply: "",
  id: 0,
  process_status: "",
});
const newFlag = ref<string>("N"); // 첫 답변인 경우
const lastFlag = ref<string>("N"); // 마지막 답변인 경우

const getPost = async (id: number): Promise<void> => {
  try {
    let { data: result } = await $ofetch(`/api/post/one?postId=${id}`);
    result.post.post_content = result.post.post_content ? result.post.post_content : "";
    result.post.benefit_content = result.post.benefit_content
      ? result.post.benefit_content
      : "";
    result.files = result.fileList;
    result.answerList.forEach((el) => {
      if (el.process_status != '삭제됨') {
        answerList.value.push(el)
      }

    });
    if (userStore.getAnswerId < 0) {
      // 첫 답변인 경우
      newFlag.value = "Y";
      answer.value = { id: result.post.id, process_status: "검토중", admin_reply: "" };
    } else {
      lastFlag.value =
        userStore.getAnswerId == answerList.value[answerList.value.length - 1].answer_id
          ? "Y"
          : "N";
      answer.value =
        answerList.value[
          answerList.value.findIndex((el) => el.answer_id === userStore.getAnswerId)
        ];
    }

    post.value = result.post;
  } catch (error) {
    console.log(error);
  }
};

// 저장 버튼 클릭 이벤트 핸들러
const onClickSaveButton = async (): Promise<void> => {
  if (await confirmAlert("저장하시겠습니까?")) {
    post.value.process_status = answer.value.process_status;
    let param = {
      post: post.value,
      answer: answer.value,
    };

    answer.value.post_type = post.value.post_type;
    answer.value.newFlag = newFlag.value;
    answer.value.lastFlag = lastFlag.value;

    $ofetch("/api/post/updateAdminReply", {
      method: "POST",
      body: param,
    }).then(async (res: { errorMessage: string }) => {
      if (res) {
        if (res.errorMessage && res.errorMessage != "") {
          await errorAlert("저장에 실패하였습니다.");
        } else {
          await successAlert("저장되었습니다.");
          router.push({
            path: "/admin/manager/user/response/detail",
            query: { id: route.query.id },
          });
        }
      }
    });
  }
};

// 목록 버튼, 답변 취소 클릭 이벤트 핸들러
const onClickListButton = async (id: number): Promise<void> => {
  if (await confirmAlert("작성중인 내용을 취소하시겠습니까?")) {
    router.push({
      path: "/admin/manager/user/response/detail",
      query: { id: route.query.id },
    });
  }
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
          router.push({
            path: "/admin/manager/user/response/detail",
            query: { id: route.query.id },
          });
        }
      }
    });
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
</script>
