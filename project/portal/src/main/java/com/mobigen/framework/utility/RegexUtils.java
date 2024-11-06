package com.mobigen.framework.utility;

public class RegexUtils {
    // 허용할 문자만 남기는 정규식 (영문자, 숫자, 일부 특수 문자만 허용)
    public static String sanitizeInput(String input) {
        if (input == null) return null;
        String pattern = "^[a-zA-Z0-9@._:-]*$";
        
        if (!input.matches(pattern)) {
            throw new IllegalArgumentException("유효하지 않은 입력입니다.");
        }
        return input;
    }
    // 허용할 문자만 남기는 정규식 (영문자, 숫자, 일부 특수 문자만 허용)
    public static String sanitizeInputRequestDate(String input) {
        if (input == null) return null;
        String pattern = "^[a-zA-Z0-9@._:-\\s]*$";
        
        if (!input.matches(pattern)) {
            throw new IllegalArgumentException("유효하지 않은 입력입니다.");
        }
        return input;
    }
}
