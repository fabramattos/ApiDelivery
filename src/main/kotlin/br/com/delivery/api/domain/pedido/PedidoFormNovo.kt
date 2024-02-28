package br.com.delivery.api.domain.pedido

import jakarta.validation.constraints.NotBlank

data class PedidoFormNovo (
    @field: NotBlank
    val descricao : String
){

}
