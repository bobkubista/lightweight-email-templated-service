package com.bobkubista.services.email.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.bobkubista.services.email.api.model.EmailContext;

/**
 * @author Bob
 *
 */
public interface EmailApi {

	/**
	 * Delete a template
	 *
	 * @param template
	 *            the template to delete
	 * @return 200 if success
	 */
	@Path("{template}")
	@DELETE
	Response deleteTemplate(@PathParam("template") final String template);

	/**
	 *
	 * @param template
	 *            the template to get
	 * @return the template file
	 */
	@Path("{template}")
	@GET
	Response getTemplate(@PathParam("template") final String template);

	/**
	 *
	 * @return a list of all email templates available
	 */
	@GET
	Response getTemplates();

	/**
	 * get the web version of the test email
	 *
	 * @param context
	 *            details to fill in the email
	 * @return a web version of the email
	 */
	@PUT
	@Path("webversion")
	Response getWebVersionOfEmail(EmailContext context);

	/**
	 * get a webversion of the email with a specific name
	 * 
	 * @param context
	 *            details to fill in the email
	 * @param template
	 *            the template name
	 * @return a webversion os a specific email
	 */
	@PUT
	@Path("webversion/{template}")
	Response getWebVersionOfEmail(EmailContext context, @PathParam("template") String template);

	/**
	 * Send a test email
	 *
	 * @param context
	 *            {@link EmailContext}
	 * @return {@link Response} with response code
	 */
	@PUT
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	Response sendEmail(EmailContext context);

	/**
	 * Send an email with given template
	 *
	 * @param context
	 *            {@link EmailContext}
	 * @param template
	 *            template name
	 * @return {@link Response} with response code
	 */
	@PUT
	@Path("{template}")
	Response sendEmail(EmailContext context, @PathParam("template") String template);
}
