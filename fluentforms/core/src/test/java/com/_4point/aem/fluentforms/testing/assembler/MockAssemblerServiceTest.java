package com._4point.aem.fluentforms.testing.assembler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com._4point.aem.fluentforms.api.Document;
import com._4point.aem.fluentforms.api.assembler.AssemblerOptionsSpec;
import com._4point.aem.fluentforms.api.assembler.AssemblerResult;
import com._4point.aem.fluentforms.impl.assembler.AdobeAssemblerServiceAdapter;
import com._4point.aem.fluentforms.testing.MockDocumentFactory;
import com._4point.aem.fluentforms.testing.assembler.MockTraditionalAssemblerService.GenerateAssemblerResultArgs;


public class MockAssemblerServiceTest {
	
	@SuppressWarnings("unchecked")
	@Test
	void testInvokeMockDocument() throws Exception {
		String expectedResultString = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><DDX xmlns=\"http://ns.adobe.com/DDX/1.0/\">\r\n" + 
				"<PDF result=\"concatenatedPDF.pdf\"><PDF source=\"File0.pdf\"/>\r\n" + 
				"<PDF source=\"File1.pdf\"/></PDF></DDX>";
		MockDocumentFactory mockDocumentFactory = new MockDocumentFactory();
		Document ddx = mockDocumentFactory.create(expectedResultString.getBytes());
		Map<String,Object>sourceDocuments =  Mockito.mock(Map.class);	
	    AssemblerResult assemblerResult =  Mockito.mock(AdobeAssemblerServiceAdapter.class);
	    MockAssemblerService underTest =MockAssemblerService.createAssemblerResultMock(assemblerResult);
		AssemblerResult result = underTest.invoke()
				 .executeOn(ddx, sourceDocuments);		
		GenerateAssemblerResultArgs capturedArgs = underTest.getGenerateAssemblerResultArgs();
		assertEquals(result,assemblerResult);
		assertEquals(ddx, capturedArgs.getDdx());
		assertEquals(sourceDocuments, capturedArgs.getSourceDocuments());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	void testInvokeMockDocumentDummyAssemblerResultProvided() throws Exception {
		String expectedResultString = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><DDX xmlns=\"http://ns.adobe.com/DDX/1.0/\">\r\n" + 
				"<PDF result=\"concatenatedPDF.pdf\"><PDF source=\"File0.pdf\"/>\r\n" + 
				"<PDF source=\"File1.pdf\"/></PDF></DDX>";
		MockDocumentFactory mockDocumentFactory = new MockDocumentFactory();
		Document ddx = mockDocumentFactory.create(expectedResultString.getBytes());
		Map<String,Object>sourceDocuments =  Mockito.mock(Map.class);
	    AssemblerOptionsSpec assemblerOptionsSpec = Mockito.mock(AssemblerOptionsSpec.class);
		MockTraditionalAssemblerService mockTraditionalAssemblerService = new MockTraditionalAssemblerService() ;
	    MockAssemblerService underTest = MockAssemblerService.createAssemblerResultMock(mockTraditionalAssemblerService.getDummyAssemblerResult());
		AssemblerResult result = underTest.invoke()
				                    .setFailOnError(false)
				                    .setDefaultStyle("abc")
				                    .setLogLevel("DEBUG")
				                    .setFirstBatesNumber(0)
				                    .setTakeOwnership(true)
				                    .setValidateOnly(true)
				    				.executeOn(ddx, sourceDocuments);
		GenerateAssemblerResultArgs capturedArgs = underTest.getGenerateAssemblerResultArgs();
		assertEquals(result, mockTraditionalAssemblerService.getDummyAssemblerResult());
		assertEquals(ddx, capturedArgs.getDdx());
		assertEquals(sourceDocuments, capturedArgs.getSourceDocuments());
		assertNotEquals(assemblerOptionsSpec, capturedArgs.getAssemblerOptionsSpec());
	}
	
	
	
	
}
