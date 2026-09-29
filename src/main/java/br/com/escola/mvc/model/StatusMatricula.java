package br.com.escola.mvc.model;

// Estados possíveis de uma matrícula
public enum StatusMatricula {
    ATIVA("Ativa"),
    TRANCADA("Trancada"),
    CONCLUIDA("Concluída");

    private final String descricao;

    StatusMatricula(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
