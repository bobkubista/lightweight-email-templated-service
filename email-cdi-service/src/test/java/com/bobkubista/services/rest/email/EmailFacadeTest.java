/**
 *
 */
package com.bobkubista.services.rest.email;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.ws.rs.core.Response;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.bobkubista.services.rest.email.EmailFacade;
import com.bobkubista.services.rest.email.api.model.EmailContext;
import com.bobkubista.services.rest.email.api.model.EmailContext.EmailBuilder;
import com.dumbster.smtp.SimpleSmtpServer;

import bobkubista.example.utils.property.ServerProperties;

/**
 * @author Bob
 *
 */
public class EmailFacadeTest {

    private static SimpleSmtpServer server;

    private final EmailFacade facade = new EmailFacade();

    @After
    public void afterClass() {
        server.stop();
    }

    @Before
    public void beforeClass() {
        server = SimpleSmtpServer.start(Integer.valueOf(ServerProperties.get()
                .getString("email.smtp.port")));
    }

    @Test
    public void testGetTemplate() {
        final Response result = this.facade.getTemplate("extraTestTemplate");
        Assert.assertEquals(Response.Status.OK.getStatusCode(), result.getStatus());
    }

    @Test
    public void testGetTemplates() {
        final Response result = this.facade.getTemplates();
        Assert.assertEquals(Response.Status.OK.getStatusCode(), result.getStatus());
    }

    @Test
    public void testgetWebVersionOfEmail() {
        final EmailContext context = new EmailBuilder("bla@foo.bar", "foobar").build();
        final Response result = this.facade.getWebVersionOfEmail(context);

        Assert.assertNotNull(result);
        Assert.assertEquals(200, result.getStatus());
    }

    @Test
    public void testgetWebVersionOfEmailTemplate() {
        final EmailContext context = new EmailBuilder("bla@foo.bar", "foobar").build();
        final Response result = this.facade.getWebVersionOfEmail(context, "extraTestTemplate");

        Assert.assertNotNull(result);
        Assert.assertEquals(200, result.getStatus());
    }

    @Test
    public void testSaveTemplate() throws IOException {
        try {
            final File file = File.createTempFile("test", "");
            ServerProperties.get()
                    .setProperty("email.template.location", ServerProperties.get()
                            .getString("java.io.tmpdir"));
            final Response result = this.facade.saveTemplate("temp", new FileInputStream(file));
            Assert.assertEquals(Response.Status.OK.getStatusCode(), result.getStatus());
            Assert.assertEquals(Response.Status.OK.getStatusCode(), this.facade.getTemplate("temp")
                    .getStatus());
            this.facade.deleteTemplate("temp");
        } finally {
            ServerProperties.get()
                    .setProperty("email.template.location", "");
        }
    }

    /**
     * Test method for
     * {@link com.bobkubista.services.rest.email.EmailFacade#sendEmail(com.bobkubista.services.rest.email.api.model.EmailContext)}
     * .
     */
    @Test
    public void testSendEmail() {
        final EmailContext context = new EmailBuilder("bla@foo.bar", "foobar").build();
        final Response result = this.facade.sendEmail(context);

        Assert.assertNotNull(result);
        Assert.assertEquals(200, result.getStatus());
    }

    /**
     * Test method for
     * {@link com.bobkubista.services.rest.email.EmailFacade#sendEmail(com.bobkubista.services.rest.email.api.model.EmailContext)}
     * .
     */
    @Test
    public void testSendEmailTemplate() {
        final EmailContext context = new EmailBuilder("bla@foo.bar", "foobar").build();
        final Response result = this.facade.sendEmail(context, "extraTestTemplate");

        Assert.assertNotNull(result);
        Assert.assertEquals(200, result.getStatus());
    }
}