/**
 *
 */
package bobkubista.examples.services.rest.cdi.email;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bobkubista.example.utils.property.ServerProperties;
import bobkubista.examples.services.api.email.EmailApi;
import bobkubista.examples.services.api.email.model.EmailContext;
import bobkubista.examples.services.rest.cdi.email.strategy.EmailStrategy;
import bobkubista.examples.services.rest.cdi.email.strategy.TemplateEmailStrategy;
import bobkubista.examples.services.rest.cdi.email.strategy.TestEmailStrategy;
import bobkubista.examples.services.rest.cdi.email.strategy.WebEmailStrategy;

/**
 * @author Bob
 *
 */
@Path("/")
public class EmailFacade implements EmailApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailFacade.class);

    @Override
    public Response deleteTemplate(final String template) {
        final String templateFile = ServerProperties.get()
                .getString("email.template.location") + template + ".tmpl";
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
                .getString("email.template.location") + template + ".tmpl"))
                .build();

    }

    @Override
    public Response getTemplates() {
        return Response.ok(new File(ServerProperties.get()
                .getString("email.template.location")).listFiles())
                .build();
    }

    @Override
    public Response getWebVersionOfEmail(final EmailContext context) {
        LOGGER.info("Viewing web version email to {}", context.getRecipient());
        return this.buildResponse(() -> new TestEmailStrategy(context).getWebVersion());
    }

    @Override
    public Response getWebVersionOfEmail(final EmailContext context, final String template) {
        LOGGER.info("Viewing web version of {} email to {}", template, context.getRecipient());
        return this.buildResponse(() -> new TemplateEmailStrategy(context, template).getWebVersion());
    }

    @Override
    public Response saveTemplate(final String template, final InputStream fos) throws IOException {
        try {
            OutputStream out = null;
            int read = 0;
            final byte[] bytes = new byte[1024];

            out = new FileOutputStream(new File(ServerProperties.get()
                    .getString("email.template.location") + File.separator + template + ".tmpl"));
            while ((read = fos.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            out.flush();
            out.close();
        } catch (final IOException e) {
            LOGGER.error("Uploaded template File not found", e);
            return Response.serverError()
                    .build();
        } finally {
            fos.close();
        }
        return Response.ok()
                .build();
    }

    @Override
    public Response sendEmail(final EmailContext context) {
        LOGGER.info("Sending email to {}", context.getRecipient());
        return this.buildResponse(() -> new TestEmailStrategy(context).send());
    }

    @Override
    public Response sendEmail(final EmailContext context, final String template) {
        LOGGER.info("Sending {} email to {}", template, context.getRecipient());
        return this.buildResponse(() -> new TemplateEmailStrategy(context, template).send());
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
