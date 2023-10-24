// Button.stories.ts

import type { Meta, StoryObj } from '@storybook/vue3';

import BaseButton from './button.vue';

const meta: Meta<typeof BaseButton> = {
  component: BaseButton,
};

//👇 This default export determines where your story goes in the story list
export default meta;
type Story = StoryObj<typeof BaseButton>;

/*
 *👇 Render functions are a framework specific feature to allow you control on how the component renders.
 * See https://storybook.js.org/docs/vue/api/csf
 * to learn how to use render functions.
 */
export const Primary: Story = {
  render: (args) => ({
    components: { BaseButton },
    setup() {
      return { args };
    },
    template: '<BaseButton v-bind="args" v-on="$props">button test</BaseButton>'
  }),
  args: {
    type: "button",
    title: "title",
    disabled: true
  },
  argTypes: { onClick: { action: "clicked" } }
};
