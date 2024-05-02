<template>
  <section class="guide-section">
    <p class="section-type">Component</p>
    <h2 class="section-title">Modal</h2>
    <p class="section-summary">
      모달은 페이지 제일 상단으로 나타나며 사용자의 상호작용이 필요한 내용을 표시합니다.
    </p>
    <article class="mb-10">
      <h3 class="section-subtitle">Variants</h3>
      <div class="flex flex-col">
        <div class="flex gap-8 mb-14">
          <figure class="section-figure">
            <div class="flex flex-col items-center gap-6">
              <div class="modal modal-padding-16" style="width:400px">
                <div class="modal-head">
                  <div class="modal-head-text">
                    <span class="modal-head-title">insert modal title here</span>
                    <span class="modal-head-subtitle">Sub title</span>
                  </div>
                  <button class="button link-button button-sm" type="button">
                    <span class="hidden-text">닫기</span>
                    <svg-icon class="button-icon" name="close"></svg-icon>
                  </button>
                </div>
                <div class="modal-body">
                  <input class="text-input text-input-lg w-full" />
                </div>
                <div class="modal-foot">
                  <div class="modal-foot-group">
                    <button class="button button-neutral-ghost button-lg">취소</button>
                    <button class="button button-primary button-lg">확인</button>
                  </div>
                </div>
              </div>
              <div class="modal modal-padding-32" style="width:400px">
                <div class="modal-head">
                  <div class="modal-head-text">
                    <span class="modal-head-title">insert modal title heredd</span>
                    <span class="modal-head-subtitle">Sub title</span>
                  </div>
                  <button class="button link-button button-sm" type="button">
                    <span class="hidden-text">닫기</span>
                    <svg-icon class="button-icon" name="close"></svg-icon>
                  </button>
                </div>
                <div class="modal-body">
                  <input class="text-input text-input-lg w-full" />
                </div>
                <div class="modal-foot">
                  <div class="modal-foot-group">
                    <button class="button button-neutral-ghost button-lg">취소</button>
                    <button class="button button-primary button-lg">확인</button>
                  </div>
                </div>
              </div>
              <div class="modal modal-padding-48" style="width:400px">
                <div class="modal-head">
                  <div class="modal-head-text">
                    <span class="modal-head-title">insert modal title here</span>
                    <span class="modal-head-subtitle">Sub title</span>
                  </div>
                  <button class="button link-button button-sm" type="button">
                    <span class="hidden-text">닫기</span>
                    <svg-icon class="button-icon" name="close"></svg-icon>
                  </button>
                </div>
                <div class="modal-body">
                  <input class="text-input text-input-lg w-full" />
                </div>
                <div class="modal-foot">
                  <div class="modal-foot-group">
                    <button class="button button-neutral-ghost button-lg">취소</button>
                    <button class="button button-primary button-lg">확인</button>
                  </div>
                </div>
              </div>
            </div>
          </figure>
          <div class="group-description">
            <h4 class="group-title">Body padding</h4>
            <p class="group-summary">
              모달 바디 영역의 내부 여백은 4개 사이즈를 갖습니다.
            </p>
            <ul>
              <li><code>default</code> : 0</li>
              <li><code>modal-padding-16</code> : 16px</li>
              <li><code>modal-padding-32</code> : 32px</li>
              <li><code>modal-padding-48</code> : 48px</li>
            </ul>
          </div>

        </div>
        <div class="flex gap-8 mb-14">
          <figure class="section-figure">
            <div class="flex flex-col items-center gap-6">
              <div class="modal modal-padding-16" style="width:400px">
                <div class="modal-head">
                  <div class="modal-head-text">
                    <span class="modal-head-title">insert modal title here</span>
                    <span class="modal-head-subtitle">Sub title</span>
                  </div>
                  <button class="button link-button button-sm" type="button">
                    <span class="hidden-text">닫기</span>
                    <svg-icon class="button-icon" name="close"></svg-icon>
                  </button>
                </div>
                <div class="modal-body">
                  <input class="text-input text-input-lg w-full" />
                </div>
                <div class="modal-foot">
                  <div class="modal-foot-group">
                    <button class="button button-neutral-ghost button-lg">취소</button>
                    <button class="button button-primary button-lg">확인</button>
                  </div>
                </div>
              </div>
              <div class="modal modal-padding-16" style="width:400px">
                <div class="modal-head">
                  <div class="modal-head-text">
                    <span class="modal-head-title">insert modal title heredd</span>
                    <span class="modal-head-subtitle">Sub title</span>
                  </div>
                  <button class="button link-button button-sm" type="button">
                    <span class="hidden-text">닫기</span>
                    <svg-icon class="button-icon" name="close"></svg-icon>
                  </button>
                </div>
                <div class="modal-body">
                  <input class="text-input text-input-lg w-full" />
                </div>
                <div class="modal-foot">
                  <button class="button button-neutral-ghost button-lg">취소</button>
                  <button class="button button-primary button-lg">확인</button>
                </div>
              </div>
              <div class="modal modal-padding-16" style="width:400px">
                <div class="modal-head">
                  <div class="modal-head-text">
                    <span class="modal-head-title">insert modal title here</span>
                    <span class="modal-head-subtitle">Sub title</span>
                  </div>
                  <button class="button link-button button-sm" type="button">
                    <span class="hidden-text">닫기</span>
                    <svg-icon class="button-icon" name="close"></svg-icon>
                  </button>
                </div>
                <div class="modal-body">
                  <input class="text-input text-input-lg w-full" />
                </div>
                <div class="modal-foot">
                  <div class="checkbox">
                    <input type="checkbox" id="today" class="checkbox-input" />
                    <label for="today" class="checkbox-label">
                      오늘 하루 그만보기 <span class="checkbox-subtext">(Optional)</span>
                    </label>
                  </div>
                  <div class="modal-foot-group">
                    <button class="button button-neutral-ghost button-lg">취소</button>
                    <button class="button button-primary button-lg">확인</button>
                  </div>
                </div>
              </div>
            </div>
          </figure>
          <div class="group-description">
            <h4 class="group-title">Footer button</h4>
            <p class="group-summary">
              버튼은 최소 1개에서 최대 3개까지 사용 가능하며 우선순위가 높은 버튼은 <code>primary-500</code>을 적용해야 합니다. 이전으로 돌아갈 수 있는 버튼을 반드시 포함해야
              합니다.
            </p>
          </div>

        </div>
      </div>
    </article>
    <article class="mb-10">
      <h3 class="section-subtitle">Width</h3>
      <div class="flex flex-col">
        <div class="flex gap-8 mb-14">
          <figure class="section-figure">
            <div class="flex flex-col items-center gap-6">
              <div class="modal modal-padding-16" style="width:400px">
                <div class="modal-head">
                  <div class="modal-head-text">
                    <span class="modal-head-title">insert modal title here</span>
                    <span class="modal-head-subtitle">Sub title</span>
                  </div>
                  <button class="button link-button button-sm" type="button">
                    <span class="hidden-text">닫기</span>
                    <svg-icon class="button-icon" name="close"></svg-icon>
                  </button>
                </div>
                <div class="modal-body">
                  <input class="text-input text-input-lg w-full" />
                </div>
                <div class="modal-foot">
                  <div class="modal-foot-group">
                    <button class="button button-neutral-ghost button-lg">취소</button>
                    <button class="button button-primary button-lg">확인</button>
                  </div>
                </div>
              </div>
              <div class="modal modal-padding-16" style="width:400px">
                <div class="modal-head">
                  <div class="modal-head-text">
                    <span class="modal-head-title">insert modal title heredd</span>
                    <span class="modal-head-subtitle">Sub title</span>
                  </div>
                  <button class="button link-button button-sm" type="button">
                    <span class="hidden-text">닫기</span>
                    <svg-icon class="button-icon" name="close"></svg-icon>
                  </button>
                </div>
                <div class="modal-body">
                  <input class="text-input text-input-lg w-full" />
                </div>
                <div class="modal-foot">
                  <button class="button button-neutral-ghost button-lg">취소</button>
                  <button class="button button-primary button-lg">확인</button>
                </div>
              </div>
              <div class="modal modal-padding-16" style="width:400px">
                <div class="modal-head">
                  <div class="modal-head-text">
                    <span class="modal-head-title">insert modal title here</span>
                    <span class="modal-head-subtitle">Sub title</span>
                  </div>
                  <button class="button link-button button-sm" type="button">
                    <span class="hidden-text">닫기</span>
                    <svg-icon class="button-icon" name="close"></svg-icon>
                  </button>
                </div>
                <div class="modal-body">
                  <input class="text-input text-input-lg w-full" />
                </div>
                <div class="modal-foot">
                  <div class="checkbox">
                    <input type="checkbox" id="today" class="checkbox-input" />
                    <label for="today" class="checkbox-label">
                      오늘 하루 그만보기 <span class="checkbox-subtext">(Optional)</span>
                    </label>
                  </div>
                  <div class="modal-foot-group">
                    <button class="button button-neutral-ghost button-lg">취소</button>
                    <button class="button button-primary button-lg">확인</button>
                  </div>
                </div>
              </div>
            </div>
          </figure>
          <div class="group-description">
            <p class="group-summary">
              버튼은 최소 1개에서 최대 3개까지 사용 가능하며 우선순위가 높은 버튼은 <code>primary-500</code>을 적용해야 합니다. 이전으로 돌아갈 수 있는 버튼을 반드시 포함해야
              합니다.
            </p>
            <ul>
              <li><code>modal-padding-16</code> : 16px</li>
              <li><code>modal-padding-32</code> : 32px</li>
              <li><code>modal-padding-48</code> : 48px</li>
            </ul>
          </div>

        </div>
      </div>
    </article>
  </section>
</template>

<script>
export default {
  name: "guide-modal"
};
</script>

<style scoped>

</style>