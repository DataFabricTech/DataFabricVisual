<template>
  <section class="guide-section">
    <p class="section-type">Component</p>
    <h2 class="section-title">Upload</h2>
    <p class="section-summary">
      업로드를 사용하여 서버에 업로드할 파일을 선택할 수 있습니다.
    </p>
    <article class="mb-10">
      <h3 class="section-subtitle">Upload</h3>
    </article>
    <div class="flex flex-col gap-5">
      <div class="v-group gap-3">
        <h4 class="group-title">Default</h4>
        <Upload></Upload>
      </div>
      <div class="v-group gap-3">
        <h4 class="group-title">loading</h4>
        <Upload class="upload-loading" :isLoadingVisible="true" :isLoadedVisible="false" :isDisabled="true"></Upload>
      </div>
      <div class="v-group gap-3">
        <h4 class="group-title">loaded</h4>
        <Upload class="upload-loaded" :isLoadingVisible="false" :isLoadedVisible="true"></Upload>
      </div>
    </div>
  </section>
</template>


<script>

export default {
  name: "guide-upload"
};
</script>

<style lang="scss" scoped>
/* @import "./index.scss"; */
</style>
