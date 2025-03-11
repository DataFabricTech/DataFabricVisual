<template>
  <div class="mu-modal" v-if="props.visibleMenuManageModal">
    <div class="mu-dialog-background"></div>

    <div :class="['mu-dialog', 'w600']">
      <div class="mu-dialog-head">
        <p class="title">메뉴 정보</p>
      </div>
      <div class="mu-dialog-body">
        <table class="mu-formbox">
          <caption>메뉴 추가/수정</caption>
          <colgroup>
            <col style="width: 150px" />
            <col />
          </colgroup>

          <tbody>
            <tr>
              <th scope="row">메뉴ID<strong class="required">필수</strong></th>
              <td>
                <input type="text" placeholder="메뉴ID를 입력하세요." class="mu-input" title="메뉴ID 입력"
                  v-bind:class="[!reg_menuId_valid ? 'error' : '', 'mu-input']"
                  v-model="props.modalParam.userObject.menu_id" :readonly="props.modalParam.updatefalg"
                  oninput="this.value = this.value.replace(/[^a-zA-Z0-9]/g, '')" required />
                <p v-if="!reg_menuId_valid" class="complete-msg error">메뉴ID를 입력해주세요.</p>
              </td>
            </tr>
            <tr>
              <th scope="row">메뉴명<strong class="required">필수</strong></th>
              <td>
                <input type="text" placeholder="메뉴명을 입력하세요." class="mu-input" title="메뉴명 입력"
                  v-bind:class="[!reg_menuNm_valid ? 'error' : '', 'mu-input']"
                  v-model="props.modalParam.userObject.menu_name" required />
                <p v-if="!reg_menuNm_valid" class="complete-msg error">메뉴명을 입력해주세요.</p>
              </td>
            </tr>
            <tr>
              <th scope="row">메뉴 경로<strong class="required">필수</strong></th>
              <td>
                <input type="text" placeholder="메뉴경로를 입력하세요." class="mu-input" title="메뉴경로 입력"
                  v-bind:class="[!reg_menuPath_valid ? 'error' : '', 'mu-input']"
                  v-model="props.modalParam.userObject.menu_path" required />
                <p v-if="!reg_menuPath_valid" class="complete-msg error">메뉴경로를 입력해주세요.</p>
              </td>
            </tr>
            <tr>
              <th scope="row">상위 메뉴</th>
              <td>
                <input type="text" placeholder="상위메뉴를 입력하세요." class="mu-input" title="상위 메뉴 입력"
                  v-model="props.modalParam.userObject.parent_menu_id"
                  :readonly="props.modalParam.updatefalg || props.modalParam.userObject.parent_menu_id != ''"
                  required />
              </td>
            </tr>
            <tr>
              <th scope="row">메뉴 Depth<strong class="required">필수</strong></th>
              <td>
                <input type="text" placeholder="메뉴 Depth를 입력하세요." class="mu-input" title="메뉴 Depth입력"
                  v-model="props.modalParam.userObject.menu_depth" :readonly="true" required />
                <p v-if="!reg_menuDepth_valid" class="complete-msg error">메뉴 Depth를 입력해주세요.</p>
              </td>
            </tr>
            <tr>
              <th scope="row">정렬 순서<strong class="required">필수</strong></th>
              <td>
                <input type="text" placeholder="정렬 순서를 입력하세요." class="mu-input" title="메뉴 정렬 순서 입력"
                  v-model="props.modalParam.userObject.order_num"
                  v-bind:class="[!reg_orderNum_valid ? 'error' : '', 'mu-input']"
                  oninput="this.value = this.value.replace(/[^0-9]/g, '')" name="description" />
                <p v-if="!reg_orderNum_valid" class="complete-msg error">정렬 순서를 입력해주세요.</p>
              </td>
            </tr>
            <tr>
              <th scope="row">노출 여부<strong class="required">필수</strong></th>
              <td>
                <div class="mu-selectbox w160">
                  <select v-model="props.modalParam.userObject.menu_visible">
                    <option value="">선택</option>
                    <option value="Y">노출</option>
                    <option value="N">미노출</option>
                  </select>
                </div>
                <p v-if="!reg_menuVisible_valid" class="complete-msg error">노출 여부를 선택해주세요.</p>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
      <div class="mu-dialog-foot">
        <button type="button" class="mu-btn" @click="onCancel"><span>취소</span></button>
        <button type="button" class="mu-btn mu-btn-primary" @click="onRegister"><span>확인</span></button>
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
import { confirmAlert, successAlert } from "@functional/alert/alert-default";
import { useNuxtApp } from "nuxt/app";
import { computed, ref } from "vue";

const props = defineProps(['visibleMenuManageModal', 'modalParam'])
const { $ofetch } = useNuxtApp();
const emit = defineEmits(["onCloseModal", "onSubmit", "onDeptDataLoad"]);


const reg_menuId_valid: ComputedRef<boolean> = computed(() => {
  return props.modalParam.userObject.menu_id.length > 0 ? true : false;
});
const reg_menuNm_valid: ComputedRef<boolean> = computed(() => {
  return props.modalParam.userObject.menu_name.length > 0 ? true : false;
});
const reg_menuPath_valid: ComputedRef<boolean> = computed(() => {
  return props.modalParam.userObject.menu_path.length > 0 ? true : false;
});
const reg_menuDepth_valid: ComputedRef<boolean> = computed(() => {
  return String(props.modalParam.userObject.menu_depth).length > 0 ? true : false;
});
const reg_orderNum_valid: ComputedRef<boolean> = computed(() => {
  return String(props.modalParam.userObject.order_num).length > 0 ? true : false;
});
const reg_menuVisible_valid: ComputedRef<boolean> = computed(() => {
  return props.modalParam.userObject.menu_visible.length > 0 ? true : false;
});

//
const onRegister = async () => {
  if (!reg_menuId_valid.value
    || !reg_menuNm_valid.value
    || !reg_menuPath_valid.value
    || !reg_orderNum_valid.value
    || !reg_menuVisible_valid.value) return;

  const result = await confirmAlert("저장 하시겠습니까?");
  if (!result) return;

  let params = {
    menu_id: props.modalParam.userObject.menu_id,
    menu_name: props.modalParam.userObject.menu_name,
    menu_path: props.modalParam.userObject.menu_path,
    parent_menu_id: props.modalParam.userObject.parent_menu_id,
    order_num: props.modalParam.userObject.order_num,
    menu_visible: props.modalParam.userObject.menu_visible,
    menu_depth: props.modalParam.userObject.menu_depth
  }

  $ofetch('/api/admin/system/role/menuRegister', {
    method: 'POST',
    body: params
  })
    .then(async (res) => {
      if (res && !res.errorMessage) {
        await successAlert('저장 되었습니다.');
        onClose();
      }
    })
}

// 취소 버튼 클릭 이벤트 핸들러
const onCancel = () => {
  onClear();
  emit("onCloseModal");
};

// 닫기 이벤트 핸들러
const onClose = () => {
  onClear();
  emit("onCloseModal");
};

//
const onClear = () => {
};

//
defineExpose({
  onClose
})
</script>
