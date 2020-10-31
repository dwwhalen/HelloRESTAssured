import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

public class MyTestClass {

    @Test
    public void test_CheckSize() {

        given().
                when().
                get("http://ergast.com/api/f1/2017/circuits.json").
                then().
                assertThat().
                body("MRData.CircuitTable.Circuits.circuitId", hasSize(20));
    }

    @Test
    public void test_CheckResponseHeader() {

        given().
                when().
                get("http://ergast.com/api/f1/2017/circuits.json").
                then().
                assertThat().
                statusCode(200).
                and().
                contentType(ContentType.JSON).
                and().
                header("Content-Length", equalTo("4551"));
    }

    @Test
    public void test_QueryParam() {

        String getCheckSumForThisText = "testyoyoyo";
        String expectedMd5CheckSum = "29fdc257dd69dee1a8d63acba6efb326";

        given().
                param("text", getCheckSumForThisText).
                when().
                get("http://md5.jsontest.com").
                then().
                assertThat().
                body("md5", equalTo(expectedMd5CheckSum));
    }

    @Test
    public void test_PathParam() {

        String season = "2017";
        int numberOfRaces = 20;

        given().
                pathParam("raceSeason", season).
                when().
                get("http://ergast.com/api/f1/{raceSeason}/circuits.json").
                then().
                assertThat().
                body("MRData.CircuitTable.Circuits.circuitId", hasSize(numberOfRaces));
    }

    @Test
    public void test_PassValuesBetweenAPICalls() {

        // First, retrieve the circuit ID for the first circuit of the 2017 season
        String circuitId = given().
                when().
                get("http://ergast.com/api/f1/2017/circuits.json").
                then().
                extract().
                path("MRData.CircuitTable.Circuits.circuitId[0]");

        // Then, retrieve the information known for that circuit and verify it is located in Australia
        given().
                pathParam("circuitId", circuitId).
                when().
                get("http://ergast.com/api/f1/circuits/{circuitId}.json").
                then().
                assertThat().
                body("MRData.CircuitTable.Circuits.Location[0].country", equalTo("Australia"));
    }

    ResponseSpecification checkStatusCodeAndContentType =
            new ResponseSpecBuilder().
                    expectStatusCode(200).
                    expectContentType(ContentType.JSON).
                    build();

    @Test
    public void test_CodeReuseWithResponseSpec() {

        given().
                when().
                get("http://ergast.com/api/f1/2017/circuits.json").
                then().
                assertThat().
                spec(checkStatusCodeAndContentType).
                and().
                body("MRData.CircuitTable.Circuits.circuitId", hasSize(20));
    }

    @Test
    public void test_LogAllResponses() {

        given().
                when().
                get("http://ergast.com/api/f1/2020/circuits.json").
                then().
                log().body();
    }

    @Test
    public void test_LogIfValidationFails() {

        given().
                when().
                get("http://ergast.com/api/f1/2020/circuits.json").
                then().
                log().ifValidationFails().
                statusCode(123);
    }
}
