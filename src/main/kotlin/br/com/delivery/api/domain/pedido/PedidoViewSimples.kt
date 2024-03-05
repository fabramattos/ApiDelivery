package br.com.delivery.api.domain.pedido

data class PedidoViewSimples(
    val pedidoId: Long,
    val descricao: String,
) {
    constructor(pedido: Pedido) : this(pedido.id!!, pedido.descricao)
}