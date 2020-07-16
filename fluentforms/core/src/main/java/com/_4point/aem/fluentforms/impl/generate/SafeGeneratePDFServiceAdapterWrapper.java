package com._4point.aem.fluentforms.impl.generate;

import com._4point.aem.fluentforms.api.Document;
import com._4point.aem.fluentforms.api.generate.GeneratePDFService.GeneratePDFServiceException;
import com._4point.aem.fluentforms.api.generate.CreatePDFResult;

import java.util.Objects;

public class SafeGeneratePDFServiceAdapterWrapper implements TraditionalGeneratePDFService {

    private final TraditionalGeneratePDFService generatePDFService;

    public SafeGeneratePDFServiceAdapterWrapper(TraditionalGeneratePDFService generatePDFService){
        super();
        this.generatePDFService = generatePDFService;
    }

    @Override
    public CreatePDFResult createPDF2(Document inputDoc, String inputFileExtension, String fileTypeSettings, String pdfSettings, String securitySettings, Document settingsDoc, Document xmpDoc) throws GeneratePDFServiceException {
        Objects.requireNonNull(inputDoc, "inputDoc cannot be null");
        Objects.requireNonNull(inputFileExtension, "inputFileExtension cannot be null");
        Objects.requireNonNull(fileTypeSettings, "fileTypeSettings cannot be null");
        Objects.requireNonNull(pdfSettings, "pdfSettings cannot be null");
        Objects.requireNonNull(securitySettings, "securitySettings cannot be null");
        return generatePDFService.createPDF2(inputDoc, inputFileExtension, fileTypeSettings, pdfSettings, securitySettings, settingsDoc, xmpDoc);
    }

    public TraditionalGeneratePDFService getGeneratePDFService() {
        return generatePDFService;
    }
}
