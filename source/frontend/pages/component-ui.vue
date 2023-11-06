<template>
  <div class="layout-guide">
    <div class="layout-guide-title">
      <div class="inner">
        <h1>Fabric UI Components</h1>
      </div>
    </div>
    <div class="guide-group">
      <div class="inner">
        <table class="guide-table">
          <tbody>
          <tr>
            <th scope="row">Alert</th>
          </tr>
          <tr>
            <td>
              <div class="v-group gap-5">
                <div class="h-group gap-2">
                  <strong class="w-[150px] font-light text-[14px] shrink-0">default</strong>
                  <commonAlert theme="default"></commonAlert>
                </div>
                <div class="h-group gap-2">
                  <strong class="w-[150px] font-light text-[14px] shrink-0">info</strong>
                  <commonAlert theme="info"></commonAlert>
                </div>
                <div class="h-group gap-2">
                  <strong class="w-[150px] font-light text-[14px] shrink-0">success</strong>
                  <commonAlert theme="success"></commonAlert>
                </div>
                <div class="h-group gap-2">
                  <strong class="w-[150px] font-light text-[14px] shrink-0">warning</strong>
                  <commonAlert theme="warning">
                    <baseButton class="button-lg button-negative">
                      <span class="button-text">확인</span>
                    </baseButton>
                  </commonAlert>
                </div>
                <div class="h-group gap-2">
                  <strong class="w-[150px] font-light text-[14px] shrink-0">error</strong>
                  <commonAlert theme="error">
                    <baseButton class="button-lg button-negative">
                      <span class="button-text">확인</span>
                    </baseButton>
                  </commonAlert>
                </div>
              </div>
            </td>
          </tr>
          <tr>
            <th scope="row">Badge</th>
          </tr>
          <tr>
            <td>
              <div class="h-group gap-2">
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
                  <baseButton class="button-negative button-lg button-icon" title="button">
                    <svg-icon name="user" class="svg-icon" />
                    <span class="hidden-text">BUTTON</span>
                  </baseButton>
                  <baseButton class="button-normal button-lg button-icon" title="button">
                    <svg-icon name="user" class="svg-icon" />
                    <span class="hidden-text">BUTTON</span>
                  </baseButton>
                  <baseButton class="button-ghost button-lg button-icon" title="button">
                    <svg-icon name="user" class="svg-icon" />
                    <span class="hidden-text">BUTTON</span>
                  </baseButton>
                  <baseButton class="button-danger button-lg button-icon" title="button">
                    <svg-icon name="user" class="svg-icon" />
                    <span class="hidden-text">BUTTON</span>
                  </baseButton>
                  <baseButton class="button-link button-lg">
                    <span class="button-text">Button</span>
                  </baseButton>
                  <baseButton class="button-link-primary button-lg">
                    <span class="button-text">Button</span>
                  </baseButton>
                  <baseButton class="button-link-negative button-lg">
                    <span class="button-text">Button</span>
                  </baseButton>
                  <baseButton class="button-link-tertiary button-lg">
                    <span class="button-text">Button</span>
                  </baseButton>
                  <baseButton class="button-link-tertiary button-lg" disabled>
                    <span class="button-text">Button</span>
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
                  <baseButton class="button-negative button-icon" title="button">
                    <svg-icon name="user" class="svg-icon" />
                    <span class="hidden-text">BUTTON</span>
                  </baseButton>
                  <baseButton class="button-normal button-icon" title="button">
                    <svg-icon name="user" class="svg-icon" />
                    <span class="hidden-text">BUTTON</span>
                  </baseButton>
                  <baseButton class="button-ghost button-icon" title="button">
                    <svg-icon name="user" class="svg-icon" />
                    <span class="hidden-text">BUTTON</span>
                  </baseButton>
                  <baseButton class="button-danger button-icon" title="button">
                    <svg-icon name="user" class="svg-icon" />
                    <span class="hidden-text">BUTTON</span>
                  </baseButton>
                  <baseButton class="button-link">
                    <span class="button-text">Button</span>
                  </baseButton>
                  <baseButton class="button-link-primary">
                    <span class="button-text">Button</span>
                  </baseButton>
                  <baseButton class="button-link-negative">
                    <span class="button-text">Button</span>
                  </baseButton>
                  <baseButton class="button-link-tertiary">
                    <span class="button-text">Button</span>
                  </baseButton>
                  <baseButton class="button-link-tertiary" disabled>
                    <span class="button-text">Button</span>
                  </baseButton>
                </div>
                <div class="h-group">
                  <strong class="w-[150px] font-light text-[14px] shrink-0">size: sm</strong>
                  <baseButton title="button-sm 사용자 관리" class="button-primary">
                    <svg-icon name="user" class="svg-icon" />
                    <span class="button-text">Button</span>
                  </baseButton>
                  <baseButton title="button-sm 사용자 관리" class="button-secondary">
                    <svg-icon name="user" class="svg-icon" />
                    <span class="button-text">Button</span>
                  </baseButton>
                  <baseButton title="button-sm 사용자 관리" class="button-tertiary">
                    <svg-icon name="user" class="svg-icon" />
                    <span class="button-text">Button</span>
                  </baseButton>
                  <baseButton class="button-sm button-negative button-icon" title="button">
                    <svg-icon name="user" class="svg-icon" />
                    <span class="hidden-text">BUTTON</span>
                  </baseButton>
                  <baseButton class="button-sm button-normal button-icon" title="button">
                    <svg-icon name="user" class="svg-icon" />
                    <span class="hidden-text">BUTTON</span>
                  </baseButton>
                  <baseButton class="button-sm button-ghost button-icon" title="button">
                    <svg-icon name="user" class="svg-icon" />
                    <span class="hidden-text">BUTTON</span>
                  </baseButton>
                  <baseButton class="button-sm button-danger button-icon" title="button">
                    <svg-icon name="user" class="svg-icon" />
                    <span class="hidden-text">BUTTON</span>
                  </baseButton>
                  <baseButton class="button-sm button-link">
                    <span class="button-text">Button</span>
                  </baseButton>
                  <baseButton class="button-sm button-link-primary">
                    <span class="button-text">Button</span>
                  </baseButton>
                  <baseButton class="button-sm button-link-negative">
                    <span class="button-text">Button</span>
                  </baseButton>
                  <baseButton class="button-sm button-link-tertiary">
                    <span class="button-text">Button</span>
                  </baseButton>
                  <baseButton class="button-sm button-link-tertiary" disabled>
                    <span class="button-text">Button</span>
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
            <th scope="row">date time picker</th>
          </tr>
          <tr>
            <td>
              <div class="v-group gap-5">
                <div class="h-group">
                  <strong class="w-[150px] font-light text-[14px] shrink-0">type: time picker</strong>
                  <div class="h-group gap-12">
                    <DatePicker
                      v-model:modelValue="args.date"
                      :type="args.dateType">
                    </DatePicker>
                  </div>
                </div>
                <div class="h-group">
                  <strong class="w-[150px] font-light text-[14px] shrink-0">type: date picker</strong>
                  <div class="h-group gap-12">
                    <DatePicker
                      v-model:modelValue="args.date"
                      :type="args.date">
                    </DatePicker>
                  </div>
                </div>
                <div class="h-group">
                  <strong class="w-[150px] font-light text-[14px] shrink-0">type: date time range picker</strong>
                  <div class="h-group gap-12">
                    <DatePicker
                      v-model:modelValue="args.dateRange"
                      :type="args.rangeType">
                    </DatePicker>
                  </div>
                </div>
              </div>
            </td>
          </tr>
          <tr>
            <th scope="row">form</th>
          </tr>
          <tr>
            <td>
              <div class="v-group gap-10">
                <div class="h-group">
                  <strong class="w-[150px] font-light text-[14px] shrink-0">type: horizontal</strong>
                  <baseFormHorizontal></baseFormHorizontal>
                </div>
                <div class="h-group">
                  <strong class="w-[150px] font-light text-[14px] shrink-0">type: vertical</strong>
                  <baseFormVertical></baseFormVertical>
                </div>
              </div>
            </td>
          </tr>
          <tr>
            <th scope="row">Nav</th>
          </tr>
          <tr>
            <td>
              <BaseNav></BaseNav>
            </td>
          </tr>

          <tr>
            <th scope="row">Notification</th>
          </tr>
          <tr>
            <td>
              <div class="v-group gap-5">
                <div class="h-group w-full">
                  <strong class="w-[150px] font-light text-[14px] shrink-0">normal</strong>
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
                  <strong class="w-[150px] font-light text-[14px] shrink-0">info</strong>
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
                  <strong class="w-[150px] font-light text-[14px] shrink-0">success</strong>
                  <BaseNotification theme="success" use-close>
                    <strong class="notification-title">
                      <em>성공</em> Notification 입니다.
                    </strong>
                  </BaseNotification>
                </div>
                <div class="h-group w-full">
                  <strong class="w-[150px] font-light text-[14px] shrink-0">warning</strong>
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
                  <strong class="w-[150px] font-light text-[14px] shrink-0">error</strong>
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
              <BasePagination></BasePagination>
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
                  <baseSelect class="select-lg is-open"></baseSelect>
                  <baseSelect class="select-lg is-disabled"></baseSelect>
                  <!--  TODO:[개발]text-input 전환시 select-selector-button 스타일 display:none; / 두개는 display:block처리 -->
                  <div class="select select-lg">
                    <div class="select-selector" role="combobox" aria-expanded="false" aria-haspopup="true">
                      <button class="select-selector-button" style="display: none" type="button" title="열기">
                        <slot name="title">
                          <span class="select-selector-title">선택하세요</span>
                        </slot>
                        <span class="hidden-text">선택</span>
                        <svg-icon class="svg-icon select-selector-icon" name="chevron-down-medium" aria-hidden="true"></svg-icon>
                      </button>
                      <div class="select-selector-input">
                        <baseTextInput class="text-input" type="text" placeholder="검색어 입력"></baseTextInput>
                      </div>
                      <baseButton class="select-selector-close-button">
                        <svg-icon class="svg-icon" name="chevron-up-medium" aria-hidden="true"></svg-icon>
                      </baseButton>
                    </div>
                    <div class="select-container" id="select-01">
                      <ul class="select-container-list" role="listbox">
                        <li class="select-container-item">
                          <baseCheckbox class="checkbox-indeterminate" role="option">전체</baseCheckbox>
                        </li>
                        <li class="select-container-item">
                          <baseCheckbox role="option">item 01</baseCheckbox>
                        </li>
                        <li class="select-container-item">
                          <button class="select-container-button" type="button" role="option">
                            <span class="select-container-text">item 02</span>
                          </button>
                        </li>
                      </ul>
                    </div>
                  </div>
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
                <div class="h-group">
                  <strong class="w-[150px] font-light text-[14px] shrink-0">round-tab</strong>
                  <div class="round-tab round-tab-sm">
                    <ul class="round-tab-list">
                      <li class="round-tab-item">
                        <button type="button" class="round-tab-button is-selected">
                          <span class="round-tab-title">전체</span>
                        </button>
                      </li>
                      <li class="round-tab-item">
                        <button type="button" class="round-tab-button">
                          <span class="round-tab-title">데이터패브릭</span>
                        </button>
                      </li>
                      <li class="round-tab-item">
                        <button type="button" class="round-tab-button">
                          <span class="round-tab-title">분석보고서</span>
                        </button>
                      </li>
                      <li class="round-tab-item">
                        <button type="button" class="round-tab-button" disabled>
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
                  <strong class="w-[150px] font-light text-[14px] shrink-0">column</strong>
                  <baseTableColumn></baseTableColumn>
                </div>
                <div class="h-group w-full">
                  <strong class="w-[150px] font-light text-[14px] shrink-0">row</strong>
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
                  <strong class="w-[150px] font-light text-[14px] shrink-0">default</strong>
                  <baseTag># 단속위치</baseTag>
                </div>
                <div class="h-group">
                  <strong class="w-[150px] font-light text-[14px] shrink-0">type: removal</strong>
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
                  <label for="inp-lg-01" class="hidden-text">label</label>
                  <baseTextInput class="text-input-lg" id="inp-lg-02" disabled value="입력할 수 없습니다."></baseTextInput>
                  <label for="inp-lg-02" class="hidden-text">label</label>
                  <baseTextInput id="inp-lg-03" class="text-input-lg" readonly value="read only"></baseTextInput>
                  <label for="inp-lg-03" class="hidden-text">label</label>
                </div>
                <div class="h-group">
                  <strong class="w-[150px] font-light text-[14px] shrink-0">size: default</strong>
                  <baseTextInput id="inp-01" placeholder="입력해주세요."></baseTextInput>
                  <label for="inp-01" class="hidden-text">label</label>
                  <baseTextInput id="inp-02" disabled value="입력할 수 없습니다."></baseTextInput>
                  <label for="inp-02" class="hidden-text">label</label>
                  <baseTextInput id="inp-03" readonly value="read only"></baseTextInput>
                  <label for="inp-03" class="hidden-text">label</label>
                </div>
                <div class="h-group">
                  <strong class="w-[150px] font-light text-[14px] shrink-0">size: sm</strong>
                  <baseTextInput id="inp-sm-01" class="text-input-sm" placeholder="입력해주세요."></baseTextInput>
                  <label for="inp-sm-01" class="hidden-text">label</label>
                  <baseTextInput id="inp-sm-02" class="text-input-sm" disabled value="입력할 수 없습니다."></baseTextInput>
                  <label for="inp-sm-02" class="hidden-text">label</label>
                  <baseTextInput id="inp-sm-03" class="text-input-sm" readonly value="read only"></baseTextInput>
                  <label for="inp-sm-03" class="hidden-text">label</label>
                </div>
              </div>
            </td>
          </tr>
          <tr>
            <th scope="row">Textarea</th>
          </tr>
          <tr>
            <td>
              <div class="h-group">
                <baseTextarea id="area-ex-01" placeholder="입력해주세요."></baseTextarea>
                <label for="area-ex-01" class="hidden-text">lable</label>
                <baseTextarea id="area-ex-02" disabled value="입력할 수 없습니다."></baseTextarea>
                <label for="area-ex-02" class="hidden-text">lable</label>
                <baseTextarea id="area-ex-03" readonly value="read only"></baseTextarea>
                <label for="area-ex-03" class="hidden-text">lable</label>
              </div>
            </td>
          </tr>

          <tr>
            <th scope="row">Toggle</th>
          </tr>
          <tr>
            <td>
              <div class="v-group gap-5">
                <div class="h-group">
                  <strong class="w-[150px] font-light text-[14px] shrink-0">size: lg</strong>
                  <div class="v-group">
                    <div class="toggle toggle-lg">
                      <BaseRadio name="rlg" id="lg01" checked>리스트형 보기</BaseRadio>
                      <BaseRadio name="rlg" id="lg03">썸네일형 보기</BaseRadio>
                    </div>
                    <div class="toggle toggle-lg toggle-icon">
                      <BaseRadio name="rlgI" id="lg-ic-01" checked title="리스트형 보기">
                        <span class="hidden-text">리스트형 보기</span>
                        <svg-icon name="list-view" class="svg-icon"></svg-icon>
                      </BaseRadio>
                      <BaseRadio name="rlgI" id="lg-ic-03" title="썸네일형 보기">
                        <span class="hidden-text">썸네일형 보기</span>
                        <svg-icon name="thumbnail-view" class="svg-icon"></svg-icon>
                      </BaseRadio>
                    </div>
                  </div>
                </div>
                <div class="h-group">
                  <strong class="w-[150px] font-light text-[14px] shrink-0">size: default</strong>
                  <div class="v-group">
                    <div class="toggle">
                      <BaseRadio name="r" id="ic01" checked>리스트형 보기</BaseRadio>
                      <BaseRadio name="r" id="ic03">썸네일형 보기</BaseRadio>
                    </div>
                    <div class="toggle toggle-icon">
                      <BaseRadio name="rI" id="ic-01" checked title="리스트형 보기">
                        <span class="hidden-text">리스트형 보기</span>
                        <svg-icon name="list-view" class="svg-icon"></svg-icon>
                      </BaseRadio>
                      <BaseRadio name="rI" id="ic-03" title="썸네일형 보기">
                        <span class="hidden-text">썸네일형 보기</span>
                        <svg-icon name="thumbnail-view" class="svg-icon"></svg-icon>
                      </BaseRadio>
                    </div>
                  </div>
                </div>
                <div class="h-group">
                  <strong class="w-[150px] font-light text-[14px] shrink-0">size: sm</strong>
                  <div class="v-group">
                    <div class="toggle toggle-sm">
                      <BaseRadio name="rsm" id="sm01" checked>리스트형 보기</BaseRadio>
                      <BaseRadio name="rsm" id="sm03">썸네일형 보기</BaseRadio>
                    </div>
                    <div class="toggle toggle-sm toggle-icon">
                      <BaseRadio name="rSmI" id="icsm-01" checked title="리스트형 보기">
                        <span class="hidden-text">리스트형 보기</span>
                        <svg-icon name="list-view" class="svg-icon"></svg-icon>
                      </BaseRadio>
                      <BaseRadio name="rSmI" id="icsm-03" title="썸네일형 보기">
                        <span class="hidden-text">썸네일형 보기</span>
                        <svg-icon name="thumbnail-view" class="svg-icon"></svg-icon>
                      </BaseRadio>
                    </div>
                  </div>
                </div>
              </div>

            </td>
          </tr>
          <!-- toggle-token으로 개선돼서 보류-->
          <tr class="hidden-text">
            <th scope="row">Toggle button</th>
          </tr>
          <tr class="hidden-text">
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
            <th scope="row">Toggle token (Multi Check)</th>
          </tr>
          <tr>
            <td>
              <div class="v-group gap-5">
                <div class="h-group">
                  <strong class="w-[150px] font-light text-[14px] shrink-0">size: lg</strong>
                  <div class="v-group">
                    <div class="toggle-token toggle-token-lg">
                      <BaseCheckbox name="toggleLg" id="toggle-lg-01" checked>
                        오늘
                      </BaseCheckbox>
                      <BaseCheckbox name="toggleLg" id="toggle-lg-02">
                        내일
                      </BaseCheckbox>
                      <BaseCheckbox name="toggleLg" id="toggle-lg-03">
                        일주일
                      </BaseCheckbox>
                      <BaseCheckbox name="toggleLg" id="toggle-lg-04">
                        일개월
                      </BaseCheckbox>
                    </div>
                    <div class="toggle-token toggle-token-lg toggle-token-icon">
                      <BaseCheckbox name="toggleLgI" id="toggle-lg-ic-01" checked>
                        <svg-icon class="svg-icon" name="data"></svg-icon>
                      </BaseCheckbox>
                      <BaseCheckbox name="toggleLgI" id="toggle-lg-ic-02">
                        <svg-icon class="svg-icon" name="date"></svg-icon>
                      </BaseCheckbox>
                      <BaseCheckbox name="toggleLgI" id="toggle-lg-ic-03">
                        <svg-icon class="svg-icon" name="bookmark"></svg-icon>
                      </BaseCheckbox>
                      <BaseCheckbox name="toggleLgI" id="toggle-lg-ic-04">
                        <svg-icon class="svg-icon" name="filter"></svg-icon>
                      </BaseCheckbox>
                    </div>
                  </div>
                </div>
                <div class="h-group">
                  <strong class="w-[150px] font-light text-[14px] shrink-0">size: default</strong>
                  <div class="v-group">
                    <div class="toggle-token">
                      <BaseCheckbox name="toggle" id="toggle-01" checked>
                        오늘
                      </BaseCheckbox>
                      <BaseCheckbox name="toggle" id="toggle-02">
                        내일
                      </BaseCheckbox>
                      <BaseCheckbox name="toggle" id="toggle-03">
                        일주일
                      </BaseCheckbox>
                      <BaseCheckbox name="toggle" id="toggle-04">
                        일개월
                      </BaseCheckbox>
                    </div>
                    <div class="toggle-token toggle-token-icon">
                      <BaseCheckbox name="toggleI" id="toggle-ic-01" checked>
                        <svg-icon class="svg-icon" name="data"></svg-icon>
                      </BaseCheckbox>
                      <BaseCheckbox name="toggleI" id="toggle-ic-02">
                        <svg-icon class="svg-icon" name="date"></svg-icon>
                      </BaseCheckbox>
                      <BaseCheckbox name="toggleI" id="toggle-ic-03">
                        <svg-icon class="svg-icon" name="bookmark"></svg-icon>
                      </BaseCheckbox>
                      <BaseCheckbox name="toggleI" id="toggle-ic-04">
                        <svg-icon class="svg-icon" name="filter"></svg-icon>
                      </BaseCheckbox>
                    </div>
                  </div>
                </div>
                <div class="h-group">
                  <strong class="w-[150px] font-light text-[14px] shrink-0">size: sm</strong>
                  <div class="v-group">
                    <div class="toggle-token toggle-token-sm">
                      <BaseCheckbox name="toggleSmI" id="toggle-sm-01" checked>
                        오늘
                      </BaseCheckbox>
                      <BaseCheckbox name="toggleSmI" id="toggle-sm-02">
                        내일
                      </BaseCheckbox>
                      <BaseCheckbox name="toggleSmI" id="toggle-sm-03">
                        일주일
                      </BaseCheckbox>
                      <BaseCheckbox name="toggleSmI" id="toggle-sm-04">
                        일개월
                      </BaseCheckbox>
                    </div>
                    <div class="toggle-token toggle-token-sm toggle-token-icon">
                      <BaseCheckbox name="toggleSmI" id="toggle-sm-ic-01" checked>
                        <svg-icon class="svg-icon" name="data"></svg-icon>
                      </BaseCheckbox>
                      <BaseCheckbox name="toggleSmI" id="toggle-sm-ic-02">
                        <svg-icon class="svg-icon" name="date"></svg-icon>
                      </BaseCheckbox>
                      <BaseCheckbox name="toggleSmI" id="toggle-sm-ic-03">
                        <svg-icon class="svg-icon" name="bookmark"></svg-icon>
                      </BaseCheckbox>
                      <BaseCheckbox name="toggleSmI" id="toggle-sm-ic-04">
                        <svg-icon class="svg-icon" name="filter"></svg-icon>
                      </BaseCheckbox>
                    </div>
                  </div>
                </div>
              </div>
            </td>
          </tr>
          <tr>
            <th scope="row">Tooltip</th>
          </tr>
          <tr>
            <td>
              <div class="v-group gap-5">
                <div class="h-group gap-3">
                  <baseTooltip class="tooltip-bottom" style="position: relative">
                    <template v-slot:title>
                      tooltip title
                    </template>
                    <template v-slot:text>
                      tooltip text
                    </template>
                  </baseTooltip>
                  <baseTooltip class="tooltip-top" style="position: relative">
                    <template v-slot:title>
                      tooltip title
                    </template>
                    <template v-slot:text>
                      tooltip text
                    </template>
                  </baseTooltip>
                  <baseTooltip class="tooltip-right" style="position: relative">
                    <template v-slot:text>
                      only tooltip text
                      <br>
                      tooltip text
                    </template>
                  </baseTooltip>
                  <baseTooltip class="tooltip-left" style="position: relative">
                    <template v-slot:title>
                      only tooltip title
                    </template>
                  </baseTooltip>
                </div>
              </div>
            </td>
          </tr>
          <tr>
            <td>
              <div class="v-group gap-5">
                <div class="h-group gap-3">
                  <!-- directive -->
                  <a v-tooltip.top="tooltipMassage">Directive Tooltip</a>
                  <!-- component - placement (top, bottom, right, left, ...) -->
                  <VTooltip placement="bottom">
                    <a>Component Tooltip</a>
                    <template #popper>
                      {{ tooltipMassage }}
                    </template>
                  </VTooltip>
                  <!-- component - other component -->
                  <VTooltip>
                    <a>Component Tooltip</a>
                    <template #popper>
                      <baseTooltip class="tooltip-top" style="position: relative">
                        <template v-slot:title>
                          tooltip title
                        </template>
                        <template v-slot:text>
                          tooltip text
                        </template>
                      </baseTooltip>
                    </template>
                  </VTooltip>
                </div>
              </div>
            </td>
          </tr>
          <tr>
            <th scope="row">List</th>
          </tr>
          <tr>
            <td>
              <div class="v-group gap-5">
                <div class="h-group">
                  <strong class="w-[150px] font-light text-[14px] shrink-0">type: Checkable List - title</strong>
                  <baseList class="w-[300px]">
                    <template v-slot:head>
                      <div class="list-head">
                        <div class="list-head-title">
                          title
                        </div>
                      </div>
                    </template>
                  </baseList>
                </div>
                <div class="h-group">
                  <strong class="w-[150px] font-light text-[14px] shrink-0">type: Checkable List - no title</strong>
                  <baseList class="w-[300px]"></baseList>
                </div>
              </div>
            </td>
          </tr>
          </tbody>
        </table>
      </div>
    </div>
    <div class="layout-guide-title">
      <div class="inner">
        <h1>Fabric UI Functional</h1>
      </div>
    </div>
    <div class="guide-group">
      <div class="inner">
        <table class="guide-table">
          <tbody>
          <tr>
            <th scope="row">Ag-grid</th>
          </tr>
          <tr>
            <td>
              <ag-grid-vue style="width: 550px; height: 300px" class="ag-theme-alpine" :gridOptions="gridOptions"
                           :columnDefs="columnDefs" :rowData="rowData">
              </ag-grid-vue>
            </td>
          </tr>
          <tr>
            <th scope="row">Card Simple</th>
          </tr>
          <tr>
            <td>
              <div class="v-group gap-5">
                <div class="h-group w-full">
                  <strong class="w-[150px] font-light text-[14px] shrink-0"> size: default </strong>
                  <ul class="card-simple-list w-[500px]">
                    <li class="card-simple-item">
                      <CardSimple></CardSimple>
                    </li>
                    <li class="card-simple-item">
                      <CardSimple></CardSimple>
                    </li>
                    <li class="card-simple-item">
                      <CardSimple hasStatus></CardSimple>
                    </li>
                    <li class="card-simple-item">
                      <CardSimple hasStatus></CardSimple>
                    </li>
                  </ul>
                </div>
              </div>
            </td>
          </tr>
          <tr>
            <th scope="row">Card</th>
          </tr>
          <tr>
            <td>
              <div class="v-group gap-5">
                <div class="h-group w-full">
                  <strong class="w-[150px] font-light text-[14px] shrink-0"> size: lg </strong>
                  <ul class="card-list w-[1100px]">
                    <li class="card-item">
                      <Card class="card-lg"></Card>
                    </li>
                    <li class="card-item">
                      <Card class="card-lg"></Card>
                    </li>
                    <li class="card-item">
                      <Card class="card-lg"></Card>
                    </li>

                  </ul>
                </div>
                <div class="h-group w-full">
                  <strong class="w-[150px] font-light text-[14px] shrink-0">size: default</strong>
                  <ul class="card-list w-[1100px]">
                    <li class="card-item">
                      <Card></Card>
                    </li>
                    <li class="card-item">
                      <Card></Card>
                    </li>
                    <li class="card-item">
                      <Card></Card>
                    </li>
                    <li class="card-item">
                      <Card class="is-modify"></Card>
                    </li>
                  </ul>
                </div>
              </div>
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
          <tr>
            <th scope="row">Kebab-menu</th>
          </tr>
          <tr>
            <td>
              <KebabMenu></KebabMenu>
            </td>
          </tr>
          <tr>
            <th scope="row">Modal</th>
          </tr>
          <tr>
            <td>
              <baseButton @click="onClickOpen" class="button-primary button-lg">Modal Open</baseButton>
            </td>
          </tr>
          <tr>
            <th scope="row">Search-field</th>
          </tr>
          <tr>
            <td>
              <div class="v-group gap-5">
                <div class="h-group">
                  <strong class="w-[150px] font-light text-[14px] shrink-0">size: lg</strong>
                  <SearchField class="search-field-lg"></SearchField>
                </div>
                <div class="h-group">
                  <strong class="w-[150px] font-light text-[14px] shrink-0">size: md</strong>
                  <SearchField></SearchField>
                </div>
                <div class="h-group">
                  <strong class="w-[150px] font-light text-[14px] shrink-0">size: sm</strong>
                  <SearchField class="search-field-sm" style="width: 300px;"></SearchField>
                </div>
              </div>
            </td>
          </tr>
          <tr>
            <th scope="row">Search filter</th>
          </tr>
          <tr>
            <td>
              <SearchFilter></SearchFilter>
            </td>
          </tr>
          <tr>
            <th scope="row">Scroll Top</th>
          </tr>
          <tr>
            <td>
              <div class="relative h-10">
                <ScrollTop style="position: absolute;left: 0;bottom: 0;"></ScrollTop>
              </div>
            </td>
          </tr>
          </tbody>
        </table>
      </div>
    </div>
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
import { sample } from "/composables/sample";
import { AgGridVue } from "ag-grid-vue3";
import DatePicker from "/components/common/date-picker/date-picker.vue";
import Tooltip from "#build/components/base/tooltip.vue";

const sampleData = sample();
const show = ref(false);
const router = useRouter();
const gridOptions = {
  headerHeight: 56,
  rowHeight: 56,
  // sort 설정 시 추가할 것
  unSortIcon: true
};

// sort 예시 추가
const columnDefs = [
  { headerName: "Make", field: "make", sortable: true },
  { headerName: "Model", field: "model" },
  { headerName: "Price", field: "price", sortable: true }
];
const rowData = [
  { make: "Toyota", model: "Celica", price: 35000 },
  { make: "Ford", model: "Mondeo", price: 32000 },
  { make: "Porsche", model: "Boxster", price: 72000 }
];

const meta: Meta<typeof DatePicker> = {
  component: DatePicker,
};

let args = {
  date: "2023-11-22",
  dateRange: ["2023-11-10", "2023-11-22"],
  // storybook 에 object 형식으로 뜸
  dateType: "time",
  rangeType: "datetime"
};

const tooltipMassage = ref(`You have ${3} new messages.`);

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
