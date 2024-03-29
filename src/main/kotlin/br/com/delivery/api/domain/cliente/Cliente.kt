package br.com.delivery.api.domain.cliente

import br.com.delivery.api.domain.pedido.Pedido
import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import org.springframework.security.core.userdetails.UserDetails

@Entity
class Cliente(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    val id: Long? = null,
    @Column(nullable = false, length = 100)
    var nome: String,
    @Column(nullable = false, unique = true, length = 100)
    var login: String,
    @Column(nullable = false)
    var senha: String,

    @OneToMany(mappedBy = "cliente", cascade = [CascadeType.ALL], orphanRemoval = true)
    @JsonIgnore
    val pedidos: MutableList<Pedido> = mutableListOf(),
) : UserDetails {

    constructor(form: ClienteFormNovo) :
            this(
                nome = form.nome,
                login = form.login,
                senha = form.senha
            )

    fun atualiza(form: ClienteFormAtualiza): Cliente {
        form.nome?.let { nome = it }
        form.login?.let { login = it }
        form.senha?.let { senha = it }
        return this
    }

    override fun getAuthorities() = null


    override fun getPassword() = senha

    override fun getUsername()  = login

    override fun isAccountNonExpired() = true

    override fun isAccountNonLocked() = true

    override fun isCredentialsNonExpired() = true

    override fun isEnabled() = true

}