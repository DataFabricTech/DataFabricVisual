import { reactive } from 'vue';

const EventBus = reactive({
  // 이벤트를 저장할 객체
  events: {},  

  // 이벤트 등록
  on(event, callback) {
    if (!this.events[event]) {
      this.events[event] = [];
    }
    this.events[event].push(callback);
  },

  // 이벤트 발생
  emit(event, data) {
    if (this.events[event]) {
      this.events[event].forEach(callback => callback(data));
    }
  },

  // 이벤트 제거
  off(event, callback) {
    if (this.events[event]) {
      this.events[event] = this.events[event].filter(cb => cb !== callback);
    }
  }
});

export default EventBus;