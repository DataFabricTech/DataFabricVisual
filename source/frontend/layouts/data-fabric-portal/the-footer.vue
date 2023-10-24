<template>
  <footer id="footer">
    <div class="h-group gap-14">
      <span class="footer-logo bg-gray">
        <a class="footer-logo-link" href="#">LOGO</a>
      </span>
      <div class="v-group gap-1">
        <ul class="fnb">
          <li class="fnb-item">
            <a href="#">이용약관</a>
          </li>
          <li class="fnb-item">
            <a href="#">개인정보처리방침</a>
          </li>
        </ul>
        <span class="copyright">Copyright © 2023 datafabric. All rights reserved.</span>
      </div>
    </div>
    <div class="h-group gap-3">
      <div class="sns">
        <a class="sns-link" href="#">
          <svg-icon class="svg-icon" name="twitter" />
        </a>
        <a class="sns-link" href="#">
          <svg-icon class="svg-icon" name="facebook" />
        </a>
        <a class="sns-link" href="#">
          <svg-icon class="svg-icon" name="instagram" />
        </a>
        <a class="sns-link" href="#">
          <svg-icon class="svg-icon" name="youtube" />
        </a>
      </div>
      <div class="h-group gap-1">
        <baseSelect class="select-sm w-[120px]"></baseSelect>
        <baseButton class="button-primary button-sm">이동</baseButton>
      </div>
    </div>
  </footer>
</template>

<script setup lang="ts"></script>
