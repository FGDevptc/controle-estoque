import { defineStore } from 'pinia'
import { ProdutoService } from '~/services/produtos/produto.service'
import type { Produto, ProdutoRequest } from '~/schemas/produto.schema'

export const useProdutosStore = defineStore('produtos', () => {
  const { $axios } = useNuxtApp()
  const service = new ProdutoService($axios)

  const itens = ref<Produto[]>([])
  const loading = ref(false)
  const error = ref<string | null>(null)
  const tipoProdutoFiltro = ref<string | null>(null)

  const page = ref(0)
  const size = ref(5)
  const total = ref(0)

  async function listar() {
    loading.value = true
    error.value = null

    try {
      const res = await service.listar({
        page: page.value,
        size: size.value,
        tipo: tipoProdutoFiltro.value ?? undefined,
      })

      if (!res) return

      itens.value = res.content
      total.value = res.totalElements
      page.value = res.page
      size.value = Math.min(res.size, 5)
    } catch (e: any) {
      error.value = e.message
    } finally {
      loading.value = false
    }
  }

  async function criar(payload: ProdutoRequest) {
    const result = await service.criar(payload)
    await listar()
    return result
  }

  async function atualizar(id: number, payload: ProdutoRequest) {
    const result = await service.atualizar(id, payload)
    await listar()
    return result
  }

  async function remover(id: number) {
    await service.remover(id)
    await listar()
  }

  return {
    itens,
    loading,
    error,
    page,
    size,
    total,
    tipoProdutoFiltro,
    listar,
    criar,
    atualizar,
    remover,
  }
})
