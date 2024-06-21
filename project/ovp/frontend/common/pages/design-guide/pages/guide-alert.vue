<template>
  <section class="guide-section">
    <p class="section-type">Component</p>
    <h2 class="section-title">Alert</h2>
    <p class="section-summary">
      얼럿은 현재 작업과 관련된 긴급 정보를 사용자에게 알립니다. 사용자의 결정에 대해 경고를 하거나 결과를 전달하는 데 사용하며 사용자가 액션을 취하기 전까지 화면에 남아 있습니다.
    </p>
    <article class="mb-10">
      <h3 class="section-subtitle">Type</h3>
      <div class="flex flex-col">
        <div class="flex gap-8 mb-14">
          <figure class="section-figure items-center">
            <div class="alert" style="width: 250px">
              <div class="alert-body">
                <div class="notification">
                  <svg-icon class="notification-icon" name="info"></svg-icon>
                  <p class="notification-detail">얼럿 메세지를 입력해주세요.</p>
                </div>
              </div>
              <div class="alert-foot">
                <div class="alert-foot-group">
                  <button class="button button-neutral-ghost button-lg">취소</button>
                  <button class="button button-primary button-lg">확인</button>
                </div>
              </div>
            </div>
          </figure>
          <div class="group-description">
            <h4 class="group-title">Normal</h4>
            <p class="group-summary">
              기본적인 상태 알림에 사용합니다.
            </p>
          </div>
        </div>
        <div class="flex gap-8 mb-14">
          <figure class="section-figure items-center">
            <div class="alert" style="width: 250px">
              <div class="alert-body">
                <div class="notification notification-info">
                  <svg-icon class="notification-icon" name="info"></svg-icon>
                  <p class="notification-detail">얼럿 메세지를 입력해주세요.</p>
                </div>
              </div>
              <div class="alert-foot">
                <div class="alert-foot-group">
                  <button class="button button-neutral-ghost button-lg">취소</button>
                  <button class="button button-primary button-lg">확인</button>
                </div>
              </div>
            </div>
          </figure>
          <div class="group-description">
            <h4 class="group-title">Info</h4>
            <p class="group-summary">
              normal타입보다 중요한 정보를 알릴 때 사용됩니다.
            </p>
          </div>
        </div>
        <div class="flex gap-8 mb-14">
          <figure class="section-figure items-center">
            <div class="alert" style="width: 250px">
              <div class="alert-body">
                <div class="notification notification-warning">
                  <svg-icon class="notification-icon" name="warning"></svg-icon>
                  <p class="notification-detail">얼럿 메세지를 입력해주세요.</p>
                </div>
              </div>
              <div class="alert-foot">
                <div class="alert-foot-group">
                  <button class="button button-neutral-ghost button-lg">취소</button>
                  <button class="button button-error button-lg">확인</button>
                </div>
              </div>
            </div>
          </figure>
          <div class="group-description">
            <h4 class="group-title">Warning</h4>
            <p class="group-summary">
              중요한 변화 또는 가벼운 경고를 나타낼 때 사용합니다.
            </p>
          </div>
        </div>
        <div class="flex gap-8 mb-14">
          <figure class="section-figure items-center">
            <div class="alert" style="width: 250px">
              <div class="alert-body">
                <div class="notification notification-error">
                  <svg-icon class="notification-icon" name="error"></svg-icon>
                  <p class="notification-detail">얼럿 메세지를 입력해주세요.</p>
                </div>
              </div>
              <div class="alert-foot">
                <div class="alert-foot-group">
                  <button class="button button-neutral-ghost button-lg">취소</button>
                  <button class="button button-error button-lg">확인</button>
                </div>
              </div>
            </div>
          </figure>
          <div class="group-description">
            <h4 class="group-title">Error</h4>
            <p class="group-summary">
              계속 진행시 위험 또는 작업이 실패했을 때 사용합니다.
            </p>
          </div>
        </div>
        <div class="flex gap-8 mb-14">
          <figure class="section-figure items-center">
            <div class="alert" style="width: 250px">
              <div class="alert-body">
                <div class="notification notification-success">
                  <svg-icon class="notification-icon" name="success"></svg-icon>
                  <p class="notification-detail">얼럿 메세지를 입력해주세요.</p>
                </div>
              </div>
              <div class="alert-foot">
                <div class="alert-foot-group">
                  <button class="button button-neutral-ghost button-lg">취소</button>
                  <button class="button button-primary button-lg">확인</button>
                </div>
              </div>
            </div>
          </figure>
          <div class="group-description">
            <h4 class="group-title">Success</h4>
            <p class="group-summary">
              사용자의 작업 성공 알림에 사용합니다.
            </p>
          </div>
        </div>
      </div>
    </article>
  </section>
</template>

<script>
export default {
  name: "guide-alert"
};
</script>

<style scoped>

</style>