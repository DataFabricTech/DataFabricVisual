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
                <input type="text" placeholder="템플릿 ID를 입력하세요." class="mu-input" title="템플릿 ID 입력"
                  v-model="props.modalParam.template_id" readonly required />
              </td>
            </tr>
            <tr>
              <th scope="row">발신자</th>
              <td>
                <div id="sender-tags" class="tags-group">
                  <div
                    v-if="props.modalParam.sender && props.modalParam.sender.length > 0 && props.modalParam.sender.some(sender => sender.name && sender.email)"
                    v-for="sender in props.modalParam.sender" :key="sender" class="tag tag-sm">
                    <span class="mail-label">{{ sender.name }} {{ sender.email }}</span>
                    <button type="button" class="mu-btn mu-btn-delete" @click="onRemoveSender(sender)">
                      <i class="mu-icon-svg item-delete"></i><span>삭제</span>
                    </button>
                  </div>
                  <button type="button" title="사원 찾아보기" class="mu-btn mu-btn-sm mu-btn-primary mu-btn-icon-only"
                    @click="onToggleDeptModal('sender')">
                    <i class="mu-icon-svg add"></i><span>찾아보기</span>
                  </button>
                </div>
              </td>
            </tr>
            <tr>
              <th scope="row">수신자</th>
              <td>
                <div id="receiver-tags" class="tags-group">
                  <div
                    v-if="props.modalParam.receiver && props.modalParam.receiver.length > 0 && props.modalParam.receiver.some(receiver => receiver.name && receiver.email)"
                    v-for="receiver in props.modalParam.receiver" :key="receiver" class="tag tag-sm">
                    <span class="mail-label">{{ receiver.name }} {{ receiver.email }}</span>
                    <button type="button" class="mu-btn mu-btn-delete" @click="onRemoveReceiver(receiver)">
                      <i class="mu-icon-svg item-delete"></i><span>삭제</span>
                    </button>
                  </div>
                  <button type="button" title="사원 찾아보기" class="mu-btn mu-btn-sm mu-btn-primary mu-btn-icon-only"
                    @click="onToggleDeptModal('receiver')">
                    <i class="mu-icon-svg add"></i><span>찾아보기</span>
                  </button>
                </div>
              </td>
            </tr>
            <tr>
              <th scope="row">참조자</th>
              <td>
                <div id="cc_recipients-tags" class="tags-group">
                  <div
                    v-if="props.modalParam.cc_recipients && props.modalParam.cc_recipients.length > 0 && props.modalParam.cc_recipients.some(cc_recipients => cc_recipients.name && cc_recipients.email)"
                    v-for="cc_recipients in props.modalParam.cc_recipients" :key="cc_recipients" class="tag tag-sm">
                    <span class="mail-label">{{ cc_recipients.name }} {{ cc_recipients.email }}</span>
                    <button type="button" class="mu-btn mu-btn-delete" @click="onRemoveCcReceiver(cc_recipients)">
                      <i class="mu-icon-svg item-delete"></i><span>삭제</span>
                    </button>
                  </div>
                  <button type="button" title="사원 찾아보기" class="mu-btn mu-btn-sm mu-btn-primary mu-btn-icon-only"
                    @click="onToggleDeptModal('cc_recipients')">
                    <i class="mu-icon-svg add"></i><span>찾아보기</span>
                  </button>
                </div>
              </td>
            </tr>
            <tr>
              <th scope="row">변수</th>
              <td>
                <div id="variables-tags" class="tags-group">
                  <input type="text" placeholder="변수명" v-model="variable._name" class="mu-input w140" title="변수명 입력" />
                  <input type="text" placeholder="변수값" v-model="variable._value" class="mu-input w140" title="변수값 입력" />
                  <button type="button" title="변수 입력" class="mu-btn mu-btn-sm mu-btn-primary mu-btn-icon-only"
                    @click="onAddVariable()">
                    <i class="mu-icon-svg add"></i><span>찾아보기</span>
                  </button>
                  <div v-if="props.modalParam.variables && props.modalParam.variables.length > 0"
                    v-for="variable in props.modalParam.variables" :key="variable" class="tag tag-sm">
                    <span class="mail-label">{{ variable._name }} : {{ variable._value }}</span>
                    <button type="button" class="mu-btn mu-btn-delete" @click="onRemoveVariable(variable)">
                      <i class="mu-icon-svg item-delete"></i><span>삭제</span>
                    </button>
                  </div>
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
      :popupTitle="getPopupTitle(select_user_type)"
      :toggle-dept-modal="toggleDeptModal"
      @onSelect="onSelectDept"
      @onCloseModal="onToggleDeptModal" />
  </div>
</template>

<script setup lang="ts">
import { confirmAlert, successAlert } from "@functional/alert/alert-default";
import { useNuxtApp } from "nuxt/app";
import { ref } from "vue";
import selectDeptUserModal from "@functional/dept/select-department-user-modal";


const props = defineProps(['visibleMailTemplateManageModal', 'modalParam'])
const { $ofetch } = useNuxtApp();
const emit = defineEmits(["onCloseModal", "onSubmit"]);

const toggleDeptModal = ref(false);
const select_user_type = ref('receiver');

const variable = ref({
  _name: "",
  _value: "",
})

const getPopupTitle = (type) => {
  if (type == 'sender') return "보내는 사람 정보"
  else if (type == 'receiver') return "받는 사람 정보"
  else if (type == 'cc_recipients') return "참조 정보"
  else return "팝업"
}

//
const onRegister = async () => {
  const result = await confirmAlert("전송 하시겠습니까?");
  if (!result) return;

  let params = {
    template_id: props.modalParam.template_id,
    variables: {
      add_sender: [],
      add_recipients: props.modalParam.receiver,
      add_cc_recipients: [],
    }
  }

  if (props.modalParam.sender.length > 0) {
    params.variables.add_sender.push(...props.modalParam.sender);
  }

  if (props.modalParam.receiver.length > 0) {
    params.variables.add_recipients.push(...props.modalParam.receiver);
  }

  if (props.modalParam.cc_recipients.length > 0) {
    params.variables.add_cc_recipients.push(...props.modalParam.cc_recipients);
  }

  if (props.modalParam.variables) {
    props.modalParam.variables.forEach((variable) => {
      params.variables[variable._name] = variable._value;
    });
  }

  $ofetch('/api/admin/system/mailTemplate/sendMailWithTemplate', {
    method: 'POST',
    body: params
  })
    .then(async (res) => {
      if (res && !res.errorMessage) {
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
  select_user_type.value = type;

  if (toggleDeptModal.value) {
    toggleDeptModal.value = false;
  } else {
    toggleDeptModal.value = true;
  }
};

//
const onSelectDept = (info: any) => {
  if (info.length > 0) {
    let i = 0;
    info.forEach((item) => {
      if (select_user_type.value === 'sender') {
        if (!props.modalParam.sender.some(existingItem => existingItem.email === item.email)) {
          if (i === 0) {
            if (props.modalParam.sender.length < 1) {
              props.modalParam.sender.push(item);
              i++;
            }
          } else {
            return;
          }
        }
      } else if (select_user_type.value === 'receiver') {
        if (!props.modalParam.receiver.some(existingItem => existingItem.email === item.email)) {
          props.modalParam.receiver.push(item);
        }
      } else if (select_user_type.value === 'cc_recipients') {
        if (!props.modalParam.cc_recipients.some(existingItem => existingItem.email === item.email)) {
          props.modalParam.cc_recipients.push(item);
        }
      }
    })
  }
};

//
const onRemoveSender = (sender: any) => {
  props.modalParam.sender.splice(props.modalParam.sender.indexOf(sender), 1);
}

//
const onRemoveCcReceiver = (cc_recipients: any) => {
  props.modalParam.cc_recipients.splice(props.modalParam.cc_recipients.indexOf(cc_recipients), 1);
}

//
const onRemoveReceiver = (receiver: any) => {
  props.modalParam.receiver.splice(props.modalParam.receiver.indexOf(receiver), 1);
}

//
const onAddVariable = () => {
  if (!variable.value._name || !variable.value._value) return;

  props.modalParam.variables.push({
    _name: variable.value._name,
    _value: variable.value._value,
  })

  variable.value._name = "";
  variable.value._value = "";
}

//
const onRemoveVariable = (variable: any) => {
  props.modalParam.variables.splice(props.modalParam.variables.indexOf(variable), 1);
}

//
defineExpose({
  onClose
})
</script>
