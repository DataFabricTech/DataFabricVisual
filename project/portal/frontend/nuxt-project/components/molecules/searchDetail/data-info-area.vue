<template>
  <div :class="props.isAdmin ? 'data-box-wrap' : ''"
    :style="props.isAdmin ? 'padding-top: 5px; padding-bottom: 35px' : ''">
    <div class="detail-section" :class="showData ? 'open' : 'close'">
      <div class="mu-heading">
        <h4>데이터 정보</h4>
        <div class="mu-item-group">
          <button type="button" class="mu-btn mu-btn-bg-non btn-section-toggle" title="접기" @click="toggleShowData()"
            style="margin-left: 10px">
            <span>{{ toggleShowDataText }}</span>
          </button>
        </div>
      </div>
      <!-- detail-box : 데이터 정보 > 메타속성 -->
      <meta-data-info :entity="props.entity" :referredEntitiesAttributes="props.referredEntitiesAttributes"
        @onClickData="onClickDataName" />
      <!-- detail-box : 데이터 정보 > 비지니스 속성 -->
      <business-attributes-area :isAdmin="props.isAdmin" :attributes="props.attributes" @onClickAddRow="onClickAdd"
        @onClickDelRow="onClickDel" />
    </div>
  </div>
</template>

<script setup lang="ts">
import MetaDataInfo from "@molecules/searchDetail/meta-data-info";
import BusinessAttributesArea from "@molecules/searchDetail/business-attributes-area";

const props = defineProps(['entity', 'referredEntitiesAttributes', 'isAdmin', 'attributes'])
const emit = defineEmits(['getAllDataByGuid', 'onClickAddRow', 'onClickDelRow'])

const showData = ref(true);
const toggleShowDataText = ref("접기");

const toggleShowData = () => {
  showData.value = !showData.value;
  if (showData.value) {
    toggleShowDataText.value = "접기";
  } else {
    toggleShowDataText.value = "펼치기";
  }
};

// 선택된 컬럼 또는 테이블 엔티티 상세 조회 화면으로 이동
const onClickDataName = (item) => {
  console.log('go To Meta Page')
  return
};

// 비지니스 속성 추가 버튼 클릭 이벤트 핸들러
const onClickAdd = () => {
  emit("onClickAddRow");
};

// 비지니스 속성 삭제 버튼 클릭 이벤트 핸들러
const onClickDel = (index) => {
  emit("onClickDelRow", index);
};

</script>
