package br.com.delivery.api.domain.cliente

data class ClienteViewAtualizado(
    val userId: Long,
    val nome: String,
    val login: String,
    val token: String,
) {
    constructor(cliente: Cliente, token: String) : this(cliente.id!!, cliente.nome, cliente.login, token)
}
