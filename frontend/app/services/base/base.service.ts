import { type AxiosInstance, isAxiosError } from 'axios'
import { z } from 'zod'
import { AppError } from '~/services/error/AppError'

export abstract class BaseService {
  constructor(protected http: AxiosInstance) {}

  private extractBackendMessage(data: any): string | null {
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

  protected throwIfErrors(
    data: unknown,
    ErrorClass: new (message: string, status?: number) => Error
  ): void {
    if (!data || typeof data !== 'object' || !('erros' in data)) return

    const maybeMessage = this.extractBackendMessage(data)
    const hasErrors =
      Array.isArray((data as any).erros)
        ? (data as any).erros.length > 0
        : Boolean((data as any).erros)

    if (hasErrors) {
      throw new ErrorClass(
        maybeMessage ?? 'Erro inesperado',
        (data as any).codigoHTTP
      )
    }
  }

  protected parseOrThrow<T>(
    schema: z.ZodSchema<T>,
    data: unknown,
    ErrorClass: new (message: string, status?: number) => Error
  ): T {
    this.throwIfErrors(data, ErrorClass)

    const parsed = schema.safeParse(data)

    if (!parsed.success) {
      console.error(parsed.error.flatten())
      throw new ErrorClass('Erro inesperado')
    }

    return parsed.data
  }

  protected handleError(
    e: unknown,
    ErrorClass: new (message: string, status?: number) => Error
  ): never {
    if (e instanceof ErrorClass) {
      throw e
    }

    if (e instanceof AppError) {
      throw new ErrorClass(e.message, e.status)
    }

    if (isAxiosError(e)) {
      const status = e.response?.status

      const data = e.response?.data

      const message =
        this.extractBackendMessage(data) ?? 'Erro inesperado'

      throw new ErrorClass(message, status)
    }

    throw new ErrorClass('Erro inesperado')
  }
}
