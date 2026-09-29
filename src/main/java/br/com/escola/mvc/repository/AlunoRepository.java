package br.com.escola.mvc.repository;

import br.com.escola.mvc.model.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AlunoRepository extends JpaRepository<Aluno, Long> {

    // Métodos para validação de unicidade de RG

    // Verifica se já existe algum aluno com determinado RG
    boolean existsByRg(String rg);

    // Busca o aluno pelo RG (usado na validação de edição)
    Optional<Aluno> findByRg(String rg);
}
