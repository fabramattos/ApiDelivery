package br.com.delivery.api.domain.entrega

import jakarta.validation.constraints.NotBlank

data class EntregaFormNovo(
    @field: NotBlank
    val endereco : String
)