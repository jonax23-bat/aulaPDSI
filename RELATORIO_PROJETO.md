# Relatório de Implementação e Respostas - Sistema Escolar MVC

Este documento contém o detalhamento completo de tudo o que foi desenvolvido para a atividade de arquitetura MVC, abrangendo a **Parte 1 (RG de Alunos)**, a **Parte 2 (CRUD de Cursos)**, o **Desafio (Funcionalidade de Matrícula)** e as **Respostas às Questões de Projeto**.

---

## 🏛️ 1. Organização da Arquitetura em Camadas

O sistema segue rigorosamente a separação de responsabilidades recomendada no padrão MVC com Spring Boot:

- **`model/`**: Entidades JPA que mapeiam as tabelas do banco de dados (`Aluno.java`, `Curso.java`, `Matricula.java`, `StatusMatricula.java`).
- **`repository/`**: Interfaces que herdam de `JpaRepository` para operações de persistência e consultas derivadas (`AlunoRepository.java`, `CursoRepository.java`, `MatriculaRepository.java`).
- **`service/`**: Camada que centraliza todas as regras de negócio, validações de campos e integridade dos dados (`AlunoService.java`, `CursoService.java`, `MatriculaService.java`).
- **`controller/`**: Controladores Spring MVC que recebem as requisições HTTP, acionam os serviços e encaminham para as views (`AlunoController.java`, `CursoController.java`, `MatriculaController.java`, `HomeController.java`).
- **`templates/`**: Páginas HTML dinâmicas com Thymeleaf (`alunos/`, `cursos/`, `matriculas/`).
- **`static/css/`**: Estilização visual centralizada com navbar, cards, badges e alertas (`style.css`).

---

## 🛠️ 2. O Que Foi Desenvolvido Passo a Passo

### Parte 1 - Campo RG no Cadastro de Alunos
1. **Model `Aluno.java`:**
   - Adicionado o atributo `rg`.
   - Método `setRg()` higienizando a entrada para salvar apenas os dígitos no banco de dados.
   - Método auxiliar `getRgFormatado()` para apresentar o RG no padrão `00.000.000-0`.
2. **Repository `AlunoRepository.java`:**
   - Criados os métodos de consulta `existsByRg(String rg)` e `findByRg(String rg)`.
3. **Service `AlunoService.java`:**
   - Validação de RG obrigatório e não vazio.
   - **Validação de Unicidade de RG:** impede o cadastro de dois alunos com o mesmo número de RG. Na edição de um aluno existente, permite que ele mantenha seu próprio RG sem gerar falso positivo de duplicidade.
   - Validação de integridade na exclusão: impede excluir alunos com matrículas ativas/vinculadas.
4. **Views e Controller:**
   - `formulario.html` com input de RG e máscara em tempo real.
   - `lista.html` com exibição da coluna RG formatada.
   - `AlunoController.java` com tratamento de mensagens de sucesso e erros de validação.

---

### Parte 2 - CRUD Completo de Cursos
1. **Model `Curso.java`:**
   - Atributos: `id` (Long, auto-incremento), `nome` (String), `descricao` (String), `cargaHoraria` (Integer).
2. **Repository `CursoRepository.java`:**
   - Interface `JpaRepository<Curso, Long>`.
3. **Service `CursoService.java`:**
   - Validação de regras mínimas: `nome` obrigatório e `cargaHoraria` maior que zero (`> 0`).
   - Proteção de exclusão: impede a exclusão de cursos que possuam matrículas associadas.
4. **Controller `CursoController.java`:**
   - Rotas implementadas:
     - `GET /cursos`: listagem geral de cursos.
     - `GET /cursos/novo`: abertura do formulário de cadastro.
     - `POST /cursos/salvar`: persistência de novo curso ou atualização.
     - `GET /cursos/editar/{id}`: carregamento do curso para alteração.
     - `POST /cursos/excluir/{id}`: exclusão segura do curso.
5. **Views `templates/cursos/`:**
   - `lista.html`: tabela com id, nome, descrição, carga horária e botões de ação.
   - `formulario.html`: formulário com validações visuais e tratamento de feedback.

---

### Desafio - Funcionalidade de Matrícula
1. **Enum `StatusMatricula.java`:**
   - Valores: `ATIVA`, `TRANCADA`, `CONCLUIDA`.
2. **Model `Matricula.java`:**
   - Relacionamentos JPA `@ManyToOne(optional = false)` referenciando `Aluno` e `Curso`.
   - Campos `dataMatricula` (`LocalDate`) e `status` (`StatusMatricula`).
3. **Repository `MatriculaRepository.java`:**
   - Consultas derivadas para checar matrículas ativas por aluno e curso.
4. **Service `MatriculaService.java`:**
   - Validação de existência de Aluno e Curso válidos.
   - Preenchimento padrão de data (`LocalDate.now()`) e status inicial (`ATIVA`).
   - **Regra de Unicidade:** impede que um aluno possua mais de uma matrícula `ATIVA` no mesmo curso.
5. **Controller `MatriculaController.java`:**
   - Rotas: `/matriculas`, `/matriculas/nova`, `/matriculas/salvar`, `/matriculas/editar/{id}`, `/matriculas/excluir/{id}`.
   - Injeção das listas de alunos, cursos e status para alimentar os menus suspensos (`<select>`).
6. **Views `templates/matriculas/`:**
   - `formulario.html`: selects dinâmicos de Aluno e Curso e campos de data/status.
   - `lista.html`: tabela com Aluno, RG, Curso, Data formatada (`dd/MM/yyyy`), Badges de status coloridos e ações.

---

## 📝 3. Respostas às Questões de Projeto

### Questão 1: Em qual camada foi implementada a validação de matrícula duplicada? Justifique.
> **Resposta:**  
> A validação de matrícula duplicada foi implementada na camada **Service** (`MatriculaService.java`).  
> **Justificativa:** A regra que impede que o mesmo aluno tenha duas matrículas com status `ATIVA` no mesmo curso é uma **regra de negócio da aplicação**.  
> - A camada **Controller** não deve conter lógicas de negócio, pois sua função é apenas receber a requisição Web e escolher qual resposta ou View devolver.  
> - A camada **Repository** tem o papel restrito de acessar o banco de dados.  
> - A camada **View** cuida apenas da apresentação.  
> Concentrar as regras no **Service** garante que, se no futuro o sistema ganhar uma API Mobile ou receber dados em lote, as mesmas validações continuarão sendo executadas sem duplicar código.

---

### Questão 2: O atributo `curso` como texto ainda deve permanecer dentro da classe `Aluno` depois da criação da entidade `Matricula`? Explique.
> **Resposta:**  
> **Não deve permanecer no modelo relacional final**, sendo mantido no início do aprendizado apenas para fins de transição didática.  
> **Explicação:** Quando criamos a entidade `Matricula`, a relação entre Aluno e Curso passa a ser representada corretamente como uma relação **Muitos-para-Muitos (N:N)** intermediada pela tabela `MATRICULAS`.  
> Manter uma coluna `curso` textual na tabela de Alunos causa:
> 1. **Redundância e Inconsistência:** Se o nome de um curso for alterado, o texto gravado no aluno ficaria desatualizado.
> 2. **Rigidez:** Impede que um mesmo estudante se matricule em múltiplos cursos simultaneamente ou guarde o histórico de cursos concluídos.

---

### Questão 3: O que deve acontecer se alguém tentar excluir um curso que possui matrículas?
> **Resposta:**  
> O sistema **deve bloquear a exclusão e retornar uma mensagem de erro compreensível ao usuário** (ex: *"Não é possível excluir este curso pois existem matrículas vinculadas a ele."*).  
> **Justificativa:** No banco de dados relacional, a tabela `MATRICULAS` possui uma restrição de integridade referencial (**Foreign Key**) apontando para a tabela `CURSOS`. Se o curso fosse apagado diretamente, o banco dispararia uma violação de chave estrangeira (`DataIntegrityViolationException`), deixando dados órfãos ou quebrando o histórico acadêmico. Por isso, a camada `CursoService` verifica antecipadamente a existência de matrículas e impede a exclusão.

---

### Questão 4: Qual é o fluxo percorrido desde o envio do formulário até a gravação da matrícula no banco?
> **Resposta (Fluxo completo da requisição MVC):**
> 1. **View (Navegador):** O usuário preenche os campos no formulário HTML (`/matriculas/nova`) e clica no botão "Salvar". O navegador dispara uma requisição HTTP `POST /matriculas/salvar` contendo os parâmetros do formulário.
> 2. **Controller (`MatriculaController`):** O método `salvar(@ModelAttribute Matricula matricula)` intercepta a rota, faz o binding dos dados enviados para o objeto `Matricula` e aciona o método `matriculaService.salvar(matricula)`.
> 3. **Service (`MatriculaService`):** Executa o método `validar(matricula)`, consultando os repositórios para certificar-se de que o aluno e o curso existem e garantindo que não existe outra matrícula com status `ATIVA` para aquele mesmo aluno e curso.
> 4. **Repository (`MatriculaRepository`):** Após todas as regras de validação serem aprovadas, o service chama `matriculaRepository.save(matricula)`.
> 5. **Banco de Dados (H2):** O Spring Data JPA / Hibernate gera o comando SQL `INSERT INTO MATRICULAS (aluno_id, curso_id, data_matricula, status) VALUES (...)` e persiste a transação na memória.
> 6. **Retorno e Redirecionamento:** O controller adiciona uma mensagem flash de sucesso e retorna um `redirect:/matriculas`. O navegador recebe o redirecionamento (HTTP 302), faz um novo `GET /matriculas`, o controller busca as matrículas atualizadas no banco e devolve a View `matriculas/lista.html` com a tabela atualizada.

---

## 🚀 4. Como Executar e Demonstrar o Projeto

1. **Iniciar a Aplicação:**
   ```powershell
   mvn spring-boot:run
   ```
   *(Ou usando o wrapper: `.\mvnw spring-boot:run`)*

2. **Rotas Principais no Navegador:**
   - 👨‍🎓 **Alunos:** `http://localhost:8080/alunos`
   - 📘 **Cursos:** `http://localhost:8080/cursos`
   - 📝 **Matrículas:** `http://localhost:8080/matriculas`
   - 🗄️ **H2 Console:** `http://localhost:8080/h2-console`
     - **JDBC URL:** `jdbc:h2:mem:escola`
     - **User Name:** `sa`
     - **Password:** *(deixar em branco)*

3. **Consultas SQL no H2 Console:**
   ```sql
   SELECT * FROM ALUNO;
   SELECT * FROM CURSOS;
   SELECT * FROM MATRICULAS;
   ```
