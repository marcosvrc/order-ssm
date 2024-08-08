# Spring State Machine


1. [Conceitos](#conceitos)
2. [Sobre o projeto](#sobre-projeto)
3. [Como executar](#como-executar)


### 1. Conceitos
<div id='conceitos'/>  
O que é o Spring State Machine?

O Spring State Machine é um framework poderoso e flexível, parte do ecossistema Spring, projetado para modelar e gerenciar sistemas que evoluem através de diferentes estados em resposta a eventos. Imagine um processo de pedido, por exemplo: um pedido pode estar em estado "criado", "pago", "enviado" ou "cancelado". O Spring State Machine permite definir essas transições de estado de forma clara e concisa, tornando a gestão de fluxos complexos mais simples e robusta.

Por que usar o Spring State Machine?

- **Simplificação de Lógica de Negócio:** Ao abstrair a lógica de estado em uma máquina de estados, você torna o código mais limpo e fácil de entender.
- **Gerenciamento de Fluxos Complexos:** Ideal para processos com múltiplos estados e transições condicionais, como workflows de aprovação ou processos de fabricação.
- **Integração com o Ecossistema Spring:** Se você já utiliza Spring em seus projetos, o Spring State Machine se integra perfeitamente com outros componentes como Spring Boot e Spring Data.
- **Extensibilidade:** Permite personalizar e estender o comportamento da máquina de estados para atender às necessidades específicas de seu projeto.

Conceitos-chave:

- **Estados:** Representam as diferentes fases de um processo.
- **Eventos:** São as ações que provocam transições entre os estados.
- **Transições:** Definem as regras para mudar de um estado para outro.
- **Guarda:** Uma condição que deve ser satisfeita para que uma transição ocorra.
- **Ação:** Uma operação executada quando uma transição é realizada.

Benefícios:

- **Melhora da Manutenabilidade:** Códigos mais claros e fáceis de entender.
- **Aumento da Robustez:** Menor probabilidade de erros em processos complexos.
- **Reutilização de Código:** Máquinas de estados podem ser reutilizadas em diferentes partes da aplicação.
- **Facilidade de Teste:** Permite testar de forma isolada a lógica de estado.

Quando usar o Spring State Machine?

- **Sistemas com fluxos de trabalho complexos:** Processos de aprovação, orquestração de serviços, gerenciamento de pedidos.
- **Sistemas reativos:** Para modelar sistemas que reagem a eventos de forma assíncrona.
- **Sistemas com estados finitos:** Quando o sistema pode estar em um número limitado de estados.

Em resumo, o Spring State Machine é uma ferramenta poderosa para modelar e gerenciar sistemas com estados. Se você precisa lidar com processos complexos e deseja tornar seu código mais organizado e fácil de manter, o Spring State Machine é uma excelente opção.
<div id='sobre-projeto'/>  

### 2. Sobre o projeto


O projeto tem como objetivo utilizar o spring state machine para implementar uma saga orquestrada de pedido. Ele foi baseado no vídeo tutorial apresentado pela Giuliana Bezerra no [link youtube](https://www.youtube.com/watch?v=QorhhL0XETI). Abaixo é mostrando o diagrama de estado que será utilizado.

![Diagrama State Machine](https://github.com/marcosvrc/imagens_readme/blob/master/order-ssm/State_Machine_Diagrama.png)

Os fluxos que serão executados são:

1. NEW -> VALIDATED -> PAID -> SHIPPED -> COMPLETED
2. NEW -> VALIDATED -> PAID -> CANCELLED
3. NEW -> CANCEL
4. NEW -> VALIDATED -> CANCELLED
<div id='como-executar'/>  

### 3. Como executar

Rodar o projeto spring boot e depois acessar o swagger gerado pela aplicação conforme a url

```sh
http://localhost:8080/swagger-ui/index.html
```
onde será exibido a imagem abaixo

![Tela Swagger](https://github.com/marcosvrc/imagens_readme/blob/master/order-ssm/Tela%20Swagger%20Order%20SSM.png)

Para executar os fluxos comentados no item 2, seguir as etapas abaixo

NEW -> VALIDATED -> PAID -> SHIPPED -> COMPLETED

```sh
1. http://localhost:8080/order/new/true
2. http://localhost:8080/order/validate/true
3. http://localhost:8080/order/pay/true
4. http://localhost:8080/order/ship
5. http://localhost:8080/order/complete
```

NEW -> VALIDATED -> PAID -> CANCELLED

```sh
1. http://localhost:8080/order/new/true
2. http://localhost:8080/order/validate/true
6. http://localhost:8080/order/pay/false
```
NEW -> CANCEL

```sh
1-6. http://localhost:8080/order/new/false
```
NEW -> VALIDATED -> CANCELLED

```sh
1. http://localhost:8080/order/new/true
2. http://localhost:8080/order/validate/true
6. http://localhost:8080/order/validate/false
```
