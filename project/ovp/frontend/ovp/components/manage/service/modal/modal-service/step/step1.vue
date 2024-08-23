<template>
  <!-- Step 01 / 시작 -->
  <div class="service-type" :style="props.style">
    <ul class="service-type-list">
      <li
        v-for="service in services"
        class="service-type-item"
        :class="{
          'is-service-type-disabled': service.isDisabled,
          'is-service-type-item-selected': serviceObj.serviceId === service.id,
        }"
      >
        <button
          class="service-type-button"
          @click="newServiceIdSelect(service)"
        >
          <div class="service-type-img">
            <img :src="service.imgUrl" :alt="service.label" />
          </div>
          <span class="service-type-title">{{ service.label }}</span>
        </button>
      </li>
    </ul>
  </div>
  <!-- Step 01 / 끝 -->
</template>

<script setup lang="ts">
import type { ModalServiceProps } from "@/components/manage/service/modal/modal-service/ModalServiceProps";
import { ModalServiceComposition } from "@/components/manage/service/modal/modal-service/ModalServiceComposition";
import _ from "lodash";

const props = withDefaults(defineProps<ModalServiceProps>(), {});

const { services, serviceObj, newServiceIdSelect, setDefaultServiceId } =
  ModalServiceComposition(props);

// auto select first img
const firstService = _.head(services.value)!;
newServiceIdSelect(firstService);

// step1 생성될때 맨 처음 실행되는 코드로, 자동선택된 img 를 기본값으로 저장해둔다.
setDefaultServiceId(firstService.id);
</script>

<style scoped></style>
