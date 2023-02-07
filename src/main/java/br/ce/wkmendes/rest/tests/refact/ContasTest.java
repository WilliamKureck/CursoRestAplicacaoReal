package br.ce.wkmendes.rest.tests.refact;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import org.junit.Test;

import br.ce.wkmendes.rest.core.BaseTest;
import br.ce.wkmendes.rest.utils.AplicacaoRealUtils;

public class ContasTest extends BaseTest{
	
	@Test
	public void testDeveIncluirContaComSucesso() {
		given()
			.body("{ \"nome\": \" Conta Inserida\" }")
		.when()
			.post("/contas")
		.then()
			.statusCode(201)
		;
	}
	
	@Test
	public void testDeveAlterarContaComSucesso() {
		Integer CONTA_ID = AplicacaoRealUtils.getIdContaPeloNome("Conta para alterar");
		given()
			.body("{ \"nome\": \"Conta alterada\" }")
			.pathParam("id", CONTA_ID)
		.when()
			.put("/contas/{id}")
		.then()
			.statusCode(200)
			.body("nome", is("Conta alterada"))
		;
	}
	
	@Test
	public void testNaoDeveIncluirContaComSucesso() {
		given()
			.body("{ \"nome\": \"Conta mesmo nome\" }")
		.when()
			.post("/contas")
		.then()
			.statusCode(400)
			.body("error", is("Já existe uma conta com esse nome!"))
		;
	}
	
}
