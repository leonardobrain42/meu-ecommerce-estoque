package com.meuecommerce.estoque.exceptions;

public class QuantidadeInsuficienteException extends RuntimeException {
    public QuantidadeInsuficienteException(String mensagem) {
        super(mensagem);
    }

    public QuantidadeInsuficienteException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
