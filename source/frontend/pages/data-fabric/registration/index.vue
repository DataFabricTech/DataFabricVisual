<!-- 저장소 관리 > 연결정보 등록 Modal -->
<template>
  <div class="v-group">
    <h3>저장소 관리 > 연결정보 등록 Step 1</h3>
    <baseButton class="button-lg button-primary" @click="onClickOpen1">모달 OPEN</baseButton>
    <modal v-model="show1" title="연결정보 등록">
      <template v-slot:body>
        <div class="w-[900px] p-6">
          <BaseStep></BaseStep>
          <div class="storage-type">
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
        <div class="w-[900px] p-6">
          <BaseStep></BaseStep>
          <div class="storage-type">
            <h4>기본 정보 입력</h4>
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
