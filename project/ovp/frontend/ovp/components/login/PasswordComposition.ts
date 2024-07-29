import type { Ref } from "vue";
import $constants from "~/utils/constant";

export interface PasswordCompositionImpl {
  newPassword: string;
  confirmPassword: string;
  // 비밀번호 보기 관련 변수
  inputPasswordType: string;
  inputConfirmPasswordType: string;

  // 에러 변수
  errorMsgPassword: string;
  errorMsgConfirmPassword: string;

  // 비밀번호 관련 함수
  isHidePw(): void;
  isHideConfirmPw(): void;
  validatePassword(): boolean;
  validateConfirmPassword(): boolean;
  resetValues(): void;
  isValidatePwReset(): boolean;
  getPwResetParam(): object;
}

/**
 * 비밀번호 변경 관련 Composition
 * @constructor
 */
export function PasswordComposition(): PasswordCompositionImpl {
  const newPassword: Ref<string> = ref("");
  const confirmPassword: Ref<string> = ref("");

  // 비밀번호 보기 해제
  const inputPasswordType: Ref<string> = ref("password");
  const isHidePw = () => {
    inputPasswordType.value =
      inputPasswordType.value === "password" ? "text" : "password";
  };
  const inputConfirmPasswordType: Ref<string> = ref("password");
  const isHideConfirmPw = () => {
    inputConfirmPasswordType.value =
      inputConfirmPasswordType.value === "password" ? "text" : "password";
  };

  // 비밀번호 에러 메세지
  const errorMsgPassword: Ref<string> = ref("");
  const errorMsgConfirmPassword: Ref<string> = ref("");

  /**
   * 비밀번호 변경 에러 처리
   */
  const validatePassword = () => {
    errorMsgPassword.value = newPassword.value
      ? ""
      : $constants.LOGIN.PASSWORD.INPUT_ERROR_MSG;
    if (errorMsgPassword.value) {
      return false;
    }

    errorMsgPassword.value = $constants.LOGIN.PASSWORD.REGEX.test(
      newPassword.value,
    )
      ? ""
      : $constants.LOGIN.PASSWORD.REGEX_ERROR_MSG;
    return !errorMsgPassword.value;
  };

  const validateConfirmPassword = () => {
    errorMsgConfirmPassword.value = confirmPassword.value
      ? ""
      : $constants.LOGIN.PASSWORD.INPUT_ERROR_MSG;
    if (errorMsgConfirmPassword.value) {
      return false;
    }
    errorMsgConfirmPassword.value =
      newPassword.value === confirmPassword.value
        ? ""
        : $constants.LOGIN.PASSWORD.MATCH_ERROR_MSG;
    return !errorMsgConfirmPassword.value;
  };

  const isValidatePwReset = () => {
    return validatePassword() && validateConfirmPassword();
  };

  const getPwResetParam = () => {
    return {
      confirmPassword: confirmPassword.value,
      newPassword: newPassword.value,
    };
  };

  const resetValues = () => {
    newPassword.value = "";
    confirmPassword.value = "";
    inputPasswordType.value = "";
    inputConfirmPasswordType.value = "";
    errorMsgPassword.value = "";
    errorMsgConfirmPassword.value = "";
  };

  return {
    newPassword,
    confirmPassword,
    inputPasswordType,
    inputConfirmPasswordType,
    errorMsgPassword,
    errorMsgConfirmPassword,
    resetValues,
    isHidePw,
    isHideConfirmPw,
    validatePassword,
    validateConfirmPassword,
    isValidatePwReset,
    getPwResetParam,
  };
}
