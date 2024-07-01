<template>
  <div class="wrap">
    <Header />
    <main id="main">
      <h2 class="hidden-text">거버넌스</h2>
      <Sidebar></Sidebar>
      <section id="section">
        <div class="section-contents">
          <ul class="card-list">
            <li class="card-item">
              <a href="" title="카테고리">
                <img src="../../../assets/images/common/ico_category.png" alt="카테고리">
                <div class="flex flex-col gap-[16px]">
                  <strong class="card-title">카테고리</strong>
                  <span class="card-content">다양한 소스에서 메타데이터를 추출하고 연결 설정할 수 있습니다.</span>
                </div>
              </a>
            </li>
            <li class="card-item">
              <a href="" title="용어사전">
                <img src="../../../assets/images/common/ico_glossary.png" alt="카테고리">
                <div class="flex flex-col gap-[16px]">
                  <strong class="card-title">용어사전</strong>
                  <span class="card-content">사용자와 팀의 관리를 효율적으로 할 수 있습니다.</span>
                </div>
              </a>
            </li>
            <li class="card-item">
              <a href="" title="분류">
                <img src="../../../assets/images/common/ico_classify.png" alt="카테고리">
                <div class="flex flex-col gap-[16px]">
                  <strong class="card-title">분류</strong>
                  <span class="card-content">다양한 소스에서 메타데이터를 추출하고 연결 설정할 수 있습니다.</span>
                </div>
              </a>
            </li>
          </ul>
        </div>
      </section>
    </main>
  </div>
</template>

<script>
import Header from "../layouts/header.vue";
import Sidebar from "../layouts/sidebar.vue";


export default {
  name: "search",
  components: { Sidebar, Header }
};
</script>

<style scoped>

</style>