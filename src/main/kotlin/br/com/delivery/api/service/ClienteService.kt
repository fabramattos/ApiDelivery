package br.com.delivery.api.service

import br.com.delivery.api.domain.cliente.ClienteFormAtualiza
import br.com.delivery.api.domain.cliente.Cliente
import br.com.delivery.api.domain.cliente.ClienteFormNovo
import br.com.delivery.api.domain.cliente.ClienteRepository
import jakarta.transaction.Transactional
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class ClienteService(
    private val repository: ClienteRepository,
    private val encoder: PasswordEncoder,
) {

    @Transactional
    fun criar(form: ClienteFormNovo): Cliente {
        val formCodificado = form.copy(senha = encoder.encode(form.senha))
        return repository.save(Cliente(formCodificado))
    }

    @Transactional
    fun atualizar(userId: Long, form: ClienteFormAtualiza): Cliente {
        val cliente = repository.findById(userId).orElseThrow()
        return cliente.atualiza(form)
    }


}