package br.com.wepdev.infrastructure.service.email;

public class EmailException extends RuntimeException{
    private static final long serialVersionUID = 5181884387119082940L;



    public EmailException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmailException(Throwable cause) {
        super(cause);
    }
}
