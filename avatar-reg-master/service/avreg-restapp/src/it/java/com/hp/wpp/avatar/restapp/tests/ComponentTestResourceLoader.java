package com.hp.wpp.avatar.restapp.tests;

import com.hp.wpp.avatar.restapp.resources.EntityResource;
import com.jayway.restassured.RestAssured;
import com.xebialabs.restito.server.StubServer;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import org.jboss.resteasy.plugins.server.tjws.TJWSEmbeddedJaxrsServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by aroraja on 12/12/2016.
 */
public class ComponentTestResourceLoader {

    public static final int PORT_NUMBER_FOR_AVREG_FLOW=9000;
    public static final int PORT_NUMBER_FOR_DLS_SERVICE = 9294;
    private static final Logger LOG = LoggerFactory.getLogger(ComponentTestResourceLoader.class);
    private static StubServer stubServer;
    private TJWSEmbeddedJaxrsServer server;

    @Autowired
    private EntityResource entityResource;


    @Before
    public void setup()
    {
        startDlsStubServer();
        startAvregFlow();

    }
    @After
    public void stop()
    {
        stopDlsStubServer();
        stopAvRegServer();

    }

    private void stopDlsStubServer() {
        stubServer.stop();
    }

    private void stopAvRegServer() {
       server.stop();
    }

    public static StubServer getAvRegDependentsServiceStubServer() {
        return stubServer;
    }


    private static void startDlsStubServer() {
        stubServer = new StubServer(PORT_NUMBER_FOR_DLS_SERVICE);
        stubServer.start();
        LOG.debug("Dls Service Stub started with port: {}",PORT_NUMBER_FOR_DLS_SERVICE);
    }

    private void startAvregFlow() {
        RestAssured.baseURI = "http://localhost:9000/v1";
        server = new TJWSEmbeddedJaxrsServer();
        server.setPort(PORT_NUMBER_FOR_AVREG_FLOW);
        server.start();
        server.getDeployment().getRegistry().addSingletonResource(entityResource);
    }

}
