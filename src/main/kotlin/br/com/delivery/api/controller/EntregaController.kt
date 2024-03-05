package br.com.delivery.api.controller

import br.com.delivery.api.domain.entrega.EntregaFormAtualiza
import br.com.delivery.api.domain.entrega.EntregaFormNovo
import br.com.delivery.api.domain.entrega.EntregaViewSimples
import br.com.delivery.api.infra.security.TokenUtils
import br.com.delivery.api.service.EntregaService
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder


@RestController
@RequestMapping("/entrega")
class EntregaController(private val service: EntregaService, private val tokenUtils: TokenUtils) {


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun criar(
        @Valid @RequestBody form: EntregaFormNovo,
        req: HttpServletRequest,
        uriBuilder: UriComponentsBuilder,
    ): ResponseEntity<EntregaViewSimples> {
        val userId = tokenUtils.getUserId(req)
        val entrega = service.criar(userId, form)
        val uri = uriBuilder
            .path("/entrega/{id}")
            .buildAndExpand(entrega.id)
            .toUri()

        return ResponseEntity.created(uri).body(EntregaViewSimples(entrega))
    }

    @PutMapping("/{entregaId}")
    @ResponseStatus(HttpStatus.OK)
    fun editar(
        @Valid @RequestBody form: EntregaFormAtualiza,
        @PathVariable entregaId: Long,
        req: HttpServletRequest,
    ): ResponseEntity<EntregaViewSimples> {
        val userId = tokenUtils.getUserId(req)
        val entrega = service.atualizar(userId, entregaId, form)
        return ResponseEntity.ok(EntregaViewSimples(entrega))
    }

    @GetMapping("/{entregaId}")
    @ResponseStatus(HttpStatus.FOUND)
    fun buscar(@PathVariable entregaId: Long, req: HttpServletRequest): ResponseEntity<EntregaViewSimples> {
        val userId = tokenUtils.getUserId(req)
        val entrega = service.buscar(userId, entregaId)
        return ResponseEntity.ok(EntregaViewSimples(entrega))
    }

    @DeleteMapping("/{entregaId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deletar(@PathVariable entregaId: Long, req: HttpServletRequest): ResponseEntity<Any> {
        val userId = tokenUtils.getUserId(req)
        service.deletar(userId, entregaId)
        return ResponseEntity.noContent().build()
    }
}