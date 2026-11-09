<template>
  <div class="mu-tooltip arrow-ct" v-show="props.tooltipObj.display" :style="tooltipLocation">
    <div class="mu-tooltip-inner">
      <!-- 좌측 카테고리 목록 컴포넌트 내에서 설정 버튼 클릭 시 -->
      <ul class="item-add-list" v-if="props.tooltipObj.display && props.tooltipObj.type === 'setup'">
        <li>
          <a href="javascript:void(0);" @click="() => onClick('insert')">
            하위카테고리추가
          </a>
        </li>
        <li>
          <a href="javascript:void(0);" @click="onClick('delete')">
            카테고리삭제
          </a>
        </li>
        <li>
          <a href="javascript:void(0);" @click="onClick('update')">
            카테고리수정
          </a>
        </li>
      </ul>

      <!-- 유사 속성 카테고리 설정 버튼 클릭 시 -->
      <ul class="item-add-list" v-if="props.tooltipObj.display && props.tooltipObj.type === 'similarAttrSetup'">
        <li>
          <a href="javascript:void(0);" @click="onClick('update')">
            카테고리수정
          </a>
        </li>
        <li>
          <a href="javascript:void(0);" @click="onClick('delete')">
            카테고리삭제
          </a>
        </li>
      </ul>

      <!-- 테이블 내에서 카테고리 더보기 버튼 클릭 시 -->
      <div class="category-group slim-scroll" v-if="props.tooltipObj.display && props.tooltipObj.type === 'more'">
        <category-more-label v-for="(category, cIndex) in categories" :category="category"
          :item="props.tooltipObj.targets" :key="cIndex" />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import ObjectUtils from "~/utils/ObjectUtils";
import CategoryMoreLabel from "@functional/categoryLabel/category-label";

import { computed } from 'vue';

const props = defineProps({
  tooltipObj: Object,
  selectedCategory: Object,
  onClickMenu: Function
});

const emit = defineEmits(['onClickMenu']);

// 카테고리 관리 툴팁을 생성할 카테고리 좌표 계산
const tooltipLocation = computed(() => {
  if (props.tooltipObj.display) {
    const ADJUST_HEIGHT = 300;
    const EXTRA_ADJUST = 10;

    const offsetY = ObjectUtils.getPropertyValue(props.tooltipObj, 'e/offsetY', 0);
    const pageY = ObjectUtils.getPropertyValue(props.tooltipObj, 'e/pageY', 0);

    const guid = ObjectUtils.getPropertyValue(props.tooltipObj, 'targets/guid', '')
             || ObjectUtils.getPropertyValue(props.tooltipObj, 'targets/id', '');
    const targetBtn = document.querySelector(`#setup_${guid}`); // 사용자가 클릭한 설정버튼

    if (props.tooltipObj.type === 'more') {
      // 더보기 툴팁
      const top = Math.ceil(pageY / 10) * 10 - ADJUST_HEIGHT - (EXTRA_ADJUST * 2);

      // 툴팁 left 위치 설정
      const targetBtnWidth = targetBtn?.clientWidth || 0;
      const textWidth = targetBtn?.previousElementSibling?.clientWidth || 0;
      const tooltip_left = targetBtnWidth + textWidth + 150;

      return `top: ${top}px; right: ${tooltip_left}px`;
    } else {
      // 카테고리 세팅 툴팁
      const top = pageY - offsetY - ADJUST_HEIGHT - EXTRA_ADJUST;

      // 툴팁 위치 설정
      const targetBtnWidth = targetBtn?.clientWidth || 0;
      const textWidth = targetBtn?.previousElementSibling?.clientWidth || 0;
      let tooltip_left = targetBtnWidth + textWidth;

      // depth 1단계 증가할때마다 padding-left 20px씩 더해지므로 depth에 따른 위치 보정 추가
      const depth = ObjectUtils.getPropertyValue(props.tooltipObj, 'targets/depth', 1);
      if (depth > 1) {
        tooltip_left += (depth - 1) * 20;
      }

      return `top: ${top}px; left: ${tooltip_left - 5}px`;
    }
  }
  return '';
});

// 더보기 툴팁 렌더 시 툴팁 내 생성할 카테고리 라벨 계산
const categories = computed(() => {
  const targets = ObjectUtils.getPropertyValue(props.tooltipObj, 'targets/classifications', []);
  const tempCategories = JSON.parse(JSON.stringify(targets));
  return tempCategories.length > 5 ? tempCategories.splice(5) : tempCategories;
});

// 카테고리 설정 툴팁 내 추가/삭제/수정 메뉴 클릭 이벤트 핸들러
const onClick = (type: string) => {
  emit('onClickMenu', type);
};

</script>
