// plugins/highcharts.client.ts  (또는 .js)
import Highcharts from 'highcharts'
import HighchartsVue from 'highcharts-vue'
import { defineNuxtPlugin } from 'nuxt/app'

// 모듈들
import variablePie from 'highcharts/modules/variable-pie'
import Networkgraph from 'highcharts/modules/networkgraph'
import Treemap from 'highcharts/modules/treemap'

export default defineNuxtPlugin((nuxtApp) => {
  // 모듈 로드 (Highcharts 인스턴스에 주입)
  Treemap(Highcharts)
  Networkgraph(Highcharts)
  variablePie(Highcharts)

  // Vue 플러그인 등록
  nuxtApp.vueApp.use(HighchartsVue, { highcharts: Highcharts })
})
