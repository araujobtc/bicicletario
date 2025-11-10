package com.bikeunirio.bicicletario.aluguel.dto;

public class ErroResposta {
    private String codigo;
    private String mensagem;

    public ErroResposta(String codigo, String mensagem) {
        this.codigo = codigo;
        this.mensagem = mensagem;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getMensagem() {
        return mensagem;
    }
}
