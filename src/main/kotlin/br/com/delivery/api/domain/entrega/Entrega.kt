package br.com.delivery.api.domain.entrega

import br.com.delivery.api.domain.pedido.Pedido
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne

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
    constructor(pedido: Pedido, form: EntregaFormNovo) :
            this(pedido = pedido, endereco = form.endereco)
}