<template>
  <div class="section-top-bar">
    <div class="l-top-bar">
      <h3 class="title">분류</h3>
    </div>
  </div>
  <div class="section-contents p-0 bg-white">
    <div class="l-split">
      <div class="work-list">
        <div class="l-top-bar">
          <span class="title">분류 목록</span>
          <button
            class="button button-secondary-stroke"
            @click="showModalClas = true"
          >
            분류 추가
          </button>
        </div>
        <!-- 결과 없을 시 no-result 표시 / 기본 .work-page로 컨텐츠 표시 -->
        <div class="no-result" style="display: none">
          <div class="notification">
            <svg-icon class="notification-icon" name="info"></svg-icon>
            <p class="notification-detail">등록된 정보가 없습니다.</p>
          </div>
        </div>
        <div class="menu border-none">
          <div class="menu-list">
            <li class="menu-item">
              <button class="menu-button">
                <svg-icon class="svg-icon" name="tag"></svg-icon>
                <a href="" class="menu-text">분류 01</a>
              </button>
            </li>
            <li class="menu-item">
              <button class="menu-button">
                <svg-icon class="svg-icon" name="tag"></svg-icon>
                <a href="" class="menu-text">분류 02</a>
              </button>
            </li>
            <li class="menu-item">
              <button class="menu-button">
                <svg-icon class="svg-icon" name="tag"></svg-icon>
                <a href="" class="menu-text">분류 03</a>
              </button>
            </li>
            <li class="menu-item">
              <button class="menu-button">
                <svg-icon class="svg-icon" name="tag"></svg-icon>
                <a href="" class="menu-text">분류 04</a>
              </button>
            </li>
          </div>
        </div>
      </div>
      <div class="work-page">
        <div class="l-top-bar">
          <div class="editable-group">
            <span class="editable-group-title">분류 01</span>
            <button class="button button-neutral-ghost button-sm" type="button">
              <span class="hidden-text">수정</span>
              <svg-icon class="button-icon" name="pen"></svg-icon>
            </button>
          </div>
          <!-- 수정 버튼 클릭시 아래 내용으로 전환됩니다 -->
          <div class="editable-group">
            <lable class="hidden-text" for="title-modify">분류 이름 수정</lable>
            <input id="title-modify" class="text-input w-4/5" />
            <div class="h-group gap-1">
              <button class="button button-neutral-stroke" type="button">
                취소
              </button>
              <button class="button button-primary-lighter" type="button">
                완료
              </button>
            </div>
          </div>
          <button class="button button-error-lighter">삭제</button>
        </div>
        <div class="work-contents">
          <!-- 결과 없을 시 no-result 표시  -->
          <div class="no-result" style="display: none">
            <div class="notification">
              <svg-icon class="notification-icon" name="info"></svg-icon>
              <p class="notification-detail">등록된 정보가 없습니다.</p>
            </div>
          </div>
          <div class="v-group gap-2">
            <div class="font-semibold text-neutral-700">설명</div>
            <div class="editable-group">
            <span class="editable-group-desc"
              >GDPR special category data is personal information of data
              subjects that is especially sensitive, the exposure of which could
              significantly impact the rights and freedoms of data subjects and
              potentially be used against them for unlawful
              discrimination.</span
            >
            <button class="button button-neutral-ghost button-sm" type="button">
              <span class="hidden-text">수정</span>
              <svg-icon class="button-icon" name="pen"></svg-icon>
            </button>
          </div>
          </div>
          <!-- 수정 버튼 클릭시 아래 내용으로 전환됩니다 -->
          <div class="v-group gap-2">
            <div class="font-semibold text-neutral-700">설명</div>
            <div class="editable-group">
            <lable class="hidden-text" for="description-modify"
              >분류 설명 수정
            </lable>
            <textarea
              id="description-modify"
              class="textarea"
              width="300px"
            ></textarea>
            <div class="h-group gap-1">
              <button class="button button-neutral-stroke" type="button">
                취소
              </button>
              <button class="button button-primary-lighter" type="button">
                완료
              </button>
            </div>
          </div>
          </div>
          <div>
            <div class="l-top-bar">
              <button
                class="button button-secondary ml-auto"
                @click="showModalTag = true"
              >
                태그추가
              </button>
            </div>
            <table class="align-center">
              <colgroup>
                <col style="width: 25%" />
                <col style="width: 25%" />
                <col style="width: 25%" />
              </colgroup>
              <thead>
                <tr>
                  <th>태그</th>
                  <th>설명</th>
                  <th>관리</th>
              </tr>
              </thead>
              <tbody>
                <tr>
                  <td>태그 01</td>
                  <td>태그에 대한 설명입니다.</td>
                  <td>
                    <div class="button-group">
                      <button class="button button button-secondary-stroke">
                        편집
                      </button>
                      <button class="button button button-error-stroke">
                        삭제
                      </button>
                    </div>
                  </td>
                </tr>
                <tr>
                  <td>태그 02</td>
                  <td>태그에 대한 설명입니다.</td>
                  <td>
                    <div class="button-group">
                      <button class="button button button-secondary-stroke">
                        편집
                      </button>
                      <button class="button button button-error-stroke">
                        삭제
                      </button>
                    </div>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  </div>
  <!--  TODO: Modal 분류 추가 width:480px height:394px-->
  <div class="modal-fixed vfm--fixed vfm--inset" v-if="showModalClas">
    <div class="modal modal-padding-16" style="width: 480px">
      <div class="modal-head">
        <div class="modal-head-text">
          <span class="modal-head-title">분류 추가</span>
        </div>
        <button
          class="button link-button button-sm"
          type="button"
          @click="showModalClas = false"
        >
          <span class="hidden-text">닫기</span>
          <svg-icon class="button-icon" name="close"></svg-icon>
        </button>
      </div>
      <div class="modal-body">
        <div class="form form-lg">
          <div class="form-body">
            <div class="form-item">
              <label for="data-model-save-name" class="form-label">
                이름
                <span class="required">*</span>
              </label>
              <div class="form-detail">
                <input
                  id="data-model-save-name"
                  class="text-input text-input-lg"
                  placeholder="이름을 입력하세요."
                />
                <div class="notification notification-sm notification-error">
                  <svg-icon class="notification-icon" name="error"></svg-icon>
                  <p class="notification-detail">이름을 입력하세요.</p>
                </div>
              </div>
            </div>
            <div class="form-item">
              <label class="form-label" for="data-model-save-description">
                설명
                <span class="required">*</span>
              </label>
              <div class="form-detail">
                <textarea
                  id="data-model-save-description"
                  class="textarea h-28"
                  placeholder="설명을 입력하세요."
                ></textarea>
                <div class="notification notification-sm notification-error">
                  <svg-icon class="notification-icon" name="error"></svg-icon>
                  <p class="notification-detail">설명을 입력하세요.</p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="modal-foot">
        <div class="modal-foot-group">
          <button
            class="button button-neutral-ghost button-lg"
            @click="showModalClas = false"
          >
            취소
          </button>
          <button class="button button-primary button-lg">저장</button>
        </div>
      </div>
    </div>
  </div>
  <!--  TODO: Modal 태그 추가 width:480px height:394px-->
  <div class="modal-fixed vfm--fixed vfm--inset" v-if="showModalTag">
    <div class="modal modal-padding-16" style="width: 480px">
      <div class="modal-head">
        <div class="modal-head-text">
          <span class="modal-head-title">태그추가</span>
        </div>
        <button
          class="button link-button button-sm"
          type="button"
          @click="showModalTag = false"
        >
          <span class="hidden-text">닫기</span>
          <svg-icon class="button-icon" name="close"></svg-icon>
        </button>
      </div>
      <div class="modal-body">
        <div class="form form-lg">
          <div class="form-body">
            <div class="form-item">
              <label for="data-model-save-name" class="form-label">
                이름
                <span class="required">*</span>
              </label>
              <div class="form-detail">
                <input
                  id="data-model-save-name"
                  class="text-input text-input-lg"
                  placeholder="이름을 입력하세요."
                />
                <div class="notification notification-sm notification-error">
                  <svg-icon class="notification-icon" name="error"></svg-icon>
                  <p class="notification-detail">이름을 입력하세요.</p>
                </div>
              </div>
            </div>
            <div class="form-item">
              <label class="form-label" for="data-model-save-description">
                설명
                <span class="required">*</span>
              </label>
              <div class="form-detail">
                <textarea
                  id="data-model-save-description"
                  class="textarea h-28"
                  placeholder="설명을 입력하세요."
                ></textarea>
                <div class="notification notification-sm notification-error">
                  <svg-icon class="notification-icon" name="error"></svg-icon>
                  <p class="notification-detail">설명을 입력하세요.</p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="modal-foot">
        <div class="modal-foot-group">
          <button
            class="button button-neutral-ghost button-lg"
            @click="showModalTag = false"
          >
            취소
          </button>
          <button class="button button-primary button-lg">저장</button>
        </div>
      </div>
    </div>
  </div>
  <!--  TODO: Modal 태그 수정 width:480px height:394px-->
  <div class="modal-fixed vfm--fixed vfm--inset" v-if="showModalTagModify">
    <div class="modal modal-padding-16" style="width: 480px">
      <div class="modal-head">
        <div class="modal-head-text">
          <span class="modal-head-title">태그수정</span>
        </div>
        <button
          class="button link-button button-sm"
          type="button"
          @click="showModalTagModify = false"
        >
          <span class="hidden-text">닫기</span>
          <svg-icon class="button-icon" name="close"></svg-icon>
        </button>
      </div>
      <div class="modal-body">
        <div class="form form-lg">
          <div class="form-body">
            <div class="form-item">
              <label for="data-model-save-name" class="form-label">
                이름
                <span class="required">*</span>
              </label>
              <div class="form-detail">
                <input
                  id="data-model-save-name"
                  class="text-input text-input-lg"
                  placeholder="이름을 입력하세요."
                />
                <div class="notification notification-sm notification-error">
                  <svg-icon class="notification-icon" name="error"></svg-icon>
                  <p class="notification-detail">이름을 입력하세요.</p>
                </div>
              </div>
            </div>
            <div class="form-item">
              <label class="form-label" for="data-model-save-description">
                설명
                <span class="required">*</span>
              </label>
              <div class="form-detail">
                <textarea
                  id="data-model-save-description"
                  class="textarea h-28"
                  placeholder="설명을 입력하세요."
                ></textarea>
                <div class="notification notification-sm notification-error">
                  <svg-icon class="notification-icon" name="error"></svg-icon>
                  <p class="notification-detail">설명을 입력하세요.</p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="modal-foot">
        <button
          class="button button-neutral-ghost button-lg"
          @click="showModalTagModify = false"
        >
          취소
        </button>
        <div class="modal-foot-group">
          <button class="button button-primary button-lg">저장</button>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped></style>
<script>
import { defineComponent } from "vue";
import MenuSearch from "@extends/menu-seach/menu-search.vue";

export default defineComponent({
  components: { MenuSearch },
  data() {
    return {
      showModalTag: false,
      showModalTagModify: false,
      showModalClas: false,
    };
  },
});
</script>
