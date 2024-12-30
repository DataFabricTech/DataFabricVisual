<template>
  <div :class="props.isAdmin ? 'data-box-wrap' : ''" :style="props.isAdmin ? 'padding-top: 5px; padding-bottom: 35px' : ''">
    <div class="detail-section" :class="showData ? 'open' : 'close'">
      <div class="mu-heading">
        <h4>데이터 흐름조회</h4>
        <div class="mu-item-group">
          <button
            type="button"
            class="mu-btn mu-btn-bg-non btn-section-toggle"
            title="접기"
            @click="toggleShowData()"
            style="margin-left: 10px"
          >
            <span>{{ toggleShowDataText }}</span>
          </button>
        </div>
      </div>
      <div class="detail-box" v-show="showData">
        <div
          v-show="props.isAdmin"
          class="mu-item-group"
          style="display: block; margin-bottom: 20px; text-align: right;"
        >
          <button
            type="button"
            class="mu-btn mu-btn-primary"
            @click="onToggleLineageModal"
          >
            <span>데이터 흐름 수정</span>
          </button>
        </div>
        <d3-diagram :startNode="props.startNode" />
      </div>
    </div>

    <!-- 데이터 흐름 수정 모달 -->
    <data-lineage-modal
        :toggle="toggleLineageModal"
        :entity="props.entity"
        @onCloseModal="onToggleLineageModal"
        :userDataParam="props.userDataParam"
      />
  </div>
</template>

<script setup lang="ts">
import D3Diagram from "@functional/dataLineage/d3-diagram";
import DataLineageModal from "@organisms/searchDetail/data-lineage-modal";

const props = defineProps(['isAdmin', 'startNode', 'entity', 'userDataParam', "isAdmin"])

// const showData = ref(true);
// const toggleShowDataText = ref("접기");
const showData = ref(false);
const toggleShowDataText = ref("펼치기");

onMounted(() => {
  // 리니지를 한번은 그리고 접어두기 위해 설정
  showData.value = false
})

const toggleShowData = () => {
  showData.value = !showData.value;
  if (showData.value) {
    toggleShowDataText.value = "접기";
  } else {
    toggleShowDataText.value = "펼치기";
  }
};

const toggleLineageModal = ref(false); // 데이터 흐름 수정 모달 제어

const onToggleLineageModal = () => {
  toggleLineageModal.value = !toggleLineageModal.value;
};

</script>
