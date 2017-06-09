/**
 *
 */
package com.bobkubista.services.email;

import javax.ws.rs.core.Response;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.bobkubista.services.email.api.model.EmailContext;
import com.bobkubista.services.email.api.model.EmailContext.EmailBuilder;
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

	/**
	 * Test method for
	 * {@link com.bobkubista.services.email.EmailFacade#sendEmail(com.bobkubista.services.email.api.model.EmailContext)}
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
	 * {@link com.bobkubista.services.email.EmailFacade#sendEmail(com.bobkubista.services.email.api.model.EmailContext)}
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
