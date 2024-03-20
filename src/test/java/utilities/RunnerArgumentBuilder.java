package utilities;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import io.cucumber.core.cli.CommandlineOptions;

public class RunnerArgumentBuilder {
	private static DriverBase webDB = new DriverBase();
	public static boolean exeFlag;
	public static Set<String> testCasesIncluded = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
	protected static String suiteName;
	public static List<String> stringList = new ArrayList<>();

	@SuppressWarnings("static-access")
	public void getIncludedTests() throws EncryptedDocumentException, IOException {
		try {
			webDB.workbook = WorkbookFactory.create(new File(webDB.getProperty("testDataFile")));
			webDB.sheet = webDB.workbook.getSheet("TestData");
			String actTestId = null;
			String actTestName = null;
			int rowsCount = webDB.sheet.getLastRowNum();
			for (int rowCount = 3; rowCount <= rowsCount; rowCount++) {
				actTestId = webDB.sheet.getRow(rowCount).getCell(0).getStringCellValue();
				if (actTestId.contains("TC")) {
					boolean testExeFlag = webDB.sheet.getRow(rowCount).getCell(1).getBooleanCellValue();
					if (testExeFlag) {
						actTestName = webDB.sheet.getRow(rowCount).getCell(2).getStringCellValue();
						testCasesIncluded.add(actTestName);
						webDB.testRowMap.put(actTestName, rowCount);
					}
					rowCount++;
				}
			}
		} catch (Exception e) {
			webDB.writeToConsole("WARN", e.toString());
		}
	}

	public String[] constructRunnerArguments(String dryrun) throws ParserConfigurationException, TransformerException {
		try {
			stringList.add(dryrun);
			stringList.add(CommandlineOptions.GLUE);
			stringList.add("stepDefinitions");
			stringList.add(".//Features");
			stringList.add(CommandlineOptions.TAGS);
			stringList.add(prepareTags());
			stringList.add(CommandlineOptions.MONOCHROME);
			stringList.add(CommandlineOptions.PLUGIN);
			stringList.add("pretty");
			stringList.add(CommandlineOptions.PLUGIN);
			stringList.add("html:.//test-output/ExecutionReport.html");

			webDB.writeToConsole("INFO", "Scenarios selected successfully!");
		} catch (Exception e) {
			webDB.writeToConsole("WARN", e.toString());
		}
		return stringList.toArray(new String[0]);
	}

	// Method to add an include tag to the methods element
	private static String prepareTags() {
		String allTags = testCasesIncluded.stream().map(tag -> "@" + tag.replace(" ", "_"))
				.collect(Collectors.joining(" or ", "", ""));
		return allTags;
	}

	public String[] getRunnerArguments(String dryrun)
			throws EncryptedDocumentException, IOException, ParserConfigurationException, TransformerException {
		try {
			getIncludedTests();
			return constructRunnerArguments(dryrun);
		} catch (Exception e) {
			webDB.writeToConsole("WARN", "Failed to prepare XML" + e.toString());
		}
		return null;
	}
}
