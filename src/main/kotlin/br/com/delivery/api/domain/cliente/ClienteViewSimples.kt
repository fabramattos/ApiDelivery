package br.com.delivery.api.domain.cliente

data class ClienteViewSimples(
    val userId: Long,
    val nome: String,
    val login: String,
) {
    constructor(cliente: Cliente) : this(cliente.id!!, cliente.nome, cliente.login)
}
