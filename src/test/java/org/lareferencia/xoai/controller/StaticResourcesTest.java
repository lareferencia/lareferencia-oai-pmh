package org.lareferencia.xoai.controller;

import org.junit.jupiter.api.Test;
import org.lareferencia.xoai.app.MainApp;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

@SpringBootTest(classes = MainApp.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "xoai.config.path=config/application.properties.model",
                "xoai.static.path=static"
        })
class StaticResourcesTest {

    @LocalServerPort
    private int port;

    @Test
    void servesBrowserPresentationAssets() {
        assertAsset("/xslt/style.xsl", "xml");
        assertAsset("/css/style.css", "css");
        assertAsset("/js/jquery.js", "javascript");
        assertAsset("/img/logo-referencia.png", "png");
        assertAsset("/fonts/glyphicons-halflings-regular.woff", "font");
    }

    private void assertAsset(String path, String expectedContentType) {
        given().port(port)
                .when().get(path)
                .then().statusCode(200)
                .header("Content-Type", containsString(expectedContentType));
    }
}
