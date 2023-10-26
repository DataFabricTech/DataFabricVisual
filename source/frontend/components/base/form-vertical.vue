<template>
  <form action="" class="form form-vertical">
    <h4 class="form-subject">세로유형 폼 타이틀</h4>
    <div class="form-list">
      <div class="form-item">
        <div class="form-label">
          <strong class="form-title">label</strong>
        </div>
        <div class="form-content">
          <p class="form-text">Sample Text</p>
        </div>
      </div>
      <div class="form-item">
        <div class="form-label">
          <strong class="form-title">label</strong>
        </div>
        <div class="form-content">
          <label for="input-ex-01" class="hidden">label</label>
          <BaseTextInput id="input-ex-01" class="w-[522px] text-input-lg" placeholder="입력해주세요."></BaseTextInput>
        </div>
      </div>
      <div class="form-item">
        <div class="form-label">
          <strong class="form-title">label</strong>
          <span class="form-label-accent">*</span>
        </div>
        <div class="form-content">
          <label for="input-ex-02" class="hidden">lable</label>
          <baseTextarea id="input-ex-02" class="w-[800px] h-48" placeholder="입력해주세요."></baseTextarea>
        </div>
      </div>
      <div class="form-item">
        <div class="form-label">
          <strong class="form-title">label</strong>
        </div>
        <div class="form-content">
          <div class="v-group gap-2">
            <div class="h-group gap-2">
              <label for="input-ex-03" class="hidden">lable</label>
              <BaseTextInput id="input-ex-03" class="w-[222px] text-input-lg" placeholder="입력해주세요."></BaseTextInput>
              <label for="input-ex-04" class="hidden">lable</label>
              <BaseTextInput id="input-ex-04" class="w-[400px] text-input-lg" placeholder="입력해주세요."></BaseTextInput>
              <baseButton class="button-icon button-ghost button-lg">
                <svg-icon name="plus" class="svg-icon" />
                <span class="hidden">추가</span>
              </baseButton>
            </div>
            <div class="h-group gap-2">
              <label for="input-ex-05" class="hidden">lable</label>
              <BaseTextInput id="input-ex-05" class="w-[222px] text-input-lg" placeholder="입력해주세요."></BaseTextInput>
              <label for="input-ex-06" class="hidden">lable</label>
              <BaseTextInput id="input-ex-06" class="w-[400px] text-input-lg" placeholder="입력해주세요."></BaseTextInput>
              <baseButton class="button-icon button-danger button-lg">
                <svg-icon name="minus" class="svg-icon" />
                <span class="hidden">삭제</span>
              </baseButton>
              <baseButton class="button-icon button-ghost button-lg">
                <svg-icon name="plus" class="svg-icon" />
                <span class="hidden">추가</span>
              </baseButton>
            </div>
          </div>
        </div>
      </div>
      <div class="form-item">
        <div class="form-label">
          <strong class="form-title">label</strong>
        </div>
        <div class="form-content">
          <label for="input-ex-07" class="hidden">lable</label>
          <BaseTextInput id="area-ex-07" class="w-[800px] text-input-lg" placeholder="입력해주세요."></BaseTextInput>
        </div>
      </div>
    </div>
  </form>
</template>
