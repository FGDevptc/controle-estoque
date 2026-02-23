export const useApi = () => {
  const { start, stop } = useGlobalLoader()
  const { $toast } = useNuxtApp()

  const extractBackendMessage = (data: any): string | null => {
    if (!data) return null

    if (typeof data === 'string') return data

    if (data.mensagem) return String(data.mensagem)
    if (data.message) return String(data.message)

    if (Array.isArray(data.erros)) {
      const messages = data.erros
        .map((err: any) => {
          if (!err) return null
          if (typeof err === 'string') return err
          if (err.mensagem) return String(err.mensagem)
          if (err.message) return String(err.message)
          try {
            return JSON.stringify(err)
          } catch {
            return null
          }
        })
        .filter((msg: string | null): msg is string => Boolean(msg))

      if (messages.length > 0) return messages.join(', ')
    }

    if (data.erros) {
      if (typeof data.erros === 'string') return data.erros
      try {
        return JSON.stringify(data.erros)
      } catch {
        return null
      }
    }

    return null
  }

  const extractSuccessMessage = (response: any): string | null => {
    if (!response) return null

    if (response.mensagem) return String(response.mensagem)
    if (response.message) return String(response.message)

    if (response.retorno?.mensagem) return String(response.retorno.mensagem)
    if (response.retorno?.message) return String(response.retorno.message)

    return null
  }

  const execute = async <T>(
    fn: () => Promise<T>,
    options?: {
      successMessage?: string
      errorMessage?: string
    }
  ): Promise<T | null> => {
    try {
      start()

      const response = await fn()

      const responseError = extractBackendMessage(response)
      if (responseError) {
        $toast.error(options?.errorMessage || responseError)
        return null
      }

      if (options?.successMessage) {
        $toast.success(options.successMessage)
      } else {
        const successMessage = extractSuccessMessage(response)
        if (successMessage) {
          $toast.success(successMessage)
        }
      }

      return response
    } catch (error: any) {
      console.error(error)

      const backendMessage =
        (error instanceof Error ? error.message : null) ||
        extractBackendMessage(error?.response?.data)

      $toast.error(options?.errorMessage || backendMessage || 'Erro inesperado')

      return null
    } finally {
      stop()
    }
  }

  return { execute }
}
