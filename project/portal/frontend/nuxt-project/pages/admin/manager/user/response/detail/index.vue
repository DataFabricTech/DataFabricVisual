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
                <dd>{{ $yyyyMMdd(dataRequest.create_date) }}</dd>
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
                    <span class="name">{{ dataRequest.user_nm }}</span
                    ><span class="team">({{ getDeptNm(dataRequest) }})</span>
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
              @click="onClickDeletedataRequestButton"
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

        <div v-for="(item, index) in answerList" :key="index" class="reply-form" style="margin-top: 20px;">
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
                    <em :class="getProcessStyle(item.process_status)">{{ getProcessStatus(item.process_status) }}</em>&nbsp;
                    <span class="name">{{ item.user_nm }}</span> &nbsp;
                    <span class="team">({{ getDeptNm(item) }})</span> &nbsp;
                    <span class="date"> {{ $yyyyMMddHHmmSS(item.create_date) }} </span>
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
          <div class="modify-btn" style="margin-top: 5px;">
            <button
              type="button"
              class="mu-btn"
              @click="onClickUpdateReplyButton(item.data_request_id, item.id)"
            >
              <span>답변 수정</span></button>&nbsp;
            <button
              type="button"
              class="mu-btn"
              @click="onClickDeleteReplyButton(item.id)"
            >
              <span>답변 삭제</span>
            </button>
          </div>

          <div v-if="index === lastVisibleAnswerIndex" class="mu-button-wrap">
            <button
              type="button"
              class="mu-btn mu-btn-lg"
              @click="onClickRegisterReplyButton(item.data_request_id)"
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
          @click="onClickRegisterReplyButton(dataRequest.id)"
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
  name: "AdminDataRequestDetail",
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

const lastVisibleAnswerIndex: ComputedRef<number> = computed(() => {
  // 마지막 답변 인덱스
  let index = answerList.value.length - 1;
  while (index >= 0 && answerList.value[index].process_status == 5) {
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
  while (index >= 0 && answerList.value[index].process_status == 5) {
    index--;
  }
  return index === -1;
});

const dataRequest = ref<DataRequest>({});
const files = ref<Array<File>>([]);
const answerList = ref<Array<Answer>>([]);

const getDeptNm = (item) => {
  if (item) {
    if (item.dept_nm) return item.dept_nm
    else return "부서없음"
  }
  return ""
}

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
  if (status == 5) return '삭제됨'
}

const getDataRequestOne = async (id: number): Promise<void> => {
  try {
    let { data: result } = await $ofetch(`/api/dataRequest/one?id=${id}`);
    result.dataRequest.contents = result.dataRequest.contents ? result.dataRequest.contents : "";
    result.dataRequest.benefit_content = result.dataRequest.benefit_content
      ? result.dataRequest.benefit_content
      : "";
    files.value = result.fileList;
    answerList.value = result.answerList.filter(
      (answer: Answer) => answer.process_status != 5
    );
    for (let i = 0; i < answerList.value.length; i++) {
      answerList.value[i].admin_reply = answerList.value[i].admin_reply
        ? answerList.value[i].admin_reply
        : "";
    }
    dataRequest.value = result.dataRequest;
  } catch (error) {
    console.log(error);
  }
};

// 목록 버튼 클릭 이벤트 핸들러
const onClickListButton = (): void => {
  router.push("/admin/manager/user/response");
};

// 삭제 버튼 클릭 이벤트 핸들러
const onClickDeleteDataRequestButton = async (): Promise<void> => {
  if (await confirmAlert("정말로 삭제하시겠습니까?")) {
    let param = {
      deleteList: [dataRequest.value.id]
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
          router.push("/admin/manager/user/response");
        }
      }
    });
  }
};

// 답변 추가 버튼 클릭 이벤트 핸들러
const onClickRegisterReplyButton = (data_request_id: number): void => {
  userStore.setAnswerId(-1);
  router.push("/admin/manager/user/response/update?id=" + data_request_id);
};

// 답변 수정 버튼 클릭 이벤트 핸들러
const onClickUpdateReplyButton = (data_request_id: number, id: number): void => {
  userStore.setAnswerId(id);
  router.push("/admin/manager/user/response/update?id=" + data_request_id);
};

// 답변 삭제 버튼 클릭 이벤트 핸들러
const onClickDeleteReplyButton = async (id: number): Promise<void> => {
  if (await confirmAlert("답변을 정말로 삭제하시겠습니까?")) {
    let param = {
      data_request_id: dataRequest.value.id,
      process_status: dataRequest.value.process_status,
      admin_reply: "",
      id: id,
    };

    $ofetch("/api/dataRequest/deleteAdminReply", {
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
          getDataRequestOne(route.query.id);
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

</script>
