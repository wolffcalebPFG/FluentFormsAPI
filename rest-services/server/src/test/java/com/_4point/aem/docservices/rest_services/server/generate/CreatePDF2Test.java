package com._4point.aem.docservices.rest_services.server.generate;


import com._4point.aem.fluentforms.api.DocumentFactory;

import com._4point.aem.fluentforms.testing.MockDocumentFactory;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.org.lidalia.slf4jtest.TestLogger;
import uk.org.lidalia.slf4jtest.TestLoggerFactory;

@ExtendWith(AemContextExtension.class)
@ExtendWith(MockitoExtension.class)
public class CreatePDF2Test {

    private static final String DATA_PARAM_NAME = "data";
    private static final String PDF_PARAM_NAME = "pdf";

    private static final String APPLICATION_XML = "application/xml";
    private static final String APPLICATION_PDF = "application/pdf";

    private final AemContext aemContext = new AemContext();

    private TestLogger loggerCapture = TestLoggerFactory.getTestLogger(CreatePDF2.class);

    private MockDocumentFactory mockDocumentFactory = new MockDocumentFactory();

}
