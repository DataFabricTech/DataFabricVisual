<template>
  <div>
    <h1>
      Dayjs
    </h1>
    {{ $dayjs('2023-01-01').utc().toString() }}
  </div>
</template>

<script setup lang="ts">
const dayjs = useDayjs();
const startDate = dayjs('2024-01-01');

console.log(startDate.isBetween(dayjs('2023-12-31'), dayjs('2024-01-02')));
</script>

<style lang="scss" scoped>

</style>
