/**
 *
 */
package bobkubista.examples.services.api.email;

import java.io.File;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import bobkubista.examples.services.api.email.model.EmailContext;

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
     * /**
     *
     * @param template
     *            the template to get
     * @return the template file
     */
    @Path("{template}")
    @POST
    Response saveTemplate(@PathParam("template") final String template, final File file);

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
