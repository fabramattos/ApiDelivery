package br.com.delivery.api.infra.database

import br.com.delivery.api.domain.cliente.ClienteFormNovo
import br.com.delivery.api.domain.cliente.ClienteRepository
import br.com.delivery.api.domain.entrega.EntregaFormNovo
import br.com.delivery.api.domain.entrega.EntregaRepository
import br.com.delivery.api.domain.pedido.PedidoFormNovo
import br.com.delivery.api.domain.pedido.PedidoRepository
import br.com.delivery.api.service.ClienteService
import br.com.delivery.api.service.EntregaService
import br.com.delivery.api.service.PedidoService
import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Component

@Component
class DatabaseInitialization(
    private var clienteRepository: ClienteRepository,
    private var entregaRepository: EntregaRepository,
    private var pedidoRepository: PedidoRepository,
    private var clienteService: ClienteService,
    private var pedidoService: PedidoService,
    private var entregaService: EntregaService,
) {

    @PostConstruct
    fun inicializaDatabase() {
        removeDados()
        criaUsuarioComEntrega()
    }

    private fun removeDados() {
        entregaRepository.deleteAll()
        pedidoRepository.deleteAll()
        clienteRepository.deleteAll()
    }

    private fun criaUsuarioComEntrega() {
        val cliente = clienteService
            .criar(
                ClienteFormNovo(
                    "Melon Husk",
                    "user@email.com",
                    "123456"
                )
            )

        val pedido = pedidoService.criar(cliente.id!!, PedidoFormNovo("pedido teste"))

        entregaService.criar(
            cliente.id, EntregaFormNovo(
                pedido.id!!,
                "Endereço do pedido"
            )
        )
    }

}