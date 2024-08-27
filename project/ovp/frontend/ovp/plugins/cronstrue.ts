import cronstrue from 'cronstrue';

export default defineNuxtPlugin((nuxtApp) => {
    nuxtApp.provide('cronstrue', cronstrue);
});
