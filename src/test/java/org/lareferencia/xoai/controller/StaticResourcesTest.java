package org.lareferencia.xoai.controller;

import org.junit.jupiter.api.Test;
import org.lareferencia.xoai.app.MainApp;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;

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

    @Test
    void exposesOnlyTheSupportedContexts() {
        given().port(port)
                .when().get("/")
                .then().statusCode(400)
                .body(containsString("Default Context"))
                .body(containsString("LAReferencia Accepted Doctypes Context"))
                .body(containsString("Doctoral and Master Thesis Context"))
                .body(not(containsString("Driver Context")))
                .body(not(containsString("OpenAIRE Context")))
                .body(not(containsString("OpenAIRE 4 Context")));
    }

    private void assertAsset(String path, String expectedContentType) {
        given().port(port)
                .when().get(path)
                .then().statusCode(200)
                .header("Content-Type", containsString(expectedContentType));
    }
}
