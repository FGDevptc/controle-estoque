import { z } from 'zod'
import { createApiResponseSchema } from './api-response.schema'

export const TipoMovimentacaoEnum = z.enum(['ENTRADA', 'SAIDA'])
export type TipoMovimentacao = z.infer<typeof TipoMovimentacaoEnum>

export const MovimentoRequestSchema = z
  .object({
    produtoId: z.number().int().positive('Produto obrigatorio'),
    tipo: TipoMovimentacaoEnum,
    quantidade: z.number().int().positive('Quantidade deve ser maior que zero'),
    valorVenda: z.number().positive('Valor de venda deve ser maior que zero').nullable().optional(),
  })
  .superRefine((data, ctx) => {
    if (data.tipo === 'SAIDA' && (data.valorVenda === null || data.valorVenda === undefined)) {
      ctx.addIssue({
        code: z.ZodIssueCode.custom,
        path: ['valorVenda'],
        message: 'Valor de venda obrigatorio para saida',
      })
    }
  })

export type MovimentoRequest = z.infer<typeof MovimentoRequestSchema>

export const MovimentoHistoricoSchema = z.object({
  id: z.number(),
  tipo: TipoMovimentacaoEnum,
  quantidade: z.number(),
  valorVenda: z.coerce.number().nullable().optional(),
  dataMovimento: z.string(),
})

export type MovimentoHistorico = z.infer<typeof MovimentoHistoricoSchema>

export const MovimentoHistoricoApiResponseSchema =
  createApiResponseSchema(z.array(MovimentoHistoricoSchema))

export const LucroProdutoSchema = z.object({
  produtoId: z.number(),
  lucroTotal: z.coerce.number(),
})

export type LucroProduto = z.infer<typeof LucroProdutoSchema>

export const LucroProdutoApiResponseSchema =
  createApiResponseSchema(LucroProdutoSchema)
