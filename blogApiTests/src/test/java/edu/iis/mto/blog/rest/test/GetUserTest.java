package edu.iis.mto.blog.rest.test;

import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class GetUserTest extends FunctionalTests {

    private static final String USER_API = "/blog/user";

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
}
