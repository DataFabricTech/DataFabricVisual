interface response {
  code : number,
  errMsg: String,
  data: any
}
export default defineEventHandler(() => {
  const res: response = {
    code: 401,
    errMsg: "서버 에러",
    data:null
  };
  return res
})
