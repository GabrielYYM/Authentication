# Sistema de Autenticação (Spring Boot + React + Thymeleaf + MongoDB Atlas)

Sistema de autenticação com controle de acesso baseado em perfis utilizando **Spring Boot 4**, **Spring Security (JWT)**, **MongoDB Atlas** e **React (Vite + Tailwind CSS) + Thymeleaf**.

---

## Pré-requisitos

- **Java JDK 21+**
- **Maven**
- Conta no MongoDB Atlas

---

## 1. Configuração do Ambiente

### 1. Configurar o MongoDB Atlas

1. Crie um cluster gratuito no MongoDB Atlas.
2. Crie um usuário e senha.
3. Copie o env fornecido

### 2. Variáveis de Ambiente no Backend

Na raiz do projeto (mesma pasta do `pom.xml`), crie um arquivo `.env`:

```env
JWT_SECRET=sua-chave-secreta-jwt-super-segura-aqui
MONGODB_URI=mongodb+srv://<usuario>:<senha>@<cluster>.mongodb.net/?retryWrites=true&w=majority
MONGODB_DATABASE=auth_db
```

## 2. Execução do Sistema

1. **Compilar o Frontend:**
   ```bash
   cd frontend
   npm install
   npm run build
   cd ..
   ```

2. **Iniciar o Backend (Spring Boot):**
   - No Windows:
     ```powershell
     .\mvnw.cmd spring-boot:run
     ```
   - No Linux/Mac:
     ```bash
     ./mvnw spring-boot:run
     ```

3. **Acessar a aplicação:**
   - Acesse no navegador: [http://localhost:8080](http://localhost:8080)

