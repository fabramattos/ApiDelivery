package br.com.delivery.api.infra.exception

import com.auth0.jwt.exceptions.TokenExpiredException
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.InternalAuthenticationServiceException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice(basePackages = ["br.com.delivery.api"])
class TratadorDeErros {


    @ExceptionHandler(Exception::class)
    fun tratarErrosGerais(e: Exception): ResponseEntity<ExceptionView> {
        val view: ExceptionView = when (e) {
            is TokenInvalidoException -> ExceptionView(e.localizedMessage)
            is TokenExpiredException -> ExceptionView("Token expirado!")
            is InternalAuthenticationServiceException -> ExceptionView("Usuário inexistente ou senha inválida")
            is BadCredentialsException -> ExceptionView(e.localizedMessage)
            else -> ExceptionView(e.localizedMessage)
        }

        return ResponseEntity.badRequest().body(view)
    }


    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun tratarValidacaoDeArgumentos(e: MethodArgumentNotValidException): ResponseEntity<ExceptionView> {
        return ResponseEntity
            .badRequest()
            .body(ExceptionView(e.localizedMessage))
    }
}