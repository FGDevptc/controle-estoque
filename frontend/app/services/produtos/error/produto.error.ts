import { AppError } from '~/services/error/AppError'

export class ProdutoError extends AppError {
  constructor(message = 'Erro ao processar requisicao de Produto', status?: number) {
    super(message, status)
    this.name = 'ProdutoError'
  }
}
