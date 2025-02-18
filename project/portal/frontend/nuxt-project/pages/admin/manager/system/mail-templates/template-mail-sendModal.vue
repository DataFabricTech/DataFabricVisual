<template>
  <div class="mu-modal" v-if="props.visibleMailTemplateManageModal">
  <div class="mu-dialog-background"></div>

  <div :class="['mu-dialog', 'w600']">
    <div class="mu-dialog-head">
      <p class="title">메일 전송 정보</p>
    </div>
    <div class="mu-dialog-body">
      <table class="mu-formbox">
        <caption>메일 전송</caption>
        <colgroup>
          <col style="width: 150px" />
          <col />
        </colgroup>

        <tbody>
          <tr>
            <th scope="row">템플릿 ID<strong class="required">필수</strong></th>
            <td>
              <input
                type="text"
                placeholder="템플릿 ID를 입력하세요."
                class="mu-input"
                title="템플릿 ID 입력"
                v-model="props.modalParam.template_id"
                readonly
                required
              />
            </td>
          </tr>
          <tr>
            <th scope="row">수신자<strong class="required">필수</strong></th>
            <td>
              <div id="receiver-tags" class="tags-group">
                <div v-if="props.modalParam.receiver && props.modalParam.receiver.length > 0 && props.modalParam.receiver.some(receiver => receiver.name && receiver.email)" 
                      v-for="receiver in props.modalParam.receiver" :key="receiver" class="tag tag-sm">
                    <span class="mail-label">{{ receiver.name }} {{ receiver.email }}</span>
                    <button type="button" class="mu-btn mu-btn-delete" @click="onRemoveReceiver(receiver)">
                        <i class="mu-icon-svg item-delete"></i><span>삭제</span>
                    </button>
                </div>
                <button type="button" title="사원 찾아보기" class="mu-btn mu-btn-sm mu-btn-primary mu-btn-icon-only" @click="onToggleDeptModal('receiver')">
                    <i class="mu-icon-svg add"></i><span>찾아보기</span>
                </button>
              </div>
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

  <select-dept-user-modal
      :toggle-dept-modal="toggleDeptModal"
      @onSelect="onSelectDept"
      @onCloseModal="onToggleDeptModal"
    />
</div>
</template>

<script setup lang="ts">
import { confirmAlert, errorAlert, successAlert } from "@functional/alert/alert-default";
import { useNuxtApp } from "nuxt/app";
import { computed, ref } from "vue";
import selectDeptUserModal from "@functional/dept/select-department-user-modal";


const props = defineProps(['visibleMailTemplateManageModal', 'modalParam'])
const { $ofetch } = useNuxtApp();
const emit = defineEmits(["onCloseModal", "onSubmit"]);

const toggleDeptModal = ref(false);
const select_user_type = ref('receiver');


// 
const onRegister = async() => {
  const result = await confirmAlert("전송 하시겠습니까?");
  if(!result) return;

  let params = {
    template_id: props.modalParam.template_id,
    variables : {
      add_recipients : props.modalParam.receiver,
      '_mailTitle_' : "@@@@@@@@@@@@@@@@@@@@@@@@@@메일 제목",
      '_initPassword_' : "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@test123",
    }
  }

  $ofetch('/api/admin/system/mailTemplate/sendMailWithTemplate', {
    method: 'POST',
    body: params
  })
  .then(async (res) => {
    if(res && !res.errorMessage) {
      await successAlert('전송 되었습니다.');
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
const onToggleDeptModal = (type: string) => {
  if (toggleDeptModal.value) {
    toggleDeptModal.value = false;
  } else {
    toggleDeptModal.value = true;
  }
};

// 
const onSelectDept = (info) => {
  if(info.length > 0){
    info.forEach((item) => {
        if (!props.modalParam.receiver.some(existingItem => existingItem.email === item.email)) {
          props.modalParam.receiver.push(item);
        }
    })
  }
};

// 
const onRemoveReceiver = (receiver) => {
  props.modalParam.receiver.splice(props.modalParam.receiver.indexOf(receiver), 1);
}

// 
defineExpose({
  onClose
})
</script>
