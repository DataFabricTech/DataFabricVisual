<template>
  <div class="mu-modal" v-if="props.visibleDeptManageModal">
  <div class="mu-dialog-background"></div>

  <div :class="['mu-dialog', 'w600']">
    <div class="mu-dialog-head">
      <p class="title">권한 생성</p>
    </div>
    <div class="mu-dialog-body">
      <table class="mu-formbox">
        <caption>
          권한 생성
        </caption>
        <colgroup>
          <col style="width: 150px" />
          <col />
        </colgroup>

        <tbody>
          <tr>
            <th scope="row">권한 ID<strong class="required">필수</strong></th>
            <td>
              <input
                type="text"
                placeholder="권한ID는 20자 이내로 입력 가능합니다."
                class="mu-input w220"
                title="권한ID 입력"
                id="role_id"
                name="role_id"
                maxlength="20"
                v-model="props.modalParam.role_id"
                v-bind:class="[!role_id_valid ? 'error' : '', 'mu-input']"
                :disabled="props.updateFlag"
              />
              <button type="button" class="mu-btn" @click="checkDupRoleId" style="margin-left:10px" :disabled="props.updateFlag">
                <span>중복확인</span>
              </button>
              <p v-if="!role_id_valid" class="complete-msg error">권한ID를 입력해 주세요.</p>
            </td>
          </tr>
          <tr>
            <th scope="row">권한 타입<strong class="required">필수</strong></th>
            <td style="display: block;">
              <div class="mu-radio">
                  <input
                      type="radio"
                      id="menu_role_type"
                      name="role_type"
                      value="0"
                      v-model="props.modalParam.role_type"
                      :checked="props.modalParam.role_type == 0"
                      v-bind:class="[!role_type_valid ? 'error' : '', 'mu-input']"
                      />
                  <label for="menu_role_type" >
                      <span class="team">메뉴 권한</span>
                  </label>
              </div>
              <div class="mu-radio">
                  <input
                      type="radio"
                      id="data_role_type"
                      name="role_type"
                      value="1"
                      v-model="props.modalParam.role_type"
                      :checked="props.modalParam.role_type == 1"
                      v-bind:class="[!role_type_valid ? 'error' : '', 'mu-input']"
                  />
                  <label for="data_role_type" >
                      <span class="team">데이터 권한</span>
                  </label>
              </div><br>
              <p v-if="!role_type_valid" class="complete-msg error" style="white-space: pre-line;">데이터 권한을 선택해 주세요.</p>
            </td>
          </tr>
          <tr>
            <th scope="row">권한명<strong class="required">필수</strong></th>
            <td>
                <input
                  type="text"
                  placeholder="권한명은 20자 이내로 입력 가능합니다."
                  class="mu-input"
                  title="권한명 입력"
                  name="role_name"
                  id="role_name"
                  maxlength="20"
                  v-model="props.modalParam.role_name"
                  required 
                  autocomplete="off"
                  v-bind:class="[!role_name_valid ? 'error' : '', 'mu-input']"
                />
                <p v-if="!role_name_valid" class="complete-msg error" style="white-space: pre-line;">권한명을 입력해 주세요.</p>
            </td>
          </tr>
          <tr>
            <th scope="row">권한 설명</th>
            <td>
                <textarea
                  name="role_description"
                  id="role_description"
                  cols="30"
                  rows="3"
                  class="mu-area h50"
                  maxlength="50"
                  placeholder="내용을 입력하세요"
                  v-model="props.modalParam.role_description"
                >데이타 관리 계정</textarea>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <div class="mu-dialog-foot">
      <button type="button" class="mu-btn" @click="onCancel"><span>취소</span></button>
      <button type="button" class="mu-btn" @click="onRegister"><span>등록</span></button>
    </div>
    <div class="mu-dialog-close-btn">
      <button type="button" class="mu-btn mu-btn-close" title="해당 팝업 닫기" @click="onClose">
        <i class="mu-icon-svg close"></i><span>팝업닫기</span>
      </button>
    </div>
  </div>
</div>
</template>

<script setup lang="ts">
import { successAlert, confirmAlert, errorAlert, defaultAlert } from "@functional/alert/alert-default";
import { useNuxtApp } from "nuxt/app";
import { computed, ref } from "vue";
const props = defineProps(['visibleDeptManageModal', 'updateFlag', 'modalParam'])
const { $ofetch } = useNuxtApp();
const emit = defineEmits(["onCloseModal", "onSubmit"]);

const checkedDupRoleId = ref(false);


// 
const onCancel = () => {
  emit("onCloseModal");
};

// 
const onClose = () => {
  onClear();
  emit("onCloseModal");
};

const onClear = () => {
  checkedDupRoleId.value = false;
};

const role_id_dup_valid: ComputedRef<boolean> = computed(() => {
  if (!props.modalParam.role_id) {
    return false;
  } else {
    return props.modalParam.role_id.length > 0 ? true : false;
  }
});

const role_id_valid: ComputedRef<boolean> = computed(() => {
  if (!props.modalParam.role_id) {
    return false;
  } else {
    return props.modalParam.role_id.length > 0 ? true : false;
  }
});

const role_type_valid: ComputedRef<boolean> = computed(() => {
  return props.modalParam.role_type !== undefined;
});

const role_name_valid: ComputedRef<boolean> = computed(() => {
  if (!props.modalParam.role_name) {
    return false;
  } else {
    return props.modalParam.role_name.length > 0 ? true : false;
  }
});


const onRegister = async () => {
  let params = {
    role_id: props.modalParam.role_id,
    role_type: props.modalParam.role_type,
    role_name: props.modalParam.role_name,
    role_description: props.modalParam.role_description,
    etc: props.modalParam.etc,
    updateFlag: props.updateFlag ? "Y" : "N",
  };

  if ( !role_id_valid.value
      || !role_type_valid.value
      || !role_name_valid.value) {
    return;
  }

  if (!checkedDupRoleId.value && !props.updateFlag) {
    await errorAlert("권한 중복확인 하세요.")
    return;
  }
  
  if (await confirmAlert("저장 하시겠습니까?")) {
    await $ofetch("/api/admin/system/role/register", {
      method: "POST",
      body: params
    }).then(async (res) => {
      if (res) {
        if (res.data.errorMessage && res.data.errorMessage != "") {
          await errorAlert(res.data.errorMessage);
        } else {
          await successAlert("등록 되었습니다.");
          onClose();
        }
      }
    })
  }
}

const checkDupRoleId = async () => {
  if (props.modalParam.role_id && props.modalParam.role_id != "") {
    await $ofetch(`/api/admin/system/role/checkDupRoleId?role_id=${props.modalParam.role_id}`).then(async (res) => {
      if (res && res.data) {
          if (res.data.register_result == 'Y') {
            await defaultAlert("사용 가능한 권한입니다.")
            checkedDupRoleId.value = true
          } else {
            checkedDupRoleId.value = false
            errorAlert("이미 사용중인 권한입니다.")
          }
        }
    })
  }
}

defineExpose({
  onClose
})
</script>
  