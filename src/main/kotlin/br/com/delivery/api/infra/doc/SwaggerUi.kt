package br.com.delivery.api.br.com.delivery.api.infra.doc

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerUi {
    @Bean
    fun customOpenAPI() = OpenAPI()
        .info(Info().apply {
            version = "1.0"
            title = "Delivery API"
            description = """
                        DESCRIÇÃO:
                        API simplificada para controle de delivery de um restaurante.

                        Segurança:
                        Permitir o cadastro de usuários e login com autenticação via token JWT.
                        Os métodos das APIs abaixo só poderão ser executados caso o usuário esteja logado.
                        
                        Cliente:
                        Permitir o cadastro, alteração, deleção e consulta de clientes.
                        
                        Pedido:
                        Permitir o cadastro, alteração, deleção e consulta de pedidos.
                        Um pedido obrigatoriamente precisa ter um cliente e um cliente pode ter vários pedidos.
                        
                        Entrega:
                        Permitir o cadastro, alteração, deleção e consulta de entregas.
                        Uma entrega obrigatoriamente necessita estar vinculada a um pedido.
                        
                        Ao deletar um usuário, verificações são feitas.
                        Usuários e pedidos só podem ser deletados se não houver entrega em andamento.
                        
                        Caso delete usuário, todos dados atrelados são deletados.
                        Caso delete pedido, entrega é deletada.
                        Caso delete entrega, pedido fica desassociado de uma entrega.
                                            """
            contact = Contact().apply {
                name = "Felipe Mattos"
//                email = "email@email.com"
//                url = "urlFrontEnd.com.br"
            }
        }
        )
}