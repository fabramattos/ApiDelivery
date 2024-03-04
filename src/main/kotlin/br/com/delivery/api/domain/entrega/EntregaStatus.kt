package br.com.delivery.api.domain.entrega

enum class EntregaStatus(val descricao: String) {
    NAO_INICIADA("Não Iniciada"),
    PREPARANDO("Preparando"),
    A_CAMINHO("A Caminho"),
    ENTREGUE("Entregue"),
    PROBLEMA("Problema")
}

