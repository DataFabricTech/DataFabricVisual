<template>
  <div>
    <h1>
      Component
    </h1>
    <ul>
      <li>
        <NuxtLink to="/dayjs">Dayjs</NuxtLink>
      </li>
      <li>
        <NuxtLink to="/highcharts">HighCharts</NuxtLink>
      </li>
      <li>
        <NuxtLink to="/vue-final-modal">Vue Final Modal</NuxtLink>
      </li>
      <li>
        <NuxtLink to="/ag-grid">Ag-Grid</NuxtLink>
      </li>
      <li>
        <NuxtLink to="/vue-datepicker-next">Vue Datepicker Next</NuxtLink>
      </li>
    </ul>
  </div>
</template>

<script setup lang="ts">

</script>

<style lang="css" scoped>
ul {
  list-style: none;
  margin: 0;
  padding: 0;
  display: flex;
}

li {
  margin-right: 10px;
  padding: 0;
  border: 0;
  float: left;
  color: white;
  font-size: 30px;
}

li:hover {
  background-color: #98E0AD;
  color: black;
}
</style>
