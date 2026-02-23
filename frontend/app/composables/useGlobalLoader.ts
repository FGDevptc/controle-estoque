export const useGlobalLoader = () => {
  const loading = useState<boolean>('global-loading', () => false)
  const activeRequests = useState<number>('global-loading-active-requests', () => 0)

  const start = () => {
    activeRequests.value += 1
    loading.value = true
  }

  const stop = () => {
    activeRequests.value = Math.max(0, activeRequests.value - 1)

    if (activeRequests.value === 0) {
      loading.value = false
    }
  }

  return {
    loading,
    start,
    stop,
  }
}
