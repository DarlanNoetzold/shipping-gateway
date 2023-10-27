package tech.noetzold;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import io.quarkus.test.security.TestSecurity;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import tech.noetzold.controller.AddressController;
import tech.noetzold.model.AddressModel;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

@QuarkusTest
@TestHTTPEndpoint(AddressController.class)
public class AddressControllerTest {

    private String accessToken;

    @BeforeEach
    public void obtainAccessToken() {
        final String username = "admin";
        final String password = "admin";

        final String tokenEndpoint = "http://localhost:8180/realms/quarkus1/protocol/openid-connect/token";

        final Map<String, String> requestData = new HashMap<>();
        requestData.put("username", username);
        requestData.put("password", password);
        requestData.put("grant_type", "password");

        final Response response = given()
                .auth().preemptive().basic("backend-service", "secret")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .formParams(requestData)
                .when()
                .post(tokenEndpoint);

        response.then().statusCode(200);

        this.accessToken = response.jsonPath().getString("access_token");
    }

    @Test
    @Order(1)
    @TestSecurity(user = "admin", roles = {"admin"})
    public void testGetAddressModelById() {
        given()
                .header("Authorization", "Bearer " + accessToken)
                .contentType(ContentType.JSON)
                .when()
                .get("http://localhost:6000/api/shipping/v1/address/{id}", "9a5c9b4a-3b88-4a37-94c8-634c25de87e1")
                .then()
                .statusCode(200);
    }

    @Test
    @Order(2)
    @TestSecurity(user = "admin", roles = {"admin"})
    public void testGetAddressModelByUserId() {
        given()
                .header("Authorization", "Bearer " + accessToken)
                .contentType(ContentType.JSON)
                .when()
                .get("http://localhost:6000/api/shipping/v1/address/user/{userId}", "userId")
                .then()
                .statusCode(200);
    }

    @Test
    @Order(3)
    @TestSecurity(user = "admin", roles = {"admin"})
    public void testSaveAddressModel() {
        AddressModel addressModel = new AddressModel();
        addressModel.setPostaCode("12345");
        addressModel.setAddress1("123 Main St");
        addressModel.setAddress2("Apt 101");
        addressModel.setCity("City");
        addressModel.setUf("State");
        addressModel.setCountry("Country");
        addressModel.setFirstName("John");
        addressModel.setLastName("Doe");
        addressModel.setFinalShippingCost(10.0);
        addressModel.setPhoneNumber("555-123-4567");
        addressModel.setDeliveryDate(new Date());
        addressModel.setRiskArea(true);
        addressModel.setUserId("user-id-here");

        given()
                .header("Authorization", "Bearer " + accessToken)
                .contentType(ContentType.JSON)
                .body(addressModel)
                .when()
                .post("http://localhost:6000/api/shipping/v1/address")
                .then()
                .statusCode(201);
    }

    @Test
    @Order(4)
    @TestSecurity(user = "admin", roles = {"admin"})
    public void testUpdateAddressModel() {
        AddressModel updatedAddressModel = new AddressModel();
        updatedAddressModel.setPostaCode("54321");
        updatedAddressModel.setAddress1("456 Elm St");
        updatedAddressModel.setAddress2("Apt 202");
        updatedAddressModel.setCity("New City");
        updatedAddressModel.setUf("New State");
        updatedAddressModel.setCountry("New Country");
        updatedAddressModel.setFirstName("Jane");
        updatedAddressModel.setLastName("Smith");
        updatedAddressModel.setFinalShippingCost(15.0);
        updatedAddressModel.setPhoneNumber("555-987-6543");
        updatedAddressModel.setDeliveryDate(new Date());
        updatedAddressModel.setRiskArea(false);
        updatedAddressModel.setUserId("user-id-here");

        given()
                .header("Authorization", "Bearer " + accessToken)
                .contentType(ContentType.JSON)
                .body(updatedAddressModel)
                .when()
                .put("http://localhost:6000/api/shipping/v1/address/{id}", "9a5c9b4a-3b88-4a37-94c8-634c25de87e1")
                .then()
                .statusCode(200);
    }

    @Test
    @Order(5)
    @TestSecurity(user = "admin", roles = {"admin"})
    public void testDeleteAddressModel() {
        given()
                .header("Authorization", "Bearer " + accessToken)
                .contentType(ContentType.JSON)
                .when()
                .delete("http://localhost:6000/api/shipping/v1/address/{id}", "b7431e27-38a9-4b8d-87ae-85cda2f106d5")
                .then()
                .statusCode(200);
    }
}
