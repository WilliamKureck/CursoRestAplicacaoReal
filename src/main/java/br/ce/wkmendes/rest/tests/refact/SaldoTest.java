package br.ce.wkmendes.rest.tests.refact;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import org.junit.Test;

import br.ce.wkmendes.rest.core.BaseTest;
import br.ce.wkmendes.rest.utils.AplicacaoRealUtils;

public class SaldoTest extends BaseTest{
	
	@Test
	public void testDeveCalcularSaldoContas() {
		Integer CONTA_ID = AplicacaoRealUtils.getIdContaPeloNome("Conta para saldo");
		
		given()
		.when()
			.get("/saldo")
		.then()
			.statusCode(200)
			.body("find{it.conta_id == "+CONTA_ID+"}.saldo", is("534.00"))
		;
	}
}
