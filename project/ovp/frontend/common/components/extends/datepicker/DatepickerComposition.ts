import type { DatepickerProps } from "./DatepickerProps";
import _ from "lodash";
import type { Ref } from "vue";

interface DatepickerComposition extends DatepickerProps {
  dateTimePanel: Ref<boolean>;

  disabledDate(date: Date): boolean;

  disabledTime(date: Date): boolean;

  isPanelVisible(): boolean;

  switchDateTimePanel(): void;
}

export function DatepickerComposition(props: DatepickerProps): DatepickerComposition {
  const DATETIME = "datetime";
  const dayjs = useDayjs();
  const disabledDate: (date: Date) => boolean = (date: Date): boolean => {
    if (_.isNil(props.disabledDateRange)) {
      return false;
    }
    if (props.disabledDateRange) {
      const [start, end] = props.disabledDateRange;
      return calculateRange(date, start, end);
    }
    return false;
  };
  const disabledTime = (date: Date): boolean => {
    if (_.isNil(props.disabledTimeRange) || _.isEmpty(props.disabledTimeRange)) {
      return false;
    }
    const [start, end] = props.disabledTimeRange!;
    if (!validateDate(start, end)) {
      return false;
    }
    let startDateTime: string | null = start === null ? null : `${dayjs(date).format("YYYY-MM-DD")} ${start}`;
    let endDateTime: string | null = end === null ? null : `${dayjs(date).format("YYYY-MM-DD")} ${end}`;
    return calculateRange(date, startDateTime, endDateTime);
  };
  const validateDate = (start: string | null | undefined, end: string | null | undefined): boolean => {
    if (start === undefined || end === undefined || start === "" || end === "") {
      console.error("Invalid props disabledTimeRange");
      return false;
    }
    return true;
  };
  const calculateRange = (date: Date, start: string | null, end: string | null): boolean => {
    if (!validateDate(start, end)) {
      return false;
    }
    if (start === null && end !== null) {
      return dayjs(date).isSameOrBefore(end);
    } else if (end === null && start !== null) {
      return dayjs(date).isSameOrAfter(start);
    } else if (start !== null && end !== null) {
      return dayjs(date).isBetween(start, end);
    } else {
      console.error("Invalid parameters.");
      return false;
    }
  };

  const dateTimePanel: Ref<boolean> = ref(false);
  if (props.type === DATETIME) {
    if (props.showTimePanel) {
      dateTimePanel.value = false;
    }
  }
  const isPanelVisible = (): boolean => {
    return <boolean>props.showTimePanel && props.type === DATETIME;
  };
  const switchDateTimePanel = (): void => {
    dateTimePanel.value = !dateTimePanel.value;
  };
  return { ...props, dateTimePanel, disabledDate, disabledTime, isPanelVisible, switchDateTimePanel };
}
