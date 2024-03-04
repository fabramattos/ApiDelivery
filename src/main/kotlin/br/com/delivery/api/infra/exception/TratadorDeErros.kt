package br.com.delivery.api.infra.exception

import br.com.delivery.api.br.com.delivery.api.infra.exception.ExceptionView
import com.auth0.jwt.exceptions.JWTDecodeException
import com.auth0.jwt.exceptions.TokenExpiredException
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.InternalAuthenticationServiceException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class TratadorDeErros {

    @ExceptionHandler(JWTDecodeException::class)
    fun tratarTokenNulo(e: JWTDecodeException): ResponseEntity<ExceptionView> {
        return ResponseEntity
            .badRequest()
            .body(ExceptionView("Token não informado"))
    }

    @ExceptionHandler(TokenExpiredException::class)
    fun tratarTokenExpirado(): ResponseEntity<ExceptionView> {
        return ResponseEntity
            .badRequest()
            .body(ExceptionView("Token expirado!"))
    }

    @ExceptionHandler(BadCredentialsException::class)
    fun tratarSenhaInvalida(e: BadCredentialsException): ResponseEntity<ExceptionView> {
        return ResponseEntity
            .badRequest()
            .body(ExceptionView(e.localizedMessage))
    }

    @ExceptionHandler(InternalAuthenticationServiceException::class)
    fun tratarUsuarioInvalido(): ResponseEntity<ExceptionView> {
        return ResponseEntity
            .badRequest()
            .body(ExceptionView("Usuário inexistente ou senha inválida"))
    }

    @ExceptionHandler(ClienteNaoEncontradoException::class)
    fun tratarClienteNaoEncontrado(e: ClienteNaoEncontradoException): ResponseEntity<ExceptionView> {
        return ResponseEntity
            .badRequest()
            .body(ExceptionView(e.localizedMessage))
    }

    @ExceptionHandler(EntregaNaoEncontradaException::class)
    fun tratarEntregaNaoEncontrada(e: EntregaNaoEncontradaException): ResponseEntity<ExceptionView> {
        return ResponseEntity
            .badRequest()
            .body(ExceptionView(e.localizedMessage))
    }

    @ExceptionHandler(PedidoNaoEncontradoException::class)
    fun tratarPedidoNaoEncontradoa(e: PedidoNaoEncontradoException): ResponseEntity<ExceptionView> {
        return ResponseEntity
            .badRequest()
            .body(ExceptionView(e.localizedMessage))
    }

    @ExceptionHandler(EntregaEmAndamentoException::class)
    fun tratarEntregaEmAndamento(e: EntregaEmAndamentoException): ResponseEntity<ExceptionView> {
        return ResponseEntity
            .badRequest()
            .body(ExceptionView(e.localizedMessage))
    }

    @ExceptionHandler(EntregaExistenteException::class)
    fun tratarEntregaExistente(e: EntregaExistenteException): ResponseEntity<ExceptionView> {
        return ResponseEntity
            .badRequest()
            .body(ExceptionView(e.localizedMessage))
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun tratarValidacaoDeArgumentos(e: MethodArgumentNotValidException): ResponseEntity<ExceptionView> {
        return ResponseEntity
            .badRequest()
            .body(ExceptionView(e.localizedMessage))
    }
}