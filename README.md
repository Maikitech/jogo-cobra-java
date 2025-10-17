# 🐍 Jogo da Cobra em Java

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)

Projeto do clássico jogo Snake (cobrinha) desenvolvido em Java com a biblioteca gráfica Swing, como parte de uma atividade acadêmica.

<br>

## 🎬 Demonstração

![Animação](https://github.com/user-attachments/assets/25587be2-b1a9-4a00-9265-0ffb971c81ab)
 <br>

## ✨ Funcionalidades

* **Loop de Jogo Dinâmico:** O jogo roda de forma autônoma em uma thread separada, sem travar a interface.
* **Controle via Teclado:** Movimente a cobra usando as **setas direcionais** ou as teclas **W, A, S, D**.
* **Sistema de Pontuação:** A cobra cresce a cada fruta comida, aumentando o desafio.
* **Aumento de Velocidade:** O jogo fica progressivamente mais rápido a cada fruta, testando seus reflexos.
* **Detecção de Colisão:** O jogo termina se a cobra bater nas paredes ou em seu próprio corpo.
* **Design Personalizado:** A cobra possui um design diferenciado com olhos, gradientes e contornos.

<br>

## 🚀 Como Executar

Este projeto foi construído com Maven. Você pode executá-lo de duas formas:

### 1. Pela Linha de Comando (com Maven)

1.  Certifique-se de ter o [Java](https://www.oracle.com/java/technologies/downloads/) e o [Maven](https://maven.apache.org/download.cgi) instalados.
2.  Clone este repositório:
    ```bash
    git clone [https://github.com/SEU_USUARIO/jogo-cobra-java.git](https://github.com/SEU_USUARIO/jogo-cobra-java.git)
    ```
3.  Navegue até a pasta do projeto:
    ```bash
    cd jogo-cobra-java
    ```
4.  Compile e execute o projeto com o seguinte comando:
    ```bash
    mvn compile exec:java
    ```

### 2. Executando o Arquivo .JAR

1.  Vá para a página de **[Releases](https://github.com/SEU_USUARIO/jogo-cobra-java/releases)** deste repositório.
2.  Baixe o arquivo `JogoCobra-1.0.jar` (ou a versão mais recente).
3.  Execute o arquivo com um duplo clique ou pelo terminal:
    ```bash
    java -jar JogoCobra-1.0.jar
    ```

<br>

## 📝 Versões do Programa

### **v1.0** - (17/10/2025)
* **Funcionalidades:**
    * Criação do loop principal do jogo com `Runnable` e `Thread`.
    * Implementação do controle da cobra via teclado (`Key Bindings` para W,A,S,D e setas).
    * Sistema de colisão com paredes e com o corpo.
    * Aumento progressivo de velocidade ao comer frutas.
    * Interface gráfica e desenho dos componentes com Java Swing.
    * Estilização visual da cobra (olhos, gradientes e contornos).
* **Status:** Versão inicial completa, atendendo a todos os requisitos da atividade.

<br>

---
*Projeto desenvolvido por **Maiki Scalvi**.*
<img width="792" height="538" alt="image" src="https://github.com/user-attachments/assets/abffa2b9-3308-441d-85ab-25aefad7a7b1" />
