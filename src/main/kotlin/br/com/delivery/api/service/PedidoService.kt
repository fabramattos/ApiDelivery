package br.com.delivery.api.service

import br.com.delivery.api.br.com.delivery.api.domain.pedido.PedidoFormAtualiza
import br.com.delivery.api.domain.pedido.Pedido
import br.com.delivery.api.domain.pedido.PedidoFormNovo
import br.com.delivery.api.domain.pedido.PedidoRepository
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
    fun criar(userId: Long, form: PedidoFormNovo) : Pedido {
        val cliente = clienteService.buscar(userId)
        val pedido = Pedido(cliente, form)
        return repository.save(pedido)
    }

    @Transactional
    fun atualizar(userId: Long, form: PedidoFormAtualiza) = buscar(userId).atualiza(form)

    fun buscar(userId: Long) = repository
        .findByIdOrNull(userId)
        ?: throw PedidoNaoEncontradoException()

    @Transactional
    fun deletar(userId: Long) = repository.deleteById(userId)


}
