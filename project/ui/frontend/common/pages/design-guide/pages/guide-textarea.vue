<template>
  <section class="guide-section">
    <h2 class="section-title">Textarea</h2>
    <article class="mb-10">
      <div class="flex flex-col gap-5">
        <div class="v-group gap-3 w-72">
          <h4 class="group-title">default</h4>
          <textarea class="textarea" cols="33"></textarea>
          <textarea class="textarea" placeholder="placeholder"></textarea>
          <textarea class="textarea" readonly>readonly style</textarea>
          <textarea class="textarea" disabled>disabled style</textarea>
          <textarea class="textarea is-textarea-fixed">resize none</textarea>
        </div>
      </div>
    </article>
  </section>
</template>

<script>
export default {
  name: "guide-textarea"
};
</script>

<style scoped>

</style>