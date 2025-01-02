<template>
  <div style="padding: 0; margin: 10px 0 0 0;">
    <div class="mu-tab-box-2">
      <ul class="mu-tab">
        <li :class="activeTab == index ? 'active' : ''" v-for="(tab, index) in props.tabs" :key="index">
          <a class="mu-switch" @click="onSelectTab(tab, index)" :title="activeTab == index ? '선택됨' : ''">{{ tab.key }}</a>
        </li>
      </ul>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue';

const emit = defineEmits(['onSelectTab'])
const props = defineProps(['isAdmin', 'tabs', 'activeTab'])

const activeTab = ref(0)

onMounted(() => {
  activeTab.value = props.activeTab
})

// 탭 변경
const onSelectTab = (tabObj, index) => {
  activeTab.value = index
  emit('onSelectTab', tabObj)
};

</script>

<style scoped>

</style>
