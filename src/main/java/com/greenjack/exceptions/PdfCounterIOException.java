package com.greenjack.exceptions;

public class PdfCounterIOException extends RuntimeException {
    public PdfCounterIOException(Exception e) {
        super(e);
    }
}
