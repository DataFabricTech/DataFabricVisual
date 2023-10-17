interface response {
  result : number,
  errorMessage: String,
  data: any
}
export default defineEventHandler(() => {
  const res: response = {
    result: 0,
    errorMessage: "서버 에러",
    data: null
  };
  return res
})
