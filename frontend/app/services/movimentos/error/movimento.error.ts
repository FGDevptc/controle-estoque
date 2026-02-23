import { AppError } from '~/services/error/AppError'

export class MovimentoError extends AppError {
  constructor(message = 'Erro ao processar requisicao de Movimentacao', status?: number) {
    super(message, status)
    this.name = 'MovimentoError'
  }
}
