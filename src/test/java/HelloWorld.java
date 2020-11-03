import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class HelloWorld {

    @Test
    public void checkEmployeeSize() {

        given().
                when().
                get("http://dummy.restapiexample.com/api/v1/employees").
                then().
                assertThat().
                statusCode(200);
    }
}
