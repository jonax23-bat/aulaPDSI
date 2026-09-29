package br.com.escola.mvc.repository;

import br.com.escola.mvc.model.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Repositório para operações de CRUD de Curso
@Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {

}
