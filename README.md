# 📦 Controle de Estoque

Sistema completo de controle de estoque desenvolvido como parte de um desafio técnico, com arquitetura full stack moderna utilizando **Spring Boot (Back-end)** e **Nuxt 3 (Front-end)**.

O projeto foi estruturado seguindo boas práticas de arquitetura, organização de código, separação de responsabilidades e validação em múltiplas camadas.

---

# 🏗️ Arquitetura

A aplicação está organizada em duas camadas principais:

```
controle-estoque/
│
├── backend/   → API REST (Spring Boot)
└── frontend/  → SPA (Nuxt 3 + Vue 3)
```

### 🔹 Back-end
- Arquitetura em camadas (Controller → Service → Repository)
- Uso de DTOs para desacoplamento da entidade
- Paginação padronizada
- Respostas estruturadas
- Pronto para evolução para banco relacional persistente

### 🔹 Front-end
- SPA moderna com Nuxt 4
- Gerenciamento de estado com Pinia
- Validação tipada com Zod
- Componentização com PrimeVue
- Integração desacoplada via service layer

---

# 🚀 Tecnologias Utilizadas

## 🔹 Back-end
- Java 21+
- Spring Boot 3
- Spring Data JPA
- Hibernate
- H2 Database (em memória)
- Maven
- Swagger / OpenAPI

## 🔹 Front-end
- Nuxt 3
- Vue 3 (Composition API)
- TypeScript
- Pinia
- Zod
- PrimeVue
- TailwindCSS

---

# 📌 Funcionalidades Implementadas

## Produtos
- Cadastro
- Edição
- Exclusão
- Listagem paginada
- Filtro por tipo
- Validações de negócio

## Movimentações
- Entrada e Saída
- Cálculo automático de valor total
- Atualização de estoque
- Validações para evitar inconsistências

---

# 🧠 Decisões Técnicas

- Separação clara de responsabilidades no back-end.
- Uso de DTOs para evitar exposição direta das entidades.
- Retorno padronizado com `BaseResponse` e `PageBaseResponse`.
- Paginação utilizando `Pageable`.
- Validação no front-end com Zod antes do envio à API.
- Centralização de estado com Pinia.
- Camada de service no front-end para desacoplamento da API.
- Estrutura preparada para fácil substituição do banco H2 por PostgreSQL/Oracle.

---

# ⚙️ Como Executar o Projeto

## 🖥️ Back-end

### Pré-requisitos
- Java 21+
- Maven 3.9+

### Executar

```bash
cd backend
mvn spring-boot:run
```

Aplicação disponível em:

```
http://localhost:8080
```

### Swagger

```
http://localhost:8080/swagger-ui.html
```

---

## 🌐 Front-end

### Pré-requisitos
- Node 18+
- npm ou pnpm

### Instalar dependências

```bash
cd frontend
npm install
```

### Executar

```bash
npm run dev
```

Aplicação disponível em:

```
http://localhost:3000
```

---

# 🔄 Integração Front ↔ Back

A URL base da API é configurada via `runtimeConfig` no Nuxt:

```ts
runtimeConfig: {
  public: {
    apiBase: 'http://localhost:8080'
  }
}
```

---

# 🧪 Testes

Back-end preparado para testes com Spring Boot Test.

```bash
mvn test
```

---

# 📈 Melhorias Futuras

- Autenticação com JWT
- Controle de permissões por perfil
- Dockerização da aplicação
- Testes automatizados no front-end
- Persistência com PostgreSQL
- Deploy em ambiente cloud

---

# 👨‍💻 Autor

Felipe Gabriel  
Desenvolvedor Full Stack  
Vue.js | Nuxt | Spring Boot | Java

---

Caso necessário, estou à disposição para apresentar a arquitetura e explicar as decisões técnicas adotadas no desenvolvimento.

