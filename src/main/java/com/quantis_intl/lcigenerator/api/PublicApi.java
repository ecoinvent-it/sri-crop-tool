/***************************************************************************
 * Quantis Sàrl CONFIDENTIAL
 * Unpublished Copyright (c) 2009-2014 Quantis SARL, All Rights Reserved.
 * NOTICE: All information contained herein is, and remains the property of Quantis Sàrl. The intellectual and
 * technical concepts contained herein are proprietary to Quantis Sàrl and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material is strictly forbidden unless prior written
 * permission is obtained from Quantis Sàrl. Access to the source code contained herein is hereby forbidden to anyone
 * except current Quantis Sàrl employees, managers or contractors who have executed Confidentiality and Non-disclosure
 * agreements explicitly covering such access.
 * The copyright notice above does not evidence any actual or intended publication or disclosure of this source code,
 * which includes information that is confidential and/or proprietary, and is a trade secret, of Quantis Sàrl.
 * ANY REPRODUCTION, MODIFICATION, DISTRIBUTION, PUBLIC PERFORMANCE, OR PUBLIC DISPLAY OF OR THROUGH USE OF THIS SOURCE
 * CODE WITHOUT THE EXPRESS WRITTEN CONSENT OF Quantis Sàrl IS STRICTLY PROHIBITED, AND IN VIOLATION OF APPLICABLE LAWS
 * AND INTERNATIONAL TREATIES. THE RECEIPT OR POSSESSION OF THIS SOURCE CODE AND/OR RELATED INFORMATION DOES NOT CONVEY
 * OR IMPLY ANY RIGHTS TO REPRODUCE, DISCLOSE OR DISTRIBUTE ITS CONTENTS, OR TO MANUFACTURE, USE, OR SELL ANYTHING THAT
 * IT MAY DESCRIBE, IN WHOLE OR IN PART.
 */
package com.quantis_intl.lcigenerator.api;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.escape.Escaper;
import com.google.common.html.HtmlEscapers;
import com.quantis_intl.lcigenerator.dao.RegistrationRequestDao;
import com.quantis_intl.lcigenerator.model.RegistrationRequest;
import com.quantis_intl.stack.mail.MailSender;

@Path("pub/")
public class PublicApi
{
    private static final Logger LOGGER = LoggerFactory.getLogger(PublicApi.class);

    private final RegistrationRequestDao registrationRequestDao;

    private final Provider<MailSender> mailSender;

    private final String formsMailTo;

    @Inject
    public PublicApi(Provider<MailSender> mailSender, @Named("forms.mail.to") String formsMailTo,
                     RegistrationRequestDao registrationRequestDao)
    {
        this.mailSender = mailSender;
        this.formsMailTo = formsMailTo;
        this.registrationRequestDao = registrationRequestDao;
    }

    @POST
    @Path("registrationRequest")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response sendRequest(@MultipartForm final RegistrationRequestForm form)
    {
        RegistrationRequest r = new RegistrationRequest(
                form.name,
                form.username,
                form.company,
                form.address,
                form.mail);

        registrationRequestDao.insertRequest(r);

        Escaper escaper = HtmlEscapers.htmlEscaper();
        final String escapedName = escaper.escape(form.name);
        final String escapedUsername = escaper.escape(form.username);
        final String escapedCompany = escaper.escape(form.company);
        final String escapedAddress = escaper.escape(form.address);
        final String escapedMail = escaper.escape(form.mail);

        // TODO: Have a default template stored somewhere and replace only some specific parts
        final String formContent = generateRegistrationRequestEmailTextFromForm(escapedName, escapedUsername,
                                                                                escapedCompany,
                                                                                escapedAddress,
                                                                                escapedMail);

        mailSender.get().sendMail(formsMailTo,
                                  "[ALCIG] Registration request",
                                  formContent);

        LOGGER.info("Email for registration request sent");

        return Response.ok().build();
    }

    @POST
    @Path("contactUs")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response contactUs(@MultipartForm final ContactUsForm form)
    {
        Escaper escaper = HtmlEscapers.htmlEscaper();
        final String escapedContactName = escaper.escape(form.contactName);
        final String escapedContactCompany = escaper.escape(form.contactCompany);
        final String escapedContactEmail = escaper.escape(form.contactEmail);
        final String escapedContactMessage = escaper.escape(form.contactMessage);

        // TODO: Have a default template stored somewhere and replace only some specific parts
        final String formContent = generateEmailTextFromContactForm(escapedContactName, escapedContactCompany,
                escapedContactEmail, escapedContactMessage, form.accept);

        mailSender.get().sendMail(formsMailTo, "[ALCIG] New feedback", formContent);

        if (isEmailComplient(escapedContactEmail))
            mailSender.get().sendMail(escapedContactEmail,
                    "[ALCIG] Thank you for your feedback",
                    generateTextForUser(escapedContactName, formContent));
        else
            LOGGER.info("Email not sent to user as the given email was not complient: {}", escapedContactEmail);

        return Response.ok().build();
    }

    private boolean isEmailComplient(String email)
    {
        // FIXME Find a better regExp or another email validation solution
        return email.matches("^([a-zA-Z0-9_\\.\\-+])+@(([a-zA-Z0-9-])+\\.)+([a-zA-Z0-9]{2,})+$");
    }

    private String generateTextForUser(String contactName, String messageFromForm)
    {
        return new StringBuilder("Dear ")
                .append(contactName.isEmpty() ? "user" : contactName)
                .append(",<br/><br/>")
                .append("Thank you for your message.")
                .append("<br/>We will get back to you as soon as possible.")
                .append("<br/><br/>Best regards,")
                .append("<br/><br/>The ALCIG team")
                .append("<br/><br/>---------------")
                .append("Sent message:---------------<br/>")
                .append(messageFromForm)
                .toString();
    }

    private String generateEmailTextFromContactForm(final String contactName, final String contactCompany,
            final String contactEmail, final String contactMessage, final boolean accept)
    {
        return new StringBuilder("Name: ")
                .append(contactName)
                .append("<br/>Company: ")
                .append(contactCompany)
                .append("<br/>Email: ")
                .append(contactEmail)
                .append("<br/>Accept the feedback to be made public : ")
                .append(accept ? "Yes" : "No")
                .append("<br/>Message: <br/>")
                .append(contactMessage)
                .toString();
    }

    private String generateRegistrationRequestEmailTextFromForm(String escapedName, String escapedUsername,
                                                                String escapedCompany,
                                                                String escapedAddress,
                                                                String escapedMail)
    {
        return new StringBuilder("Dear Sir or Madam,<br/>")
                .append("The following user just sent you a registration request :<br/><br/>")
                .append("<br/>Name : ")
                .append(escapedName)
                .append("<br/>Username : ")
                .append(escapedUsername)
                .append("<br/>Company : ")
                .append(escapedCompany)
                .append("<br/>Address : ")
                .append(escapedAddress)
                .append("<br/>Mail : ")
                .append(escapedMail)
                .toString();
    }

    public static class ContactUsForm
    {
        @FormParam("contactName")
        public String contactName;
        @FormParam("contactCompany")
        public String contactCompany;
        @FormParam("contactEmail")
        public String contactEmail;
        @FormParam("contactMessage")
        public String contactMessage;
        @FormParam("accept")
        public boolean accept;
    }

    public static class RegistrationRequestForm
    {
        @FormParam("name")
        public String name;
        @FormParam("username")
        public String username;
        @FormParam("company")
        public String company;
        @FormParam("address")
        public String address;
        @FormParam("mail")
        public String mail;
    }
}
