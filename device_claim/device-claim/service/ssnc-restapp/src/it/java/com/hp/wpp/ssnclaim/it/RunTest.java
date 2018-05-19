package com.hp.wpp.ssnclaim.it;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;

@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {"pretty", "html:target/html/", "json:target/cucumber.json", "usage:target/usage.html"},
        tags = {"@sanity"},
        features = {"classpath:features/"}
)
@ContextConfiguration(locations = {"classpath:cucumber.xml"})
public class RunTest {


}
