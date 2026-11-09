<template>
  <p> 자식 컴포넌트 </p>
  <button @click="sendParentEvent()">부모함수 호출 요청</button>
</template>

<script>
export default {
  props: ['title', 'likes'],

  created() {
    // 부모로부터 받은 데이터 사용
    console.log(this.title)
    console.log(this.likes)
  },

  methods: {
    sendParentEvent() {
      const data = "Hello"
      this.$emit('myEvent', data)
    }
  },
}
</script>
