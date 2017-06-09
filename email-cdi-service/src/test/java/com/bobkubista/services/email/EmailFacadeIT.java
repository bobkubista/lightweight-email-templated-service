/**
 *
 */
package com.bobkubista.services.email;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.bobkubista.services.email.api.model.DateReplacement;
import com.bobkubista.services.email.api.model.EmailContext;
import com.bobkubista.services.email.api.model.EmailContext.EmailBuilder;
import com.bobkubista.services.email.api.model.LinkReplacement;
import com.dumbster.smtp.SimpleSmtpServer;
import com.dumbster.smtp.SmtpMessage;

import bobkubista.example.utils.property.ServerProperties;

/**
 * @author bkubista
 *
 */
public class EmailFacadeIT extends JerseyTest {

	private static final String EMAIL_SUBJECT_HEADER = "Subject";

	private static SimpleSmtpServer server;

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
	public void testGetTemplates() {
		final Response response = this.target()
				.request(MediaType.APPLICATION_JSON)
				.get();

		Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
		final List<String> readEntity = response.readEntity(new GenericType<List<String>>() {
		});
		Assert.assertNotNull(readEntity);
		Assert.assertEquals(2, readEntity.size());
	}

	@Test
	public void testgetWebVersionOfEmail() throws URISyntaxException {
		final EmailContext email = new EmailBuilder("bla@foo.bar", "foobar")
				.addReplacement(new DateReplacement(new Date()))
				.addReplacement(new LinkReplacement(new URI("http://bla.bla")))
				.build();
		final Response response = this.target("/webversion")
				.request(MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON)
				.put(Entity.entity(email.getEmail(), MediaType.APPLICATION_XML), Response.class);

		Assert.assertNotNull(response);
		Assert.assertEquals(Status.OK.getStatusCode(), response.getStatus());
		Assert.assertEquals(0, server.getReceivedEmailSize());
		Assert.assertEquals(
				"Hello bla@foo.bar,Please click link below to activate your account, http://bla.blaThank you,",
				response.readEntity(String.class));
	}

	@Test
	public void testgetWebVersionOfEmailTemplate() throws URISyntaxException {
		final EmailContext email = new EmailBuilder("bla@foo.bar", "foobar")
				.addReplacement(new DateReplacement(new Date()))
				.addReplacement(new LinkReplacement(new URI("http://bla.bla")))
				.build();
		final Response response = this.target("webversion/extraTestTemplate")
				.request(MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON)
				.put(Entity.entity(email.getEmail(), MediaType.APPLICATION_XML), Response.class);

		Assert.assertNotNull(response);
		Assert.assertEquals(Status.OK.getStatusCode(), response.getStatus());
		Assert.assertEquals(0, server.getReceivedEmailSize());
		Assert.assertEquals("testing email template", response.readEntity(String.class));
	}

	@Test
	public void testSendEmail() throws URISyntaxException {
		final EmailContext email = new EmailBuilder("bla@foo.bar", "foobar")
				.addReplacement(new DateReplacement(new Date()))
				.addReplacement(new LinkReplacement(new URI("http://bla.bla")))
				.build();
		final Response response = this.target("/")
				.request(MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON)
				.put(Entity.entity(email.getEmail(), MediaType.APPLICATION_XML), Response.class);

		Assert.assertNotNull(response);
		Assert.assertEquals(Status.OK.getStatusCode(), response.getStatus());
		Assert.assertEquals(1, server.getReceivedEmailSize());
		final SmtpMessage recievedEmail = (SmtpMessage) server.getReceivedEmail()
				.next();
		Assert.assertEquals("foobar", recievedEmail.getHeaderValue(EMAIL_SUBJECT_HEADER));
	}

	@Test
	public void testSendEmailTemplate() throws URISyntaxException {
		final EmailContext email = new EmailBuilder("bla@foo.bar", "foobar")
				.addReplacement(new DateReplacement(new Date()))
				.addReplacement(new LinkReplacement(new URI("http://bla.bla")))
				.build();
		final Response response = this.target("extraTestTemplate")
				.request(MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON)
				.put(Entity.entity(email.getEmail(), MediaType.APPLICATION_XML), Response.class);

		Assert.assertNotNull(response);
		Assert.assertEquals(Status.OK.getStatusCode(), response.getStatus());
		Assert.assertEquals(1, server.getReceivedEmailSize());
		final SmtpMessage recievedEmail = (SmtpMessage) server.getReceivedEmail()
				.next();
		Assert.assertEquals("foobar", recievedEmail.getHeaderValue(EMAIL_SUBJECT_HEADER));
	}

	@Override
	protected Application configure() {

		return new ResourceConfig(EmailFacade.class);
	}

}
