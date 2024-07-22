<template>
  <div class="section-contents">
    <div class="l-split">
      <div class="main-content">
        <div class="l-top-bar">
          <span class="main-content-title">최근 탐색 데이터</span>
          <button class="button link-button-support">
            <span class="button-title">모두 보기</span>
          </button>
        </div>
        <!-- 최대 3개까지 표시 -->
        <template v-for="card in 3" :key="card">
          <!--  TODO: [개발] resource-box 컴포넌트 적용 -->
          <div class="resource-box">
            <div class="resource-box-function">
              <div class="resource-box-model">
                <img />
                <div class="breadcrumb">
                  <ul class="breadcrumb-list">
                    <li class="breadcrumb-item">
                      <span class="breadcrumb-text">1depth</span>
                    </li>
                    <li class="breadcrumb-item">
                      <span class="breadcrumb-text">2depth</span>
                    </li>
                    <li class="breadcrumb-item">
                      <span class="breadcrumb-text">data</span>
                    </li>
                  </ul>
                </div>
              </div>
            </div>
            <div class="resource-box-initial-name">최초 데이터 이름</div>
            <div class="editable-group">
              <a title="상세 보기" href="" class="editable-group-title"
                >세종특별자치시 상하수도요금표</a
              >
              <span class="editable-group-desc"
                >한국교통안전공단에서 교통카드를 한국교통안전공단에서
                교통카드를한국교통안전공단에서 교통카드를한국교통안전공단에서
                교통카드를한국교통안전공단에서 교통카드를한국교통안전공단에서
                교통카드를한국교통안전공단에서 교통카드를한국교통안전공단에서
                교통카드를</span
              >
            </div>
            <div class="resource-box-info">
              <dl class="resource-box-list">
                <dt>소유자</dt>
                <dd>Owner</dd>
              </dl>
              <dl class="resource-box-list">
                <dt>도메인</dt>
                <dd>Domain</dd>
              </dl>
            </div>
          </div>
        </template>
      </div>
      <div class="main-content">
        <div class="l-top-bar">
          <span class="main-content-title">북마크 한 데이터</span>
          <button class="button link-button-support">
            <span class="button-title">모두 보기</span>
          </button>
        </div>
        <template v-for="card in 2" :key="card">
          <!--  TODO: [개발] resource-box 컴포넌트 적용 -->
          <div class="resource-box">
            <div class="resource-box-function">
              <div class="resource-box-model">
                <img />
                <div class="breadcrumb">
                  <ul class="breadcrumb-list">
                    <li class="breadcrumb-item">
                      <span class="breadcrumb-text">1depth</span>
                    </li>
                    <li class="breadcrumb-item">
                      <span class="breadcrumb-text">2depth</span>
                    </li>
                    <li class="breadcrumb-item">
                      <span class="breadcrumb-text">data</span>
                    </li>
                  </ul>
                </div>
              </div>
            </div>
            <div class="resource-box-initial-name">최초 데이터 이름</div>
            <div class="editable-group">
              <a title="상세 보기" href="" class="editable-group-title"
                >세종특별자치시 상하수도요금표</a
              >
              <span class="editable-group-desc"
                >한국교통안전공단에서 교통카드를 한국교통안전공단에서
                교통카드를한국교통안전공단에서 교통카드를한국교통안전공단에서
                교통카드를한국교통안전공단에서 교통카드를한국교통안전공단에서
                교통카드를한국교통안전공단에서 교통카드를한국교통안전공단에서
                교통카드를</span
              >
            </div>
            <div class="resource-box-info">
              <dl class="resource-box-list">
                <dt>소유자</dt>
                <dd>Owner</dd>
              </dl>
              <dl class="resource-box-list">
                <dt>도메인</dt>
                <dd>Domain</dd>
              </dl>
            </div>
          </div>
        </template>
      </div>
    </div>

    <div class="l-split">
      <div class="main-content">
        <div class="l-top-bar">
          <span class="main-content-title">추천 많은 데이터</span>
          <button class="button link-button-support">
            <span class="button-title">모두 보기</span>
          </button>
        </div>
        <template v-for="card in 2" :key="card">
          <!--  TODO: [개발] resource-box 컴포넌트 적용 -->
          <div class="resource-box">
            <div class="resource-box-function">
              <div class="resource-box-model">
                <img />
                <div class="breadcrumb">
                  <ul class="breadcrumb-list">
                    <li class="breadcrumb-item">
                      <span class="breadcrumb-text">1depth</span>
                    </li>
                    <li class="breadcrumb-item">
                      <span class="breadcrumb-text">2depth</span>
                    </li>
                    <li class="breadcrumb-item">
                      <span class="breadcrumb-text">data</span>
                    </li>
                  </ul>
                </div>
              </div>
            </div>
            <div class="resource-box-initial-name">최초 데이터 이름</div>
            <div class="editable-group">
              <a title="상세 보기" href="" class="editable-group-title"
                >세종특별자치시 상하수도요금표</a
              >
              <span class="editable-group-desc"
                >한국교통안전공단에서 교통카드를 한국교통안전공단에서
                교통카드를한국교통안전공단에서 교통카드를한국교통안전공단에서
                교통카드를한국교통안전공단에서 교통카드를한국교통안전공단에서
                교통카드를한국교통안전공단에서 교통카드를한국교통안전공단에서
                교통카드를</span
              >
            </div>
            <div class="resource-box-info">
              <dl class="resource-box-list">
                <dt>소유자</dt>
                <dd>Owner</dd>
              </dl>
              <dl class="resource-box-list">
                <dt>도메인</dt>
                <dd>Domain</dd>
              </dl>
            </div>
          </div>
        </template>
      </div>
      <div class="main-content">
        <div class="l-top-bar">
          <span class="main-content-title">나의 데이터</span>
          <button class="button link-button-support">
            <span class="button-title">모두 보기</span>
          </button>
        </div>
        <!-- 결과 없을 시 no-result 표시 -->
        <div class="no-result">
          <div class="notification">
            <svg-icon class="notification-icon" name="info"></svg-icon>
            <p class="notification-detail">출력할 정보가 없습니다.</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useUserStore } from "@/store/user/userStore";
import { storeToRefs } from "pinia";
import { onMounted } from "vue";

const userStore = useUserStore();
const { getUserInfo } = userStore;
const { user } = storeToRefs(userStore);

onMounted(async () => {
  await getUserInfo();
  console.log("main에서 user", user.value);
});
</script>

<style scoped></style>
