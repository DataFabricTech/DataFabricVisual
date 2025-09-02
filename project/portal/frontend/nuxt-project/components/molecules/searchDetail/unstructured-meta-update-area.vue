<template>
  <div v-if="props.isAdmin && !props.isDetail && props.typeName && props.typeName.toUpperCase() == 'FILE'" :class="props.isAdmin ? 'data-box-wrap' : ''"
    :style="props.isAdmin ? 'padding-top: 5px; padding-bottom: 35px' : ''">
    <div class="detail-section" :class="showData ? 'open' : 'close'">
      <div class="mu-heading">
        <h4>비정형 데이터</h4>
        <div class="mu-item-group">
          <button type="button" class="mu-btn mu-btn-bg-non btn-section-toggle" title="접기" @click="toggleShowData()"
            style="margin-left: 10px">
            <span>{{ toggleShowDataText }}</span>
          </button>
        </div>
      </div>
      <div class="board-body">
        <!-- mu-formbox -->
        <table class="mu-formbox">
          <caption>
            비정형 데이터 등록
          </caption>
          <colgroup>
            <col style="width: 85px" />
            <col style="width: 200px" />
            <col style="width: 85px" />
            <col style="width: 200px" />
          </colgroup>
          <tbody>
            <tr>
              <th scope="row">메타데이터 ID<strong class="required">필수</strong></th>
              <td style="display: flex; align-items: center;">
                {{ entity.meta_id }}

              </td>
              <th scope="row">데이터 출처</th>
              <td>
                <input type="text" placeholder="데이터 출처를 작성해주세요." class="mu-input"
                  :title="currentDataFrom" v-model="currentDataFrom" maxlength="100"/>
              </td>
            </tr>
            <tr>
              <th scope="row">파일 명<strong class="required">필수</strong></th>
              <td>
                {{ entity.meta_name }}
              </td>
              <th scope="row" rowspan="3">데이터 설명</th>
              <td rowspan="3">
                <textarea class="mu-area h150" :placeholder="`데이터 설명을 작성해주세요. 엔터키는 지원하지 않습니다. (최대512자)`" title="데이터 설명 입력"
                  maxlength="512" v-model="currentUserDescription" @keydown="preventNewline" />
              </td>
            </tr>
            <tr>
              <th scope="row">데이터 명</th>
              <td>
                <input type="text" placeholder="데이터 명을 작성해주세요." class="mu-input"
                  :title="currentDescription" v-model="currentDescription" maxlength="100"/>
              </td>
            </tr>
            <tr>
              <th scope="row">공개설정</th>
              <td>
                <div class="mu-radio" v-for="(authType, idx) in authTypeList">
                  <input type="radio" :id="'rd_' + idx" name="authTypeSelect" :value="authType.id"
                    v-model="currentAuthType" />
                  <label :for="'rd_' + idx"><span>{{ authType.description }}</span></label>
                </div>
              </td>
            </tr>
          </tbody>
        </table>
        <div class="mu-button-wrap" style="margin: 5px; text-align: right;">
          <button class="mu-btn mu-btn-md mu-btn-primary" @click="onClickUnstructuredMetaUpdate">
            <span>적용</span>
          </button>
        </div>
        <!-- //mu-formbox -->
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { confirmAlert } from '@/components/functional/alert/alert-default';


const props = defineProps(['entity', 'referredEntitiesAttributes', 'isAdmin', 'attributes', 'isEdited', 'isDetail', 'entity', 'typeName'])
const emit = defineEmits(['getAllDataByGuid', 'onClickAddRow', 'onClickDelRow', 'onClickBusinessAttributesConfirm', 'updateUnstructuredMetaUpdate'])

const showData = ref(true);
const toggleShowDataText = ref("접기");
const currentDataFrom = ref("");
const currentDescription = ref(""); // 데이터 명 (file타입은 description을 데이터 명으로 사용)
const currentUserDescription = ref(""); // 데이터 설명 (file타입은 user_description을 데이터 설명으로 사용)
const currentAuthType = ref(0) // 공개 설정 (0: secret, 1: public, 2: private)
const authTypeList = ref([
  {
    id: 0,
    description: "secret"
  }, {
    id: 1,
    description: "public"
  }, {
    id: 2,
    description: "private"
  },
])

watch(() => props.entity, (newVal) => {
  if (newVal) {
    if (currentDataFrom.value == "") currentDataFrom.value = newVal.attributes.data_from ? newVal.attributes.data_from : null;
    if (currentDescription.value == "") currentDescription.value = newVal.description ? newVal.description : null;
    if (currentUserDescription.value == "") currentUserDescription.value = newVal.user_description ? newVal.user_description : null;

    if (newVal.release == 0) currentAuthType.value = 0;
    else if (newVal.release == 1) currentAuthType.value = 1;
    else if (newVal.release == 2) currentAuthType.value = 2;
  }
})

const toggleShowData = () => {
  showData.value = !showData.value;
  if (showData.value) {
    toggleShowDataText.value = "접기";
  } else {
    toggleShowDataText.value = "펼치기";
  }
};

const preventNewline = (event) => {
  if (event.key === 'Enter') {
    event.preventDefault(); // Enter 키 입력 차단
  }
};

const onClickUnstructuredMetaUpdate = async () => {
  if (await confirmAlert("비정형 데이터 메타정보를 수정 하시겠습니까?")) {
    const updateMetaInfo = {
      meta_id: props.entity.meta_id,
      description: currentDescription.value,
      user_description: currentUserDescription.value,
      attributes: {
        ...props.entity.attributes,
        data_from: currentDataFrom.value
      },
      release: currentAuthType.value
    }

    emit("updateUnstructuredMetaUpdate", updateMetaInfo);
  }
};
</script>
