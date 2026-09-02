package br.com.escola.mvc.repository;

import br.com.escola.mvc.model.Aluno;
import br.com.escola.mvc.model.Curso;
import br.com.escola.mvc.model.Matricula;
import br.com.escola.mvc.model.StatusMatricula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// ==========================================
// [DESAFIO - REPOSITORY DE MATRÍCULA]
// Métodos derivados do Spring Data JPA para consultas com filtros
// ==========================================
@Repository
public interface MatriculaRepository extends JpaRepository<Matricula, Long> {

    // ==========================================
    // [DESAFIO] Consulta para validar duplicidade de matrícula ativa para o mesmo aluno e curso
    // ==========================================
    boolean existsByAlunoAndCursoAndStatus(Aluno aluno, Curso curso, StatusMatricula status);

    Optional<Matricula> findByAlunoAndCursoAndStatus(Aluno aluno, Curso curso, StatusMatricula status);

    // ==========================================
    // [DESAFIO] Consultas para proteção de exclusão (integridade referencial)
    // ==========================================
    List<Matricula> findByCursoId(Long cursoId);

    List<Matricula> findByAlunoId(Long alunoId);
}
