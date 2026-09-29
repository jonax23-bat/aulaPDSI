package br.com.escola.mvc.repository;

import br.com.escola.mvc.model.Aluno;
import br.com.escola.mvc.model.Curso;
import br.com.escola.mvc.model.Matricula;
import br.com.escola.mvc.model.StatusMatricula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// Repositório de consultas e persistência de Matrícula
@Repository
public interface MatriculaRepository extends JpaRepository<Matricula, Long> {

    // Validação de duplicidade de matrícula ativa
    boolean existsByAlunoAndCursoAndStatus(Aluno aluno, Curso curso, StatusMatricula status);

    Optional<Matricula> findByAlunoAndCursoAndStatus(Aluno aluno, Curso curso, StatusMatricula status);

    // Consultas para integridade referencial
    List<Matricula> findByCursoId(Long cursoId);

    List<Matricula> findByAlunoId(Long alunoId);
}
