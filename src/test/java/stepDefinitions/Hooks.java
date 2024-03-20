package stepDefinitions;

import java.awt.AWTException;

import io.cucumber.java.*;
import pageObjects.Page;
import utilities.DriverBase;

public class Hooks {

	private static DriverBase webDB = new DriverBase();
	Login_Step steps = new Login_Step();

	@Before(order = 0)
	public void beforeFeature() throws AWTException {
		webDB.browserOpen();
	}

	@Before(order = 1)
	public void beforeScenario(Scenario scenario) {
		String scenarioName = scenario.getName();
		webDB.writeToConsole("INFO", "Running scenarion: " + scenarioName);
		DriverBase.testScenarioName = scenarioName;
		steps.page = new Page(webDB.driver);
	}

	@After(order = 1)
	public void afterScenario(Scenario scenario) {
		webDB.writeToConsole("INFO", "Completed running scenarion: " + scenario.getName() + ":" + scenario.getStatus());
	}

	@After(order = 0)
	public void afterScenarioFinish() {
		webDB.browserClose();
	}

}
