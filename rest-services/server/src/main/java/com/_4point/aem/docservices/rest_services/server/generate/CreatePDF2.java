package com._4point.aem.docservices.rest_services.server.generate;

import com._4point.aem.docservices.rest_services.server.AcceptHeaders;
import com._4point.aem.docservices.rest_services.server.ContentType;
import com._4point.aem.docservices.rest_services.server.Exceptions;
import com._4point.aem.docservices.rest_services.server.ServletUtils;
import com._4point.aem.docservices.rest_services.server.forms.RenderPdfForm;
import com._4point.aem.fluentforms.api.Document;
import com._4point.aem.fluentforms.api.DocumentFactory;
import com._4point.aem.fluentforms.api.generate.CreatePDFResult;
import com._4point.aem.fluentforms.api.generate.GeneratePDFService;
import com._4point.aem.fluentforms.impl.generate.AdobeGeneratePDFServiceAdapter;
import com._4point.aem.fluentforms.impl.generate.GeneratePDFServiceImpl;
import com._4point.aem.fluentforms.impl.generate.TraditionalGeneratePDFService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
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
import org.w3c.dom.Attr;
import org.w3c.dom.Element;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Base64;
import java.util.List;
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

        log.info("In REST Services Client processInput.");

        GeneratePDFService generatePDFService = new GeneratePDFServiceImpl(generatePDFServiceFactory.get(), null);

        CreatePDF2.ReaderExtensionsParameters reqParameters = CreatePDF2.ReaderExtensionsParameters.readReaderExtensionsParameters(request);	// TODO: Make the validation of XML a config parameter.

        Document inputDoc = docFactory.create(reqParameters.getInputDoc());
        String inputFileExtension = reqParameters.getInputFileExtension();
        String fileTypeSettings = reqParameters.getFileTypeSettings();
        String pdfSettings = reqParameters.getPDFSettings();
        String securitySettings = reqParameters.getSecuritySettings();
        Document settingsDoc = docFactory.create(reqParameters.getSettingsDoc());
        Document xmpDoc = docFactory.create(reqParameters.getXMPDoc());

        /*CreatePDFResult result = null;
        try {
            log.info("before the AEM call");
            result = generatePDFService.createPDF2(inputDoc, inputFileExtension, fileTypeSettings, pdfSettings, securitySettings, settingsDoc, xmpDoc);
            log.info("after the AEM call");
        } catch (GeneratePDFService.GeneratePDFServiceException e) {
            throw new Exceptions.InternalServerErrorException("Error calling Adobe API.", e);
        }

        String resultXML = null;
        try {
            log.info("before convert to XML");
            resultXML = convertCreatePDFResultToXML(result);
            log.info("after convert to XML");
        } catch (ParserConfigurationException | TransformerException e) {
            throw new Exceptions.InternalServerErrorException("Error converting result to XML.", e);
        }

        log.info("XML: " + resultXML);*/

        String contentType = ContentType.APPLICATION_XML.toString();	// We know the result is always PDF.
        ServletUtils.validateAcceptHeader(request.getHeader(AcceptHeaders.ACCEPT_HEADER_STR), contentType);
        response.setContentType(contentType);
        try {
            response.getWriter().write("<test><message>ok</message></test>");
        } catch (IOException e) {
            throw new Exceptions.InternalServerErrorException("Error writing XML to response.", e);
        }

    }

    private static String convertCreatePDFResultToXML(CreatePDFResult createPDFResult) throws ParserConfigurationException, Exceptions.InternalServerErrorException, TransformerException {

        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
        org.w3c.dom.Document document = documentBuilder.newDocument();
        org.w3c.dom.Element root = document.createElement("createPDFResult");
        document.appendChild(root);

        createElementWithAttribute(document, root, "dataDoc", "value", toBase64String(createPDFResult.getCreatedDocument()));
        createElementWithAttribute(document, root, "logDoc", "value", toBase64String(createPDFResult.getLogDocument()));

        return domToString(document);
    }

    private static String domToString(org.w3c.dom.Document document)
            throws TransformerConfigurationException, TransformerFactoryConfigurationError, TransformerException {
        DOMSource domSource = new DOMSource(document);
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        StringWriter sw = new StringWriter();
        StreamResult sr = new StreamResult(sw);
        transformer.transform(domSource, sr);
        return sw.toString();
    }

    private static void addMapOfResultDocInXml(String docName, Document resultDoc, org.w3c.dom.Document document, Element root) {
        try {
            Element result = document.createElement("resultDocument");
            root.appendChild(result);
            Attr attr = document.createAttribute("documentName");
            attr.setValue(docName);
            result.setAttributeNode(attr);
            if (resultDoc != null) {
                byte[] concatenatedPdf = null;
                concatenatedPdf = IOUtils.toByteArray(resultDoc.getInputStream());
                if (concatenatedPdf != null) {
                    String doc = Base64.getEncoder().encodeToString(concatenatedPdf);
                    Element generatedDoc = document.createElement("generatedDoc");
                    generatedDoc.appendChild(document.createTextNode(doc));
                    result.appendChild(generatedDoc);
                }
            }
        } catch (IOException e) {
            String msg = e.getMessage();
            throw new IllegalStateException("Error while reading result document. (" + (msg == null ? e.getClass().getName() : msg) + ").", e);
        }
    }

    private static String toBase64String(Document doc) throws Exceptions.InternalServerErrorException {
        try {
            return Base64.getEncoder().encodeToString(IOUtils.toByteArray(doc.getInputStream()));
        } catch (IOException e) {
            String msg = e.getMessage();
            throw new Exceptions.InternalServerErrorException("Error while reading job log from assembler result. (" + (msg == null ? e.getClass().getName() : msg) + ").", e);
        }
    }

    private static void createElementWithAttribute(org.w3c.dom.Document document, Element root, String parentElementName,
                                                   String attributeName, String property) {
        Element parentElement = document.createElement(parentElementName);
        root.appendChild(parentElement);
        Attr attr = document.createAttribute(attributeName);
        attr.setValue(property);
        parentElement.setAttributeNode(attr);

    }

    private static void createElementList(org.w3c.dom.Document document, org.w3c.dom.Element root,
                                          String parentElementName, String childElementName, List<String> stringList) {
        Element elementName = document.createElement(parentElementName);
        root.appendChild(elementName);
        if (CollectionUtils.isNotEmpty(stringList)) {
            stringList.forEach(resultPropertyName -> createElement(document, elementName, childElementName,
                    resultPropertyName));
        }
    }

    private static void createElement(org.w3c.dom.Document document, Element Parent, String elementName,
                                      String elementValue) {
        Element element = document.createElement(elementName);
        element.appendChild(document.createTextNode(elementValue));
        Parent.appendChild(element);
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

        public byte[] getXMPDoc() { return xmpDoc; }

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
