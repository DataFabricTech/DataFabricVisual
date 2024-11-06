<template>
  <div class="mu-modal">
    <div class="mu-alert">
      <div class="mu-alert-body">
        <p class="mu-alert-msg" v-html="message"></p>
      </div>
      <div class="mu-alert-foot">
        <button type="button" class="mu-btn" @click.stop="onCancel">
          <span>취소</span>
        </button>
        <button type="button" class="mu-btn mu-btn-primary" @click.stop="onConfirm">
          <span>확인</span>
        </button>
      </div>
      <div class="mu-alert-close-btn">
        <button type="button" title="해당 알림창 닫기" class="mu-btn mu-btn-close" @click="onClose">
          <i class="mu-icon-svg close"></i><span>알림창닫기</span>
        </button>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue';

export default defineComponent({
  name: "PortalConfirm",

  props: {
    message: {
      type: String,
      required: true
    },
  },

  emits: ['close', 'confirm', 'cancel'],

  setup(props, { emit }) {
    const onCancel = () => {
      emit('cancel');
      emit('close');
    };

    const onConfirm = () => {
      emit('confirm');
      emit('close');
    };

    const onClose = () => {
      emit('close');
    };

    return {
      onCancel,
      onConfirm,
      onClose
    };
  }
});
</script>
