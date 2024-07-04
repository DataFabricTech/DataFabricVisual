<template>
  <div class="data-detail">
    <table>
      <colgroup>
        <col style="width: 20%" />
        <col style="width: 20%" />
        <col style="width: 60%" />
      </colgroup>
      <thead>
        <tr>
          <th>NAME</th>
          <th>DATA TYPE</th>
          <th>Description</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="tr in 6" :key="tr">
          <td>요일번호</td>
          <td>integer</td>
          <td>no description</td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup lang="ts"></script>

<style lang="scss" scoped></style>
