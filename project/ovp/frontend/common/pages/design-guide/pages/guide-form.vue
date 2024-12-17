<template>
  <section class="guide-section">
    <p class="section-type">Component</p>
    <h2 class="section-title">Form</h2>
    <p class="section-summary">
      폼은 사용자로부터 정보를 입력하고 제출할 수 있는 요소입니다. 일반적으로 여러 입력 필드와 제출 버튼등으로 구성되어 있습니다. </p>
    <article class="mb-10">
      <h3 class="section-subtitle">Vertical</h3>
      <div class="flex flex-col items-center">
        <div class="flex gap-8 mb-14">
          <figure class="section-figure items-center">
            <div class="form form-lg w-[400px]">
              <div class="form-head">
                <p3 class="form-title">Form title</p3>
              </div>
              <div class="form-body">
                <div class="form-item">
                  <label for="" class="form-label">
                    label
                    <span class="required">*</span>
                    <span class="sub-label">(sublabel)</span>
                  </label>
                  <div class="form-detail flex flex-col">
                    <div class="search-input">
                      <label class="hidden-text" for="text-input-example-4">label</label>
                      <input id="text-input-example-4" class="text-input text-input-lg" value="prefix icon + button" />
                      <svg-icon class="text-input-icon" name="user"></svg-icon>
                      <button class="search-input-action-button button button-neutral-ghost button-sm" type="button">
                        <span class="hidden-text">지우기</span>
                        <svg-icon class="button-icon" name="close"></svg-icon>
                      </button>
                    </div>
                    <div class="notification notification-sm notification-error">
                      <svg-icon class="notification-icon" name="error"></svg-icon>
                      <p class="notification-detail">얼럿 메세지를 입력해주세요.</p>
                    </div>
                  </div>
                </div>
                <div class="form-item">
                  <label for="" class="form-label">
                    label
                    <span class="required">*</span>
                    <span class="sub-label">(sublabel)</span>
                  </label>
                  <div class="form-detail">
                    <div class="form-input-group">
                      <div class="checkbox">
                        <input type="checkbox" id="checkbox-form-ex" class="checkbox-input" />
                        <label for="checkbox-form-ex" class="checkbox-label"> <span class="checkbox-text"> label </span></label>
                      </div>
                      <div class="checkbox">
                        <input type="checkbox" id="checkbox2" class="checkbox-input" disabled />
                        <label for="checkbox2" class="checkbox-label">
                          <span class="checkbox-text"> label </span></label
                        >
                      </div>
                    </div>
                  </div>
                </div>
                <div class="form-item">
                  <label for="" class="form-label">
                    label
                    <span class="required">*</span>
                    <span class="sub-label">(sublabel)</span>
                  </label>
                  <div class="form-detail">
                    <textarea class="textarea" rows="5"></textarea>
                    <div class="notification notification-sm notification-error">
                      <svg-icon class="notification-icon" name="error"></svg-icon>
                      <p class="notification-detail">얼럿 메세지를 입력해주세요.</p>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div class="form form-vertical form-sm w-[400px]">
              <div class="form-head">
                <p3 class="form-title">Form title</p3>
              </div>
              <div class="form-body">
                <div class="form-item">
                  <label for="" class="form-label">
                    label
                    <span class="required">*</span>
                    <span class="sub-label">(sublabel)</span>
                  </label>
                  <div class="form-detail flex flex-col">
                    <div class="search-input">
                      <label class="hidden-text" for="text-input-example-4">label</label>
                      <input id="text-input-example-4" class="text-input text-input-sm" value="prefix icon + button" />
                      <svg-icon class="text-input-icon" name="user"></svg-icon>
                      <button class="search-input-action-button button button-neutral-ghost button-sm" type="button">
                        <span class="hidden-text">지우기</span>
                        <svg-icon class="button-icon" name="close"></svg-icon>
                      </button>
                    </div>
                    <div class="notification notification-sm notification-error">
                      <svg-icon class="notification-icon" name="error"></svg-icon>
                      <p class="notification-detail">얼럿 메세지를 입력해주세요.</p>
                    </div>
                  </div>
                </div>
                <div class="form-item">
                  <label for="" class="form-label">
                    label
                    <span class="required">*</span>
                    <span class="sub-label">(sublabel)</span>
                  </label>
                  <div class="form-detail">
                    <div class="form-input-group">
                      <div class="checkbox checkbox-sm">
                        <input type="checkbox" id="checkbox-sm-form" class="checkbox-input" />
                        <label for="checkbox-sm-form" class="checkbox-label"> <span class="checkbox-text"> label </span></label>
                      </div>
                      <div class="checkbox checkbox-sm">
                        <input type="checkbox" id="checkbox2" class="checkbox-input" disabled />
                        <label for="checkbox2" class="checkbox-label">
                          <span class="checkbox-text"> label </span></label
                        >
                      </div>
                    </div>
                  </div>
                </div>
                <div class="form-item">
                  <label for="" class="form-label">
                    label
                    <span class="required">*</span>
                    <span class="sub-label">(sublabel)</span>
                  </label>
                  <div class="form-detail">
                    <textarea class="textarea" rows="5"></textarea>
                    <div class="notification notification-sm notification-error">
                      <svg-icon class="notification-icon" name="error"></svg-icon>
                      <p class="notification-detail">얼럿 메세지를 입력해주세요.</p>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </figure>
          <div class="group-description">
            <p class="group-summary">
              주요 버튼(Primary Buttons)은 페이지 키 액션에 사용하는 Button으로 한 페이지에 한 번만 사용합니다.
            </p>
          </div>
        </div>
      </div>
    </article>
    <article class="mb-10">
      <h3 class="section-subtitle">Horizontal</h3>
      <div class="flex flex-col items-center">
        <div class="flex gap-8 mb-14">
          <figure class="section-figure items-center">
            <div class="form form-horizontal form-lg w-[538px]">
              <div class="form-head">
                <p class="form-title">Form title</p>
              </div>
              <div class="form-body">
                <div class="form-item">
                  <label for="" class="form-label">
                    label
                    <span class="required">*</span>
                    <span class="sub-label">(sublabel)</span>
                  </label>
                  <div class="form-detail flex flex-col">
                    <div class="search-input">
                      <label class="hidden-text" for="text-input-example-4">label</label>
                      <input id="text-input-example-4" class="text-input text-input-lg" value="prefix icon + button" />
                      <svg-icon class="text-input-icon" name="user"></svg-icon>
                      <button class="search-input-action-button button button-neutral-ghost button-sm" type="button">
                        <span class="hidden-text">지우기</span>
                        <svg-icon class="button-icon" name="close"></svg-icon>
                      </button>
                    </div>
                    <div class="notification notification-sm notification-error">
                      <svg-icon class="notification-icon" name="error"></svg-icon>
                      <p class="notification-detail">얼럿 메세지를 입력해주세요.</p>
                    </div>
                  </div>
                </div>
                <div class="form-item">
                  <label for="" class="form-label">
                    label
                    <span class="required">*</span>
                    <span class="sub-label">(sublabel)</span>
                  </label>
                  <div class="form-detail">
                    <div class="form-input-group">
                      <div class="checkbox">
                        <input type="checkbox" id="checkbox" class="checkbox-input" />
                        <label for="checkbox" class="checkbox-label"> <span class="checkbox-text"> label </span></label>
                      </div>
                      <div class="checkbox">
                        <input type="checkbox" id="checkbox2" class="checkbox-input" disabled />
                        <label for="checkbox2" class="checkbox-label">
                          <span class="checkbox-text"> label </span></label
                        >
                      </div>
                    </div>
                  </div>
                </div>
                <div class="form-item">
                  <label for="" class="form-label">
                    label
                    <span class="required">*</span>
                    <span class="sub-label">(sublabel)</span>
                  </label>
                  <div class="form-detail">
                    <textarea class="textarea" rows="5"></textarea>
                    <div class="notification notification-sm notification-error">
                      <svg-icon class="notification-icon" name="error"></svg-icon>
                      <p class="notification-detail">얼럿 메세지를 입력해주세요.</p>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div class="form form-horizontal w-[538px]">
              <div class="form-head">
                <p3 class="form-title">Form title</p3>
              </div>
              <div class="form-body">
                <div class="form-item">
                  <label for="" class="form-label">
                    label
                    <span class="required">*</span>
                    <span class="sub-label">(sublabel)</span>
                  </label>
                  <div class="form-detail flex flex-col">
                    <div class="search-input">
                      <label class="hidden-text" for="text-input-example-4">label</label>
                      <input id="text-input-example-4" class="text-input" value="prefix icon + button" />
                      <svg-icon class="text-input-icon" name="user"></svg-icon>
                      <button class="search-input-action-button button button-neutral-ghost button-sm" type="button">
                        <span class="hidden-text">지우기</span>
                        <svg-icon class="button-icon" name="close"></svg-icon>
                      </button>
                    </div>
                    <div class="notification notification-sm notification-error">
                      <svg-icon class="notification-icon" name="error"></svg-icon>
                      <p class="notification-detail">얼럿 메세지를 입력해주세요.</p>
                    </div>
                  </div>
                </div>
                <div class="form-item">
                  <label for="" class="form-label">
                    label
                    <span class="required">*</span>
                    <span class="sub-label">(sublabel)</span>
                  </label>
                  <div class="form-detail">
                    <div class="form-input-group">
                      <div class="checkbox">
                        <input type="checkbox" id="checkbox" class="checkbox-input" />
                        <label for="checkbox" class="checkbox-label"> <span class="checkbox-text"> label </span></label>
                      </div>
                      <div class="checkbox">
                        <input type="checkbox" id="checkbox2" class="checkbox-input" disabled />
                        <label for="checkbox2" class="checkbox-label">
                          <span class="checkbox-text"> label </span></label
                        >
                      </div>
                    </div>
                  </div>
                </div>
                <div class="form-item">
                  <label for="" class="form-label">
                    label
                    <span class="required">*</span>
                    <span class="sub-label">(sublabel)</span>
                  </label>
                  <div class="form-detail">
                    <textarea class="textarea" rows="5"></textarea>
                    <div class="notification notification-sm notification-error">
                      <svg-icon class="notification-icon" name="error"></svg-icon>
                      <p class="notification-detail">얼럿 메세지를 입력해주세요.</p>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div class="form form-horizontal form-sm w-[538px]">
              <div class="form-head">
                <p3 class="form-title">Form title</p3>
              </div>
              <div class="form-body">
                <div class="form-item">
                  <label for="" class="form-label">
                    label
                    <span class="required">*</span>
                    <span class="sub-label">(sublabel)</span>
                  </label>
                  <div class="form-detail flex flex-col">
                    <div class="search-input">
                      <label class="hidden-text" for="text-input-example-4">label</label>
                      <input id="text-input-example-4" class="text-input text-input-sm" value="prefix icon + button" />
                      <svg-icon class="text-input-icon" name="user"></svg-icon>
                      <button class="search-input-action-button button button-neutral-ghost button-sm" type="button">
                        <span class="hidden-text">지우기</span>
                        <svg-icon class="button-icon" name="close"></svg-icon>
                      </button>
                    </div>
                    <div class="notification notification-sm notification-error">
                      <svg-icon class="notification-icon" name="error"></svg-icon>
                      <p class="notification-detail">얼럿 메세지를 입력해주세요.</p>
                    </div>
                  </div>
                </div>
                <div class="form-item">
                  <label for="" class="form-label">
                    label
                    <span class="required">*</span>
                    <span class="sub-label">(sublabel)</span>
                  </label>
                  <div class="form-detail">
                    <div class="form-input-group">
                      <div class="checkbox checkbox-sm">
                        <input type="checkbox" id="checkbox" class="checkbox-input" />
                        <label for="checkbox" class="checkbox-label"> <span class="checkbox-text"> label </span></label>
                      </div>
                      <div class="checkbox checkbox-sm">
                        <input type="checkbox" id="checkbox2" class="checkbox-input" disabled />
                        <label for="checkbox2" class="checkbox-label">
                          <span class="checkbox-text"> label </span></label
                        >
                      </div>
                    </div>
                  </div>
                </div>
                <div class="form-item">
                  <label for="" class="form-label">
                    label
                    <span class="required">*</span>
                    <span class="sub-label">(sublabel)</span>
                  </label>
                  <div class="form-detail">
                    <textarea class="textarea" rows="5"></textarea>
                    <div class="notification notification-sm notification-error">
                      <svg-icon class="notification-icon" name="error"></svg-icon>
                      <p class="notification-detail">얼럿 메세지를 입력해주세요.</p>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </figure>
          <div class="group-description">
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
  name: "guide-form"
};
</script>

<style scoped>

</style>