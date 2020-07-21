package com._4point.aem.docservices.rest_services.server.generate;

import com._4point.aem.docservices.rest_services.server.Exceptions;
import com._4point.aem.fluentforms.api.Document;
import com._4point.aem.fluentforms.api.DocumentFactory;
import com._4point.aem.fluentforms.api.generate.GeneratePDFService;
import com._4point.aem.fluentforms.impl.generate.AdobeGeneratePDFServiceAdapter;
import com._4point.aem.fluentforms.impl.generate.GeneratePDFServiceImpl;
import com._4point.aem.fluentforms.impl.generate.TraditionalGeneratePDFService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.function.Supplier;

import static com._4point.aem.docservices.rest_services.server.FormParameters.getMandatoryParameter;
import static com._4point.aem.docservices.rest_services.server.FormParameters.getOptionalParameter;

@Component(service=Servlet.class, property={Constants.SERVICE_DESCRIPTION + "=GeneratePDFService.CreatePDF2 Service",
        "sling.servlet.methods=" + HttpConstants.METHOD_POST})
@SlingServletPaths("/services/GeneratePDFService_CreatePDF2")

public class CreatePDF2 extends SlingAllMethodsServlet {

    private static final Logger log = LoggerFactory.getLogger(CreatePDF2.class);

    private final DocumentFactory docFactory = DocumentFactory.getDefault();
    private Supplier<TraditionalGeneratePDFService> generatePDFServiceFactory = this::getAdobeGeneratePDFService;
    private ResourceResolver resourceResolver;

    @Reference
    private com.adobe.pdfg.service.api.GeneratePDFService adobeGeneratePDFService;

    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {
        try {
            this.resourceResolver = request.getResourceResolver();
            this.processInput(request, response);
        } catch (Exceptions.BadRequestException br) {
            log.warn("Bad Request from the user.", br);
            response.sendError(SlingHttpServletResponse.SC_BAD_REQUEST, br.getMessage());
        } catch (Exceptions.InternalServerErrorException ise) {
            log.error("Internal server error.", ise);
            response.sendError(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR, ise.getMessage());
        } catch (Exceptions.NotAcceptableException nae) {
            log.error("NotAcceptable error.", nae);
            response.sendError(SlingHttpServletResponse.SC_NOT_ACCEPTABLE, nae.getMessage());
        } catch (Exception e) {  			// Some exception we haven't anticipated.
            log.error(e.getMessage() != null ? e.getMessage() : e.getClass().getName() , e);	// Make sure this gets into our log.
            throw e;
        }
    }

    private void processInput(SlingHttpServletRequest request, SlingHttpServletResponse response) throws Exceptions.BadRequestException, Exceptions.InternalServerErrorException, Exceptions.NotAcceptableException {
        GeneratePDFService generatePDFService = new GeneratePDFServiceImpl(generatePDFServiceFactory.get(), null);

        Document inputDoc;
        String inputFileExtension;
        String fileTypeSettings;
        String pdfSettings;
        String securitySettings;
        Document settingsDoc;
        Document xmpDoc;


    }

    private TraditionalGeneratePDFService getAdobeGeneratePDFService() {
        return new AdobeGeneratePDFServiceAdapter(adobeGeneratePDFService);
    }

    private static class ReaderExtensionsParameters {
        private static final String INPUT_DOC_PARAM = "inputDoc";
        private static final String INPUT_FILE_EXTENSION_PARAM = "inputFileExtension";
        private static final String FILE_TYPE_SETTINGS_PARAM = "fileTypeSettings";
        private static final String PDF_SETTINGS_PARAM = "pdfSettings";
        private static final String SECURITY_SETTINGS_PARAM = "securitySettings";
        private static final String SETTINGS_DOC_PARAM = "settingsDoc";
        private static final String XMP_DOC_PARAM = "xmpDoc";

        private byte[] inputDoc = null;

        private String inputFileExtension = null;
        private String fileTypeSettings = null;
        private String pdfSettings = null;
        private String securitySettings = null;
        private byte[] settingsDoc = null;
        private byte[] xmpDoc = null;

        public ReaderExtensionsParameters(byte[] inputDoc){
            super();
            this.inputDoc = inputDoc;
        }

        public byte[] getInputDoc() { return inputDoc; }

        public String getInputFileExtension() { return inputFileExtension; }

        public ReaderExtensionsParameters setInputFileExtension(String inputFileExtension) {
            this.inputFileExtension = inputFileExtension;
            return this;
        }

        public String getFileTypeSettings() { return fileTypeSettings; }

        public ReaderExtensionsParameters setFileTypeSettings(String fileTypeSettings) {
            this.fileTypeSettings = fileTypeSettings;
            return this;
        }

        public String getPDFSettings() { return pdfSettings; }

        public ReaderExtensionsParameters setPDFSettings(String pdfSettings) {
            this.pdfSettings = pdfSettings;
            return this;
        }

        public String getSecuritySettings() { return securitySettings; }

        public ReaderExtensionsParameters setSecuritySettings(String securitySettings) {
            this.securitySettings = securitySettings;
            return this;
        }

        public byte[] getSettingsDoc() { return settingsDoc; }

        public ReaderExtensionsParameters setSettingsDoc(byte[] settingsDoc) {
            this.settingsDoc = settingsDoc;
            return this;
        }

        public byte[] setXMPDoc() { return xmpDoc; }

        public ReaderExtensionsParameters setXMPDoc(byte[] xmpDoc) {
            this.xmpDoc = xmpDoc;
            return this;
        }

        public static ReaderExtensionsParameters readReaderExtensionsParameters(SlingHttpServletRequest request) throws Exceptions.BadRequestException {
            try {
                byte[] inputDoc = getMandatoryParameter(request, INPUT_DOC_PARAM).get();

                ReaderExtensionsParameters result = new ReaderExtensionsParameters(inputDoc);

                getOptionalParameter(request, INPUT_FILE_EXTENSION_PARAM).ifPresent(rp->result.setInputFileExtension(rp.getString()));
                getOptionalParameter(request, FILE_TYPE_SETTINGS_PARAM).ifPresent(rp->result.setFileTypeSettings(rp.getString()));
                getOptionalParameter(request, PDF_SETTINGS_PARAM).ifPresent(rp->result.setPDFSettings(rp.getString()));
                getOptionalParameter(request, SECURITY_SETTINGS_PARAM).ifPresent(rp->result.setSecuritySettings(rp.getString()));
                getOptionalParameter(request, SETTINGS_DOC_PARAM).ifPresent(rp->result.setSettingsDoc(rp.get()));
                getOptionalParameter(request, XMP_DOC_PARAM).ifPresent(rp->result.setXMPDoc(rp.get()));

                return result;
            } catch (IllegalArgumentException e) {
                throw new Exceptions.BadRequestException("There was a problem with one of the incoming Reader Extensions parameters.", e);
            }
        }

    }


}
