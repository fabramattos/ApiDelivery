package br.com.delivery.api.domain.entrega

import br.com.delivery.api.domain.pedido.PedidoViewSimples

data class EntregaViewSimples(
    val entregaId: Long,
    val endereco: String,
    val status: String,
    val pedido: PedidoViewSimples,
) {

    constructor(entrega: Entrega) : this(
        entrega.id!!,
        entrega.endereco,
        entrega.status.descricao,
        PedidoViewSimples(entrega.pedido)
    )
}
