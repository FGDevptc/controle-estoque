import { type AxiosInstance } from 'axios'
import { BaseService } from '../base/base.service'
import {
  LucroProdutoApiResponseSchema,
  MovimentoHistoricoApiResponseSchema,
  type LucroProduto,
  type MovimentoHistorico,
  type MovimentoRequest,
} from '~/schemas/movimento.schema'
import { MovimentoError } from './error/movimento.error'

export class MovimentoService extends BaseService {
  constructor(http: AxiosInstance) {
    super(http)
  }

  async movimentar(payload: MovimentoRequest) {
    try {
      const { data } = await this.http.post('/api/movimentos', payload)

      this.throwIfErrors(data, MovimentoError)

      return (data as any)?.retorno ?? null
    } catch (e) {
      this.handleError(e, MovimentoError)
    }
  }

  async consultarLucro(produtoId: number): Promise<LucroProduto> {
    try {
      const { data } = await this.http.get(
        `/api/movimentos/produto/${produtoId}/lucro`
      )

      const parsed = this.parseOrThrow(
        LucroProdutoApiResponseSchema,
        data,
        MovimentoError
      )

      return parsed.retorno
    } catch (e) {
      this.handleError(e, MovimentoError)
    }
  }

  async listarPorProduto(produtoId: number): Promise<MovimentoHistorico[]> {
    try {
      const { data } = await this.http.get(
        `/api/movimentos/produto/${produtoId}`
      )

      const parsed = this.parseOrThrow(
        MovimentoHistoricoApiResponseSchema,
        data,
        MovimentoError
      )

      return parsed.retorno
    } catch (e) {
      this.handleError(e, MovimentoError)
    }
  }
}
