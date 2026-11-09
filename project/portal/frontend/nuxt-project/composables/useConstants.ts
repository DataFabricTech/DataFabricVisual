export function useConstants() {
  const CONSTANT = Object.freeze({
    TITLE: "모비젠 데이터 카탈로그",

    PAGE_URL: {
      PORTAL_MAIN_PAGE: "/main"
    },

    EVENT: {
      STATUS: {
        ADD: 1,
        MODIFY: 2,
        DELETE: 3
      },
      TYPE: {
        METADATA: 0,
        SYSTEM: 1
      }
    },


  })

  return {
    CONSTANT
  };
}


