/**
 *
 */
package com.bobkubista.services.email.strategy;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;

import com.bobkubista.services.email.api.model.DateReplacement;
import com.bobkubista.services.email.api.model.EmailContext;
import com.bobkubista.services.email.api.model.LinkReplacement;
import com.bobkubista.services.email.api.model.EmailContext.EmailBuilder;
import com.bobkubista.services.email.strategy.TestEmailStrategy;

/**
 * @author Bob
 *
 */
public class TestEmailStrategyTest {

    private static final String GENERAL_EMAIL = "Hello bla@foo.bar,Please click link below to activate your account, http://bla.blaThank you,";

    private static final String SUBJECT = "foobar";

    @Test
    public void testGeneralEmailStrategy() throws URISyntaxException {
        final String recipient = "bla@foo.bar";
        final EmailContext email = new EmailBuilder("bla@foo.bar", "foobar").addReplacement(new DateReplacement(new Date()))
                .addReplacement(new LinkReplacement(new URI("http://bla.bla")))
                .build();
        email.setMessage(GENERAL_EMAIL);
        final TestEmailStrategy strategy = new TestEmailStrategy(email);

        final EmailContext composedEmail = strategy.getEmail();
        Assert.assertEquals(recipient, composedEmail.getRecipient());
        Assert.assertEquals(SUBJECT, composedEmail.getSubject());
        Assert.assertFalse(email.getMessage(), StringUtils.contains(email.getMessage(), "${"));
        Assert.assertEquals(GENERAL_EMAIL, email.getMessage());
    }

    @Test
    public void testGeneralEmailStrategyWebVersion() throws URISyntaxException {
        final EmailContext email = new EmailBuilder("bla@foo.bar", "foobar").addReplacement(new DateReplacement(new Date()))
                .addReplacement(new LinkReplacement(new URI("http://bla.bla")))
                .build();
        email.setMessage(GENERAL_EMAIL);
        final TestEmailStrategy strategy = new TestEmailStrategy(email);

        final String composedEmail = strategy.getWebVersion();
        Assert.assertEquals("Hello bla@foo.bar,Please click link below to activate your account, http://bla.blaThank you,", composedEmail);
    }

}
