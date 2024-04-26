import { createRouter, createWebHistory } from "vue-router";
import Button from "../pages/button/guide-button.vue";
import GuideLnb from "../pages/guide-lnb/guide-lnb.vue";

const routes = [
  { path: '/', redirect: '/button' },
  { path: "/guide-button", component: Button },
  { path: "/guide-lnb", component: GuideLnb }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

export default router;
