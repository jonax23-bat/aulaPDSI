package br.com.escola.mvc;

import br.com.escola.mvc.model.Aluno;
import br.com.escola.mvc.model.Curso;
import br.com.escola.mvc.model.Matricula;
import br.com.escola.mvc.model.StatusMatricula;
import br.com.escola.mvc.repository.AlunoRepository;
import br.com.escola.mvc.repository.CursoRepository;
import br.com.escola.mvc.repository.MatriculaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;

@SpringBootApplication
public class ProjetoMvcAlunosApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjetoMvcAlunosApplication.class, args);
    }

    // ==========================================
    // CARGA DE DADOS INICIAIS (CommandLineRunner)
    // Inicializa o banco de dados H2 com dados de teste para Alunos com RG, Cursos e Matrículas
    // ==========================================
    @Bean
    CommandLineRunner carregarDadosIniciais(AlunoRepository alunoRepo,
                                           CursoRepository cursoRepo,
                                           MatriculaRepository matriculaRepo) {
        return args -> {
            if (alunoRepo.count() == 0 && cursoRepo.count() == 0) {
                // [PARTE 2] Cadastrando Cursos Iniciais
                Curso c1 = cursoRepo.save(new Curso("Informática para Internet", "Desenvolvimento web com Java, Spring, HTML e banco de dados.", 1200));
                Curso c2 = cursoRepo.save(new Curso("Desenvolvimento de Sistemas", "Engenharia de software, modelagem e arquitetura de sistemas.", 1200));
                Curso c3 = cursoRepo.save(new Curso("Banco de Dados & Cloud", "Administração de bancos SQL, NoSQL e serviços em nuvem.", 400));

                // [PARTE 1] Cadastrando Alunos Iniciais com RG
                Aluno a1 = alunoRepo.save(new Aluno("385164233", "Ana Souza", "ana@email.com", c1.getNome()));
                Aluno a2 = alunoRepo.save(new Aluno("999999999", "Carlos Lima", "carlos@email.com", c1.getNome()));
                Aluno a3 = alunoRepo.save(new Aluno("888888888", "Mariana Silva", "mariana@email.com", c2.getNome()));

                // [DESAFIO] Cadastrando Matrículas Iniciais
                matriculaRepo.save(new Matricula(a1, c1, LocalDate.now().minusMonths(2), StatusMatricula.ATIVA));
                matriculaRepo.save(new Matricula(a2, c1, LocalDate.now().minusMonths(1), StatusMatricula.ATIVA));
                matriculaRepo.save(new Matricula(a3, c2, LocalDate.now().minusDays(15), StatusMatricula.ATIVA));
            }
        };
    }
}
