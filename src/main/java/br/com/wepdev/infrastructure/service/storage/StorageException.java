package br.com.wepdev.infrastructure.service.storage;

public class StorageException extends RuntimeException{
    private static final long serialVersionUID = 5181884387119082940L;



    public StorageException(String message, Throwable cause) {
        super(message, cause);
    }

    public StorageException(Throwable cause) {
        super(cause);
    }
}
