import { z } from 'zod'
import {
  createApiResponseSchema,
  createPageSchema,
} from './api-response.schema'

export const TipoProdutoEnum = z.enum([
  'ELETRONICO',
  'ELETRODOMESTICO',
  'MOVEL',
])

export const ProdutoSchema = z.object({
  id: z.number(),
  codigo: z.string(),
  descricao: z.string(),
  tipoProduto: TipoProdutoEnum,
  valorFornecedor: z.number(),
  quantidadeEstoque: z.number(),
  quantidadeDisponivel: z.number(),
  quantidadeTotalSaida: z.number(),
})

export type Produto = z.infer<typeof ProdutoSchema>


export const ProdutoRequestSchema = z.object({
  codigo: z.string().min(1, 'Código obrigatório'),
  descricao: z.string().min(3, 'Descrição mínima de 3 caracteres'),
  tipoProduto: TipoProdutoEnum,
  valorFornecedor: z.number().positive('Valor deve ser maior que zero'),
  quantidadeEstoque: z.number().int().nonnegative('Estoque inválido'),
})

export type ProdutoRequest = z.infer<typeof ProdutoRequestSchema>

export const ProdutoPageSchema =
  createPageSchema(ProdutoSchema)

export const ProdutoApiResponseSchema =
  createApiResponseSchema(ProdutoPageSchema)
