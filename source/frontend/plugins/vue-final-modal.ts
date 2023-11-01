import {createVfm} from 'vue-final-modal'

export default defineNuxtPlugin((nuxtApp) => {
  const vfm = createVfm() as any
  nuxtApp.vueApp.use(vfm)

  // NOTE: return provide를 해줘야 전역에서 사용가능
  // const {$vfm} = useNuxtApp();
  return {
    provide: {
      vfm
    }
  }
})
