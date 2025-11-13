<template>
  <header class="header-sub">
    <div class="header">
      <div class="inner">
        <h1 class="logo-custom">
          <NuxtLink to="/main" @click.native="onClickLogo">
              <img v-if="logoImageFile"
                :src="logoImageFile"
                style="width: 120px; height: 44px; background-size: cover"
                @error="setDefaultImage"
              />
          </NuxtLink>
        </h1>
        <div class="gnb-area" v-if="user">
          <nav class="nav">
            <ul>
              <li v-for="(item, index) in user.userMenu"
                @mouseover="addActive(index, item)" @mouseout="removeActive()"
                :class="tab && item.title === tab.currentPathDepth1 ? 'current' + (activeIndex === index ? ' active' : '') : (activeIndex === index ? ' active' : '')"
                :key="index">
                <NuxtLink @focus="isLnbOpen = true" :to="getFirstDepthHref(item)">{{ item.title }}</NuxtLink>
                <ul>
                  <li v-for="(subItem, subIndex) in item.child" :key="subIndex"
                    :class="tab && subItem.menu_path === tab.currentPath ? 'current' : ''">
                    <NuxtLink :to='getSubDepthHref(subItem)' v-if="subItem.menu_visible">
                      {{ subItem.menu_name }}
                    </NuxtLink>
                  </li>
                </ul>
              </li>
            </ul>
          </nav>
          <ul class="top-menu">
            <li>
              <a @click="goToMyCatalog()">&nbsp;&nbsp;<span>{{ user.username }}</span> 님&nbsp;</a>
            </li>
            <li>
              <li><a @click="logout">로그아웃</a></li>
            </li>
            <li v-if="user.authority == 'Super-Admin' || user.authority == 'Manager' || user.authority == 'Admin'">
              <a @click="goToAdminPage()" class="mu-btn mu-btn-round"><div class="link-home">관리자 홈</div></a>
            </li>
          </ul>
        </div>
      </div>
    </div>
  </header>
</template>

<script setup lang="ts">
import { useCategoryStore } from '@/store/category';
import { useUserStore } from '@/store/user';
import { useNuxtApp } from 'nuxt/app';
import { computed, onMounted, onUnmounted, ref } from 'vue';
import { useRouter } from 'vue-router';
import { useCookies } from 'vue3-cookies';

const { $ofetch } = useNuxtApp()
const router = useRouter()
const { cookies } = useCookies()
const userStore = useUserStore()
const user = computed(() => userStore._user);
const tab = computed(() => userStore._tab);
const activeIndex = ref(null);

const width = ref(0)
const isLnbOpen = ref(false);

const logoImageFileId = ref("")
const logoImageFile = ref("")

const categoryStore = useCategoryStore()

// 사이트맵 배치 조정
const getStyle = (item) => {
  return 'width: 154px;'
}

// 마우스 올린 대메뉴 지정
const addActive = (index, item) => {
  activeIndex.value = index;
}

// 마우스 올린 대메뉴 지정 해제
const removeActive = () => {
  activeIndex.value = null
}

const clearResize = (event: any): void => {
  width.value = window.innerWidth
}

onMounted(() => {
  window.addEventListener('resize', clearResize )
  getLogoImage()
})
onUnmounted(() => { window.removeEventListener('resize', clearResize )})

const clearStore = (): void => {
  cookies.remove('x-access-token');
  cookies.remove('x-refresh-token');

  let user = {
    username: "",
    authority: "",
    dept_nm: ""
  };
  let tab = {
    adminPath: '/admin/data/status/lookup', // 사용자 화면에서 메뉴바의 'ADMIN' 버튼 클릭 시 이동 대상 메뉴 path
    userPath: '/search/keyword',
    currentPath: '/',
    currentPathDepth1: '',
    currentPathDepth2: '',
  };

  userStore.setUser(user);
  userStore.setTab(tab)
}

const goToMyCatalog = () => {
  router.push('/my/dashboard')
}

const goToAdminPage = () => {
  router.push('/admin/data/status/lookup')
}

const logout = () => {
  clearStore()
  router.push('/')
}

const getFirstDepthHref = (menu: any): string => {
  const child = menu.child[0]
  if (child && child.menu_path)
    return child.menu_path
  else
    return '/main'
}

const getSubDepthHref = (subMenu: any): string => {
  if (subMenu && subMenu.menu_path)
    return subMenu.menu_path
  else
    return '/main'
}

const onClickLogo = (): void => {
  let tab = {
    adminPath: '/admin/data/status/lookup',
    userPath: '/search/keyword',
    currentPath: '/',
    currentPathDepth1: '',
    currentPathDepth2: '',
  };
  userStore.setTab(tab)
  router.push({ path: '/main'})
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
