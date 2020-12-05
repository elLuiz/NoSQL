# Banco de dados NoSQL

![Java e gRPC](https://img.shields.io/badge/JAVA-gRPC-yellow)

Projeto feito por alunos da Universidade Federal de Uberlândia na matéria de Sistemas Distribuídos dos professores **Lásaro Jonas Camargos** e **Paulo Rodolfo da Silva Leite Coelho**.

## Objetivos

- Hash Table acessível remotamente por interface CRUD usando gRPC.
- Armazenamento em disco com recuperação de dados no caso de falhas

## Especificações

###### Ambiente indicado:

![JDK Version](https://img.shields.io/badge/openjdk-v11.0.9.1-orange)
![Ambiente](https://img.shields.io/badge/Ambiente%20utilizado-Linux%20Ubuntu-blue)
![IDEs](https://img.shields.io/badge/Eclipse-IntelliJ-red)

## Download e execução

- Faça o clone do repositório com o comando:
    ````GIT
        git clone https://github.com/Fritask/TrabalhoSD.git
    ````
- Certifique-se que está na branch master;
- Abra o repositório na sua IDE de preferência como um **projeto maven**;
- Com o projeto aberto, é preciso fazer o build como **MAVEN BUILD** no arquivo **`pom.xml`**;
- **Como executar o servidor:**
    - Entre na pasta **`src/main/java/com/server/`** e execute como uma **aplicação JAVA** o arquivo **`KeyValueServer.java`**
- **Como executar o cliente:**
    - Entre na pasta **`src/main/java/com/client/`** e execute como uma **aplicação JAVA** o arquivo **`Client.java`**

````JSON
Observação: "para que qualquer mudança realizada no cliente tenha o efeito desejado, é necessário que o servidor esteja executando"
````

- **Como executar os testes:**
    - **teste de estresse com 1000 dados para testar cada função:** com o servidor executando (é preciso que ele esteja rodando) execute os testes no arquivo **`src/test/java/com/test/Client/ClientStressTest.java`** utilizando o JUnit.
    - **teste básico com 10 dados para testar cada função:** com o servidor executando (é preciso que ele esteja rodando) execute os testes no arquivo **`src/test/java/com/test/Client/ClientTest.java`** utilizando o JUnit.

## Membros do Grupo

- Arthur Filipe Sousa Gomes
- Guilherme Henrique de Araújo Santos
- Guilherme Moreira Bomfim de Freitas
- Luiz Henrique Dias Lima
- Silas Mota de Sousa