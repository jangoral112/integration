package edu.iis.mto.blog.rest.test;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class GetPostTest extends FunctionalTests {

    private final static String GET_POST_BY_USER_ENDPOINT = "/blog/user/{id}/post";

    private final static String GET_POST_ENDPOINT = "/blog/post/{id}";

    @Test
    void getPostsReturnsPostMadeByUserWithGivenId() {
        Response response = given().accept(ContentType.JSON)
                .pathParams("id", 3)
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_OK)
                .when()
                .get(GET_POST_BY_USER_ENDPOINT);

        JSONArray responseBody = new JSONArray(response.getBody().asString());
        assertThat(responseBody.length(), equalTo(2));
    }

    @Test
    void getPostWhenUserIsRemovedReturnsBadRequestStatus()  {
        given().accept(ContentType.JSON)
                .pathParams("id", 5)
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .when()
                .get(GET_POST_BY_USER_ENDPOINT);
    }

    @Test
    void getPostReturnsCorrectLikePostCount() {
        Response response = given().accept(ContentType.JSON)
                .pathParams("id", 1)
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_OK)
                .when()
                .get(GET_POST_ENDPOINT);

        JSONObject responseBody = new JSONObject(response.getBody().asString());
        assertThat(responseBody.getInt("likesCount"), equalTo(1));
    }

}
