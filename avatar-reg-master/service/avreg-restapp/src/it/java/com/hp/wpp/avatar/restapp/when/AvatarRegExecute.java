package com.hp.wpp.avatar.restapp.when;

import com.hp.wpp.avatar.restapp.then.EntityResourceResponse;
import com.hp.wpp.logger.WPPLogger;
import com.hp.wpp.logger.impl.WPPLoggerFactory;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Header;
import com.jayway.restassured.response.Headers;
import com.jayway.restassured.response.Response;
import cucumber.api.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;

import static com.jayway.restassured.RestAssured.given;

public class AvatarRegExecute {

	private static WPPLogger LOG = WPPLoggerFactory.getLogger(AvatarRegExecute.class);
	
	@Autowired
	private com.hp.wpp.avatar.restapp.mock.AvatarRegInput avatarRegInput;

    @Autowired
    private EntityResourceResponse entityResourceResponse;

	private Response response;
    private String path;

    @When("^register printer api is called$")
    public void callCreateJobApi()
    {
        path = "/entities/";
        try {
            Header header1 = new Header("With-Postcard","false");
            Headers headers = new Headers(header1);
            response = given().contentType(ContentType.JSON).headers(headers).body(avatarRegInput.getPayload()).when().post(path);
        } catch (Exception e) {
            Assert.fail(e.getMessage().toString());
        }
        entityResourceResponse.setResponse(response);
    }

    @When("^register printer api with (.*) is called$")
    public void callCreateRegisterWithHeaderApi(String blacklistRuleType)
    {
        path = "/entities/";
        try {
            Header header1 = new Header("With-Postcard","false");
            Header header2 = getHeaders(blacklistRuleType);
            Headers headers = new Headers(header1, header2);
            response = given().contentType(ContentType.JSON).headers(headers).body(avatarRegInput.getPayload()).when().post(path);
        } catch (Exception e) {
            Assert.fail(e.getMessage().toString());
        }
        entityResourceResponse.setResponse(response);
    }

    private Header getHeaders(String blacklistRuleType) {
        if("model_and_firmwareVersion".equals(blacklistRuleType))
            return new Header("User-Agent", "A B K1234B ABCDE1.2");
        else if("firmwareVersion".equals(blacklistRuleType))
            return new Header("User-Agent", "A B C ABCDE1.1");
        else
            return new Header("User-Agent", "A B K1234A D");
    }


}
