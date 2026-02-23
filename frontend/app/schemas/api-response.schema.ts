import { z } from 'zod'

export const createPageSchema = <T extends z.ZodTypeAny>(
  itemSchema: T,
) =>
  z.object({
    content: z.array(itemSchema),
    page: z.number(),
    size: z.number(),
    totalElements: z.number(),
    totalPages: z.number(),
    hasNext: z.boolean(),
    hasPrevious: z.boolean(),
  })

export const createApiResponseSchema = <T extends z.ZodTypeAny>(
  retornoSchema: T,
) =>
  z.object({
    codigoHTTP: z.number(),
    retorno: retornoSchema,
    erros: z.any().nullable(),
  })