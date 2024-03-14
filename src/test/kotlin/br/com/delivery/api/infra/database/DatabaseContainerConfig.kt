package br.com.delivery.api.infra.database

import jakarta.transaction.Transactional
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.test.context.ActiveProfiles
import org.testcontainers.containers.PostgreSQLContainer

/**
 * Não utilizar anotação de classe:
 *      @TestContainer
 *      @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
 * Não utilizar anotação no container:
 *      @TestContainer
 *
 * Caso utilizados, elas irão gerenciar o ciclo de vida do container, juntamente com o padrão Singleton adotado.
 * Após a primeira classe utilizar o singleton do container, a conexão será fechada!
 * Futuras classes não conseguirão conectar ao container.
 * Poderia ser resolvido usando @DirtyContext, mas resulta em um reinicio total do sistema a cada teste.
 *
 * Sem anotações, o singleton é criado uma unica vez, e o start é feito manualmente no .apply{}
 *
 * Cronometrado 3 classes de testes:
 *      gerenciadas com as anotações: 17s
 *      manualmente: 6,5s
 *
 */
@ActiveProfiles("test")
@Transactional
@SpringBootTest
abstract class DatabaseContainerConfig {

    companion object {
        @ServiceConnection
        @JvmStatic
        private val postgresqlContainer =
            PostgreSQLContainer("postgres:latest")
                .apply { start() }

    }
}