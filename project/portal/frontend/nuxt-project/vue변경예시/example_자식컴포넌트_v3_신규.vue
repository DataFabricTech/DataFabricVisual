<template>
  <p> 자식 컴포넌트 </p>
  <p> 부모 데이터 title : {{ title }}</p>
  <p> 부모 데이터 likes : {{ likes }}</p>
  <button @click="sendParentEvent()">부모함수 호출 요청</button>
</template>

<script>
export default {
  name: 'childComponent',
  emits: ['myEvent'], // 부모함수 이벤트 함수 이름 정의
  props: {
    title: String,
    likes: Number
  },

  setup(props, ctx) { // props를 인수로 사용
    const title = props.title
    const likes = props.likes

    function sendParentEvent() {
      const data = "Hello"
      ctx.emit('myEvent', data)
    }

    return {
      title,
      likes,
      sendParentEvent
    }
  },
}
</script>
