/**
 * @desc    목록 화면 가독성을 위해 긴 설명을 잘라서 표시
 *          전문은 각 화면의 title 툴팁으로 확인
 *
 * @text    자를 대상 문자열
 * @maxLength 표시할 최대 글자 수 (기본 30자)
 * @emptyText 값이 없을 때 표시할 문자열 (기본 "-")
 */
export const truncateDescription = (text, maxLength = 120, emptyText = "-") => {
  if (!text) return emptyText;
  return text.length > maxLength ? text.slice(0, maxLength) + "..." : text;
};
