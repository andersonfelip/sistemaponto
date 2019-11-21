# PontoEletronico
### 1.0.0 
### [ Base URL: com.br.anderonfelip/ ]

API para controle de ponto eletrônico

Aplicação baseada no spring boot usando spring data jpa e hibernate, seguindo abordagem de "contract-first" e sendo assim um (ou mais) arquivo Swagger definido e apontado no POM. 
O código é gerado a cada build e a implementação da API usa a interface e o modelo gerado automaticamente a cada Build da aplicação.

Utiliza injeção de dependências.

## Banco de dados

create database pontoeletronico

### Application.Properties

No arquivo Application.Properties deve ser colocado o login e senha para acesso ao banco de dados, por parte do sistema.
Também o url de acesso

```
db.default.url="jdbc:h2:./data/sistemaponto"
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
```

## Maven

Na pasta do projeto, digite: mvn clean install compile, para instalação das dependências e criação das classes de interface e modelos.

## Levantando serviço via Eclipse
Executar a classe com.br.pontoeletronico.app.PontoeletronicoApplication

### Documentação para uso da API encontra-se no LINK: ###
+ https://app.swaggerhub.com/apis-docs/andersonfelipe/pontoeletronico/1.0.0

Sistema criado em Spring Boot, utilizando das seguintes tecnologias:
- SpringBoot
- Spring Security
- GSON
- Swagger
- Autenticacao (Basic)
- jackson
- Org.Json
- JPA
- Hibernate
- MySql
- JUnit
- Cucumber
- ModelMapper no formato Singleton

As Dependências são instaladas utilizando o gerenciador MAVEN

### Utilizando

Deve ser criado um banco de nome: pontoeletronico

- create table pontoeletronico;

As entidades estão mapeadas para gerar suas respectivas tabelas.

## Segurança
A segurança do sistema está implementada através do Spring Security pela autenticação BASIC.

Toda requisição deve ser autenticada através do usuário e senha no header da solicitação.

Para ter acesso as requisições siga os passos abaixo:
Através do PostMan clique no guia Authorization, no combo Type selecione "Basic Auth"

```
usename=123456
password=123456
```

### Incluir Batida
Para realizar uma batida basta enviar um POST para a url https://pontoeletronicoanderson.herokuapp.com/funcionarios/{pisFuncionario}/incluirBatida Exemplo: https://pontoeletronicoanderson.herokuapp.com/funcionarios/123456/incluirBatida

Exemplo do Objeto enviado:
```
{
	"dataHoraBatida":"25/11/2018 23:00"
}
```

Obs.: A data deve estar no formato descrito no exemplo acima - "dd/MM/yyyy hh:MM" para funcionar.

### Verificar Batida por dia
Para realizar uma batida basta enviar um GET para a url https://pontoeletronicoanderson.herokuapp.com/funcionarios/{pisFuncionario}/batidas/{dataFiltro} 
Exemplo: https://pontoeletronicoanderson.herokuapp.com/funcionarios/123456/batidas/2018-11-27

Exemplo do Objeto retornado:
```
{
    "id": 1,
    "dataHoraBatida": "27/11/2018 08:00",
    "tipoRegistro": "Entrada",
    "pisFuncionario": "123456"
}
```

Serão listadas as batidas informadas no dia passado através da url. O formato da data descrito como {dataFiltro} deve estar no formato "yyyy-MM-dd" para funcionar.

### Verificar Batida por mes
Para realizar uma batida basta enviar um GET para a url https://pontoeletronicoanderson.herokuapp.com/funcionarios/{pisFuncionario}/batidas/{mesFiltro} 
Exemplo: https://pontoeletronicoanderson.herokuapp.com/funcionarios/123456/batidas/2018-11

Exemplo do Objeto retornado:
```
{
    "id": 1,
    "dataHoraBatida": "27/11/2018 08:00",
    "tipoRegistro": "Entrada",
    "pisFuncionario": "123456"
}
```
Serão listadas as batidas informadas no mês passado através da url. O formato da data descrito como {dataFiltro} deve estar no formato "yyyy-MM" para funcionar.