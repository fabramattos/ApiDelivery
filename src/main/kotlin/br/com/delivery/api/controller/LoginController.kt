package br.com.delivery.api.controller

import br.com.delivery.api.domain.cliente.Cliente
import br.com.delivery.api.domain.cliente.ClienteFormLogin
import br.com.delivery.api.infra.security.TokenUtils
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder


@RestController
@RequestMapping("/cliente")
class LoginController (private val tokenUtils: TokenUtils, private val manager: AuthenticationManager){

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    fun criar(@Valid @RequestBody form: ClienteFormLogin, uriBuilder: UriComponentsBuilder) : ResponseEntity<String> {
        val login = UsernamePasswordAuthenticationToken(form.login, form.senha)
        val authentication =  manager.authenticate(login)
        val jwt = tokenUtils.geraToken(authentication.principal as Cliente)

        return ResponseEntity.ok(jwt)
    }


}