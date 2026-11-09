<template>
  <div class="contents">
    <div class="inner">
      <div class="board-wrap">
        <div class="mu-heading">
          <h4>용어 정보</h4>
          <div class="mu-item-group">
            <button type="button" class="mu-btn mu-btn-primary" @click="onToggleTermInfoUpdateModal">
              <span>용어 정보 수정</span>
            </button>
            <button type="button" class="mu-btn mu-btn-primary" @click="onToggleDomainUpdateModal">
              <span>도메인 수정</span>
            </button>
            <p class="txt-info">(필수<strong class="required">필수</strong>)</p>
          </div>
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
                <th scope="row">용어명<strong class="required">필수</strong></th>
                <td>
                  <input type="text" placeholder="" class="mu-input w600" style="margin-right: 5px;"
                    :title="formData.name" v-model="formData.name" maxlength="100" readonly/>
                </td>
              </tr>
              <tr>
                <th scope="row">용어 영문 약어명<strong class="required">필수</strong></th>
                <td>
                  <input type="text" placeholder="" class="mu-input w600" style="margin-right: 5px;"
                    :title="formData.english_sign" v-model="formData.english_sign" maxlength="100" readonly/>
                </td>
              </tr>
              <tr>
                <th scope="row">도메인명<strong class="required">필수</strong></th>
                <td>
                  <input type="text" placeholder="" class="mu-input w600"
                    :title="formData.domain" v-model="formData.domain" maxlength="100" readonly/>
                </td>
              </tr>
              <tr>
                <th scope="row">공통표준용어 설명</th>
                <td>
                  <textarea class="mu-area h100 w600" :title="formData.description" placeholder="" v-model="formData.description" />
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
                  <input type="text" placeholder="" class="mu-input w600"
                    :title="formData.permission_value" v-model="formData.permission_value" maxlength="100"/>
                </td>
              </tr>
              <tr>
                <th scope="row">저장형식</th>
                <td>
                  <input type="text" placeholder="" class="mu-input w600"
                    :title="formData.save_format" v-model="formData.save_format" maxlength="100"/>
                </td>
              </tr>
              <tr>
                <th scope="row">표현형식</th>
                <td>
                  <input type="text" placeholder="" class="mu-input w600"
                    :title="formData.expression_format" v-model="formData.expression_format" maxlength="100"/>
                </td>
              </tr>
              <tr>
                <th scope="row">행정표준코드명</th>
                <td>
                  <input type="text" placeholder="" class="mu-input w600"
                    :title="formData.admin_standard_code_name" v-model="formData.admin_standard_code_name" maxlength="100"/>
                </td>
              </tr>
              <tr>
                <th scope="row">소관기관명</th>
                <td>
                  <input type="text" placeholder="" class="mu-input w600"
                    :title="formData.competent_authority" v-model="formData.competent_authority" maxlength="100"/>
                </td>
              </tr>
              <tr>
                <th scope="row">이음동의어</th>
                <td>
                  <input type="text" placeholder="" class="mu-input w600"
                    :title="formData.synonym" v-model="formData.synonym" maxlength="100"/>
                </td>
              </tr>
              <tr>
                <th scope="row">최종버전일</th>
                <td>
                  <input type="text" placeholder="" class="mu-input w600"
                    :title="formData.final_version_date" v-model="formData.final_version_date" maxlength="100"/>
                </td>
              </tr>
              <tr>
                <th scope="row">사용여부</th>
                <td>
                  <div class="mu-radio" v-for="(used, idx) in usedList">
                    <input type="radio" :id="'rd_' + idx" name="used" :value="used.name"
                      v-model="formData.used"/>
                    <label :for="'rd_' + idx"><span>{{ used.description }}</span></label>
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
          <!-- //mu-formbox -->
        </div>
      </div>
      <!-- mu-button-wrap -->
      <div class="mu-button-wrap">
        <button type="button" class="mu-btn mu-btn-lg" @click="onClickCancelButton">
          <span>취소</span>
        </button>
        <button type="button" class="mu-btn mu-btn-lg mu-btn-primary" @click="onClickUpdateButton">
          <span>수정</span>
        </button>
      </div>
      <!-- //mu-button-wrap -->
    </div>
    <StandardTermInfoUpdateModal
      :showTermInfoUpdateModal="showTermInfoUpdateModal"
      :id="route.query.id"
      @reload="getMetaDataStandardInfo(route.query.id, 'term')"
      @onCloseModal="onToggleTermInfoUpdateModal"
    />
    <StandardTermDomainUpdateModal
      v-if="showDomainUpdateModal"
      :id="route.query.id"
      :formData="formData"
      @reload="getMetaDataStandardInfo(route.query.id, 'term')"
      @onCloseModal="onToggleDomainUpdateModal"
    />
  </div>
</template>

<script setup lang="ts">
import { confirmAlert, errorAlert, successAlert } from "@/components/functional/alert/alert-default";
import StandardTermDomainUpdateModal from "@/components/organisms/standard/standard-term-domain-update-modal.vue";
import StandardTermInfoUpdateModal from "@/components/organisms/standard/standard-term-info-update-modal.vue";
import { useNuxtApp, useRouter } from "nuxt/app";

definePageMeta({
  name: "AdminMetaDataStandardTermDetail",
  layout: "admin",
});

const { $ofetch } = useNuxtApp();
const route = useRoute();
const router = useRouter();

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

const showTermInfoUpdateModal = ref(false) // 용어 정보 수정 모달 활성화 여부
const showDomainUpdateModal = ref(false) // 도메인 수정 모달 활성화 여부

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
const onClickCancelButton = async () => {
  if (await confirmAlert("수정을 취소하시겠습니까?")) {
    history.go(-1)
  }
}

// 수정 버튼 클릭
const onClickUpdateButton = async () => {
  if (await confirmAlert("수정하시겠습니까?")) {
    await $ofetch(`/api/metadata/standard/updateTerm`, {
      method: 'POST',
      body: formData.value
    }).then(async (res) => {
      if (res && res.data) {
        if (res.data.result) {
          await successAlert('저장되었습니다.');
          getMetaDataStandardInfo(route.query.id, 'term');
        } else {
          errorAlert('수정에 실패하였습니다.')
        }
      } else {
        errorAlert('수정에 실패하였습니다.')
      }
    }).catch((err) => {
      errorAlert('수정에 실패하였습니다.')
    });
  }
}

// 용어 정보 수정 모달 열기
const onToggleTermInfoUpdateModal = () => {
  showTermInfoUpdateModal.value = !showTermInfoUpdateModal.value
  showDomainUpdateModal.value = false
}

// 도메인 수정 모달 열기
const onToggleDomainUpdateModal = () => {
  showTermInfoUpdateModal.value = false
  showDomainUpdateModal.value = !showDomainUpdateModal.value
}

</script>
