<template>
  <div class="profile-box">
    <span class="profile-avatar">
      {{ profileFirstWord }}
    </span>
    <div class="profile-group">
      <div class="editable-group">
        <span class="profile-name">{{
          props.targetUserInfo?.displayName || "이름없음"
        }}</span>
        <button
          class="button button-neutral-ghost button-sm"
          type="button"
          @click="editDisplayName(true)"
          v-show="IsEditAgree"
        >
          <span class="hidden-text">수정</span>
          <svg-icon class="button-icon" name="pen"></svg-icon>
        </button>
      </div>
      <!-- TODO : [개발] 수정시 인풋 활성화-->
      <div class="editable-group" v-show="isOpenDisplayEdit">
        <label class="hidden-text" for="profile-name-modify"
          >프로필 이름 입력</label
        >
        <input
          id="profile-name-modify"
          class="text-input w-1/4"
          :placeholder="localDisplayName ? '' : '이름을 입력해주세요.'"
          v-model="localDisplayName"
        />
        <div class="h-group gap-2 shrink-0">
          <button
            class="button button-neutral-lighter button-sm"
            type="button"
            @click="editDisplayName(false)"
          >
            취소
          </button>
          <button
            class="button button-primary-lighter button-sm"
            type="button"
            @click="changeDisplayName"
          >
            완료
          </button>
        </div>
      </div>
      <div class="profile-list">
        <div class="profile-title">이메일</div>
        <div>{{ props.targetUserInfo.email }}</div>
      </div>
      <div class="profile-list">
        <div class="profile-title">역할</div>
        <div>
          <div class="editable-group">
            <span v-show="props.targetUserInfo.role === 'USER'">사용자</span>
            <span v-show="props.targetUserInfo.role === 'ADMIN'">관리자</span>
            <button
              class="button button-neutral-ghost button-sm"
              type="button"
              @click="editRole"
              v-show="
                props.userInfo.admin && props.targetUserInfo.name !== 'admin'
              "
            >
              <span class="hidden-text">수정</span>
              <svg-icon class="button-icon" name="pen"></svg-icon>
            </button>
          </div>
        </div>
      </div>
      <div class="profile-list" v-show="isOpenRoleEdit">
        <div>
          <div class="switch">
            <input
              type="checkbox"
              id="manager-sw"
              class="switch-input"
              :checked="props.targetUserInfo.admin"
              v-model="localAdmin"
              @click="changeRoleSwitch"
            />
            <label for="manager-sw" class="switch-label">
              관리자
              <div class="switch-control"></div>
              <button
                class="button button-primary-lighter button-sm"
                type="button"
                @click="changeRole"
              >
                완료
              </button>
            </label>
          </div>
        </div>
      </div>
      <div class="profile-list">
        <div class="profile-title">설명</div>
        <div class="editable-group">
          <span class="editable-group-desc">{{
            props.targetUserInfo?.description || "-"
          }}</span>
          <button
            class="button button-neutral-ghost button-sm"
            type="button"
            @click="editDescription(true)"
            v-show="IsEditAgree"
          >
            <span class="hidden-text">수정</span>
            <svg-icon class="button-icon" name="pen"></svg-icon>
          </button>
        </div>
      </div>
      <div class="editable-group w-2/4" v-show="isOpenDescriptionEdit">
        <label class="hidden-text" for="profile-description-modify"
          >계정 설명 수정</label
        >
        <textarea
          id="profile-description-modify"
          class="textarea"
          :placeholder="localDescription ? '' : '설명을 입력해주세요.'"
          v-model="localDescription"
        ></textarea>
        <div class="h-group gap-1">
          <button
            class="button button-neutral-stroke"
            type="button"
            @click="editDescription(false)"
          >
            취소
          </button>
          <button
            class="button button-primary-lighter"
            type="button"
            @click="changeDescription"
          >
            완료
          </button>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
const isOpenDisplayEdit = ref(false);
const isOpenDescriptionEdit = ref(false);
const isOpenRoleEdit = ref(false);

const props = defineProps({
  targetUserInfo: {
    type: Object,
  },
  userInfo: {
    type: Object,
  },
});

const emit = defineEmits<{
  (e: "changeDisplayName", data: object): void;
  (e: "changeRole", data: object): void;
  (e: "changeDescription", data: object): void;
}>();

const localAdmin = ref(props.targetUserInfo.admin);
const localDescription = ref(props.targetUserInfo.description);
const localDisplayName = ref(props.targetUserInfo.displayName);
const localName = ref(props.targetUserInfo.name);

const profileFirstWord = computed(() => {
  let word =
    props.targetUserInfo.displayName || props.targetUserInfo.name || "";
  return word.slice(0, 1).toUpperCase();
});

const editDisplayName = (value: boolean) => {
  if (!value) {
    localDisplayName.value = props.targetUserInfo.displayName;
  }
  isOpenDisplayEdit.value = value;
};

const editDescription = (value: boolean) => {
  if (!value) {
    localDescription.value = props.targetUserInfo.description;
  }
  isOpenDescriptionEdit.value = value;
};

const editRole = () => {
  isOpenRoleEdit.value = !isOpenRoleEdit.value;
};

const changeDisplayName = () => {
  let data = {
    value: localDisplayName.value.trim(),
    op: "add",
    path: "/displayName",
  };
  emit("changeDisplayName", data);
  isOpenDisplayEdit.value = false;
};

const changeDescription = () => {
  let data = {
    value: localDescription.value,
    op: "add",
    path: "/description",
  };
  emit("changeDescription", data);
  isOpenDescriptionEdit.value = false;
};

const changeRoleSwitch = () => {
  localAdmin.value = !localAdmin.value;
};

const changeRole = () => {
  let data = {
    value: localAdmin.value,
    op: "replace",
    path: "/isAdmin",
  };
  emit("changeRole", data);
};

const IsEditAgree = computed(() => {
  const isCurrentUserAdmin = props.userInfo.admin;
  const isCurrentUserSuperAdmin =
    isCurrentUserAdmin && props.userInfo.name === "admin";
  const isTargetUserAdmin = props.targetUserInfo.admin;
  const isTargetUserSuperAdmin =
    isTargetUserAdmin && props.targetUserInfo.name === "admin";

  // NOTE: 최고관리자(U)가 유저(T) -> true
  if (isCurrentUserSuperAdmin && !isTargetUserAdmin) {
    return true;
  }

  // NOTE: 최고관리자(U)가 관리자(T) -> true
  if (isCurrentUserSuperAdmin && isTargetUserAdmin && !isTargetUserSuperAdmin) {
    return true;
  }

  // NOTE: 최고관리자(U)가 최고관리자(T) -> true
  if (isCurrentUserSuperAdmin && isTargetUserSuperAdmin) {
    return true;
  }

  // NOTE: 관리자(U)가 유저(T) -> true
  if (isCurrentUserAdmin && !isCurrentUserSuperAdmin && !isTargetUserAdmin) {
    return true;
  }

  // NOTE: 관리자(U)가 관리자(T) -> true
  if (
    isCurrentUserAdmin &&
    !isCurrentUserSuperAdmin &&
    isTargetUserAdmin &&
    !isTargetUserSuperAdmin
  ) {
    return true;
  }

  // NOTE: 관리자(U)가 최고관리자(T) -> false
  if (
    isCurrentUserAdmin &&
    !isCurrentUserSuperAdmin &&
    isTargetUserSuperAdmin
  ) {
    return false;
  }

  // NOTE: 유저(U)가 최고관리자(T) -> false
  if (!isCurrentUserAdmin && isTargetUserSuperAdmin) {
    return false;
  }

  // NOTE: 유저(U)가 관리자(T) -> false
  if (!isCurrentUserAdmin && isTargetUserAdmin && !isTargetUserSuperAdmin) {
    return false;
  }

  // NOTE: 유저(U)가 유저(본인) -> true
  if (
    !isCurrentUserAdmin &&
    !isTargetUserAdmin &&
    props.userInfo.name === props.targetUserInfo.name
  ) {
    return true;
  }

  // NOTE: 유저(U)가 유저(타인) -> false
  if (
    !isCurrentUserAdmin &&
    !isTargetUserAdmin &&
    props.userInfo.name !== props.targetUserInfo.name
  ) {
    return false;
  }

  // NOTE: 기본값으로 false 반환
  return false;
});
</script>
<style lang="scss" scoped></style>
