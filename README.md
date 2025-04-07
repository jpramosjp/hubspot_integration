# HubSpot Integration API

Esta aplicação foi desenvolvida para integrar com a API do HubSpot, recebendo eventos de criação de contatos via Webhook e permitindo a criação de contatos manualmente através de um endpoint protegido por autenticação OAuth2.

---

## Como executar a aplicação

### Pré-requisitos

- Java 23+
- Docker e Docker Compose
- Maven 3.8+
- IDE (recomendado: IntelliJ ou VSCode)

### Instruções

1. **Clone o repositório:**
   ```bash
   git clone https://github.com/jpramosjp/hubspot_integration.git
   cd hubspot_integration
   ```

2. **Configure as variáveis de ambiente:**

   Edite o arquivo `src/main/resources/application.properties` com suas credenciais da HubSpot:

   ```properties
   hubSpot.clientSecret=SEU_CLIENT_SECRET
   hubSpot.clientId=SEU_CLIENT_ID
   hubSpot.scope=SEU_SCOPE
   hubSpot.redirectUri=SEU_REDIRECT_URI
   ```

3. **Suba os serviços com Docker Compose:**
   ```bash
   docker-compose up -d --build
   ```

4. **Acesse os serviços:**

- **API:** [http://localhost:8080](http://localhost:8080)
- **Documentação Swagger:** [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- **RabbitMQ:** [http://localhost:15672](http://localhost:15672)  
  *(Login: `guest` / Senha: `guest`)*
- **Redis:** Porta `6379`  
  *(Utilizado internamente pela aplicação ou acessível via cliente Redis)*
- **MySQL:**
   - **Host:** `localhost`  
   - **Porta:** `3306`  
   - **Banco de dados:** `meubanco`  
   - **Usuário:** `root`  
   - **Senha:** `root`  
   - **Tabela:** `contact_creation_event`  
      *(Utilizada para armazenar os eventos recebidos do HubSpot via webhook)*

   

5. **Para parar os containers:**
   ```bash
   docker-compose down
   ```
---

## Endpoints principais

### Clique para visualizar os detalhes de cada rota
<details>
  <summary><code>GET /auth/</code> – Retorna a URL para iniciar o OAuth2 com o HubSpot.</summary>

  **Descrição:**  
   Esse endpoint gera a URL de autenticação para iniciar o processo de conexão com o HubSpot via OAuth2.

  **Funcionamento:**  
   Quando acionado, ele constrói dinamicamente a URL de autorização utilizando as variáveis definidas no arquivo de propriedades, como `client_id`, `redirect_uri` e `scopes`, retornando a URL completa para o usuário iniciar o fluxo de autenticação.

</details>
<details>
  <summary><code>GET /auth/callback</code> – Callback do OAuth2 que armazena o token.</summary>

  **Descrição:**  
   Esse endpoint é o callback chamado após a autenticação do usuário via URL gerada na rota `/auth/`.

  **Funcionamento:**  
   Quando acionado, ele envia uma requisição para a API do HubSpot utilizando o `code` recebido como parâmetro, a fim de obter o token de acesso.  
   Em seguida, o token é armazenado no Redis para uso posterior na validação da rota protegida `/contact/create`.  
   Por fim, o token também é retornado ao usuário para que possa ser utilizado em chamadas autenticadas.

</details>

<details>
  <summary><code>POST /contact/create</code> – Cria um novo contato no HubSpot manualmente (requer Authorization header)</summary>

  **Descrição:**  
   Esse endpoint permite a criação manual de um contato no HubSpot por meio de uma requisição autenticada.

  **Funcionamento:**  
   Ao ser acionado, a requisição passa por um filtro que valida o token enviado no header `Authorization`, verificando se ele é válido e se ainda não expirou.  
   Em caso de validação bem-sucedida, os dados do contato são enviados para a API do HubSpot para efetuar a criação.  
   Esta rota possui um **rate limit de 100 requisições por minuto**, seguindo as limitações impostas pela própria API do HubSpot.

 **Exemplo de corpo da requisição:**
   ``` json 
      {
      "properties": {
         "email": "example@hubspot.com",
         "firstname": "Jane",
         "lastname": "Doe",
         "phone": "(555) 555-5555",
         "company": "HubSpot",
         "website": "hubspot.com"
      }
   }
```
</details>


<details>
  <summary><code>POST /webhook/</code> – Recebe eventos de criação de contato do HubSpot.</summary>

  **Descrição:**  
   Esse endpoint é chamado pelo HubSpot quando um novo contato é criado.

  **Funcionamento:**  
   Após ser acionada, essa rota envia os dados para a fila `contact.queue`, garantindo que o processamento ocorra de forma assíncrona e sem impactar a resposta ao usuário.  
   A fila `contact.queue` realiza até **3 tentativas** de salvar as informações recebidas do HubSpot na tabela `contact_creation_event`, garantindo a persistência dos dados no banco mesmo em caso de falhas temporárias.

</details>

<details>
  <summary><code>GET /webhook/allEvents</code> – Lista todos os eventos recebidos</summary>

  **Descrição:**  
   Esse endpoint retorna todos os eventos armazenados que foram capturados via webhook de criação de contato.

  **Funcionamento:**  
   Ao ser acionado, essa rota realiza uma consulta na tabela `contact_creation_event`, responsável por armazenar os dados recebidos do HubSpot.  
   Os eventos são então retornados ao usuário, permitindo auditoria, visualização ou processamento adicional.

</details>



A documentação completa da API está disponível via Swagger:
📄 [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

---

> ⚠️ **Observação sobre Webhook (HubSpot):**
>
> Para que o HubSpot consiga enviar eventos webhook para sua aplicação local, é necessário expor a API publicamente. Durante o desenvolvimento, foi utilizado o [Ngrok](https://ngrok.com/) para criar um túnel seguro para o `localhost`.
>
> Exemplo de uso:
> ```bash
> ngrok http 8080
> ```
> Após iniciar, cadastre a URL fornecida (ex: `https://1234-abc.ngrok.io/webhook/`) nas configurações do webhook no painel do HubSpot.

## Principais tecnologias e bibliotecas

| Tecnologia                     | Descrição                                                                                          |
|-------------------------------|----------------------------------------------------------------------------------------------------|
| **Spring Boot**               | Framework Java para criação de aplicações web robustas com configuração mínima                     |
| **Spring Security**           | Módulo de segurança da Spring para autenticação e autorização com suporte a OAuth2                |
| **HikariCP**                  | Pool de conexões JDBC leve e de alta performance                                                   |
| **MySQL**                     | Sistema de gerenciamento de banco de dados relacional (RDBMS) utilizado para armazenar os dados persistentes |
| **Lombok**                    | Biblioteca que reduz a verbosidade no código Java, gerando automaticamente getters, setters e construtores |
| **RabbitMQ**                  | Sistema de mensageria baseado em filas, utilizado para processamento assíncrono e desacoplamento entre componentes |
| **Swagger (Springdoc OpenAPI)** | Ferramenta para geração automática e visualização interativa da documentação da API               |
| **Bucket4j**                  | Biblioteca Java para controle de taxa de requisições (Rate Limiting), garantindo conformidade com as políticas da API externa |
| **Redis**                     | Armazenamento em cache em memória, usado para melhorar a performance em operações como reutilização de tokens |
| **Docker Compose**            | Ferramenta de orquestração de containers Docker, facilitando a configuração do ambiente com MySQL, RabbitMQ, Redis e a aplicação |


---

## Decisões técnicas

- **Spring Boot** foi escolhido pela sua produtividade, facilidade de configuração e integração com o ecossistema Spring.
- **OAuth2 (Spring Security)** utilizado para autenticação segura, conforme exigência da API do HubSpot.
- **Swagger (Springdoc OpenAPI)** adicionado para facilitar a documentação e testes da API de forma visual e interativa.
- **Bucket4j** implementado para controle de Rate Limit, garantindo o respeito às políticas da API do HubSpot (ex: limite de 100 contatos por batch).
- **MySQL + JPA (Hibernate)** definidos para persistência confiável dos eventos recebidos, garantindo integridade e consistência dos dados.
- **RabbitMQ** adotado para desacoplar o recebimento de eventos da etapa de persistência, promovendo resiliência e escalabilidade no processamento assíncrono.
- **Redis** introduzido como cache in-memory para armazenar tokens e reduzir chamadas desnecessárias, melhorando a performance da aplicação.


---

## Segurança

- Endpoints como `/contact/create` são protegidos com token de autenticação OAuth.
- Segurança customizada com `SecurityFilterChain` para desabilitar CSRF e configurar rotas públicas.
- Filtro `AuthorizationFilter` intercepta as requisições para validar o token recebido.
- O controle de requisições por IP foi implementado através do filtro `RateLimitFilter`, para limitar o número de chamadas por minuto e garantir conformidade com as políticas da API do HubSpot.

---

## Melhorias futuras

- 🥪 **Testes unitários e de integração** com cobertura mínima de 80%.
- 🔐 **Refresh automático de tokens OAuth** antes do vencimento.
- 📊 **Dashboard de monitoramento** para exibir eventos recebidos.
- ↺ **Retries automáticos para falhas na criação de contatos.**

---

## Autor

Desenvolvido por [João Pedro Ramos](https://github.com/jpramosjp)

---

