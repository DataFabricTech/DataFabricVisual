<template>
  <div>
    <div class="inner">
      <div class="board-list-wrap">
        <div class="mu-heading">
          <div class="mu-checkbox">
            <input
              type="checkbox"
              id="chkAll"
              title="행선택"
              v-model="allChecked"
              @click="checkedAll($event.target.checked)"
            />
            <label for="chkAll"><span class="blind">전체선택</span></label>
          </div>
          <!-- board-list-search -->
          <faq-border-list-search :searchObj.sync="searchObj" @searchMethod="getPost" />

          <faq-ord-update-modal
            ref="faqOrdupdateModal"
            v-model:visible="visible"
            @searchMethod="getPost"
          />

          <page-size v-model:pageSize="pagingObj.limit" @searchMethod="getPost" />
        </div>

        <!-- mu-accordion : 아코디언타입 FAQ리스트 타 질문 클릭 시, 확장된 본 답변 영역이 축소되며 선택된 질문에 대한 답변 영역이 확장됨 / 확장 가능한 답변 영역 총 1개 -->
        <ul class="mu-accordion">
          <li v-for="(item, index) in postList" :key="index" class="mu-accordion-item">
            <div class="mu-checkbox">
              <input
                type="checkbox"
                :id="'check_' + item.id"
                title="행선택"
                :value="item.id"
                v-model="item.selected"
                @change="selected(item.id)"
              />
              <label :for="'check_' + item.id"><span class="blind">행선택</span></label>
            </div>

            <div
              v-bind:class="[item.showAnswer ? 'active' : '', 'mu-accordion-head']"
              @click="onClickShowAnswerButton(index)"
            >
              <!-- active시 mu-accordion-body 가 block -->
              <div class="question">
                <span class="ico-question">질문</span>
                <em class="data-label">{{ item.category }}</em>
                <p class="inner-cms-share" v-html="item.post_content"></p>
              </div>
            </div>
            <div v-if="item.showAnswer" class="mu-accordion-body">
              <div class="answer">
                <span class="ico-answer">답변</span>
                <p class="inner-cms-share" v-html="item.admin_reply"></p>
              </div>
            </div>
          </li>
          <li v-if="postList.length === 0">
            <p class="nodata">FAQ가 존재하지 않습니다.</p>
          </li>
        </ul>

        <pagination :paging="pagingObj" @onClickPage="onClickPage" />
      </div>
      <!-- mu-button-wrap -->
      <div class="mu-button-wrap">
        <button type="button" class="mu-btn mu-btn-lg" @click="onClickDeleteButton">
          <span>삭제</span>
        </button>
        <button type="button" class="mu-btn mu-btn-lg" @click="onClickOrdUpdateButton">
          <span>순서변경</span>
        </button>
        <button
          type="button"
          class="mu-btn mu-btn-lg mu-btn-primary"
          @click="onClickUpdateButton"
        >
          <span>수정</span>
        </button>
        <button
          type="button"
          class="mu-btn mu-btn-lg mu-btn-primary"
          @click="onClickRegisterButton"
        >
          <span>등록</span>
        </button>
      </div>
      <!-- //mu-button-wrap -->
    </div>
  </div>
</template>

<script setup lang="ts">
import PageSize from "@/pages/admin/manager/user/page-size";
import Pagination from "@functional/pagination/pagination";
import FaqBorderListSearch from "@organisms/post/faq-border-list-search";
import FaqOrdUpdateModal from "@organisms/post/faq-ord-update-modal";
import { successAlert, confirmAlert, errorAlert } from "@functional/alert/alert-default";
import { useNuxtApp, useRouter } from "nuxt/app";
import { onMounted, ref, shallowRef } from "vue";

definePageMeta({
  name: "AdminFaqManage",
  layout: "admin",
});

const { $ofetch } = useNuxtApp();
const router = useRouter();

interface PagingObj {
  total: number;
  limit: number;
  block: number;
  currentPage: number;
}

interface Post {
  admin_reply: string;
  admin_reply_status: boolean;
  category: string;
  file_cnt: number;
  id: number;
  is_open: boolean;
  new_yn: boolean;
  ord_no: number;
  post_content: string;
  post_type: string;
  rownum: number;
  title: string;
  upload_date: string;
  user_nm: string;
  view_count: number;
  writer: string;
  selected?: boolean;
  showAnswer?: boolean;
}

interface SearchObj {
  searchType: string;
  searchWord: string;
}

const faqOrdupdateModal = shallowRef(null);

const pagingObj = ref<PagingObj>({
  total: 0,
  limit: 10,
  block: 10,
  currentPage: 1,
});

const postList = ref<Array<Post>>([]);
const checkedList = ref<Array<number>>([]);
const allChecked = ref<boolean>(false);
const visible = ref<boolean>(false);
const searchObj = ref<SearchObj>({
  searchType: "all",
  searchWord: "",
});

onMounted(() => {
  getPost();
});

// FAQ 목록 조회
const getPost = async (offset: number = 1): Promise<void> => {
  try {
    let { data } = await $ofetch(
      `/api/post/list?postType=faq&pageSize=${pagingObj.value.limit}&pnum=${offset}&searchType=${searchObj.value.searchType}&searchWord=${searchObj.value.searchWord}`
    );

    pagingObj.value = {
      ...pagingObj.value,
      total: data.total,
      currentPage: offset,
    };

    postList.value = data.list;
    checkedList.value = [];
  } catch (e) {
    console.log(e);
  }
};

// 삭제 버튼 클릭
const onClickDeleteButton = async (): Promise<void> => {
  if (checkedList.value.length < 1) {
    await errorAlert("한건 이상 체크후 삭제하시기 바랍니다.");
    return;
  }

  if (await confirmAlert("정말로 삭제하시겠습니까?")) {
    let param = {
      deleteList: checkedList.value,
      post_type: postList.value[0].post_type,
    };

    $ofetch("/api/post/delete", {
      method: "POST",
      body: param,
    }).then(async (res) => {
      if (res) {
        if (res.errorMessage && res.errorMessage != "") {
          await errorAlert("삭제에 실패하였습니다.");
        } else {
          await successAlert("삭제되었습니다.");
          await getPost();
        }
      }
    });
  }
};

// 전체선택 체크박스 클릭
const checkedAll = (checked: boolean): void => {
  allChecked.value = checked;
  for (let i in postList.value) {
    postList.value[i].selected = allChecked.value;
    if (allChecked.value) {
      if (checkedList.value.findIndex((v) => v == postList.value[i].id) >= 0) continue;
      checkedList.value.push(postList.value[i].id);
    } else {
      checkedList.value = [];
    }
  }
};

// 체크박스 클릭
const selected = (postId: number): void => {
  const selectedItem = postList.value.find((v) => v.id == postId);
  if (selectedItem.selected) {
    checkedList.value.push(postId);
  } else {
    checkedList.value.splice(
      checkedList.value.findIndex((v) => v == postId),
      1
    );
  }

  for (let i in postList.value) {
    if (!postList.value[i].selected) {
      allChecked.value = false;
      return;
    } else {
      allChecked.value = true;
    }
  }
};

// 순서 변경버튼 클릭
const onClickOrdUpdateButton = (): void => {
  faqOrdupdateModal.value.getPost();
  visible.value = !visible.value;
};

// 답변보기 클릭
const onClickShowAnswerButton = (index: number): void => {
  postList.value[index].showAnswer = !postList.value[index].showAnswer;

  for (var i = 0; i < postList.value.length; i++) {
    if (i == index) continue;
    postList.value[i].showAnswer = false;
  }
};

// 등록 버튼 클릭
const onClickRegisterButton = (): void => {
  router.push("/admin/manager/user/faq/register");
};

//수정 버튼 클릭
const onClickUpdateButton = async (): Promise<void> => {
  if (checkedList.value.length == 1) {
    router.push("/admin/manager/user/faq/update?id=" + checkedList.value[0]);
    return;
  }
  await errorAlert("수정할 게시물을 한건만 선택해주세요.");
  return;
};

// 페이지 번호 클릭
const onClickPage = (page: number): void => {
  allChecked.value = false;
  checkedList.value = [];
  getPost(page);
};
</script>
<style scoped>
  /* FAQ 체크박스 덮어쓰는 부분 수정 */
  .mu-checkbox {
    width: 40px;              /* 체크박스의 너비를 명확하게 설정 */
  }

  .mu-accordion-head {
    flex-grow: 1;             /* 나머지 공간을 모두 차지 */
    margin-left: 20px;
  }

</style>
