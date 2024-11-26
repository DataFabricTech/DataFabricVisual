<template>
  <div>
    <div class="inner">
      <!-- board-wrap -->
      <div class="board-wrap">
        <div class="mu-heading">
          <h4>외부 데이터 링크 정보</h4>
          <div class="mu-item-group">
            <p class="txt-info">(필수<strong class="required">필수</strong>)</p>
          </div>
        </div>
        <div class="board-body">
          <!-- mu-formbox -->
          <table class="mu-formbox">
            <caption>
              외부 데이터 링크 정보
            </caption>
            <colgroup>
              <col style="width: 190px" />
              <col />
            </colgroup>
            <tbody>
              <tr>
                <th scope="row">카테고리 </th>
                <td>
                  <div class="btn-category">
                    <div v-for="(cat, idx) in categories.content" class="mu-checkbox">
                      <input type="checkbox" :name="cat.name" :id="'ckb_'+cat.name" :value="cat.name" @change="onChangeCheckBox(cat, $event);" :checked="cat.checked">
                      <label :for="'ckb_'+cat.name"><span>{{ cat.description }}</span></label>
                    </div>
                  </div>
                </td>
              </tr>
              <tr>
                <th scope="row">제공 기관 </th>
                <td>
                  <!-- <div class="btn-category">
                    <div v-for="(sys, idx) in systems.content" class="mu-checkbox">
                      <input type="checkbox" :name="sys.name" :id="'ckb_'+sys.name" :value="sys.name" @change="onChangeCheckBox(sys, $event);" :checked="sys.checked">
                      <label :for="'ckb_'+sys.name"><span>{{ sys.description }}</span></label>
                    </div>
                  </div> -->
                  <input
                    type="text"
                    v-model="system"
                    placeholder="제공 기관을 입력해 주세요."
                    v-bind:class="['mu-input']"
                    title="데이터명 입력"
                    maxlength="50"
                  />
                </td>
              </tr>
              <tr>
                <th scope="row">데이터명 <strong class="required">필수</strong></th>
                <td>
                  <input
                    type="text"
                    v-model="dataName"
                    placeholder="데이터명을 입력해주세요(최대 50자)"
                    v-bind:class="[!dataNameValid ? 'error' : '', 'mu-input']"
                    title="데이터명 입력"
                    maxlength="50"
                  />
                  <p v-if="!dataNameValid" class="complete-msg error">내용을 입력해주세요.</p>
                </td>
              </tr>
              <tr>
                <th scope="row">데이터 링크 <strong class="required">필수</strong></th>
                <td>
                  <input
                    type="text"
                    v-model="dataLink"
                    placeholder="Link를 입력해주세요(최대 50자)"
                    v-bind:class="[!dataLinkValid ? 'error' : '', 'mu-input']"
                    title="Link 입력"
                    maxlength="50"
                  />
                  <p v-if="!dataLinkValid" class="complete-msg error">내용을 입력해주세요.</p>
                </td>
              </tr>
              <tr>
                <th scope="row">데이터 설명 <strong class="required">필수</strong></th>
                <td>
                  <textarea
                    id="textarea01"
                    v-model="dataDescription"
                    placeholder="내용을 입력하세요"
                    v-bind:class="[!dataDescriptionValid ? 'error' : '', 'mu-area']"
                    title="공지사항 내용 입력"
                    rows="10"
                  ></textarea>
                  <p v-if="!dataDescriptionValid" class="complete-msg error">
                    내용을 입력해주세요.
                  </p>
                </td>
              </tr>
              <tr>
                <th scope="row">태그</th>
                <td>
                  <!-- tag-box : 태그/키워드 관리 -->
                  <tag-register-area :isRegister="true" :allTagList="tags"
                    @onClickAddTag="onClickAddTag"
                    @onClickDeleteTag="onClickDeleteTag"
                    @reload="getExternalInfo()"
                  />
                </td>
              </tr>
              <tr>
                <th scope="row">등록일</th>
                <td>
                  <date-picker
                    style="width: 200px;"
                    @change="onSelectDate($event)"
                    v-model:date="selectDateText"
                    v-model="selectDate"
                    :type="datetimeType"
                    :time-picker-options="timePickerOptions"
                    :lang="lang"
                    :clearable="false"
                    placeholder="등록일"
                  ></date-picker>
                </td>
              </tr>
            </tbody>
          </table>
          <!-- //mu-formbox -->
          <!-- mu-button-wrap -->
          <div class="mu-button-wrap">
            <button
              type="button"
              class="mu-btn mu-btn-lg"
              @click="onClickRegisterButton()"
            >
              <span>등록</span></button
            >
            <button
              type="button"
              class="mu-btn mu-btn-lg"
              @click="onClickCancelButton()"
            >
              <span>취소</span></button
            >
          </div>
        </div>
      </div>
      <!-- //board-wrap -->
    </div>
  </div>
</template>

<script setup lang="ts">
import { successAlert, confirmAlert, errorAlert } from "@functional/alert/alert-default";
import DatePicker from "@functional/datepicker/date-picker";
import TagRegisterArea from "@molecules/external/tag-register-area";
import { useNuxtApp, useRoute, useRouter, useRuntimeConfig } from "nuxt/app";
import { onMounted, ref, computed, ComputedRef } from "vue";
import { useCategoryStore } from "@/store/category";

definePageMeta({
  name: "AdminExternalDataRegister",
  layout: "admin",
});

const { $ofetch, $yyyyMMdd } = useNuxtApp();
const route = useRoute();
const router = useRouter();
const categoryStore = useCategoryStore()

const required_flag = ref(false)
const categories = ref([])
const selectedCategories = ref([])
const systems = ref([])
const selectedSystems = ref([])
const system = ref("")
const dataName = ref("")
const dataLink = ref("")
const dataDescription = ref("")
const tags = ref([])
const selectedTags = ref([])
const selectDate = ref(0)
const selectDateText = ref("")

const datetimeType = ref<string>("datetime");

interface TimeOptions {
  start: string;
  step: string;
  end: string;
  format: string;
  timePickerIncrement?: number;
}

const timePickerOptions = ref<TimeOptions>({
  start: "00:00",
  step: "00:30",
  end: "23:30",
  format: "hh:mm",
  timePickerIncrement: 30,
});

interface Lang {
  yearFormat: string;
  days: Array<string>;
  months: Array<string>;
  pickers: Array<string>;
  placeholder: {
    date: string;
    dateRange: string;
  }
}

const lang = ref<Lang>({
  yearFormat: "YYYY년",
  days: ["일", "월", "화", "수", "목", "금", "토"],
  months: [
    "1월",
    "2월",
    "3월",
    "4월",
    "5월",
    "6월",
    "7월",
    "8월",
    "9월",
    "10월",
    "11월",
    "12월",
  ],
  pickers: ["next 7 days", "next 30 days", "previous 7 days", "previous 30 days"],
  placeholder: {
    date: "Select Date",
    dateRange: "Select Date Range",
  },
});

const dataNameValid: ComputedRef<boolean> = computed(() => {
  if (!required_flag.value) return true;
  else return dataName.value.length > 0 ? true : false;
});

const dataLinkValid: ComputedRef<boolean> = computed(() => {
  if (!required_flag.value) return true;
  else return dataLink.value.length > 0 ? true : false;
});

const dataDescriptionValid: ComputedRef<boolean> = computed(() => {
  if (!required_flag.value) return true;
  else return dataDescription.value.length > 0 ? true : false;
});

onMounted(async () => {
  await categoryStore.fetchCategories()
  await getExternalInfo();
});

// 등록할 때 사용할 카테고리, 시스템, 태그 정보 조회
const getExternalInfo = async (): Promise<void> => {
  try {
    await $ofetch(`/api/external/externalInfo`).then((res) => {
      if (res && !res.errorMessage) {
        categories.value = res.data.categories
        systems.value = res.data.systems
        tags.value = res.data.tags
        // 등록일 기본값 설정
        selectDate.value = new Date().getTime()
        selectDateText.value = $yyyyMMdd(selectDate.value)
      }
    });
  } catch (error) {
    console.log(error);
  }
};

// 카테고리, 시스템 클릭 이벤트
const onChangeCheckBox = (key, event: any) => {
  const checked = event.target.checked
  key.checked = checked

  // 체크
  if (checked) {
    if (key && key.root == 'CAT') { // 카테고리
      const tmpArray = Object.assign(selectedCategories.value)
      tmpArray.push(key.name)
      selectedCategories.value = [...new Set(tmpArray)] // 중복제거
    }

    // if (key && key.root == 'SYS') { // 시스템
    //   // 중복제거
    //   const tmpArray = Object.assign(selectedSystems.value)
    //   tmpArray.push(key.name)
    //   selectedSystems.value = [...new Set(tmpArray)] // 중복제거
    // }

  } else { // 체크 해제
    if (key && key.root == 'CAT') {
      // 체크 해제
      const tmpArray = selectedCategories.value.filter((c_name) => c_name != key.name)
      selectedCategories.value = tmpArray
    }

    // if (key && key.root == 'SYS') {
    //   // 체크 해제
    //   const tmpArray = selectedSystems.value.filter((s_name) => s_name != key.name)
    //   selectedSystems.value = tmpArray
    // }
  }
}

const onClickAddTag = async (list) => {
  selectedTags.value = list
}

const onClickDeleteTag = async (list) => {
  selectedTags.value = list
}

const onSelectDate = (event) => {
  selectDate.value = new Date(event).getTime()
  selectDateText.value = $yyyyMMdd(event)
}

// 카테고리 한글명
const getDescription = (key) => {
  return categoryStore.getClassificationNameKr(key)
}

// 입력 확인
const checkInput = async () => {
  required_flag.value = true
  // 데이터명
  if (!dataName.value) {
    return false
  }

  // 데이터 링크
  if (!dataLink.value) {
    return false
  }

  // 데이터 설명
  if (!dataDescription.value) {
    return false
  }

  return true
}


// 등록 버튼 클릭 이벤트 핸들러
const onClickRegisterButton = async () => {
  const check = await checkInput();
  if (!check) return

  if (system.value) {
    selectedSystems.value.push(system.value)
  }

  const params = {
    categories: selectedCategories.value,
    systems: selectedSystems.value,
    name: dataName.value,
    link: dataLink.value,
    description: dataDescription.value,
    tags: selectedTags.value,
    create_date: selectDateText.value ? selectDateText.value : $yyyyMMdd(new Date()) + ' 00:00'
  }

  if (await confirmAlert("등록하시겠습니까?")) {
    await $ofetch(`/api/external/register`, {
      method: 'POST',
      body: params
    }).then((res) => {
      if (res && !res.errorMessage) {
        successAlert("등록되었습니다.")
        router.push("/admin/data/meta-data/external")
      } else {
        errorAlert("등록에 실패하였습니다.")
      }
    }).catch((e) => {
      errorAlert("등록에 실패하였습니다.")
    })
  }
}

// 취소 버튼 클릭 이벤트 핸들러
const onClickCancelButton = async (): void => {
  if (await confirmAlert("작성을 취소하시겠습니까?")) {
    router.push("/admin/data/meta-data/external");
  }
};


</script>
