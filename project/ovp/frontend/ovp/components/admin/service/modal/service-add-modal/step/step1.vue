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
        <button class="service-type-button" @click="serviceImgClick(service)">
          <div class="service-type-img">
            <img :src="service.imgUrl" :alt="service.label" />
          </div>
          <!-- TODO: 개발 : 선택한 서비스 퍼블 처리 완료 되면 아래 코드 수정 필요함. -->
          <span class="service-type-title"
            >{{ serviceObj.serviceId === service.id ? "선택됨" : ""
            }}{{ service.label }}</span
          >
        </button>
      </li>
    </ul>
  </div>
  <!-- Step 01 / 끝 -->
</template>

<script setup lang="ts">
import type {
  IService,
  ServiceAddModalProps,
} from "@/components/admin/service/modal/service-add-modal/ServiceAddModalProps";
import { ServiceAddModalComposition } from "~/components/admin/service/modal/service-add-modal/ServiceAddModalComposition";
import _ from "lodash";

const props = withDefaults(defineProps<ServiceAddModalProps>(), {});
const services = ref<IService[]>([
  {
    id: "minIo",
    label: "MinIO",
    img: "storage-type_06",
    imgUrl: "",
    isDisabled: false,
  },
  {
    id: "mySql",
    label: "MySQL",
    img: "storage-type_02",
    imgUrl: "",
    isDisabled: false,
  },
  {
    id: "mariaDB",
    label: "MariaDB",
    img: "storage-type_01",
    imgUrl: "",
    isDisabled: false,
  },
  {
    id: "postgreSql",
    label: "PostgreSQL",
    img: "storage-type_03",
    imgUrl: "",
    isDisabled: false,
  },
  {
    id: "oracle",
    label: "Oracle",
    img: "storage-type_04",
    imgUrl: "",
  },
]);
const { serviceObj, serviceImgClick, setDefaultServiceId } =
  ServiceAddModalComposition(props);

// 이미지 URL 동적 셋팅
services.value.forEach(async (service: any) => {
  // @assetsPublic : nuxt.config.ts 에서 alias 처리한 문자임으로 ignore 하고 넘어감.
  // @ts-ignore
  const imgUrl: any = await import(`@assetsPublic/images/${service.img}.png`);
  service.imgUrl = imgUrl.default;
});

// auto select first img
const firstService = _.head(services.value)!;
serviceImgClick(firstService);

// step1 생성될때 맨 처음 실행되는 코드로, 자동선택된 img 를 기본값으로 저장해둔다.
setDefaultServiceId(firstService.id);
</script>

<style scoped></style>
