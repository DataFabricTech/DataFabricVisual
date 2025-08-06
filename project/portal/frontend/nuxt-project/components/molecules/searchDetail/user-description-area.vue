<template>
  <div :class="props.isAdmin ? 'data-box-wrap' : ''"
    :style="props.isAdmin ? 'padding-top: 5px; padding-bottom: 35px' : ''">
    <div class="detail-section" :class="showData ? 'open' : 'close'">
      <div class="mu-heading">
        <h4>사용자 데이터 설명</h4>
        <div class="mu-item-group">
          <button type="button" class="mu-btn mu-btn-bg-non btn-section-toggle" title="접기" @click="toggleShowData()"
            style="margin-left: 10px">
            <span>{{ toggleShowDataText }}</span>
          </button>
        </div>
      </div>
      <div class="detail-box" v-show="showData">
        <div class="comment-wrap">
          <div class="comment-write">
            <div class="comment-write-box">
              <div class="comment-form">
                <textarea class="mu-area" :placeholder="`데이터 설명을 작성해주세요. 엔터키는 지원하지 않습니다. (최대${userDescriptionTextMaxCount}자)`" title="데이터 설명 입력"
                  :maxlength="userDescriptionTextMaxCount" v-model="userDescription" @keydown="preventNewline" />
                <button type="button" class="mu-btn mu-btn-primary" @click="saveUserDescription">
                  <span>등록</span>
                </button>
              </div>
              <div class="comment-write-bottom">
                <div class="letter-count">
                  <strong>{{ userDescriptionTextCounts }}</strong> / {{ userDescriptionTextMaxCount }}자
                </div>
              </div>
            </div>
          </div>
          <div class="mu-heading">
            <div class="mu-item-group">
              <div class="btn-sort">
                <div class="mu-radio" v-for="(order, idx) in orderTypeList">
                  <input type="radio" :id="order" name="listSorting" @click="onSelectSort(order)"
                    :checked="userDescriptionOrderType == orderType" />
                  <label :for="order"><span>{{ getKrName(order) }}</span></label>
                </div>
              </div>
            </div>
          </div>
          <!-- 사용자 추천 설명 뷰 폼-->
          <div v-for="(item, index) in props.list" :key="index" class="comment-view">
            <div class="comment-cont">
              <div class="comment-info">
                <span class="name">{{ item.user_name }}</span>
                <span class="team">({{ item.dept_name ? item.dept_name : "부서없음" }})</span>
                <span class="date">{{ item.create_date }}</span>
                <favorite-button :favoriteId="item.id" :params="props.params" @reload="getAllDataByGuid" />
              </div>
              <div class="comment-message">
                {{ item.user_description }}
              </div>
              <!-- 사용자 추천 설명 수정 버튼 : 사용자페이지는 내 데이터 설명에서만 수정/삭제, 관리자는 삭제만 가능 -->
              <!--                        관리자페이지는 내 데이터 설명에서만 수정/삭제, 관리자는 삭제만 가능 -->
              <div class="modify-btn"
                v-if="props.isAdmin || userStore.getUser.user_id === item.user_id || props.isUserDescriptionMenuAuth">
                <button v-if="userStore.getUser.user_id === item.user_id" type="button"
                  class="mu-btn mu-btn-sm mu-btn-bg-non" title="해당 데이터 설명 수정" @click="onClickEditUserDescription(item)">
                  <span>수정</span><i class="mu-icon-svg edit"></i>
                </button>
                <button type="button" class="mu-btn mu-btn-sm mu-btn-bg-non" title="해당 데이터 설명 삭제"
                  @click="onClickDeleteUserDescription(item.id)">
                  <span>삭제</span><i class="mu-icon-svg del"></i>
                </button>
              </div>
              <!-- 사용자 추천 설명 수정 폼-->
              <div v-if="
                editingUserDescriptionObj.display &&
                editingUserDescriptionObj.userDescription.id === item.id
              " class="comment-write">
                <div class="comment-write-box">
                  <form action="">
                    <fieldset>
                      <legend>데이터 설명 입력</legend>
                      <textarea class="mu-area" :placeholder="`데이터 설명을 작성하실 수 있습니다. (${userDescriptionTextMaxCount}자 까지)`" title="데이터 설명 수정"
                        :maxlength="userDescriptionTextMaxCount" v-model="editingUserDescription" />
                      <div class="comment-write-bottom">
                        <div class="letter-count">
                          <strong>{{ editingUserDescriptionTextCounts() }}</strong> / {{ userDescriptionTextMaxCount }}
                        </div>
                        <button type="button" class="mu-btn" @click="onCancelEditUserDescription">
                          <span>취소</span>
                        </button>
                        <button type="button" class="mu-btn mu-btn-secondary" @click="onSaveEditUserDescription">
                          <span>완료</span>
                        </button>
                      </div>
                    </fieldset>
                  </form>
                </div>
              </div>
              <!-- // 사용자 추천 설명 수정 폼-->
            </div>
          </div>
          <!-- // 사용자 추천 설명 뷰 폼-->
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import ObjectUtils from "@/utils/ObjectUtils";
import FavoriteButton from "@atoms/searchDetail/favorite-button";
import { confirmAlert, errorAlert } from "@functional/alert/alert-default";
import { watch } from "vue";
import { useUserStore } from "~/store/user";

const { $ofetch } = useNuxtApp();
const userStore = useUserStore();
const emit = defineEmits(["reload", "reloadUserDescriptionList"]);
const props = defineProps(["list", "params", "initUserDescription", "isAdmin", "isUserDescriptionMenuAuth"]);

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

const orderTypeList = ref(['favorite', 'last', 'old'])
const orderType = ref("sortRecommend")

const userDescriptionOrderType = ref("favorite"); // 정렬 방식
const userDescription = ref(""); // 작성 중인 사용자 추천 설명 텍스트

const editingUserDescriptionObj = ref({
  display: false, // 수정 폼 display 제어 flag
  userDescription: null, // 사용자 추천 설명 수정 대상 데이터
}); // 사용자 추천 설명 수정 제어
const editingUserDescription = ref("favorite"); // 수정 중인 사용자 추천 설명 텍스트

// 사용자 추천 설명 최대 글자수
const userDescriptionTextMaxCount = ref(100)
// 사용자 추천 설명 글자수 계산
const userDescriptionTextCounts = computed(() => userDescription.value.length);

// 수정 중인 사용자 추천 설명 글자수 계산
const editingUserDescriptionTextCounts = () => {
  if (editingUserDescription.value) {
    return editingUserDescription.value.length;
  } else {
    return 0
  }
};

// 사용자 추천 설명 수정 여부 계산
const isUserDescriptionEdited = () => {
  let isEdited = false;
  const originalUserDescription = ObjectUtils.getPropertyValue(
    editingUserDescriptionObj.value,
    "user_description",
    ""
  );
  if (originalUserDescription !== editingUserDescription.value) {
    isEdited = true;
  }

  return !isEdited;
};

const getKrName = (order) => {
  if (order == 'favorite') return '추천순'
  if (order == 'last') return '최신순'
  if (order == 'old') return '오래된순'
  return order
}

// 정렬 라디오 버튼 클릭 이벤트
const onSelectSort = async (order) => {
  userDescriptionOrderType.value = order
  emit("reloadUserDescriptionList", userDescriptionOrderType.value); // 사용자 추천 설명 목록 갱신
}

// 상세 조회 페이지 데이터 갱신
const getAllDataByGuid = (guid, favoriteEvent) => {
  emit("reload", guid, favoriteEvent);
};

// 사용자 추천 설명 조회순서 변경 이벤트
const changeUserDescriptionOrderType = () => {
  emit("reloadUserDescriptionList", userDescriptionOrderType.value); // 사용자 추천 설명 목록 갱신
};

// 사용자 추천 설명 등록
const saveUserDescription = () => {
  if (userDescription.value) {
    // 데이터 입력 확인
    const params = {
      ...props.params,
      user_description: userDescription.value,
    };

    $ofetch(`/api/search/detail/userDescriptionInsert`, {
      method: "POST",
      body: params,
    }).then((res) => {
      userDescription.value = "";
      emit("reload");
    });
  }
};

// 사용자 추천 설명 삭제 버튼 클릭 이벤트 핸들러
const onClickDeleteUserDescription = async (id) => {
  const confirm = await confirmAlert("정말 삭제하시겠습니까?");
  if (confirm) {
    const params = {
      ...props.params,
      id,
    };
    $ofetch(`/api/search/detail/userDescriptionDelete`, {
      method: "POST",
      body: params,
    }).then((res) => {
      emit("reload");
    });
  }
};

// 사용자 추천 설명 수정 버튼 클릭 이벤트 핸들러
const onClickEditUserDescription = (userDescription) => {
  editingUserDescription.value = userDescription.user_description;

  editingUserDescriptionObj.value = {
    display: true,
    userDescription: userDescription,
  };
};

// 사용자 추천 설명 수정 취소 버튼 클릭 이벤트 핸들러
const onCancelEditUserDescription = () => {
  editingUserDescriptionObj.value = {
    display: false,
    user_description: null,
  };
  editingUserDescription.value = "";
};

// 사용자 추천 설명 수정 등록 버튼 클릭 이벤트 핸들러
const onSaveEditUserDescription = (e) => {
  e.preventDefault();

  if (editingUserDescription.value) {
    const params = {
      ...props.params,
      user_description: editingUserDescription.value,
      id: editingUserDescriptionObj.value.userDescription.id,
    };

    $ofetch(`/api/search/detail/userDescriptionUpdate`, {
      method: "PUT",
      body: params,
    }).then((res) => {
      editingUserDescriptionObj.value = {
        display: false,
        userDescription: null,
      };
      editingUserDescription.value = "";
      emit("reload");
    });
  } else {
    errorAlert("데이터 설명을 입력해주세요.");
  }
};

const preventNewline = (event) => {
  if (event.key === 'Enter') {
    event.preventDefault(); // Enter 키 입력 차단
  }
};

watch(
  () => props.initUserDescription,
  (newVal, oldVal) => {
    userDescription.value = "";
  }
);
</script>
