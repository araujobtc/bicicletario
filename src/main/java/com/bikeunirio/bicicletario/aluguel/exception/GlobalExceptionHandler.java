package com.bikeunirio.bicicletario.aluguel.exception;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.bikeunirio.bicicletario.aluguel.dto.ErroResposta;

@RestControllerAdvice // Torna este handler global para todos os controllers
public class GlobalExceptionHandler {
	
	private static String DADOS_INVALIDOS = "DADOS_INVALIDOS";

	/*
	 * O método com @ExceptionHandler(MethodArgumentNotValidException.class) será
	 * chamado automaticamente sempre que qualquer controller lançar essa exceção
	 * (quando o @Valid falhar). Assim, não precisa colocar o método
	 * handleValidationExceptions dentro de cada controller.
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public static ResponseEntity<List<ErroResposta>> handleValidationExceptions(MethodArgumentNotValidException ex) {
		//422
		return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(ex.getBindingResult().getFieldErrors()
				.stream().map(e -> new ErroResposta(DADOS_INVALIDOS, e.getDefaultMessage())).toList());
	}
	
	/*
	 * O método com @ExceptionHandler(MethodArgumentNotValidException.class) será
	 * chamado automaticamente sempre que qualquer parametro receber um valor de
	 * tipo invalido.
	 * Usado na validação do parametro
	 */
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public static ResponseEntity<ErroResposta> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
		//422
		ErroResposta erro = new ErroResposta(DADOS_INVALIDOS, "O valor do parâmetro '" + ex.getName() + "' é inválido.");
	    
	    return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(erro);
	}

}
