package com.cryptotrading.exception;

public class DuplicateAssetException extends RuntimeException {
    public DuplicateAssetException() {
        super("This asset already exists!");
    }
}
