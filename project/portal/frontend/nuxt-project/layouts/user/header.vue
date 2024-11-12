<template>
  <header class="header-sub">
    <div class="header" :class='isSitemap ? "header-overlay" : ""'>
      <div class="inner">
        <h1 class="logo">
          <NuxtLink to="/main" @click.native="onClickLogo">
            <span>데이터 카탈로그</span>
          </NuxtLink>
        </h1>
        <div class="gnb-area" v-if="user">
          <nav class="nav" :class="isSitemap ? 'hidden' : ''">
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
            <li v-if="user.authority == 'Super-Admin' || user.authority == 'Manager'">
              <a @click="goToAdminPage()" class="mu-btn mu-btn-round"><div class="link-home">ADMIN</div></a>
            </li>
            <li>
              <a @click="goToMenualPage()" class="mu-btn mu-btn-round"><div class="link-home">매뉴얼</div></a>
            </li>
            <li>
              <a @click="goToAnalysisToolPage()" class="mu-btn mu-btn-round"><div class="link-home">분석도구</div></a>
            </li>
            <li>
              <button type="button" title="My Catalog" class="mu-btn mu-btn-round mu-btn-icon-only" @click="goToMyCatalog()"><i class="mu-icon-svg user"></i></button>
            </li>
            <li>
              <button type="button" title="로그아웃" class="mu-btn mu-btn-round mu-btn-icon-only" @:click="logout"><NuxtLink href="/"><i class="mu-icon-svg logout"></i></NuxtLink></button>
            </li>
            <li>
              <button type="button" title="사이트맵" class="mu-btn mu-btn-round mu-btn-icon-only" @click="openSitemap">
                <i class="mu-icon-svg menu"></i>
              </button>
            </li>
          </ul>
        </div>
        <template v-if="isSitemap">
          <div class="sitemap">
            <div class="sitemap-header">
              <div class="inner">
                <!-- <ul class="sitemap-util">
                  <li v-if="user.authority == 'Super-Admin' || user.authority == 'Manager'"><a @click="goToAdminPage()"><i class="mu-icon-svg admin"></i> 관리자 홈으로 이동</a></li>
                  <li><a @click="goToMyCatalog()"><i class="mu-icon-svg user"></i> 마이페이지</a></li>
                  <li><a @click="logout()"><i class="mu-icon-svg logout" @click="logout()"></i> 로그아웃</a></li>
                </ul> -->
                <button type="button" title="전체메뉴 닫기" class="mu-btn mu-btn-icon-only mu-btn-close" @click="closeSitemap()">
                  <i class="mu-icon-svg close"></i>
                </button>
              </div>
            </div>
            <div class="sitemap-container">
              <div class="inner">
                <div class="sitemap-slogan">
                  <h2>BIGS<br />Data Portal</h2>
                  <p>창의적 사고와 끝없는 도전을 통해 새로운 미래를<br />창조함으로써 인류사회의 꿈을 실현하는 현대로템 데이터포털</p>
                </div>
                <div class="sitemap-nav">
                  <!-- nav -->
                  <nav class="nav">
                    <ul>
                      <li v-for="(item, index) in user.userMenu"
                        :class="tab && item.title === tab.currentPathDepth1 ? 'current' : ''"
                        :key="index">
                        <NuxtLink :to="getFirstDepthHref(item)" @click="closeSitemap()"><span>{{ item.title }}</span></NuxtLink>
                        <ul>
                          <template v-for="(subItem, subIndex) in item.child" :key="subIndex">
                            <li v-if="subItem.menu_visible" :style="getStyle(subItem)">
                              <NuxtLink :to='getSubDepthHref(subItem)' @click="closeSitemap()">
                                {{ subItem.menu_name }}
                              </NuxtLink>
                            </li>

                          </template>
                        </ul>
                      </li>
                    </ul>
                  </nav>
                  <!-- //nav -->
                </div>
              </div>
            </div>
          </div>
        </template>
      </div>
    </div>
  </header>
</template>

<script setup lang="ts">
import { useCookies } from 'vue3-cookies';
import { computed, ref ,onMounted, onUnmounted } from 'vue';
import { useUserStore } from '@/store/user';
import { useRouter } from 'vue-router';
import { useCategoryStore } from '@/store/category';

const router = useRouter()
const { cookies } = useCookies()
const userStore = useUserStore()
const user = computed(() => userStore._user);
const tab = computed(() => userStore._tab);
const activeIndex = ref(null);

const width = ref(0)
const isSitemap = ref(false)
const isLnbOpen = ref(false);

const categoryStore = useCategoryStore()

// 사이트맵 배치 조정
const getStyle = (item) => {
  return 'width: 154px;'
}

// 마우스 올린 대메뉴 지정
const addActive = (index, item) => {
  // 활용사례의 경우 active되지 않도록 설정
  if (item.title != '활용 사례') activeIndex.value = index;
}

// 마우스 올린 대메뉴 지정 해제
const removeActive = () => {
  activeIndex.value = null
}

const clearResize = (event: any): void => {
  width.value = window.innerWidth
}

onMounted(() => { window.addEventListener('resize', clearResize )})
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
    adminPath: '/admin/data/status/lookup',
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

const goToMenualPage = () => {
  router.push('/support/notice/detail?id=82')
}

const goToAnalysisToolPage = () => {
  router.push('/support/notice/detail?id=83')
}

const openSitemap = () => {
  isSitemap.value = true
}

const closeSitemap = () => {
  isSitemap.value = false
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

</script>
