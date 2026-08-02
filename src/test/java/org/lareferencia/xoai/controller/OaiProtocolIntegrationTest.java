package org.lareferencia.xoai.controller;

import io.restassured.response.Response;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.SolrInputDocument;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.lareferencia.xoai.app.MainApp;
import org.lareferencia.xoai.support.SharedSolrContainer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = MainApp.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = "xoai.config.path=config/xoai.config")
class OaiProtocolIntegrationTest {

    private static final String HANDLE = "20.500.12345/public+record";

    @LocalServerPort
    private int port;

    private static SolrClient solrClient;

    @DynamicPropertySource
    static void solrProperties(DynamicPropertyRegistry registry) {
        registry.add("solr.url", SharedSolrContainer::getSolrUrl);
    }

    @BeforeAll
    static void indexFixtures() throws Exception {
        String solrUrl = SharedSolrContainer.getSolrUrl();
        solrClient = new HttpSolrClient.Builder(solrUrl).build();
        solrClient.add(item(1, HANDLE, true));
        solrClient.add(item(2, "20.500.12345/private", false));
        solrClient.commit();
    }

    @AfterAll
    static void closeClient() throws Exception {
        if (solrClient != null) {
            solrClient.close();
        }
    }

    @Test
    void servesEveryOaiVerbAsXml() throws Exception {
        assertVerb("Identify", null, null);
        assertVerb("ListMetadataFormats", null, null);
        assertVerb("ListSets", null, null);
        assertVerb("ListIdentifiers", "metadataPrefix", "oai_dc");
        assertVerb("ListRecords", "metadataPrefix", "oai_dc");

        Response getRecord = request("GetRecord", "metadataPrefix", "oai_dc",
                "identifier", HANDLE);
        assertOaiXml(getRecord.asString());
        getRecord.then().body(containsString("<GetRecord>"))
                .body(containsString("<identifier>" + HANDLE + "</identifier>"));
    }

    @Test
    void reportsProtocolErrorsAsOaiXml() throws Exception {
        Response response = request("UnknownVerb");

        assertOaiXml(response.asString());
        response.then().statusCode(200)
                .body(containsString("<error code=\"badVerb\""));
    }

    @Test
    void excludesPrivateRecordsFromTheDefaultContext() {
        request("ListIdentifiers", "metadataPrefix", "oai_dc")
                .then().statusCode(200)
                .body(containsString(HANDLE))
                .body(org.hamcrest.Matchers.not(containsString("20.500.12345/private")));
    }

    private void assertVerb(String verb, String parameter, String value) throws Exception {
        Response response = parameter == null
                ? request(verb)
                : request(verb, parameter, value);

        response.then().statusCode(200).body(containsString("<" + verb + ">"));
        assertOaiXml(response.asString());
    }

    private Response request(String verb, String... parameters) {
        io.restassured.specification.RequestSpecification request = given()
                .port(port)
                .queryParam("verb", verb);
        for (int i = 0; i < parameters.length; i += 2) {
            request.queryParam(parameters[i], parameters[i + 1]);
        }
        return request.when().get("/request");
    }

    private static void assertOaiXml(String body) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        String root = factory.newDocumentBuilder()
                .parse(new ByteArrayInputStream(body.getBytes(StandardCharsets.UTF_8)))
                .getDocumentElement().getLocalName();
        assertEquals("OAI-PMH", root);
    }

    private static SolrInputDocument item(int id, String handle, boolean isPublic) {
        SolrInputDocument document = new SolrInputDocument();
        document.addField("item.id", id);
        document.addField("item.handle", handle);
        document.addField("item.public", isPublic);
        document.addField("item.deleted", false);
        document.addField("item.lastmodified", Instant.parse("2026-08-02T10:15:30Z").toString());
        document.addField("item.communities", "community:test");
        document.addField("item.collections", "collection:test");
        document.addField("item.compile", "<metadata xmlns=\"http://www.lyncode.com/xoai\">"
                + "<element name=\"dc\"><element name=\"title\"><element name=\"none\">"
                + "<field name=\"value\">Test title</field></element></element></element>"
                + "</metadata>");
        return document;
    }
}
