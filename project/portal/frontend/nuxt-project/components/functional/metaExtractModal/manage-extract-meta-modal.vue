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
                    <input type="radio" :id="'rd_' + idx" name="authTypeSelect" :value="extractType.id"
                      v-model="currentExtractType" />
                    <label :for="'rd_' + idx"><span>{{ extractType.description }}</span></label>
                  </div>
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

const emit = defineEmits(["onCloseModal", "onSubmit"]);
const props = defineProps(["extractModal", "currentExtractType"]);

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

// 확인 버튼 클릭 이벤트 핸들러
const onConfirm = async () => {
  let params = {
    extractType: currentExtractType.value
  }
  emit("onSubmit", params);
};

defineExpose({
  onClose
})
</script>
