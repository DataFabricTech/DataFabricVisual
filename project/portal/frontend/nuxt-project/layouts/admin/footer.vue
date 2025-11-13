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
      <div class="footer-logo-custom">
        <img v-if="logoImageFile"
          :src="logoImageFile"
          style="width: 120px; height: 44px; background-size: cover"
          @error="setDefaultImage"
        />
      </div>
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

const logoImageFileId = ref("")
const logoImageFile = ref("")

onMounted(() => {
  getLayoutList()
  getLogoImage()
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

// 로고 이미지 파일 조회
const getLogoImage = async () => {
  await $ofetch(`/api/admin/manager/logoImageList`).then((res) => {
    if (res && res.data) {
      if (res.data.logoImageList && res.data.logoImageList.length > 0) {
        logoImageFileId.value = res.data.logoImageList[0].value
        // file_id가 있는 경우 해당 파일 조회
        if (logoImageFileId.value != "") {
          if (location.hostname === "localhost" && logoImageFileId.value != "") {
            logoImageFile.value = "http://localhost:38080/api/ckeditor/attachFile/" + logoImageFileId.value;
          } else if (logoImageFileId.value == "") {
            console.log("No logo image !!")
            logoImageFile.value = "/logo-basic.png"
          } else {
            logoImageFile.value = "/api/ckeditor/attachFile/" + logoImageFileId.value;
          }
        }
      } else {
        console.log("No logo image !!")
        logoImageFile.value = "/logo-basic.png"
      }
    }
  }).catch((err) => {
    console.error("Fail to load !!")
    logoImageFileId.value = ""
    logoImageFile.value = "/logo-basic.png"
  })
}

// 로고 이미지 경로에 파일이 없는 경우 처리
const setDefaultImage = () => {
  console.log("set default logo Image !!")
  logoImageFileId.value = ""
  logoImageFile.value = "/logo-basic.png"
}

</script>
