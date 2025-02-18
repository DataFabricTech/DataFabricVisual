<template>
  <div class="inner">
    <!-- board-wrap -->
    <div class="board-wrap">
      <!-- mu-heading -->
      <div class="mu-heading">
        <slot name="heading"></slot>
        <div class="mu-item-group">
          <p class="txt-info">
            <strong class="required">필수</strong>표시는 필수입력 항목입니다.
          </p>
        </div>
      </div>
      <!-- //mu-heading -->
      <!-- board-body -->
      <div class="board-body">
        <!-- mu-formbox 상세폼테이블 -->
        <table class="mu-formbox">
          <slot name="caption"></slot>
          <colgroup>
            <col style="width: 180px" />
            <col />
          </colgroup>
          <tbody>
            <tr>
              <th scope="row">템플릿 ID<strong class="required">필수</strong></th>
              <td>
                <input
                  type="text"
                  v-model="props.post.template_id"
                  placeholder="템플릿 ID 입력해 주세요"
                  v-bind:class="[!templateId_Valid ? 'error' : '', 'mu-input']"
                  title="템플릿 ID 입력"
                  maxlength="50"
                  oninput="this.value = this.value.replace(/[^a-zA-Z0-9_-]/g, '')"
                  :readonly="route.query.mode != 'add'"
                  required
                />
                <p v-if="!templateId_Valid" class="complete-msg error">ID를 입력해주세요.</p>
              </td>
            </tr>
            <tr>
              <th scope="row">제목<strong class="required">필수</strong></th>
              <td>
                <input
                  type="text"
                  v-model="props.post.template_title"
                  placeholder="제목은 50자 이내로 입력 가능합니다."
                  v-bind:class="[!templateId_Valid ? 'error' : '', 'mu-input']"
                  title="제목 입력"
                  maxlength="50"
                />
                <p v-if="!templateTitle_Valid" class="complete-msg error">제목을 입력해주세요.</p>
              </td>
            </tr>
            <tr>
              <th scope="row">보내는 사람<strong class="required">필수</strong></th>
              <td>
                <div class="tags-group">
                    <div v-if="props.post.sender && props.post.sender.length > 0 && props.post.sender.some(sender => sender.name && sender.email)" id="sender-tags" 
                         v-for="sender in props.post.sender" :key="sender.email" class="tag tag-sm">
                        <span class="mail-label">{{ sender.name }} {{ sender.email }}</span>
                        <button type="button" class="mu-btn mu-btn-delete" @click="onRemoveSender(sender)">
                            <i class="mu-icon-svg item-delete"></i><span>삭제</span>
                        </button>
                    </div>
                    <button type="button" title="사원 찾아보기" class="mu-btn mu-btn-sm mu-btn-primary mu-btn-icon-only" @click="onToggleDeptModal('sender')">
                        <i class="mu-icon-svg add"></i><span>찾아보기</span>
                    </button>
                </div>
                <p v-if="!templateSender_Valid" class="complete-msg error">보내는 사람을 선택해주세요.</p>
              </td>
            </tr>
            <tr>
              <th scope="row">받는 사람</th>
              <td>
                <div id="receiver-tags" class="tags-group">
                  <div v-if="props.post.receiver && props.post.receiver.length > 0 && props.post.receiver.some(receiver => receiver.name && receiver.email)" 
                       v-for="receiver in props.post.receiver" :key="receiver" class="tag tag-sm">
                      <span class="mail-label">{{ receiver.name }} {{ receiver.email }}</span>
                      <button type="button" class="mu-btn mu-btn-delete" @click="onRemoveReceiver(receiver)">
                          <i class="mu-icon-svg item-delete"></i><span>삭제</span>
                      </button>
                  </div>
                  <button type="button" title="사원 찾아보기" class="mu-btn mu-btn-sm mu-btn-primary mu-btn-icon-only" @click="onToggleDeptModal('receiver')">
                      <i class="mu-icon-svg add"></i><span>찾아보기</span>
                  </button>
                </div>
                <!-- <p v-if="!templateReceiver_Valid" class="complete-msg error">받는 사람을 선택해주세요.</p> -->
              </td>
            </tr>
            <tr>
              <th scope="row">참조</th>
              <td>
                <div class="tags-group">
                  <div v-if="props.post.cc_recipients && props.post.cc_recipients.length > 0 && props.post.cc_recipients.some(recipient => recipient.name && recipient.email)" id="cc-recipients-tags" 
                       v-for="cc_recipient in props.post.cc_recipients" :key="cc_recipient" class="tag tag-sm">
                      <span class="mail-label">{{ cc_recipient.name }} {{ cc_recipient.email }}</span>
                      <button type="button" class="mu-btn mu-btn-delete" @click="onRemoveCcRecipient(cc_recipient)">
                          <i class="mu-icon-svg item-delete"></i><span>삭제</span>
                      </button>
                  </div>
                  <button type="button" title="사원 찾아보기" class="mu-btn mu-btn-sm mu-btn-primary mu-btn-icon-only" @click="onToggleDeptModal('cc_recipients')">
                      <i class="mu-icon-svg add"></i><span>찾아보기</span>
                  </button>
                </div>
              </td>
            </tr>
            <tr>
              <th scope="row">내용<strong class="required">필수</strong></th>
              <td>
                <!-- <ckeditor :editor="editor" v-model="props.post.template_content"></ckeditor> -->
                 <textarea v-model="props.post.template_content" style="width:100%; height:500px"></textarea>
                <p v-if="!templateContent_Valid" class="complete-msg error">내용을 입력해주세요.</p>
              </td>
            </tr>
            <tr>
              <th>템플릿 변수</th>
              <td>
                <span style="color:blue">※ 템플릿 변수는 영문 대소문자, 숫자, 특수문자(-, _[]) 만 허용됩니다.<br></span>ex) _tmp_variable-1_<br><br>
                <input type="text" id="variable" placeholder="변수를 입력해주세요." class="mu-input w300" title="변수 입력" value="" oninput="this.value = this.value.replace(/[^a-zA-Z0-9\[\]_-]/g, '')">
                <button type="button" title="변수 추가" class="mu-btn mu-btn-sm mu-btn-primary mu-btn-icon-only" style="margin-left: 5px;" @click="onAddVariable">
                  <i class="mu-icon-svg add"></i><span>변수 추가</span>
                </button>
                <div class="user-info max-h400 slim-scroll">
                  <ul v-if="props.post.template_variables && props.post.template_variables.length > 0 && props.post.template_variables.some(variable => variable)">
                    <li v-for="variable in props.post.template_variables" :key="variable">
                        <button type="button" class="mu-btn mu-btn-sm mu-btn-icon-only mu-btn-bg-non" title="삭제" @click="onRemoveVariable(variable)">
                            <i class="mu-icon-svg item-delete"></i>
                            <span>삭제</span>
                        </button>
                        <span class="name">{{ variable }}</span>
                    </li>
                  </ul>
                </div>
              </td>
            </tr>
            <tr>
              <th scope="row">SMS 메세지</th>
              <td>
                <textarea rows="10" placeholder="SMS 발송 메세지를 입력해주세요." title="SMS 발송 메세지 입력" class="mu-area h80" v-model="props.post.template_sms_content">{{ props.post.template_sms_content }}</textarea>
              </td>
            </tr>
            <tr>
              <th scope="row">사용여부</th>
              <td>
                <div class="mu-item-group">
                  <div class="mu-radio">
                      <input type="radio" id="r111" name="name2" v-model="props.post.use_yn" value="Y" :checked="props.post.use_yn === 'Y'">
                      <label for="r111"><em class="step-process review">사용</em></label>
                  </div>
                  <div class="mu-radio">
                      <input type="radio" id="r444" name="name2" v-model="props.post.use_yn" value="N" :checked="props.post.use_yn === 'N'">
                      <label for="r444"><em class="step-process unsupply">미사용</em></label>
                  </div>
                  <p v-if="!templateUseYn_Valid" class="complete-msg error">사용 여부를 선택해주세요.</p>
                </div>
              </td>
            </tr>
          </tbody>
        </table>
        <!-- //mu-formbox -->
      </div>
      <!-- //board-body -->
    </div>

    <!-- mu-button-wrap -->
    <div class="mu-button-wrap">
      <button type="button" class="mu-btn mu-btn-lg" @click="onClickListButton">
        <span>취소</span>
      </button>
      <button
        type="button"
        class="mu-btn mu-btn-lg mu-btn-primary"
        @click="onClickSaveButton"
      >
        <slot name="savebutton"></slot>
      </button>
    </div>
    <!-- //mu-button-wrap -->
  </div>

  <select-dept-user-modal
      :toggle-dept-modal="toggleDeptModal"
      @onSelect="onSelectDept"
      @onCloseModal="onToggleDeptModal"
    />
</template>

<script setup lang="ts">
import { ref, computed, ComputedRef } from "vue";
import { useRoute, useRouter } from 'vue-router';
import selectDeptUserModal from "@functional/dept/select-department-user-modal";
import { errorAlert } from "@/components/functional/alert/alert-default";
const route = useRoute();

const { $ckeditor5  } = useNuxtApp();
const editor = $ckeditor5.customEditor5;

// import ClassicEditor from '@ckeditor/ckeditor5-build-classic';
// import Preview from '@ckeditor/ckeditor5-preview/src/preview';
// const editor = ClassicEditor.create(document.querySelector('#editor'), {
//     plugins: [Preview, ...], // 다른 플러그인과 함께 Preview 플러그인 추가
//     toolbar: [
//         'undo', 'redo', 'preview', // 툴바에 미리 보기 버튼 추가
//         // 다른 툴바 버튼들...
//     ],
// });


const required_flag = ref<boolean>(false);

const emit = defineEmits(["cancelMethod", "update:post", "saveMethod"]);
const props = defineProps(["post", "cancelMethod", "saveMethod"]);

const toggleDeptModal = ref(false);
const select_user_type = ref('');

const templateId_Valid: ComputedRef<boolean> = computed(() => {
  return props.post.template_id.length > 0 ? true : false;
});
const templateTitle_Valid: ComputedRef<boolean> = computed(() => {
  return props.post.template_title.length > 0 ? true : false;
});
const templateSender_Valid: ComputedRef<boolean> = computed(() => {
  return props.post.sender.length > 0 ? true : false;
});
// const templateReceiver_Valid: ComputedRef<boolean> = computed(() => {
//   return props.post.receiver.length > 0 ? true : false;
// });
const templateContent_Valid: ComputedRef<boolean> = computed(() => {
  return props.post.template_content.length > 0 ? true : false;
});
const templateUseYn_Valid: ComputedRef<boolean> = computed(() => {
  return props.post.use_yn.length > 0 ? true : false;
});

const onClickListButton = (): void => {
  emit("cancelMethod");
};

const onClickSaveButton = (): void => {
  // required_flag.value = true;
  if (   !templateId_Valid.value 
      || !templateTitle_Valid.value
      || !templateSender_Valid.value
      // || !templateReceiver_Valid.value
      || !templateContent_Valid.value
      || !templateUseYn_Valid.value
  ) return;

  emit("update:post", props.post);
  emit("saveMethod");
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
const onSelectDept = (info) => {
  if(info.length > 0){
    let i = 0;
    info.forEach((item) => {
      if(select_user_type.value === 'sender') {
        if (!props.post.sender.some(existingItem => existingItem.email === item.email)) {
          if (i === 0) {
            if(info.length > 0){
              errorAlert("보내는 사람은 한명만 선택할 수 있습니다.");
            }

            if(props.post.sender.length < 1) {
              props.post.sender.push(item);
            }
          } else {
            return;
          }
          
          i++;
        }
      } else if(select_user_type.value === 'receiver') {
        if (!props.post.receiver.some(existingItem => existingItem.email === item.email)) {
          props.post.receiver.push(item);
        }
      } else if(select_user_type.value === 'cc_recipients') {
        if (!props.post.cc_recipients.some(existingItem => existingItem.email === item.email)) {
          props.post.cc_recipients.push(item);
        }
      } else if(select_user_type.value === 'bcc_recipients') {
        if (!props.post.bcc_recipients.some(existingItem => existingItem.email === item.email)) {
          props.post.bcc_recipients.push(item);
        }
      }
    })
  }
};

// 
const onRemoveSender = (sender) => {
  props.post.sender.splice(props.post.sender.indexOf(sender), 1);
}

// 
const onRemoveReceiver = (receiver) => {
  props.post.receiver.splice(props.post.receiver.indexOf(receiver), 1);
}

// 
const onRemoveCcRecipient = (cc_recipient) => {
  props.post.cc_recipients.splice(props.post.cc_recipients.indexOf(cc_recipient), 1);
}

// 
const onRemoveBccRecipient = (bcc_recipient) => {
  props.post.bcc_recipients.splice(props.post.bcc_recipients.indexOf(bcc_recipient), 1);
}

// 
const onRemoveVariable = (variable) => {
  props.post.template_variables.splice(props.post.template_variables.indexOf(variable), 1);
}

//
const onAddVariable = () => {
  const variable = document.getElementById('variable').value;
  if (!variable) return;

  const variablePattern = /^[a-zA-Z0-9\[\]_-]+$/;
  if (!variablePattern.test(variable)) return;

  props.post.template_variables = props.post.template_variables.filter(v => v !== null && v !== '');

  // 
  if (!props.post.template_variables.includes(variable)) {
    props.post.template_variables.push(variable);

    // 
    document.getElementById('variable').value = '';
  }
}
</script>
