package com.bikeunirio.bicicletario.aluguel.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bikeunirio.bicicletario.aluguel.service.AluguelService;
import com.bikeunirio.bicicletario.aluguel.webservice.ExternoService;


@RestController
@RequestMapping("/")
public class AluguelController {
	
    private AluguelService aluguelService;	
	private ExternoService externoService;
    
	public AluguelController(AluguelService aluguelService, ExternoService externoService) {
		this.aluguelService = aluguelService;
		this.externoService = externoService;
	}

	// UC16
	/*
	 * R1 – Devem ser registrados: data/hora da cobrança, valor extra do aluguel, o
	 * cartão usado para cobrança. R2 – Deve ser enviado um e-mail para o ciclista
	 * com todos os dados da devolução da bicicleta referente à cobrança em atraso.
	 * [E1] Erro no pagamento ou pagamento não autorizado.
	 * 
	 * A2.1 O sistema mantém o valor da cobrança extra para permitir ser efetuada
	 * posteriormente. Pré-condições: Devem ter ocorrido devoluções de bicicletas no
	 * último intervalo de 12hs Trigger: Passou-se 12hs desde a última verificação
	 * de taxas atrasadas Fluxo Principal: 1. O sistema identifica o conjunto de
	 * cobranças relativas a taxas extras
	 * 
	 * de aluguel que ainda não foram cobradas. 2. O sistema realiza a cobrança para
	 * cada ciclista em atraso utilizando o cartão de crédito mais recente que
	 * estiver cadastrado para o ciclista (caso ele tenha alterado) [R1][E1]. 3. O
	 * sistema notifica os ciclistas que tiveram cobrança em atraso [R2]. 4. O caso
	 * de uso é encerrado
	 */
    
    // Caso de uso 03
	@PostMapping("/aluguel")
	public void alugarBicicleta() {}

	// Casos de uso UC04 e UC16
	@PostMapping("/devolucao")
	public void devolverBicicleta() {}
	
	@PostMapping("/restaurarBanco")
	public void restaurarBD() { }
}
