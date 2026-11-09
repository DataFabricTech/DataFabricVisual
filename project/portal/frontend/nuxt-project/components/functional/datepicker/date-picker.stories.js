import DatePicker from "./date-picker";
import { action } from "@storybook/addon-actions";


export default {
  title: "Functional/[PIFI] date-picker",
  components: DatePicker,
  decorators: [
    () => ({
      template:
        '<div style="margin: 1em;"><h2 style="margin-bottom: 20px; font-size: 20px; color: #212121;">(예시) DatePicker </h2><div style="display: flex; flex-direction: row; "><story /></div></div>'
    })
  ],
  args: {
    date: "2023-07-20",
    startDate: "2023.07.20",
    endDate: "20231223",
    format: "",
    type: "date",
    range: false,
    confirm: true,
    confirmText: "OK",
    disabled: false,
    placeholder: ""
  },
  argTypes: {
    date: {
      type: "string",
      description: "단일 설정 일때 값"
    },
    startDate: {
      type: "string",
      description: "기간 설정 - 시작시간"
    },
    endDate: {
      type: "string",
      description: "기간설정 - 끝시간"
    },
    format: {
      type: "string",
      description: "날짜 포맷",
      table: {
        defaultValue: {
          summary: "DATE: YYYY-MM-DD|DATETIME: YYYY-MM-DD HH:mm:ss|YEAR: YYYY|MONTH: YYYY-MM|TIME: HH:mm:ss"
        }
      }
    },
    type: {
      type: "string",
      description: "picker 타입",
      control: {
        type: "select",
        options: ["date", "datetime", "year", "month", "time", "week"]
      },
      table: { defaultValue: { summary: "date" } }
    },
    range: {
      type: "boolean",
      description: "기간 설정",
      table: { defaultValue: { summary: false } }
    },
    confirm: {
      type: "boolean",
      description: "확인 버튼",
      table: { defaultValue: { summary: false } }
    },
    confirmText: {
      type: "string",
      description: "확인 버튼 메세지",
      table: { defaultValue: { summary: "OK" } }
    },
    disabled: {
      type: "boolean",
      description: "disabled",
      table: { defaultValue: { summary: false } }
    },
    placeholder: {
      type: "string",
      description: "placeholder"
    }
  }
};

export const Default = (args, { argTypes }) => {
  return {
    components: { DatePicker },
    template: `<Date-picker
      :date="date"
      :start-date="startDate"
      :end-date="endDate"
      :format="format"
      :type="type"
      :range="range"
      :confirm="confirm"
      :confirm-text="confirmText"
      :disabled="disabled"
      :placeholder="placeholder"
      @change="change" />
    `,
    computed: {
      date() {
        return args.date;
      },
      startDate() {
        return args.startDate;
      },
      endDate() {
        return args.endDate;
      },
      format() {
        return args.format;
      },
      type() {
        return args.type;
      },
      range() {
        return args.range;
      },
      confirm() {
        return args.confirm;
      },
      confirmText() {
        return args.confirmText;
      },
      disabled() {
        return args.disabled;
      },
      placeholder() {
        return args.placeholder;
      }
    },
    methods: {
      change(val) {
        action("change")(val);
      }
    }
  };
};
