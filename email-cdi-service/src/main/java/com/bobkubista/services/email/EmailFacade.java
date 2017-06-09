/**
 *
 */
package com.bobkubista.services.email;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.Path;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bobkubista.services.email.api.EmailApi;
import com.bobkubista.services.email.api.model.EmailContext;
import com.bobkubista.services.email.strategy.EmailStrategy;
import com.bobkubista.services.email.strategy.TemplateEmailStrategy;
import com.bobkubista.services.email.strategy.TestEmailStrategy;
import com.bobkubista.services.email.strategy.WebEmailStrategy;

import bobkubista.example.utils.property.ServerProperties;

/**
 * @author Bob
 *
 */
@Path("/")
public class EmailFacade implements EmailApi {

	private static final String EMAIL_TEMPLATE_LOCATION = "email.template.location";
	private static final Logger LOGGER = LoggerFactory.getLogger(EmailFacade.class);

	@Override
	public Response deleteTemplate(final String template) {
		final String templateFile = ServerProperties.get()
				.getString(EMAIL_TEMPLATE_LOCATION) + template + ".tmpl";
		if (new File(templateFile).delete()) {
			return Response.ok()
					.build();
		} else {
			return Response.status(Status.NOT_FOUND)
					.build();
		}

	}

	@Override
	public Response getTemplate(final String template) {
		return Response.ok(new File(ServerProperties.get()
				.getString(EMAIL_TEMPLATE_LOCATION) + template + ".tmpl"))
				.build();

	}

	@Override
	public Response getTemplates() {
		final File[] listFiles = new File(ServerProperties.get()
				.getString(EMAIL_TEMPLATE_LOCATION)).listFiles();
		final List<String> filesNames = Arrays.stream(listFiles)
				.filter(File::isFile)
				.map(File::getName)
				.map(name -> name.substring(0, name.lastIndexOf('.')))
				.collect(Collectors.toList());
		final GenericEntity<List<String>> entity = new GenericEntity<List<String>>(filesNames) {
		};
		return Response.ok(entity)
				.build();
	}

	@Override
	public Response getWebVersionOfEmail(final EmailContext context) {
		LOGGER.info("Viewing web version email to {}", context.getRecipient());
		return this.buildResponse(new TestEmailStrategy(context)::getWebVersion);
	}

	@Override
	public Response getWebVersionOfEmail(final EmailContext context, final String template) {
		LOGGER.info("Viewing web version of {} email to {}", template, context.getRecipient());
		return this.buildResponse(new TemplateEmailStrategy(context, template)::getWebVersion);
	}

	@Override
	public Response sendEmail(final EmailContext context) {
		LOGGER.info("Sending email to {}", context.getRecipient());
		return this.buildResponse(new TestEmailStrategy(context)::send);
	}

	@Override
	public Response sendEmail(final EmailContext context, final String template) {
		LOGGER.info("Sending {} email to {}", template, context.getRecipient());
		return this.buildResponse(new TemplateEmailStrategy(context, template)::send);
	}

	private Response buildResponse(final EmailStrategy strategy) {
		if (strategy.send()) {
			return Response.ok()
					.build();
		} else {
			return Response.serverError()
					.build();
		}
	}

	private Response buildResponse(final WebEmailStrategy stategy) {
		return Response.ok(stategy.getWebVersion())
				.build();
	}
}
