<template>
  <div class="category">
    <em class="category-label" :title="getKrName(category.typeName)">{{ getKrName(category.typeName) }}</em>
    <button
      type="button"
      class="mu-btn mu-btn-delete"
      @click.stop="onClickDelete"
      v-if="!readOnly"
    >
      <i class="mu-icon-svg item-delete"></i><span>삭제</span>
    </button>
  </div>
</template>

<script setup lang="ts">
import { useUserStore } from '~/store/user';
import { useCategoryStore } from "~/store/category.ts";
import { onMounted, ref } from "vue";
import { useNuxtApp, useRouter } from "nuxt/app";
import { useEventStore  } from "~/store/event.ts";

const userStore = useUserStore();
const categoryStore = useCategoryStore();
const { $ofetch } = useNuxtApp();
const eventStore = useEventStore();

// interface
interface Props {
  item: any;
  category: any;
  readOnly: boolean;
}

const emit = defineEmits(["onClickTableBodyButtons"]);
const props = defineProps<Props>();

const onClickDelete = () => {
      // 삭제 대상 카테고리 파라미터 가공
      const params = {
        item: props.item,
        name: props.category.typeName,
      };
      
      eventStore.emit('onClickTableBodyButtons', 'category-del', params);
    };

const getKrName = (key) => {
      if (key) {
        return categoryStore.getClassificationNameKr(key);
      } else {
        return ''; // 카테고리 미분류
      }
    };    
</script>
