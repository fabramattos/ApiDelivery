package br.com.delivery.api.domain.cliente

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank


data class ClienteFormNovo(
    @field:NotBlank
    val nome : String,
    @field: NotBlank  @field:Email
    val login : String,
    @field:NotBlank
    val senha : String
)