<template>
  <button @click="increment">숫자 세기 : {{ count }}</button>

  <p>책을 가지고 있다:</p>
  <span>{{ publishedBooksMessage }}</span>

  <ChildComponent
    @myEvent="onMyEventFunction"
    :title="title"
    :likes="likes"
  />

</template>

<script lang="ts">
import ChildComponent from '~/자식컴포넌트경로'
import { useCookies } from 'vue3-cookies';
import { useUserStore } from '~/store/user';

definePageMeta({
  name: "Main",
  layout: 'main' // Layout 설정, 경로는 ~/layouts/main.vue
})

export default {
  components: {
    ChildComponent,
  },

  setup() { // Vue2 생명주기의 beforeCreated, created 역할
    // 0. composable 사용
    const router = useRouter() // 라우터 (페이지 이동용)
    const testRouteFunc = () => {
      router.push("/main")
    }


    const userStore = useUserStore() // Pinia (기존 vuex 대용), 해당Store에 있는 모든 정보를 사용할 수 있음

    // getters
    const user = userStore.getUser()

    // actions ( Mutation과 Action 동일)
    userStore.setUser({ name: "test", age: 20 })


    // 1. data() 선언
    const count = ref(0) // number, string, boolean

    const author = reactive({
      name: 'John Doe',
      books: [
        'Vue 2 - Advanced Guide',
        'Vue 3 - Basic Guide',
        'Vue 4 - The Mystery'
      ]
    })

    const question = ref('')
    const answer = ref('답변입니다.')

    // 2. computed
    const publishedBooksMessage = computed(() => {
      return author.books.length > 0 ? 'Yes' : 'No'
    })

    // 3. watch
    watch(question, (newVal, oldVal) => {
      if (newQuestion.indexOf('?') > -1) {
        answer.value = '생각 중...'
        try {
          const res = await $ofetch('/api/test') // ~/plugins의 ofetch 커스텀 사용, Nuxt3에서는 axios -> ohmyfetch 사용권장
          // const { data, pending, error, refresh } = await $ofetch('/api/test') 4개 인수를 받을 수 있음
          answer.value = (await res.json()).answer === 'yes' ? '네' : '아니오'
        } catch (error) {
          answer.value = '에러! API에 연결할 수 없습니다. ' + error
        }
      }
    })

    // 4. 생명주기 훅(Lifecycle hooks)은 컴포넌트 생명주기의 여러 단계에서 호출됩니다.
    // beforeCreated, created 는 setup()에서 동일한 역할
    onBeforeMount(() => {})
    onMounted(() => {})
    onBeforeUpdate(() => {})
    onUpdated(() => {})
    onBeforeUnmount(() => {})
    onUnmounted(() => {})

    // 5. methods
    // 속성 값을 변경하고 업데이트 할 수 있는 함수.
    function increment() {
      count.value++
    }

    function onMyEventFunction() {
      console.log('자식으로부터 받은 요청 함수 실행')
      console.log('자식으로부터 받은 데이터 : ' + data)

    }

    // 사용하려면 return에 추가 필요
    return {
      count,
      increment,
      onMyEventFunction
      author,
      firstName,
      lastName,
      publishedBooksMessage,
      fullName,
      question,
      answer,
      testRouteFunc
    }
  }
}
</script>
