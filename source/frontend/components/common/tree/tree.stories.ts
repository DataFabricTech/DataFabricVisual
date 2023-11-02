import { Meta, StoryObj } from "@storybook/vue3";
import Tree from "./tree.vue";

const meta: Meta<typeof Tree> = {
  component: Tree
};
export default meta;

type Story = StoryObj<typeof Tree>;
export const Primary: Story = {
  render: (args) => ({
    components: { Tree },
    setup() {
      return { args };
    },
    template: `<Tree v-bind="args" v-on="$props"></Tree>`
  }),
  args: {
    node: {
      id1: {
        text: "text1",
        children: ["id11", "id12"],
      },
      id11: {
        text: "text11",
      },
      id12: {
        text: "text12",
      },
      id2: {
        text: "text2",
      },
      id3: {
        text: "text3",
        children: ["id31"],
        state: {
          opened: true
        }
      },
      id31: {
        text: "text31",
        children: ["id311"],
        state: {
          opened: true,
          disabled: false
        }
      },
      id311: {
        text: "text311",
        state: {
          disabled: false,
          checked: true
        }
      },
      id4: {
        text: "text4",
      },
    },
    config: { "roots": ["id1", "id2", "id3"] }
  }
};
