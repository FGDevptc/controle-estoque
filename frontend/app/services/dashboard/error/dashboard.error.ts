import { AppError } from '~/services/error/AppError'

export class DashboardError extends AppError {
  constructor(message = 'Erro ao processar requisicao de Dashboard', status?: number) {
    super(message, status)
    this.name = 'DashboardError'
  }
}
