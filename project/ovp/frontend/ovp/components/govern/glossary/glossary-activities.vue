<template>
  <!--  활동사항 탭 시작-->
  <ul class="activity-info">
    <li class="activity-info-item" v-for="activity in activities">
      <div class="profile">
        <span class="profile-avatar">
          <image class="profile-img" alt="프로필 이미지"></image>
        </span>
      </div>
      <div class="activity-info-wrap">
        <div class="activity-info-head">
          <p class="activity-info-title">
            <!-- TODO: 디자인 퍼블리싱 -->
            <strong>{{ activity.headerMessage }}</strong>
          </p>
          <span class="activity-info-ago">{{ activity.updatedAt }}</span>
        </div>
        <div
          class="activity-info-contents"
          v-if="activity.cardStyle === 'tags'"
        >
          <div
            class="tag tag-primary tag-sm"
            v-if="
              activity.fieldOperation === 'updated' ||
              activity.fieldOperation === 'added'
            "
            v-for="tag in activity.entitySpecificInfo.updatedTags"
          >
            <a class="tag-link">{{ tag.name }}</a>
          </div>
          <div
            class="tag tag-primary tag-sm"
            v-if="activity.fieldOperation === 'deleted'"
            v-for="tag in activity.entitySpecificInfo.previousTags"
          >
            <a class="tag-link">{{ tag.name }}</a>
          </div>
        </div>
        <div
          class="activity-info-contents"
          v-if="activity.cardStyle === 'domain'"
        >
          <span>{{ activity.message }}</span>
          <span class="diff-added">{{ activity.updatedBy }}</span>
        </div>
        <div
          class="activity-info-contents"
          v-if="activity.cardStyle === 'description'"
        >
          <span class="diff-removed">{{
            activity.entitySpecificInfo.previousDescription
          }}</span>
          <span class="diff-added">{{
            activity.entitySpecificInfo.newDescription
          }}</span>
        </div>
        <!-- TODO 퍼블리싱 : Created 되는 경우만 있고 DELETE 되는 화면은 없음 -->
        <div
          class="activity-info-contents is-column"
          v-if="
            activity.cardStyle === 'entityCreated' ||
            activity.cardStyle === 'entityDeleted'
          "
        >
          <strong>{{ activity.entitySpecificInfo.entity.displayName }}</strong>
          <span class="diff-description">
            {{ activity.entitySpecificInfo.entity.description }}</span
          >
          <div class="creation-info">
            <dl class="creation-info-item">
              <dt class="creation-info-label">소유자</dt>
              <dd class="creation-info-value">
                {{
                  activity.entitySpecificInfo.entity.owner
                    .fullyQualifiedName === "" || null
                    ? "없음"
                    : activity.entitySpecificInfo.entity.owner
                        .fullyQualifiedName
                }}
              </dd>
            </dl>
            <dl class="creation-info-item">
              <dt class="creation-info-label">도메인</dt>
              <!-- TODO 개발 : 도메인 데이터 구조 확인 -->
              <dd class="creation-info-value">없음</dd>
            </dl>
          </div>
        </div>
      </div>
    </li>
  </ul>
  <!--  활동사항 탭 끝-->
</template>
<script setup lang="ts">
import { useGlossaryStore } from "@/store/glossary";
const { activities } = useGlossaryStore();
</script>
