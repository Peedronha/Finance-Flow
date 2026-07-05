# Finance-Flow

O Finance-Flow é um aplicativo Android desenvolvido para oferecer um controle financeiro pessoal eficiente, inteligente e intuitivo. O app utiliza as tecnologias mais modernas do ecossistema Android para garantir performance, segurança de dados e uma experiência consistente ao usuário.

## Tecnologias Utilizadas

Este projeto foi construído seguindo as melhores práticas de arquitetura Android atual:

* Linguagem: Kotlin
* Arquitetura: MVVM (Model-View-ViewModel) + Clean Architecture
* Banco de Dados: Room Database (com suporte a BigDecimal via TypeConverters)
* Programação Reativa: Coroutines e Flow (StateFlow)
* Interface: Material Design 3 e ViewBinding
* Persistência de Preferências: DataStore
* Injeção de Dependência: ViewModel Factory Pattern

## Funcionalidades

* Controle de Transações: Registro de receitas e despesas com categorização (Fixa, Variável, Investimento).
* Inteligência Financeira: Implementação da metodologia 50/30/20 (Cálculo automático de metas financeiras).
* Dashboard em Tempo Real: Visualização dinâmica do saldo com atualização reativa via StateFlow.
* Persistência Segura: Uso rigoroso de BigDecimal para cálculos monetários precisos.
* Interface Adaptável: Layout responsivo e organizado.

## Estrutura do Projeto

* data/: Gerenciamento de persistência com Room e Repositórios.
* model/: Entidades de dados e enums de suporte.
* ui/: Camada de apresentação com Activities, Adapters e XMLs.
* viewModel/: Regras de negócio e exposição de estado para a UI.

## Como Rodar

1. Clone o repositório:
git clone [https://github.com/Peedronha/Finance-Flow.git](https://www.google.com/search?q=https://github.com/Peedronha/Finance-Flow.git)
2. Abra o projeto no Android Studio.
3. Certifique-se de que o Gradle sincronizou todas as dependências.
4. Conecte um dispositivo ou inicie um emulador e execute o projeto.

---

Desenvolvido como um projeto de controle financeiro profissional.

---
