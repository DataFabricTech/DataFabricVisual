<template>
  <header class="header-sub">
    <div class="header">
      <div class="inner">
        <h1 class="logo">
          <NuxtLink to="/main" @click.native="onClickLogo">
            <span>데이터 카탈로그</span>
          </NuxtLink>
        </h1>
        <div class="gnb-area" v-if="user">
          <nav class="nav">
            <ul>
              <li v-for="(item, index) in user.adminMenu"
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
              <a @click="goToMyCatalog()"><span>{{ user.username }}</span>님</a>
            </li>
            <!-- 2022-08-16 MOD : 디자인적용span추가 -->
            <li><a @click="logout">로그아웃</a></li>
            <li><a @click="onClickLogo()" class="link-home">사용자 홈</a></li>
          </ul>
        </div>
      </div>
    </div>
  </header>
</template>

<script setup lang="ts">
import { useCookies } from 'vue3-cookies';
import { computed, ref ,onMounted, onUnmounted } from 'vue';
import { useUserStore } from '@/store/user';
import { useRouter } from 'vue-router';

const router = useRouter()
const { cookies } = useCookies()
const userStore = useUserStore()
const user = computed(() => userStore._user);
const tab = computed(() => userStore._tab);
const activeIndex = ref(null);

const width = ref(0)
const isLnbOpen = ref(false);

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
