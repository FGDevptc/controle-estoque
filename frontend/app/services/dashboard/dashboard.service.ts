import { type AxiosInstance } from 'axios'
import { BaseService } from '../base/base.service'
import {
  DashboardResumoApiResponseSchema,
  type DashboardResumo,
} from '~/schemas/dashboard.schema'
import { DashboardError } from './error/dashboard.error'

export class DashboardService extends BaseService {
  constructor(http: AxiosInstance) {
    super(http)
  }

  async resumo(): Promise<DashboardResumo> {
    try {
      const { data } = await this.http.get('/api/dashboard/resumo')

      const parsed = this.parseOrThrow(
        DashboardResumoApiResponseSchema,
        data,
        DashboardError
      )

      return parsed.retorno
    } catch (e) {
      this.handleError(e, DashboardError)
    }
  }
}
