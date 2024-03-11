package br.com.delivery.api.service

import br.com.delivery.api.domain.cliente.Cliente
import br.com.delivery.api.domain.cliente.ClienteFormBuilder
import br.com.delivery.api.domain.entrega.EntregaFormNovo
import br.com.delivery.api.domain.entrega.EntregaRepository
import br.com.delivery.api.domain.entrega.EntregaStatus
import br.com.delivery.api.domain.pedido.Pedido
import br.com.delivery.api.domain.pedido.PedidoFormBuilder
import br.com.delivery.api.domain.pedido.PedidoRepository
import br.com.delivery.api.infra.exception.EntregaEmAndamentoException
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
import kotlin.test.assertNotNull

@ActiveProfiles("test")
@Transactional
@ExtendWith(SpringExtension::class)
@SpringBootTest
class PedidoServiceTestIntegration(
    @Autowired private val clienteService: ClienteService,
    @Autowired private val pedidoService: PedidoService,
    @Autowired private val entregaService: EntregaService,
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

        val pedido = criaPedido()
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

        val pedido = criaPedido()
        assertEquals(1, pedidoRepository.count())

        val pedidoAtualizado = atualizaPedido(pedido.id!!)
        val pedidoBuscado = pedidoRepository.findAll().first()

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

        val pedido = criaPedido()
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


    @Test
    @DisplayName("Dado um clienteId e pedidoId válidos, Deve retornar o pedido.")
    fun buscar_1() {
        assertEquals(0, pedidoRepository.count())

        val pedido = criaPedido()
        val pedidoBuscado = pedidoService.buscar(cliente.id!!, pedido.id!!)

        assertEquals(pedido, pedidoBuscado)
    }

    @Test
    @DisplayName("Dado um clienteId válido e um pedidoId inválido, Deve lançar exception de pedido não encontrado.")
    fun buscar_2() {
        assertEquals(0, pedidoRepository.count())

        criaPedido()

        assertThrows<PedidoNaoEncontradoException> { pedidoService.buscar(cliente.id!!, -1L) }
    }

    @Test
    @DisplayName(
        """
        Dado um clientId sem relação com pedidoId,
        Quando solicitado dados do pedido,
        Deve lançar exception de pedido não encontrado."""
    )
    fun buscar_3() {
        assertEquals(0, pedidoRepository.count())

        val pedido = criaPedido()

        assertThrows<PedidoNaoEncontradoException> { pedidoService.buscar(-1L, pedido.id!!) }
    }

    @Test
    @DisplayName(
        """
        Dado um clientId e pedidoId válidos e sem entrega,
        Quando tentar deletar o pedido,
        Deve deletar o pedido"""
    )
    fun deletar_1() {
        assertEquals(0, pedidoRepository.count())

        val pedido = criaPedido()
        assertEquals(1, pedidoRepository.count())
        assertNull(pedido.entrega)
        pedidoService.deletar(cliente.id!!, pedido.id!!)
        assertEquals(0, pedidoRepository.count())
    }

    @Test
    @DisplayName(
        """
        Dado um clientId sem direitos sobre o pedidoId válido e sem entrega,
        Quando tentar deletar o pedido,
        Deve lançar exception de pedido não encontrado"""
    )
    fun deletar_2() {
        assertEquals(0, pedidoRepository.count())

        val pedido = criaPedido()
        assertEquals(1, pedidoRepository.count())
        assertThrows<PedidoNaoEncontradoException> { pedidoService.deletar(-1L, pedido.id!!) }

        assertEquals(1, pedidoRepository.count())
    }

    @Test
    @DisplayName(
        """
        Dado um clientId e pedidoId válido e com entrega mas ainda não iniciada,
        Quando tentar deletar o pedido,
        Deve deletar o pedido E deletar a entrega"""
    )
    fun deletar_3() {
        assertEquals(0, pedidoRepository.count())

        val pedido = criaPedido()
        criaEntrega(pedido)
        assertNotNull(pedido.entrega)
        assertEquals(EntregaStatus.NAO_INICIADA, pedido.entrega?.status)
        assertEquals(1, pedidoRepository.count())
        assertEquals(1, entregaRepository.count())

        pedidoService.deletar(cliente.id!!, pedido.id!!)

        assertEquals(0, pedidoRepository.count())
        assertEquals(0, entregaRepository.count())
    }

    @Test
    @DisplayName(
        """
        Dado um clientId e pedidoId válido e com entrega já iniciada,
        Quando tentar deletar o pedido,
        Deve lançar exception informando da entrega em andamento"""
    )
    fun deletar_4() {
        assertEquals(0, pedidoRepository.count())

        val pedido = criaPedido()
        val entrega = criaEntrega(pedido)
        entrega.status = EntregaStatus.PREPARANDO

        assertEquals(1, pedidoRepository.count())
        assertEquals(1, entregaRepository.count())
        assertNotNull(pedido.entrega)
        assertEquals(EntregaStatus.PREPARANDO, pedido.entrega?.status)

        assertThrows<EntregaEmAndamentoException> { pedidoService.deletar(cliente.id!!, pedido.id!!) }

        assertEquals(1, pedidoRepository.count())
        assertEquals(1, entregaRepository.count())
    }

    private fun criaPedido() = pedidoService.criar(cliente.id!!, pedidoFormNovo)

    private fun atualizaPedido(pedidoId: Long) =
        pedidoService.atualizar(cliente.id!!, pedidoId, pedidoFormAtualiza)

    private fun criaEntrega(pedido: Pedido) =
        entregaService.criar(cliente.id!!, EntregaFormNovo(pedido.id!!, "endereço da entrega"))

}