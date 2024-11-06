import BaseCheckbox from "./base-checkbox.vue";

export default {
  title: 'Styled/Atoms/[PCFI] 체크 박스',
  components: BaseCheckbox,
  decorators: [
    () => ({
      template:
        '<div style="margin: 1em;"><h2 style="margin-bottom: 20px; font-size: 20px; color: #212121;">(예시) Checkbox Input Element </h2><div style="display: flex; flex-direction: row; "><story /></div></div>',
    }),
  ],
};

export const Default = () => ({
  components: { BaseCheckbox },
  template:
    '<BaseCheckbox name="checkbox-test" checkboxId="checkbox1"><template v-slot:label>Basic Checkbox</template></BaseCheckbox>',
});

export const CheckboxIcon = () => ({
  components: { BaseCheckbox },
  template:
    '<BaseCheckbox name="checkbox-test-icon" checkboxId="checkbox2" class="checkbox--icon"><template v-slot:label><svg-icon class="svg-icon mu-icon-svg" name="app-switcher" aria-hidden="true"></svg-icon><span class="hidden">카드형</span></template></BaseCheckbox>',
});
