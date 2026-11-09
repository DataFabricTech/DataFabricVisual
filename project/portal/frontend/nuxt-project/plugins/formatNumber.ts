export default defineNuxtPlugin((nuxtApp) => {
  nuxtApp.provide('numberComma', (value) => {
    if (!!Number(value)) {
      if (Number(value) > 1) {
        let textArr = String(value).split(".");
        if (Number(textArr[0]) > 1) {
          if (textArr.length > 1) {
            return (
              String(textArr[0]).replace(/\B(?=(\d{3})+(?!\d))/g, ",") + "." + textArr[1]
            );
          } else {
            return String(textArr[0]).replace(/\B(?=(\d{3})+(?!\d))/g, ",");
          }
        } else {
          return value;
        }
      } else {
        return value;
      }
    } else {
      if (Number(value) === 0) {
        return value;
      }
    }
  });
});
