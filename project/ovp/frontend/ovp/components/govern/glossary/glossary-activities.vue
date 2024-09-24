<template>
  <ul v-if="activities.length !== 0" class="activity-info">
    <li class="activity-info-item" v-for="activity in activities">
      <div class="profile">
        <span class="profile-avatar">
          {{ profileFirstWord }}
        </span>
      </div>
      <div class="activity-info-wrap">
        <div class="activity-info-head">
          <p class="activity-info-title">
            <span v-html="headerMessage(activity)"></span>
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
          v-if="
            activity.cardStyle === 'domain' || activity.cardStyle === 'default'
          "
        >
          <span v-html="activity.message"></span>
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

        <!-- TODO 퍼블리싱 : 화면 너비 사이즈 줄어들때 태그 ui 변경 필요 -->
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
    <div ref="scrollTrigger" class="w-full h-[1px] mt-px"></div>
    <Loading
      id="loader"
      :use-loader-overlay="true"
      class="loader-lg is-loader-inner"
      style="display: none"
    ></Loading>
  </ul>
  <div v-else class="no-result">
    <div class="notification">
      <svg-icon class="notification-icon" name="info"></svg-icon>
      <p class="notification-detail">등록된 정보가 없습니다.</p>
    </div>
  </div>
</template>
<script setup lang="ts">
import Loading from "@base/loading/Loading.vue";
import { useUserStore } from "@/store/user/userStore";
import { useGlossaryStore } from "@/store/glossary";
import type { Activity } from "@/type/glossary";
import { useIntersectionObserver } from "@/composables/intersectionObserverHelper";
import { storeToRefs } from "pinia";

const { getGlossaryActivities, resetGlossaryActivities, activities, glossary } =
  useGlossaryStore();

const userStore = useUserStore();
const { profileFirstWord } = storeToRefs(userStore);

const headerMessage = (activity: Activity): string => {
  const { updatedBy, fieldOperation } = activity;
  const commonMsg = (prefix: string): string =>
    `<strong>${updatedBy}</strong> ${prefix} <strong>${glossary.name}</strong>`;

  switch (activity.cardStyle) {
    case "tags":
      return commonMsg(`${fieldOperation} <strong>Tags</strong> for`);
    case "description":
      return commonMsg(`${fieldOperation} <strong>Description</strong> for`);
    case "entityCreated":
      return commonMsg(`added`);
    case "entityDeleted":
      return commonMsg(`<p style="color: red;">deleted</p>`);
    case "domain":
      return commonMsg("posted on");
    case "default":
      return commonMsg("posted on");
    default:
      return "";
  }
};

resetGlossaryActivities();
await getGlossaryActivities();

const { scrollTrigger } = useIntersectionObserver(getGlossaryActivities);
</script>
