# 📦 HubSpot Integration API

Esta aplicação foi desenvolvida para integrar com a API do HubSpot, recebendo eventos de criação de contatos via Webhook e permitindo a criação de contatos manualmente através de um endpoint protegido por autenticação OAuth2.

---

## 🚀 Como executar a aplicação

### ✅ Pré-requisitos

- Java 23+
- Docker e Docker Compose
- Maven 3.8+
- IDE (recomendado: IntelliJ ou VSCode)

### 🔧 Instruções

1. **Clone o repositório:**
   ```bash
   git clone https://github.com/jpramosjp/hubspot_integration.git
   cd hubspot_integration
   ```

2. **Configure as variáveis de ambiente:**

   Edite o arquivo `src/main/resources/application.properties` com suas credenciais da HubSpot:

   ```
   hubSpot.clientId=SEU_CLIENT_ID
   hubSpot.clientSecret=SEU_CLIENT_SECRET
   hubSpot.appId=SEU_APP_ID
   ```

3. **Suba os serviços com Docker Compose:**
   ```bash
   docker-compose up --build
   ```

4. **Acesse os serviços:**

   - API: [http://localhost:8080](http://localhost:8080)
   - Documentação Swagger: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
   - RabbitMQ: [http://localhost:15672](http://localhost:15672) (login: `guest` / `guest`)
   - Redis: Porta `6379` (uso interno ou via cliente Redis)

5. **Para parar os containers:**
   ```bash
   docker-compose down
   ```
---

## 📚 Endpoints principais

- `POST /webhook/`: Recebe eventos de criação de contato do HubSpot.
- `GET /webhook/allEvents`: Lista todos os eventos recebidos.
- `POST /contact/create`: Cria um novo contato no HubSpot manualmente (requer Authorization header).
- `GET /auth/`: Retorna a URL para iniciar o OAuth2 com o HubSpot.
- `GET /auth/callback`: Callback do OAuth2 que armazena o token.

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

## ⚙️ Principais tecnologias e bibliotecas

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

## 🧠 Decisões técnicas

- **Spring Boot** foi escolhido pela sua produtividade, facilidade de configuração e integração com o ecossistema Spring.
- **OAuth2 (Spring Security)** utilizado para autenticação segura, conforme exigência da API do HubSpot.
- **Swagger (Springdoc OpenAPI)** adicionado para facilitar a documentação e testes da API de forma visual e interativa.
- **Bucket4j** implementado para controle de Rate Limit, garantindo o respeito às políticas da API do HubSpot (ex: limite de 100 contatos por batch).
- **MySQL + JPA (Hibernate)** definidos para persistência confiável dos eventos recebidos, garantindo integridade e consistência dos dados.
- **RabbitMQ** adotado para desacoplar o recebimento de eventos da etapa de persistência, promovendo resiliência e escalabilidade no processamento assíncrono.
- **Redis** introduzido como cache in-memory para armazenar tokens e reduzir chamadas desnecessárias, melhorando a performance da aplicação.


---

## 🔐 Segurança

- Endpoints como `/contact/create` são protegidos com token de autenticação OAuth.
- Segurança customizada com `SecurityFilterChain` para desabilitar CSRF e configurar rotas públicas.
- Filtro `AuthorizationFilter` intercepta as requisições para validar o token recebido.
- O controle de requisições por IP foi implementado através do filtro `RateLimitFilter`, para limitar o número de chamadas por minuto e garantir conformidade com as políticas da API do HubSpot.

---

## 📈 Melhorias futuras

- 📂 **Salvar os tokens em banco de dados** para persistência entre sessões.
- 🥪 **Testes unitários e de integração** com cobertura mínima de 80%.
- 🔐 **Refresh automático de tokens OAuth** antes do vencimento.
- 📊 **Dashboard de monitoramento** para exibir eventos recebidos.
- ↺ **Retries automáticos para falhas na criação de contatos.**

---

## 👨‍💼 Autor

Desenvolvido por [João Pedro Ramos](https://github.com/jpramosjp)

---

