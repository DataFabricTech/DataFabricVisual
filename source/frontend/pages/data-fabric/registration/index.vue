<!-- 저장소 관리 > 연결정보 등록 Modal -->
<template>
  <div class="v-group">
    <h3>저장소 관리 > 연결정보 등록 Step 1</h3>
    <baseButton class="button-lg button-primary" @click="onClickOpen1">모달 OPEN</baseButton>
    <modal v-model="show1" title="연결정보 등록 Step 1">
      <template v-slot:body>
        <div class="w-[900px] p-6">
          <BaseStep></BaseStep>
          <div class="storage-type">
            <SearchField class="search-field-lg"></SearchField>
            <ul class="storage-type-list">
              <li class="storage-type-item">
                <div class="storage-type-img">
                  <img src="../../../assets/images/storage-type_01.png" alt="mariaDB">
                </div>
                <span class="storage-type-title">MariaDB</span>
              </li>
              <li class="storage-type-item is-selected">
                <div class="storage-type-img">
                  <img src="../../../assets/images/storage-type_02.png" alt="MySQL">
                </div>
                <span class="storage-type-title">MySQL</span>
              </li>
              <li class="storage-type-item is-disabled">
                <div class="storage-type-img">
                  <img src="../../../assets/images/storage-type_03.png" alt="PostgreSQL">
                </div>
                <span class="storage-type-title">PostgreSQL</span>
              </li>
              <li class="storage-type-item">
                <div class="storage-type-img">
                  <img src="../../../assets/images/storage-type_04.png" alt="Oracle">
                </div>
                <span class="storage-type-title">Oracle</span>
              </li>
              <li class="storage-type-item">
                <div class="storage-type-img">
                  <img src="../../../assets/images/storage-type_05.png" alt="mariaDB">
                </div>
                <span class="storage-type-title">Hadoop</span>
              </li>
              <li class="storage-type-item">
                <div class="storage-type-img">
                  <img src="../../../assets/images/storage-type_06.png" alt="MinIO">
                </div>
                <span class="storage-type-title">MinIO</span>
              </li>
              <li class="storage-type-item">
                <div class="storage-type-img">
                  <img src="../../../assets/images/storage-type_07.png" alt="IRIS">
                </div>
                <span class="storage-type-title">IRIS</span>
              </li>
              <li class="storage-type-item">
                <div class="storage-type-img">
                  <img src="../../../assets/images/storage-type_08.png" alt="MongoDB">
                </div>
                <span class="storage-type-title">MongoDB</span>
              </li>
              <li class="storage-type-item">
                <div class="storage-type-img">
                  <img src="../../../assets/images/storage-type_06.png" alt="sample-01">
                </div>
                <span class="storage-type-title">sample-01</span>
              </li>
              <li class="storage-type-item">
                <div class="storage-type-img">
                  <img src="../../../assets/images/storage-type_07.png" alt="sample-02">
                </div>
                <span class="storage-type-title">sample-02</span>
              </li>
              <li class="storage-type-item">
                <div class="storage-type-img">
                  <img src="../../../assets/images/storage-type_08.png" alt="sample-03">
                </div>
                <span class="storage-type-title">sample-01</span>
              </li>
            </ul>
          </div>
        </div>
      </template>
    </modal>

    <h3>저장소 관리 > 연결정보 등록 Step 2</h3>
    <baseButton class="button-lg button-primary" @click="onClickOpen2">모달 OPEN</baseButton>
    <modal v-model="show2" title="연결정보 등록 Step 2">
      <template v-slot:body>
        가나다라마바사~가나다라마바사~가나다라마바사~가나다라마바사~
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
