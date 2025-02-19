<template>
  <div>
    <div class="inner">
      <!-- board-wrap -->
      <div class="board-wrap">
        <div class="board-head">
          <strong class="board-title">{{ dataRequest.title }}</strong>
          <div class="board-desc">
            <div class="data-info">
              <dl>
                <dt class="blind">게시일</dt>
                <dd>{{ dataRequest.create_date }}</dd>
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
                    <span>{{ getClassification(dataRequest) }}</span>
                  </td>
                </tr>
              <tr>
                <th scope="row">신청 상태</th>
                <td>
                  <span :class="getProcessStyle(dataRequest.process_status)">{{ getProcessStatus(dataRequest.process_status) }}</span>
                </td>
              </tr>
              <tr>
                <th scope="row">신청 데이터명</th>
                <td>{{ dataRequest.data_name }}</td>
              </tr>
              <tr>
                <th scope="row">신청자</th>
                <td>
                  <div class="user-info">
                    <span class="name">{{ dataRequest.user_nm }}</span>
                    <span class="team">({{ dataRequest.dept_nm }})</span>
                  </div>
                </td>
              </tr>
              <tr>
                <th scope="row">신청 내용</th>
                <td>
                  <!-- inner-cms : cms영역 -->
                  <div class="inner-cms">
                    <p>{{ dataRequest.contents }}</p>
                  </div>
                  <!-- //inner-cms : cms영역 -->
                </td>
              </tr>
              <tr>
                <th scope="row">요청기간</th>
                <td>{{ dataRequest.request_start }} ~ {{ dataRequest.request_end }}</td>
              </tr>
              <tr>
                <th scope="row">활용 목적 및 기대효과</th>
                <td>
                  <!-- inner-cms : cms영역 -->
                  <div class="inner-cms">
                    <p>{{ dataRequest.benefit_content }}</p>
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
              @click="onClickDeleteDataRequestButton"
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
                            value=1
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
                            value=2
                            v-model="answer.process_status"
                          />
                          <label for="r2"><em class="step-process ing">진행중</em></label>
                        </div>
                        <div class="mu-radio">
                          <input
                            type="radio"
                            id="r3"
                            name="r3"
                            value=3
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
                            value=4
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
              <div class="modify-btn" style="margin-top: 10px;">
                <button type="button" class="mu-btn" @click="onClickListButton">
                  <span>답변 취소</span></button> &nbsp;
                <button
                  type="button"
                  class="mu-btn mu-btn-primary"
                  @click="onClickSaveButton"
                >
                  <span>답변 확인</span></button>
              </div>
            </div>
          </div>
          <div class="mu-button-wrap">
            <button
              type="button"
              class="mu-btn mu-btn-lg mu-btn-primary"
              @click="onClickListButton(dataRequest.id)"
            >
              <span>취소</span>
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
  name: "AdminDataRequestUpdate",
  layout: "admin",
});

const { $ofetch } = useNuxtApp();
const route = useRoute();
const router = useRouter();
const userStore = useUserStore();

interface DataRequest {
  id: number
	writer: string
	request_type: number
	title: string
	data_name: string
	contents: string
	request_start: string
	request_end: string
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
}

interface Answer {
  id?: number;
  data_request_id: number;
  writer?: string;
  process_status: number;
  admin_reply: string;
  create_date?: string;
  update_date?: string;
  user_nm?: string;
  newFlag?: string;
  lastFlag?: string;
}

onMounted(() => {
  let id = route.query.id;
  getDataRequestOne(id);
});

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

const files = ref<Array<File>>([]);
const answerList = ref<Array<Answer>>([]);
const answer = ref<Answer>({
  admin_reply: "",
  data_request_id: 0,
  process_status: 0,
});
const newFlag = ref<string>("N"); // 첫 답변인 경우
const lastFlag = ref<string>("N"); // 마지막 답변인 경우

const getClassification = (dataRequest) => {
  if (dataRequest) {
    if (dataRequest.request_type == 0) return "수집"
    if (dataRequest.request_type == 1) return "권한"
    if (dataRequest.request_type == 2) return "수집, 권한"
  } else {
    return ''
  }
}

const getProcessStyle = (status) => {
  let className = 'step-process'
  if (status == 0) return 'apply ' + className
  if (status == 1) return 'review ' + className
  if (status == 2) return 'ing ' + className
  if (status == 3) return 'supply ' + className
  if (status == 4) return 'unsupply ' + className
  else return ' ' + className
}

const getProcessStatus = (status) => {
  if (status == 0) return '신청'
  if (status == 1) return '검토중'
  if (status == 2) return '진행중'
  if (status == 3) return '제공'
  if (status == 4) return '미제공'
  if (status == 5) return '-'
}

const getDataRequestOne = async (id: number): Promise<void> => {
  try {
    let { data: result } = await $ofetch(`/api/dataRequest/one?id=${id}`);
    result.dataRequest.contents = result.dataRequest.contents ? result.dataRequest.contents : "";
    result.dataRequest.benefit_content = result.dataRequest.benefit_content
      ? result.dataRequest.benefit_content
      : "";
    result.files = result.fileList;
    result.answerList.forEach((el) => {
      if (el.process_status != 5) {
        answerList.value.push(el)
      }

    });

    if (userStore.getAnswerId < 0) {
      // 첫 답변인 경우
      newFlag.value = "Y";
      answer.value = { data_request_id: result.dataRequest.id, process_status: 1, admin_reply: "" };
    } else {
      if (answerList.value && answerList.value.length > 0) {
        lastFlag.value =
        userStore.getAnswerId == answerList.value[answerList.value.length - 1].id
          ? "Y"
          : "N";
        answer.value = answerList.value[answerList.value.findIndex((el) => el.id === userStore.getAnswerId)];
      }

    }

    dataRequest.value = result.dataRequest;
  } catch (error) {
    console.log(error);
  }
};

// 저장 버튼 클릭 이벤트 핸들러
const onClickSaveButton = async (): Promise<void> => {
  if (await confirmAlert("저장하시겠습니까?")) {
    dataRequest.value.process_status = answer.value.process_status;
    let param = {
      dataRequest: dataRequest.value,
      answer: answer.value,
    };

    answer.value.newFlag = newFlag.value;
    answer.value.lastFlag = lastFlag.value;

    $ofetch("/api/dataRequest/updateAdminReply", {
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

// 취소, 답변 취소 클릭 이벤트 핸들러
const onClickListButton = async (id: number): Promise<void> => {
  if (await confirmAlert("작성중인 내용을 취소하시겠습니까?")) {
    router.push({
      path: "/admin/manager/user/response/detail",
      query: { id: route.query.id },
    });
  }
};

// 삭제 버튼 클릭 이벤트 핸들러
const onClickDeleteDataRequestButton = async (): Promise<void> => {
  if (await confirmAlert("정말로 삭제하시겠습니까?")) {
    let param = {
      deleteList: [dataRequest.value.id],
    };

    $ofetch("/api/dataRequest/delete", {
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

</script>
