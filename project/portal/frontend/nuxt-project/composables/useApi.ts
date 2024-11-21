// composables/useApi.js

import {useNuxtApp, useRuntimeConfig} from "nuxt/app";


export function useApi() {
  const { $ofetch } = useNuxtApp();
  const runConfig = useRuntimeConfig().public

  // event log api 호출
  const setEventLog = async (params) => {
    return await $ofetch(`${runConfig.API_URL}/log`, {
      method: "POST",
      body: params
    })
  }

  // sync meta data api 호출
  const setMetaSync = async (metaId: string, event_status: number | string, event_name = "") => {
    return await $ofetch(`${runConfig.SYNC_API_URL}/update`, {
      method: "POST",
      body: {
        metaid: metaId,
        event_status,
        event_name
      }
    });
  };

  return {
    setEventLog,
    setMetaSync
  }
}


