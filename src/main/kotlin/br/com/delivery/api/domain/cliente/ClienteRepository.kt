package br.com.delivery.api.domain.cliente

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ClienteRepository : JpaRepository<Cliente, Long> {

    fun findByLogin(login: String?) : Optional<Cliente>

}