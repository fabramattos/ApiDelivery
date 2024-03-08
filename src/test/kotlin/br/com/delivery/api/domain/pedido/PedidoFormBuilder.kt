package br.com.delivery.api.domain.pedido

class PedidoFormBuilder{
    fun formNovo() = PedidoFormNovo("Descrição do pedido")

    fun formAtualizado() = PedidoFormAtualiza("Descrição atualizada do pedido")
}