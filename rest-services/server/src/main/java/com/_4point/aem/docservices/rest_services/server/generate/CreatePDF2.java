package com._4point.aem.docservices.rest_services.server.generate;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;

@Component(service=Servlet.class, property={Constants.SERVICE_DESCRIPTION + "=GeneratePDFService.CreatePDF2 Service",
        "sling.servlet.methods=" + HttpConstants.METHOD_POST})
@SlingServletPaths("/services/GeneratePDFService_CreatePDF2")

public class CreatePDF2 extends SlingAllMethodsServlet {

    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {
        // TODO Auto-generated method stub
        super.doPost(request, response);
    }
}
