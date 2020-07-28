package com._4point.aem.docservices.rest_services.server.generate;


import com._4point.aem.docservices.rest_services.server.TestUtils;
import com._4point.aem.fluentforms.api.Document;
import com._4point.aem.fluentforms.api.DocumentFactory;

import com._4point.aem.fluentforms.api.generate.CreatePDFResult;
import com._4point.aem.fluentforms.impl.generate.TraditionalGeneratePDFService;
import com._4point.aem.fluentforms.testing.MockDocumentFactory;
import com._4point.aem.fluentforms.testing.generate.MockTraditionalGeneratePDFService;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.org.lidalia.slf4jtest.TestLogger;
import uk.org.lidalia.slf4jtest.TestLoggerFactory;

import javax.servlet.ServletException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(AemContextExtension.class)
@ExtendWith(MockitoExtension.class)
public class CreatePDF2Test {

    private static final String DATA_PARAM_NAME = "data";
    private static final String PDF_PARAM_NAME = "pdf";

    private static final String APPLICATION_XML = "application/xml";
    private static final String APPLICATION_PDF = "application/pdf";

    private final CreatePDF2 underTest =  new CreatePDF2();

    private final AemContext aemContext = new AemContext();

    private TestLogger loggerCapture = TestLoggerFactory.getTestLogger(CreatePDF2.class);

    private MockDocumentFactory mockDocumentFactory = new MockDocumentFactory();

    @Test
    void testDoPost_HappyPath() throws ServletException, IOException, NoSuchFieldException {
        String resultData = "testDoPost Happy Path Result";
        String templateData = TestUtils.SAMPLE_FORM.toString();

        String logData = "testDoPost Happy Path Log";

        byte[] resultDataBytes = resultData.getBytes();
        byte[] logDataBytes = logData.getBytes();
        MockTraditionalGeneratePDFService createPDF2Mock = mockCreatePDF2(resultDataBytes, logDataBytes);


        MockSlingHttpServletRequest request = new MockSlingHttpServletRequest(aemContext.bundleContext());
        MockSlingHttpServletResponse response = new MockSlingHttpServletResponse();

        underTest.doPost(request, response);

        // Validate the result.
        //assertEquals(SlingHttpServletResponse.SC_OK, response.getStatus());
        //assertEquals(APPLICATION_XML, response.getContentType());

        FileWriter writer = new FileWriter("createPDF2_response.txt");
        writer.write(response.getOutputAsString());
        writer.close();

    }

    public MockTraditionalGeneratePDFService mockCreatePDF2(byte[] resultDataBytes, byte[] logDataBytes) throws NoSuchFieldException {
        Document dataDoc = mockDocumentFactory.create(resultDataBytes);
        Document logDoc = mockDocumentFactory.create(logDataBytes);

        CreatePDFResult createPDFResult = new CreatePDFResult() {
            @Override
            public Document getCreatedDocument() {
                return dataDoc;
            }

            @Override
            public Document getLogDocument() {
                return logDoc;
            }
        };

        MockTraditionalGeneratePDFService createPDF2Mock = new MockTraditionalGeneratePDFService();

        createPDF2Mock.setCreatePDFResult(createPDFResult);

        junitx.util.PrivateAccessor.setField(underTest, "generatePDFServiceFactory", (Supplier<TraditionalGeneratePDFService>)()->(TraditionalGeneratePDFService)createPDF2Mock);

        return createPDF2Mock;
    }

}
