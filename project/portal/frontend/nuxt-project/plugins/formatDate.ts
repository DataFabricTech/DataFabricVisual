export default defineNuxtPlugin((nuxtApp) => {
  nuxtApp.provide('yyyyMMdd', (value) => {
    let date = new Date(value);
    let year = date.getFullYear();
    let month = String(date.getMonth() + 1).padStart(2, "0");
    let day = String(date.getDate()).padStart(2, "0");
    let hours = String(date.getHours()).padStart(2, "0");
    let min = String(date.getMinutes()).padStart(2, "0");
    let sec = String(date.getSeconds()).padStart(2, "0");
    if (value == null || value == undefined || year == 'NaN' || month == 'NaN' || day == 'NaN' || hours == 'NaN' || min == 'NaN' || sec == 'NaN') {
      return "-";
    }
    return year + "-" + month + "-" + day;
  });

  nuxtApp.provide('yyyyMMddHHmmSS', (value) => {
    let date = new Date(value);
    let year = date.getFullYear();
    let month = String(date.getMonth() + 1).padStart(2, "0");
    let day = String(date.getDate()).padStart(2, "0");
    let hours = String(date.getHours()).padStart(2, "0");
    let min = String(date.getMinutes()).padStart(2, "0");
    let sec = String(date.getSeconds()).padStart(2, "0");
    if (value == null || value == undefined || year == 'NaN' || month == 'NaN' || day == 'NaN' || hours == 'NaN' || min == 'NaN' || sec == 'NaN') {
      return "-";
    }
    return year + "-" + month + "-" + day + " " + hours + ":" + min + ":" + sec;
  });

  nuxtApp.provide('yyyyMMddHHmmSS_Sep', (value, sep) => {
    let date = new Date(value);
    let year = date.getFullYear();
    let month = String(date.getMonth() + 1).padStart(2, "0");
    let day = String(date.getDate()).padStart(2, "0");
    let hours = String(date.getHours()).padStart(2, "0");
    let min = String(date.getMinutes()).padStart(2, "0");
    let sec = String(date.getSeconds()).padStart(2, "0");
    if (value == null || value == undefined || year == 'NaN' || month == 'NaN' || day == 'NaN' || hours == 'NaN' || min == 'NaN' || sec == 'NaN') {
      return "-";
    }

    return year + "-" + month + "-" + day + sep + hours + ":" + min + ":" + sec;
  });

  nuxtApp.provide('HHmmSS', (value) => {
    let date = new Date(value);
    let year = date.getFullYear();
    let month = String(date.getMonth() + 1).padStart(2, "0");
    let day = String(date.getDate()).padStart(2, "0");
    let hours = String(date.getHours()).padStart(2, "0");
    let min = String(date.getMinutes()).padStart(2, "0");
    let sec = String(date.getSeconds()).padStart(2, "0");
    if (value == null || value == undefined || year == 'NaN' || month == 'NaN' || day == 'NaN' || hours == 'NaN' || min == 'NaN' || sec == 'NaN') {
      return "-";
    }

    return hours + ":" + min + ":" + sec;
  });

  nuxtApp.provide('yyyyMMdd_dot', (value) => {
    let date = new Date(value);
    let year = date.getFullYear();
    let month = String(date.getMonth() + 1).padStart(2, "0");
    let day = String(date.getDate()).padStart(2, "0");
    if (value == null || value == undefined) {
      return "";
    }
    return year + "." + month + "." + day;
  });
});
