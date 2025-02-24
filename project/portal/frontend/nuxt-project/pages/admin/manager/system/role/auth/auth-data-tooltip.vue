<template>
  <div
    class="mu-tooltip arrow-ct"
    v-show="props.tooltipObj.display"
    :style="tooltipLocation"
  >
    <div class="mu-tooltip-inner">
      <!-- 테이블 내에서 카테고리 더보기 버튼 클릭 시 -->
      <div class="category-group slim-scroll" v-if="props.tooltipObj.display && props.tooltipObj.type === 'more'">
        <auth-Group-label
          v-for="(auth, cIndex) in authIds"
          :auth="auth"
          :item="props.selectedAuth"
          :key="cIndex"
        />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import ObjectUtils from "~/utils/ObjectUtils";
import AuthGroupLabel from "@/pages/admin/manager/system/role/auth/auth-label";

import { computed } from 'vue';

const props = defineProps({
  tooltipObj: Object,
  selectedAuth: Object,
  pageName: String,
  onClickMenu: Function
});

const emit = defineEmits(['onClickMenu']);

// 카테고리 관리 툴팁을 생성할 카테고리 좌표 계산
const tooltipLocation = computed(() => {
  if (props.tooltipObj.display) {
    const ADJUST_HEIGHT = 292;
    const EXTRA_ADJUST = 10;
    

    const offsetY = ObjectUtils.getPropertyValue(props.tooltipObj, 'e/offsetY', 0);
    const pageY = ObjectUtils.getPropertyValue(props.tooltipObj, 'e/pageY', 0);

    const guid = ObjectUtils.getPropertyValue(props.tooltipObj, 'targets/guid', '');
    const targetBtn = document.querySelector(`#setup_${guid}`); // 사용자가 클릭한 설정버튼

    if (props.tooltipObj.type === 'more') {
      // 더보기 툴팁
      let top = Math.ceil(pageY / 10) * 10 - ADJUST_HEIGHT - (EXTRA_ADJUST * 2);

      if(props.pageName === 'dataAuth') {
        top = top - 10;
      }

      // 툴팁 left 위치 설정
      const targetBtnWidth = targetBtn?.clientWidth || 0;
      const textWidth = targetBtn?.previousElementSibling?.clientWidth || 0;
      const tooltip_left = targetBtnWidth + textWidth + 2;

      return `top: ${top}px; right: ${tooltip_left}px`;
    }
  }
  return '';
});

// 더보기 툴팁 렌더 시 툴팁 내 생성할 카테고리 라벨 계산
const authIds = computed(() => {  
  const tempAuths = JSON.parse(JSON.stringify(props.selectedAuth));
  return tempAuths.length > 5 ? tempAuths.splice(5) : tempAuths;
});

// 카테고리 설정 툴팁 내 추가/삭제/수정 메뉴 클릭 이벤트 핸들러
const onClick = (type: string) => {
  emit('onClickMenu', type);
};

</script>
