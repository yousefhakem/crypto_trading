package com.cryptotrading.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/** Thrown when a user attempts to create a second wallet for an asset they already own. */
@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateWalletException extends RuntimeException {

    public DuplicateWalletException(String message) {
        super(message);
    }
}
