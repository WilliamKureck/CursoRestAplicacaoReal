package br.ce.wkmendes.rest.tests.refact.suite;

import static io.restassured.RestAssured.given;

import java.util.HashMap;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

import br.ce.wkmendes.rest.core.BaseTest;
import br.ce.wkmendes.rest.tests.refact.AuthTest;
import br.ce.wkmendes.rest.tests.refact.ContasTest;
import br.ce.wkmendes.rest.tests.refact.MovimentacaoTest;
import br.ce.wkmendes.rest.tests.refact.SaldoTest;
import io.restassured.RestAssured;

@RunWith(org.junit.runners.Suite.class)
@SuiteClasses({
	ContasTest.class,
	MovimentacaoTest.class,
	SaldoTest.class,
	AuthTest.class
})
public class Suite extends BaseTest {
	
	@BeforeClass
	public static void login() {
		Map<String, String> login = new HashMap<String, String>();
		login.put("email", "emailtestewilliam@emailteste.com");
		login.put("senha", "0123456789");
		
		//Login na api e obtenção do token
		String TOKEN = given()
			.body(login)
		.when()
			.post("/signin")
		.then()
			.statusCode(200)
			.extract().path("token")
		;
		
		RestAssured.requestSpecification.header("Authorization", "JWT " + TOKEN);
		
		RestAssured.get("/reset").then().statusCode(200);
	}
}
