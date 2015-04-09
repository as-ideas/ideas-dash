package de.axelspringer.ideas.tools.dash.business.jira;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CryptoUtilTest {

    @Test
    public void test() throws Exception {

        String password = "foo";
        final byte[] encrypted = CryptoUtil.encrypt(password);
        final String decrypted = CryptoUtil.decrypt(encrypted);
        assertEquals(password, decrypted);
    }
}
