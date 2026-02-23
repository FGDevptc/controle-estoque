import type { Produto } from '~/schemas/produto.schema'
import { useApi } from '~/composables/useApi'

interface ProdutosLookupOptions {
  page?: number
  size?: number
  errorMessage?: string
}

export const useProdutosLookup = () => {
  const { $axios } = useNuxtApp()
  const { execute } = useApi()

  const produtos = ref<Produto[]>([])
  const carregandoProdutos = ref(false)

  const produtoOptions = computed(() =>
    produtos.value.map((produto) => ({
      label: `${produto.codigo} - ${produto.descricao}`,
      value: produto.id,
    }))
  )

  const findProdutoById = (id: number | null) =>
    produtos.value.find((produto) => produto.id === id) ?? null

  async function carregarProdutos(options?: ProdutosLookupOptions) {
    carregandoProdutos.value = true

    const response = await execute(
      async () => {
        const { data } = await $axios.get('/api/produtos', {
          params: {
            page: options?.page ?? 0,
            size: options?.size ?? 200,
          },
        })

        return data
      },
      {
        errorMessage: options?.errorMessage ?? 'Erro ao carregar produtos',
      }
    )

    carregandoProdutos.value = false

    if (!response) {
      produtos.value = []
      return null
    }

    produtos.value = response.retorno?.content ?? []
    return produtos.value
  }

  return {
    produtos,
    carregandoProdutos,
    produtoOptions,
    findProdutoById,
    carregarProdutos,
  }
}
