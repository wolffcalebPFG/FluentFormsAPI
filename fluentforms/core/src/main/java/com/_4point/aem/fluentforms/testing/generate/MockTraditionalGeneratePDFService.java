package com._4point.aem.fluentforms.testing.generate;

import com._4point.aem.fluentforms.api.Document;
import com._4point.aem.fluentforms.api.DocumentFactory;
import com._4point.aem.fluentforms.api.generate.CreatePDFResult;
import com._4point.aem.fluentforms.api.generate.GeneratePDFService;
import com._4point.aem.fluentforms.impl.generate.TraditionalGeneratePDFService;
import com._4point.aem.fluentforms.testing.MockDocumentFactory;

public class MockTraditionalGeneratePDFService implements TraditionalGeneratePDFService {
    private final DocumentFactory documentFactory;
    private final Document DUMMY_DOCUMENT;

    public MockTraditionalGeneratePDFService() {
        super();
        this.documentFactory = new MockDocumentFactory();
        this.DUMMY_DOCUMENT = documentFactory.create(new byte[0]);
    }

    public MockTraditionalGeneratePDFService(DocumentFactory documentFactory) {
        super();
        this.documentFactory = documentFactory;
        this.DUMMY_DOCUMENT = documentFactory.create(new byte[0]);
    }

    @Override
    public CreatePDFResult createPDF2(Document inputDoc, String inputFileExtension, String fileTypeSettings, String pdfSettings, String securitySettings, Document settingsDoc, Document xmpDoc)
            throws GeneratePDFService.GeneratePDFServiceException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("createPDF2 has not been implemented yet.");
    }
}
