package br.com.escola.mvc.service;

import br.com.escola.mvc.model.Aluno;
import br.com.escola.mvc.model.Curso;
import br.com.escola.mvc.model.Matricula;
import br.com.escola.mvc.model.StatusMatricula;
import br.com.escola.mvc.repository.AlunoRepository;
import br.com.escola.mvc.repository.CursoRepository;
import br.com.escola.mvc.repository.MatriculaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

// Service com regras de negócio e restrições de matrícula
@Service
public class MatriculaService {

    private final MatriculaRepository matriculaRepository;
    private final AlunoRepository alunoRepository;
    private final CursoRepository cursoRepository;

    public MatriculaService(MatriculaRepository matriculaRepository,
                            AlunoRepository alunoRepository,
                            CursoRepository cursoRepository) {
        this.matriculaRepository = matriculaRepository;
        this.alunoRepository = alunoRepository;
        this.cursoRepository = cursoRepository;
    }

    public List<Matricula> listarTodas() {
        return matriculaRepository.findAll();
    }

    public Matricula buscarPorId(Long id) {
        return matriculaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Matrícula não encontrada com ID: " + id));
    }

    public Matricula salvar(Matricula matricula) {
        validar(matricula);
        return matriculaRepository.save(matricula);
    }

    public void alterarStatus(Long id, StatusMatricula novoStatus) {
        Matricula matricula = buscarPorId(id);
        matricula.setStatus(novoStatus);
        matriculaRepository.save(matricula);
    }

    public void excluir(Long id) {
        if (!matriculaRepository.existsById(id)) {
            throw new IllegalArgumentException("Matrícula não encontrada com ID: " + id);
        }
        matriculaRepository.deleteById(id);
    }

    // Regras de negócio e validações da matrícula
    private void validar(Matricula matricula) {
        // Validação de aluno obrigatório
        if (matricula.getAluno() == null || matricula.getAluno().getId() == null) {
            throw new IllegalArgumentException("É obrigatório selecionar um aluno.");
        }

        // Validação de curso obrigatório
        if (matricula.getCurso() == null || matricula.getCurso().getId() == null) {
            throw new IllegalArgumentException("É obrigatório selecionar um curso.");
        }

        Aluno aluno = alunoRepository.findById(matricula.getAluno().getId())
                .orElseThrow(() -> new IllegalArgumentException("Aluno selecionado não existe."));

        Curso curso = cursoRepository.findById(matricula.getCurso().getId())
                .orElseThrow(() -> new IllegalArgumentException("Curso selecionado não existe."));

        matricula.setAluno(aluno);
        matricula.setCurso(curso);

        // Data da matrícula padrão (hoje) caso não informada
        if (matricula.getDataMatricula() == null) {
            matricula.setDataMatricula(LocalDate.now());
        }

        // Toda nova matrícula deve iniciar com status ATIVA por padrão
        if (matricula.getStatus() == null) {
            matricula.setStatus(StatusMatricula.ATIVA);
        }

        // Não permite duas matrículas ativas do mesmo aluno no mesmo curso
        if (matricula.getStatus() == StatusMatricula.ATIVA) {
            Optional<Matricula> matriculaAtivaExistente = matriculaRepository.findByAlunoAndCursoAndStatus(
                    aluno, curso, StatusMatricula.ATIVA
            );

            if (matriculaAtivaExistente.isPresent()) {
                Matricula existente = matriculaAtivaExistente.get();
                if (matricula.getId() == null || !existente.getId().equals(matricula.getId())) {
                    throw new IllegalArgumentException("O aluno " + aluno.getNome() + " já possui uma matrícula ATIVA no curso " + curso.getNome() + ".");
                }
            }
        }
    }
}
