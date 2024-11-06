<template>
  <div class="contents">
    <div>
      <!-- 제목위치가 컨텐츠 최상단에 위치해야함. -->
      <!-- 관리자에서 상태 변경 시, 상태 라벨(신청apply – 검토중review – 진행중ing – 제공supply/미제공unsupply) 변경 -->
      <strong class="board-title"><em class="step-process apply">{{ post?.process_status }}</em> {{ post?.title }}</strong>

      <div class="inner">
        <div class="board-wrap">
          <div class="board-head">
            <div class="board-desc">
              <div class="data-info">
                <dl>
                  <dt>요청자</dt>
                  <dd>{{ post?.user_nm }}</dd>
                  <dt>요청일</dt>
                  <dd>{{ post?.upload_date }}</dd>
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
                <col style="width: 190px" />
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
                  <td>{{ post?.data_name }}</td>
                </tr>
                <tr>
                  <th scope="row">신청자</th>
                  <td>
                    <div class="user-info">
                      <span class="name">{{ post?.user_nm }}</span
                      ><span class="team">({{ getDeptNm(post) }})</span>
                    </div>
                  </td>
                </tr>
                <tr>
                  <th scope="row">신청 내용</th>
                  <td>
                    <!-- inner-cms : cms영역 -->
                    <div class="inner-cms data-request-content">
                      <p>{{ post?.post_content }}</p>
                    </div>
                    <!-- //inner-cms : cms영역 -->
                  </td>
                </tr>
                <tr>
                  <th scope="row">요청기간</th>
                  <td>{{ post?.request_start }} ~ {{ post?.request_end }}</td>
                </tr>
                <tr>
                  <th scope="row">활용 목적 및 기대효과</th>
                  <td>
                    <!-- inner-cms : cms영역 -->
                    <div class="inner-cms data-request-content">
                      <!--                    <p v-html='post?.benefit_content'></p>-->
                      <p>{{ post?.benefit_content }}</p>
                    </div>
                    <!-- //inner-cms : cms영역 -->
                  </td>
                </tr>
                <tr>
                  <th scope="row">첨부파일</th>
                  <td>
                    <ul class="file-sch-list">
                      <li v-for="(item, index) in files" :key="index">
                        <!-- <a :href="getDownloadUrl(item.file_id)">  -->
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

            <!-- 상태값이 신청이 아닌경우에만 -->
            <!-- mu-button-wrap -->
            <div v-if="!hasDeletedAnswers" class="mu-button-wrap mb40">
              <button
                type="button"
                class="mu-btn mu-btn-lg"
                :disabled="post?.writer !== userStore.getUser.user_id"
                @click="onClickDeleteButton"
              >
                <span>삭제</span>
              </button>
            </div>
            <!-- //mu-button-wrap -->
            <div v-for="(item, index) in answerList" :key="index" class="reply-box">
              <div class="reply-head">
                <h4><i class="mu-icon-svg complete"></i> 처리내용</h4>
                <div class="mu-item-group">
                  <em class="step-process supply">제공</em>
                </div>
              </div>
              <!-- reply-box : 관리자답변보기 -->
              <div class="reply-cont">
                <div class="user-info">
                  <span class="name">{{ item.user_nm }}</span>
                  <span class="team">({{ getDeptNm(item) }})</span>
                  <span class="date">({{ formatDate(item.upload_date) }})</span>
                </div>
                <div class="inner-cms">
                  <p>{{ item.admin_reply }}</p>
                </div>
              </div>
            </div>
            <!-- //reply-box -->
            <!-- 상태값이 신청이 아닌경우에만 -->
          </div>
        </div>
        <!-- mu-button-wrap -->
        <div class="mu-button-wrap">
          <button
            v-if="hasDeletedAnswers"
            type="button"
            class="mu-btn mu-btn-lg"
            @click="onClickUpdateButton"
            :disabled="post?.writer !== userStore.getUser.user_id"
          >
            <span>수정</span></button
          ><!-- 신청 상태일 경우에만 수정 가능 / 그 외, 삭제만 가능(수정 버튼 미생성) -->
          <button
            v-if="hasDeletedAnswers"
            type="button"
            class="mu-btn mu-btn-lg"
            @click="onClickDeleteButton"
            :disabled="post?.writer !== userStore.getUser.user_id"
          >
            <span>삭제</span>
          </button>
          <button
            type="button"
            class="mu-btn mu-btn-lg mu-btn-primary"
            @click="onClickListButton"
          >
            <span>목록</span>
          </button>
        </div>
        <!-- //mu-button-wrap -->
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import ObjectUtils from "@/utils/ObjectUtils";
import { confirmAlert, errorAlert } from "@functional/alert/alert-default";
import { useUserStore } from "@/store/user";
import { onMounted, computed, ref } from "vue";
import { useNuxtApp, useRoute, useRouter } from "nuxt/app";
import { successAlert } from "@/components/functional/alert/alert-default";

definePageMeta({
  name: "DataRequestPost",
  layout: "user",
});

interface Answer {
  process_status: string;
  user_nm: string;
  dept_nm: string;
  admin_reply: string;
  upload_date: Date;
}

interface Post {
  id: number;
  post_type: string;
  process_status: string;
  title: string;
  upload_date: string;
  data_name: string;
  user_nm: string;
  dept_nm: string;
  post_content: string;
  request_start: string;
  request_end: string;
  benefit_content: string;
  writer: string;
}
interface File {
  file_id: number;
  name: string;
}

const answerList = ref<Answer[]>([]);
const post = ref<Post | null>(null);
const files = ref<File[]>();

const { $ofetch } = useNuxtApp();
const userStore = useUserStore();
const route = useRoute();
const router = useRouter();

const formatDate = (value: string | number | Date): string => {
  const date = new Date(value);
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, "0");
  const day = String(date.getDate()).padStart(2, "0");
  const hours = String(date.getHours()).padStart(2, "0");
  const minutes = String(date.getMinutes()).padStart(2, "0");
  const seconds = String(date.getSeconds()).padStart(2, "0");

  return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
};

onMounted(() => {
  let id = route.query.id;
  getPost(id);
});

const lastVisibleAnswerIndex = computed(() => {
  // 마지막 답변 인덱스
  let index = answerList.value.length - 1;
  while (index >= 0 && answerList.value[index].process_status === "삭제됨") {
    index--;
  }
  return index;
});

const hasDeletedAnswers = computed(() => {
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

const getDeptNm = (item) => {
  if (item) {
    if (item.dept_nm) return item.dept_nm
    else return "부서없음"
  }
  return ""
}

const getPost = async (id: string | string[]) => {
  try {
    let { data: result } = await $ofetch(`/api/post/one?postId=${id}`);
    // url 접근 시 권한 없는 경우 접근 불가
    if (result.accessOk == false) {
      errorAlert("상세 요청내용은 작성자만 볼 수 있습니다.");
      router.push({ path: `/data/request` });
      return;
    }
    result.post.post_content = result.post.post_content ? result.post.post_content : "";
    result.post.benefit_content = result.post.benefit_content
      ? result.post.benefit_content
      : "";
    result.post.admin_reply = result.post.admin_reply ? result.post.admin_reply : "";
    files.value = result.fileList;

    result.answerList.forEach((el) => {
      if (el.process_status != '삭제됨') {
        answerList.value.push(el)
      }
    })

    for (let i = 0; i < answerList.value.length; i++) {
      answerList.value[i].admin_reply = answerList.value[i].admin_reply
        ? answerList.value[i].admin_reply
        : "";
    }
    return (post.value = result.post);
  } catch (error) {
    console.log(error);
  }
};

// 수정 버튼 클릭 이벤트 핸들러
const onClickUpdateButton = () => {
  router.push("/data/request/update?id=" + post.value?.id);
};

// 삭제 버튼 클릭 이벤트 핸들러
const onClickDeleteButton = async () => {
  if (!(await confirmAlert("정말로 삭제하시겠습니까?"))) return;

  let param = {
    deleteList: [post.value?.id],
    post_type: post.value?.post_type,
  };
  await $ofetch("/api/post/delete", {
    method: "POST",
    body: param,
  }).then(async (res: { errorMessage: string }) => {
    if (res) {
      if (res.errorMessage && res.errorMessage != "") {
        await errorAlert("삭제에 실패하였습니다.");
      } else {
        await successAlert("삭제되었습니다.");
        router.push("/data/request");
      }
    }
  });
};

// 목록 버튼 클릭 이벤트 핸들러
const onClickListButton = () => {
  router.push("/data/request");
};

// 첨부파일 다운로드
const fileDownload = async (file_id: any) => {
  const config = useRuntimeConfig();
  const res = await $ofetch(`/api/service/file/fileDownload?fileId=${file_id}`, {
    headers: { responseType: "blob" },
  });

  const errorMessage = ObjectUtils.getPropertyValue(res, "data/errorMessage", "");
  if (!errorMessage) {
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
