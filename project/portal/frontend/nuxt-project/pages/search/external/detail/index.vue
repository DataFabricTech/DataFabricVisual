<template>
  <div>
    <!-- 제목위치가 컨텐츠 최상단에 위치해야함. -->
    <div class="inner">
      <!-- board-wrap -->
      <div class="board-wrap">
        <div class="mu-heading">
          <h4>외부 데이터 링크 정보</h4>
        </div>
        <div class="board-body">
          <!-- mu-formbox -->
          <table class="mu-formbox">
            <caption>
              외부 데이터 링크 게시판
            </caption>
            <colgroup>
              <col style="width: 180px" />
              <col />
            </colgroup>
            <tbody>
              <tr>
                <th scope="row">카테고리</th>
                <td>
                  <div class="category-group">
                  <div
                      v-for="(categories, idx1) in externalData.categories"
                      :key="idx1"
                    >
                      <div class="category">
                        <em class="category-label">{{ getDescription(categories) }}</em>
                      </div>
                    </div>
                  </div>
                </td>
              </tr>
              <tr>
                <th scope="row">제공 기관</th>
                <td>
                  <div class="category-group">
                    <div
                      v-for="(system, idx2) in externalData.systems"
                      :key="idx2"
                    >
                      <div class="category">
                        <em class="category-label" style="max-width: 1000px;" :title="system">{{ system }}</em>
                      </div>
                    </div>
                  </div>
                </td>
              </tr>
              <tr>
                <th scope="row">데이터명</th>
                <td>{{ externalData.name }}</td>
              </tr>
              <tr>
                <th scope="row">데이터 링크</th>
                <td><a :href="externalData.link" target="_blank" class="data-link" style="color: #2855ce;">{{ externalData.link }}</a></td>
              </tr>
              <tr>
                <th scope="row">데이터 설명</th>
                <td>
                  <div v-html="formattedContent(externalData.description)"></div>
                </td>
              </tr>
              <tr>
                <th scope="row">태그</th>
                <td>
                  <div class="tags-group">
                    <em class="tag tag-sm" v-for="(tag, idx1) in externalData.tags" :key="idx1">
                      <span class="category-tag"># {{ tag }}</span>
                    </em>
                  </div>
                </td>
              </tr>
              <tr>
                <th scope="row">등록일</th>
                <td>{{ yyyyMMdd(externalData.create_date) }}</td>
              </tr>
            </tbody>
          </table>
          <!-- //mu-formbox -->
          <!-- mu-button-wrap -->
          <div class="mu-button-wrap">
            <button
              type="button"
              class="mu-btn mu-btn-lg mu-btn-primary"
              @click="onClickListButton()"
            >
              <span>목록</span>
            </button>
          </div>
        </div>
      </div>
      <!-- //board-wrap -->
    </div>
  </div>
</template>

<script setup lang="ts">
import { successAlert, confirmAlert, errorAlert } from "@functional/alert/alert-default";
import { useUserStore } from "@/store/user";
import { useNuxtApp, useRoute, useRouter, useRuntimeConfig } from "nuxt/app";
import { onMounted, ref, computed, ComputedRef } from "vue";
import { useCategoryStore } from "@/store/category";

definePageMeta({
  name: "ExternalDataDetail",
  layout: "user",
});

const { $ofetch } = useNuxtApp();
const route = useRoute();
const router = useRouter();
const categoryStore = useCategoryStore()
const userStore = useUserStore()

onMounted(async () => {
  await categoryStore.fetchCategories()
  let id = route.query.id;
  getExternalData(id);
});

const externalData = ref({})


const getExternalData = async (id: number): Promise<void> => {
  try {
    await $ofetch(`/api/external/data?id=${id}`).then((res) => {
      if (res && !res.errorMessage) {
        externalData.value = res.data
      }
    });
  } catch (error) {
    console.log(error);
  }
};

// 카테고리 한글명
const getDescription = (key) => {
  return categoryStore.getClassificationNameKr(key)
}

// 데이터 설명 줄바꿈, 공백 치환
const formattedContent = (content) => {
  if (content) {
    return content
    .replace(/ /g, '&nbsp;')  // 공백을 &nbsp;로 변환
    .replace(/\n/g, '<br>');  // 줄바꿈을 <br>로 변환
  } else {
    return ""
  }
}

// 목록 버튼 클릭 이벤트 핸들러
const onClickListButton = (): void => {
  // 통합검색에서 넘어온 상환인 경우 상세페이지로 이동
  if (userStore.getFromPath == '/search/keyword') router.push("/search/keyword")
  else router.push("/search/external");
};

// 날짜 변환
const yyyyMMdd = (value) => {
  let date = new Date(value);
  let year = date.getFullYear();
  let month = String(date.getMonth() + 1).padStart(2, "0");
  let day = String(date.getDate()).padStart(2, "0");
  // let hours = String(date.getHours()).padStart(2, "0");
  // let min = String(date.getMinutes()).padStart(2, "0");
  // let sec = String(date.getSeconds()).padStart(2, "0");
  if (value == null || value == undefined) {
    return "";
  }
  // return year + "-" + month + "-" + day + " " + hours + ":" + min + ":" + sec;
  return year + "-" + month + "-" + day;
};
</script>
