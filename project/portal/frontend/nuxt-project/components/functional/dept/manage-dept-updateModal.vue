<template>
    <div class="mu-modal" v-if="props.visibleDeptManageModal">
    <div class="mu-dialog-background"></div>

    <div :class="['mu-dialog', 'w600']">
      <div class="mu-dialog-head">
        <p class="title">조직 정보</p>
      </div>
      <div class="mu-dialog-body">
        <table class="mu-formbox">
          <caption>조직 추가/수정</caption>
          <colgroup>
            <col style="width: 150px" />
            <col />
          </colgroup>

          <tbody>
            <tr>
              <th scope="row">조직 ID<strong class="required">필수</strong></th>
              <td>
                <input
                  type="text"
                  placeholder="조직 ID를 입력하세요."
                  class="mu-input"
                  title="조직 ID 입력"
                  name="dept_id"
                  v-model="props.modalParam.userObject.dept_id"
                  v-bind:class="[!reg_deptId_valid ? 'error' : '', 'mu-input']"
                  :disabled="props.modalParam.updatefalg"
                  oninput="this.value = this.value.replace(/[^a-zA-Z]/g, '')"
                  required
                />
                <p v-if="!reg_deptId_valid" class="complete-msg error">조직 ID를 입력해주세요.</p>
              </td>
            </tr>
            <tr>
              <th scope="row">조직명<strong class="required">필수</strong></th>
              <td>
                <input
                  type="text"
                  placeholder="조직명을 입력하세요."
                  class="mu-input"
                  title="조직명을 입력"
                  v-model="props.modalParam.userObject.dept_nm"
                  v-bind:class="[!reg_deptNm_valid ? 'error' : '', 'mu-input']"
                  @keypress.enter.prevent='onDeptGenerate()'
                  required
                />
                <button type="button" class="mu-btn" @click="onDeptGenerate" style="margin-left:10px" v-show="false">
                  <span>Generate</span>
                </button>
                <p v-if="!reg_deptNm_valid" class="complete-msg error">조직명을 입력해주세요.</p>
              </td>
            </tr>
            <tr>
              <th scope="row">조직 설명</th>
              <td>
                <input
                  type="text"
                  placeholder="조직 설명을 입력하세요."
                  class="mu-input"
                  title="조직 설명 입력"
                  name="description"
                  v-model="props.modalParam.userObject.description"
                />
              </td>
            </tr>
            <tr>
              <th scope="row">상위 조직 ID</th>
              <td>
                <input
                  type="text"
                  placeholder="상위 조직 ID를 입력하세요."
                  class="mu-input w220"
                  title="상위 조직 ID 입력"
                  name="parent_dept_nm"
                  v-model="props.modalParam.userObject.parent_nm"
                  readonly
                />
                <input
                  type="text"
                  name="parent_dept_id"
                  v-model="props.modalParam.userObject.parent_id"
                  v-show="false"
                />
                <button type="button" class="mu-btn" @click="onToggleDeptModal" style="margin-left:10px" :disabled="props.modalParam.mode === 'insert'">
                  <span>찾아보기</span>
                </button>
              </td>
            </tr>
            <tr>
              <th scope="row">Ord no</th>
              <td>
                <input
                  type="text"
                  placeholder="Ord no를 입력하세요."
                  class="mu-input"
                  title="Ord no 입력"
                  name="ord_no"
                  v-model="props.modalParam.userObject.ord_no"
                />
              </td>
            </tr>
          </tbody>
        </table>
      </div>
      <div class="mu-dialog-foot">
        <button type="button" class="mu-btn" @click="onCancel"><span>취소</span></button>
        <button type="button" class="mu-btn" @click="onRegister"><span>확인</span></button>
      </div>
      <div class="mu-dialog-close-btn">
        <button type="button" class="mu-btn mu-btn-close" title="해당 팝업 닫기" @click="onClose">
          <i class="mu-icon-svg close"></i><span>팝업닫기</span>
        </button>
      </div>
    </div>

    <select-dept-modal
      :toggle-dept-modal="toggleDeptModal"
      @onSelect="onSelectDept"
      @onCloseModal="onToggleDeptModal"
    />
  </div>
  </template>
  
  <script setup lang="ts">
  import { confirmAlert, errorAlert, successAlert } from "@functional/alert/alert-default";
  import ObjectUtils from "@utils/ObjectUtils";
  import { useNuxtApp } from "nuxt/app";
  import { computed, ref } from "vue";
  import selectDeptModal from "@functional/dept/select-department-modal";
  import { useEventStore } from "@store/event.ts";
  import { useDeptStore } from "@store/dept.ts";

  const props = defineProps(['visibleDeptManageModal', 'modalParam'])
  const { $ofetch } = useNuxtApp();
  const emit = defineEmits(["onCloseModal", "onSubmit", "onDeptDataLoad"]);
  
  const eventStore = useEventStore();
  const toggleDeptModal = ref(false);
  const deptStore = useDeptStore();

  const reg_deptId_valid: ComputedRef<boolean> = computed(() => {
    return props.modalParam.userObject.dept_id.length > 0 ? true : false;
  });

  const reg_deptNm_valid: ComputedRef<boolean> = computed(() => {
    return props.modalParam.userObject.dept_nm.length > 0 ? true : false;
  });

  // 
  const onRegister = async() => {
    if (!reg_deptId_valid.value || !reg_deptNm_valid.value) return;

    const result = await confirmAlert("저장하시겠습니까?");
    if(!result) return;

    let params = {
      dept_id: props.modalParam.userObject.dept_id,
      dept_name: props.modalParam.userObject.dept_nm,
      description: props.modalParam.userObject.description,
      parent_dept_id: props.modalParam.userObject.parent_id,
      ord_no: props.modalParam.userObject.ord_no,
    }

    $ofetch('/api/deptRegister', {
      method: 'POST',
      body: params
    })
      .then(async (res) => {
        if(res && !res.errorMessage) {
          await successAlert('저장되었습니다.');
          eventStore.emit('departmentRegistered');
          onClose();
        }
      })
  }

  // 담당부서 선택 모달 토글 제어
  const onToggleDeptModal = () => {
    toggleDeptModal.value = !toggleDeptModal.value;
  };

  // 담당부서 선택 이벤트 핸들러
  const onSelectDept = (info) => {
    props.modalParam.userObject.parent_id = info.dept_cd;
    props.modalParam.userObject.parent_nm = info.dept_nm;
  };

  // 취소 버튼 클릭 이벤트 핸들러
  const onCancel = () => {
    onClear();
    emit("onCloseModal");
  };
  
  // 닫기 이벤트 핸들러
  const onClose = () => {
    onClear();
    emit("onCloseModal");
  };
  
  //
  const onClear = () => {
  };

  //
  const onDeptGenerate = () => {
    // try{
      // props.modalParam.userObject.dept_id = deptStore.getGenerateDeptCode(props.modalParam.userObject.dept_nm);
    // }catch(e){
    // }
  }

  defineExpose({
    onClose
  })
  </script>
  