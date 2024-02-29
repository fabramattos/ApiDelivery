package br.com.delivery.api.domain.cliente

data class ClienteViewSimples(val cliente: Cliente) {
    private val id = cliente.id
    private val nome = cliente.nome
    private val login = cliente.login
}