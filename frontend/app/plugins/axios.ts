import axios from 'axios'
import { AppError } from '~/services/error/AppError'

export default defineNuxtPlugin(() => {
  const config = useRuntimeConfig()

  const instance = axios.create({
    baseURL: config.public.apiBase,
  })

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

  instance.interceptors.response.use(
    (response) => {
      const data = response?.data
      if (data && typeof data === 'object' && 'erros' in data) {
        const hasErrors =
          Array.isArray((data as any).erros)
            ? (data as any).erros.length > 0
            : Boolean((data as any).erros)

        if (hasErrors) {
          const message =
            extractBackendMessage(data) ?? 'Erro inesperado'
          return Promise.reject(
            new AppError(message, (data as any).codigoHTTP)
          )
        }
      }

      return response
    },
    (error) => {
      const data = error?.response?.data
      const message =
        extractBackendMessage(data) ??
        (error?.message ? String(error.message) : 'Erro inesperado')
      return Promise.reject(new AppError(message, error?.response?.status))
    }
  )

  return {
    provide: {
      axios: instance,
    },
  }
})
