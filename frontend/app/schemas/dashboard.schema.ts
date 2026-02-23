import { z } from 'zod'
import { createApiResponseSchema } from './api-response.schema'

export const DashboardResumoSchema = z.object({
  totalProdutos: z.coerce.number(),
  totalEstoque: z.coerce.number(),
  lucroTotal: z.coerce.number(),
})

export type DashboardResumo = z.infer<typeof DashboardResumoSchema>

export const DashboardResumoApiResponseSchema =
  createApiResponseSchema(DashboardResumoSchema)
