package br.com.delivery.api.service

import br.com.delivery.api.domain.cliente.Cliente
import br.com.delivery.api.domain.cliente.ClienteFormBuilder
import br.com.delivery.api.domain.cliente.ClienteRepository
import br.com.delivery.api.domain.entrega.EntregaRepository
import br.com.delivery.api.domain.pedido.PedidoFormBuilder
import br.com.delivery.api.domain.pedido.PedidoRepository
import br.com.delivery.api.infra.exception.PedidoNaoEncontradoException
import jakarta.transaction.Transactional
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension

@ActiveProfiles("test")
@Transactional
@ExtendWith(SpringExtension::class)
@SpringBootTest
class PedidoServiceTestIntegration(
    @Autowired private val clienteService: ClienteService,
    @Autowired private val pedidoService: PedidoService,
    @Autowired private val entregaService: EntregaService,
    @Autowired private val clienteRepository: ClienteRepository,
    @Autowired private val pedidoRepository: PedidoRepository,
    @Autowired private val entregaRepository: EntregaRepository,

    ) {

    private lateinit var cliente: Cliente
    private final val pedidoFormNovo = PedidoFormBuilder().formNovo()
    private final val pedidoFormAtualiza = PedidoFormBuilder().formAtualizado()

    @BeforeEach
    fun setup() {
        cliente = clienteService.criar(ClienteFormBuilder().formNovo())
    }

    @Test
    @DisplayName("Dado um PedidoFormNovo valido, Deve criar um novo Pedido")
    fun criar_1() {
        assertEquals(0, pedidoRepository.count())

        val pedido = pedidoService.criar(cliente.id!!, pedidoFormNovo)
        assertEquals(1, pedidoRepository.count())

        val pedidoBuscado = pedidoRepository.findAll().first()

        assertEquals(1L, pedidoRepository.count())
        assertEquals(pedido, pedidoBuscado)
        assertEquals(pedidoFormNovo.descricao, pedido.descricao)
        assertEquals(cliente, pedido.cliente)
        assertNull(pedido.entrega)
    }

    @Test
    @DisplayName("Dado um pedidoId e clienteId e um ClienteFormAtualiza válidos, Deve atualizar o pedido")
    fun atualizar_1() {
        assertEquals(0, pedidoRepository.count())

        val pedido = pedidoService.criar(cliente.id!!, pedidoFormNovo)
        assertEquals(1, pedidoRepository.count())

        val pedidoAtualizado = pedidoService.atualizar(cliente.id!!, pedido.id!!, pedidoFormAtualiza)
        val pedidoBuscado = pedidoRepository.findAll().first()

        assertEquals(1L, clienteRepository.count())
        assertEquals(pedido, pedidoAtualizado)
        assertEquals(pedidoAtualizado, pedidoBuscado)
        assertEquals(pedidoFormAtualiza.descricao, pedido.descricao)
        assertEquals(cliente, pedido.cliente)
    }

    @Test
    @DisplayName(
        """
        Dado um pedidoId inválido com clienteId e ClienteFormAtualiza válidos,
        Quando tentar atualizar,
        Deve lançar exception referente ao pedido"""
    )
    fun atualizar_2() {
        assertEquals(0, pedidoRepository.count())

        val pedido = pedidoService.criar(cliente.id!!, pedidoFormNovo)
        assertEquals(1, pedidoRepository.count())

        assertThrows<PedidoNaoEncontradoException> {
            pedidoService.atualizar(
                cliente.id!!,
                -1L,
                pedidoFormAtualiza
            )
        }

        assertEquals(1L, pedidoRepository.count())
        assertEquals(pedidoFormNovo.descricao, pedido.descricao)
        assertEquals(cliente, pedido.cliente)
        assertNull(pedido.entrega)

    }
//
//    @Test
//    @DisplayName("Dado um clientId e um ClienteFormAtualiza com campos em nulo, Deve manter os dados originais")
//    fun atualizar_2() {
//        assertEquals(0, clienteRepository.count())
//
//        val cliente = clienteService.criar(clienteFormNovo)
//        val clienteAtualizado = clienteService.atualizar(cliente.id!!, ClienteFormAtualiza(null, null, null))
//
//        val clienteBuscado = clienteRepository.findAll().first()
//
//        assertTrue(clienteRepository.count() == 1L)
//        assertEquals(cliente, clienteAtualizado)
//        assertEquals(clienteAtualizado, clienteBuscado)
//        assertEquals(clienteFormNovo.nome, clienteBuscado.nome)
//        assertEquals(clienteFormNovo.login, clienteBuscado.login)
//        assertEquals(clienteFormNovo.senha, clienteBuscado.senha)
//    }
//
//    @Test
//    @DisplayName("Dado um clientId válido, Deve retornar o cliente")
//    fun buscar_1() {
//        assertEquals(0, clienteRepository.count())
//
//        val cliente = clienteService.criar(clienteFormNovo)
//        val clienteBuscado = clienteService.buscar(cliente.id!!)
//
//        assertEquals(cliente, clienteBuscado)
//    }
//
//    @Test
//    @DisplayName("Dado um clientId inválido, Deve lançar exception")
//    fun buscar_2() {
//        assertEquals(0, clienteRepository.count())
//
//        clienteService.criar(clienteFormNovo)
//
//        org.junit.jupiter.api.assertThrows<ClienteNaoEncontradoException> { clienteService.buscar(-1L) }
//    }
//
//    @Test
//    @DisplayName("Dado um clientId válido sem entrega em andamento, Quando tentar deletar, Deve deletar o cliente")
//    fun deletar_1() {
//        assertEquals(0, clienteRepository.count())
//
//        val cliente = clienteService.criar(clienteFormNovo)
//        assertEquals(1, clienteRepository.count())
//
//        clienteService.deletar(cliente.id!!)
//        assertEquals(0, clienteRepository.count())
//    }
//
//    @Test
//    @DisplayName(
//        """
//        Dado um clientId válido com pedido e com entrega nao iniciada,
//        Quando tentar deletar,
//        Deve deletar cliente e dados de entrega e pedido relacionados"""
//    )
//    fun deletar_2() {
//        assertEquals(0, clienteRepository.count())
//        assertEquals(0, pedidoRepository.count())
//        assertEquals(0, entregaRepository.count())
//
//        val cliente = clienteService.criar(clienteFormNovo)
//        val pedido = pedidoService.criar(cliente.id!!, pedidoFormNovo)
//        entregaService.criar(cliente.id!!, EntregaFormBuilder().formNovo(pedido.id!!))
//
//        assertEquals(1, clienteRepository.count())
//        assertEquals(1, pedidoRepository.count())
//        assertEquals(1, entregaRepository.count())
//
//        clienteService.deletar(cliente.id!!)
//        assertEquals(0, clienteRepository.count())
//        assertEquals(0, pedidoRepository.count())
//        assertEquals(0, entregaRepository.count())
//    }
//
//    @Test
//    @DisplayName(
//        """
//        Dado um clientId válido com pedido e com entrega iniciada,
//        Quando tentar deletar,
//        Deve lançar exception informando da entrega"""
//    )
//    fun deletar_3() {
//        assertEquals(0, clienteRepository.count())
//        assertEquals(0, pedidoRepository.count())
//        assertEquals(0, entregaRepository.count())
//
//        val cliente = clienteService.criar(clienteFormNovo)
//        val pedido = pedidoService.criar(cliente.id!!, pedidoFormNovo)
//        val entrega = entregaService.criar(cliente.id!!, EntregaFormBuilder().formNovo(pedido.id!!))
//        entregaService.atualizar(cliente.id!!, entrega.id!!, EntregaFormBuilder().formAtualiza_EntregaIniciada())
//
//        assertEquals(1, clienteRepository.count())
//        assertEquals(1, pedidoRepository.count())
//        assertEquals(1, entregaRepository.count())
//
//        org.junit.jupiter.api.assertThrows<EntregaEmAndamentoException> { clienteService.deletar(cliente.id!!) }
//
//        assertEquals(1, clienteRepository.count())
//        assertEquals(1, pedidoRepository.count())
//        assertEquals(1, entregaRepository.count())
//    }
}