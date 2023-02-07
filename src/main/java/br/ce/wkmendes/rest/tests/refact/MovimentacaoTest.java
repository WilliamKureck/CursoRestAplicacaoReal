package br.ce.wkmendes.rest.tests.refact;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import org.junit.Test;

import br.ce.wkmendes.rest.core.BaseTest;
import br.ce.wkmendes.rest.tests.Movimentacao;
import br.ce.wkmendes.rest.utils.AplicacaoRealUtils;
import br.ce.wkmendes.rest.utils.DataUtils;

public class MovimentacaoTest extends BaseTest{
	
	private Movimentacao getMovimentacaoValida() {
		Movimentacao mov = new Movimentacao();
		mov.setConta_id(AplicacaoRealUtils.getIdContaPeloNome("Conta para movimentacoes"));
		mov.setDescricao("Descricao movimentacao teste");
		mov.setEnvolvido("Envolvido na movimentacao");
		mov.setTipo("REC");
		mov.setData_transacao(DataUtils.getDataDiferencaDias(-1));
		mov.setData_pagamento(DataUtils.getDataDiferencaDias(5));
		mov.setValor(100f);
		mov.setStatus(true);
		return mov;
	}
	
	@Test
	public void testDeveInserirMovimentacaoComSucesso() {
		Movimentacao mov = getMovimentacaoValida();
		
		given()
			.body(mov)
		.when()
			.post("/transacoes")
		.then()
			.statusCode(201)
		;
	}
	
	@Test
	public void testNaoDeveInserirMovimentacaoSemCamposObrigatorios() {
		given()
			.body("{}")
		.when()
			.post("/transacoes")
		.then()
			.statusCode(400)
			.body("$", hasSize(8))
			.body("msg", hasItems(
					"Data da Movimentação é obrigatório",
					"Data do pagamento é obrigatório",
					"Descrição é obrigatório",
					"Interessado é obrigatório",
					"Valor é obrigatório",
					"Valor deve ser um número",
					"Conta é obrigatório",
					"Situação é obrigatório"
					))
		;
	}
	
	@Test
	public void testNaoDeveCadastrarMovimentacaoComDataFutura() {
		Movimentacao mov = getMovimentacaoValida();
		mov.setData_transacao(DataUtils.getDataDiferencaDias(2));
		
		given()
			.body(mov)
		.when()
			.post("/transacoes")
		.then()
			.statusCode(400)
			.body("$", hasSize(1))
			.body("param", hasItem("data_transacao"))
			.body("msg", hasItem("Data da Movimentação deve ser menor ou igual à data atual"))
			.body("value", hasItem(mov.getData_transacao()))
		;
	}
	
	@Test
	public void testNaoDeveRemoverContaComMovimentacao() {
		Integer CONTA_ID = AplicacaoRealUtils.getIdContaPeloNome("Conta com movimentacao");
		
		given()
			.pathParam("id", CONTA_ID)
		.when()
			.delete("/contas/{id}")
		.then()
			.statusCode(500)
			.body("constraint", is("transacoes_conta_id_foreign"))
		;
	}
	
	@Test
	public void testDeveRemoverMovimentacao() {
		Integer MOV_ID = AplicacaoRealUtils.getIdMovimentacaoPelaDescricao("Movimentacao para exclusao");
		
		given()
			.pathParam("id", MOV_ID)
		.when()
			.delete("/transacoes/{id}")
		.then()
			.statusCode(204)
		;
	}
}
