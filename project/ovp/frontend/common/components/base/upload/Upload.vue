<template>
  <div class="upload">
    <div class="upload-file">
      <input
        type="file"
        class="upload-file-input"
        v-bind:accept="inputAcceptList || undefined"
        @change="handleFileChange"
      />
      <button class="button button-neutral-stroke button-lg" :disabled="isDisabled">
        <svg-icon class="button-icon" name="upload"></svg-icon>
        <span class="button-title">{{ props.uploadBtnText }}</span>
      </button>
    </div>
    <div v-if="isLoadingVisible" class="upload-status">
      <Loading class="loader-xs" :use-hidden-text="true"></Loading>
    </div>
    <div v-if="isLoadedVisible && selectedFile !== null" class="upload-status">
      <p class="upload-file-name">{{ selectedFile?.name ?? "" }}</p>
      <button class="button link-button button-sm" @click="deleteUpload">
        <span class="hidden-text">닫기</span>
        <svg-icon class="button-icon" name="close"></svg-icon>
      </button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { defineEmits, defineProps } from "vue";

const props = defineProps({
  isLoadingVisible: {
    type: Boolean,
    default: false
  },
  isLoadedVisible: {
    type: Boolean,
    default: false
  },
  isDisabled: {
    type: Boolean,
    default: false
  },
  uploadBtnText: {
    type: String,
    default: "File Upload"
  },
  allowedExtensions: {
    type: Array,
    default: () => []
  },
  useInputAccept: {
    type: Boolean,
    default: false
  }
});

const inputAcceptList = ref<string | null>(null);
if (props.useInputAccept) {
  inputAcceptList.value = props.allowedExtensions.join(",");
}

const emit = defineEmits<{
  (e: "changed", file: any): void;
}>();

const selectedFile = ref<File | null>(null);
const handleFileChange = ($event: Event) => {
  const input = $event.target as HTMLInputElement;

  if (input.files && input.files.length > 0) {
    const file = input.files[0];
    const fileExtension = file.name.split(".").pop()?.toLowerCase();

    if (fileExtension && props.allowedExtensions.includes(`.${fileExtension}`)) {
      selectedFile.value = file;
      emit("changed", selectedFile.value);
    } else {
      alert("Invalid file type. Only .crt, .pem, .key files are allowed.");
    }
  }
};

const deleteUpload = () => {
  selectedFile.value = null;
};
</script>

<style lang="scss" scoped>
/* @import "./index.scss"; */
</style>
