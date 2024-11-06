<template>
  <div class="detail-section" :class="showData ? 'open' : 'close'">
    <div class="mu-heading">
      <h4>연관 데이터</h4>
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
    <div class="detail-box">
      <ul class="connection-list">
        <li v-for="(item, index) in classifications" :key="index">
          <!-- <div class="datatype">
            <em class="datatype-label">{{ item.typeName }}</em>
          </div> -->
          <a v-on:click="onClickDataName(item)"
            ><span>{{ item.attributes && item.attributes.comment ? item.attributes.comment : item.displayText }}</span>
            <!-- <span class="datatype-desc">{{ item.attributes.comment }}</span> -->
          </a>
        </li>
      </ul>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from "vue-router";
import { useUserStore } from "~/store/user";

const userStore = useUserStore();
const router = useRouter();

const emit = defineEmits(["onClickData"]);
const props = defineProps(["classifications"]);

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

const onClickDataName = (item) => {
  // 페이지 리로드 없이 URL 변경
  router.push({ query: { guid: item.guid } }).then(() => {
    // 페이지를 강제로 새로고침
    userStore.setGuid(item.guid);
    emit("onClickData", item.guid);
    window.scrollTo(0, 0);
  });
}

// const onClickDataName = (item) => {
//   emit("onClickData", item.guid);
//   userStore.setGuid(item.guid);
//   window.scrollTo(0, 0);
// };
</script>
