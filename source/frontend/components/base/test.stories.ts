import { Meta, StoryObj } from "@storybook/vue3";
import BaseButton from "./button.vue"; // Imported with '~' shorthand syntax

// More on how to set up stories at: https://storybook.js.org/docs/vue/writing-stories/introduction
const meta: Meta<typeof BaseButton> = {
  title: "components/base/button",
  component: BaseButton,
  // This component will have an automatically generated Autodocs entry: https://storybook.js.org/docs/vue/writing-docs/autodocs
  tags: ["autodocs"],
  render: (args) => ({
    // Components used in your story `template` are defined in the `components` object
    components: { BaseButton },
    // The story's `args` need to be mapped into the template through the `setup()` method
    setup() {
      // Story args can be spread into the returned object
      return {
        ...args
      };
    },
    // Then, the spread values can be accessed directly in the template
    template: `<BaseButton>BaseButton</BaseButton>`
  }),
  parameters: {
    // More on how to position stories at: https://storybook.js.org/docs/vue/configure/story-layout
    layout: 'fullscreen'
  }
};

export default meta;

type Story = StoryObj<typeof BaseButton>;

export const Default: Story = {};
