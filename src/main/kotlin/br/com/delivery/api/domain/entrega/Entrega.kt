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

    @OneToOne
    @JoinColumn(name = "pedido_id")
    var pedido: Pedido,

    @Enumerated(EnumType.STRING)
    var status: EntregaStatus = EntregaStatus.NAO_INICIADA

) {
    fun atualiza(form: EntregaFormAtualiza): Entrega {
        endereco = form.endereco
        return this
    }


    constructor(pedido: Pedido, form: EntregaFormNovo) :
            this(pedido = pedido, endereco = form.endereco)
}