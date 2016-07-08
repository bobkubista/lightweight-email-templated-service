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
import com.bobkubista.services.email.strategy.TemplateEmailStrategy;

public class TemplateEmailStrategyTest {

    @Test
    public void testGeneralEmailStrategyWebVersion() throws URISyntaxException {
        final EmailContext email = new EmailBuilder("bla@foo.bar", "foobar").addReplacement(new DateReplacement(new Date()))
                .addReplacement(new LinkReplacement(new URI("http://bla.bla")))
                .build();
        final TemplateEmailStrategy strategy = new TemplateEmailStrategy(email, "extraTestTemplate");

        final String composedEmail = strategy.getWebVersion();
        Assert.assertEquals("testing email template", composedEmail);

    }

    @Test
    public void testTemplateEmailStrategy() throws URISyntaxException {
        final String recipient = "bla@foo.bar";
        final EmailContext email = new EmailBuilder("bla@foo.bar", "foobar").addReplacement(new DateReplacement(new Date()))
                .addReplacement(new LinkReplacement(new URI("http://bla.bla")))
                .build();
        final TemplateEmailStrategy strategy = new TemplateEmailStrategy(email, "extraTestTemplate");
        final EmailContext composedEmail = strategy.getEmail();
        Assert.assertEquals(recipient, composedEmail.getRecipient());
        Assert.assertFalse(email.getMessage(), StringUtils.contains(email.getMessage(), "${"));
        Assert.assertEquals("testing email template", composedEmail.getMessage());
    }
}
