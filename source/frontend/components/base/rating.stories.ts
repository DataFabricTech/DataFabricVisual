import { Meta, StoryObj } from "@storybook/vue3";
import Rating from "./rating.vue";
import { action } from "@storybook/addon-actions";

const meta: Meta<typeof Rating> = {
  component: Rating
};
export default meta;

type Story = StoryObj<typeof Rating>;
export const Primary: Story = {
  render: (args) => ({
    components: { Rating },
    template: `
      <span>등록</span>
      <Rating
        @change="setStar">
      </Rating>
      
      <br>
      <span>조회</span>
      <Rating 
        :star="3"
        :disabled="true">
      </Rating>
    `,
    setup() {
      return { args };
    },
    methods: {
      setStar(value: number) {
        action("clicked")(value);
      }
    }
  })
};