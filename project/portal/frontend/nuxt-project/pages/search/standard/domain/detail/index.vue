<template>
  <div class="contents">
    <div class="inner">
      <div class="board-wrap">
        <div class="mu-heading">
          <h4>도메인 필수값</h4>
        </div>
        <div class="board-body">
          <!-- mu-formbox -->
          <table class="mu-formbox">
            <caption>
              도메인 필수값
            </caption>
            <colgroup>
              <col style="width: 200px" />
              <col />
            </colgroup>
            <tbody>
              <tr>
                <th scope="row">도메인 분류명</th>
                <td>
                  {{ formData.domain_classified_name }}
                </td>
              </tr>
              <tr>
                <th scope="row">데이터타입</th>
                <td>
                  {{ dataType.name }}
                </td>
              </tr>
              <tr v-if="dataType.sizeRequired">
                <th scope="row">데이터길이</th>
                <td>
                  {{ dataSize }}
                </td>
              </tr>
              <tr v-if="dataType.value === 'NUMERIC'">
                <th scope="row">소수점길이</th>
                <td>
                  {{ decimalPointSize }}
                </td>
              </tr>
              <tr>
                <th scope="row">도메인명</th>
                <td>
                  {{ formData.name }}
                </td>
              </tr>
              <tr>
                <th scope="row">도메인 설명</th>
                <td>
                  {{ formData.description }}
                </td>
              </tr>
              <tr>
                <th scope="row">도메인 그룹명</th>
                <td>
                  {{ domainGroup }}
                </td>
              </tr>
            </tbody>
          </table>
          <!-- //mu-formbox -->
        </div>
        <div class="mu-heading">
          <h4>부가정보</h4>
        </div>
        <div class="board-body">
          <!-- mu-formbox -->
          <table class="mu-formbox">
            <caption>
              부가정보
            </caption>
            <colgroup>
              <col style="width: 200px" />
              <col />
            </colgroup>
            <tbody>
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
                <th scope="row">단위</th>
                <td>
                  {{ formData.unit }}
                </td>
              </tr>
              <tr>
                <th scope="row">허용값</th>
                <td>
                  {{ formData.permission_value }}
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
  name: "MetaDataStandardDomainDetail",
  layout: "admin",
});

const { $ofetch } = useNuxtApp();
const router = useRouter();
const route = useRoute();

const domainGroup = ref("")

const formData = ref({
  id: undefined, // UUID
  name: '', // 도메인명
  description: '', // 도메인 설명
  domain_classified_name: '',
  group_name: '', // 도메인 그룹명
  data_type: '', // 데이터타입
  data_size: '', // 데이터길이
  decimal_point_size: '', // 소수점길이
  save_format: '', // 저장형식
  expression_format: '', // 표현형식
  unit: '', // 단위
  permission_value: '', // 허용값
  final_version_date: '', // 최종버전일
  used: 'Y', // 사용/미사용
  creator: '', // 생성자
  modifier: '', // 수정자
})

const dataTypeList = ref([
  {
    value: null,
    name: '선택',
    sign: 'NO',
    sizeRequired: false,
  },
  {
    value: 'CHAR',
    name: 'CHAR',
    sign: 'C',
    sizeRequired: true,
  },
  {
    value: 'VARCHAR',
    name: 'VARCHAR',
    sign: 'V',
    sizeRequired: true,
  },
  {
    value: 'TEXT',
    name: 'TEXT',
    sign: 'T',
    sizeRequired: false,
  },
  {
    value: 'NUMERIC',
    name: 'NUMERIC',
    sign: 'N',
    sizeRequired: true,
  },
  {
    value: 'INTEGER',
    name: 'INTEGER',
    sign: 'I',
    sizeRequired: false,
  },
  {
    value: 'DATETIME',
    name: 'DATETIME',
    sign: 'D',
    sizeRequired: false,
  },
])
// 데이터 타입
const dataType = ref({
  value: null,
  name: '선택',
  sign: 'NO',
  sizeRequired: false,
})
const dataSize = ref("") // 데이터 크기
const decimalPointSize = ref("") // DECIMAL 타입 소수점 크기

onMounted(() => {
  const id = route.query.id
  if (id && id != "") getMetaDataStandardInfo(id, "domain")
})

// 표준 데이터 정보 조회
const getMetaDataStandardInfo = async (id, select_type) => {
  await $ofetch(`/api/metadata/standard/info?id=${id}&select_type=${select_type}`).then(async (res) => {
    if (res && res.data && res.data.result) {
      formData.value = res.data.info

      dataType.value = dataTypeList.value.find((el) => el.name == formData.value.data_type)
      if (dataType.value && dataType.value.sizeRequired) {
        // 데이터 길이
        if (formData.value.data_size != '')
          dataSize.value = formData.value.data_size

        // 소수점 길이
        if (dataType.value.name === 'NUMERIC') {
          if (formData.value.decimal_point_size != '')
            decimalPointSize.value = formData.value.decimal_point_size
        }
      }
      domainGroup.value = formData.value.group_name
    } else {
      await errorAlert("조회에 실패하였습니다.")
    }
  }).catch(async (err) => {
    await errorAlert("조회에 실패하였습니다.")
  });
}

const goToList = () => {
  router.push(`/search/standard`)
}

</script>
