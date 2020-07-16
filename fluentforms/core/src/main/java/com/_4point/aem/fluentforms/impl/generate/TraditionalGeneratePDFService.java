package com._4point.aem.fluentforms.impl.generate;

import com._4point.aem.fluentforms.api.Document;
import com._4point.aem.fluentforms.api.generate.CreatePDFResult;
import com._4point.aem.fluentforms.api.generate.GeneratePDFService.GeneratePDFServiceException;

public interface TraditionalGeneratePDFService {

    CreatePDFResult createPDF2(Document inputDoc, String inputFileExtension, String fileTypeSettings, String pdfSettings, String securitySettings, Document settingsDoc, Document xmpDoc)
            throws GeneratePDFServiceException;

}
