package br.com.delivery.api.service

import br.com.delivery.api.domain.entrega.Entrega
import br.com.delivery.api.domain.entrega.EntregaFormAtualiza
import br.com.delivery.api.domain.entrega.EntregaFormNovo
import br.com.delivery.api.domain.entrega.EntregaRepostory
import br.com.delivery.api.infra.exception.EntregaNaoEncontradaException
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class EntregaService(
    private val repository: EntregaRepostory,
    private val pedidoService: PedidoService,
) {

    @Transactional
    fun criar(userId: Long, form: EntregaFormNovo): Entrega {
        val pedido = pedidoService.buscar(userId, form.pedidoId)

        if(pedido.entrega != null)
            throw  EntregaJaExistenteException()

        val entrega = Entrega(pedido, form)
        pedido.entrega = entrega
        return repository.save(entrega)
    }

    @Transactional
    fun atualizar(userId: Long, entregaId: Long, form: EntregaFormAtualiza) = buscar(userId, entregaId).atualiza(form)

    fun buscar(userId: Long, entregaId: Long): Entrega {
        val entrega = repository
            .findByIdOrNull(entregaId)
            ?: throw EntregaNaoEncontradaException()

        if(entrega.pedido.cliente.id != userId)
            throw EntregaNaoEncontradaException()

        return entrega
    }

    @Transactional
    fun deletar(userId: Long, entregaId: Long){
        val entrega = buscar(userId, entregaId)
        repository.delete(entrega)
    }


}
