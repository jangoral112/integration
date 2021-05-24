package edu.iis.mto.blog.rest.test;

import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class CreatePostTest extends FunctionalTests {

    private static final String CREATE_POST_API = "/blog/user/{userid}/post";

    @Test
    void createPostByNEWUserReturnsBadRequestStatus() {
        JSONObject jsonObj = new JSONObject().put("entry", "sample entry");
        given().accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .pathParam("userid", 2)
                .body(jsonObj.toString())
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .when()
                .post(CREATE_POST_API);
    }
}