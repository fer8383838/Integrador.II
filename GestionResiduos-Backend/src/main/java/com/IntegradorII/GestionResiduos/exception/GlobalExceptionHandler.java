



package com.IntegradorII.GestionResiduos.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice  // Esta clase se activa automáticamente cuando ocurre cualquier excepción en tus controladores
public class GlobalExceptionHandler {

    // Captura errores internos inesperados no manejados por try-catch (ej: NullPointer, fallo en BD, etc.)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAllExceptions(Exception ex) {
        ex.printStackTrace(); // Imprime la traza completa en la consola (IntelliJ)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error interno del servidor: " + ex.getMessage());
    }

    // Captura errores cuando el JSON enviado es inválido o faltan campos
    // Por ejemplo: si mandas un campo string vacío cuando se esperaba un número
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleBadJson(HttpMessageNotReadableException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("El formato del JSON es inválido o faltan campos obligatorios.");
    }

    // Captura errores de validación cuando usas @Valid en el controlador
    // Devuelve el primer mensaje de error personalizado si el DTO falla en su validación
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidation(MethodArgumentNotValidException ex) {
        String errorMsg = ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Validación fallida: " + errorMsg);
    }
}



