package br.com.delivery.api.domain.pedido

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class PedidoFormAtualiza(
    @field: NotBlank @field: NotNull
    val descricao: String
)
