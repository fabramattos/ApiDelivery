package br.com.delivery.api.service

import br.com.delivery.api.domain.entrega.*
import br.com.delivery.api.infra.exception.EntregaEmAndamentoException
import br.com.delivery.api.infra.exception.EntregaExistenteException
import br.com.delivery.api.infra.exception.EntregaNaoEncontradaException
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class EntregaService(
    private val repository: EntregaRepository,
    private val pedidoService: PedidoService,
) {

    @Transactional
    fun criar(userId: Long, form: EntregaFormNovo) =
        pedidoService
            .buscar(userId, form.pedidoId)
            .takeIf { it.entrega == null }
            ?.let { pedido ->
                val entrega = Entrega(pedido, form)
                pedido.entrega = entrega
                repository.save(entrega)
            } ?: throw EntregaExistenteException()

    @Transactional
    fun atualizar(userId: Long, entregaId: Long, form: EntregaFormAtualiza) = buscar(userId, entregaId).atualiza(form)

    fun buscar(userId: Long, entregaId: Long) = repository
        .findByIdOrNull(entregaId)
        ?.takeIf { it.pedido.cliente.id == userId }
        ?: throw EntregaNaoEncontradaException()

    @Transactional
    fun deletar(userId: Long, entregaId: Long) {
        val entrega = buscar(userId, entregaId)
            .takeIf { it.status == EntregaStatus.NAO_INICIADA }
            ?.apply { pedido.entrega = null }
            ?: throw EntregaEmAndamentoException()

        repository.delete(entrega)
    }
}
