<!-- 저장소 관리 > 연결정보 등록 Modal -->
<template>
  <div class="v-group">
    <h3>저장소 관리 > 연결정보 등록 Step 1</h3>
    <baseButton class="button-lg button-primary" @click="onClickOpen1">모달 OPEN</baseButton>
    <modal v-model="show1" title="연결정보 등록">
      <template v-slot:body>
        <div class="modal-body w-[900px] p-6 items-center">
          <BaseStep></BaseStep>
          <div class="step-info storage-type">
            <h4 class="storage-type-title">저장소 유형 선택</h4>
            <SearchField class="search-field-lg w-[440px]"></SearchField>
            <ul class="storage-type-list">
              <li class="storage-type-item">
                <button class="storage-type-button">
                  <div class="storage-type-img">
                    <img src="../../../assets/images/storage-type_01.png" alt="mariaDB">
                  </div>
                  <span class="storage-type-item-title">MariaDB</span>
                </button>
              </li>
              <!-- item 요소 선택시 is-selected 클래스 추가 필요 -->
              <li class="storage-type-item is-selected">
                <button class="storage-type-button">
                  <div class="storage-type-img">
                    <img src="../../../assets/images/storage-type_02.png" alt="MySQL">
                  </div>
                  <span class="storage-type-item-title">MySQL</span>
                </button>
              </li>
              <!-- 필요시 is-disabled 사용가능 -->
              <li class="storage-type-item is-disabled">
                <button class="storage-type-button">
                  <div class="storage-type-img">
                    <img src="../../../assets/images/storage-type_03.png" alt="PostgreSQL">
                  </div>
                  <span class="storage-type-item-title">PostgreSQL</span>
                </button>
              </li>
              <li class="storage-type-item">
                <button class="storage-type-button">
                  <div class="storage-type-img">
                    <img src="../../../assets/images/storage-type_04.png" alt="Oracle">
                  </div>
                  <span class="storage-type-item-title">Oracle</span>
                </button>
              </li>
              <li class="storage-type-item">
                <button class="storage-type-button">
                  <div class="storage-type-img">
                    <img src="../../../assets/images/storage-type_05.png" alt="mariaDB">
                  </div>
                  <span class="storage-type-item-title">Hadoop</span>
                </button>
              </li>
              <li class="storage-type-item">
                <button class="storage-type-button">
                  <div class="storage-type-img">
                    <img src="../../../assets/images/storage-type_06.png" alt="MinIO">
                  </div>
                  <span class="storage-type-item-title">MinIO</span>
                </button>
              </li>
              <li class="storage-type-item">
                <button class="storage-type-button">
                  <div class="storage-type-img">
                    <img src="../../../assets/images/storage-type_07.png" alt="IRIS">
                  </div>
                  <span class="storage-type-item-title">IRIS</span>
                </button>
              </li>
              <li class="storage-type-item">
                <button class="storage-type-button">
                  <div class="storage-type-img">
                    <img src="../../../assets/images/storage-type_08.png" alt="MongoDB">
                  </div>
                  <span class="storage-type-item-title">MongoDB</span>
                </button>
              </li>
              <li class="storage-type-item">
                <button class="storage-type-button">
                  <div class="storage-type-img">
                    <img src="../../../assets/images/storage-type_06.png" alt="sample-01">
                  </div>
                  <span class="storage-type-item-title">sample-01</span>
                </button>
              </li>
              <li class="storage-type-item">
                <button class="storage-type-button">
                  <div class="storage-type-img">
                    <img src="../../../assets/images/storage-type_07.png" alt="sample-02">
                  </div>
                  <span class="storage-type-item-title">sample-02 타이틀이 길어질때 말줄임 처리</span>
                </button>
              </li>
              <li class="storage-type-item">
                <button class="storage-type-button">
                  <div class="storage-type-img">
                    <img src="../../../assets/images/storage-type_08.png" alt="sample-03">
                  </div>
                  <span class="storage-type-item-title">sample-03</span>
                </button>
              </li>
            </ul>
          </div>
        </div>
      </template>
      <template v-slot:foot>
        <baseButton class="button-normal button-lg">
          <span class="button-text">취소</span>
        </baseButton>
        <div class="h-group ml-auto">
          <baseButton class="button-primary button-lg">
            <span class="button-text">다음</span>
          </baseButton>
        </div>
      </template>
    </modal>

    <h3>저장소 관리 > 연결정보 등록 Step 2</h3>
    <baseButton class="button-lg button-primary" @click="onClickOpen2">모달 OPEN</baseButton>
    <modal v-model="show2" title="연결정보 등록">
      <template v-slot:body>
        <div class="modal-body w-[900px] p-6 items-center">
          <BaseStep></BaseStep>
          <div class="step-info">
            <form action="" class="form form-vertical">
              <h4 class="form-subject">기본정보 입력</h4>
              <div class="form-list w-full">
                <div class="form-item">
                  <div class="form-label">
                    <strong class="form-title">저장소 유형</strong>
                  </div>
                  <div class="form-content">
                    <p class="form-text">MySQL</p>
                  </div>
                </div>
                <div class="form-item">
                  <div class="form-label">
                    <strong class="form-title">연결정보 이름</strong>
                    <span class="form-label-accent">*</span>
                  </div>
                  <div class="form-content">
                    <label for="input-ex-01" class="hidden">연결정보 이름</label>
                    <BaseTextInput id="input-ex-01" class="w-[522px] text-input-lg" placeholder="이름을 입력해주세요."></BaseTextInput>
                  </div>
                </div>
                <div class="form-item w-full">
                  <div class="form-label">
                    <strong class="form-title">연결정보 설명</strong>
                  </div>
                  <div class="form-content w-full">
                    <label for="input-ex-02" class="hidden">연결정보 설명</label>
                    <baseTextarea id="input-ex-02" class="w-full h-48" placeholder="내용을 입력해주세요."></baseTextarea>
                  </div>
                </div>
              </div>
              <h4 class="form-subject">사용자 추가 정보 입력</h4>
              <div class="form-list">
                <div class="form-item">
                  <div class="form-content">
                    <div class="v-group gap-2">
                      <div class="h-group gap-2">
                        <label for="input-ex-03" class="hidden">lable</label>
                        <BaseTextInput id="input-ex-03" class="w-[222px] text-input-lg" placeholder="Key를 입력해주세요."></BaseTextInput>
                        <label for="input-ex-04" class="hidden">lable</label>
                        <BaseTextInput id="input-ex-04" class="w-[480px] text-input-lg" placeholder="Value를 입력해주세요."></BaseTextInput>
                        <baseButton class="button-icon button-ghost button-lg">
                          <svg-icon name="plus" class="svg-icon" />
                          <span class="hidden">추가</span>
                        </baseButton>
                      </div>
                      <div class="h-group gap-2">
                        <label for="input-ex-05" class="hidden">lable</label>
                        <BaseTextInput id="input-ex-05" class="w-[222px] text-input-lg" placeholder="Key를 입력해주세요."></BaseTextInput>
                        <label for="input-ex-06" class="hidden">lable</label>
                        <BaseTextInput id="input-ex-06" class="w-[480px] text-input-lg" placeholder="Value를 입력해주세요."></BaseTextInput>
                        <baseButton class="button-icon button-danger button-lg">
                          <svg-icon name="minus" class="svg-icon" />
                          <span class="hidden">삭제</span>
                        </baseButton>
                        <baseButton class="button-icon button-ghost button-lg">
                          <svg-icon name="plus" class="svg-icon" />
                          <span class="hidden">추가</span>
                        </baseButton>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
              <h4 class="form-subject">Tag 입력</h4>
              <div class="form-list w-full">
                <div class="form-item w-full">
                    <div class="form-content w-full">
                      <label for="input-ex-07" class="hidden">Tag 입력</label>
                      <BaseTextInput id="area-ex-07" class="w-full text-input-lg" placeholder="Tag를 입력해주세요."></BaseTextInput>
                    </div>
                </div>
              </div>
            </form>
          </div>
        </div>
      </template>
      <template v-slot:foot>
        <baseButton class="button-normal button-lg">
          <span class="button-text">취소</span>
        </baseButton>
        <div class="h-group ml-auto">
          <baseButton class="button-ghost button-lg">
            <span class="button-text">이전</span>
          </baseButton>
          <baseButton class="button-primary button-lg">
            <span class="button-text">다음</span>
          </baseButton>
        </div>
      </template>
    </modal>
  </div>
</template>
<script lang="ts" setup>
import {ref} from "@vue/reactivity";
import {useRouter} from "vue-router";

definePageMeta({
  layout: "default-tab-layout",
});

const show1 = ref(false);
const show2 = ref(false);
const router = useRouter();

function onClickOpen1() {
  show1.value = !show1.value;
}

function onClickOpen2() {
  show2.value = !show2.value;
}

function onClickMain() {
  router.push("/main");
}

</script>
