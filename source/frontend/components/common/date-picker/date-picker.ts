export const enum PickerType {
  DATE = "date",
  DATETIME = "datetime",
  YEAR = "year",
  MONTH = "month",
  TIME = "time",
  WEEK = "week"
};
export const enum DateFormat {
  DATE = "YYYY-MM-DD",
  DATETIME = "YYYY-MM-DD HH:mm:ss",
  YEAR = "YYYY",
  MONTH = "YYYY-MM",
  TIME = "HH:mm:ss"
};

export type DateType = Array<String> | String;
