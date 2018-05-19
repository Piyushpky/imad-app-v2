package com.hp.wpp.ssnc.restapp.resources;

import org.jboss.resteasy.plugins.server.tjws.TJWSEmbeddedJaxrsServer;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public abstract class BaseResourceTest {
    private TJWSEmbeddedJaxrsServer server;
    private static final int PORT_NUMBER_1 = 9099;
    protected static final String ROOT_URL = "http://localhost:" + PORT_NUMBER_1;

    /**
     * This method will be executed before test cases. If you are overriding this method
     * then make sure that you are calling <code>super.setup();</code> as the first line of your method.
     */
    @BeforeMethod
    public void setup() {
        server = new TJWSEmbeddedJaxrsServer();
        server.setPort(PORT_NUMBER_1);        
        server.start();
        server.getDeployment().getRegistry().addSingletonResource(getResourceClassToBeTested());
    }

    /**
     * Pass the class for which you are writing this test case. This class will be passed to test server for initialization.
     *
     * @return Class to be tested.
     */
    protected abstract Object getResourceClassToBeTested();

    /**
     * This method will be executed after the test case is complete. If you are overriding this method
     * then make sure that you are calling <code>super.tearDown();</code> as the first line of your method.
     */
    @AfterMethod
    public void tearDown() {
        server.stop();
    }
}
