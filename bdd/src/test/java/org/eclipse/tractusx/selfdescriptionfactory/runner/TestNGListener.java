package org.eclipse.tractusx.selfdescriptionfactory.runner;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.tractusx.selfdescriptionfactory.config.Configuration;
import org.eclipse.tractusx.selfdescriptionfactory.utils.XrayUtils;
import org.testng.ISuite;
import org.testng.ISuiteListener;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


@Slf4j
public class TestNGListener implements ISuiteListener {

    @Override
    public void onStart(ISuite suite) {
        log.info("Test Suite started");
    }

    @Override
    public void onFinish(ISuite suite) {
        boolean xray = Boolean.parseBoolean(Configuration.getXrayFlag());
        log.info("Test Suite execution completed");
        if (xray) {
            try {
                var json = Files.readString(Path.of("target/cucumber-reports/Cucumber.json"));
                new XrayUtils().importReport(json);
            } catch (IOException e) {
                log.error("Error while exporting test results to xray"+ e.getMessage());
            }
        }
        else {
            log.info("xray execution skipped");
        }
    }
}
