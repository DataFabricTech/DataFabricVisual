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
            <div class="flex gap-6 justify-center items-center h-full">
              <button class="button button-primary button-lg" @click="showModalP16 = true">padding-16</button>
              <button class="button button-primary button-lg" @click="showModalP32 = true">padding-32</button>
              <button class="button button-primary button-lg" @click="showModalP48 = true">padding-48</button>
              <div class="modal-fixed vfm--fixed vfm--inset" v-if="showModalP16">
                <div class="modal modal-padding-16" style="width:350px">
                  <div class="modal-head">
                    <div class="modal-head-text">
                      <span class="modal-head-title">insert modal title here</span>
                      <span class="modal-head-subtitle">Sub title</span>
                    </div>
                    <button class="button link-button button-sm" type="button"  @click="showModalP16 = false">
                      <span class="hidden-text">닫기</span>
                      <svg-icon class="button-icon" name="close"></svg-icon>
                    </button>
                  </div>
                  <div class="modal-body">
                    <input class="text-input text-input-lg w-full" />
                  </div>
                  <div class="modal-foot">
                    <div class="modal-foot-group">
                      <button class="button button-neutral-ghost button-lg" @click=" showModalP16 = false">취소</button>
                      <button class="button button-primary button-lg">확인</button>
                    </div>
                  </div>
                </div>
              </div>
              <div class="modal-fixed vfm--fixed vfm--inset" v-if="showModalP32">
                <div class="modal modal-padding-32" style="width:350px">
                  <div class="modal-head">
                    <div class="modal-head-text">
                      <span class="modal-head-title">insert modal title heredd</span>
                      <span class="modal-head-subtitle">Sub title</span>
                    </div>
                    <button class="button link-button button-sm" type="button" @click="showModalP32 = false">
                      <span class="hidden-text">닫기</span>
                      <svg-icon class="button-icon" name="close"></svg-icon>
                    </button>
                  </div>
                  <div class="modal-body">
                    <input class="text-input text-input-lg w-full" />
                  </div>
                  <div class="modal-foot">
                    <div class="modal-foot-group">
                      <button class="button button-neutral-ghost button-lg" @click="showModalP32 = false">취소</button>
                      <button class="button button-primary button-lg">확인</button>
                    </div>
                  </div>
                </div>

              </div>
              <div class="modal-fixed vfm--fixed vfm--inset" v-if="showModalP48">
                <div class="modal modal-padding-48" style="width:350px">
                  <div class="modal-head">
                    <div class="modal-head-text">
                      <span class="modal-head-title">insert modal title here</span>
                      <span class="modal-head-subtitle">Sub title</span>
                    </div>
                    <button class="button link-button button-sm" type="button" @click="showModalP48 = false">
                      <span class="hidden-text">닫기</span>
                      <svg-icon class="button-icon" name="close"></svg-icon>
                    </button>
                  </div>
                  <div class="modal-body">
                    <input class="text-input text-input-lg w-full" />
                  </div>
                  <div class="modal-foot">
                    <div class="modal-foot-group">
                      <button class="button button-neutral-ghost button-lg" @click="showModalP48 = false">취소</button>
                      <button class="button button-primary button-lg">확인</button>
                    </div>
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
            <div class="flex gap-6 justify-center items-center h-full">
              <button class="button button-primary button-lg" @click="showModalDefault = true">Default</button>
              <button class="button button-primary button-lg" @click="showModalFullBtn = true">Full Button</button>
              <button class="button button-primary button-lg" @click="showModalBothBtn = true">Both Button</button>
              <button class="button button-primary button-lg" @click="showModalCheckbox = true">With Checkbox</button>
              <div class="modal-fixed vfm--inset vfm--fixed" v-if="showModalDefault">
                <div class="modal modal-padding-16" style="width:350px">
                  <div class="modal-head">
                    <div class="modal-head-text">
                      <span class="modal-head-title">insert modal title here</span>
                      <span class="modal-head-subtitle">Sub title</span>
                    </div>
                    <button class="button link-button button-sm" type="button" @click="showModalDefault = false">
                      <span class="hidden-text">닫기</span>
                      <svg-icon class="button-icon" name="close"></svg-icon>
                    </button>
                  </div>
                  <div class="modal-body">
                    <input class="text-input text-input-lg w-full" />
                  </div>
                  <div class="modal-foot">
                    <div class="modal-foot-group">
                      <button class="button button-neutral-ghost button-lg" @click="showModalDefault = false">취소</button>
                      <button class="button button-primary button-lg">확인</button>
                    </div>
                  </div>
                </div>
              </div>
              <div class="modal-fixed vfm--inset vfm--fixed" v-if="showModalFullBtn">
                <div class="modal modal-padding-16" style="width:350px">
                  <div class="modal-head">
                    <div class="modal-head-text">
                      <span class="modal-head-title">insert modal title heredd</span>
                      <span class="modal-head-subtitle">Sub title</span>
                    </div>
                    <button class="button link-button button-sm" type="button" @click="showModalFullBtn = false">
                      <span class="hidden-text">닫기</span>
                      <svg-icon class="button-icon" name="close"></svg-icon>
                    </button>
                  </div>
                  <div class="modal-body">
                    <input class="text-input text-input-lg w-full" />
                  </div>
                  <div class="modal-foot">
                    <button class="button button-neutral-ghost button-lg" @click="showModalFullBtn = false">취소</button>
                    <button class="button button-primary button-lg">확인</button>
                  </div>
                </div>
              </div>
              <div class="modal-fixed vfm--inset vfm--fixed" v-if="showModalBothBtn">
                <div class="modal modal-padding-16" style="width:350px">
                  <div class="modal-head">
                    <div class="modal-head-text">
                      <span class="modal-head-title">insert modal title here</span>
                      <span class="modal-head-subtitle">Sub title</span>
                    </div>
                    <button class="button link-button button-sm" type="button" @click="showModalBothBtn = false">
                      <span class="hidden-text">닫기</span>
                      <svg-icon class="button-icon" name="close"></svg-icon>
                    </button>
                  </div>
                  <div class="modal-body">
                    <input class="text-input text-input-lg w-full" />
                  </div>
                  <div class="modal-foot">
                    <button class="button-error-lighter button button-lg" type="button">초기화</button>
                    <div class="modal-foot-group">
                      <button class="button button-neutral-ghost button-lg" @click="showModalBothBtn = false">취소</button>
                      <button class="button button-primary button-lg">확인</button>
                    </div>
                  </div>
                </div>
              </div>
              <div class="modal-fixed vfm--inset vfm--fixed" v-if="showModalCheckbox">
                <div class="modal modal-padding-16" style="width:350px">
                  <div class="modal-head">
                    <div class="modal-head-text">
                      <span class="modal-head-title">insert modal title here</span>
                      <span class="modal-head-subtitle">Sub title</span>
                    </div>
                    <button class="button link-button button-sm" type="button" @click="showModalCheckbox = false">
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
                      <button class="button button-neutral-ghost button-lg" @click="showModalCheckbox = false">취소</button>
                      <button class="button button-primary button-lg">확인</button>
                    </div>
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
            <div class="flex gap-6 justify-center items-center h-full">
              <button class="button button-primary button-lg" @click="showModalXl = true">Xl</button>
              <button class="button button-primary button-lg" @click="showModalLg = true">Lg</button>
              <button class="button button-primary button-lg" @click="showModalMd = true">Md</button>
              <button class="button button-primary button-lg" @click="showModalSm = true">Sm</button>
              <button class="button button-primary button-lg" @click="showModalXs = true">Xs</button>
              <div class="modal-fixed vfm--fixed vfm--inset" v-if="showModalXl">
                <div class="modal modal-padding-16" style="width:1180px">
                  <div class="modal-head">
                    <div class="modal-head-text">
                      <span class="modal-head-title">insert modal title here</span>
                      <span class="modal-head-subtitle">Sub title</span>
                    </div>
                    <button class="button link-button button-sm" type="button" @click="showModalXl = false">
                      <span class="hidden-text">닫기</span>
                      <svg-icon class="button-icon" name="close"></svg-icon>
                    </button>
                  </div>
                  <div class="modal-body">
                    <input class="text-input text-input-lg w-full" />
                  </div>
                  <div class="modal-foot">
                    <div class="modal-foot-group">
                      <button class="button button-neutral-ghost button-lg" @click="showModalXl = false">취소</button>
                      <button class="button button-primary button-lg">확인</button>
                    </div>
                  </div>
                </div>
              </div>
              <div class="modal-fixed vfm--fixed vfm--inset" v-if="showModalLg">
                <div class="modal modal-padding-16" style="width:900px">
                  <div class="modal-head">
                    <div class="modal-head-text">
                      <span class="modal-head-title">insert modal title here</span>
                      <span class="modal-head-subtitle">Sub title</span>
                    </div>
                    <button class="button link-button button-sm" type="button" @click="showModalLg = false">
                      <span class="hidden-text">닫기</span>
                      <svg-icon class="button-icon" name="close"></svg-icon>
                    </button>
                  </div>
                  <div class="modal-body">
                    <input class="text-input text-input-lg w-full" />
                  </div>
                  <div class="modal-foot">
                    <div class="modal-foot-group">
                      <button class="button button-neutral-ghost button-lg" @click="showModalLg = false">취소</button>
                      <button class="button button-primary button-lg">확인</button>
                    </div>
                  </div>
                </div>
              </div>
              <div class="modal-fixed vfm--fixed vfm--inset" v-if="showModalMd">
                <div class="modal modal-padding-16" style="width:620px">
                  <div class="modal-head">
                    <div class="modal-head-text">
                      <span class="modal-head-title">insert modal title here</span>
                      <span class="modal-head-subtitle">Sub title</span>
                    </div>
                    <button class="button link-button button-sm" type="button" @click="showModalMd = false">
                      <span class="hidden-text">닫기</span>
                      <svg-icon class="button-icon" name="close"></svg-icon>
                    </button>
                  </div>
                  <div class="modal-body">
                    <input class="text-input text-input-lg w-full" />
                  </div>
                  <div class="modal-foot">
                    <div class="modal-foot-group">
                      <button class="button button-neutral-ghost button-lg" @click="showModalMd = false">취소</button>
                      <button class="button button-primary button-lg">확인</button>
                    </div>
                  </div>
                </div>
              </div>
              <div class="modal-fixed vfm--fixed vfm--inset" v-if="showModalSm">
                <div class="modal modal-padding-16" style="width:480px">
                  <div class="modal-head">
                    <div class="modal-head-text">
                      <span class="modal-head-title">insert modal title here</span>
                      <span class="modal-head-subtitle">Sub title</span>
                    </div>
                    <button class="button link-button button-sm" type="button "@click="showModalSm = false">
                      <span class="hidden-text">닫기</span>
                      <svg-icon class="button-icon" name="close"></svg-icon>
                    </button>
                  </div>
                  <div class="modal-body">
                    <input class="text-input text-input-lg w-full" />
                  </div>
                  <div class="modal-foot">
                    <div class="modal-foot-group">
                      <button class="button button-neutral-ghost button-lg" @click="showModalSm = false">취소</button>
                      <button class="button button-primary button-lg">확인</button>
                    </div>
                  </div>
                </div>
              </div>
              <div class="modal-fixed vfm--fixed vfm--inset" v-if="showModalXs">
                <div class="modal modal-padding-16" style="width:350px">
                  <div class="modal-head">
                    <div class="modal-head-text">
                      <span class="modal-head-title">insert modal title here</span>
                      <span class="modal-head-subtitle">Sub title</span>
                    </div>
                    <button class="button link-button button-sm" type="button" @click="showModalXs = false">
                      <span class="hidden-text">닫기</span>
                      <svg-icon class="button-icon" name="close"></svg-icon>
                    </button>
                  </div>
                  <div class="modal-body">
                    <input class="text-input text-input-lg w-full" />
                  </div>
                  <div class="modal-foot">
                    <div class="modal-foot-group">
                      <button class="button button-neutral-ghost button-lg" @click="showModalXs = false">취소</button>
                      <button class="button button-primary button-lg">확인</button>
                    </div>
                  </div>
                </div>
              </div>

            </div>
          </figure>
          <div class="group-description">
            <p class="group-summary">
              모달의 너비를 지정하는 여러 가지 방법이 있습니다. 너비 지정은 디자인 가이드에 정의된 크기 옵션을 사용하는 것을 권장합니다.
            </p>
            <ul>
              <li>Xl : 1180px</li>
              <li>Lg : 900px</li>
              <li>Md : 620px</li>
              <li>Sm : 480px</li>
              <li>Xs : 350px</li>
            </ul>
          </div>

        </div>
      </div>
    </article>
  </section>
</template>

<script>
export default {
  name: "guide-modal",
  data() {
    return {
      showModalP16: false,
      showModalP32: false,
      showModalP48: false,
      showModalDefault: false,
      showModalFullBtn: false,
      showModalBothBtn: false,
      showModalCheckbox: false,
      showModalXl: false,
      showModalLg: false,
      showModalMd: false,
      showModalSm: false,
      showModalXs: false,
    };
  }
};
</script>

<style scoped>

</style>