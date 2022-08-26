import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class Mesto1Test {

    String bearerToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJfaWQiOiI2MmJkOTg1N2QzYjg2YTAwM2Q2N2M4NGMiLCJpYXQiOjE2NjE1MjQ2OTYsImV4cCI6MTY2MjEyOTQ5Nn0.Fszda3rZvBPha22qd07uCphLeNVpefslCJcLCWxNEco";

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-mesto.praktikum-services.ru";
    }
    @Test
    @DisplayName("Check user name")
    @Description("This test is for check current user's name.")
    public void checkUserName() {
        given()
                .auth().oauth2(bearerToken) // ������� ����� ��� ��������������
                .get("/api/users/me") // ������ GET-������
                .then().assertThat().body("data.name", equalTo("Incorrect Name")); // ���������, ��� ��� ������������� ����������
    }

    @Test
    @DisplayName("Add a new photo")
    @Description("This test is for adding a new photo to Mesto.")
    public void addNewPhoto() {
        given()
                .header("Content-type", "application/json") // ������� Content-type � ��������� ��� �������� ���� �����
                .auth().oauth2(bearerToken) // ������� ����� ��� ��������������
                .body("{\"name\":\"������\",\"link\":\"https://code.s3.yandex.net/qa-automation-engineer/java/files/paid-track/sprint1/photoSelenium.jpg\"}") // ��������� ���� �������
                .post("/api/cards") // ������ POST-������
                .then().statusCode(201); // ��������� ��� ������
    }

    @Test
    @DisplayName("Like the first photo")
    @Description("This test is for liking the first photo on Mesto.")
    public void likeTheFirstPhoto() {
        String photoId = getTheFirstPhotoId();

        likePhotoById(photoId);
        deleteLikePhotoById(photoId);
    }

    @Step("Take the first photo from the list")
    private String getTheFirstPhotoId() {
        // ��������� ������ ���������� � ����� ������ �� ����
        return given()
                .auth().oauth2(bearerToken) // ������� ����� ��� ��������������
                .get("/api/cards") // ������ GET-������
                .then().extract().body().path("data[0]._id"); // �������� ID ���������� �� ������� ������
    }

    @Step("Like a photo by id")
    private void likePhotoById(String photoId) {
        // ���� ���������� �� photoId
        given()
                .auth().oauth2(bearerToken) // ������� ����� ��� ��������������
                .put("/api/cards/{photoId}/likes", photoId) // ������ PUT-������
                .then().assertThat().statusCode(200); // ���������, ��� ������ ������ ��� 200
    }

    @Step("Delete like from the photo by id")
    private void deleteLikePhotoById(String photoId) {
        // ����� ���� � ���������� �� photoId
        given()
                .auth().oauth2(bearerToken) // ������� ����� ��� ��������������
                .delete("/api/cards/{photoId}/likes", photoId) // ������ DELETE-������
                .then().assertThat().statusCode(200); // ���������, ��� ������ ������ ��� 200
    }

}
