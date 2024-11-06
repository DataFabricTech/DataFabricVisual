import { defineStore } from 'pinia';

export const useEventStore = defineStore('event', {
  actions: {
    events: {} as Record<string, Function[]>,

    emit(event: string, ...args: any[]) {
      if (this.events[event]) {
        this.events[event].forEach(callback => callback(...args));
      }
    },
    on(event: string, callback: Function) {
      if (!this.events[event]) {
        this.events[event] = [];
      }
      this.events[event].push(callback);
    },
    off(event: string, callback: Function) {
      if (this.events[event]) {
        this.events[event] = this.events[event].filter(cb => cb !== callback);
      }
    }
  }
});
