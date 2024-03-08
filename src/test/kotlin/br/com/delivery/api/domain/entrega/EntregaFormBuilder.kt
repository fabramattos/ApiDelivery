package br.com.delivery.api.domain.entrega

class EntregaFormBuilder{
    fun formNovo(pedidoId: Long) = EntregaFormNovo(
        pedidoId,
        "Endereço da entrega"
    )

    fun formAtualiza_EntregaNaoIniciada() = EntregaFormAtualiza(
        null,
        EntregaStatus.NAO_INICIADA
    )
    fun formAtualiza_EntregaIniciada() = EntregaFormAtualiza(
        null,
        EntregaStatus.A_CAMINHO
    )
    fun formAtualiza_Endereco() = EntregaFormAtualiza(
        "Endereço da entrega atualizado",
        null
    )
}