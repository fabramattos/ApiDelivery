package br.com.delivery.api.infra.security

import br.com.delivery.api.domain.cliente.ClienteRepository
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class TokenFilter(
    private val tokenUtils: TokenUtils,
    private val repository: ClienteRepository,
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        tokenUtils
            .recuperaToken(request)
            ?.let {
                val subject = tokenUtils.getSubject(it)
                val usuario = repository.findByLogin(subject).orElseThrow()
                val tokenAuthentication = UsernamePasswordAuthenticationToken(usuario, null, null)
                SecurityContextHolder.getContext().authentication = tokenAuthentication
            }
        filterChain.doFilter(request, response)
    }

}
