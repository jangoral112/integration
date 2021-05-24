package edu.iis.mto.blog.rest.test;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class GetUserTest extends FunctionalTests {

    private static final String USER_API = "/blog/user";

    private static final String FIND_USER_ENDPOINT = USER_API + "/find";

    @Test
    void getUserReturnsNotFoundWhenUserWithGivenIdIsNotPresent() {
        given().accept(ContentType.JSON)
                .pathParam("id", -1)
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .when()
                .get(USER_API + "/{id}");
    }

    @Test
    void findUserByEmailReturnsUser() {
        Response response = given().accept(ContentType.JSON)
                .queryParam("searchString", "john@domain.com")
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_OK)
                .when()
                .get(FIND_USER_ENDPOINT);

        JSONArray responseBody = new JSONArray(response.getBody().asString());
        assertThat(responseBody.length(), equalTo(1));
    }

    @Test
    void findUserByFirstNameReturnsUser() {
        Response response = given().accept(ContentType.JSON)
                .queryParam("searchString", "John")
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_OK)
                .when()
                .get(FIND_USER_ENDPOINT);

        JSONArray responseBody = new JSONArray(response.getBody().asString());
        assertThat(responseBody.length(), equalTo(1));
    }

    @Test
    void findUserBySecondNameReturnsUser() {
        Response response = given().accept(ContentType.JSON)
                .queryParam("searchString", "Steward")
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_OK)
                .when()
                .get(FIND_USER_ENDPOINT);

        JSONArray responseBody = new JSONArray(response.getBody().asString());
        assertThat(responseBody.length(), equalTo(1));
    }

    @Test
    void findUserByEmailDoesNotReturnsRemovedUser() {
        Response response = given().accept(ContentType.JSON)
                .queryParam("searchString", "removed@domain.com")
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_OK)
                .when()
                .get(FIND_USER_ENDPOINT);

        JSONArray responseBody = new JSONArray(response.getBody().asString());
        assertThat(responseBody.length(), equalTo(0));
    }
}
