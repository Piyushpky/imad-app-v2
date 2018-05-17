import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@RunWith(Cucumber.class)
@CucumberOptions(
		plugin={"pretty","html:target/cucumber"},
		tags={"@sanity"},
		features = {"src/it/resources/features"},
		glue = "com.hp.wpp.avatar.restapp"
		)
@ContextConfiguration(locations = {"classpath:cucumber.xml"})
public class RunTest {

	@Autowired
	com.hp.wpp.avatar.restapp.tests.ComponentTestResourceLoader ComponentTestResourceLoader;

}
