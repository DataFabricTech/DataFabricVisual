<template>
  <div class="detail-section" :class="showData ? 'open' : 'close'">
    <div class="mu-heading">
      <h4>사용자 리뷰</h4>
      <div class="mu-item-group">
        <button
          type="button"
          class="mu-btn mu-btn-bg-non btn-section-toggle"
          title="접기"
          @click="toggleShowData()"
          style="margin-left: 10px"
        >
          <span>{{ toggleShowDataText }}</span>
        </button>
      </div>
    </div>
    <div class="detail-box" v-show="showData">
      <div class="comment-wrap">
        <div class="comment-write">
          <div class="comment-write-box">
            <div class="comment-form">
              <textarea
                class="mu-area"
                placeholder="리뷰를 작성해주세요. (최대200자)"
                title="리뷰 입력"
                maxlength="200"
                v-model="userReview"
              />
              <button type="button" class="mu-btn mu-btn-primary" @click="saveUserReview">
                <span>등록</span>
              </button>
            </div>
            <div class="comment-write-bottom">
              <div class="letter-count">
                <strong>{{ userReviewTextCounts }}</strong> / 200
              </div>
            </div>
          </div>
        </div>

        <!-- 리뷰 뷰 폼-->
        <div
          v-for="(item, index) in props.list" :key="index"
          class="comment-view"
        >
          <div class="user-image"><i class="mu-icon-svg user"></i></div>
          <div class="comment-cont">
            <div class="comment-info">
              <span class="name">{{ item.user_nm }}</span>
              <span class="team" v-if="item.dept_nm">({{ item.dept_nm }})</span>
              <span class="team" v-if="item.dept_nm == undefined">(부서 없음)</span>
              <span class="date">{{ item.review_date }}</span>
            </div>
            <div class="comment-message">
              {{ item.review }}
            </div>
            <!-- 댓글 수정 버튼 : 내 댓글에서만 수정 -->
            <div class="modify-btn" v-if="userStore.getUser.user_id === item.user_id">
              <button
                type="button"
                class="mu-btn mu-btn-sm mu-btn-bg-non"
                title="해당답변 수정"
                @click="onClickEditUserReview(item)"
              >
                <span>수정</span><i class="mu-icon-svg edit"></i>
              </button>
              <button
                type="button"
                class="mu-btn mu-btn-sm mu-btn-bg-non"
                title="해당답변 삭제"
                @click="onClickDeleteUserReview(item.review_id)"
              >
                <span>삭제</span><i class="mu-icon-svg del"></i>
              </button>
            </div>
            <!-- 리뷰 수정 폼-->
            <div
              v-if="
                editingReviewObj.display &&
                editingReviewObj.review.review_id === item.review_id
              "
              class="comment-write"
            >
              <div class="comment-write-box">
                <form action="">
                  <fieldset>
                    <legend>내 답변 수정</legend>
                    <textarea
                      class="mu-area"
                      placeholder="답변을 작성하실 수 있습니다. (200자 까지)"
                      title="내 답변 수정"
                      maxlength="200"
                      v-model="editingUserReview"
                    />
                    <div class="comment-write-bottom">
                      <div class="letter-count">
                        <strong>{{ editingUserReviewTextCounts }}</strong> / 200
                      </div>
                      <button
                        type="button"
                        class="mu-btn"
                        @click="onCancelEditUserReview"
                      >
                        <span>취소</span>
                      </button>
                      <button
                        type="button"
                        class="mu-btn mu-btn-primary"
                        @click="onSaveEditUserReview"
                        :disabled="isUserReviewEdited"
                      >
                        <span>등록</span>
                      </button>
                    </div>
                  </fieldset>
                </form>
              </div>
            </div>
            <!-- // 리뷰 수정 폼-->        
          </div>
        </div>
        <!-- // 리뷰 뷰 폼-->

      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useUserStore } from "@/store/user";
import ObjectUtils from "@/utils/ObjectUtils";
import { confirmAlert } from "@functional/alert/alert-default";

const userStore = useUserStore();
const { $ofetch } = useNuxtApp();
const emit = defineEmits(["reload"]);
const props = defineProps(["list", "params", "initUserReview"]);

const showData = ref(true);
const toggleShowDataText = ref("접기");

const toggleShowData = () => {
  showData.value = !showData.value;
  if (showData.value) {
    toggleShowDataText.value = "접기";
  } else {
    toggleShowDataText.value = "펼치기";
  }
};

// 랜더링 후 안보이도록 하기 위함
// onMounted(() => { showData.value = false})

const reviewList = ref([]); // 해당 엔티티에 대한 사용자 리뷰 목록
const userReview = ref(""); // 작성 중인 사용자 리뷰 텍스트
const editingReviewObj = ref({
  // 사용자 리뷰 수정 제어
  display: false, // 수정 폼 display 제어 flag
  review: null, // 사용자 리뷰 수정 대상 데이터
});
const editingUserReview = ref(""); // 수정 중인 사용자 리뷰 텍스트

const userReviewTextCounts = computed(() => userReview.value.length);
const editingUserReviewTextCounts = computed(
  () => editingUserReview.value.length
);
const isUserReviewEdited = computed(() => {
  let isEdited = false;

  const originalReview = ObjectUtils.getPropertyValue(
    editingReviewObj.value,
    "review/review",
    ""
  );
  if (originalReview !== editingUserReview.value) {
    isEdited = true;
  }

  return !isEdited;
});
// 사용자 리뷰 등록
const saveUserReview = () => {
  if (userReview.value) {
    const params = {
      ...props.params,
      review: userReview.value,
    };

    $ofetch(`/api/search/detail/review`, {
      method: "POST",
      body: params,
    }).then(() => {
      userReview.value = "";
      emit("reload");
    });
  }
};

// 사용자 리뷰 삭제 버튼 클릭 이벤트 핸들러
const onClickDeleteUserReview = async (review_id) => {
  const confirm = await confirmAlert("정말 삭제하시겠습니까?");
  if (confirm) {
    $ofetch(`/api/search/detail/review/${review_id}`, {
      method: "DELETE",
    }).then(() => emit("reload"));
  }
};

// 사용자 리뷰 수정 버튼 클릭 이벤트 핸들러
const onClickEditUserReview = (target) => {
  editingUserReview.value = target.review;

  editingReviewObj.value = {
    display: true,
    review: target,
  };
};

// 사용자 리뷰 수정 취소 버튼 클릭 이벤트 핸들러
const onCancelEditUserReview = () => {
  editingReviewObj.value = {
    display: false,
    review: null,
  };
  editingUserReview.value = "";
};

// 사용자 리뷰 수정 등록 버튼 클릭 이벤트 핸들러
const onSaveEditUserReview = (e) => {
  // e.preventDefault();

  if (editingUserReview.value) {
    const params = {
      review: editingUserReview.value,
      reviewID: editingReviewObj.value.review.review_id,
    };

    $ofetch(`/api/search/detail/review`, {
      method: "PUT",
      body: params,
    }).then(() => {
      editingReviewObj.value = {
        display: false,
        review: null,
      };
      editingUserReview.value = "";
      emit("reload");
    });
  }
};

watch(
  () => props.initUserReview,
  (newVal, oldVal) => {
    userReview.value = "";
  }
);
</script>
