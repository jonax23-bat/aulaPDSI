package br.com.escola.mvc.model;

// ==========================================
// [DESAFIO - ENUM STATUS DA MATRÍCULA]
// Representa os estados possíveis de uma matrícula no sistema
// ==========================================
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
