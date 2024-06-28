package com.uade.tpo.ecommerce.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN, reason = "No tiene privilegios suficientes para realizar esta acción")
public class InsufficientPrivilegesException extends Exception {
}