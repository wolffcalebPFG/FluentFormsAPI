package com._4point.aem.fluentforms.impl.generate;

import com._4point.aem.fluentforms.api.Document;
import com._4point.aem.fluentforms.api.generate.CreatePDFResult;
import com._4point.aem.fluentforms.api.generate.GeneratePDFService;
import com._4point.aem.fluentforms.impl.UsageContext;

public class GeneratePDFServiceImpl implements GeneratePDFService {

    private final TraditionalGeneratePDFService adobeGeneratePDFService;
    private final UsageContext usageContext;

    public GeneratePDFServiceImpl(TraditionalGeneratePDFService adobeGeneratePDFService, UsageContext usageContext) {
        super();
        this.adobeGeneratePDFService = new SafeGeneratePDFServiceAdapterWrapper(adobeGeneratePDFService);
        this.usageContext = usageContext;
    }

    public CreatePDFResult createPDF2(Document inputDoc, String inputFileExtension, String fileTypeSettings, String pdfSettings, String securitySettings, Document settingsDoc, Document xmpDoc) throws GeneratePDFService.GeneratePDFServiceException {
        return this.adobeGeneratePDFService.createPDF2(inputDoc, inputFileExtension, fileTypeSettings, pdfSettings, securitySettings, settingsDoc, xmpDoc);
    }
}
