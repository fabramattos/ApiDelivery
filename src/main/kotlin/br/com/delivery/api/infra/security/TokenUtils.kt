package br.com.delivery.api.infra.security

import br.com.delivery.api.br.com.delivery.api.infra.exception.TokenGeradoException
import br.com.delivery.api.br.com.delivery.api.infra.exception.TokenInvalidoException
import br.com.delivery.api.br.com.delivery.api.infra.exception.TokenNaoInformadoException
import br.com.delivery.api.domain.cliente.Cliente
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.time.Duration
import java.time.Instant

@Component
class TokenUtils {

    @Value("\${jwt.secret}")
    private val JWT_SECRET: String? = null
    private val algoritmo = Algorithm.HMAC512(JWT_SECRET)
    private val ISSUER = "acert_api"
    private val DIAS_VALIDADE = 1L

    fun geraToken(cliente: Cliente) = JWT
        .create()
        .withIssuer(ISSUER)
        .withSubject(cliente.login)
        .withClaim("userId", cliente.id)
        .withExpiresAt(diasValidade(DIAS_VALIDADE))
        .sign(algoritmo)
        ?: throw TokenGeradoException()

    fun getSubject(token: String?): String = JWT
        .require(algoritmo)
        .withIssuer(ISSUER)
        .build()
        .verify(token)
        .subject
        ?: throw TokenInvalidoException()

    fun getUserId(req: HttpServletRequest) = JWT
        .require(algoritmo)
        .withIssuer(ISSUER)
        .build()
        .verify(recuperaToken(req))
        .getClaim("userId")
        .asLong()
        ?: throw TokenInvalidoException()

    private fun diasValidade(dias: Long): Instant {
        val duration = Duration.ofDays(dias).toSeconds()
        return Instant.now().plusSeconds(duration)
    }

    fun recuperaToken(req: HttpServletRequest): String? =
        req
            .getHeader("Authorization")
            ?.replace("Bearer ", "")
            ?: throw TokenNaoInformadoException()

}
