<template>
  <div class="table-row">
    <table>
      <caption class="caption-out">
        Description Row
      </caption>
      <tbody>
        <tr v-for="row in props.rows" :key="row">
          <th scope="row">{{ row.title }}</th>
          <td>{{ row.content }}</td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup lang="ts">
const props = defineProps({
  rows: {
    type: Array,
    default: () => [
      {
        title: "테스트",
        content: "테스트 12345"
      },
      {
        title: "테스트",
        content: "테스트 12345"
      },
      {
        title: "테스트",
        content: "테스트 12345"
      }

    ]
  }
});
</script>
