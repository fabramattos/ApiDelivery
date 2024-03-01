package br.com.delivery.api.domain.entrega

import br.com.delivery.api.domain.pedido.Pedido
import jakarta.persistence.*

@Entity
class Entrega(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    val id: Long? = null,
    @Column(nullable = false)
    var endereco: String,

    @OneToOne(mappedBy = "entrega")
    @JoinColumn(name = "pedido_id")
    @Column(nullable = false)
    var pedido: Pedido


) {
    fun atualiza(form: EntregaFormAtualiza): Entrega {
        endereco = form.endereco
        return this
    }


    constructor(pedido: Pedido, form: EntregaFormNovo) :
            this(pedido = pedido, endereco = form.endereco)
}