package com.my.demo.site.core.servlets;

import static org.apache.sling.api.servlets.ServletResolverConstants.SLING_SERVLET_METHODS;
import static org.apache.sling.api.servlets.ServletResolverConstants.SLING_SERVLET_PATHS;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.demo.site.core.services.ComponentFinderService;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;


/**
 * A Servlet that invokes an AEM Service to get the component usage count.
 */
@Component(service = Servlet.class, immediate = true, property = {
        SLING_SERVLET_PATHS+"="+"/mydemosite/componentFinder",
        SLING_SERVLET_METHODS+"="+ HttpConstants.METHOD_GET
})
public class ComponentFinderServlet extends SlingSafeMethodsServlet {


    /**
     * The ComponentFinderService.
     */
    @Reference
    ComponentFinderService componentFinderService;

    /**
     * Method to handle the get request for the servlet.
     *
     * @param request  - The request object.
     * @param response - The response object.
     */
    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {

        String rootPath ="";
        String resourceType ="";
        if(Objects.nonNull(request.getParameter("rootPath")) &&
                Objects.nonNull(request.getParameter("resourceType"))){
            rootPath = request.getParameter("rootPath");
            resourceType = request.getParameter("resourceType");
            HashMap<String,Integer> resultMap = componentFinderService.getComponentUsageCount(rootPath,resourceType);
            ObjectMapper objectMapper = new ObjectMapper();
            String resultString = objectMapper.writeValueAsString(resultMap);
            response.setContentLength(resultString.getBytes().length);
            response.setContentType("application/json");
            response.getOutputStream().write(resultString.getBytes());
        }
    }
}
