import io.restassured.RestAssured;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.response.Response;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import top.jfunc.json.impl.JSONObject;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class MyTestClass {

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 3000;
    }

    @Test
    public void checkResponseSize() {
        // @formatter:off
        given().
        when().
                get("albums").
        then().
                assertThat().
                body("size()", is(12));
        // @formatter:off
    }

    @Test
    public void checkResponseHeader() {
        // @formatter:off
        given().
        when().
                get("albums").
        then().
                assertThat().
                statusCode(200).
        and().
                contentType(ContentType.JSON);
        // @formatter:on
    }

    @Test
    public void queryParamExample() {

        Integer expectedId = 2;
        String titleToGet = "With the Beatles";

        // @formatter:off
        given().
                param("title", titleToGet).
        when().
                get("albums").
        then().
                assertThat().
                body("id[0]", equalTo(expectedId));
        // @formatter:on
    }

    @Test
    public void pathParamExample() {

        String idToGet = "4";
        String expectedTitle = "Beatles for Sale";
        // @formatter:off
        given().
                pathParam("id", idToGet).
        when().
                get("albums/{id}").
        then().
                assertThat().
                body("title", equalTo(expectedTitle));
        // @formatter:on
    }

    @Test
    public void extractDataAndPassToNextAPICall() {

        // @formatter:off
        Response response = given().
                            when().
                                get("albums").
                            then().
                                extract().
                                    response();
        String validId = response.jsonPath().getString("id[0]");
        String validTitle = response.jsonPath().getString("title[0]");

        given().
                pathParam("id", validId).
        when().
                get("albums/{id}").
        then().
                assertThat().
                body("title", equalTo(validTitle));
        // @formatter:on
    }

    @Test
    public void codeReuseWithResponseSpec() {

        // @formatter:off
        given().
        when().
                get("albums/").
        then().
                assertThat().
                spec(checkStatusCodeAndContentType);
        // @formatter:on
    }

    ResponseSpecification checkStatusCodeAndContentType =
            new ResponseSpecBuilder().
                    expectStatusCode(200).
                    expectContentType(ContentType.JSON).
                    build();

    @Test
    public void logAllResponses() {

        // @formatter:off
        given().
        when().
                get("albums/").
        then().
                log().body();
        // @formatter:on
    }

    @Test
    public void logIfValidationFails() {

        // @formatter:off
        given().
        when().
                get("albums/").
        then().
                log().ifValidationFails().
                statusCode(200);
        // @formatter:on
    }

    @Test
    public void postNewAlbumThenDelete() {

        Header acceptJson = new Header("Accept", "application/json");

        JSONObject requestParams = new JSONObject();
        requestParams.put("artist", "The Beatles");
        requestParams.put("title", "A Hard Day's Night");
        requestParams.put("year", "1964");

        // @formatter:off
        //add the new album
        Response response =
                given().
                    contentType(ContentType.JSON).
                    body(requestParams.toString()).
                when().
                    post("/albums").
                then().
                    statusCode(201).
                    body("$", hasKey("id")).
                    body("title",equalTo("A Hard Day's Night")).
                    body("year",equalTo("1964")).
                    extract().response();

        //delete album that was just added
        given().
            contentType(ContentType.JSON).
            body(requestParams.toString()).
        when().
            delete("/albums/" + response.jsonPath().getInt("id")).
        then().
            statusCode(200);

        //try to get the album we just deleted
        given().
        when().
            get("/albums/" + response.jsonPath().getInt("id")).
       then().
            statusCode(404);

        // @formatter:on
    }
}
