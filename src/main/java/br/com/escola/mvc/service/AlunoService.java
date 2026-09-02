package br.com.escola.mvc.service;

import br.com.escola.mvc.model.Aluno;
import br.com.escola.mvc.repository.AlunoRepository;
import br.com.escola.mvc.repository.MatriculaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AlunoService {

    private final AlunoRepository repository;
    private final MatriculaRepository matriculaRepository;

    public AlunoService(AlunoRepository repository, MatriculaRepository matriculaRepository) {
        this.repository = repository;
        this.matriculaRepository = matriculaRepository;
    }

    public List<Aluno> listarTodos() {
        return repository.findAll();
    }

    public Aluno buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Aluno não encontrado com ID: " + id));
    }

    public Aluno salvar(Aluno aluno) {
        validar(aluno);
        return repository.save(aluno);
    }

    public void excluir(Long id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Aluno não encontrado com ID: " + id);
        }

        // ==========================================
        // [DESAFIO] REGRA DE INTEGRIDADE REFERENCIAL
        // Impede a exclusão de um aluno que possua matrículas ativas/vinculadas
        // ==========================================
        if (!matriculaRepository.findByAlunoId(id).isEmpty()) {
            throw new IllegalArgumentException("Não é possível excluir este aluno pois existem matrículas vinculadas a ele.");
        }

        repository.deleteById(id);
    }

    private void validar(Aluno aluno) {
        if (aluno.getNome() == null || aluno.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("O nome é obrigatório.");
        }

        if (aluno.getEmail() == null || aluno.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("O e-mail é obrigatório.");
        }

        // ==========================================
        // [PARTE 1] VALIDAÇÃO 1: RG É CAMPO OBRIGATÓRIO
        // ==========================================
        if (aluno.getRg() == null || aluno.getRg().trim().isEmpty()) {
            throw new IllegalArgumentException("O RG é obrigatório.");
        }

        // ==========================================
        // [PARTE 1] VALIDAÇÃO 2: UNICIDADE DE RG
        // Impede o cadastro de dois alunos com o mesmo RG.
        // Na edição (quando aluno.getId() != null), permite manter o próprio RG.
        // ==========================================
        Optional<Aluno> alunoExistenteComMesmoRg = repository.findByRg(aluno.getRg());
        if (alunoExistenteComMesmoRg.isPresent()) {
            Aluno existente = alunoExistenteComMesmoRg.get();
            // Se for novo aluno (id nulo) OU se o ID do aluno encontrado for diferente do que está sendo editado
            if (aluno.getId() == null || !existente.getId().equals(aluno.getId())) {
                throw new IllegalArgumentException("Já existe um aluno cadastrado com este RG (" + aluno.getRgFormatado() + ").");
            }
        }
    }
}
