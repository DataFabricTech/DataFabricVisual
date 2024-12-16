<template>
  <!-- <modal :name="'dept-modal' + props.name"> -->
    <div class="mu-modal" v-if="props.toggleDeptModal">
      <div class="mu-dialog-background"></div>
      <div class="mu-dialog w500">
        <div class="mu-dialog-head">
          <p class="title">원천시스템담당부서 선택</p>
        </div>
        <div class="mu-dialog-body">
          <!-- filter-wrap -->
          <div class="filter-wrap">
            <!-- mu-search-group -->
            <div class="mu-search-group">
              <div class="mu-search-item">
                <table>
                  <caption>
                    부서 검색 게시판
                  </caption>
                  <colgroup>
                    <col style="width: 100px" />
                    <col />
                  </colgroup>
                  <tbody>
                    <tr>
                      <th scope="row">부서명</th>
                      <td>
                        <input
                          type="text"
                          placeholder="검색어를 입력하세요."
                          class="mu-input"
                          title="검색어입력"
                          v-model="word"
                          @keyup.enter="onEnterKeyUp"
                        />
                      </td>
                    </tr>
                  </tbody>
                </table>
              </div>
              <div class="mu-search-btn">
                <button
                  type="button"
                  class="mu-btn mu-btn-primary"
                  @click.prevent="onSearch"
                >
                  <i class="mu-icon-svg search"></i><span>검색</span>
                </button>
              </div>
            </div>
            <!-- //mu-search-group -->
            <div class="mu-heading">
              <h4>부서 목록</h4>
            </div>
            <div class="filter-list h400 slim-scroll">
              <p class="nodata" v-if="deptList.length === 0">
                검색결과가 존재하지 않습니다.<span>다른 검색어를 입력해주세요.</span>
              </p>
              <ul v-else>
                <li>
                  <div class="mu-radio">
                    <input
                      type="radio"
                      id=""
                      :checked="'' === selected.dept_cd"
                      @click="onClickRadio({ dept_cd: '', dept_nm: '' })"
                    />
                    <label for="" @click="onClickRadio({ dept_cd: '', dept_nm: '' })">
                      <span class="team">없음</span>
                    </label>
                  </div>
                </li>
                <li v-for="(dept, index) in deptList" :key="index">
                  <div class="mu-radio">
                    <input
                      type="radio"
                      :id="dept.dept_cd"
                      :checked="dept.dept_cd === selected.dept_cd"
                      @click="onClickRadio(dept)"
                    />
                    <label :for="dept.dept_nm" @click="onClickRadio(dept)">
                      <span class="team">{{ dept.dept_nm }}({{ dept.dept_cd }})</span>
                    </label>
                  </div>
                </li>
              </ul>
            </div>
            <!-- //filter-list  -->
            <table class="mu-formbox mt20">
              <caption>
                선택된 부서 게시판
              </caption>
              <colgroup>
                <col style="width: 120px" />
                <col />
              </colgroup>
              <tbody>
                <tr>
                  <th scope="row">선택된 부서</th>
                  <td>
                    <span v-if="selected.dept_nm == ''" class="team color-primary"
                      >없음({{ selected.dept_cd }})</span
                    >
                    <span v-else class="team color-primary"
                      >{{ selected.dept_nm }}({{ selected.dept_cd }})</span
                    >
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
          <!-- //filter-wrap -->
        </div>
        <div class="mu-dialog-foot">
          <button type="button" class="mu-btn" @click="onCancel">
            <span>취소</span>
          </button>
          <button type="button" class="mu-btn mu-btn-primary" @click="onConfirm">
            <span>확인</span>
          </button>
        </div>
        <!-- 접근성 : 내용을 읽고 나서 닫기 나중으로 빼준다  -->
        <div class="mu-dialog-close-btn">
          <button
            type="button"
            class="mu-btn mu-btn-close"
            title="해당 팝업 닫기"
            @click="onClose"
          >
            <i class="mu-icon-svg close"></i><span>팝업닫기</span>
          </button>
        </div>
      </div>
    </div>
  <!-- </modal> -->
</template>

<script setup lang="ts">
import { useNuxtApp } from "nuxt/app";
import { onMounted, ref } from 'vue';

const { $ofetch } = useNuxtApp();
const emit = defineEmits(["onCloseModal", "onSelect"]);
const props = defineProps([
  "toggleDeptModal", // 부서 선택 모달 토글 제어 flag
  "onSelect", // 부서 선택 이벤트 핸들러
  "onCloseModal", // 부서 선택 모달 닫기 이벤트 핸들러
  "name",
]);

const word = ref(""); // 검색어
const deptList = ref([]); // 부서 목록
const selected = ref({
  // 선택된 부서 정보
  dept_cd: "",
  dept_nm: "",
});

onMounted(() => onSearch());

// 부서 목록 조회
const onSearch = async () => {
  const result = await $ofetch(`/api/depts?word=${word.value}`);

  if (result.data && Array.isArray(result.data)) {
    deptList.value = result.data;
  }
};

// 라디오버튼 클릭 이벤트 핸들러
const onClickRadio = (info) => {
  selected.value = info;
};

// 선택 초기화 이벤트 핸들러
const onClear = (isOnClose) => {
  word.value = "";
  deptList.value = [];
  selected.value = { dept_cd: "" };

  // 모달 닫는 시점이 아니면 재조회
  if (!isOnClose) {
    onSearch();
  }
};

// 검색어 입력란 내 엔터키 입력 이벤트 핸들러
const onEnterKeyUp = (e) => {
  e.preventDefault();
  onSearch();
};

// 모달 닫기 이벤트 핸들러
const onClose = () => {
  emit("onCloseModal");
};

// 취소 버튼 클릭 이벤트 핸들러
const onCancel = () => {
  onClose();
};

// 확인 버튼 클릭 이벤트 핸들러
const onConfirm = () => {
  // 선택된 부서 정보 있을 경우 데이터 전달
  if (props.toggleDeptModal) {
    if ((selected.value && selected.value.dept_cd) || selected.value.dept_cd == "") {
      emit("onSelect", selected.value);
    }

    onClose();
  }
};
</script>
<style scoped>
.mu-modal {
  z-index: 101
}
</style>
