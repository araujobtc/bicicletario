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
	private static String NAO_ENCONTRADO = "NAO_ENCONTRADO";
	/*
	 * O método com @ExceptionHandler(MethodArgumentNotValidException.class) será
	 * chamado automaticamente sempre que qualquer controller lançar essa exceção
	 * (quando o @Valid falhar). Assim, não precisa colocar o método
	 * handleValidationExceptions dentro de cada controller.
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<List<ErroResposta>> handleValidationExceptions(MethodArgumentNotValidException ex) {
	    List<ErroResposta> erros = ex.getBindingResult().getFieldErrors()
	            .stream()
	            .map(e -> new ErroResposta(DADOS_INVALIDOS, e.getDefaultMessage()))
	            .toList();

	    // Retorna 422 com lista de erros
	    return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(erros);
	}

	/*
	 * O método com @ExceptionHandler(MethodArgumentNotValidException.class) será
	 * chamado automaticamente sempre que qualquer parametro receber um valor de
	 * tipo invalido.
	 * Usado na validação do parametro
	 */
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<ErroResposta> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
		//422
	    return unprocessableEntity("O valor do parâmetro '" + ex.getName() + "' é inválido.");
	}
	

    public static ResponseEntity<ErroResposta> notFound(String mensagem) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErroResposta(NAO_ENCONTRADO, mensagem));
    }
    
    public static ResponseEntity<ErroResposta> unprocessableEntity(String mensagem) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(new ErroResposta(DADOS_INVALIDOS, mensagem));
    }

}
