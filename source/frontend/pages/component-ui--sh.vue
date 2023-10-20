<template>
  <div class="layout-guide">
    <div class="layout-guide-title">
      <h1>Fabric UI Components</h1>
    </div>
    <table class="guide-table">
      <tbody>
      <tr>
        <th scope="row">Badge</th>
      </tr>
      <tr>
        <td>
          <div class="h-group">
            <strong class="w-[150px] font-light text-[14px] shrink-0">type</strong>
            <baseBadge class="bg-marker-purple">Badge</baseBadge>
            <baseBadge class="bg-marker-cyan">Badge</baseBadge>
            <baseBadge class="bg-marker-red">Badge</baseBadge>
            <baseBadge class="bg-marker-yellow">Badge</baseBadge>
            <baseBadge class="bg-marker-gray">Badge</baseBadge>
          </div>
        </td>
      </tr>
      <tr>
        <th scope="row">Button</th>
      </tr>
      <tr>
        <td>
          <div class="v-group gap-5">
            <div class="h-group">
              <strong class="w-[150px] font-light text-[14px] shrink-0">size: lg</strong>
              <baseButton title="사용자 관리" class="button-primary button-lg">
                <svg-icon name="user" class="svg-icon" />
                <span class="button-text">Button</span>
              </baseButton>
              <baseButton title="사용자 관리" class="button-secondary button-lg">
                <svg-icon name="user" class="svg-icon" />
                <span class="button-text">Button</span>
              </baseButton>
              <baseButton title="사용자 관리" class="button-tertiary button-lg">
                <svg-icon name="user" class="svg-icon" />
                <span class="button-text">Button</span>
              </baseButton>
              <baseButton class="button-negative button-lg">
                <span class="button-text">Button</span>
              </baseButton>
              <baseButton class="button-normal button-lg">
                <span class="button-text">Button</span>
              </baseButton>
              <baseButton class="button-danger button-lg">
                <span class="button-text">Button</span>
              </baseButton>
              <baseButton title="사용자 관리" class="button-icon button-link button-lg">
                <svg-icon name="user" class="svg-icon" />
                <span class="hidden">사용자 관리</span>
              </baseButton>
              <baseButton title="사용자 관리" class="button-icon button-link-primary button-lg">
                <svg-icon name="user" class="svg-icon" />
                <span class="hidden">사용자 관리</span>
              </baseButton>
              <baseButton title="사용자 관리" class="button-icon button-link-negative button-lg">
                <svg-icon name="user" class="svg-icon" />
                <span class="hidden">사용자 관리</span>
              </baseButton>
              <baseButton title="사용자 관리" class="button-icon button-link-primary button-lg" disabled>
                <svg-icon name="user" class="svg-icon" />
                <span class="hidden">Disabled</span>
              </baseButton>
            </div>
            <div class="h-group">
              <strong class="w-[150px] font-light text-[14px] shrink-0">size: default</strong>
              <baseButton title="사용자 관리" class="button-primary">
                <svg-icon name="user" class="svg-icon" />
                <span class="button-text">Button</span>
              </baseButton>
              <baseButton title="사용자 관리" class="button-secondary">
                <svg-icon name="user" class="svg-icon" />
                <span class="button-text">Button</span>
              </baseButton>
              <baseButton title="사용자 관리" class="button-tertiary">
                <svg-icon name="user" class="svg-icon" />
                <span class="button-text">Button</span>
              </baseButton>
              <baseButton class="button-negative">
                <span class="button-text">Button</span>
              </baseButton>
              <baseButton class="button-normal">
                <span class="button-text">Button</span>
              </baseButton>
              <baseButton class="button-danger">
                <span class="button-text">Button</span>
              </baseButton>
              <baseButton title="사용자 관리" class="button-icon button-link">
                <svg-icon name="user" class="svg-icon" />
                <span class="hidden">사용자 관리</span>
              </baseButton>
              <baseButton title="사용자 관리" class="button-icon button-link-primary">
                <svg-icon name="user" class="svg-icon" />
                <span class="hidden">사용자 관리</span>
              </baseButton>
              <baseButton title="사용자 관리" class="button-icon button-link-negative">
                <svg-icon name="user" class="svg-icon" />
                <span class="hidden">사용자 관리</span>
              </baseButton>
              <baseButton title="사용자 관리" class="button-primary" disabled>
                <svg-icon name="user" class="svg-icon" />
                <span class="button-text">Disabled</span>
              </baseButton>
            </div>
            <div class="h-group">
              <strong class="w-[150px] font-light text-[14px] shrink-0">size: sm</strong>
              <baseButton title="사용자 관리" class="button-primary button-sm">
                <svg-icon name="user" class="svg-icon" />
                <span class="button-text">Button</span>
              </baseButton>
              <baseButton title="사용자 관리" class="button-secondary button-sm">
                <svg-icon name="user" class="svg-icon" />
                <span class="button-text">Button</span>
              </baseButton>
              <baseButton title="사용자 관리" class="button-tertiary button-sm">
                <svg-icon name="user" class="svg-icon" />
                <span class="button-text">Button</span>
              </baseButton>
              <baseButton class="button-negative button-sm">
                <span class="button-text">Button</span>
              </baseButton>
              <baseButton class="button-normal button-sm">
                <span class="button-text">Button</span>
              </baseButton>
              <baseButton class="button-danger button-sm">
                <span class="button-text">Button</span>
              </baseButton>
              <baseButton title="사용자 관리" class="button-icon button-link button-sm">
                <svg-icon name="user" class="svg-icon" />
                <span class="hidden">사용자 관리</span>
              </baseButton>
              <baseButton title="사용자 관리" class="button-icon button-link-primary button-sm">
                <svg-icon name="user" class="svg-icon" />
                <span class="hidden">사용자 관리</span>
              </baseButton>
              <baseButton title="사용자 관리" class="button-icon button-link-negative button-sm">
                <svg-icon name="user" class="svg-icon" />
                <span class="hidden">사용자 관리</span>
              </baseButton>
              <baseButton title="사용자 관리" class="button-danger button-sm" disabled>
                <svg-icon name="user" class="svg-icon" />
                <span class="button-text">Disabled</span>
              </baseButton>
            </div>
          </div>
        </td>
      </tr>
      <tr>
        <th scope="row">Checkbox</th>
      </tr>
      <tr>
        <td>
          <div class="v-group gap-5">
            <div class="h-group">
              <strong class="w-[150px] font-light text-[14px] shrink-0">size: lg</strong>
              <div class="h-group gap-12">
                <baseCheckbox class="checkbox-lg" id="lg-01">default</baseCheckbox>
                <baseCheckbox class="checkbox-lg" id="lg-02" checked>checked</baseCheckbox>
                <baseCheckbox class="checkbox-lg checkbox-indeterminate" id="lg-03">indeterminate</baseCheckbox>
                <baseCheckbox class="checkbox-lg" id="lg-04" disabled>disabled</baseCheckbox>
              </div>
            </div>
            <div class="h-group">
              <strong class="w-[150px] font-light text-[14px] shrink-0">size: default</strong>
              <div class="h-group gap-12">
                <baseCheckbox id="01">default</baseCheckbox>
                <baseCheckbox id="02" checked>checked</baseCheckbox>
                <baseCheckbox class="checkbox-indeterminate" id="03">indeterminate</baseCheckbox>
                <baseCheckbox id="04" disabled>disabled</baseCheckbox>
              </div>
            </div>
            <div class="h-group">
              <strong class="w-[150px] font-light text-[14px] shrink-0">size: sm</strong>
              <div class="h-group gap-12">
                <baseCheckbox class="checkbox-sm" id="sm-01">default</baseCheckbox>
                <baseCheckbox class="checkbox-sm" id="sm-02" checked>checked</baseCheckbox>
                <baseCheckbox class="checkbox-sm checkbox-indeterminate" id="sm-03">indeterminate</baseCheckbox>
                <baseCheckbox class="checkbox-sm" id="sm-04" disabled>disabled</baseCheckbox>
              </div>
            </div>
          </div>
        </td>
      </tr>
      <tr>
        <th scope="row">Context</th>
      </tr>
      <tr>
        <td>
          <div class="v-group gap-5">
            <div class="h-group">
              <strong class="w-[150px] font-light text-[14px] shrink-0">size: lg</strong>
              <div class="h-group gap-12">
                <baseContext class="context-lg w-[180px]"></baseContext>
              </div>
            </div>
            <div class="h-group">
              <strong class="w-[150px] font-light text-[14px] shrink-0">size: default</strong>
              <div class="h-group gap-12">
                <baseContext class="w-[180px]"></baseContext>
              </div>
            </div>
          </div>
        </td>
      </tr>
      <tr>
        <th scope="row">Nav</th>
      </tr>
      <tr>
        <td class="h-screen">
          <div class="h-group">
            <strong class="w-[150px] font-light text-[14px] shrink-0">type: browser</strong>
            <BaseNav></BaseNav>
          </div>
        </td>
      </tr>

      <tr>
        <th scope="row">Notification</th>
      </tr>
      <tr>
        <td>
          <div class="v-group gap-5">
            <div class="h-group w-full">
              <strong class="w-[150px] font-light text-[14px] shrink-0">theme: normal</strong>
              <BaseNotification use-close>
                <strong class="notification-title">
                  <em>기본</em> Notification 입니다.
                </strong>
                <a href="#" class="notification-link">
                  바로가기
                </a>
              </BaseNotification>
            </div>
            <div class="h-group w-full">
              <strong class="w-[150px] font-light text-[14px] shrink-0">theme: info</strong>
              <BaseNotification theme="info" use-close>
                <strong class="notification-title">
                  <em>정보</em> Notification 입니다.
                </strong>
                <a href="#" class="notification-link">
                  바로가기
                </a>
              </BaseNotification>
            </div>
            <div class="h-group w-full">
              <strong class="w-[150px] font-light text-[14px] shrink-0">theme: success</strong>
              <BaseNotification theme="success" use-close>
                <strong class="notification-title">
                  <em>성공</em> Notification 입니다.
                </strong>
              </BaseNotification>
            </div>
            <div class="h-group w-full">
              <strong class="w-[150px] font-light text-[14px] shrink-0">theme: warning</strong>
              <BaseNotification theme="warning">
                <strong class="notification-title">
                  <em>경고</em> Notification 입니다.
                </strong>
                <a href="#" class="notification-link">
                  바로가기
                </a>
              </BaseNotification>
            </div>
            <div class="h-group w-full">
              <strong class="w-[150px] font-light text-[14px] shrink-0">theme: error</strong>
              <BaseNotification theme="error">
                <strong class="notification-title">
                  <em>에러</em> Notification 입니다.
                </strong>
              </BaseNotification>
            </div>
          </div>
        </td>
      </tr>
      <tr>
        <th scope="row">Pagination</th>
      </tr>
      <tr>
        <td>
          <div class="h-group">
            <strong class="w-[150px] font-light text-[14px] shrink-0">type: default</strong>
            <BasePagination></BasePagination>
          </div>
        </td>
      </tr>

      <tr>
        <th scope="row">Radio</th>
      </tr>
      <tr>
        <td>
          <div class="v-group gap-5">
            <div class="h-group">
              <strong class="w-[150px] font-light text-[14px] shrink-0">size: lg</strong>
              <div class="h-group gap-12">
                <baseRadio class="radio-lg" id="radio-lg-01">default</baseRadio>
                <baseRadio class="radio-lg" id="radio-lg-02" checked>checked</baseRadio>
                <baseRadio class="radio-lg" id="radio-lg-03" disabled>disabled</baseRadio>
              </div>
            </div>
            <div class="h-group">
              <strong class="w-[150px] font-light text-[14px] shrink-0">size: default</strong>
              <div class="h-group gap-12">
                <baseRadio id="radio-01">default</baseRadio>
                <baseRadio id="radio-02" checked>checked</baseRadio>
                <baseRadio id="radio-03" disabled>disabled</baseRadio>
              </div>
            </div>
            <div class="h-group">
              <strong class="w-[150px] font-light text-[14px] shrink-0">size: sm</strong>
              <div class="h-group gap-12">
                <baseRadio class="radio-sm" id="radio-sm-01">default</baseRadio>
                <baseRadio class="radio-sm" id="radio-sm-02" checked>checked</baseRadio>
                <baseRadio class="radio-sm" id="radio-sm-03" disabled>disabled</baseRadio>
              </div>
            </div>
          </div>
        </td>
      </tr>
      <tr>
        <th scope="row">Rating</th>
      </tr>
      <tr>
        <td>
          <div class="v-group gap-5">
            <BaseRating></BaseRating>
          </div>
        </td>
      </tr>
      <tr>
        <th scope="row">Select</th>
      </tr>
      <tr>
        <td>
          <div class="v-group gap-4">
            <div class="h-group">
              <strong class="w-[150px] font-light text-[14px]">size: lg</strong>
              <baseSelect class="select-lg"></baseSelect>
              <baseSelect class="select-lg is-disabled"></baseSelect>
            </div>
            <div class="h-group">
              <strong class="w-[150px] font-light text-[14px]">size: default</strong>
              <baseSelect></baseSelect>
              <baseSelect class="is-disabled"></baseSelect>

            </div>
            <div class="h-group">
              <strong class="w-[150px] font-light text-[14px]">size: sm</strong>
              <baseSelect class="select-sm"></baseSelect>
              <baseSelect class="select-sm is-disabled"></baseSelect>
              <baseSelect class="select-sm is-open"></baseSelect>
            </div>
          </div>
        </td>
      </tr>

      <tr>
        <th scope="row">Step</th>
      </tr>
      <tr>
        <td>
          <BaseStep></BaseStep>
        </td>
      </tr>
      <tr>
        <th scope="row">Switch</th>
      </tr>
      <tr>
        <td>
          <div class="v-group gap-5">
            <div class="h-group">
              <strong class="w-[150px] font-light text-[14px] shrink-0">size: default</strong>
              <div class="h-group gap-12">
                <baseSwitch checked>
                  <template v-slot:hidden-text>
                    hidden text
                  </template>
                </baseSwitch>
                <baseSwitch>
                  <template v-slot:switch-text>
                    label
                  </template>
                  <template v-slot:hidden-text>
                    hidden text
                  </template>
                </baseSwitch>
                <baseSwitch class="switch-disabled">
                  <template v-slot:switch-text>
                    label
                  </template>
                  <template v-slot:hidden-text>
                    hidden text
                  </template>
                </baseSwitch>
              </div>
            </div>
            <div class="h-group">
              <strong class="w-[150px] font-light text-[14px] shrink-0">size: sm</strong>
              <div class="h-group gap-12">
                <baseSwitch class="switch-sm" checked>
                  <template v-slot:hidden-text>
                    hidden
                  </template>
                </baseSwitch>
                <baseSwitch class="switch-sm">
                  <template v-slot:switch-text>
                    label
                  </template>
                  <template v-slot:hidden-text>
                    hidden
                  </template>
                </baseSwitch>
                <baseSwitch class="switch-sm switch-disabled">
                  <template v-slot:switch-text>
                    label
                  </template>
                </baseSwitch>
              </div>
            </div>
          </div>
        </td>
      </tr>

      <tr>
        <th scope="row">Tab</th>
      </tr>
      <tr>
        <td>
          <div class="v-group gap-5">
            <!--            Component 사용 -->
            <div class="h-group w-full">
              <strong class="w-[150px] font-light text-[14px] shrink-0">tab</strong>
              <BaseTab></BaseTab>
            </div>
            <div class="h-group">
              <strong class="w-[150px] font-light text-[14px] shrink-0">fill-tab</strong>
              <BaseTab theme="fill-tab"></BaseTab>
            </div>
            <div class="h-group">
              <strong class="w-[150px] font-light text-[14px] shrink-0">round-tab</strong>
              <BaseTab theme="round-tab" use-icon></BaseTab>
            </div>
            <!--            Component 미사용 -->
            <div class="h-group w-full">
              <strong class="w-[150px] font-light text-[14px] shrink-0">tab</strong>
              <div class="tab">
                <ul class="tab-list">
                  <li class="tab-item">
                    <button type="button" class="tab-button is-selected">
                      <span class="tab-title">기본정보</span>
                    </button>
                  </li>
                  <li class="tab-item">
                    <button type="button" class="tab-button">
                      <span class="tab-title">프로파일링</span>
                    </button>
                  </li>
                  <li class="tab-item">
                    <button type="button" class="tab-button">
                      <span class="tab-title">샘플데이터</span>
                    </button>
                  </li>
                  <li class="tab-item">
                    <button type="button" class="tab-button" disabled>
                      <span class="tab-title">Disabled</span>
                    </button>
                  </li>
                </ul>
              </div>
            </div>
            <div class="h-group">
              <strong class="w-[150px] font-light text-[14px] shrink-0">fill-tab</strong>
              <div class="fill-tab">
                <ul class="fill-tab-list">
                  <li class="fill-tab-item">
                    <button type="button" class="fill-tab-button is-selected">
                      <span class="fill-tab-title">카테고리</span>
                    </button>
                  </li>
                  <li class="fill-tab-item">
                    <button type="button" class="fill-tab-button">
                      <span class="fill-tab-title">카탈로그</span>
                    </button>
                  </li>
                  <li class="fill-tab-item">
                    <button type="button" class="fill-tab-button">
                      <span class="fill-tab-title">연결정보</span>
                    </button>
                  </li>
                  <li class="fill-tab-item">
                    <button type="button" class="fill-tab-button" disabled>
                      <span class="fill-tab-title">Disabled</span>
                    </button>
                  </li>
                </ul>
              </div>
            </div>
            <div class="h-group">
              <strong class="w-[150px] font-light text-[14px] shrink-0">round-tab</strong>
              <div class="round-tab">
                <ul class="round-tab-list">
                  <li class="round-tab-item">
                    <button type="button" class="round-tab-button is-selected">
                      <svg-icon name="data" class="svg-icon" />
                      <span class="round-tab-title">전체</span>
                    </button>
                  </li>
                  <li class="round-tab-item">
                    <button type="button" class="round-tab-button">
                      <svg-icon name="data" class="svg-icon" />
                      <span class="round-tab-title">데이터패브릭</span>
                    </button>
                  </li>
                  <li class="round-tab-item">
                    <button type="button" class="round-tab-button">
                      <svg-icon name="data" class="svg-icon" />
                      <span class="round-tab-title">분석보고서</span>
                    </button>
                  </li>
                  <li class="round-tab-item">
                    <button type="button" class="round-tab-button" disabled>
                      <svg-icon name="data" class="svg-icon" />
                      <span class="round-tab-title">Disabled</span>
                    </button>
                  </li>
                </ul>
              </div>
            </div>
          </div>
        </td>
      </tr>

      <tr>
        <th scope="row">Table</th>
      </tr>
      <tr>
        <td>

          <div class="v-group gap-5">
            <div class="h-group w-full">
              <strong class="w-[150px] font-light text-[14px] shrink-0">type: column</strong>
              <baseTableColumn></baseTableColumn>
            </div>
            <div class="h-group w-full">
              <strong class="w-[150px] font-light text-[14px] shrink-0">type: row</strong>
              <baseTableRow></baseTableRow>
            </div>
          </div>
        </td>
      </tr>
      <tr>
        <th scope="row">Tag</th>
      </tr>
      <tr>
        <td>
          <div class="v-group gap-5">
            <div class="h-group">
              <strong class="w-[150px] font-light text-[14px] shrink-0">size: default</strong>
              <baseTag># 단속위치</baseTag>
            </div>
            <div class="h-group">
              <strong class="w-[150px] font-light text-[14px] shrink-0">category: removal</strong>
              <baseTagRemovable>CCTV위치</baseTagRemovable>
            </div>
          </div>
        </td>
      </tr>

      <tr>
        <th scope="row">Text-input</th>
      </tr>
      <tr>
        <td>
          <div class="v-group gap-5">
            <div class="h-group">
              <strong class="w-[150px] font-light text-[14px] shrink-0">size: lg</strong>
              <baseTextInput id="inp-lg-01" class="text-input-lg" placeholder="입력해주세요."></baseTextInput>
              <label for="inp-lg-01" class="hidden">label</label>
              <baseTextInput class="text-input-lg" id="inp-lg-02" disabled value="입력할 수 없습니다."></baseTextInput>
              <label for="inp-lg-02" class="hidden">label</label>
              <baseTextInput id="inp-lg-03" class="text-input-lg" readonly value="read only"></baseTextInput>
              <label for="inp-lg-03" class="hidden">label</label>
            </div>
            <div class="h-group">
              <strong class="w-[150px] font-light text-[14px] shrink-0">size: default</strong>
              <baseTextInput id="inp-01" placeholder="입력해주세요."></baseTextInput>
              <label for="inp-01" class="hidden">label</label>
              <baseTextInput id="inp-02" disabled value="입력할 수 없습니다."></baseTextInput>
              <label for="inp-02" class="hidden">label</label>
              <baseTextInput id="inp-03" readonly value="read only"></baseTextInput>
              <label for="inp-03" class="hidden">label</label>
            </div>
            <div class="h-group">
              <strong class="w-[150px] font-light text-[14px] shrink-0">size: sm</strong>
              <baseTextInput id="inp-sm-01" class="text-input-sm" placeholder="입력해주세요."></baseTextInput>
              <label for="inp-sm-01" class="hidden">label</label>
              <baseTextInput id="inp-sm-02" class="text-input-sm" disabled value="입력할 수 없습니다."></baseTextInput>
              <label for="inp-sm-02" class="hidden">label</label>
              <baseTextInput id="inp-sm-03" class="text-input-sm" readonly value="read only"></baseTextInput>
              <label for="inp-sm-03" class="hidden">label</label>
            </div>
          </div>
        </td>
      </tr>
      <tr>
        <th scope="row">Textarea</th>
      </tr>
      <tr>
        <td>
          <div class="v-group gap-5">
            <div class="h-group">
              <strong class="w-[150px] font-light text-[14px] shrink-0">size: default</strong>
              <baseTextarea id="area-ex-01" placeholder="입력해주세요."></baseTextarea>
              <label for="area-ex-01" class="hidden">lable</label>
              <baseTextarea id="area-ex-02" disabled value="입력할 수 없습니다."></baseTextarea>
              <label for="area-ex-02" class="hidden">lable</label>
              <baseTextarea id="area-ex-03" readonly value="read only"></baseTextarea>
              <label for="area-ex-03" class="hidden">lable</label>
            </div>
          </div>
        </td>
      </tr>
      <tr>
        <th scope="row">Toggle button</th>
      </tr>
      <tr>
        <td>
          <div class="v-group gap-5">
            <div class="h-group">
              <strong class="w-[150px] font-light text-[14px] shrink-0">size: lg</strong>
              <baseToggleButton class="toggle-button-lg"></baseToggleButton>
            </div>
            <div class="h-group">
              <strong class="w-[150px] font-light text-[14px] shrink-0">size: default</strong>
              <baseToggleButton></baseToggleButton>
            </div>
            <div class="h-group">
              <strong class="w-[150px] font-light text-[14px] shrink-0">size: sm</strong>
              <baseToggleButton class="toggle-button-sm"></baseToggleButton>
            </div>
          </div>
        </td>
      </tr>
      <tr>
        <th scope="row">Search-field</th>
      </tr>
      <tr>
        <td>
          <SearchField></SearchField>
        </td>
      </tr>
      <tr>
        <th scope="row">Data-status</th>
      </tr>
      <tr>
        <td>
          <DataStatus></DataStatus>
        </td>
      </tr>
      </tbody>
    </table>
    <div class="layout-guide-title">
      <h1>Fabric UI Layouts</h1>
    </div>
    <table class="guide-table">
      <tbody>
      <tr>
        <th scope="row">Modal</th>
      </tr>
      <tr>
        <td>
          <baseButton @click="onClickOpen" class="button-primary button-lg">Modal Open</baseButton>
        </td>
      </tr>
      <tr>
        <th scope="row">footer</th>
      </tr>
      <tr>
        <td>
          <div>
            <TheFooter></TheFooter>
          </div>
        </td>
      </tr>
      </tbody>
    </table>
  </div>

  <modal v-model="show" title="Modal Sample">
    <template v-slot:body>
      ~ Modal is Here! Modal is Here! ~
    </template>
  </modal>
</template>
<script lang="ts" setup>
import { ref } from "@vue/reactivity";
import { useRouter } from "vue-router";
import TheFooter from "../layouts/data-fabric-portal/the-footer"

const show = ref(false);
const router = useRouter();

function onClickOpen() {
  show.value = !show.value;
}

function onClickMain() {
  router.push("/main");
}


</script>

<style lang="scss">
@import "component-ui";
</style>
