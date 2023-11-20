<template>
  <div class="table-col">
    <table>
      <caption class="caption-out">
        Description Column
      </caption>
      <thead>
        <tr>
          <th v-for="header in props.headers" :key="header" scope="col">{{ header }}</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="rows in props.rows" :key="rows">
          <td v-for="col in rows">{{ col }}</td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup lang="ts">
const props = defineProps({
  headers: {
    type: Array,
    default: () => ["Head-title-1", "Head-title-2", "Head-title-3"]
  },
  rows: {
    type: Array,
    default: () => [
      [1, 2, 3],
      [4, 5, 6],
      [7, 8, 9],
      [10, 11, 12],
    ]
  }
});
</script>
