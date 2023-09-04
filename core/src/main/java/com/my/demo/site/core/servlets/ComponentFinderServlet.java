package com.my.demo.site.core.servlets;

import com.day.cq.wcm.api.NameConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.my.demo.site.core.services.ComponentFinderService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.apache.sling.api.servlets.ServletResolverConstants.SLING_SERVLET_METHODS;
import static org.apache.sling.api.servlets.ServletResolverConstants.SLING_SERVLET_PATHS;

@Component(service = Servlet.class, immediate = true, property = {
        SLING_SERVLET_PATHS+"="+"/bin/task/componentFinder",
        SLING_SERVLET_METHODS+"="+ HttpConstants.METHOD_GET
})
public class ComponentFinderServlet extends SlingSafeMethodsServlet {


    /**
     * The ComponentFinderService.
     */
    @Reference
    ComponentFinderService componentFinderService;


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
