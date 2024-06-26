<template>
  <section class="guide-section">
    <p class="section-type">Component</p>
    <h2 class="section-title">Notification</h2>
    <p class="section-summary">
      노티피케이션은 중요한 알림을 빠르고 한눈에 보이게 하여 원활한 작업을 도와주는 요소입니다. 페이지 레이아웃에 포함되어 있어 사용자를 방해하지 않습니다.
    </p>
    <article class="mb-10">
      <h3 class="section-subtitle">Variants</h3>
      <div class="flex flex-col">
        <div class="flex gap-8 mb-14">
          <figure class="section-figure items-center">
            <div class="h-group gap-4">
              <div class="notification">
                <svg-icon class="notification-icon" name="info"></svg-icon>
                <p class="notification-detail">얼럿 메세지를 입력해주세요.</p>
              </div>
              <div class="notification notification-sm">
                <svg-icon class="notification-icon" name="info"></svg-icon>
                <p class="notification-detail">얼럿 메세지를 입력해주세요.</p>
              </div>
            </div>
            <div class="h-group gap-4">
              <div class="notification notification-info">
                <svg-icon class="notification-icon" name="info"></svg-icon>
                <p class="notification-detail">얼럿 메세지를 입력해주세요.</p>
              </div>
              <div class="notification notification-sm notification-info">
                <svg-icon class="notification-icon" name="info"></svg-icon>
                <p class="notification-detail">얼럿 메세지를 입력해주세요.</p>
              </div>
            </div>
            <div class="h-group gap-4">
              <div class="notification notification-warning">
                <svg-icon class="notification-icon" name="warning"></svg-icon>
                <p class="notification-detail">얼럿 메세지를 입력해주세요.</p>
              </div>
              <div class="notification notification-sm notification-warning">
                <svg-icon class="notification-icon" name="warning"></svg-icon>
                <p class="notification-detail">얼럿 메세지를 입력해주세요.</p>
              </div>
            </div>
            <div class="h-group gap-4">
              <div class="notification notification-success">
                <svg-icon class="notification-icon" name="success"></svg-icon>
                <p class="notification-detail">얼럿 메세지를 입력해주세요.</p>
              </div>
              <div class="notification notification-sm notification-success">
                <svg-icon class="notification-icon" name="success"></svg-icon>
                <p class="notification-detail">얼럿 메세지를 입력해주세요.</p>
              </div>
            </div>
            <div class="h-group gap-4">
              <div class="notification notification-error">
                <svg-icon class="notification-icon" name="error"></svg-icon>
                <p class="notification-detail">얼럿 메세지를 입력해주세요.</p>
              </div>
              <div class="notification notification-sm notification-error">
                <svg-icon class="notification-icon" name="error"></svg-icon>
                <p class="notification-detail">얼럿 메세지를 입력해주세요.</p>
              </div>
            </div>
          </figure>
          <div class="group-description">
            <h4 class="group-title">Message</h4>
            <p class="group-summary">
              주로 폼안에 입력 서식 밑으로 출력되며 입력 방법, 오류, 피드백등의 내용이 올 수 있습니다.
            </p>
          </div>
        </div>
        <div class="flex gap-8 mb-14">
          <figure class="section-figure items-center">
            <div class="h-group gap-4">
              <Notification></Notification>
              <Notification class="notification-box-normal"></Notification>
            </div>
            <div class="h-group gap-4">
              <Notification :notification-type="'info'"></Notification>
              <Notification class="notification-box-info"></Notification>
            </div>
            <div class="h-group gap-4">
              <Notification :notification-type="'warning'"></Notification>
              <Notification class="notification-box-warning"></Notification>
            </div>
            <div class="h-group gap-4">
              <Notification :notification-type="'success'"></Notification>
              <Notification class="notification-box-success"></Notification>
            </div>
            <div class="h-group gap-4">
              <Notification :notification-type="'error'"></Notification>
              <Notification class="notification-box-error"></Notification>
            </div>
          </figure>
          <div class="group-description">
            <h4 class="group-title">Box (Default)</h4>
            <p class="group-summary">
              주요 버튼(Primary Buttons)은 페이지 키 액션에 사용하는 Button으로 한 페이지에 한 번만 사용합니다.
            </p>
          </div>
        </div>
        <div class="flex gap-8 mb-14">
          <figure class="section-figure items-center">
            <div class="h-group gap-4">
              <Notification :isExtends="true"></Notification>
              <Notification :isExtends="true" class="notification-box-normal"></Notification>
            </div>
            <div class="h-group gap-4">
              <Notification :isExtends="true" :notification-type="'info'"></Notification>
              <Notification :isExtends="true" class="notification-box-info"></Notification>
            </div>
            <div class="h-group gap-4">
              <Notification :isExtends="true" :notification-type="'warning'"></Notification>
              <Notification :isExtends="true" class="notification-box-warning"></Notification>
            </div>
            <div class="h-group gap-4">
              <Notification :isExtends="true" :notification-type="'success'"></Notification>
              <Notification :isExtends="true" class="notification-box-success"></Notification>
            </div>
            <div class="h-group gap-4">
              <Notification :isExtends="true" :notification-type="'error'"></Notification>
              <Notification :isExtends="true" class="notification-box-error"></Notification>
            </div>
          </figure>
          <div class="group-description">
            <h4 class="group-title">Box Extends</h4>
            <p class="group-summary">
              주요 버튼(Primary Buttons)은 페이지 키 액션에 사용하는 Button으로 한 페이지에 한 번만 사용합니다.
            </p>
          </div>
        </div>
      </div>
    </article>
  </section>
</template>

<script>
export default {
  name: "guide-Notification"
};
</script>

<style scoped>

</style>