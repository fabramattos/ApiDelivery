package br.com.delivery.api.service

import br.com.delivery.api.domain.cliente.ClienteFormAtualiza
import br.com.delivery.api.domain.cliente.ClienteFormNovo
import br.com.delivery.api.domain.cliente.ClienteRepository
import br.com.delivery.api.infra.exception.ClienteNaoEncontradoException
import jakarta.transaction.Transactional
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import kotlin.test.assertEquals
import kotlin.test.assertTrue


@ActiveProfiles("test")
@ExtendWith(SpringExtension::class)
@Transactional
@SpringBootTest
class ClienteServiceTestIntegration(
    @Autowired private val service: ClienteService,
    @Autowired private val repository: ClienteRepository,
) {

    private val clienteFormNovo = ClienteFormNovo(
        "melon husk",
        "user@email.com",
        "123456"
    )

    private val clienteFormAtualiza = ClienteFormAtualiza(
        "nome atualizado",
        "atualizado@email.com",
        "11223344"
    )

    @Test
    @DisplayName("Dado um ClienteFormNovo valido, Deve criar um novo cliente")
    fun criar_1() {
        assertEquals(0, repository.count())

        val cliente = service.criar(clienteFormNovo)
        assertEquals(1, repository.count())

        val clienteBuscado = repository.findAll().first()

        assertTrue(repository.count() == 1L)
        assertEquals(cliente, clienteBuscado)
        assertEquals(clienteFormNovo.nome, clienteBuscado.nome)
        assertEquals(clienteFormNovo.login, clienteBuscado.login)
        assertEquals(clienteFormNovo.senha, clienteBuscado.senha)
    }

    @Test
    @DisplayName("Dado um clientId e um ClienteFormAtualiza válidos, Deve atualizar o cliente")
    fun atualizar_1() {
        assertEquals(0, repository.count())

        val cliente = service.criar(clienteFormNovo)
        val clienteAtualizado = service.atualizar(cliente.id!!, clienteFormAtualiza)

        val clienteBuscado = repository.findAll().first()

        assertTrue(repository.count() == 1L)
        assertEquals(cliente, clienteAtualizado)
        assertEquals(clienteAtualizado, clienteBuscado)
        assertEquals(clienteFormAtualiza.nome, clienteBuscado.nome)
        assertEquals(clienteFormAtualiza.login, clienteBuscado.login)
        assertEquals(clienteFormAtualiza.senha, clienteBuscado.senha)
    }

    @Test
    @DisplayName("Dado um clientId e um ClienteFormAtualiza com campos em nulo, Deve manter os dados originais")
    fun atualizar_2() {
        assertEquals(0, repository.count())

        val cliente = service.criar(clienteFormNovo)
        val clienteAtualizado = service.atualizar(cliente.id!!, ClienteFormAtualiza(null, null, null))

        val clienteBuscado = repository.findAll().first()

        assertTrue(repository.count() == 1L)
        assertEquals(cliente, clienteAtualizado)
        assertEquals(clienteAtualizado, clienteBuscado)
        assertEquals(clienteFormNovo.nome, clienteBuscado.nome)
        assertEquals(clienteFormNovo.login, clienteBuscado.login)
        assertEquals(clienteFormNovo.senha, clienteBuscado.senha)
    }

    @Test
    @DisplayName("Dado um clientId válido, Deve retornar o cliente")
    fun buscar_1() {
        assertEquals(0, repository.count())

        val cliente = service.criar(clienteFormNovo)
        val clienteBuscado = service.buscar(cliente.id!!)

        assertEquals(cliente, clienteBuscado)
    }

    @Test
    @DisplayName("Dado um clientId inválido, Deve lançar exception")
    fun buscar_2() {
        assertEquals(0, repository.count())

        service.criar(clienteFormNovo)

        assertThrows<ClienteNaoEncontradoException> { service.buscar(-1L) }
    }

    @Test
    @DisplayName("Dado um clientId válido, Deve deletar o cliente")
    fun deletar() {
        assertEquals(0, repository.count())

        val cliente = service.criar(clienteFormNovo)
        assertEquals(1, repository.count())

        service.deletar(cliente.id!!)
        assertEquals(0, repository.count())
    }
}