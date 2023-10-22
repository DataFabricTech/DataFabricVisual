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
            <th scope="row">Kebab-menu</th>
          </tr>
          <tr>
            <td>
              <KebabMenu></KebabMenu>
            </td>
          </tr>
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
            <th scope="row">Toggle token (Multi Check)</th>
          </tr>
          <tr>
            <td>
              <div class="v-group gap-5">
                <div class="h-group">
                  <strong class="w-[150px] font-light text-[14px] shrink-0">size: lg</strong>
                  <div class="v-group">
                    <div class="toggle-token toggle-token-lg">
                      <BaseCheckbox name="toggleLg" id="toggle-lg-01" checked>
                        오늘
                      </BaseCheckbox>
                      <BaseCheckbox name="toggleLg" id="toggle-lg-02">
                        내일
                      </BaseCheckbox>
                      <BaseCheckbox name="toggleLg" id="toggle-lg-03">
                        일주일
                      </BaseCheckbox>
                      <BaseCheckbox name="toggleLg" id="toggle-lg-04">
                        일개월
                      </BaseCheckbox>
                    </div>
                    <div class="toggle-token toggle-token-lg toggle-token-icon">
                      <BaseCheckbox name="toggleLgI" id="toggle-lg-ic-01" checked>
                        <svg-icon class="svg-icon" name="data"></svg-icon>
                      </BaseCheckbox>
                      <BaseCheckbox name="toggleLgI" id="toggle-lg-ic-02">
                        <svg-icon class="svg-icon" name="date"></svg-icon>
                      </BaseCheckbox>
                      <BaseCheckbox name="toggleLgI" id="toggle-lg-ic-03">
                        <svg-icon class="svg-icon" name="bookmark"></svg-icon>
                      </BaseCheckbox>
                      <BaseCheckbox name="toggleLgI" id="toggle-lg-ic-04">
                        <svg-icon class="svg-icon" name="filter"></svg-icon>
                      </BaseCheckbox>
                    </div>
                  </div>
                </div>
                <div class="h-group">
                  <strong class="w-[150px] font-light text-[14px] shrink-0">size: lg<br /> type: icon</strong>
                  <div class="v-group">
                    <div class="toggle-token">
                      <BaseCheckbox name="toggle" id="toggle-01" checked>
                        오늘
                      </BaseCheckbox>
                      <BaseCheckbox name="toggle" id="toggle-02">
                        내일
                      </BaseCheckbox>
                      <BaseCheckbox name="toggle" id="toggle-03">
                        일주일
                      </BaseCheckbox>
                      <BaseCheckbox name="toggle" id="toggle-04">
                        일개월
                      </BaseCheckbox>
                    </div>
                    <div class="toggle-token toggle-token-icon">
                      <BaseCheckbox name="toggleI" id="toggle-ic-01" checked>
                        <svg-icon class="svg-icon" name="data"></svg-icon>
                      </BaseCheckbox>
                      <BaseCheckbox name="toggleI" id="toggle-ic-02">
                        <svg-icon class="svg-icon" name="date"></svg-icon>
                      </BaseCheckbox>
                      <BaseCheckbox name="toggleI" id="toggle-ic-03">
                        <svg-icon class="svg-icon" name="bookmark"></svg-icon>
                      </BaseCheckbox>
                      <BaseCheckbox name="toggleI" id="toggle-ic-04">
                        <svg-icon class="svg-icon" name="filter"></svg-icon>
                      </BaseCheckbox>
                    </div>
                  </div>
                </div>
                <div class="h-group">
                  <strong class="w-[150px] font-light text-[14px] shrink-0">size: default</strong>
                  <div class="v-group">
                    <div class="toggle-token toggle-token-sm">
                      <BaseCheckbox name="toggleSmI" id="toggle-sm-01" checked>
                        오늘
                      </BaseCheckbox>
                      <BaseCheckbox name="toggleSmI" id="toggle-sm-02">
                        내일
                      </BaseCheckbox>
                      <BaseCheckbox name="toggleSmI" id="toggle-sm-03">
                        일주일
                      </BaseCheckbox>
                      <BaseCheckbox name="toggleSmI" id="toggle-sm-04">
                        일개월
                      </BaseCheckbox>
                    </div>
                    <div class="toggle-token toggle-token-sm toggle-token-icon">
                      <BaseCheckbox name="toggleSmI" id="toggle-sm-ic-01" checked>
                        <svg-icon class="svg-icon" name="data"></svg-icon>
                      </BaseCheckbox>
                      <BaseCheckbox name="toggleSmI" id="toggle-sm-ic-02">
                        <svg-icon class="svg-icon" name="date"></svg-icon>
                      </BaseCheckbox>
                      <BaseCheckbox name="toggleSmI" id="toggle-sm-ic-03">
                        <svg-icon class="svg-icon" name="bookmark"></svg-icon>
                      </BaseCheckbox>
                      <BaseCheckbox name="toggleSmI" id="toggle-sm-ic-04">
                        <svg-icon class="svg-icon" name="filter"></svg-icon>
                      </BaseCheckbox>
                    </div>
                  </div>
                </div>
              </div>

            </td>
          </tr>
          <tr>
            <th scope="row">Toggle</th>
          </tr>
          <tr>
            <td>
              <div class="v-group gap-5">
                <div class="h-group">
                  <strong class="w-[150px] font-light text-[14px] shrink-0">size: lg</strong>
                  <div class="v-group">
                  <div class="toggle toggle-lg">
                    <BaseRadio name="rlg" id="lg01">오늘</BaseRadio>
                    <BaseRadio name="rlg" id="lg03">일주일</BaseRadio>
                    <BaseRadio name="rlg" id="lg04" checked>1개월</BaseRadio>
                    <BaseRadio name="rlg" id="lg05">3개월</BaseRadio>
                  </div>
                    <div class="toggle toggle-lg toggle-icon">
                      <BaseRadio name="rlgI" id="lg-ic-01">
                        <svg-icon name="bookmark" class="svg-icon"></svg-icon>
                      </BaseRadio>
                      <BaseRadio name="rlgI" id="lg-ic-03">
                        <svg-icon name="bookmark" class="svg-icon"></svg-icon>
                      </BaseRadio>
                      <BaseRadio name="rlgI" id="lg-ic-04" checked>
                        <svg-icon name="bookmark" class="svg-icon"></svg-icon>
                      </BaseRadio>
                      <BaseRadio name="rlgI" id="lg-ic-05">
                        <svg-icon name="bookmark" class="svg-icon"></svg-icon>
                      </BaseRadio>
                    </div>
                  </div>
                </div>
                <div class="h-group">
                  <strong class="w-[150px] font-light text-[14px] shrink-0">size: default</strong>
                  <div class="v-group">
                    <div class="toggle">
                      <BaseRadio name="r" id="01">오늘</BaseRadio>
                      <BaseRadio name="r" id="03">일주일</BaseRadio>
                      <BaseRadio name="r" id="04" checked>1개월</BaseRadio>
                      <BaseRadio name="r" id="05">3개월</BaseRadio>
                    </div>
                    <div class="toggle toggle-icon">
                      <BaseRadio name="rI" id="ic-01">
                        <svg-icon name="bookmark" class="svg-icon"></svg-icon>
                      </BaseRadio>
                      <BaseRadio name="rI" id="ic-03">
                        <svg-icon name="bookmark" class="svg-icon"></svg-icon>
                      </BaseRadio>
                      <BaseRadio name="rI" id="ic-04" checked><svg-icon name="bookmark" class="svg-icon"></svg-icon>
                      </BaseRadio>
                      <BaseRadio name="rI" id="ic-05">
                        <svg-icon name="bookmark" class="svg-icon"></svg-icon>
                      </BaseRadio>
                    </div>
                  </div>
                </div>
                <div class="h-group">
                  <strong class="w-[150px] font-light text-[14px] shrink-0">size: sm</strong>
                  <div class="v-group">
                  <div class="toggle toggle-sm">
                    <BaseRadio name="rsm" id="sm01">오늘</BaseRadio>
                    <BaseRadio name="rsm" id="sm03">일주일</BaseRadio>
                    <BaseRadio name="rsm" id="sm04" checked>1개월</BaseRadio>
                    <BaseRadio name="rsm" id="sm05">3개월</BaseRadio>
                  </div>
                    <div class="toggle toggle-sm toggle-icon">
                      <BaseRadio name="rSmI" id="icsm-01">
                        <svg-icon name="bookmark" class="svg-icon"></svg-icon>
                      </BaseRadio>
                      <BaseRadio name="rSmI" id="icsm-03">
                        <svg-icon name="bookmark" class="svg-icon"></svg-icon>
                      </BaseRadio>
                      <BaseRadio name="rSmI" id="icsm-04" checked><svg-icon name="bookmark" class="svg-icon"></svg-icon>
                      </BaseRadio>
                      <BaseRadio name="rSmI" id="icsm-05">
                        <svg-icon name="bookmark" class="svg-icon"></svg-icon>
                      </BaseRadio>
                    </div>
                  </div>
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
