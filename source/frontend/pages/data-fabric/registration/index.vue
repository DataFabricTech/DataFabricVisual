<!-- 저장소 관리 > 연결정보 등록 Modal -->
<template>
  <div class="v-group">
    <h3>저장소 관리 > 연결정보 등록</h3>
    <baseButton class="button-lg button-primary" @click="clickOpen">모달 OPEN</baseButton>

    <modal v-model="showModal" title="연결정보 등록">
      <template v-slot:body>
        <div class="modal-body w-[900px] p-6 items-center">
          <!--<BaseStep :step="obj" :testTitle="obj.toString()"></BaseStep>-->
          <div class="step">
            <div class="step-item" v-for="(item, index) in titles"
                 :key="index" :class="[index + 1 === step ? 'is-current':'']">
              <div class="step-icon">
                <span class="step-icon-text">
                  {{ index + 1 }}
                </span>
              </div>
              <div class="step-title">
                <span class="step-title-text">
                  {{ item }}
                </span>
              </div>
            </div>
          </div>
          <div class="step-info" v-show="step === 1">
            <h4 class="step-info-title">{{ title }}</h4>
            <SearchField class="search-field-lg w-[440px]"></SearchField>
            <ul class="storage-type">
              <li class="storage-type-item">
                <button class="storage-type-button">
                  <div class="storage-type-img">
                    <img src="../../../assets/images/storage-type_01.png" alt="mariaDB">
                  </div>
                  <span class="storage-type-item-title">MariaDB</span>
                </button>
              </li>
              <!-- item 요소 선택시 is-selected 클래스 추가 필요 -->
              <li class="storage-type-item is-selected">
                <button class="storage-type-button">
                  <div class="storage-type-img">
                    <img src="../../../assets/images/storage-type_02.png" alt="MySQL">
                  </div>
                  <span class="storage-type-item-title">MySQL</span>
                </button>
              </li>
              <!-- 필요시 is-disabled 사용가능 -->
              <li class="storage-type-item is-disabled">
                <button class="storage-type-button">
                  <div class="storage-type-img">
                    <img src="../../../assets/images/storage-type_03.png" alt="PostgreSQL">
                  </div>
                  <span class="storage-type-item-title">PostgreSQL</span>
                </button>
              </li>
              <li class="storage-type-item">
                <button class="storage-type-button">
                  <div class="storage-type-img">
                    <img src="../../../assets/images/storage-type_04.png" alt="Oracle">
                  </div>
                  <span class="storage-type-item-title">Oracle</span>
                </button>
              </li>
              <li class="storage-type-item">
                <button class="storage-type-button">
                  <div class="storage-type-img">
                    <img src="../../../assets/images/storage-type_05.png" alt="mariaDB">
                  </div>
                  <span class="storage-type-item-title">Hadoop</span>
                </button>
              </li>
              <li class="storage-type-item">
                <button class="storage-type-button">
                  <div class="storage-type-img">
                    <img src="../../../assets/images/storage-type_06.png" alt="MinIO">
                  </div>
                  <span class="storage-type-item-title">MinIO</span>
                </button>
              </li>
              <li class="storage-type-item">
                <button class="storage-type-button">
                  <div class="storage-type-img">
                    <img src="../../../assets/images/storage-type_07.png" alt="IRIS">
                  </div>
                  <span class="storage-type-item-title">IRIS</span>
                </button>
              </li>
              <li class="storage-type-item">
                <button class="storage-type-button">
                  <div class="storage-type-img">
                    <img src="../../../assets/images/storage-type_08.png" alt="MongoDB">
                  </div>
                  <span class="storage-type-item-title">MongoDB</span>
                </button>
              </li>
              <li class="storage-type-item">
                <button class="storage-type-button">
                  <div class="storage-type-img">
                    <img src="../../../assets/images/storage-type_06.png" alt="sample-01">
                  </div>
                  <span class="storage-type-item-title">sample-01</span>
                </button>
              </li>
              <li class="storage-type-item">
                <button class="storage-type-button">
                  <div class="storage-type-img">
                    <img src="../../../assets/images/storage-type_07.png" alt="sample-02">
                  </div>
                  <span class="storage-type-item-title">sample-02 타이틀이 길어질때 말줄임 처리</span>
                </button>
              </li>
              <li class="storage-type-item">
                <button class="storage-type-button">
                  <div class="storage-type-img">
                    <img src="../../../assets/images/storage-type_08.png" alt="sample-03">
                  </div>
                  <span class="storage-type-item-title">sample-03</span>
                </button>
              </li>
            </ul>
          </div>
          <div class="step-info" v-show="step === 2">
            <form action="" class="form form-vertical">
              <h4 class="form-subject">{{ title }}</h4>
              <div class="form-list w-full">
                <div class="form-item">
                  <div class="form-label">
                    <strong class="form-title">저장소 유형</strong>
                  </div>
                  <div class="form-content">
                    <p class="form-text">MySQL</p>
                  </div>
                </div>
                <div class="form-item">
                  <div class="form-label">
                    <strong class="form-title">연결정보 이름</strong>
                    <span class="form-label-accent">*</span>
                  </div>
                  <div class="form-content">
                    <label for="input-ex-01" class="hidden-text">연결정보 이름</label>
                    <BaseTextInput id="input-ex-01" class="w-[522px] text-input-lg"
                                   placeholder="이름을 입력해주세요."></BaseTextInput>
                  </div>
                </div>
                <div class="form-item w-full">
                  <div class="form-label">
                    <strong class="form-title">연결정보 설명</strong>
                  </div>
                  <div class="form-content w-full">
                    <label for="input-ex-02" class="hidden-text">연결정보 설명</label>
                    <baseTextarea id="input-ex-02" class="w-full h-48" placeholder="내용을 입력해주세요."></baseTextarea>
                  </div>
                </div>
              </div>
              <h4 class="form-subject">사용자 추가 정보 입력</h4>
              <div class="form-list">
                <div class="form-item">
                  <div class="form-content">
                    <div class="v-group gap-2">
                      <div class="h-group gap-2">
                        <label for="input-ex-03" class="hidden-text">lable</label>
                        <BaseTextInput id="input-ex-03" class="w-[222px] text-input-lg"
                                       placeholder="Key를 입력해주세요."></BaseTextInput>
                        <label for="input-ex-04" class="hidden-text">lable</label>
                        <BaseTextInput id="input-ex-04" class="w-[460px] text-input-lg"
                                       placeholder="Value를 입력해주세요."></BaseTextInput>
                        <baseButton class="button-icon button-ghost button-lg">
                          <svg-icon name="plus" class="svg-icon" />
                          <span class="hidden-text">추가</span>
                        </baseButton>
                      </div>
                      <div class="h-group gap-2">
                        <label for="input-ex-05" class="hidden-text">lable</label>
                        <BaseTextInput id="input-ex-05" class="w-[222px] text-input-lg"
                                       placeholder="Key를 입력해주세요."></BaseTextInput>
                        <label for="input-ex-06" class="hidden-text">lable</label>
                        <BaseTextInput id="input-ex-06" class="w-[460px] text-input-lg"
                                       placeholder="Value를 입력해주세요."></BaseTextInput>
                        <baseButton class="button-icon button-danger button-lg">
                          <svg-icon name="minus" class="svg-icon" />
                          <span class="hidden-text">삭제</span>
                        </baseButton>
                        <baseButton class="button-icon button-ghost button-lg">
                          <svg-icon name="plus" class="svg-icon" />
                          <span class="hidden-text">추가</span>
                        </baseButton>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
              <h4 class="form-subject">Tag 입력</h4>
              <div class="form-list w-full">
                <div class="form-item w-full">
                  <div class="form-content w-full">
                    <label for="input-ex-07" class="hidden-text">Tag 입력</label>
                    <BaseTextInput id="area-ex-07" class="w-full text-input-lg"
                                   placeholder="Tag를 입력해주세요."></BaseTextInput>
                  </div>
                </div>
              </div>
            </form>
          </div>
          <div class="step-info" v-show="step === 3">
            <h4 class="step-info-title">{{ title }}</h4>
            <div class="description">
              <dl>
                <dt>저장소 유형</dt>
                <dd>MySQL</dd>
              </dl>
              <dl>
                <dt>연결정보 이름</dt>
                <dd>MySQL_DB</dd>
              </dl>
            </div>
            <div class="step-info-inner w-full">
              <form action="" class="form">
                <div class="form-list w-full">
                  <div class="form-item w-full">
                    <div class="form-label">
                      <strong class="form-title">host</strong>
                      <span class="form-label-accent">*</span>
                    </div>
                    <div class="form-content w-full">
                      <label for="input-ex-01" class="hidden-text">host</label>
                      <BaseTextInput id="input-ex-01" class="w-full text-input-lg"
                                     placeholder="내용을 입력해주세요."></BaseTextInput>
                    </div>
                  </div>
                  <div class="form-item w-full">
                    <div class="form-label">
                      <strong class="form-title">port</strong>
                      <span class="form-label-accent">*</span>
                    </div>
                    <div class="form-content w-full">
                      <label for="input-ex-02" class="hidden-text">port</label>
                      <BaseTextInput id="input-ex-02" class="w-full text-input-lg"
                                     placeholder="내용을 입력해주세요."></BaseTextInput>
                    </div>
                  </div>
                  <div class="form-item w-full">
                    <div class="form-label">
                      <strong class="form-title">database</strong>
                      <span class="form-label-accent">*</span>
                    </div>
                    <div class="form-content w-full">
                      <label for="input-ex-03" class="hidden-text">database</label>
                      <BaseTextInput id="input-ex-03" class="w-full text-input-lg"
                                     placeholder="내용을 입력해주세요."></BaseTextInput>
                    </div>
                  </div>
                  <div class="form-item w-full">
                    <div class="form-label">
                      <strong class="form-title">user</strong>
                      <span class="form-label-accent">*</span>
                    </div>
                    <div class="form-content w-full">
                      <label for="input-ex-04" class="hidden-text">user</label>
                      <BaseTextInput id="input-ex-04" class="w-full text-input-lg"
                                     placeholder="내용을 입력해주세요."></BaseTextInput>
                    </div>
                  </div>
                  <div class="form-item w-full">
                    <div class="form-label">
                      <strong class="form-title">password</strong>
                      <span class="form-label-accent">*</span>
                    </div>
                    <div class="form-content w-full">
                      <label for="input-ex-05" class="hidden-text">password</label>
                      <BaseTextInput id="input-ex-05" class="w-full text-input-lg"
                                     placeholder="내용을 입력해주세요."></BaseTextInput>
                    </div>
                  </div>
                </div>
              </form>
              <baseButton class="button-ghost button-lg ml-auto flex-shrink-0">
                <span class="button-text">연결 테스트</span>
              </baseButton>
            </div>
          </div>
          <div class="step-info" v-show="step === 4">
            <h4 class="step-info-title">{{ title }}</h4>
            <div class="v-group w-full gap-2">
              <baseSwitch>
                <template v-slot:switch-text>
                  데이터 자동 추가 설정
                </template>
              </baseSwitch>
              <div class="w-full table-col">
                <table class="w-full table-nesting">
                  <caption class="caption-out">
                    Description Column
                  </caption>
                  <colgroup>
                    <col width="129px">
                    <col>
                  </colgroup>
                  <thead>
                  <tr>
                    <!-- 세로 => col, 가로 => row -->
                    <th scope="col">항목</th>
                    <th scope="col">내용</th>
                  </tr>
                  </thead>
                  <tbody>
                  <!-- 플러스 항목 추가시 여기서부터 <tr> 반복됩니다 -->
                  <tr>
                    <td colspan="2">
                      <div class="h-group w-full gap-2">
                        <table class="w-full">
                          <colgroup>
                            <col width="120px">
                            <col>
                          </colgroup>
                          <tbody>
                          <tr>
                            <td>Datetime</td>
                            <td>
                              <div class="h-group">
                                <baseSelect></baseSelect>
                                <DatePicker
                                  class="date-picker"
                                  v-model:modelValue="args.date"
                                  :type="args.date">
                                </DatePicker>
                                <span class="px-2 color-font-gray-01">~</span>
                                <DatePicker
                                  class="date-picker"
                                  v-model:modelValue="args.date"
                                  :type="args.date">
                                </DatePicker>
                              </div>
                            </td>
                          </tr>
                          <tr>
                            <td>Data Name</td>
                            <td>
                              <baseTextInput placeholder="정규 표현식을 입력하세요(50자이내)"></baseTextInput>
                            </td>
                          </tr>
                          <tr>
                            <td>Type / Size</td>
                            <td>
                              <div class="v-group">
                                <div class="h-group">
                                  <strong class="pr-2">(table)</strong>
                                  <span class="color-font-gray-01">최소</span>
                                  <baseTextInput class="w-[100px]"></baseTextInput>
                                  <span>row</span>
                                  <span class="px-2">~</span>
                                  <span>최대</span>
                                  <baseTextInput class="w-[100px]"></baseTextInput>
                                  <span>row</span>
                                </div>
                              </div>
                            </td>
                          </tr>
                          </tbody>
                        </table>
                        <div class="h-group">
                          <baseButton class="button-icon button-danger" disabled>
                            <svg-icon name="minus" class="svg-icon" />
                            <span class="hidden-text">삭제</span>
                          </baseButton>
                          <baseButton class="button-icon button-ghost">
                            <svg-icon name="plus" class="svg-icon" />
                            <span class="hidden-text">추가</span>
                          </baseButton>
                        </div>
                      </div>
                    </td>
                  </tr>
                  <tr>
                    <td colspan="2">
                      <div class="h-group w-full gap-2">
                        <table class="w-full">
                          <colgroup>
                            <col width="120px">
                            <col>
                          </colgroup>
                          <tbody>
                          <tr>
                            <td>Datetime</td>
                            <td>
                              <div class="h-group">
                                <baseSelect></baseSelect>
                                <DatePicker
                                  class="date-picker"
                                  v-model:modelValue="args.dateRange"
                                  :type="args.rangeType">
                                </DatePicker>
                              </div>
                            </td>
                          </tr>
                          <tr>
                            <td>Data Name</td>
                            <td>
                              <baseTextInput placeholder="정규 표현식을 입력하세요(50자이내)"></baseTextInput>
                            </td>
                          </tr>
                          <tr>
                            <td>Type / Size</td>
                            <td>
                              <div class="v-group">
                                <div class="h-group">
                                  <strong class="pr-2">(table)</strong>
                                  <span class="color-font-gray-01">최소</span>
                                  <baseTextInput class="w-[100px]"></baseTextInput>
                                  <span>row</span>
                                  <span class="px-2">~</span>
                                  <span>최대</span>
                                  <baseTextInput class="w-[100px]"></baseTextInput>
                                  <span>row</span>
                                </div>
                              </div>
                            </td>
                          </tr>
                          </tbody>
                        </table>
                        <div class="h-group">
                          <baseButton class="button-icon button-danger">
                            <svg-icon name="minus" class="svg-icon" />
                            <span class="hidden-text">삭제</span>
                          </baseButton>
                          <baseButton class="button-icon button-ghost">
                            <svg-icon name="plus" class="svg-icon" />
                            <span class="hidden-text">추가</span>
                          </baseButton>
                        </div>
                      </div>
                    </td>
                  </tr>
                  </tbody>
                </table>
              </div>
            </div>
            <div class="v-group w-full gap-2">
              <baseSwitch>
                <template v-slot:switch-text>
                  동기화 설정
                </template>
              </baseSwitch>
              <div class="table-col w-full">
                <table class="w-full">
                  <caption class="caption-out">
                    Description Column
                  </caption>
                  <colgroup>
                    <col width="150px">
                    <col>
                  </colgroup>
                  <thead>
                  <tr>
                    <!-- 세로 => col, 가로 => row -->
                    <th scope="col">항목</th>
                    <th scope="col">내용</th>
                  </tr>
                  </thead>
                  <tbody>
                  <tr>
                    <td>동기화 주기</td>
                    <td>
                      <div class="v-group">
                        <baseSelect></baseSelect>
                        <div class="h-group">
                          <baseRadio id="radio-lg-01"></baseRadio>
                          <baseTextInput class="w-[100px]"></baseTextInput>
                          <span>일 마다</span>
                        </div>
                        <div class="h-group">
                          <baseRadio id="radio-lg-02"></baseRadio>
                          <div class="toggle-token">
                            <BaseCheckbox name="toggleLg" id="toggle-lg-01" checked>
                              월요일
                            </BaseCheckbox>
                            <BaseCheckbox name="toggleLg" id="toggle-lg-02">
                              화요일
                            </BaseCheckbox>
                            <BaseCheckbox name="toggleLg" id="toggle-lg-03">
                              수요일
                            </BaseCheckbox>
                            <BaseCheckbox name="toggleLg" id="toggle-lg-04">
                              목요일
                            </BaseCheckbox>
                            <BaseCheckbox name="toggleLg" id="toggle-lg-05">
                              금요일
                            </BaseCheckbox>
                            <BaseCheckbox name="toggleLg" id="toggle-lg-06">
                              토요일
                            </BaseCheckbox>
                            <BaseCheckbox name="toggleLg" id="toggle-lg-07">
                              일요일
                            </BaseCheckbox>
                          </div>
                        </div>
                      </div>
                    </td>
                  </tr>
                  <tr>
                    <td>동기화 시작 일시</td>
                    <td>
                      <DatePicker class="date-picker"></DatePicker>
                    </td>
                  </tr>
                  </tbody>
                </table>
              </div>
            </div>
            <div class="v-group w-full gap-2">
              <baseSwitch>
                <template v-slot:switch-text>
                  모니터링 설정
                </template>
              </baseSwitch>
              <div class="table-col w-full">
                <table class="w-full">
                  <caption class="caption-out">
                    Description Column
                  </caption>
                  <colgroup>
                    <col width="150px">
                    <col>
                  </colgroup>
                  <thead>
                  <tr>
                    <!-- 세로 => col, 가로 => row -->
                    <th scope="col">항목</th>
                    <th scope="col">내용</th>
                  </tr>
                  </thead>
                  <tbody>
                  <tr>
                    <td>Protocol</td>
                    <td>
                      <div class="h-group">
                        <baseRadio id="radio-lg-03">TCP</baseRadio>
                        <baseRadio id="radio-lg-04">UDP</baseRadio>
                        <baseRadio id="radio-lg-05">ICMP</baseRadio>
                        <baseRadio id="radio-lg-06">SQL</baseRadio>
                      </div>
                    </td>
                  </tr>
                  <tr>
                    <td>Port</td>
                    <td>
                      <baseTextInput placeholder="port를 입력하세요"></baseTextInput>
                    </td>
                  </tr>
                  <tr>
                    <td>Timeout</td>
                    <td>
                      <div class="h-group">
                        <baseRadio id="radio-lg-01"></baseRadio>
                        <baseTextInput class="w-[70px]"></baseTextInput>
                        <span>초(sec)</span>
                      </div>
                    </td>
                  </tr>
                  <tr>
                    <td>정상 임계 값</td>
                    <td>
                      <div class="h-group">
                        <baseRadio id="radio-lg-01"></baseRadio>
                        <baseTextInput class="w-[70px]"></baseTextInput>
                        <span>회(sec)</span>
                      </div>
                    </td>
                  </tr>
                  <tr>
                    <td>비정상 임계 값</td>
                    <td>
                      <div class="h-group">
                        <baseRadio id="radio-lg-01"></baseRadio>
                        <baseTextInput class="w-[70px]"></baseTextInput>
                        <span>회(sec)</span>
                      </div>
                    </td>
                  </tr>
                  </tbody>
                </table>
              </div>
            </div>
          </div>
        </div>
      </template>
      <template v-slot:foot>
        <baseButton class="button-normal button-lg" @click="clickClose">
          <span class="button-text">취소</span>
        </baseButton>
        <div class="h-group ml-auto">
          <baseButton class="button-ghost button-lg" v-show="step !== 1" @click="clickPrev">
            <span class="button-text">이전</span>
          </baseButton>
          <baseButton class="button-primary button-lg" v-show="step !== 4" @click="clickNext">
            <span class="button-text">다음</span>
          </baseButton>
          <baseButton class="button-primary button-lg" v-show="step === 4" @click="clickSave">
            <span class="button-text">저장</span>
          </baseButton>
        </div>
      </template>
    </modal>
  </div>
</template>
<script lang="ts" setup>
import { ref } from "vue";

definePageMeta({
  layout: "default-tab-layout"
});

const showModal = ref(false);

let args = {
  date: "2023-11-22",
  dateRange: ["2023-11-10", "2023-11-22"],
  // storybook 에 object 형식으로 뜸
  dateType: "time",
  rangeType: "datetime"
};

function clickOpen() {
  showModal.value = true;
  step.value = 1;
}

// modal
const step = ref(1);
const obj = reactive({ step });

const titles = ["저장소 유형 선택", "기본 정보 입력", "상세 정보 입력", "추가 정보 입력"];
const title = computed(() => {
  return titles.at(step.value - 1);
});

function addStep() {
  obj.step++;
}

function subStep() {
  obj.step--;
}

function clickClose() {
  console.log("close");
  showModal.value = false;
}

function clickPrev() {
  console.log("prev");
  if (obj.step > 1) {
    subStep();
  }
}

function clickNext() {
  console.log("next");
  if (obj.step < 4) {
    addStep();
  }
}

function clickSave() {
  console.log("save");
  alert("저장되었습니다.");
  showModal.value = false;
}
</script>
