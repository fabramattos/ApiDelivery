package br.com.delivery.api.service

import br.com.delivery.api.domain.cliente.Cliente
import br.com.delivery.api.domain.cliente.ClienteFormBuilder
import br.com.delivery.api.domain.cliente.ClienteRepository
import br.com.delivery.api.domain.entrega.EntregaFormAtualiza
import br.com.delivery.api.domain.entrega.EntregaFormNovo
import br.com.delivery.api.domain.entrega.EntregaRepository
import br.com.delivery.api.domain.entrega.EntregaStatus
import br.com.delivery.api.domain.pedido.Pedido
import br.com.delivery.api.domain.pedido.PedidoFormBuilder
import br.com.delivery.api.domain.pedido.PedidoRepository
import br.com.delivery.api.infra.database.DatabaseContainerConfig
import br.com.delivery.api.infra.exception.EntregaEmAndamentoException
import br.com.delivery.api.infra.exception.EntregaExistenteException
import br.com.delivery.api.infra.exception.EntregaNaoEncontradaException
import br.com.delivery.api.infra.exception.PedidoNaoEncontradoException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import kotlin.test.assertTrue

class EntregaServiceTestIntegration(
    @Autowired private val clienteService: ClienteService,
    @Autowired private val pedidoService: PedidoService,
    @Autowired private val entregaService: EntregaService,
    @Autowired private val clienteRepository: ClienteRepository,
    @Autowired private val pedidoRepository: PedidoRepository,
    @Autowired private val entregaRepository: EntregaRepository,

    ):DatabaseContainerConfig() {

    private lateinit var cliente: Cliente
    private lateinit var pedido: Pedido
    private lateinit var entregaFormNovo: EntregaFormNovo

    @BeforeEach
    fun setup() {
        cliente = clienteService.criar(ClienteFormBuilder().formNovo())
        pedido = pedidoService.criar(cliente.id!!, PedidoFormBuilder().formNovo())
        entregaFormNovo = EntregaFormNovo(pedido.id!!, "endereço da entrega")
    }

    @Test
    @DisplayName(
        """
        Dado um clienteId e EntregaFormNovo válidos,
        Deve criar uma nova Entrega e associa-la ao pedido/cliente."""
    )
    fun criar_1() {
        assertEquals(0, entregaRepository.count())

        val entrega = criaEntrega(pedido.id!!)
        val entregaBuscada = entregaRepository.findAll().first()

        assertEquals(1L, entregaRepository.count())
        assertEquals(1L, pedidoRepository.count())
        assertEquals(1L, clienteRepository.count())
        assertEquals(entrega, entregaBuscada)
        assertEquals(entregaFormNovo.endereco, entrega.endereco)
        assertEquals(EntregaStatus.NAO_INICIADA, entrega.status)
        assertEquals(pedido, entrega.pedido)
        assertEquals(cliente, entrega.pedido.cliente)
    }

    @Test
    @DisplayName(
        """
        Dado um pedidoId inválido,
        Quando tentar criar um uma nova Entrega,
        Deve lançar exception referente ao pedido."""
    )
    fun criar_2() {
        assertEquals(0, entregaRepository.count())

        assertThrows<PedidoNaoEncontradoException> { criaEntrega(-1L) }

        assertEquals(0L, entregaRepository.count())
    }

    @Test
    @DisplayName(
        """Dado um clienteId inválido,
        Quando tentar criar um uma nova Entrega,
        Deve lançar exception referente ao pedido."""
    )
    fun criar_3() {
        assertEquals(0, entregaRepository.count())

        assertThrows<PedidoNaoEncontradoException> { entregaService.criar(-1L, entregaFormNovo) }

        assertEquals(0L, entregaRepository.count())
    }

    @Test
    @DisplayName(
        """
        Dado um pedidoId ja com entrega,
        Quando tentar criar um uma nova Entrega,
        Deve lançar exception referente ao pedido."""
    )
    fun criar_4() {
        assertEquals(0, entregaRepository.count())
        criaEntrega(pedido.id!!)
        assertEquals(1L, entregaRepository.count())

        assertThrows<EntregaExistenteException> {criaEntrega(pedido.id!!) }

        assertEquals(1L, entregaRepository.count())
    }


    @Test
    @DisplayName(
        """
        Dado um EntregaFormAtualiza válido para atualizar endereço,
        Quando chamar atualizar,
        Deve atualizar a Entrega"""
    )
    fun atualizar_1() {
        assertEquals(0, entregaRepository.count())

        val entrega = criaEntrega(pedido.id!!)
        val entregaAtualizada = atualizaEntrega(cliente.id!!, entrega.id!!, "Novo Endereço", null)
        val entregaBuscada = entregaRepository.findAll().first()

        assertEquals(entrega, entregaAtualizada)
        assertEquals(entrega, entregaBuscada)
        assertEquals("Novo Endereço", entrega.endereco)
        assertEquals(EntregaStatus.NAO_INICIADA, entrega.status)
    }

    @Test
    @DisplayName(
        """
        Dado um EntregaFormAtualiza válido para atualizar Status da entrega,
        Quando chamar atualizar,
        Deve atualizar a Entrega"""
    )
    fun atualizar_2() {
        assertEquals(0, entregaRepository.count())

        val entrega = criaEntrega(pedido.id!!)
        val entregaAtualizada = atualizaEntrega(cliente.id!!, entrega.id!!, null, EntregaStatus.PREPARANDO)
        val entregaBuscada = entregaRepository.findAll().first()

        assertEquals(entrega, entregaAtualizada)
        assertEquals(entrega, entregaBuscada)
        assertEquals(entregaFormNovo.endereco, entrega.endereco)
        assertEquals(EntregaStatus.PREPARANDO, entrega.status)
    }

    @Test
    @DisplayName(
        """
        Dado um clienteId sem relação com uma entrega existente,
        Quando chamar atualizar,
        Deve lançar exception sobre entrega"""
    )
    fun atualizar_3() {
        assertEquals(0, entregaRepository.count())

        val entrega = criaEntrega(pedido.id!!)

        assertThrows<EntregaNaoEncontradaException> {
            atualizaEntrega(
                -1L,
                entrega.id!!,
                "Endereço Atualizado",
                EntregaStatus.PREPARANDO
            )
        }

        assertEquals(entregaFormNovo.endereco, entrega.endereco)
        assertEquals(EntregaStatus.NAO_INICIADA, entrega.status)
    }

    @Test
    @DisplayName(
        """
        Dado uma entregaId inválida,
        Quando chamar atualizar,
        Deve lançar exception sobre entrega"""
    )
    fun atualizar_4() {
        assertEquals(0, entregaRepository.count())

        val entrega = criaEntrega(pedido.id!!)

        assertThrows<EntregaNaoEncontradaException> {
            atualizaEntrega(
                cliente.id!!,
                -1L,
                "Endereço Atualizado",
                EntregaStatus.PREPARANDO
            )
        }

        assertEquals(entregaFormNovo.endereco, entrega.endereco)
        assertEquals(EntregaStatus.NAO_INICIADA, entrega.status)
    }


    @Test
    @DisplayName(
        """
        Dado um clienteId e entregaId válidos,
        Quando chamar busca,
        Deve retornar a entrega."""
    )
    fun buscar_1() {
        assertEquals(0, entregaRepository.count())

        val entrega = criaEntrega(pedido.id!!)
        val entregaBuscada = buscaEntrega(cliente.id!!, entrega.id!!)

        assertEquals(entrega, entregaBuscada)
    }

    @Test
    @DisplayName(
        """
        Dado um clienteId sem relação com a entrega,
        Quando chamar busca,
        Deve retornar exception referente a entrega."""
    )
    fun buscar_2() {
        assertEquals(0, entregaRepository.count())

        val entrega = criaEntrega(pedido.id!!)
        assertThrows<EntregaNaoEncontradaException> { buscaEntrega(-1L, entrega.id!!) }

    }

    @Test
    @DisplayName(
        """
        Dado uma entregaId inválida,
        Quando chamar busca,
        Deve retornar exception referente a entrega."""
    )
    fun buscar_3() {
        assertEquals(0, entregaRepository.count())

        criaEntrega(pedido.id!!)
        assertThrows<EntregaNaoEncontradaException> { buscaEntrega(cliente.id!!, -1L) }

    }

    @Test
    @DisplayName(
        """
        Dado um entregaId válido com entrega não iniciada,
        Quando solicitar deletar,
        Deve deletar entrega, mantendo pedido e cliente"""
    )
    fun deletar_1() {
        assertEquals(0, entregaRepository.count())

        val entrega = criaEntrega(pedido.id!!)

        deletarEntrega(cliente.id!!, entrega.id!!)

        assertEquals(0L, entregaRepository.count())
        assertEquals(1L, pedidoRepository.count())
        assertEquals(1L, clienteRepository.count())

        assertTrue(cliente.pedidos.contains(pedido))
        assertNull(pedido.entrega)
    }

    @Test
    @DisplayName(
        """
        Dado um entregaId inválido,
        Quando solicitar deletar,
        Deve lançar exception relativo a entrega."""
    )
    fun deletar_2() {
        assertEquals(0L, entregaRepository.count())

        val entrega = criaEntrega(pedido.id!!)

        assertThrows<EntregaNaoEncontradaException> { deletarEntrega(cliente.id!!, -1L) }

        assertEquals(1L, entregaRepository.count())
        assertEquals(1L, pedidoRepository.count())
        assertEquals(1L, clienteRepository.count())

        assertTrue(cliente.pedidos.contains(pedido))
        assertEquals(entrega, pedido.entrega)
    }

    @Test
    @DisplayName(
        """
        Dado um clienteId inválido,
        Quando solicitar deletar,
        Deve lançar exception relativo a entrega."""
    )
    fun deletar_3() {
        assertEquals(0L, entregaRepository.count())

        val entrega = criaEntrega(pedido.id!!)

        assertThrows<EntregaNaoEncontradaException> { deletarEntrega(-1L, entrega.id!!) }

        assertEquals(1L, entregaRepository.count())
        assertEquals(1L, pedidoRepository.count())
        assertEquals(1L, clienteRepository.count())

        assertTrue(cliente.pedidos.contains(pedido))
        assertEquals(entrega, pedido.entrega)
    }

    @Test
    @DisplayName(
        """
        Dado um entregaId válido com entrega inicada,
        Quando solicitar deletar,
        Deve lançar exception relativo a entrega."""
    )
    fun deletar_4() {
        assertEquals(0L, entregaRepository.count())

        val entrega = criaEntrega(pedido.id!!)
            .apply { status = EntregaStatus.PREPARANDO }

        assertThrows<EntregaEmAndamentoException> { deletarEntrega(cliente.id!!, entrega.id!!) }

        entrega.apply { status = EntregaStatus.A_CAMINHO }
        assertThrows<EntregaEmAndamentoException> { deletarEntrega(cliente.id!!, entrega.id!!) }

        entrega.apply { status = EntregaStatus.ENTREGUE }
        assertThrows<EntregaEmAndamentoException> { deletarEntrega(cliente.id!!, entrega.id!!) }

        entrega.apply { status = EntregaStatus.PROBLEMA }
        assertThrows<EntregaEmAndamentoException> { deletarEntrega(cliente.id!!, entrega.id!!) }

        assertEquals(1L, entregaRepository.count())
        assertEquals(1L, pedidoRepository.count())
        assertEquals(1L, clienteRepository.count())
        assertTrue(cliente.pedidos.contains(pedido))
        assertEquals(entrega, pedido.entrega)
    }

    private fun criaEntrega(pedidoId: Long) =
        entregaService.criar(cliente.id!!, EntregaFormNovo(pedidoId, "endereço da entrega"))

    private fun atualizaEntrega(
        clienteId: Long,
        entregaId: Long,
        endereco: String?,
        entregaStatus: EntregaStatus?,
    ) = entregaService.atualizar(clienteId, entregaId, EntregaFormAtualiza(endereco, entregaStatus))

    private fun buscaEntrega(clienteId: Long, entregaId: Long) =
        entregaService.buscar(clienteId, entregaId)

    private fun deletarEntrega(clienteId: Long, entregaId: Long) =
        entregaService.deletar(clienteId, entregaId)
}