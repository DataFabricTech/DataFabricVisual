import { Meta, StoryObj } from "@storybook/vue3";
import DatePicker from "./date-picker.vue";
import { action } from "@storybook/addon-actions";
import { DateType } from "../../../components/common/date-picker/date-picker";

const meta: Meta<typeof DatePicker> = {
  component: DatePicker
};
export default meta;

type Story = StoryObj<typeof DatePicker>;
export const Primary: Story = {
  render: (args) => ({
    components: { DatePicker },
    template: `
      <span>단일 날짜 설정</span>
      <DatePicker 
        v-model:modelValue="args.date" 
        :type="args.dateType"
        @update="onUpdateDate">
      </DatePicker>
      
      <br>
      <span>기간 날짜 설정</span>
      <DatePicker 
        v-model:modelValue="args.dateRange" 
        :type="args.rangeType"
        @update="onUpdateDate">
      </DatePicker>
    `,
    setup() {
      return { args };
    },
    methods: {
      onUpdateDate(value: DateType) {
        action("clicked")(value);
      }
    }
  }),
  args: {
    date: "2023-11-22",
    dateRange: ["2023-11-10", "2023-11-22"],
    // storybook 에 object 형식으로 뜸
    dateType: "time",
    rangeType: "datetime"
  }
};
