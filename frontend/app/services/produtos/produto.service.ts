import { BaseService } from '../base/base.service'
import { type AxiosInstance } from 'axios'
import {
  ProdutoSchema,
  ProdutoApiResponseSchema,
  type ProdutoRequest,
  type Produto,
} from '~/schemas/produto.schema'
import { ProdutoError } from './error/produto.error'

export class ProdutoService extends BaseService {
  constructor(http: AxiosInstance) {
    super(http)
  }

  async listar(params?: {
    page?: number
    size?: number
    tipo?: string
  }) {
    try {
      const { data } = await this.http.get(
        '/api/produtos',
        { params },
      )

      const parsed = this.parseOrThrow(
        ProdutoApiResponseSchema,
        data,
        ProdutoError,
      )

      return parsed.retorno
    } catch (e) {
      this.handleError(e, ProdutoError)
    }
  }

  async buscarPorId(id: number): Promise<Produto> {
    try {
      const { data } = await this.http.get(
        `/api/produtos/${id}`,
      )

      return this.parseOrThrow(
        ProdutoSchema,
        data.retorno ?? data,
        ProdutoError,
      )
    } catch (e) {
      this.handleError(e, ProdutoError)
    }
  }
  async criar(payload: ProdutoRequest) {
    try {
      const { data } = await this.http.post(
        '/api/produtos',
        payload
      )

      this.throwIfErrors(data, ProdutoError)

      return (data as any)?.retorno ?? data
    } catch (e) {
      this.handleError(e, ProdutoError)
    }
  }

  async atualizar(id: number, payload: ProdutoRequest) {
    try {
      const { data } = await this.http.put(
        `/api/produtos/${id}`,
        payload
      )

      this.throwIfErrors(data, ProdutoError)

      return (data as any)?.retorno ?? data
    } catch (e) {
      this.handleError(e, ProdutoError)
    }
  }

  async remover(id: number) {
    try {
      await this.http.delete(
        `/api/produtos/${id}`
      )
    } catch (e) {
      this.handleError(e, ProdutoError)
    }
  }
}
