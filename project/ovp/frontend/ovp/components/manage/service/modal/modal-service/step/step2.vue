<template>
  <!-- Step 02 / 시작 -->
  <div class="service-form" :style="props.style">
    <selected-service-info />

    <div class="form form-vertical">
      <div class="form-body">
        <div class="form-item">
          <label for="" class="form-label">
            서비스 이름
            <span class="required">*</span>
          </label>
          <div class="form-detail flex flex-col">
            <div class="search-input">
              <label class="hidden-text" for="text-input-example-4"
                >label</label
              >
              <input
                id="text-input-example-4"
                class="text-input text-input-lg"
                v-model="serviceObj.defaultInfo.serviceNm"
              />
              <button
                class="search-input-action-button button button-neutral-ghost button-sm"
                type="button"
                @click="resetInput('defaultInfo.serviceNm')"
              >
                <span class="hidden-text">지우기</span>
                <svg-icon class="button-icon" name="close"></svg-icon>
              </button>
            </div>
          </div>
        </div>
        <div class="form-item">
          <label for="" class="form-label"> 서비스 설명 </label>
          <div class="form-detail">
            <textarea
              name=""
              id=""
              cols="30"
              rows="5"
              class="textarea"
              v-model="serviceObj.defaultInfo.serviceDesc"
            >
            </textarea>
          </div>
        </div>
      </div>
    </div>
  </div>
  <!-- Step 02 / 끝 -->
</template>

<script setup lang="ts">
import type { ModalServiceProps } from "~/components/manage/service/modal/modal-service/ModalServiceProps";
import { ModalServiceComposition } from "~/components/manage/service/modal/modal-service/ModalServiceComposition";
import SelectedServiceInfo from "~/components/manage/service/modal/modal-service/part/selected-service-info.vue";

const props = withDefaults(defineProps<ModalServiceProps>(), {});
const { serviceObj, resetInput } = ModalServiceComposition(props);
</script>

<style scoped></style>
