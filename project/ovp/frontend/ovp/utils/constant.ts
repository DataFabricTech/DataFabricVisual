const $constants = {
  LOGIN: {
    PASSWORD: {
      REGEX:
        /^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[~`!@#$%^()_\-=\[\]{}|;:<>,.\/?]).{8,56}$/,
      REGEX_ERROR_MSG:
        "비밀번호는 최소 8자, 최대56자여야 하며 대문자(AZ), 소문자(az), 숫자, 특수문자(예:!,%,@ 또는 #등)를 하나 이상 포함해야 합니다.",
      MATCH_ERROR_MSG: "비밀번호가 일치하지 않습니다.",
      INPUT_ERROR_MSG: "사용자 비밀번호를 입력하세요.",
    },
    EMAIL: {
      REGEX: /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/,
    },
  },
};
export default Object.freeze($constants);
