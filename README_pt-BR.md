*Change language:* [![en](https://img.shields.io/badge/lang-en-red.svg)](https://github.com/fabramattos/ApiDelivery)<br>
<br>

🚧 PROJETO EM DESENVOLVIMENTO 🚧

# Controle de Delivery de Restaurante
API simplificada para controle de delivery de um restaurante.<br>
Este projeto, originalmente desenvolvido por mim em Java, era parte de um processo seletivo para BackEnd.<br>
Resolvi refazer em Kotlin alterando um pouco a stack  e incrementando algumas funcionalidades como parte de meus estudos.<br>

## Stack
### API RESTful:
- Kotlin
- Spring Boot 3
- Spring (Security, Data, Web)
- Docker
- PostgresSQL
- Gradle
- OpenAPI 3.0 (Swagger)

## Funcionalidades
### Segurança:
- Permitir o cadastro de usuários e login com autenticação via token JWT. Os métodos das APIs abaixo só poderão ser executados caso o usuário esteja logado.
### Cliente:
- Permitir o cadastro, alteração, deleção e consulta de clientes.
### Pedido:
- Permitir o cadastro, alteração, deleção e consulta de pedidos. Um pedido obrigatoriamente precisa ter um cliente e um cliente pode ter vários pedidos.
### Entrega:
- Permitir o cadastro, alteração, deleção e consulta de entregas. Uma entrega obrigatoriamente necessita estar vinculada a um pedido.

### Obs sobre Deleção:
Ao deletar um usuário, verificações são feitas. Usuários e pedidos só podem ser deletados se não houver entrega em andamento.<br>
Caso delete usuário, todos dados atrelados são deletados.<br>
Caso delete pedido, entrega é deletada.<br>
Caso delete entrega, pedido fica desassociado de uma entrega.<br>

## Instruções para Execução
ℹ️Informações sobre autenticação jwt e login  estarão disponíveis ao acessar o link do Swagger-UI da aplicaçãoℹ️

### Localmente, sem Docker:
1. Certifique-se de ter instalado em sua máquina: Java 19, PostgresSQL
2. No PostgreSQL, crie um banco de dados para a aplicação.
3. Clone este repositório em seu ambiente local.
4. Configure as credenciais do banco de dados no arquivo application.yml.
5. Execute o comando Maven ```gradle bootRun```
6. (em desenvolvimento) A API estará disponível no seguinte endereço: http://localhost:8080/swagger-ui.html


### Localmente, com Docker:
😎dispensa configurações nos arquivos do projeto😎
1. Certifique-se de ter instalado e em execução na sua máquina: Docker, Java 19
2. Clone este repositório em seu ambiente local.
3. Execute os seguintes comandos no terminal da IDE (na raiz do projeto):
  - Gradle: ```gradle build``` (Gera o artefato)
  - Docker: ```docker build -t delivery-api -f Dockerfile-dev .``` (utiliza o arquivo DockerFile-dev para gerar a imagem docker)
  - Docker: ```docker-compose up``` (utiliza o arquivo Docker-Compose para configurar e executar a API e o PostgresSQL)
4. (em desenvolvimento) A API estará disponível no seguinte endereço: http://localhost:8080/swagger-ui.html


## Melhorias futuras:
### No app
- Melhor refinamento para tratar as exception devido as Validations, mostrando mensagem de erro mais coesa para o front-end
- Testes para os Controllers com mockMvc.
- criação de arquitetura para "Itens", substituindo uma descrição por um controle de estoque, com adição de itens, remoção, calculo de preço do pedido.
- Controle por Role para permitir um admin registrar itens no estoque.

### CI/CD
- reestruturar arquitetura de branches e workflows do github actions
