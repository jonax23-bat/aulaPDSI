package br.com.escola.mvc.model;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

// Entidade de associação entre Aluno e Curso
@Entity
@Table(name = "matriculas")
public class Matricula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relacionamento com Aluno
    @ManyToOne(optional = false)
    @JoinColumn(name = "aluno_id", nullable = false)
    private Aluno aluno;

    // Relacionamento com Curso
    @ManyToOne(optional = false)
    @JoinColumn(name = "curso_id", nullable = false)
    private Curso curso;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataMatricula;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusMatricula status = StatusMatricula.ATIVA;

    public Matricula() {
        this.dataMatricula = LocalDate.now();
        this.status = StatusMatricula.ATIVA;
    }

    public Matricula(Long id, Aluno aluno, Curso curso, LocalDate dataMatricula, StatusMatricula status) {
        this.id = id;
        this.aluno = aluno;
        this.curso = curso;
        this.dataMatricula = dataMatricula != null ? dataMatricula : LocalDate.now();
        this.status = status != null ? status : StatusMatricula.ATIVA;
    }

    public Matricula(Aluno aluno, Curso curso, LocalDate dataMatricula, StatusMatricula status) {
        this.aluno = aluno;
        this.curso = curso;
        this.dataMatricula = dataMatricula != null ? dataMatricula : LocalDate.now();
        this.status = status != null ? status : StatusMatricula.ATIVA;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public LocalDate getDataMatricula() {
        return dataMatricula;
    }

    public void setDataMatricula(LocalDate dataMatricula) {
        this.dataMatricula = dataMatricula;
    }

    public StatusMatricula getStatus() {
        return status;
    }

    public void setStatus(StatusMatricula status) {
        this.status = status;
    }
}
