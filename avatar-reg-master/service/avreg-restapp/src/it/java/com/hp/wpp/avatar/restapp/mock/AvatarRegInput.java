package com.hp.wpp.avatar.restapp.mock;

import com.hp.wpp.avatar.restapp.tests.ComponentTestResourceLoader;
import cucumber.api.java.en.Given;
import org.glassfish.grizzly.http.Method;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.springframework.test.context.ContextConfiguration;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Pattern;

import static com.xebialabs.restito.builder.stub.StubHttp.whenHttp;
import static com.xebialabs.restito.semantics.Action.status;
import static com.xebialabs.restito.semantics.Condition.matchesUri;
import static com.xebialabs.restito.semantics.Condition.method;

@ContextConfiguration(locations = {"classpath:cucumber.xml"})
public class AvatarRegInput {

	String payload;
	
	public String getPayload() {
		return payload;
	}

	@Given("^a (.*) printer identification payload$")
	public void registerPrinter(String payloadType) throws Exception
	{
		payloadType = "json/" + payloadType +".json";
	    payload = new String(Files.readAllBytes(Paths.get(ClassLoader.getSystemResource(payloadType).toURI())));
	
	}

	private static String DLS_SERVICE_PUT_URL = "/virtualprinter/v1/notification/printer";



	@Given("^a (.*) printer identification payload but DLS returning (.*)$")
	public void registerPrinterDLSFailing(String payloadType, int statusCode)throws Exception
	{
		payloadType = "json/" + payloadType + ".json";
		whenHttp(ComponentTestResourceLoader.getAvRegDependentsServiceStubServer())
				.match(method(Method.PUT), matchesUri(Pattern.compile(DLS_SERVICE_PUT_URL, Pattern.CASE_INSENSITIVE)))
				.then(status(HttpStatus.getHttpStatus(statusCode)));
			payload = new String(Files.readAllBytes(Paths.get(ClassLoader.getSystemResource(payloadType).toURI())));

	}
}
