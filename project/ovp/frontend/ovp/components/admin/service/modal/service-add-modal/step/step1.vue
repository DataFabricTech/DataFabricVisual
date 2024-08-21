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
          <!-- TODO: 개발 : 선택한 서비스 퍼블 처리 완료 되면 아래 코드 수정 필요함. -->
          <span class="service-type-title">{{ service.label }}</span>
        </button>
      </li>
    </ul>
  </div>
  <!-- Step 01 / 끝 -->
</template>

<script setup lang="ts">
import type { ServiceAddModalProps } from "@/components/admin/service/modal/service-add-modal/ServiceAddModalProps";
import { ServiceAddModalComposition } from "~/components/admin/service/modal/service-add-modal/ServiceAddModalComposition";
import _ from "lodash";

const props = withDefaults(defineProps<ServiceAddModalProps>(), {});

const { services, serviceObj, newServiceIdSelect, setDefaultServiceId } =
  ServiceAddModalComposition(props);

// auto select first img
const firstService = _.head(services.value)!;
newServiceIdSelect(firstService);

// step1 생성될때 맨 처음 실행되는 코드로, 자동선택된 img 를 기본값으로 저장해둔다.
setDefaultServiceId(firstService.id);
</script>

<style scoped></style>
