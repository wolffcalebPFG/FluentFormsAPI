package com._4point.aem.fluentforms.impl.generate;

import com._4point.aem.fluentforms.api.Document;
import com._4point.aem.fluentforms.api.DocumentFactory;
import com._4point.aem.fluentforms.api.generate.CreatePDFResult;
import com._4point.aem.fluentforms.api.generate.GeneratePDFService.GeneratePDFServiceException;
import com._4point.aem.fluentforms.impl.AdobeDocumentFactoryImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class AdobeGeneratePDFServiceAdapter implements TraditionalGeneratePDFService{

    private static final Logger log = LoggerFactory.getLogger(AdobeGeneratePDFServiceAdapter.class);

    private final DocumentFactory documentFactory;

    private final com.adobe.pdfg.service.api.GeneratePDFService adobeGeneratePDFService;

    public AdobeGeneratePDFServiceAdapter(com.adobe.pdfg.service.api.GeneratePDFService adobeGeneratePDFService) {
        super();
        this.documentFactory = DocumentFactory.getDefault();
        this.adobeGeneratePDFService = Objects.requireNonNull(adobeGeneratePDFService, "adobeGeneratePDFService is null.");
    }

    public AdobeGeneratePDFServiceAdapter(DocumentFactory documentFactory, com.adobe.pdfg.service.api.GeneratePDFService adobeGeneratePDFService) {
        super();
        this.documentFactory = Objects.requireNonNull(documentFactory, "documentFactory is null");
        this.adobeGeneratePDFService = Objects.requireNonNull(adobeGeneratePDFService, "adobeGeneratePDFService is null");
    }

    @Override
    public CreatePDFResult createPDF2(Document inputDoc, String inputFileExtension, String fileTypeSettings, String pdfSettings, String securitySettings, Document settingsDoc, Document xmpDoc)
            throws GeneratePDFServiceException {
        try {
            return new CreatePDFResultImpl(adobeGeneratePDFService.createPDF2(AdobeDocumentFactoryImpl.getAdobeDocument(inputDoc), inputFileExtension, fileTypeSettings, pdfSettings, securitySettings, AdobeDocumentFactoryImpl.getAdobeDocument(settingsDoc), AdobeDocumentFactoryImpl.getAdobeDocument(xmpDoc)));
        } catch(Exception e) {
            throw new GeneratePDFServiceException(e.getMessage());
        }
    }

}
