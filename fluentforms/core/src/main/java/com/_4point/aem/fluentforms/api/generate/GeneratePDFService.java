package com._4point.aem.fluentforms.api.generate;

import com._4point.aem.fluentforms.api.Document;
import com._4point.aem.fluentforms.api.generate.CreatePDFResult;

public interface GeneratePDFService {

    CreatePDFResult createPDF2(Document inputDoc, String inputFileExtension, String fileTypeSettings, String pdfSettings, String securitySettings, Document settingsDoc, Document xmpDoc)
            throws GeneratePDFServiceException;

    public static class GeneratePDFServiceException extends Exception {

        private static final long serialVersionUID = -9187778886719471016L;

        public GeneratePDFServiceException() {
            super();
        }

        public GeneratePDFServiceException(String arg0, Throwable arg1) {
            super(arg0, arg1);
        }

        public GeneratePDFServiceException(String arg0) {
            super(arg0);
        }

        public GeneratePDFServiceException(Throwable arg0) {
            super(arg0);
        }
    }

}
