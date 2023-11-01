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
    template: `
      <baseButton class="button-normal button-lg" :type="args.type" :title="args.title" :disabled="args.disabled">
        <span class="button-text">취소</span>
      </baseButton>
      <baseButton class="button-primary button-lg" >
        <span class="button-text">확인</span>
      </baseButton>
    `,
    setup() {
      return { args };
    }
  }),
  args: {
    type: "button",
    title: "title",
    disabled: false
  },
  argTypes: { onClick: { action: "clicked" } }
};
