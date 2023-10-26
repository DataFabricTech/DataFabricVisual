// Button.stories.ts
import type { Meta, StoryObj } from '@storybook/vue3';
import BaseButton from './button.vue';

const meta: Meta<typeof BaseButton> = {
  component: BaseButton,
};

//👇 This default export determines where your story goes in the story list
export default meta;
type Story = StoryObj<typeof BaseButton>;

export const Primary: Story = {
  render: (args) => ({
    components: { BaseButton },
    template: `<BaseButton v-bind="args" v-on="$props">${args.message}</BaseButton>`,
    setup() {
      return { args };
    }
  }),
  args: {
    message : "버튼!!!!",
    type: "button",
    title: "title",
    disabled: false
  },
  argTypes: { onClick: { action: "clicked" } }
};
