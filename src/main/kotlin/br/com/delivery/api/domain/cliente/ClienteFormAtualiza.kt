package br.com.delivery.api.domain.cliente

import jakarta.validation.constraints.Email

data class ClienteFormAtualiza(
    val nome: String?,
    @field: Email
    val login: String?,
    val senha: String?,
)