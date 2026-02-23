interface AppConfirmOptions {
  title?: string
  message: string
  confirmLabel?: string
  cancelLabel?: string
}

interface AppConfirmState {
  visible: boolean
  title: string
  message: string
  confirmLabel: string
  cancelLabel: string
}

const defaultState: Omit<AppConfirmState, 'visible'> = {
  title: 'Confirmar exclusao',
  message: '',
  confirmLabel: 'Excluir',
  cancelLabel: 'Cancelar',
}

let pendingResolver: ((value: boolean) => void) | null = null

export const useAppConfirm = () => {
  const state = useState<AppConfirmState>('app-confirm', () => ({
    visible: false,
    ...defaultState,
  }))

  function ask(options: AppConfirmOptions) {
    if (pendingResolver) {
      pendingResolver(false)
      pendingResolver = null
    }

    state.value = {
      visible: true,
      title: options.title ?? defaultState.title,
      message: options.message,
      confirmLabel: options.confirmLabel ?? defaultState.confirmLabel,
      cancelLabel: options.cancelLabel ?? defaultState.cancelLabel,
    }

    return new Promise<boolean>((resolve) => {
      pendingResolver = resolve
    })
  }

  function close(result: boolean) {
    if (pendingResolver) {
      pendingResolver(result)
      pendingResolver = null
    }

    state.value.visible = false
  }

  function confirm() {
    close(true)
  }

  function cancel() {
    close(false)
  }

  return {
    state,
    ask,
    confirm,
    cancel,
  }
}
