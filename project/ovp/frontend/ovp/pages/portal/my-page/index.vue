<template>
  <div class="section-contents bg-white">
    <div class="interaction-buttons">
      <button class="button button-neutral-stroke" @click="showModalPwChange">
        비밀번호 변경
      </button>
    </div>
    <profile-box></profile-box>
    <div class="tab tab-line">
      <ul class="tab-list">
        <li class="tab-item is-tab-item-selected">
          <button class="tab-button">
            <p class="tab-button-text">나의 북마크</p>
          </button>
        </li>
        <li class="tab-item">
          <button class="tab-button">
            <p class="tab-button-text">나의 데이터</p>
          </button>
        </li>
      </ul>
      <div class="tab-contents">
        <div class="search-input search-input-lg w-96">
          <label class="hidden-text" for="text-input-example-4">label</label>
          <input
            id="text-input-example-4"
            class="text-input"
            placeholder="검색어를 입력하세요."
          />
          <svg-icon class="text-input-icon" name="search"></svg-icon>
          <button
            class="search-input-action-button button button-neutral-ghost button-sm"
            type="button"
          >
            <span class="hidden-text">지우기</span>
            <svg-icon class="button-icon" name="close"></svg-icon>
          </button>
        </div>
        <div class="l-split">
          <div class="data-page">
            <div class="data-list">
              <div class="resource-box" v-for="card in 9" :key="card">
                <div class="resource-box-function">
                  <div class="resource-box-model">
                    <img src="" alt="" />
                    <div class="breadcrumb">
                      <ul class="breadcrumb-list">
                        <li class="breadcrumb-item">
                          <span class="breadcrumb-text">1depth</span>
                        </li>
                        <li class="breadcrumb-item">
                          <span class="breadcrumb-text">2depth</span>
                        </li>
                        <li class="breadcrumb-item">
                          <span class="breadcrumb-text">데이터 모델</span>
                        </li>
                      </ul>
                    </div>
                  </div>
                </div>
                <a href="#" class="editable-group-title" title="상세 보기"
                  >세종특별자치시 상하수도요금표</a
                >
                <span class="editable-group-desc"
                  >한국교통안전공단에서 교통카드를 이용한 대중교통 사용시 1회
                  이용요금 평균을 조사한 결과 입니다.한국교통안전공단에서
                  교통카드를 이용한 대중교통 사용시 1회 이용요금 평균을 조사한
                  결과 입니다한국교통안전공단에서 교통카드를 이용한 대중교통
                  사용시 1회 이용요금 평균을 조사한 결과
                  입니다한국교통안전공단에서 교통카드를 이용한 대중교통 사용시
                  1회 이용요금 평균을 조사한 결과 입니다한국교통안전공단에서
                  교통카드를 이용한 대중교통 사용시 1회 이용요금 평균을 조사한
                  결과 입니다한국교통안전공단에서 교통카드를 이용한 대중교통
                  사용시 1회 이용요금 평균을 조사한 결과
                  입니다한국교통안전공단에서 교통카드를 이용한 대중교통 사용시
                  1회 이용요금 평균을 조사한 결과 입니다</span
                >
                <div class="resource-box-info">
                  <dl class="resource-box-list">
                    <dt>소유자</dt>
                    <dd>Owner</dd>
                  </dl>
                  <dl class="resource-box-list">
                    <dt>도메인</dt>
                    <dd>Domain</dd>
                  </dl>
                </div>
              </div>
            </div>
          </div>
          <div class="preview">
            <div class="ml-auto">
              <button class="button button-neutral-ghost button-sm">
                <svg-icon class="svg-icon" name="close"></svg-icon>
                <span class="hidden-text">닫기</span>
              </button>
            </div>
            <div class="preview-contents">
              <div class="preview-item">
                <div class="preview-title">세종특별자치시 상하수도요금표</div>
                <div class="preview-desc">
                  한국교통안전공단에서 교통카드를 이용한 대중교통 사용시 1회
                  이용요금 평균을 조사한 결과 입니다.
                </div>
                <table>
                  <colgroup>
                    <col style="width: 30%" />
                    <col />
                  </colgroup>
                  <tr>
                    <th>확장자</th>
                    <td>PDF</td>
                  </tr>
                  <tr>
                    <th>전체 행</th>
                    <td>10000</td>
                  </tr>
                </table>
              </div>
              <div class="preview-item">
                <div class="preview-title">태그</div>
                <div class="preview-group">
                  <div
                    class="tag tag-primary tag-sm"
                    v-for="tag in 8"
                    :key="tag"
                  >
                    <span class="tag-text">tag</span>
                  </div>
                </div>
              </div>
              <div class="preview-item">
                <div class="preview-title">용어</div>
                <div class="preview-group">
                  <div class="tag tag-primary tag-sm">
                    <span class="tag-text">관련 용어</span>
                  </div>
                </div>
              </div>
              <div class="preview-item">
                <div class="preview-title">상세 설명</div>
                <div class="preview-desc">상세 설명을 하는 구간입니다.</div>
              </div>
              <div class="preview-item">
                <div class="preview-title">URL</div>
                <a href="#" class="preview-link">
                  https://sandbox.open-metadata.org/callback#state=49e9675588d3414f9585fb455e
                </a>
              </div>
              <div class="preview-item">
                <div class="preview-title">스키마</div>
                <div class="v-group gap-2 w-full">
                  <div class="preview-schema">
                    <div class="h-group gap-1">
                      <span class="schema-title">payment_id</span>
                      <span class="schema-subtitle">(INT)</span>
                    </div>
                    <div class="schema-desc">schema description</div>
                  </div>
                  <div class="preview-schema">
                    <div class="h-group gap-1">
                      <span class="schema-title">payment_id</span>
                      <span class="schema-subtitle">(INT)</span>
                    </div>
                    <div class="schema-desc">schema description</div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <!-- TODO : [개발] 정보가 없을때-->
        <div class="no-result" style="display: none">
          <div class="notification">
            <svg-icon class="notification-icon" name="info"></svg-icon>
            <p class="notification-detail">등록된 정보가 없습니다.</p>
          </div>
        </div>
      </div>
    </div>
  </div>
  <PwResetModal :modal-id="PW_RESET_MODAL_ID"></PwResetModal>
</template>

<script setup lang="ts">
import { useNuxtApp } from "nuxt/app";
const { $vfm } = useNuxtApp();
import profileBox from "~/components/my-page/profile-box.vue"
const PW_RESET_MODAL_ID: string = "pw-reset-modal";

const showModalPwChange: () => void = () => {
  console.log(PW_RESET_MODAL_ID);
  $vfm.open(PW_RESET_MODAL_ID);
};
</script>

<style scoped></style>
