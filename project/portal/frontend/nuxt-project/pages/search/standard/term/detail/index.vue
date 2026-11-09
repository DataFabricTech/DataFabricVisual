<template>
  <div class="contents">
    <div class="inner">
      <div class="board-wrap">
        <div class="mu-heading">
          <h4>용어 정보</h4>
        </div>
        <div class="board-body">
          <!-- mu-formbox -->
          <table class="mu-formbox">
            <caption>
              용어 정보
            </caption>
            <colgroup>
              <col style="width: 200px" />
              <col />
            </colgroup>
            <tbody>
              <tr>
                <th scope="row">용어명</th>
                <td>
                  {{ formData.name }}
                </td>
              </tr>
              <tr>
                <th scope="row">용어 영문 약어명</th>
                <td>
                  {{ formData.english_sign }}
                </td>
              </tr>
              <tr>
                <th scope="row">도메인명</th>
                <td>
                  {{ formData.domain }}
                </td>
              </tr>
              <tr>
                <th scope="row">공통표준용어 설명</th>
                <td>
                  {{ formData.description }}
                </td>
              </tr>
            </tbody>
          </table>
          <!-- //mu-formbox -->
        </div>
        <div class="mu-heading">
          <h4>추가 정보</h4>
        </div>
        <div class="board-body">
          <!-- mu-formbox -->
          <table class="mu-formbox">
            <caption>
              추가 정보
            </caption>
            <colgroup>
              <col style="width: 200px" />
              <col />
            </colgroup>
            <tbody>
              <tr>
                <th scope="row">허용값</th>
                <td>
                  {{ formData.permission_value }}
                </td>
              </tr>
              <tr>
                <th scope="row">저장형식</th>
                <td>
                  {{ formData.save_format }}
                </td>
              </tr>
              <tr>
                <th scope="row">표현형식</th>
                <td>
                  {{ formData.expression_format }}
                </td>
              </tr>
              <tr>
                <th scope="row">행정표준코드명</th>
                <td>
                  {{ formData.admin_standard_code_name }}
                </td>
              </tr>
              <tr>
                <th scope="row">소관기관명</th>
                <td>
                  {{ formData.competent_authority }}
                </td>
              </tr>
              <tr>
                <th scope="row">이음동의어</th>
                <td>
                  {{ formData.synonym }}
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
import { useNuxtApp } from "nuxt/app";

definePageMeta({
  name: "MetaDataStandardTermDetail",
  layout: "admin",
});

const { $ofetch } = useNuxtApp();
const route = useRoute();

const formData = ref({
  id: undefined, // UUID
  name: '', // 한글명
  english_sign: '', // 영문약어명
  english_name: '', // 영문명
  description: '', // 설명(공통표준용어 설명)
  domain: '', // 도메인명
  permission_value: '', // 허용값
  save_format: '', // 저장형식
  expression_format: '', // 표현형식
  admin_standard_code_name: '', // 행정표준코드명
  competent_authority: '', //소관기관명
  synonym: '', // 이음동의어
  final_version_date: '', // 최종버전일
  used: 'Y', // 사용/미사용
  creator: '', // 생성자
  modifier: '', // 수정자
  domain_classified_name: '', // 도메인 분류명
  data_type: '', // 데이터 타입
  data_size: '', // 데이터 크기
  decimal_point_size: '', // 소수점 길이
})

onMounted(() => {
  const id = route.query.id
  if (id && id != "") getMetaDataStandardInfo(id, "term")
})

// 표준 데이터 정보 조회
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
  history.go(-1)
}

</script>
