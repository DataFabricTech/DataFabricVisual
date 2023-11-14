<template>
  <div class="card">
    <div class="card-content">
      <div class="h-group justify-between w-full">
        <div class="h-group gap-[8px]">
          <BaseBadge class="bg-marker-cyan">{{ props.model.storageInfo.storageType }}</BaseBadge>
          <BaseBadge class="bg-marker-purple">{{ props.model.domain }}</BaseBadge>
        </div>
        <div class="h-group gap-[16px]">
          <BaseButton class="button-link-primary" v-if="props.showPreviewBtn" @click="preview">
            <span class="button-text">미리보기</span>
          </BaseButton>
          <BaseButton class="button-normal" v-if="!props.showConnectInfo" @click="download">
            <span class="button-text">
              {{ downloadStatus }}
            </span>
          </BaseButton>
          <div v-if="props.showConnectInfo">
            <baseBadge :class="isConnected(props.model.status) ? 'bg-marker-cyan' : 'bg-marker-gray'">
              {{ isConnected(props.model.status) ? "Connected" : "Inactive(Disconnected)" }}</baseBadge
            >
          </div>
          <KebabMenu class="is-bottom"></KebabMenu>
        </div>
      </div>
      <div class="v-group w-full">
        <a href="#" class="card-link" title="이동" @click="$emit('click', props.model.id)" v-if="!props.isUpdate">{{
          props.model.name
        }}</a>
        <baseTextInput
          placeholder="연결정보 이름 영역입니다."
          v-model="props.model.name"
          v-if="props.isUpdate"
        ></baseTextInput>
        <p class="card-detail" v-if="!props.isUpdate">{{ props.model.description }}</p>
        <baseTextInput
          placeholder="연결정보 설명 영역입니다."
          v-model="props.model.description"
          v-if="props.isUpdate"
        ></baseTextInput>
      </div>
      <div class="h-group gap-[16px]">
        <BaseTag v-for="item in props.model.tags" v-if="!props.isUpdate">#{{ item }}</BaseTag>
        <div class="input-tag" v-if="props.isUpdate">
          <baseTextInput placeholder="태그 영역입니다." class="w-96" v-model="tagList"></baseTextInput>
          <VTooltip class="tooltip" placement="top-start">
            <svg-icon class="svg-icon" name="help-outline"></svg-icon>
            <template #popper>
              {{ tooltipMassage }}
            </template>
          </VTooltip>
        </div>
      </div>
      <div :class="props.showInfoComplex ? 'v-group justify-between w-full' : 'h-group justify-between w-full mt-auto'">
        <div class="h-group gap-[16px]">
          <dl class="define">
            <dt class="define-term">
              <svg-icon name="date" class="svg-icon"></svg-icon>
              수정일자:
            </dt>
            <dd class="define-desc">
              {{ props.model.lastModifiedAt.strDateTime }}
            </dd>
          </dl>
          <dl class="define">
            <dt class="define-term">
              <svg-icon name="user" class="svg-icon"></svg-icon>
              소유자:
            </dt>
            <dd class="define-desc">
              {{ props.model.createdBy.name }}
            </dd>
          </dl>
        </div>
        <div class="h-group gap-[30px]" v-if="props.showStatistics">
          <dl class="define">
            <dt class="define-term">
              <svg-icon class="svg-icon" name="eye"></svg-icon>
              <span class="hidden-text">조회수</span>
            </dt>
            <dd class="define-desc">
              {{ props.model.statistics.accessCount }}
            </dd>
          </dl>
          <dl class="define">
            <dt class="define-term">
              <svg-icon class="svg-icon" name="rating-fill"></svg-icon>
              <span class="hidden-text">평균 평점</span>
            </dt>
            <dd class="define-desc">
              {{ props.model.statistics.avgResponseTime.toFixed(1) }}
            </dd>
          </dl>
          <dl class="define">
            <dt class="define-term">
              <svg-icon class="svg-icon" name="bookmark"></svg-icon>
              <span class="hidden-text">북마크수</span>
            </dt>
            <dd class="define-desc">
              {{ props.model.statistics.bookMarkCount }}
            </dd>
          </dl>
          <dl class="define">
            <dt class="define-term">
              <svg-icon class="svg-icon" name="download"></svg-icon>
              <span class="hidden-text">다운로드수</span>
            </dt>
            <dd class="define-desc">
              {{ props.model.statistics.downloadCount }}
            </dd>
          </dl>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted } from "vue";
import { ref } from "@vue/reactivity";

/** TODO: 모델 타입 인터페이스 구현 */
interface ModelType {
  id: string;
  name: string;
  description: string;
  tags: string[];
  storageInfo: {
    storageType: string;
  };
  domain: string;
  updatedAt: string;
  creator: string;
  statInfo: {
    access: number;
    rating: number;
    favorite: number;
    download: number;
  };
  downloadInfo: {
    status: number;
    uri: string;
  };
}

const props = defineProps({
  model: {
    type: Object as () => ModelType,
    default: () => ({
      id: "111",
      name: "불법 주정차 구간 데이터",
      description: "서울시에서 수집되고 있는 불법 주정차 차량 단속 이력 정보",
      tags: ["tag1", "tag2", "tag3", "tag4", "tag5"],
      storageInfo: {
        storageType: "HDFS"
      },
      status: "CONNECTED",
      domain: "공간",
      lastModifiedAt: {
        strDateTime: "2023-11-20 13:30:40.123",
        utcTime: 1606824000000
      },
      createdBy: {
        id: "user-id01",
        name: "user-name01"
      },
      statistics: {
        accessCount: 1000,
        downloadCount: 10,
        bookMarkCount: 20,
        avgResponseTime: 1.2
      },
      downloadInfo: {
        status: 2,
        link: "uri"
      }
    })
  },
  showConnectInfo: {
    type: Boolean,
    default: false
  },
  showStatistics: {
    type: Boolean,
    default: true
  },
  showPreviewBtn: {
    type: Boolean,
    default: true
  },
  showInfoComplex: {
    type: Boolean,
    default: false
  },
  isUpdate: {
    type: Boolean,
    default: false
  }
});
const tagList = ref("");
const tooltipMassage = ref(`태그 추가 시 콤마(,)로 구분해주세요.`);
const emit = defineEmits(["preview", "download", "click", "update"]);
const computedStatus = (property: string) => {
  computed(() => {
    switch (props.model?.[property]) {
      case 1:
        return "다운로드 요청";
      case 2:
        return "다운로드 중";
      case 3:
        return "다운로드 가능";
      default:
        return "다운로드";
    }
  });
};
computedStatus("downloadInfo.status");
function preview() {
  emit("preview", props.model.id);
}
function download() {
  let downloadInfo = {
    id: props.model.id,
    uri: props.model.downloadInfo.uri
  };
  emit("download", downloadInfo);
}

const isConnected = (value: string) => {
  return value === "CONNECTED";
};
const watchAndUpdate = (property: string) => {
  watch(
    () => props.model?.[property],
    (newVal, oldVal) => {
      if (newVal !== oldVal) {
        emit("update", props.model);
      }
    }
  );
  watch(
    () => tagList.value,
    (newVal, oldVal) => {
      if (newVal != oldVal) {
        let data: string[] = tagList.value.split(",");
        if (props.model) {
          props.model.tags = data;
        }
        emit("update", props.model);
      }
    }
  );
};
watchAndUpdate("name");
watchAndUpdate("description");

onMounted(() => {
  if (props.isUpdate && props.model?.tags) {
    tagList.value = props.model.tags.join(",");
  }
});
</script>
