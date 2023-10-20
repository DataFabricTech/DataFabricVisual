<template>
  <div class="layout-guide">
    <div class="layout-guide-title">
      <div class="inner">
        <h1>Fabric UI Components</h1>
      </div>
    </div>
    <div class="guide-group">
      <div class="inner">
        <table class="guide-table">
          <tbody>
          <tr>
            <th scope="row">Card</th>
          </tr>
          <tr>
            <td>
              <div class="v-group gap-5">
                <div class="h-group w-full">
                  <strong class="w-[150px] font-light text-[14px] shrink-0"> size: lg <br />theme: default </strong>
                  <Card class="card-lg">
                    <template v-slot:img>
                      <svg-icon class="svg-icon" name="data"></svg-icon>
                    </template>
                  </Card>
                </div>
                <div class="h-group w-full">
                  <strong class="w-[150px] font-light text-[14px] shrink-0"> size: lg <br />theme: card-primary
                  </strong>
                  <Card class="card-primary card-lg">
                    <template v-slot:img>
                      <img src="../assets/images/sample.png" alt="">
                    </template>
                  </Card>
                </div>
                <div class="h-group w-full">
                  <strong class="w-[150px] font-light text-[14px] shrink-0"> size: lg <br />theme: card-negative
                  </strong>
                  <Card class="card-negative card-lg">
                    <template v-slot:img>
                      <svg-icon class="svg-icon" name="data"></svg-icon>
                    </template>
                  </Card>
                </div>
                <div class="h-group w-full">
                  <strong class="w-[150px] font-light text-[14px] shrink-0">theme: default</strong>
                  <Card>
                    <template v-slot:img>
                      <svg-icon class="svg-icon" name="data"></svg-icon>
                    </template>
                  </Card>
                </div>
                <div class="h-group w-full">
                  <strong class="w-[150px] font-light text-[14px] shrink-0">theme: card-primary</strong>
                  <Card class="card-primary">
                    <template v-slot:img>
                      <img src="../assets/images/sample.png" alt="">
                    </template>
                  </Card>
                </div>
                <div class="h-group w-full">
                  <strong class="w-[150px] font-light text-[14px] shrink-0">theme: card-negative</strong>
                  <Card class="card-negative">
                    <template v-slot:img>
                      <svg-icon class="svg-icon" name="data"></svg-icon>
                    </template>
                  </Card>
                </div>
              </div>
            </td>
          </tr>
          <tr>
            <th scope="row">Search filter</th>
          </tr>
          <tr>
            <td>
              <div class="v-group gap-5">
                <div class="h-group w-full">
                  <strong class="w-[150px] font-light text-[14px] shrink-0"> size: lg <br />theme: default </strong>
                  <SearchFilter></SearchFilter>
                </div>
              </div>
            </td>
          </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>
<script lang="ts">
import KebabMenu from "../components/kebab-menu.vue";

export default {
  components: { KebabMenu }
};
</script>

<style lang="scss">
@import "component-ui";
</style>
