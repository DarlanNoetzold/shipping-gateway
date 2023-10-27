package tech.noetzold;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import io.quarkus.test.security.TestSecurity;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import tech.noetzold.controller.ShippingController;
import tech.noetzold.model.AddressModel;
import tech.noetzold.model.ShippingModel;
import tech.noetzold.model.enums.ShippingMethod;
import tech.noetzold.model.enums.State;


import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static io.restassured.RestAssured.given;

@QuarkusTest
@TestHTTPEndpoint(ShippingController.class)
public class ShippingControllerTest {

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
    public void testGetShippingModelById() {
        given()
                .header("Authorization", "Bearer " + accessToken)
                .contentType(ContentType.JSON)
                .when()
                .get("http://localhost:6000/api/shipping/v1/shipping/{id}", "57c54c33-88b3-4e99-bb09-5dbf91e6e186")
                .then()
                .statusCode(200);
    }

    @Test
    @Order(2)
    @TestSecurity(user = "admin", roles = {"admin"})
    public void testGetShippingModelByOrderId() {
        given()
                .header("Authorization", "Bearer " + accessToken)
                .contentType(ContentType.JSON)
                .when()
                .get("http://localhost:6000/api/shipping/v1/shipping/order/{orderId}", "orderId")
                .then()
                .statusCode(200);
    }

    @Test
    @Order(3)
    @TestSecurity(user = "admin", roles = {"admin"})
    public void testSaveShippingModel() {
        ShippingModel shippingModel = new ShippingModel();
        shippingModel.setCarrierCode("12345");
        shippingModel.setShippingMethod(ShippingMethod.FAST_SHIPPING);
        shippingModel.setState(State.INIT);
        shippingModel.setUserId("user-id-here");
        shippingModel.setOrderId("order-id-here");
        AddressModel addressModel = new AddressModel();
        addressModel.setAddressId(UUID.fromString("9a5c9b4a-3b88-4a37-94c8-634c25de87e1"));
        shippingModel.setAddressModel(addressModel);
        shippingModel.setTrackingUrl("https://example.com/tracking");
        // Configure outros campos conforme necessário.

        given()
                .header("Authorization", "Bearer " + accessToken)
                .contentType(ContentType.JSON)
                .body(shippingModel)
                .when()
                .post("http://localhost:6000/api/shipping/v1/shipping")
                .then()
                .statusCode(201);
    }

    @Test
    @Order(4)
    @TestSecurity(user = "admin", roles = {"admin"})
    public void testUpdateShippingModel() {
        ShippingModel updatedShippingModel = new ShippingModel();
        updatedShippingModel.setCarrierCode("54321");
        updatedShippingModel.setShippingMethod(ShippingMethod.FAST_SHIPPING);
        updatedShippingModel.setState(State.INIT);
        updatedShippingModel.setUserId("userId");
        updatedShippingModel.setOrderId("orderId");
        AddressModel addressModel = new AddressModel();
        addressModel.setAddressId(UUID.fromString("9a5c9b4a-3b88-4a37-94c8-634c25de87e1"));
        updatedShippingModel.setAddressModel(addressModel);
        updatedShippingModel.setTrackingUrl("https://example.com/new-tracking");
        // Configure outros campos conforme necessário.

        given()
                .header("Authorization", "Bearer " + accessToken)
                .contentType(ContentType.JSON)
                .body(updatedShippingModel)
                .when()
                .put("http://localhost:6000/api/shipping/v1/shipping/{id}", "57c54c33-88b3-4e99-bb09-5dbf91e6e186")
                .then()
                .statusCode(200);
    }

    @Test
    @Order(5)
    @TestSecurity(user = "admin", roles = {"admin"})
    public void testDeleteShippingModel() {
        given()
                .header("Authorization", "Bearer " + accessToken)
                .contentType(ContentType.JSON)
                .when()
                .delete("http://localhost:6000/api/shipping/v1/shipping/{id}", "c6fca2e5-178c-488e-aa7e-cb276c1b7ce9")
                .then()
                .statusCode(200);
    }
}
