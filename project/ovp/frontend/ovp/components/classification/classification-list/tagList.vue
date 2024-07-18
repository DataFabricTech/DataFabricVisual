<template>
  <div class="l-top-bar">
    <button
        class="button button-secondary ml-auto"
        @click="showModalTag = true"
    >
      태그추가
    </button>
  </div>
  <table class="align-center">
    <colgroup>
      <col style="width: 25%" />
      <col style="width: 25%" />
      <col style="width: 25%" />
    </colgroup>
    <tr>
      <th>태그</th>
      <th>설명</th>
      <th>관리</th>
    </tr>
    <tr>
      <td>태그 01</td>
      <td>태그에 대한 설명입니다.</td>
      <td>
        <div class="button-group">
          <button class="button button button-secondary-stroke" @click="showModifyTag = true">
            편집
          </button>
          <button class="button button button-error-stroke">
            삭제
          </button>
        </div>
      </td>
    </tr>
    <tr>
      <td>태그 02</td>
      <td>태그에 대한 설명입니다.</td>
      <td>
        <div class="button-group">
          <button class="button button button-secondary-stroke">
            편집
          </button>
          <button class="button button button-error-stroke">
            삭제
          </button>
        </div>
      </td>
    </tr>
  </table>
  <tag-create v-if="showModalTag == true" @close-modal="closeModalTag"></tag-create>
  <tag-modify v-if="showModifyTag == true" @close-modal="closeModifyTag"></tag-modify>
</template>

<script setup lang="ts">
const showModalTag = ref(false);
const showModifyTag = ref(false);

const closeModalTag = () => {
  showModalTag.value = !showModalTag.value;
}
const closeModifyTag = () => {
  showModifyTag.value = !showModifyTag.value;
}
</script>

<style lang="scss" scoped></style>
