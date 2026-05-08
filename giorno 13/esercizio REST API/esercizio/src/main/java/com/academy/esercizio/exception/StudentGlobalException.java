package com.academy.esercizio.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StudentGlobalException {

    private String message;
    private int id;
    private String timestamp;
    
}
