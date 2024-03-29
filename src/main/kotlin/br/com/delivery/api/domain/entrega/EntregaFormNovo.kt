package br.com.delivery.api.domain.entrega

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class EntregaFormNovo(
    @field: NotNull
    val pedidoId: Long,

    @field: NotBlank
    val endereco : String
)