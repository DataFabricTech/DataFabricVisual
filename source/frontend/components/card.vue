<template>
  <div class="card">
    <div class="card-content">
      <div class="h-group justify-between w-full">
        <div class="h-group gap-[8px]">
          <BaseBadge class="bg-marker-cyan">{{ model.storageInfo.storageType }}</BaseBadge>
          <BaseBadge class="bg-marker-purple">{{ model.domain }}</BaseBadge>
          <!--TODO: 기획 및 API 명세서 fix 되면 코드 수정 -->
<!--          <BaseBadge v-for="(item, index) in model.tags" :class="badgeClass[index % badgeClass.length]">{{ item }}</BaseBadge>-->
        </div>
        <div class="h-group gap-[16px]">
          <BaseButton class="button-link-primary" @click="preview">
            <span class="button-text">미리보기</span>
          </BaseButton>
          <BaseButton class="button-normal" @click="download">
            <span class="button-text">
              {{ downloadStatus }}
            </span>
          </BaseButton>
          <div :class="cardMode === true ? 'card-status' : 'card-status hidden'">
            <!-- TODO: 데이터 연결상태 리턴값에 따라 클래스 및 명칭 변경 -->
            <baseBadge class="bg-marker-gray">Inactive(Disconnected)</baseBadge>
          </div>
          <KebabMenu class="is-bottom"></KebabMenu>
        </div>
      </div>
      <div class="v-group w-full">
        <a href="#" class="card-link" title="이동" @click="$emit('click', model.id)">{{ model.name }}</a>
        <baseTextInput placeholder="연결정보 이름 영역입니다." class="hidden"></baseTextInput>
        <p class="card-detail">{{ model.description }}</p>
        <baseTextInput placeholder="연결정보 설명 영역입니다." class="hidden"></baseTextInput>
      </div>
      <div class="h-group gap-[16px]">
        <baseTextInput placeholder="태그 영역입니다." class="hidden"></baseTextInput>
        <BaseTag v-for="item in model.tags">#{{ item }}</BaseTag>
      </div>
      <div class="h-group justify-between w-full">
        <div class="h-group gap-[16px]">
          <dl class="define">
            <dt class="define-term">
              <svg-icon name="date" class="svg-icon"></svg-icon>
              수정일자:
            </dt>
            <dd class="define-desc">
              {{ model.updatedAt }}
            </dd>
          </dl>
          <dl class="define">
            <dt class="define-term">
              <svg-icon name="user" class="svg-icon"></svg-icon>
              소유자:
            </dt>
            <dd class="define-desc">
              {{ model.creator }}
            </dd>
          </dl>
        </div>
        <div class="h-group gap-[30px]">
          <dl class="define">
            <dt class="define-term">
              <svg-icon class="svg-icon" name="eye"></svg-icon>
              <span class="hidden">조회수</span>
            </dt>
            <dd class="define-desc">
              {{ model.statInfo.access }}
            </dd>
          </dl>
          <dl class="define">
            <dt class="define-term">
              <svg-icon class="svg-icon" name="rating-fill"></svg-icon>
              <span class="hidden">평균 평점</span>
            </dt>
            <dd class="define-desc">
              {{ model.statInfo.rating.toFixed(1) }}
            </dd>
          </dl>
          <dl class="define">
            <dt class="define-term">
              <svg-icon class="svg-icon" name="bookmark"></svg-icon>
              <span class="hidden">북마크수</span>
            </dt>
            <dd class="define-desc">
              {{ model.statInfo.favorite }}
            </dd>
          </dl>
          <dl class="define">
            <dt class="define-term">
              <svg-icon class="svg-icon" name="download"></svg-icon>
              <span class="hidden">다운로드수</span>
            </dt>
            <dd class="define-desc">
              {{ model.statInfo.download }}
            </dd>
          </dl>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { defineProps, defineEmits } from "vue/dist/vue";

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
    status: number,
    uri: string
  }
}

const props = defineProps({
  model: {
    type: Object as () => ModelType,
    required: true
  },
  cardMode: {
    type: Boolean,
    default: false
  }
});
const emit = defineEmits(['preview', 'download', 'click']);
const badgeClass = ["bg-marker-purple", "bg-marker-red", "bg-marker-yellow"]
const downloadStatus = computed(() => {
  switch (props.model.downloadInfo.status) {
    case 1:
      return '다운로드 요청';
    case 2:
      return '다운로드 중';
    case 3:
      return '다운로드 가능';
    default:
      return '다운로드'
  }
})
function preview() {
  emit('preview', props.model.id)
}
function download() {
  let downloadInfo = {
    id: props.model.id,
    uri: props.model.downloadInfo.uri
  }
  emit('download', downloadInfo)
}
</script>
