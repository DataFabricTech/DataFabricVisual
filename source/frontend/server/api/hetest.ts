interface response {
  code : number,
  errMsg: String,
  data: any
}
export default defineEventHandler(() => {
  const res: response = {
    code: 0,
    errMsg: "",
    data: {
      data: "hello - test"
    }
  };
  return res
})
