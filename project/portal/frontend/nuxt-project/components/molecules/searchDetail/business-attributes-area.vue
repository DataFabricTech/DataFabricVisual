<template>
  <div class="detail-box" v-if="props.isAdmin ? true : props.attributes.length > 0 ? true : false">
    <div class="mu-heading">
      <h4 v-if="isRegister">비지니스 속성</h4>
      <h5 v-else>비지니스 속성</h5>
      <!--        <div class="mu-item-group">-->
      <div v-show="props.isAdmin" class="mu-item-group">
        <button :style="isRegister ? 'padding: 0 20px' : ''" type="button" class="mu-btn mu-btn-primary" title="비지니스 속성 추가" @click="onClickAdd">
          <i class="mu-icon-svg add"></i>
          <span>비지니스 속성 추가</span>
        </button>
      </div>
    </div>
    <div class="table-scroll max-h400">
      <table class="mu-grid mu-grid-data">
        <colgroup>
          <col style="width: 200px" />
          <col style="width: auto" />
        </colgroup>
        <thead>
          <tr>
            <th scope="col">Key</th>
            <th scope="col">Value</th>
          </tr>
        </thead>
        <tbody v-if="props.attributes && props.attributes.length > 0">
          <tr v-for="(item, index) in props.attributes" :key="index">
            <td v-if="props.isAdmin">
              <input type="text" id="businessName01" placeholder="Key값을 입력하세요" :class="inputClass('key', item)"
                v-model="item.key" />
            </td>
            <td v-else>{{ item.key }}</td>
            <td v-if="props.isAdmin">
              <div class="mu-item-group">
                <input type="text" id="businessValue01" placeholder="Value값을 입력하세요" :class="inputClass('value', item)"
                  v-model="item.value" />
                <button type="button" class="mu-btn mu-btn-icon-only" title="비지니스 속성 삭제" @click="onClickDel(index)">
                  <i class="mu-icon-svg minus"></i><span>비지니스 속성 삭제</span>
                </button>
              </div>
            </td>
            <td v-else>{{ item.value }}</td>
          </tr>
        </tbody>
        <tbody v-else-if="props.isAdmin" style="height: 52px;">
          <tr>
            <td colspan="2">
              <p class="nodatas">비즈니스 속성을 추가해 주세요.</p>
            </td>
          </tr>
        </tbody>
      </table>
      <div v-if="props.isAdmin && props.isDetail" class="mu-button-wrap" style="margin: 5px; text-align: right;">
        <button class="mu-btn mu-btn-md mu-btn-primary" @click="onClickBusinessAttributesConfirm">
          <span>적용</span>
        </button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { confirmAlert } from '@/components/functional/alert/alert-default';

const emit = defineEmits(["onClickAddRow", "onClickDelRow", "onClickBusinessAttributesConfirm"]);
const props = defineProps(["isAdmin", "attributes", "isRegister", "isEdited", "isDetail"]);

// 비지니스 속성 추가 버튼 클릭 이벤트 핸들러
const onClickAdd = () => {
  emit("onClickAddRow");
};

// 비지니스 속성 삭제 버튼 클릭 이벤트 핸들러
const onClickDel = (index) => {
  emit("onClickDelRow", index);
};

// 입력값에 따라 input 적용 클래스 목록 계산
const inputClass = (type, item) => {
  const classNames = ["mu-input"];

  if (!item[type]) {
    classNames.push("error");
  }

  return classNames.join(" ");
};

// 적용 버튼 클릭 이벤트 핸들러
const onClickBusinessAttributesConfirm = async () => {
  const confirm = await confirmAlert(
    "비지니스 속성을 적용 하시겠습니까?"
  );
  if (confirm) {
    if (props.isEdited) {
      // 수정된 내용이 있을 경우에만 저장 처리
      emit("onClickBusinessAttributesConfirm", props.attributes);
    } else {
      // 수정된 내용이 없을 경우 알림
      await confirmAlert("수정된 내용이 없습니다.");
    }
  }
};

</script>
