package com._4point.aem.fluentforms.impl.generate;

import com._4point.aem.fluentforms.api.Document;
import com._4point.aem.fluentforms.api.DocumentFactory;
import com._4point.aem.fluentforms.api.generate.CreatePDFResult;

public class CreatePDFResultImpl implements CreatePDFResult {

    private final Document document;
    private final Document createPDFResultLogDoc;

    protected CreatePDFResultImpl(com.adobe.pdfg.result.CreatePDFResult createPDFResult) {
        super();
        this.document = DocumentFactory.getDefault().create(createPDFResult.getCreatedDocument());
        this.createPDFResultLogDoc = DocumentFactory.getDefault().create(createPDFResult.getLogDocument());
    }

    protected CreatePDFResultImpl(com.adobe.pdfg.result.CreatePDFResult createPDFResult, DocumentFactory documentFactory) {
        super();
        this.document = documentFactory.create(createPDFResult.getCreatedDocument());
        this.createPDFResultLogDoc = documentFactory.create(createPDFResult.getLogDocument());
    }

    @Override
    public Document getCreatedDocument() {
        return this.document;
    }

    @Override
    public Document getLogDocument() {
        return this.createPDFResultLogDoc;
    }

}
