<template>
  <div class="profile-box">
    <span class="profile-avatar">
      <img class="profile-img" alt="프로필 이미지" />
    </span>
    <div class="profile-group">
      <div class="editable-group">
        <span class="profile-name">{{ props.targetUserInfo.displayName }}</span>
        <button
          class="button button-neutral-ghost button-sm"
          type="button"
          @click="editDisplayName(true)"
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
              v-show="props.userInfo.admin"
            >
              <span class="hidden-text">수정</span>
              <svg-icon class="button-icon" name="pen"></svg-icon>
            </button>
          </div>
        </div>
      </div>
      <div class="profile-list">
        <div>
          <div class="switch" v-show="isOpenRoleEdit">
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
      <div class="editable-group">
        <span class="editable-group-desc">{{
          props.targetUserInfo.description
        }}</span>
        <button
          class="button button-neutral-ghost button-sm"
          type="button"
          @click="editDescription(true)"
        >
          <span class="hidden-text">수정</span>
          <svg-icon class="button-icon" name="pen"></svg-icon>
        </button>
      </div>
      <div class="editable-group w-2/4" v-show="isOpenDescriptionEdit">
        <label class="hidden-text" for="profile-description-modify"
          >계정 설명 수정</label
        >
        <textarea
          id="profile-description-modify"
          class="textarea"
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
    value: localDisplayName.value,
    op: "add",
    path: "/displayName",
  };
  emit("changeDisplayName", data);
};

const changeDescription = () => {
  let data = {
    value: localDescription.value,
    op: "add",
    path: "/description",
  };
  emit("changeDescription", data);
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
</script>
<style lang="scss" scoped></style>
