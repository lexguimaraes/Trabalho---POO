package com.alex.exception;

public class ImpossivelCancelarPedidoException extends RuntimeException {
    public ImpossivelCancelarPedidoException(String message) {
        super(message);
    }
}
