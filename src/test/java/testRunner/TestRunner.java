package testRunner;

import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(
		features = {".//Features"},
		glue = "stepDefinitions",
		tags ="@Abhi",
		dryRun = false,
		monochrome = true,
		plugin = { "pretty","html:./test-output/ExecutionReport.html" })

public class TestRunner {

}
