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
                  <baseButton class="button-negative button-lg">
                    <span class="button-text">Button</span>
                  </baseButton>
                  <baseButton class="button-normal button-lg">
                    <span class="button-text">Button</span>
                  </baseButton>
                  <baseButton class="button-ghost button-lg">
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
                  <baseButton class="button-ghost">
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
                  <baseButton class="button-ghost button-sm">
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
              <div class="h-group">
                <baseTextarea id="area-ex-01" placeholder="입력해주세요."></baseTextarea>
                <label for="area-ex-01" class="hidden">lable</label>
                <baseTextarea id="area-ex-02" disabled value="입력할 수 없습니다."></baseTextarea>
                <label for="area-ex-02" class="hidden">lable</label>
                <baseTextarea id="area-ex-03" readonly value="read only"></baseTextarea>
                <label for="area-ex-03" class="hidden">lable</label>
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
                        <span class="hidden">리스트형 보기</span>
                        <svg-icon name="list-view" class="svg-icon"></svg-icon>
                      </BaseRadio>
                      <BaseRadio name="rlgI" id="lg-ic-03" title="썸네일형 보기">
                        <span class="hidden">썸네일형 보기</span>
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
                        <span class="hidden">리스트형 보기</span>
                        <svg-icon name="list-view" class="svg-icon"></svg-icon>
                      </BaseRadio>
                      <BaseRadio name="rI" id="ic-03" title="썸네일형 보기">
                        <span class="hidden">썸네일형 보기</span>
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
                        <span class="hidden">리스트형 보기</span>
                        <svg-icon name="list-view" class="svg-icon"></svg-icon>
                      </BaseRadio>
                      <BaseRadio name="rSmI" id="icsm-03" title="썸네일형 보기">
                        <span class="hidden">썸네일형 보기</span>
                        <svg-icon name="thumbnail-view" class="svg-icon"></svg-icon>
                      </BaseRadio>
                    </div>
                  </div>
                </div>
              </div>

            </td>
          </tr>
          <!-- toggle-token으로 개선돼서 보류-->
          <tr class="hidden">
            <th scope="row">Toggle button</th>
          </tr>
          <tr class="hidden">
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
            <td>
            <th scope="row">List</th>
            </td>
          </tr>
          <tr>
            <td>
              <div class="h-group">
                <strong class="w-[150px] font-light text-[14px] shrink-0">type: Checkable List</strong>
                <baseListCheckable class="w-[300px]">
                  title
                </baseListCheckable>
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
                      <Card class="card-lg ">
                        <template v-slot:img>
                          <svg-icon class="svg-icon" name="data"></svg-icon>
                        </template>
                      </Card>
                    </li>
                    <li class="card-item">
                      <Card class="card-lg card-primary">
                        <template v-slot:img>
                          <img src="../assets/images/sample.png" alt="">
                        </template>
                      </Card>
                    </li>
                    <li class="card-item">
                      <Card class="card-lg card-negative">
                        <template v-slot:img>
                          <svg-icon class="svg-icon" name="data"></svg-icon>
                        </template>
                      </Card>
                    </li>
                  </ul>
                </div>
                <div class="h-group w-full">
                  <strong class="w-[150px] font-light text-[14px] shrink-0">size: default</strong>
                  <ul class="card-list w-[1100px]">
                    <li class="card-item">
                      <Card class=" ">
                        <template v-slot:img>
                          <svg-icon class="svg-icon" name="data"></svg-icon>
                        </template>
                      </Card>
                    </li>
                    <li class="card-item">
                      <Card class=" card-primary">
                        <template v-slot:img>
                          <img src="../assets/images/sample.png" alt="">
                        </template>
                      </Card>
                    </li>
                    <li class="card-item">
                      <Card class=" card-negative">
                        <template v-slot:img>
                          <svg-icon class="svg-icon" name="data"></svg-icon>
                        </template>
                      </Card>
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
import { sample } from "../composables/sample";
import { AgGridVue } from "ag-grid-vue3";

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
