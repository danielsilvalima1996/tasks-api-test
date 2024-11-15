package apitest;

import org.hamcrest.CoreMatchers;
import org.junit.BeforeClass;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class APITest {

    @BeforeClass
    public static void setup() {
        RestAssured.baseURI = "http://localhost:8001/tasks-backend";
    }

    @Test
    public void deveRetornarTarefas() {
        RestAssured.given()
                .when()
                .get("/todo")
                .then()
                .statusCode(200);
    }

    @Test
    public void deveAdicionarTarefaComSucesso() {
        String dataAtual = LocalDate.now().format(DateTimeFormatter.ISO_DATE);
        String json = "{\"task\": \"Teste API\", \"dueDate\": \"" + dataAtual + "\"}";
        RestAssured.given()
                .body(json)
                .contentType(ContentType.JSON)
                .when()
                .post("/todo")
                .then()
                .statusCode(201);
    }

    @Test
    public void naoDeveAdicionarTarefaInvalida() {
        RestAssured.given()
                .body("{\"task\": \"Teste API\", \"dueDate\": \"2020-10-02\"}")
                .contentType(ContentType.JSON)
                .when()
                .post("/todo")
                .then()
                .log().all()
                .statusCode(400)
                .body("message", CoreMatchers.is("Due date must not be in past"));
    }

    @Test
    public void deveRemoverTarefaComSucesso() {
        String dataAtual = LocalDate.now().format(DateTimeFormatter.ISO_DATE);
        String json = "{\"task\": \"Teste Test\", \"dueDate\": \"" + dataAtual + "\"}";
        int id = RestAssured.given()
                .body(json)
                .contentType(ContentType.JSON)
                .when()
                .post("/todo")
                .then()
                .statusCode(201)
                .extract()
                .path("id");

        // remover
        RestAssured.given()
                .when()
                .delete("/todo/" + id)
                .then()
                .statusCode(204);



    }

}
