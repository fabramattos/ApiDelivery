package br.com.delivery.api.domain.pedido

import br.com.delivery.api.domain.cliente.Cliente
import br.com.delivery.api.domain.entrega.Entrega
import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*

@Entity
class Pedido(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    val id: Long? = null,
    @Column(nullable = false)
    var descricao: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id")
    var cliente: Cliente,

    @OneToOne(mappedBy = "pedido", cascade = [CascadeType.ALL], orphanRemoval = true)
    @JsonIgnore
    @JoinColumn(name = "entrega_id")
    var entrega: Entrega? = null,
) {
    fun atualiza(form: PedidoFormAtualiza): Pedido {
        descricao = form.descricao
        return this
    }

    constructor(cliente: Cliente, form: PedidoFormNovo) :
            this(descricao = form.descricao, cliente = cliente)
}