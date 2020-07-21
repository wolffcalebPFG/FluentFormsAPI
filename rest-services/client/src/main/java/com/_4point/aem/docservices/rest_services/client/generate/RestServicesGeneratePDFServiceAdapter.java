package com._4point.aem.docservices.rest_services.client.generate;

import com._4point.aem.docservices.rest_services.client.helpers.Builder;
import com._4point.aem.docservices.rest_services.client.helpers.BuilderImpl;
import com._4point.aem.docservices.rest_services.client.helpers.RestServicesServiceAdapter;
import com._4point.aem.fluentforms.api.Document;
import com._4point.aem.fluentforms.api.generate.CreatePDFResult;
import com._4point.aem.fluentforms.api.generate.GeneratePDFService.GeneratePDFServiceException;
import com._4point.aem.fluentforms.impl.SimpleDocumentFactoryImpl;
import com._4point.aem.fluentforms.impl.generate.TraditionalGeneratePDFService;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.util.function.Supplier;

public class RestServicesGeneratePDFServiceAdapter extends RestServicesServiceAdapter implements TraditionalGeneratePDFService {


    private static final String CREATE_PDF_2_PATH="/services/GeneratePDFService/GeneratePDF";

    private RestServicesGeneratePDFServiceAdapter(WebTarget target) {
        super(target);
    }

    private RestServicesGeneratePDFServiceAdapter(WebTarget target, Supplier<String> correlationId) {
        super(target, correlationId);
    }

    @Override
    public CreatePDFResult createPDF2(Document inputDoc, String inputFileExtension, String fileTypeSettings, String pdfSettings, String securitySettings, Document settingsDoc, Document xmpDoc) throws GeneratePDFServiceException {
        if (inputDoc == null || inputFileExtension == null || fileTypeSettings == null || pdfSettings == null || securitySettings == null) {
            throw new GeneratePDFServiceException("Internal Error, must provide document and its dataformat");
        }

        WebTarget createPDF2Target = baseTarget.path(CREATE_PDF_2_PATH);
        try (final FormDataMultiPart multipart = new FormDataMultiPart()) {
            multipart.field("inputDoc", inputDoc.getInputStream(), APPLICATION_PDF)
                    .field("inputFileExtension", inputFileExtension)
                    .field("fileTypeSettings", fileTypeSettings)
                    .field("pdfSettings", pdfSettings)
                    .field("securitySettings", securitySettings)
                    .field("settingsDoc", settingsDoc.getInputStream(), APPLICATION_PDF)
                    .field("xmpDoc", xmpDoc.getInputStream(), APPLICATION_PDF);


        Response result = postToServer(createPDF2Target, multipart, MediaType.APPLICATION_XML_TYPE);//xml
        Response.StatusType resultStatus = result.getStatusInfo();

        if (!Response.Status.Family.SUCCESSFUL.equals(resultStatus.getFamily())) {
            String message = "Call to server failed, statusCode='" + resultStatus.getStatusCode() + "', reason='" + resultStatus.getReasonPhrase() + "'.";
            if (result.hasEntity()) {
                InputStream entityStream = (InputStream) result.getEntity();
                message += "\n" + inputStreamtoString(entityStream);
            }
            throw new GeneratePDFServiceException(message);
        }
        if (resultStatus.getStatusCode() != Response.Status.NO_CONTENT.getStatusCode()) {
            if (!result.hasEntity()) {
                // If the status code is not "No content" then we expect there to be an entity.  Throw an exception if there isn't one.
                throw new GeneratePDFServiceException("Call to server succeeded but server failed to return document.  This should never happen.");
            }
            String responseContentType = result.getHeaderString(HttpHeaders.CONTENT_TYPE);
            if (responseContentType == null) {
                String msg = "Response from AEM server was null.  "
                        + (responseContentType != null ? "content-type='" + responseContentType + "'"
                        : "content-type was null")
                        + ".";
                InputStream entityStream = (InputStream) result.getEntity();
                msg += "\n" + inputStreamtoString(entityStream);
                throw new GeneratePDFServiceException(msg);
            }

            Document resultDoc = SimpleDocumentFactoryImpl.getFactory().create((InputStream) result.getEntity());
            CreatePDFResult createPDFResultDoc = (CreatePDFResult) resultDoc;
            return createPDFResultDoc;
        } else {
            return (CreatePDFResult) SimpleDocumentFactoryImpl.emptyDocument();
        }

        } catch (IOException e) {
            throw new GeneratePDFServiceException("I/O Error while exporting data. (" + baseTarget.getUri().toString() + ").", e);
        } catch (RestServicesServiceException e) {
            throw new GeneratePDFServiceException("Error while POSTing to server", e);
        }
    }

    public static RestServicesGeneratePDFServiceAdapter.GeneratePDFServiceBuilder builder() {
        return new RestServicesGeneratePDFServiceAdapter.GeneratePDFServiceBuilder();
    }

    public static class GeneratePDFServiceBuilder implements Builder {
        private BuilderImpl builder = new BuilderImpl();
//		private final static Supplier<Client> defaultClientFactory = ()->ClientBuilder.newClient();

        private GeneratePDFServiceBuilder() {
            super();
        }

        @Override
        public RestServicesGeneratePDFServiceAdapter.GeneratePDFServiceBuilder machineName(String machineName) {
            builder.machineName(machineName);
            return this;
        }

        @Override
        public RestServicesGeneratePDFServiceAdapter.GeneratePDFServiceBuilder port(int port) {
            builder.port(port);
            return this;
        }

        @Override
        public RestServicesGeneratePDFServiceAdapter.GeneratePDFServiceBuilder useSsl(boolean useSsl) {
            builder.useSsl(useSsl);
            return this;
        }

        @Override
        public RestServicesGeneratePDFServiceAdapter.GeneratePDFServiceBuilder clientFactory(Supplier<Client> clientFactory) {
            builder.clientFactory(clientFactory);
            return this;
        }

        @Override
        public RestServicesGeneratePDFServiceAdapter.GeneratePDFServiceBuilder basicAuthentication(String username, String password) {
            builder.basicAuthentication(username, password);
            return this;
        }

        @Override
        public RestServicesGeneratePDFServiceAdapter.GeneratePDFServiceBuilder correlationId(Supplier<String> correlationIdFn) {
            builder.correlationId(correlationIdFn);
            return this;
        }

        @Override
        public Supplier<String> getCorrelationIdFn() {
            return builder.getCorrelationIdFn();
        }

        @Override
        public WebTarget createLocalTarget() {
            return builder.createLocalTarget();
        }

        public RestServicesGeneratePDFServiceAdapter build() {
            return new RestServicesGeneratePDFServiceAdapter(this.createLocalTarget(), this.getCorrelationIdFn());
        }
    }
}
