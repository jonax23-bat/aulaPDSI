package br.com.escola.mvc.repository;

import br.com.escola.mvc.model.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// ==========================================
// [PARTE 2 - REPOSITORY DE CURSO]
// Fornece operações de CRUD prontas (findAll, findById, save, deleteById, existsById)
// ==========================================
@Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {

}
