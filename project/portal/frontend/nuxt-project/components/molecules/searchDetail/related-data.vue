<template>
  <div :class="props.isAdmin ? 'data-box-wrap' : ''" :style="props.isAdmin ? 'padding-top: 5px; padding-bottom: 35px' : ''">
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
      <div class="detail-box" v-if="classifications && classifications.length > 0">
        <ul class="connection-list">
          <li v-for="(item, index) in classifications" :key="index">
            <div class="datatype">
              <em class="datatype-label">{{ item.systemtype }}</em>
            </div>
            <div class="datatype">
              <em class="datatype-label">{{ item.metatype }}</em>
            </div>
            <a v-on:click="onClickDataName(item)">
              <span>{{ item.name }}</span>
              <span class="datatype-desc" v-if="item.description"> - {{ item.description }}</span>
            </a>
          </li>
        </ul>
      </div>
      <p v-else class="nodata">연관 데이터가 존재하지 않습니다.</p>

    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted } from "vue";
import { useRouter } from "vue-router";
import { useUserStore } from "~/store/user";

const userStore = useUserStore();
const router = useRouter();

const emit = defineEmits(["onClickData"]);
const props = defineProps(["classifications", "isAdmin"]);

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
  router.push({ query: { guid: item.id } }).then(() => {
    // 페이지를 강제로 새로고침
    userStore.setGuid(item.id);
    emit("onClickData", item.id);
    window.scrollTo(0, 0);
  });
}

// const onClickDataName = (item) => {
//   emit("onClickData", item.guid);
//   userStore.setGuid(item.guid);
//   window.scrollTo(0, 0);
// };
</script>
