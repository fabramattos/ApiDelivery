package br.com.delivery.api.service

import br.com.delivery.api.domain.cliente.Cliente
import br.com.delivery.api.domain.cliente.ClienteFormAtualiza
import br.com.delivery.api.domain.cliente.ClienteFormNovo
import br.com.delivery.api.domain.cliente.ClienteRepository
import br.com.delivery.api.domain.entrega.EntregaStatus
import br.com.delivery.api.infra.exception.ClienteNaoEncontradoException
import br.com.delivery.api.infra.exception.EntregaEmAndamentoException
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class ClienteService(
    private val repository: ClienteRepository,
    private val encoder: PasswordEncoder,
) : UserDetailsService {

    @Transactional
    fun criar(form: ClienteFormNovo) = repository
        .save(
            Cliente(
                form.apply { senha = encoder.encode(senha) }
            )
        )


    @Transactional
    fun atualizar(userId: Long, form: ClienteFormAtualiza) = buscar(userId)
        .atualiza(form
            .apply {
                senha?.apply { senha = encoder.encode(senha) }
            }
        )


    fun buscar(userId: Long) = repository
        .findByIdOrNull(userId)
        ?: throw ClienteNaoEncontradoException()

    @Transactional
    fun deletar(userId: Long) {
        val cliente = buscar(userId)
            .takeUnless { cliente ->
                cliente.pedidos.any { pedido ->
                    pedido.entrega?.status != EntregaStatus.NAO_INICIADA
                }
            } ?: throw EntregaEmAndamentoException()

        repository.delete(cliente)
    }

    override fun loadUserByUsername(username: String?): UserDetails = repository
        .findByLogin(username)
        .orElseThrow { ClienteNaoEncontradoException() }

}
