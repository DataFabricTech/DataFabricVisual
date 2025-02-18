<template>
  <footer>
    <div class="inner">
      <!-- footer-info -->
      <div class="footer-info">
        <address>
          <p v-for="(info, idx) in footerList" :class="idx == footerList.length - 1 ? 'copyright' : ''">{{ info.value }}</p>
        </address>
      </div>
      <!-- footer-logo -->
      <div class="footer-logo"><span>Mobigen</span></div>
    </div>
    <!-- floating-button -->
    <div class="floating-button">
      <a href="#" title="맨 위로 가기" class="mu-btn btn-go-top">상단가기</a>
    </div>
  </footer>
</template>

<script setup>
const { $ofetch } = useNuxtApp()

const footerList = ref([
  {
    name: "FOOTER_CONTENT1",
    value: "",
    description: "하단 설명1",
    type: "portal_config"
  },
  {
    name: "FOOTER_CONTENT2",
    value: "", 
    description: "하단 설명2",
    type: "portal_config"
  },
  {
    name: "FOOTER_CONTENT3",
    value: "", 
    description: "하단 설명3",
    type: "portal_config"
  }
])

onMounted(() => {
  getLayoutList()
})

// 레이아웃 관리 목록 조회
const getLayoutList = async () => {
  $ofetch(`/api/main/footerList`).then((res) => {
    if (res && res.data) {
      if (res.data.footerList && res.data.footerList.length == 3) {
        footerList.value = res.data.footerList
      }
    }
  })
}
</script>
