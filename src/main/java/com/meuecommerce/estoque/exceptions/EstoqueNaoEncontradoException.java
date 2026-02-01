package com.meuecommerce.estoque.exceptions;

public class EstoqueNaoEncontradoException extends RuntimeException {
    public EstoqueNaoEncontradoException(String mensagem) {
        super(mensagem);
    }

    public EstoqueNaoEncontradoException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
