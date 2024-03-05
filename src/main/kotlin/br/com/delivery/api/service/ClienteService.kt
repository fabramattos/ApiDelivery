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
    fun criar(form: ClienteFormNovo): Cliente {
        val formCodificado = form.copy(senha = encoder.encode(form.senha))
        return repository.save(Cliente(formCodificado))
    }

    @Transactional
    fun atualizar(userId: Long, form: ClienteFormAtualiza) = buscar(userId).atualiza(form)

    fun buscar(userId: Long) = repository
        .findByIdOrNull(userId)
        ?: throw ClienteNaoEncontradoException()

    @Transactional
    fun deletar(userId: Long) {
        buscar(userId)
            .pedidos
            .forEach { pedido ->
                pedido.entrega?.let {
                    if (it.status != EntregaStatus.NAO_INICIADA)
                        throw EntregaEmAndamentoException()
                }
            }

        repository.deleteById(userId)
    }

    override fun loadUserByUsername(username: String?): UserDetails = repository
        .findByLogin(username)
        .orElseThrow { ClienteNaoEncontradoException() }

}
