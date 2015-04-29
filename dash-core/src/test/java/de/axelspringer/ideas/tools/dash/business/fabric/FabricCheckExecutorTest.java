package de.axelspringer.ideas.tools.dash.business.fabric;

import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class FabricCheckExecutorTest {

    private FabricCheckExecutor fabricCheckExecutor = new FabricCheckExecutor();

    @Test
    public void testExtractToken() {

        final String testToken = UUID.randomUUID().toString();
        final String testMarkup = "<some random shit askjd24i90jfwoijwijciw<<>>>fb5c679090103d95b918cfaccb8f21c\",\"modules/sdk/twitter\":\"https://cdn.crashlytics.io/assets/modules/sdk/twitter-be27b553c9a4a483278c1f66da47ece5\",\"modules/sdk/mopub\":\"https://cdn.crashlytics.io/assets/modules/sdk/mopub-e8ba403a7125330407fa812ffbb622af\",\"modules/sdk/twitter_conversion_tracking\":\"https://cdn.crashlytics.io/assets/modules/sdk/twitter_conversion_tracking-89391d3c5662ae206c9488cc197a251c\"}};</script>\n" +
                "      <script data-main=\"webapp-577f23b5bbf627b7ca937fff978f0e50\" src=\"https://cdn.crashlytics.io/assets/require-5cfadf23cd92409c2a120f760fea0228.js\"></script>\n" +
                "\n" +
                "\n" +
                "    <meta content=\"authenticity_token\" name=\"csrf-param\" />\n" +
                "<meta content=\"" + testToken + "\" name=\"csrf-token\" />\n" +
                "    \n" +
                "  </head>\n" +
                "  <body class=\"font-standard sdk\">\n" +
                "    <div id=\"chrome\">\n" +
                "      <div id=\"app-loader\">\n" +
                "      <i class=\"icon-fabric\"></i>\n" +
                "      </div>";

        assertEquals(testToken, fabricCheckExecutor.extractToken(testMarkup));
    }
}