<template>
  <div class="contents">
    <div class="inner">
      <div class="board-wrap">
        <div class="mu-heading">
          <h4>표준단어 상세</h4>
          <div class="mu-item-group">
            <p class="txt-info">(필수)</p>
          </div>
        </div>
        <div class="board-body">
          <!-- mu-formbox -->
          <table class="mu-formbox">
            <caption>
              단어 정보
            </caption>
            <colgroup>
              <col style="width: 200px" />
              <col />
            </colgroup>
            <tbody>
              <tr>
                <th scope="row">단어명</th>
                <td>
                  {{ formData.name }}
                </td>
              </tr>
              <tr>
                <th scope="row">영문약어명</th>
                <td>
                  {{ formData.english_sign }}
                </td>
              </tr>
              <tr>
                <th scope="row">단어영문명</th>
                <td>
                  {{ formData.word_eng }}
                </td>
              </tr>
              <tr>
                <th scope="row">단어설명</th>
                <td>
                  {{ formData.description }}
                </td>
              </tr>
              <tr>
                <th scope="row">형식단어여부</th>
                <td>
                  {{ formData.form_checked }}
                </td>
              </tr>
              <tr>
                <th scope="row">도메인분류명</th>
                <td>
                  {{ formData.domain_classified_name }}
                </td>
              </tr>
              <tr>
                <th scope="row">이음동의어목록</th>
                <td>
                  {{ formData.synonym }}
                </td>
              </tr>
              <tr>
                <th scope="row">금칙어목록</th>
                <td>
                  {{ formData.forbidden }}
                </td>
              </tr>
              <tr>
                <th scope="row">최종버전일</th>
                <td>
                  {{ formData.final_version_date }}
                </td>
              </tr>
              <tr>
                <th scope="row">사용여부</th>
                <td>
                  {{ formData.used === 'Y' ? '사용' : '미사용' }}
                </td>
              </tr>
            </tbody>
          </table>
          <!-- //mu-formbox -->
        </div>
      </div>
      <!-- mu-button-wrap -->
      <div class="mu-button-wrap">
        <button type="button" class="mu-btn mu-btn-lg mu-btn-primary" @click="goToList">
          <span>목록</span>
        </button>
      </div>
      <!-- //mu-button-wrap -->
    </div>
  </div>
</template>

<script setup lang="ts">
import { errorAlert } from "@/components/functional/alert/alert-default";
import { useNuxtApp, useRouter } from "nuxt/app";

definePageMeta({
  name: "DataStandardWordDetail",
  layout: "user",
});

const { $ofetch } = useNuxtApp();
const route = useRoute();
const router = useRouter();

const formData = ref({
  id: undefined, // UUID
  name: '', // 한글명
  english_sign: '', // 영문약어명
  word_eng: '', // 단어영문명
  description: '', // 설명(공통표준용어 설명)
  form_checked: 'N', // 형식단어여부(Y, N)
  domain_classified_name: '', // 도메인 분류명
  synonym: '', // 이음동의어
  forbidden: '', // 금칙어목록
  final_version_date: '', // 최종버전일
  used: 'Y', // 사용/미사용
  created_date: '', // 생성일
  modified_date: '', // 수정일
  creator: '', // 생성자
  modifier: '', // 수정자
})

// 사용여부 라디오 버튼 목록
const usedList = ref([
  {
    name: "Y",
    description: "사용"
  },
  {
    name: "N",
    description: "미사용"
  }
])

onMounted(() => {
  const id = route.query.id
  if (id && id != "") getMetaDataStandardInfo(id, "word")
})

// 표준단어 검색목록 조회 (50개씩 검색)
const getMetaDataStandardInfo = async (id, select_type) => {
  await $ofetch(`/api/metadata/standard/info?id=${id}&select_type=${select_type}`).then(async (res) => {
    console.log(res)
    if (res && res.data && res.data.result) {
      formData.value = res.data.info
    } else {
      await errorAlert("조회에 실패하였습니다.")
    }
  }).catch(async (err) => {
    await errorAlert("조회에 실패하였습니다.")
  });
}

// 취소 버튼 클릭
const goToList = async () => {
  router.push("/search/standard")
}

</script>
