package br.com.delivery.api.controller

import br.com.delivery.api.domain.cliente.ClienteFormAtualiza
import br.com.delivery.api.domain.cliente.ClienteFormNovo
import br.com.delivery.api.domain.cliente.ClienteViewSimples
import br.com.delivery.api.infra.security.TokenUtils
import br.com.delivery.api.service.ClienteService
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder


@RestController
@RequestMapping("/cliente")
class ClienteController (private val service: ClienteService, private val tokenUtils: TokenUtils){

    @PostMapping
    fun cria(@Valid @RequestBody form: ClienteFormNovo, uriBuilder: UriComponentsBuilder) : ResponseEntity<ClienteViewSimples>{
        val cliente = service.criar(form)
        val uri = uriBuilder
            .path("/cliente/{id}")
            .buildAndExpand(cliente.id)
            .toUri()

        return ResponseEntity.created(uri).body(ClienteViewSimples(cliente))
    }

    @PutMapping
    fun edita(@Valid @RequestBody form: ClienteFormAtualiza, req: HttpServletRequest) : ResponseEntity<ClienteViewSimples>{
        val userId = tokenUtils.getUserId(req)
        val cliente = service.atualizar(userId, form)
        return ResponseEntity.ok(ClienteViewSimples(cliente))
    }

}