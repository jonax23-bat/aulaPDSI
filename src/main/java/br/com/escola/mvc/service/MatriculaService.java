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

// ==========================================
// [DESAFIO - SERVICE DE MATRÍCULA]
// Camada onde são aplicadas todas as regras e restrições de matrícula
// ==========================================
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

    // ==========================================
    // [DESAFIO] REGRAS DE NEGÓCIO E VALIDAÇÕES DA MATRÍCULA
    // ==========================================
    private void validar(Matricula matricula) {
        // Regra 1: Aluno selecionado deve existir
        if (matricula.getAluno() == null || matricula.getAluno().getId() == null) {
            throw new IllegalArgumentException("É obrigatório selecionar um aluno.");
        }

        // Regra 2: Curso selecionado deve existir
        if (matricula.getCurso() == null || matricula.getCurso().getId() == null) {
            throw new IllegalArgumentException("É obrigatório selecionar um curso.");
        }

        Aluno aluno = alunoRepository.findById(matricula.getAluno().getId())
                .orElseThrow(() -> new IllegalArgumentException("Aluno selecionado não existe."));

        Curso curso = cursoRepository.findById(matricula.getCurso().getId())
                .orElseThrow(() -> new IllegalArgumentException("Curso selecionado não existe."));

        matricula.setAluno(aluno);
        matricula.setCurso(curso);

        // Regra 3: Data da matrícula padrão (hoje) caso não informada
        if (matricula.getDataMatricula() == null) {
            matricula.setDataMatricula(LocalDate.now());
        }

        // Regra 4: Toda nova matrícula deve iniciar com status ATIVA por padrão
        if (matricula.getStatus() == null) {
            matricula.setStatus(StatusMatricula.ATIVA);
        }

        // ==========================================
        // [DESAFIO] REGRA PRINCIPAL: NÃO PERMITIR DUAS MATRÍCULAS ATIVAS DO MESMO ALUNO NO MESMO CURSO
        // ==========================================
        if (matricula.getStatus() == StatusMatricula.ATIVA) {
            Optional<Matricula> matriculaAtivaExistente = matriculaRepository.findByAlunoAndCursoAndStatus(
                    aluno, curso, StatusMatricula.ATIVA
            );

            if (matriculaAtivaExistente.isPresent()) {
                Matricula existente = matriculaAtivaExistente.get();
                // Bloqueia se for nova matrícula ou se o ID for de outra matrícula diferente da que está sendo editada
                if (matricula.getId() == null || !existente.getId().equals(matricula.getId())) {
                    throw new IllegalArgumentException("O aluno " + aluno.getNome() + " já possui uma matrícula ATIVA no curso " + curso.getNome() + ".");
                }
            }
        }
    }
}
