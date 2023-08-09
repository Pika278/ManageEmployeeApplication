package com.example.demo.exception;

public class EmailExistedException extends RuntimeException{
    public EmailExistedException(String messsage) {
        super(messsage);
    }
}
