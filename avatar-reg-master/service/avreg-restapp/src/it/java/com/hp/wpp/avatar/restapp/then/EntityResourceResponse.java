package com.hp.wpp.avatar.restapp.then;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.hp.wpp.avatar.restmodel.errors.Error;
import com.hp.wpp.avatar.restmodel.errors.Errors;
import com.jayway.restassured.path.json.JsonPath;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import org.testng.Assert;

public class EntityResourceResponse extends Response {
	
	public static final String version = "version";
	public static final String cloudId = "cloud_id";
	public static final String links = "links";
	
	private JsonPath responseJsonPath;

    void afterSetResponse() {
        responseJsonPath = getResponse().jsonPath();
    }
    
    @And("^the response payload should be as per entity config response schema$")
    public void validateEntityConfig()
    {
    	String version = responseJsonPath.get("version");
    	Assert.assertNotNull(version);
    	Assert.assertNotNull(responseJsonPath.get(cloudId));
    }

    @Then("^the response code should be (\\d+)$")
    public void responseStatusCodeShouldBe(int expectedStatusCode) throws Throwable {
        Assert.assertEquals( getStatusCode(), expectedStatusCode);
        
    }

    @Then("^the response code should be (\\d+) and (.*)$")
    public void responseStatusCodeShouldBe(int expectedStatusCode, String errorCode) throws Throwable {
        Assert.assertEquals( getStatusCode(), expectedStatusCode);
        String expectedErrorCode = getResponse().header("Internal-Error-Code");
        Assert.assertEquals(errorCode,expectedErrorCode);
    }
    
    @Then("^the registration api should return (.*) response (.*) and (.*)$")
    public void responseStatusErrorCodeShouldBe(String status,String detailCode,String description) throws Throwable {
    	if(status.equals("error")) {
            ObjectMapper objectMapper = new ObjectMapper();
            Errors errors = objectMapper.readValue(getResponse().asString(), Errors.class);
            Error error = errors.getErrors().iterator().next();
            Assert.assertEquals(detailCode,error.getCode());
            Assert.assertEquals(description,error.getDescription());

         }
    }   
}
