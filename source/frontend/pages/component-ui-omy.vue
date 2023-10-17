<template>
  <div class="layout-guide">
    <div class="layout-guide-title">
      <h1>Fabric UI Components</h1>
    </div>
    <table class="guide-table">
      <tbody>
      <tr>
        <th scope="row">Button</th>
      </tr>
      <tr>
        <td>
          <div class="v-group gap-3">
            <div class="h-group">
              <strong class="w-[100px] font-light text-[14px]">size: lg</strong>
              <baseButton title="사용자 관리" class="button-primary button-lg">
                <svg-icon name="user" class="svg-icon" />
                <span class="button-text">Button</span>
              </baseButton>
              <baseButton title="사용자 관리" class="button-secondary button-lg">
                <svg-icon name="user" class="svg-icon" />
                <span class="button-text">Button</span>
              </baseButton>
              <baseButton title="사용자 관리" class="button-tertiary button-lg">
                <svg-icon name="user" class="svg-icon" />
                <span class="button-text">Button</span>
              </baseButton>
              <baseButton title="사용자 관리" class="button-negative">
                <svg-icon name="user" class="svg-icon" />
                <span class="button-text">Button</span>
              </baseButton>
              <baseButton title="사용자 관리" class="button-danger">
                <svg-icon name="user" class="svg-icon" />
                <span class="button-text">Button</span>
              </baseButton>
              <baseButton title="사용자 관리" class="button-normal">
                <svg-icon name="user" class="svg-icon" />
                <span class="button-text">Button</span>
              </baseButton>
              <baseButton title="사용자 관리" class="button-link button-sm">
                <svg-icon name="user" class="svg-icon" />
                <span class="button-text">Button</span>
              </baseButton>
              <baseButton title="사용자 관리" class="button-link-primary button-sm">
                <svg-icon name="user" class="svg-icon" />
                <span class="button-text">Button</span>
              </baseButton>
              <baseButton title="사용자 관리" class="button-link-negative button-sm">
                <svg-icon name="user" class="svg-icon" />
                <span class="button-text">Button</span>
              </baseButton>
              <baseButton title="사용자 관리" class="button-link-negative button-sm" disabled>
                <svg-icon name="user" class="svg-icon" />
                <span class="button-text">Disabled</span>
              </baseButton>
            </div>
            <div class="h-group">
              <strong class="w-[100px] font-light text-[14px]">size: md</strong>
              <baseButton class="button-primary button-lg">
                <span class="button-text">Button</span>
              </baseButton>
              <baseButton class="button-secondary button-lg">
                <span class="button-text">Button</span>
              </baseButton>
              <baseButton class="button-tertiary button-lg">
                <span class="button-text">Button</span>
              </baseButton>
              <baseButton class="button-negative">
                <span class="button-text">Button</span>
              </baseButton>
              <baseButton class="button-danger">
                <span class="button-text">Button</span>
              </baseButton>
              <baseButton class="button-normal">
                <span class="button-text">Button</span>
              </baseButton>
              <baseButton class="button-link button-sm">
                <span class="button-text">Button</span>
              </baseButton>
              <baseButton class="button-link-primary button-sm">
                <span class="button-text">Button</span>
              </baseButton>
              <baseButton class="button-link-negative button-sm">
                <span class="button-text">Button</span>
              </baseButton>
              <baseButton class="button-primary button-lg" disabled>
                <span class="button-text">Disabled</span>
              </baseButton>
            </div>
            <div class="h-group">
              <strong class="w-[100px] font-light text-[14px]">size: sm</strong>
              <baseButton title="사용자 관리" class="button-icon button-primary button-lg">
                <svg-icon name="user" class="svg-icon" />
                <span class="hidden">사용자 관리</span>
              </baseButton>
              <baseButton title="사용자 관리" class="button-icon button-secondary button-lg">
                <svg-icon name="user" class="svg-icon" />
                <span class="hidden">사용자 관리</span>
              </baseButton>
              <baseButton title="사용자 관리" class="button-icon button-tertiary button-lg">
                <svg-icon name="user" class="svg-icon" />
                <span class="hidden">사용자 관리</span>
              </baseButton>
              <baseButton title="사용자 관리" class="button-icon button-negative">
                <svg-icon name="user" class="svg-icon" />
                <span class="hidden">사용자 관리</span>
              </baseButton>
              <baseButton title="사용자 관리" class="button-icon button-danger">
                <svg-icon name="user" class="svg-icon" />
                <span class="hidden">사용자 관리</span>
              </baseButton>
              <baseButton title="사용자 관리" class="button-icon button-normal">
                <svg-icon name="user" class="svg-icon" />
                <span class="hidden">사용자 관리</span>
              </baseButton>
              <baseButton title="사용자 관리" class="button-icon button-link button-sm">
                <svg-icon name="user" class="svg-icon" />
                <span class="hidden">사용자 관리</span>
              </baseButton>
              <baseButton title="사용자 관리" class="button-icon button-link-primary button-sm">
                <svg-icon name="user" class="svg-icon" />
                <span class="hidden">사용자 관리</span>
              </baseButton>
              <baseButton title="사용자 관리" class="button-icon button-link-negative button-sm">
                <svg-icon name="user" class="svg-icon" />
                <span class="hidden">사용자 관리</span>
              </baseButton>
              <baseButton title="사용자 관리" class="button-icon button-normal" disabled>
                <svg-icon name="user" class="svg-icon" />
                <span class="hidden">사용자 관리</span>
              </baseButton>
            </div>
          </div>
        </td>
      </tr>
      <tr>
        <th scope="row">Tab</th>
      </tr>
      <tr>
        <td>
          <div class="v-group gap-3">
            <!--            Used Component-->
            <div class="h-group">
              <strong class="w-[100px] font-light text-[14px]">nav</strong>
              <BaseNav></BaseNav>
            </div>
            <div class="h-group">
              <strong class="w-[100px] font-light text-[14px]">tab</strong>
              <BaseTab></BaseTab>
            </div>
            <div class="h-group">
              <strong class="w-[100px] font-light text-[14px]">fill-tab</strong>
              <BaseTab theme="fill-tab"></BaseTab>
            </div>
            <div class="h-group">
              <strong class="w-[100px] font-light text-[14px]">round-tab</strong>
              <BaseTab theme="round-tab" use-icon="true"></BaseTab>
            </div>
            <!--            Unused Component-->

            <div class="h-group">
              <strong class="w-[100px] font-light text-[14px]">tab</strong>
              <div class="tab">
                <ul class="tab-list">
                  <li class="tab-item">
                    <button type="button" class="tab-button is-selected">
                      <span class="tab-title">기본정보</span>
                    </button>
                  </li>
                  <li class="tab-item">
                    <button type="button" class="tab-button">
                      <span class="tab-title">프로파일링</span>
                    </button>
                  </li>
                  <li class="tab-item">
                    <button type="button" class="tab-button">
                      <span class="tab-title">샘플데이터</span>
                    </button>
                  </li>
                  <li class="tab-item">
                    <button type="button" class="tab-button" disabled>
                      <span class="tab-title">Disabled</span>
                    </button>
                  </li>
                </ul>
              </div>
            </div>
            <div class="h-group">
              <strong class="w-[100px] font-light text-[14px]">fill-tab</strong>
              <div class="fill-tab">
                <ul class="fill-tab-list">
                  <li class="fill-tab-item">
                    <button type="button" class="fill-tab-button is-selected">
                      <span class="fill-tab-title">카테고리</span>
                    </button>
                  </li>
                  <li class="fill-tab-item">
                    <button type="button" class="fill-tab-button">
                      <span class="fill-tab-title">카탈로그</span>
                    </button>
                  </li>
                  <li class="fill-tab-item">
                    <button type="button" class="fill-tab-button">
                      <span class="fill-tab-title">연결정보</span>
                    </button>
                  </li>
                  <li class="fill-tab-item">
                    <button type="button" class="fill-tab-button" disabled>
                      <span class="fill-tab-title">Disabled</span>
                    </button>
                  </li>
                </ul>
              </div>
            </div>
            <div class="h-group">
              <strong class="w-[100px] font-light text-[14px]">round-tab</strong>
              <div class="round-tab">
                <ul class="round-tab-list">
                  <li class="round-tab-item">
                    <button type="button" class="round-tab-button is-selected">
                      <svg-icon name="data" class="svg-icon" />
                      <span class="round-tab-title">전체</span>
                    </button>
                  </li>
                  <li class="round-tab-item">
                    <button type="button" class="round-tab-button">
                      <svg-icon name="data" class="svg-icon" />
                      <span class="round-tab-title">데이터패브릭</span>
                    </button>
                  </li>
                  <li class="round-tab-item">
                    <button type="button" class="round-tab-button">
                      <svg-icon name="data" class="svg-icon" />
                      <span class="round-tab-title">분석보고서</span>
                    </button>
                  </li>
                  <li class="round-tab-item">
                    <button type="button" class="round-tab-button" disabled>
                      <svg-icon name="data" class="svg-icon" />
                      <span class="round-tab-title">Disabled</span>
                    </button>
                  </li>
                </ul>
              </div>
            </div>
          </div>
        </td>
      </tr>
      <tr>
        <th scope="row">Rating</th>
      </tr>
      <tr>
        <td>
          <div class="v-group gap-3">
            <BaseRating></BaseRating>
          </div>
        </td>
      </tr>
      </tbody>
    </table>
  </div>
</template>
<script lang="ts" setup></script>

<style lang="scss">
@import "component-ui";
</style>
