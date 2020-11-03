import io.restassured.RestAssured;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class MyTestClass {

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 3000;
    }

    @Test
    public void checkResponseSize() {

        given().
                when().
                get("albums").
                then().
                assertThat().
                body("size()", is(12));
    }

    @Test
    public void checkResponseHeader() {

        given().
                when().
                get("albums").
                then().
                assertThat().
                statusCode(200).
                and().
                contentType(ContentType.JSON).
                and().
                header("Content-Encoding", equalTo("gzip"));
    }

    @Test
    public void queryParamExample() {

        String idToGet = "10";
        String expectedTitle = "Yellow Submarine";

        given().
                param("id", idToGet).
                when().
                get("albums").
                then().
                assertThat().
                body("title[0]", equalTo(expectedTitle));
    }

    @Test
    public void pathParamExample() {

        String idToGet = "4";
        String expectedTitle = "Beatles for Sale";

        given().
                pathParam("id", idToGet).
                when().
                get("albums/{id}").
                then().
                assertThat().
                body("title", equalTo(expectedTitle));
    }

    @Test
    public void extractDataAndPassToNextAPICall() {

        Integer albumId = given().
                when().
                get("albums").
                then().
                extract().
                path("id[0]");

        given().
                pathParam("id", albumId.toString()).
                when().
                get("albums/{id}").
                then().
                assertThat().
                body("title", equalTo("Please Please Me"));
    }

    @Test
    public void codeReuseWithResponseSpec() {

        given().
                when().
                get("albums/").
                then().
                assertThat().
                spec(checkStatusCodeAndContentType).
                and().
                body("size()", is(12));
    }

    ResponseSpecification checkStatusCodeAndContentType =
            new ResponseSpecBuilder().
                    expectStatusCode(200).
                    expectContentType(ContentType.JSON).
                    build();

    @Test
    public void logAllResponses() {

        given().
                when().
                get("albums/").
                then().
                log().body();
    }

    @Test
    public void logIfValidationFails() {

        given().
                when().
                get("albums/").
                then().
                log().ifValidationFails().
                statusCode(200);
    }
}
