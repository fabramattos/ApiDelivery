package br.com.delivery.api.domain.entrega

data class EntregaFormAtualiza(
    val endereco : String?,
    val status: EntregaStatus?
)
