<template>
  <div class="mu-modal" v-if="props.extractModal.toggle">
    <div class="mu-dialog-background"></div>

    <div :class="['mu-dialog', 'w600']">
      <div class="mu-dialog-head">
        <p class="title">메타 추출</p>
      </div>
      <div class="mu-dialog-body">
        <div class="mu-item-group">
          <table class="mu-formbox">
            <caption>메타 추출</caption>
            <colgroup>
              <col style="width: 150px" />
              <col />
            </colgroup>
            <tbody>
              <tr>
                <th>
                  추출 타입
                </th>
                <td>
                  <div class="mu-radio" v-for="(extractType, idx) in extractTypeList">
                    <input type="radio" :id="'rd_extract' + idx" name="extractsTypeSelect" :value="extractType.id"
                      v-model="currentExtractType" />
                    <label :for="'rd_extract' + idx"><span>{{ extractType.description }}</span></label>
                  </div>
                </td>
              </tr>
              <tr>
                <th>
                  추출 방식
                </th>
                <td>
                  <div class="mu-radio" v-for="(downloadType, idx) in downloadTypeList">
                    <input type="radio" :id="'rd_download' + idx" name="downloadTypeSelect" :value="downloadType.id"
                      v-model="currentDownloadType" />
                    <label :for="'rd_download' + idx"><span>{{ downloadType.description }}</span></label>
                  </div>
                </td>
              </tr>
              <tr v-if="currentDownloadType == 0">
                <th>
                  Queue 이름
                </th>
                <td>
                  <input
                    type="text"
                    class="mu-input"
                    pattern="^\S+$"
                    title="Queue 이름에는 공백을 포함할 수 없습니다."
                    v-model="queueName"
                    placeholder="Queue 이름에는 공백을 포함할 수 없습니다."></input>
                </td>
              </tr>
            </tbody>
          </table>

        </div>
      </div>
      <div class="mu-dialog-foot">
        <button type="button" class="mu-btn" @click="onCancel"><span>취소</span></button>
        <button type="button" class="mu-btn mu-btn-primary" @click="onConfirm">
          <span>확인</span>
        </button>
      </div>
      <!-- 접근성 : 내용을 읽고 나서 닫기 나중으로 빼준다  -->
      <div class="mu-dialog-close-btn">
        <button type="button" class="mu-btn mu-btn-close" title="해당 팝업 닫기" @click="onClose">
          <i class="mu-icon-svg close"></i><span>팝업닫기</span>
        </button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from "vue";
import { errorAlert } from "../alert/alert-default";

const emit = defineEmits(["onCloseModal", "onSubmit"]);
const props = defineProps(["extractModal", "currentExtractType", "currentDownloadType"]);

const currentExtractType = ref(0)
const extractTypeList = ref([
  {
    id: 0,
    description: "JSON"
  }, {
    id: 1,
    description: "XML"       
  }
])

const currentDownloadType = ref(0)
const downloadTypeList = ref([
  {
    id: 0,
    description: "RabbitMQ"
  }, {
    id: 1,
    description: "다운로드"
  }
])

const queueName = ref("")

// 공개설정 모달 여는 시점에서
watch(
  () => props.authModal,
  (newVal, oldVal) => {
    // 설정하고자 하는 항목이 2개 이상이면 기본 0 설정
    if (newVal && newVal.target) {
      if (newVal.target.length > 1) {
        currentExtractType.value = 0
      } else {
        currentExtractType.value = newVal.target[0].release ? newVal.target[0].release : 0
      }
    }
  }
);

const onClear = () => {
  currentExtractType.value = 0
  currentDownloadType.value = 0
  queueName.value = ""
}

// 취소 버튼 클릭 이벤트 핸들러
const onCancel = () => {
  emit("onCloseModal");
  onClear()
};

// 닫기 이벤트 핸들러
const onClose = () => {
  emit("onCloseModal");
  onClear()
};

// 확인 버튼 클릭 이벤트 핸들러
const onConfirm = async () => {
  let params = {
    extractType: currentExtractType.value,
    downloadType: currentDownloadType.value,
    queueName: queueName.value
  }

  // RabbitMQ 전송 시 Queue 이름이 있는 경우 동작
  if (currentDownloadType.value == 0) {
    if (!queueName.value || queueName.value.trim() === "") {
      errorAlert("Queue 이름을 입력해주세요.")
      return
    }
  }

  emit("onSubmit", params);
  onClear()
};

defineExpose({
  onClose
})
</script>
