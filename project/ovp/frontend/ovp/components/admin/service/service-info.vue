<template>
  <div class="work-page">
    <div class="l-top-bar">
      <div class="h-group gap-2">
        <svg-icon class="svg-icon menu-data-icon" name="resource"></svg-icon>
        <h4 class="service-title">서비스A</h4>
      </div>
      <button class="button button-error-lighter">삭제</button>
    </div>
    <div class="work-contents">
      <div class="v-group gap-4">
        <div class="h-group gap-2">
          <div class="text-neutral-700 font-semibold w-14">소유자</div>
          <div class="editable-group w-auto">
            <div class="text-neutral-700">owner</div>
            <button class="button button-neutral-ghost button-sm" type="button">
              <span class="hidden-text">수정</span>
              <svg-icon class="button-icon" name="pen"></svg-icon>
            </button>
          </div>
        </div>
        <div class="h-group gap-8">
          <div class="h-group gap-2">
            <div class="font-semibold text-neutral-700 w-14">태그</div>
            <div class="editable-group w-auto">
              <div class="tag tag-primary tag-sm">
                <span class="tag-text">tag</span>
              </div>
              <div class="tag tag-primary tag-sm">
                <span class="tag-text">DATA-tag</span>
              </div>
              <button
                class="button button-neutral-ghost button-sm"
                type="button"
              >
                <span class="hidden-text">수정</span>
                <svg-icon class="button-icon" name="pen"></svg-icon>
              </button>
            </div>
          </div>
          <div class="h-group gap-2">
            <div class="font-semibold text-neutral-700 w-14">용어</div>
            <div class="editable-group w-auto">
              <div class="tag tag-primary tag-sm">
                <span class="tag-text">term 01</span>
              </div>
              <div class="tag tag-primary tag-sm">
                <span class="tag-text">term 04</span>
              </div>
              <button
                class="button button-neutral-ghost button-sm"
                type="button"
              >
                <span class="hidden-text">수정</span>
                <svg-icon class="button-icon" name="pen"></svg-icon>
              </button>
            </div>
          </div>
        </div>
      </div>
      <div class="tab tab-line">
        <ul class="tab-list">
          <li class="tab-item is-tab-item-selected">
            <button class="tab-button">
              <p class="tab-button-text">저장소</p>
            </button>
          </li>
          <li class="tab-item">
            <button class="tab-button">
              <p class="tab-button-text">수집</p>
            </button>
          </li>
          <li class="tab-item">
            <button class="tab-button">
              <p class="tab-button-text">연결정보</p>
            </button>
          </li>
        </ul>
      </div>
      <!-- 저장소 탭 시작 -->
      <div class="px-4">
        <div class="v-group gap-2">
          <div class="font-semibold text-neutral-700">설명</div>
          <div class="editable-group">
            <span class="editable-group-desc">
              서비스 A에 대한 설명이 기재됩니다.
            </span>
            <button class="button button-neutral-ghost button-sm" type="button">
              <span class="hidden-text">수정</span>
              <svg-icon class="svg-icon" name="pen"></svg-icon>
            </button>
          </div>
        </div>
        <table class="mt-4">
          <colgroup>
            <col />
            <col />
            <col style="width: 35%" />
          </colgroup>
          <tr>
            <th>구분</th>
            <th class="align-center">이름</th>
            <th>설명</th>
            <th>소유자</th>
            <th>USAGE</th>
          </tr>
          <tr v-for="tr in 3" :key="tr">
            <td>DB기본정보</td>
            <td>
              <a
                href=""
                class="link-button link-button link-button-underline"
                title="상세 페이지 이동"
                >DB Table1</a
              >
            </td>
            <td>DB Table에 대한 설명입니다.</td>
            <td>-</td>
            <td>0th pctile</td>
          </tr>
        </table>
        <!-- 결과 없을 시 no-result 표시 -->
        <div class="no-result max-h-40" style="display: none">
          <div class="notification">
            <svg-icon class="notification-icon" name="info"></svg-icon>
            <p class="notification-detail">데이터 리스트가 없습니다.</p>
          </div>
        </div>
      </div>
      <!-- 저장소 탭 끝 -->
      <!-- 수집 탭 시작 -->
      <div class="px-4">
        <div class="l-between">
          <div class="search-input">
            <label class="hidden-text" for="text-input-example-11">label</label>
            <input
              id="text-input-example-11"
              class="text-input"
              placeholder="검색어 입력"
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
          <!--  TODO: [개발] 수집추가 모달을 메타데이터 수집추가, 프로파일러 수집추가 드롭다운 메뉴에 추가 필요, dropdown의 위치를 right:0으로 조정 필요  -->
          <select-box class="" @click="showModalCollection = true"
            >수집추가</select-box
          >
        </div>
        <table class="mt-3">
          <colgroup>
            <col />
            <col />
            <col style="width: 30%" />
          </colgroup>
          <tr>
            <th>이름</th>
            <th>유형</th>
            <th>스케줄</th>
            <th class="align-center">현황</th>
            <th class="align-center">동작</th>
          </tr>
          <tr>
            <td>pgv2_metadata_ingestion</td>
            <td>metadata</td>
            <td>00***</td>
            <td>
              <div class="badge badge-green-lighter">
                <p class="badge-text">Success</p>
              </div>
            </td>
            <td>
              <div class="button-group">
                <button class="button button button-secondary-stroke">
                  실행
                </button>
                <button class="button button button-secondary-stroke">
                  동기화
                </button>
                <button class="button button button-secondary-stroke">
                  편집
                </button>
                <button class="button button button-error-stroke">삭제</button>
                <button class="button button button-error-stroke">종료</button>
                <button
                  class="button button button-neutral-stroke"
                  @click="showModalLog = true"
                >
                  로그
                </button>
              </div>
            </td>
          </tr>
          <tr>
            <td>pgv2_metadata_ingestion</td>
            <td>metadata</td>
            <td>00***</td>
            <td>
              <div class="badge badge-red-lighter">
                <p class="badge-text">Fail</p>
              </div>
            </td>
            <td>
              <div class="button-group">
                <button class="button button button-secondary-stroke">
                  실행
                </button>
                <button class="button button button-secondary-stroke">
                  동기화
                </button>
                <button class="button button button-secondary-stroke">
                  편집
                </button>
                <button class="button button button-error-stroke">삭제</button>
                <button class="button button button-error-stroke">종료</button>
                <button class="button button button-neutral-stroke">
                  로그
                </button>
              </div>
            </td>
          </tr>
        </table>
        <!-- 결과 없을 시 no-result 표시 -->
        <div class="no-result max-h-40">
          <div class="notification">
            <svg-icon class="notification-icon" name="info"></svg-icon>
            <p class="notification-detail">데이터 리스트가 없습니다.</p>
          </div>
        </div>
      </div>
      <!-- 수집 탭 끝 -->
      <!-- 연결정보 탭 시작 -->
      <div class="px-4">
        <div class="flex justify-end gap-2">
          <button class="button button-neutral-stroke">연결정보 수정</button>
          <button class="button button-secondary">연결 테스트</button>
        </div>
        <table class="mt-3">
          <colgroup>
            <col style="width: 20%" />
            <col />
          </colgroup>
          <tr>
            <th>scheme</th>
            <td>mysal+pymsal</td>
          </tr>
          <tr>
            <th>username</th>
            <td>root</td>
          </tr>
          <tr>
            <th>password</th>
            <td>******</td>
          </tr>
          <tr>
            <th>hostPort</th>
            <td>192.168.105.81:30001</td>
          </tr>
          <tr>
            <th>databaseName</th>
            <td>ABC</td>
          </tr>
          <tr>
            <th>databaseSchema</th>
            <td></td>
          </tr>
          <tr>
            <th>prefix</th>
            <td></td>
          </tr>
          <tr>
            <th>bucketName</th>
            <td></td>
          </tr>
          <tr>
            <th>overwirteData</th>
            <td>true</td>
          </tr>
          <tr>
            <th>filePathPattern</th>
            <td></td>
          </tr>
        </table>
        <!-- 결과 없을 시 no-result 표시 -->
        <div class="no-result max-h-40" style="display: none">
          <div class="notification">
            <svg-icon class="notification-icon" name="info"></svg-icon>
            <p class="notification-detail">데이터 리스트가 없습니다.</p>
          </div>
        </div>
      </div>
      <!-- 연결정보 탭 끝 -->
    </div>
  </div>
  <!-- 결과 없을 시 no-result 표시 / 기본 .work-page로 컨텐츠 표시 -->
  <div class="no-result" style="display: none">
    <div class="notification">
      <svg-icon class="notification-icon" name="info"></svg-icon>
      <p class="notification-detail">등록된 서비스가 없습니다.</p>
    </div>
  </div>
</template>

<script>
import { defineComponent } from "vue";
import SelectBox from "@extends/select-box/SelectBox.vue";

export default defineComponent({
  components: { SelectBox },
});
</script>
