<template>
  <button @click="increment">숫자 세기: {{ count }}</button>

  <p>책을 가지고 있다:</p>
  <span>{{ publishedBooksMessage }}</span>

  <ChildComponent
    @myEvent="onMyEventFunction"
    :title="title"
    :likes="likes"
  />

</template>

<script>
import ChildComponent from '/자식컴포넌트경로'
import { mapState, mapGetters, mapMutations, mapActions } from "vuex";

export default {
  layout: 'my/user', // layout 파일 경로

  // 1. data()에서 반환된 속성들은 반응적인 상태가 되어 `this`에 노출됩니다.
  data() {
    return {
      count: 0,

      author: {
        name: 'John Doe',
        books: [
          'Vue 2 - Advanced Guide',
          'Vue 3 - Basic Guide',
          'Vue 4 - The Mystery'
        ]
      },

      firstName: 'John',
      lastName: 'Doe',

      question: '',
      answer: '현재',

      title="자식컴포넌트에 전달할 제목",
      likes="자식컴포넌트에 전달할 좋아요수",
    }
  },

  // 컴포넌트
  components: {
    ChildComponent,
  },

  // 2-1. computed
  computed: {
    // 계산된 값을 반환하는 속성, data()의 값처럼 사용 가능
    publishedBooksMessage() {
      // `this`는 컴포넌트 인스턴스를 가리킵니다.
      return this.author.books.length > 0 ? 'Yes' : 'No'
    },
    // Vuex 사용 방법
    // methods 구간에서 this로 접근하여 사용 가능
    ...mapState(['state명1', 'state명2']) // this.state명1, this.state명2
    ...mapGetters(['getter명1, getter명2']) // this.getter명1, this.getter명2
    ...mapMutations({
      myMuta1: "mutation명1", // this.myMuta1(args)
      myMuta2: "mutation명2" // this.myMuta2(args)
    })
    ...mapActions(['action명1', 'action명2']) // this.action명1(args)
  }

  // 3. watch
  watch: {
    question(newVal, oldVal) {
      if (newVal.includes('?')) {
        this.getAnswer()
      }
    }
  },

  // 4. 생명주기 훅(Lifecycle hooks)은 컴포넌트 생명주기의 여러 단계에서 호출됩니다.
  beforeCreated() {},
  created() {},
  beforeMounted() {},
  mounted() {},
  updated() {},
  beforeDestroy() {},
  destroyed() {},

  // 5. methods
  methods: {
    increment() {
      this.count++
    },

    async getAnswer() {
      this.answer = 'think...'
      try {
        const res = await this.$axios.get('/api/test')
        this.answer = (res.answer) === 'yes' ? '네' : '아니오'
      } catch (error) {
        this.answer = '에러! API에 연결할 수 없습니다. ' + error
      }
    },

    onMyEventFunction(data) {
      console.log('자식으로부터 받은 요청 함수 실행')
      console.log('자식으로부터 받은 데이터 : ' + data)
    },
  },
}
</script>
