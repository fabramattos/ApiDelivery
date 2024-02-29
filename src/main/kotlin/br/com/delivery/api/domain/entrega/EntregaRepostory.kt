package br.com.delivery.api.domain.entrega

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface EntregaRepostory : JpaRepository<Entrega, Long> {
}