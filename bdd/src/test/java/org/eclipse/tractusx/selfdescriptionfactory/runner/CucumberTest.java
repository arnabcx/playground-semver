package org.eclipse.tractusx.selfdescriptionfactory.runner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.Test;

@Test
@CucumberOptions(
        features = "src/test/resources/functional",
        glue = {"org/eclipse/tractusx/selfdescriptionfactory/testStepsDefinitions"},
        plugin = {"pretty", "json:target/cucumber-reports/Cucumber.json" },
        monochrome = true
)
public class CucumberTest extends AbstractTestNGCucumberTests {
}
