package de.axelspringer.ideas.tools.dash.business.art;

import com.google.gson.Gson;
import de.axelspringer.ideas.tools.dash.business.check.Check;
import de.axelspringer.ideas.tools.dash.business.check.CheckExecutor;
import de.axelspringer.ideas.tools.dash.business.check.CheckResult;
import de.axelspringer.ideas.tools.dash.presentation.State;
import de.axelspringer.ideas.tools.dash.util.RestClient;
import org.apache.http.HttpHost;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ArtCheckExecutor implements CheckExecutor {

    private static final String N_A = "N/A";

    private static final String CALLBACK_NAME = "a";
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(ArtCheckExecutor.class);

    @Autowired
    private RestClient restClient;

    @Autowired
    private Gson gson;

    @Override
    public List<CheckResult> executeCheck(Check check) {

        ArtCheck artCheck = (ArtCheck) check;

        // load results
        final ArtTestSuitesResult artTestSuitesResult;
        final String url = artCheck.getUrl();
        log.debug("Retrieving job result from art url {}", url);
        try {
            String testSuiteResultString = getJsonp(url, CALLBACK_NAME, artCheck.getProxy());
            artTestSuitesResult = gson.fromJson(testSuiteResultString, ArtTestSuitesResult.class);
        } catch (Exception e) {
            log.error("error when calling art check '{}' for url '{}'", artCheck.getName(), artCheck.getUrl());
            return Arrays.asList(new CheckResult(State.RED, artCheck.getName(), N_A, 0, 0, artCheck.getGroup()));
        }

        List<CheckResult> checkResults = new ArrayList<>();

        // iterate over results and aggregate
        for (String artTestSuiteName : artTestSuitesResult.keySet()) {
            final ArtTestResults artTestResults = artTestSuitesResult.get(artTestSuiteName);
            if (artCheck.getIgnoredTestSuites().contains(artTestSuiteName)) {
                continue;
            }

            int testCount = 0;
            int failCount = 0;
            for (String artTestName : artTestResults.keySet()) {
                final ArtTestResultInfo testInfo = artTestResults.get(artTestName);

                testCount += 1;
                if (!testInfo.isSuccessfull()) {
                    failCount += 1;
                }
            }
            String info = failCount > 0 ? artTestSuiteName + failCount + "/" + testCount : artTestSuiteName;
            State state = failCount > 0 ? State.YELLOW : State.GREEN;
            checkResults.add(new CheckResult(state, "ART@WELT", info, testCount, failCount, artCheck.getGroup()));
        }

        return checkResults;
    }

    private String getJsonp(String url, String callbackName, HttpHost proxy) throws URISyntaxException, IOException {
        URIBuilder uriBuilder = new URIBuilder(url);
        uriBuilder.addParameter("callback", callbackName);

        // result will be something like callbackName({"key": "value"});
        String result = restClient.get(uriBuilder.build().toString());

        // cut off the leading "callbackName(" and trailing ");"
        return result.substring(callbackName.length() + 1, result.length() - 2);
    }

    @Override
    public boolean isApplicable(Check check) {
        return check instanceof ArtCheck;
    }
}
