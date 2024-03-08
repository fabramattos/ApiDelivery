package br.com.delivery.api.service

import br.com.delivery.api.domain.entrega.EntregaStatus
import br.com.delivery.api.domain.pedido.Pedido
import br.com.delivery.api.domain.pedido.PedidoFormAtualiza
import br.com.delivery.api.domain.pedido.PedidoFormNovo
import br.com.delivery.api.domain.pedido.PedidoRepository
import br.com.delivery.api.infra.exception.EntregaEmAndamentoException
import br.com.delivery.api.infra.exception.PedidoNaoEncontradoException
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class PedidoService(
    private val repository: PedidoRepository,
    private val clienteService: ClienteService,
) {

    @Transactional
    fun criar(userId: Long, form: PedidoFormNovo): Pedido {
        val cliente = clienteService.buscar(userId)
        val pedido = Pedido(cliente, form)
        cliente.pedidos.add(pedido)
        return repository.save(pedido)
    }

    @Transactional
    fun atualizar(userId: Long, pedidoId: Long, form: PedidoFormAtualiza) =
        buscar(userId, pedidoId)
            .takeUnless { it.entrega != null && it.entrega?.status != EntregaStatus.NAO_INICIADA }
            ?.atualiza(form)
            ?: throw EntregaEmAndamentoException()

    fun buscar(userId: Long, pedidoId: Long) = repository
        .findByIdOrNull(pedidoId)
        ?.takeIf { it.cliente.id == userId }
        ?: throw PedidoNaoEncontradoException()

    @Transactional
    fun deletar(userId: Long, pedidoId: Long) {
        val pedido = buscar(userId, pedidoId)
            .takeUnless { it.entrega?.status != EntregaStatus.NAO_INICIADA }
            ?: throw EntregaEmAndamentoException()

        repository.delete(pedido)
    }
}
