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
      data: "ddd"
    }
  };
  return res
})
