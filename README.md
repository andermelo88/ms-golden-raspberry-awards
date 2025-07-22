# ms-golden-raspberry-awards

Aplicação Spring Boot para análise dos prêmios Golden Raspberry Awards.

## Requisitos

- Java 17+
- Maven 3.8+
- (Opcional) Visual Studio Code com extensões Java e Lombok

## Como executar a aplicação

1. **Clone o repositório:**
   ```
   git clone https://github.com/andermelo88/ms-golden-raspberry-awards.git
   cd ms-golden-raspberry-awards
   ```

2. **Compile o projeto:**
   ```
   mvn clean package
   ```

3. **Execute a aplicação:**
   ```
   java -jar target/ms-golden-raspberry-awards-0.0.1-SNAPSHOT.jar
   ```
   Ou, para rodar direto pelo Maven:
   ```
   mvn spring-boot:run
   ```

4. **Acesse os endpoints:**
   - API principal: `http://localhost:8080/movie/v1/intervals/`
   - H2 Console: `http://localhost:8080/h2-console`  
     (usuário: `sa`, senha: em branco, JDBC URL: `jdbc:h2:file:./data/moviedb`)

## Testes

1. **Executar testes unitários:**
   ```
   mvn test
   ```

2. **Relatórios de teste:**
   Os relatórios são gerados em `target/surefire-reports`.

## Observações

- O banco H2 é criado em `./data/moviedb.mv.db` e removido automaticamente ao encerrar a aplicação.
- O arquivo CSV de filmes deve estar em `src/main/resources/Movielist.csv`.
- O CSV é carregado apenas uma vez, evitando duplicidade de dados.

## Estrutura dos principais diretórios

- `src/main/java/com/outsera/ms_golden_raspberry_awards/` — Código fonte
- `src/main/resources/` — Arquivos de configuração e CSV
- `src/test/java/` — Testes automatizados
