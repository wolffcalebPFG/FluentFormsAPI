package com._4point.aem.docservices.rest_services.client.generate;

import com._4point.aem.docservices.rest_services.client.helpers.Builder;
import com._4point.aem.docservices.rest_services.client.helpers.BuilderImpl;
import com._4point.aem.docservices.rest_services.client.helpers.RestServicesServiceAdapter;
import com._4point.aem.fluentforms.api.Document;
import com._4point.aem.fluentforms.api.generate.CreatePDFResult;
import com._4point.aem.fluentforms.api.generate.GeneratePDFService;
import com._4point.aem.fluentforms.impl.generate.TraditionalGeneratePDFService;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import java.util.function.Supplier;

public class RestServicesGeneratePDFServiceAdapter extends RestServicesServiceAdapter implements TraditionalGeneratePDFService {

    private RestServicesGeneratePDFServiceAdapter(WebTarget target) {
        super(target);
    }

    private RestServicesGeneratePDFServiceAdapter(WebTarget target, Supplier<String> correlationId) {
        super(target, correlationId);
    }

    @Override
    public CreatePDFResult createPDF2(Document inputDoc, String inputFileExtension, String fileTypeSettings, String pdfSettings, String securitySettings, Document settingsDoc, Document xmpDoc) throws GeneratePDFService.GeneratePDFServiceException {
        throw new UnsupportedOperationException("createPDF2 has not been implemented yet.");
    }

    public static RestServicesGeneratePDFServiceAdapter.GeneratePDFServiceBuilder builder() {
        return new RestServicesGeneratePDFServiceAdapter.GeneratePDFServiceBuilder();
    }

    public static class GeneratePDFServiceBuilder implements Builder {
        private BuilderImpl builder = new BuilderImpl();
//		private final static Supplier<Client> defaultClientFactory = ()->ClientBuilder.newClient();

        private GeneratePDFServiceBuilder() {
            super();
        }

        @Override
        public RestServicesGeneratePDFServiceAdapter.GeneratePDFServiceBuilder machineName(String machineName) {
            builder.machineName(machineName);
            return this;
        }

        @Override
        public RestServicesGeneratePDFServiceAdapter.GeneratePDFServiceBuilder port(int port) {
            builder.port(port);
            return this;
        }

        @Override
        public RestServicesGeneratePDFServiceAdapter.GeneratePDFServiceBuilder useSsl(boolean useSsl) {
            builder.useSsl(useSsl);
            return this;
        }

        @Override
        public RestServicesGeneratePDFServiceAdapter.GeneratePDFServiceBuilder clientFactory(Supplier<Client> clientFactory) {
            builder.clientFactory(clientFactory);
            return this;
        }

        @Override
        public RestServicesGeneratePDFServiceAdapter.GeneratePDFServiceBuilder basicAuthentication(String username, String password) {
            builder.basicAuthentication(username, password);
            return this;
        }

        @Override
        public RestServicesGeneratePDFServiceAdapter.GeneratePDFServiceBuilder correlationId(Supplier<String> correlationIdFn) {
            builder.correlationId(correlationIdFn);
            return this;
        }

        @Override
        public Supplier<String> getCorrelationIdFn() {
            return builder.getCorrelationIdFn();
        }

        @Override
        public WebTarget createLocalTarget() {
            return builder.createLocalTarget();
        }

        public RestServicesGeneratePDFServiceAdapter build() {
            return new RestServicesGeneratePDFServiceAdapter(this.createLocalTarget(), this.getCorrelationIdFn());
        }
    }
}
