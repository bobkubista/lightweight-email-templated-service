/**
 *
 */
package com.bobkubista.services.email.api.model;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.junit.Assert;
import org.junit.Test;

import com.bobkubista.services.email.api.model.DateReplacement;
import com.bobkubista.services.email.api.model.EmailContext;
import com.bobkubista.services.email.api.model.LinkReplacement;
import com.bobkubista.services.email.api.model.EmailContext.EmailBuilder;

/**
 * @author Bob
 *
 */
public class EmailContextTest {

    @Test
    public void testBuilder() throws URISyntaxException {
        final EmailContext emailContext = new EmailBuilder("bla@foo.bar", "foobar").addReplacement(new DateReplacement(new Date()))
                .addReplacement(new LinkReplacement(new URI("http://bla.bla"))).build();

        Assert.assertNotNull(emailContext);
        Assert.assertEquals("bla@foo.bar", emailContext.getRecipient());
        Assert.assertEquals("foobar", emailContext.getSubject());
        Assert.assertEquals(3, emailContext.getReplacements().size());
    }

    @Test
    public void testMarshalling() throws URISyntaxException, JAXBException, IOException {
        final EmailContext emailContext = new EmailBuilder("bla@foo.bar", "foobar").addReplacement(new DateReplacement(new Date()))
                .addReplacement(new LinkReplacement(new URI("http://bla.bla"))).build();
        final JAXBContext jaxbCtx = JAXBContext.newInstance(EmailContext.class);
        final Marshaller marshaller = jaxbCtx.createMarshaller();
        marshaller.marshal(emailContext, System.out);
    }

}
