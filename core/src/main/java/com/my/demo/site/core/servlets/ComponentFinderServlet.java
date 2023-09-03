package com.my.demo.site.core.servlets;

import com.day.cq.wcm.api.NameConstants;
import com.my.demo.site.core.services.ComponentFinderService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.apache.sling.api.servlets.ServletResolverConstants.SLING_SERVLET_METHODS;
import static org.apache.sling.api.servlets.ServletResolverConstants.SLING_SERVLET_PATHS;

@Component(service = Servlet.class, immediate = true, property = {
        SLING_SERVLET_PATHS+"="+"/bin/task/componentFinder",
        SLING_SERVLET_METHODS+"="+ HttpConstants.METHOD_GET
})
public class ComponentFinderServlet extends SlingAllMethodsServlet {

    /**
     * type=nt:unstructured
     * path=/content/we-retail/us/en
     * 1_property=sling:resourceType
     * 1_property.value=weretail/components/content/button
     * p.limit=-1
     */

    @Reference
    ComponentFinderService componentFinderService;


    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {


        Map<String,String> predicateMap = componentFinderService.createPredicateMap();
        response.getWriter().write("HELLO");

    }



}
