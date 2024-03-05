package br.com.delivery.api.controller

import br.com.delivery.api.domain.pedido.PedidoFormAtualiza
import br.com.delivery.api.domain.pedido.PedidoFormNovo
import br.com.delivery.api.domain.pedido.PedidoViewSimples
import br.com.delivery.api.infra.security.TokenUtils
import br.com.delivery.api.service.PedidoService
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder


@RestController
@RequestMapping("/pedido")
class PedidoController(private val service: PedidoService, private val tokenUtils: TokenUtils) {


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun criar(
        @Valid @RequestBody form: PedidoFormNovo,
        req: HttpServletRequest,
        uriBuilder: UriComponentsBuilder,
    ): ResponseEntity<PedidoViewSimples> {
        val userId = tokenUtils.getUserId(req)
        val pedido = service.criar(userId, form)
        val uri = uriBuilder
            .path("/pedido/{id}")
            .buildAndExpand(pedido.id)
            .toUri()

        return ResponseEntity.created(uri).body(PedidoViewSimples(pedido))
    }

    @PutMapping("/{pedidoId}")
    @ResponseStatus(HttpStatus.OK)
    fun editar(
        @Valid @RequestBody form: PedidoFormAtualiza,
        @PathVariable pedidoId: Long,
        req: HttpServletRequest,
    ): ResponseEntity<PedidoViewSimples> {
        val userId = tokenUtils.getUserId(req)
        val pedido = service.atualizar(userId, pedidoId, form)
        return ResponseEntity.ok(PedidoViewSimples(pedido))
    }

    @GetMapping("/{pedidoId}")
    @ResponseStatus(HttpStatus.FOUND)
    fun buscar(@PathVariable pedidoId: Long, req: HttpServletRequest): ResponseEntity<PedidoViewSimples> {
        val userId = tokenUtils.getUserId(req)
        val pedido = service.buscar(userId, pedidoId)
        return ResponseEntity.ok(PedidoViewSimples(pedido))
    }

    @DeleteMapping("/{pedidoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deletar(@PathVariable pedidoId: Long, req: HttpServletRequest): ResponseEntity<Any> {
        val userId = tokenUtils.getUserId(req)
        service.deletar(userId, pedidoId)
        return ResponseEntity.noContent().build()
    }
}