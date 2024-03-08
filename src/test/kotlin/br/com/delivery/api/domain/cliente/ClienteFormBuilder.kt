package br.com.delivery.api.domain.cliente

class ClienteFormBuilder{
    fun formNovo() = ClienteFormNovo(
        "melon husk",
        "user@email.com",
        "123456"
    )

    fun formAtualizado() = ClienteFormAtualiza(
        "nome atualizado",
        "atualizado@email.com",
        "11223344"
    )
}