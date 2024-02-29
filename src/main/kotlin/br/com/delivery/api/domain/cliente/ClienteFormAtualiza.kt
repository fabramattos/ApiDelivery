package br.com.delivery.api.domain.cliente

import jakarta.validation.constraints.Email

data class ClienteFormAtualiza(
    val nome: String? = null,
    @field: Email
    val login: String? = null,
    val senha: String? = null,
)