package br.com.escola.mvc.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Aluno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Documento de identificação (RG)
    private String rg;

    private String nome;
    private String email;
    private String curso;

    public Aluno() {
    }

    public Aluno(Long id, String rg, String nome, String email, String curso) {
        this.id = id;
        this.rg = rg;
        this.nome = nome;
        this.email = email;
        this.curso = curso;
    }

    public Aluno(String rg, String nome, String email, String curso) {
        this.rg = rg;
        this.nome = nome;
        this.email = email;
        this.curso = curso;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // Getter e Setter de RG
    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        if (rg != null) {
            this.rg = rg.replaceAll("\\D", "");
        } else {
            this.rg = null;
        }
    }

    // Formatação visual do RG (00.000.000-0)
    public String getRgFormatado() {
        if (this.rg == null || this.rg.length() < 9) {
            return this.rg;
        }
        return this.rg.replaceAll("(\\d{2})(\\d{3})(\\d{3})([0-9Xx])", "$1.$2.$3-$4");
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }
}
