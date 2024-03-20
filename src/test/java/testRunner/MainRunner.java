package testRunner;

import io.cucumber.core.cli.CommandlineOptions;
import io.cucumber.core.cli.Main;
import utilities.RunnerArgumentBuilder;

public class MainRunner {
	static RunnerArgumentBuilder runArgsBuild = new RunnerArgumentBuilder();

	public static void main(String[] args) {
		try {
			String[] arguments = runArgsBuild.getRunnerArguments(CommandlineOptions.NO_DRY_RUN);
//			String[] arguments = { CommandlineOptions.GLUE, "stepDefinitions", ".//Features",
//					CommandlineOptions.MONOCHROME, CommandlineOptions.PLUGIN, "pretty", CommandlineOptions.PLUGIN,
//					"html:.//test-output/ExecutionReport.html", CommandlineOptions.TAGS,
//					"@Adding_a_single_item_in_cart_and_verifying_sub_total or @Adding_multiple_items_in_cart_and_verifying_sub_total" };
			Main.run(arguments, Thread.currentThread().getContextClassLoader());
		} catch (Exception e) {
			System.err.println("Unable to run scenarios: ");
			e.printStackTrace();
		}
	}
}
