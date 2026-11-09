// composables/ramda.ts
import * as Ramda from "ramda";

export const $R = Ramda;
export const isEmpties = Ramda.either(Ramda.isEmpty, Ramda.isNil);
